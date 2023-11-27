package com.application.bit_time.utils.AlarmUtils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

public class AlarmApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if(getApplicationContext() == null)
            Log.i("alarmApp","null context");
        else
            Log.i("alarmApp","not null context");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "alarm_id";
            String channelName = "alarm_name";
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel =
                    new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        AlarmScheduler alarmScheduler = new AlarmScheduler(this);
        alarmScheduler.schedule(new AlarmInfo());

    }
}
