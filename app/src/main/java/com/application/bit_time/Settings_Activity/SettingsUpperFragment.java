package com.application.bit_time.Settings_Activity;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.R;
import com.application.bit_time.utils.SettingsModeData;

public class SettingsUpperFragment extends Fragment {


    private CustomViewModel viewModel;
    private String modeStr;
    //private TextView leftLabel;
    //private TextView rightLabel;
    private Button activitiesButton;
    private Button tasksButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /*View view = inflater.inflate(R.layout.s_settings_upper_fragment_layout,container,false);




        leftLabel = view.findViewById(R.id.leftLabel);
        rightLabel  = view.findViewById(R.id.rightLabel);*/

        View view = inflater.inflate(R.layout.new_s_settings_upper_fragment_layout,container,false);

        tasksButton = view.findViewById(R.id.tasksButton);
        activitiesButton = view.findViewById(R.id.activitiesButton);



        Bundle b = getArguments();
        if(b != null)
        {

            modeStr = b.getString("mode","noValue");

            Log.i("SUF modeStr",modeStr);
            if(modeStr.equals("Tasks"))
            {
                //leftSelection();
                tasksSelected();
            }
            else if(modeStr.equals("Activities"))
            {
                //rightSelection();
                activitiesSelected();
            }
        }else
        {
            Log.i("SUF default","rightSel");
            //rightSelection();
            activitiesSelected();
        }



        /*leftLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                leftSelection();

                Log.i("SETTACT topfrag","left pressed");
                //Toast.makeText(getContext(),"left label pressed",Toast.LENGTH_SHORT).show();
                viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.Tasks));
            }
        });

        rightLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                rightSelection();


                Log.i("SETTACT topfrag","right pressed");
                //Toast.makeText(getContext(),"right label pressed",Toast.LENGTH_SHORT).show();

                viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.Activities));
            }
        });*/



        tasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tasksSelected();
                viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.Tasks));
            }
        });



        activitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activitiesSelected();
                viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.Activities));
            }
        });


        return view;
    }

    /*private void rightSelection()
    {
        leftLabel.setTypeface(null, Typeface.NORMAL);
        rightLabel.setTypeface(null,Typeface.BOLD);

    }

    private void leftSelection()
    {
        rightLabel.setTypeface(null, Typeface.NORMAL);
        leftLabel.setTypeface(null,Typeface.BOLD);
    }*/

    private void activitiesSelected()
    {
        activitiesButton.setBackgroundColor(getResources().getColor(R.color.yellow));
        tasksButton.setBackgroundColor(getResources().getColor(R.color.light_grey));

    }

    private void tasksSelected()
    {
        activitiesButton.setBackgroundColor(getResources().getColor(R.color.light_grey));
        tasksButton.setBackgroundColor(getResources().getColor(R.color.yellow));
    }



}
