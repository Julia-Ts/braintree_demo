package com.yalantis.db.saver;

import com.voltazor.dblib.BaseDBSaver;
import com.yalantis.db.table.BaseTable;

/**
 * Created by Dmytro Dovbnya on 4/16/14.
 */
public abstract class BaseDAOSaver<Table extends BaseTable, T> extends BaseDBSaver<T> {

    protected Table table;

    public BaseDAOSaver(Table table, T object) {
        super(object);
        this.table = table;
    }

}
