package org.rso.services;

import org.rso.utils.NodeInfo;


public interface AppService {
    void getGraduteInCountry(NodeInfo nodeInfo);

    void getGraduateInUniversity(NodeInfo nodeInfo);

    void getGraduateInUniversityInCountry(NodeInfo nodeInfo, long countryId);

    void uploadJobToCoordinator(NodeInfo nodeInfo);
}
