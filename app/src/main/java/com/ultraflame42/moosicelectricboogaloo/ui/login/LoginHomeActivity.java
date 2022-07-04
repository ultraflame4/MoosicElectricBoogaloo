package com.ultraflame42.moosicelectricboogaloo.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.login.LoginManager;
import com.ultraflame42.moosicelectricboogaloo.login.LoginStatus;

public class LoginHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login_home);

    }

    @Override
    public void onBackPressed() {
    }

    public void handleContinueAsGuest(View view) {
        LoginManager.setStatus(LoginStatus.GUEST);
        finish();
    }
}
