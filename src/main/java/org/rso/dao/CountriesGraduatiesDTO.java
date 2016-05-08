package org.rso.dao;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rso.utils.Location;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class CountriesGraduatiesDTO {
    private Map<Location,Integer> result = new HashMap<>();
    public void add(Location location,int val){
        result.put(location,val);
    }
}
