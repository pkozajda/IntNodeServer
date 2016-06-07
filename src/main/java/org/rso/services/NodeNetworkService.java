package org.rso.services;


import javaslang.control.Try;
import org.rso.dto.NetworkStatusDto;
import org.rso.utils.NodeInfo;

public interface NodeNetworkService {
    Try<NetworkStatusDto> getNetworkStatus(final NodeInfo node);
}
