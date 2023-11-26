package com.application.bit_time.utils.AlarmUtils;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class AlarmScheduler implements AlarmSchedulerInterface
{
    private Context context;
    private AlarmManager alarmManager;

    @Override
    public void schedule(AlarmInfo info) {
        //Intent intent = new Intent(context,AlarmReceiver.class);

        long alarmTime = info.getAlarmTimeLong();
        //this.alarmManager.setExact(AlarmManager.RTC_WAKEUP,alarmTime, PendingIntent.getBroadcast(context,alarmItem.hashCode(),intent,PendingIntent.FLAG_UPDATE_CURRENT));


    }

    @Override
    public void cancel(AlarmInfo info) {

    }



    public AlarmScheduler()
    {
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    }

}
