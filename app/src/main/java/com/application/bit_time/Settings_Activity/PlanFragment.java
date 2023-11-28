package com.application.bit_time.Settings_Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.R;
import com.application.bit_time.utils.AlarmUtils.AlarmInfo;
import com.application.bit_time.utils.AlarmUtils.AlarmScheduler;
import com.application.bit_time.utils.AlarmUtils.DatePlanDialog;
import com.application.bit_time.utils.AlarmUtils.TimePlanDialog;
import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.PlannerViewModel;
import com.application.bit_time.utils.SubtasksViewModel;
import com.application.bit_time.utils.SubtasksViewModelData;

import java.util.Date;

public class PlanFragment extends Fragment {


    CustomViewModel viewModel;
    SubtasksViewModel subtasksViewModel;
    PlannerViewModel plannerViewModel;

    AlarmInfo alarmToPlan;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plannerViewModel = new ViewModelProvider(this.getActivity()).get(PlannerViewModel.class);
        subtasksViewModel = new ViewModelProvider(this.getActivity()).get("subTasksVM",SubtasksViewModel.class);
        viewModel  = new ViewModelProvider(this.getActivity()).get(CustomViewModel.class);





    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_plan_fragment_layout,container,false);

        Button pickDateBtn = view.findViewById(R.id.pickDateButton);
        Button pickTimeBtn = view.findViewById(R.id.pickTimeButton);
        Button ScheduleBtn = view.findViewById(R.id.scheduleButton);
        TextView DateText = view.findViewById(R.id.dateTextView);
        TextView TimeText = view.findViewById(R.id.timeTextView);


        plannerViewModel.getSelectedItem().observe(this, item->
        {
            Log.i("PlanFragment observs",item.toString());

            if(item.isDateSet())
            {
                DateText.setText(item.printDate());
            }

            if(item.isTimeSet())
            {
                TimeText.setText(item.printTime());
            }


        });





        pickTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePlanDialog planningDialog = new TimePlanDialog();
                planningDialog.show(getChildFragmentManager(),"time_picker");

            }
        });

        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePlanDialog datePlanDialog = new DatePlanDialog();
                datePlanDialog.show(getActivity().getSupportFragmentManager(),"date_picker");

            }
        });


        ScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alarmToPlan = plannerViewModel.getSelectedItem().getValue();



                if(alarmToPlan.isDateSet() && alarmToPlan.isTimeSet())
                {
                    DbManager dbManager = new DbManager(getContext());

                    alarmToPlan = plannerViewModel.getSelectedItem().getValue();
                    if(viewModel.getSelectedItem().getValue().equals("ModifyActivity"))
                    {
                        SubtasksViewModelData svmData = subtasksViewModel.getSelectedItem().getValue();
                        Log.i("PLANFRAG",svmData.toStringId());
                        AlarmScheduler scheduler = new AlarmScheduler(getContext());
                        scheduler.schedule(alarmToPlan);
                        Log.i("PLANFRAGMENT success","set at "+alarmToPlan.printDate()+" "+alarmToPlan.printTime());

                        dbManager.insertActivitySchedule(svmData.getActivityId(),alarmToPlan.getInfoGC());
                    }

                }
                else
                {
                    Log.i("PLANFRAGMENT alert","Some alarmInfo fields are still empty");
                }



            }
        });







        return view;
    }


}
