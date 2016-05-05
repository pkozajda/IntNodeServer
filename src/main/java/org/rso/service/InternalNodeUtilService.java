package org.rso.service;

import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.rso.utils.AppProperty;
import org.rso.utils.DataTimeLogger;
import org.rso.utils.DateComperator;
import org.rso.utils.NodeInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
@Log
public class InternalNodeUtilService {

    private static final long TIME_TO_RUN_ELLECTION = 15000;

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
    }

    public void doHeartBeat() {
//        TODO nie ma obslugi bledow + zastanowic sie nad mniejszym timeoutem
        log.info("przeprowadzamy procedure bicia serca ");
        RestTemplate restTemplate = new RestTemplate();
        AppProperty appProperty = AppProperty.getInstance();
        NodeInfo actualNode;
        for(String ip:appProperty.getAvaiableNodesIpAddresses()){
            StringBuilder builder = new StringBuilder("http://");
            builder.append(ip);
            builder.append(":8080/utils/heartbeat");
            try {
                actualNode = restTemplate.getForObject(builder.toString(), NodeInfo.class);
                log.info("bicie serca odebral obiekt "+actualNode);
            }catch (Exception e){
                log.info("dupa");
//                TODO nie ma wezla wiec trzeba go usunac z listy wezlow rozeslac ze go nie ma i zreplikowac dane
            }
        }
    }

    public void verifyCoordinatorPresence() {
        Date lastPresence = AppProperty.getInstance().getLastCoordinatorPresence();
        log.info("koordynator obecny byl ostatnio " + DataTimeLogger.logTime(lastPresence));
        long dif = DateComperator.compareDate(lastPresence,new Date());
        if(dif>TIME_TO_RUN_ELLECTION){
            doElection();
        }
    }


}
