package com.example.android.top10downloadedappnew.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.android.top10downloadedappnew.activities.HomescreenActivity;
import com.example.android.top10downloadedappnew.activities.LoginActivity;
import com.example.android.top10downloadedappnew.activities.MainActivity;
import com.example.android.top10downloadedappnew.activities.RegistrationActivity;
import com.example.android.top10downloadedappnew.interfaces.IFirebaseAuthentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class FirebaseAuthentication implements IFirebaseAuthentication {

    private FirebaseAuth auth;

    public FirebaseAuthentication(FirebaseAuth mAuth) {
        this.auth = mAuth;
    }

    @Override
    public void createAccount(String email, String password, final Activity fromActivity) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(fromActivity, "Registration successful!", Toast.LENGTH_LONG).show();


                            Intent intent = new Intent(fromActivity, HomescreenActivity.class);
                            fromActivity.startActivity(intent);
                        } else {
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            switch (errorCode) {

                                case "ERROR_INVALID_EMAIL":
                                    Toast.makeText(fromActivity, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    Toast.makeText(fromActivity, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_WEAK_PASSWORD":
                                    Toast.makeText(fromActivity, "The password is invalid it must 6 characters at least", Toast.LENGTH_LONG).show();
                                    break;

                            }
                        }
                    }
                });
    }


    @Override
    public void login(String email, String password, final Activity fromActivity) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(fromActivity, "Login successful!", Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);

                            Intent intent = new Intent(fromActivity, MainActivity.class);
                            fromActivity.startActivity(intent);
                        } else {
                            Toast.makeText(fromActivity, "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            switch (errorCode) {

                                case "ERROR_INVALID_EMAIL":
                                    Toast.makeText(fromActivity, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    Toast.makeText(fromActivity, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_WRONG_PASSWORD":
                                    Toast.makeText(fromActivity, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    }
                });
    }
}
