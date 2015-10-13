package com.yalantis.model.example;

import com.google.gson.annotations.SerializedName;
import com.yalantis.model.dto.BaseDTO;

public class AuthDTO extends BaseDTO {

    @SerializedName("auth_token")
    private String auth_token;
    @SerializedName("user")
    private User user;

    private String password;

    public User getUser() {
        return user;
    }

    public String getToken() {
        return auth_token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
