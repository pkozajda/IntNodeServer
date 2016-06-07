package org.rso.services;

import org.rso.utils.AppProperty;
import org.rso.utils.NodeInfo;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class AppServiceImpl implements AppService {

    private static final int TIMEOUT = 5000;

    private static final String DEFAULT_NODES_PORT = "8080";
    private static final String GET_ALL_COUNTRIES_URS = "http://{ip}:{port}/intLayer/allCountries";


    private final RestTemplate restTemplate = new RestTemplate();

    private final AppProperty appProperty = AppProperty.getInstance();

    @PostConstruct
    public void initialize() {
        ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory()).setReadTimeout(TIMEOUT);
        ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory()).setConnectTimeout(TIMEOUT);
    }

    @Override
    public void getGraduteInCountry(NodeInfo nodeInfo) {

    }

    @Override
    public void getGraduateInUniversity(NodeInfo nodeInfo) {

    }

    @Override
    public void getGraduateInUniversityInCountry(NodeInfo nodeInfo, long countryId) {

    }

    @Override
    public void uploadJobToCoordinator(NodeInfo nodeInfo) {
        final String coordinatorIp = appProperty.getCoordinatorNode().getNodeIPAddress();

    }
}
