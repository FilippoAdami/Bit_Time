package com.application.bit_time.utils.AlarmUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.utils.PlannerViewModel;

import java.util.Calendar;

public class TimePlanDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{

    PlannerViewModel plannerViewModel;
    AlarmInfo currentInfo;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        plannerViewModel = new ViewModelProvider(this.getActivity()).get(PlannerViewModel.class);
        currentInfo = plannerViewModel.getSelectedItem().getValue();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute  = calendar.get(Calendar.MINUTE);


        TimePickerDialog timePicker = new TimePickerDialog(getActivity(),this,hour,minute, DateFormat.is24HourFormat(getActivity()));
        return timePicker;

    }


    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Log.i("TIME PLAN DIAL","ready to update");

        currentInfo.setTime(i,i1);
        this.plannerViewModel.selectItem(currentInfo);
    }
}
