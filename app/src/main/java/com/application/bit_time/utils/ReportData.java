package com.application.bit_time.utils;

import androidx.annotation.NonNull;

import com.application.bit_time.Main_Activity.newRunningActivityData;
import com.application.bit_time.utils.RunningActivityData;

public class ReportData {

    public int subtaskId;
    public String subtaskName;
    public newRunningActivityData.EndStatus endStatus;
    private int totalTime;
    public int lastedTime ;


    public ReportData(int subtaskId,String subtaskName,int totalTime)
    {
        this.subtaskId = subtaskId;
        this.subtaskName = subtaskName;
        this.totalTime = totalTime;
        this.endStatus = newRunningActivityData.EndStatus.notSet;
        this.lastedTime = -100; // stands for not set yet
    }
    public ReportData(int subtaskId,String subtaskName, newRunningActivityData.EndStatus currentStatus, int lastedTime,int totalTime)
    {
        this.subtaskId = subtaskId;
        this.subtaskName = subtaskName;
        this.endStatus = currentStatus;
        this.lastedTime = lastedTime;
        this.totalTime = totalTime;
    }

    @NonNull
    @Override
    public String toString() {
        return this.subtaskName + " " + this.endStatus.toString() + " "+ lastedTime;
    }

    public ReportData updateReportData(newRunningActivityData.EndStatus endStatus,int lastedTime)
    {
        this.endStatus =endStatus;
        this.lastedTime = lastedTime;

        return this;
    }
    public int getTotalTime()
    {
        return this.totalTime;
    }

    public String getMetadata()
    {

        return "-"+subtaskId+"-"+subtaskName ;
    }


    public TaskItem getTaskItem()
    {
        return new TaskItem(this.subtaskId,this.subtaskName,this.getTotalTime());
    }
}
