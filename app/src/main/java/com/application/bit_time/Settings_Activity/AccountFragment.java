package com.application.bit_time.Settings_Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.InputFilter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.application.bit_time.R;

public class AccountFragment extends Fragment {
    private EditText emailEditText;
    private EditText pinEditText;
    private TextView showHidePassword;
    private EditText usernameEditText;
    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_account_and_stats_layout, container, false);

        // Initialize UI elements
        emailEditText = view.findViewById(R.id.emailEditText);
        pinEditText = view.findViewById(R.id.pinEditText);
        pinEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        pinEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        pinEditText.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               // Check if the input is less than 4 digits
               String input = charSequence.toString();
               if (input.length() < 4) {
                   // Display an error or provide feedback to the user
                   // For example, set an error message on the EditText
                   pinEditText.setError("PIN must be at least 4 digits.");
               } else {
                   // Clear the error if the input is valid
                   pinEditText.setError(null);
               }
           }
           @Override
           public void afterTextChanged(Editable s) {

           }
        });

        showHidePassword = view.findViewById(R.id.showHidePassword);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        saveButton = view.findViewById(R.id.submit);

        //Find the saved data and set it to the UI elements
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("user_email", "");
        String savedPIN = sharedPreferences.getString("storedPIN", "");
        String savedUsername = sharedPreferences.getString("user_name", "");

        emailEditText.setText(savedEmail);
        pinEditText.setText(savedPIN);
        usernameEditText.setText(savedUsername);

        // Set click listener for save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save data to shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_email", emailEditText.getText().toString());
                editor.putString("storedPIN", pinEditText.getText().toString());
                editor.putString("user_name", usernameEditText.getText().toString());
                editor.apply();
                Toast.makeText(getActivity(), "Successfully updated!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for show/hide password toggle
        showHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        return view;
    }

    // Toggle password visibility
    private void togglePasswordVisibility() {
        int inputType = pinEditText.getInputType();
        if (inputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            // Password is hidden, show it
            pinEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //showHidePassword.setImageResource(R.drawable.ic_visibility_on);
        } else {
            // Password is shown, hide it
            pinEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            //showHidePassword.setImageResource(R.drawable.ic_visibility_off);
        }
        // Move cursor to the end of the text
        pinEditText.setSelection(pinEditText.getText().length());
    }
}
