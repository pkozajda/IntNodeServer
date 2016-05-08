package org.rso.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rso.utils.Location;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class University implements Serializable{
    @Id @GeneratedValue
    @Column(name = "UNIVERSITY_ID")
    private long id;
    private String name;
    private String yearOfFundation;
    private Location location;
    @OneToMany
    private List<Department> departments = new ArrayList<>();

}
