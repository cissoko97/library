package org.ckCoder.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverted {

    public static Date sqlDateToUtilDate(java.sql.Date date) {
        return (new Date(date.getTime()));
    }

    public static String formatDateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
        return format.format(date);
    }
}
