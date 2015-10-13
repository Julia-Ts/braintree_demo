package com.yalantis.interfaces;

import android.content.Context;

/**
 * Base interface for all managers
 * Created by Dmitriy Dovbnya on 25.09.2014.
 */
public interface Manager {

    public void init(Context context);

    public void clear();

}
