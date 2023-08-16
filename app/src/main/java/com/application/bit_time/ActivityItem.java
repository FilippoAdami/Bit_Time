package com.application.bit_time;

import android.util.Log;

import androidx.annotation.NonNull;

public class ActivityItem {

    ActivityInfo activityInfo;
    TaskItem[] subtask;


    public ActivityItem()
    {
        this.activityInfo = new ActivityInfo();

       // this.subtask = new TaskItem[DbContract.Activities.DIM_MAX];

        /*for(TaskItem ti : this.subtask )
        {
            ti = new TaskItem();
            Log.i("ACTITEM CONSTR",ti.toString());
        }*/
    }

    public ActivityItem(ActivityItem original)
    {
        this.activityInfo = new ActivityInfo(original.activityInfo);

        /*for(int  i =0 ; i< DbContract.Activities.DIM_MAX ; i++)
        {
            this.subtask[i] = new TaskItem(original.subtask[i]);
        }*/
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
}
