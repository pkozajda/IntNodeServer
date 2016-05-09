package org.rso.service;

import org.rso.dto.NetworkStatusDto;
import org.rso.dto.NodeStatusDto;
import org.rso.utils.NetworkStatus;
import org.rso.utils.NodeInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class NodeNetworkServiceImpl implements NodeNetworkService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public NetworkStatus getNetworkStatusFromNode(final NodeInfo node) {
        return null;
    }
}
