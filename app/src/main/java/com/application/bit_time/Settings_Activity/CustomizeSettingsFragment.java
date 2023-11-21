package com.application.bit_time.Settings_Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.widget.Toast;

import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CustomizeSettingsFragment extends Fragment {
    DbManager dbManager;

    private LinearLayout themeDefault;
    private LinearLayout themeBW;
    private LinearLayout themeVivid;
    private LinearLayout themeEarth;
    private Button loadBackgroundButton;
    private TextView backgroundText;
    private final ActivityResultLauncher<String> backgroundPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                // Handle the result, 'uri' contains the selected image URI
                if (uri != null) {
                    //Save the new background in the database
                    String image = saveImageToInternalStorage(uri);
                    backgroundText.setText(image);
                    dbManager.changeBackground(image);
                }
            }
    );
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Button loadRingtoneButton;
    private CheckBox checkbox1;
    private SeekBar volumeSeekBar;
    private Button loadRingtoneButton1;
    private Uri tempUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_customize_settings_layout, container, false);

        dbManager = new DbManager(getActivity());
        // Initialize UI elements
        themeDefault = view.findViewById(R.id.ThemeColumn1);
        themeVivid = view.findViewById(R.id.ThemeColumn2);
        themeEarth = view.findViewById(R.id.ThemeColumn3);
        themeBW = view.findViewById(R.id.ThemeColumn4);
        loadBackgroundButton = view.findViewById(R.id.loadBackgroundButton);
        backgroundText = view.findViewById(R.id.currentBackground);
        checkbox1 = view.findViewById(R.id.checkbox1);
        volumeSeekBar = view.findViewById(R.id.volumeSeekBar);
        loadRingtoneButton1 = view.findViewById(R.id.loadRingtoneButton1);

        // Set the saved values
        String currentTheme = dbManager.getTheme();
        Log.i("currentTheme", currentTheme);
        highlightCurrentTheme(currentTheme);

        String currentBackground = dbManager.getBackground();
        backgroundText.setText(currentBackground);

        themeDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the layout click event
                switchTheme("PastelTheme");
            }
        });
        themeVivid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the layout click event
                switchTheme("VividTheme");
            }
        });
        themeEarth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the layout click event
                switchTheme("EarthTheme");
            }
        });
        themeBW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the layout click event
                switchTheme("BWTheme");
            }
        });
        loadBackgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] supportedExtensions = {"jpg", "jpeg", "png", "bmp", "webp"};
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg");

                if (mimeType != null) {
                    for (String extension : supportedExtensions) {
                        mimeType += "|" + MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                    }
                }

                backgroundPickerLauncher.launch(mimeType);
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

    private void highlightCurrentTheme(String currentTheme) {
        // Clear previous highlights
        clearThemeHighlights();

        // Highlight the current theme with a blue border
        switch (currentTheme) {
            case "PastelTheme":
                themeDefault.setBackgroundResource(R.drawable.theme_border);
                break;
            case "BWTheme":
                themeBW.setBackgroundResource(R.drawable.theme_border);
                break;
            case "EarthTheme":
                themeEarth.setBackgroundResource(R.drawable.theme_border);
                break;
            case "VividTheme":
                themeVivid.setBackgroundResource(R.drawable.theme_border);
                break;
        }
    }
    private void clearThemeHighlights() {
        // Clear all theme highlights by removing the blue border
        themeDefault.setBackgroundResource(0);
        themeBW.setBackgroundResource(0);
        themeEarth.setBackgroundResource(0);
        themeVivid.setBackgroundResource(0);
    }
    private void switchTheme(String theme) {
        //get current theme
        String currentTheme = dbManager.getTheme();
        int newTheme = R.style.PastelTheme;
        switch (theme) {
            case "PastelTheme":
                if (currentTheme.equals("PastelTheme")) {
                    return;
                }
                newTheme = R.style.PastelTheme;
                currentTheme = "PastelTheme";
                break;
            case "BWTheme":
                if (currentTheme.equals("BWTheme")) {
                    return;
                }
                newTheme = R.style.BWTheme;
                currentTheme = "BWTheme";
                break;
            case "EarthTheme":
                if (currentTheme.equals("EarthTheme")) {
                    return;
                }
                newTheme = R.style.EarthTheme;
                currentTheme = "EarthTheme";
                break;
            case "VividTheme":
                if (currentTheme.equals("VividTheme")) {
                    return;
                }
                newTheme = R.style.VividTheme;
                currentTheme = "VividTheme";
                break;
            default:
                currentTheme.equals("PastelTheme");
                break;
        }
        //set new theme
        highlightCurrentTheme(currentTheme);
        dbManager.changeTheme(currentTheme);
        requireActivity().setTheme(newTheme);
    }

    private String saveImageToInternalStorage(Uri uri) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            tempUri = uri;
            // Request the permission
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            return(saveOperations(uri));
        }
        return null;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, perform the operation that requires this permission
                if(tempUri != null){
                saveOperations(tempUri);}
            } else {
                // Permission denied, show a toast and close the picker
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                tempUri = null;
            }
        }
    }
    public String saveOperations(Uri uri){

        String imagePath = null;

        try {
            // Open an InputStream from the selected image URI
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);

            // Determine the file extension from the MIME type
            String extension;
            String mimeType = getActivity().getContentResolver().getType(uri);
            if ("image/jpeg".equals(mimeType)) {
                extension = ".jpg";
            } else if ("image/png".equals(mimeType)) {
                extension = ".png";
            } else {
                // Handle unsupported file types
                return null;
            }

            // Create a unique file name
            String fileName = "background_image" + extension;

            // Create an OutputStream to write the image to internal storage
            FileOutputStream outputStream = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);

            // Copy the image data from InputStream to OutputStream
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close the streams
            inputStream.close();
            outputStream.close();

            // Get the path of the saved image
            File file = new File(getActivity().getFilesDir(), fileName);
            imagePath = file.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }

        return imagePath;
    }

    private void openRingtonePicker() {
        // Implement your code to open a file picker for ringtone selection
    }

    private void updateVolume(int volume) {
        // Implement your code to update the volume
        // You can use AudioManager or other appropriate APIs
    }
}
