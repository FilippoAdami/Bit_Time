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

import com.application.bit_time.R;
import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.utils.Db.DbViewModelData;
import com.application.bit_time.utils.ErrorDialog;
import com.application.bit_time.utils.SettingsModeData;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.TimeHelper;

public class ModifyTasksFragment extends Fragment {


    int MAX_LENGTH = 12;
    private DbViewModel dbViewModel;

    private EditText editName;
    private EditText editmin;
    private EditText editsec;

    private CustomViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);


        //View view = inflater.inflate(R.layout.task_creation_upper_fragment_layout,container,false);
        View view = inflater.inflate(R.layout.task_creation_upper_fragment_layout,container,false);

        editName = view.findViewById(R.id.editTaskNameLabel);
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

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checks())
                {
                    int h = 0;//parseContent(edith.getText().toString());
                    int min = parseContent(editmin.getText().toString());
                    int sec = parseContent(editsec.getText().toString());

                    int totalTime = h * 3600 + min * 60 + sec;

                    TaskItem newItem = new TaskItem(taskToModify.getID(), editName.getText().toString(), totalTime);

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
