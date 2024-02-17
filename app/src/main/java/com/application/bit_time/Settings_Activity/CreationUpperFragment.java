package com.application.bit_time.Settings_Activity;

import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.provider.MediaStore.getExternalVolumeNames;
import static android.provider.MediaStore.getVersion;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.R;
import com.application.bit_time.utils.ErrorDialog;
import com.application.bit_time.utils.ImageInfo;
import com.application.bit_time.utils.SettingsModeData;
import com.application.bit_time.utils.SubtasksViewModel;
import com.application.bit_time.utils.TaskAdapter;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.Db.DbViewModelData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        ImageInfo latestImage = null;
        //View view = inflater.inflate(R.layout.task_creation_upper_fragment_layout,container,false);
        View view = inflater.inflate(R.layout.task_creation_upper_fragment_layout,container,false);

        ImageView thumbnailIV = view.findViewById(R.id.taskCreThumbnail);

        if(ContextCompat.checkSelfPermission(this.getContext(),READ_MEDIA_IMAGES) == PERMISSION_GRANTED )
        {
            Log.i("AccessMedia","permission granted");


            for(String s : getExternalVolumeNames(this.getContext()))
            {
                Log.i("externalVolume",s);
            }

            String version = getVersion(this.getContext(),"external_primary");
            Log.i("externalVolume versionStr",version);


            List<ImageInfo> imageList = new ArrayList<>();

            Uri collection;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            {
                collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
            }
            else
            {
                collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }

            String[] projection = new String[]
                    {
                            MediaStore.Images.Media._ID,
                            MediaStore.Images.Media.DISPLAY_NAME
                    };
            try(Cursor cursor = this.getContext().getContentResolver().query(collection,projection,null,null,null)){
                Log.i("cursor res","rows "+cursor.getCount());
                Log.i("cursor res",Integer.toString(cursor.getColumnIndex(MediaStore.Images.Media._ID)));

                int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                int nameColumnIndex= cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);

                while(cursor.moveToNext())
                {
                    long id = cursor.getLong(idColumnIndex);
                    String name = cursor.getString(nameColumnIndex);

                    Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id);
                    latestImage = new ImageInfo(contentUri,name);
                    imageList.add(latestImage);
                    Log.i("cursor res",latestImage.toString());

                }

                if(latestImage != null) {
                    Bitmap thumbnail = this.getContext().getContentResolver().loadThumbnail(latestImage.getUri(), new Size(800, 800), null);

                    thumbnailIV.setImageDrawable(new BitmapDrawable(getResources(), thumbnail));
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else
        {
            Log.i("AccessMedia","permission denied");

            if(shouldShowRequestPermissionRationale(READ_MEDIA_IMAGES))
            {
                Log.i("AccessMedia","you should show a rationale");
            }else
            {
                Log.i("AccessMedia","rationale is not requested");
            }


            ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if(isGranted)
                {
                    Log.i("AccessMedia","is granted");
                }
                else
                {
                    Log.i("AccessMedia","was NOT granted");
                }
            });

            requestPermissionLauncher.launch(READ_MEDIA_IMAGES);


        }
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

        ImageInfo finalLatestImage = latestImage;
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(compulsoryFieldsAreFilled()) {

                    int hours = 0;//parseContent(edtTxtHrs.getText().toString()) * 3600;
                    int minutes = parseContent(edtTxtMin.getText().toString()) * 60;
                    int seconds = parseContent(edtTxtSec.getText().toString()) ;
                    int totalTime = hours + minutes + seconds;
                    Log.i("totalTime",Integer.toString(totalTime));

                    TaskItem newTask = new TaskItem(-2, editName.getText().toString(), totalTime, finalLatestImage.getUri());

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

        return view;
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
