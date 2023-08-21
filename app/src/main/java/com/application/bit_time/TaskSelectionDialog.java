package com.application.bit_time;

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

import java.util.ArrayList;
import java.util.Arrays;

public class TaskSelectionDialog extends DialogFragment {


    DbManager dbManager;
    Cursor tasksCursor;
    ArrayList<TaskItem> selectedTasks;
    ArrayList<TaskItem> oldSelectedTasks;
    SubtasksViewModel subtasksViewModel;
    SubtaskAdapter subtaskAdapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        Log.i("TASKSCURSOR log",""+tasksCursor.getCount());



        builder.setTitle("Choose tasks to add to activity")
                .setMultiChoiceItems(tasksCursor,DbContract.Tasks.COLUMN_NAME_TASK_DURATION , DbContract.Tasks.COLUMN_NAME_TASK_NAME, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {

                        if(isChecked)
                        {

                            if(selectedTasks.size() <= DbContract.Activities.DIM_MAX)
                            {
                                selectedTasks.add(new TaskItem(tasksCursor.getInt(0),tasksCursor.getString(1),tasksCursor.getString(2)));
                                Toast.makeText(getContext(),"added "+tasksCursor.getString(1),Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getContext(),"DIM MAX reached",Toast.LENGTH_SHORT).show();


                        }
                        else
                        {
                            Toast.makeText(getContext(),"UNselected"+i,Toast.LENGTH_SHORT).show();
                            selectedTasks.remove(new TaskItem(tasksCursor.getInt(0),tasksCursor.getString(1),tasksCursor.getString(2)));


                        }

                    }
                })
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i("SELECTED TASKS","pressed positive");

                                TaskItem[] selectedTasksA = new TaskItem[selectedTasks.size()];
                                TaskItem[] oldSelectedTasksA = new TaskItem[oldSelectedTasks.size()];

                                selectedTasksA = selectedTasks.toArray(selectedTasksA);
                                //oldSelectedTasksA = oldSelectedTasks.toArray(oldSelectedTasks);

                                subtasksViewModel.selectItem(new SubtasksViewModelData(selectedTasksA,subtaskAdapter));

                                for(TaskItem ti : selectedTasksA)
                                    Log.i("SELTASK_A",ti.toString());

                                Log.i("DIALOG","subtasks submitted");

                                //subtaskAdapter.notifyDataSetChanged();

                                final TasksDiffCallback diffCallback = new TasksDiffCallback(oldSelectedTasks,selectedTasks);
                                final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

                                diffResult.dispatchUpdatesTo(subtaskAdapter);
                            }
                        });






        return builder.create();
    }





}

