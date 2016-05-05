package org.rso.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Radosław on 04.05.2016.
 */
public class DataTimeLogger {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SS");

    public static String logTime(){
        return dateFormat.format(new Date());
    }
}
