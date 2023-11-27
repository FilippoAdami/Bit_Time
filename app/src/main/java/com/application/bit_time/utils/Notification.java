package com.application.bit_time.utils;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.application.bit_time.R;
import com.application.bit_time.utils.NotificationsUtils.NotificationsSupervisor;

public class Notification {


    public Notification(Context context, String channelId, NotificationsSupervisor supervisor) {


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.happy_dog) // TODO : happy_dog is just a placeholder, change it before the delivery
                        .setContentTitle("test notification")
                        .setContentText("test notification text")
                        .setPriority(NotificationCompat.PRIORITY_HIGH);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


        //notificationManager.notify(supervisor.incrementNotificationsN(), builder.build());



    }




}
