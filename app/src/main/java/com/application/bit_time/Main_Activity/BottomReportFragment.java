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

public class BottomReportFragment extends Fragment {


    private MainActivityViewModel mainActivityViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainActivityViewModel= new ViewModelProvider(this.getActivity()).get(MainActivityViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_bottom_report_fragment_layout,container,false);

        Button backReportButton = view.findViewById(R.id.backReportButton);

        backReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityViewModel.selectItem(new MainActivityStatusData(MainActivityStatusData.Status.QuickstartMenu));
            }
        });

        return view;
    }
}
