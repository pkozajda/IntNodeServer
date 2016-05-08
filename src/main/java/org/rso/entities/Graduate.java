package org.rso.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rso.utils.ComeFrom;
import org.rso.utils.Location;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Graduate implements Serializable{
    @Id @GeneratedValue
    @Column(name = "GRADUATE_ID")
    private long id;
    private String name;
    private String surname;
    private ComeFrom comeFrom;
    private Location locationFrom;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<FieldOfStudy> fieldOfStudies = new ArrayList<>();
}
