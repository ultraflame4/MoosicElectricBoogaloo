package com.ultraflame42.moosicelectricboogaloo.account;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.ultraflame42.moosicelectricboogaloo.tools.events.DefaultEvent;
import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;

public class AccountManager {
    private static LoginStatus authStatus = LoginStatus.NOT_LOGGED_IN;

    private static FirebaseAuth firebaseAuth;

    /**
     *  Event fired when status changes to logged in or guest
     */
    public static final DefaultEvent LoggedInEvent = new DefaultEvent();
    public static final DefaultEvent LoggedOutEvent = new DefaultEvent();

    public static final DefaultEvent AppHomeExitEvent = new DefaultEvent();

    public static void init() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static LoginStatus getAuthStatus() {
        return authStatus;
    }

    public static void setAuthStatus(LoginStatus authStatus) {
        if (authStatus == AccountManager.authStatus) {
            Log.w("AccountManager", "Attempted to set auth status to same value. Ignoring...");
            return;
        }

        switch (authStatus) {

            case NOT_LOGGED_IN:
                Log.i("AccountManager", "Logging out ...");
                firebaseAuth.signOut();
                LoggedOutEvent.pushEvent(null);
                break;
            case LOGGED_IN:
                Log.i("AccountManager", "Logged in with mode    " + authStatus);
                LoggedInEvent.pushEvent(null);
                break;
            case GUEST:
                Log.i("AccountManager", "Logged in with mode    " + authStatus);
                LoggedInEvent.pushEvent(null);
                break;
        }


        AccountManager.authStatus = authStatus;
    }


    /**
     * When sign in with email and password fails
     */
    public static CustomEvents<String> OnAuthFailureEvent = new CustomEvents<>();

    public static void SignIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("AccountManager", "signInWithEmail:success");
                        setAuthStatus(LoginStatus.LOGGED_IN);
                    } else {
                        // If sign in fails, display a message to the user.
                        Exception e = task.getException();
                        Log.w("AccountManager", "Warning: signInWithEmail:failure",e);
                        try{
                            throw e;
                        }
                        catch (FirebaseAuthInvalidCredentialsException e1) {
                            OnAuthFailureEvent.pushEvent("Invalid email or password");
                        }
                        catch (FirebaseAuthInvalidUserException e1) {
                            OnAuthFailureEvent.pushEvent("Invalid email or password");
                        }

                        catch (Exception e2) {
                            OnAuthFailureEvent.pushEvent("Sign in failed! Check Logs");
                            Log.e("AccountManager", "signInWithEmail:unkownFailure", e);
                        }

                        setAuthStatus(LoginStatus.NOT_LOGGED_IN);
                    }
                });
    }

}