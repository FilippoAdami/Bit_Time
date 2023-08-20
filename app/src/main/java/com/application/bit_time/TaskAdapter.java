package com.application.bit_time;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ListItemHolder> {

    private DbViewModel dbViewModel;
    private CustomViewModel viewModel;

    private List<TaskItem> taskList;
    private SettingsLowerFragmentTasks settingsLowerFragmentTasks;



    public TaskAdapter(SettingsLowerFragmentTasks settingsLowerFragmentTasks, List<TaskItem> taskList)
    {
        this.settingsLowerFragmentTasks = settingsLowerFragmentTasks;
        this.taskList = taskList;
        dbViewModel = new ViewModelProvider(settingsLowerFragmentTasks.requireActivity()).get(DbViewModel.class);
        viewModel = new ViewModelProvider(settingsLowerFragmentTasks.requireActivity()).get(CustomViewModel.class);
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
        holder.id = task.getID();
        //immagine boh


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int id;
        TextView labelName;
        TextView labelDuration;

        Button modifyButton;
        Button deleteButton;
        //immagine boh;


        public ListItemHolder(View view)
        {
            super(view);
            labelName = view.findViewById(R.id.labelTask);
            labelDuration = view.findViewById(R.id.labelDuration);
            modifyButton = view.findViewById(R.id.modifyTaskButton);
            deleteButton = view.findViewById(R.id.deleteTaskButton);


            //immagine boh


            modifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "modify pressed",Toast.LENGTH_SHORT).show();


                    DbViewModelData newData = dbViewModel.getSelectedItem().getValue();
                    newData.taskToModify = new TaskItem(id,labelName.getText().toString(),labelDuration.getText().toString());
                    Log.i("TASKAD newData ttm",newData.taskToModify.toString());
                    dbViewModel.selectItem(newData);

                    viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.ModifyTask));



                }
            });


            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("TASKAD btn" ,"delete pressed");

                    DbViewModelData dbData = dbViewModel.getSelectedItem().getValue();
                    dbData.taskToDelete = new TaskItem(id,labelName.getText().toString(),labelDuration.getText().toString());
                    dbViewModel.selectItem(dbData);


                }
            });



            view.setClickable(true);
            view.setOnClickListener(this);




        }

        @Override
        public void onClick(View view) {
            Toast.makeText(settingsLowerFragmentTasks.getContext(),"item pressed",Toast.LENGTH_SHORT).show();
        }
    }
}
