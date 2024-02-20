package com.application.bit_time.Main_Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.application.bit_time.utils.MainActivityStatusData;
import com.application.bit_time.utils.MainActivityViewModel;
import com.application.bit_time.utils.ReportData;
import com.application.bit_time.utils.ReportDataAdapter;
import com.application.bit_time.utils.RunningActivityViewModel;

import java.util.ArrayList;
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

    private Drawable sadIcon;
    private Drawable happyIcon;

    private static evaluation evaluation;

    private MainActivityViewModel mainActivityViewModel;
    private MainActivityStatusData statusData;


    enum evaluation {
        StillToImprove,
        GoodJob,
        WellDone,
        Great
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        givePoints();
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

        View view = inflater.inflate(R.layout.a_report_fragment_layout,container,false);

        TextView reportTitle = view.findViewById(R.id.reportTitle);
        TextView evaluationField = view.findViewById(R.id.evaluationField);
        TextView totalScoreTV = view.findViewById(R.id.totalscore);

        int evaluationId=-1;

        if(evaluation.equals(ReportFragment.evaluation.StillToImprove))
        {
            evaluationId = R.string.StillToImproveIta;
        }else if(evaluation.equals(ReportFragment.evaluation.GoodJob))
        {
            evaluationId = R.string.GoodJobIta;
        }
        else if(evaluation.equals(ReportFragment.evaluation.WellDone))
        {
            evaluationId = R.string.WellDoneIta;
        }
        else if(evaluation.equals(ReportFragment.evaluation.Great))
        {
            evaluationId = R.string.GreatIta;
        }

        evaluationField.setText(evaluationId);
        totalScoreTV.setText(Integer.toString(currentScore));
        recyclerView = view.findViewById(R.id.recyclerView);


        Log.i("REPFRAG",Integer.toString(this.reportDataList.size()));
        dataAdapter = new ReportDataAdapter(this,this.reportDataList,this.scoreList,happyIcon,sadIcon);



        SharedPreferences sharedPreferences =  getActivity().getPreferences(Context.MODE_PRIVATE);
        String name =sharedPreferences.getString("activityName","empty error");
        //int id = sharedPreferences.getInt("activityToRun",-100);
        //Log.i("retrieved actId",Integer.toString(id));
        reportTitle.setText(name + " REPORT");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(dataAdapter);



        //Button saveButton = view.findViewById(R.id.collectButton);
        //Button discardButton = view.findViewById(R.id.discardButton);

        mainActivityViewModel= new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);
        statusData=mainActivityViewModel.getSelectedItem().getValue();
        /*saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("PRESSED","Save");
                statusData.setBackField(MainActivityStatusData.BackField.Save);

                commonButtonRoutine();


            }
        });

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("PRESSED","ignore");
                statusData.setBackField(MainActivityStatusData.BackField.Ignore);

                commonButtonRoutine();

            }
        });*/



        return view;

    }


    public int assignPoints(ReportData RD,int currentPos)
    {
        int rightScore = -100;      //default value, it means nothing, just initializes the variable
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

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("actually","inside of onDetach");
        statusData.setCurrentStatus(MainActivityStatusData.Status.QuickstartMenu);
        mainActivityViewModel.selectItem(statusData);
    }

    private void commonButtonRoutine()
    {
        statusData.setCurrentStatus(MainActivityStatusData.Status.QuickstartMenu);
        mainActivityViewModel.selectItem(statusData);
        Log.i("toString",statusData.toString());
    }


    public void givePoints()
    {
        DbManager dbManager = new DbManager(this.getContext());

        happyIcon = dbManager.getPositiveIcon();
        sadIcon = dbManager.getNegativeIcon();

        timescores = new int[DbContract.timescores];
        timescores = dbManager.getGamificationPoints();
        runningActivityViewModel = new ViewModelProvider(getActivity()).get(RunningActivityViewModel.class);
        this.reportDataList = new ArrayList<>();

        /*Log.i("TIMESCORES from RepFrag",Integer.toString(timescores.length));

        for(int i : timescores)
        {
            Log.i("TIMESCORE ",Integer.toString(i));
        }*/



        this.reportDataList = runningActivityViewModel.getSelectedItem().getValue().getFullReport();
        ListIterator<ReportData> iterator = this.reportDataList.listIterator();
        this.scoreList = new int[this.reportDataList.size()];

        currentScore = 0;   // in the end will hold the total score of the activity
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
            evaluation = ReportFragment.evaluation.GoodJob;

        }
        else
        {
            Log.i("result","MEH");
            evaluation = ReportFragment.evaluation.StillToImprove;
        }

        //this.reportDataList.add(new ReportData("emptyTest", RunningActivityData.Status.OnTime));

    }



}
