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

import com.application.bit_time.utils.ActivityItem;
import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.utils.Db.DbViewModel;
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

    TaskItem[] allTaskItems;
    CustomViewModel viewModel;
    DbManager dbManager;
    Cursor allTasksCursor;
    ArrayList<TaskItem> selectedTasks;
    ArrayList<TaskItem> oldSelectedTasks;
    SubtasksViewModel subtasksViewModel;
    SubtaskAdapter subtaskAdapter;
    DbViewModel dbViewModel ;
    boolean[] checkedItems;
    String[] allTasksNames;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);

        dbManager = new DbManager(getContext());
        allTasksCursor= dbManager.selectAllTasks();

        dbViewModel = new ViewModelProvider(requireActivity()).get(DbViewModel.class);

        selectedTasks = new ArrayList<>();
        subtasksViewModel = new ViewModelProvider(requireActivity()).get("subTasksVM",SubtasksViewModel.class);
        subtaskAdapter = subtasksViewModel.getSelectedItem().getValue().subtaskAdapter;
        oldSelectedTasks = new ArrayList<>(Arrays.asList(subtasksViewModel.getSelectedItem().getValue().subtasks));

        for(TaskItem ti : oldSelectedTasks)
        {
            Log.i("oldSelTasks",ti.toString());
        }

        Log.i("taskCursor",Integer.toString(allTasksCursor.getCount()));
        Log.i("subtAdapt Dialog is",""+subtaskAdapter.toString());

        Log.i("testfromdialog",subtasksViewModel.toString());


        //Toast.makeText(getContext(),dbManager.getDbName(),Toast.LENGTH_SHORT).show();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {

        Log.i("from dialog vm",this.viewModel.getSelectedItem().getValue().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        //Log.i("TASKSCURSOR log",""+tasksCursor.getColumnCount());


        if(this.viewModel.getSelectedItem().getValue().toString().equals("NewActivity"))
        {

            if (this.subtasksViewModel.getSelectedItem().getValue().isAlreadyModified())
            {
                SubtasksViewModelData currentSVMData = this.subtasksViewModel.getSelectedItem().getValue();

                checkedItems = new boolean[currentSVMData.getMask().length];
                allTaskItems = new TaskItem[checkedItems.length];
                allTasksNames = new String[checkedItems.length];

                int i = 0;
                for (boolean b : currentSVMData.getMask()) {
                    this.checkedItems[i] = new Boolean(b);
                    i++;
                }

                i = 0;

                for (TaskItem ti : currentSVMData.getAllTaskItems()) {
                    this.allTaskItems[i] = new TaskItem(ti);
                    this.allTasksNames[i]= new String(this.allTaskItems[i].getName());
                    Log.i(this.allTaskItems[i].getName(), Boolean.toString(this.checkedItems[i]));

                            if(this.checkedItems[i]==true)
                            {
                                this.selectedTasks.add(new TaskItem(this.allTaskItems[i]));
                            }
                    i++;
                }

                Log.i("RESTORING", "completed");
            }
            else
            {
                if (allTasksCursor.getCount() > 0)
                {
                    allTasksCursor.moveToFirst();
                    checkedItems = new boolean[allTasksCursor.getCount()];
                    allTasksNames = new String[allTasksCursor.getCount()];
                    allTaskItems = new TaskItem[allTasksCursor.getCount()];
                    int i = 0;
                    do {
                        checkedItems[i] = new Boolean(false);
                        allTaskItems[i] = new TaskItem(allTasksCursor.getInt(0), allTasksCursor.getString(1), allTasksCursor.getInt(2));
                        allTasksNames[i] = new String(allTaskItems[i].getName());

                        Log.i("building dialog structs", allTasksNames[i] + " " + Boolean.toString(checkedItems[i]));
                        i++;
                    } while (allTasksCursor.moveToNext());
                } else
                {
                    //Log.i("TASK SEL DIALOG","empty cursor");
                    builder.setTitle("Non riesco a trovare nessun task !");
                    builder.setPositiveButton("Capito", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    return builder.create();
                }

            }

                    builder.setTitle("Choose tasks to add to activity")
                            .setMultiChoiceItems(allTasksNames, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {

                                    if (isChecked) {

                                        if (selectedTasks.size() <= DbContract.Activities.DIM_MAX) {
                                            //selectedTasks.add(new TaskItem(allTasksCursor.getInt(0), allTasksCursor.getString(1), allTasksCursor.getString(2)));
                                            selectedTasks.add(new TaskItem(allTaskItems[i]));
                                            //Toast.makeText(getContext(), "added " + allTasksCursor.getString(1), Toast.LENGTH_SHORT).show();
                                            Toast.makeText(getContext(), "added " + allTasksNames[i], Toast.LENGTH_SHORT).show();

                                        } else
                                            Toast.makeText(getContext(), "DIM MAX reached", Toast.LENGTH_SHORT).show();


                                    } else {
                                        Toast.makeText(getContext(), "UNselected" + i, Toast.LENGTH_SHORT).show();
                                        //selectedTasks.remove(new TaskItem(allTasksCursor.getInt(0), allTasksCursor.getString(1), allTasksCursor.getString(2)));
                                        selectedTasks.remove(new TaskItem(allTaskItems[i]));

                                    }
                                }
                            })
                            .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.i("SELECTED TASKS", "pressed positive");
                                    Log.i("before submitting",Boolean.toString(checkedItems[0]));
                                    TaskItem[] selectedTasksA = new TaskItem[selectedTasks.size()];
                                    selectedTasksA = selectedTasks.toArray(selectedTasksA);
                                    //TaskItem[] oldSelectedTasksA = new TaskItem[oldSelectedTasks.size()];
                                    //oldSelectedTasksA = oldSelectedTasks.toArray(oldSelectedTasks);


                                    SubtasksViewModelData updatedData = new SubtasksViewModelData(selectedTasksA, subtaskAdapter);
                                    updatedData.setAllTaskItems(allTaskItems);
                                    updatedData.setMask(checkedItems);
                                    updatedData.hasBeenModified();
                                    subtasksViewModel.selectItem(updatedData);


                                    for(TaskItem ti: oldSelectedTasks)
                                    {
                                        Log.i("taksdiffcallback",ti.toString());
                                    }
                                    Log.i("taksdiffcallback","now new");
                                    for (TaskItem ti : selectedTasks)
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
        else if(this.viewModel.getSelectedItem().getValue().toString().equals("ModifyActivity")) {

            //riprendi da dichiarare alltasksitems con la dimensione di subtasks credo nel subtasksviewmodel
            //poi procedi


            SubtasksViewModelData currentData = subtasksViewModel.getSelectedItem().getValue();
            List<TaskItem> subtasks = new ArrayList<>();
            List<String> stringList = new ArrayList<>();
            List<Boolean> checkedItemsList = new ArrayList<>();


            allTaskItems= new TaskItem[currentData.getAllTaskItems().length];
            checkedItems= new boolean[allTaskItems.length];
            allTasksNames = new String[allTaskItems.length];

            for(TaskItem ti : currentData.getSubtasks())
            {
                if(ti.getID()!= -1) {
                    selectedTasks.add(ti);
                    Log.i("resume from", ti.toString());
                }
            }

            Log.i("TASKSELDIALOG test",currentData.toString());
            int i=0;
            for(TaskItem ti : currentData.getAllTaskItems()) {
                allTaskItems[i]= new TaskItem(ti);
                allTasksNames[i]= new String(allTaskItems[i].getName());

                if(selectedTasks.contains(ti))
                {
                    Log.i("is contained",ti.toString());
                    checkedItems[i]=new Boolean(true);
                }
                else
                    checkedItems[i]=new Boolean(false);

                Log.i("TASKSELDIALOG test", ti.toString());
                i++;
            }



            //Log.i("activityId",Integer.toString(subtasksViewModel.getSelectedItem().getValue().getActivityId()));
            //Cursor activityCursor = dbManager.searchActivityById(subtasksViewModel.getSelectedItem().getValue().getActivityId());

            //ActivityItem activity = dbViewModel.getSelectedItem().getValue().activityItem;
            //Log.i("activity in vm", activity.toString());
            //fillAlreadySelectedTasks(activityCursor,stringList,subtasks,checkedItemsList);
            //fillAlreadySelectedTasks(activity, stringList, subtasks, checkedItemsList);

            //Log.i("filler check out", Integer.toString(subtasks.size()));

            //unselectedTasksFiller(stringList, subtasks, checkedItemsList);

            //Log.i("filler check out", Integer.toString(subtasks.size()));


            String[] subtasksStrings = new String[stringList.size()];
            //boolean[] checkedItems = new boolean[stringList.size()];
            TaskItem[] subtasksArr = new TaskItem[subtasks.size()];

            i = 0;

            Iterator<Boolean> checkIt = checkedItemsList.iterator();
            Iterator<TaskItem> tasksIt = subtasks.iterator();

            // converting from lists to arrays
            for (String currentName : stringList) {
                TaskItem currentTask = tasksIt.next();
                Log.i("taskitem", currentTask.toString());
                subtasksArr[i] = new TaskItem(currentTask);
                subtasksStrings[i] = currentName;
                checkedItems[i] = checkIt.next();
                i++;
            }

            builder.setTitle("Modify the tasks of the activity")

                    .setMultiChoiceItems(allTasksNames, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                            if (isChecked) {

                                if (selectedTasks.size() <= DbContract.Activities.DIM_MAX) {
                                    selectedTasks.add(new TaskItem(allTaskItems[i]));
                                    //Toast.makeText(getContext(), "added " + allTasksNames[i], Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getContext(), "DIM MAX reached", Toast.LENGTH_SHORT).show();


                            } else {
                                //Toast.makeText(getContext(), "UNselected" + i, Toast.LENGTH_SHORT).show();
                                selectedTasks.remove(new TaskItem(allTaskItems[i]));


                            }
                        }
                    });


            builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int k) {
                    Log.i("SELECTED TASKS", "pressed positive");

                    TaskItem[] selectedTasksA = new TaskItem[DbContract.Activities.DIM_MAX];

                    for (int i = 0; i < DbContract.Activities.DIM_MAX; i++) {
                        if (i < selectedTasks.size())
                            selectedTasksA[i] = new TaskItem(selectedTasks.get(i));
                        else
                            selectedTasksA[i] = new TaskItem();

                        Log.i("selectedTasksA", selectedTasksA[i].toString());
                    }

                    //selectedTasksA = selectedTasks.toArray(selectedTasksA);
                    //oldSelectedTasksA = oldSelectedTasks.toArray(oldSelectedTasks);


                    //SubtasksViewModelData newSTVMData = new SubtasksViewModelData(selectedTasksA, subtaskAdapter);

                    SubtasksViewModelData newSTVMData = subtasksViewModel.getSelectedItem().getValue();
                    newSTVMData.hasBeenModified();
                    newSTVMData.setSubtasks(selectedTasksA);
                    newSTVMData.setMask(checkedItems);
                    subtasksViewModel.selectItem(newSTVMData);

                    for (TaskItem ti : selectedTasksA)
                        Log.i("SELTASK_A", ti.toString());

                    Log.i("DIALOG", "subtasks submitted");

                    //subtaskAdapter.notifyDataSetChanged();

                    final TasksDiffCallback diffCallback = new TasksDiffCallback(oldSelectedTasks, selectedTasks);
                    final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
                    Log.i("subtasksadapt",subtaskAdapter.toString());
                    diffResult.dispatchUpdatesTo(subtaskAdapter);
                }
            });
        }

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

    public void fillAlreadySelectedTasks(ActivityItem activityItem,List<String> stringList, List<TaskItem> subtasks,List<Boolean> checkedItemsList)
    {


        for(TaskItem ti : activityItem.getSubtasks())
        {
            if(ti.getID()>0) {
                Log.i("filler", ti.toString());
                stringList.add(ti.getName());
                checkedItemsList.add(Boolean.TRUE);
                subtasks.add(ti);
                selectedTasks.add(new TaskItem(ti));
            }
        }

    }

    public void unselectedTasksFiller(List<String> stringList,List<TaskItem> subtasks, List<Boolean> checkedItemsList)
    {
        allTasksCursor.moveToFirst();
        do
        {
            String currentElemName = allTasksCursor.getString(1);
            if(!stringList.contains(currentElemName))
            {
                stringList.add(currentElemName);
                subtasks.add(new TaskItem(allTasksCursor.getInt(0),allTasksCursor.getString(1),allTasksCursor.getInt(2)));
                Log.i("fromSetce",currentElemName);
                checkedItemsList.add(Boolean.FALSE);
            }

        }while(allTasksCursor.moveToNext());
        }
    }







