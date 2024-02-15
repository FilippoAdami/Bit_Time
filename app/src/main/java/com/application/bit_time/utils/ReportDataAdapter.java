package com.application.bit_time.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.Main_Activity.ReportFragment;
import com.application.bit_time.Main_Activity.newRunningActivityData;
import com.application.bit_time.R;

import java.util.List;

public class ReportDataAdapter extends RecyclerView.Adapter<ReportDataAdapter.ListItemHolder> {

    private List<ReportData> reportDataList;
    private ReportFragment fragment;
    private Drawable happyIcon;
    private Drawable sadIcon;


    public ReportDataAdapter(ReportFragment reportFragment, List<ReportData> dataList,Drawable happyIcon,Drawable sadIcon)
    {
        this.reportDataList = dataList;
        Log.i("RepAdapt",Integer.toString(dataList.size()));
        this.fragment = reportFragment;
        this.happyIcon=happyIcon;
        this.sadIcon = sadIcon;
    }

    @NonNull
    @Override
    public ReportDataAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item_layout,parent,false);

        return new ListItemHolder((itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull ReportDataAdapter.ListItemHolder holder, int position) {
        ReportData reportData = this.reportDataList.get(position);
        Log.i("viewholder log",reportData.subtaskName);
        holder.subtaskName.setText(reportData.subtaskName);
        holder.subtaskScore.setText(reportData.endStatus.toString());
        holder.subtaskTime.setText(reportData.lastedtimeToString());
        holder.image.setImageDrawable(happyIcon);
        holder.image.setImageDrawable(sadIcon);

    }

    @Override
    public int getItemCount() {
        return this.reportDataList.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder {

        TextView subtaskName;
        TextView subtaskScore;
        TextView subtaskTime;
        ImageView image;

        public ListItemHolder(View view)
        {
            super(view);
            this.subtaskName = view.findViewById(R.id.subtaskName);
            this.subtaskScore = view.findViewById(R.id.subtaskScore);
            this.subtaskTime = view.findViewById(R.id.subtaskTime);
            this.image = view.findViewById(R.id.taskImg);
        }

    }
}
