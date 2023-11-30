package com.application.bit_time.utils;

import androidx.annotation.NonNull;

import com.application.bit_time.utils.AlarmUtils.AlarmInfo;

import java.util.GregorianCalendar;

public class PlanningInfo {

    boolean planned;
    AlarmInfo info;


    public PlanningInfo()
    {
        planned= false;
        //info = null;
        this.info = new AlarmInfo();
    }

    public PlanningInfo(AlarmInfo info)
    {
        this();
        this.info = new AlarmInfo(info);
    }
    public PlanningInfo(PlanningInfo info)
    {
        this.planned = info.isPlanned();
        this.info = new AlarmInfo(info.getInfo());
    }


    public void setInfo(AlarmInfo info)
    {
        this.info = new AlarmInfo(info);
        this.planned = true;
    }

    public boolean isPlanned()
    {
        return this.planned;
    }

    public AlarmInfo getInfo()
    {
        return this.info;
    }

    @NonNull
    @Override
    public String toString() {

        return this.info.toString();
    }
}
