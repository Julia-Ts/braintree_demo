package com.yalantis.db.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public abstract class BaseTable {

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    protected static final String PRIMARY_KEY_COMMA = " PRIMARY KEY, ";
    protected static final String FOREIGN_KEY = " FOREIGN KEY ";
    protected static final String REFERENCES = " REFERENCES ";
    protected static final String NOT_NULL_COMMA = " TEXT NOT NULL, ";
    protected static final String STRING = " TEXT ";
    protected static final String STRING_COMMA = " TEXT, ";
    protected static final String INTEGER_COMMA = " INTEGER, ";
    protected static final String INTEGER = " INTEGER ";
    protected static final String DOUBLE = " DOUBLE ";
    protected static final String DOUBLE_COMMA = " DOUBLE, ";
    protected static final String CREATE_TABLE_STR = "CREATE TABLE ";
    protected static final String COMMA = ", ";
    protected static final String BOOLEAN = " BOOLEAN ";
    protected static final String BOOLEAN_COMMA = " BOOLEAN, ";
    protected ContentValues cv;

    protected SQLiteDatabase database;

    public BaseTable(SQLiteDatabase database) {
        this.database = database;
    }

    public abstract void clear();

    protected void updateCVIfNotNull(ContentValues contentValues, String name, Object value) {
        if (value instanceof String) {
            if (!TextUtils.isEmpty((String) value)) {
                contentValues.put(name, (String) value);
            }
        }
        if (value instanceof Double) {
            if ((double) value != -1) {
                contentValues.put(name, (double) value);
            }
        }
        if (value instanceof Integer) {
            if ((int) value != -1) {
                contentValues.put(name, (int) value);
            }
        }
    }
}
