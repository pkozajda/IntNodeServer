package org.rso.service;


import org.rso.utils.NetworkStatus;
import org.rso.utils.NodeInfo;

public interface NodeNetworkService {
    NetworkStatus getNetworkStatusFromNode(final NodeInfo node);
}
