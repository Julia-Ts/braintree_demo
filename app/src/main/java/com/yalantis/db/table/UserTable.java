package com.yalantis.db.table;

import timber.log.Timber;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yalantis.model.example.User;

/**
 * Created by Ed
 */
public class UserTable extends BaseTable {

    public static final String TAG = UserTable.class.getSimpleName();
    public static final String TABLE_NAME = "user_table";
    // columns
    public static final String USER_ID = "user_id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String EMAIL = "user_email";
    public static final String GENDER = "gender";
    public static final String LOGO = "logo";
    public static final String CREATE_TABLE = new StringBuilder()
            .append(CREATE_TABLE_STR).append(TABLE_NAME).append(" (")
            .append(USER_ID).append(STRING).append(PRIMARY_KEY_COMMA)
            .append(FIRST_NAME).append(STRING_COMMA)
            .append(LAST_NAME).append(STRING_COMMA)
            .append(EMAIL).append(STRING_COMMA)
            .append(GENDER).append(STRING_COMMA)
            .append(LOGO).append(STRING)
            .append(");").toString();

    public UserTable(SQLiteDatabase database) {
        super(database);
    }

    public void insert(User user) {
        cv = new ContentValues();
//        cv.put(USER_ID, user.getUserId());
//        cv.put(FIRST_NAME, user.getFirstName());
//        cv.put(LAST_NAME, user.getLastName());
//        cv.put(GENDER, user.getGender());
        cv.put(EMAIL, user.getEmail());
//        cv.put(LOGO, user.getLogo());
        database.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public User getUserById(String id) {
        User user = null;
        Cursor cursor = database.query(TABLE_NAME, null, USER_ID + " = '" + id + "'", null, null, null, null,
                null);

        try {
            if (cursor.moveToFirst()) {
                user = extractUser(cursor);
            }
        } catch (Exception e) {
            Timber.e(e, "Error extracting home from cursor");
        } finally {
            cursor.close();
        }
        return user;
    }

    private User extractUser(Cursor cursor) {
        User user = new User();

//        user.setGender(cursor.getString(cursor.getColumnIndex(GENDER)));
//        user.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
//        user.setFirstName(cursor.getString(cursor.getColumnIndex(FIRST_NAME)));
//        user.setLastName(cursor.getString(cursor.getColumnIndex(LAST_NAME)));
//        user.setLogo(cursor.getString(cursor.getColumnIndex(LOGO)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
        return user;
    }

    public void update(User user, String id) {
        cv = new ContentValues();
//        updateCVIfNotNull(cv, FIRST_NAME, user.getFirstName());
//        updateCVIfNotNull(cv, LAST_NAME, user.getLastName());
//        updateCVIfNotNull(cv, GENDER, user.getGender());
        updateCVIfNotNull(cv, EMAIL, user.getEmail());
//        updateCVIfNotNull(cv, LOGO, user.getLogo());

        database.update(TABLE_NAME, cv, USER_ID + " = '" + id + "'", null);
    }

    @Override
    public void clear() {
        database.delete(TABLE_NAME, null, null);
    }

}
