package com.application.bit_time.utils;


import android.content.Context;
import android.util.Log;

import com.application.bit_time.utils.AlarmUtils.AlarmInfo;

import java.util.ArrayList;
import java.util.List;

public class PlannerViewModelData {


    private List<PlanningInfo> plans;
    private PlanningInfo latestPlan;



    public PlannerViewModelData()
    {
        this.plans = new ArrayList<>();
        this.latestPlan = new PlanningInfo();
        Log.i("PVMData constructor",""+this.latestPlan.info.toString());

    }

    public PlannerViewModelData(AlarmInfo info)
    {
        this();
        this.latestPlan.setInfo(info);
    }

    public void addPlan(PlanningInfo PIToAdd)
    {
        this.plans.add(PIToAdd);
        this.latestPlan = new PlanningInfo();
    }

    public void setLatestPlan(PlanningInfo latestPlan)
    {
        this.latestPlan = new PlanningInfo(latestPlan);
    }
    public int countPlans()
    {
        return this.plans.size();
    }

    public PlanningInfo getLatestPlan()
    {
        return this.latestPlan;
    }

    public List<PlanningInfo> getPlans()
    {
        return this.plans;
    }



}
