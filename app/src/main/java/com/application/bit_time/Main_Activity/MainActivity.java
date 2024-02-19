package com.application.bit_time.Main_Activity;


import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.MainActivityStatusData;
import com.application.bit_time.utils.MainActivityViewModel;
import com.application.bit_time.utils.ReportData;
import com.application.bit_time.utils.RunningActivityViewModel;


public class MainActivity extends AppCompatActivity {
    // TODO: at modactivity time if modified the subtasks doesn0t refresh into the recview
    //TODO : if all the subtasks of an activity are deleted then ask if the user wants to delete also the activity or select at least one from the existing ones
    //TODO : show clearly if daily has been pressed and is set or not
    // TODO : consider isPlanned field of activities schema



    private RunningActivityViewModel runningActivityViewModel;
    private DbManager dbManager;
    private MainActivityViewModel statusVM;
    private OnBackPressedCallback ActRunningOBPCallback;
    private FragmentManager fragmentManager;
    private String iconPath;


    public static class QuitDialog extends DialogFragment
    {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            Log.i("OBP callback","quit dialog in OnCreateDial");
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setTitle("uscire?")
                    .setMessage("stai per uscire da questa schermata")
                    .setPositiveButton("esci", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            MainActivityViewModel MAV = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);
                            MainActivityStatusData MASData = new MainActivityStatusData(MainActivityStatusData.Status.QuickstartMenu);
                            MASData.setBackField(MainActivityStatusData.BackField.Quit);
                            MAV.selectItem(MASData);
                        }
                    })
                    .setNegativeButton("rimani qui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });


            return builder.create();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        fragmentManager = getSupportFragmentManager();
        OnBackPressedDispatcher OBPDispatcher = getOnBackPressedDispatcher();
        Log.i("OBP main",OBPDispatcher.toString());

        //DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        //Log.i("pixelWidth",Integer.toString(metrics.widthPixels));

        dbManager = new DbManager(getApplicationContext());

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


        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
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

        //FragmentContainerView topContainer = (FragmentContainerView) findViewById(R.id.controlbarFragment);

        /*if(topContainer == null)
        {
            Log.i("fragment search","failed, null result");
        }
        else {
            Log.i("fragment search","found");
        }*/

        //Fragment bottomFragment = new RunningTaskFragment();
        //Fragment bottomFragment = new Fragment();
        //Fragment controlbarFragment = new ControlsFragment();

        statusVM = new ViewModelProvider(this).get(MainActivityViewModel.class);


        statusVM.getSelectedItem().observe(this, item->
        {
            Log.i("backstack entries",Integer.toString(fragmentManager.getBackStackEntryCount()));
            ActRunningOBPCallback.setEnabled(false);
            Log.i("OBP callback","ActRunning set to false");
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



                /*if(item.isBack())
                {
                    /*if(item.getBackField().equals(MainActivityStatusData.BackField.Save))
                    {


                    }
                    else if(item.getBackField().equals(MainActivityStatusData.BackField.Quit))
                    {
                        newHomeFragment currentHF = (newHomeFragment)fragmentManager.findFragmentByTag("currentNewHomeFragment");
                        //currentHF.quitTimer();
                    }
                    else
                    {
                        Log.i("Backfield choice","Ignore");
                    }
                }*/

                String entryName= getResources().getString(R.string.quickstartMenuEntry);

                if(fragmentManager.popBackStackImmediate(entryName,0)) {
                    Log.i("back to",entryName);
                }
                else
                {

                    Log.i("first time",entryName);
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.controlbarFragment,new ControlsFragment())
                            .replace(R.id.fragment_container, new QuickstartMenuFragment())
                            .replace(R.id.bottomFragmentContainer, new Fragment())
                            .addToBackStack(entryName)
                            .commit();
                }


            }
            else if( currentStatus.equals(MainActivityStatusData.Status.RunningActivity))
            {
                ActRunningOBPCallback.setEnabled(true);
                Log.i("OBP callback","ActRunning set to true");

                Log.i("CURRENT STATUS MAINACT","RUNNING ACTIVITY");

                SharedPreferences sharedPrefs = this.getPreferences(Context.MODE_PRIVATE);
                int value = sharedPrefs.getInt("activityToRun",-1);
                iconPath = sharedPrefs.getString("activityIconPath",null);
                Log.i("activityToRun",Integer.toString(value));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("activityIconPathhh", iconPath);
                editor.apply();
                Log.i("activityIconPathM", ""+iconPath);

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.controlbarFragment,new ControlsFragment())
                        .replace(R.id.fragment_container,new newHomeFragment(),"currentNewHomeFragment")
                        .replace(R.id.bottomFragmentContainer,new NewRunningTaskFragment(),"currentRunningTaskFrag")
                        .addToBackStack(getResources().getString(R.string.runningActivityEntry))
                        .commit();
            }
            else if(currentStatus.equals(MainActivityStatusData.Status.CaregiverLogin))
            {
                String entryName = getResources().getString(R.string.caregiverLoginEntry);

                if(item.getBackField() != null && item.getBackField().equals(MainActivityStatusData.BackField.Quit))
                {
                    newHomeFragment currentHF = (newHomeFragment)fragmentManager.findFragmentByTag("currentNewHomeFragment");
                    currentHF.quitTimer();
                    fragmentManager.popBackStackImmediate(getResources().getString(R.string.runningActivityEntry),FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }


                if(fragmentManager.popBackStackImmediate(entryName,0))
                {
                    Log.i("back to",entryName);
                }
                else
                {

                    Log.i("first time",entryName);
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container,new CaregiverLoginFragment())
                            .replace(R.id.bottomFragmentContainer,new Fragment())
                            .addToBackStack(entryName)
                            .commit();
                }

            }
        });


        runningActivityViewModel = new ViewModelProvider(this).get(RunningActivityViewModel.class);

        runningActivityViewModel.getSelectedItem().observe(this, item->
        {

            if(item.status.toString().equals("ActivityDone"))
            {
                ActRunningOBPCallback.setEnabled(false);
                Log.i("Main Activity detection","ActivityDone detected");
                Log.i("Main act detection",Integer.toString(item.getFullReport().size()));

                Toast.makeText(this,"REPORT WILL BE SAVED",Toast.LENGTH_SHORT).show();

                SharedPreferences tempsharedPreferences = getPreferences(Context.MODE_PRIVATE);
                int actID = tempsharedPreferences.getInt("activityToRun",-100);
                iconPath = tempsharedPreferences.getString("activityIconPath",null);
                Log.i("Backfield choice","Save actId "+actID);
                dbManager.insertFullReportData(actID,this.runningActivityViewModel.getSelectedItem().getValue().getFullReport());

                //this piece of code has test purpose only !!!
                Cursor c = dbManager.retrieveReportDataByActId(actID);

                if(c.moveToFirst())
                {
                    do {
                        String rawMD =c.getString(1);
                        Log.i("rawMD",rawMD);
                        ReportData.metadataParser(rawMD);
                    }while(c.moveToNext());
                }
                //up to here

                fragmentManager.popBackStackImmediate(getResources().getString(R.string.runningActivityEntry),FragmentManager.POP_BACK_STACK_INCLUSIVE);

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.controlbarFragment,new ReportFragment())
                        .replace(R.id.fragment_container,new Fragment())
                        .replace(R.id.bottomFragmentContainer,new Fragment())
                        .addToBackStack("reportEntry")
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
            statusVM.selectItem(new MainActivityStatusData(MainActivityStatusData.Status.RunningActivity));
        }
        else
        {
            Log.i("sourceAct", "intentBundle was null");
        }

        /*fragmentManager
                .beginTransaction()
                .replace(R.id.controlbarFragment,controlbarFragment)
                .replace(R.id.fragment_container, new QuickstartMenuFragment())
                .replace(R.id.bottomFragmentContainer,bottomFragment)
                .commit();*/

        OnBackPressedCallback mainOBPCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.i("OBP callback", "main");
                //Log.i("popBack preview",fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-1).getName());
                if(fragmentManager.getBackStackEntryCount()>1) {
                    fragmentManager.popBackStackImmediate();
                }
            }
        };

        ActRunningOBPCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.i("OBP callback","ACT RUNNING VERSION");

                QuitDialog quitDialog = new QuitDialog();
                quitDialog.show(fragmentManager,null);
                this.setEnabled(false);

            }
        };

        OBPDispatcher.addCallback(this, mainOBPCallback);
        OBPDispatcher.addCallback(this,ActRunningOBPCallback);

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
