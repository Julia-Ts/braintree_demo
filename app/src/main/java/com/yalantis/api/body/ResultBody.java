package com.yalantis.api.body;

import com.google.gson.annotations.SerializedName;

/**
 * Created by voltazor on 27/06/16.
 */
public class ResultBody<T> {

    @SerializedName("result")
    T result;

    public ResultBody(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

}
