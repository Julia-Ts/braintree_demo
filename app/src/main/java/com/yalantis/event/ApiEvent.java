package com.yalantis.event;

/**
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public class ApiEvent<T> implements BaseEvent {

    public final T data;

    public ApiEvent(T data) {
        this.data = data;
    }

}
