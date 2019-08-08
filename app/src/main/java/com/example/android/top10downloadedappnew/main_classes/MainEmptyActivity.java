package com.example.android.top10downloadedappnew.main_classes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.android.top10downloadedappnew.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_empty);

        Intent activityIntent;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            activityIntent = new Intent(this, MainActivity.class);
        } else {
            // User is signed out
            activityIntent = new Intent(this, HomeScreen.class);
        }
        startActivity(activityIntent);
        finish();
    }
}
