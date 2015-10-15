package com.yalantis;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.view.View;

import com.yalantis.manager.ApiManager;
import com.yalantis.manager.DataManager;
import com.yalantis.manager.SharedPrefManager;
import com.yalantis.util.Logger;

import de.greenrobot.event.EventBus;

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
        initManagers();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext() {
        return context;
    }

    // TODO: Move this code in new App
    private static void initManagers() {
        apiManager.init(context);
        spManager.init(context);
        apiManager.init(context);
        dataManager.init(context);
    }

    public void clear() {
        apiManager.clear();
        dataManager.clear();
        spManager.clear();
    }

    // TODO: Move all code below - in new App

    /**
     * Check if Internet enabled
     *
     * @return true if enabled and false in other case
     */
    public static boolean isInternetConnectionAvailable() {
        return isNetworkEnabled(ConnectivityManager.TYPE_MOBILE) || isNetworkEnabled(ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check specific network status, if module is available
     *
     * @param networkType type of network (TYPE_WIFI or TYPE_MOBILE)
     * @return true if connected or false in other case
     */
    private static boolean isNetworkEnabled(int networkType) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(networkType);
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * Shows snackbar with given text. Snackbar is used to show any info to user (like toast, but cooler).
     *
     * @param viewToAttach    view to which parent Snackbar will be attached
     * @param messageText     text to be shown to user
     * @param buttonText      text for clickable action
     * @param buttonListener  listener of clickable action
     * @param buttonTextColor clickable action text color
     * @param showLength      length of displaying
     */
    public static void showSnackbar(
            View viewToAttach, String messageText, String buttonText,
            View.OnClickListener buttonListener, int buttonTextColor, int showLength) {
        if (viewToAttach == null) {
            return;
        }

        Snackbar snackbarBuilder = Snackbar.make(
                viewToAttach,
                messageText,
                showLength == 1 ? Snackbar.LENGTH_LONG : showLength);

        if (buttonText != null && buttonListener != null) {
            snackbarBuilder.setAction(buttonText, buttonListener);
            snackbarBuilder.setActionTextColor(buttonTextColor);
        }

        snackbarBuilder.show();
    }
}
