package org.rso.dto;

import lombok.*;
import lombok.experimental.Wither;
import org.rso.utils.Location;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Wither
@EqualsAndHashCode
public class LocationValueDto {
    private Location location;
    private long value;
}
