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
    private List<ActivityInfo> list;
    private SettingsLowerFragment settingsLowerFragment;

    public ListAdapter(SettingsLowerFragment settingsLowerFragment, List<ActivityInfo> list)
    {
        this.settingsLowerFragment = settingsLowerFragment;
        this.list = list;

        dbViewModel = new ViewModelProvider(settingsLowerFragment.requireActivity()).get(DbViewModel.class);
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

        ActivityInfo activityInfo = list.get(position);
        holder.labelName.setText(activityInfo.getName());
        holder.labelTime.setText(activityInfo.getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
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
                    Toast.makeText(labelName.getContext(), "modify button is pressed",Toast.LENGTH_SHORT).show();

                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // magari aggiungiamo una richiesta di conferma

                    DbViewModelData newData = dbViewModel.getSelectedItem().getValue();


                    Log.i("TEST",""+labelName.getText().toString()+" "+labelTime.getText().toString());
                    Log.i("TEST","immediatly after "+ newData.activityToDelete.getName());

                    ActivityInfo itemToDelete = new ActivityInfo(labelName.getText().toString(),labelTime.getText().toString());

                    newData.activityToDelete = itemToDelete;

                    Log.i("TEST","immediatly after again "+ newData.activityToDelete.getName());

                    dbViewModel.selectItem(newData);
                }
            });

        }

        @Override
        public void onClick(View view) {
            //settingsLowerFragment.showNote(getAdapterPosition());
        }
    }
}
