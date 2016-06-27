package com.yalantis.ui.activity;

import android.support.annotation.NonNull;
import android.view.View;

import com.yalantis.R;
import com.yalantis.contract.SomeContract;
import com.yalantis.presenter.SomePresenter;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by voltazor on 27/06/16.
 */
public class SomeActivity extends BaseMvpActivity<SomeContract.Presenter> implements SomeContract.View {

    @Bind(R.id.some_button)
    View mSomeButton;

    @NonNull
    @Override
    protected SomeContract.Presenter getPresenterInstance() {
        return new SomePresenter();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_some;
    }

    @Override
    public void showSomeResult() {
        mSomeButton.setEnabled(true);
        showMessage("Some action was performed");
    }

    @OnClick(R.id.some_button)
    void onSomeButtonClick() {
        mSomeButton.setEnabled(false);
        mPresenter.doSomeAction();
    }

}
