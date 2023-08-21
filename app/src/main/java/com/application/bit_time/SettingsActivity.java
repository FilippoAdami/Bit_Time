package com.application.bit_time;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private SubtasksViewModel subtasksViewModel;
    private SubtasksViewModel dbTasksViewModel;
    private DbManager dbManager;
    private CustomViewModel viewModel;
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


        dbViewModel = new ViewModelProvider(this).get(DbViewModel.class);
        subtasksViewModel = new ViewModelProvider(this).get("subTasksVM",SubtasksViewModel.class);
        dbTasksViewModel = new ViewModelProvider(this).get("DbTasksVM",SubtasksViewModel.class);



        subtasksViewModel.getSelectedItem().observe(this,item ->
        {

            Log.i("SETT ACT ",item.toString());
        });

        dbManager = new DbManager(getApplicationContext());

        dbViewModel = new ViewModelProvider(this).get(DbViewModel.class);

        currentDbViewModelData = new DbViewModelData(dbViewModel.getSelectedItem().getValue());


        viewModel = new ViewModelProvider(this).get(CustomViewModel.class);

        fManager = getSupportFragmentManager();
        Fragment frag = fManager.findFragmentById(R.id.fragmentsContainer);

        upperFrag = new SettingsUpperFragment();
        middleFrag = new SettingsMiddleFragment();
        lowerFrag = new SettingsLowerFragmentTasks();



        dbViewModel.getSelectedItem().observe(this, item ->
        {
            Log.i("FROM SETTINGS ACTIVITY","something has changed");

            Log.i("FROM SETTINGS ACTIVITY","old was "+currentDbViewModelData.toString());

            Log.i("FROM SETTINGS ACTIVITY"," new is "+item.toString());


            if(!currentDbViewModelData.taskToAdd.equals(item.taskToAdd))
            {
                Log.i("FROM SETTINGS ACTIVITY"," entro qui ed inserisco task");
                dbManager.insertTaskRecord(item.taskToAdd.getName(),item.taskToAdd.getDuration());

            }
            else if(!currentDbViewModelData.taskToDelete.equals(item.taskToDelete))
            {
                Log.i("FROM SETTINGS ACTIVITY","now i would delete the selected task, implement the method");
                dbManager.deleteTask(item.taskToDelete);
            }
            else if(!currentDbViewModelData.activityToAdd.equals(item.activityToAdd))
            {
                Log.i("FROM SETTINGS ACTIVITY"," entro qui ed inserisco act");
                //dbManager.insertActivityRecord(item.activityToAdd.getName(),item.activityToAdd.getTime());

            }
            else if(!currentDbViewModelData.activityToDelete.equals(item.activityToDelete))
            {
                Log.i("FROM SETTINGS ACTIVITY"," entro qui e cancello act");
                dbManager.deleteActivity(item.activityToDelete);

                lowerFrag = new SettingsLowerFragment(SettingsModeData.Mode.Activities.toString());

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

                Log.i("SettingsActivity VM","item : "+item.toString());

                if(frag == null)
                {
                    //Log.i("INFOZ","entro in if");
                    if(item.equals("Tasks"))
                    {
                        taskRender();
                    }
                    else if(item.equals("Activities"))
                    {
                        activitiesRender();
                    }
                }

                if(item.equals("NewTask"))
                {
                    newTaskRender();
                }else if(item.equals("NewActivity"))
                {
                    newActivityRender();
                }else if(item.equals("ModifyActivity"))
                {
                    Toast.makeText(getApplicationContext(),"i should modify data",Toast.LENGTH_SHORT);
                }else if(item.equals("ModifyTask"))
                {
                    //Toast.makeText(this,"received modify Task",Toast.LENGTH_SHORT).show();
                    modifyTask();


                }

            });
    }



    private void taskRender()
    {
        Fragment lowerFrag = new SettingsLowerFragmentTasks();

        fManager.beginTransaction()
                .replace(R.id.bottom_fragment_container_view,lowerFrag)
                .commit();

        //Log.i("SettingsActivity VM","taskRender called");

    }

    private void activitiesRender()
    {
        lowerFrag = new SettingsLowerFragmentActivities();
        fManager.beginTransaction()
                .replace(R.id.bottom_fragment_container_view,lowerFrag)
                .commit();

        //Log.i("SettingsActivity VM","activitiesRender called");
    }


    @Override
    protected void onDestroy() {
        dbManager.closeDb();
        super.onDestroy();
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

    private void modifyTask()
    {
        upperFrag = new ModifyTasksFragment();

        fManager.beginTransaction()
                .replace(R.id.top_fragment_container_view,upperFrag)
                .remove(middleFrag)
                .remove(lowerFrag)
                .commit();
    }


}