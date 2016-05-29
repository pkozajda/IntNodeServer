package org.rso.service;

import lombok.extern.java.Log;
import org.rso.config.LocationMap;
import org.rso.dto.JobEntityDto;
import org.rso.entities.resposnObjct.LocationMapResponse;
import org.rso.mongo.dto.LocationValueDto;
import org.rso.utils.JobQueue;
import org.rso.utils.Location;
import org.rso.utils.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Radosław on 23.05.2016.
 */
@Service
@Transactional
@Log
public class JobServiceImpl implements JobService {

    private final static int EXTERNAL_NODES_PORT = 9000;

    @Autowired
    private JobQueue jobQueue;

    @Autowired
    private LocationMap locationMap;

    private final Random random = new Random();

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${timeout.request.read}")
    private int readTimeout;

    @Value("${timeout.request.connect}")
    private int connectionTimeout;


    private static final String GRADUATE_BY_LOCATION = "http://{ip}:{port}/int/graduatesByLocation/{location}";
    private static final int BASE_PORT = 8080;

    @PostConstruct
    public void initialize() {
        ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory()).setReadTimeout(readTimeout);
        ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory()).setConnectTimeout(connectionTimeout);
    }

    @Override
    public void registerJob(JobEntityDto jobEntityDto) {
        log.info("add to queue "+jobEntityDto);
        jobQueue.add(jobEntityDto);
    }

    @Override
    public void getGraduatesFromAllCountriesJob(JobEntityDto jobEntityDto) {

        List<LocationValueDto> result = new ArrayList<>();
        LocationMapResponse locationMapResponse = new LocationMapResponse();
        for (Location location: Arrays.asList(Location.MALOPOLSKIE,Location.MAZOWIECKIE,Location.SWIETOKRZYSKIE,Location.POMORSKIE)){
            final String ipAddress = getResourceNodeIp(location);
            LocationValueDto res = restTemplate.getForObject(
                    GRADUATE_BY_LOCATION,
                    LocationValueDto.class,
                    ipAddress,
                    BASE_PORT,
                    location
            );
//            log.info(res.toString());
            result.add(res);
            locationMapResponse.addToMap(location,(int)res.getValue());
        }
        jobEntityDto.setResponsBody(locationMapResponse);
        sendResponse(jobEntityDto,result);
    }

    private void sendResponse(JobEntityDto jobEntityDto, Object result){
        String ipAddress = jobEntityDto.getOrderCustomer().getNodeIPAddress();
        final String jobUrl = jobEntityDto.getJobType().getUrl();
        final String resUrl = "http://{ip}:{port}"+jobUrl+"/{jobId}";
        log.info("wysylam cialo "+jobEntityDto.getResponsBody().toString());
        try {
            restTemplate.postForObject(
                    resUrl,
                    result,
                    Void.class,
                    ipAddress,
                    EXTERNAL_NODES_PORT,
                    jobEntityDto.getId()
            );
        }catch (Exception e){
            e.printStackTrace();
            log.info("exception to retransision ");
        }
    }

    private String getResourceNodeIp(Location location){
        List<NodeInfo> nodeInfos = locationMap.getNodesByLocation(location);
        int randomNumber = random.nextInt(nodeInfos.size());
        return nodeInfos.get(randomNumber).getNodeIPAddress();
    }


}
