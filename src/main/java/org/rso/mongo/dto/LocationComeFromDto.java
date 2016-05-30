package org.rso.mongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rso.utils.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radosław on 30.05.2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LocationComeFromDto {
    private Location location;
    private List<ComeFromDto> comeFromDtos = new ArrayList<>();
}
