package com.application.bit_time.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.bit_time.utils.AlarmUtils.AlarmInfo;

public class PlannerViewModel extends ViewModel {



    private final MutableLiveData<PlannerViewModelData> selectedItem = new MutableLiveData(new PlannerViewModelData() );

    public void addPlanToSchedule(PlanningInfo piToAdd)
    {
        this.selectedItem.getValue().addPlan(piToAdd);
        //this.selectedItem.getValue().setLatestPlan(new PlanningInfo());
        selectItem(this.selectedItem.getValue());
    }

    public void clearSelectedItem()
    {
        this.selectedItem.setValue(new PlannerViewModelData());
        selectItem(this.selectedItem.getValue());
    }
    public void selectItem(PlannerViewModelData item)
    {
        selectedItem.setValue(item);
    }

    public LiveData<PlannerViewModelData> getSelectedItem()
    {
        return selectedItem;
    }






}
