package com.application.bit_time.utils.AlarmUtils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.application.bit_time.Main_Activity.MainActivity;
import com.application.bit_time.R;

public class AlarmReceiver extends BroadcastReceiver {


    int durMax = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        int actId = intent.getExtras().getInt("actId");

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("actId",actId);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT |PendingIntent.FLAG_IMMUTABLE);


        String actName = intent.getExtras().getString("actName","unnamed activity");

        Log.i("ALARMRECEIVER","log from me");

        String channelId = "17";
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle("test notification")
                        .setContentText(actName + " is ready to start")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                        .setTimeoutAfter(durMax);



        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


        notificationManager.notify(17, builder.build());
        //TODO:manage how the alarm behaves aftere the notification is fired
        AlarmScheduler alarmScheduler = new AlarmScheduler(context.getApplicationContext());
        //alarmScheduler.manage();
    }
}
