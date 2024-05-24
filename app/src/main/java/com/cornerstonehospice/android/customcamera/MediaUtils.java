package com.cornerstonehospice.android.customcamera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaUtils {


	public static final String PUBLIC_DIR = "CORNERSTONE";
	public static final String IMAGE_DIR  = "Images";
	public static final String VOICE_DIR = "Voice";
	public static final String VIDEO_DIR = "Video";
	private static final String FILE_NAME_FORMAT_DATE_PREFIX = "yyyyMMdd_HHmmss";
	private static final String TAG = MediaUtils.class.getSimpleName();
	


	public static File createFile(Context ctx, int mediaType ){
		switch (mediaType) {
		case 0:
			return createImageFile(ctx);
 		default:
 			return createImageFile(ctx);
		}
	}


	@SuppressLint("SimpleDateFormat")
	private static File createImageFile( Context context){

		String imageFileName = new SimpleDateFormat(FILE_NAME_FORMAT_DATE_PREFIX).format(new Date());
		String extStore = Environment.getExternalStorageDirectory().toString();
		//String extStore = context.getExternalFilesDir(null).getAbsolutePath();
		File storageDir = new File(extStore, String.format("Cornerstone/%s", IMAGE_DIR));
		if(!storageDir.exists())
			storageDir.mkdirs();
		File image = null;
		try {
			image = File.createTempFile(
					imageFileName,  /* prefix */
					".jpg",         /* suffix */
					storageDir     /* directory */
					);
		} catch (IOException e) {
			Log.e(TAG , "createImageFile() " + e.getMessage());
		}

		return image;
	}
	
}
