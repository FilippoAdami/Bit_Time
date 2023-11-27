package com.application.bit_time.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.bit_time.utils.Db.DbContract;

import java.util.Arrays;
import java.util.Iterator;


public class ActivityItem {

    ActivityInfo activityInfo;
    TaskItem[] subtasks;
    PlanningInfo planningInfo;

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

        if(original.subtasks[0]!=null)
        {
            //Iterator<TaskItem> tiIterator = Arrays.stream(original.getSubtasks()).iterator();

            this.subtasks = new TaskItem[original.getSubtasks().length];

            this.subtasks= original.getSubtasks();

            for(int i = 0;i<DbContract.Activities.DIM_MAX;i++) {
                //this.subtasks[i]= new TaskItem(tiIterator.next());
            }

        }else
        {
            for(int i = 0;i<DbContract.Activities.DIM_MAX;i++) {
                this.subtasks[i]= new TaskItem();
            }
        }
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

        //this.subtasks= new TaskItem[DbContract.Activities.DIM_MAX];
        for(int i = 0;i< DbContract.Activities.DIM_MAX ;i++)
        {
            subtasks[i] = new TaskItem(subtasksA[i],"placeholderName",0);
            Log.i("creatingAI",subtasks[i].toString());
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

    public ActivityItem(ActivityInfo info, TaskItem[] subtasks)
    {
        this.activityInfo = info ;

        this.subtasks = new TaskItem[subtasks.length];

        int i =0;
        for(TaskItem subItem : subtasks)
        {
            this.subtasks[i]= new TaskItem(subItem);
            i++;
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

  public ActivityInfo getInfo()
  {
      return this.activityInfo;
  }
  public TaskItem[] getSubtasks(){ return this.subtasks;}
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

public void setId(int id)
{
    this.activityInfo.setId(id);
}
    //@Override
    public boolean equals(@Nullable Object obj)
    {

        if(obj instanceof ActivityItem)
        {
            ActivityItem objAI = (ActivityItem) obj;

            if
            (
                    this.activityInfo.getIdInt() == objAI.activityInfo.getIdInt() &&
                    this.activityInfo.getName().equals(objAI.getName()) &&
                    this.activityInfo.getTimeInt() == objAI.activityInfo.getTimeInt()
            )
            {
                return true;
            }
        }

        return false;
    }

    public void setPlanningInfo(PlanningInfo info)
    {
        this.planningInfo = new PlanningInfo(info);
    }

}
