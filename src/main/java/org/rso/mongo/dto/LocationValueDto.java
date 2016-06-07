package org.rso.mongo.dto;

import lombok.*;
import org.rso.utils.Location;

/**
 * Created by Radosław on 28.05.2016.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LocationValueDto {
    private Location location;
    private long value;


}
