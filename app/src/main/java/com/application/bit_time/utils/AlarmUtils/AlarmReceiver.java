package com.application.bit_time.utils.AlarmUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.application.bit_time.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("ALARMRECEIVER","log from me");

        String channelId = "17";
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.happy_dog) // TODO : happy_dog is just a placeholder, change it before the delivery
                        .setContentTitle("test notification")
                        .setContentText("test notification text")
                        .setPriority(NotificationCompat.PRIORITY_HIGH);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


        notificationManager.notify(17, builder.build());
    }
}
