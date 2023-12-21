package com.application.bit_time.Settings_Activity;


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
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.R;
import com.application.bit_time.utils.AlarmUtils.AlarmInfo;
import com.application.bit_time.utils.AlarmUtils.DatePlanDialog;
import com.application.bit_time.utils.AlarmUtils.TimePlanDialog;
import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.utils.ErrorDialog;
import com.application.bit_time.utils.PlannerViewModel;
import com.application.bit_time.utils.PlanningInfo;
import com.application.bit_time.utils.SubtasksViewModel;

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
        TextView dateText = view.findViewById(R.id.dateTextView);
        TextView timeText = view.findViewById(R.id.timeTextView);

        Button dailyFreqButton = view.findViewById(R.id.dailyFreqButton);
        dailyFreqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dailyFreqButton.isSelected()) {
                    dailyFreqButton.setSelected(false);
                    Log.i("dailyFreqBtnState","false");
                }
                else
                {
                    dailyFreqButton.setSelected(true);
                    Log.i("dailyFreqBtnState","true");

                }
            }
        });




        this.plannerViewModel.getSelectedItem().observe(this,item->
        {
            alarmToPlan = item.getLatestPlan().getInfo();

            Log.i("plannerVM PlanFrag",""+alarmToPlan.printFlags());
            Log.i("plannerVM PlanFrag",""+item.countPlans());

            if(alarmToPlan.isDateSet())
            {
                dateText.setText(alarmToPlan.printDate());
            }
            else
            {
                dateText.setText(R.string.pickDateButton);
            }

            if(alarmToPlan.isTimeSet())
            {
                timeText.setText(alarmToPlan.printTime());
            }
            else
            {
                timeText.setText(R.string.pickTimeButton);
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

                if(dailyFreqButton.isSelected())
                {
                    alarmToPlan.setFreq(AlarmInfo.Frequency.Daily);
                    Log.i("freq set","would schedule daily freq");
                }

                if(alarmToPlan.isDateSet() && alarmToPlan.isTimeSet())
                {

                    plannerViewModel.addPlanToSchedule(new PlanningInfo(alarmToPlan));
                    Log.i("after.addPlanToSchedule",Integer.toString(plannerViewModel.getSelectedItem().getValue().countPlans()));
                    dailyFreqButton.setSelected(false);
                    /*DbManager dbManager = new DbManager(getContext());

                    //alarmToPlan = plannerViewModel.getSelectedItem().getValue();
                    if(viewModel.getSelectedItem().getValue().equals("ModifyActivity"))
                    {
                        SubtasksViewModelData svmData = subtasksViewModel.getSelectedItem().getValue();
                        Log.i("PLANFRAG",svmData.toStringId());
                        AlarmScheduler scheduler = new AlarmScheduler(getContext());
                        scheduler.schedule(alarmToPlan);
                        Log.i("PLANFRAGMENT success","set at "+alarmToPlan.printDate()+" "+alarmToPlan.printTime());

                        dbManager.insertActivitySchedule(svmData.getActivityId(),alarmToPlan.getInfoGC());
                    }*/

                }
                else
                {
                    Log.i("PLANFRAGMENT alert","Some alarmInfo fields are still empty");

                    Bundle b = new Bundle();
                    b.putString("ErrorCode","PlanningNotFilled");
                    ErrorDialog errorDialog = new ErrorDialog();
                    errorDialog.setArguments(b);
                    errorDialog.show(getChildFragmentManager(),null);
                }



            }
        });







        return view;
    }


}
