package com.application.bit_time.utils;

import static android.view.View.GONE;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.R;
import com.application.bit_time.Settings_Activity.CommonSettingsLowerFragment;
import com.application.bit_time.Settings_Activity.SettingsLowerFragmentActivities;
import com.application.bit_time.utils.AlarmUtils.AlarmInfo;
import com.application.bit_time.utils.Db.DbContract;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.Db.DbViewModelData;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListItemHolder>
{
    private SubtasksViewModel subtasksViewModel;
    private DbManager dbManager;
    private DbViewModel dbViewModel;
    private List<ActivityItem> list;
    private CommonSettingsLowerFragment settingsLowerFragmentActivities;
    private CustomViewModel viewModel;
    private Context context;


    public ListAdapter(CommonSettingsLowerFragment settingsLowerFragmentActivities, List<ActivityItem> list, Context context)
    {

        for(ActivityItem AI : list) {
            Log.i("ListAdapterImage", AI.getInfo().getImage());
        }
        this.settingsLowerFragmentActivities = settingsLowerFragmentActivities;
        this.list = list;
        this.context = context;
        //Log.i("SUBTASKS[0] content",list.get(0).subtasks[0].toString());
        dbViewModel = new ViewModelProvider(settingsLowerFragmentActivities.requireActivity()).get(DbViewModel.class);
        viewModel = new ViewModelProvider(settingsLowerFragmentActivities.requireActivity()).get(CustomViewModel.class);
        dbManager = new DbManager(settingsLowerFragmentActivities.requireActivity());
        subtasksViewModel = new ViewModelProvider(settingsLowerFragmentActivities.requireActivity()).get("subTasksVM",SubtasksViewModel.class);
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
        holder.duration=activityItem.getInfo().getTimeInt();
// icon path
        holder.iconPathah = activityItem.activityInfo.getImage();
        String imagePath = activityItem.activityInfo.getImage();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//decodeFile(imagePath);
        holder.iconah.setImageBitmap(bitmap);

        Bitmap originalEditBitmap = BitmapFactory.decodeResource(this.context.getResources(),R.drawable.edit);
        Bitmap editBitmap = Bitmap.createScaledBitmap(originalEditBitmap,100,100,true);
        holder.modifyButton.setImageBitmap(editBitmap);

        Bitmap originalDeleteBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.delete);
        Bitmap deleteBitmap = Bitmap.createScaledBitmap(originalDeleteBitmap,100,100,true);
        holder.deleteButton.setImageBitmap(deleteBitmap);

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
        int duration;
// img & path added
        ImageView iconah;
        String iconPathah;
        TextView labelName;
        TextView labelTime;
        ImageButton modifyButton;
        ImageButton deleteButton;
        LinearLayout subitem;
        TextView[] subtasks;

        TaskItem[] subtaskItems;

        public ListItemHolder (View view)
        {
            super(view);
            this.subtaskItems = new TaskItem[DbContract.Activities.DIM_MAX];
            //this.subtaskItems[0] = new TaskItem(4,"provainseritam",15);

            for(int i = 0;i<DbContract.Activities.DIM_MAX ; i++)
            {
                this.subtaskItems[i] = new TaskItem();
                Log.i("initSubtaskItems",this.subtaskItems[i].toString());
            }


            labelName = view.findViewById(R.id.labelName);
            labelTime = view.findViewById(R.id.labelTime);
//img in layout reference
            iconah = view.findViewById(R.id.activityIcon);

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
                    Log.i("MODIFY","pressed");
                    Log.i("MODIFY","would modify "+ id);
                    //Toast.makeText(view.getContext(), "would modify "+ id,Toast.LENGTH_SHORT).show();
// updated constructor call
                    ActivityInfo activityToModifyInfo = new ActivityInfo(id,labelName.getText().toString(),duration, iconPathah);


                    Log.i("ACT INFO",activityToModifyInfo.toString());
                    TaskItem[] subtasksToBeAdded = new TaskItem[subtaskItems.length];

                    int i =0;
                    for(TaskItem ti : subtaskItems)
                    {
                        subtasksToBeAdded[i]= new TaskItem(ti);
                        Log.i("SUBT",subtasksToBeAdded[i].toString());
                        i++;
                    }






                    ActivityItem activityToModify = new ActivityItem(activityToModifyInfo,subtasksToBeAdded);
                    Log.i("ACTIVITY TO MOD",activityToModify.toString());
                    SubtasksViewModelData SVMData = subtasksViewModel.getSelectedItem().getValue();
                    SVMData.setActivityToModify(activityToModify);
                    Log.i("subtcheck",SVMData.toString());
                    subtasksViewModel.selectItem(SVMData);

                    Log.i("DATA","SUBMITTED");
                    /*DbViewModelData newDbData = new DbViewModelData(
                            DbViewModelData.ACTION_TYPE.MODIFY,
                            DbViewModelData.ITEM_TYPE.ACTIVITY,
                            new ActivityItem());


                    newDbData.activityItem.subtasks = new TaskItem[DbContract.Activities.DIM_MAX];




                    Log.i("actInfo",newDbData.activityItem.activityInfo.toString());

                    for(TaskItem ti : newDbData.activityItem.subtasks)
                    {
                        Log.i("LISTADAPTERsub",ti.toString());
                    }


                    dbViewModel.selectItem(newDbData);*/
                    viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.ModifyActivity));

                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // magari aggiungiamo una richiesta di conferma

                    Log.i("justtest",labelTime.getText().toString());
