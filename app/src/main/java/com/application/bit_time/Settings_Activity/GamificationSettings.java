package com.application.bit_time.Settings_Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.application.bit_time.R;

public class GamificationSettings extends Fragment {

    private RadioButton radio1, radio2;
    private Spinner spinner1, spinner2;
    private EditText editText1, editText2, editText3, editText4, editText5, editText6;
    private Button applyButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_gamification_layout, container, false);

        // Initialize UI elements
        radio1 = view.findViewById(R.id.radio1);
        radio2 = view.findViewById(R.id.radio2);
        spinner1 = view.findViewById(R.id.iconSpinner1);
        spinner2 = view.findViewById(R.id.iconSpinner2);
        editText1 = view.findViewById(R.id.timePoints1);
        editText2 = view.findViewById(R.id.timePoints2);
        editText3 = view.findViewById(R.id.timePoints3);
        editText4 = view.findViewById(R.id.timePoints4);
        editText5 = view.findViewById(R.id.timePoints5);
        editText6 = view.findViewById(R.id.timePoints6);
        applyButton = view.findViewById(R.id.submitButton);

        // Add your logic for radio buttons, spinners, and the apply button here

        // Example: When the apply button is clicked, save the user's settings
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs from UI elements
                String radioSelection = radio1.isChecked() ? "Bonus/Malus" : "Solo bonus";
                String spinner1Selection = spinner1.getSelectedItem().toString();
                String spinner2Selection = spinner2.getSelectedItem().toString();
                String editText1Value = editText1.getText().toString();
                String editText2Value = editText2.getText().toString();
                String editText3Value = editText3.getText().toString();
                String editText4Value = editText4.getText().toString();
                String editText5Value = editText5.getText().toString();
                String editText6Value = editText6.getText().toString();

                // Perform actions with the user's settings, such as saving them to preferences or a database
                // You can use SharedPreferences or your preferred method to store settings

                // Example: Save settings to SharedPreferences
                // SharedPreferences prefs = getActivity().getSharedPreferences("MySettings", Context.MODE_PRIVATE);
                // SharedPreferences.Editor editor = prefs.edit();
                // editor.putString("radioSelection", radioSelection);
                // editor.putString("spinner1Selection", spinner1Selection);
                // editor.putString("spinner2Selection", spinner2Selection);
                // editor.putString("editText1Value", editText1Value);
                // editor.putString("editText2Value", editText2Value);
                // editor.putString("editText3Value", editText3Value);
                // editor.putString("editText4Value", editText4Value);
                // editor.putString("editText5Value", editText5Value);
                // editor.putString("editText6Value", editText6Value);
                // editor.apply();

                // You can also perform other actions or validations based on the user's settings
            }
        });

        return view;
    }
}
