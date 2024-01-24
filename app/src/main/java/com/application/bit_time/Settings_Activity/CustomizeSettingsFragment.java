package com.application.bit_time.Settings_Activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.ImagePicker;
import com.application.bit_time.utils.SoundPicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    private final ActivityResultLauncher<Void> imagePickerLauncher = registerForActivityResult(
            new ImagePicker(),
            uri -> {
                // Handle the result, 'uri' contains the selected image URI
                if (uri != null) {
                    // Save the new background in the database
                    type = "background";
                    currentBackground = uri.toString();
                    backgroundExtension = saveToInternalStorage(uri);
                }
            }
    );
    private final ActivityResultLauncher<Void> soundPickerLauncher = registerForActivityResult(
            new SoundPicker(),
            uri -> {
                if (uri != null) {
                    // Handle the selected sound URI
                    Log.i("SoundPicker", "Selected sound URI: " + uri.toString());
                    // Add your logic to handle the selected sound URI
                }
            }
    );
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    static final int REQUEST_CODE_MEDIA_PERMISSION = 123;
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

        //do not show subtextCheckbox3 and switch1 and subtextSwitch1 and switch1off and switch1on
        TextView subtextCheckbox3 = view.findViewById(R.id.subtextCheckbox3);
        subtextCheckbox3.setVisibility(View.GONE);
        switch1.setVisibility(View.GONE);
        TextView subtextSwitch1 = view.findViewById(R.id.subtextSwitch1);
        subtextSwitch1.setVisibility(View.GONE);
        TextView switch1off = view.findViewById(R.id.switch1off);
        switch1off.setVisibility(View.GONE);
        TextView switch1on = view.findViewById(R.id.switch1on);
        switch1on.setVisibility(View.GONE);


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
        loadBackgroundButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 22) {
                imagePickerLauncher.launch(null);
            }else{
                requestMediaPermissions(Manifest.permission.READ_MEDIA_IMAGES);
            }
        });
        loadRingtoneButton1.setOnClickListener(v -> {
            type = "ringtone";
            if (Build.VERSION.SDK_INT >= 22) {
                try {
                    retrieveAndShowRingtones();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                requestMediaPermissions(Manifest.permission.READ_MEDIA_AUDIO);
            }
        });
        loadNotificationButton.setOnClickListener(v -> {
            type = "notification";
            if (Build.VERSION.SDK_INT >= 22) {
                try {
                    retrieveAndShowRingtones();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                requestMediaPermissions(Manifest.permission.READ_MEDIA_AUDIO);
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
        Log.i("newTheme", "highlightCurrentTheme: "+newTheme);
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
    public String saveImageOperations(Uri uri) {
        // Determine the file extension from the MIME type
        String extension;
        String mimeType = requireActivity().getContentResolver().getType(uri);
        if ("image/jpeg".equals(mimeType)) {
            extension = ".jpg";
        } else if ("image/png".equals(mimeType)) {
            extension = ".png";
        } else {
            // Toast unsupported file types
            Toast.makeText(getActivity(), "File non supportato", Toast.LENGTH_SHORT).show();
            return null;
        }

        // Construct the file name with extension
        String fileName = "background_image" + extension;

        // Save the image to internal storage and get the Bitmap
        Bitmap savedBitmap = saveImageFile(uri, fileName);

        if (savedBitmap != null) {
            // Display the saved image in your ImageView
            ImageView imageView = getView().findViewById(R.id.internalImage);
            imageView.setImageBitmap(savedBitmap);

            Toast.makeText(getActivity(), "sfondo aggiornato correttamente", Toast.LENGTH_SHORT).show();
            return fileName;
        } else {
            // Toast something went wrong
            Toast.makeText(getActivity(), "Errore nel salvataggio", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    private void retrieveAndShowRingtones() throws IOException {
        Toast.makeText(getActivity(), "Caricamento suonerie...", Toast.LENGTH_SHORT).show();
        RingtoneManager ringtoneManager = new RingtoneManager(getActivity());
        ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = ringtoneManager.getCursor();
        cursor.moveToFirst();

        Set<String> ringtoneTitles = new HashSet<>();
        Set<Uri> ringtoneUris = new HashSet<>();

        while (!cursor.isAfterLast()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            Uri uri = ringtoneManager.getRingtoneUri(cursor.getPosition());
            int duration = getRingtoneDuration(getActivity(), uri) / 1000;

            if (type.equals("notification") && duration <= 10) {
                ringtoneTitles.add(title);
                ringtoneUris.add(uri);
            } else if (type.equals("ringtone") && duration > 10) {
                ringtoneTitles.add(title);
                ringtoneUris.add(uri);
            }

            cursor.moveToNext();
        }

        List<String> uniqueTitles = new ArrayList<>(ringtoneTitles);
        List<Uri> uniqueUris = new ArrayList<>(ringtoneUris);

        new AlertDialog.Builder(requireActivity())
                .setTitle("Choose a " + type)
                .setItems(uniqueTitles.toArray(new String[0]), (dialog, which) -> {
                    Uri selectedRingtoneUri = uniqueUris.get(which);
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
            Toast.makeText(getActivity(), "Suono di notifica aggiornato correttamente", Toast.LENGTH_SHORT).show();
            fileName = "notification" + extension;
        }else if (type.equals("ringtone")){
            Toast.makeText(getActivity(), "Suoneria aggiornata correttamente", Toast.LENGTH_SHORT).show();
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
    private void updateUserData() {
        if(newTheme == null){
            newTheme = currentTheme;
        }
        switchTheme(newTheme);
        if(currentBackground != null && backgroundExtension != null){
            Log.i("currentBackground", "updateUserData: "+currentBackground+" "+backgroundExtension);
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
        Toast.makeText(getActivity(), "Salvataggio completato!", Toast.LENGTH_SHORT).show();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(CustomizeSettingsFragment.this).commit();
        fragmentManager.popBackStack();
    }
    private void showConfirmationDialog() {
        Log.i("back", "showConfirmationDialog: ");
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Salvare le modifiche?");
        builder.setMessage("Vuoi salvare le modifiche prima di uscire?");
        builder.setPositiveButton("Salva", (dialogInterface, i) -> {
            // Save the changes
            updateUserData();
            preferencesChanged = false;
        });
        builder.setNegativeButton("Non salvare", (dialogInterface, i) -> {
            // Discard the changes
            preferencesChanged = false;
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(CustomizeSettingsFragment.this).commit();
            fragmentManager.popBackStack();
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
    public Bitmap saveImageFile(Uri uri, String fileName) {

        Bitmap bitmap = null;
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

            // Load the saved image into a Bitmap
            File file = new File(requireActivity().getFilesDir(), fileName);
            String imagePath = file.getAbsolutePath();
            Log.i("saveFileAbPath", imagePath);
            bitmap = BitmapFactory.decodeFile(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // Method to request permissions
    private void requestMediaPermissions(String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            // Check if the permission is already granted
            int permissionCheck = ContextCompat.checkSelfPermission(requireContext(), permission);

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted
                handleMediaPermissionResult(permission);
            } else {
                // Request the specific permission based on API level
                //for api 33 or higher, use new permissions
                if (Build.VERSION.SDK_INT >= 33) {
                    // For API level 33 (Android 13) and higher, use new permissions
                    requestPermissions(new String[]{permission}, REQUEST_CODE_MEDIA_PERMISSION);
                } else {
                    // For lower API levels, fall back to READ_EXTERNAL_STORAGE
                    int storagePermissionCheck = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

                    if (storagePermissionCheck != PackageManager.PERMISSION_GRANTED) {
                        // Request the storage permission
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_MEDIA_PERMISSION);
                    }
                }
            }
        }
    }
    // Method to handle permission request response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Check if the permission request is granted or not
        if (requestCode == REQUEST_CODE_MEDIA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                handleMediaPermissionResult(permissions[0]);
            } else {
                // Permission is denied
                handleMediaPermissionDenied();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    void handleMediaPermissionResult(String requestedPermission) {
        // Check the type of permission request and perform corresponding actions
        if (requestedPermission.equals(Manifest.permission.READ_MEDIA_IMAGES)) {
            imagePickerLauncher.launch(null);
        } else if (requestedPermission.equals(Manifest.permission.READ_MEDIA_AUDIO)) {
            try {
                retrieveAndShowRingtones();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    void handleMediaPermissionDenied() {
        // Inform the user or take appropriate actions when permission is denied
        // For example, show a message, disable certain features, etc.
        Toast.makeText(requireContext(), "Autorizzazione non concessa.", Toast.LENGTH_SHORT).show();
    }

}
