package org.rso.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rso.utils.Location;
import org.rso.utils.UniversityType;


/**
 * Created by Radosław on 20.05.2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UniversityDto {
    private String name;
    private String yerOfFundation;
    private Location location;
    private UniversityType universityType;
}
