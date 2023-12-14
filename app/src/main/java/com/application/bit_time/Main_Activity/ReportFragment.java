package com.application.bit_time.Main_Activity;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ReportFragment extends Fragment {

    static final int THRESHOLD = 100;
    int currentScore;
    int[] scoreList;
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
        this.reportDataList = runningActivityViewModel.getSelectedItem().getValue().getFullReport();
        ListIterator<ReportData> iterator = this.reportDataList.listIterator();
        this.scoreList = new int[this.reportDataList.size()];

        currentScore = 0;
        while(iterator.hasNext()) {
            int pos =iterator.nextIndex();
            ReportData RD = iterator.next();
            Log.i("RD", RD.toString());
            int currentPoints = assignPoints(RD,pos);
            currentScore = currentScore + currentPoints;

        }

        if(currentScore>THRESHOLD)
        {
            Log.i("result","BRAVO");
        }
        else
        {
            Log.i("result","MEH");
        }

        //this.reportDataList.add(new ReportData("emptyTest", RunningActivityData.Status.OnTime));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        /*runningActivityViewModel.getSelectedItem().observe(this, item->
        {
            if(item.getStatus().toString().equals("ActivityDone"))
            {
                Log.i("ReportFragmentListSize",Integer.toString(item.getReportDataList().size()));
                this.reportDataList.addAll(item.getReportDataList());
            }
        });*/

        View view = inflater.inflate(R.layout.report_fragment_layout,container,false);

        TextView reportTextView = view.findViewById(R.id.reportTextView);
        recyclerView = view.findViewById(R.id.recyclerView);
        Log.i("REPFRAG",Integer.toString(this.reportDataList.size()));
        dataAdapter = new ReportDataAdapter(this,this.reportDataList);



        SharedPreferences sharedPreferences =  getActivity().getPreferences(Context.MODE_PRIVATE);
        String name =sharedPreferences.getString("activityName","empty error");
        //int id = sharedPreferences.getInt("activityToRun",-100);
        //Log.i("retrieved actId",Integer.toString(id));
        reportTextView.setText(name + "REPORT");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(dataAdapter);

        return view;

    }


    public int assignPoints(ReportData RD,int currentPos)
    {
        int rightScore = -100;
        if(RD.endStatus.toString().equals("BigAnticipation"))
        {
            rightScore=timescores[0];
        }
        else if(RD.endStatus.toString().equals("Anticipation"))
        {
            rightScore=timescores[1];
        }
        else if(RD.endStatus.toString().equals("OnTime"))
        {
            rightScore=timescores[2];
        }
        else if(RD.endStatus.toString().equals("Delay"))
        {
            rightScore=timescores[3];
        }


        scoreList[currentPos]= rightScore;
        Log.i("scoreList"+currentPos,Integer.toString(scoreList[currentPos]));
        return rightScore;
    }

}
