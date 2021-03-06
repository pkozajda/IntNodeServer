package org.rso.controllers;

import lombok.extern.java.Log;
import org.rso.dto.DtoConverters;
import org.rso.dto.NodeStatusDto;
import org.rso.utils.AppProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Log
@RestController
@RequestMapping("utils")
public class HeartbeatController {

    @Value("${log.tag.heartbeat}")
    private String heartbeatTag;

    private final AppProperty appProperty = AppProperty.getInstance();

    @RequestMapping(value = "/heartbeat")
    public NodeStatusDto heartbeatProtocol(final HttpServletRequest httpServletRequest){

        log.info(String.format("%s: Received heartbeat request from: %s", heartbeatTag, httpServletRequest.getRemoteAddr()));

        appProperty.setLastCoordinatorPresence(new Date());

        return DtoConverters.nodeInfoToNodeStatusDto.apply(appProperty.getSelfNode());
    }
}
