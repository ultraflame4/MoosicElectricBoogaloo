package com.ultraflame42.moosicelectricboogaloo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ultraflame42.moosicelectricboogaloo.account.AccountManager;
import com.ultraflame42.moosicelectricboogaloo.account.GoogleAuthHelper;
import com.ultraflame42.moosicelectricboogaloo.account.LoginStatus;
import com.ultraflame42.moosicelectricboogaloo.tools.events.EventListenerGroup;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;
import com.ultraflame42.moosicelectricboogaloo.ui.login.AppSigninActivity;
import com.ultraflame42.moosicelectricboogaloo.ui.login.AppSignupActivity;

public class AppLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    GoogleAuthHelper googleAuthHelper;
    private final EventListenerGroup eGroup = new EventListenerGroup();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("AppLogin: ", "onCreate()");
        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        AccountManager.init();
        // Google one tap init
        googleAuthHelper = new GoogleAuthHelper(this);

        eGroup.subscribe(AccountManager.LoggedInEvent, (data) -> {
            gotoHomeActivity();
        });

        eGroup.subscribe(AccountManager.AppHomeExitEvent, (data) -> {
            startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
    });

        eGroup.subscribe(googleAuthHelper.OnAuthSuccessEvent,(data) -> {
            Log.d("AppLogin", "Successfully logged in with Google");
            AccountManager.setAuthStatus(LoginStatus.LOGGED_IN);
        });

        eGroup.subscribe(googleAuthHelper.OnAuthFailureEvent,(data) -> {
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        });


    }

    private void gotoHomeActivity() {
        Log.d("AppLogin", "Switching to home activity");
        Intent intent = new Intent(this, AppHomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (AccountManager.getAuthStatus() == LoginStatus.GUEST) {
            Log.d("AppLogin", "Already logged in as guest");
            gotoHomeActivity();

        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            AccountManager.setAuthStatus(LoginStatus.LOGGED_IN);
        }
    }

    @Override
    public void onDestroy() {
        Log.d("AppLogin", "Forced exit");
        AccountManager.setAuthStatus(LoginStatus.NOT_LOGGED_IN);
        // Unsubscribe all event listeners
        Log.d("AppLogin", "Unsubscribing all event listeners");
        eGroup.unsubscribeAll();
        super.onDestroy();
    }

    public void handleContinueAsGuest(View view) {
        Log.d("AppLogin", "Continue as guest");
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
        googleAuthHelper.oneTapSignIn();
    }

    private static final int REQ_ONE_TAP = 2;
    private boolean showOneTapUI = true;

}
