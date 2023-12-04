package com.application.bit_time.Settings_Activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.RingtoneManager;
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

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.widget.Switch;
import android.widget.Toast;

import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomizeSettingsFragment extends Fragment {
    DbManager dbManager;
    private boolean preferencesChanged = false;
    private LinearLayout themeDefault;
    private LinearLayout themeBW;
    private LinearLayout themeVivid;
    private LinearLayout themeEarth;
    public String currentTheme;
    public String newTheme;
    private String currentBackground;
    private String backgroundExtension;
    private final ActivityResultLauncher<String> backgroundPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                // Handle the result, 'uri' contains the selected image URI
                if (uri != null) {
                    //Save the new background in the database
                    type = "background";
                    currentBackground = uri.toString();
                    backgroundExtension = saveToInternalStorage(uri);
                }
            }
    );
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private CheckBox checkbox3;
    private CheckBox checkbox4;
    private int currentVolume;
    private Uri currentRingtone;
    private String ringtoneExtension;
    private Uri currentNotification;
    private String notificationExtension;
    private Switch switch1;
    private Uri tempUri;
    private String type = "background";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_customize_settings_layout, container, false);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(preferencesChanged) {
                    showConfirmationDialog();
                } else {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().remove(CustomizeSettingsFragment.this).commit();
                    fragmentManager.popBackStack();
                }
            }
        });
        dbManager = new DbManager(getActivity());
        // Initialize UI elements
        themeDefault = view.findViewById(R.id.ThemeColumn1);
        themeVivid = view.findViewById(R.id.ThemeColumn2);
        themeEarth = view.findViewById(R.id.ThemeColumn3);
        themeBW = view.findViewById(R.id.ThemeColumn4);
        Button loadBackgroundButton = view.findViewById(R.id.loadBackgroundButton);
        checkbox1 = view.findViewById(R.id.checkbox1);
        checkbox2 = view.findViewById(R.id.checkbox2);
        checkbox3 = view.findViewById(R.id.checkbox3);
        checkbox4 = view.findViewById(R.id.checkbox4);
        SeekBar volumeSeekBar = view.findViewById(R.id.volumeSeekBar);
        Button loadRingtoneButton1 = view.findViewById(R.id.loadRingtoneButton1);
        Button loadNotificationButton = view.findViewById(R.id.loadRingtoneButton2);
        Button saveButton = view.findViewById(R.id.saveButton);
        switch1 = view.findViewById(R.id.switch1);

        // Set the saved values
        currentTheme = dbManager.getTheme();
        highlightCurrentTheme(currentTheme);
        currentBackground = dbManager.getBackground();
        boolean check1 = dbManager.getSounds();
        checkbox1.setChecked(check1);
        boolean check2 = dbManager.getNotifications();
        checkbox2.setChecked(check2);
        boolean check3 = dbManager.getFocus();
        checkbox3.setChecked(check3);
        boolean check4 = dbManager.getGamification();
        checkbox4.setChecked(check4);
        volumeSeekBar.setProgress(dbManager.getVolume());
        switch1.setChecked(dbManager.getHomeType());

        // Set the click listeners
        themeDefault.setOnClickListener(v -> {
            // Handle the layout click event
            highlightCurrentTheme("PastelTheme");
        });
        themeVivid.setOnClickListener(v -> {
            // Handle the layout click event
            highlightCurrentTheme("VividTheme");
        });
        themeEarth.setOnClickListener(v -> {
            // Handle the layout click event
            highlightCurrentTheme("EarthTheme");
        });
        themeBW.setOnClickListener(v -> {
            // Handle the layout click event
            highlightCurrentTheme("BWTheme");
        });
        loadBackgroundButton.setOnClickListener(v -> {
            String[] supportedExtensions = {"jpg", "jpeg", "png", "bmp", "webp"};
            StringBuilder mimeType = new StringBuilder(Objects.requireNonNull(MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg")));

            for (String extension : supportedExtensions) {
                mimeType.append("|").append(MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension));
            }

            backgroundPickerLauncher.launch(mimeType.toString());
        });
        checkbox1.setOnCheckedChangeListener((buttonView, isChecked) -> preferencesChanged = true);
        checkbox2.setOnCheckedChangeListener((buttonView, isChecked) -> preferencesChanged = true);
        checkbox3.setOnCheckedChangeListener((buttonView, isChecked) -> preferencesChanged = true);
        checkbox4.setOnCheckedChangeListener((buttonView, isChecked) -> preferencesChanged = true);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int volume, boolean fromUser) {
                currentVolume = volume;
                preferencesChanged = true;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        loadRingtoneButton1.setOnClickListener(v -> {
            type = "ringtone";
            try {
                retrieveAndShowRingtones();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        loadNotificationButton.setOnClickListener(v -> {
            type = "notification";
            try {
                retrieveAndShowRingtones();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> preferencesChanged = true);
        saveButton.setOnClickListener(v -> {
            // Handle the save button click event
            updateUserData();
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
                if (this.currentTheme.equals("PastelTheme")) {
                    return;
                }
                preferencesChanged = true;
                break;
            case "BWTheme":
                themeBW.setBackgroundResource(R.drawable.theme_border);
                if (this.currentTheme.equals("BWTheme")) {
                    return;
                }
                preferencesChanged = true;
                break;
            case "EarthTheme":
                themeEarth.setBackgroundResource(R.drawable.theme_border);
                if (this.currentTheme.equals("EarthTheme")) {
                    return;
                }
                preferencesChanged = true;
                break;
            case "VividTheme":
                themeVivid.setBackgroundResource(R.drawable.theme_border);
                if (this.currentTheme.equals("VividTheme")) {
                    return;
                }
                preferencesChanged = true;
                break;
        }
        newTheme = currentTheme;
    }
    private void clearThemeHighlights() {
        // Clear all theme highlights by removing the blue border
        themeDefault.setBackgroundResource(0);
        themeBW.setBackgroundResource(0);
        themeEarth.setBackgroundResource(0);
        themeVivid.setBackgroundResource(0);
    }
    private void switchTheme(String theme) {
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
                currentTheme ="PastelTheme";
                break;
        }
        //set new theme
        dbManager.changeTheme(currentTheme);
        requireActivity().setTheme(newTheme);
    }
    private String saveToInternalStorage(Uri uri) {
        //check API version
        int apiLevel = android.os.Build.VERSION.SDK_INT;
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (apiLevel > 32){
            permission = Manifest.permission.READ_MEDIA_AUDIO;
            if(type.equals("background")) {
                permission = Manifest.permission.READ_MEDIA_IMAGES;
            }
        }
        if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED) {
            preferencesChanged = true;
            tempUri = uri;
            // Request the permission
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {Log.i("type", "saveToInternalStorage: "+type);
            if (type.equals("background")){
                return(saveImageOperations(uri));
            }else if (type.equals("notification") || type.equals("ringtone")){
                return(saveRingtoneOperations(uri));
            }
        }
        return null;
    }
    public String saveImageOperations(Uri uri){
        // Determine the file extension from the MIME type
        String extension;
        String mimeType = requireActivity().getContentResolver().getType(uri);
        if ("image/jpeg".equals(mimeType)) {
            extension = ".jpg";
        } else if ("image/png".equals(mimeType)) {
            extension = ".png";
        } else {
            // Toast unsupported file types
            Toast.makeText(getActivity(), "Unsupported file type", Toast.LENGTH_SHORT).show();
            return null;
        }
        Toast.makeText(getActivity(), "Background changed", Toast.LENGTH_SHORT).show();
        return "background_image" + extension;
    }
    private void retrieveAndShowRingtones() throws IOException {
        RingtoneManager ringtoneManager = new RingtoneManager(getActivity());
        ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = ringtoneManager.getCursor();

        List<String> ringtoneTitles = new ArrayList<>();
        List<Uri> ringtoneUris = new ArrayList<>();

        while (cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            Uri uri = ringtoneManager.getRingtoneUri(cursor.getPosition());
            int duration = getRingtoneDuration(getActivity(), uri) / 1000;
            if (type.equals("notification")) {
                if (duration > 30) {
                    ringtoneTitles.add(title);
                    ringtoneUris.add(uri);
                }
            } else {
                if (duration <= 30) {
                    ringtoneTitles.add(title);
                    ringtoneUris.add(uri);
                }
            }
        }
        String[] items = ringtoneTitles.toArray(new String[0]);
        Uri[] uris = ringtoneUris.toArray(new Uri[0]);

        new AlertDialog.Builder(requireActivity())
                .setTitle("Choose a " + type)
                .setItems(items, (dialog, which) -> {
                    Uri selectedRingtoneUri = uris[which];
                    String sound = saveRingtoneOperations(selectedRingtoneUri);
                    if (type.equals("notification")) {
                        currentNotification = selectedRingtoneUri;
                        notificationExtension = sound;
                    } else {
                        currentRingtone = selectedRingtoneUri;
                        ringtoneExtension = sound;
                    }
                })
                .show();
    }
    private int getRingtoneDuration(Context context, Uri uri) throws IOException {
        Log.d("RingtonePicker", "Inside getRingtoneDuration");

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(context, uri);

            // Use METADATA_KEY_DURATION to obtain the duration in milliseconds
            String durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

            if (durationString != null) {
                int duration = Integer.parseInt(durationString);
                Log.d("RingtonePicker", "Duration: " + duration + " milliseconds");
                return duration;
            } else {
                Log.e("RingtonePicker", "Failed to retrieve duration");
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            retriever.release();
        }
    }
    public String saveRingtoneOperations(Uri uri) {
        String extension;
        String mimeType = requireActivity().getContentResolver().getType(uri);
        Log.i("mimeType", "saveRingtoneOperations: "+mimeType);
        if ("audio/mpeg".equals(mimeType) || "application/mpeg".equals(mimeType)) {
            extension = ".mp3";
        } else if ("audio/ogg".equals(mimeType) || "application/ogg".equals(mimeType)) {
            extension = ".ogg";
        } else if ("audio/x-wav".equals(mimeType) || "application/x-wav".equals(mimeType)) {
            extension = ".wav";
        } else if ("audio/mp4a-latm".equals(mimeType) || "application/mp4a-latm".equals(mimeType)) {
            extension = ".m4a";
        } else {
            return null;
        }

        String fileName = "ringtone" + extension;
        if (type.equals("notification")) {
            Toast.makeText(getActivity(), "Notification sound changed", Toast.LENGTH_SHORT).show();
            fileName = "notification" + extension;
        }else if (type.equals("ringtone")){
            Toast.makeText(getActivity(), "Ringtone changed", Toast.LENGTH_SHORT).show();
        }
        return fileName;
    }
    public String saveFile(Uri uri, String fileName){
        Log.i("saveFile", "saveFile: "+uri+" "+fileName);

        String soundPath = null;
        try {
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = requireActivity().openFileOutput(fileName, Context.MODE_PRIVATE);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            File file = new File(requireActivity().getFilesDir(), fileName);
            soundPath = file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return soundPath;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (tempUri != null) {
                    if (type.equals("background")) {
                        saveImageOperations(tempUri);
                    } else if (type.equals("notification") || type.equals("ringtone")) {
                        saveRingtoneOperations(tempUri);
                    }
                }
            } else {
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                tempUri = null;
            }
        }
    }
    private void updateUserData() {
        // Handle the save button click event
        Log.i("update", "updateUserData: "+newTheme);
        switchTheme(newTheme);
        if(currentBackground != null && backgroundExtension != null){
            String backgroundPath = saveFile(Uri.parse(currentBackground), backgroundExtension);
            dbManager.changeBackground(backgroundPath);
        }
        dbManager.changeSounds(checkbox1.isChecked());
        dbManager.changeNotifications(checkbox2.isChecked());
        dbManager.changeFocus(checkbox3.isChecked());
        dbManager.changeGamification(checkbox4.isChecked());
        dbManager.changeVolume(currentVolume);
        if(currentRingtone != null && ringtoneExtension != null){
            String ringtonePath = saveFile(currentRingtone, ringtoneExtension);
            dbManager.changeRingtone(ringtonePath);
        }
        if(currentNotification != null && notificationExtension != null){
            String notificationPath = saveFile(currentNotification, notificationExtension);
            dbManager.changeNotification(notificationPath);
        }
        dbManager.changeHomeType(switch1.isChecked());
        preferencesChanged = false;
        Toast.makeText(getActivity(), "Successfully updated!", Toast.LENGTH_SHORT).show();
    }
    private void showConfirmationDialog() {
        Log.i("back", "showConfirmationDialog: ");
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Save Changes?");
        builder.setMessage("Do you want to save your changes before exiting?");
        builder.setPositiveButton("Save", (dialogInterface, i) -> {
            // Save the changes
            updateUserData();
            preferencesChanged = false;
            requireActivity().onBackPressed();
        });
        builder.setNegativeButton("Discard", (dialogInterface, i) -> {
            // Discard the changes
            preferencesChanged = false;
            requireActivity().onBackPressed();
        });
        builder.setNeutralButton("Cancel", (dialogInterface, i) -> {
            // Cancel the dialog, do nothing
        });
        builder.show();
    }
    @Override
    public void onResume() {
        super.onResume();
        preferencesChanged = false;
    }
}
