package org.rso.timerTasks;

import lombok.extern.java.Log;
import org.rso.service.InternalNodeUtilService;
import org.rso.utils.AppProperty;
import org.rso.utils.NodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Log
@Component
public class TimerTask {
//    private static final int PERIOD_OF_TIME = 5000; // in milliseconds

    @Autowired
    private InternalNodeUtilService utilService;

//    wykonuje koordator
    @Scheduled(fixedRateString = "${delay.heartbeat}")
    public void doHeartBeat(){
        NodeType selfType = AppProperty.getInstance().getSelfNode().getNodeType();
        if(selfType==NodeType.INTERNAL_COORDINATOR){
            utilService.doHeartBeat();
        }
    }

//    wykonuje zwykly wezel
//    @Scheduled(fixedRate = PERIOD_OF_TIME)
//    TODO co jaki czas sprawdza obecnosc koordynatora zwykly wezel
    public void verifyCoordinatorPresence(){
        NodeType selfType = AppProperty.getInstance().getSelfNode().getNodeType();
        if(selfType==NodeType.INTERNAL){
            utilService.verifyCoordinatorPresence();
        }
    }
}
