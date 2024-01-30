package com.application.bit_time.Settings_Activity;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import static com.application.bit_time.Settings_Activity.CustomizeSettingsFragment.REQUEST_CODE_MEDIA_PERMISSION;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.application.bit_time.Main_Activity.ControlsFragment;
import com.application.bit_time.utils.ActivityItem;
import com.application.bit_time.utils.AlarmUtils.AlarmScheduler;
import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbContract;
import com.application.bit_time.utils.PlannerViewModel;
import com.application.bit_time.utils.PlanningInfo;
import com.application.bit_time.utils.SettingsModeData;
import com.application.bit_time.utils.SubtasksViewModel;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.Db.DbViewModelData;
import com.application.bit_time.utils.TaskItem;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.annotations.Nullable;

public class SettingsActivity extends AppCompatActivity{
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
    private SharedPreferences sharedPreferences;
    AlarmScheduler alarmScheduler;
    PlannerViewModel plannerViewModel;
    //ActivityResultLauncher arl;

    @Override
    protected void onResume() {
        super.onResume();

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.SCHEDULE_EXACT_ALARM) == PERMISSION_GRANTED)
        {
            Log.i("onResumePerms","now we have them");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        OnBackPressedDispatcher OBPDispatcher = getOnBackPressedDispatcher();
        Log.i("OBP sett",OBPDispatcher.toString());
        OnBackPressedCallback mainOBPCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                Log.i("OBPCallback","in main");
                int backstackDim = fManager.getBackStackEntryCount();
                Log.i("before popping",fManager.getBackStackEntryAt(backstackDim-1).toString());
                if(backstackDim>1)
                {
                    fManager.popBackStackImmediate();
                    viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.Back));
                    Log.i("after popping",fManager.getBackStackEntryAt(backstackDim-2).toString());
                    Fragment f = fManager.findFragmentById(R.id.middle_fragment_container_view);
                    //Log.i("fragment",f.getTag().toString());

                }

            }
        };


        OBPDispatcher.addCallback(this,mainOBPCallback);

        dbManager = new DbManager(getApplicationContext());
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
        setContentView(R.layout.activity_settings);

        alarmScheduler = new AlarmScheduler(this);
        dbViewModel = new ViewModelProvider(this).get(DbViewModel.class);
        subtasksViewModel = new ViewModelProvider(this).get("subTasksVM",SubtasksViewModel.class);
        //Log.i("SETTACT svm",this.subtasksViewModel.);
        dbTasksViewModel = new ViewModelProvider(this).get("DbTasksVM",SubtasksViewModel.class);
        plannerViewModel = new ViewModelProvider(this).get(PlannerViewModel.class);
        subtasksViewModel.getSelectedItem().observe(this,item ->
        {
            Log.i("SETT ACT ",item.toString());
        });

        dbManager = new DbManager(getApplicationContext());

        dbViewModel = new ViewModelProvider(this).get(DbViewModel.class);

        currentDbViewModelData = new DbViewModelData(Objects.requireNonNull(dbViewModel.getSelectedItem().getValue()));

        viewModel = new ViewModelProvider(this).get(CustomViewModel.class);

        fManager = getSupportFragmentManager();

        //Log.i("BSECount",Integer.toString(fManager.getBackStackEntryCount()));

        //Fragment frag = fManager.findFragmentById(R.id.fragmentsContainer);


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

                    // we need to save both currentActId and the ids of the plannings saved so we
                    // will return a list where :
                    // THE FIRST ELEMENT contains the "currentActId"
                    // while the following elements are the db's ids of the plans inserted, in the order of insertion
                    List<Integer> ids = new ArrayList<>();
                    ids.add(dbManager.insertActivityRecord(currentData.activityItem));
                    currentData.activityItem.setId(ids.get(0));
                    Log.i("currentData test",currentData.activityItem.toString());
                    for(PlanningInfo pi : currentData.activityItem.getPlans())
                    {
                        Log.i("pi in SA",pi.toString());
                    }

                    //int currentActId = dbManager.insertActivityRecord(currentData.activityItem);

                    ids.addAll(dbManager.insertMultipleActivitySchedule(currentData.activityItem));

                    for(Integer id : ids)
                    {
                        Log.i("ids",id.toString());
                    }

                    Log.i("plans list contains ",Integer.toString(currentData.activityItem.getPlans().size()));

                    alarmScheduler.scheduleAll(currentData.activityItem.getPlans(),currentData.activityItem.getName(),ids);

                }
            }
            else if(currentData.action == DbViewModelData.ACTION_TYPE.DELETE)
            {
                if(currentData.selector == DbViewModelData.ITEM_TYPE.TASK)
                {
                    dbManager.deleteTask(currentData.taskItem);
                }
                else if(currentData.selector == DbViewModelData.ITEM_TYPE.ACTIVITY) {
                    dbManager.deleteActivity(currentData.activityItem.getInfo());
                    dbManager.deleteAllPlansByActivityId(currentData.activityItem.getInfo().getIdInt());
                    if (currentData.activityItem.getPlans() != null)
                        alarmScheduler.cancelAll(currentData.activityItem.getPlans());
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
                    Log.i("currentData plans check","plans inside are "+ currentData.activityItem.getPlans().size());
                    ActivityItem currentActivity = new ActivityItem(currentData.activityItem);
                    Log.i("currentActivity",currentActivity.toString());


                    for(TaskItem ti : currentActivity.getSubtasks())
                    {
                        Log.i("currAct",ti.getIdStr());
                    }

                    int[] subtasksIds= new int[DbContract.Activities.DIM_MAX];

                    int i=0;
                    for(TaskItem ti: currentActivity.getSubtasks())
                    {
                        subtasksIds[i]=ti.getID();
                        i++;
                    }

                    List<Integer> ids = new ArrayList<>(dbManager.modifyActivity(currentData.activityItem,subtasksIds));

                    if(currentActivity.getPlans()== null)
                    {
                        Log.i("SettAct here plans","gets null");
                    }
                    else
                    {
                        Log.i("id here",Integer.toString(currentActivity.getInfo().getIdInt()));
                        Log.i("SettAct here plans","is not null");
                        //List<Integer> ids = new ArrayList<>();
                        //ids.add(currentActivity.getInfo().getIdInt());
                        //ids.addAll(dbManager.insertMultipleActivitySchedule(currentActivity));
                        Log.i("ids","size "+ids.size());

                        for(Integer id : ids)
                        {
                            Log.i("idprint",Integer.toString(id));
                        }



                        alarmScheduler.scheduleAll(currentActivity.getPlans(), currentActivity.getName(),ids);

                    }

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


        //homeSettingsRedirect();

        viewModel.getSelectedItem().observe(this, item ->
                {
                    Log.i("enter","here as well "+item.toString());
                    //if (fManager.getBackStackEntryCount() > 0)
                    //    Log.i("BackStackLog UP", "back to " + fManager.getBackStackEntryAt(fManager.getBackStackEntryCount() - 1).getName());

                    //Log.i("SettingsActivity VM", "item : " + item.toString());
                    //Log.i("BSECount", Integer.toString(fManager.getBackStackEntryCount()));

                    //Log.i("SETTINGS ACT vm", item.toString());

                    //if(frag == null)
                    //{
                    //Log.i("INFOZ","entro in if");


               /* if(item.equals("Back")) {
                    if(fManager.getBackStackEntryCount()>1)
                    {
                        fManager.popBackStackImmediate();
                    }
                }*/
                    if (item.equals("BackToTasks")) {
                        taskRender();
                    } else if (item.equals("BackToActivities")) {
                        activitiesRender();
                    } else if (item.equals("Tasks")) {
                        taskRender();
                    } else if (item.equals("Activities")) {
                        activitiesRender();
                    } else if (item.equals("EntryPoint")) {
                        managementEntryPoint();
                    } else if (item.equals("NewTask")) {
                        newTaskRender();
                    } else if (item.equals("NewActivity")) {
                        newActivityRender();
                    } else if (item.equals("ModifyActivity")) {
                        //Log.i("access","access is here");
                        //Toast.makeText(getApplicationContext(),"i should modify data",Toast.LENGTH_SHORT);
                        modifyActivity();
                    } else if (item.equals("ModifyTask")) {
                        //Toast.makeText(this,"received modify Task",Toast.LENGTH_SHORT).show();
                        modifyTask();
                    } else if (item.equals("MainEntry")) {
                        homeSettingsRedirect();
                        //mainEntry();
                        //fManager.popBackStackImmediate("SettActBackStackBase",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }

                    if(fManager.getBackStackEntryCount()>0)
                        Log.i("BackStackLog DOWN","back to "+fManager.getBackStackEntryAt(fManager.getBackStackEntryCount()-1).getName());



                });

    }

    private void taskRender()
    {
        String entryName =getResources().getString(R.string.tasksSettEntry);;
        if(fManager.popBackStackImmediate(entryName,FragmentManager.POP_BACK_STACK_INCLUSIVE))
        {
            Log.i("back to",entryName);

        }/*else
        {

            Bundle b = new Bundle();
            b.putString("mode","Tasks");
            //Log.i("BackStackLog","taskRender");
            upperFrag = new SettingsUpperFragment();
            upperFrag.setArguments(b);
            middleFrag = new SettingsMiddleFragment();
            middleFrag.setArguments(b);
            lowerFrag = new SettingsLowerFragmentTasks();

            fManager.beginTransaction()
                    .replace(R.id.top_fragment_container_view,upperFrag)
                    .replace(R.id.middle_fragment_container_view,middleFrag,"taskFrag")
                    .replace(R.id.bottom_fragment_container_view,lowerFrag)
                    .addToBackStack(entryName)
                    .commit();
            //Log.i("BackStackLog","to set 'taskRender'");

        }*/


        Bundle b = new Bundle();
        b.putString("mode","Tasks");
        //Log.i("BackStackLog","taskRender");
        upperFrag = new SettingsUpperFragment();
        upperFrag.setArguments(b);
        middleFrag = new SettingsMiddleFragment();
        middleFrag.setArguments(b);
        lowerFrag = new SettingsLowerFragmentTasks();

        fManager.beginTransaction()
                .replace(R.id.top_fragment_container_view,upperFrag)
                .replace(R.id.middle_fragment_container_view,middleFrag,"taskFrag")
                .replace(R.id.bottom_fragment_container_view,lowerFrag)
                .addToBackStack(entryName)
                .commit();




        /*if(fManager.getBackStackEntryAt(fManager.getBackStackEntryCount()-1).getName().equals("ActivitiesRender")
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

        }*/

        Log.i("current dim",Integer.toString(fManager.getBackStackEntryCount()));


    }
    private void activitiesRender()
    {

        /*if(fManager.getBackStackEntryAt(fManager.getBackStackEntryCount()-1).getName().equals("ActivitiesRender")
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

        }*/

        //Log.i("SETTINGS ACT log","also here");

        String entryName = getResources().getString(R.string.settActBackStackBase);

        if(fManager.popBackStackImmediate(entryName,0))
        {
            Log.i("back to",entryName);
        }
        else
        {
            Log.i("first time of","activitiesRender");

            Bundle b= new Bundle();
            b.putString("mode","Activities");
            upperFrag = new SettingsUpperFragment();
            upperFrag.setArguments(b);
            middleFrag= new SettingsMiddleFragment();
            middleFrag.setArguments(b);
            lowerFrag = new SettingsLowerFragmentActivities();
            fManager.beginTransaction()
                    .replace(R.id.top_fragment_container_view,upperFrag)
                    .replace(R.id.middle_fragment_container_view,middleFrag,"actFrag")
                    .replace(R.id.bottom_fragment_container_view,lowerFrag)
                    .addToBackStack(entryName)
                    .commit();

            //Log.i("BackStackLog","to set 'ActivitiesRender'");
        }

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
                .replace(R.id.middle_fragment_container_view,new Fragment())
                .replace(R.id.bottom_fragment_container_view, new Fragment())
                .addToBackStack("newTaskBackStackLabel")
                .commit();
    }
    private void newActivityRender()
    {
        upperFrag = new ActivityCreationFragment();

        fManager.beginTransaction()
                .replace(R.id.top_fragment_container_view,upperFrag)
                .replace(R.id.middle_fragment_container_view,new Fragment())
                .replace(R.id.bottom_fragment_container_view,new Fragment())
                .addToBackStack("newActBackStackEntry")
                .commit();
    }
    private void modifyTask()
    {
        upperFrag = new ModifyTasksFragment();

        fManager.beginTransaction()
                .replace(R.id.top_fragment_container_view,upperFrag)
                .replace(R.id.middle_fragment_container_view,new Fragment())
                .replace(R.id.bottom_fragment_container_view,new Fragment())
                .addToBackStack("modifyT")
                .commit();
    }
    public void modifyActivity() {
        upperFrag = new ActivityCreationFragment();


        fManager.beginTransaction()
                .replace(R.id.top_fragment_container_view, upperFrag)
                .replace(R.id.middle_fragment_container_view,new Fragment())
                .replace(R.id.bottom_fragment_container_view,new Fragment())
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

    public void homeSettingsRedirect(){
        fManager.beginTransaction()
                .replace(R.id.top_fragment_container_view, new Fragment())
                .replace(R.id.middle_fragment_container_view, new SettingsHomeFragment())
                .replace(R.id.bottom_fragment_container_view, new Fragment())
                .addToBackStack("SettingsMenuBackStackLabel")
                .commit();
    }


    public void managementEntryPoint()
    {
        Log.i("BackStackLog","managmententryPoint");
        upperFrag = new SettingsUpperFragment();
        middleFrag = new SettingsMiddleFragment();
        lowerFrag = new SettingsLowerFragmentActivities();

        fManager.beginTransaction()
                .replace(R.id.top_fragment_container_view,upperFrag)
                .replace(R.id.middle_fragment_container_view,middleFrag,"actFrag")
                .replace(R.id.bottom_fragment_container_view,lowerFrag)
                .addToBackStack(getResources().getString(R.string.settActBackStackBase))
                .commit();
        Log.i("BackStackLog"," to set 'SettActBackStackBase'");


    }
}