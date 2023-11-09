package com.application.bit_time.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.bit_time.TimeHelper;

public class TaskItem {


    private int IDpk;
    private String Name;
    private int Duration;
    //immagine boh


    public TaskItem()
    {
        this.IDpk = -1;
        this.Name = "emptyTask";
        this.Duration = 0;

        //Log.i("TASKITEMCONST",this.toString());
    }

    public TaskItem(int id,String name, int duration)
    {
        this.IDpk = id;
        this.Name = name;
        this.Duration = duration;
    }

    public TaskItem(TaskItem original)
    {
        this.IDpk = original.IDpk;
        this.Name = new String(original.getName());
        this.Duration = original.getDurationInt();
    }

    public TaskItem(int id,String name,String duration)
    {
        this.IDpk = id;
        this.Name = name;

        try {
            this.Duration = Integer.parseInt(duration);
        }catch(NumberFormatException ex)
        {
            this.Duration = -1 ;
            Log.e("ERROR","exception thrown when converting time for TaskItem obj");
        }

    }

    public String getName()
    {
        return Name;
    }

    public String getDuration()
    {
        return Integer.toString(Duration);
    }

    public int getDurationInt()
    {
        return Duration;
    }

    public void setName(String name)
    {
        this.Name = name;
    }

    public void setDuration(String duration)
    {
        this.Duration= Integer.parseInt(duration);
    }

    public int getID()
    {
        return this.IDpk;
    }

    public String getIdStr()
    {
        return Integer.toString(this.IDpk);
    }


    @Override
    public boolean equals(@Nullable Object obj) {

        if(obj instanceof TaskItem)
        {

            TaskItem objData = (TaskItem) obj;

            if(this.IDpk == objData.IDpk)
                return true;
        }

        return false;


    }


    @NonNull
    @Override
    public String toString() {

        return getIdStr()+" "+getName() + " " + getDuration();


    }




    public boolean isEqualToEmpty()
    {
        TaskItem emptyTask = new TaskItem();

        if(
                this.IDpk == emptyTask.IDpk &&
                this.Name.equals(emptyTask.getName()) &&
                this.Duration == emptyTask.Duration
        )
            return true;
        else return false;
    }


    public TimeHelper getFormattedDuration()
    {
        int min = this.Duration%60;
        int hrs = this.Duration - min;

        return new TimeHelper(hrs,min,0);




    }


    public String toStringShrt()
    {
        return getIdStr();
    }
}
