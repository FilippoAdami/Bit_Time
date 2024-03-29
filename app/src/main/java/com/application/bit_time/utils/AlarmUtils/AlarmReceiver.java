package com.application.bit_time.utils.AlarmUtils;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.application.bit_time.Main_Activity.MainActivity;
import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbManager;

public class AlarmReceiver extends BroadcastReceiver {


    int durMax = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        DbManager dbManager = new DbManager(context.getApplicationContext());

        Bundle extrasBundle = intent.getExtras();
        int actId = extrasBundle.getInt("actId");
        int alarmId = extrasBundle.getInt("alarmId");
        String title = extrasBundle.getString("title");   //String.valueOf(R.string.AlarmNotificationTitle)
        String msg = extrasBundle.getString("msg");   //String.valueOf(R.string.readyToStartMsg)

        Log.i("title recevied",title);
        Log.i("msg received",msg);

        Log.i("alarmId received sched", Integer.toString(alarmId));

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("actId", actId);
        //resultIntent.putExtra("alarmId",alarmId);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        String actName = intent.getExtras().getString("actName", "unnamed activity");

        Log.i("ALARMRECEIVER", "log from me");

        //TODO:manage how the alarm behaves aftere the notification is fired
        AlarmScheduler alarmScheduler = new AlarmScheduler(context.getApplicationContext());
        Log.i("actId passed 2 mansched",Integer.toString(actId));
        AlarmInfo schedule= dbManager.selectScheduleById(alarmId);
        Log.i("schedule retr is",schedule.toString());
        alarmScheduler.manage(schedule,alarmId,actId,actName);



        String channelId = "17";
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.happy_dog)
                        .setContentTitle(title)
                        .setContentText(actName + msg)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                        .setTimeoutAfter(durMax);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);



        notificationManager.notify(17, builder.build());

    }
}
