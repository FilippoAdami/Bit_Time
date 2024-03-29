package com.application.bit_time.Settings_Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.utils.ActivityInfo;
import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.utils.ListAdapter;
import com.application.bit_time.R;
import com.application.bit_time.utils.SettingsModeData;
import com.application.bit_time.utils.TaskAdapter;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.Db.DbContract;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.Db.DbViewModelData;

import java.util.ArrayList;
import java.util.List;

public class SettingsLowerFragment extends Fragment
{


    public List<ActivityInfo> activityList;
    public List<TaskItem> taskList;
    DbContract activityContract;
    public RecyclerView recyclerView;
    ListAdapter listAdapter;
    TaskAdapter taskAdapter;
    DbManager dbManager;
    DbViewModel dbViewModel;
    DbViewModelData latestData;
    CustomViewModel viewModel;
    String mode;

    SettingsLowerFragment(String mode)
    {
        this.mode = mode;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbManager = new DbManager(getContext());

        viewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);
        dbViewModel = new ViewModelProvider(requireActivity()).get(DbViewModel.class);

        latestData = dbViewModel.getSelectedItem().getValue();

        dbViewModel.getSelectedItem().observe(this, item ->
        {

            //Log.i("FROM SETTINGS LOWER FRAG","first step of observer");

            //dbManager.deleteActivity(item.activityToDelete);

            dbManager.deleteActivity(item.activityItem.getInfo());

            if(!item.activityItem.equals(latestData.activityItem))
            {
                //Log.i("FROM SETTINGS LOWER FRAGMENT","new activity to delete");
            }




            latestData = item; // update di latest data


        });


        //Log.i("SLF","inside onCreate");


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.s_settings_lower_fragment_layout,container,false);

        recyclerView= view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),LinearLayoutManager.VERTICAL));


        /*viewModel.getSelectedItem().observe(this,item ->
        {
            if(item.equals(SettingsModeData.Mode.Tasks))*/
            if(this.mode.equals(SettingsModeData.Mode.Tasks))
            {
                taskList = new ArrayList<>();
                taskList = getTaskData(dbManager.selectAllTasks());
                //taskAdapter = new TaskAdapter(this,taskList);
                recyclerView.setAdapter(taskAdapter);
            }
            else if(this.mode.equals(SettingsModeData.Mode.Activities))
            {

                activityList = new ArrayList<>();
                activityList = getActivityData(dbManager.selectAllActivities());
                //listAdapter = new ListAdapter(this,activityList);
                recyclerView.setAdapter(listAdapter);


            }
        //});

        taskList = new ArrayList<>();
        taskList = getTaskData(dbManager.selectAllTasks());
        //taskAdapter = new TaskAdapter(this,taskList);
        recyclerView.setAdapter(taskAdapter);

        return view;

    }



    private List<ActivityInfo> getActivityData(Cursor c)
    {
        List<ActivityInfo> list = new ArrayList<>();


        Log.i("HELP","uptohere");

        while(c.moveToNext())
        {
// updated constructor call
            ActivityInfo activityInfo = new ActivityInfo(c.getString(0),c.getString(1), c.getString(2), c.getString(3));
            list.add(activityInfo);
        }

        return list;
    }

    private List<TaskItem> getTaskData(Cursor c)
    {
        List<TaskItem> list = new ArrayList<>();


        Log.i("HELP","uptohere");

        while(c.moveToNext())
        {
// updated constructor call
            TaskItem listItem = new TaskItem(-1,c.getString(1),c.getString(2), c.getString(3));
            Log.i("CREAD",listItem.toString());
            list.add(listItem);
        }

        return list;
    }

    public void notifyDataSetChanged()
    {
        this.taskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        dbManager.closeDb();
        super.onDestroy();

    }
}
