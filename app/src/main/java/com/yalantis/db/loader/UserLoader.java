package com.yalantis.db.loader;

import java.sql.SQLException;

import com.yalantis.App;
import com.yalantis.db.table.UserTable;
import com.yalantis.model.User;

/**
 * Created by Ed on 09.09.2014.
 */
public class UserLoader extends BaseDAOLoader<User, UserTable> {

    private String userName;

    public UserLoader(String userName, DBLoaderCallback<User> callback) {
        super(callback, App.dataManager.getUserTable());
        this.userName = userName;
    }

    @Override
    protected User load() throws SQLException {
        return App.dataManager.getUserById(userName);
    }
}
