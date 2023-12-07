package com.application.bit_time.Main_Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.application.bit_time.R;

public class GameFragment extends Fragment {

    private static final long DELAY_MILLIS = 5000; // 5 seconds

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.a_game_layout, container, false);

        // Post a delayed action to return to the previous fragment after 5 seconds
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(GameFragment.this).commit();
            fragmentManager.popBackStack();
        }, DELAY_MILLIS);

        return rootView;
    }
}

