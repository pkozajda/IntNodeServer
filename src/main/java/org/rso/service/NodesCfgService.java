package org.rso.service;

import org.rso.utils.NodeInfo;

import java.util.List;

public interface NodesCfgService {
    List<NodeInfo> getAllNodes();
    List<NodeInfo> getInternalNodes();
    NodeInfo getCoordinatorNode();
    NodeInfo getNodeById(final int nodeId);
    NodeInfo getSelfNode();
}
