package org.rso.dto;

import org.rso.utils.NodeInfo;

import java.util.function.Function;

public class DtoConverters {

    public static Function<NodeInfo, NodeStatusDto> nodeIntoToNodeStatusDto = nodeInfo ->
            NodeStatusDto.builder()
                    .nodeId(nodeInfo.getNodeId())
                    .nodeIPAddress(nodeInfo.getNodeIPAddress())
                    .nodeType(nodeInfo.getNodeType().name())
                    .build();
}
