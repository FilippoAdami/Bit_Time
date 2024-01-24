package com.application.bit_time.Main_Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.R;
import com.application.bit_time.utils.RunningActivityViewModel;

public class RunningTaskDialog extends DialogFragment {

    private RunningActivityViewModel runningActivityViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        runningActivityViewModel = new ViewModelProvider(this.getActivity()).get(RunningActivityViewModel.class);


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {



        newRunningActivityData currentData = this.runningActivityViewModel.getSelectedItem().getValue();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int lastedTime = sharedPreferences.getInt("lastedTime",-1);
        String currentTaskName = sharedPreferences.getString("currentTaskName","");
        String message = getString(R.string.terminateTaskAlertMsg).replace("thistask",currentTaskName);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.terminateTaskAlertTitle)
                .setMessage(message)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(getContext(),"POSITIVE PRESSED",Toast.LENGTH_SHORT).show();

                        currentData.setAsTerminated(lastedTime);
                        Log.i("datafromdialog",currentData.toString());
                        runningActivityViewModel.selectItem(currentData);
                    }
                })
                /*.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(getContext(),"NEGATIVE PRESSED",Toast.LENGTH_SHORT).show();
                    }
                })*/
                .create();
    }
}
