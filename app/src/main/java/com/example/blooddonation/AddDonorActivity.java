package com.example.blooddonation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.blooddonation.database.DonorDatabase;
import com.example.blooddonation.models.Donor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddDonorActivity extends AppCompatActivity {
    private AutoCompleteTextView bloodGroupSpinner;
    private Button saveButton;
    private ImageView profileImageView;
    private ProgressBar progressBar;
    private Uri selectedImageUri;
    private DonorDatabase donorDatabase;
    private ExecutorService executorService;

    private final String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    Glide.with(this)
                            .load(selectedImageUri)
                            .into(profileImageView);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donor);

        // Initialize database and executor
        donorDatabase = DonorDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        // Initialize views
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
        saveButton = findViewById(R.id.saveButton);
        profileImageView = findViewById(R.id.profileImageView);
        progressBar = findViewById(R.id.progressBar);

        // Setup blood group spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, bloodGroups);
        bloodGroupSpinner.setAdapter(adapter);

        // Set click listeners
        profileImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImage.launch(intent);
        });

        saveButton.setOnClickListener(v -> saveDonorInfo());
    }

    private void saveDonorInfo() {
        String bloodGroup = bloodGroupSpinner.getText().toString().trim();

        if (TextUtils.isEmpty(bloodGroup)) {
            Toast.makeText(this, "Please select blood group", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Save image to internal storage if selected
        String imagePath = null;
        if (selectedImageUri != null) {
            try {
                imagePath = saveImageToInternalStorage(selectedImageUri);
            } catch (IOException e) {
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
        }

        // Create and save donor
        final String finalImagePath = imagePath;
        executorService.execute(() -> {
            Donor donor = new Donor(
                    "New Donor", // You can add more fields in the UI if needed
                    "Phone Number",
                    "Address",
                    bloodGroup,
                    "Never",
                    finalImagePath,
                    true
            );

            donorDatabase.donorDao().insert(donor);
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddDonorActivity.this, "Donor information saved successfully",
                        Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private String saveImageToInternalStorage(Uri imageUri) throws IOException {
        String fileName = "donor_" + System.currentTimeMillis() + ".jpg";
        File file = new File(getFilesDir(), fileName);

        try (InputStream inputStream = getContentResolver().openInputStream(imageUri);
             OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }

        return file.getAbsolutePath();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
} 