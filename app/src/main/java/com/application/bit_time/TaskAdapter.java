package com.application.bit_time;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ListItemHolder> {

    private List<TaskItem> taskList;
    private SettingsLowerFragment settingsLowerFragment;


    public TaskAdapter(SettingsLowerFragment settingsLowerFragment, List<TaskItem> taskList)
    {
        this.settingsLowerFragment = settingsLowerFragment;
        this.taskList = taskList;
    }


    @NonNull
    @Override
    public TaskAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item_fragment_layout,parent,false);

        return new ListItemHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ListItemHolder holder, int position) {

        TaskItem task = taskList.get(position);

        holder.labelName.setText(task.getName());
        holder.labelDuration.setText(task.getDuration());
        //immagine boh


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView labelName;
        TextView labelDuration;
        //immagine boh;


        public ListItemHolder(View view)
        {
            super(view);
            labelName = view.findViewById(R.id.labelTask);
            labelDuration = view.findViewById(R.id.labelDuration);
            //immagine boh


            view.setClickable(true);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(settingsLowerFragment.getContext(),"item pressed",Toast.LENGTH_SHORT).show();
        }
    }
}
