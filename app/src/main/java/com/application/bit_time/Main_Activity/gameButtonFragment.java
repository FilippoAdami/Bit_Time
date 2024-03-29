package com.application.bit_time.Main_Activity;

import android.os.Bundle;
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

public class gameButtonFragment extends Fragment {


    MainActivityViewModel mainActivityViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mainActivityViewModel = new ViewModelProvider(this.requireActivity()).get(MainActivityViewModel.class);


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.a_gamebutton_fragment_layout,container,false);

        Button gameButton = view.findViewById(R.id.gameButton);

        gameButton.setOnClickListener(view1 -> mainActivityViewModel.selectItem(new MainActivityStatusData(MainActivityStatusData.Status.GameArea)));


        return view;
    }
}
