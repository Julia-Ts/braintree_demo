package com.yalantis.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.yalantis.R;
import com.yalantis.model.ShareModel;
import com.yalantis.model.ShareModelFacebook;
import com.yalantis.util.Logger;
import com.yalantis.util.SharingUtils;
import com.yalantis.util.Toaster;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Alexander Zaitsev on 02.12.2014.
 */
public class ShareActivity extends BaseActivity implements Session.StatusCallback {
    private ImageView imageView;
    private UiLifecycleHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        imageView = (ImageView) findViewById(R.id.image_view);
        uiHelper = new UiLifecycleHelper(this, this);
        uiHelper.onCreate(savedInstanceState);

        // share on facebook directly

        findViewById(R.id.btn_share_fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareModelFacebook model = new ShareModelFacebook();
                model.setCaption("template caption");
                model.setName("template");
                model.setDescription("template descr");
                model.setPicture("http://www.jpl.nasa.gov/spaceimages/images/mediumsize/PIA17011_ip.jpg");
                SharingUtils.openFacebookDialog(ShareActivity.this, uiHelper, model);
            }
        });

        // show share dialog

        final ShareModel model = new ShareModel();
        model.setDescription("template descr");
        model.setNeedCropImage(false);
        model.setObjectTitleId(R.string.app_name);
        model.setShareTypeCode(0);
        model.setActionType("comyalantis:join");
        model.setObjectName("party");
        model.setUrl("http://google.com");

        findViewById(R.id.btn_share_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharingUtils.openShareDialog(ShareActivity.this, (File) getBitmapFile(findViewById(R.id.btn_share_fb))[1], model);
            }
        });
        findViewById(R.id.btn_share_bitmap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharingUtils.openShareDialog(ShareActivity.this, (Bitmap) getBitmapFile(findViewById(R.id.btn_share_fb))[0], model);
            }
        });
        findViewById(R.id.btn_share_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharingUtils.openShareDialog(ShareActivity.this, v, model);
            }
        });
        findViewById(R.id.btn_crop_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

    }

    private Object[] getBitmapFile(View view) {
        try {
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
        } catch (Exception e) {
            Logger.e(e);
            return null;
        }
        Bitmap bm = view.getDrawingCache();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(getExternalCacheDir(), "share_screenshot.jpg");
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

        return new Object[]{bm, f};
    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Toaster.showShort(String.format("Error: %s", error.toString()));
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Toaster.showShort("Success!");
            }
        });
        if (requestCode == PhotoCropActivity.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == PhotoCropActivity.PHOTO_CROP_REQUEST && resultCode == RESULT_OK) {
            handleCrop(data.getStringExtra(PhotoCropActivity.PHOTO_PATH));
        }
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.lbl_select_photo)), PhotoCropActivity.PICK_IMAGE_REQUEST);
    }

    private void beginCrop(Uri uri) {
        startActivityForResult(new Intent(this, PhotoCropActivity.class).setData(uri), PhotoCropActivity.PHOTO_CROP_REQUEST);
    }

    private void handleCrop(String path) {
        Bitmap bmp = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(bmp);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void call(Session session, SessionState sessionState, Exception e) {
        onSessionStateChange(session, sessionState, e);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            // logged in
        } else if (state.isClosed()) {
            // logged out
        }
        if (exception != null) {
            Logger.e(exception);
        }
    }
}
