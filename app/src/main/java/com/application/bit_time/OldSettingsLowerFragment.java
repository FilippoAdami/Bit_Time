package com.application.bit_time;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.application.bit_time.R;

import java.util.ArrayList;

public class OldSettingsLowerFragment extends ListFragment {

    ArrayList<String> content;
    ArrayAdapter<String> aAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        content = new ArrayList<>();

        content.add("ehi");
        content.add("hola");

        ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,content );
        setListAdapter(aAdapter);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_lower_fragment_layout, container, false);

        return view;

    }


    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }





}
