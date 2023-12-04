package com.application.bit_time.Main_Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.application.bit_time.R;
import com.application.bit_time.Settings_Activity.SettingsActivity;
import com.application.bit_time.utils.Db.DbManager;

public class CaregiverLoginFragment extends Fragment {

    private DbManager dbManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.a_caregiver_login_layout, container, false);

        EditText PIN_txt_input = rootView.findViewById(R.id.PIN_txt_input);
        Button backButton = rootView.findViewById(R.id.backButton);
        Button logInButton = rootView.findViewById(R.id.loginButton);
        dbManager = new DbManager(getActivity());
        backButton.setOnClickListener(v -> {
            // Navigate back to the previous fragment or perform another action
            requireActivity().onBackPressed();
        });

        logInButton.setOnClickListener(v -> {
            String enteredPIN = PIN_txt_input.getText().toString();
            if (enteredPIN.equals(dbManager.getUserPin())) {
                // Create an Intent to start the SettingsActivity
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                //close the login fragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(CaregiverLoginFragment.this).commit();
                fragmentManager.popBackStack();
                requireActivity().finish();
                // Start the new activity
                startActivity(intent);
            } else {
                // Show error message
                Toast.makeText(getActivity(), "Incorrect PIN. Please try again.", Toast.LENGTH_SHORT).show();
            }


        });

        Button forgotPasswordButton = rootView.findViewById(R.id.forgotPasswordButton);
        forgotPasswordButton.setOnClickListener(v -> {
            // Show the custom dialog fragment
            ResetPINDialog dialogFragment = new ResetPINDialog();
            dialogFragment.show(getParentFragmentManager(), "ForgotPasswordDialog");
        });

        return rootView;
    }
}

