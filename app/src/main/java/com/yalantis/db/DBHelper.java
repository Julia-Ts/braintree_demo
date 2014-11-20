package com.yalantis.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yalantis.db.table.BaseTable;
import com.yalantis.db.table.UserTable;

import timber.log.Timber;

public class DBHelper {

    private static final String DATABASE_NAME = "data_base.db";

    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    public DBHelper(Context ctx) {

        databaseHelper = new DatabaseHelper(ctx);
    }

    public synchronized SQLiteDatabase open() throws SQLException {
        if (db == null || !db.isOpen()) {
            Timber.d("SQLiteDatabase");
            db = databaseHelper.getWritableDatabase();
        }
        return db;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private Context context;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTable(db);
        }

        private void createTable(SQLiteDatabase db) {
            db.execSQL(UserTable.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < newVersion) {
                db.execSQL(BaseTable.DROP_TABLE + UserTable.TABLE_NAME);
                createTable(db);
            }
        }

    }
}
