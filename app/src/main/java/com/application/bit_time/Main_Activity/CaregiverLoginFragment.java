package com.application.bit_time.Main_Activity;

import android.content.SharedPreferences;
import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.application.bit_time.Settings_Activity.SettingsHomeFragment;
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
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous fragment or perform another action
                requireActivity().onBackPressed();
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPIN = PIN_txt_input.getText().toString();
                if (enteredPIN.equals(dbManager.getUserPin())) {
                    // Create an Intent to start the SettingsActivity
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    // Start the new activity
                    startActivity(intent);
                } else {
                    // Show error message
                    Toast.makeText(getActivity(), "Incorrect PIN. Please try again."+dbManager.getUserPin()+"", Toast.LENGTH_SHORT).show();
                }


            }
        });

        Button forgotPasswordButton = rootView.findViewById(R.id.forgotPasswordButton);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the custom dialog fragment
                ResetPINDialog dialogFragment = new ResetPINDialog();
                dialogFragment.show(getParentFragmentManager(), "ForgotPasswordDialog");
            }
        });

        return rootView;
    }
    private void openSettingsActivity() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }
}

