package org.rso.controllers;

import lombok.extern.java.Log;
import org.rso.dto.JobEntityDto;
import org.rso.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Radosław on 23.05.2016.
 */
@Log
@RestController
@RequestMapping(value = "/job")
public class JobController {
    @Autowired
    private JobService jobService;


    @RequestMapping(value = "/registerJob",method = RequestMethod.PUT)
    public ResponseEntity registerJob(@RequestBody JobEntityDto jobEntityDto){
//        log.info("job to register " + jobEntityDto.toString());
        jobService.registerJob(jobEntityDto);
        return new ResponseEntity("ok", HttpStatus.OK);
    }


}
