package com.application.bit_time.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<MainActivityStatusData> selectedItem = new MutableLiveData(new MainActivityStatusData());

    public void selectItem(MainActivityStatusData item)
    {
        selectedItem.setValue(item);
    }

    public LiveData<MainActivityStatusData> getSelectedItem()
    {
        return selectedItem;
    }



}
