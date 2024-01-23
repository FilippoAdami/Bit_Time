package com.application.bit_time.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.application.bit_time.R;

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

        Resources res= getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(error.equals("generic error")) {
            title = res.getString(R.string.genericErrorTitle);
            message =  res.getString(R.string.genericErrorMsg);
        }
        else if(error.equals("emptyNameAct"))
        {
            title=res.getString(R.string.emptyNameActErrorTitle);
            message = res.getString(R.string.emptyNameActErrorMsg);
        }
        else if(error.equals("emptyNameTask"))
        {
            title=res.getString(R.string.emptyNameTaskErrorTitle);
            message = res.getString(R.string.emptyNameTaskErrorMsg);
        }
        else if(error.equals("PlanningNotFilled"))
        {
            title = res.getString(R.string.planningNotFilledErrorTitle);
            message = res.getString(R.string.planningNotFilledErrorTitle);
        }
        else if(error.equals("TaskErrLen"))
        {
            title= res.getString(R.string.nameTooLongTaskErrorTitle);
            message = res.getString(R.string.nameTooLongTaskErrorMsg);
        }
        else if(error.equals("ActErrLen"))
        {
            title=res.getString(R.string.ActErrLenTitle);
            message=res.getString(R.string.ActErrLenMsg);
        }
        else if(error.equals("zeroTimeTaskErr"))
        {
            title = res.getString(R.string.zeroTimeTaskErrorTitle);
            message = res.getString(R.string.zeroTimeTaskErrorMsg);
        }
        else if(error.equals("emptySubtasksErr"))
        {
            title=res.getString(R.string.emptySubtasksErrTitle);
            message=res.getString(R.string.emptySubtasksErrMsg);
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
