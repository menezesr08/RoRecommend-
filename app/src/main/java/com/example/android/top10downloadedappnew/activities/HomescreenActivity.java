package com.example.android.top10downloadedappnew.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.top10downloadedappnew.R;

public class HomescreenActivity extends AppCompatActivity {
    private Button logInButton;
    private Button signUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        initializeUI();

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomescreenActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomescreenActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeUI(){
        logInButton = findViewById(R.id.logInButton);
        signUpButton = findViewById(R.id.signUpButton);
    }
}
