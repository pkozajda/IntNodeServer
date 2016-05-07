package org.rso.controllers;

import lombok.extern.java.Log;
import org.rso.dto.DtoConverters;
import org.rso.dto.NodeStatusDto;
import org.rso.service.InternalNodeUtilService;
import org.rso.utils.AppProperty;
import org.rso.utils.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Log
@RestController
@RequestMapping("utils")
public class UtilController {

    @Autowired
    private InternalNodeUtilService utilService;

    @RequestMapping(value = "/heartbeat")
    public NodeStatusDto heartbeatProtocol(final HttpServletRequest httpServletRequest){
        log.info("Received heartbeat signal from: " + httpServletRequest.getRemoteAddr());

        /* TODO:
            1. Integrity checks to ensure we got a heartbeat from current coordinator
            2. Yoda time
            3. Remove ugly singleton
         */

        AppProperty appProperty = AppProperty.getInstance();
        appProperty.setLastCoordinatorPresence(new Date());

        return DtoConverters.nodeIntoToNodeStatusDto.apply(appProperty.getSelfNode());
    }

    @RequestMapping(value = "/election",method = RequestMethod.POST)
    public NodeInfo electionAction(@RequestBody NodeInfo node){
        log.info("przeslano rzadanie elekcji od wezla " + node.toString());
        AppProperty appProperty = AppProperty.getInstance();
        NodeInfo selfNode = appProperty.getSelfNode();
        int selfNodeId = selfNode.getNodeId();
        if(selfNodeId>node.getNodeId()){
            utilService.doElection();
        }
        return selfNode;
    }

}
