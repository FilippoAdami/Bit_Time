package com.application.bit_time;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {


    RunningActivityViewModel runningActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();


        // da qui
        setContentView(R.layout.activity_main_wrapper_layout);
        Log.i("FMANAGER log",fragmentManager.toString());
        Fragment wrapperFragment = new WrapperFragment();

        fragmentManager
                .beginTransaction()
                .add(R.id.mainActivityFragmentWrapper,wrapperFragment,null)
                .commit();


        /*FragmentManager childFragmentManager = wrapperFragment.getChildFragmentManager();
        Log.i("FMANAGER log",childFragmentManager.toString());
        Fragment firstChild = new HomeFragment();

        childFragmentManager.beginTransaction()
                .add(firstChild,null)
                .commit();*/




        // a qui


        //Fragment bottomFragment = new RunningTaskFragment();
        Fragment bottomFragment = new HomeBottomFragment();
        Fragment upperFragment = new HomeFragment();

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


        // questo lo ho tolto provando a mettere i children fragment

        /*FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, upperFragment)
                           .replace(R.id.bottomFragmentContainer,bottomFragment);
        fragmentTransaction.commit();*/
    }
}
