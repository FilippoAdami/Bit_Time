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

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbManager;

public class GamificationSettings extends Fragment {
    private RadioButton radio1, radio2;
    private ImageView happyIcon, sadIcon;
    private Spinner spinner1, spinner2;
    private EditText editText1, editText2, editText3, editText4, editText5, editText6;
    private DbManager dbManager;

    boolean gamificationType;
    String positiveIcon;
    String negativeIcon;
    int[] timePoints;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set user configurations before the fragment is created
        dbManager = new DbManager(getActivity());
        gamificationType = dbManager.getGamificationType();
        positiveIcon = dbManager.getPositiveIcon();
        negativeIcon = dbManager.getNegativeIcon();
        timePoints = dbManager.getGamificationPoints();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_gamification_layout, container, false);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (checkChanges()) {
                    showConfirmationDialog();
                } else {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().remove(GamificationSettings.this).commit();
                    fragmentManager.popBackStack();
                }
            }
        });

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
        Button applyButton = view.findViewById(R.id.submitButton);

        // Set UI elements based on user configurations
        if (!gamificationType) {
            radio1.setChecked(true);
        } else {
            radio2.setChecked(true);
        }

        ArrayAdapter<CharSequence> adapterx = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.happy_icon_array,
                android.R.layout.simple_spinner_item
        );
        adapterx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapterx);
        int spinner1Position = adapterx.getPosition(positiveIcon);
        spinner1.setSelection(spinner1Position);

        ArrayAdapter<CharSequence> adaptery = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sad_icon_array,
                android.R.layout.simple_spinner_item
        );
        adaptery.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adaptery);
        int spinner2Position = adaptery.getPosition(negativeIcon);
        spinner2.setSelection(spinner2Position);

        editText1.setText(String.valueOf(timePoints[0]));
        editText2.setText(String.valueOf(timePoints[1]));
        editText3.setText(String.valueOf(timePoints[2]));
        editText4.setText(String.valueOf(timePoints[3]));
        editText5.setText(String.valueOf(timePoints[4]));
        editText6.setText(String.valueOf(timePoints[5]));

        // when the user selects one of the radio buttons, the other one is deselected
        radio1.setOnClickListener(v -> radio2.setChecked(false));
        radio2.setOnClickListener(v -> radio1.setChecked(false));

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
        applyButton.setOnClickListener(v -> updateUserData());
        return view;
    }

    private void updateUserData() {
        boolean radioSelection = !radio1.isChecked();
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
        Toast.makeText(getActivity(), "Impostazioni aggiornate correttamente" , Toast.LENGTH_SHORT).show();
    }

    private void showConfirmationDialog() {
        Log.i("back", "showConfirmationDialog: ");
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Save Changes?");
        builder.setMessage("Do you want to save your changes before exiting?");
        builder.setPositiveButton("Save", (dialogInterface, i) -> {
            // Save the changes
            updateUserData();
            requireActivity().onBackPressed();
        });
        builder.setNegativeButton("Discard", (dialogInterface, i) -> {
            // Discard the changes
            requireActivity().onBackPressed();
        });
        builder.setNeutralButton("Cancel", (dialogInterface, i) -> {
            // Cancel the dialog, do nothing
        });
        builder.show();
    }
    public boolean checkChanges(){
        boolean radioSelection = !radio1.isChecked();
        String spinner1Selection = spinner1.getSelectedItem().toString();
        String spinner2Selection = spinner2.getSelectedItem().toString();
        int editText1Value = Integer.parseInt(editText1.getText().toString());
        int editText2Value = Integer.parseInt(editText2.getText().toString());
        int editText3Value = Integer.parseInt(editText3.getText().toString());
        int editText4Value = Integer.parseInt(editText4.getText().toString());
        int editText5Value = Integer.parseInt(editText5.getText().toString());
        int editText6Value = Integer.parseInt(editText6.getText().toString());

        if (radioSelection != dbManager.getGamificationType()) {
            return true;
        }
        if (!spinner1Selection.equals(dbManager.getPositiveIcon())) {
            return true;
        }
        if (!spinner2Selection.equals(dbManager.getNegativeIcon())) {
            return true;
        }
        if (editText1Value != dbManager.getGamificationPoints()[0]) {
            return true;
        }
        if (editText2Value != dbManager.getGamificationPoints()[1]) {
            return true;
        }
        if (editText3Value != dbManager.getGamificationPoints()[2]) {
            return true;
        }
        if (editText4Value != dbManager.getGamificationPoints()[3]) {
            return true;
        }
        if (editText5Value != dbManager.getGamificationPoints()[4]) {
            return true;
        }
        return editText6Value != dbManager.getGamificationPoints()[5];
    }
}
