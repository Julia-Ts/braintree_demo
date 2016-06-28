package com.yalantis.api.body;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by voltazor on 16/06/16.
 */
public class ErrorBody {

    //TODO: apply real error codes
    public static final int INVALID_CREDENTIALS = 101;
    public static final int EMAIL_IS_TAKEN = 102;
    public static final int INVALID_TOKEN = 103;

    private static final Gson GSON = new Gson();

    @SerializedName("code")
    private int code;

    @SerializedName("error")
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{code:" + code + ", message:\"" + message + "\"}";
    }

    public static ErrorBody parseError(Response response) {
        ErrorBody error = null;
        if (response != null && response.errorBody() != null) {
            try {
                error = GSON.fromJson(response.errorBody().string(), ErrorBody.class);
            } catch (IOException ignored) {
                return null;
            }
        }
        return error;
    }

}
