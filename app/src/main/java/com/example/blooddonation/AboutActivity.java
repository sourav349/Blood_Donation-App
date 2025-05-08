package com.example.blooddonation;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView aboutText = findViewById(R.id.aboutText);
        aboutText.setText("Blood Donation App\n\nOur mission is to connect blood donors with those in need, making it easier to save lives. We believe in the power of community and technology to make a positive impact.\n\nFeatures:\n- Find blood donors near you\n- Register as a donor\n- Track donations and lives saved\n\nThank you for being a part of this life-saving journey!");
    }
} 