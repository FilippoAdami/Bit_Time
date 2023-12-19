package com.application.bit_time.utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.R;
import com.application.bit_time.Settings_Activity.SettingsLowerFragmentTasks;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.Db.DbViewModelData;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ListItemHolder> {

    private DbViewModel dbViewModel;
    private CustomViewModel viewModel;
    private SubtasksViewModel subtasksViewModel;
    private List<TaskItem> taskList;
    private SettingsLowerFragmentTasks settingsLowerFragmentTasks;



    public TaskAdapter(SettingsLowerFragmentTasks settingsLowerFragmentTasks, List<TaskItem> taskList)
    {
        this.settingsLowerFragmentTasks = settingsLowerFragmentTasks;
        this.taskList = taskList;
        dbViewModel = new ViewModelProvider(settingsLowerFragmentTasks.requireActivity()).get(DbViewModel.class);
        viewModel = new ViewModelProvider(settingsLowerFragmentTasks.requireActivity()).get(CustomViewModel.class);
        //this.subtasksViewModel = new ViewModelProvider(settingsLowerFragmentTasks.requireActivity()).get(SubtasksViewModel.class);
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
        holder.labelDuration.setText(task.getFormattedDuration());
        holder.id = task.getID();
        holder.duration = task.getDurationInt();
        Log.i("TaskAdapter idact",Integer.toString(holder.id));
        //immagine boh


    }

    public void updateTasksListItems(List<TaskItem> tasks) {

        Log.i("TASKADAPTER","updateTaskListItems called");
        final TasksDiffCallback diffCallback = new TasksDiffCallback(this.taskList, tasks);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.taskList.clear();
        this.taskList.addAll(tasks);
        diffResult.dispatchUpdatesTo(this);

    }




        @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int id;
        int duration;
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



                    //DbViewModelData newData = dbViewModel.getSelectedItem().getValue();
                    //newData.taskToModify = new TaskItem(id,labelName.getText().toString(),labelDuration.getText().toString());
                    TaskItem thisTaskItem = new TaskItem(id,labelName.getText().toString(), duration);

                    DbViewModelData newData = new DbViewModelData(
                            DbViewModelData.ACTION_TYPE.UNDEFINED,
                            DbViewModelData.ITEM_TYPE.UNDEFINED,
                            thisTaskItem);

                    //Log.i("TASKAD newData ttm",newData.taskItem.toString());
                    dbViewModel.selectItem(newData);

                    viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.ModifyTask));

                    //SubtasksViewModelData updatedSVMData = subtasksViewModel.getSelectedItem().getValue();
                    //updatedSVMData.setActivityId(id);
                    //subtasksViewModel.selectItem(updatedSVMData);

                }
            });


            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("TASKAD btn" ,"delete pressed");

                    //DbViewModelData dbData = dbViewModel.getSelectedItem().getValue();
                    //dbData.taskToDelete = new TaskItem(id,labelName.getText().toString(),labelDuration.getText().toString());

                    TaskItem currentTask = new TaskItem(id,labelName.getText().toString(),labelDuration.getText().toString());

                    dbViewModel.selectItem(new DbViewModelData(
                            DbViewModelData.ACTION_TYPE.DELETE,
                            DbViewModelData.ITEM_TYPE.TASK,
                            currentTask));

                    //dbViewModel.selectItem(dbData);

                    List<TaskItem> newTasksList = new ArrayList<>(taskList);
                    //newTasksList.remove(dbData.taskToDelete);
                    newTasksList.remove(currentTask);

                    for(TaskItem ti : taskList)
                    {
                        Log.i("currentTL",ti.toString());
                    }

                    for(TaskItem ti : newTasksList)
                    {
                        Log.i("newTL",ti.toString());
                    }

                    updateTasksListItems(newTasksList);





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
