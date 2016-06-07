package org.rso.unit;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Assert;
import org.rso.utils.DateComparator;

import java.util.Date;

public class DateComperatorTest {

    @Ignore
    public void compareDate() throws InterruptedException {
        int time = 6000;
        Date a = new Date();
        Thread.sleep(time);
        Date b = new Date();
        long dif = DateComparator.compareDate(a,b);
        Assert.assertEquals(time,dif);
    }
}
