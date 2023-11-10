package com.application.bit_time.Settings_Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.utils.ActivityInfo;
import com.application.bit_time.utils.ActivityItem;
import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbViewModelData;
import com.application.bit_time.utils.SubtaskAdapter;
import com.application.bit_time.utils.SubtasksViewModel;
import com.application.bit_time.utils.SubtasksViewModelData;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.Db.DbContract;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.Db.DbViewModel;

import java.util.Arrays;
import java.util.List;

public class ActivityCreationFragment extends Fragment {

    private DbManager dbManager;
    private RecyclerView subtasksRecyclerView;
    private SubtaskAdapter subtaskAdapter;
    private CustomViewModel viewModel;

    private SubtasksViewModel subtasksViewModel;
    private DbViewModel dbViewModel;
    private TaskItem[] subtasksToAdd;

    private int idToBeModified;
    private String activityName;
    private SettingsModeData currentState;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);
        dbManager = new DbManager(getContext());
        subtasksViewModel = new ViewModelProvider(requireActivity()).get("subTasksVM",SubtasksViewModel.class);
        dbViewModel = new ViewModelProvider(requireActivity()).get(DbViewModel.class);

        idToBeModified=-1;

        subtasksToAdd = new TaskItem[DbContract.Activities.DIM_MAX];

        for(int i =0 ;i <DbContract.Activities.DIM_MAX ; i++)
        {
            subtasksToAdd[i] = new TaskItem();
        }

        currentState = viewModel.getSelectedItem().getValue();

        if(currentState.equals("NewActivity"))
        {
            this.subtasksViewModel.selectItem(new SubtasksViewModelData());
        }else if(currentState.equals("ModifyActivity"))
        {
            ActivityItem activityToModifyInfo = dbViewModel.getSelectedItem().getValue().activityItem;

            subtasksViewModel.getSelectedItem().observe(this,item -> {
                if(subtasksViewModel.getSelectedItem().getValue().isAlreadyModified())
                {
                    for(TaskItem ti : subtasksViewModel.getSelectedItem().getValue().subtasks)
                    {
                        Log.i("ti modified",ti.toString());
                    }

                }
                else
                {
                    Log.i("ti modified","not really");
                }
            });



            TaskItem[] sharedSubtasks = new TaskItem[DbContract.Activities.DIM_MAX];

            int pos =0 ;
            for(TaskItem ti : activityToModifyInfo.getSubtasks())
            {

                Log.i("subtask shared",ti.toString());
                sharedSubtasks[pos]=new TaskItem(ti);
                pos++;
            }


            //Log.i("contentz",dbViewModel.getSelectedItem().getValue().toString());

            //Cursor activityToModifyData = dbManager.searchActivityById(activityToModifyInfo.getIdInt());

            //activityToModifyData.moveToFirst();

            this.activityName = activityToModifyInfo.getName();//activityToModifyData.getString(1);
            this.idToBeModified= activityToModifyInfo.getInfo().getIdInt();//activityToModifyData.getInt(0);

            for(int i = 0; i< DbContract.Activities.DIM_MAX; i++)
            {
                int subtaskToAddId = sharedSubtasks[i].getID();//activityToModifyData.getInt(3+i);
                if(subtaskToAddId > -1) {

                    subtasksToAdd[i] = new TaskItem(sharedSubtasks[i]);//dbManager.searchTask(subtaskToAddId);
                    Log.i("subtoAdd", subtasksToAdd[i].toString());
                }
                /*else
                {
                    subtasksToAdd[i]=new TaskItem();
                }*/
            }

            subtasksViewModel.selectItem(new SubtasksViewModelData(subtasksToAdd,null));

        }


        List<TaskItem> subtasksList =  Arrays.asList(subtasksToAdd);
        subtaskAdapter = new SubtaskAdapter(this,subtasksList);

        SubtasksViewModelData AdaptData = subtasksViewModel.getSelectedItem().getValue();

        if(AdaptData.subtaskAdapter == null)
        {
            Log.i("adaptData state"," was null");
            AdaptData.subtaskAdapter = subtaskAdapter;
            Log.i("adaptData state","now is "+ AdaptData.subtaskAdapter.toString());
            AdaptData.setActivityId(idToBeModified);
            subtasksViewModel.selectItem(AdaptData);
        }

        Log.i("subtest",subtasksViewModel.toString());

        subtasksViewModel.getSelectedItem().observe(this,item -> {

            for (int i = 0; i < item.subtasks.length; i++) {
                Log.i("ACF subtasks[i]= ", item.subtasks[i].toString());

                subtasksToAdd[i] = new TaskItem(item.subtasks[i]);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_creation_fragment_layout,container,false);

        TextView nameLabel = view.findViewById(R.id.editNameLabel);
        TextView totalTimelabel = view.findViewById(R.id.totalTimeLabel);
        Button addButton = view.findViewById(R.id.addTaskButton);
        Button endButton = view.findViewById(R.id.fineButton);


        if(currentState.equals("ModifyActivity"))
        {
            nameLabel.setHint(activityName);
        }



        subtasksRecyclerView = view.findViewById(R.id.subtasksRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        subtasksRecyclerView.setLayoutManager(layoutManager);
        subtasksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        subtasksRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),LinearLayoutManager.VERTICAL));

        subtasksRecyclerView.setAdapter(subtaskAdapter);

        subtasksViewModel.getSelectedItem().observe(this,item ->
                {

                    int totalTime = 0;

                    for (int i = 0; i < DbContract.Activities.DIM_MAX; i++) {
                        if (!subtasksToAdd[i].isEqualToEmpty()) {
                            totalTime = totalTime + subtasksToAdd[i].getDurationInt();
                        }
                    }

                    totalTimelabel.setText(Integer.toString(totalTime));
                });



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getContext(),"pressed and all ok", Toast.LENGTH_SHORT).show();
                //SubtasksViewModelData SVMData = subtasksViewModel.getSelectedItem().getValue();
                //SVMData.setSubtasks(subtasksToAdd);
                //subtasksViewModel.selectItem(SVMData);
                TaskSelectionDialog taskSelectionDialog = new TaskSelectionDialog();
                taskSelectionDialog.show(getActivity().getSupportFragmentManager(),null);
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),nameLabel.getText().toString(),Toast.LENGTH_SHORT).show();


                DbViewModelData newData = new DbViewModelData();
                newData.selector=DbViewModelData.ITEM_TYPE.ACTIVITY;


                //dbViewModel.selectItem();
                if(viewModel.getSelectedItem().getValue().equals("NewActivity")) {
                    ActivityItem activity = new ActivityItem(nameLabel.getText().toString(),-1, subtasksToAdd);
                   // dbManager.insertActivityRecord(new ActivityItem(nameLabel.getText().toString(),-1, subtasksToAdd));
                    newData.action= DbViewModelData.ACTION_TYPE.INSERT;
                    newData.activityItem= new ActivityItem(activity);


                    Log.i("activity is",activity.toString());
                    for(TaskItem ti : activity.getSubtasks())
                    {
                        Log.i("actsubtaksks are",ti.toString());
                    }
                }
                else if(viewModel.getSelectedItem().getValue().equals("ModifyActivity"))
                {

                    int[] subtasksId = new int[DbContract.Activities.DIM_MAX];

                    int duration = 0;
                    for(int i = 0;i< DbContract.Activities.DIM_MAX; i++)
                    {
                        subtasksId[i] = subtasksToAdd[i].getID();
                        duration += subtasksToAdd[i].getDurationInt();
                        Log.i("changessub",Integer.toString(subtasksId[i]));
                    }

                    String currentName;

                    if(nameLabel.length()>0) {
                        currentName = nameLabel.getText().toString();
                    }
                    else
                    {
                        currentName = activityName;
                    }

                    Log.i("ACT_CRE_FRA",Integer.toString(idToBeModified));
                    Log.i("ACT_CRE_FRA",activityName);
                    Log.i("ACT_CRE_FRA",Integer.toString(duration));

                    newData.action= DbViewModelData.ACTION_TYPE.MODIFY;
                    newData.activityItem=new ActivityItem(Integer.toString(idToBeModified),currentName,Integer.toString(duration),subtasksId);

                    /*dbViewModel.selectItem(new DbViewModelData(
                            DbViewModelData.ACTION_TYPE.MODIFY,
                            DbViewModelData.ITEM_TYPE.ACTIVITY,
                            new ActivityItem(Integer.toString(idToBeModified),activityName,Integer.toString(duration),subtasksId)));*/
                    //dbManager.modifyActivity(dbViewModel.getSelectedItem().getValue().activityToModify.getIdInt(), nameLabel.getText().toString(),Integer.parseInt(totalTimelabel.getText().toString()),subtasksId);
                    //dbManager.modifyActivity(dbViewModel.getSelectedItem().getValue().activityItem.getInfo(),subtasksId);
                }

                dbViewModel.selectItem(newData);


                viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.MainEntry));

            }
        });


        return view;
    }



}
