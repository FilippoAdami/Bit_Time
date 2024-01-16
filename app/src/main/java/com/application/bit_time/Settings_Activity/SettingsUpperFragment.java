package com.application.bit_time.Settings_Activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_upper_fragment_layout,container,false);




        viewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);


        TextView leftLabel = view.findViewById(R.id.leftLabel);
        TextView rightLabel = view.findViewById(R.id.rightLabel);

        rightLabel.setTypeface(null, Typeface.BOLD);
        leftLabel.setTypeface(null,Typeface.NORMAL);

        leftLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rightLabel.setTypeface(null, Typeface.NORMAL);
                leftLabel.setTypeface(null,Typeface.BOLD);

                Log.i("SETTACT topfrag","left pressed");
                //Toast.makeText(getContext(),"left label pressed",Toast.LENGTH_SHORT).show();
                viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.Tasks));
            }
        });

        rightLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                leftLabel.setTypeface(null, Typeface.NORMAL);
                rightLabel.setTypeface(null,Typeface.BOLD);


                Log.i("SETTACT topfrag","right pressed");
                //Toast.makeText(getContext(),"right label pressed",Toast.LENGTH_SHORT).show();
                viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.Activities));
            }
        });



        return view;
    }
}
