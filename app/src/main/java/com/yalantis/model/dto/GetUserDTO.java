package com.yalantis.model.dto;

import com.google.gson.annotations.SerializedName;
import com.yalantis.model.User;

/**
 * Created by Ed on 08.09.2014.
 */
public class GetUserDTO extends BaseDTO {


    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
