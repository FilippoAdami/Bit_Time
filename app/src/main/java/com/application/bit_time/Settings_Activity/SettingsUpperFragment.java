package com.application.bit_time.Settings_Activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private TextView leftLabel;
    private TextView rightLabel;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.s_settings_upper_fragment_layout,container,false);

        viewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);


        leftLabel = view.findViewById(R.id.leftLabel);
        rightLabel  = view.findViewById(R.id.rightLabel);


        Bundle b = getArguments();
        if(b != null)
        {

            modeStr = b.getString("mode","noValue");

            Log.i("SUF modeStr",modeStr);
            if(modeStr.equals("Tasks"))
            {
                leftSelection();
            }
            else if(modeStr.equals("Activities"))
            {
                rightSelection();
            }
        }else
        {
            Log.i("SUF default","rightSel");
            rightSelection();
        }



        leftLabel.setOnClickListener(new View.OnClickListener() {
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
        });



        return view;
    }

    private void rightSelection()
    {
        leftLabel.setTypeface(null, Typeface.NORMAL);
        rightLabel.setTypeface(null,Typeface.BOLD);

    }

    private void leftSelection()
    {
        rightLabel.setTypeface(null, Typeface.NORMAL);
        leftLabel.setTypeface(null,Typeface.BOLD);
    }


}
