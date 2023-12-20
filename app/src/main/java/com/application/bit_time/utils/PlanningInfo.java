package com.application.bit_time.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.bit_time.utils.AlarmUtils.AlarmInfo;

import java.util.GregorianCalendar;

public class PlanningInfo {

    boolean planned;
    int planId;
    AlarmInfo info;


    public PlanningInfo()
    {
        planId=-1;
        planned= false;
        //info = null;
        this.info = new AlarmInfo();
    }


    public PlanningInfo(AlarmInfo info)
    {
        this();
        this.info = new AlarmInfo(info);
    }

    public PlanningInfo(int planId,AlarmInfo info)
    {
        this(info);
        this.planId = planId;
    }

    public PlanningInfo(PlanningInfo info)
    {
        this();
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

        return this.planned + " "+this.info.toString();
    }

    public int getPlanId()
    {
        return this.planId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if(obj instanceof PlanningInfo)
        {
            PlanningInfo objPI = (PlanningInfo) obj;
            Log.i("objtoStr" , objPI.toString() + objPI.planId);

            Log.i("equals log",this.planId + " =?=" + objPI.getPlanId());
            if(this.planId == objPI.getPlanId())
                return true;
        }

        return false;
    }
}
