package com.yalantis.db.saver;

import java.sql.SQLException;

import com.yalantis.App;
import com.yalantis.db.table.UserTable;
import com.yalantis.model.example.User;

/**
 * Created by Ed
 */
public class UserDAOSaver extends BaseDAOSaver<UserTable, User> {

    public UserDAOSaver(User user) {
        super(App.dataManager.getUserTable(), user);
    }

    @Override
    protected boolean save(User user) throws SQLException {
        table.insert(user);
        return true;
    }

}
