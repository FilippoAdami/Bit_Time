package com.application.bit_time.utils.AlarmUtils;



import static android.Manifest.permission.SCHEDULE_EXACT_ALARM;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.application.bit_time.utils.PlanningInfo;

import java.util.List;


public class AlarmScheduler implements AlarmSchedulerInterface
{
    private Context context;
    private AlarmManager alarmManager;
    private String actName;

    private int actId;

    @SuppressLint("ScheduleExactAlarm")
    @Override
    public void schedule(AlarmInfo info) {

        Intent intent = new Intent(context.getApplicationContext(),AlarmReceiver.class);
        intent.putExtra("actName",actName);
        intent.putExtra("actId",actId);

        long alarmTime = info.getAlarmTimeLong();
        Log.i("alarmtimeLOG",Long.toString(alarmTime)+" is alarmTimeLNG");

        //TODO : must put conditions on the API version
        if(ContextCompat.checkSelfPermission(context,SCHEDULE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED) {
            Log.i("ALARMPERMS","can be set");
            this.alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, PendingIntent.getBroadcast(context, info.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT));

        }
        else
        {
            this.alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, PendingIntent.getBroadcast(context, info.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT));
            Log.i("ALARMPERMS","cannot be set");
        }


    }

    @Override
    public void cancel(AlarmInfo info) {
        Log.i("AlarmScheduler canc","chose to delete "+info.toString());
        alarmManager.cancel(PendingIntent.getBroadcast(context,info.hashCode(),new Intent(context, AlarmReceiver.class),PendingIntent.FLAG_UPDATE_CURRENT));
    }



    public AlarmScheduler(Context context)
    {
            this.context = context;
            this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    }

    public void scheduleAll(List<PlanningInfo> plans, String actName,int actId)
    {
        this.actName = actName;
        this.actId = actId;

        for(PlanningInfo pi : plans)
        {
            schedule(pi.getInfo());
        }
    }


    public void cancelAll(List<PlanningInfo> plansToDelete)
    {
        for(PlanningInfo pi : plansToDelete)
        {
            cancel(pi.getInfo());
        }
    }

}
