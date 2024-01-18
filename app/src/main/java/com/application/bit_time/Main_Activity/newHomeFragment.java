package com.application.bit_time.Main_Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import com.application.bit_time.R;
import com.application.bit_time.utils.AnalogClockView;
import com.application.bit_time.utils.RunningActivityViewModel;
import com.application.bit_time.utils.TaskItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class newHomeFragment extends Fragment {

    private SimpleDateFormat timeFormat;
    private TextView clockTextView;
    private AnalogClockView analogClockView;
    private final Handler handler = new Handler();



    RunningActivityViewModel RAVM;
    int lastedTime;

    TaskItem currentTask;

    List<TaskItem> subtasksData;

    Paint primaryPaint;
    Paint BluePaint;
    Paint RedPaint;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.subtasksData = null;
        timeFormat = new SimpleDateFormat("HH : mm ", Locale.getDefault());
        lastedTime = 0;
        RAVM = new ViewModelProvider(this.getActivity()).get(RunningActivityViewModel.class);



    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_new_home_fragment_layout,container,false);
        init();
        clockTextView = view.findViewById(R.id.clockTextView2);
        analogClockView = view.findViewById(R.id.analogClockView2);


        // Observe changes in the selected item using LiveData
        RAVM.getSelectedItem().observe(getViewLifecycleOwner(), item -> {

            // Check if the currentTask in the observed item is not null
            if (item.currentTask != null) {
                // Log information about the observed item
                Log.i("item from NHF", item.toString());

                // Check the status of the observed item
                if (item.status.toString().equals("Uploaded")) {

                    // Check if subtasksData is null (first time processing Uploaded status)
                    if (this.subtasksData == null) {
                        // Initialize subtasksData with a copy of subtasksData from the observed item
                        this.subtasksData = new ArrayList<>(item.getSubtasksData());

                        // Log information about each TaskItem in subtasksData
                        for (TaskItem TI : this.subtasksData) {
                            Log.i("TI from NHF", TI.toString());
                        }

                        // Generate and log the time string based on subtasksData
                        generateTimeString();
                        generateIDString();

                        // Update the time (e.g., start a timer)
                        updateTime();
                    }

                    // Log information about the item when the status is Uploaded
                    Log.i("item at uploaded", item.toString());

                    // Reset the lastedTime and set the status to Running
                    lastedTime = 0;
                    RAVM.selectItem(new newRunningActivityData(item.currentTask, newRunningActivityData.Status.Running));
                } else if (item.status.toString().equals("Running")) {

                    // Log information about the item when the status is Running
                    Log.i("item running NHF", item.toString());

                    // Set the currentTask and reset the lastedTime

                    currentTask = item.currentTask;

                    lastedTime = 0;
                } else if (item.status.toString().equals("OnWait")) {

                    // Log information about the item when the status is OnWait
                    Log.i("item at onwait", item.toString());

                    // Store the lastedTime in SharedPreferences
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    sharedPreferences
                            .edit()
                            .putInt("lastedTime", lastedTime)
                            .putString("currentTaskName",currentTask.getName())
                            .commit();
                }
            }
        });


        return view;
    }

    private void updateTime()
    {
        handler.postDelayed(() -> {

            lastedTime++;
            Log.i("lastedTime",Integer.toString(lastedTime));

            /*if(lastedTime== currentTask.getDurationInt())
            {
                Log.i("currTask should exp",Integer.toString(lastedTime));
                newRunningActivityData currentRAD = new newRunningActivityData(currentTask);
                currentRAD.setAsExpired();
                Log.i("currTask nFH",currentRAD.fullToString());
                RAVM.selectItem(currentRAD);
            }*/


            String currentTime = getCurrentTime();

            String[] parts = currentTime.split(":");
            String hours = parts[0];
            String minutes = parts[1];

            SpannableString spannableTime = new SpannableString(currentTime);
            spannableTime.setSpan(new ForegroundColorSpan(BluePaint.getColor()), 0, hours.length(), 0);
            spannableTime.setSpan(new ForegroundColorSpan(RedPaint.getColor()), hours.length() + 1, currentTime.length(), 0);
            clockTextView.setText(spannableTime);

            analogClockView.invalidate();

            updateTime();
        },1000);
    }

    private String getCurrentTime() {

        return timeFormat.format(new Date());
    }

    private void init() {
        Context context = getContext();
        int earthRedColor = ContextCompat.getColor(context, R.color.earth_red);
        int earthBlueColor = ContextCompat.getColor(context, R.color.earth_purple);

        int pastelRedColor = ContextCompat.getColor(context, R.color.pastel_red);
        int pastelBlueColor = ContextCompat.getColor(context, R.color.pastel_purple);

        int vividRedColor = ContextCompat.getColor(context, R.color.red);
        int vividBlueColor = ContextCompat.getColor(context, R.color.purple);

        int primaryColor = ContextCompat.getColor(context, R.color.primary);
        primaryPaint = new Paint();
        primaryPaint.setColor(primaryColor);

        //get the theme of the activity
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String theme = sharedPreferences.getString("theme", "pastel");

        BluePaint = new Paint();
        BluePaint.setStyle(Paint.Style.STROKE);
        RedPaint = new Paint();
        RedPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        if(theme.equals("PastelTheme") || theme.equals("BWTheme")){
            BluePaint.setColor(pastelBlueColor);
            RedPaint.setColor(pastelRedColor);
        }
        else if(theme.equals("EarthTheme")){
            BluePaint.setColor(earthBlueColor);
            RedPaint.setColor(earthRedColor);
        }
        else{
            BluePaint.setColor(vividBlueColor);
            RedPaint.setColor(vividRedColor);
        }
    }

    public void generateTimeString(){
        //Get an array with the times of each task
        int[] times = new int[subtasksData.size()];
        for(int i = 0; i < subtasksData.size(); i++){
            times[i] = subtasksData.get(i).getDurationInt();
        }
        //Get an array with the names of each task
        String[] names = new String[subtasksData.size()];
        for(int i = 0; i < subtasksData.size(); i++){
            names[i] = subtasksData.get(i).getName();
        }
        //Get the total time
        int totalTime = 0;
        for(int i = 0; i < times.length; i++){
            totalTime += times[i];
        }
        //Get the number of tasks
        int numberOfTasks = times.length;

        String string = "";
        for(int i = 0; i < 5; i++){
            //if the task exists, add it to the string, else add "0000"
            String x = "";
            if(i < numberOfTasks){
                x = Integer.toString(times[i]);
                //Convert from secondts to mmss, if one of the numbers is one digits, att a 0 before it
                String mm = Integer.toString(Integer.parseInt(x) / 60);
                if (mm.length() == 1) {
                    mm = "0" + mm;
                }
                String ss= Integer.toString(Integer.parseInt(x) % 60);
                if (ss.length() == 1) {
                    ss = "0" + ss;
                }
                x = mm + ss;
            }
            else{
                x = "0000";
            }
            //add "0" es before the number to make it reach 6 digits
            while(x.length() < 6){
                x = "0" + x;
            }
            string += x+",";
        }
        //add the current time in format hhmmss
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String currentTime = sdf.format(new Date());
        string += currentTime;
        //add the total time at the beginning of the string, check if it is 6 digits first, else correct it adding 0s
        if(Integer.toString(totalTime).length() < 6){
            String x = Integer.toString(totalTime);
            while(x.length() < 6){
                x = "0" + x;
            }
            totalTime = Integer.parseInt(x);
        }
        string = Integer.toString(totalTime) + "," + string;

        //publish the string to SharedPreferences, if there is already a string, it will be overwritten
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("TimesString", string).commit();
    }
    public void generateIDString(){
        // Load the first ID as the current task
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int currentTaskID = Integer.parseInt(subtasksData.get(0).getIdStr());
        sharedPreferences.edit().putInt("currentTask", currentTaskID).commit();
        //Get an array with the IDs of each task
        int[] IDs = new int[subtasksData.size()];
        for(int i = 0; i < subtasksData.size(); i++){
            IDs[i] = Integer.parseInt(subtasksData.get(i).getIdStr());
        }
        //Get the number of tasks
        int numberOfTasks = IDs.length;

        String string = "";
        for(int i = 0; i < 5; i++){
            //if the task exists, add it to the string, else add "0000"
            String x = "";
            if(i < numberOfTasks){
                x = Integer.toString(IDs[i]);
            }
            else{
                x = "0000";
            }
            string += x+",";
        }
        //remove the last comma

        //publish the string to SharedPreferences, if there is already a string, it will be overwritten
        sharedPreferences.edit().putString("IDsString", string).commit();
    }


}
