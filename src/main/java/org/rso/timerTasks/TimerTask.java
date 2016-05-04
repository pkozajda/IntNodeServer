package org.rso.timerTasks;

import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Radosław on 04.05.2016.
 */

@Component
public class TimerTask {

    private static final int PERIOD_OF_TIME = 2000; // in milliseconds

    @Scheduled(fixedRate = PERIOD_OF_TIME)
    public void heartBeatTask(){
        HeartBeatRunner.run();
    }

}
