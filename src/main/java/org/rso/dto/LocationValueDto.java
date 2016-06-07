package org.rso.dto;

import lombok.*;
import org.rso.utils.Location;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LocationValueDto {
    private Location location;
    private long value;


}
