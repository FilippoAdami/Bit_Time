package com.application.bit_time.Settings_Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.R;
import com.application.bit_time.utils.AlarmUtils.AlarmInfo;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.PlanAdapter;
import com.application.bit_time.utils.PlanningInfo;
import com.application.bit_time.utils.SubtasksViewModel;
import com.application.bit_time.utils.SubtasksViewModelData;

import java.util.ArrayList;
import java.util.List;

public class PlanningFragment extends Fragment {

    SubtasksViewModel subtasksViewModel;
    private List<PlanningInfo> plansList;
    private RecyclerView recyclerView;
    private PlanAdapter planAdapter;
    //ArrayList<AlarmInfo> currentPlanning;
    int n;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        n=0;

        subtasksViewModel = new ViewModelProvider(this.getActivity()).get("subTasksVM",SubtasksViewModel.class);
        plansList = new ArrayList<>();
        DbManager dbManager = new DbManager(this.getActivity());
        Cursor c = dbManager.getActivityScheduleInfo(subtasksViewModel.getSelectedItem().getValue().getActivityId());

        n=c.getCount();
        if(n>0)
        {

            Log.i("PLAN FRAG","actual schedule for the selected activity");
            c.moveToFirst();

            do {
                Log.i("PLAN FRAG",""+c.getInt(0) +" "+ c.getInt(1)+" "+c.getInt(2)+" "+c.getInt(3)+" "+c.getInt(4)+" "+c.getInt(5)+" "+c.getInt(6)+" "+c.getString(7));
                plansList.add(new PlanningInfo(c.getInt(0),new AlarmInfo(c.getInt(2),c.getInt(3),c.getInt(4),c.getInt(5),c.getInt(6), AlarmInfo.Frequency.valueOf(c.getString(7)))));
            }while(c.moveToNext());
        }else
        {
            Log.i("PLAN FRAG"," c.getCount() gave 0 res");
        }





    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {





        View view = inflater.inflate(R.layout.s_planning_fragment_layout,container,false);


        Switch planningSwitch = view.findViewById(R.id.planSwitch);
        //Button addPlanBtn = view.findViewById(R.id.addPlanButton);
        TextView statusTextView = view.findViewById(R.id.statusTextView);
        recyclerView = view.findViewById(R.id.plansRecyclerView);
        planAdapter = new PlanAdapter(this,plansList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(planAdapter);

        if(n==0)
        {
            statusTextView.setText("Al momento non è stata pianificata l'activity");
        }
        else
        {
            statusTextView.setVisibility(View.INVISIBLE);
        }

        planningSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    getChildFragmentManager()
                            .beginTransaction()
                            .add(R.id.planFragmentContainer,new PlanFragment(),"currentPlanFragment")
                            .commit();
                }
                else
                {
                    Log.i("PLANNINGFRAG ALERT","here i should remove the planfragment");
                    getChildFragmentManager()
                            .beginTransaction()
                            .remove(getChildFragmentManager().findFragmentByTag("currentPlanFragment"))
                            .commit();
                }
            }
        });

        /*addPlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager childFManager = getChildFragmentManager();
                childFManager.beginTransaction()
                        .add(R.id.planFragmentContainer,new PlanFragment(),"currentPlanFragment")
                        .commit();

               }
        });*/


        return view;
    }



}
