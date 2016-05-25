package org.rso.utils;

import org.rso.dto.UniversityDto;
import org.rso.mongo.dto.GraduateDto;
import org.rso.mongo.entities.FieldOfStudy;
import org.rso.mongo.repo.UniversityMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Radosław on 25.05.2016.
 */
@Component
public class DataBasePopulator {

    @Autowired
    private UniversityMongoRepository universityRepository;

    @PostConstruct
    public void init(){
        universityRepository.clear();
        populateDB();
    }

    private void populateDB() {
        GraduateDto graduateDto = new GraduateDto("radek","bolek",ComeFrom.SMALL_TOWN,Location.MALOPOLSKIE,
                new FieldOfStudy("Inf"),new UniversityDto(
                "UJ",
                "1364",
                Location.MALOPOLSKIE,
                UniversityType.UNIVERSITY
        ));
        GraduateDto tomek = new GraduateDto("tomek","taboret",ComeFrom.SMALL_TOWN,Location.MALOPOLSKIE,
                new FieldOfStudy("Inf"),new UniversityDto(
                "UJ",
                "1364",
                Location.MALOPOLSKIE,
                UniversityType.UNIVERSITY
        ));
        GraduateDto lukasz = new GraduateDto("lukasz","szafka",ComeFrom.SMALL_TOWN,Location.MALOPOLSKIE,
                new FieldOfStudy("Inf"),new UniversityDto(
                "PW",
                "1364",
                Location.MALOPOLSKIE,
                UniversityType.UNIVERSITY
        ));


        universityRepository.insertGraduate(graduateDto);
        universityRepository.insertGraduate(lukasz);
        universityRepository.insertGraduate(tomek);
    }
}
