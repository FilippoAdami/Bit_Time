package com.application.bit_time.Settings_Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.R;
import com.application.bit_time.utils.ErrorDialog;
import com.application.bit_time.utils.SettingsModeData;
import com.application.bit_time.utils.SubtasksViewModel;
import com.application.bit_time.utils.TaskAdapter;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.Db.DbViewModelData;

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

        //View view = inflater.inflate(R.layout.task_creation_upper_fragment_layout,container,false);
        View view = inflater.inflate(R.layout.new_task_creation_upper_fragment_layout,container,false);

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

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(compulsoryFieldsAreFilled()) {

                    int hours = 0;//parseContent(edtTxtHrs.getText().toString()) * 3600;
                    int minutes = parseContent(edtTxtMin.getText().toString()) * 60;
                    int seconds = parseContent(edtTxtSec.getText().toString()) ;
                    int totalTime = hours + minutes + seconds;
                    Log.i("totalTime",Integer.toString(totalTime));

                    TaskItem newTask = new TaskItem(-2, editName.getText().toString(), totalTime);

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
        int mins = getTime(edtTxtMin);
        int secs = getTime(edtTxtSec);


        if(length>0 && length<=MAX_LENGTH && mins+secs>0) {
            return true;
        }
        else if(length>MAX_LENGTH)
        {
            showError(1);
        }
        else if(length<0)
        {
            showError(2);
        }
        else if(mins+secs<=0)
        {
            showError(3);
        }
        return false;
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
