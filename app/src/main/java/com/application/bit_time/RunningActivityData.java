package com.application.bit_time;

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
        OnTime,
        LittleDelay,
        BigDelay
    }
    private Choice choice;
    private Status status;

    private TaskItem currentTask;

    public RunningActivityData()
    {
        this.choice = Choice.NoChoice;
        this.status = Status.NoStatus;
        this.currentTask = null;


    }

    public RunningActivityData(Status currentStatus, Choice currentChoice)
    {
        this.choice = currentChoice;
        this.status = currentStatus;
        this.currentTask = null;
    }

    public  RunningActivityData(TaskItem currentTask)
    {
        this.choice = Choice.NoChoice;
        this.status = Status.NoStatus;
        this.currentTask = currentTask;
    }


    public void setChoice(Choice currentChoice)
    {
        this.choice = currentChoice;
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





}
