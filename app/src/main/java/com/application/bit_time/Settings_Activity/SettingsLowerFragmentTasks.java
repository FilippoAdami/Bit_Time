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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.R;
import com.application.bit_time.utils.TaskAdapter;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.Db.DbManager;

import java.util.ArrayList;
import java.util.List;

public class SettingsLowerFragmentTasks extends Fragment
{
    private List<TaskItem> taskList;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    DbManager dbManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DbManager(getContext());
        taskList  = new ArrayList<>();
        //fillTaskList();


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.s_settings_lower_fragment_layout,container,false);

        recyclerView = view.findViewById(R.id.recyclerView);

        taskAdapter= new TaskAdapter(this,taskList,this.getContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(taskAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("SettLowFragTasks","onREsume called");
        taskList.clear();
        fillTaskList();

    }


    public void fillTaskList()
    {
        Cursor c = dbManager.selectAllTasks();


        while(c.moveToNext())
        {
            taskList.add(new TaskItem(c.getInt(0),c.getString(1),c.getString(2)));
        }
    }
}
