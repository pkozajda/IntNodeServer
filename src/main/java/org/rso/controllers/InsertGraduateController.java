package org.rso.controllers;

import lombok.extern.java.Log;
import org.rso.dto.GraduateInsertDto;
import org.rso.entities.Graduate;
import org.rso.service.GraduateService;
import org.rso.utils.AppProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log
@RequestMapping("graduate")
public class InsertGraduateController {

    @Autowired
    private GraduateService graduateService;

    private final AppProperty appProperty = AppProperty.getInstance();

    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public HttpStatus insert(@RequestBody GraduateInsertDto graduateInsertDto){
        if(appProperty.isSelfNodeCoordinator()){
            graduateService.insertGraduateAsCoordinator(graduateInsertDto);
        }else {
            graduateService.transferToCoordinator(graduateInsertDto);
        }
        return HttpStatus.ACCEPTED;
    }

    @RequestMapping(value = "/insertList",method = RequestMethod.POST)
    public HttpStatus insertAll(@RequestBody List<GraduateInsertDto> graduateInsertDtos){
        if(appProperty.isSelfNodeCoordinator()){
            graduateService.insertGraduatesAsCoordinator(graduateInsertDtos);
        }else {
            graduateService.transferToCoordinatorList(graduateInsertDtos);
        }
        return HttpStatus.ACCEPTED;
    }

    @RequestMapping(value = "/insertInNode",method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity insertInNode(@RequestBody GraduateInsertDto graduateInsertDto){
        try {
            Graduate graduate = graduateService.insertGraduate(graduateInsertDto);
            return ResponseEntity.ok(graduate);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/insertInNode",method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity insertAllInNode(@RequestBody List<GraduateInsertDto> graduateInsertDto){
        try {
            List<Graduate> graduate = graduateService.insertGraduates(graduateInsertDto);
            return ResponseEntity.ok(graduate);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}