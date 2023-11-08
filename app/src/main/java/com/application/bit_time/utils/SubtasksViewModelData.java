package com.application.bit_time.utils;

import androidx.annotation.NonNull;

import com.application.bit_time.utils.Db.DbContract;

public class SubtasksViewModelData {


    public TaskItem[] subtasks;
    public SubtaskAdapter subtaskAdapter;
    private boolean alreadyModified;

    private int activityId;

    public SubtasksViewModelData()
    {
        subtasks = new TaskItem[DbContract.Activities.DIM_MAX];

      for(int i = 0; i< DbContract.Activities.DIM_MAX ; i++)
      {
          subtasks[i] = new TaskItem();
          //Log.i("new subtask",subtasks[i].toString());
      }

      subtaskAdapter = null;
      activityId = -1;
      alreadyModified=false;
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

    public void setActivityId(int id)
    {
        this.activityId=id;
    }

    public int getActivityId()
    {
        return this.activityId;
    }

    public boolean isAlreadyModified()
    {
        return this.alreadyModified;
    }

    public void hasBeenModified()
    {
        this.alreadyModified = true;
    }

    public void setSubtasks(TaskItem[] taskItems)
    {
        int pos=0;
        for(TaskItem ti : taskItems)
        {
            this.subtasks[pos]=new TaskItem(ti);
            pos++;
        }
    }

    public void hasNotBeenModified()
    {
        this.alreadyModified=false;
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
