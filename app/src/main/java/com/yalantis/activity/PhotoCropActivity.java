package com.yalantis.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.edmodo.cropper.CropImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yalantis.R;
import com.yalantis.interfaces.PhotoSavedCallback;
import com.yalantis.util.SavePhotoTask;

import java.io.File;

public class PhotoCropActivity extends BaseActivity implements PhotoSavedCallback {
    private static final String CROPPED = "cropped.jpg";
    private static final int ROTATION_DEGREES = 90;

    public static final int PICK_IMAGE_REQUEST = 234;
    public static final int PHOTO_CROP_REQUEST = 425;
    public static final String PHOTO_PATH = "path";

    private Uri uri;
    private Bitmap bitmap;
    private int angle = 0;

    private View progressBar;
    private CropImageView cropView;
    private boolean cropping = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_crop);
        progressBar = findViewById(R.id.progress);

        uri = getIntent().getData();

        cropView = (CropImageView) findViewById(R.id.photo);

        loadPhoto(uri);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo_crop, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.apply:
                applyCrop();
                return true;
            case R.id.rotate:
                rotate();
                return true;
            case R.id.cancel:
                cancel();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void rotate() {
        angle += ROTATION_DEGREES;
        angle %= 360;
        cropView.rotateImage(ROTATION_DEGREES);
    }

    private void showPhoto(Bitmap bitmap) {
        this.bitmap = bitmap;
        if (bitmap != null) {
            cropView.setImageBitmap(bitmap);
            cropView.post(new Runnable() {
                @Override
                public void run() {
                    cropView.setGuidelines(1);
                    cropView.setAspectRatio(10, 10);
                    cropView.setFixedAspectRatio(true);
                }
            });
        }
    }

    private void applyCrop() {
        if (cropping) {
            return;
        }
        cropping = true;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        String path = new File(getCacheDir(), CROPPED).getPath();
        bitmap = cropView.getCroppedImage();
        SavePhotoTask task = new SavePhotoTask.Builder()
                .setAngle(angle).setSize(width, height)
                .setRect(cropView.getActualCropRect())
                .setContext(this).setOutPath(path)
                .setCallback(this)
                .setUri(uri)
                .build();
        task.execute();
    }

    protected void loadPhoto(Uri uri) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Picasso.with(this).load(uri)
                .config(Bitmap.Config.RGB_565)
                .into(loadingTarget);
    }

    private Target loadingTarget = new Target() {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            progressBar.setVisibility(View.GONE);
            showPhoto(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            progressBar.setVisibility(View.GONE);
            showPhoto(null);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            progressBar.setVisibility(View.VISIBLE);
        }

    };

    @Override
    public void photoSaved(String path) {
        cropping = false;
        setResult(RESULT_OK, new Intent().putExtra(PHOTO_PATH, path));
        finish();
    }

    private void cancel() {
        finish();
    }

}
