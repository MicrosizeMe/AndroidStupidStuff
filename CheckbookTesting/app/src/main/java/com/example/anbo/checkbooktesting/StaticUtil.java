package com.example.anbo.checkbooktesting;

import java.util.Calendar;

/**
 * Created by Anbo on 10/10/2015.
 */
public class StaticUtil {

    public static Calendar roundDate(Calendar date) {
        date.set(Calendar.MILLISECOND, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.HOUR_OF_DAY, 0);
        return date;
    }

    public static String getStringFromCalendar(Calendar date){
        return (date.get(Calendar.MONTH) + 1) + "."
                + date.get(Calendar.DAY_OF_MONTH) + "."
                + date.get(Calendar.YEAR);
    }

    public static long getMinutesSinceEpoch(Calendar date) {
        return (int) Math.floor((double)(date.getTimeInMillis()) / 60000.0);
    }
}
