package com.we.common.logging;

import android.text.TextUtils;
import android.util.Log;

import com.cornerstonehospice.android.manager.WEFrameworkDataInjector;
//import com.we.common.api.http.helpers.WEHttpHelper;
import com.we.common.api.http.helpers.WEHttpHelper;
import com.we.common.models.AppPropertiesModel;
import com.we.common.utils.FileUtilsForLogger;
import com.we.common.utils.WEConfigConstants;
import com.we.common.utils.WELogger;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.ContentBody;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//import org.apache.log4j.Logger;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.ContentBody;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ManageLogs extends Thread {
	private static final long THREAD_SLEEP 		= 		1000;
	private static final String TAG 			= 		ManageLogs.class.getSimpleName();
	private static ManageLogs instance;
	public  File parentDir 						= 		new File(FileUtilsForLogger.logPath);
	public int mIsSendLogEnabled				=		0;
	public String mLogsUploadURL				=		null;
	public int 	mMaxFileUploadSize;	

	public static ManageLogs getInstance() {
		if (instance == null) {
			instance = new ManageLogs();
			instance.setSendLogState();
			instance.setUploadLogsURL();
			instance.setMAxUploadFileSize();
		}
		return instance;
	}

	public void setMAxUploadFileSize(){
		AppPropertiesModel		appProperties		=		WEFrameworkDataInjector.getInstance().getAppProperties();
		mMaxFileUploadSize		=		appProperties.logMaxLogFileSize;
		WELogger.infoLog(TAG, "setMAxUploadFileSize :: Max Log fie upload size : "+mMaxFileUploadSize); 
	}
	
	public void setSendLogState(){
		AppPropertiesModel		appProperties		=		WEFrameworkDataInjector.getInstance().getAppProperties();
		mIsSendLogEnabled		=		appProperties.logEnableSendLog;
		WELogger.infoLog(TAG, "setSendLogState :: Send log state 1 is to enable Upload : "+mIsSendLogEnabled);
	}
	
	public void setUploadLogsURL(){
		AppPropertiesModel		appProperties		=		WEFrameworkDataInjector.getInstance().getAppProperties();
		mLogsUploadURL		=		appProperties.logUploadURL;
		WELogger.infoLog(TAG, "setUploadLogsURL :: Upload logs URL : "+mIsSendLogEnabled);
	}

	private ManageLogs() {
	}

	@Override
	public void run() {
		while (true) {
			if ( mIsSendLogEnabled == WEConfigConstants.LOG_ENABLED_CODE ) {
				List<File> files = getFilesList();
				if (files.size() > 0) {
					for (File file : files) {
						String gzipFile = file.getAbsolutePath();
						// gzipFile = gzipFile.substring(0,gzipFile.lastIndexOf("."));
						gzipFile 				= 		gzipFile + ".gz";
						boolean fileZipped 		=		FileUtilsForLogger.compressGzipFile(file.getAbsolutePath(), gzipFile);
						if (fileZipped) {
							file.delete();
						} else {
							sendLogs(file);
						}
					}
				}

				List<File> zippedFiles = getZippedFilesList();
				if (zippedFiles.size() > 0) {
					for (File zippedFile : zippedFiles) {
						sendLogAsZipFile(zippedFile);
					}
				}

				try {
					Thread.sleep(THREAD_SLEEP);
				} catch (InterruptedException interruptedException) {
					/*
					 * Interrupted exception will be thrown when a sleeping or
					 * waiting getDashboardDataThread is interrupted.
					 */
					System.out.println("ManageLogs Thread is interrupted when it is sleeping"
							+ interruptedException);
				}
			}
		}
	}

	private void sendLogs(File file) {
		String mFileName = file.getName();
		mFileName = mFileName.substring(0, mFileName.lastIndexOf("."));
		mFileName = mFileName + ".gz";
		Map<String, Object> mapLog = new HashMap<String, Object>();
		mapLog.put("fileData", file);
		// Need to get this every time to see the memory issues if any
		mapLog.put("fileName", file.getName());
		String logSentSuccessfully = post(mapLog);
		if (!TextUtils.isEmpty(logSentSuccessfully)&& logSentSuccessfully.equals("1")) {
			System.out.println("Successfully log is sent to server File:"+ file.getName());
			if (file.delete()) {
				System.out.println("Successfully sent to server and deleted the File: "+ file.getName());
			}
		}
	}

	private void sendLogAsZipFile(File zippedFile) {
		Map<String, Object> mapLog  = 		new HashMap<String, Object>();
		mapLog.put("fileData", zippedFile);
		// Need to get this every time to see the memory issues if any
		mapLog.put("fileName", zippedFile.getName());
		String logSentSuccessfully = post(mapLog);
		if (!TextUtils.isEmpty(logSentSuccessfully)&& logSentSuccessfully.equals("1")) {
			System.out.println("Successfully log is sent to server File:"+ zippedFile.getName());
			if (zippedFile.delete()) {
				System.out.println("Successfully sent to server and deleted the File:"+ zippedFile.getName());
			}
		}
	}


