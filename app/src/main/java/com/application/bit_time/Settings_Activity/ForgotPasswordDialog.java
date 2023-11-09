package com.application.bit_time.Settings_Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.application.bit_time.Main_Activity.HomeFragment;
import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.JavaMail;

public class ForgotPasswordDialog extends DialogFragment {

    private DbManager dbManager;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        dbManager = new DbManager(getActivity());

        builder.setMessage("verrà inviato un codice temporaneo alla tua email, continuare?");
        builder.setPositiveButton("Invia", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //navigate to TemporaryCodeFragment
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.middle_fragment_container_view, new TemporaryCodeFragment());
                fragmentTransaction.commit();
                //show a toast message
                Toast.makeText(getActivity(), "controlla la tua email", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Annulla", null);

        return builder.create();
    }
}

