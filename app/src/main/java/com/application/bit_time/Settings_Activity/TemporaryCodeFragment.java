package com.application.bit_time.Settings_Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbManager;
import com.application.bit_time.utils.JavaMail;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class TemporaryCodeFragment extends Fragment {

    // Declare UI elements
    private EditText textInputEditText;
    String currentInput = "- - - - - -";
    private TextView countdownTextView;
    private int remainingTime = 120; // Initial value in seconds
    private Handler countdownHandler;
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Do nothing
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Update currentInput and formatted text
            currentInput = s.toString().replaceAll("\\s", "");
            updateFormattedText();
        }
        @Override
        public void afterTextChanged(Editable s) {
            // Do nothing
        }
    };
    private DbManager dbManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.s_temporary_code_layout, container, false);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        // Initialize UI elements
        countdownTextView = rootView.findViewById(R.id.countdownTextView);
        textInputEditText = rootView.findViewById(R.id.textInputEditText);
        // Set up TextWatcher to handle input changes
        textInputEditText.addTextChangedListener(watcher);
        Button submitButton = rootView.findViewById(R.id.submitButton);

        dbManager = new DbManager(getContext());
        //get the email from the database
        String email = dbManager.getUserEmail();
        //generate a random code
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        String code = Integer.toString(randomNumber);
        //save the code in the database as new password
        dbManager.updatePassword(email, code);

        //send email
        JavaMail.sendEmail(email, "Temporary code", "Your temporary code is: " + code);

        // Set the initial countdown text
        updateCountdownText();
        // Start the countdown handler
        startCountdownHandler();

        // Set click listener for submitButton
        submitButton.setOnClickListener(v -> {
            // Check if the input is correct
            if (currentInput.equals(code)) {
                // Navigate to the ChangePasswordFragment
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.middle_fragment_container_view, new ChangePasswordFragment()).commit();

            } else {
                // Show an error message
                Toast.makeText(getActivity(), "Codice errato", Toast.LENGTH_SHORT).show();
            }
        });
        // Set click listener for countdownTextView
        countdownTextView.setOnClickListener(v -> {
            if (remainingTime > 0) {
                return;
            }
            // Reset the remaining time and start the countdown handler again
            remainingTime = 120;
            startCountdownHandler();

            int randomNumber1 = random.nextInt(900000) + 100000;
            String code1 = Integer.toString(randomNumber1);
            //save the code in the database as new password
            dbManager.updatePassword(email, code1);
            //send email
            JavaMail.sendEmail(email, "Temporary code", "Your temporary code is: " + code1);
            //show a toast message
            Toast.makeText(getActivity(), "controlla la tua email", Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }

    // Update the formatted text in the EditText
    private void updateFormattedText() {
        // Remove all text change listeners to prevent infinite loop
        textInputEditText.removeTextChangedListener(watcher);

        // Format the text
        StringBuilder formattedText = new StringBuilder();
        for (int i = 0; i < currentInput.length(); i++) {
            formattedText.append(currentInput.charAt(i));
        }

        // Update the text
        textInputEditText.setText(formattedText.toString());

        // Set the cursor position
        textInputEditText.setSelection(formattedText.length());

        // Add text change listeners again
        textInputEditText.addTextChangedListener(watcher);
    }

    // Start the countdown timer
    private void startCountdownHandler() {
        countdownHandler = new Handler(Objects.requireNonNull(Looper.myLooper()));
        countdownHandler.postDelayed(() -> {
            if (remainingTime > 0) {
                remainingTime--;
                updateCountdownText();
                startCountdownHandler();
            }
        }, 1000); // Run every second
    }

    // Update the countdown text and enable/disable countdownTextView
    private void updateCountdownText() {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        String countdownText = String.format(Locale.getDefault(), "Resend code in %02d:%02d", minutes, seconds);
        countdownTextView.setText(countdownText);

        // Enable/disable the countdownTextView based on the remaining time
        countdownTextView.setEnabled(remainingTime <= 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove the callbacks to prevent memory leaks
        if (countdownHandler != null) {
            countdownHandler.removeCallbacksAndMessages(null);
        }
    }
}
