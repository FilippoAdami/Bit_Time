package com.application.bit_time.utils.AlarmUtils;



import static android.Manifest.permission.SCHEDULE_EXACT_ALARM;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.PlanningInfo;

import java.util.List;


public class AlarmScheduler implements AlarmSchedulerInterface
{
    private Context context;
    private AlarmManager alarmManager;
    private String actName;
    private int actId;
    private int alarmId;

    @SuppressLint("ScheduleExactAlarm")
    @Override
    public void schedule(AlarmInfo info) {

        Log.i("alarmInfo sched",info.toString());
        //Log.i("alarmScheduler","actId "+actId + " actName "+actName);

        Intent intent = new Intent(context.getApplicationContext(),AlarmReceiver.class);
        Log.i("intent cre",intent.toString());
        intent.putExtra("actName",actName);
        intent.putExtra("actId",actId);
        intent.putExtra("alarmId",alarmId);
        //intent.putExtra("alarmId",info.)


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), info.getCode(), intent,PendingIntent.FLAG_IMMUTABLE );

        /*Log.i("pendint info",context.getApplicationContext().toString());
        Log.i("pendint info",Integer.toString(info.hashCode()));
        Log.i("pendint info",intent.toString());*/


        Log.i("pendingIntent cre",pendingIntent.toString());
        long alarmTime = info.getAlarmTimeLong();
        Log.i("alarmtimeLOG",Long.toString(alarmTime)+" is alarmTimeLNG");

        //TODO : must put conditions on the API version
        if(ContextCompat.checkSelfPermission(context,SCHEDULE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED) {
            Log.i("ALARMPERMS","can be set");
            this.alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime,pendingIntent );

        }
        else
        {
            this.alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
            Log.i("ALARMPERMS","cannot be set");
        }
    }

    @Override
    public void cancel(AlarmInfo info) {


        Intent intent = new Intent(context.getApplicationContext(),AlarmReceiver.class);


        Log.i("alarmInfo canc",info.toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), info.getCode(),  intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_NO_CREATE );
        if(pendingIntent!= null) {
            Log.i("pendingIntent", pendingIntent.toString());
            alarmManager.cancel(pendingIntent);
        }
        else
            Log.i("pendingIntent","is null");

        Log.i("pendint info",context.getApplicationContext().toString());
        Log.i("pendint info",Integer.toString(info.hashCode()));
        Log.i("pendint info",intent.toString());





    }

    public void manage(AlarmInfo info,int planDbId)
    {
        alarmId = planDbId;
        DbManager dbManager = new DbManager(this.context);
        dbManager.deleteActivitySchedule(planDbId);
        cancel(info);
        if(info.freq.toString().equals("NotSet"))
        {
            Log.i("freq check","freq is not set so i will call cancel");

        }
        else if(info.freq.toString().equals("Daily"))
        {
            Log.i("freq check","freq is set to daily so i will reset the alarm");
            info.min +=1;
            schedule(info);
            dbManager.insertActivitySchedule(actId,info.getInfoGC(),info.getFreq());
        }

        dbManager.closeDb();
    }

    public AlarmScheduler(Context context)
    {
            this.context = context;
            this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    }

    public void scheduleAll(List<PlanningInfo> plans, String actName,List<Integer> ids)
    {
        int i=0;
        this.actName = actName;
        this.actId = ids.get(i);

        for(PlanningInfo pi : plans)
        {
            i++;
            alarmId = ids.get(i);
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
