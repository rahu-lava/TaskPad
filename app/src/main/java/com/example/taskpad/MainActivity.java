package com.example.taskpad;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Create a handler to delay the transition from the splash screen
        Handler handlers = new Handler();
        handlers.postDelayed(() -> {
            // Start AuthActivity after the delay
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            // Finish SplashActivity so the user cannot return to it
            finish();
        }, 3300); // Delay of 4 seconds
    }
}