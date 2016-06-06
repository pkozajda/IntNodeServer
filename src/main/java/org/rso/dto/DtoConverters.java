package org.rso.dto;

import org.rso.utils.NodeInfo;
import org.rso.utils.NodeType;

import java.util.function.Function;

public class DtoConverters {

    public static Function<NodeInfo, NodeStatusDto> nodeInfoToNodeStatusDto = nodeInfo ->
            NodeStatusDto.builder()
                    .nodeId(nodeInfo.getNodeId())
                    .nodeIPAddress(nodeInfo.getNodeIPAddress())
                    .nodeType(nodeInfo.getNodeType().name())
                    .locations(nodeInfo.getLocations())
                    .build();

    public static Function<NodeStatusDto, NodeInfo> nodeStatusDtoToNodeInfo  = nodeStatusDto ->
            NodeInfo.builder()
                    .nodeId(nodeStatusDto.getNodeId())
                    .nodeIPAddress(nodeStatusDto.getNodeIPAddress())
                    .nodeType(NodeType.valueOf(nodeStatusDto.getNodeType()))
                    .locations(nodeStatusDto.getLocations())
                    .build();

}
