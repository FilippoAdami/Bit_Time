package com.application.bit_time;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {


    RunningActivityViewModel runningActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment bottomFragment = new RunningTaskFragment();

        runningActivityViewModel = new ViewModelProvider(this).get(RunningActivityViewModel.class);

        runningActivityViewModel.getSelectedItem().observe(this, item->
        {
            if(item.getStatus().toString().equals("ActivityDone"))
            {
                Log.i("Main Activity detection","ActivityDone detected");
                Log.i("Main act detection",Integer.toString(item.getReportDataList().size()));
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer,new ReportFragment())
                        .detach(bottomFragment)
                        .commit();
            }
        });



        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, new HomeFragment())
                           .replace(R.id.bottomFragmentContainer,bottomFragment);
        fragmentTransaction.commit();
    }
}
