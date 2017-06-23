package com.yalantis.api;

import android.content.Context;
import android.widget.Toast;

import com.yalantis.App;
import com.yalantis.R;
import com.yalantis.api.body.ErrorBody;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import retrofit2.HttpException;

/**
 * Created by voltazor on 20/06/16.
 */
public class LogOutWhenSessionExpired implements Function<Observable<? extends Throwable>, Observable<?>> {

    private Context mContext;

    public LogOutWhenSessionExpired(Context context) {
        this.mContext = context;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) {
        return observable.observeOn(AndroidSchedulers.mainThread()).flatMap(new Function<Throwable, ObservableSource<?>>() {

            @Override
            public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {
                if (throwable instanceof HttpException) {
                    ErrorBody errorBody = ErrorBody.parseError(((HttpException) throwable).response());
                    if (errorBody != null) {
                        final int code = errorBody.getCode();
                        if (code == ErrorBody.INVALID_TOKEN) {
                            return Observable.empty().observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                                @Override
                                public void run() throws Exception {
                                    Toast.makeText(mContext, R.string.your_session_expired, Toast.LENGTH_SHORT).show();
                                    App.logOut(mContext);
                                }
                            });
                        }
                    }
                }
                // If cannot be handled - pass the original error through.
                return Observable.error(throwable);
            }

        });
    }

}
