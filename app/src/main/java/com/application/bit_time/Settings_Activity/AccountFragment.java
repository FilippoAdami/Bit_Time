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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.application.bit_time.Main_Activity.HomeFragment;
import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbManager;

public class AccountFragment extends Fragment {
    private EditText emailEditText;
    private EditText pinEditText;
    private TextView showHidePassword;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button saveButton;
    private Button logoutButton;
    private DbManager dbManager; // Initialize DbManager
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_account_and_stats_layout, container, false);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //check if user is logged in
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if(!sharedPreferences.getBoolean("loggedIn", false)){
            // If the user is not logged in, go to the LogInFragment
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.middle_fragment_container_view, new LogInFragment());
            fragmentTransaction.commit();
        }
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
        passwordEditText = view.findViewById(R.id.password_edit_text);
        saveButton = view.findViewById(R.id.submit);
        logoutButton = view.findViewById(R.id.logout);

        dbManager = new DbManager(getActivity());

        // Load and set user data in UI elements
        loadUserData();

        // Set click listener for save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the data to the database
                updateUserData();
                Toast.makeText(getActivity(), "Successfully updated!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch islogin to false
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("loggedIn", false);
                editor.apply();

                // Go to the home fragment
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.middle_fragment_container_view, new HomeFragment());
                fragmentTransaction.commit();
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
        int inputType = passwordEditText.getInputType();
        if (inputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            // Password is hidden, show it
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //showHidePassword.setImageResource(R.drawable.ic_visibility_on);
        } else {
            // Password is shown, hide it
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            //showHidePassword.setImageResource(R.drawable.ic_visibility_off);
        }
        // Move cursor to the end of the text
        passwordEditText.setSelection(passwordEditText.getText().length());
    }
    // Load user data from the database and set it in UI elements
    private void loadUserData() {
        String email = dbManager.getUserEmail(); // Get user's email
        String username = dbManager.getUsername(); // Get user's username
        String pin = dbManager.getUserPin(); // Get user's PIN

        emailEditText.setText(email);
        usernameEditText.setText(username);
        pinEditText.setText(pin);
    }
    // Update user data in the database
    private void updateUserData() {
        String email = emailEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String pin = pinEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Update email, username, and PIN
        dbManager.updateEmail(dbManager.getUserEmail(), email);
        dbManager.updateUsername(email, username);
        dbManager.updatePin(pin);

        // Update password if provided
        if (!password.isEmpty()) {
            dbManager.updatePassword(email, password);
        }

        Toast.makeText(getActivity(), "Successfully updated!", Toast.LENGTH_SHORT).show();
    }
}
