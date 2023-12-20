package com.application.bit_time.utils;

import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.R;
import com.application.bit_time.Settings_Activity.PlanningFragment;
import com.application.bit_time.utils.AlarmUtils.AlarmScheduler;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.NotificationsUtils.NotificationsSupervisor;

import java.util.ArrayList;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ListItemHolder>{

    private List<PlanningInfo> planList;
    private PlanningFragment planningFragment;

    public PlanAdapter(PlanningFragment planningFragment, List<PlanningInfo> plans)
    {
        this.planList = plans;
        this.planningFragment = planningFragment;
    }

    @NonNull
    @Override
    public PlanAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.s_single_plan_item_layout,parent,false);
                return new ListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.ListItemHolder holder, int position) {

        PlanningInfo currentPlan = planList.get(position);
        holder.id = currentPlan.getPlanId();
        holder.fullDate.setText(currentPlan.getInfo().toString());
        holder.planningInfo = new PlanningInfo(currentPlan);
        holder.planningInfo.info.setAsSet();
        Log.i("inspection","picp "+currentPlan);


    }

    @Override
    public int getItemCount() {
        int count = this.planList.size();
        Log.i("plansList count",Integer.toString(count));
        return count;
    }



    public void updatePlans(List<PlanningInfo> newPlans)
    {
        final PlansDiffCallback diffCallback = new PlansDiffCallback(this.planList,newPlans);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.planList.clear();
        this.planList.addAll(newPlans);

        for(PlanningInfo pi : planList)
        {
            Log.i("newplanList item",pi.toString());
        }

        diffResult.dispatchUpdatesTo(this);
    }

    class ListItemHolder extends RecyclerView.ViewHolder {

        int id;
        TextView fullDate;
        Button removeButton;
        PlanningInfo planningInfo;
        public ListItemHolder(View view)
        {
            super(view);
            this.fullDate = view.findViewById(R.id.fullDateTextView);
            this.removeButton = view.findViewById(R.id.deletePlanButton);
            this.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("removePlanButton","pressed, will remove plan "+id);
                    DbManager dbManager = new DbManager(planningFragment.getActivity());
                    dbManager.deletePlanById(id);
                    //Cursor c = dbManager.selectAllActivitySchedule();

                    List<PlanningInfo> newplanList = new ArrayList<>();

                    AlarmScheduler alarmScheduler = new AlarmScheduler(planningFragment.getActivity());

                    alarmScheduler.cancel(planningInfo.info);

                    for(PlanningInfo pi : planList)
                    {
                        if(id != pi.getPlanId()) {
                            newplanList.add(pi);
                            Log.i("added to newplanList",pi.toString() +" "+ pi.getPlanId() + " "+id);
                        }
                    }

                    Log.i("planning info",planningInfo.toString());

                    for(PlanningInfo pi : newplanList)
                    {
                        Log.i("newplans item",pi.toString());
                    }

                    updatePlans(newplanList);
                    /*if (c.getCount()>0)
                    {
                        c.moveToFirst();
                         do {
                             Log.i("ACTSCHED report",""+c.getInt(0)+ " " +c.getInt(1) + " "+ c.getInt(2));
                         }while(c.moveToNext());
                    }*/

                }
            });

        }
    }


}
