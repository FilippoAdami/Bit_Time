package com.application.bit_time.Settings_Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.application.bit_time.R;

public class GAccountPopUpFragment extends DialogFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_g_account_popup_layout, container, false);

        Button connectButton = view.findViewById(R.id.connectButton);
        Button dismissButton = view.findViewById(R.id.dismissButton);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle connecting with Google
                // You can launch the Google Sign-In flow from here.
            }
        });

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the pop-up fragment
                dismiss();
            }
        });

        return view;
    }

}
