package com.application.bit_time.Main_Activity;


import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.MainActivityStatusData;
import com.application.bit_time.utils.MainActivityViewModel;
import com.application.bit_time.utils.RunningActivityViewModel;


public class MainActivity extends AppCompatActivity {
    // TODO done : at modifyact time insert the previous name and then if pressed delete it in order to insert a new one
    // TODO: at modactivity time if modified the subtasks doesn0t refresh into the recview
    //TODO: link score inside report fragment with those selected inside settings
    //TODO : if all the subtasks of an activity are deleted then ask if the user wants to delete also the activity or select at least one from the existing ones
    //TODO : show clearly if daily has been pressed and is set or not
    //TODO: decide clearly about how to display the plan and planning section
    // TODO : consider isPlanned field of activities schema
    private MainActivityViewModel statusVM;
    private RunningActivityViewModel runningActivityViewModel;
    private DbManager dbManager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dbManager = new DbManager(getApplicationContext());

        Log.i("buildVersion",Integer.toString(Build.VERSION.SDK_INT));

        //CHECKS FOR PERMISSIONS
        if(Build.VERSION.SDK_INT > 22)
        {
            if(ContextCompat.checkSelfPermission(this,"POST_NOTIFICATIONS") == PERMISSION_GRANTED)
            {
                Log.i("permission","granted");
               }
            else
            {
                Log.i("permission","denied");
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,"POST_NOTIFICATION"))
                {
                    Log.i("Should","true");
                }
                else
                {
                    Log.i("should","false");
                }

                ActivityResultLauncher<String> requestPermissionLauncher =registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->
                {
                    if(isGranted)
                    {
                        Log.i("isGranted","yesss");
                    }
                    else
                    {
                        Log.i("isGranted","false");
                    }
                });

                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);

            }
        }

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("17","notChannel",importance);
            this.getApplicationContext().getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }










        sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //check if there is a shared preference for the theme
        String currentTheme = sharedPreferences.getString("CurrentTheme", null);
        if (currentTheme == null) {
            //if there is no shared preference for the theme, set the default theme to PastelTheme
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("CurrentTheme", "PastelTheme");
            editor.apply();
        }

        String theme = dbManager.getTheme();
        Log.i("Theme", "Theme: " + theme);
        Log.i("Theme", "Current theme: " + currentTheme);
        Log.i("Theme", String.valueOf(theme.equals(currentTheme)));
        if (theme != null && !(theme.equals(currentTheme))) {
            int newTheme = R.style.PastelTheme;
            switch (theme) {
                case "PastelTheme":
                    newTheme = R.style.PastelTheme;
                    theme = "PastelTheme";
                    break;
                case "BWTheme":
                    newTheme = R.style.BWTheme;
                    theme = "BWTheme";
                    Log.i("BWTheme", "BWTheme hjvhgvmgh");
                    break;
                case "EarthTheme":
                    newTheme = R.style.EarthTheme;
                    theme = "EarthTheme";
                    break;
                case "VividTheme":
                    newTheme = R.style.VividTheme;
                    theme = "VividTheme";
                    break;
                default:
                    break;
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("theme", theme);
            editor.apply();
            setTheme(newTheme);
            Log.i("Theme", "Theme changed");
        }
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
                        .replace(R.id.fragment_container,new newHomeFragment())
                        //.add(R.id.bottomFragmentContainer,new GameFragment())
                        .commit();
            }
            else if( currentStatus.equals(MainActivityStatusData.Status.QuickstartMenu))
            {
                //dbManager.selectAndPrintAllReportData();

                Log.i("CURRENT STATUS MAINACT","QUICKSTART MENU");

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container,new QuickstartMenuFragment())
                        .replace(R.id.bottomFragmentContainer,new Fragment())
                        .commit();

                if(item.isBack())
                {
                    if(item.getBackField().equals(MainActivityStatusData.BackField.Save))
                    {
                        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                        int actID = sharedPreferences.getInt("activityToRun",-100);
                        Log.i("Backfield choice","Save actId "+actID);
                        dbManager.insertFullReportData(actID,this.runningActivityViewModel.getSelectedItem().getValue().getFullReport());
                    }
                    else
                    {
                        Log.i("Backfield choice","Ignore");
                    }
                }


            }
            else if( currentStatus.equals(MainActivityStatusData.Status.RunningActivity))
            {
                Log.i("CURRENT STATUS MAINACT","RUNNING ACTIVITY");

                //SharedPreferences sharedPrefs = this.getPreferences(Context.MODE_PRIVATE);
                //int value = sharedPrefs.getInt("activityToRun",-1);
                //Log.i("activityToRun",Integer.toString(value));

                fragmentManager
                        .beginTransaction()
                        //TODO : uncomment the line under this one
                        //.replace(R.id.fragment_container,new HomeFragment())
                        .replace(R.id.bottomFragmentContainer,new NewRunningTaskFragment(),"currentRunningTaskFrag")
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
            if(item.status.toString().equals("ActivityDone"))
            {
                Log.i("Main Activity detection","ActivityDone detected");
                Log.i("Main act detection",Integer.toString(item.getFullReport().size()));
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container,new ReportFragment())
                        .replace(R.id.bottomFragmentContainer,new Fragment())
                        .commit();
            }
        });



        Bundle intentBundle = getIntent().getExtras();

        //TODO : UNCOMMENT AND FIX
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


        this.statusVM.getSelectedItem().observe(this, item->
        {
            Log.i("STATUSVM DETECTION","MainActivity detected something");


            MainActivityStatusData.Status currentStatus = item.getCurrentStatus();

            if(currentStatus.equals(MainActivityStatusData.Status.Idle))
            {
                Log.i("CURRENT STATUS MAINACT","IDLE");
                fragmentManager
                        .beginTransaction()
                        .add(R.id.bottomFragmentContainer,new newHomeFragment())
                        //.replace(R.id.fragment_container,new Fragment())
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
                        .replace(R.id.bottomFragmentContainer,new newHomeFragment())
                        //.replace(R.id.fragment_container,new Fragment())
                        .replace(R.id.fragment_container,new NewRunningTaskFragment())
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



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(android.content.BroadcastReceiver);
    }
}
