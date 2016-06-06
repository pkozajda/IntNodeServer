package org.rso.mongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rso.dto.UniversityDto;

import java.util.List;

/**
 * Created by Radosław on 30.05.2016.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UniversityComeFromDto {
    private UniversityDto universityDto;
    private List<ComeFromDto> comeFromDtos;
}
