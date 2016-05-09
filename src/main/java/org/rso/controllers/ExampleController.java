package org.rso.controllers;

import lombok.extern.java.Log;
import org.rso.config.LocationMap;
import org.rso.utils.DataTimeLogger;
import org.rso.utils.Location;
import org.rso.utils.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("example")
@Log
public class ExampleController {

    @Autowired
    private LocationMap locationMap;

    @RequestMapping("/hello")
    public String sayHello(){
        log.info("logowanie wywolanie kontrollera " + DataTimeLogger.logTime());
        return "hello example client ";
    }

    @RequestMapping(value = "/node",method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity example(@RequestBody NodeInfo nodeInfo){
        locationMap.add(Location.DOLNOSLASKIE,nodeInfo);
        return new ResponseEntity(locationMap.getNodesByLocation(Location.DOLNOSLASKIE), HttpStatus.OK);
    }

}
