package com.application.bit_time.utils;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SettingsModeData {

    public enum Mode
    {
        EntryPoint,
        Tasks,
        Activities,
        NewTask,
        NewActivity,
        ModifyTask,
        ModifyActivity,
        MainEntry,
        BackToTasks,
        BackToActivities
    }

    private Mode currentMode;

    public SettingsModeData()
    {
        currentMode = Mode.MainEntry;
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
