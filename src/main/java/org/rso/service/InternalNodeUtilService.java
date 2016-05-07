package org.rso.service;

import javaslang.control.Try;
import lombok.extern.java.Log;
import org.rso.dto.NodeStatusDto;
import org.rso.utils.AppProperty;
import org.rso.utils.DataTimeLogger;
import org.rso.utils.DateComperator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Log
@Service
public class InternalNodeUtilService {

    private static final String DEFAULT_NODES_PORT = "8080";

    @Value("${delay.election}")
    private long electionDelay;

    @Value("${log.tag.coordinator}")
    private String coordinatorTag;

    private static final String HEARTBEAT_URL = "http://{ip}:{port}/utils/heartbeat";
    private static final String ELECTION_URL = "http://{ip}:{port}/utils/election";

    /* TODO: remove singleton */
    private final AppProperty appProperty = AppProperty.getInstance();

    private final RestTemplate restTemplate = new RestTemplate();

    public void doHeartBeat() {
//        TODO nie ma obslugi bledow + zastanowic sie nad mniejszym timeoutem

        log.info(String.format("%s: Running heartbeat checks", coordinatorTag));

        /*
            TODO: Parallel calls to nodes
         */
        appProperty.getAvaiableNodesIpAddresses().forEach(nodeIpAddress -> {
            Try.run(() -> {
                final NodeStatusDto internalNodeStatusDto = restTemplate.getForObject(
                        HEARTBEAT_URL,
                        NodeStatusDto.class,
                        nodeIpAddress,
                        DEFAULT_NODES_PORT
                );
                log.info(String.format("%s: Heartbeat check of %s received: %s", coordinatorTag, nodeIpAddress, internalNodeStatusDto));
            }).onFailure(e -> log.info(String.format("Node %s stopped responding", nodeIpAddress)));
//            TODO nie ma wezla wiec trzeba go usunac z listy wezlow rozeslac ze go nie ma i zreplikowac dane
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
        log.info("Running election procedure");

        final int selfNodeId = appProperty.getSelfNode().getNodeId();
        final List<String> listOfIpAddresses = getAviableIPAddresses(appProperty, selfNodeId);

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
                    log.info(String.format("Exception during election procedure - host %s not found", ip));
                }

            }
        }
    }

    public void verifyCoordinatorPresence() {
        Date lastPresence = appProperty.getLastCoordinatorPresence();
        log.info("koordynator obecny byl ostatnio " + DataTimeLogger.logTime(lastPresence));
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

    }

    private List<String> getAviableIPAddresses(AppProperty appProperty, int selfNodeId) {
        return appProperty.getListOfAvaiableNodes().stream()
                .filter(p -> p.getNodeId() > selfNodeId).
                        map(n -> n.getNodeIPAddress()).
                        collect(toList());
    }

}
