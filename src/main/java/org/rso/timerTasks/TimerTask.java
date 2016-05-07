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

    @Autowired
    private InternalNodeUtilService utilService;

    private AppProperty appProperty = AppProperty.getInstance();

    /* Heartbeat procedure task - executed by current coordinator */
    @Scheduled(fixedRateString = "${delay.heartbeat}")
    public void heartbeatTask() {
        final NodeType selfType = appProperty.getSelfNode().getNodeType();

        if(selfType == NodeType.INTERNAL_COORDINATOR) {
            utilService.doHeartBeat();
        }
    }

    /* Coordinator presence verification task - executed by each internal node */
    //@Scheduled(fixedRateString = "${delay.coordinator.check}")
    public void coordinatorPresenceVerificationTask() {
        final NodeType selfType = appProperty.getSelfNode().getNodeType();

        if(selfType == NodeType.INTERNAL) {
            utilService.verifyCoordinatorPresence();
        }
    }
}
