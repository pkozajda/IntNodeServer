package org.rso.mongo.entities;

import lombok.*;
import org.rso.utils.ComeFrom;
import org.rso.utils.Location;

/**
 * Created by Radosław on 24.05.2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Graduate {
    private String name;
    private String surname;
    private ComeFrom comeFrom;
    private Location locationFrom;
    private FieldOfStudy fieldOfStudy;
}
