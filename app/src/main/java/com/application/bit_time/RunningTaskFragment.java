package com.application.bit_time;

import android.database.Cursor;
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

public class RunningTaskFragment extends Fragment {

    DbManager dbManager;

    RunningActivityViewModel runningActivityViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RunningActivityData[] runningActivityData = new RunningActivityData[DbContract.Activities.DIM_MAX];
        int currentPos = 0;
        int activtiyId = 1;
        dbManager = new DbManager(getContext());
        runningActivityViewModel = new ViewModelProvider(this.requireActivity()).get(RunningActivityViewModel.class);



        Cursor runningActivityCursor = dbManager.searchActivityById(activtiyId);

        runningActivityViewModel.getSelectedItem().observe(this.getActivity(),item ->
        {
            Log.i("RTF viewModel",item.getChoice().toString() + " "+item.getStatus().toString());

            if(item.isFilled())
            {
                Log.i("RTF viewModel","item filled");
                runningActivityData[currentPos] = new RunningActivityData(item.getStatus(),item.getChoice());
            }
            else
            {
                Log.i("RTF viewModel","item not filled");
            }


        });

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.front_running_activity_fragment_layout,container,false);

        TextView runningTask = view.findViewById(R.id.currentTaskTextView);
        TextView nextTask = view.findViewById(R.id.nextTaskTextView);
        Button endTaskButton = view.findViewById(R.id.endTaskButton);

        runningTask.setText("TASK PLACEHOLDER");
        nextTask.setText("NEXT TASK");


        endTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(),"end button pressed",Toast.LENGTH_SHORT).show();

                RunningTaskDialog runningTaskDialog = new RunningTaskDialog();
                runningTaskDialog.show(requireActivity().getSupportFragmentManager(),null);
            }
        });

        return view;



    }
}
