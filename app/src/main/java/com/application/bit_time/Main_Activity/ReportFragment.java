package com.application.bit_time.Main_Activity;

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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.R;
import com.application.bit_time.Utils.RunningActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {

    int score;
    private List<ReportData> reportDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReportDataAdapter dataAdapter;
    private RunningActivityViewModel runningActivityViewModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runningActivityViewModel = new ViewModelProvider(getActivity()).get(RunningActivityViewModel.class);
        this.reportDataList = new ArrayList<>();

        //this.reportDataList.add(new ReportData("emptyTest", RunningActivityData.Status.OnTime));
        score = 101;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        runningActivityViewModel.getSelectedItem().observe(this, item->
        {
            if(item.getStatus().toString().equals("ActivityDone"))
            {
                Log.i("ReportFragment list size",Integer.toString(item.getReportDataList().size()));
                this.reportDataList.addAll(item.getReportDataList());
            }
        });

        View view = inflater.inflate(R.layout.report_fragment_layout,container,false);

        TextView reportTextView = view.findViewById(R.id.reportTextView);
        recyclerView = view.findViewById(R.id.recyclerView);
        Log.i("REPFRAG",Integer.toString(this.reportDataList.size()));
        dataAdapter = new ReportDataAdapter(this,this.reportDataList);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(dataAdapter);


        if(score > 100)
        {
            reportTextView.setText("BRAVO");
        }
        else if(score <100)
        {
            reportTextView.setText("MEH");
        }


        return view;

    }
}
