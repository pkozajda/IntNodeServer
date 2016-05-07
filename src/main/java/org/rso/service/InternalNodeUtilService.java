package org.rso.service;

import javaslang.control.Try;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.rso.dto.NodeStatusDto;
import org.rso.utils.AppProperty;
import org.rso.utils.DataTimeLogger;
import org.rso.utils.DateComperator;
import org.rso.utils.NodeInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log
@Service
public class InternalNodeUtilService {

    private static final String DEFAULT_NODES_PORT = "8080";

    @Value("${delay.election}")
    private long electionDelay;

    @Value("${log.tag.coordinator}")
    private String coordinatorTag;


    private static final String HEARTBEAT_URL = "http://{ip}:{port}/utils/heartbeat";

    private final AppProperty appProperty = AppProperty.getInstance();


    /*
    * 1-pobrac wszystkie wezly o wiekszym identyfikatorze
    * 2-nawiazac kontakt z kazdym
    *       a - jezeli odpowie przerwij proces elekcji
    *       b - jezeli nie odpowie jestes koordnatorem
    *               -poinformuj wszystkie wezly o tym fakcjie
    *               -zmien swoje glowne ustawienia
    *               */
    public void doElection(){
        log.info("przeprowadzamy procedure elekcji ");
        AppProperty appProperty = AppProperty.getInstance();
        int selfNodeId = appProperty.getSelfNode().getNodeId();
        List<String> listOfIpAddresses = getAviableIPAddresses(appProperty, selfNodeId);
        if(listOfIpAddresses.isEmpty()){
//            koniec elekcji jestem nowym koorynatorem
            comunicateAsNewCoordinator();
        }else {
//            proces elekcji dla innych wezlow
            RestTemplate restTemplate = new RestTemplate();
            for(String ip: listOfIpAddresses){
                StringBuilder builder = new StringBuilder("http://");
                builder.append(ip);
                builder.append(":8080/utils/election");
                try {
                    NodeInfo info = restTemplate.postForObject(builder.toString(),appProperty.getSelfNode(),NodeInfo.class);
                    log.info("info "+ info);
                    if(info.getNodeId()>selfNodeId){
                        return;
                    }
                }catch (Exception e){
                    log.info("wyjatek przy robieniu elekcji nie znaleziono hosta ");
                }

            }
        }
    }

    /*
    * powiadom wszytskich ze jestes nowym koordynatorem
    * to automatycznie usuwa koordynatorow jako dostepne serwery z listy AppProperty
    * Rozpocznij proces replikacji danych ktore byly dostepne na koordynatorze - chyba najtrudniejsze jak narazie*/
    private void comunicateAsNewCoordinator() {

    }

    private ArrayList<String> getAviableIPAddresses(AppProperty appProperty, int selfNodeId) {
        return appProperty.getListOfAvaiableNodes().stream()
                                        .filter(p -> p.getNodeId() > selfNodeId).
                                        map(n -> n.getNodeIPAddress()).
                                        collect(Collectors.toCollection(ArrayList::new));
    }

    public void doHeartBeat() {
//        TODO nie ma obslugi bledow + zastanowic sie nad mniejszym timeoutem
        log.info(String.format("%s: Running heartbeat checks", coordinatorTag));
        RestTemplate restTemplate = new RestTemplate();

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
                log.info("bicie serca odebral obiekt " + internalNodeStatusDto);
            }).onFailure(e -> log.info(String.format("Node %s stoped responding", nodeIpAddress)));
//            TODO nie ma wezla wiec trzeba go usunac z listy wezlow rozeslac ze go nie ma i zreplikowac dane
        });
    }

    public void verifyCoordinatorPresence() {
        Date lastPresence = AppProperty.getInstance().getLastCoordinatorPresence();
        log.info("koordynator obecny byl ostatnio " + DataTimeLogger.logTime(lastPresence));
        long dif = DateComperator.compareDate(lastPresence,new Date());

        if(dif > electionDelay){
            doElection();
        }
    }


}
