package org.rso.controllers;

import lombok.extern.java.Log;
import org.rso.service.AppService;
import org.rso.utils.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Log
@RestController
@RequestMapping("intLayer")
public class AppController {

    @Autowired
    private AppService appService;

    /*
    * functional requirement 2.1.1
    */
    @RequestMapping(value = "/allCounties",method = RequestMethod.POST)
    public HttpStatus getGraduateInCountry(@RequestBody NodeInfo nodeInfo){
        appService.getGraduteInCountry(nodeInfo);
        return HttpStatus.ACCEPTED;
    }

}
