package org.rso.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Radosław on 04.05.2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeInfo {
    private int nodeId;
    private String nodeIPAddress;
    private NodeType nodeType;
}
