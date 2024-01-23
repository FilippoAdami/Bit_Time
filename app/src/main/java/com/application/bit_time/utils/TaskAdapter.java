package com.application.bit_time.utils;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

    private Context context;


    public TaskAdapter(SettingsLowerFragmentTasks settingsLowerFragmentTasks, List<TaskItem> taskList, Context context)
    {
        this.settingsLowerFragmentTasks = settingsLowerFragmentTasks;
        this.taskList = taskList;
        this.context = context;
        dbViewModel = new ViewModelProvider(settingsLowerFragmentTasks.requireActivity()).get(DbViewModel.class);
        viewModel = new ViewModelProvider(settingsLowerFragmentTasks.requireActivity()).get(CustomViewModel.class);
        //this.subtasksViewModel = new ViewModelProvider(settingsLowerFragmentTasks.requireActivity()).get(SubtasksViewModel.class);
    }


    @NonNull
    @Override
    public TaskAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_task_item_fragment_layout,parent,false);

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

        Bitmap originalDeleteBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.delete);
        Bitmap bitmapDeleteIcon = Bitmap.createScaledBitmap(originalDeleteBitmap,100,100,true);
        holder.deleteButton.setImageBitmap(bitmapDeleteIcon);

        Bitmap originalEditBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.edit);
        Bitmap bitmapEditIcon = Bitmap.createScaledBitmap(originalEditBitmap,100,100,true);
        holder.modifyButton.setImageBitmap(bitmapEditIcon);
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
        ImageButton modifyButton;
        //Button modifyButton;
        ImageButton deleteButton;
        //Button deleteButton;
        //immagine boh;

        LinearLayout holderLayout;





        public ListItemHolder(View view)
        {
            super(view);
            labelName = view.findViewById(R.id.labelTask);
            labelDuration = view.findViewById(R.id.labelDuration);
            modifyButton = view.findViewById(R.id.modifyTaskIButton);
            deleteButton = view.findViewById(R.id.deleteTaskIButton);
            holderLayout = view.findViewById(R.id.itemHolderLayout);


            //immagine boh


            modifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(), "modify pressed",Toast.LENGTH_SHORT).show();



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
