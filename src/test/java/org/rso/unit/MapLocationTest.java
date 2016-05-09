package org.rso.unit;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rso.IntNodeServerApplication;
import org.rso.controllers.config.LocationMap;
import org.rso.utils.Location;
import org.rso.utils.NodeInfo;
import org.rso.utils.NodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IntNodeServerApplication.class)
@WebAppConfiguration
public class MapLocationTest {

    @Autowired
    private LocationMap locationMap;

    @Test
    public void addToMapLocation(){
        NodeInfo nodeInfo = new NodeInfo(1,"123.33.33.22", NodeType.INTERNAL);
        NodeInfo nodeInfo1 = new NodeInfo(2,"123.33.33.22", NodeType.INTERNAL_COORDINATOR);

        locationMap.add(Location.MAZOWIECKIE,nodeInfo);
        locationMap.add(Location.DOLNOSLASKIE,nodeInfo);
        locationMap.add(Location.DOLNOSLASKIE,nodeInfo1);

        Assert.assertEquals(2,locationMap.getNodesByLocation(Location.DOLNOSLASKIE).size());

        locationMap.removeNodeInfo(nodeInfo);

        Assert.assertEquals(1,locationMap.getNodesByLocation(Location.DOLNOSLASKIE).size());
        Assert.assertEquals(0,locationMap.getNodesByLocation(Location.MAZOWIECKIE).size());

    }

}
