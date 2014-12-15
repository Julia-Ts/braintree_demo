package com.yalantis.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.yalantis.interfaces.PhotoSavedCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Konstantin on 12.12.2014.
 */
public class SavePhotoTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = SavePhotoTask.class.getSimpleName();

    private Context context;
    private Uri uri;
    private String outPath;
     private int width;
    private int height;
    private int angle;
    private RectF rect;
    private PhotoSavedCallback callback;

    public SavePhotoTask(Context context, Uri uri, String outPath, int angle, int width, int height, RectF rect, PhotoSavedCallback callback) {
        this.context = context;
        this.uri = uri;
        this.outPath = outPath;
        this.rect = rect;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Bitmap src = null;
        try {
            src = Picasso.with(context).load(uri).get();
        } catch (IOException e) {
            Log.e(TAG, "Image not found: " + uri);
        }
        if (src == null) {
            Log.e(TAG, "Image not found: " + uri);
            return null;
        }

        float koefW = (float) width / (float) src.getWidth();
        float koefH = (float) height / (float) src.getHeight();

        rect.top /= koefH;
        rect.left /= koefW;
        rect.right /= koefW;
        rect.bottom /= koefH;
        width = (int) rect.width();
        height = (int) rect.height();

        Matrix matrix = new Matrix();
        matrix.preRotate(angle);
        Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        bitmap = Bitmap.createBitmap(bitmap, (int) rect.left, (int) rect.top, width, height);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(outPath));

            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            }

        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.getMessage(), e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (callback != null) {
            callback.photoSaved(outPath);
        }
    }

    public static class Builder {
        private PhotoSavedCallback callback;
        private Context context;
        private Uri uri;
        private String outPath;
        private int angle = -1;
        private int width;
        private int height;
        private RectF rect;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setUri(Uri uri) {
            this.uri = uri;
            return this;
        }

        public Builder setOutPath(String outPath) {
            this.outPath = outPath;
            return this;
        }

        public Builder setAngle(int angle) {
            this.angle = angle;
            return this;
        }

        public Builder setSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setRect(RectF rect) {
            this.rect = rect;
            return this;
        }

        public Builder setCallback(PhotoSavedCallback callback) {
            this.callback = callback;
            return this;
        }

        public SavePhotoTask build() {
            return new SavePhotoTask(context, uri, outPath, angle, width, height, rect, callback);
        }
    }

}

