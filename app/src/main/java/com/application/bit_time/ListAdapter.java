package com.application.bit_time;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListItemHolder>
{

    private DbViewModel dbViewModel;
    private List<ActivityItem> list;
    private SettingsLowerFragmentActivities settingsLowerFragmentActivities;

    private CustomViewModel viewModel;

    public ListAdapter(SettingsLowerFragmentActivities settingsLowerFragmentActivities, List<ActivityItem> list)
    {
        this.settingsLowerFragmentActivities = settingsLowerFragmentActivities;
        this.list = list;
        dbViewModel = new ViewModelProvider(settingsLowerFragmentActivities.requireActivity()).get(DbViewModel.class);
        viewModel = new ViewModelProvider(settingsLowerFragmentActivities.requireActivity()).get(CustomViewModel.class);

    }


    @NonNull
    @Override
    public ListAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_fragment_layout, parent, false);

        return new ListItemHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ListItemHolder holder, int position) {

        ActivityItem activityItem = list.get(position);
        holder.labelName.setText(activityItem.getName());
        holder.labelTime.setText(activityItem.getTime());
        holder.id = activityItem.activityInfo.getIdInt();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        int id;
        TextView labelName;
        TextView labelTime;
        Button modifyButton;
        Button deleteButton;

        public ListItemHolder (View view)
        {
            super(view);
            labelName = view.findViewById(R.id.labelName);
            labelTime = view.findViewById(R.id.labelTime);
            modifyButton = view.findViewById(R.id.modifyButton);
            deleteButton = view.findViewById(R.id.deleteButton);


            view.setClickable(true);
            view.setOnClickListener(this);


            modifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "would modify "+ id,Toast.LENGTH_SHORT).show();

                    DbViewModelData newDbData = new DbViewModelData(dbViewModel.getSelectedItem().getValue());
                    newDbData.activityToModify = new ActivityInfo(id,labelName.getText().toString(),labelTime.getText().toString());
                    dbViewModel.selectItem(newDbData);
                    viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.ModifyActivity));



                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // magari aggiungiamo una richiesta di conferma

                    DbViewModelData newData = dbViewModel.getSelectedItem().getValue();


                    Log.i("TEST",""+labelName.getText().toString()+" "+labelTime.getText().toString());
                    Log.i("TEST","immediatly after "+ newData.activityToDelete.getName());

                    ActivityInfo itemToDelete = new ActivityInfo(id,labelName.getText().toString(),labelTime.getText().toString());

                    newData.activityToDelete = itemToDelete;

                    Log.i("TEST","immediatly after again "+ newData.activityToDelete.getName());

                    dbViewModel.selectItem(newData);
                }
            });

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "item pressed this is the action",Toast.LENGTH_SHORT);

        }
    }
}
