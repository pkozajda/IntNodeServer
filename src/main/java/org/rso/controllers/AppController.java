package org.rso.controllers;

import lombok.extern.java.Log;
import org.rso.service.AppService;
import org.rso.service.AppServiceImpl;
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

    @Autowired
    private AppServiceImpl appService;

    /*
    * functional requirement 2.1.1
    */

    private static final AppProperty appProperty = AppProperty.getInstance();

    @RequestMapping(value = "/allCounties")
    public @ResponseBody ResponseEntity getGraduateInCountry(){
        return new ResponseEntity("ok",HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/selfNode")
    public @ResponseBody ResponseEntity getSelfInfo(){
        return new ResponseEntity(appProperty.getSelfNode(),HttpStatus.OK);
    }

}
