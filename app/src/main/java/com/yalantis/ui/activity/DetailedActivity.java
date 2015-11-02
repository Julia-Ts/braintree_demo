package com.yalantis.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.yalantis.BuildConfig;

/**
 * Created by Oleksii Shliama.
 */
public class DetailedActivity extends BaseActivity {

    private static final String BUNDLE_REPO_ID = BuildConfig.APPLICATION_ID + ".REPO_ID";

    public static Intent getCallingIntent(@NonNull Context context, long repoId) {
        Intent intent = new Intent(context, DetailedActivity.class);
        intent.putExtra(BUNDLE_REPO_ID, repoId);
        return intent;
    }

}
