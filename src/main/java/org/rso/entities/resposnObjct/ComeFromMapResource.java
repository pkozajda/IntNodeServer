package org.rso.entities.resposnObjct;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rso.utils.ComeFrom;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Radosław on 20.05.2016.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ComeFromMapResource<T> extends ResponsBody {
    private Map<T,Map<ComeFrom,Integer>> locationComeFromMap = new HashMap<>();

    public void addToMap(T t, Map<ComeFrom,Integer> map){
        this.locationComeFromMap.put(t,map);
    }
}
