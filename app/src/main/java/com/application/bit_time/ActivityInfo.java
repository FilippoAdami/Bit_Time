package com.application.bit_time;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ActivityInfo {


    private String labelName;
    private int labelTime;


    public ActivityInfo()
    {
        this.labelName = new String("emptyActivityInfoName");
        this.labelTime = -1;

    }

    public ActivityInfo(String name, int time)
    {
        labelName = name;
        labelTime = time;

    }

    public ActivityInfo(ActivityInfo original)
    {
        this.labelTime = original.getTimeInt();
        this.labelName = new String(original.getName());

    }


    public ActivityInfo(String name, String time)
    {
        labelName = name;
        try {
            labelTime = Integer.parseInt(time);
        }catch(NumberFormatException ex)
        {
            labelTime = -1 ;
            Log.e("ERROR","exception thrown when converting time for listItem obj");
        }
    }

    public String getName()
    {
        return this.labelName;
    }

    public void setName(String labelName)
    {
        this.labelName = labelName;
    }

    public String getTime()
    {
        return Integer.toString(this.labelTime);
    }

    public int getTimeInt() { return this.labelTime;}


    public void setTime(int labelTime)
    {
        this.labelTime = labelTime;
    }


    @Override
    public boolean equals(@Nullable Object obj) {

        if(obj instanceof ActivityInfo)
        {
            ActivityInfo objLI = (ActivityInfo) obj;


            if(this.labelName.equals(objLI.getName()) && this.labelTime == objLI.getTimeInt())
                return true;

        }

        return false;


    }

    @NonNull
    @Override
    public String toString() {
        return getName() + " " + getTime();
    }
}
