package com.yalantis.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.yalantis.fragment.dialog.ProgressDialogFragment;
import com.yalantis.fragment.dialog.ShareDialogFragment;
import com.yalantis.interfaces.IShareDialog;
import com.yalantis.model.ShareModel;
import com.yalantis.model.ShareModelFacebook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ed Baev 28.11.2014
 * @author Alexander Zaitsev
 *
 * Call static methods open...Dialog to open share dialogs
 */
public class SharingUtils {

    private static final String FACEBOOK = "facebook";
    public static final String GOOGLE_PLUS = "com.google.android.apps.plus";
    private static final String TEMP_FILE_NAME = "share_screenshot.jpg";
    private static ShareTask shareTask;
    private static SaveTask saveTask;

    /**
     * Opens Facebook dialog in application if it is installed or in the web if the app is absent
     *
     * @param activity
     * @param uiHelper
     * @param data
     */
    public static void openFacebookDialog(final Activity activity, UiLifecycleHelper uiHelper, final ShareModelFacebook data) {
        if (FacebookDialog.canPresentShareDialog(activity, FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(activity)
                    .setName(data.getName())
                    .setCaption(data.getCaption())
                    .setDescription(data.getDescription())
                            // This can be link to your site.
                            // Attention! You must register a domen in developers.facebook.com before using it!
                    .setLink(data.getLink())
                    .setPicture(data.getPicture())
                    .build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        } else {
            Session.OpenRequest openRequest = new Session.OpenRequest(activity);
            openRequest.setCallback(new Session.StatusCallback() {
                @Override
                public void call(Session session, SessionState state, Exception exception) {
                    if (session.isOpened()) {
                        openFacebookWebDialog(activity, data);
                    }
                }
            });
            Session.getActiveSession().openForRead(openRequest);
        }
    }

    private static void openFacebookWebDialog(Activity activity, ShareModelFacebook data) {
        Bundle postParams = new Bundle();
        postParams.putString(ShareModelFacebook.FB_NAME, data.getName());
        postParams.putString(ShareModelFacebook.FB_CAPTION, data.getCaption());
        postParams.putString(ShareModelFacebook.FB_DESCRIPTION, data.getDescription());
        // This can be link to your site.
        // Attention! You must register a domen in developers.facebook.com before using it!
        postParams.putString(ShareModelFacebook.FB_LINK, data.getLink());
        postParams.putString(ShareModelFacebook.FB_PICTURE, data.getPicture());
        WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(
                activity, Session.getActiveSession(), postParams))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values, FacebookException error) {
                        // define onComplete actions here
                    }

                }).build();
        feedDialog.show();
    }

    /**
     *
     * @param activity
     * @param file jpeg image
     * @param shareModel
     */
    public static void openShareDialog(final FragmentActivity activity, final File file, final ShareModel shareModel) {
        try {
            Session.openActiveSessionFromCache(activity);
            ShareDialogFragment dialog = ShareDialogFragment.newInstance();
            dialog.setShareDialog(new IShareDialog() {
                @Override
                public void onShare(ResolveInfo shareWith) {
                    if (shareWith.activityInfo.packageName.contains(FACEBOOK)) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                        publishFb(activity, bitmap, shareModel);
                    } else {
                        publish(activity, shareWith, shareModel, file.getAbsolutePath());
                    }
                }
            });
            dialog.show(activity.getSupportFragmentManager());
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    public static void openShareDialog(final FragmentActivity activity, final Bitmap bitmap, final ShareModel shareModel) {
        try {
            Session.openActiveSessionFromCache(activity);
            ShareDialogFragment dialog = ShareDialogFragment.newInstance();
            dialog.setShareDialog(new IShareDialog() {
                @Override
                public void onShare(ResolveInfo shareWith) {
                    if (shareWith.activityInfo.packageName.contains(FACEBOOK)) {
                        publishFb(activity, bitmap, shareModel);
                    } else {
                        if (saveTask != null) {
                            saveTask.cancel(true);
                        }
                        saveTask = new SaveTask(activity, bitmap, shareWith, shareModel);
                        saveTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            });
            dialog.show(activity.getSupportFragmentManager());
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    public static void openShareDialog(final FragmentActivity activity, final View view, final ShareModel shareModel) {
        try {
            Session.openActiveSessionFromCache(activity);
            ShareDialogFragment dialog = ShareDialogFragment.newInstance();
            dialog.setShareDialog(new IShareDialog() {
                @Override
                public void onShare(ResolveInfo shareWith) {
                    if (shareTask != null) {
                        shareTask.cancel(true);
                    }
                    shareTask = new ShareTask(activity, view, shareWith, shareModel);
                    shareTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });
            dialog.show(activity.getSupportFragmentManager());
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    private static void publishFb(final FragmentActivity activity, Bitmap shareFile, ShareModel data) {
        String actionType = data.getActionType();
        String graphObjectName = data.getObjectName();
        UiLifecycleHelper uiHelper = new UiLifecycleHelper(activity, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState sessionState, Exception e) {
                Logger.d(session.toString());
            }
        });
        OpenGraphObject object = OpenGraphObject.Factory.createForPost(graphObjectName);
        object.setDescription(data.getDescription());
        object.setTitle(activity.getString(data.getObjectTitleId()));
        object.setUrl(data.getUrl());
        OpenGraphAction action = GraphObject.Factory.create(OpenGraphAction.class);
        action.setProperty(graphObjectName, object);
        action.setType(actionType);

        List<Bitmap> images = new ArrayList<>();
        if (shareFile != null) {
            images.add(shareFile);
        }
        FacebookDialog shareDialog = new FacebookDialog.OpenGraphActionDialogBuilder(activity, action, graphObjectName)
                .setImageAttachmentsForObject(graphObjectName, images)
                .build();

        uiHelper.trackPendingDialogCall(shareDialog.present());
    }

    private static void publish(final FragmentActivity activity, ResolveInfo info, ShareModel data) {
        publish(activity, info, data, activity.getExternalCacheDir() + File.pathSeparator + TEMP_FILE_NAME);
    }

    private static void publish(final FragmentActivity activity, ResolveInfo info, ShareModel data, String filePath) {
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

    private static void saveFile(Context context, Bitmap bm) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(context.getExternalCacheDir(), TEMP_FILE_NAME);
        try {
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            Logger.e(e);
        }
    }

    private static class ShareTask extends AsyncTask<Void, Void, Bitmap> implements DialogInterface.OnCancelListener {

        final FragmentActivity activity;
        final View view;
        final ResolveInfo info;
        final ShareModel shareModel;
        ProgressDialogFragment progressDialog;

        public ShareTask(FragmentActivity activity, View view, ResolveInfo info, ShareModel shareModel) {
            this.activity = activity;
            this.view = view;
            this.info = info;
            this.shareModel = shareModel;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialogFragment();
            progressDialog.setOnCancelListener(this);
            progressDialog.show(activity.getSupportFragmentManager(), progressDialog.getFragmentTag());
        }

        @Override
        protected Bitmap doInBackground(final Void... meh) {
            try {
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
            } catch (Exception e) {
                Logger.e(e);
                return null;
            }
            Bitmap bm = view.getDrawingCache();
            if (shareModel.isNeedCropImage()) {
                //the bitmap should be square
                Bitmap bitmap = Bitmap.createBitmap(bm.getWidth(), bm.getWidth(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawRGB(255, 255, 255);
                int cy = (bm.getWidth() - bm.getHeight()) >> 1;
                canvas.drawBitmap(bm, 0, cy, null);
                bm = bitmap;
            }

            // save screenshot to share it in future
            if (!info.activityInfo.packageName.contains(FACEBOOK)) {
                saveFile(activity, bm);
            }

            return bm;
        }

        @Override
        protected void onPostExecute(final Bitmap shareFile) {
            try {
                progressDialog.dismiss();
                progressDialog = null;

                if (info.activityInfo.packageName.contains(FACEBOOK)) {
                    publishFb(activity, shareFile, shareModel);
                } else {
                    publish(activity, info, shareModel);
                }
            } catch (Exception e) {
                Logger.e(e);
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            cancel(true);
        }
    }

    private static class SaveTask extends AsyncTask<Void, Void, Void> implements DialogInterface.OnCancelListener {

        final FragmentActivity activity;
        final Bitmap bitmap;
        final ResolveInfo info;
        final ShareModel shareModel;
        ProgressDialogFragment progressDialog;

        public SaveTask(FragmentActivity activity, Bitmap bitmap, ResolveInfo info, ShareModel shareModel) {
            this.activity = activity;
            this.bitmap = bitmap;
            this.info = info;
            this.shareModel = shareModel;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialogFragment();
            progressDialog.setOnCancelListener(this);
            progressDialog.show(activity.getSupportFragmentManager(), progressDialog.getFragmentTag());
        }

        @Override
        protected Void doInBackground(final Void... meh) {
            saveFile(activity, bitmap);
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            try {
                progressDialog.dismiss();
                progressDialog = null;

                publish(activity, info, shareModel);
            } catch (Exception e) {
                Logger.e(e);
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            cancel(true);
        }
    }
}
