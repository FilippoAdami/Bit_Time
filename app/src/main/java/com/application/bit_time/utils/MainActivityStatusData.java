package com.application.bit_time.utils;

public class MainActivityStatusData {


    public enum Status
    {
        Idle,
        QuickstartMenu,
        RunningActivity
    }


    private Status currentStatus;

   public MainActivityStatusData()
   {
       this.currentStatus = Status.Idle;
   }
    public void setCurrentStatus(Status currentStatus) {
        this.currentStatus = currentStatus;
    }


    public Status getCurrentStatus() {
        return currentStatus;
    }
}
