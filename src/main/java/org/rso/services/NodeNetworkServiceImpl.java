package org.rso.services;

import org.rso.utils.NetworkStatus;
import org.rso.utils.NodeInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NodeNetworkServiceImpl implements NodeNetworkService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public NetworkStatus getNetworkStatusFromNode(final NodeInfo node) {
        return null;
    }
}
