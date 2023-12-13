package com.application.bit_time.Main_Activity;

import androidx.annotation.NonNull;

import com.application.bit_time.utils.ReportData;
import com.application.bit_time.utils.RunningActivityData;
import com.application.bit_time.utils.TaskItem;

import java.util.ArrayList;
import java.util.List;

public class newRunningActivityData {



    static final int BIGANTICIPATION = 70;
    static final int ANTICIPATION = 90;

    static final int ONTIME= 110;

    static final int LITTLEDELAY =150 ;

    static final int BIGDELAY = 200;

    public enum Status
    {
        notSet,
        Uploaded,
        Running,
        OnWait,
        End,
        ActivityDone
    }

    public enum EndStatus
    {
        notSet,
        BigAnticipation,
        Anticipation,
        OnTime,
        Delay,
        Expired
    }

    TaskItem currentTask;
    Status status;
    EndStatus endStatus;
    int lastedTime;
    private List<ReportData> fullReport;

    public newRunningActivityData()
    {
        this.currentTask = null;
        this.lastedTime = -1;
        this.endStatus = EndStatus.notSet;
        this.status= Status.notSet;
        this.fullReport = null;
    }

    public newRunningActivityData(TaskItem currentTask)
    {
        this();
        this.currentTask = new TaskItem(currentTask);
        this.lastedTime=-1;
        this.status = Status.Uploaded;
    }

    public newRunningActivityData(TaskItem currentTask,Status currentStatus)
    {
        this(currentTask);
        this.status=currentStatus;
    }

    public newRunningActivityData(Status currentStatus,List<ReportData> reportData)
    {
        this();
        this.status = currentStatus;
        this.fullReport = new ArrayList<>(reportData);
    }

    public List<ReportData> getFullReport()
    {
        return this.fullReport;
    }

    public newRunningActivityData(TaskItem currentTask, int lastedTime)
    {
        this(currentTask);
        this.lastedTime = lastedTime;
        setLastedTime(this.lastedTime);
    }


    public void setLastedTime(int lastedTime)
    {
        int MAXTIME = currentTask.getDurationInt();

        this.lastedTime=lastedTime;

        if(this.lastedTime < 70*MAXTIME/100)
        {
            this.endStatus = EndStatus.BigAnticipation;
        }
        else if(this.lastedTime < 90*MAXTIME/100)
        {
            this.endStatus = EndStatus.Anticipation;
        }
        else if(this.lastedTime <110*MAXTIME/100)
        {
            this.endStatus = EndStatus.OnTime;
        }
        else
        {
            this.endStatus = EndStatus.Delay;
        }
    }


    public void setFullReport(List<ReportData> reportData)
    {
        this.fullReport=new ArrayList<>(reportData);
    }

    public void setAsExpired()
    {
        this.status= Status.End;
        this.endStatus = EndStatus.Expired;
        this.lastedTime= this.currentTask.getDurationInt();
    }
    public ReportData getReportData()
    {
        ReportData reportData = new ReportData(currentTask.getName(),this.endStatus,this.lastedTime);

        return reportData;
    }

    public void setAsTerminated(int lastedTime)
    {
        this.status=Status.End;
        this.lastedTime=lastedTime;
        setLastedTime(lastedTime);
    }

    @NonNull
    @Override
    public String toString() {
        return this.currentTask.getName() + " " + this.status.toString();
    }

    public String fullToString()
    {
        return this.currentTask.getName() +
                " " + this.status.toString() +
                " " + this.endStatus.toString();
    }
}
