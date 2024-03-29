package com.application.bit_time.Main_Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.R;
import com.application.bit_time.utils.MainActivityStatusData;
import com.application.bit_time.utils.MainActivityViewModel;

public class ControlsFragment extends Fragment {


    Button settingsButton;
    MainActivityViewModel mainActivityViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.mainActivityViewModel = new ViewModelProvider(this.getActivity()).get(MainActivityViewModel.class);



    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.a_controls_fragment_layout,container,false);


        settingsButton = view.findViewById(R.id.settingsButton);

        MainActivityStatusData.Status currentStatus = mainActivityViewModel.getSelectedItem().getValue().getCurrentStatus();
        MainActivityStatusData MASData = new MainActivityStatusData(MainActivityStatusData.Status.CaregiverLogin);
        if(currentStatus.equals(MainActivityStatusData.Status.RunningActivity))
        {
            settingsButton.setVisibility(View.INVISIBLE);
        }else {
            settingsButton.setVisibility(View.VISIBLE);
        }

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("current MAV pos",mainActivityViewModel.getSelectedItem().getValue().getCurrentStatus().toString());
                MainActivityStatusData.Status currentStatus = mainActivityViewModel.getSelectedItem().getValue().getCurrentStatus();
                MainActivityStatusData MASData = new MainActivityStatusData(MainActivityStatusData.Status.CaregiverLogin);
                if(currentStatus.equals(MainActivityStatusData.Status.RunningActivity))
                {
                    MASData.setBackField(MainActivityStatusData.BackField.Quit);
                }

                mainActivityViewModel.selectItem(MASData);
            }
        });



        return view;
    }
}
