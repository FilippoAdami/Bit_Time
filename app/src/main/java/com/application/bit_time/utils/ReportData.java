package com.application.bit_time.utils;

import androidx.annotation.NonNull;

import com.application.bit_time.Main_Activity.newRunningActivityData;
import com.application.bit_time.utils.RunningActivityData;

public class ReportData {

    public String subtaskName;
    public newRunningActivityData.EndStatus endStatus;
    public int lastedTime ;



    public ReportData(String subtaskName, newRunningActivityData.EndStatus currentStatus, int lastedTime)
    {
        this.subtaskName = subtaskName;
        this.endStatus = currentStatus;
        this.lastedTime = lastedTime;
    }

    @NonNull
    @Override
    public String toString() {
        return this.subtaskName + " " + this.endStatus.toString() + " "+ lastedTime;
    }
}
