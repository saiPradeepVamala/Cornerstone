package com.we.common.logging;

import java.util.Date;

import com.cornerstonehospice.android.manager.WEFrameworkDataInjector;
import com.we.common.models.AppPropertiesModel;
import com.we.common.utils.WELogger;

import android.util.Log;

/**
 * @author Wenable Saving log information in to the database
 */
public class Logger {
	private static Logger _instance 		= 		null;
	private static boolean logCatLog 		= 		true;					//	By default route the application logs to adb logs

	private int logLevel 					=		LoggerConstants.LogLevel.LOGVERBOSE;		

	private static String LOGGING_TAG 		= 		null;
	private static String	TAG				=		Logger.class.getName();
//	private static org.apache.log4j.Logger logger  = 		null;

	public static synchronized Logger getInstance() {
		if( !WELogger.testMode ){
			if ( _instance == null) {
//				logger									=		org.apache.log4j.Logger.getLogger("WEAF");
				_instance 								= 		new Logger();
				AppPropertiesModel	appProperties		=		WEFrameworkDataInjector.getInstance().getAppProperties();
				int logLevel							=		appProperties.logLevel;
				String logTag							=		appProperties.logFileName;
				LOGGING_TAG								=		logTag;
				_instance.setLogLevel(logLevel);
				Log.d(TAG, "getInstance() :: Setting logLevel :: "+logLevel +"/n Log Tag : "+logTag);
			}else{
				WELogger.infoLog(TAG, "getInstance :: applicaiton is in Test mode we cannot process the android relaated peice of code, so igoring Logging instatse");
			}
		}
		return _instance;
	}

	public void seLogTagName(String logTagName){
	}

	public void setLogLevel(int _logLevel) {
		logLevel = _logLevel;
		try{
			logLevel			=		_logLevel;
		}catch(Exception exception ){
//			logger.error("Exception while setting the loglevel "+exception);
		}
	}

	/**
	 * Get the log level. 0 - FATAL, 1 - ERROR, 2 - WARN, 3 - INFO, 4 - DEBUG, 5 - VERBOSE
	 * @return
	 */
	public int getLogLevel() {
		return logLevel;
	}


	/**
	 * Route the application logs to adb logs, not used as of now.
	 */
	public void setLogCatOn() {
		logCatLog = true;
	}

	/**
	 * Don't route the application logs to adb logs, not used as of now.
	 */
	public void setLogCatOff() {
		logCatLog = false;
	}

	private Logger() {
		if(!WELogger.testMode){		// When the test mode is off only we need to use Logger else don't use
//			logger.debug((new Date()).toString() + " " + LOGGING_TAG + " " +
//					"thread intialized with PID:" + android.os.Process.myPid());
		}
	}

	/**
	 * DEBUG Related Logger
	 * 
//	 * @param Log
//	 *            executed time
//	 * @param Log
	 *            tag name, it indicates the file name
	 * @param message
	 *            , log message
	 **/
	public void d(String tag, String message) {
		if (logCatLog)
//			logger.debug((new Date()).toString() + " " + LOGGING_TAG + " " + tag + " " + message);
		if (logLevel >= LoggerConstants.LogLevel.LOGDEBUG) {
			printMessage(LoggerConstants.DEBUG, tag, message);
		}
	}

	/**
	 * DEBUG Related Logger
	 * 
//	 * @param Log
//	 *            executed time
//	 * @param Log
	 *            tag name, it indicates the file name
	 * @param message
	 *            , log message
	 **/
	public void d(boolean toAndroidLog, String tag, String message) {
//		logger.debug((new Date()).toString() + " " + LOGGING_TAG + " " + tag + " " + message);

	}

	public void d(boolean toAndroidLog, String tag, String message, java.lang.Throwable e) {
//		logger.debug((new Date()).toString() + " " + LOGGING_TAG + " " + tag + " " + message, e);
	}

	/**
	 * ERROR Related Logger
	 * 
//	 * @param Log
//	 *            executed time
//	 * @param Log
	 *            tag name, it indicates the file name
	 * @param message
	 *            , log message
	 **/
	public void e(String tag, String message) {
		if (logCatLog)
//			logger.error((new Date()).toString() + " " + LOGGING_TAG + " " + tag + " " + message);
		if (logLevel >= LoggerConstants.LogLevel.LOGERROR) {
			printMessage(LoggerConstants.ERROR, tag, message);
		}
	}

