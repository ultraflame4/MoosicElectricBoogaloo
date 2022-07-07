package com.ultraflame42.moosicelectricboogaloo.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.account.AccountManager;
import com.ultraflame42.moosicelectricboogaloo.account.LoginStatus;
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
        String email = emailInput.getText().toString();
        String pwd = pwdInput.getText().toString();
        String confirmPwd = confirmPwdInput.getText().toString();

        if (!pwd.equals(confirmPwd)) {
            Toast toast = Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        Log.d("AppSignupActivity", "Attempt signup with: " + email + "and password: " + pwd);
        // Create account in firebase
        mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("AccountManager", "createUserWithEmail:success");
                        AccountManager.setAuthStatus(LoginStatus.LOGGED_IN);
                    } else {

                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            Toast toast = Toast.makeText(this, "Password is too weak! Must be atleast 6 characters", Toast.LENGTH_SHORT);
                            toast.show();
                        } catch (Exception e) {
                            Toast toast = Toast.makeText(this, "Signup failed! Check Logs", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        // If sign in fails, display a message to the user.
                        Log.w("AccountManager", "createUserWithEmail:failure", task.getException());
                        AccountManager.setAuthStatus(LoginStatus.NOT_LOGGED_IN);
                    }
                });


    }
}