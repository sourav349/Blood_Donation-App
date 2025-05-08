package com.example.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonation.adapters.DonorAdapter;
import com.example.blooddonation.database.DonorDatabase;
import com.example.blooddonation.models.Donor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.example.blooddonation.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView bloodGroupSpinner;
    private RecyclerView donorsRecyclerView;
    private FloatingActionButton addDonorFab;
    private DonorAdapter donorAdapter;
    private List<User> donorList;
    private FirebaseFirestore db;

    private final String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        // Initialize views
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
        donorsRecyclerView = findViewById(R.id.recyclerView);
        addDonorFab = findViewById(R.id.fabAddDonor);

        // Setup blood group spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, bloodGroups);
        bloodGroupSpinner.setAdapter(adapter);

        // Setup RecyclerView
        donorList = new ArrayList<>();
        donorAdapter = new DonorAdapter(this, donorList, donor -> {
            // Handle donor click
            Toast.makeText(this, "Selected: " + donor.getName(), Toast.LENGTH_SHORT).show();
        });
        donorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        donorsRecyclerView.setAdapter(donorAdapter);

        // Setup click listeners
        findViewById(R.id.searchButton).setOnClickListener(v -> searchDonors());
        addDonorFab.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddDonorActivity.class));
        });

        // Load all donors initially
        loadDonors();
    }

    private void searchDonors() {
        String selectedBloodGroup = bloodGroupSpinner.getText().toString();
        if (selectedBloodGroup.isEmpty()) {
            Toast.makeText(this, "Please select a blood group", Toast.LENGTH_SHORT).show();
            return;
        }
        db.collection("donors")
          .whereEqualTo("bloodGroup", selectedBloodGroup)
          .get()
          .addOnSuccessListener(queryDocumentSnapshots -> {
              donorList.clear();
              for (DocumentSnapshot doc : queryDocumentSnapshots) {
                  User user = doc.toObject(User.class);
                  donorList.add(user);
              }
              donorAdapter.updateDonors(donorList);
          })
          .addOnFailureListener(e -> {
              Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
          });
    }

    private void loadDonors() {
        db.collection("donors")
          .get()
          .addOnSuccessListener(queryDocumentSnapshots -> {
              donorList.clear();
              for (DocumentSnapshot doc : queryDocumentSnapshots) {
                  User user = doc.toObject(User.class);
                  donorList.add(user);
              }
              donorAdapter.updateDonors(donorList);
          })
          .addOnFailureListener(e -> {
              Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
          });
    }
}