package org.pra.nse.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

    public static LocalDate toLocalDate(Date utilDate) {
        //new java.sql.Date(date.getTime()).toLocalDate();
        Instant.ofEpochMilli(utilDate.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date toSqlDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }

    public static Date toUtilDate(LocalDate localDate) {
        return java.util.Date.from(localDate.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static String transformDate(String oldFormat) {
        return oldFormat.substring(4,8)
                + "-" + oldFormat.substring(2,4)
                + "-" + oldFormat.substring(0, 2);
    }
}
