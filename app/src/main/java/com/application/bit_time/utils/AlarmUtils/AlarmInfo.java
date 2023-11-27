package com.application.bit_time.utils.AlarmUtils;

import android.util.Log;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AlarmInfo {


  private GregorianCalendar calendar;



   public AlarmInfo(int year,int month, int dayOfMonth, int hourOfDay, int minute, int second)
   {
       Log.i("AlarmInfo","new calendar set ;)");
       this.calendar = new GregorianCalendar(year,month,dayOfMonth,hourOfDay,minute);
   }

   public AlarmInfo()
   {
       this.calendar = (GregorianCalendar) Calendar.getInstance();
       Log.i("AlarmInfo","new calendar set ;)");
   }

   public AlarmInfo(GregorianCalendar calendarInfo)
   {
        this.calendar = calendarInfo;
   }

   public long getAlarmTimeLong()
   {
       return this.calendar.getTimeInMillis();
   }

    public GregorianCalendar getInfo()
    {
        return this.calendar;
    }


}
