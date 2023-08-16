package com.application.bit_time;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DbViewModelData {

    TaskItem taskToDelete;
    TaskItem taskToAdd;
    ActivityInfo activityToDelete;
    ActivityItem activityToAdd;

    public DbViewModelData()
    {
        this.taskToDelete = new TaskItem(" ",-1);
        this.taskToAdd = new TaskItem(" ",-1);
        this.activityToAdd =  new ActivityItem();
        this.activityToDelete = new ActivityInfo(" ",-1);

        Log.i("NEW VMDATA",taskToDelete+" "+taskToAdd+" "+activityToDelete+" "+activityToAdd);


    }


    public DbViewModelData(DbViewModelData original)
    {
        this.taskToDelete = new TaskItem(original.taskToDelete);
        this.taskToAdd = new TaskItem(original.taskToAdd);
        this.activityToAdd = new ActivityItem(original.activityToAdd);
        this.activityToDelete = new ActivityInfo(original.activityToDelete);
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

        String taskToDeleteStr = taskToDelete.toString();
        String taskToAddStr = taskToAdd.toString();
        String activityToAddStr = activityToAdd.toString();
        String activityToDeleteStr = activityToDelete.toString();

        String res =
                taskToDeleteStr + " "
                + taskToAddStr + " "
                + activityToDeleteStr + " "
                + activityToAddStr;

        return res;
    }





}
