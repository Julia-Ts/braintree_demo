package com.yalantis.model;

import io.realm.Realm;
import io.realm.RealmMigration;
import io.realm.internal.Table;

public class Migration implements RealmMigration {

    public static final long CURRENT_VERSION = 0;

    @Override
    public long execute(Realm realm, long version) {

        return version;
    }

    private long getIndexForProperty(Table table, String name) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (table.getColumnName(i).equals(name)) {
                return i;
            }
        }
        return -1;
    }

}
