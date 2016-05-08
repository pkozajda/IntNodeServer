package org.rso.controllers;

import lombok.extern.java.Log;
import org.rso.dto.DtoConverters;
import org.rso.dto.NetworkStatusDto;
import org.rso.utils.AppProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.toList;

@Log
@RestController
@RequestMapping("utils")
public class StatusController {
    private final AppProperty appProperty = AppProperty.getInstance();

    @RequestMapping(value = "/status")
    public NetworkStatusDto getNetworkStatus() {
        return NetworkStatusDto.builder()
                .coordinator(DtoConverters.nodeInfoToNodeStatusDto.apply(appProperty.getCoordinatorNode()))
                .nodes(appProperty.getAvailableNodes().stream().map(DtoConverters.nodeInfoToNodeStatusDto).collect(toList()))
                .build();
    }
}
