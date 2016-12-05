package com.yalantis.data;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

/**
 * Created by irinagalata on 12/1/16.
 */
public class Migration implements RealmMigration {

    public static final long CURRENT_VERSION = 0;

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

    }

}