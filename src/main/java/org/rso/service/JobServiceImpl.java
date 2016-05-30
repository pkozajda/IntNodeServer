package org.rso.service;

import lombok.extern.java.Log;
import org.rso.config.LocationMap;
import org.rso.dto.JobEntityDto;
import org.rso.dto.UniversityDto;
import org.rso.entities.resposnObjct.LocationMapResponse;
import org.rso.mongo.dto.FieldOfStudyDto;
import org.rso.mongo.dto.LocationValueDto;
import org.rso.utils.JobQueue;
import org.rso.utils.Location;
import org.rso.utils.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.*;

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
    private static final String GRADUATE_BY_UNIVERSITIES = "http://{ip}:{port}/int/getGraduatesByLocationInAllUniwersity/{location}";
    private static final String GRADUATE_BY_FIELD_OF_STUDIES = "http://{ip}:{port}/int/getGraduatesByLocationInAllFieldOfStudy/{location}";
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
        for (Location location: avaiableLocation()){
            final String ipAddress = getResourceNodeIp(location);
            LocationValueDto res = restTemplate.getForObject(
                    GRADUATE_BY_LOCATION,
                    LocationValueDto.class,
                    ipAddress,
                    BASE_PORT,
                    location
            );
//            TODO if not responding try agian
//            log.info(res.toString());
            result.add(res);
            locationMapResponse.addToMap(location,(int)res.getValue());
        }
        jobEntityDto.setResponsBody(locationMapResponse);
        sendResponse(jobEntityDto,result);
    }

    @Override
    public void getGraduatesFromAllUniversities(JobEntityDto jobEntityDto) {
        List<UniversityDto> result = new ArrayList<>();
        for (Location location: avaiableLocation()){
            final String ipAddress = getResourceNodeIp(location);

            ResponseEntity<List<UniversityDto>> uniResponseEntity = restTemplate.exchange(
                    GRADUATE_BY_UNIVERSITIES,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<UniversityDto>>() {
                        @Override
                        public Type getType() {
                            return super.getType();
                        }
                    },
                    ipAddress,
                    BASE_PORT,
                    location
            );
//            TODO if not responding try agian
            result.addAll(uniResponseEntity.getBody());
        }
        sendResponse(jobEntityDto,result);
    }

    @Override
    public void getGraduatesFromAllFieldOfStudies(JobEntityDto jobEntityDto) {
        List<FieldOfStudyDto> result = new ArrayList<>();
        Map<String, Long> map = new HashMap<>();
        for (Location location: avaiableLocation()){
            final String ipAddress = getResourceNodeIp(location);
            ResponseEntity<List<FieldOfStudyDto>> listResponseEntity = restTemplate.exchange(
                    GRADUATE_BY_FIELD_OF_STUDIES,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<FieldOfStudyDto>>() {
                        @Override
                        public Type getType() {
                            return super.getType();
                        }
                    },
                    ipAddress,
                    BASE_PORT,
                    location
            );
//            TODO if not responding try agian
//            reduceResult(listResponseEntity.getBody(),map);
            listResponseEntity.getBody().stream().forEach(le -> {
                map.computeIfPresent(le.getName(),(k,v)->v+=le.getVal());
                map.putIfAbsent(le.getName(),le.getVal());
            });

        }

        for(String str: map.keySet()){
            result.add(new FieldOfStudyDto(str,map.get(str)));
        }
        sendResponse(jobEntityDto,result);
    }


    private void sendResponse(JobEntityDto jobEntityDto, Object result){
        String ipAddress = jobEntityDto.getOrderCustomer().getNodeIPAddress();
        final String jobUrl = jobEntityDto.getJobType().getUrl();
        final String resUrl = "http://{ip}:{port}"+jobUrl+"/{jobId}";
//        log.info("wysylam cialo "+jobEntityDto.getResponsBody().toString());
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

    private List<Location> avaiableLocation(){
        return Arrays.asList(
                Location.MAZOWIECKIE,
                Location.MALOPOLSKIE,
                Location.DOLNOSLASKIE,
                Location.POMORSKIE,
                Location.SWIETOKRZYSKIE,
                Location.SLASKIE,
                Location.PODKARPADZKIE
        );
    }

}
