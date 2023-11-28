package com.application.bit_time.Settings_Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.R;
import com.application.bit_time.utils.AlarmUtils.AlarmInfo;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.SubtasksViewModel;
import com.application.bit_time.utils.SubtasksViewModelData;

import java.util.ArrayList;

public class PlanningFragment extends Fragment {

    SubtasksViewModel subtasksViewModel;

    ArrayList<AlarmInfo> currentPlanning;
    int n;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        n=0;

        this.currentPlanning = new ArrayList<>();

        subtasksViewModel = new ViewModelProvider(this.getActivity()).get(SubtasksViewModel.class);



        DbManager dbManager = new DbManager(this.getActivity());
        Cursor c = dbManager.getActivityScheduleInfo(subtasksViewModel.getSelectedItem().getValue().getActivityId());

        if(c.getCount()>0)
        {

            Log.i("PLAN FRAG","actual schedule for the selected activity");
            c.moveToFirst();

            do {
                Log.i("PLAN FRAG",""+c.getInt(2)+" "+c.getInt(3)+" "+c.getInt(4)+" "+c.getInt(5)+" "+c.getInt(6));
                currentPlanning.add(new AlarmInfo(c.getInt(2),c.getInt(3),c.getInt(4),c.getInt(5),c.getInt(6)));
            }while(c.moveToNext());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.s_planning_fragment_layout,container,false);

        Button addPlanBtn = view.findViewById(R.id.addPlanButton);
        TextView statusTextView = view.findViewById(R.id.statusTextView);

        if(n==0)
        {
            statusTextView.setText("Al momento non è stata pianificata l'activity");
        }
        else
        {
            statusTextView.setVisibility(View.INVISIBLE);
        }

        addPlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager childFManager = getChildFragmentManager();
                childFManager.beginTransaction()
                        .add(R.id.planFragmentContainer,new PlanFragment(),"currentPlanFragment")
                        .commit();

               }
        });


        return view;
    }
}
