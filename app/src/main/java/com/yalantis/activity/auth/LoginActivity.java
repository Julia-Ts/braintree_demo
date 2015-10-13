package com.yalantis.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.yalantis.App;
import com.yalantis.R;
import com.yalantis.activity.BaseActivity;
import com.yalantis.fragment.auth.LoginFragment;
import com.yalantis.interfaces.CallbackListener;
import com.yalantis.interfaces.LoginListener;
import com.yalantis.model.example.AuthDTO;
import com.yalantis.navigation.Navigator;
import com.yalantis.util.Toaster;

import retrofit.Response;

public class LoginActivity extends BaseActivity implements Session.StatusCallback {

    private UiLifecycleHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        uiHelper = new UiLifecycleHelper(this, this);
        replaceFragment(LoginFragment.newInstance(), false, R.id.profile_fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void call(Session session, SessionState sessionState, Exception e) {
        onSessionStateChange(session, sessionState, e);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Navigator.navigateShare(this);
        } else if (state.isClosed()) {
            Toaster.showShort("Logged out facebook");
        }
    }

    /**
     * Method contains snippets for handling login via REST requests
     */
    private void startSignIn() {
        /**
         * Api request with async task
         */
        App.getApiManager().login("Email", "Password", new CallbackListener() {
            @Override
            public void onSuccess(Response<AuthDTO> response) {
                // TODO: handle received AuthDTO in response.body();
            }

            @Override
            public void onFailure(Throwable error) {
                // TODO: handle error if needed. Usually this handling will be in ApiTask;
            }
        });

        /**
         * Api request with RxJava
         */
        App.getApiManager().loginRX("Email", "Password", new LoginListener() {
            @Override
            public void onSuccess(AuthDTO data) {
                // TODO: handle received AuthDTO;
            }

            @Override
            public void onFailure(Throwable throwable) {
                // TODO: handle error if needed;
            }
        });
    }
}
