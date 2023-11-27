package com.application.bit_time.utils.AlarmUtils;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePlanDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {



        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute  = calendar.get(Calendar.MINUTE);


        TimePickerDialog timePicker = new TimePickerDialog(getActivity(),this,hour,minute, DateFormat.is24HourFormat(getActivity()));

        return timePicker;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Log.i("TIME SET","at "+i+" "+i1);
    }
}
