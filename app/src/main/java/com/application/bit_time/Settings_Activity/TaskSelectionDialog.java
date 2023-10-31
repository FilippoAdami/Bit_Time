package com.application.bit_time.Settings_Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;

import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.utils.SubtaskAdapter;
import com.application.bit_time.utils.SubtasksViewModel;
import com.application.bit_time.utils.SubtasksViewModelData;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.TasksDiffCallback;
import com.application.bit_time.utils.Db.DbContract;
import com.application.bit_time.utils.Db.DbManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskSelectionDialog extends DialogFragment {

    CustomViewModel viewModel;
    DbManager dbManager;
    Cursor tasksCursor;
    ArrayList<TaskItem> selectedTasks;
    ArrayList<TaskItem> oldSelectedTasks;
    SubtasksViewModel subtasksViewModel;
    SubtaskAdapter subtaskAdapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);
        dbManager = new DbManager(getContext());
        tasksCursor= dbManager.selectAllTasks();
        selectedTasks = new ArrayList<>();
        subtasksViewModel = new ViewModelProvider(requireActivity()).get("subTasksVM",SubtasksViewModel.class);
        subtaskAdapter = subtasksViewModel.getSelectedItem().getValue().subtaskAdapter;
        oldSelectedTasks = new ArrayList<>(Arrays.asList(subtasksViewModel.getSelectedItem().getValue().subtasks));


        Log.i("subtAdapt Dialog is",""+subtaskAdapter.toString());

        Log.i("testfromdialog",subtasksViewModel.toString());


        //Toast.makeText(getContext(),dbManager.getDbName(),Toast.LENGTH_SHORT).show();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Log.i("from dialog vm",this.viewModel.getSelectedItem().getValue().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        Log.i("TASKSCURSOR log",""+tasksCursor.getCount());


        if(this.viewModel.getSelectedItem().getValue().toString().equals("NewActivity")) {

            builder.setTitle("Choose tasks to add to activity")
                    .setMultiChoiceItems(tasksCursor, DbContract.Tasks.COLUMN_NAME_TASK_DURATION, DbContract.Tasks.COLUMN_NAME_TASK_NAME, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {

                            if (isChecked) {

                                if (selectedTasks.size() <= DbContract.Activities.DIM_MAX) {
                                    selectedTasks.add(new TaskItem(tasksCursor.getInt(0), tasksCursor.getString(1), tasksCursor.getString(2)));
                                    Toast.makeText(getContext(), "added " + tasksCursor.getString(1), Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getContext(), "DIM MAX reached", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(getContext(), "UNselected" + i, Toast.LENGTH_SHORT).show();
                                selectedTasks.remove(new TaskItem(tasksCursor.getInt(0), tasksCursor.getString(1), tasksCursor.getString(2)));


                            }

                        }
                    })
                    .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.i("SELECTED TASKS", "pressed positive");

                            TaskItem[] selectedTasksA = new TaskItem[selectedTasks.size()];
                            TaskItem[] oldSelectedTasksA = new TaskItem[oldSelectedTasks.size()];

                            selectedTasksA = selectedTasks.toArray(selectedTasksA);
                            //oldSelectedTasksA = oldSelectedTasks.toArray(oldSelectedTasks);

                            subtasksViewModel.selectItem(new SubtasksViewModelData(selectedTasksA, subtaskAdapter));

                            for (TaskItem ti : selectedTasksA)
                                Log.i("SELTASK_A", ti.toString());

                            Log.i("DIALOG", "subtasks submitted");

                            //subtaskAdapter.notifyDataSetChanged();

                            final TasksDiffCallback diffCallback = new TasksDiffCallback(oldSelectedTasks, selectedTasks);
                            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

                            diffResult.dispatchUpdatesTo(subtaskAdapter);
                        }
                    });



        }
        else if(this.viewModel.getSelectedItem().getValue().toString().equals("ModifyActivity"))
        {


            Set<String> stringSet = new HashSet<>();



            TaskItem[] subTasks = new TaskItem[tasksCursor.getCount()];
            boolean[] checkedItems = new boolean[tasksCursor.getCount()];

            Log.i("activityId",Integer.toString(subtasksViewModel.getSelectedItem().getValue().getActivityId()));
            Cursor activityCursor = dbManager.searchActivityById(subtasksViewModel.getSelectedItem().getValue().getActivityId());

            activityCursor.moveToFirst();



           for(int i = 0;i<DbContract.Activities.DIM_MAX;i++)
           {
               int currentTaskId = activityCursor.getInt(3+i);
               if(currentTaskId>0) {
                   TaskItem currentElem = dbManager.searchTask(currentTaskId);
                   if (stringSet.add(currentElem.getName())) {
                       Log.i(currentElem.toString(), "added to set");
                       checkedItems[i] = true;
                   }
               }

           }

           tasksCursor.moveToFirst();

           String[] subtasksStrings= new String[stringSet.size()];
           int i = 0;
           for(String elem : stringSet)
           {

               subtasksStrings[i] = elem;
               Log.i("fromSet",subtasksStrings[i]);
               i++;
           }



            builder.setTitle("Modify the tasks of the activity")
                    .setMultiChoiceItems(subtasksStrings, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                        }
                    });

        }

        return builder.create();
    }





}

