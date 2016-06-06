package org.rso.mongo.controllers;

import lombok.NonNull;
import lombok.extern.java.Log;
import org.rso.dto.DtoConverters;
import org.rso.dto.NodeStatusDto;
import org.rso.dto.UniversityDto;
import org.rso.mongo.entities.University;
import org.rso.mongo.repo.UniversityMongoRepository;
import org.rso.mongo.repo.UniversityRepo;
import org.rso.mongo.utils.Converter;
import org.rso.utils.AppProperty;
import org.rso.utils.Location;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log
@RestController
@RequestMapping("utils")
public class ReplicationController {

    @Resource
    private UniversityMongoRepository universityMongoRepository;

    @Resource
    private UniversityRepo universityRepo;

    private final AppProperty appProperty = AppProperty.getInstance();

    @RequestMapping(value = "/replication", method = RequestMethod.POST)
    public ResponseEntity<Void> saveUniversitiesForLocation(@RequestBody @NonNull final List<UniversityDto> universityDtoList){

        universityDtoList.stream()
                .map(Converter.universityDtoToEntity)
                .forEach(universityMongoRepository::insert);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/replication/{locationId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeUniversitiesForLocation(@PathVariable(value="locationId") final Location locationId) {
        final List<University> universitiesByLocation = Optional.ofNullable(universityRepo.findByLocation(locationId))
                .orElseThrow(() -> new RuntimeException(
                        String.format("No universities for location: %s found at node with ID: %d", locationId.toString(), appProperty.getSelfNode().getNodeId()))
                );

        universityRepo.delete(universitiesByLocation);

        return ResponseEntity.noContent().build();
    }
}
