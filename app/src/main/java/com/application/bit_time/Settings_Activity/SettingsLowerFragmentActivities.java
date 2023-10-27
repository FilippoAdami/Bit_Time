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
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.application.bit_time.utils.ActivityItem;
import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.utils.ListAdapter;
import com.application.bit_time.R;
import com.application.bit_time.utils.TaskAdapter;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.Db.DbContract;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.Db.DbViewModelData;

import java.util.ArrayList;
import java.util.List;

public class SettingsLowerFragmentActivities extends Fragment
{


    private List<ActivityItem> activityList;
    private List<TaskItem> taskList;
    DbContract activityContract;
    private RecyclerView recyclerView;
    ListAdapter listAdapter;
    TaskAdapter taskAdapter;
    DbManager dbManager;
    DbViewModel dbViewModel;
    DbViewModelData latestData;
    CustomViewModel viewModel;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbManager = new DbManager(getContext());

        viewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);
        dbViewModel = new ViewModelProvider(requireActivity()).get(DbViewModel.class);

        latestData = dbViewModel.getSelectedItem().getValue();

        dbViewModel.getSelectedItem().observe(this, item ->
        {

            Log.i("FROM SETTINGS LOWER FRAGMENT","first step of observer");

            dbManager.deleteActivity(item.activityToDelete);

            if(!item.activityToDelete.equals(latestData.activityToDelete))
            {
                Log.i("FROM SETTINGS LOWER FRAGMENT","new activity to delete");
            }

            latestData = item; // update di latest data


        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        Log.i("LOWFRAGACT","uptohere bro");
        View view = inflater.inflate(R.layout.settings_lower_fragment_layout,container,false);

        recyclerView= view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),LinearLayoutManager.VERTICAL));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        activityList = new ArrayList<>();
        activityList = getActivityData(dbManager.selectAllActivities());
        listAdapter = new ListAdapter(this,activityList);
        Log.i("LOWFRAGACT adaptState",listAdapter.toString());

        recyclerView.setAdapter(listAdapter);

        return view;

    }


    private List<ActivityItem> getActivityData(Cursor c)
    {
        List<ActivityItem> list = new ArrayList<>();


        Log.i("HELP"," "+c.getColumnCount());

        while(c.moveToNext())
        {

            int[] subtasksA = new int[DbContract.Activities.DIM_MAX];

            for(int i = 0; i< DbContract.Activities.DIM_MAX; i++)
            {
                subtasksA[i] = c.getInt(3+i);
                Log.i("currentsubtask",Integer.toString(subtasksA[i]));
            }


            ActivityItem activityItem = new ActivityItem(c.getString(0),c.getString(1), c.getString(2),subtasksA);
            //Log.i("activity item",c.getString(0)+" "+c.getString(1) + " " + c.getString(2));
            list.add(activityItem);
        }


        return list;
    }




    @Override
    public void onDestroy() {
        dbManager.closeDb();
        super.onDestroy();

    }
}

