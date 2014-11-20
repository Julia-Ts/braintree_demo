package com.yalantis.db;

import timber.log.Timber;

/**
 * Created by: Dmitriy Dovbnya
 * Date: 04.09.13 19:18
 */
public class DBTaskExecutor extends com.voltazor.dblib.DBTaskExecutor {

    private static final String TAG = DBTaskExecutor.class.getSimpleName();

    @Override
    public void finished(long time) {
        Timber.d("DBTask time: " + time + "ms");
        super.finished(time);
    }
}
