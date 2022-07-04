package com.ultraflame42.moosicelectricboogaloo.login;

public class LoginManager {
    private static LoginStatus status = LoginStatus.NOT_LOGGED_IN;

    public static LoginStatus getStatus() {
        return status;
    }

    public static void setStatus(LoginStatus status) {
        LoginManager.status = status;
    }

    //todo add event handler
}