//	 To make this as a separate entity we are building the
	public String post(Map<String, Object> postData) {
		WEHttpHelper httpHelper			=		new WEHttpHelper();
		DefaultHttpClient httpClient	=		httpHelper.getHTTPClient(mLogsUploadURL);		// Will create a new HTTP client while getting from Helper class
//		Logger log4jLogger 				= 		Logger.getLogger(ManageLogs.class);
		String returnValue 				= 		"0";
		String actionUrl 				=		mLogsUploadURL;
		HttpPost httpPost 				= 		new HttpPost(actionUrl);
		MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		for (Entry<String, Object> entry : postData.entrySet()) {
			if (entry.getValue() instanceof File) {
				ContentBody cbFile 		= 		new FileBody((File) entry.getValue());
				mpEntity.addPart(entry.getKey(), (org.apache.http.entity.mime.content.ContentBody) cbFile);
			} else if (entry.getValue() instanceof String) {
				ContentBody cbStr = null;
				try {
					cbStr = (ContentBody) new StringBody(entry.getValue().toString());
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, Log.getStackTraceString(e));
					e.printStackTrace();
				}
//				mpEntity.addPart(entry.getKey(), cbStr);
			} else {
			}
		}
		httpPost.setEntity((HttpEntity) mpEntity);
		try {
			// execute the method
			HttpResponse response 		= 		null;
			response 					= (HttpResponse) httpHelper.getHTTPClient(mLogsUploadURL).execute((HttpUriRequest) httpPost);
			WELogger.infoLog(TAG,"post :: Upload log response from Server :: "+response);
//			if (response.getStatusLine().getStatusCode() == 200) {
//				// get the response body as an array of bytes
////				HttpEntity entity = response.getEntity();
//				if (entity != null) {
//
//					byte[] bytes = EntityUtils.toByteArray(entity);
//					String readS = new String(bytes);
//					if (readS.equals("1")) {
//						returnValue = "1";
//					}
//				}
//			}
		} catch (Exception e) {
//			log4jLogger.error(e.getMessage());
			httpPost.abort();
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}
		return returnValue;
	}

	private List<File> getFilesList() {
		ArrayList<File> inFiles = new ArrayList<File>();
		if (parentDir != null && parentDir.exists()) {
			File[] files = parentDir.listFiles();
			if (files != null && files.length > 0) {
				for (File file : files) {
					if (((file.getName().startsWith("Crash"))
							|| (!isTodaysLog(file)) || ((file.length() >= mMaxFileUploadSize)))
							&& (file.getName().endsWith(".txt"))) {
						inFiles.add(file);
					}
				}
			}
		}
		return inFiles;
	}

	private boolean isTodaysLog(File file) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddyyyy");
		String mDate = simpleDateFormat.format(new Date());
		if (file.getName().contains(mDate)) {
			return true;
		} else
			return false;
	}

	private List<File> getZippedFilesList() {
		ArrayList<File> zippedFilesList = new ArrayList<File>();
		if (parentDir != null && parentDir.exists()) {
			File[] zippedFiles = parentDir.listFiles();
			for (File file : zippedFiles) {
				if (file.getName().endsWith(".gz")) {
					zippedFilesList.add(file);
				}
			}
		}
		return zippedFilesList;
	}
}
