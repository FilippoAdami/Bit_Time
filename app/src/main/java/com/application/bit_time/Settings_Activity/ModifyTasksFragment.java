package com.application.bit_time.Settings_Activity;

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

import com.application.bit_time.R;
import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.utils.Db.DbViewModelData;
import com.application.bit_time.utils.SettingsModeData;
import com.application.bit_time.utils.TaskItem;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.TimeHelper;

public class ModifyTasksFragment extends Fragment {


    private DbViewModel dbViewModel;


    private CustomViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);


        View view = inflater.inflate(R.layout.task_creation_upper_fragment_layout,container,false);

        TextView editName = view.findViewById(R.id.editTaskNameLabel);
        TextView edith = view.findViewById(R.id.editTextHours);
        TextView editmin = view.findViewById(R.id.editTextMinutes);
        TextView editsec = view.findViewById(R.id.editTextSeconds);

        Button confirmButton = view.findViewById(R.id.confirmButton);

        dbViewModel = new ViewModelProvider(getActivity()).get(DbViewModel.class);


        Log.i("TASKTOMOD",dbViewModel.getSelectedItem().getValue().taskItem.toString());

        TaskItem taskToModify = dbViewModel.getSelectedItem().getValue().taskItem;



        //int totalTime = taskToModify.getDurationInt();
        TimeHelper th = taskToModify.getTimeHelper();

        /*int h = (totalTime - totalTime % 60) / 60;
        int min = totalTime -h*60;
        int sec = 0;*/

        editName.setText(taskToModify.getName());
        /*edith.setText(Integer.toString(h));
        editmin.setText(Integer.toString(min));
        editsec.setText(Integer.toString(sec));*/

        edith.setText(Integer.toString(th.getHrs()));
        editmin.setText(Integer.toString(th.getMin()));
        editsec.setText(Integer.toString(th.getSec()));

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int h = parseContent(edith.getText().toString());
                int min = parseContent(editmin.getText().toString());
                int sec = parseContent(editsec.getText().toString());

                int totalTime = h*3600 + min*60 + sec;

                TaskItem newItem = new TaskItem(taskToModify.getID(),editName.getText().toString(),totalTime);

                Log.i("UPDATE",newItem.toString());

                DbViewModelData dbViewModelData = new DbViewModelData(
                        DbViewModelData.ACTION_TYPE.MODIFY,
                        DbViewModelData.ITEM_TYPE.TASK,
                        newItem);

                dbViewModel.selectItem(dbViewModelData);

                viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.MainEntry));




            }
        });




        return view;
    }



    public int parseContent(String stringToParse)
    {
        if(stringToParse.equals(""))
        {
            return 0;
        }
        else
        {
            return Integer.parseInt(stringToParse);
        }
    }
}
