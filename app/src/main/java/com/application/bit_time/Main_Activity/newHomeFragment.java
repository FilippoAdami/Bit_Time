package com.application.bit_time.Main_Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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

        clockTextView = view.findViewById(R.id.clockTextView2);
        analogClockView = view.findViewById(R.id.analogClockView2);


        RAVM.getSelectedItem().observe(getViewLifecycleOwner(),item->
        {

            if(item.currentTask!= null) {
                Log.i("item from NHF", item.toString());
                if (item.status.toString().equals("Uploaded")) {

                    if(this.subtasksData ==null)
                    {
                        this.subtasksData = new ArrayList<>(item.getSubtasksData());
                        for(TaskItem TI : this.subtasksData)
                        {
                            Log.i("TI from NHF",TI.toString());
                        }
                        generateTimeString();
                        updateTime();

                    }

                    Log.i("item at uploaded",item.toString());
                    lastedTime = 0;
                    RAVM.selectItem(new newRunningActivityData(item.currentTask, newRunningActivityData.Status.Running));
                } else if (item.status.toString().equals("Running")) {

                    Log.i("item running NHF",item.toString());
                    currentTask = item.currentTask;
                    lastedTime = 0;
                } else if (item.status.toString().equals("OnWait")) {
                    Log.i("item at onwait",item.toString());
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putInt("lastedTime", lastedTime).commit();
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

            if(lastedTime== currentTask.getDurationInt())
            {
                Log.i("currTask should exp",Integer.toString(lastedTime));
                newRunningActivityData currentRAD = new newRunningActivityData(currentTask);
                currentRAD.setAsExpired();
                Log.i("currTask nFH",currentRAD.fullToString());
                RAVM.selectItem(currentRAD);
            }


            String currentTime = getCurrentTime();

            String[] parts = currentTime.split(":");
            String hours = parts[0];
            String minutes = parts[1];

            SpannableString spannableTime = new SpannableString(currentTime);
            spannableTime.setSpan(new ForegroundColorSpan(Color.BLUE), 0, hours.length(), 0);
            spannableTime.setSpan(new ForegroundColorSpan(Color.RED), hours.length() + 1, currentTime.length(), 0);
            clockTextView.setText(spannableTime);

            analogClockView.invalidate();

            updateTime();
        },1000);
    }

    private String getCurrentTime() {

        return timeFormat.format(new Date());
    }

    public String generateTimeString(){
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
        //Log the string with the names and times of each task
        String string = "";
        for(int i = 0; i < numberOfTasks; i++){
            string += names[i] + ": " + times[i] + " minutes\n";
        }
        Log.i("string", string);
        return "";
    }


}
