package com.yalantis.model.dto;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {

    private static final String MESSAGE = "message";

    @SerializedName(MESSAGE)
    private String errorMessage;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
