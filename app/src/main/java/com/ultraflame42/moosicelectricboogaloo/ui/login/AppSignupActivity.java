package com.ultraflame42.moosicelectricboogaloo.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.account.AccountManager;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

public class AppSignupActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText pwdInput;
    private EditText confirmPwdInput;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_signup);

        emailInput = findViewById(R.id.signUpEmailInput);
        pwdInput = findViewById(R.id.signUpPasswordInput);
        confirmPwdInput = findViewById(R.id.signUpConfirmPasswordInput);
        mAuth = FirebaseAuth.getInstance();

    }

    public void handleCancel(View view) {
        finish();
    }

    public void handleSignup(View view) {
        //todo
        String email = emailInput.getText().toString();
        String pwd = pwdInput.getText().toString();
        String confirmPwd = confirmPwdInput.getText().toString();

        if (!pwd.equals(confirmPwd)) {
            Toast toast = Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        Log.d("AppSignupActivity", "Attempt signup with: " + email + "and password: " + pwd);
        AccountManager.SignUp(email, pwd);


    }
}