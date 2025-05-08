package com.example.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class WelcomeActivity extends AppCompatActivity {
    private TextView donorsCountText;
    private TextView donationsCountText;
    private TextView livesSavedText;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Initialize views
        donorsCountText = findViewById(R.id.donorsCountText);
        donationsCountText = findViewById(R.id.donationsCountText);
        livesSavedText = findViewById(R.id.livesSavedText);

        // Setup click listeners
        setupClickListeners();

        // Load statistics
        loadStatistics();
    }

    private void setupClickListeners() {
        // Find a Donor
        findViewById(R.id.findDonorCard).setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        });

        // Register as Donor
        findViewById(R.id.registerDonorCard).setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
        });

        // Contact Us
        findViewById(R.id.contactUsCard).setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, ContactUsActivity.class));
        });

        // About
        findViewById(R.id.aboutCard).setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, AboutActivity.class));
        });
    }

    private void loadStatistics() {
        // Get total donors count
        db.collection("donors")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int donorsCount = queryDocumentSnapshots.size();
                    donorsCountText.setText(String.valueOf(donorsCount));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load statistics", Toast.LENGTH_SHORT).show();
                });

        // Get total donations count
        db.collection("donations")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int donationsCount = queryDocumentSnapshots.size();
                    donationsCountText.setText(String.valueOf(donationsCount));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load statistics", Toast.LENGTH_SHORT).show();
                });

        // Calculate lives saved (assuming each donation saves 3 lives)
        db.collection("donations")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int livesSaved = queryDocumentSnapshots.size() * 3;
                    livesSavedText.setText(String.valueOf(livesSaved));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load statistics", Toast.LENGTH_SHORT).show();
                });
    }
} 