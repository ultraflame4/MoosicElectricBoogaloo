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
import com.ultraflame42.moosicelectricboogaloo.tools.DefaultEventManager;
import com.ultraflame42.moosicelectricboogaloo.tools.EventManager;

public class GoogleAuthHelper {


    private SignInClient oneTapClient;
    private BeginSignInRequest oneTapSignInRequest;

    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;

    private ActivityResultLauncher<IntentSenderRequest> onetapSignInUiResultLauncher;
    private ActivityResultLauncher<Intent> googleSignInUiResultLauncher;

    private FirebaseAuth mAuth;

    public DefaultEventManager OnAuthSuccessEvent = new DefaultEventManager();


    public GoogleAuthHelper(AppCompatActivity activity) {
        mAuth = FirebaseAuth.getInstance();
        oneTapClient = Identity.getSignInClient(activity);

        oneTapSignInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(activity.getString(R.string.web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                ).build();

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(activity.getString(R.string.web_client_id))
                .build();

        googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions);

        onetapSignInUiResultLauncher = activity.registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), (result) -> {
            OnOneTapResult(result.getResultCode(), result.getData());
        });

        googleSignInUiResultLauncher = activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (result) -> {
            OnGoogleSignInResult(result.getResultCode(),result.getData());
        });

    }

    /**
     * Authenticate with firebase using the google id token given by both one tap and google sign in
     * @param googleIdToken
     */
    private void AuthenticateWithFirebase(String googleIdToken) {
        Log.d("GoogleAuthHelper:FirebaseAuth", "Beginning sign in with firebase");
        if (googleIdToken != null) {
            // Got an ID token from Google. Use it to authenticate
            // with Firebase.
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null);
            mAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("GoogleAuthHelper", "signInWithCredential:success");
                            OnAuthSuccessEvent.pushEvent();

                        } else {
                            // If sign in fails, todo display a message to the user.
                            Log.w("GoogleAuthHelper", "signInWithCredential:failure", task.getException());
                        }
                    });
        }
        else{
            Log.w("GoogleAuthHelper:FirebaseAuth", "WARNING - No google id token given");
        }
    }

    /**
     * The old way for signing in with Google. Exists as a fallback for one tap
     */
    private void GoogleFallbackSignIn() {
        Log.d("GoogleAuthHelper:GoogleSignIn", "Lauching Google Sign In Ui");
        googleSignInUiResultLauncher.launch(googleSignInClient.getSignInIntent());
    }


    private void OnGoogleSignInResult(int resultCode, @Nullable Intent data) {
        Task<GoogleSignInAccount> completedTask = GoogleSignIn.getSignedInAccountFromIntent(data);
        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            AuthenticateWithFirebase(account.getIdToken());
        }
        catch (ApiException e) {
            Log.w("GoogleAuthHelper:GoogleSignIn", "Api Error, Google sign in failed. code="+e.getStatusCode(), e);
        }
    }


    public void oneTapSignIn() {
        oneTapClient.beginSignIn(oneTapSignInRequest)
                .addOnSuccessListener(result -> {
                    try {
                        onetapSignInUiResultLauncher.launch(
                                new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build()
                        );

                    } catch (ActivityNotFoundException e) {
                        Log.e("GoogleAuthHelper:OneTap", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                    }
                })
                .addOnFailureListener(e -> {
                    try {
                        throw e;
                    } catch (ApiException apiException) {
                        if (apiException.getStatusCode() == 16) {
                            Log.d("GoogleAuthHelper:OneTap", "No google accounts detected, falling back to google signin");
                            // todo, implement the old google sign in api as fallback.
                            GoogleFallbackSignIn();

                        } else {
                            Log.e("GoogleAuthHelper:OneTap", "Unknown Api Error ", e);
                        }
                    } catch (Exception e1) {
                        Log.e("GoogleAuthHelper:OneTap", "Unknown Error ", e);
                    }

                });
    }

    public void OnOneTapResult(int resultCode, @Nullable Intent data) {
        try {
            SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
            String idToken = googleCredential.getGoogleIdToken();
            AuthenticateWithFirebase(idToken);

        } catch (ApiException e) {
            // ...
        }
    }
}
