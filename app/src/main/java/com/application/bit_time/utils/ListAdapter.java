package com.application.bit_time.utils;

import static android.view.View.GONE;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.R;
import com.application.bit_time.Settings_Activity.SettingsLowerFragmentActivities;
import com.application.bit_time.Settings_Activity.SettingsModeData;
import com.application.bit_time.utils.Db.DbContract;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.Db.DbViewModelData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListItemHolder>
{

    private DbManager dbManager;
    private DbViewModel dbViewModel;
    private List<ActivityItem> list;
    private SettingsLowerFragmentActivities settingsLowerFragmentActivities;
    private CustomViewModel viewModel;



    public ListAdapter(SettingsLowerFragmentActivities settingsLowerFragmentActivities, List<ActivityItem> list)
    {

        this.settingsLowerFragmentActivities = settingsLowerFragmentActivities;
        this.list = list;
        //Log.i("SUBTASKS[0] content",list.get(0).subtasks[0].toString());
        dbViewModel = new ViewModelProvider(settingsLowerFragmentActivities.requireActivity()).get(DbViewModel.class);
        viewModel = new ViewModelProvider(settingsLowerFragmentActivities.requireActivity()).get(CustomViewModel.class);
        dbManager = new DbManager(settingsLowerFragmentActivities.requireActivity());

    }


    @NonNull
    @Override
    public ListAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_fragment_layout, parent, false);

        return new ListItemHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ListItemHolder holder, int position) {

        ActivityItem activityItem = list.get(position);

        Log.i("ADAPTonbind",activityItem.toString());
        holder.bind(activityItem);
        holder.id = activityItem.activityInfo.getIdInt();


        holder.itemView.setOnClickListener(v -> {
            boolean expanded = activityItem.isExpanded();
            activityItem.setExpanded(!expanded);
            notifyItemChanged(position);
        });

        /*holder.labelName.setText(activityItem.getName());
        holder.labelTime.setText(activityItem.getTime());
        holder.id = activityItem.activityInfo.getIdInt();
        //holder.subtask1.setText(activityItem.subtasks[0].getName());*/



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateActivityListItems(List<ActivityItem> newList)
    {
        Log.i("dims",Integer.toString(this.list.size())+" "+Integer.toString(newList.size()));
        final ActivityDiffCallback diffCallback = new ActivityDiffCallback(this.list,newList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        for(ActivityItem ai : list)
        {
            Log.i("listprint",ai.toString());
        }

        this.list.clear();
        this.list.addAll(newList);

        for(ActivityItem ai : list)
        {
            Log.i("newlistprint",ai.toString());
        }


        diffResult.dispatchUpdatesTo(this);
    }



    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        int id;
        TextView labelName;
        TextView labelTime;
        Button modifyButton;
        Button deleteButton;
        LinearLayout subitem;
        TextView[] subtasks;

        TaskItem[] subtaskItems;
        TaskItem ti0;

        public ListItemHolder (View view)
        {
            super(view);
            ti0 = new TaskItem();
            this.subtaskItems = new TaskItem[DbContract.Activities.DIM_MAX];

            labelName = view.findViewById(R.id.labelName);
            labelTime = view.findViewById(R.id.labelTime);
            modifyButton = view.findViewById(R.id.modifyButton);
            deleteButton = view.findViewById(R.id.deleteButton);
            subitem = view.findViewById(R.id.sub_item);

            subtasks = new TextView[DbContract.Activities.DIM_MAX];
            subtasks[0] = view.findViewById(R.id.subtaskLabel1);
            subtasks[0].setVisibility(GONE);
            subtasks[1] = view.findViewById(R.id.subtaskLabel2);
            subtasks[1].setVisibility(GONE);
            subtasks[2] = view.findViewById(R.id.subtaskLabel3);
            subtasks[2].setVisibility(GONE);
            subtasks[3] = view.findViewById(R.id.subtaskLabel4);
            subtasks[3].setVisibility(GONE);
            subtasks[4] = view.findViewById(R.id.subtaskLabel5);
            subtasks[4].setVisibility(GONE);

            view.setClickable(true);
            view.setOnClickListener(this);




            modifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "would modify "+ id,Toast.LENGTH_SHORT).show();


                    DbViewModelData newDbData = new DbViewModelData(
                            DbViewModelData.ACTION_TYPE.MODIFY,
                            DbViewModelData.ITEM_TYPE.ACTIVITY,
                            new ActivityItem());
                    newDbData.activityItem.activityInfo = new ActivityInfo(
                            id,
                            labelName.getText().toString(),
                            labelTime.getText().toString());

                    newDbData.activityItem.subtasks = new TaskItem[DbContract.Activities.DIM_MAX];

                    int i =0;
                    for(TaskItem ti : subtaskItems)
                    {
                        newDbData.activityItem.subtasks[i]= new TaskItem(ti);
                        i++;
                    }


                    Log.i("actInfo",newDbData.activityItem.activityInfo.toString());
                    Log.i("ti0",ti0.toString());

                    for(TaskItem ti : newDbData.activityItem.subtasks)
                    {
                        Log.i("LISTADAPTER sub",ti.toString());
                    }

                    dbViewModel.selectItem(newDbData);
                    viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.ModifyActivity));

                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // magari aggiungiamo una richiesta di conferma

                    ActivityInfo itemToDelete = new ActivityInfo(id,labelName.getText().toString(),labelTime.getText().toString());
                    ActivityItem item = dbManager.searchActivityItem(itemToDelete);


                    DbViewModelData newData = new DbViewModelData(DbViewModelData.ACTION_TYPE.DELETE, DbViewModelData.ITEM_TYPE.ACTIVITY,new ActivityItem());


                    Log.i("TEST",""+labelName.getText().toString()+" "+labelTime.getText().toString());
                    Log.i("TEST","immediatly after "+ item.getName());


                    newData.activityItem.activityInfo = itemToDelete;

                    Log.i("TEST","immediatly after again "+ newData.activityItem.getName());

                    dbViewModel.selectItem(newData);

                    List<ActivityItem> newList = new ArrayList<>(list);
                    newList.remove(item);
                    Log.i("newlist",newList.get(0).toString());
                    updateActivityListItems(newList);



                }
            });

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "item pressed this is the action",Toast.LENGTH_SHORT);

            //this.subtask1.setVisibility(View.INVISIBLE);

        }

        private void bind(ActivityItem activityItem) {

            Log.i("bind","is called");
            boolean expanded = activityItem.isExpanded();
            int subtasksNum = activityItem.subtasks.length;

            subitem.setVisibility(expanded ? View.VISIBLE : GONE);

            labelName.setText(activityItem.getName());
            labelTime.setText(activityItem.getTime());



            Log.i("ti0 from bind",ti0.toString());
            //Log.i("subtasks from bind",subtaskItems[0].toString());

            for(int i = 0; i < subtasksNum ; i++)
            {

                if(activityItem.subtasks[i].getID() != -1) {
                    TaskItem ti = new TaskItem(dbManager.searchTask(activityItem.subtasks[i].getID()));

                    if(this.subtaskItems[i] == null)
                    {
                        Log.i("subtaskItems","this is null");
                    }
                    ti0 = ti;


                    Log.i("ti test",ti.toString());

                    subtasks[i].setText(ti.getName() + "    " + ti.getDurationInt());
                    subtasks[i].setVisibility(View.VISIBLE);
                }
            }

            //Log.i("print test",subtasksItems[subtasksItems.length-1].toString());
        }
    }
}
