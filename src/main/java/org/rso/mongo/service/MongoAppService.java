package org.rso.mongo.service;

import org.rso.dto.UniversityDto;
import org.rso.mongo.dto.*;
import org.rso.mongo.exceptions.LocationDoesNotExist;
import org.rso.mongo.repo.UniversityMongoRepository;
import org.rso.utils.ComeFrom;
import org.rso.utils.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public List<UniversityDto> getGraduatesByLocationInAllUniwersity(Location location) {
        if(!isLocationExist(location)){
            throw new LocationDoesNotExist(String.format("location %s does not exist!!! ",location.toString()));
        }
        return universityMongoRepository.getGraduatesByLocationInAllUniwersity(location);
    }

    public List<FieldOfStudyDto> getGraduatesByLocationInAllFieldOfStudy(Location location) {
        if(!isLocationExist(location)){
            throw new LocationDoesNotExist(String.format("location %s does not exist!!! ",location.toString()));
        }
        Map<String, Long> fromDob = universityMongoRepository.getGraduatesByLocationInAllFieldOfStudy(location);
        List<FieldOfStudyDto> fileOfStudyDtos = new ArrayList<>();
        for(String k: fromDob.keySet()){
            long val = fromDob.get(k);
            fileOfStudyDtos.add(new FieldOfStudyDto(k,val));
        }
        return fileOfStudyDtos;
    }

    public List<ComeFromDto> getStatisticOrginGraduateByLocation(Location location) {
        if(!isLocationExist(location)){
            throw new LocationDoesNotExist(String.format("location %s does not exist!!! ",location.toString()));
        }
        Map<String ,Long> res = universityMongoRepository.getStatisticOrginGraduateByLocation(location);
        List<ComeFromDto> resList = new ArrayList<>();
        for(String k: res.keySet()){
            long val = res.get(k);
            resList.add(new ComeFromDto(ComeFrom.valueOf(k),val));
        }
        return resList;
    }

    public List<UniversityComeFromDto> getStatisticOrginGaduateByUniversities(Location location) {
        if(!isLocationExist(location)){
            throw new LocationDoesNotExist(String.format("location %s does not exist!!! ",location.toString()));
        }
        return universityMongoRepository.getStatisticOrginGaduateByUniversities(location);
//        return result;
    }

    private boolean isLocationExist(Location location) {
        return Arrays.asList(Location.values()).contains(location);
    }


    public List<FieldOfStudyComeFromDto> getStatisticOrginGraduateByFieldOfStudies(Location location) {
        if(!isLocationExist(location)){
            throw new LocationDoesNotExist(String.format("location %s does not exist!!! ",location.toString()));
        }
        return universityMongoRepository.getStatisticOrginGraduateByFieldOfStudies(location);
    }

    public LocationValueDto getStatisticWorkingStudentsByCountries(Location location) {
        if(!isLocationExist(location)){
            throw new LocationDoesNotExist(String.format("location %s does not exist!!! ",location.toString()));
        }
        return universityMongoRepository.getStatisticWorkingStudentsByCountries(location);
    }

    public List<UniversityDto> getStatisticWorkingStudentsByUniverities(Location location) {
        if(!isLocationExist(location)){
            throw new LocationDoesNotExist(String.format("location %s does not exist!!! ",location.toString()));
        }
        return universityMongoRepository.getStatisticWorkingStudentsByUniverities(location);
    }


    public List<FieldOfStudyDto> getStatisticWorkingStudentsByFieldOfStudy(Location location) {
        if(!isLocationExist(location)){
            throw new LocationDoesNotExist(String.format("location %s does not exist!!! ",location.toString()));
        }
        return universityMongoRepository.getStatisticWorkingStudentsByFieldOfStudy(location);
    }

    public LocationValueDto getGraduatesMoreThanOneFieldOfStudyByCountries(Location location) {
        if(!isLocationExist(location)){
            throw new LocationDoesNotExist(String.format("location %s does not exist!!! ",location.toString()));
        }
        return universityMongoRepository.getGraduatesMoreThanOneFieldOfStudyByCountries(location);
    }

    public List<UniversityDto> getGraduatesMoreThanOneFieldOfStudyByUniversities(Location location) {
        if(!isLocationExist(location)){
            throw new LocationDoesNotExist(String.format("location %s does not exist!!! ",location.toString()));
        }
        return universityMongoRepository.getGraduatesMoreThanOneFieldOfStudyByUniversities(location);
    }
}
