package com.application.bit_time.utils;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.bit_time.Settings_Activity.SettingsModeData;

public class CustomViewModel extends ViewModel {

    private final MutableLiveData<SettingsModeData> selectedItem = new MutableLiveData(new SettingsModeData());

    public void selectItem(SettingsModeData item)
    {
        selectedItem.setValue(item);
    }

    public LiveData<SettingsModeData> getSelectedItem()
    {
        return selectedItem;
    }



}
