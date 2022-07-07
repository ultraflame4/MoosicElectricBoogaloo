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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.tools.DefaultEventManager;

public class GoogleAuthHelper {

    private SignInClient oneTapClient;
    private BeginSignInRequest oneTapSignInRequest;


    private ActivityResultLauncher<IntentSenderRequest> onetapSignInUiResultLauncher;
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


        onetapSignInUiResultLauncher = activity.registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), (result) -> {
            OnOneTapResult(result.getResultCode(), result.getData());
        });

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
            if (idToken != null) {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                mAuth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("GoogleSignInAuth", "signInWithCredential:success");
                                OnAuthSuccessEvent.pushEvent();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("GoogleSignInAuth", "signInWithCredential:failure", task.getException());
                            }
                        });
            }

        } catch (ApiException e) {
            // ...
        }
    }
}
