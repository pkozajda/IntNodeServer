package org.rso.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Department {
    private Long id;
    private String name;

    private List<FieldOfStudy> fieldOfStudies = new ArrayList<>();

}
