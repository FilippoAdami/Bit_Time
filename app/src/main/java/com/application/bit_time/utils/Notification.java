package com.application.bit_time.utils;


import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.application.bit_time.R;
import com.application.bit_time.utils.NotificationsUtils.NotificationsSupervisor;

public class Notification {


    public Notification(Context context, String channelId, NotificationsSupervisor supervisor) {


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle("Attività imminente programmata")
                        .setContentText("Activity "+"nome"+" sta per partire")
                        .setPriority(NotificationCompat.PRIORITY_HIGH);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(supervisor.getNotificationsN(), builder.build());



    }




}
