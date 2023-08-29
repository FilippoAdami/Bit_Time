package com.application.bit_time.Main_Activity;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.application.bit_time.Main_Activity.ForgotPasswordFragment;
import com.application.bit_time.R;
import com.application.bit_time.SettingsActivity;

public class CaregiverLoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.a_caregiver_login_layout, container, false);

        Button backButton = rootView.findViewById(R.id.backButton);
        Button logInButton = rootView.findViewById(R.id.loginButton);
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
                openSettingsActivity();
            }
        });

        Button forgotPasswordButton = rootView.findViewById(R.id.forgotPasswordButton);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the custom dialog fragment
                ForgotPasswordFragment dialogFragment = new ForgotPasswordFragment();
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

