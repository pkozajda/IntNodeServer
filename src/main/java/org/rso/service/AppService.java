package org.rso.service;

import org.rso.utils.NodeInfo;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


public interface AppService {
    void getGraduteInCountry(NodeInfo nodeInfo);

    void getGraduateInUniversity(NodeInfo nodeInfo);

    void getGraduateInUniversityInCountry(NodeInfo nodeInfo, long countryId);

}
