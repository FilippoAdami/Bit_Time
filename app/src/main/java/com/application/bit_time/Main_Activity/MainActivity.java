package com.application.bit_time.Main_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.application.bit_time.R;
import com.application.bit_time.utils.MainActivityStatusData;
import com.application.bit_time.utils.MainActivityViewModel;
import com.application.bit_time.utils.RunningActivityViewModel;


public class MainActivity extends AppCompatActivity {

    //TODO: link score inside report fragment with those selected inside settings
    private MainActivityViewModel statusVM;
    private RunningActivityViewModel runningActivityViewModel;
    private SharedPreferences sharedPreferences;


    //BroadcastReceiver timeBroadcastReceiver;
    //IntentFilter timeBrFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_main);



        FragmentManager fragmentManager = getSupportFragmentManager();
        //Fragment bottomFragment = new RunningTaskFragment();
        Fragment bottomFragment = new Fragment();


        Fragment controlbarFragment = new ControlsFragment();

        this.statusVM = new ViewModelProvider(this).get(MainActivityViewModel.class);


        this.statusVM.getSelectedItem().observe(this, item->
        {
            Log.i("STATUSVM DETECTION","MainActivity detected something");


            MainActivityStatusData.Status currentStatus = item.getCurrentStatus();

            if(currentStatus.equals(MainActivityStatusData.Status.Idle))
            {
                Log.i("CURRENT STATUS MAINACT","IDLE");
                fragmentManager
                        .beginTransaction()
                        .add(R.id.fragment_container,new HomeFragment())
                        //.add(R.id.bottomFragmentContainer,new GameFragment())
                        .commit();
            }
            else if( currentStatus.equals(MainActivityStatusData.Status.QuickstartMenu))
            {
                Log.i("CURRENT STATUS MAINACT","QUICKSTART MENU");
                fragmentManager
                        .beginTransaction()
                        .add(R.id.fragment_container,new QuickstartMenuFragment())
                        .add(R.id.bottomFragmentContainer,new Fragment())
                        .commit();
            }
            else if( currentStatus.equals(MainActivityStatusData.Status.RunningActivity))
            {
                Log.i("CURRENT STATUS MAINACT","RUNNING ACTIVITY");

                SharedPreferences sharedPrefs = this.getPreferences(Context.MODE_PRIVATE);
                int value = sharedPrefs.getInt("activityToRun",-1);

                Log.i("activityToRun",Integer.toString(value));

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container,new HomeFragment())
                        .replace(R.id.bottomFragmentContainer,new RunningTaskFragment())
                        .commit();
            }
            else if(currentStatus.equals(MainActivityStatusData.Status.CaregiverLogin))
            {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container,new CaregiverLoginFragment())
                        .replace(R.id.bottomFragmentContainer,new Fragment())
                        .addToBackStack(null)
                        .commit();
            }


        });


        runningActivityViewModel = new ViewModelProvider(this).get(RunningActivityViewModel.class);

        runningActivityViewModel.getSelectedItem().observe(this, item->
        {
            if(item.getStatus().toString().equals("ActivityDone"))
            {
                Log.i("Main Activity detection","ActivityDone detected");
                Log.i("Main act detection",Integer.toString(item.getReportDataList().size()));
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container,new ReportFragment())
                        .detach(bottomFragment)
                        .commit();
            }
        });



        Bundle intentBundle = getIntent().getExtras();

        if(intentBundle != null)
        {

            int actId = (int) intentBundle.get("actId");
            Log.i("sourceAct"," actId : " + actId);

            SharedPreferences sharedPrefs = this.getPreferences(Context.MODE_PRIVATE);
            sharedPrefs
                    .edit()
                    .putInt("activityToRun",actId)
                    .commit();


            this.statusVM.selectItem(new MainActivityStatusData(MainActivityStatusData.Status.RunningActivity));


        }
        else
        {
            Log.i("sourceAct", "intentBundle was null");
        }





        fragmentManager
                .beginTransaction()
                .replace(R.id.controlbarFragment,controlbarFragment)
                .replace(R.id.fragment_container, new QuickstartMenuFragment())
                .replace(R.id.bottomFragmentContainer,bottomFragment)
                .commit();
        /*fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .replace(R.id.bottomFragmentContainer,bottomFragment)
                .commit();

         */
        /*
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("loggedIn", false)){
            // If the user is logged in, go to the HomeFragment
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new HomeFragment()).replace(R.id.bottomFragmentContainer,bottomFragment);;
            fragmentTransaction.commit();
            //here for a future implementation we will load the user data from the cloud database
        } else {
            // If the user is not logged in, go to the LogInFragment
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new LogInFragment())
                    .replace(R.id.bottomFragmentContainer,bottomFragment);
            fragmentTransaction.commit();
        }
        */





    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(android.content.BroadcastReceiver);
    }
}
