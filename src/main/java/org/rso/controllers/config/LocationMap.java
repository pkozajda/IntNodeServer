package org.rso.controllers.config;


import org.rso.utils.Location;
import org.rso.utils.NodeInfo;
import org.springframework.stereotype.Component;

import java.util.*;


public class LocationMap {
    private Map<Location,List<NodeInfo>> locationMap = new HashMap<>();
    public void add(Location location, NodeInfo nodeInfo){
        if(locationMap.containsKey(location)){
            List<NodeInfo> nodeInfos = locationMap.get(location);
            nodeInfos.add(nodeInfo);
//            locationMap.get(location).add(nodeInfo);
        }
        else {
            List<NodeInfo> nodeInfos = new ArrayList<>();
            nodeInfos.add(nodeInfo);
            locationMap.put(location,nodeInfos);
        }
    }

    public List<NodeInfo> getNodesByLocation(Location location){
        return locationMap.get(location);
    }

    public void removeNodeInfo(NodeInfo nodeInfo){
        for (Location location: locationMap.keySet()){
            List<NodeInfo> nodeInfos = locationMap.get(location);
            nodeInfos.remove(nodeInfo);
        }
    }
}
