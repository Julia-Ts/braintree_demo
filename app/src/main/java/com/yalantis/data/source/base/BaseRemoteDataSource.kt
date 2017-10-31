package com.yalantis.data.source.base

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.yalantis.BuildConfig
import com.yalantis.api.ApiSettings
import com.yalantis.api.deserializer.StringDeserializer
import com.yalantis.api.services.GithubService
import io.reactivex.schedulers.Schedulers
import io.realm.RealmObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by irinagalata on 12/1/16.
 */

abstract class BaseRemoteDataSource : BaseDataSource {

    protected lateinit var githubService: GithubService
    private lateinit var retrofit: Retrofit

    override fun init() {
        initRetrofit()
        initServices()
    }

    private fun initServices() {
        githubService = retrofit.create<GithubService>(GithubService::class.java)
    }

    private fun initRetrofit() {
        val level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                    //.header(ApiSettings.HEADER_AUTH_TOKEN, getAuthToken())
                    .method(original.method(), original.body())
                    .build()
            chain.proceed(request)
        }.addInterceptor(HttpLoggingInterceptor().setLevel(level)).build()

        retrofit = Retrofit.Builder()
                .baseUrl(ApiSettings.SERVER)
                .addConverterFactory(createGsonConverter())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
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
        builder.registerTypeAdapter(String::class.java, StringDeserializer())
        return GsonConverterFactory.create(builder.create())
    }

    override fun clear() {

    }

}
