package org.rso.services;

import javaslang.control.Try;
import lombok.extern.java.Log;
import org.rso.dto.NetworkStatusDto;
import org.rso.utils.NodeInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Log
@Service
public class NodeNetworkServiceImpl implements NodeNetworkService {

    @Value("${timeout.request.read}")
    private int readTimeout;

    @Value("${timeout.request.connect}")
    private int connectionTimeout;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String STATUS_URL = "http://{ip}:{port}/utils/status";
    private static final String DEFAULT_NODES_PORT = "8080";

    @PostConstruct
    public void initialize() {
        ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory()).setReadTimeout(readTimeout);
        ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory()).setConnectTimeout(connectionTimeout);
    }

    @Override
    public Try<NetworkStatusDto> getNetworkStatus(final NodeInfo nodeInfo) {
        return Try.of(() -> {

            final ResponseEntity<NetworkStatusDto> networkStatusEntity = restTemplate.getForEntity(
                    STATUS_URL,
                    NetworkStatusDto.class,
                    nodeInfo.getNodeIPAddress(),
                    DEFAULT_NODES_PORT
            );

            if(networkStatusEntity.getStatusCode() != HttpStatus.OK) {

                final String errorMessage = String.format("Unable to get network status from node: %d [%s]", nodeInfo.getNodeId(), nodeInfo.getNodeIPAddress());

                log.info(errorMessage);
                throw new RuntimeException(errorMessage);
            }

            return networkStatusEntity.getBody();
        });
    }
}
