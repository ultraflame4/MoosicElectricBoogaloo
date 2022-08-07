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
        // Set up the activity
        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        // Initialize AccountManager
        AccountManager.init();
        // Google one tap init
        googleAuthHelper = new GoogleAuthHelper(this);
        // Setup listeners ---------
        // On logged in event , call goToHomeActivity
        eGroup.subscribe(AccountManager.LoggedInEvent, (data) -> {
            gotoHomeActivity();
        });
        // on AppHomeExit , simulate android home button click
        eGroup.subscribe(AccountManager.AppHomeExitEvent, (data) -> {
            startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
        });
        // On Successful google login , call change auth status
        eGroup.subscribe(googleAuthHelper.OnAuthSuccessEvent, (data) -> {
            Log.d("AppLogin", "Successfully logged in with Google");
            AccountManager.setAuthStatus(LoginStatus.LOGGED_IN);
        });
        // On fail google login , show toast
        eGroup.subscribe(googleAuthHelper.OnAuthFailureEvent, (data) -> {
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        });


    }

    private void gotoHomeActivity() {
        // Go to the home activity
        Log.d("AppLogin", "Switching to home activity");
        Intent intent = new Intent(this, AppHomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if alr logged in as guest
        if (AccountManager.getAuthStatus() == LoginStatus.GUEST) {
            Log.d("AppLogin", "Already logged in as guest");
            gotoHomeActivity();

        }
        // Check if alr logged in firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            AccountManager.setAuthStatus(LoginStatus.LOGGED_IN);
        }
    }

    @Override
    public void onDestroy() {
        // Unsubscribe all event listeners
        Log.d("AppLogin", "Unsubscribing all event listeners");
        eGroup.unsubscribeAll();
        super.onDestroy();
    }

    public void handleContinueAsGuest(View view) {
        // Continue as guest by setting auth status
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
        // Start google signin flow.
        googleAuthHelper.oneTapSignIn();
    }

}
