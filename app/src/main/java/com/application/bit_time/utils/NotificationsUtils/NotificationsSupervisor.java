package com.application.bit_time.utils.NotificationsUtils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.application.bit_time.utils.Notification;

public class NotificationsSupervisor {

    Context applicationContext;
    static int notificationsN;


    public NotificationsSupervisor(Context context)
    {
       this.applicationContext = context;
       //createChannel();
        this.notificationsN=0;
       Log.i("NotificationSupinit","supervisor created and context saved");
    }


    public void createChannel(String channelId)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence channelName = "BitTimeNotificationChannel";
            String description = "NotificationChannel dedicated to BitTime's app notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel = new NotificationChannel(channelId,channelName,importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) this.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            Log.i("NotificationSupervisor","new channel created");
        }
        else
        {
            Log.i("NotificationSupervisor","No need to create channels for this android version");
        }


    }

    public void createNotification()
    {
        String channelId = "tobedefined"; //TODO: handle this
        this.notificationsN+=1;
        Notification notification = new Notification(this.applicationContext,channelId,this);

    }


    public int getNotificationsN()
    {
        return this.notificationsN;
    }

}
