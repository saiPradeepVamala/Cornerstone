package com.we.common.utils;

/**
 * @author Shashi
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnectionUtil {

	private static final String TAG = NetworkConnectionUtil.class.getName();
	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;
	public static boolean LOOPED = false;


	public static boolean isConnectedToNetwork(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}


	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		} 
		return TYPE_NOT_CONNECTED;
	}
	public static String getConnectivityStatusString(Context context) {
        int conn = NetworkConnectionUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkConnectionUtil.TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == NetworkConnectionUtil.TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == NetworkConnectionUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }
}
