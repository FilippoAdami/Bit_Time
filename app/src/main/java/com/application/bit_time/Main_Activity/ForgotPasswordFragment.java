package com.application.bit_time.Main_Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.application.bit_time.Main_Activity.TemporaryCodeFragment;
import com.application.bit_time.R;

public class ForgotPasswordFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("verrà inviato un codice temporaneo all'indirizzo ma****si@gmail.com");
        builder.setPositiveButton("Invia", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Open the TemporaryCodeFragment
                TemporaryCodeFragment temporaryCodeFragment = new TemporaryCodeFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, temporaryCodeFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        builder.setNegativeButton("Annulla", null);

        return builder.create();
    }
}

