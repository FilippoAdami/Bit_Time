package com.application.bit_time.Main_Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.application.bit_time.R;

public class HomeBottomFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_bottom_fragment_layout,container,false);

        Button gamesbutton = view.findViewById(R.id.gamesbutton);

        gamesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"gamesbutton pressed",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }








}
