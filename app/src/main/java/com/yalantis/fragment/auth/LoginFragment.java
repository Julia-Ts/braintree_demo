package com.yalantis.fragment.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.widget.LoginButton;
import com.yalantis.R;
import com.yalantis.activity.BaseActivity;
import com.yalantis.fragment.BaseFragment;

/**
 * Created by Alexander Zaitsev on 02.12.2014.
 */
public class LoginFragment extends BaseFragment {

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.authButton);
        loginButton.setPublishPermissions("publish_actions");
        return view;
    }

}