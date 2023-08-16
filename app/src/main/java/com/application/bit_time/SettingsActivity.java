package com.application.bit_time;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends AppCompatActivity {

    private DbManager dbManager;
    private CustomViewModel  viewModel;
    private DbViewModel dbViewModel;
    FragmentManager fManager;
    Fragment upperFrag;
    Fragment middleFrag;
    Fragment lowerFrag;

    DbViewModelData currentDbViewModelData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        dbManager = new DbManager(getApplicationContext());

        //Log.i("DBLOG",dbManager.getDbName());

        dbViewModel = new ViewModelProvider(this).get(DbViewModel.class);
        //Log.i("dbViewModel",dbViewModel.getSelectedItem().getValue().toString());

        currentDbViewModelData = new DbViewModelData(dbViewModel.getSelectedItem().getValue());

        //currentDbViewModelData = new DbViewModelData();
        /*currentDbViewModelData.activityToAdd = dbViewModel.getSelectedItem().getValue().activityToAdd;
        currentDbViewModelData.activityToDelete = dbViewModel.getSelectedItem().getValue().activityToDelete;
        currentDbViewModelData.taskToDelete = dbViewModel.getSelectedItem().getValue().taskToDelete;
        currentDbViewModelData.taskToAdd = dbViewModel.getSelectedItem().getValue().taskToAdd;*/



        viewModel = new ViewModelProvider(this).get(CustomViewModel.class);
        viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.Tasks));

        fManager = getSupportFragmentManager();
        Fragment frag = fManager.findFragmentById(R.id.fragmentsContainer);

        upperFrag = new SettingsUpperFragment();
        middleFrag = new SettingsMiddleFragment();
        lowerFrag = new SettingsLowerFragment();



        dbViewModel.getSelectedItem().observe(this, item ->
        {
            Log.i("FROM SETTINGS ACTIVITY","something has changed");

            Log.i("FROM SETTINGS ACTIVITY","old was "+currentDbViewModelData.toString());

            Log.i("FROM SETTINGS ACTIVITY"," new is "+item.toString());


            if(!currentDbViewModelData.taskToAdd.equals(item.taskToAdd))
            {
                Log.i("FROM SETTINGS ACTIVITY"," entro qui");
                dbManager.insertTaskRecord(item.taskToAdd.getName(),item.taskToAdd.getDuration());

            }
            else if(!currentDbViewModelData.taskToDelete.equals(item.taskToDelete))
            {
                Log.i("FROM SETTINGS ACTIVITY","now i would delete the selected task, implement the method");
            }
            else if(!currentDbViewModelData.activityToAdd.equals(item.activityToAdd))
            {
                Log.i("FROM SETTINGS ACTIVITY"," entro qui");
                dbManager.insertActivityRecord(item.activityToAdd.getName(),item.activityToAdd.getTime());

            }
            else if(!currentDbViewModelData.activityToDelete.equals(item.activityToDelete))
            {
                Log.i("FROM SETTINGS ACTIVITY"," entro qui");
                dbManager.deleteActivity(item.activityToDelete);

                lowerFrag = new SettingsLowerFragment();

                fManager.beginTransaction()
                        .replace(R.id.bottom_fragment_container_view,lowerFrag)
                        .commit();
            }

        });




        fManager.beginTransaction()
                .add(R.id.top_fragment_container_view,upperFrag)
                .add(R.id.middle_fragment_container_view,middleFrag)
                .add(R.id.bottom_fragment_container_view,lowerFrag)
                .commit();


        viewModel.getSelectedItem().observe(this, item ->
            {
                if(frag == null)
                {
                    //Log.i("INFOZ","entro in if");
                    if(item.equals("Tasks"))
                    {
                    }
                    else if(item.equals("Activities"))
                    {

                    }
                }

                if(item.equals("NewTask"))
                {
                    newTaskRender();
                }else if(item.equals("NewActivity"))
                {
                    newActivityRender();
                }

            });
    }



    private void taskRender()
    {
        Fragment lowerFrag = new SettingsLowerFragment();

    }

    private void activitiesRender()
    {
        lowerFrag = new PlaceholderFragment();

        fManager.beginTransaction()
                .replace(R.id.bottom_fragment_container_view,lowerFrag)
                .commit();
    }


    private void newTaskRender()
    {
        upperFrag = new CreationUpperFragment();

        fManager.beginTransaction()
                .replace(R.id.top_fragment_container_view,upperFrag)
                .remove(middleFrag)
                .remove(lowerFrag)
                .commit();
    }

    private void newActivityRender()
    {
        upperFrag = new ActivityCreationFragment();

        fManager.beginTransaction()
                .replace(R.id.top_fragment_container_view,upperFrag)
                .remove(middleFrag)
                .remove(lowerFrag)
                .commit();
    }


}