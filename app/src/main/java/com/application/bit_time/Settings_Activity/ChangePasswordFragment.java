package com.application.bit_time.Settings_Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.application.bit_time.R;
import com.application.bit_time.utils.Db.DbManager;

public class ChangePasswordFragment extends Fragment {

    private EditText editTextNewPassword;
    private EditText editTextConfirmPassword;
    private Button btnSubmit;
    private DbManager dbManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_change_password_fragment, container, false);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        editTextNewPassword = view.findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        dbManager = new DbManager(requireContext()); // Initialize your DBManager (replace with your actual DB manager class)

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = editTextNewPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter both fields", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Passwords match, update the password in the database
                    String email = dbManager.getUserEmail();
                    dbManager.updatePassword(email, newPassword);
                    // Navigate to the Login fragment
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new AccountFragment())
                            .replace(R.id.bottomFragmentContainer, new Fragment());;
                    fragmentTransaction.commit();
                    Toast.makeText(requireContext(), "Password updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
