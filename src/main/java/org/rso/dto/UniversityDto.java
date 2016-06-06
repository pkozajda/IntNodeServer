package org.rso.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.rso.utils.Location;
import org.rso.utils.UniversityType;


/**
 * Created by Radosław on 20.05.2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UniversityDto {
    private String name;
    private String yerOfFundation;
    private Location location;
    private UniversityType universityType;
    private int value;
}
