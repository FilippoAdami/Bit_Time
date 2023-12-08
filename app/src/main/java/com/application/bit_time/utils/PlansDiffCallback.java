package com.application.bit_time.utils;

import android.util.Log;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

public class PlansDiffCallback extends DiffUtil.Callback
{

    private final List<PlanningInfo> oldPlans;
    private final List<PlanningInfo> newPlans;


    public PlansDiffCallback(List<PlanningInfo> oldPlans, List<PlanningInfo> newPlans)
    {
        this.oldPlans = new ArrayList<>(oldPlans);
        this.newPlans = new ArrayList<>(newPlans);

    }
    @Override
    public int getOldListSize() {
        return this.oldPlans.size();
    }

    @Override
    public int getNewListSize() {
        return this.newPlans.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

        int newId =  this.newPlans.get(newItemPosition).getPlanId();
        int oldId = this.oldPlans.get(oldItemPosition).getPlanId();


        Log.i("check if",""+ newId +"="+oldId);

        if( newId == oldId )
        {
            return true;
        }

        return false;


    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }
}
