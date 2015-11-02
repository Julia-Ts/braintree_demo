package com.yalantis;

public class Constant {

    public class Realm {
        public static final String STORAGE_MAIN = "MainStorage.realm";
    }

    /**
     * General rule is not to use {@link de.greenrobot.event.EventBus#post(Object)} with
     * objects containing data. But only use one of {@link com.yalantis.Constant.Event} options
     * if other methods (e.g. using interface or intent) is hard/impossible to implement.
     *
     * Using single enum prohibits sending unsupported event objects, makes it easier to read and debug
     * code.
     */
    public enum Event {
        Example
    }

}
