package org.rso.utils;

import com.google.common.base.Predicate;
import org.rso.mongo.dto.GraduateDto;
import org.rso.mongo.entities.FieldOfStudy;
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
import java.util.stream.Collectors;

/**
 * Created by Radosław on 25.05.2016.
 */
@Component
public class DataBasePopulator {

    @Autowired
    private UniversityMongoRepository universityRepository;

    private final Random random = new Random();

    private static final Predicate<Location> locationPredicate = location -> location!=Location.LAND;

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
        return Graduate.builder()
                .name(randomName())
                .surname(randomName())
                .comeFrom(randoComeFrom())
                .locationFrom(randomLocation())
                .fieldOfStudy(randomFieldOfStudy())
                .build();
    }

    private FieldOfStudy randomFieldOfStudy() {
        List<FieldOfStudy> fieldOfStudies = Arrays.asList(
                new FieldOfStudy("Informatyka"),
                new FieldOfStudy("Chemia"),
                new FieldOfStudy("Matematyka"),
                new FieldOfStudy("Biologia"),
                new FieldOfStudy("Fizyka"),
                new FieldOfStudy("Historia"),
                new FieldOfStudy("Kulturoznastwo"),
                new FieldOfStudy("Administracja"),
                new FieldOfStudy("Elektronika"),
                new FieldOfStudy("Telekomunikacja"),
                new FieldOfStudy("Radiolgia"),
                new FieldOfStudy("Budownictwo"),
                new FieldOfStudy("Religioznastwo")
        );
        return fieldOfStudies.get(random.nextInt(fieldOfStudies.size()));
    }

    private Location randomLocation(){

        List location = Arrays.asList(Location.values()).stream().filter(l->locationPredicate.apply(l)).collect(Collectors.toList());
        return (Location) location.get(random.nextInt(location.size()));
    }

    private ComeFrom randoComeFrom(){
        return Arrays.asList(ComeFrom.values()).get(random.nextInt(ComeFrom.values().length));
    }

    private String randomName(){

        List<String> names = Arrays.asList("radek","tomek","marek","lukasz","bartek","kamil","darek","michal",
                                            "kamila","lucja","lucyna","marta","agata","krystyna","gosia","mariola");
        return names.get(random.nextInt(names.size()));
    }

    private String radnemSurname(){

        List<String> surnames = Arrays.asList("xxx","xxy","xxz","aaa","xbs","jdk","java","python","anaconda");
        return surnames.get(random.nextInt(surnames.size()));
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
