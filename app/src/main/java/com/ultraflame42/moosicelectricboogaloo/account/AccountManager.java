package com.ultraflame42.moosicelectricboogaloo.account;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.ultraflame42.moosicelectricboogaloo.tools.events.DefaultEvent;
import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;

/**
 * Utility class to facilitate the signing in and signing out of the user's account
 * Also have other uses related to user account management
 */
public class AccountManager {
    // The status. Whether the user is logged in or not, or in guest mode
    private static LoginStatus authStatus = LoginStatus.NOT_LOGGED_IN;
    // The firebase object to interact with firebase authentication
    private static FirebaseAuth firebaseAuth;

    /**
     * Event fired when status changes to logged in or guest
     */
    public static final DefaultEvent LoggedInEvent = new DefaultEvent();
    /**
     * Event fired when status changes to logged out
     */
    public static final DefaultEvent LoggedOutEvent = new DefaultEvent();
    /**
     * Event fired the home activity is exited
     */
    public static final DefaultEvent AppHomeExitEvent = new DefaultEvent();

    /**
     * Initializes the account manager
     */
    public static void init() {
        // Get firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Returns the current login status
     * @return
     */
    public static LoginStatus getAuthStatus() {
        return authStatus;
    }

    /**
     * Change the current logged in status. Note. this will fire teh various events
     * @param authStatus
     */
    public static void setAuthStatus(LoginStatus authStatus) {
        // Check if previous status is different than new status, if yes, do nothing
        if (authStatus == AccountManager.authStatus) {
            Log.w("AccountManager", "Attempted to set auth status to same value. Ignoring...");
            return;
        }
        // use switch case to match condtions
        switch (authStatus) {
            // if new status is not logged in, log out
            case NOT_LOGGED_IN:
                // debug
                Log.i("AccountManager", "Logging out ...");
                // sign out from fire base if previous status was not guest

                // push out logged out event
                LoggedOutEvent.pushEvent(null);
                break;

            // if new status is logged in, log in
            case LOGGED_IN:
                Log.i("AccountManager", "Logged in with mode    " + authStatus);
                // no need to do any code for log in as the status should be set after being logged in
                // push logged in event
                LoggedInEvent.pushEvent(null);
                break;

            // if new status is guest,
            case GUEST:
                Log.i("AccountManager", "Logged in with mode    " + authStatus);
                // push logged in event
                LoggedInEvent.pushEvent(null);
                break;
        }

        // set current/prev status to new status
        AccountManager.authStatus = authStatus;
    }


    /**
     * When sign in with email and password fails
     *
     * Has string parameter to show error message to user
     */
    public static CustomEvents<String> OnAuthFailureEvent = new CustomEvents<>();

    public static void SignOut() {
        // Check if user is logged in as guest.
        if (AccountManager.authStatus != LoginStatus.GUEST) {
            // if not, they are logged in with firebase. log out from firebase
            firebaseAuth.signOut();
        }
        setAuthStatus(LoginStatus.NOT_LOGGED_IN);
    }

    /**
     * Initiates sign in with firebase using email and password
     *
     * @param email The user's email
     * @param password The user's password
     */
    public static void SignIn(String email, String password) {
        // initiate firebase login with email and password
        firebaseAuth.signInWithEmailAndPassword(email, password)
                // add listener to listen for sign in success or failure
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("AccountManager", "signInWithEmail:success");
                        setAuthStatus(LoginStatus.LOGGED_IN);
                    } else {
                        // If sign in fails, display a message to the user.
                        Exception e = task.getException();
                        Log.w("AccountManager", "Warning: signInWithEmail:failure", e);
                        // check what kind of error it is
                        try {
                            // rethrow and catch the various possible exceptions
                            throw e;
                        } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthInvalidUserException e1) {
                            // Invalid email/password
                            OnAuthFailureEvent.pushEvent("Invalid email or password");
                        } catch (Exception e2) {
                            // unknown failure
                            OnAuthFailureEvent.pushEvent("Sign in failed! Check Logs");
                            Log.e("AccountManager", "signInWithEmail:unknownFailure", e);
                        }

                        // set status to not logged in
                        setAuthStatus(LoginStatus.NOT_LOGGED_IN);
                    }
                });
    }

    public static void DeleteAccount() {
        // Check if user is logged in (with firebase)
        if (authStatus == LoginStatus.LOGGED_IN) {
            // Get the firebase user object. delete. Also add a listener to listen for success or failure
            firebaseAuth.getCurrentUser().delete().addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    // if successful, change status, and push event and log stuff
                    Log.d("AccountManager", "User account deleted.");
                    setAuthStatus(LoginStatus.NOT_LOGGED_IN);
                } else {
                    // if not successful, log error
                    Log.w("AccountManager", "User account delete failed.", task.getException());
                }
            });

        }
        else{
            // User is not logged in with firebase, OR in guest mode. in which both cases, do nothing
            Log.w("AccountManager", "Attempted to delete account when not logged in or in guest mode");
        }
    }

}