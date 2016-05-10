package org.rso.service;

import org.rso.dto.GraduateInsertDto;
import org.rso.entities.Graduate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class GraduateServiceImpl implements GraduateService{

    @Value("${timeout.request.read}")
    private int readTimeout;

    @Value("${timeout.request.connect}")
    private int connectionTimeout;

    private static final String DEFAULT_NODES_PORT = "8080";


    @Override
    public Graduate insertGraduate(GraduateInsertDto graduateInsertDto) {
        return null;
    }

    @Override
    public List<Graduate> insertGraduates(List<GraduateInsertDto> graduateInsertDtos) {
        return null;
    }

    @Override
    public void transferToCoordinator(GraduateInsertDto graduateInsertDto) {

    }

    @Override
    public void transferToCoordinatorList(List<GraduateInsertDto> graduateInsertDto) {

    }

    @Override
    public void insertGraduateAsCoordinator(GraduateInsertDto graduateInsertDto) {

    }

    @Override
    public void insertGraduatesAsCoordinator(List<GraduateInsertDto> graduateInsertDtos) {

    }
}
