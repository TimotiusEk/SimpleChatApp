package com.example.timotiuseka.chatapplication;

import java.util.Date;

/**
 * Created by TimotiusEk on 1/16/2017.
 */

public class SignedInUser {

    private String userEmail;
    private long userLastLogInTime;

    public SignedInUser(String userEmail) {
        this.userEmail = userEmail;
        this.userLastLogInTime = new Date().getTime();
    }

    public SignedInUser(){

    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public long getUserLastLogInTime() {
        return userLastLogInTime;
    }

    public void setUserLastLogInTime(long userLastLogInTime) {
        this.userLastLogInTime = userLastLogInTime;
    }


}
