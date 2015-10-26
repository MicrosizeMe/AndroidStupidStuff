package com.example.anbo.checkbooktesting;

import com.example.anbo.checkbooktesting.checkbookInterface.Entry;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by Anbo on 10/10/2015.
 */
public class StaticUtil {

    public static final double MILLISECONDS_PER_MINUTE = 60000.0;

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
        return (int) Math.floor((double)(date.getTimeInMillis()) / MILLISECONDS_PER_MINUTE);
    }

    public static Calendar getCalendarFromMinutes(long minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(minutes * (long) MILLISECONDS_PER_MINUTE);
        return cal;
    }

    public static double sumEntries(List<Entry> entries) {
        double sum = 0.0;
        for (Entry entry : entries) {
            sum += entry.getCost();
        }
        return sum;
    }

    public static String parseDouble(double input) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(input);
    }
}
