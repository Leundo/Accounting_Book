package com.leundo.flow.entity;

import java.util.Calendar;

public class CalendarExpand {
    public static Calendar getFirstDayOfMouth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020,8,1,0,0,0);
        return calendar;
    }
}
