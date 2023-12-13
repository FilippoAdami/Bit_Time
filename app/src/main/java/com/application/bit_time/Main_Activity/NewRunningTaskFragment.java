package com.application.bit_time.Main_Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.R;
import com.application.bit_time.utils.ActivityInfo;
import com.application.bit_time.utils.ActivityItem;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.ReportData;
import com.application.bit_time.utils.RunningActivityData;
import com.application.bit_time.utils.RunningActivityViewModel;
import com.application.bit_time.utils.TaskItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class NewRunningTaskFragment extends Fragment {


    DbManager dbManager;

    private TaskItem currentTask;
    private List<TaskItem> subtasksList;
    private RunningActivityViewModel RAVM;
    private List<ReportData> reportDataList;
    private ListIterator<TaskItem> SLIterator;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        this.dbManager = new DbManager(this.getContext());
        subtasksList = new ArrayList<>();
        RAVM = new ViewModelProvider(this.requireActivity()).get(RunningActivityViewModel.class);
        this.reportDataList = new ArrayList<>();

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int actId= sharedPreferences.getInt("activityToRun",-500);

        ActivityInfo activityInfoToSearch = new ActivityInfo(actId,"placeholderName",-1);
        ActivityItem activityItem =dbManager.searchActivityItem(activityInfoToSearch);
        Log.i("actToRun in NRTF",activityItem.toString());

        for(TaskItem ti : activityItem.getSubtasks())
        {
            if(ti.getID()!=-1)
            {
                subtasksList.add(ti);
                Log.i("ti added",ti.toString());
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.front_running_activity_fragment_layout,container,false);


        Button endTaskButton = view.findViewById(R.id.endTaskButton);
        endTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RAVM.selectItem(new newRunningActivityData(currentTask, newRunningActivityData.Status.OnWait));
                RunningTaskDialog runningTaskDialog = new RunningTaskDialog();
                runningTaskDialog.show(requireActivity().getSupportFragmentManager(),null);

            }
        });

        RAVM.getSelectedItem().observe(this,item->
        {
            if(item.currentTask!=null) {
                Log.i("NRTF item", item.toString());

                if (item.status.toString().equals("Running")) {
                    TextView currentTask = view.findViewById(R.id.currentTaskTextView);
                    currentTask.setText(item.currentTask.getName());
                    TextView durationTextView = view.findViewById(R.id.clockPlaceholder);
                    durationTextView.setText(item.currentTask.getDuration());

                    TextView nextTask = view.findViewById(R.id.nextTaskTextView);
                    if (SLIterator.hasNext()) {

                        nextTask.setText(SLIterator.next().getName());
                        SLIterator.previous();
                    } else {
                        nextTask.setVisibility(View.INVISIBLE);
                    }


                }
                if (item.status.toString().equals("End")) {

                    ReportData currentReportData = item.getReportData();
                    Log.i("currRepoData", currentReportData.toString());
                    this.reportDataList.add(currentReportData);

                    if (SLIterator.hasNext()) {
                        RAVM.selectItem(new newRunningActivityData(SLIterator.next()));
                    } else {
                        for (ReportData RD : this.reportDataList) {
                            Log.i("reportData", RD.toString());
                        }

                        Log.i("Activity done", "check");
                        RAVM.selectItem(new newRunningActivityData(newRunningActivityData.Status.ActivityDone, reportDataList));

                    }
                }

            }
        });

        SLIterator = subtasksList.listIterator();

        if(SLIterator.hasNext())
        {
            currentTask = SLIterator.next();
            Log.i("currentTask",currentTask.toString());
            RAVM.selectItem(new newRunningActivityData(currentTask));
        }




        return view;
    }
}
