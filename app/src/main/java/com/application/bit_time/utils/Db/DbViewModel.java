package com.application.bit_time.utils.Db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DbViewModel extends ViewModel {

    private final MutableLiveData<DbViewModelData> selectedItem = new MutableLiveData(new DbViewModelData());

    public void selectItem(DbViewModelData item)
    {
        selectedItem.setValue(item);
    }

    public LiveData<DbViewModelData> getSelectedItem()
    {
        return selectedItem;
    }


}
