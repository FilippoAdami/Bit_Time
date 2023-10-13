package com.application.bit_time;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TasksViewModel extends ViewModel {

    private final MutableLiveData<TasksViewModelData> selectedItem = new MutableLiveData<TasksViewModelData>(new TasksViewModelData());

    public  void selectItem(TasksViewModelData item){ selectedItem.setValue(item);}

    public LiveData<TasksViewModelData> getSelectedItem() { return selectedItem;}





}
