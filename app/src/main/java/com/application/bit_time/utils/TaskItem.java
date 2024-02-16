package com.application.bit_time.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;

public class TaskItem {


    private int IDpk;
    private String Name;
    private int Duration;
// img added; updated all the constructors to take also the image
    private String img;

    public TaskItem()
    {
        this.IDpk = -1;
        this.Name = "emptyTask";
        this.Duration = 0;
        this.img = "empty";

        //Log.i("TASKITEMCONST",this.toString());
    }
    public TaskItem(int id,String name, int duration, String img)
    {
        this.IDpk = id;
        this.Name = name;
        this.Duration = duration;
        this.img = img;
    }
    public TaskItem(int id,String name,String duration, String img)
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
        this.img = img;
    }
    public TaskItem(TaskItem original)
    {
        this.IDpk = original.IDpk;
        this.Name = new String(original.getName());
        this.Duration = original.getDurationInt();
        this.img = new String(original.img);
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
// function to get and set the image
    public String getImg()
    {
        return img;
    }
    public void setImg(String img)
    {
        this.img = img;
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
    public String getFormattedDuration()
    {

         String res =getTimeHelper().toStringShrt();
         Log.i("reslog",res);
         return res;

    }
    public TimeHelper getTimeHelper()
    {
        return new TimeHelper(this.Duration);
    }
    public String toStringShrt()
    {
        return getIdStr();
    }
}
