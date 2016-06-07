package org.rso.network.services;

import javaslang.control.Try;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.rso.dto.DtoConverters;
import org.rso.network.dto.NetworkStatusDto;
import org.rso.network.dto.NodeStatusDto;
import org.rso.replication.ReplicationService;
import org.rso.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/*
    TODO:
     1. Parallel calls to nodes
     2. # of retries before a node is considered inactive and therefore removed
 */
@Log
@Service("internalNodeUtilService")
public class InternalNodeUtilService implements NodeUtilService {

    @Value("${delay.election}")
    private long electionDelay;

    @Value("${log.tag.coordinator}")
    private String coordinatorTag;

    @Value("${log.tag.election}")
    private String electionTag;

    @Value("${log.tag.heartbeat}")
    private String heartbeatTag;

    @Value("${timeout.request.read}")
    private int readTimeout;

    @Value("${timeout.request.connect}")
    private int connectionTimeout;

    @Value("${replication.redundancy}")
    private int replicationRedundancy;

    @Resource
    private NodeNetworkService nodeNetworkService;

    @Resource
    private ReplicationService replicationService;

    private static final String DEFAULT_NODES_PORT = "8080";
    private static final String ELECTION_URL = "http://{ip}:{port}/utils/election";
    private static final String COORDINATOR_URL = "http://{ip}:{port}/utils/coordinator";
    private static final String STATUS_URL = "http://{ip}:{port}/utils/status";
    private static final String NODES_URL = "http://{ip}:{port}/coord/nodes";

