package com.application.bit_time;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Locale;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.temporary_code_layout, container, false);

        // Initialize UI elements
        countdownTextView = rootView.findViewById(R.id.countdownTextView);
        textInputEditText = rootView.findViewById(R.id.textInputEditText);

        // Set up TextWatcher to handle input changes
        textInputEditText.addTextChangedListener(watcher);

        // Set the initial countdown text
        updateCountdownText();
        // Start the countdown handler
        startCountdownHandler();
        // Set click listener for countdownTextView
        countdownTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remainingTime > 0) {
                    return;
                }
                // Reset the remaining time and start the countdown handler again
                remainingTime = 120;
                startCountdownHandler();

                // TODO: Handle resend code action
                // For example, show a confirmation dialog before resending the code
            }
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
        countdownHandler = new Handler(Looper.myLooper());
        countdownHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (remainingTime > 0) {
                    remainingTime--;
                    updateCountdownText();
                    startCountdownHandler();
                }
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
