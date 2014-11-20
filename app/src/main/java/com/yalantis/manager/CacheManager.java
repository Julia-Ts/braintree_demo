package com.yalantis.manager;

import android.content.Context;

import com.yalantis.model.User;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public class CacheManager implements Manager {

    private User user;

    @Override
    public void init(Context context) {

    }

    void setUser(User user) {
        this.user = user;
    }

    User getCurrentUser() {
        return user;
    }

    @Override
    public void clear() {
        user = null;
    }

}
