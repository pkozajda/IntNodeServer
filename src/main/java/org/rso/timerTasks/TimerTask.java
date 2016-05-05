package org.rso.timerTasks;

import lombok.extern.java.Log;
import org.rso.service.InternalNodeUtilService;
import org.rso.utils.AppProperty;
import org.rso.utils.NodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Radosław on 04.05.2016.
 */

@Component
@Log
public class TimerTask {
    private static final int PERIOD_OF_TIME = 2000; // in milliseconds

    @Autowired
    private InternalNodeUtilService utilService;

    @Scheduled(fixedRate = PERIOD_OF_TIME)
    public void doHeartBeat(){
        NodeType selfType = AppProperty.getInstance().getSelfNode().getNodeType();
        if(selfType==NodeType.INTERNAL_COORDINATOR){
            utilService.doHeartBeat();
        }
    }
}
