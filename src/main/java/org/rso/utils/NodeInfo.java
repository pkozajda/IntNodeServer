package org.rso.utils;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class NodeInfo {

    public static final String NODE_ID = "id";
    public static final String NODE_ADDRESS = "ip";
    public static final String NODE_TYPE = "type";

    private final int nodeId;
    private final String nodeIPAddress;
    private final NodeType nodeType;
}
