package com.example.android.top10downloadedappnew.interfaces;

import android.app.Activity;
import android.content.Context;

public interface IFirebaseAuthentication {

    void createAccount(String email, String password, Activity fromActivity);

    void login(String email, String password, Activity fromActivity);
}
