
package com.we.common.utils;


import android.util.Log;

import com.cornerstonehospice.android.manager.WEFrameworkDataInjector;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Date;


public class WELogger {
    public static final String LOG_TAG = "WE Logger";
    public static final boolean testMode = WEFrameworkDataInjector.getInstance().isInTestMode();

    private static Logger mLogger = LogManager.getLogger(WELogger.class);
//            .getInstance();


    public static boolean inTestMode() {
        return WEFrameworkDataInjector.getInstance().isInTestMode();
    }


    public static void infoLog(String tag, String msg) {
        //if(!AppTestPrefs.BUILD_STATE.isProduction()){
        if (testMode) {
            if (inTestMode()) {
                logForTestMode(tag, msg);
                return;
            }
            if (msg != null)
                mLogger.debug(tag, String.format("%s - %s - %s", new Date().toString(), tag, msg));
            Log.d(tag, msg);
        }
    }

    private static void logForTestMode(String tag, String msg, Exception... optional) {
        //if(!AppTestPrefs.BUILD_STATE.isProduction()){  // Changed Build type to Prod we need to change this with the
        if (testMode) {
            Exception e = optional == null || optional.length == 0 ? null : optional[0];
            if (e == null) {
                System.out.println(String.format("%s - %s - %s", new Date().toString(), tag, msg));
            } else {
                System.out.println(String.format("%s - %s - %s  Error message = %s", new Date().toString(), tag, msg, e.getCause() == null ? e.getMessage() : e.getCause().getMessage()));
            }
        }
    }

    public static void errorLog(String tag, String msg, Exception... optional) {
        //if(!AppTestPrefs.BUILD_STATE.isProduction()){
        if (testMode) {
            if (inTestMode()) {
                logForTestMode(tag, msg, optional);
                return;
            }
            Exception e = optional == null || optional.length == 0 ? null : optional[0];
            if (e == null) {
                Log.e(tag, String.format("%s  Error message = %s  Exception e is null.  ", msg, ""));
            } else {
                mLogger.debug(tag, String.format("%s - %s - %s", new Date().toString(), tag, msg));
                Log.e(tag, String.format("%s  Error message = %s", msg, e.getCause() == null ? e.getMessage() : e.getCause().getMessage()));
            }
        }
    }
}