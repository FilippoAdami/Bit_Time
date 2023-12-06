package com.application.bit_time.Main_Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.widget.Button;
import android.widget.TextView;

import com.application.bit_time.R;
import com.application.bit_time.utils.RunningActivityData;
import com.application.bit_time.utils.RunningActivityViewModel;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.AnalogClockView;

public class HomeFragment extends Fragment {

    int lastedTime;
    private SimpleDateFormat timeFormat;
    private RunningActivityViewModel runningActivityViewModel;
    private TextView clockTextView;
    private AnalogClockView analogClockView;
    private Handler handler = new Handler();
    private TaskItem currentTask;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        currentTask = new TaskItem();

        lastedTime = 0;
        super.onCreate(savedInstanceState);
        timeFormat = new SimpleDateFormat(" HH : mm ", Locale.getDefault());

        runningActivityViewModel = new ViewModelProvider(requireActivity()).get(RunningActivityViewModel.class);

        //currentTask= runningActivityViewModel.getSelectedItem().getValue().getCurrentTask();

        //Log.i("RAVM currentTask dur ",currentTask.getDuration());

        runningActivityViewModel.getSelectedItem().observe(this,item->
        {
            Log.i("Home fragment","notified by observer");
            RunningActivityData.Status currentStatus = item.getStatus();

            if(currentStatus.toString().equals("Uploaded"))
            {
                Log.i("HomeFrag in Uploaded",item.getCurrentTask().toString());
                currentTask= runningActivityViewModel.getSelectedItem().getValue().getCurrentTask();
                runningActivityViewModel.selectItem(new RunningActivityData(RunningActivityData.Status.Running, RunningActivityData.Choice.NoChoice,currentTask));
            }else if(currentStatus.toString().equals("OnWait"))
            {
                int duration = currentTask.getDurationInt();
                if(lastedTime <= duration/2)
                {
                    //Log.i("From OnWait ","to OnTime");
                    runningActivityViewModel.selectItem(new RunningActivityData(RunningActivityData.Status.OnTime, RunningActivityData.Choice.Yes,currentTask));
                }
                else if(lastedTime <= 3*duration/4)
                {
                    //Log.i("From OnWait","to LittleDelay");
                    runningActivityViewModel.selectItem(new RunningActivityData(RunningActivityData.Status.LittleDelay,RunningActivityData.Choice.Yes,currentTask));

                }
                else
                {
                    //Log.i("From OnWait","to BigDelay");
                    runningActivityViewModel.selectItem(new RunningActivityData(RunningActivityData.Status.BigDelay,RunningActivityData.Choice.Yes,currentTask));

                }

                lastedTime = 0;
            }


        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.a_home_layout, container, false);
        View rootView = inflater.inflate(R.layout.a_new_home_fragment_layout, container, false);

        clockTextView = rootView.findViewById(R.id.clockTextView2);
        analogClockView = rootView.findViewById(R.id.analogClockView2);
        //Button switchButton = rootView.findViewById(R.id.caregiver_button);
        /*Button gameButton = rootView.findViewById(R.id.games_button2);

        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with a new fragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new GameFragment());
                fragmentTransaction.addToBackStack(null); // Optional: Add to back stack
                fragmentTransaction.commit();
            }
        });
        /*switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with a new fragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new CaregiverLoginFragment());
                fragmentTransaction.addToBackStack(null); // Optional: Add to back stack
                fragmentTransaction.commit();
            }
        });*/

        updateTime();

        return rootView;
    }

    private void updateTime() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                lastedTime++;

                Log.i("lastedTime",Integer.toString(lastedTime));

                if(currentTask != null) {
                    if (lastedTime == currentTask.getDurationInt()) {
                        //Log.i("duration", "reached");
                        runningActivityViewModel.selectItem(new RunningActivityData(RunningActivityData.Status.Expired, RunningActivityData.Choice.NoChoice,currentTask));
                        lastedTime = 0;
                    }
                }
                // qui credo vada anche il codice per regolare la colorazione dell'orologio

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
            }
        }, 1000);
    }

    private String getCurrentTime() {

        return timeFormat.format(new Date());
    }
}

