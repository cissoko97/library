package org.ckCoder.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateConverted {

    public static Date sqlDateToUtilDate(java.sql.Date date) {
        return (new Date(date.getTime()));
    }

    public static String formatDateToString(LocalDateTime date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
        return format.format(date);
    }
}
