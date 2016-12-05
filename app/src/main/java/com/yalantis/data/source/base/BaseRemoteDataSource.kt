package com.yalantis.data.source.base

import android.content.Context
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.yalantis.BuildConfig
import com.yalantis.api.ApiSettings
import com.yalantis.api.services.GithubService
import io.realm.RealmObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by irinagalata on 12/1/16.
 */

abstract class BaseRemoteDataSource : BaseDataSource {

    protected lateinit var mGithubService: GithubService
    private lateinit var mRetrofit: Retrofit

    override fun init(context: Context) {
        initRetrofit()
        initServices()
    }

    private fun initServices() {
        mGithubService = mRetrofit.create<GithubService>(GithubService::class.java)
    }

    private fun initRetrofit() {
        val level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                    //                        .header(ApiSettings.HEADER_AUTH_TOKEN, getAuthToken())
                    .method(original.method(), original.body())
                    .build()
            chain.proceed(request)
        }.addInterceptor(HttpLoggingInterceptor().setLevel(level)).build()

        mRetrofit = Retrofit.Builder()
                .baseUrl(ApiSettings.SERVER)
                .addConverterFactory(createGsonConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build()
    }

    private fun createGsonConverter(): GsonConverterFactory {
        val builder = GsonBuilder()
        builder.serializeNulls()
        builder.setExclusionStrategies(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return f.declaringClass == RealmObject::class.java
            }

            override fun shouldSkipClass(clazz: Class<*>): Boolean {
                return false
            }
        })
//        try {
//            builder.registerTypeAdapter(Class.forName("io.realm.RepositoryRealmProxy"), RepositorySerializer())
//        } catch (e: ClassNotFoundException) {
//            throw RuntimeException(e.message)
//        }

        return GsonConverterFactory.create(builder.create())
    }

    override fun clear() {

    }

}
