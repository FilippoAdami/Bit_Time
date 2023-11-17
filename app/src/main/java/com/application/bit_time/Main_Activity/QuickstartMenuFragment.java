package com.application.bit_time.Main_Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.R;
import com.application.bit_time.utils.ActivityInfo;
import com.application.bit_time.utils.ActivityItem;
import com.application.bit_time.utils.Db.DbContract;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.QuickstartAdapter;
import com.application.bit_time.utils.TaskItem;

import java.util.ArrayList;
import java.util.List;

public class QuickstartMenuFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private DbManager dbManager;
    private List<ActivityInfo> activitiesList;
    private RecyclerView recyclerView;
    private QuickstartAdapter adapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        this.dbManager = new DbManager(this.getContext());

        this.activitiesList = new ArrayList<>();

        Cursor allActivitiesCursor = this.dbManager.selectAllActivities();

        if(allActivitiesCursor.getCount()>0) {
            allActivitiesCursor.moveToFirst();

            do {
                ActivityInfo currentAIInfo = new ActivityInfo(
                        allActivitiesCursor.getInt(0),
                        allActivitiesCursor.getString(1),
                        allActivitiesCursor.getInt(2));

                Log.i("QMF db action",currentAIInfo.toString());

                TaskItem[] currentAISubtasks = new TaskItem[DbContract.Activities.DIM_MAX];

                /*List<TaskItem> currentAISubtasksList = new ArrayList<>(this.dbManager.retrieveSubtasks(allActivitiesCursor));

                int i =0;

                for(TaskItem ti : currentAISubtasksList )
                {
                    Log.i("retrieved ti",ti.toString());
                    currentAISubtasks[i]= new TaskItem(ti);
                    i++;
                }*/



                this.activitiesList.add(new ActivityInfo(currentAIInfo));

            }while(allActivitiesCursor.moveToNext());


        }




        this.adapter = new QuickstartAdapter(this,this.activitiesList);

        Log.i("QMF","onCreate called");

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.a_quickstart_menu_layout,container,false);

        this.recyclerView = view.findViewById(R.id.quickstartRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());

        this.recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),LinearLayoutManager.VERTICAL));

        this.recyclerView.setAdapter(this.adapter);



        return view;
    }
}
