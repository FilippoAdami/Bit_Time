package com.application.bit_time.Utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RunningActivityViewModel extends ViewModel {

    private final MutableLiveData<RunningActivityData> selectedItem = new MutableLiveData(new RunningActivityData());

    public void selectItem(RunningActivityData item)
    {
        selectedItem.setValue(item);
    }

    public LiveData<RunningActivityData> getSelectedItem()
    {
        return selectedItem;
    }



}
