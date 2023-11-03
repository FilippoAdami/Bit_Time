package com.application.bit_time.Settings_Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.R;
import com.application.bit_time.utils.SubtasksViewModel;
import com.application.bit_time.utils.TaskAdapter;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.Db.DbViewModelData;

public class CreationUpperFragment extends Fragment {

    DbViewModel dbViewModel;
    SubtasksViewModel subtasksViewModel;
    CustomViewModel viewModel;
    TaskAdapter taskAdapter;

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

        View view = inflater.inflate(R.layout.task_creation_upper_fragment_layout,container,false);

        EditText editName = view.findViewById(R.id.editTaskNameLabel);

        EditText edtTxtHrs = view.findViewById(R.id.editTextHours);
        EditText edtTxtMin = view.findViewById(R.id.editTextMinutes);
        EditText edtTxtSec = view.findViewById(R.id.editTextSeconds);

        Button confirmButton = view.findViewById(R.id.confirmButton);


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int hours = Integer.parseInt(edtTxtHrs.getText().toString())*60;
                int minutes = Integer.parseInt(edtTxtMin.getText().toString());

                int totalTime = hours+minutes;


                TaskItem newTask = new TaskItem(-2,editName.getText().toString(),totalTime);

                Log.i("INFOZ",newTask.getName()+" "+newTask.getDuration());

                DbViewModelData data = dbViewModel.getSelectedItem().getValue();

                //data.taskToAdd = newTask;

                data.taskItem = newTask;
                data.action = DbViewModelData.ACTION_TYPE.INSERT;
                data.selector = DbViewModelData.ITEM_TYPE.TASK;


                dbViewModel.selectItem(data);

                viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.MainEntry));



            }
        });

        return view;
    }
}
