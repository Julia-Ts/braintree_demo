package com.yalantis.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.yalantis.interfaces.Manager;
import com.yalantis.util.CachedValue;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public class SharedPrefManager implements Manager {

    private static final String NAME = "sharedPrefs";

    private static final String API_KEY = "api_key";

    private SharedPreferences sp;

    private Set<CachedValue> cachedValues;

    private CachedValue<String> apiKey;

    @Override
    public void init(Context context) {
        sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        CachedValue.initialize(sp);
        cachedValues = new HashSet<>();

        cachedValues.add(apiKey = new CachedValue<>(API_KEY, String.class));
    }

    public void setApiKey(String apiKey) {
        this.apiKey.setValue(apiKey);
    }

    public String getApiKey() {
        return apiKey.getValue();
    }


    @Override
    public void clear() {
        for (CachedValue value : cachedValues) {
            value.delete();
        }
    }

}
