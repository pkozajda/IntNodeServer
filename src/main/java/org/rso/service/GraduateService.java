package org.rso.service;

import org.rso.dto.GraduateInsertDto;
import org.rso.entities.Graduate;

import java.util.List;

public interface GraduateService {
    Graduate insertGraduate(GraduateInsertDto graduateInsertDto);
    List<Graduate> insertGraduates(List<GraduateInsertDto> graduateInsertDtos);
    void transferToCoordinator(GraduateInsertDto graduateInsertDto);
    void transferToCoordinatorList(List<GraduateInsertDto> graduateInsertDto);

    void insertGraduateAsCoordinator(GraduateInsertDto graduateInsertDto);

    void insertGraduatesAsCoordinator(List<GraduateInsertDto> graduateInsertDtos);
}
