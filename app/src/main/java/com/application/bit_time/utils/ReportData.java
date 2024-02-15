package com.application.bit_time.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.application.bit_time.Main_Activity.newRunningActivityData;

public class ReportData {

    public int subtaskId;
    public String subtaskName;
    public newRunningActivityData.EndStatus endStatus;
    private int totalTime;
    public int lastedTime ;

    public static class Metadata
    {
        int actId;
        int pos;
        int subtaskId;
        String subtaskName;

        public Metadata(int subtaskId,String subtaskName)
        {
            this.actId=-1;
            this.pos=-1;
            this.subtaskId=subtaskId;
            this.subtaskName=subtaskName;
        }

        public void updateMetadata(int actId,int pos)
        {
            this.actId=actId;
            this.pos=pos;
        }

        public String getSubtaskInfotoStr()
        {
            return this.subtaskId+"-"+this.subtaskName;
        }

        @NonNull
        @Override
        public String toString() {
            return "actID:"+this.actId+",pos:"+this.pos+",subId:"+this.subtaskId+",subName:"+this.subtaskName;
        }
    }

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

    public ReportData updateReportData(newRunningActivityData.UpdatePackage updatePackage)
    {
        this.endStatus =updatePackage.getEndStatus();
        this.lastedTime = updatePackage.getLastedTime();

        return this;
    }
    public int getTotalTime()
    {
        return this.totalTime;
    }

    public Metadata getMetadata()
    {
        return new Metadata(subtaskId,subtaskName) ;
    }


    public TaskItem getTaskItem()
    {
        return new TaskItem(this.subtaskId,this.subtaskName,this.getTotalTime());
    }

    public static void metadataParser(String rawMetadata)
    {
        Log.i("index","of "+rawMetadata.indexOf("-"));
    }

    public String lastedtimeToString()
    {
        int lastedTimeCopy = this.lastedTime;

        int h = lastedTimeCopy/3600;
        int min = (lastedTimeCopy-h)/60;
        int sec = (lastedTimeCopy-h-min);


        return  h+" h "+min+" min "+sec+" sec ";
    }
}
