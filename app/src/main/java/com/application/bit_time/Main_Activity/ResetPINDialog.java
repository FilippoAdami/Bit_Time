package com.application.bit_time.Main_Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.application.bit_time.utils.Db.DbManager;

public class ResetPINDialog extends DialogFragment {

    private DbManager dbManager;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        dbManager = new DbManager(getActivity());

        builder.setMessage("il PIN verrà resettato a 0000, continuare?");
        builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Reset the PIN to null
                dbManager.updatePin("0");
                //show a toast message
                Toast.makeText(getActivity(), "PIN has been reset to 0000", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Annulla", null);

        return builder.create();
    }
}

