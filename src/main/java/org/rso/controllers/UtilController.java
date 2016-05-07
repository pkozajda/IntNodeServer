package org.rso.controllers;

import lombok.extern.java.Log;
import org.rso.dto.DtoConverters;
import org.rso.dto.NodeStatusDto;
import org.rso.service.InternalNodeUtilService;
import org.rso.utils.AppProperty;
import org.rso.utils.NodeInfo;
import org.rso.utils.NodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /* TODO:
            1. Integrity checks to ensure we got a heartbeat from current coordinator
            2. Yoda time
            3. Remove ugly singleton
    */

    @Autowired
    private InternalNodeUtilService utilService;

    @Value("${log.tag.election}")
    private String electionTag;

    @Value("${log.tag.heartbeat}")
    private String heartbeatTag;

    private final AppProperty appProperty = AppProperty.getInstance();

    @RequestMapping(value = "/heartbeat")
    public NodeStatusDto heartbeatProtocol(final HttpServletRequest httpServletRequest){

        log.info(String.format("%s: Received heartbeat request from: %s", heartbeatTag, httpServletRequest.getRemoteAddr()));

        appProperty.setLastCoordinatorPresence(new Date());

        return DtoConverters.nodeInfoToNodeStatusDto.apply(appProperty.getSelfNode());
    }

    @RequestMapping(value = "/election", method = RequestMethod.POST)
    public NodeStatusDto electionAction(@RequestBody final NodeStatusDto node) {

        log.info(String.format("%s: Received election request from: %s", electionTag, node.toString()));

        final NodeInfo selfNode = appProperty.getSelfNode();
        final int selfNodeId = selfNode.getNodeId();

        if(selfNodeId > node.getNodeId()) {
            utilService.doElection();
        }

        return DtoConverters.nodeInfoToNodeStatusDto.apply(selfNode);
    }

    @RequestMapping(value = "/coordinator", method = RequestMethod.GET)
    public NodeStatusDto getCurrentCoordinator() {
        return DtoConverters.nodeInfoToNodeStatusDto.apply(appProperty.getCoordinatorNode());
    }

    @RequestMapping(value = "/coordinator", method = RequestMethod.PUT)
    public ResponseEntity<Void> setNewCoordinator(@RequestBody final NodeStatusDto coordinator) {

        /* TODO: safety checks of input coordinator DTO */

        if(coordinator.getNodeId() == appProperty.getCoordinatorNode().getNodeId()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        final NodeInfo coordinatorNode = appProperty.getNodeById(coordinator.getNodeId());


        final NodeInfo newCoordinatorNode = NodeInfo.builder()
                .nodeId(coordinator.getNodeId())
                .nodeIPAddress(coordinator.getNodeIPAddress())
                .nodeType(NodeType.INTERNAL_COORDINATOR)
                .build();

        log.info(String.format("%s: Setting new coordinator: %s", electionTag, newCoordinatorNode));

        appProperty.removeUnAvaiableNode(coordinator.getNodeId());

        appProperty.addAvaiableNode(newCoordinatorNode);
        appProperty.setCoordinatorNode(newCoordinatorNode);

//        if(appProperty.getSelfNode().getNodeId() == newCoordinatorNode.getNodeId()) {
//            appProperty.setSelfNode(newCoordinatorNode);
//        }

        return ResponseEntity.noContent().build();
    }
}
