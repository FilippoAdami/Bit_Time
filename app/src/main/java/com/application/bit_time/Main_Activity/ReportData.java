package com.application.bit_time.Main_Activity;

import com.application.bit_time.Utils.RunningActivityData;

public class ReportData {

    String subtaskName;
    RunningActivityData.Status endStatus;

    public ReportData(String subtaskName, RunningActivityData.Status currentStatus)
    {
        this.subtaskName = subtaskName;
        this.endStatus = currentStatus;
    }



}
