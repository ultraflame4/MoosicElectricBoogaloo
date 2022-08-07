package com.ultraflame42.moosicelectricboogaloo.account;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.tools.events.DefaultEvent;
import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;

/**
 * This class helps with firebase google authentication for logging in with google.
 *
 * For google authentication, there is two methods:
 *
 * 1. Google One-Tap Sign-In
 *
 * 2. Google Sign-In
 *
 * Google recommends one tap to sign in. However it does not work if the user is not signed in on the device
 *
 * So we use the second method to sign in as a fallback
 */
public class GoogleAuthHelper {

    private SignInClient oneTapClient;
    private BeginSignInRequest oneTapSignInRequest;

    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;

    private ActivityResultLauncher<IntentSenderRequest> onetapSignInUiResultLauncher;
    private ActivityResultLauncher<Intent> googleSignInUiResultLauncher;

    private FirebaseAuth mAuth;

    public DefaultEvent OnAuthSuccessEvent = new DefaultEvent();
    public CustomEvents<String> OnAuthFailureEvent = new CustomEvents<>();


    public GoogleAuthHelper(AppCompatActivity activity) {
        // Get firebase auth object instance
        mAuth = FirebaseAuth.getInstance();

        // Configure settings for Google one tap sign in
        oneTapSignInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(activity.getString(R.string.web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                ).build();

        // Configure settings for Google Sign-In
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(activity.getString(R.string.web_client_id))
                .build();

        // get the one tap sign in client
        oneTapClient = Identity.getSignInClient(activity);

        // get the google sign in client
        googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions);

        // create activity intent launcher for google one tap
        onetapSignInUiResultLauncher = activity.registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), (result) -> {
            OnOneTapResult(result.getResultCode(), result.getData());
        });

        // create activity intent launcher for google sign in
        googleSignInUiResultLauncher = activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (result) -> {
            OnGoogleSignInResult(result.getResultCode(),result.getData());
        });

    }

    /**
     * Authenticate with firebase using the google id token returned by both one tap and google sign in
     * @param googleIdToken
     */
    private void AuthenticateWithFirebase(String googleIdToken) {
        Log.d("GoogleAuthHelper:FirebaseAuth", "Beginning sign in with firebase");
        if (googleIdToken != null) {
            // Got an ID token from Google. Use it to authenticate with Firebase.

            // first get the credential using the id token
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null);
            // sign in to firebase with the credential
            mAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("GoogleAuthHelper:OneTap", "signInWithCredential:success");
                            // Push google auth success event
                            OnAuthSuccessEvent.pushEvent(null);

                        } else {
                            // If sign in fails
                            Exception e = task.getException();
                            // log exception
                            Log.w("GoogleAuthHelper", "signInWithCredential:failure", e);
                            // Push google auth failed event
                            OnAuthFailureEvent.pushEvent("Firebase Authentication failed: " + e.getClass() + ":" +e.getMessage());
                        }
                    });
        }
        else{
            Log.w("GoogleAuthHelper:FirebaseAuth", "WARNING - No google id token given");
        }
    }

    /**
     * The old way for signing in with Google. Exists as a fallback for one tap
     *
     * method launches the activity for google sign in
     */
    private void GoogleFallbackSignIn() {
        // Log for debugging
        Log.d("GoogleAuthHelper:GoogleSignIn", "Launching Google Sign In Ui");
        // Launch the Google Sign-In activity
        googleSignInUiResultLauncher.launch(googleSignInClient.getSignInIntent());
    }

    /**
     * Listener for the google sign in
     * @param resultCode
     * @param data
     */
    private void OnGoogleSignInResult(int resultCode, @Nullable Intent data) {
        // get google sign in result
        Task<GoogleSignInAccount> completedTask = GoogleSignIn.getSignedInAccountFromIntent(data);
        try{
            // try to get the account from google sign in result
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Sign in to firebase with the id token
            AuthenticateWithFirebase(account.getIdToken());
        }
        catch (ApiException e) {
            // if failed, log exception
            Log.w("GoogleAuthHelper:GoogleSignIn", "Api Error, Google sign in failed. code="+e.getStatusCode(), e);
            // push failure event
            OnAuthFailureEvent.pushEvent("Google sign in failed, Api Error Code: "+e.getStatusCode());
        }
    }

    /**
     * Starts activity for GoogleOneTapSignIn
     */
    public void oneTapSignIn() {
        // Initiate one tap sign in
        // Add on success and failure listeners
        oneTapClient.beginSignIn(oneTapSignInRequest)
                .addOnSuccessListener(result -> {
                    try {
                        // On success start the activity for google one tap sign in
                        onetapSignInUiResultLauncher.launch(
                                new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build()
                        );

                    } catch (ActivityNotFoundException e) {
                        // On error log exception
                        Log.e("GoogleAuthHelper:OneTap", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        OnAuthFailureEvent.pushEvent("Couldn't start One Tap UI: " + e.getLocalizedMessage());
                    }
                })
                .addOnFailureListener(e -> {
                    try {
                        throw e;
                    } catch (ApiException apiException) {
                        if (apiException.getStatusCode() == 16) {
                            // if status code is 16, there weren't any google accounts. Thats why it failed
                            Log.d("GoogleAuthHelper:OneTap", "No google accounts detected, falling back to google signin");
                            // Fallback to google sign in
                            GoogleFallbackSignIn();

                        } else {
                            // Else, log exception cuz error is unknown
                            Log.e("GoogleAuthHelper:OneTap", "Unknown Api Error ", e);
                            OnAuthFailureEvent.pushEvent("Unknown Api Error " + e.getLocalizedMessage());
                        }
                    } catch (Exception e1) {
                        // Else, log exception cuz error is unknown
                        Log.e("GoogleAuthHelper:OneTap", "Unknown Error ", e);
                        OnAuthFailureEvent.pushEvent("Unknown Error " + e.getLocalizedMessage());
                    }

                });
    }

    // Listener for the one tap sign in
    public void OnOneTapResult(int resultCode, @Nullable Intent data) {
        try {
            // Get google credentials
            SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
            // Get id token from google creds
            String idToken = googleCredential.getGoogleIdToken();
            // Sign in with firebase with google creds
            AuthenticateWithFirebase(idToken);

        } catch (ApiException e) {
            // ...
        }
    }
}
