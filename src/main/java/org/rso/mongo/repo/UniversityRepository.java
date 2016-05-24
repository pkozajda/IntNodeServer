package org.rso.mongo.repo;

import org.rso.mongo.dto.GraduateDto;
import org.rso.mongo.entities.Graduate;
import org.rso.mongo.entities.University;
import org.rso.mongo.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Radosław on 24.05.2016.
 */
@Repository
public class UniversityRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UniversityRepo universityRepo;

    public void insertGraduate(GraduateDto graduateDto){
        University university = universityRepo.findByName(graduateDto.getUniversityDto().getName());
        if(university!=null) {
            Graduate graduate = Converter.graduateDtoToGraduate.apply(graduateDto);
            university.getGraduates().add(graduate);
            universityRepo.save(university);
        }else {
            University newUniversity = Converter.universityDtoTOUniversity.apply(graduateDto.getUniversityDto());
            Graduate graduate = Converter.graduateDtoToGraduate.apply(graduateDto);
            newUniversity.getGraduates().add(graduate);
            universityRepo.save(newUniversity);
        }
    }

    public University insert(University university){
        return universityRepo.insert(university);
    }

    public University findByName(String name){
        return universityRepo.findByName(name);
    }

    public List<University> findAll(){
        return universityRepo.findAll();
    }
}
