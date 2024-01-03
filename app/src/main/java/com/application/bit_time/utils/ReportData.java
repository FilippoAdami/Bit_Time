package com.application.bit_time.utils;

import androidx.annotation.NonNull;

import com.application.bit_time.Main_Activity.newRunningActivityData;
import com.application.bit_time.utils.RunningActivityData;

public class ReportData {

    public int subtaskId;
    public String subtaskName;
    public newRunningActivityData.EndStatus endStatus;
    public int lastedTime ;



    public ReportData(int subtaskId,String subtaskName, newRunningActivityData.EndStatus currentStatus, int lastedTime)
    {
        this.subtaskId = subtaskId;
        this.subtaskName = subtaskName;
        this.endStatus = currentStatus;
        this.lastedTime = lastedTime;
    }

    @NonNull
    @Override
    public String toString() {
        return this.subtaskName + " " + this.endStatus.toString() + " "+ lastedTime;
    }


    public String getMetadata()
    {

        return "-"+subtaskId+"-"+subtaskName ;
    }
}
