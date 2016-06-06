package org.rso.utils;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class NodeInfo {

    public static final String NODE_ID = "id";
    public static final String NODE_ADDRESS = "ip";
    public static final String NODE_TYPE = "type";

    private  int nodeId;
    private  String nodeIPAddress;
    private  NodeType nodeType;
}
