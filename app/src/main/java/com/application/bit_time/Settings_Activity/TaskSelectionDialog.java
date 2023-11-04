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
import java.util.Iterator;
import java.util.LinkedHashSet;
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

        Log.i("taskCursor",Integer.toString(tasksCursor.getCount()));
        Log.i("subtAdapt Dialog is",""+subtaskAdapter.toString());

        Log.i("testfromdialog",subtasksViewModel.toString());


        //Toast.makeText(getContext(),dbManager.getDbName(),Toast.LENGTH_SHORT).show();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Log.i("from dialog vm",this.viewModel.getSelectedItem().getValue().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        //Log.i("TASKSCURSOR log",""+tasksCursor.getColumnCount());


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

            List<TaskItem> subtasks = new ArrayList<>();
            List<String> stringList = new ArrayList<>();
            List<Boolean> checkedItemsList = new ArrayList<>();


            //Log.i("activityId",Integer.toString(subtasksViewModel.getSelectedItem().getValue().getActivityId()));
            Cursor activityCursor = dbManager.searchActivityById(subtasksViewModel.getSelectedItem().getValue().getActivityId());




            fillAlreadySelectedTasks(activityCursor,stringList,subtasks,checkedItemsList);

            tasksCursor.moveToFirst();
            while(tasksCursor.moveToNext())
            {
                String currentElemName = tasksCursor.getString(1);
                if(!stringList.contains(currentElemName))
                {
                    stringList.add(currentElemName);
                    subtasks.add(new TaskItem(tasksCursor.getInt(0),tasksCursor.getString(1),tasksCursor.getInt(2)));
                    Log.i("fromSetce",currentElemName);
                    checkedItemsList.add(Boolean.FALSE);
                }

            }

           String[] subtasksStrings= new String[stringList.size()];
           boolean[] checkedItems = new boolean[stringList.size()];
           TaskItem[] subtasksArr = new TaskItem[subtasks.size()];

           int i=0;

           Iterator<Boolean> checkIt = checkedItemsList.iterator();
           Iterator<TaskItem> tasksIt = subtasks.iterator();

            // converting from lists to arrays
           for(String currentName : stringList)
           {
               TaskItem currentTask = tasksIt.next();
               Log.i("taskitem",currentTask.toString());
               subtasksArr[i]= new TaskItem(currentTask);
               subtasksStrings[i] = currentName;
               checkedItems[i]=checkIt.next();
               i++;
           }

            builder.setTitle("Modify the tasks of the activity")
                    .setMultiChoiceItems(subtasksStrings, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                        }
                    })
                    .setMultiChoiceItems(subtasksStrings, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                            if (isChecked) {

                                if (selectedTasks.size() <= DbContract.Activities.DIM_MAX) {
                                    selectedTasks.add(new TaskItem(subtasksArr[i].getID(), subtasksArr[i].getName(), subtasksArr[i].getDuration()));
                                    Toast.makeText(getContext(), "added " + subtasksArr[i].getName(), Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getContext(), "DIM MAX reached", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(getContext(), "UNselected" + i, Toast.LENGTH_SHORT).show();
                                selectedTasks.remove(new TaskItem(subtasksArr[i].getID(), subtasksArr[i].getName(),subtasksArr[i].getDuration()));


                            }
                        }
                    });


        }


        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
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

        return builder.create();
    }


    public void fillAlreadySelectedTasks(Cursor activityCursor, List<String> stringList, List<TaskItem> subtasks, List<Boolean> checkedItemsList)
    {
        activityCursor.moveToFirst();
        Log.i("ACTCURSOR logcount",""+activityCursor.getCount());
        Log.i("ACTCURSOR logcolcount",""+activityCursor.getColumnCount());

        for(int i = 0;i<DbContract.Activities.DIM_MAX;i++)
        {
            int currentTaskId = activityCursor.getInt(3+i);
            if(currentTaskId>0)
            {
                TaskItem currentElem = dbManager.searchTask(currentTaskId);
                if(!stringList.contains(currentElem.getName()))
                {
                    subtasks.add(currentElem);
                    this.selectedTasks.add(currentElem);

                    if (stringList.add(currentElem.getName()))
                    {
                        Log.i(currentElem.toString(), "added to set");
                        checkedItemsList.add(Boolean.TRUE);
                    }
                }
            }

        }
    }

}




