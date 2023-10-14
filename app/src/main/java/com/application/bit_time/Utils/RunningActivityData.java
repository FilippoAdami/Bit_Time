package com.application.bit_time.Utils;

import androidx.annotation.NonNull;

import com.application.bit_time.Main_Activity.ReportData;

import java.util.List;

public class RunningActivityData {


    public enum Choice
    {
        NoChoice,
        Yes,
        No
    }

    public enum Status
    {
        NoStatus,
        Uploaded,
        Running,
        OnWait,
        OnTime,
        LittleDelay,
        BigDelay,
        Expired,
        ActivityDone
    }
    private Choice choice;
    private Status status;
    private TaskItem currentTask;

    private List<ReportData> reportDataList;

    public RunningActivityData()
    {
        this.choice = Choice.NoChoice;
        this.status = Status.NoStatus;
        this.currentTask = new TaskItem(-1,"testTask",10); // will be null
        reportDataList = null;
    }

    public RunningActivityData(Status currentStatus, Choice currentChoice , TaskItem currentTask)
    {
        this(currentStatus,currentChoice);
        this.currentTask = currentTask;
        reportDataList = null;
    }
    public RunningActivityData(Status currentStatus, Choice currentChoice)
    {
        this.choice = currentChoice;
        this.status = currentStatus;
        this.currentTask = null;
        reportDataList = null;
    }

    public  RunningActivityData(TaskItem currentTask)
    {
        this.choice = Choice.NoChoice;
        this.status = Status.NoStatus;
        this.currentTask = currentTask;
        reportDataList = null;
    }

    public RunningActivityData(Status currentStatus, TaskItem currentTask)
    {
        this.status = currentStatus;
        this.choice = Choice.NoChoice;
        this.currentTask = currentTask;
        reportDataList = null;
    }


    public RunningActivityData(Status currentStatus, List<ReportData> dataList)
    {
        this.status = currentStatus;
        this.reportDataList = dataList;
    }

    public void setChoice(Choice currentChoice)
    {
        this.choice = currentChoice;
        this.status = Status.OnWait;
    }

    public RunningActivityData(Status currentStatus)
    {
        this.status = currentStatus;
        this.choice = Choice.Yes;
        this.currentTask = null;
    }


    public void setStatus(Status currentStatus)
    {
        this.status = currentStatus;
    }

    public Choice getChoice()
    {
        return this.choice;
    }

    public Status getStatus()
    {
        return this.status;
    }


    public List<ReportData> getReportDataList()
    {
        return this.reportDataList;
    }
    public boolean isFilled()
    {
        if(this.choice.equals(Choice.NoChoice) || this.status.equals(Status.NoStatus))
        {
            return false;
        }
        return true;
    }

    public void setCurrentTask(TaskItem currentTask)
    {
        this.currentTask = currentTask;
    }

    public TaskItem getCurrentTask()
    {
        return this.currentTask;
    }

    @NonNull
    @Override
    public String toString() {

        String print=
                this.currentTask.getName() + " " +
                this.choice.toString() + " " +
                this.status.toString();

        return print;


    }
}
