package com.example.blooddonation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blooddonation.databinding.ActivityRegisterBinding;
import com.example.blooddonation.models.User;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private final String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    private final String[] genders = {"Male", "Female", "Other"};
    private static final String PREF_NAME = "UserPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupSpinners();
        setupClickListeners();
    }

    private void setupSpinners() {
        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, bloodGroups);
        binding.bloodGroupInput.setAdapter(bloodGroupAdapter);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, genders);
        binding.genderInput.setAdapter(genderAdapter);
    }

    private void setupClickListeners() {
        binding.registerButton.setOnClickListener(v -> {
            if (validateInput()) {
                registerUser();
            }
        });
    }

    private boolean validateInput() {
        boolean isValid = true;

        if (binding.nameInput.getText().toString().trim().isEmpty()) {
            binding.nameInput.setError("Name is required");
            isValid = false;
        }

        if (binding.ageInput.getText().toString().trim().isEmpty()) {
            binding.ageInput.setError("Age is required");
            isValid = false;
        } else {
            try {
                int age = Integer.parseInt(binding.ageInput.getText().toString());
                if (age < 18 || age > 65) {
                    binding.ageInput.setError("Age must be between 18 and 65");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                binding.ageInput.setError("Invalid age");
                isValid = false;
            }
        }

        if (binding.genderInput.getText().toString().trim().isEmpty()) {
            binding.genderInput.setError("Gender is required");
            isValid = false;
        }

        if (binding.bloodGroupInput.getText().toString().trim().isEmpty()) {
            binding.bloodGroupInput.setError("Blood group is required");
            isValid = false;
        }

        if (binding.phoneInput.getText().toString().trim().isEmpty()) {
            binding.phoneInput.setError("Phone number is required");
            isValid = false;
        }

        if (binding.locationInput.getText().toString().trim().isEmpty()) {
            binding.locationInput.setError("Location is required");
            isValid = false;
        }

        if (binding.emailInput.getText().toString().trim().isEmpty()) {
            binding.emailInput.setError("Email is required");
            isValid = false;
        }

        if (binding.passwordInput.getText().toString().trim().isEmpty()) {
            binding.passwordInput.setError("Password is required");
            isValid = false;
        } else if (binding.passwordInput.getText().toString().length() < 6) {
            binding.passwordInput.setError("Password must be at least 6 characters");
            isValid = false;
        }

        return isValid;
    }

    private void registerUser() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.registerButton.setEnabled(false);

        String email = binding.emailInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();

        User userData = new User(
                email, // Using email as user ID
                binding.nameInput.getText().toString().trim(),
                Integer.parseInt(binding.ageInput.getText().toString().trim()),
                binding.bloodGroupInput.getText().toString().trim(),
                binding.locationInput.getText().toString().trim(),
                binding.phoneInput.getText().toString().trim(),
                binding.genderInput.getText().toString().trim()
        );

        // Save user data to SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_id", userData.getUid());
        editor.putString("user_name", userData.getName());
        editor.putInt("user_age", userData.getAge());
        editor.putString("user_blood_group", userData.getBloodGroup());
        editor.putString("user_location", userData.getLocation());
        editor.putString("user_phone", userData.getPhoneNumber());
        editor.putString("user_gender", userData.getGender());
        editor.putBoolean("is_logged_in", true);
        editor.apply();

        binding.progressBar.setVisibility(View.GONE);
        binding.registerButton.setEnabled(true);
        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
        finish();
    }
} 