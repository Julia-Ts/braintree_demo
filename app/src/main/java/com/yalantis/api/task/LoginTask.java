package com.yalantis.api.task;

import com.yalantis.api.services.TokenLessService;
import com.yalantis.interfaces.CallbackListener;
import com.yalantis.model.example.AuthBody;
import com.yalantis.model.example.AuthDTO;

import retrofit.Response;

/**
 * Example of performing async request with callback
 * Base ApiTask implements Callback for error handling in one place
 */
public class LoginTask extends ApiTask<TokenLessService, AuthDTO> {

    private String mEmail;
    private String mPassword;
    private CallbackListener mListener;

    public LoginTask(TokenLessService api, String email, String password,
                     final CallbackListener listener) {
        super(api);
        mEmail = email;
        mPassword = password;
        mListener = listener;
    }

    @Override
    public void run() {
        AuthBody bundle = new AuthBody();
        bundle.email = mEmail;
        bundle.password = mPassword;
        // Async request. Callback implemented in parent class
        api.login(bundle).enqueue(this);
    }

    @Override
    protected void onSuccess(Response response) {
        mListener.onSuccess(response);
    }

    @Override
    public void onFailure(Throwable t) {
        super.onFailure(t);
        // Message can be delivered to task caller if needed
        // mListener.onFailure(t);
    }
}
