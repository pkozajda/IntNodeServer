package org.rso.controllers;

import lombok.extern.java.Log;
import org.rso.dto.DtoConverters;
import org.rso.dto.NodeStatusDto;
import org.rso.exceptions.NodeNotFoundException;
import org.rso.service.NodeUtilService;
import org.rso.utils.AppProperty;
import org.rso.utils.NodeInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log
@RestController
@RequestMapping("coord")
public class CoordinatorController {

    @Resource(name = "internalNodeUtilService")
    private NodeUtilService nodeUtilService;

    private final AppProperty appProperty = AppProperty.getInstance();


    @RequestMapping(value = "/nodes", method = RequestMethod.GET)
    public List<NodeStatusDto> getAllNodes() {
        return appProperty.getAvailableNodes().stream()
                .map(DtoConverters.nodeInfoToNodeStatusDto)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/nodes/{nodeId}", method = RequestMethod.GET)
    public ResponseEntity<NodeStatusDto> getNodeById(@PathVariable(value="nodeId") final int nodeId) {
        final NodeStatusDto nodeStatusDto = Optional.ofNullable(appProperty.getNodeById(nodeId))
                .map(DtoConverters.nodeInfoToNodeStatusDto)
                .orElseThrow(() -> new NodeNotFoundException(String.format("Node with id = %s does not exist", nodeId)));

        return ResponseEntity.ok(nodeStatusDto);
    }


    @RequestMapping(value = "/nodes", method = RequestMethod.POST)
    public ResponseEntity<Void> registerNode(final HttpServletRequest httpServletRequest) {
        // allocate new ID, data structures for new node
        final NodeInfo allocatedNodeInfo = appProperty.allocateNewNode();

        final NodeInfo createdNodeInfo = NodeInfo.builder()
                .nodeId(allocatedNodeInfo.getNodeId())
                .nodeIPAddress(httpServletRequest.getRemoteAddr())
                .nodeType(allocatedNodeInfo.getNodeType())
                .build();

        // add entry into appProperty

        appProperty.addAvaiableNode(createdNodeInfo);

        // TODO: inform other nodes about network update

        // TODO: do not block for network updates (this can take a looong time)

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdNodeInfo.getNodeId())
                .toUri())
                .build();
    }
}
