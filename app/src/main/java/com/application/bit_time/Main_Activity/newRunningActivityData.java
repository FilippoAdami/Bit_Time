package com.application.bit_time.Main_Activity;

import androidx.annotation.NonNull;

import com.application.bit_time.utils.ReportData;
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

    public class UpdatePackage
    {
        private int lastedTime;
        private EndStatus endStatus;

        UpdatePackage(int lastedTime,EndStatus endStatus)
        {
            this.lastedTime = lastedTime;
            this.endStatus = endStatus;
        }

       public int getLastedTime()
       {
           return this.lastedTime;
       }

       public EndStatus getEndStatus()
       {
           return this.endStatus;
       }


    }

    TaskItem currentTask;
    Status status;
    EndStatus endStatus;
    int lastedTime;
    private String activityName;
    String activityIconPath;
    private List<ReportData> fullReport;

    public newRunningActivityData()
    {
        this.currentTask = null;
        this.lastedTime = -1;
        this.endStatus = EndStatus.notSet;
        this.status= Status.notSet;
        this.fullReport = null;
        this.activityName = null;
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
        float MAXTIME = currentTask.getDurationInt();

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
    public void setFullReport(List<ReportData> reportList)
    {
        this.fullReport=new ArrayList<>(reportList);

    }
    public boolean isExpired()
    {
        if(this.endStatus.equals(EndStatus.Expired))
            return true;

        return false;
    }
    public void setAsExpired()
    {
        this.status= Status.End;
        this.endStatus = EndStatus.Expired;
        this.lastedTime= this.currentTask.getDurationInt();
    }
    public UpdatePackage getUpdatePackage()
    {
        return new UpdatePackage(this.lastedTime,this.endStatus);
    }
    public ReportData getReportData()
    {
        ReportData reportData = new ReportData(currentTask.getID(),currentTask.getName(),this.endStatus,this.lastedTime,currentTask.getDurationInt(), currentTask.getImg());

        return reportData;
    }
    public void setAsTerminated(int lastedTime)
    {
        this.status=Status.End;
        this.lastedTime=lastedTime;
        setLastedTime(lastedTime);
    }

    public List<TaskItem> getSubtasksData()
    {
        List<TaskItem> subtasksList = new ArrayList<>();

        for(ReportData RD : this.fullReport)
        {
// should add the image as well at the end, temporary constructor call
            subtasksList.add(new TaskItem(RD.subtaskId,RD.subtaskName,RD.getTotalTime(), ""));
        }

        return subtasksList;
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