	/**
	 * The idea is to provide a single "logger" functionality to the application
	 * with different params indicating where to write the logs.. If the
	 * application needs to write to GUI logs, then use this variant. Note that
	 * @param tag
	 * @param message
	 */
	public void e(boolean toAndroidLog, String tag, String message) {
//		logger.error((new Date()).toString() + " " + LOGGING_TAG + " " + tag + " " + message);
	}

	public void e(boolean toAndroidLog, String tag, String message, java.lang.Throwable e) {
//		logger.error((new Date()).toString() + " " + LOGGING_TAG + " " + tag + " " + message, e);
	}

	/**
	 * ERROR Related Logger
	 * 
//	 * @param Log
//	 *            executed time
//	 * @param Log
	 *            tag name, it indicates the file name
	 * @param message
	 *            , log message
	 * @param exception
	 *            , exception - if any
	 **/
	public void e(String tag, String message, java.lang.Throwable exception) {
		if (logCatLog)
//			logger.error((new Date()).toString() + " " + LOGGING_TAG + " " + tag + " " + message,
//					exception);
		if (logLevel >= LoggerConstants.LogLevel.LOGERROR) {
			printMessage(LoggerConstants.ERROR, tag,
					message + ":" + Log.getStackTraceString(exception));
		}
	}

	/**
	 * INFO Related Logger
	 * 
//	 * @param Log
//	 *            executed time
//	 * @param Log
//	 *            tag name, it indicates the file name
	 * @param message
	 *            , log message
	 **/
	public void i(boolean toAndroidLog, String tag, String message) {
//		logger.info((new Date()).toString() + " " + LOGGING_TAG + " " + tag + " " + message);
	}

	public void i(String tag, String message) {
		if (logCatLog)
//			logger.info((new Date()).toString() + " " + LOGGING_TAG + " " + tag + " " + message);

		if (logLevel >= LoggerConstants.LogLevel.LOGINFO) {
			printMessage(LoggerConstants.INFO, tag, message);
		}
	}

	/**
	 * VERB Related Logger
	 * 
//	 * @param Log
//	 *            executed time
//	 * @param Log
	 *            tag name, it indicates the file name
	 * @param message
	 *            , log message
	 **/
	public void v(String tag, String message) {
		if (logCatLog)
//			logger.trace((new Date()).toString() + " " + LOGGING_TAG + " " + tag + " " + message);

		if (logLevel >= LoggerConstants.LogLevel.LOGVERBOSE) {
			printMessage(LoggerConstants.VERB, tag, message);
		}
	}

	public void v(boolean toAndroidLog, String tag, String message) {
//		logger.trace((new Date()).toString() + " " + LOGGING_TAG + " " + tag + " " + message);
	}

	public void v(boolean toAndroidLog, String tag, String message,
			Throwable exception) {
//		logger.trace((new Date()).toString() + " " + LOGGING_TAG + " " + tag + " " + message,
//				exception);
	}

	/**
	 * WARN Related Logger
	 * 
//	 * @param Log
//	 *            executed time
//	 * @param Log
	 *            tag name, it indicates the file name
	 * @param message
	 *            , log message
	 **/
	public void w(String tag, String message) {
		if (logCatLog)
//			logger.warn((new Date()).toString() + " " + LOGGING_TAG + " " + tag + " " + message);
		if (logLevel >= LoggerConstants.LogLevel.LOGWARN) {
			printMessage(LoggerConstants.WARN, tag, message);
		}
	}

	public void w(boolean toAndroidLog, String tag, String message) {
//		logger.warn((new Date()).toString() + " " + LOGGING_TAG + " " + tag + " " + message);
	}

	/**
	 * FATAL Related Logger
	 * 
//	 * @param Log
//	 *            executed time
//	 * @param Log
	 *            tag name, it indicates the file name
	 * @param message
	 *            , log message
	 **/
	public void f(String tag, String message) {
		if (logCatLog)
//			logger.error((new Date()).toString() + " " + LOGGING_TAG + " " + tag + " " + message);
		if (logLevel >= LoggerConstants.LogLevel.LOGFATAL) {
			printMessage(LoggerConstants.FATAL, tag, message);
		}
	}

	/**
	 * Writing messages in to DatabaseAdapter
	 * 
	 * @param level
	 *            , it is the Log Level
//	 * @param Log
//	 *            executed time
//	 * @param Log
//	 *            tag name, it indicates the file name
//	 * @param message
	 *            , log message
	 **/
	private void printMessage(String level, String tag, String msg) {
		// Nothing to do for now.
	}
}
