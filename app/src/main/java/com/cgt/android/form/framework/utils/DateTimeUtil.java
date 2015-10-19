package com.cgt.android.form.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by kst-android on 19/10/15.
 */
public class DateTimeUtil {

    public static String convertGMTtoDateTimeAmPm(Date date)
    {
        SimpleDateFormat dateformat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
        TimeZone tz = TimeZone.getDefault(); //Will return your device current time zone
        dateformat.setTimeZone(tz); //Set the time zone to your simple date formatter
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        Date currenTimeZone = (Date) calendar.getTime();
        return dateformat.format(currenTimeZone);
    }

    public static String convertGMTtoDateTime(Date date)
    {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone tz = TimeZone.getDefault(); //Will return your device current time zone
        dateformat.setTimeZone(tz); //Set the time zone to your simple date formatter
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        Date currenTimeZone = (Date) calendar.getTime();
        return dateformat.format(currenTimeZone);
    }

    public static String convertGMTtoDateTimeZone(Date date)
    {
        SimpleDateFormat dateformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zz yyyy");
        TimeZone tz = TimeZone.getDefault(); //Will return your device current time zone
        dateformat.setTimeZone(tz); //Set the time zone to your simple date formatter
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        Date currenTimeZone = (Date) calendar.getTime();
        return dateformat.format(currenTimeZone);
    }

    public static String convertGMTtoDate(Date date)
    {
        SimpleDateFormat dateformat = new SimpleDateFormat("MMM dd, yyyy");
        TimeZone tz = TimeZone.getDefault(); //Will return your device current time zone
        dateformat.setTimeZone(tz); //Set the time zone to your simple date formatter
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        Date currenTimeZone = (Date) calendar.getTime();
        return dateformat.format(currenTimeZone);
    }

    public static String convertGMTtoDateFormat(Date date)
    {
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        TimeZone tz = TimeZone.getDefault(); //Will return your device current time zone
        dateformat.setTimeZone(tz); //Set the time zone to your simple date formatter
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        Date currenTimeZone = (Date) calendar.getTime();
        return dateformat.format(currenTimeZone);
    }

    public static boolean isValidDateDifference(Date date)
    {
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();

        long diff = now.getTime() - date.getTime();
        long diffHours = diff / (60 * 60 * 1000);

        if (diffHours > 1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static int DateDifference(Date date)
    {
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();

        long diff = now.getTime() - date.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        int diffInDays = (int) ((now.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));

        return (int) diffMinutes;
    }

}
