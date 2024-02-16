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
import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.utils.SettingsModeData;
import com.application.bit_time.utils.TaskAdapter;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.Db.DbManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SettingsLowerFragmentTasks extends Fragment
{
    private CustomViewModel customVM;
    private List<TaskItem> taskList;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private FloatingActionButton addFAB;
    private LinearLayout addLLMenu;
    private boolean isAddLLMenuVisible;
    DbManager dbManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DbManager(getContext());
        taskList  = new ArrayList<>();
        customVM= new ViewModelProvider(this.getActivity()).get(CustomViewModel.class);

        //fillTaskList();


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_s_settings_lower_fragment_layout,container,false);

        recyclerView = view.findViewById(R.id.recyclerView);
        addFAB = view.findViewById(R.id.addFAB);
        addLLMenu = view.findViewById(R.id.FloatingAddMenu);
        setAddLLMenuInvisible();

        Button addTaskButton = view.findViewById(R.id.addTaskMenuButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "addTaskMenuButton pressed", Toast.LENGTH_SHORT).show();
                customVM.selectItem(new SettingsModeData(SettingsModeData.Mode.NewTask));
            }
        });

        Button addActivityButton = view.findViewById(R.id.addActivityMenuButton);
        addActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customVM.selectItem(new SettingsModeData(SettingsModeData.Mode.NewActivity));
            }
        });


        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),"FAB pressed", Toast.LENGTH_SHORT).show();
                if(!isAddLLMenuVisible)
                {
                   setAddLLMenuVisible();
                }
                else if(isAddLLMenuVisible)
                {
                    setAddLLMenuInvisible();
                }
            }
        });


        //taskAdapter= new TaskAdapter(this,taskList,this.getContext());

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
//updated constructor call
            taskList.add(new TaskItem(c.getInt(0),c.getString(1),c.getString(2), c.getString(3)));
        }
    }

    private void setAddLLMenuVisible()
    {
        addLLMenu.setVisibility(View.VISIBLE);
        isAddLLMenuVisible=true;
    }

    private void setAddLLMenuInvisible()
    {
        addLLMenu.setVisibility(View.INVISIBLE);
        isAddLLMenuVisible = false;
    }
}
