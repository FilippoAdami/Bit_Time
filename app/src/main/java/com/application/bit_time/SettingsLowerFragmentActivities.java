package com.application.bit_time;



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

import com.application.bit_time.ActivityItem;
import com.application.bit_time.CustomViewModel;
import com.application.bit_time.DbContract;
import com.application.bit_time.DbManager;
import com.application.bit_time.DbViewModel;
import com.application.bit_time.DbViewModelData;
import com.application.bit_time.ListAdapter;
import com.application.bit_time.R;
import com.application.bit_time.TaskAdapter;
import com.application.bit_time.TaskItem;

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


        //Log.i("SLF","inside onCreate");


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


        Log.i("HELP","uptohere");

        while(c.moveToNext())
        {
            ActivityItem activityItem = new ActivityItem(c.getString(0),c.getString(1), c.getString(2));
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

