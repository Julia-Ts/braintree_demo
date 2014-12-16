package com.yalantis.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.Session;
import com.yalantis.R;
import com.yalantis.navigation.Navigator;

public class MainActivity extends BaseActivity {

    static Intent getCallingIntent(Context context, Bundle params) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            Navigator.navigateToUserList(this);
        } else {
            Navigator.navigateLogin(this);
        }
    }
}
