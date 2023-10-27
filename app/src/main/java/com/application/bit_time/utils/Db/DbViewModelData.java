package com.application.bit_time.utils.Db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.bit_time.utils.ActivityInfo;
import com.application.bit_time.utils.ActivityItem;
import com.application.bit_time.utils.TaskItem;

public class DbViewModelData {

    public TaskItem taskToDelete;
    public TaskItem taskToAdd;
    public TaskItem taskToModify;
    public ActivityInfo activityToDelete;
    public ActivityItem activityToAdd;
    public ActivityInfo activityToModify;

    public DbViewModelData()
    {
        this.taskToDelete = new TaskItem(-1," ",-1);
        this.taskToAdd = new TaskItem(-1," ",-1);
        this.taskToModify = new TaskItem(-1,"",-1);
        this.activityToAdd =  new ActivityItem();
        this.activityToDelete = new ActivityInfo(-1," ",-1);
        this.activityToModify = new ActivityInfo(-1,"",-1);

        Log.i("NEW VMDATA",taskToDelete+" "+taskToAdd+" "+activityToDelete+" "+activityToAdd);


    }


    public DbViewModelData(DbViewModelData original)
    {
        this.taskToDelete = new TaskItem(original.taskToDelete);
        this.taskToAdd = new TaskItem(original.taskToAdd);
        this.taskToModify = new TaskItem(original.taskToModify);
        this.activityToAdd = new ActivityItem(original.activityToAdd);
        this.activityToDelete = new ActivityInfo(original.activityToDelete);
        this.activityToModify = new ActivityInfo(original.activityToModify);

    }

    @Override
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


    }


    @NonNull
    @Override
    public String toString() {

        String taskToDeleteStr = taskToDelete.toStringShrt();
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
                + activityToDeleteStr;


        return res;
    }





}
