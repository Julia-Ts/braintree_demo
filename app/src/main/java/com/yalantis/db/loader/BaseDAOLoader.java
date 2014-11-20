package com.yalantis.db.loader;

import com.voltazor.dblib.BaseDBLoader;
import com.yalantis.db.table.BaseTable;

/**
 * Created by Ed
 */
public abstract class BaseDAOLoader<T, Table extends BaseTable> extends BaseDBLoader<T> {

    protected Table table;

    public BaseDAOLoader(DBLoaderCallback<T> callback, Table table) {
        super(callback);
        this.table = table;
    }
}
