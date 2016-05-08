package org.rso.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class FieldOfStudy implements Serializable{
    @Id @GeneratedValue
    @Column(name = "FIELD_OF_STUDY_ID")
    private long id;
    private String name;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Graduate> graduates = new ArrayList<>();

    public FieldOfStudy(String name){
        this.name = name;
    }
}
