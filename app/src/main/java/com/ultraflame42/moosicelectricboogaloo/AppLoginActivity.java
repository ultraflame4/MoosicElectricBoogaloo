package com.ultraflame42.moosicelectricboogaloo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.ultraflame42.moosicelectricboogaloo.login.LoginManager;
import com.ultraflame42.moosicelectricboogaloo.login.LoginStatus;

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

        LoginManager.OnExitAppHome.addListener(() -> {
            finish();
        });
    }


    public void handleContinueAsGuest(View view) {
        LoginManager.setStatus(LoginStatus.GUEST);
    }

}
