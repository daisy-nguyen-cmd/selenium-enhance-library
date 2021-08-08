package org.my.automationtest.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    public static String obtainCurrentDateTime() {
        Date current = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmm");
        return sdf.format(current);
    }
    public static String obtainCurrentDate() {
        Date current = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
        return sdf.format(current);
    }

    public static String obtainCurrentDateTimeInMiliSecond() {
        Date current = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmm.SSS");
        return sdf.format(current);
    }

    public static String obtainCurrentDateInYYYYMMDD() {
        Date current = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(current);
    }
}
