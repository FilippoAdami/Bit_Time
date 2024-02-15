package com.application.bit_time.utils.AlarmUtils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    public enum Frequency
    {
        NotSet,
        Daily,
        Weekly
    }

    Field DateField;
    Field TimeField;

  int year;
  int month;
  int day;
  int hour;
  int min;

  Frequency freq;




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


    public AlarmInfo(int year,int month, int dayOfMonth, int hourOfDay, int minute,Frequency freq)
    {
        this(year,month,dayOfMonth,hourOfDay,minute);
        this.freq = freq;

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
       this.freq = Frequency.NotSet;

       Log.i("default constr",toString());
   }

   public AlarmInfo(AlarmInfo info) {
       this();
       this.TimeField = info.TimeField;
       this.DateField = info.DateField;
       this.year = info.year;
       this.month = info.month;
       this.day = info.day;
       this.hour = info.hour;
       this.min = info.min;
       this.freq = info.freq;
   }
   public void setAsSet()
   {
       this.DateField = Field.SET;
       this.TimeField = Field.SET;
   }

    public long getAlarmTimeLong()
   {
       return new GregorianCalendar(this.year,this.month,this.day,this.hour,this.min)
               .getTimeInMillis();
   }

    public GregorianCalendar getInfoGC()
    {
        GregorianCalendar GC =  new GregorianCalendar(this.year,this.month,this.day,this.hour,this.min);
        Log.i("GC",GC.toString());
        return GC;

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


    public String printFlags()
    {
        String str = "DATE : ";
        if(isDateSet())
        {
            str = str.concat("SET");
        }
        else
        {
            str =str.concat(("UNSET"));
        }


        str = str.concat(" TIME ");

        if(isTimeSet())
        {
            str = str.concat("SET");
        }else
        {
            str = str.concat("UNSET");
        }

        return str;
    }

    public String toString()
    {
        String baseStr = "";

        String total = baseStr + " "+printFlags()+" "+printDate()+" "+printTime() + " "+ printFreq();

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
    public void setFreq(Frequency freqToSet)
    {
        this.freq = freqToSet;
    }

    public Frequency getFreq()
    {
        return this.freq;
    }


    public String printFreq()
    {
        return this.freq.toString();
    }

    public int getCode()
    {
        return this.year+this.month+this.day+this.hour+this.min;
    }

    public AlarmInfo dailyPostpone()
    {
        //TODO : checks over months and days and change values
        Log.i("fromAlarmInfo","this.day now is "+this.min);
        this.min=this.min+1;
        Log.i("fromAlarmInfo","this.day now is "+this.min);
        return this;

    }


    public String getFreqStr()
    {
        if(this.freq.equals(Frequency.Daily)) {
            return "Daily";
        }
        else if(this.freq.equals(Frequency.Weekly))
        {
            return "Weekly";
        }
        else return "Not Set";
    }

    public AlarmInfo weeklyPostpone()
    {
        //TODO : checks over months and days and change values
        Log.i("fromAlarmInfo","this.day now is "+this.min);
        this.min=this.min+2;
        Log.i("fromAlarmInfo","this.day now is "+this.min);
        return this;
    }

    public String toFormattedString()
    {
        return this.day+"/"+this.month+"/"+this.year+" "+this.hour+":"+this.min;
    }
}
