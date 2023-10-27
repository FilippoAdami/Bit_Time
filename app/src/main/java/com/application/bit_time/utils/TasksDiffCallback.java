package com.application.bit_time.utils;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class TasksDiffCallback extends DiffUtil.Callback {

    List<TaskItem> oldTasksList;
    List<TaskItem> newTasksList;

    public TasksDiffCallback(List<TaskItem> oldTasksList, List<TaskItem> newTasksList)
    {
        this.oldTasksList = oldTasksList;
        this.newTasksList = newTasksList;
    }



    @Override
    public int getOldListSize() {
        return this.oldTasksList.size();
    }

    @Override
    public int getNewListSize() {
        return this.newTasksList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return this.oldTasksList.get(oldItemPosition).getID() == this.newTasksList.get(newItemPosition).getID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return
                this.oldTasksList.get(oldItemPosition).getName().equals(this.newTasksList.get(newItemPosition).getName())
                && this.oldTasksList.get(oldItemPosition).getDurationInt() == this.newTasksList.get(newItemPosition).getDurationInt();
    }
}
