package com.application.bit_time.Settings_Activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SettingsModeData {

    public enum Mode
    {
        Tasks,
        Activities,
        NewTask,
        NewActivity,
        ModifyTask,
        ModifyActivity,
        MainEntry
    }

    private Mode currentMode;

    public SettingsModeData()
    {
        currentMode = Mode.Activities;
    }

    public SettingsModeData(Mode mode)
    {
        currentMode = mode;
    }

    public void setModeData(Mode newMode)
    {
        this.currentMode = newMode;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof String)
        {
            String toCompare = (String) obj;

            if(this.currentMode.toString().equals(toCompare))
            {
                return true;
            }

        }

        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return this.currentMode.toString();
    }
}
