package com.application.bit_time;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.widget.Button;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    private TextView clockTextView;
    private AnalogClockView analogClockView;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_layout, container, false);

        clockTextView = rootView.findViewById(R.id.clockTextView);
        analogClockView = rootView.findViewById(R.id.analogClockView);
        Button switchButton = rootView.findViewById(R.id.caregiver_button);
        Button gameButton = rootView.findViewById(R.id.games_button);

        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with a new fragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new GameFragment());
                fragmentTransaction.addToBackStack(null); // Optional: Add to back stack
                fragmentTransaction.commit();
            }
        });
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with a new fragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new CaregiverLoginFragment());
                fragmentTransaction.addToBackStack(null); // Optional: Add to back stack
                fragmentTransaction.commit();
            }
        });

        updateTime();

        return rootView;
    }

    private void updateTime() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = getCurrentTime();

                String[] parts = currentTime.split(":");
                String hours = parts[0];
                String minutes = parts[1];

                SpannableString spannableTime = new SpannableString(currentTime);
                spannableTime.setSpan(new ForegroundColorSpan(Color.BLUE), 0, hours.length(), 0);
                spannableTime.setSpan(new ForegroundColorSpan(Color.RED), hours.length() + 1, currentTime.length(), 0);
                clockTextView.setText(spannableTime);

                analogClockView.invalidate();

                updateTime();
            }
        }, 1000);
    }

    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat(" HH : mm ", Locale.getDefault());
        return timeFormat.format(new Date());
    }
}

