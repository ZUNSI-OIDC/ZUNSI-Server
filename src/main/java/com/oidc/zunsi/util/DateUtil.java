package com.oidc.zunsi.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

public class DateUtil {
    public static LocalDateTime getCurrentYearFirstDate() {
        int y = LocalDate.now().getYear();
        return getStartDate(y, 1);
    }

    public static LocalDateTime getLastMonthFirstDate() {
        int y = LocalDate.now().getYear();
        int m = LocalDate.now().getMonthValue() - 1;
        return getStartDate(y, m);
    }

    public static LocalDateTime getLastMonthLastDate() {
        int y = LocalDate.now().getYear();
        int m = LocalDate.now().getMonthValue() - 1;
        return getEndDate(y, m);
    }

    public static LocalDateTime getCurrentMonthFirstDate() {
        int y = LocalDate.now().getYear();
        int m = LocalDate.now().getMonthValue();
        return getStartDate(y, m);
    }

    public static LocalDateTime getCurrentMonthLastDate() {
        int y = LocalDate.now().getYear();
        int m = LocalDate.now().getMonthValue();
        return getEndDate(y, m);
    }

    public static LocalDateTime getStartDate(int year, int month) {
        return LocalDateTime.of(LocalDate.of(year, month, 1), LocalTime.of(0, 0, 0));
    }

    public static LocalDateTime getEndDate(int year, int month) {
        Calendar cal = Calendar.getInstance();
        int lastDate = cal.getActualMaximum(Calendar.DATE);
        return LocalDateTime.of(LocalDate.of(year, month, lastDate), LocalTime.of(23, 59, 59));
    }
}
