package org.rso.entities.resposnObjct;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rso.dto.UniversityDto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Radosław on 20.05.2016.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class UniversitiesMapResponse extends ResponsBody {
    private Map<UniversityDto,Integer> universityDtoIntegerMap = new HashMap<>();

    public void addToMap(UniversityDto k, int val){
        this.universityDtoIntegerMap.put(k,val);
    }
}
