package com.application.bit_time;

public class ReportData {

    String subtaskName;
    RunningActivityData.Status endStatus;

    public ReportData(String subtaskName, RunningActivityData.Status currentStatus)
    {
        this.subtaskName = subtaskName;
        this.endStatus = currentStatus;
    }



}
