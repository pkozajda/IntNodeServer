package org.rso.utils;

import org.rso.mongo.dto.GraduateDto;
import org.rso.mongo.entities.Graduate;
import org.rso.mongo.entities.University;
import org.rso.mongo.repo.UniversityMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
        for (University university: getUniversities()){
            Random random = new Random();
            int val = random.nextInt(100)+1000;
            List<GraduateDto> graduateDtoList = new ArrayList<>();
            for (int i = 0; i < val; i++) {
                Graduate graduate = this.createGraduate();
                university.getGraduates().add(graduate);
            }
            this.universityRepository.insert(university);
        }
    }

    private Graduate createGraduate() {
        return null;
    }

    private List<University> getUniversities(){
        return Arrays.asList(
                new University("PW","1820",Location.MAZOWIECKIE,UniversityType.POLYTECHNIC),
                new University("UJ","1364",Location.MALOPOLSKIE,UniversityType.UNIVERSITY),
                new University("SGGW","1820",Location.MAZOWIECKIE,UniversityType.AGRICULTURAL_UNIVERSITY),
                new University("PK","1820",Location.MALOPOLSKIE,UniversityType.POLYTECHNIC),
                new University("Politechnika Wroclawska","1920",Location.DOLNOSLASKIE,UniversityType.POLYTECHNIC),
                new University("Politechnika Gdanska","1930",Location.POMORSKIE,UniversityType.POLYTECHNIC),
                new University("UW","1820",Location.MAZOWIECKIE,UniversityType.UNIVERSITY),
                new University("SGH","1876",Location.MAZOWIECKIE,UniversityType.UNIVERSITY_OF_ECONOMICS),
                new University("Politechnika Swietokrzyska","1820",Location.SWIETOKRZYSKIE,UniversityType.POLYTECHNIC),
                new University("UJK","1820",Location.SWIETOKRZYSKIE,UniversityType.UNIVERSITY),
                new University("AWF Katowice","1820",Location.SLASKIE,UniversityType.OTHER),
                new University("Uniwersytet Slaski","1820",Location.SLASKIE,UniversityType.UNIVERSITY),
                new University("UP","1820",Location.MALOPOLSKIE,UniversityType.UNIVERSITY),
                new University("AGH","1820",Location.MALOPOLSKIE,UniversityType.POLYTECHNIC),
                new University("Uniwersytet Wroclawski","1820",Location.DOLNOSLASKIE,UniversityType.UNIVERSITY),
                new University("Politechnika Rzeszowska","1820",Location.PODKARPADZKIE,UniversityType.POLYTECHNIC),
                new University("Uniwersytet Ekonomiczny w Krakowie","1820",Location.MALOPOLSKIE,UniversityType.UNIVERSITY)
        );
    }
}
