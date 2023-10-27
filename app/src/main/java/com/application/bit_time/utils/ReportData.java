package com.application.bit_time.utils;

import com.application.bit_time.utils.RunningActivityData;

public class ReportData {

    String subtaskName;
    RunningActivityData.Status endStatus;

    public ReportData(String subtaskName, RunningActivityData.Status currentStatus)
    {
        this.subtaskName = subtaskName;
        this.endStatus = currentStatus;
    }



}
