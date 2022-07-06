package com.ultraflame42.moosicelectricboogaloo;

import static android.provider.Settings.System.getString;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ultraflame42.moosicelectricboogaloo.account.AccountManager;
import com.ultraflame42.moosicelectricboogaloo.account.LoginStatus;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;
import com.ultraflame42.moosicelectricboogaloo.ui.login.AppSigninActivity;
import com.ultraflame42.moosicelectricboogaloo.ui.login.AppSignupActivity;

public class AppLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);
        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        AccountManager.init();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this,getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        AccountManager.LoggedInStatusEvent.addListener(() -> {
            Intent intent = new Intent(this, AppHomeActivity.class);
            startActivity(intent);
        });

        AccountManager.OnExitAppHome.addListenerOnce(() -> {
            finish();
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            AccountManager.setAuthStatus(LoginStatus.LOGGED_IN);
        }
    }

    public void handleContinueAsGuest(View view) {
        AccountManager.setAuthStatus(LoginStatus.GUEST);
    }

    // own login.
    public void handleLogin(View view) {
        // send to signin page
        Intent intent = new Intent(this, AppSigninActivity.class);
        startActivity(intent);

    }

    // own signup.
    public void handleSignUp(View view) {
        // send to signin page
        Intent intent = new Intent(this, AppSignupActivity.class);
        startActivity(intent);

    }

    public void handleGoogleSignIn(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        // todo switch to registerActivityForResult
        // todo code from  tutorial https://medium.com/geekculture/how-to-integrate-firebase-authentication-for-google-sign-in-functionality-e955d7e549bf
        // todo finish tutorial
        startActivityForResult(signInIntent, 1);
    }



}
