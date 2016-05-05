package org.rso.controllers;

import lombok.extern.java.Log;
import org.rso.utils.DataTimeLogger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Radosław on 04.05.2016.
 */
@RestController
@RequestMapping("example")
@Log
public class ExampleController {

    @RequestMapping("/hello")
    public String sayHello(){
        log.info("logowanie wywolanie kontrollera " + DataTimeLogger.logTime());
        return "hello example client ";
    }

}
