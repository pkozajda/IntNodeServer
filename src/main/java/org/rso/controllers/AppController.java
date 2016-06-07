package org.rso.controllers;

import lombok.extern.java.Log;
import org.rso.services.AppServiceImpl;
import org.rso.utils.AppProperty;
import org.rso.utils.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log
@RestController
@RequestMapping("intLayer")
public class AppController {

    private static final AppProperty appProperty = AppProperty.getInstance();

    @Autowired
    private AppServiceImpl appService;

    /*
    * functional requirement 2.1.1
    */

    @RequestMapping(value = "/allCountries",method = RequestMethod.POST)
    public @ResponseBody ResponseEntity getGraduateInCountry(@RequestBody NodeInfo nodeInfo){
        if(appProperty.isSelfNodeCoordinator()){
            appService.getGraduteInCountry(nodeInfo);
        }else {
//            TODO upload job to coordinator
            appService.uploadJobToCoordinator(nodeInfo);
        }
        return new ResponseEntity("",HttpStatus.ACCEPTED);
    }



    @RequestMapping(value = "/selfNode")
    public @ResponseBody ResponseEntity getSelfInfo(){
        return new ResponseEntity(appProperty.getSelfNode(),HttpStatus.OK);
    }

}
