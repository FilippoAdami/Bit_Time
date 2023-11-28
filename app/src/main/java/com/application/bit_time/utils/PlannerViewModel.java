package com.application.bit_time.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.bit_time.utils.AlarmUtils.AlarmInfo;

public class PlannerViewModel extends ViewModel {



    private final MutableLiveData<AlarmInfo> selectedItem = new MutableLiveData(new AlarmInfo());

    public void selectItem(AlarmInfo item)
    {
        selectedItem.setValue(item);
    }

    public LiveData<AlarmInfo> getSelectedItem()
    {
        return selectedItem;
    }






}
