package com.cornerstonehospice.android.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cornerstonehospice.R;
import com.cornerstonehospice.android.utils.AppConstants;
import com.we.common.utils.WELogger;

import java.io.ByteArrayOutputStream;

/**
 * @author Shashi
 */
public class TakePictureActivity extends AppCompatActivity {

    private ImageView mPreviewImageView;
    private static final int TAKE_PICTURE_REQUEST = 100;
    private static final String TAG = TakePictureActivity.class.getName();
    private Bitmap mCameraBitmap;
    private int lastTakenImageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        init();
        setIdsToViews();
        startImageCapture();
    }

    private void init() {
        lastTakenImageId = getLastImageId();
    }

    private void setIdsToViews() {
        mPreviewImageView = (ImageView) findViewById(R.id.camera_preview_iv);
    }

    private void startImageCapture() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                TAKE_PICTURE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        WELogger.infoLog(TAG, "onActivityResult() :: In onActivityResult");
        hideKeyPad();
        if (requestCode == TAKE_PICTURE_REQUEST) {
            WELogger.infoLog(TAG, "onActivityResult)( :: TAKE_PICTURE_REQUEST recived");
            if (resultCode == RESULT_OK) {
                WELogger.infoLog(TAG, "onActivityResult :: Request code is OK Lets recycle the Bitmap");
                // Recycle the previous bitmap.
                if (mCameraBitmap != null) {
                    mCameraBitmap.recycle();
                    mCameraBitmap = null;
                }
                Bundle extras = data.getExtras();
                if (extras != null) {           // extras not null
                    if (extras.containsKey("data")) {
                        WELogger.infoLog(TAG, "onActivityResult :: Intent extra contains DATA as a key");
                        mCameraBitmap = (Bitmap) extras.get("data");
                        try {
                            WELogger.infoLog(TAG, "onActivityResult :: Captured image is set to ImageView ");
                            mPreviewImageView.setImageBitmap(mCameraBitmap);
                            byte[] imageBytes   =   getBytesForImage();
                            WELogger.infoLog(TAG, "onActivityResult :: ImageBytes size :  " + imageBytes.length);
                            data.putExtra(AppConstants.CAMERA_IMAGE_BYTES_KEY, getBytesForImage());
                            setResult(Activity.RESULT_OK, data);
                            TakePictureActivity.this.finish();
                            deleteImage();
                        } catch (Exception ex) {
                            Log.e(TAG, Log.getStackTraceString(ex));
                            WELogger.infoLog(TAG, "onActivityResult" + ex);
                        }
                    } else {
                        WELogger.infoLog(TAG, "onActivityResult :: Data is null from extras/ from camera");
                    }

                } else {
                    WELogger.infoLog(TAG, "onActivityResult :: Extras are null from camera.");
                }

            } else if (resultCode == RESULT_CANCELED) {         // Preview discard
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        }
    }

    public void deleteImage() {
        WELogger.infoLog(TAG, "deleteImage :: deleteing images from DB : Image ID : " + lastTakenImageId);
        final String[] imageColumns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.SIZE, MediaStore.Images.Media._ID};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        final String imageWhere = MediaStore.Images.Media._ID + ">?";
        final String[] imageArguments = {Integer.toString(lastTakenImageId)};
        Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, imageWhere, imageArguments, imageOrderBy);
        assert imageCursor != null;
        if (imageCursor.getCount() >= 1) {
            while (imageCursor.moveToNext()) {
                @SuppressLint("Range") int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
                ContentResolver cr = getContentResolver();
                cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media._ID + "=?", new String[]{Long.toString(id)});
                break;
            }
        }
        imageCursor.close();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }


    private int getLastImageId() {
        final String[] imageColumns = {MediaStore.Images.Media._ID};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        final String imageWhere = null;
        final String[] imageArguments = null;
        Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, imageWhere, imageArguments, imageOrderBy);
        assert imageCursor != null;
        if (imageCursor.moveToFirst()) {
            @SuppressLint("Range") int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
            imageCursor.close();
            WELogger.infoLog(TAG, "getLastImageID() :: Last image ID : "+id);
            return id;
        } else {
            return 0;
        }
//        return 0; // as commented out ID
    }

    private byte[] getBytesForImage() {
        WELogger.infoLog(TAG, "In getBytesForImage");
        byte[] imageBytes = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mCameraBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imageBytes = stream.toByteArray();
        try {
            String storageState = Environment.getExternalStorageState();
            if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                WELogger.infoLog(TAG, "getBytesForImage :: MEDIA MOUNTED ");
                View b = findViewById(R.id.camera_preview_iv);
                b.setDrawingCacheEnabled(true);
                b.buildDrawingCache(true);
                b.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                b.layout(0, 0, b.getMeasuredWidth(), b.getMeasuredHeight());
                mCameraBitmap = b.getDrawingCache(true);
                imageBytes = convertBitmapToByteArray(mCameraBitmap);
            }
        } catch (Exception ex) {
            WELogger.infoLog(TAG, "Exception while opening  image ");
        }
        return imageBytes;

    }


    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        WELogger.infoLog(TAG, "convertBitmapToByteArray :: Converting bit map to array to store in DB ");
        if (bitmap == null) {
            WELogger.infoLog(TAG, "convertBitmapToByteArray :: Bitmap is null we cannot convert to bytes lets return back with NULL");
            return null;
        } else {
            byte[] byteArray = null;
            try {
                WELogger.infoLog(TAG, "convertBitmapToByteArray :: Byte conversion Process is started.");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byteArray = byteArrayOutputStream.toByteArray();
                WELogger.infoLog(TAG, "convertBitmapToByteArray :: Converted byte array length " + byteArray.length);
            } catch (Exception exception) {
                WELogger.errorLog(TAG, "convertBitmapToByteArray :: Exception while perfroming conversion : " + exception);
                exception.printStackTrace();
            }
            return byteArray;
        }
    }


    protected void hideKeyPad() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
