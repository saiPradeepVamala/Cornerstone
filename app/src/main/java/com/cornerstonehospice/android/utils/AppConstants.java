/**
 *
 */
package com.cornerstonehospice.android.utils;

/**
 * @author Shashi
 */
public class AppConstants {

    public static final String TOKEN_ID_KEY = "access_token";

    //SharedPref keys
    public static final String TOKEN_KEY = "token";

    //Static TOKEN which we will pass to server
    public static final String STATIC_TOKEN = "c2579c5bfba7f47ff37e0747ed778308db2fde96d8957d5f06ee8d749c8ececb";

    //Shared Pref Keys
    public static final String KEY_PHONE_NO = "contact_phone_no";
    public static final String KEY_EMAIL = "contact_email";
    public static final String KEY_SHORTCUT_CREATED = "KEY_SHORTCUT_CREATED";

    //Static details of Cornerstone
    public static final String CORNERSTONE_CONSTANT_PHONE_NUMBER = "18667426655";
    public static final String CORNERSTONE_CONSTANT_EMAIL = "info@cshospice.org";

    public static final String PREFS_FILE_NAME = "ConrverStonePref";

    //Default/Generic strings
    public static final String DEFAULT_STRING = "";

    public static final String CRITERIA_DATA = "CRITERIA_DATA";

    public static final String IMAGE_PATH_KEY = "path";

    public static final String CAMERA_IMAGE_BYTES_KEY = "CAMERA_IMAGE_BYTES";
    public static final String PREVIEW_IMG_BYTES_KEY = "PREVIEW_IMAGE_BYTES";

    public static final String DOCTOR_NAME = "DOCTOR_NAME";
    public static final String DOCTOR_PHONE_NUMBER = "DOCTOR_PHONE_NUMBER";
    public static final String DOCTOR_RELATION_WITH_PATIENT = "DOCTOR_RELATION_WITH_PATIENT";
    public static final String PATIENT_NAME = "PATIENT_NAME";
    public static final String PATIENT_PHONE_NUMBER = "PATIENT_PHONE_NUMBER";
    public static final String PATIENT_DIAGNOSIS = "PATIENT_DIAGNOSIS";
    public static final String OPENED_DRAWER = "OPENED_DRAWER";
    public static final String FB_LINK = "https://www.facebook.com/CornerstoneHospice?fref=ts";
    // public static final String CORNERSTONE_EMAIL_ID = "deepak@wenable.com"; //Demo

    public static final String CORNERSTONE_EMAIL_ID = "sravan.boga@wenable.com"; //testing
  //  public static final String CORNERSTONE_EMAIL_ID = "admissions@cshospice.org"; //production
    // public static final String CORNERSTONE_EMAIL_ID = "venkateshprasad.s@wenable.com"; //testing
    //  public static final String CORNERSTONE_EMAIL_ID = "shashikanth.kulala@wenable.com";

    //Adding this to navigate to MainDashBoardActivity from MainActivity;

    public static final int navigateToReferralFragment = 0;
    public static final int navigateToCriteriaFragment = 1;
    public static final int navigateToToolsFragment = 2;
    public static final int navigateToAboutusFragment = 3;


}
