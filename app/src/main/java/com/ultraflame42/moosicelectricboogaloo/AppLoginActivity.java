package com.ultraflame42.moosicelectricboogaloo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ultraflame42.moosicelectricboogaloo.account.AccountManager;
import com.ultraflame42.moosicelectricboogaloo.account.LoginStatus;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;
import com.ultraflame42.moosicelectricboogaloo.ui.login.AppSigninActivity;
import com.ultraflame42.moosicelectricboogaloo.ui.login.AppSignupActivity;

public class AppLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);
        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        AccountManager.init();
        // Google one tap init
        oneTapClient = Identity.getSignInClient(this);

        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                ).build();


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
        if (currentUser != null) {

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
        oneTapClient.beginSignIn(signInRequest)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                            // start the ui intent for google one tap sign in
                            startIntentSenderForResult(
                                    task.getResult().getPendingIntent().getIntentSender(),
                                    REQ_ONE_TAP,
                                    null, 0, 0, 0
                            );
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("HandleGoogleSignIn", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        }
                    } else {

                        Exception e = task.getException();
                        Log.d("HandleGoogleSignIn", "Error " ,e);
                    }
                });

    }

    private static final int REQ_ONE_TAP = 2;
    private boolean showOneTapUI = true;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = googleCredential.getGoogleIdToken();
                    if (idToken != null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with Firebase.
                        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                        mAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("GoogleSignInAuth", "signInWithCredential:success");
                                        AccountManager.setAuthStatus(LoginStatus.LOGGED_IN);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("GoogleSignInAuth", "signInWithCredential:failure", task.getException());
                                    }
                                });
                    }

                } catch (ApiException e) {
                    // ...
                }
                break;
        }
    }


}
