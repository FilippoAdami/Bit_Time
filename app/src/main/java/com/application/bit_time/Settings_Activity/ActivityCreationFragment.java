package com.application.bit_time.Settings_Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.utils.ActivityItem;
import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbViewModelData;
import com.application.bit_time.utils.ErrorDialog;
import com.application.bit_time.utils.ImagePicker;
import com.application.bit_time.utils.PlannerViewModel;
import com.application.bit_time.utils.PlannerViewModelData;
import com.application.bit_time.utils.PlanningInfo;
import com.application.bit_time.utils.SettingsModeData;
import com.application.bit_time.utils.SubtaskAdapter;
import com.application.bit_time.utils.SubtasksViewModel;
import com.application.bit_time.utils.SubtasksViewModelData;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.Db.DbContract;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.TimeHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ActivityCreationFragment extends Fragment {

    private DbManager dbManager;
    private RecyclerView subtasksRecyclerView;
    private SubtaskAdapter subtaskAdapter;
    private CustomViewModel viewModel;
    private PlannerViewModel plannerViewModel;
    private SubtasksViewModel subtasksViewModel;
    private DbViewModel dbViewModel;
    private TaskItem[] subtasksToAdd;
    private String currentName;
    private int idToBeModified;
    private String activityName;
    private SettingsModeData currentState;

    private TextView totalTimelabel ;
    private Button addButton ;
    private Button endButton ;
    private TextView nameLabel;
    private int MAX_LENGTH = 12;

    private ImageView icon;
    RelativeLayout changeIcon;
    String currentIcon = "empty";
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);
        dbManager = new DbManager(getContext());
        subtasksViewModel = new ViewModelProvider(requireActivity()).get("subTasksVM",SubtasksViewModel.class);
        //Log.i("ACTCREFRAG svm",this.subtasksViewModel.toString());
        dbViewModel = new ViewModelProvider(requireActivity()).get(DbViewModel.class);
        plannerViewModel = new ViewModelProvider(requireActivity()).get(PlannerViewModel.class);
        idToBeModified=-1;

        Cursor allTasksCursor = dbManager.selectAllTasks();
        TaskItem[] allTasks;

        if(allTasksCursor.getCount()>0)
        {
            int i =0;
            allTasks = new TaskItem[allTasksCursor.getCount()];
            allTasksCursor.moveToFirst();
            do{
//retrieved also the added column of the task table
                allTasks[i]=new TaskItem(allTasksCursor.getInt(0), allTasksCursor.getString(1),allTasksCursor.getInt(2), allTasksCursor.getString(3));
                //Log.i("allTasks",allTasks[i].toString());
                i++;
            }while(allTasksCursor.moveToNext());

            Objects.requireNonNull(subtasksViewModel.getSelectedItem().getValue()).setAllTaskItems(allTasks);
        }

        subtasksToAdd = new TaskItem[DbContract.Activities.DIM_MAX];

        for(int i =0 ;i <DbContract.Activities.DIM_MAX ; i++)
        {
            subtasksToAdd[i] = new TaskItem();
        }

        currentState = viewModel.getSelectedItem().getValue();

        if(currentState.equals("NewActivity"))
        {
            this.subtasksViewModel.selectItem(new SubtasksViewModelData());
        }else if(currentState.equals("ModifyActivity"))
        {
            Log.i("DATA","RETRIEVED");
            //ActivityItem activityToModifyInfo = dbViewModel.getSelectedItem().getValue().activityItem;
            SubtasksViewModelData SVMData = subtasksViewModel.getSelectedItem().getValue();
            ActivityItem activityToModify = SVMData.getActivityToModify();

            Log.i("activityToModify retr",activityToModify.toString());

            subtasksViewModel.getSelectedItem().observe(this,item -> {
                if(subtasksViewModel.getSelectedItem().getValue().isAlreadyModified())
                {
                    for(TaskItem ti : SVMData.getSubtasks())
                    {
                        Log.i("ti modified",ti.toString());
                    }

                }
                else
                {
                    Log.i("ti modified","not really");
                }
            });

            TaskItem[] sharedSubtasks = new TaskItem[DbContract.Activities.DIM_MAX];

            int pos =0 ;
            for(TaskItem ti : activityToModify.getSubtasks())
            {

                Log.i("subtask shared",ti.toString());
                sharedSubtasks[pos]=new TaskItem(ti);
                pos++;
            }

            //Log.i("contentz",dbViewModel.getSelectedItem().getValue().toString());
            //Cursor activityToModifyData = dbManager.searchActivityById(activityToModifyInfo.getIdInt());
            //activityToModifyData.moveToFirst();

            this.activityName = activityToModify.getName();//activityToModifyData.getString(1);
            this.idToBeModified= activityToModify.getInfo().getIdInt();//activityToModifyData.getInt(0);
            this.currentIcon = activityToModify.getInfo().getImage();//activityToModifyData.getString(4);
            Log.i("ACT_CRE_FRA", "ToBeModified: "+currentIcon);

            for(int i = 0; i< DbContract.Activities.DIM_MAX; i++)
            {
                int subtaskToAddId = sharedSubtasks[i].getID();//activityToModifyData.getInt(3+i);
                if(subtaskToAddId > -1) {

                    subtasksToAdd[i] = new TaskItem(sharedSubtasks[i]);//dbManager.searchTask(subtaskToAddId);
                    Log.i("subtoAdd", subtasksToAdd[i].toString());
                }

                {
                    subtasksToAdd[i]=new TaskItem();
                }
            }

            //subtasksViewModel.selectItem(new SubtasksViewModelData(subtasksToAdd,null));

        }


        List<TaskItem> subtasksList =  Arrays.asList(subtasksToAdd);
        subtaskAdapter = new SubtaskAdapter(this,subtasksList);

        SubtasksViewModelData AdaptData = subtasksViewModel.getSelectedItem().getValue();

        if(AdaptData.subtaskAdapter == null)
        {
            Log.i("adaptData state"," was null");
            AdaptData.subtaskAdapter = subtaskAdapter;
            Log.i("adaptData state","now is "+ AdaptData.subtaskAdapter.toString());
            AdaptData.setActivityId(idToBeModified);
            subtasksViewModel.selectItem(AdaptData);
        }

        Log.i("subtest",subtasksViewModel.toString());

        subtasksViewModel.getSelectedItem().observe(this,item -> {

            for (int i = 0; i < item.subtasks.length; i++) {
                Log.i("ACF subtasks[i]= ", item.subtasks[i].toString());

                subtasksToAdd[i] = new TaskItem(item.subtasks[i]);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentManager fManager= getChildFragmentManager();

        //View view = inflater.inflate(R.layout.activity_creation_fragment_layout,container,false);
        View view = inflater.inflate(R.layout.activity_creation_fragment_layout,container,false);
        TextView actCreWarning = view.findViewById(R.id.ActCreWarning);
        actCreWarning.setText("The name of the activity cannot be longer than "+MAX_LENGTH+" chars");
        actCreWarning.setVisibility(View.INVISIBLE);
        icon = view.findViewById(R.id.activityIcon);
        changeIcon = view.findViewById(R.id.editActivityIcon);
        nameLabel = view.findViewById(R.id.editNameLabel);
        nameLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>MAX_LENGTH)
                {
                    Log.i("ACF","the name is too long");
                    actCreWarning.setVisibility(View.VISIBLE);
                }
                else
                    actCreWarning.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        totalTimelabel = view.findViewById(R.id.totalTimeLabel);
        addButton = view.findViewById(R.id.addTaskButton);
        endButton = view.findViewById(R.id.fineButton);

        fManager.beginTransaction()
                .add(R.id.planningFragment,new PlanningFragment(),"currentPlanningFragment")
                .commit();

        //Switch planningSwitch = view.findViewById(R.id.planSwitch);

        changeIcon.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT <= 22) {
                imagePickerLauncher.launch(null);
            }else{
                requestMediaPermissions(Manifest.permission.READ_MEDIA_IMAGES);
            }
        });


        if(currentState.equals("ModifyActivity"))
        {
            nameLabel.setHint(activityName);
            nameLabel.setText(activityName);
            Bitmap bitmap = BitmapFactory.decodeFile(this.currentIcon);
            icon.setImageBitmap(bitmap);
            nameLabel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if(hasFocus)
                    {
                        nameLabel.setText("");
                        Log.i("focus log","b is true");
                    }
                    else
                    {
                        currentName = nameLabel.getText().toString();
                        if(currentName.equals(""))
                        {
                            nameLabel.setText(activityName);
                        }
                        else
                        {
                            nameLabel.setText(currentName);
                        }
                        Log.i("focus log","b is false");
                    }
                }
            });
            //TODO: set stuff if the activity to be modified was previously planned
        }

        subtasksRecyclerView = view.findViewById(R.id.subtasksRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        subtasksRecyclerView.setLayoutManager(layoutManager);
        subtasksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        subtasksRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),LinearLayoutManager.VERTICAL));

        subtasksRecyclerView.setAdapter(subtaskAdapter);

        subtasksViewModel.getSelectedItem().observe(getViewLifecycleOwner(),item ->
                {

                    int totalTime = 0;

                    for (int i = 0; i < DbContract.Activities.DIM_MAX; i++) {
                        if (!subtasksToAdd[i].isEqualToEmpty()) {
                            totalTime = totalTime + subtasksToAdd[i].getDurationInt();
                        }
                    }
                    TimeHelper th = new TimeHelper(totalTime);
                    //totalTimelabel.setText(Integer.toString(totalTime));
                    totalTimelabel.setText(th.toStringShrt());
                });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getContext(),"pressed and all ok", Toast.LENGTH_SHORT).show();
                //SubtasksViewModelData SVMData = subtasksViewModel.getSelectedItem().getValue();
                //SVMData.setSubtasks(subtasksToAdd);
                //subtasksViewModel.selectItem(SVMData);
                TaskSelectionDialog taskSelectionDialog = new TaskSelectionDialog();
                taskSelectionDialog.show(getActivity().getSupportFragmentManager(),null);
            }
        });

        /*planningSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    Log.i("planningSwitch","checked");
                    fManager.beginTransaction()
                            .add(R.id.planningFragment,new PlanningFragment(),"currentPlanningFragment")
                            .commit();
                }
                else
                {
                    Log.i("planningSwitch","NOT checked");

                    fManager.beginTransaction()
                            .remove(fManager.findFragmentByTag("currentPlanningFragment"))
                            .commit();
                }
            }
        });*/

        endButton.setOnClickListener(new View.OnClickListener() {

            //TODO: make this override plannerviewmodel so that next activity will find a new AlarmInfo obj
            @Override
            public void onClick(View view) {
                if (checks()) {
                    //Toast.makeText(getContext(), nameLabel.getText().toString(), Toast.LENGTH_SHORT).show();
                    DbViewModelData newData = new DbViewModelData();
                    newData.selector = DbViewModelData.ITEM_TYPE.ACTIVITY;
                    //dbViewModel.selectItem();
                    if (viewModel.getSelectedItem().getValue().equals("NewActivity")) {
//should also add the image at the end
                        Log.i("image current path", "path: "+currentIcon);
                        ActivityItem activity = new ActivityItem(nameLabel.getText().toString(), -1, subtasksToAdd, currentIcon);
                        // dbManager.insertActivityRecord(new ActivityItem(nameLabel.getText().toString(),-1, subtasksToAdd));
                        newData.action = DbViewModelData.ACTION_TYPE.INSERT;
                        Log.i("activity image is", activity.getInfo().getImage());
                        newData.activityItem = activity;


                        Log.i("activity is", activity.toString());
                        for (TaskItem ti : activity.getSubtasks()) {
                            Log.i("actsubtaksks are", ti.toString());
                        }
                    }
                    else if (viewModel.getSelectedItem().getValue().equals("ModifyActivity")) {

                        int[] subtasksId = new int[DbContract.Activities.DIM_MAX];

                        int duration = 0;
                        for (int i = 0; i < DbContract.Activities.DIM_MAX; i++) {
                            subtasksId[i] = subtasksToAdd[i].getID();
                            duration += subtasksToAdd[i].getDurationInt();
                            //Log.i("changessub", Integer.toString(subtasksId[i]));
                        }

                        String currentName;

                        if (nameLabel.length() > 0) {
                            currentName = nameLabel.getText().toString();
                        } else {
                            currentName = activityName;
                        }

                        Log.i("ACT_CRE_FRA", Integer.toString(idToBeModified));
                        Log.i("ACT_CRE_FRA", activityName);
                        Log.i("ACT_CRE_FRA", Integer.toString(duration));

                        newData.action = DbViewModelData.ACTION_TYPE.MODIFY;
                        newData.activityItem = new ActivityItem(Integer.toString(idToBeModified), currentName, Integer.toString(duration), currentIcon, subtasksId);

                    /*dbViewModel.selectItem(new DbViewModelData(
                            DbViewModelData.ACTION_TYPE.MODIFY,
                            DbViewModelData.ITEM_TYPE.ACTIVITY,
                            new ActivityItem(Integer.toString(idToBeModified),activityName,Integer.toString(duration),subtasksId)));*/
                        //dbManager.modifyActivity(dbViewModel.getSelectedItem().getValue().activityToModify.getIdInt(), nameLabel.getText().toString(),Integer.parseInt(totalTimelabel.getText().toString()),subtasksId);
                        //dbManager.modifyActivity(dbViewModel.getSelectedItem().getValue().activityItem.getInfo(),subtasksId);
                    }

                    PlannerViewModelData plannerViewModelData = plannerViewModel.getSelectedItem().getValue();

                    if (plannerViewModelData != null) {
                        newData.activityItem.setPlans(plannerViewModelData.getPlans());
                        Log.i("ActCreFrag", "plans set");

                        for (PlanningInfo pi : newData.activityItem.getPlans()) {
                            Log.i("ActCreFrag", pi.toString());
                        }
                    }

                    dbViewModel.selectItem(newData);
                    plannerViewModel.selectItem(new PlannerViewModelData());

                    viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.BackToActivities));
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
            Toast.makeText(getContext(), currentIcon, Toast.LENGTH_SHORT).show();
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
            icon = getView().findViewById(R.id.activityIcon);
            icon.setImageBitmap(savedBitmap);
        } else {
            // Toast something went wrong
            Toast.makeText(getActivity(), "Errore nel salvataggio", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checks()
    {

        int checkLen = countValidSubtasks();

        if(nameLabel.length()<=0)
        {
            showError(1);
            return false;
        }else if(nameLabel.length()>MAX_LENGTH)
        {
            showError(2);
            return false;
        }
        else if(checkLen==0)
        {
            showError(3);
            return false;
        }


        return true;
    }
    private void showError(int code)
    {
        String errorStr = "";
        Bundle b = new Bundle();

        if(code ==1)
        {
            errorStr="emptyNameAct";
        }
        else if(code==2)
        {
            errorStr ="ActErrLen";
        }
        else if(code==3)
        {
            errorStr = "emptySubtasksErr";
        }

        b.putString("ErrorCode",errorStr);
        ErrorDialog errorDialog = new ErrorDialog();
        errorDialog.setArguments(b);
        errorDialog.show(getChildFragmentManager(),null);
    }
    private int countValidSubtasks()
    {
        int count=0;

        for(TaskItem ti : subtasksToAdd)
        {
            if(ti.getID()>0)
                count++;
        }

        return count;
    }
}
