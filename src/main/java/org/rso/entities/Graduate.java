package org.rso.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rso.utils.ComeFrom;
import org.rso.utils.Location;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Graduate implements Serializable{
    private long id;
    private String name;
    private String surname;
    private ComeFrom comeFrom;
    private Location locationFrom;
    private List<FieldOfStudy> fieldOfStudies = new ArrayList<>();
}
