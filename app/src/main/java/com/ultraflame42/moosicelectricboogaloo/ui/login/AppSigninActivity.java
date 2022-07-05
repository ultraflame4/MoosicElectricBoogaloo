package com.ultraflame42.moosicelectricboogaloo.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.ultraflame42.moosicelectricboogaloo.R;

public class AppSigninActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText pwdInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_app_signin);

        emailInput = findViewById(R.id.signInEmailInput);
        pwdInput = findViewById(R.id.signInPasswordInput);
    }

    public void handleCancel(View view) {
        finish();
    }

    public void handleSignin(View view) {
        //todo
        String email = emailInput.getText().toString();
        String pwd = pwdInput.getText().toString();
        Log.d("AppSigninActivity", "Attempt signin with: " + email + "," + pwd);
    }

}