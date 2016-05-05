package org.rso.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeInfo {
    private int nodeId;
    private String nodeIPAddress;
    private NodeType nodeType;
}
