package org.rso.services;


public interface NodeUtilService {
    void doHeartBeat();
    void doElection();
    void verifyCoordinatorPresence();

    void connectToNetwork(final String nodeIpAddress);
}
