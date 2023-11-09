package com.application.bit_time.Settings_Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbManager;

public class CustomizeSettingsFragment extends Fragment {

    private LinearLayout themeDefault;
    private LinearLayout themeBW;
    private LinearLayout themeVivid;
    private LinearLayout themeEarth;
    private Button loadBackgroundButton;
    private CheckBox checkbox1;
    private SeekBar volumeSeekBar;
    private Button loadRingtoneButton1;
    DbManager dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_customize_settings_layout, container, false);

        dbManager = new DbManager(getActivity());
        // Initialize UI elements
        themeDefault = view.findViewById(R.id.ThemeColumn1);
        themeBW = view.findViewById(R.id.ThemeColumn2);
        themeVivid = view.findViewById(R.id.ThemeColumn3);
        themeEarth = view.findViewById(R.id.ThemeColumn4);
        loadBackgroundButton = view.findViewById(R.id.loadBackgroundButton);
        checkbox1 = view.findViewById(R.id.checkbox1);
        volumeSeekBar = view.findViewById(R.id.volumeSeekBar);
        loadRingtoneButton1 = view.findViewById(R.id.loadRingtoneButton1);

        themeDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the layout click event
                switchTheme("default");
            }
        });
        themeBW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the layout click event
                switchTheme("bw");
            }
        });
        themeEarth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the layout click event
                switchTheme("earth");
            }
        });
        themeVivid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the layout click event
                switchTheme("vivid");
            }
        });
        loadBackgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the action to load a background
                // For example, open a file picker or gallery to select a background image
                // You can replace this with your implementation
                openBackgroundPicker();
            }
        });
        loadRingtoneButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the action to load a ringtone
                // For example, open a file picker to select a ringtone file
                // You can replace this with your implementation
                openRingtonePicker();
            }
        });
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Handle volume adjustment here
                // You can update the volume based on the progress value
                // For example, AudioManager can be used to set the volume
                updateVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Handle when the user starts touching the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Handle when the user stops touching the SeekBar
            }
        });

        return view;
    }

    private void switchTheme(String theme) {
        //get current theme
        String currentTheme = dbManager.getTheme();
        int newTheme = R.style.PastelTheme;
        switch (theme) {
            case "default":
                if (currentTheme.equals("PastelTheme")) {
                    return;
                }
                break;
            case "bw":
                if (currentTheme.equals("BWTheme")) {
                    return;
                }
                newTheme = R.style.BWTheme;
                currentTheme = "BWTheme";
                break;
            case "earth":
                if (currentTheme.equals("EarthTheme")) {
                    return;
                }
                newTheme = R.style.EarthTheme;
                currentTheme = "EarthTheme";
                break;
            case "vivid":
                if (currentTheme.equals("VividTheme")) {
                    return;
                }
                newTheme = R.style.VividTheme;
                currentTheme = "VividTheme";
                break;
            default:
                break;
        }
        //set new theme
        dbManager.changeTheme(currentTheme);
        requireActivity().setTheme(newTheme);
    }

    private final ActivityResultLauncher<String> backgroundPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                // Handle the result, 'uri' contains the selected image URI
                if (uri != null) {
                    //Save the new background in the database
                    dbManager.changeBackground(uri.toString());
                }
            }
    );
    private void openBackgroundPicker() {
        backgroundPickerLauncher.launch("image/*");
    }

    private void openRingtonePicker() {
        // Implement your code to open a file picker for ringtone selection
    }

    private void updateVolume(int volume) {
        // Implement your code to update the volume
        // You can use AudioManager or other appropriate APIs
    }
}
