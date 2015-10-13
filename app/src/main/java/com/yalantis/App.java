package com.yalantis;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.yalantis.manager.ApiManager;
import com.yalantis.manager.DataManager;
import com.yalantis.manager.SharedPrefManager;
import com.yalantis.util.Logger;

import de.greenrobot.event.EventBus;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public class App extends Application {

    public static final EventBus eventBus = EventBus.getDefault();
    public static final ApiManager apiManager = new ApiManager();
    public static final DataManager dataManager = new DataManager();
    public static final SharedPrefManager spManager = new SharedPrefManager();

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
        new Logger();
        context = this;
        spManager.init(this);
        apiManager.init(this);
        dataManager.init(this);
    }

    public static Context getContext() {
        return context;
    }

    public void clear() {
        apiManager.clear();
        dataManager.clear();
        spManager.clear();
    }

}
