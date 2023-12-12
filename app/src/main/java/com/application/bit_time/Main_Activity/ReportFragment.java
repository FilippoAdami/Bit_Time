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
import com.application.bit_time.utils.Db.DbContract;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.ReportData;
import com.application.bit_time.utils.ReportDataAdapter;
import com.application.bit_time.utils.RunningActivityData;
import com.application.bit_time.utils.RunningActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {

    int score;
    private List<ReportData> reportDataList ;
    private RecyclerView recyclerView;
    private ReportDataAdapter dataAdapter;
    private RunningActivityViewModel runningActivityViewModel;
    private int[] timescores;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timescores = new int[DbContract.timescores];


        DbManager dbManager = new DbManager(this.getContext());
        timescores = dbManager.getGamificationPoints();

        Log.i("TIMESCORES from RepFrag",Integer.toString(timescores.length));

        for(int i : timescores)
        {
            Log.i("TIMESCORE ",Integer.toString(i));
        }


        runningActivityViewModel = new ViewModelProvider(getActivity()).get(RunningActivityViewModel.class);
        this.reportDataList = new ArrayList<>();
        this.reportDataList = runningActivityViewModel.getSelectedItem().getValue().getReportDataList();

        for(ReportData RD : reportDataList)
        {
            Log.i("RD",RD.toString());
        }

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
                Log.i("ReportFragmentListSize",Integer.toString(item.getReportDataList().size()));
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
