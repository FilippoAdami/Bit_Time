package com.application.bit_time.Settings_Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.application.bit_time.Main_Activity.HomeFragment;
import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbContract;
import com.application.bit_time.utils.Db.DbManager;

public class LogInFragment extends Fragment {

    private DbManager dbManager;
    //private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private Button submitButton;
    private TextView forgotPassword;
    private EditText password_edit_text;
    private EditText emailEditText;
    private TextView switchMode;
    private View showHidePassword;
    private DbManager.DbHelper dbHelper;
    private boolean isLoginMode = true;
    private SharedPreferences sharedPreferences;

    // Define the ActivityResultLauncher for Google Sign-In
    /*
    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RC_SIGN_IN) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);
                }
            }
    );
     */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_login_fragment_layout, container, false);

        /*
        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
        */
        dbManager = new DbManager(getContext());
        dbHelper = new DbManager.DbHelper(getContext());
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        emailEditText = view.findViewById(R.id.emailEditText);
        password_edit_text = view.findViewById(R.id.password_edit_text);
        showHidePassword = view.findViewById(R.id.showHidePassword);
        switchMode = view.findViewById(R.id.switchMode);
        submitButton = view.findViewById(R.id.submit_button);
        forgotPassword = view.findViewById(R.id.forgotPassword);

        // Set initial mode (login)
        updateUIForMode();
        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle between login and sign-up modes
                isLoginMode = !isLoginMode;
                updateUIForMode();
            }
        });

        // Set a click listener for the sign-in button
        view.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the user-entered email and password
                String email = emailEditText.getText().toString();
                String password = password_edit_text.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    // Show an error message if email or password is empty
                    Toast.makeText(getActivity(), "insert email and password first", Toast.LENGTH_SHORT).show();
                return;
                }

                // Handle login or sign-up based on the mode
                if (isLoginMode) {
                    // Check the credentials in the database
                    if (isCredentialsValid(email, password)) {
                        // Set the user as logged in
                        sharedPreferences.edit().putBoolean("loggedIn", true).apply();
                        // redirect to home fragment
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new AccountFragment());
                        fragmentTransaction.commit();
                    } else {
                        // Credentials are not valid, show an error message
                        Toast.makeText(getActivity(), "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();                    }

                } else {
                    // Insert the email and password into the database
                    saveCredentialsToDatabase(email, password);
                }
            }
        });
        /*view.findViewById(R.id.Glogin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
                fragmentTransaction.commit();
            }
        });
         */
        showHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        //set a click listener for the forgot password textview
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the user-entered email
                String email = emailEditText.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    //show an error message if email is empty
                    Toast.makeText(getActivity(), "Please enter your email first", Toast.LENGTH_SHORT).show();
                    return;
                }
                //search for the user in the database
                Cursor cursor = dbManager.searchUser(email);
                if (cursor.moveToFirst()) {
                    //user found, show a dialog to get a temporary code
                    ForgotPasswordDialog dialog = new ForgotPasswordDialog();
                    dialog.show(getChildFragmentManager(), "ForgotPasswordDialog");
                } else {
                    //user not found, show an error message
                    Toast.makeText(getActivity(), "User not found. Please sign up.", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });


        return view;
    }
    /*private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent); // Launch the activity with the launcher
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult();
            // Signed in successfully, get user's information
            String displayName = account.getDisplayName();
            String email = account.getEmail();
            // You can now use this user information as needed
        } catch (Exception e) {
            // Sign in failed, handle the exception
        }
    }
     */
    private void updateUIForMode() {
        if (isLoginMode) {
            // Update UI elements for login mode
            submitButton.setText("Login");
            switchMode.setText("Don't have an account yet? \n click here to Sign Up");
            forgotPassword.setVisibility(View.VISIBLE);
        } else {
            // Update UI elements for sign-up mode
            submitButton.setText("Sign Up");
            switchMode.setText("Already have an account? \n click here to Login");
            forgotPassword.setVisibility(View.GONE);
        }
    }
    private void togglePasswordVisibility() {
        int inputType = password_edit_text.getInputType();
        if (inputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            // Password is hidden, show it
            password_edit_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //showHidePassword.setImageResource(R.drawable.ic_visibility_on);
        } else {
            // Password is shown, hide it
            password_edit_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            //showHidePassword.setImageResource(R.drawable.ic_visibility_off);
        }
        // Move cursor to the end of the text
        password_edit_text.setSelection(password_edit_text.getText().length());
    }
    private void saveCredentialsToDatabase(String email, String password) {
        // Search for the user in the database
        Cursor cursor = dbManager.searchUser(email);
        if (cursor.moveToFirst()) {
            // User found, say that the user is already registered and switch to login mode
            isLoginMode = true;
            updateUIForMode();
            Toast.makeText(getActivity(), "User already registered. Please login.", Toast.LENGTH_SHORT).show();
            cursor.close();
        } else if (dbManager.isUserRegistered()) {
            // User not found, but some other user is already registered, so show an error message
            isLoginMode = true;
            updateUIForMode();
            Toast.makeText(getActivity(), "Some other user is already registered. Please login.", Toast.LENGTH_SHORT).show();
            cursor.close();
        } else {
            // User not found, so register the new user
            dbManager.registerUser(email, password);

            // Show a success message, clear the fields and switch to login mode
            isLoginMode = true;
            updateUIForMode();
            Toast.makeText(getActivity(), "Registration successful. Please login.", Toast.LENGTH_SHORT).show();
            emailEditText.setText("");
            password_edit_text.setText("");
            cursor.close();
        }
    }
    private boolean isCredentialsValid(String email, String password) {
        // Search for the user in the database
        Cursor cursor = dbManager.searchUser(email);
        if (cursor.moveToFirst()) {
            // User found, check the password using the checkUser method
            boolean isPasswordCorrect = dbManager.checkUser(cursor, password);
            cursor.close();
            return isPasswordCorrect;
        } else {
            // User not found, so say that the user is not registered
            Toast.makeText(getActivity(), "User not registered. Please sign up.", Toast.LENGTH_SHORT).show();
            cursor.close();
            return false;
        }
    }
}