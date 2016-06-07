package org.rso.mongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rso.dto.UniversityDto;
import org.rso.mongo.entities.FieldOfStudy;
import org.rso.utils.ComeFrom;
import org.rso.utils.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radosław on 24.05.2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GraduateDto {
    private String name;
    private String surname;
    private ComeFrom comeFrom;
    private Location locationFrom;
    private List<FieldOfStudy> fieldOfStudies = new ArrayList<>();
    private UniversityDto universityDto;
}
