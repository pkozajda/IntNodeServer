package org.rso.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Department implements Serializable{
    @Id @GeneratedValue
    @Column(name = "DEPARTMENT_ID")
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<FieldOfStudy> fieldOfStudies = new ArrayList<>();

    public Department(String name){
        this.name = name;
    }

}
