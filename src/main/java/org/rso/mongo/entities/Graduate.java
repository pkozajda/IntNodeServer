package org.rso.mongo.entities;

import lombok.*;
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
@Builder
public class Graduate {
    private String name;
    private String surname;
    private ComeFrom comeFrom;
    private Location locationFrom;
    private boolean workedAtStudy;
    private List<FieldOfStudy> fieldOfStudyList = new ArrayList<>();
}
