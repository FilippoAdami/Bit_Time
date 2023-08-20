package com.application.bit_time;

import android.util.Log;

import androidx.annotation.NonNull;

public class SubtasksViewModelData {


    TaskItem[] subtasks;


    public SubtasksViewModelData()
    {
        subtasks = new TaskItem[DbContract.Activities.DIM_MAX];

      for(int i = 0; i< DbContract.Activities.DIM_MAX ; i++)
      {
          subtasks[i] = new TaskItem();
          //Log.i("new subtask",subtasks[i].toString());
      }
    }


    public SubtasksViewModelData(TaskItem[] subtasks)
    {
        this();

        if(subtasks.length > 0) {
            for (int i = 0; i < subtasks.length; i++) {
                this.subtasks[i] = new TaskItem(subtasks[i]);
            }
        }

    }


    @NonNull
    @Override
    public String toString() {
        String str = "";

        for(TaskItem ti : subtasks)
        {
            str.concat(" "+ti.getName());
        }

        return str;
    }
}
