package org.rso.mongo.service;

import lombok.NonNull;
import lombok.extern.java.Log;
import org.rso.config.LocationMap;
import org.rso.dto.UniversityDto;
import org.rso.mongo.repo.UniversityRepo;
import org.rso.mongo.utils.Converter;
import org.rso.utils.Location;
import org.rso.utils.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Log
@Service
public class ReplicationService {

    @Resource
    private UniversityRepo universityRepo;

    @Autowired
    private LocationMap locationMap;

    @Value("${timeout.request.read}")
    private int readTimeout;

    @Value("${timeout.request.connect}")
    private int connectionTimeout;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String DEFAULT_NODES_PORT = "8080";
    private static final String REPLICATION_URL = "http://{ip}:{port}/utils/replication";

    @PostConstruct
    public void initialize() {
        ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory()).setReadTimeout(readTimeout);
        ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory()).setConnectTimeout(connectionTimeout);
    }

    public List<Location> getTopLocations(final int top) {
        return locationMap.getLocationMap().entrySet().stream()
                .sorted((es1, es2) -> Integer.compare(es1.getValue().size(), es2.getValue().size()))
                .limit(top)
                .map(Map.Entry::getKey)
                .collect(toList());
    }


    public void replicateLocation(@NonNull final Location location, @NonNull final NodeInfo nodeInfo) {

        final List<UniversityDto> universitiesForLocationDto = Optional.ofNullable(universityRepo.findByLocation(location))
                .orElseThrow(() -> new RuntimeException(
                        String.format("No universities found for location: %s", location.toString()))
                ).stream().map(Converter.universityEntityToDto).collect(Collectors.toList());


        final ResponseEntity<Void> replicationResponseEntity = restTemplate.postForEntity(
                REPLICATION_URL,
                universitiesForLocationDto,
                Void.class,
                nodeInfo.getNodeIPAddress(),
                DEFAULT_NODES_PORT
        );

        if(replicationResponseEntity.getStatusCode() != HttpStatus.OK) {
            log.warning(String.format("Could replicate location: %s to node %d[%s]", location, nodeInfo.getNodeId(), nodeInfo.getNodeIPAddress()));
            throw new RuntimeException(String.format("Could not replicate data to node: %d[%s]", nodeInfo.getNodeId(), nodeInfo.getNodeIPAddress()));
        }
    }
}
