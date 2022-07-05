package com.ultraflame42.moosicelectricboogaloo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.ultraflame42.moosicelectricboogaloo.login.LoginManager;
import com.ultraflame42.moosicelectricboogaloo.login.LoginStatus;
import com.ultraflame42.moosicelectricboogaloo.ui.login.AppSigninActivity;

public class AppLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login_home);

        LoginManager.LoggedInStatusEvent.addListener(() -> {
            Intent intent = new Intent(this, AppHomeActivity.class);
            startActivity(intent);
        });

        LoginManager.OnExitAppHome.addListenerOnce(() -> {
            finish();
        });

    }

    public void handleContinueAsGuest(View view) {
        LoginManager.setStatus(LoginStatus.GUEST);
    }


    // own login.
    public void handleLogin(View view) {
        // send to signin page
        Intent intent = new Intent(this, AppSigninActivity.class);
        startActivity(intent);

    }


}
