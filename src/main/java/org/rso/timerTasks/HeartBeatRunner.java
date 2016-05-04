package org.rso.timerTasks;

import lombok.extern.java.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Radosław on 04.05.2016.
 */
@Log
public class HeartBeatRunner {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public static void run(){
        log.info("run HEART BEAT protocol: " + dateFormat.format(new Date()));
    }

}
