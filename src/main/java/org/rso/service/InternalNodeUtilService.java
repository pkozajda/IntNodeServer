package org.rso.service;

import javaslang.control.Try;
import lombok.extern.java.Log;
import org.rso.dto.DtoConverters;
import org.rso.dto.NetworkStatusDto;
import org.rso.dto.NodeStatusDto;
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
import java.util.Optional;

import static java.util.stream.Collectors.toList;

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

//    @Resource
//    private NodeNetworkService nodeNetworkService;

    private static final String DEFAULT_NODES_PORT = "8080";
    private static final String HEARTBEAT_URL = "http://{ip}:{port}/utils/heartbeat";
    private static final String ELECTION_URL = "http://{ip}:{port}/utils/election";
    private static final String COORDINATOR_URL = "http://{ip}:{port}/utils/coordinator";
    private static final String STATUS_URL = "http://{ip}:{port}/utils/status";
    private static final String NODES_URL = "http://{ip}:{port}/coord/nodes";

    /* TODO: remove singleton */
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

        /*
            TODO: Parallel calls to nodes
         */

        appProperty.getAvailableNodes().forEach(nodeInfo -> {
            Try.run(() -> {
                final NodeStatusDto internalNodeStatusDto = restTemplate.getForObject(
                        HEARTBEAT_URL,
                        NodeStatusDto.class,
                        nodeInfo.getNodeIPAddress(),
                        DEFAULT_NODES_PORT
                );
                log.info(String.format("%s %s: Heartbeat check of %s received: %s", coordinatorTag, heartbeatTag, nodeInfo.getNodeIPAddress(), internalNodeStatusDto));
            }).onFailure(e -> {

                log.info(String.format("%s %s: Node %s stopped responding", coordinatorTag, heartbeatTag, nodeInfo.getNodeIPAddress()));
                /* TODO: Numer of retries before node is removed ??? */

                // TODO nie ma wezla wiec trzeba go:
                // 1. usunac z listy wezlow (+)
                // 2. rozeslac ze go nie ma (+)
                // 3. zreplikowac dane
                appProperty.removeUnAvaiableNode(nodeInfo.getNodeId());

            });
        });

        /* TODO:
                Do not send any updates if nothing changed!
         */
        /* Inform all the remaining nodes about changes in network */
        final NetworkStatusDto updatedNetworkStatusDto = NetworkStatusDto.builder()
                .coordinator(DtoConverters.nodeInfoToNodeStatusDto.apply(appProperty.getCoordinatorNode()))
                .nodes(appProperty.getAvailableNodes().stream().map(DtoConverters.nodeInfoToNodeStatusDto).collect(toList()))
                .build();

        /* TODO: parallel calls to nodes */
        appProperty.getAvailableNodes().forEach(nodeInfo -> {
            Try.run(() -> {
                final ResponseEntity<Void> networkStatusUpdateEntity = restTemplate.postForEntity(
                        STATUS_URL,
                        updatedNetworkStatusDto,
                        Void.class,
                        nodeInfo.getNodeIPAddress(),
                        DEFAULT_NODES_PORT
                );

                if(networkStatusUpdateEntity.getStatusCode() != HttpStatus.NO_CONTENT) {
                    /* TODO: This might create some integrity issues. Consider what to do here... */
                    throw new RuntimeException("Unable to update network status in node: " + nodeInfo.getNodeId());
                }

            }).onFailure(e -> {
                /* A node suddenly stopped responding; we don't need to do anything here though
                   since it will be removed during the next Heartbeat check iteration anyway.
                 */
                log.info(String.format("%s %s: Node %s stopped responding during network status update. It should be removed in the next Heartbeat check",
                        coordinatorTag, heartbeatTag, nodeInfo.getNodeIPAddress()));
            });
        });


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
                log.info(String.format("Registration successful: %s", registrationEntity.getHeaders().getLocation().toASCIIString()));
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
        long dif = DateComperator.compareDate(lastPresence,new Date());

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
