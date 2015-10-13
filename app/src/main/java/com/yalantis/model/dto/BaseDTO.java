package com.yalantis.model.dto;

/**
 * Base data transfer object
 * All sub classes should provide getters and model for data objects
 * Created by Ed
 */
public abstract class BaseDTO {

    private int status;
    private String method;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
