package org.rso.unit;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.rso.utils.DateComperator;

import java.util.Date;

/**
 * Created by Radosław on 05.05.2016.
 */
public class DateComperatorTest {

    @Ignore
    public void compareDate() throws InterruptedException {
        int time = 6000;
        Date a = new Date();
        Thread.sleep(time);
        Date b = new Date();
        long dif = DateComperator.compareDate(a,b);
        Assert.assertEquals(time,dif);
    }
}
