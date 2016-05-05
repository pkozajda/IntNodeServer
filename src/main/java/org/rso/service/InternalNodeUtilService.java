package org.rso.service;

import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.rso.utils.AppProperty;
import org.rso.utils.NodeInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Radosław on 05.05.2016.
 */
@Service
@Log
public class InternalNodeUtilService {

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
            actualNode = restTemplate.getForObject(builder.toString(), NodeInfo.class);
            log.info("bicie serca odebral obiekt "+actualNode);
        }
    }
}
