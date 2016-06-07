package org.rso.services;


import org.rso.utils.NetworkStatus;
import org.rso.utils.NodeInfo;

public interface NodeNetworkService {
    NetworkStatus getNetworkStatusFromNode(final NodeInfo node);
}
