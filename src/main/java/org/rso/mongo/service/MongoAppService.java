package org.rso.mongo.service;

import org.rso.dto.UniversityDto;
import org.rso.mongo.dto.LocationValueDto;
import org.rso.mongo.exceptions.LocationDoesNotExist;
import org.rso.mongo.repo.UniversityMongoRepository;
import org.rso.utils.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Radosław on 28.05.2016.
 */
@Service
@Transactional
public class MongoAppService {

    @Autowired
    private UniversityMongoRepository universityMongoRepository;

    public LocationValueDto getGraduatesByLocation(Location location) {
        if(!isLocationExist(location)){
            throw new LocationDoesNotExist(String.format("location %s does not exist!!! ",location.toString()));
        }
        return universityMongoRepository.getGraduatesByLocation(location);
    }

    private boolean isLocationExist(Location location) {
        return Arrays.asList(Location.values()).contains(location);
    }

    public List<UniversityDto> getGraduatesByLocationInAllUniwersity(Location location) {
        if(!isLocationExist(location)){
            throw new LocationDoesNotExist(String.format("location %s does not exist!!! ",location.toString()));
        }
        return universityMongoRepository.getGraduatesByLocationInAllUniwersity(location);
    }

    public Map<String, Long> getGraduatesByLocationInAllFieldOfStudy(Location location) {
        if(!isLocationExist(location)){
            throw new LocationDoesNotExist(String.format("location %s does not exist!!! ",location.toString()));
        }
        return universityMongoRepository.getGraduatesByLocationInAllFieldOfStudy(location);
    }
}
