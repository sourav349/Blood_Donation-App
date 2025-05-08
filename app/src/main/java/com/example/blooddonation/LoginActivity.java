package com.example.blooddonation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blooddonation.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private static final String PREF_NAME = "UserPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.loginButton.setOnClickListener(v -> {
            if (validateInput()) {
                loginUser();
            }
        });

        binding.registerLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private boolean validateInput() {
        boolean isValid = true;

        if (binding.emailInput.getText().toString().trim().isEmpty()) {
            binding.emailInput.setError("Email is required");
            isValid = false;
        }

        if (binding.passwordInput.getText().toString().trim().isEmpty()) {
            binding.passwordInput.setError("Password is required");
            isValid = false;
        }

        return isValid;
    }

    private void loginUser() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.loginButton.setEnabled(false);

        String email = binding.emailInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();

        // Check credentials in SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String storedUserId = preferences.getString("user_id", "");

        if (storedUserId.equals(email)) {
            // Update login status
            preferences.edit().putBoolean("is_logged_in", true).apply();
            
            binding.progressBar.setVisibility(View.GONE);
            binding.loginButton.setEnabled(true);
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            
            // Start MainActivity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.loginButton.setEnabled(true);
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
} 