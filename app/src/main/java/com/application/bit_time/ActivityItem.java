package com.application.bit_time;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;

public class ActivityItem {

    ActivityInfo activityInfo;
    TaskItem[] subtasks;


    public ActivityItem()
    {
        this.activityInfo = new ActivityInfo();

        subtasks = new TaskItem[DbContract.Activities.DIM_MAX];
    }

    public ActivityItem(ActivityItem original)
    {
        this.activityInfo = new ActivityInfo(original.activityInfo);

        subtasks = new TaskItem[DbContract.Activities.DIM_MAX];
    }

    public ActivityItem(String id,String name,String duration)
    {
        this.activityInfo = new ActivityInfo(id,name,duration);
        subtasks = new TaskItem[DbContract.Activities.DIM_MAX];
    }

    public ActivityItem(String name, int duration, TaskItem[] subtasks)
    {

        Log.i("SUB2ADD",subtasks[0].toString());

        this.activityInfo = new ActivityInfo(-1,name,duration);
        this.subtasks =  new TaskItem[DbContract.Activities.DIM_MAX];

        for(int i = 0; i< DbContract.Activities.DIM_MAX;i++)
        {
            if(i< subtasks.length)
            {
                this.subtasks[i] = new TaskItem(subtasks[i]);
            }
            else
            {
                this.subtasks[i] =  new TaskItem();
            }
        }
    }


  public String getName()
  {
      return this.activityInfo.getName();
  }

  public String getTime()
  {
      return this.activityInfo.getTime();
  }

    @NonNull
    @Override
    public String toString() {
        String info = this.activityInfo.toString();

        String subtasksInfo = "";


        /*for(TaskItem ti : this.subtask)
        {
            subtasksInfo.concat(" "+ti.toString());
        }*/


        return info;//+ " "+ subtasksInfo;
    }


    //@Override
    /*public boolean equals(@Nullable Object obj) {

        if(obj instanceof ActivityItem)
        {
            ActivityItem objAI = (ActivityItem) obj;
            if(
                    //this.activityInfo.equals(objAI.activityInfo));


        }

        return false;



    }*/
}
