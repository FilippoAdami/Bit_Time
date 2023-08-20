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

import java.util.ArrayList;

public class TaskSelectionDialog extends DialogFragment {


    DbManager dbManager;
    Cursor tasksCursor;
    ArrayList<TaskItem> selectedTasks;
    SubtasksViewModel subtasksViewModel;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbManager = new DbManager(getContext());
        tasksCursor= dbManager.selectAllTasks();
        selectedTasks = new ArrayList<>();
        subtasksViewModel = new ViewModelProvider(requireActivity()).get(SubtasksViewModel.class);

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

                                selectedTasksA = selectedTasks.toArray(selectedTasksA);

                                subtasksViewModel.selectItem(new SubtasksViewModelData(selectedTasksA));

                                for(TaskItem ti : selectedTasksA)
                                    Log.i("SELTASK_A",ti.toString());

                                Log.i("DIALOG","subtasks submitted");
                            }
                        });






        return builder.create();
    }





}

