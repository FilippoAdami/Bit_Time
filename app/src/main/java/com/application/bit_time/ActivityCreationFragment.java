package com.application.bit_time;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.CustomViewModel;
import com.application.bit_time.DbManager;
import com.application.bit_time.R;
import com.application.bit_time.TaskSelectionDialog;

public class ActivityCreationFragment extends Fragment {

    private DbManager dbManager;

    private CustomViewModel viewModel;

    private SubtasksViewModel subtasksViewModel;

    private TaskItem[] subtasksToAdd;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subtasksToAdd = new TaskItem[DbContract.Activities.DIM_MAX];

        for(int i =0 ;i <DbContract.Activities.DIM_MAX ; i++)
        {
            subtasksToAdd[i] = new TaskItem();
        }


        dbManager = new DbManager(getContext());
        subtasksViewModel = new ViewModelProvider(requireActivity()).get(SubtasksViewModel.class);



        Log.i("subtest",subtasksViewModel.toString());

        subtasksViewModel.getSelectedItem().observe(this,item -> {

            for(int i = 0; i< item.subtasks.length ; i++) {
                Log.i("ACF subtasks[i]= ", item.subtasks[i].toString());

                subtasksToAdd[i] = new TaskItem(item.subtasks[i]);
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_creation_fragment_layout,container,false);


        TextView nameLabel = view.findViewById(R.id.editNameLabel);
        Button addButton = view.findViewById(R.id.addTaskButton);
        Button endButton = view.findViewById(R.id.fineButton);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(),"pressed and all ok", Toast.LENGTH_SHORT).show();
                TaskSelectionDialog taskSelectionDialog = new TaskSelectionDialog();
                taskSelectionDialog.show(getActivity().getSupportFragmentManager(),null);
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),nameLabel.getText().toString(),Toast.LENGTH_SHORT).show();





                dbManager.insertActivityRecord(nameLabel.getText().toString(),  subtasksToAdd);
            }
        });


        return view;
    }



}
