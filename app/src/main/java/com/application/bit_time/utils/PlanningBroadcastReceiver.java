package com.application.bit_time.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class PlanningBroadcastReceiver extends BroadcastReceiver {

    BroadcastReceiver timeBrRec;
    IntentFilter timeIntentFilter;
    Context appContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("brRec log",intent.getAction().toString());


        /*this.appContext = context.getApplicationContext();

        Log.i("brRec log","timeBrRec on creation");
        this.timeBrRec = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
             Log.i("brRec log","log from timeBrRec");
            }
        };

        buildIntentFilter();

        ContextCompat.registerReceiver(
                this.appContext,
                this.timeBrRec,this.timeIntentFilter,
                ContextCompat.RECEIVER_EXPORTED);*/



    }





    private IntentFilter buildIntentFilter()
    {
        this.timeIntentFilter = new IntentFilter();

        this.timeIntentFilter.addAction("android.intent.action.TIME_TICK");

        return this.timeIntentFilter;
    }
}
