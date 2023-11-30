package com.application.bit_time.utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.R;
import com.application.bit_time.Settings_Activity.PlanningFragment;
import com.application.bit_time.utils.Db.DbManager;

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



    }

    @Override
    public int getItemCount() {
        int count = this.planList.size();
        Log.i("plansList count",Integer.toString(count));
        return count;
    }

    class ListItemHolder extends RecyclerView.ViewHolder {

        int id;
        TextView fullDate;
        Button removeButton;
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

                }
            });

        }
    }
}
