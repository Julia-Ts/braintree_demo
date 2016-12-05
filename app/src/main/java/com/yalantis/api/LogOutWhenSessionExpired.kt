package com.yalantis.api

import android.content.Context
import android.widget.Toast
import com.yalantis.R
import com.yalantis.api.body.ErrorBody
import com.yalantis.data.source.repository.ReposRepository
import com.yalantis.manager.SharedPrefManager
import retrofit2.adapter.rxjava.HttpException
import rx.Completable
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1

/**
 * Created by voltazor on 20/06/16.
 */
class LogOutWhenSessionExpired(private val mContext: Context) : Func1<Observable<out Throwable>, Observable<Any>> {

    override fun call(observable: Observable<out Throwable>): Observable<Any> {
        return observable.observeOn(AndroidSchedulers.mainThread()).flatMap(Func1<Throwable, Observable<*>> { throwable ->
            if (throwable is HttpException) {
                val errorBody = ErrorBody.parseError(throwable.response())
                if (errorBody != null) {
                    val code = errorBody.code
                    if (code == ErrorBody.INVALID_TOKEN) {
                        return@Func1 Completable.complete().observeOn(AndroidSchedulers.mainThread()).doOnCompleted {
                            Toast.makeText(mContext, R.string.your_session_expired, Toast.LENGTH_SHORT).show()
                            logOut()
                        }.toObservable<Any>()
                    }
                }
            }
            // If cannot be handled - pass the original error through.
            Observable.error<Any>(throwable)
        })
    }

    fun logOut() {
        SharedPrefManager.getInstance(mContext).clear()
        ReposRepository(mContext).clear()
    }
}
