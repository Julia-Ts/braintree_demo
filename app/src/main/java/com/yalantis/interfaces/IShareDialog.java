package com.yalantis.interfaces;

import android.content.pm.ResolveInfo;

/**
 * Created by Aleksandr on 27.11.2014.
 */
public interface IShareDialog {

    void onShare(ResolveInfo shareWith);
}
