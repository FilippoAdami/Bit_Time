package com.application.bit_time.Main_Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.R;
import com.application.bit_time.utils.ActivityInfo;
import com.application.bit_time.utils.ActivityItem;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.ReportData;
import com.application.bit_time.utils.RunningActivityViewModel;
import com.application.bit_time.utils.TaskItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

public class NewRunningTaskFragment extends Fragment {


    DbManager dbManager;

    private TaskItem currentTask;
    //private List<TaskItem> subtasksList;
    private RunningActivityViewModel RAVM;
    private List<ReportData> reportDataList;
    private ListIterator<ReportData> SLIterator;


    private ImageView currentTaskThumbnail;
    private ImageView nextTaskThumbnail;






    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        this.dbManager = new DbManager(this.getContext());
        //subtasksList = new ArrayList<>();
        RAVM = new ViewModelProvider(this.requireActivity()).get(RunningActivityViewModel.class);
        this.reportDataList = new ArrayList<>();




        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int actId= sharedPreferences.getInt("activityToRun",-500);

        ActivityInfo activityInfoToSearch = new ActivityInfo(actId,"placeholderName",-1, Uri.parse("UriActPlaceholderNewRTFrag"));
        ActivityItem activityItem =dbManager.searchActivityItem(activityInfoToSearch);

        Log.i("actToRun in NRTF",activityItem.toString());

        for(TaskItem ti : activityItem.getSubtasks())
        {
            if(ti.getID()!=-1)
            {
                Log.i("URI filling",ti.getImageUri().toString());
                ReportData latestReportDataBase = new ReportData(ti.getID(),ti.getName(),ti.getDurationInt(),ti.getImageUri());
                this.reportDataList.add(latestReportDataBase);
                Log.i("latestRDBase",latestReportDataBase.toString());
                //subtasksList.add(ti);
                //Log.i("ti added",ti.toString());

            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.front_running_activity_fragment_layout,container,false);


        Button endTaskButton = view.findViewById(R.id.endTaskButton);
        endTaskButton.setOnClickListener(view1 -> {

            RAVM.selectItem(new newRunningActivityData(currentTask, newRunningActivityData.Status.OnWait));

            RunningTaskDialog runningTaskDialog = new RunningTaskDialog();
            runningTaskDialog.show(requireActivity().getSupportFragmentManager(),null);

        });

        RAVM.getSelectedItem().observe(getViewLifecycleOwner(),item->
        {
            if(item.currentTask!=null)
            {
                Log.i("NRTF item", item.toString());
                currentTask = item.currentTask;
                if (item.status.toString().equals("Running")) {
                    Log.i("item at running",item.toString());
                    TextView currentTaskTW = view.findViewById(R.id.currentTaskTextView);
                    currentTaskTW.setText(item.currentTask.getName());
                    TextView durationTextView = view.findViewById(R.id.clockPlaceholder);
                    //durationTextView.setText(item.currentTask.getDuration());
                    durationTextView.setText(item.currentTask.getFormattedDuration());
                    currentTaskThumbnail = view.findViewById(R.id.currentTaskThumbnail);
                    try {
                        currentTaskThumbnail.setImageDrawable(currentTask.getDrawableThumbnail(this.getContext()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                    TextView nextTaskTW = view.findViewById(R.id.nextTaskTextView);
                    TextView nextDurationTW = view.findViewById(R.id.nextClockPlaceholder);
                    nextTaskThumbnail = view.findViewById(R.id.nextTaskThumbnail);

                    if (SLIterator.hasNext()) {

                        TaskItem nextTask = SLIterator.next().getTaskItem();
                        Log.i("nextTaskItem URI",nextTask.getImageUri().toString());
                        nextTaskTW.setText(nextTask.getName());
                        //nextDurationTW.setText(nextTask.getDuration());
                        nextDurationTW.setText(nextTask.getFormattedDuration());
                        try {
                            nextTaskThumbnail.setImageDrawable(nextTask.getDrawableThumbnail(this.getContext()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        SLIterator.previous();
                    } else {
                        nextTaskTW.setVisibility(View.INVISIBLE);
                        nextDurationTW.setVisibility(View.INVISIBLE);
                        nextTaskThumbnail.setVisibility(View.INVISIBLE);
                    }


                }
                else if (item.status.toString().equals("End")) {
                    Log.i("item at end",item.toString());
                    newRunningActivityData.UpdatePackage updatePackage= item.getUpdatePackage();
                    int index = SLIterator.previousIndex();
                    //Log.i("indexTest",Integer.toString(index));
                    //Log.i("before updateReportDataTest",this.reportDataList.get(index).toString());
                    this.reportDataList.get(index).updateReportData(updatePackage);
                    Log.i("updateReportDataTest",this.reportDataList.get(index).toString());
                    //ReportData currentReportData = item.getReportData();
                    //Log.i("currRepoData", currentReportData.toString());
                    //this.reportDataList.add(currentReportData);

                    if (SLIterator.hasNext()) {
                        currentTask = SLIterator.next().getTaskItem();
                        Log.i("URI testz",currentTask.getImageUri().toString());
                        // Load the current subTask index to SharedPreferences
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        int currentTaskID = Integer.parseInt(currentTask.getIdStr());
                        sharedPreferences.edit().putInt("currentTask", currentTaskID).commit();
                        // Load the current time to SharedPreferences
                        Calendar calendar = Calendar.getInstance();
                        int currentMinute = calendar.get(Calendar.MINUTE);
                        int currentSecond = calendar.get(Calendar.SECOND);
                        float taskStartingTime = currentMinute + currentSecond/60f;
                        sharedPreferences.edit().putFloat("taskStartingTime", taskStartingTime).commit();

                        RAVM.selectItem(new newRunningActivityData(currentTask));
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

        SLIterator = this.reportDataList.listIterator();

        if(SLIterator.hasNext())
        {
            currentTask = SLIterator.next().getTaskItem();
            Log.i("currentTask URI",currentTask.getImageUri().toString());
            newRunningActivityData nRAD = new newRunningActivityData(currentTask);
            nRAD.setFullReport(this.reportDataList);
            RAVM.selectItem(nRAD);
        }




        return view;
    }
}
