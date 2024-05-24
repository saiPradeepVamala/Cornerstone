package com.cornerstonehospice.android.customcamera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cornerstonehospice.R;
import com.cornerstonehospice.android.utils.AppConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends Activity {
	
	private static final String TAG = CameraActivity.class.getSimpleName(); 
    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private static Context context;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "onCreate() :: ");
        context = this;
        setContentView(R.layout.activity_camera);
        mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mCameraPreview);

        final ImageButton captureButton = (ImageButton) findViewById(R.id.button_capture);
        captureButton.setEnabled(true);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Log.d(TAG, "onClick() :: Capture Button : ");
            	captureButton.setEnabled(false);
                mCamera.takePicture(null, null, mPicture);
            }
        });
    }

    /**
     * Helper method to access the camera returns null if it cannot get the
     * camera or does not exist
     * 
     * @return
     */
    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            // cannot get camera or does not exist
        	Log.e(TAG, "getCameraInstance() :: Exception : " + e);
        }
        return camera;
    }

    PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
        	try {
        		File pictureFile = createImageFile();
        		if (pictureFile == null) {
        			return;
        		}
        		FileOutputStream fos = new FileOutputStream(pictureFile);
        		fos.write(data);
        		fos.close();
        		Intent intent = new Intent();
        		intent.putExtra(AppConstants.IMAGE_PATH_KEY, rotateImage(pictureFile.getAbsolutePath()));
        		setResult(RESULT_OK, intent);
        		finish();
        	} catch (Exception e) {
        		Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
        	}
        }
    };
    
    private String rotateImage(String filePath) {
    	String filePathFinal = "";
    	String photopath = filePath;
    	Bitmap bmp = BitmapFactory.decodeFile(photopath);

    	Matrix matrix = new Matrix();
    	matrix.postRotate(90);
    	bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

    	FileOutputStream fOut;
    	try {
    		File photoFile =  MediaUtils.createFile(context, MediaType.PICTURE);
			String imagePath = photoFile.getAbsolutePath();

		    File file = new File(imagePath);
    		fOut = new FileOutputStream(file);
    		bmp.compress(Bitmap.CompressFormat.JPEG, 45, fOut);
    		fOut.flush();
    		fOut.close();
    		filePathFinal = file.getAbsolutePath();
    		
    		File fileToDelete = new File(filePath);
    		fileToDelete.delete();
    	} catch (Exception ex) {
    		Log.e(TAG, "rotateImage() :: Exception : " + ex);
    	}
    	return filePathFinal;
    }
    
    private static File createImageFile() throws IOException {
		File file = null;
		try {
		    // Create an image file name
			File photoFile =  MediaUtils.createFile(context, MediaType.PICTURE);
			String imagePath = photoFile.getAbsolutePath();

		    file = new File(imagePath);
		    
		    // Save a file: path for use with ACTION_VIEW intents
		    //mCurrentImagePath = file.getAbsolutePath();
		} catch(Exception ex) { 
			Log.e(TAG, "createImageFile() :: Exception : " + ex);
			return null;
		}
	    return file;
	}
}