// updated constructor call
                    ActivityInfo itemToDelete = new ActivityInfo(id,labelName.getText().toString(),duration, iconPathah);
                    ActivityItem item = dbManager.searchActivityItem(itemToDelete);


                    DbViewModelData newData = new DbViewModelData(DbViewModelData.ACTION_TYPE.DELETE, DbViewModelData.ITEM_TYPE.ACTIVITY,new ActivityItem());


                    Log.i("TEST",""+labelName.getText().toString()+" "+labelTime.getText().toString());
                    Log.i("TEST","immediatly after "+ item.getName());


                    newData.activityItem.activityInfo = itemToDelete;

                    Cursor c = dbManager.getActivityScheduleInfo(id);

                    if(c.getCount()>0)
                    {
                        List<PlanningInfo> plans = new ArrayList<>();
                        c.moveToFirst();

                        do{
                            plans.add(new PlanningInfo(new AlarmInfo(c.getInt(2),c.getInt(3),c.getInt(4),c.getInt(5),c.getInt(6))));
                        }while(c.moveToNext());


                        newData.activityItem.setPlans(plans);
                    }



                    Log.i("TEST","immediatly after again "+ newData.activityItem.getName());

                    dbViewModel.selectItem(newData);

                    List<ActivityItem> newList = new ArrayList<>(list);
                    Log.i("itemToRemove",item.toString());
                    boolean res = newList.remove(item);
                    Log.i("remove res",Boolean.toString(res));
                    if(newList.size()>0)
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

            boolean expanded = activityItem.isExpanded();
            int subtasksNum = activityItem.subtasks.length;
            Log.i("subtasksNum",Integer.toString(subtasksNum));

            subitem.setVisibility(expanded ? View.VISIBLE : GONE);

            labelName.setText(activityItem.getName());
            labelTime.setText(activityItem.getTimeHelper().toStringShrt());

            //Log.i("subtasks from bind",subtaskItems[0].toString());

            for(int i = 0; i < subtasksNum ; i++)
            {

                if(activityItem.subtasks[i] != null) {
                    TaskItem ti = new TaskItem(dbManager.searchTask(activityItem.subtasks[i].getID()));

                    this.subtaskItems[i]= new TaskItem(ti);


                    subtasks[i].setText(ti.getName() + "    " + ti.getFormattedDuration());
                    subtasks[i].setVisibility(View.VISIBLE);
                }
                else
                {
                    this.subtaskItems[i]=new TaskItem();
                }

                Log.i("this subtask is",this.subtaskItems[i].toString());
            }

            //Log.i("print test",subtasksItems[subtasksItems.length-1].toString());
        }
    }
}
