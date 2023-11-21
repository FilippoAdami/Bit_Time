package com.application.bit_time.Settings_Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbManager;

import java.io.Console;

public class GamificationSettings extends Fragment {

    private RadioButton radio1, radio2;
    private ImageView happyIcon, sadIcon;
    private Spinner spinner1, spinner2;
    private EditText editText1, editText2, editText3, editText4, editText5, editText6;
    private Button applyButton;
    private DbManager dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_gamification_layout, container, false);
        dbManager = new DbManager(getActivity());

    // Initialize UI elements
        radio1 = view.findViewById(R.id.radio1);
        radio2 = view.findViewById(R.id.radio2);
        happyIcon = view.findViewById(R.id.PositiveIcon);
        sadIcon = view.findViewById(R.id.NegativeIcon);
        spinner1 = view.findViewById(R.id.iconSpinner1);
        spinner2 = view.findViewById(R.id.iconSpinner2);
        editText1 = view.findViewById(R.id.timePoints1);
        editText2 = view.findViewById(R.id.timePoints2);
        editText3 = view.findViewById(R.id.timePoints3);
        editText4 = view.findViewById(R.id.timePoints4);
        editText5 = view.findViewById(R.id.timePoints5);
        editText6 = view.findViewById(R.id.timePoints6);
        applyButton = view.findViewById(R.id.submitButton);

    // When the fragment is created, load the user's settings
        boolean gamificationType = dbManager.getGamificationType();
        Log.println(Log.DEBUG, "gamificationType", String.valueOf(gamificationType) );
        if (!gamificationType) {
            radio1.setChecked(true);
        } else {radio2.setChecked(true);}
        String positiveIcon = dbManager.getPositiveIcon();
        ArrayAdapter<CharSequence> adapterx = ArrayAdapter.createFromResource(
                getContext(),
                R.array.happy_icon_array,  // Create a string array resource containing the icon names
                android.R.layout.simple_spinner_item
        );
        adapterx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapterx);
        int spinner1Position = adapterx.getPosition(positiveIcon);
        spinner1.setSelection(spinner1Position);
        ArrayAdapter<CharSequence> adaptery = ArrayAdapter.createFromResource(
                getContext(),
                R.array.sad_icon_array,  // Create a string array resource containing the icon names
                android.R.layout.simple_spinner_item
        );
        adaptery.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adaptery);
        String negativeIcon = dbManager.getNegativeIcon();
        int spinner2Position = adaptery.getPosition(negativeIcon);
        spinner2.setSelection(spinner2Position);
        int timePoints[] = dbManager.getGamificationPoints();
        Log.println(Log.DEBUG, "timePoints", String.valueOf(timePoints[0]) );
        editText1.setText(String.valueOf(timePoints[0]));
        editText2.setText(String.valueOf(timePoints[1]));
        editText3.setText(String.valueOf(timePoints[2]));
        editText4.setText(String.valueOf(timePoints[3]));
        editText5.setText(String.valueOf(timePoints[4]));
        editText6.setText(String.valueOf(timePoints[5]));

    // when the user selects one of the radio buttons, the other one is deselected
        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio2.setChecked(false);
            }
        });
        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio1.setChecked(false);
            }
        });

    // when the user clicks a spinner, the icon list appears
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected icon name
                String selectedIcon = parentView.getItemAtPosition(position).toString();
                Log.println(Log.DEBUG, "selectedIcon", selectedIcon);
                // Set the selected icon to the ImageView
                int resourceId = getResources().getIdentifier(selectedIcon, "drawable", requireContext().getPackageName());
                Log.println(Log.DEBUG, "resourceId", String.valueOf(resourceId));
                happyIcon.setImageResource(resourceId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing when nothing is selected
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected icon name
                String selectedIcon = parentView.getItemAtPosition(position).toString();
                Log.println(Log.DEBUG, "selectedIcon", selectedIcon);
                // Set the selected icon to the ImageView
                int resourceId = getResources().getIdentifier(selectedIcon, "drawable", requireContext().getPackageName());
                Log.println(Log.DEBUG, "resourceId", String.valueOf(resourceId));
                sadIcon.setImageResource(resourceId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing when nothing is selected
            }
        });
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs from UI elements
                boolean radioSelection = radio1.isChecked() ? false : true;
                String spinner1Selection = spinner1.getSelectedItem().toString();
                String spinner2Selection = spinner2.getSelectedItem().toString();
                int editText1Value = Integer.parseInt(editText1.getText().toString());
                int editText2Value = Integer.parseInt(editText2.getText().toString());
                int editText3Value = Integer.parseInt(editText3.getText().toString());
                int editText4Value = Integer.parseInt(editText4.getText().toString());
                int editText5Value = Integer.parseInt(editText5.getText().toString());
                int editText6Value = Integer.parseInt(editText6.getText().toString());

                dbManager.changeGamificationType(radioSelection);
                dbManager.changePositiveIcon(spinner1Selection);
                dbManager.changeNegativeIcon(spinner2Selection);
                dbManager.changeGamificationPoints(editText1Value, editText2Value, editText3Value, editText4Value, editText5Value, editText6Value);

                // toast message
                Toast.makeText(getActivity(), "Gamification settings updated" , Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }
}
