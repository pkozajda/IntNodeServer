package org.rso.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rso.utils.Location;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@EqualsAndHashCode
public class University {
    private String name;
    private String yearOfFundation;
    private Location location;

    private List<Department> departments = new ArrayList<>();

}
