package org.rso.utils;

import java.util.Date;

/**
 * Created by Radosław on 05.05.2016.
 */
public class DateComperator {

    public static long compareDate(Date a, Date b){
        long time =0L;
        if(a.after(b)){
            time = a.getTime() - b.getTime();
        }else {
            time = b.getTime() - a.getTime();
        }
        return time;
    }
}
