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

public class TaskSelectionDialog extends DialogFragment {


    DbManager dbManager;
    Cursor tasksCursor;
    TaskItem[] selectedTasks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbManager = new DbManager(getContext());
        tasksCursor= dbManager.selectAllTasks();
        selectedTasks = new TaskItem[tasksCursor.getCount()];

        for(TaskItem ti : selectedTasks)
        {
            ti =  new TaskItem();
        }
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
                            selectedTasks[i] = new TaskItem(tasksCursor.getInt(0),tasksCursor.getString(1),tasksCursor.getString(2));
                            Toast.makeText(getContext(),"added "+tasksCursor.getString(1),Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(getContext(),"UNselected"+i,Toast.LENGTH_SHORT).show();
                            selectedTasks[i] = new TaskItem(-1,"empty","-1");
                        }

                    }
                })
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i("SELECTED TASKS",selectedTasks[0].toString());
                            }
                        });





        return builder.create();
    }





}

