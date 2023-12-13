package com.application.bit_time.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.bit_time.Main_Activity.newRunningActivityData;

public class RunningActivityViewModel extends ViewModel {

    private final MutableLiveData<newRunningActivityData> selectedItem = new MutableLiveData(new newRunningActivityData());

    public void selectItem(newRunningActivityData item)
    {
        selectedItem.setValue(item);
    }

    public LiveData<newRunningActivityData> getSelectedItem()
    {
        return selectedItem;
    }



}
