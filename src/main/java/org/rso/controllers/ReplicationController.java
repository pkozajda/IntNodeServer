package org.rso.controllers;

import lombok.extern.java.Log;
import org.rso.dto.DtoConverters;
import org.rso.dto.NodeStatusDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Log
@RestController
@RequestMapping("utils")
public class ReplicationController {

    @RequestMapping(value = "/replication", method = RequestMethod.POST)
    public ResponseEntity<Void> doDataReplication(){



        return ResponseEntity.ok().build();
    }
}
