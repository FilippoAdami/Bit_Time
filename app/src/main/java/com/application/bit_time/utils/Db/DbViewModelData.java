package com.application.bit_time.utils.Db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.bit_time.utils.ActivityInfo;
import com.application.bit_time.utils.ActivityItem;
import com.application.bit_time.utils.TaskItem;




public class DbViewModelData {


    /*public TaskItem taskToDelete;
    public TaskItem taskToAdd;
    public TaskItem taskToModify;
    public ActivityInfo activityToDelete;
    public ActivityItem activityToAdd;
    public ActivityInfo activityToModify;*/

    public enum ITEM_TYPE
    {
        UNDEFINED,
        ACTIVITY,
        TASK
    };

    public enum ACTION_TYPE
    {
        UNDEFINED,
        INSERT,
        MODIFY,
        DELETE
    };

    public ACTION_TYPE action;
    public ITEM_TYPE selector;
    public ActivityItem activityItem;
    public TaskItem taskItem;


    public DbViewModelData()
    {
        this.action = ACTION_TYPE.UNDEFINED;
        this.selector = ITEM_TYPE.UNDEFINED;
        this.activityItem = null;
        this.taskItem = null;
    }

    /*public DbViewModelData()
    {
        this.taskToDelete = new TaskItem(-1," ",-1);
        this.taskToAdd = new TaskItem(-1," ",-1);
        this.taskToModify = new TaskItem(-1,"",-1);
        this.activityToAdd =  new ActivityItem();
        this.activityToDelete = new ActivityInfo(-1," ",-1);
        this.activityToModify = new ActivityInfo(-1,"",-1);

        Log.i("NEW VMDATA",taskToDelete+" "+taskToAdd+" "+activityToDelete+" "+activityToAdd);


    }*/


    public DbViewModelData(ACTION_TYPE action, ITEM_TYPE item, TaskItem newItem)
    {
        this();
        this.action = action;
        this.selector = item;
        Log.i("from DbVmD",newItem.getImageUri().toString());
        this.taskItem = new TaskItem(newItem);
    }

    public DbViewModelData(ACTION_TYPE action, ITEM_TYPE item, ActivityItem newItem)
    {
        this();
        this.action = action;
        this.selector = item;
        this.activityItem = new ActivityItem(newItem);
    }



    public DbViewModelData(DbViewModelData original)
    {
        /*this.taskToDelete = new TaskItem(original.taskToDelete);
        this.taskToAdd = new TaskItem(original.taskToAdd);
        this.taskToModify = new TaskItem(original.taskToModify);
        this.activityToAdd = new ActivityItem(original.activityToAdd);
        this.activityToDelete = new ActivityInfo(original.activityToDelete);
        this.activityToModify = new ActivityInfo(original.activityToModify);*/

        this.action = original.action;
        this.selector = original.selector;
        this.taskItem = original.taskItem;
        this.activityItem = original.activityItem;

    }

    /*@Override
    public boolean equals(@Nullable Object obj) {

        if(obj instanceof DbViewModelData)
        {
            DbViewModelData objData = (DbViewModelData) obj;


            if(
                    this.activityToDelete.equals(objData.activityToDelete)
                    && this.activityToAdd.equals(objData.activityToAdd)
                    && this.taskToAdd.equals(objData.taskToAdd)
                    && this.taskToDelete.equals(objData.taskToDelete)
                    && this.activityToModify.equals(objData.activityToModify)


            )
            {
                return true;
            }
        }

        return false;


    }*/


    @NonNull
    @Override
    public String toString() {

        /*String taskToDeleteStr = taskToDelete.toStringShrt();
        String taskToAddStr = taskToAdd.toStringShrt();
        String activityToAddStr = activityToAdd.toString();
        String activityToDeleteStr = activityToDelete.toString();
        String activityToModifyStr = activityToModify.toString();
        String taskToModifyStr = taskToModify.toStringShrt();

        String res =
                taskToAddStr + " "
                + taskToModifyStr + " "
                + taskToDeleteStr + " "
                + activityToAddStr + " "
                + activityToModifyStr +" "
                + activityToDeleteStr;*/

        String res = "act : "+this.action + " on "+ this.selector.toString();


        return res;
    }





}
