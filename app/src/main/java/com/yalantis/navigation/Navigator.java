/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.yalantis.navigation;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import com.yalantis.R;
import com.yalantis.activity.BaseActivity;
import com.yalantis.activity.PhotoCropActivity;
import com.yalantis.activity.ShareActivity;
import com.yalantis.activity.auth.LoginActivity;
import com.yalantis.model.ShareModel;

import java.io.File;


/**
 * Class used to navigate through the application.
 */
public class Navigator {

    public void Navigator() {
    }

    /**
     * Goes to the user list screen.
     *
     * @param activity A BaseActivity needed to open the destiny activity.
     */
    public static void navigateToUserList(BaseActivity activity) {
        activity.startActivity(new Intent(activity, ShareActivity.class));
        activity.finish();
    }

    /**
     * Goes to the facebook login screen.
     *
     * @param activity A LoginActivity needed to open the destiny activity.
     */
    public static void navigateLogin(BaseActivity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();
    }

    /**
     * Start activity for result for crop image.
     *
     * @param activity A LoginActivity needed to open the destiny activity.
     * @param uri      path for selected image
     */
    public static void navigateLogin(BaseActivity activity, Uri uri) {
        activity.startActivityForResult(new Intent(activity, PhotoCropActivity.class).setData(uri), PhotoCropActivity.PHOTO_CROP_REQUEST);
    }

    /**
     * Start action for selection image from gallery or camera for cropping.
     *
     * @param activity A LoginActivity needed to open the destiny activity.
     */
    public static void navigatePickImage(BaseActivity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.lbl_select_photo)), PhotoCropActivity.PICK_IMAGE_REQUEST);
    }

    /**
     * Start facebook sharing activity.
     *
     * @param activity A LoginActivity needed to open the destiny activity.
     */
    public static void navigateShare(BaseActivity activity) {
        activity.startActivity(new Intent(activity, ShareActivity.class));
        activity.finish();
    }
    /**
     * Start action for facebook publishing image.
     *
     * @param activity A LoginActivity needed to open the destiny activity.
     */
    public static void publishImage(BaseActivity activity, ResolveInfo info, ShareModel data, String filePath) {
        File f = new File(filePath);
        if (f.exists()) {
            Intent targetedShare = new Intent(Intent.ACTION_SEND);
            targetedShare.setType("image/jpeg");
            targetedShare.setPackage(info.activityInfo.packageName);
            targetedShare.putExtra(Intent.EXTRA_TEXT, data.getDescription());
            targetedShare.putExtra(Intent.EXTRA_STREAM, Uri.parse(f.getAbsolutePath()));
            activity.startActivity(targetedShare);
        }
    }
}
