package com.application.bit_time.Utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SubtasksViewModel extends ViewModel {

    private final MutableLiveData<SubtasksViewModelData> selectedItem = new MutableLiveData(new SubtasksViewModelData());

    public void selectItem(SubtasksViewModelData item)
    {
        selectedItem.setValue(item);
    }

    public LiveData<SubtasksViewModelData> getSelectedItem() {
        return selectedItem;
    }

    @NonNull
    @Override
    public String toString() {

        return "data is : "+selectedItem.getValue().toString();
    }
}
