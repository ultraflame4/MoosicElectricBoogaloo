package com.ultraflame42.moosicelectricboogaloo.account;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.ultraflame42.moosicelectricboogaloo.tools.DefaultEventManager;

public class AccountManager {
    private static LoginStatus authStatus = LoginStatus.NOT_LOGGED_IN;

    private static FirebaseAuth firebaseAuth;

    /**
     *  Event fired when status changes to logged in or guest
     */
    public static final DefaultEventManager LoggedInEvent = new DefaultEventManager();
    public static final DefaultEventManager LoggedOutEvent = new DefaultEventManager();
    public static final DefaultEventManager AppHomeExitEvent = new DefaultEventManager();

    public static void init() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static LoginStatus getAuthStatus() {
        return authStatus;
    }

    public static void setAuthStatus(LoginStatus authStatus) {
        if (AccountManager.authStatus == LoginStatus.NOT_LOGGED_IN && authStatus != LoginStatus.NOT_LOGGED_IN) {
            Log.d("AccountManager", "Logged in with mode    " + authStatus.toString());
            // Only push event if previous status was not logged in
            LoggedInEvent.pushEvent();
        }

        AccountManager.authStatus = authStatus;
    }



    public static void SignIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("AccountManager", "signInWithEmail:success");
                        setAuthStatus(LoginStatus.LOGGED_IN);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("AccountManager", "signInWithEmail:failure", task.getException());
                        setAuthStatus(LoginStatus.NOT_LOGGED_IN);
                    }
                });
    }
}