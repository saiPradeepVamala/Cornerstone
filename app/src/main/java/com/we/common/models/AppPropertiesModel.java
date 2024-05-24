/**
 * 
 */
package com.we.common.models;


/**
 * Class to hold the config.properties file values 
 * @author shashi
 */
public class AppPropertiesModel {
	
	public String dbName;
	public int dbActivationDepth;
	public String dbFileDirectory;
	
	public int httpMaxTotalConnections;
	public int httpConnectionTimeOut;
	public int httpSOTimeOut;
	public int httpSoketBuffer;
	
	public int logEnableSendLog;
	public String logUploadURL;		 
	public int logMaxLogFileSize;
	public int logLevel;
	public String logFileName;
	
	public String extDirname;
	
	public String buildType;
	
	public boolean testMode;
}
