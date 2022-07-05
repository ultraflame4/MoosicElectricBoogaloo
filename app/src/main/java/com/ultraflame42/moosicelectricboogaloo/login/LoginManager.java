package com.ultraflame42.moosicelectricboogaloo.login;

import com.ultraflame42.moosicelectricboogaloo.tools.DefaultEventManager;

public class LoginManager {
    private static LoginStatus status = LoginStatus.NOT_LOGGED_IN;

    /**
     *  Event fired when status changes to logged in or guest
     */
    public static final DefaultEventManager LoggedInStatusEvent = new DefaultEventManager();
    public static final DefaultEventManager OnExitAppHome = new DefaultEventManager();

    public static LoginStatus getStatus() {
        return status;
    }

    public static void setStatus(LoginStatus status) {
        if (LoginManager.status == LoginStatus.NOT_LOGGED_IN && status!= LoginStatus.NOT_LOGGED_IN) {
            // Only push event if previous status was not logged in
            LoggedInStatusEvent.pushEvent();
        }
        LoginManager.status = status;
    }

}