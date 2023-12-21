package com.application.bit_time.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ErrorDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        String error;
        Bundle b= getArguments();
        if(b != null)
            error= b.getString("ErrorCode","generic error");
        else
            error = "generic error";

        String title ="";
        String message ="";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(error.equals("generic error")) {
            title = "OPS ! Something got messed up";
            message = "this is a general error message";
        }
        else if(error.equals("emptyNameAct"))
        {
            title="No name for activity";
            message = "The activity needs a name to be saved";
        }
        else if(error.equals("emptyNameTask"))
        {
            title="No name for task";
            message = "The task needs a name to be saved";
        }


        builder.setTitle(title);

        builder.setMessage(message);

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        return builder.create();

    }
}
