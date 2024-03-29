package com.application.bit_time.Settings_Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbViewModel;
import com.application.bit_time.utils.SettingsModeData;

public class SettingsMiddleFragment extends Fragment {

    CustomViewModel viewModel;
    DbViewModel dbViewModel;
    TextView text;
    Button addButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);
        dbViewModel = new ViewModelProvider(requireActivity()).get(DbViewModel.class);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.s_settings_middle_fragment_layout,container,false);

        addButton = view.findViewById(R.id.addButton);
        text = view.findViewById(R.id.label);

        Bundle b = getArguments();


        /*viewModel.getSelectedItem().observe(this.getActivity(),item -> {

            Log.i("viewModel value",item.toString());
            if(item.equals("Tasks"))
            {
                Log.i("ehi man","i check this");
                tasksSelected();
            }
            else if(item.equals("Activities") || item.equals("EntryPoint")) {
                activitiesSelected();
            }


        });*/




        if(b != null)
        {
            Log.i("bundle settMidFrag",b.toString());
            String modeStr = b.getString("mode","noValue");
            Log.i("SettMidFrag modeStr",modeStr);
            if(modeStr.equals("Tasks"))
            {
                tasksSelected();
            }else
            {
                activitiesSelected();
            }
            b.clear();

        }else
        {
            Log.i("SettMidFrag def","actmode");
            activitiesSelected();
        }




        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                Log.i("SMF Width",Integer.toString(view.getWidth()));
                view.findViewById(R.id.addButton).setLayoutParams(new LinearLayout.LayoutParams(view.getWidth()/2, ViewGroup.LayoutParams.MATCH_PARENT));

                view.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });



        /*text.setText(R.string.newActivityLabel);
        addButton.setText(R.string.addActivityBtn);

        Log.i("TEST","res da middle "+dbViewModel.getSelectedItem().getValue());*/






        return view;
    }



    private void tasksSelected()
    {
        text.setText(R.string.newTaskLabel);
        addButton.setText(R.string.addTaskBtn);
        Log.i("ehi man","already here");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(),"pressed", Toast.LENGTH_SHORT).show();

                viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.NewTask));


            }
        });
    }


    private void activitiesSelected()
    {
        Log.i("ehi man","in activitiesSelected");
        text.setText(R.string.newActivityLabel);
        addButton.setText(R.string.addActivityBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.NewActivity));


                        /*Fragment upperFrag = new PlaceholderFragment();
                        Fragment middleFrag = new PlaceholderFragment();
                        Fragment lowerFrag = new PlaceholderFragment();

                        //FragmentManager parentfManager = getParentFragmentManager();

                        /*parentfManager.beginTransaction()
                                .replace(R.id.top_fragment_container_view,upperFrag)
                                .replace(R.id.middle_fragment_container_view,middleFrag)
                                .replace(R.id.bottom_fragment_container_view,lowerFrag)
                                .commit();*/
            }
        });
    }
}
