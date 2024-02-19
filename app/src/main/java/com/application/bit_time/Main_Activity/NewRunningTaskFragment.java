package com.application.bit_time.Main_Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

public class NewRunningTaskFragment extends Fragment {
    DbManager dbManager;
    private TaskItem currentTask;
    private TaskItem nextTask;
    //private List<TaskItem> subtasksList;
    private RunningActivityViewModel RAVM;
    private List<ReportData> reportDataList;
    private ListIterator<ReportData> SLIterator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.dbManager = new DbManager(this.getContext());
        //subtasksList = new ArrayList<>();
        RAVM = new ViewModelProvider(this.requireActivity()).get(RunningActivityViewModel.class);
        this.reportDataList = new ArrayList<>();

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int actId= sharedPreferences.getInt("activityToRun",-500);
// updated constructor call
        ActivityInfo activityInfoToSearch = new ActivityInfo(actId,"placeholderName",-1, "placeholderImg");
        ActivityItem activityItem =dbManager.searchActivityItem(activityInfoToSearch);



        Log.i("actToRun in NRTF",activityItem.toString());

        for(TaskItem ti : activityItem.getSubtasks())
        {
            if(ti.getID()!=-1)
            {
                Log.i("img inside actItem",ti.getImg());
                ReportData latestReportDataBase = new ReportData(ti.getID(),ti.getName(),ti.getDurationInt(),ti.getImg());
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
// Punto 1
                    ImageView currentImageTW = view.findViewById(R.id.imagePlaceholder);
                    String currentTaskIconPath = item.currentTask.getImg();
                    Log.i("currentTaskIconPathA",currentTaskIconPath);
                    Bitmap bitmap = BitmapFactory.decodeFile(currentTaskIconPath);
                    currentImageTW.setImageBitmap(bitmap);

                    TextView nextTaskTW = view.findViewById(R.id.nextTaskTextView);
                    TextView nextDurationTW = view.findViewById(R.id.nextClockPlaceholder);
                    ImageView nextImageTW = view.findViewById(R.id.nextImagePlaceholder);

                    if (SLIterator.hasNext()) {

                        TaskItem nextTask = SLIterator.next().getTaskItem();
                        nextTaskTW.setText(nextTask.getName());
//Punto 2
                        String nextTaskIconPath = nextTask.getImg();
                        Log.i("nextTaskIconPathA",nextTaskIconPath);
                        Bitmap nextbitmap = BitmapFactory.decodeFile(nextTaskIconPath);
                        nextImageTW.setImageBitmap(nextbitmap);
                        //nextImageTW.setAlpha(0.5f);

                        //nextDurationTW.setText(nextTask.getDuration());
                        nextDurationTW.setText(nextTask.getFormattedDuration());
                        SLIterator.previous();
                    } else {
                        nextTaskTW.setVisibility(View.INVISIBLE);
                        nextDurationTW.setVisibility(View.INVISIBLE);
                        nextImageTW.setVisibility(View.INVISIBLE);
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
            Log.i("currentTask img",currentTask.getImg());
            newRunningActivityData nRAD = new newRunningActivityData(currentTask);
            nRAD.setFullReport(this.reportDataList);
            RAVM.selectItem(nRAD);
        }




        return view;
    }
}
