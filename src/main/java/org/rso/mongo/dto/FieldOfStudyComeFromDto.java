package org.rso.mongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rso.mongo.entities.FieldOfStudy;

import java.util.List;

/**
 * Created by Radosław on 30.05.2016.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class FieldOfStudyComeFromDto {
    private FieldOfStudy fieldOfStudy;
    private List<ComeFromDto> comeFromDtos;
}
