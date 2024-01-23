package com.application.bit_time.Settings_Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.application.bit_time.R;

public class CustomizationSettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_settings_home_layout, container, false);

        Button buttonAS = view.findViewById(R.id.buttonAS);
        Button buttonIP = view.findViewById(R.id.buttonIP);
        Button buttonNA = view.findViewById(R.id.buttonNA);
        buttonAS.setText(R.string.customize_app);
        buttonIP.setText(R.string.gamification);
        buttonNA.setText(R.string.start_activity);

        buttonAS.setOnClickListener(v -> {
            // Replace the current fragment with a new fragment
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.middle_fragment_container_view, new CustomizeSettingsFragment());
            fragmentTransaction.addToBackStack(null); // Optional: Add to back stack
            fragmentTransaction.commit();
        });

        buttonIP.setOnClickListener(v -> {
            // Replace the current fragment with a new fragment
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.middle_fragment_container_view, new GamificationSettings());
            fragmentTransaction.addToBackStack(null); // Optional: Add to back stack
            fragmentTransaction.commit();
        });


        //set the text of the buttonNA to "sezione in fase di sviluppo"
        buttonNA.setText("Sezione in fase di sviluppo");

        buttonNA.setOnClickListener(v -> {
            //toast "sezione in fase di sviluppo"
            Toast.makeText(getActivity(), "Sezione in fase di sviluppo", Toast.LENGTH_SHORT).show();

            // Replace the current fragment with a new fragment
            /*FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.middle_fragment_container_view, new ModifyTasksFragment());
            fragmentTransaction.addToBackStack(null); // Optional: Add to back stack
            fragmentTransaction.commit();*/
        });



        return view;
    }

}