    private final AppProperty appProperty = AppProperty.getInstance();

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void initialize() {
        ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory()).setReadTimeout(readTimeout);
        ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory()).setConnectTimeout(connectionTimeout);
    }

    public void doHeartBeat() {

        if(appProperty.getAvailableNodes().isEmpty()) {
            return;
        }

        log.info(String.format("%s %s: Running heartbeat checks (%s nodes)", coordinatorTag, heartbeatTag, appProperty.getAvailableNodes().size()));

        appProperty.getAvailableNodes().forEach(nodeInfo ->
                nodeNetworkService.getHeartbeat(nodeInfo)
                    .onSuccess(nodeStatusDto ->
                        log.info(
                                String.format("%s %s: Heartbeat check of %s received: %s",
                                        coordinatorTag, heartbeatTag,
                                        nodeInfo.getNodeIPAddress(), nodeStatusDto)
                        )
                    )
                    .onFailure(e -> {
                        log.info(
                                String.format("%s %s: Node %s stopped responding",
                                        coordinatorTag, heartbeatTag, nodeInfo.getNodeIPAddress())
                        );

                        /* Node is not responding. We need to: */

                        /* 1. remove it from a list of available nodes */
                        appProperty.removeUnAvaiableNode(nodeInfo.getNodeId());

                        /* 2. Replicate data placed on that node */
                        final List<Location> locationsStoredOnNode = nodeInfo.getLocations();

                        if(locationsStoredOnNode.isEmpty()) {
                            log.info(
                                    String.format("%s %s: Node %s did not store any data! There is no need for any replication!",
                                            coordinatorTag, heartbeatTag, nodeInfo.getNodeIPAddress())
                            );
                        } else {
                            log.info(
                                    String.format("%s %s: Node %s stored the following data: %s. The need for data replication is real :(",
                                            coordinatorTag, heartbeatTag, nodeInfo.getNodeIPAddress(), nodeInfo.getLocations())
                            );


                            final NetworkStatus currentNetworkStatus = appProperty.getNetworkStatus();

                            if(currentNetworkStatus.getNodes().isEmpty()) {
                                log.info(
                                        String.format("%s %s: Network does not contain any nodes. Aborting replication...", coordinatorTag, heartbeatTag)
                                );
                            } else {
                                replicateLocations(nodeInfo.getLocations());
                            }


                        }


                        /* 3. Inform all the remaining nodes about changes in network */
                        final NetworkStatusDto updatedNetworkStatusDto = NetworkStatusDto.builder()
                                .coordinator(DtoConverters.nodeInfoToNodeStatusDto.apply(appProperty.getCoordinatorNode()))
                                .nodes(appProperty.getAvailableNodes().stream().map(DtoConverters.nodeInfoToNodeStatusDto).collect(toList()))
                                .build();

                        appProperty.getAvailableNodes().forEach(availableNodeInfo ->
                                nodeNetworkService.setNetworkStatus(availableNodeInfo, updatedNetworkStatusDto)
                                        .onFailure(ez ->
                                                /* A node suddenly stopped responding; we don't need to do anything here though
                                                   since it will be removed during the next Heartbeat check iteration anyway.
                                                 */
                                                log.info(String.format("%s %s: Node %s stopped responding during network status update. It should be removed in the next Heartbeat check",
                                                        coordinatorTag, heartbeatTag, availableNodeInfo.getNodeIPAddress()))
                                        )
                        );


                    })
        );
    }

    // replication algorithm
    private Try<Void> replicateLocations(@NonNull final List<Location> locations) {

        final Map<Location, List<NodeInfo>> replicationMap = appProperty.getReplicationMap();

        locations.forEach(locationToReplicate -> {
            final List<NodeInfo> nodesWithLocation = Optional.ofNullable(replicationMap.get(locationToReplicate))
                        .orElseThrow(() -> new RuntimeException("No node with location: " + locationToReplicate + " found"));

            if(nodesWithLocation.size() >= replicationRedundancy) {
                log.info(
                        String.format("Location %s is already duplicated %d times. Skipping this one...",
                                locationToReplicate, nodesWithLocation.size())
                );
            } else if(nodesWithLocation.size() == 0 || nodesWithLocation.isEmpty()){
                log.warning(
                        String.format("Location %s is not stored on any other node! It has been lost for eternity...",
                                locationToReplicate)
                );
            } else {
                // pick a random/first node with the location

                final NodeInfo replicationSource = pickReplicationSource(nodesWithLocation);
                final NodeInfo replicationDest = pickReplicationDest(locationToReplicate, replicationMap);

                log.info(
                        String.format("Performing replication of location: %s from source: %s to destination: %s",
                                locationToReplicate, replicationSource.getNodeIPAddress(), replicationDest.getNodeIPAddress())
                );

                replicationService.replicateLocation(locationToReplicate, replicationSource, replicationDest)
                        .onSuccess((e) -> log.info("Successfully replicated! :)"))
                        .onFailure(Throwable::printStackTrace);

            }
        });


        return Try.success(null);
    }

    private NodeInfo pickReplicationDest(Location locationToReplicate, Map<Location, List<NodeInfo>> replicationMap) {
        return replicationMap.entrySet().stream()
                .filter(locationListEntry -> !locationListEntry.getKey().equals(locationToReplicate))
                .map(Map.Entry::getValue)
                .findAny()
                .orElseThrow(() -> new RuntimeException("Cannot return node for replication destination"))
                .get(0);
    }

    // simplest algorithm of picking nodes...
    private NodeInfo pickReplicationSource(@NonNull final List<NodeInfo> nodesWithLocation) {
        return nodesWithLocation.get(0);
    }


    /*
    * 1-pobrac wszystkie wezly o wiekszym identyfikatorze
    * 2-nawiazac kontakt z kazdym
    *       a - jezeli odpowie przerwij proces elekcji
    *       b - jezeli nie odpowie jestes koordnatorem
    *               -poinformuj wszystkie wezly o tym fakcjie
    *               -zmien swoje glowne ustawienia
    *               */
    public void doElection(){
        log.info(String.format("%s: Running election procedure", electionTag));

        final int selfNodeId = appProperty.getSelfNode().getNodeId();
        final List<String> listOfIpAddresses = getAvailableIPAddresses(appProperty.getAvailableNodes(), selfNodeId);

        if(listOfIpAddresses.isEmpty()) {
//            koniec elekcji jestem nowym koorynatorem
            comunicateAsNewCoordinator();
        } else {
//            proces elekcji dla innych wezlow

            for(String ip: listOfIpAddresses){

                try {

                    final NodeStatusDto info = restTemplate.postForObject(
                            ELECTION_URL,
                            appProperty.getSelfNode(),
                            NodeStatusDto.class,
                            ip,
                            DEFAULT_NODES_PORT
                    );
                    log.info("info "+ info);
                    if(info.getNodeId()>selfNodeId){
                        return;
                    }
                }catch (Exception e){
                    log.info(String.format("%s: Exception during election procedure - host %s not found", electionTag, ip));
                }

            }
        }
    }

    /* TODO: Refactor */
    @Override
    public void connectToNetwork(final String nodeIpAddress) {
        assert !StringUtils.isEmpty(nodeIpAddress);

        log.info(String.format("Trying to connect to network using node %s configuration", nodeIpAddress));

        /* Get network configuration from node */
        Try.run(() -> {
            final NetworkStatusDto networkStatusDto = restTemplate.getForObject(
                    STATUS_URL,
                    NetworkStatusDto.class,
                    nodeIpAddress,
                    DEFAULT_NODES_PORT
            );

            final NodeStatusDto coordinatorStatusDto = Optional.ofNullable(networkStatusDto.getCoordinator())
                    .orElseThrow(() -> new RuntimeException(String.format("Cannot retrieve network coordinator details from %s", nodeIpAddress)));

            log.info(String.format("Retrieved network configuration from %s.\nContacting network coordinator %s (id: %s)",
                    nodeIpAddress, coordinatorStatusDto.getNodeIPAddress(), coordinatorStatusDto.getNodeId()));

            /* Contact coordinator to get registered into network */

            final ResponseEntity<Void> registrationEntity = restTemplate.postForEntity(
                    NODES_URL,
                    null,
                    Void.class,
                    coordinatorStatusDto.getNodeIPAddress(),
                    DEFAULT_NODES_PORT
            );

            if(registrationEntity.getStatusCode() == HttpStatus.CREATED) {

                final String createdNodeLocation = registrationEntity.getHeaders().getLocation().toASCIIString();

                log.info(String.format("Registration successful: %s", createdNodeLocation));

                final NodeStatusDto createdNodeDto = restTemplate.getForObject(
                        createdNodeLocation,
                        NodeStatusDto.class
                );

                final NetworkStatusDto updatedNetworkStatusDto = restTemplate.getForObject(
                        STATUS_URL,
                        NetworkStatusDto.class,
                        nodeIpAddress,
                        DEFAULT_NODES_PORT
                );

                appProperty.setSelfNode(DtoConverters.nodeStatusDtoToNodeInfo.apply(createdNodeDto));
                appProperty.setCoordinatorNode(DtoConverters.nodeStatusDtoToNodeInfo.apply(updatedNetworkStatusDto.getCoordinator()));
                appProperty.setListOfAvaiableNodes(
                        updatedNetworkStatusDto.getNodes().stream()
                                .filter(nodeStatusDto -> nodeStatusDto.getNodeId() != createdNodeDto.getNodeId())
                                .map(DtoConverters.nodeStatusDtoToNodeInfo)
                                .collect(Collectors.toList())
                );



            } else {
                log.info("Could not register new node :(");
                throw new RuntimeException("Could not register new node :(");
            }

        }).onFailure((e) -> { throw new RuntimeException(e.getMessage()); });
    }

    /* TODO: Refactor using Yoda Time */
    public void verifyCoordinatorPresence() {
        Date lastPresence = appProperty.getLastCoordinatorPresence();
        final NodeInfo currentCoordinator = appProperty.getCoordinatorNode();
//        log.info("koordynator obecny byl ostatnio " + DataTimeLogger.logTime(lastPresence));
        log.info(String.format("Coordinator (id = %s, IP = %s) last seen: %s",
                currentCoordinator.getNodeId(), currentCoordinator.getNodeIPAddress(), DataTimeLogger.logTime(lastPresence)));
        long dif = DateComparator.compareDate(lastPresence,new Date());

        if(dif > electionDelay){
            doElection();
        }
    }

    /*
    * powiadom wszytskich ze jestes nowym koordynatorem
    * to automatycznie usuwa koordynatorow jako dostepne serwery z listy AppProperty
    * Rozpocznij proces replikacji danych ktore byly dostepne na koordynatorze - chyba najtrudniejsze jak narazie*/
    private void comunicateAsNewCoordinator() {

        final NodeInfo currentSelfNode = appProperty.getSelfNode();

        final NodeInfo newCoordinatorNode = NodeInfo.builder()
                .nodeId(currentSelfNode.getNodeId())
                .nodeIPAddress(currentSelfNode.getNodeIPAddress())
                .nodeType(NodeType.INTERNAL_COORDINATOR)
                .build();

        appProperty.removeUnAvaiableNode(currentSelfNode.getNodeId());

        appProperty.setCoordinatorNode(newCoordinatorNode);
        appProperty.setSelfNode(newCoordinatorNode);

        log.info(String.format("%s: I am the new coordinator! %s", coordinatorTag, appProperty.getSelfNode()));

        final HttpEntity<NodeStatusDto> selfNodeStatusDtoHttpEntity =
                new HttpEntity<>(DtoConverters.nodeInfoToNodeStatusDto.apply(appProperty.getSelfNode()));

        /* TODO: parallel calls to nodes */
        appProperty.getAvaiableNodesIpAddresses().forEach(nodeIpAddress -> {
            Try.run(() -> {
                final ResponseEntity<Void> newCoordinatorResponseEntity = restTemplate.exchange(
                        COORDINATOR_URL,
                        HttpMethod.PUT,
                        selfNodeStatusDtoHttpEntity,
                        Void.class,
                        nodeIpAddress,
                        DEFAULT_NODES_PORT
                );

                log.info(String.format("%s %s: Elected coordinator update returned status: %s", coordinatorTag, electionTag, newCoordinatorResponseEntity.getStatusCode()));
            }).onFailure(e -> log.info(String.format("%s %s: Node %s stopped responding? %s", coordinatorTag, heartbeatTag, nodeIpAddress, e.getMessage())));
        });

        log.info(String.format("%s %s: Election process completed!", coordinatorTag, electionTag));
    }

    private List<String> getAvailableIPAddresses(final List<NodeInfo> availableNodes, final int selfNodeId) {
        return availableNodes.stream()
                .filter(node -> node.getNodeId() > selfNodeId)
                .map(NodeInfo::getNodeIPAddress)
                .collect(toList());
    }
}
