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

import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.R;
import com.application.bit_time.utils.ErrorDialog;
import com.application.bit_time.utils.ImagePicker;
import com.application.bit_time.utils.SettingsModeData;
import com.application.bit_time.utils.SubtasksViewModel;
import com.application.bit_time.utils.TaskAdapter;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.Db.DbViewModelData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CreationUpperFragment extends Fragment {

    private int MAX_LENGTH = 12;
    DbViewModel dbViewModel;
    SubtasksViewModel subtasksViewModel;
    CustomViewModel viewModel;
    TaskAdapter taskAdapter;

    EditText editName;
    //EditText edtTxtHrs;
    EditText edtTxtMin;
    EditText edtTxtSec;
    TextView WarningTW;
    ImageView imgView;
    RelativeLayout changeIcon;
    String currentIcon;
    String iconExtension;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    static final int REQUEST_CODE_MEDIA_PERMISSION = 123;
    private final ActivityResultLauncher<Void> imagePickerLauncher = registerForActivityResult(
            new ImagePicker(),
            uri -> {
                // Handle the result, 'uri' contains the selected image URI
                if (uri != null) {
                    currentIcon = uri.toString();
                    iconExtension = saveToInternalStorage(uri);
                }
            }
    );

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbViewModel = new ViewModelProvider(requireActivity()).get(DbViewModel.class);
        subtasksViewModel = new ViewModelProvider(requireActivity()).get("dbTasksVM", SubtasksViewModel.class);

        viewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //View view = inflater.inflate(R.layout.task_creation_upper_fragment_layout,container,false);
        View view = inflater.inflate(R.layout.task_creation_upper_fragment_layout,container,false);

        /*EditText editName = view.findViewById(R.id.editTaskNameLabel);
        EditText edtTxtHrs = view.findViewById(R.id.editTextHours);
        EditText edtTxtMin = view.findViewById(R.id.editTextMinutes);
        EditText edtTxtSec = view.findViewById(R.id.editTextSeconds);*/
        editName = view.findViewById(R.id.editTaskNameLabel);
        //edtTxtHrs = view.findViewById(R.id.editTextHours);
        edtTxtMin = view.findViewById(R.id.editTextMinutes);
        edtTxtSec = view.findViewById(R.id.editTextSeconds);
        WarningTW = view.findViewById(R.id.TaskCreWarning);
        WarningTW.setText("The name of the task cannot be longer than "+MAX_LENGTH+" chars");
        WarningTW.setVisibility(View.INVISIBLE);
    //added image and path
        imgView = view.findViewById(R.id.taskIcon);
        changeIcon = view.findViewById(R.id.editTaskIcon);

        Button confirmButton = view.findViewById(R.id.confirmButton);

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>MAX_LENGTH) {
                    //Log.i("TCListener", Integer.toString(charSequence.length()));
                    WarningTW.setVisibility(View.VISIBLE);
                }
                else
                {
                    WarningTW.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.i("AFTER TEXT CHANGED","here");

            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(compulsoryFieldsAreFilled()) {

                    int hours = 0;//parseContent(edtTxtHrs.getText().toString()) * 3600;
                    int minutes = parseContent(edtTxtMin.getText().toString()) * 60;
                    int seconds = parseContent(edtTxtSec.getText().toString()) ;
                    int totalTime = hours + minutes + seconds;
//added the set of the image
                    String iconPath = "empty";
                    if(currentIcon != null && iconExtension != null){
                        Log.i("currentBackground", "updateUserData: "+ currentIcon +" "+ iconExtension);
                        iconPath = saveFile(Uri.parse(currentIcon), iconExtension);
                    }
                    Log.i("totalTime",Integer.toString(totalTime));

                    TaskItem newTask = new TaskItem(-2, editName.getText().toString(), totalTime, iconPath);

                    Log.i("INFOZ", newTask.getName() + " " + newTask.getDuration());

                    DbViewModelData data = dbViewModel.getSelectedItem().getValue();

                    //data.taskToAdd = newTask;

                    data.taskItem = newTask;
                    data.action = DbViewModelData.ACTION_TYPE.INSERT;
                    data.selector = DbViewModelData.ITEM_TYPE.TASK;


                    dbViewModel.selectItem(data);

                    viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.BackToTasks));
                }
                /*else
                {
                    showError();
                }*/



            }
        });
// listener for the image change
        changeIcon.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT <= 22) {
                imagePickerLauncher.launch(null);
            }else{
                requestMediaPermissions(Manifest.permission.READ_MEDIA_IMAGES);
            }
        });

        return view;
    }
//tons of functions to save image path and request permissions
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
    private String saveToInternalStorage(Uri uri) {
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
            return(saveImageOperations(uri));
        }
        return null;
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
            Log.i("saveFileAbPath", imagePath);
            bitmap = BitmapFactory.decodeFile(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public String saveImageOperations(Uri uri) {
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
            return null;
        }

        // Construct the file name with extension
        String fileName = "background_image" + extension;

        // Save the image to internal storage and get the Bitmap
        Bitmap savedBitmap = saveImageFile(uri, fileName);

        if (savedBitmap != null) {
            // Display the saved image in your ImageView
            imgView = getView().findViewById(R.id.taskIcon);
            imgView.setImageBitmap(savedBitmap);

            Toast.makeText(getActivity(), "sfondo aggiornato correttamente", Toast.LENGTH_SHORT).show();
            return fileName;
        } else {
            // Toast something went wrong
            Toast.makeText(getActivity(), "Errore nel salvataggio", Toast.LENGTH_SHORT).show();
            return null;
        }
    }









    private boolean compulsoryFieldsAreFilled()
    {
        int length = editName.length();
        Log.i("length","is "+length);
        int mins = getTime(edtTxtMin);
        int secs = getTime(edtTxtSec);

        if(length>MAX_LENGTH)
        {
            Log.i("checks","errorCode 1");
            showError(1);
            return false;
        }
        else if(length==0)
        {

            Log.i("checks","errorCode 2");
            showError(2);
            return false;
        }
        else if(mins+secs<=0)
        {

            Log.i("checks","errorCode 3");
            showError(3);
            return false;
        }
        return true;
    }
    private void showError(int code)
    {
        ErrorDialog errorDialog = new ErrorDialog();
        Bundle b = new Bundle();
        String strCode = "";
        if(code==1)
        {
            strCode="TaskErrLen";
        }
        else if(code==2)
        {
            strCode="emptyNameTask";
        }
        else if(code==3)
        {
            strCode="zeroTimeTaskErr";
        }
        b.putString("ErrorCode",strCode);
        errorDialog.setArguments(b);
        errorDialog.show(getActivity().getSupportFragmentManager(),null);
    }
    private int parseContent(String stringToParse)
    {
        if(stringToParse.equals(""))
            return 0;
        else
            return Integer.parseInt(stringToParse);
    }
    private int getTime(EditText edtTxt)
    {
        String srcStr=edtTxt.getText().toString();
        if(srcStr.equals(""))
            return 0;
        else return Integer.parseInt(srcStr);
    }
}
