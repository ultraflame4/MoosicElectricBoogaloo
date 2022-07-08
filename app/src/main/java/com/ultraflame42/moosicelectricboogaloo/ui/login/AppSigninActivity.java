package com.ultraflame42.moosicelectricboogaloo.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.account.AccountManager;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

public class AppSigninActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText pwdInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_app_signin);

        emailInput = findViewById(R.id.signInEmailInput);
        pwdInput = findViewById(R.id.signInPasswordInput);

        AccountManager.OnAuthFailureEvent.addListener(data -> {
            Log.d("AppSigninActivity", "Auth failure: " + data);
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        });

    }

    public void handleCancel(View view) {
        finish();
    }

    public void handleSignin(View view) {

        String email = emailInput.getText().toString();
        String pwd = pwdInput.getText().toString();
        Log.d("AppSigninActivity", "Attempt signin with: " + email + "," + pwd);
        AccountManager.SignIn(email, pwd);
    }

}