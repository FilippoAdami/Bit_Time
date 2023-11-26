package com.application.bit_time.utils.AlarmUtils;

public class DateTimeUtil {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;


    public DateTimeUtil(int year,int month,int dayOfMonth,int hour,int minute)
    {
            this.year= year;
            this.month = month;
            this.day = dayOfMonth;
            this.hour = hour;
            this.min = minute;

    }


    public DateTimeUtil(DateTimeUtil original)
    {
        this.year = original.year;
        this.month = original.month;
        this.day = original.day;
        this.hour= original.hour;
        this.min = original.min;
    }




}
