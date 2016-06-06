package org.rso.mongo.utils;

import org.rso.dto.UniversityDto;
import org.rso.mongo.dto.GraduateDto;
import org.rso.mongo.entities.Graduate;
import org.rso.mongo.entities.University;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * Created by Radosław on 24.05.2016.
 */
public class Converter {

    public static Function<GraduateDto,Graduate> graduateDtoToGraduate = graduateDto ->
        Graduate.builder()
                .name(graduateDto.getName())
                .surname(graduateDto.getSurname())
                .comeFrom(graduateDto.getComeFrom())
                .locationFrom(graduateDto.getLocationFrom())
                .fieldOfStudy(graduateDto.getFieldOfStudy())
                .build();

    public static Function<UniversityDto, University> universityDtoToEntity = universityDto ->
            University.builder()
                    .name(universityDto.getName())
                    .location(universityDto.getLocation())
                    .universityType(universityDto.getUniversityType())
                    .yearOfFundation(universityDto.getYerOfFundation())
                    .graduates(new ArrayList<>())
                    .build();

    public static Function<University, UniversityDto> universityEntityToDto = entity ->
            UniversityDto.builder()
                    .name(entity.getName())
                    .yerOfFundation(entity.getYearOfFundation())
                    .location(entity.getLocation())
                    .universityType(entity.getUniversityType())
                    .build();
}
