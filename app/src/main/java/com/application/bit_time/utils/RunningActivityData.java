package com.application.bit_time.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.application.bit_time.utils.Db.DbContract;

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


    private int[] points;
    private Choice choice;
    private Status status;
    private TaskItem currentTask;
    private int currentLastedTime;

    private List<ReportData> reportDataList;

    public RunningActivityData()
    {
        this.choice = Choice.NoChoice;
        this.status = Status.NoStatus;
        this.currentTask = new TaskItem(-1,"testTask",10); // will be null
        reportDataList = null;
        this.currentLastedTime = 0;
        this.points = new int[DbContract.timescores];

    }

    public RunningActivityData(Status currentStatus, Choice currentChoice , TaskItem currentTask)
    {
        this(currentStatus,currentChoice);
        this.currentTask = currentTask;
        reportDataList = null;
    }
    public RunningActivityData(Status currentStatus, Choice currentChoice)
    {
        this.points = new int[DbContract.timescores];
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
        this.points = new int[DbContract.timescores];
        this.status = currentStatus;
        this.choice = Choice.NoChoice;
        this.currentTask = currentTask;
        reportDataList = null;
    }


    public RunningActivityData(Status currentStatus, List<ReportData> dataList)
    {
        this.points = new int[DbContract.timescores];
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

    public void setPoints(int[] points)
    {
        for(int i =0; i<DbContract.timescores ; i++)
        {
            this.points[i]=points[i];
            Log.i("point to save",Integer.toString(this.points[i]));
        }
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
        Log.i("inside isfilled","choice "+this.choice.toString() + " status "+this.status.toString());
        if(this.choice.equals(Choice.NoChoice) || this.choice.equals(Choice.No) || this.status.equals(Status.NoStatus))
        {
            Log.i("inside isfilled","will return false");
            return false;
        }


        Log.i("inside isfilled","will return true");
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

    public void setCurrentLastedTime(int currentLastedTime)
    {
        this.currentLastedTime = currentLastedTime;
    }

    public int getCurrentLastedTime()
    {
        return this.currentLastedTime;
    }
    @NonNull
    @Override
    public String toString() {

        String print=
                this.currentTask.getName() + " " +
                this.choice.toString() + " " +
                this.status.toString()+ " " +
                this.getCurrentLastedTime();

        return print;


    }
}
