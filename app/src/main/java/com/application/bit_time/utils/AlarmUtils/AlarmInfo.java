package com.application.bit_time.utils.AlarmUtils;

import android.util.Log;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AlarmInfo {

    private enum Field
    {
        SET,
        NOT_SET
    };

    Field DateField;
    Field TimeField;

  int year;
  int month;
  int day;
  int hour;
  int min;



   public AlarmInfo(int year,int month, int dayOfMonth, int hourOfDay, int minute)
   {
       this();
       Log.i("AlarmInfo","new calendar set ;)");
       this.year= year;
       this.month= month;
       this.day = dayOfMonth;
       this.hour = hourOfDay;
       this.min = minute;
   }

   public AlarmInfo()
   {
       this.TimeField = Field.NOT_SET;
       this.DateField = Field.NOT_SET;
       //TODO: this is a test, todo as placeholder to find it quicker

       GregorianCalendar calendar = new GregorianCalendar();

       this.year= calendar.get(Calendar.YEAR);
       this.month = calendar.get(Calendar.MONTH);
       this.day = calendar.get(Calendar.DAY_OF_MONTH);
       this.hour = calendar.get(Calendar.HOUR);
       this.min = calendar.get(Calendar.MINUTE);

       Log.i("default constr",toString());
   }

   public AlarmInfo(AlarmInfo info)
   {
       this.year = info.year;
       this.month = info.month;
       this.day = info.day;
       this.hour = info.hour;
       this.min =info.min;
   }



   public long getAlarmTimeLong()
   {
       return new GregorianCalendar(this.year,this.month,this.day,this.hour,this.min).getTimeInMillis();
   }

    public GregorianCalendar getInfoGC()
    {
        return new GregorianCalendar(this.year,this.month,this.day,this.hour,this.min);
    }

    public void setTime(int hours,int minutes)
    {
        this.hour = hours;
        this.min= minutes;
        this.TimeField = Field.SET;
    }

    public void setDate(int year,int month,int day)
    {
        this.year=year;
        this.month=month;
        this.day=day;
        this.DateField = Field.SET;
    }

    public String printDate()
    {
        int monthToBePrinted = this.month+1;
        String baseStr = "";
        return baseStr+this.year+"/"+monthToBePrinted+"/"+this.day;
    }

    public String printTime()
    {
        String baseStr = "";
        return baseStr + this.hour +":"+this.min;
    }


    public String toString()
    {


        String baseStr = "";

        String total = baseStr + printDate()+" "+printTime();

        return total;
    }

    public boolean isDateSet()
    {
        if(this.DateField.equals(Field.SET))
            return true;

        return false;
    }

    public boolean isTimeSet()
    {
        if(this.TimeField.equals(Field.SET))
            return true;

        return false;
    }
}
