package org.rso.entities.resposnObjct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rso.utils.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Radosław on 19.05.2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LocationMapResponse extends ResponsBody {
    private Map<Location,Integer> result = new HashMap<>();

    public void addToMap(Location location, int val){
        result.put(location,val);
    }
}
