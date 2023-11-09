package com.application.bit_time.Settings_Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.bit_time.utils.CustomViewModel;
import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbViewModel;

public class SettingsMiddleFragment extends Fragment {

    CustomViewModel viewModel;
    DbViewModel dbViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);
        dbViewModel = new ViewModelProvider(requireActivity()).get(DbViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.settings_middle_fragment_layout,container,false);

        Button addButton = view.findViewById(R.id.addButton);
        TextView text = view.findViewById(R.id.label);
        text.setText("Nuova attività");


        Log.i("TEST","res da middle "+dbViewModel.getSelectedItem().getValue());

        viewModel.getSelectedItem().observe(this,item -> {


            if(item.equals("Tasks"))
            {
                text.setText("Nuovo task");


                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),"pressed", Toast.LENGTH_SHORT).show();

                        viewModel.selectItem(new SettingsModeData(SettingsModeData.Mode.NewTask));


                    }
                });
            }
            else if(item.equals("Activities")) {
                text.setText("Nuova attività");

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
        });




        return view;
    }
}