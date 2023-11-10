package com.application.bit_time.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ActivityInfo {


    private int IDpk;
    private String labelName;
    private int labelTime;


    public ActivityInfo()
    {
        this.IDpk = -1;
        this.labelName = new String("emptyActivityInfoName");
        this.labelTime = -1;

    }

    public ActivityInfo(int ID,String name, int time)
    {
        IDpk = ID;
        labelName = name;
        labelTime = time;

    }



    public ActivityInfo(ActivityInfo original)
    {
        this.IDpk = original.IDpk;
        this.labelTime = original.getTimeInt();
        this.labelName = new String(original.getName());

    }


    public ActivityInfo(int id,String name, String time)
    {
        this.IDpk = id;
        this.labelName = name;

        try {
            labelTime = Integer.parseInt(time);
        }catch(NumberFormatException ex)
        {
            labelTime = -1 ;
            Log.e("ERROR","exception thrown when converting time for listItem obj");
        }

    }

    public ActivityInfo(String ID,String name, String time)
    {
        try{
            IDpk = Integer.parseInt(ID);
        }catch(NumberFormatException ex)
        {
            labelTime = -1 ;
            Log.e("ERROR","exception thrown when converting ID for listItem obj");
        }

        labelName = name;
        try {
            labelTime = Integer.parseInt(time);
        }catch(NumberFormatException ex)
        {
            labelTime = -1 ;
            Log.e("ERROR","exception thrown when converting time for listItem obj");
        }


        Log.i("ACTINFO CRE completed",this.toString());
    }

    public String getName()
    {
        return this.labelName;
    }

    public void setName(String labelName)
    {
        this.labelName = labelName;
    }

    public String getIDpk()
    {
        return Integer.toString(IDpk);
    }

    public String getTime()
    {
        return Integer.toString(this.labelTime);
    }


    public int getTimeInt() { return this.labelTime;}

    public int getIdInt() {return this.IDpk;}


    public void setTime(int labelTime)
    {
        this.labelTime = labelTime;
    }


    @Override
    public boolean equals(@Nullable Object obj) {

        if(obj instanceof ActivityInfo)
        {
            ActivityInfo objLI = (ActivityInfo) obj;

        /*    if(this.labelName.equals(objLI.getName()) && this.labelTime == objLI.getTimeInt())*/
                if(this.IDpk == objLI.getIdInt()) {
                    return true;
                }
        }

        return false;

    }

    public void setId(int id)
    {
        this.IDpk = id;
    }

    @NonNull
    @Override
    public String toString() {
        return getIDpk()+ "" + getName() + " " + getTime();
    }
}
