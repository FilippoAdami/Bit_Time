package com.application.bit_time.utils;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.application.bit_time.utils.ActivityInfo;
import com.application.bit_time.utils.ActivityItem;

import java.util.ArrayList;
import java.util.List;

public class ActivityDiffCallback extends DiffUtil.Callback {

    private final List<ActivityItem> newActivityItemsList;
    private final List<ActivityItem> oldActivityItemList;

    ActivityDiffCallback(List<ActivityItem> oldActivityItemsList,List<ActivityItem> newActivityItemsList)
    {
        this.oldActivityItemList = new ArrayList<>(oldActivityItemsList);
        this.newActivityItemsList = new ArrayList<>(newActivityItemsList);
    }

    /*@Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }*/

    @Override
    public int getOldListSize() {
        return this.oldActivityItemList.size();
    }

    @Override
    public int getNewListSize() {
        return this.newActivityItemsList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

        ActivityInfo oldItemInfo = this.oldActivityItemList.get(oldItemPosition).getInfo();
        ActivityInfo newItemInfo = this.newActivityItemsList.get(newItemPosition).getInfo();

        if(newItemInfo.equals(oldItemInfo))
        {
            Log.i("TRUE",newItemInfo.toString()+" "+oldItemInfo.toString());
            return true;
        }
        else
        {
            Log.i("FALSE",newItemInfo.toString()+" "+oldItemInfo.toString());
            return false;

        }
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        ActivityItem oldItem = this.oldActivityItemList.get(oldItemPosition);
        ActivityItem newItem = this.newActivityItemsList.get(newItemPosition);

        return newItem.equals(oldItem);
    }
}
