package com.application.bit_time.utils.AlarmUtils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.utils.PlannerViewModel;
import com.application.bit_time.utils.PlannerViewModelData;
import com.application.bit_time.utils.PlanningInfo;

import java.util.Calendar;

public class DatePlanDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    PlannerViewModel plannerViewModel;
    AlarmInfo currentInfo;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        plannerViewModel = new ViewModelProvider(this.getActivity()).get(PlannerViewModel.class);
        currentInfo = plannerViewModel.getSelectedItem().getValue().getLatestPlan().getInfo();


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(getActivity(),this,year,month,day);
        return picker;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Log.i("DATE set","at "+i+" "+i1+" "+i2);


        currentInfo.setDate(i,i1,i2);

        PlannerViewModelData dataToBeUploaded = this.plannerViewModel.getSelectedItem().getValue();
        dataToBeUploaded.setLatestPlan(new PlanningInfo(currentInfo));
        this.plannerViewModel.selectItem(dataToBeUploaded);

    }
}
