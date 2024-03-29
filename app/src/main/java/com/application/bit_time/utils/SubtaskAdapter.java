package com.application.bit_time.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.R;
import com.application.bit_time.Settings_Activity.ActivityCreationFragment;

import java.util.List;

public class SubtaskAdapter extends RecyclerView.Adapter<SubtaskAdapter.ListItemHolder>
{
    public List<TaskItem> subtasks;
    public ActivityCreationFragment activityCreationFragment;

    public SubtaskAdapter(ActivityCreationFragment activityCreationFragment, List<TaskItem> subtasks)
    {
        this.activityCreationFragment = activityCreationFragment;
        this.subtasks = subtasks;
    }

    @NonNull
    @Override
    public SubtaskAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemVIew = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subtask_item_fragment_layout,parent,false);

        return new ListItemHolder(itemVIew);
    }

    @Override
    public void onBindViewHolder(@NonNull SubtaskAdapter.ListItemHolder holder, int position) {

        TaskItem ti = subtasks.get(position);
        holder.subtaskNameLabel.setText(ti.getName());
        holder.subtaskTimeLabel.setText(ti.getFormattedDuration());

    }

    @Override
    public int getItemCount() {
        return subtasks.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder
    {
        TextView subtaskNameLabel;
        TextView subtaskTimeLabel;

        public ListItemHolder(View view)
        {
            super(view);

            subtaskNameLabel = view.findViewById(R.id.subtaskNameLabel);
            subtaskTimeLabel = view.findViewById(R.id.subtaskTimeLabel);
        }


    }
}
