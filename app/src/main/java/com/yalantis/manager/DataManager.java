package com.yalantis.manager;

import android.content.Context;

import com.yalantis.db.table.UserTable;
import com.yalantis.interfaces.Manager;
import com.yalantis.model.example.AuthDTO;
import com.yalantis.model.example.User;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public class DataManager implements Manager {

    private static final CacheManager cacheManager = new CacheManager();
    private static final DBManager dbManager = new DBManager();

    @Override
    public void init(Context context) {
        cacheManager.init(context);
        dbManager.init(context);
    }

    @Override
    public void clear() {
        cacheManager.clear();
        dbManager.clear();
    }

    public User getUserById(String id) {
        return dbManager.getUserById(id);

    }

    public UserTable getUserTable() {
        return dbManager.getUserTable();
    }

    public void saveProfileFromServerData(AuthDTO getProfileDTO) {
        // save into database and cache
        User user = new User();
        cacheManager.setUser(user);
        dbManager.getUserTable().update(getProfileDTO.getUser(),
                String.valueOf(getProfileDTO.getUser().getUserDataId()));
    }
}
