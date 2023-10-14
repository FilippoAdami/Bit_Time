package com.application.bit_time.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.application.bit_time.Utils.Db.DbContract;


public class ActivityItem {

    public ActivityInfo activityInfo;
    public TaskItem[] subtasks;

    boolean expanded;


    public ActivityItem()
    {
        this.activityInfo = new ActivityInfo();

        subtasks = new TaskItem[DbContract.Activities.DIM_MAX];
        expanded = false;
    }

    public ActivityItem(ActivityItem original)
    {
        this.activityInfo = new ActivityInfo(original.activityInfo);

        subtasks = new TaskItem[DbContract.Activities.DIM_MAX];
        expanded = false;
    }

    public ActivityItem(String id,String name,String duration)
    {
        this.activityInfo = new ActivityInfo(id,name,duration);
        expanded = false;
        subtasks = new TaskItem[DbContract.Activities.DIM_MAX];


    }


    public ActivityItem(String id, String name, String duration, int[] subtasksA)
    {
        this(id,name,duration);

        for(int i = 0;i< DbContract.Activities.DIM_MAX ;i++)
        {
            subtasks[i] = new TaskItem(subtasksA[i],"placeholderName",0);
        }
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

  public boolean isExpanded()
  {
      return this.expanded;
  }

  public void setExpanded(boolean expandedState)
  {
      this.expanded = expandedState;
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
