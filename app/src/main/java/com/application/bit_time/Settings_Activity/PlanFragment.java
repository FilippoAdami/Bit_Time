package com.application.bit_time.Settings_Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.application.bit_time.R;
import com.application.bit_time.utils.AlarmUtils.AlarmInfo;
import com.application.bit_time.utils.AlarmUtils.AlarmScheduler;
import com.application.bit_time.utils.AlarmUtils.DatePlanDialog;
import com.application.bit_time.utils.AlarmUtils.TimePlanDialog;

import java.util.Date;

public class PlanFragment extends Fragment {

    AlarmInfo alarmToPlan;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_plan_fragment_layout,container,false);

        Button pickDateBtn = view.findViewById(R.id.pickDateButton);
        Button pickTimeBtn = view.findViewById(R.id.pickTimeButton);





        pickTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePlanDialog planningDialog = new TimePlanDialog();
                planningDialog.show(getActivity().getSupportFragmentManager(),"time_picker");

            }
        });

        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePlanDialog datePlanDialog = new DatePlanDialog();
                datePlanDialog.show(getActivity().getSupportFragmentManager(),"date_picker");

            }
        });




        //TODO : these hardcoded values will be erased
        int year=2023;
        int month =11;
        int day = 27;
        int hour = 13;
        int min = 20;

        this.alarmToPlan = new AlarmInfo(year,month,day,hour,min,0);
        AlarmScheduler scheduler = new AlarmScheduler(this.getActivity().getApplicationContext());
        scheduler.schedule(alarmToPlan);


        return view;
    }
}
