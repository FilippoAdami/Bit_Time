package com.application.bit_time.Settings_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.application.bit_time.utils.ActivityItem;
import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbContract;
import com.application.bit_time.utils.SubtasksViewModel;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.Db.DbViewModelData;
import com.application.bit_time.utils.TaskItem;

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

        Log.i("BSECount",Integer.toString(fManager.getBackStackEntryCount()));

        Fragment frag = fManager.findFragmentById(R.id.fragmentsContainer);

        upperFrag = new SettingsUpperFragment();
        middleFrag = new SettingsMiddleFragment();
        lowerFrag = new SettingsLowerFragmentTasks();

        dbViewModel.getSelectedItem().observe(this, item ->
        {
            Log.i("FROM SETTINGS ACTIVITY","something has changed");

            Log.i("FROM SETTINGS ACTIVITY","old was "+currentDbViewModelData.toString());

            Log.i("FROM SETTINGS ACTIVITY"," new is "+item.toString());

            DbViewModelData currentData = new DbViewModelData(item);

            if(currentData.action == DbViewModelData.ACTION_TYPE.INSERT)
            {
                if(currentData.selector == DbViewModelData.ITEM_TYPE.TASK)
                {
                    dbManager.insertTaskRecord(currentData.taskItem);
                }
                else if(currentData.selector == DbViewModelData.ITEM_TYPE.ACTIVITY)
                {

                    dbManager.insertActivityRecord(currentData.activityItem);
                }
            }
            else if(currentData.action == DbViewModelData.ACTION_TYPE.DELETE)
            {
                if(currentData.selector == DbViewModelData.ITEM_TYPE.TASK)
                {
                    dbManager.deleteTask(currentData.taskItem);
                }
                else if(currentData.selector == DbViewModelData.ITEM_TYPE.ACTIVITY)
                {
                    dbManager.deleteActivity(currentData.activityItem.getInfo());
                }
            }
            else if(currentData.action == DbViewModelData.ACTION_TYPE.MODIFY)
            {
                if(currentData.selector == DbViewModelData.ITEM_TYPE.TASK)
                {
                    dbManager.modifyTask(currentData.taskItem);
                }
                else if(currentData.selector == DbViewModelData.ITEM_TYPE.ACTIVITY)
                {
                    Log.i("FROM SETTINGS ACTIVITY","would modify activity");
                    ActivityItem currentActivity = currentData.activityItem;
                    Log.i("currentActivity",currentActivity.toString());
                    TaskItem[] taskItems = new TaskItem[DbContract.Activities.DIM_MAX];
                    int[] subtasksIds= new int[taskItems.length];

                    int i=0;
                    for(TaskItem ti: currentActivity.getSubtasks())
                    {
                        subtasksIds[i]=ti.getID();
                        i++;
                    }
                    dbManager.modifyActivity(currentData.activityItem.getInfo(),subtasksIds);
                }
            }



            /*if(!currentDbViewModelData.taskToDelete.equals(item.taskToDelete))
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
            else if(!currentDbViewModelData.taskToAdd.equals(item.taskToAdd))
            {
                Log.i("FROM SETTINGS ACTIVITY"," entro qui ed inserisco task");
                dbManager.insertTaskRecord(item.taskToAdd.getName(),item.taskToAdd.getDuration());
            }

            currentDbViewModelData = item;
            Log.i("updatedDbViewModelData",currentDbViewModelData.toString());*/

        });




        /*fManager.beginTransaction()
                .add(R.id.top_fragment_container_view,upperFrag)
                .add(R.id.middle_fragment_container_view,middleFrag)
                .add(R.id.bottom_fragment_container_view,lowerFrag)
                .addToBackStack("EntryPoint")
                .commit();*/
        mainEntry();


        viewModel.getSelectedItem().observe(this, item ->
            {

                Log.i("SettingsActivity VM","item : "+item.toString());
                Log.i("BSECount",Integer.toString(fManager.getBackStackEntryCount()));

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
                    Log.i("access","access is here");
                    //Toast.makeText(getApplicationContext(),"i should modify data",Toast.LENGTH_SHORT);
                    modifyActivity();
                }else if(item.equals("ModifyTask"))
                {
                    //Toast.makeText(this,"received modify Task",Toast.LENGTH_SHORT).show();
                    modifyTask();
                }
                else if(item.equals("MainEntry"))
                {
                    mainEntry();
                }
            });
    }



    private void taskRender()
    {
        lowerFrag = new SettingsLowerFragmentTasks();

        if(fManager.getBackStackEntryAt(fManager.getBackStackEntryCount()-1).getName().equals("ActivitiesRender")
        || fManager.getBackStackEntryAt(fManager.getBackStackEntryCount()-1).getName().equals("taskRender"))
        {

            if(fManager.popBackStackImmediate())
            {
                Log.i("popped","yes");
            }
            else
            {
                Log.i("popped","false");
            }

        }

        fManager.beginTransaction()
                .replace(R.id.bottom_fragment_container_view,lowerFrag)
                .addToBackStack("taskRender")
                .commit();




        Log.i("current dim",Integer.toString(fManager.getBackStackEntryCount()));


    }

    private void activitiesRender()
    {

        if(fManager.getBackStackEntryAt(fManager.getBackStackEntryCount()-1).getName().equals("ActivitiesRender")
                || fManager.getBackStackEntryAt(fManager.getBackStackEntryCount()-1).getName().equals("taskRender"))
        {

            if(fManager.popBackStackImmediate())
            {
                Log.i("popped","yes");
            }
            else
            {
                Log.i("popped","false");
            }

        }

        lowerFrag = new SettingsLowerFragmentActivities();
        fManager.beginTransaction()
                .replace(R.id.bottom_fragment_container_view,lowerFrag)
                .addToBackStack("ActivitiesRender")
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
                .addToBackStack("newTask")
                .commit();
    }

    private void newActivityRender()
    {
        upperFrag = new ActivityCreationFragment();

        fManager.beginTransaction()
                .replace(R.id.top_fragment_container_view,upperFrag)
                .remove(middleFrag)
                .remove(lowerFrag)
                .addToBackStack("newAct")
                .commit();
    }

    private void modifyTask()
    {
        upperFrag = new ModifyTasksFragment();

        fManager.beginTransaction()
                .replace(R.id.top_fragment_container_view,upperFrag)
                .remove(middleFrag)
                .remove(lowerFrag)
                .addToBackStack("modifyT")
                .commit();
    }



    public void modifyActivity() {
        upperFrag = new ActivityCreationFragment();


        fManager.beginTransaction()
                .replace(R.id.top_fragment_container_view, upperFrag)
                .remove(middleFrag)
                .remove(lowerFrag)
                .addToBackStack("modifyA")
                .commit();
    }


    public void mainEntry()
    {


        /*fManager.beginTransaction()
                .remove(upperFrag)
                .remove(middleFrag)
                .remove(lowerFrag)
                .commit();*/

        upperFrag = new SettingsUpperFragment();
        middleFrag = new SettingsMiddleFragment();
        lowerFrag = new SettingsLowerFragmentActivities();


        fManager.beginTransaction()
                .replace(R.id.top_fragment_container_view,upperFrag)
                .replace(R.id.middle_fragment_container_view,middleFrag)
                .replace(R.id.bottom_fragment_container_view,lowerFrag)
                .addToBackStack("MainEntry")
                .commit();
    }


}