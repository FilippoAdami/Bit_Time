package com.application.bit_time.Settings_Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.R;
import com.application.bit_time.utils.ActivityItem;
import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.utils.Db.DbContract;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.ListAdapter;
import com.application.bit_time.utils.SettingsModeData;
import com.application.bit_time.utils.TaskAdapter;
import com.application.bit_time.utils.TaskItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonSettingsLowerFragment extends Fragment {

    private  CustomViewModel customViewModel;
    private String modeStr;
    private DbManager dbManager;

    private boolean addFABisPressed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle argsBundle = getArguments();

        modeStr = argsBundle.getString("mode","noValue");
        Log.i("CommonSettingsLowerFrag","modeStr = "+modeStr);

        dbManager = new DbManager(this.getContext());

        customViewModel = new ViewModelProvider(this.getActivity()).get(CustomViewModel.class);



    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.new_s_settings_lower_fragment_layout,container,false);

        FloatingActionButton addFAB = view.findViewById(R.id.addFAB);
        Button addTaskButton = view.findViewById(R.id.addTaskMenuButton);
        Button addActivityButton = view.findViewById(R.id.addActivityMenuButton);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),"+task", Toast.LENGTH_SHORT).show();
                customViewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.NewTask));

            }
        });
        addActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),"+activity", Toast.LENGTH_SHORT).show();
                customViewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.NewActivity));
            }
        });

        LinearLayout menuLL = view.findViewById(R.id.FloatingAddMenu);
        addFABIsNotPressedAction(menuLL);


        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!addFABisPressed)
                {
                    addFABIsPressedAction(menuLL);


                }
                else if(addFABisPressed)
                {
                    addFABIsNotPressedAction(menuLL);
                }

            }
        });




        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),DividerItemDecoration.VERTICAL));


        if(modeStr.equals("Activities"))
        {
            List<ActivityItem> activitiesList = getActivityData(dbManager.selectAllActivities());
            Log.i("CommonSettingsLowerFrag","activitiesList dim = "+activitiesList.size());
            recyclerView.setAdapter(new ListAdapter(this,activitiesList,this.getContext()));

        }else if(modeStr.equals("Tasks"))
        {
            List<TaskItem> tasksList = getTasksData(dbManager.selectAllTasks());
            recyclerView.setAdapter(new TaskAdapter(this,tasksList,this.getContext()));
        }
        else
        {
            Log.i("modeStr behaves","not as expected");
        }

        return view;

    }


    private List<TaskItem> getTasksData(Cursor c)
    {
        List<TaskItem> taskList = new ArrayList<>();


        if(c.moveToFirst())
        {

            do{
// took also the last column to retrieve the image from db
                taskList.add(new TaskItem(c.getInt(0),c.getString(1),c.getString(2), c.getString(3)));
            }while(c.moveToNext());


        }


        return taskList;
    }

    private List<ActivityItem> getActivityData(Cursor c)
    {
        List<ActivityItem> list = new ArrayList<>();

        c.moveToFirst();
        //Log.i("HELP"," "+c.getColumnCount());

        if(c.getCount()>0)
        {
            do {

                int[] subtasksA = new int[DbContract.Activities.DIM_MAX];

                for (int i = 0; i < DbContract.Activities.DIM_MAX; i++) {
                    subtasksA[i] = c.getInt(3 + i);
                    Log.i("currentsubtask", Integer.toString(subtasksA[i]));
                }


                ActivityItem activityItem = new ActivityItem(c.getString(0), c.getString(1), c.getString(2),c.getString(9), subtasksA);
                Log.i("activity item SLA",c.getString(0)+" "+c.getString(1) + " " + c.getString(2) + " "+activityItem.getInfo().getImage());
                list.add(activityItem);
            } while (c.moveToNext());
        }

        return list;
    }


    private void addFABIsNotPressedAction(LinearLayout menuLayout)
    {
        menuLayout.setVisibility(View.INVISIBLE);
        this.addFABisPressed = false;
    }

    private void addFABIsPressedAction(LinearLayout menuLayout)
    {
        menuLayout.setVisibility(View.VISIBLE);
        this.addFABisPressed = true;
    }


}
