package com.example.pruebanavdrawer.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example2 {

    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("auth")
    @Expose
    private Auth auth;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }
}
