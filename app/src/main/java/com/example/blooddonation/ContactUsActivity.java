package com.example.blooddonation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ContactUsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        TextView infoText = findViewById(R.id.infoText);
        infoText.setText("For any queries, suggestions, or support, please contact us:\n\nEmail: support@blooddonationapp.com\nPhone: +1-234-567-8901\n\nWe are here to help you 24/7!");

        Button emailButton = findViewById(R.id.emailButton);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:support@blooddonationapp.com"));
                startActivity(emailIntent);

            }
        });
    }
} 