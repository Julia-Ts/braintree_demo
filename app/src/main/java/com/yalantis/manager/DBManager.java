package com.yalantis.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.yalantis.db.DBHelper;
import com.yalantis.db.DBTaskExecutor;
import com.voltazor.dblib.BaseDBLoader;
import com.voltazor.dblib.DBError;
import com.yalantis.db.table.UserTable;
import com.yalantis.interfaces.Manager;
import com.yalantis.model.example.User;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public class DBManager implements Manager {

    private static Context context;

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private DBTaskExecutor executor;
    private UserTable userTable;

    @Override
    public void init(Context context) {
        DBManager.context = context;
        dbHelper = new DBHelper(context);
        db = dbHelper.open();
        executor = new DBTaskExecutor();
        initTables();
    }

    private void initTables() {
        userTable = new UserTable(db);
    }

    UserTable getUserTable() {
        return userTable;
    }

    @Override
    public void clear() {
        userTable.clear();
    }

    User getUserById(String id) {
        return userTable.getUserById(id);
    }

    public static abstract class SimpleLoaderCallback<T> implements BaseDBLoader.DBLoaderCallback<T> {

        @Override
        public void onFailure(DBError error) {
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

}
