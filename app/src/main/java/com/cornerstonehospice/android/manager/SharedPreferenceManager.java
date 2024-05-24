/**
 * 
 */
package com.cornerstonehospice.android.manager;

import java.util.concurrent.atomic.AtomicReference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.cornerstonehospice.android.utils.AppConstants;
import com.we.common.utils.WELogger;

/**
 * Singleton SharedPreference class which is responsible to hadle all the SharedPreference operations 
 * @author shashi
 *
 */
public class SharedPreferenceManager {

	private static final AtomicReference<SharedPreferenceManager> sInstance 	= 		new AtomicReference<SharedPreferenceManager>();
	private static final String TAG 							 				= 		SharedPreferenceManager.class.getName();
	private static Context mContext;

	public static SharedPreferenceManager getInstance() {
		WELogger.infoLog(TAG, "getInstance() :: getting instance for SharedPreferenceManager");
		if (sInstance.get() == null){
			sInstance.set(new SharedPreferenceManager());
		}
		return sInstance.get();
	}

	private SharedPreferenceManager() {
	}

	public static void intialize(Context applicationContext) {
		mContext = applicationContext;
	}

	public void saveString(String key, String value) {
		SharedPreferences pref = mContext.getSharedPreferences(AppConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void saveInt(String key, int value) {
		SharedPreferences pref = mContext.getSharedPreferences(AppConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void saveBool(String key, boolean value) {
		SharedPreferences pref = mContext.getSharedPreferences(AppConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void saveLong(String key, long value) {
		SharedPreferences pref = mContext.getSharedPreferences(AppConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putLong(key, value);
		editor.commit();
	}


	public String getString(String key, String defValue){
		SharedPreferences pref = mContext.getSharedPreferences(AppConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		return pref.getString(key, defValue);
	}

	public int getInt(String key, int defValue){
		SharedPreferences pref = mContext.getSharedPreferences(AppConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		return pref.getInt(key, defValue);
	}

	public boolean getBool(String key, boolean defValue){
		SharedPreferences pref = mContext.getSharedPreferences(AppConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		return pref.getBoolean(key, defValue);
	}

	public long getLong(String key, long defValue) {
		SharedPreferences pref = mContext.getSharedPreferences(AppConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		return pref.getLong(key,defValue);
	}

	public void clearAllPref(){
		SharedPreferences settings = mContext.getSharedPreferences(AppConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		settings.edit().clear().commit();
	}

	public String getUserId() {
		// TODO Auto-generated method stub
		return null;
	}
}
