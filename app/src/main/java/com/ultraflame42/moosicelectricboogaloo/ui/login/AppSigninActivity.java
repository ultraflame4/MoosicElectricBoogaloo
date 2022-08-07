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
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventListenerGroup;

public class AppSigninActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText pwdInput;
    private EventListenerGroup eGroup = new EventListenerGroup();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_app_signin);
        // Get email ans password edit text views
        emailInput = findViewById(R.id.signInEmailInput);
        pwdInput = findViewById(R.id.signInPasswordInput);

        // on failed sign in
        eGroup.subscribe(AccountManager.OnAuthFailureEvent, data -> {
            // Toast to user and log error
            Log.d("AppSigninActivity", "Auth failure: " + data);
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        });


    }

    public void handleCancel(View view) {
        // go back to login activity
        finish();
    }

    public void handleSignin(View view) {
        // Get email and password
        String email = emailInput.getText().toString();
        String pwd = pwdInput.getText().toString();
        Log.d("AppSigninActivity", "Attempt signin with: " + email + "," + pwd);
        // Attempt sign in using AccountManager
        AccountManager.SignIn(email, pwd);
    }

    @Override
    public void finish() {
        // prevent memory leaks by removing all listeners
        eGroup.unsubscribeAll();
        super.finish();
    }
}
