package com.application.bit_time.Main_Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.R;
import com.application.bit_time.utils.ReportData;
import com.application.bit_time.utils.RunningActivityData;
import com.application.bit_time.utils.RunningActivityViewModel;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.Db.DbManager;

import java.util.ArrayList;
import java.util.List;

public class RunningTaskFragment extends Fragment {

    DbManager dbManager;
    int printFlag = 0;
    RunningActivityViewModel runningActivityViewModel;
    private int currentSubtask = 0;
    private List<TaskItem> subtasks;
    private List<RunningActivityData> runningActivityData;
    private List<ReportData> reportDataList;
    private Cursor runningActivityCursor;
    LinearLayout lowerLinearLayout;
    TextView runningTask;
    TextView nextTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       //Log.i("ENTERED","ON CREATE");

        // here there are the steps that retrieve the data of the activity from the db and set variables

        //SimpleDateFormat timeFormat = new SimpleDateFormat(" HH : mm ", Locale.getDefault());
        runningActivityData = new ArrayList<>();
        reportDataList = new ArrayList<>();
        subtasks = new ArrayList<>();
        int activityId = 3;
        dbManager = new DbManager(getContext());

        runningActivityViewModel = new ViewModelProvider(this.requireActivity()).get(RunningActivityViewModel.class);

        runningActivityCursor = dbManager.searchActivityById(activityId) ;
        runningActivityCursor.moveToFirst();

        /*for(int j =0 ; j<DbContract.Activities.DIM_MAX;j++)
        {
            int taskId = runningActivityCursor.getInt(3+j);

            if(taskId != -1) {
                TaskItem task = dbManager.searchTask(taskId);
                Log.i("task", task.getName());
            }
        }*/

        subtasks.addAll(dbManager.retrieveSubtasks(runningActivityCursor));

        Log.i("subtasks test ret",Integer.toString(subtasks.size()));
        Log.i("subtask get1",subtasks.get(1).toString());


        this.runningActivityViewModel.selectItem(new RunningActivityData(RunningActivityData.Status.Uploaded,subtasks.get(0)));
        Log.i("Running task fragment","selecting get0");

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //Log.i("ENTERED","ON CREATE VIEW");
        View view = inflater.inflate(R.layout.front_running_activity_fragment_layout,container,false);

        lowerLinearLayout = view.findViewById(R.id.lowerLinearLayout);
        runningTask = view.findViewById(R.id.currentTaskTextView);
        nextTask = view.findViewById(R.id.nextTaskTextView);
        Button endTaskButton = view.findViewById(R.id.endTaskButton);

        runningTask.setText(subtasks.get(currentSubtask).getName());
        nextTask.setText(subtasks.get(currentSubtask+1).getName());


        /* runningTaskFragment deve occuparsi di :
            > mettere un nuovo task che possa essere mostrato su home fragment
            > in caso di fine dei subtasks di gestire questa situazione
            > segnarsi cosa è avvenuto nel task precedente (STATUS/choices/tempi, boh) prima del cambio */

        runningActivityViewModel.getSelectedItem().observe(this.getActivity(),item ->
        {

            //Log.i("RTF viewModel",item.getChoice().toString() + " "+item.getStatus().toString());
            if(item.getCurrentTask() != null) {
                if (item.getStatus().toString().equals("Expired") || item.isFilled()) {
                    runningActivityData.add(new RunningActivityData(item.getStatus(), item.getChoice(), item.getCurrentTask()));
                    Log.i("RTF item", item.toString());
                    updateCurrentTask();
                }

            }


        });



        endTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(),"end button pressed",Toast.LENGTH_SHORT).show();

                RunningTaskDialog runningTaskDialog = new RunningTaskDialog();
                runningTaskDialog.show(requireActivity().getSupportFragmentManager(),null);
            }
        });

        return view;



    }

    private void updateCurrentTask() {

        if(currentSubtask < subtasks.size()-2)
        {
            currentSubtask++;
            runningTask.setText(subtasks.get(currentSubtask).getName());
            nextTask.setText(subtasks.get(currentSubtask+1).getName());
            TaskItem taskToUpload = subtasks.get(currentSubtask);
            //Log.i("incrementing currPos",taskToUpload.toString());
            this.runningActivityViewModel.selectItem(new RunningActivityData(RunningActivityData.Status.Uploaded,taskToUpload));

        }
        else if(currentSubtask < subtasks.size()-1)
        {
            currentSubtask++;
            runningTask.setText(subtasks.get(currentSubtask).getName());
            lowerLinearLayout.setVisibility(View.GONE);
            TaskItem taskToUpload = subtasks.get(currentSubtask);
            //Log.i("incrementing currPos",taskToUpload.toString());
            this.runningActivityViewModel.selectItem(new RunningActivityData(RunningActivityData.Status.Uploaded,taskToUpload));

        }
        else
        {
            if(printFlag == 0)
            {
                Log.i("activity completed","print report");
                for(RunningActivityData rad : runningActivityData)
                {
                    Log.i("report",rad.toString());
                    reportDataList.add(new ReportData(rad.getCurrentTask().getName(),rad.getStatus()));
                }
                printFlag = 1;
                Log.i("RTF list size",Integer.toString(this.reportDataList.size()));
                this.runningActivityViewModel.selectItem(new RunningActivityData(RunningActivityData.Status.ActivityDone,this.reportDataList));
            }
        }


    }

}
