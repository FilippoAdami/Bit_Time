package com.application.bit_time;

public class TimeHelper {

    long hrs;
    long min;
    long sec;


    public TimeHelper(long hrs, long min,long sec)
    {
        this.hrs = hrs;
        this.min = min;
        this.sec = sec;
    }


    public void addHrs(long hrs)
    {
        long hrsToAddInMill = 1000 * 60* 60 * hrs;
        this.hrs = this.hrs + hrsToAddInMill;
    }
    public void addMins(long mins)
    {
        long minsToAddInMill = 1000* 60 * mins;
        this.min = this.min + minsToAddInMill;
    }


}
