package com.application.bit_time.Settings_Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.R;
import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.utils.Db.DbViewModelData;
import com.application.bit_time.utils.ErrorDialog;
import com.application.bit_time.utils.ImagePicker;
import com.application.bit_time.utils.SettingsModeData;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.TimeHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ModifyTasksFragment extends Fragment {


    int MAX_LENGTH = 12;
    private DbViewModel dbViewModel;

    private EditText editName;
    private EditText editmin;
    private EditText editsec;
    private ImageView icon;
    RelativeLayout changeIcon;
    String currentIcon;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    static final int REQUEST_CODE_MEDIA_PERMISSION = 123;
    private final ActivityResultLauncher<Void> imagePickerLauncher = registerForActivityResult(
            new ImagePicker(),
            uri -> {
                // Handle the result, 'uri' contains the selected image URI
                if (uri != null) {
                    currentIcon = uri.toString();
                    saveToInternalStorage(uri);
                }
            }
    );

    private CustomViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);


        //View view = inflater.inflate(R.layout.task_creation_upper_fragment_layout,container,false);
        View view = inflater.inflate(R.layout.task_creation_upper_fragment_layout,container,false);

        editName = view.findViewById(R.id.editTaskNameLabel);
        icon = view.findViewById(R.id.taskIcon);
        changeIcon = view.findViewById(R.id.editTaskIcon);
        //TextView edith = view.findViewById(R.id.editTextHours);
        editmin = view.findViewById(R.id.editTextMinutes);
        editsec = view.findViewById(R.id.editTextSeconds);
        Button confirmButton = view.findViewById(R.id.confirmButton);
        TextView warningTV = view.findViewById(R.id.TaskCreWarning);
        warningTV.setText("The name of the task cannot be longer than "+MAX_LENGTH+" chars");
        warningTV.setVisibility(View.INVISIBLE);

        dbViewModel = new ViewModelProvider(getActivity()).get(DbViewModel.class);


        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length()>MAX_LENGTH)
                {
                    warningTV.setVisibility(View.VISIBLE);
                }
                else
                {
                    warningTV.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        changeIcon.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT <= 22) {
                imagePickerLauncher.launch(null);
            }else{
                requestMediaPermissions(Manifest.permission.READ_MEDIA_IMAGES);
            }
        });

        Log.i("TASKTOMOD",dbViewModel.getSelectedItem().getValue().taskItem.toString());

        TaskItem taskToModify = dbViewModel.getSelectedItem().getValue().taskItem;



        int totalTime = taskToModify.getDurationInt();
        TimeHelper th = taskToModify.getTimeHelper();

        int h = (totalTime - totalTime % 60) / 60;
        int min = totalTime -h*60;
        int sec = 0;


        editName.setText(taskToModify.getName());
        /*edith.setText(Integer.toString(h));*/
        editmin.setText(Integer.toString(min));
        editsec.setText(Integer.toString(sec));

        //edith.setText(Integer.toString(th.getHrs()));
        editmin.setText(Integer.toString(th.getMin()));
        editsec.setText(Integer.toString(th.getSec()));

        currentIcon = taskToModify.getImg();
        Bitmap bitmap = BitmapFactory.decodeFile(currentIcon);//decodeFile(imagePath);
        icon.setImageBitmap(bitmap);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checks())
                {
                    int h = 0;//parseContent(edith.getText().toString());
                    int min = parseContent(editmin.getText().toString());
                    int sec = parseContent(editsec.getText().toString());

                    int totalTime = h * 3600 + min * 60 + sec;

// should add the image as well at the end, temporary constructor call
                    TaskItem newItem = new TaskItem(taskToModify.getID(), editName.getText().toString(), totalTime, currentIcon);

                    Log.i("UPDATE", newItem.toString());

                    DbViewModelData dbViewModelData = new DbViewModelData(
                            DbViewModelData.ACTION_TYPE.MODIFY,
                            DbViewModelData.ITEM_TYPE.TASK,
                            newItem);

                    dbViewModel.selectItem(dbViewModelData);

                    viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.BackToTasks));
                }
            }
        });

        return view;
    }

