package com.application.bit_time.utils;

import androidx.annotation.NonNull;

public class MainActivityStatusData {


    public enum Status
    {
        Idle,
        QuickstartMenu,
        RunningActivity,
        CaregiverLogin,
        GameArea
    }

    public enum BackField
    {
        NotBack,
        Save,
        Ignore
    }

    private Status currentStatus;

    private BackField backField;
   public MainActivityStatusData()
   {
       this.currentStatus = Status.QuickstartMenu;
       this.backField = BackField.NotBack;
   }

   public MainActivityStatusData(Status newStatus)
   {
       this.currentStatus = newStatus;
   }
    public void setCurrentStatus(Status currentStatus) {
        this.currentStatus = currentStatus;
    }

    public boolean isBack()
    {
        return !this.backField.equals(BackField.NotBack);
    }

    public void setBackField(BackField backFieldToSet)
    {
        this.backField = backFieldToSet;
    }

    public BackField getBackField()
    {
        return this.backField;
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    @NonNull
    @Override
    public String toString() {
        return this.currentStatus.toString() + " "+ this.backField.toString();
    }
}
