package org.rso.controllers;

import lombok.extern.java.Log;
import org.rso.service.JobService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Radosław on 23.05.2016.
 */
@Log
@RestController
@RequestMapping(value = "/job")
public class JobController {
    private JobService jobService;
}
