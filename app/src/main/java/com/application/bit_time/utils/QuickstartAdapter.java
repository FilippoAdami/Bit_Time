package com.application.bit_time.utils;


import static com.google.android.material.internal.ContextUtils.getActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bit_time.Main_Activity.MainActivity;
import com.application.bit_time.Main_Activity.QuickstartMenuFragment;
import com.application.bit_time.R;

import java.util.List;

public class QuickstartAdapter extends RecyclerView.Adapter<QuickstartAdapter.ListItemHolder> {

    private List<ActivityInfo> activitiesList;
    private QuickstartMenuFragment quickstartMenuFragment;



    public QuickstartAdapter(QuickstartMenuFragment quickstartMenuFragment, List<ActivityInfo> activitiesList)
    {
        this.activitiesList = activitiesList;
        this.quickstartMenuFragment = quickstartMenuFragment;
    }


    @NonNull
    @Override
    public QuickstartAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.quickstart_menu_element,parent,false);

        itemView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                int itemWidth = itemView.getWidth();
                Log.i("viewWidth",Integer.toString(itemWidth));
                itemView.findViewById(R.id.startButton).setLayoutParams(new LinearLayout.LayoutParams(itemWidth/3, ViewGroup.LayoutParams.WRAP_CONTENT));
                itemView.getViewTreeObserver().removeOnPreDrawListener(this);



                return false;
            }
        });
        return new ListItemHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull QuickstartAdapter.ListItemHolder holder, int position) {

        ActivityInfo currentAI = this.activitiesList.get(position);
        holder.id = currentAI.getIdInt();
        holder.titleString = currentAI.getName();
        holder.title.setText(holder.titleString);
        holder.duration.setText(currentAI.getFormattedDuration());





    }

    @Override
    public int getItemCount() {
        return this.activitiesList.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView duration;
        Button startActBtn;

        private MainActivityViewModel mainActivityViewModel;

        int id;
        String titleString;



        public ListItemHolder(View view)
        {
            super(view);
            this.title=view.findViewById(R.id.activityNameText);
            this.duration=view.findViewById(R.id.activityDurationText);
            this.startActBtn = view.findViewById(R.id.startButton);
            this.mainActivityViewModel = new ViewModelProvider(quickstartMenuFragment.getActivity()).get(MainActivityViewModel.class);




            this.startActBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("STARTBUTTON ACT","would share id: "+id);
                    SharedPreferences sharedPreferences = quickstartMenuFragment.getActivity().getPreferences(Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    Log.i("titlename",titleString);
                    editor.putInt("activityToRun",id);
                    editor.putString("activityName",titleString);
                    editor.apply();
                    mainActivityViewModel.selectItem(new MainActivityStatusData(MainActivityStatusData.Status.RunningActivity));

                }
            });

        }




    }

}
