package com.application.bit_time.utils;

import android.util.Log;

import androidx.annotation.NonNull;

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

    public TimeHelper(int duration)
    {
        int sec = duration%60;
        this.sec = sec;
        int minAndHrs = (duration - sec)/60;
        int mins = minAndHrs % 60 ;
        this.min = mins;
        int hrs = (minAndHrs-mins)/60;
        this.hrs = hrs;
        Log.i("formatting","hrs "+this.hrs + " mins "+this.min + " sec "+this.sec);

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

    @NonNull
    @Override
    public String toString() {
        return this.hrs + " hrs " + this.min +" min " +this.sec + " sec ";
    }

    public String toStringShrt()
    {
        return this.hrs+" h "+this.min+" m "+this.sec+" s";
    }

    public int getHrs()
    {
        return (int)this.hrs;
    }
    public int getMin()
    {
        return (int)this.min;
    }

    public int getSec()
    {
        return (int)this.sec;
    }

}
