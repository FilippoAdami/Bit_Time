package com.application.bit_time.Settings_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.application.bit_time.R;
import com.application.bit_time.SettingsActivity;

public class SettingsHomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_settings_home_layout, container, false);

        Button buttonAS = view.findViewById(R.id.buttonAS);
        Button buttonIP = view.findViewById(R.id.buttonIP);
        Button buttonNA = view.findViewById(R.id.buttonNA);

        buttonAS.setOnClickListener(v -> {
            // Navigate to FragmentA
            // Example: replaceFragment(new FragmentA());
        });

        buttonIP.setOnClickListener(v -> {
            // Navigate to FragmentB
            // Example: replaceFragment(new FragmentB());
        });

        buttonNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        });

        return view;
    }

}