//tons of functions to save image path and request permissions
    private void saveToInternalStorage(Uri uri) {
        //check API version
        int apiLevel = android.os.Build.VERSION.SDK_INT;
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (apiLevel > 32){
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        }
        if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {Log.i("type", "saveToInternalStorage");
            saveImageOperations(uri);
        }
    }
    private void requestMediaPermissions(String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            // Check if the permission is already granted
            int permissionCheck = ContextCompat.checkSelfPermission(requireContext(), permission);

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted
                handleMediaPermissionResult();
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Check if the permission request is granted or not
        if (requestCode == REQUEST_CODE_MEDIA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                handleMediaPermissionResult();
            } else {
                // Permission is denied
                handleMediaPermissionDenied();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    void handleMediaPermissionResult() {
        imagePickerLauncher.launch(null);
    }
    void handleMediaPermissionDenied() {
        // Inform the user or take appropriate actions when permission is denied
        // For example, show a message, disable certain features, etc.
        Toast.makeText(requireContext(), "Autorizzazione non concessa.", Toast.LENGTH_SHORT).show();
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
            currentIcon = imagePath;
            Log.i("saveFileAbPath", imagePath);
            bitmap = BitmapFactory.decodeFile(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public void saveImageOperations(Uri uri) {
        // Determine the file extension from the MIME type
        String extension;
        String mimeType = requireActivity().getContentResolver().getType(uri);
        if ("image/jpeg".equals(mimeType)) {
            extension = ".jpg";
        } else if ("image/png".equals(mimeType)) {
            extension = ".png";
        }else if ("image/ico".equals(mimeType)) {
            extension = ".ico";
        } else {
            // Toast unsupported file types
            Toast.makeText(getActivity(), "File non supportato", Toast.LENGTH_SHORT).show();
            return;
        }
        // Construct the file name with extension
        //Generate a random string to append to the file name to make it unique
        String random = Long.toString(System.currentTimeMillis());
        String fileName = "task_image" +random+ extension;

        // Save the image to internal storage and get the Bitmap
        Bitmap savedBitmap = saveImageFile(uri, fileName);

        if (savedBitmap != null) {
            // Display the saved image in your ImageView
            icon = getView().findViewById(R.id.taskIcon);
            icon.setImageBitmap(savedBitmap);

        } else {
            // Toast something went wrong
            Toast.makeText(getActivity(), "Errore nel salvataggio", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checks()
    {
        int length = editName.getText().length();
        int mins = getTime(editmin);
        int secs = getTime(editsec);

        if(length>MAX_LENGTH)
        {
            showError(1);
            return false;
        }
        else if(length==0)
        {
            showError(2);
            return false;
        }
        else if(mins+secs<=0)
        {
            showError(3);
            return false;
        }

        return true;
    }

    public int parseContent(String stringToParse)
    {
        if(stringToParse.equals(""))
        {
            return 0;
        }
        else
        {
            return Integer.parseInt(stringToParse);
        }
    }

    private void showError(int code)
    {
        ErrorDialog errorDialog = new ErrorDialog();
        Bundle b = new Bundle();
        String strCode = "";
        if(code == 1)
        {
            strCode = "TaskErrLen";
        }else if(code == 2)
        {
            strCode = "emptyNameTask";
        }
        else if(code==3)
        {
            strCode = "zeroTimeTaskErr";
        }

        b.putString("ErrorCode",strCode);
        errorDialog.setArguments(b);
        errorDialog.show(getActivity().getSupportFragmentManager(),null);
    }

    private int getTime(EditText edtTxt)
    {
        String srcStr = edtTxt.getText().toString();

        return parseContent(srcStr);
    }
}
