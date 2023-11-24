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
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                    type = "background";
                    String image = saveToInternalStorage(uri);
                    backgroundText.setText(image);
                    dbManager.changeBackground(image);
                }
            }
    );
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private CheckBox checkbox3;
    private CheckBox checkbox4;
    private SeekBar volumeSeekBar;
    private Button loadRingtoneButton1;
    private TextView ringtoneText;
    private Button loadNotificationButton;
    private TextView notificationText;
    private Uri tempUri;
    private String type = "background";

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
        checkbox2 = view.findViewById(R.id.checkbox2);
        checkbox3 = view.findViewById(R.id.checkbox3);
        checkbox4 = view.findViewById(R.id.checkbox4);
        volumeSeekBar = view.findViewById(R.id.volumeSeekBar);
        loadRingtoneButton1 = view.findViewById(R.id.loadRingtoneButton1);
        ringtoneText = view.findViewById(R.id.textButton1);
        loadNotificationButton = view.findViewById(R.id.loadRingtoneButton2);
        notificationText = view.findViewById(R.id.textButton2);

        // Set the saved values
        String currentTheme = dbManager.getTheme();
        highlightCurrentTheme(currentTheme);

        String currentBackground = dbManager.getBackground();
        backgroundText.setText(currentBackground);

        checkbox1.setChecked(dbManager.getSounds());
        checkbox2.setChecked(dbManager.getNotifications());
        checkbox3.setChecked(dbManager.getFocus());
        checkbox4.setChecked(dbManager.getGamification());

        volumeSeekBar.setProgress(dbManager.getVolume());

        String currentRingtone = dbManager.getRingtone();
        ringtoneText.setText(currentRingtone);
        String currentNotification = dbManager.getNotification();
        notificationText.setText(currentNotification);

        // Set the click listeners
        themeDefault.setOnClickListener(v -> {
            // Handle the layout click event
            switchTheme("PastelTheme");
        });
        themeVivid.setOnClickListener(v -> {
            // Handle the layout click event
            switchTheme("VividTheme");
        });
        themeEarth.setOnClickListener(v -> {
            // Handle the layout click event
            switchTheme("EarthTheme");
        });
        themeBW.setOnClickListener(v -> {
            // Handle the layout click event
            switchTheme("BWTheme");
        });
        loadBackgroundButton.setOnClickListener(v -> {
            String[] supportedExtensions = {"jpg", "jpeg", "png", "bmp", "webp"};
            StringBuilder mimeType = new StringBuilder(Objects.requireNonNull(MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg")));

            for (String extension : supportedExtensions) {
                mimeType.append("|").append(MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension));
            }

            backgroundPickerLauncher.launch(mimeType.toString());
        });
        checkbox1.setOnClickListener(v -> dbManager.changeSounds(checkbox1.isChecked()));
        checkbox2.setOnClickListener(v -> dbManager.changeNotifications(checkbox2.isChecked()));
        checkbox3.setOnClickListener(v -> dbManager.changeFocus(checkbox3.isChecked()));
        checkbox4.setOnClickListener(v -> dbManager.changeGamification(checkbox4.isChecked()));
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int volume, boolean fromUser) {
                dbManager.changeVolume(volume);
                Log.i("updated volume", String.valueOf(dbManager.getVolume()));
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
                currentTheme ="PastelTheme";
                break;
        }
        //set new theme
        highlightCurrentTheme(currentTheme);
        dbManager.changeTheme(currentTheme);
        requireActivity().setTheme(newTheme);
    }

    private String saveToInternalStorage(Uri uri) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

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

        String imagePath = null;

        try {
            // Open an InputStream from the selected image URI
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);

            // Determine the file extension from the MIME type
            String extension;
            String mimeType = requireActivity().getContentResolver().getType(uri);
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
            FileOutputStream outputStream = requireActivity().openFileOutput(fileName, Context.MODE_PRIVATE);

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
    private void retrieveAndShowRingtones() throws IOException {
        Log.d("RingtonePicker", "retrieveAndShowRingtones - Start");

        RingtoneManager ringtoneManager = new RingtoneManager(getActivity());
        ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = ringtoneManager.getCursor();

        List<String> ringtoneTitles = new ArrayList<>();
        List<Uri> ringtoneUris = new ArrayList<>();

        while (cursor.moveToNext()) {
            Log.d("RingtonePicker", "Inside Loop");

            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            Uri uri = ringtoneManager.getRingtoneUri(cursor.getPosition());
            Log.d("RingtonePicker", "Ringtone Title: " + title + ", URI: " + uri);

            int duration = getRingtoneDuration(getActivity(), uri) / 1000;
            Log.d("RingtonePicker", "Duration: " + duration + " seconds");

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

        Log.d("RingtonePicker", "Before AlertDialog");

        String[] items = ringtoneTitles.toArray(new String[0]);
        Uri[] uris = ringtoneUris.toArray(new Uri[0]);

        new AlertDialog.Builder(requireActivity())
                .setTitle("Choose a " + type)
                .setItems(items, (dialog, which) -> {
                    Log.d("RingtonePicker", "Inside AlertDialog");

                    Uri selectedRingtoneUri = uris[which];

                    Log.d("RingtonePicker", "Selected Ringtone URI: " + selectedRingtoneUri);

                    String sound = saveRingtoneOperations(selectedRingtoneUri);
                    if (type.equals("notification")) {
                        notificationText.setText(sound);
                        dbManager.changeNotification(sound);
                    } else {
                        ringtoneText.setText(sound);
                        dbManager.changeRingtone(sound);
                    }
                })
                .show();

        Log.d("RingtonePicker", "retrieveAndShowRingtones - End");
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
        Log.d("RingtonePicker", "saveRingtoneOperations - Start");

        String soundPath = null;

        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);

            String extension;
            String mimeType = getActivity().getContentResolver().getType(uri);
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
                fileName = "notification" + extension;
            }
            Log.i("fileName", "saveRingtoneOperations: "+fileName);

            FileOutputStream outputStream = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            File file = new File(getActivity().getFilesDir(), fileName);
            soundPath = file.getAbsolutePath();

            Log.d("RingtonePicker", "Saved Ringtone Path: " + soundPath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("RingtonePicker", "saveRingtoneOperations - End");

        return soundPath;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("RingtonePicker", "onRequestPermissionsResult - Start");

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

        Log.d("RingtonePicker", "onRequestPermissionsResult - End");
    }

}
