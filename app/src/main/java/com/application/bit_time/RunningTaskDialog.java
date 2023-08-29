package com.application.bit_time;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

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


        RunningActivityData currentData = this.runningActivityViewModel.getSelectedItem().getValue();


        return new AlertDialog.Builder(getActivity())
                .setTitle("title test")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(),"POSITIVE PRESSED",Toast.LENGTH_SHORT).show();

                        currentData.setChoice(RunningActivityData.Choice.Yes);

                        runningActivityViewModel.selectItem(currentData);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(),"NEGATIVE PRESSED",Toast.LENGTH_SHORT).show();

                        currentData.setChoice(RunningActivityData.Choice.No);

                        runningActivityViewModel.selectItem(currentData);
                    }
                })
                .create();
    }
}
