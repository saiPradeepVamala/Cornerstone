package com.we.common.utils;


/**
 * @author Shashi
 * 
 */
public class AndroidUtils {
	public static boolean testMode = false;

	public static final String getAndroidRelease() {
		if (testMode) return "TESTMODE RELEASE";
		return android.os.Build.VERSION.RELEASE;
	}

	public static final String getAndroidBuildId() {
		if (testMode) return "TESTMODE BUILD ID";
		return android.os.Build.ID;
	}

	public static final String getAndroidManufacturerName() {
		if (testMode) return "TESTMODE MANUF NAME";
		return android.os.Build.MANUFACTURER;
	}

	public static final String getAndroidSdkVersion() {
		if (testMode) return "TESTMODE SDK VERSION";
		return String.valueOf(android.os.Build.VERSION.SDK_INT);
	}
}
