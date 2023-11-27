package com.application.bit_time.Settings_Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.application.bit_time.R;

public class PlanningFragment extends Fragment {


    int n;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        n=0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.s_planning_fragment_layout,container,false);

        Button addPlanBtn = view.findViewById(R.id.addPlanButton);
        TextView statusTextView = view.findViewById(R.id.statusTextView);

        if(n==0)
        {
            statusTextView.setText("Al momento non è stata pianificata l'activity");
        }
        else
        {
            statusTextView.setVisibility(View.INVISIBLE);
        }

        addPlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager childFManager = getChildFragmentManager();
                childFManager.beginTransaction()
                        .add(R.id.planFragmentContainer,new PlanFragment(),"currentPlanFragment")
                        .commit();

               }
        });


        return view;
    }
}
