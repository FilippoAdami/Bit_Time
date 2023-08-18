package com.application.bit_time;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
