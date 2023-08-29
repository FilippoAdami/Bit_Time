package com.application.bit_time.Settings_Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.application.bit_time.R;

public class Settings_home_fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_settings_home_layout, container, false);

        Button buttonA = view.findViewById(R.id.buttonA);
        Button buttonB = view.findViewById(R.id.buttonB);
        Button buttonC = view.findViewById(R.id.buttonC);

        buttonA.setOnClickListener(v -> {
            // Navigate to FragmentA
            // Example: replaceFragment(new FragmentA());
        });

        buttonB.setOnClickListener(v -> {
            // Navigate to FragmentB
            // Example: replaceFragment(new FragmentB());
        });

        buttonC.setOnClickListener(v -> {
            // Navigate to FragmentC
            // Example: replaceFragment(new FragmentC());
        });

        return view;
    }

    // Add your navigation method (e.g., replaceFragment) here
}
