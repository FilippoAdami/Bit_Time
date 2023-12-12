package com.application.bit_time.utils;

import androidx.annotation.NonNull;

import com.application.bit_time.utils.RunningActivityData;

public class ReportData {

    String subtaskName;
    RunningActivityData.Status endStatus;

    int lastedTime ;

    public ReportData(String subtaskName, RunningActivityData.Status currentStatus, int lastedTime)
    {
        this.subtaskName = subtaskName;
        this.endStatus = currentStatus;
        this.lastedTime = lastedTime;
    }

    @NonNull
    @Override
    public String toString() {
        return this.subtaskName + " " + this.endStatus.toString();
    }
}
