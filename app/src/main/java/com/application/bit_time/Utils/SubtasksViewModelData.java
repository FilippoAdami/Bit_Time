package com.application.bit_time.Utils;

import androidx.annotation.NonNull;

import com.application.bit_time.Utils.Db.DbContract;

public class SubtasksViewModelData {


    public TaskItem[] subtasks;
    public SubtaskAdapter subtaskAdapter;


    public SubtasksViewModelData()
    {
        subtasks = new TaskItem[DbContract.Activities.DIM_MAX];

      for(int i = 0; i< DbContract.Activities.DIM_MAX ; i++)
      {
          subtasks[i] = new TaskItem();
          //Log.i("new subtask",subtasks[i].toString());
      }

      subtaskAdapter = null;
    }


    public SubtasksViewModelData(TaskItem[] subtasks, SubtaskAdapter subtadapt)
    {
        this();

        if(subtasks.length > 0) {
            for (int i = 0; i < subtasks.length; i++) {
                this.subtasks[i] = new TaskItem(subtasks[i]);
            }
        }

        this.subtaskAdapter = subtadapt;

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
