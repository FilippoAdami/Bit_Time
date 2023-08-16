package com.application.bit_time;

import android.os.Bundle;
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

public class ActivityCreationFragment extends Fragment {

    private DbManager dbManager;

    private CustomViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DbManager(getContext());

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

            }
        });


        return view;
    }



}
