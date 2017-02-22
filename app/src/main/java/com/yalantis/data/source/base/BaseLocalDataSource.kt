package com.yalantis.data.source.base

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmMigrationNeededException
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by irinagalata on 12/1/16.
 */

abstract class BaseLocalDataSource : BaseDataSource {

    var realm by ThreadLocalRealmDelegate()

    companion object {
        @JvmStatic
        private val THREAD_LOCAL_REALM = ThreadLocal<Realm?>()
        @JvmStatic
        private val REALMS_LIST = ArrayList<Realm?>()
    }

    override fun init() {

    }

    fun clearRealm() {
        val realm = THREAD_LOCAL_REALM.get()
        if (realm != null && !realm.isClosed) {
            realm.close()
            REALMS_LIST.remove(realm)
        }
        THREAD_LOCAL_REALM.set(null)
    }

    override fun clear() {
        REALMS_LIST
                .filter { it?.isClosed?.not() ?: false }
                .forEach { it?.close() }
        THREAD_LOCAL_REALM.remove()
    }

    private class ThreadLocalRealmDelegate : ReadWriteProperty<Any?, Realm> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): Realm {
            var realm = THREAD_LOCAL_REALM.get()
            if (realm == null || realm.isClosed) {
                realm = getRealmInstance()
                THREAD_LOCAL_REALM.set(realm)
            }
            REALMS_LIST.add(realm)
            return realm
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Realm) {

        }

        private fun getRealmInstance(): Realm {
            try {
                return Realm.getDefaultInstance()
            } catch (exception: RealmMigrationNeededException) {
                Realm.deleteRealm(RealmConfiguration.Builder().build())
                return Realm.getDefaultInstance()
            }
        }
    }

}
