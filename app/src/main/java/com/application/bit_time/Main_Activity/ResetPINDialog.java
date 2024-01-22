package com.application.bit_time.Main_Activity;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.application.bit_time.utils.Db.DbManager;

public class ResetPINDialog extends DialogFragment {

    private DbManager dbManager;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        dbManager = new DbManager(getActivity());

        builder.setMessage("il PIN verrà reimpostato a 0000, continuare?");
        builder.setPositiveButton("Reset", (dialog, which) -> {
            // Reset the PIN to null
            dbManager.updatePin("0");
            //show a toast message
            Toast.makeText(getActivity(), "PIN reimpostato a 0000", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Annulla", null);

        return builder.create();
    }
}

