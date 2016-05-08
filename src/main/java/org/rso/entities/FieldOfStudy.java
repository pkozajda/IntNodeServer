package org.rso.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class FieldOfStudy implements Serializable{
    private long id;
    private String name;

    private List<Department> departments = new ArrayList<>();

    private List<Graduate> graduates = new ArrayList<>();
}
