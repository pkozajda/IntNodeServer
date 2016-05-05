package org.rso.controllers;

import lombok.extern.java.Log;
import org.rso.service.InternalNodeUtilService;
import org.rso.utils.AppProperty;
import org.rso.utils.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by Radosław on 05.05.2016.
 */
@RestController
@RequestMapping("utils")
@Log
public class UtilController {

    @Autowired
    private InternalNodeUtilService utilService;


    @RequestMapping(value = "/heartbeat")
    public NodeInfo heartbeatProtocol(){
        log.info("kontroler odebral bicie serca ");
        AppProperty appProperty = AppProperty.getInstance();
        appProperty.setLastCoordinatorPresence(new Date());
        return appProperty.getSelfNode();
    }

    @RequestMapping(value = "/election",method = RequestMethod.POST)
    public NodeInfo electionAction(@RequestBody NodeInfo node){
        AppProperty appProperty = AppProperty.getInstance();
        NodeInfo selfNode = appProperty.getSelfNode();
        int selfNodeId = selfNode.getNodeId();
        if(selfNodeId>node.getNodeId()){
            utilService.doElection();
        }
        return selfNode;
    }

}
