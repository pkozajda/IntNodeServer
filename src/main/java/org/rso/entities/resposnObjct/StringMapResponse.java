package org.rso.entities.resposnObjct;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Radosław on 20.05.2016.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class StringMapResponse extends ResponsBody{
    private Map<String,Integer> map = new HashMap<>();

    public void addToMap(String k, int v){
        map.put(k,v);
    }
}
