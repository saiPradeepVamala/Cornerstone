package com.cornerstonehospice.android.application;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cornerstonehospice.android.api.builders.CriteriaBuilder;
import com.cornerstonehospice.android.api.requests.CriteriaDataRequest;
import com.cornerstonehospice.android.api.results.CriteriaDataResult;
import com.cornerstonehospice.android.manager.SharedPreferenceManager;
import com.cornerstonehospice.android.manager.WEFrameworkDataInjector;
import com.we.common.api.data.results.DataResult;
import com.we.common.async.DataApiAsyncTask;
import com.we.common.builders.json.CommonJsonBuilder;
import com.we.common.logging.ConfigureLog4J;
import com.we.common.logging.ManageLogs;
import com.we.common.models.AppPropertiesModel;
import com.we.common.utils.ApplicationContextAdapter;
import com.we.common.utils.WEConfigConstants;
import com.we.common.utils.WELogger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.State;
import java.util.Properties;
/**
 * Application class which will load at the time of launch of application, 
 * usually this will be having the initialization , global access of methods and controlling the activity life cycle 
 * @author shashi
 */

public class CornerStoneApplication extends Application {

	private static String TAG		=	CornerStoneApplication.class.getName();
	private String mCriteriaString	=	null;

	@Override
	public void onCreate() {
		super.onCreate();
		ApplicationContextAdapter.context = this;
		readPropertiesFile();
		WELogger.infoLog(TAG, "onCreate :: In Application class onCreate() ");
		intit();
		setupLogging();
		getCriteria();
	}


//
//	private Handler criteriaAPIHandler = new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			DataResult<CriteriaDataResult> criteriaResults	=	(DataResult<CriteriaDataResult>)msg.obj;
//			String str = new CommonJsonBuilder().getJsonForEntity(criteriaResults.entity);
//			mCriteriaString	=	"Hello All";
//		}
//	};


	public String getParsedCriteria(){
		return mCriteriaString;
	}


	/**
	 * Method to get the criteria this is one time load as the data is a static content
	 */
	private void getCriteria() {
		// TODO: get list of criteria from server
		WELogger.infoLog(TAG, "getCriteriaInfo() :: Getting criteria information from Server");
		CriteriaDataRequest contactRequest 		= 		new CriteriaDataRequest(getApplicationContext());
		contactRequest.requestDelegate 			= 		new CriteriaBuilder();
		contactRequest.requestType 				= 		CriteriaBuilder.RequestType.GET_CRITERIA;
//		new DataApiAsyncTask(true, this, criteriaAPIHandler, null).execute(contactRequest);

	}


	private void intit() {
		SharedPreferenceManager.intialize(this);				//Initilize shared preference
	}

	private void setupLogging() {
		configureLog4J();
		initializeSendLog();
	}

	private void initializeSendLog() {
		AppPropertiesModel		appProperties		=		WEFrameworkDataInjector.getInstance().getAppProperties();
		if ( appProperties.logEnableSendLog == WEConfigConstants.LOG_ENABLED_CODE ) {	
			ManageLogs manageLogs 		=		ManageLogs.getInstance();
			if (manageLogs.getState() == State.NEW)
				manageLogs.start();
		}
	}

	private void configureLog4J() {
		try {
			WELogger.infoLog(TAG, "In configureLog4J");
			ConfigureLog4J.configure();
			WELogger.infoLog(TAG, "Out configureLog4J");
		} catch (Exception ex) {
			WELogger.errorLog(TAG, "Exception while configuring Log4J :: ", ex);
		}
	}


	/**
	 * Read the properties file from assets/ this will be configurable. 
	 */
	private void readPropertiesFile() {
		Log.d(TAG, "readPropertiesFile ::  Reading config.properties frile from assets");
		Resources resources		  = 		this.getResources();
		AssetManager assetManager = 		resources.getAssets();

		// Read from the /assets directory
		try {
			AppPropertiesModel	appPropertiesModel		=		new AppPropertiesModel();
			InputStream propertiesInputStream 			= 		assetManager.open("config.properties");
			Properties configPropertiesObj 				= 		new Properties();
			configPropertiesObj.load(propertiesInputStream);

			appPropertiesModel.dbName					=		configPropertiesObj.getProperty(WEConfigConstants.CONFIG_DB_NAME);			// These 
			appPropertiesModel.dbActivationDepth		=		Integer.valueOf(configPropertiesObj.getProperty(WEConfigConstants.CONFIG_DB_ACTIVATION_DEPTH));
			appPropertiesModel.dbFileDirectory			=		configPropertiesObj.getProperty(WEConfigConstants.CONFIG_DB_LOCAITON_PATH);

			appPropertiesModel.httpConnectionTimeOut	=		Integer.valueOf(configPropertiesObj.getProperty(WEConfigConstants.CONFIG_HTTP_CONNECTION_TIMEOUT));
			appPropertiesModel.httpMaxTotalConnections	=		Integer.valueOf(configPropertiesObj.getProperty(WEConfigConstants.CONFIG_HTTP_MAX_TOTAL_CONNECTIONS));
			appPropertiesModel.httpSOTimeOut			=		Integer.valueOf(configPropertiesObj.getProperty(WEConfigConstants.CONFIG_HTTP_SO_TIMEOUT));
			appPropertiesModel.httpSoketBuffer			=		Integer.valueOf(configPropertiesObj.getProperty(WEConfigConstants.CONFIG_HTTP_SOKET_BUFFER));

			appPropertiesModel.logEnableSendLog			=		Integer.valueOf(configPropertiesObj.getProperty(WEConfigConstants.CONFIG_LOG_ENABLE_LOG));
			appPropertiesModel.logUploadURL				=		configPropertiesObj.getProperty(WEConfigConstants.CONFIG_LOG_UPLOAD_URL);
			appPropertiesModel.logMaxLogFileSize		=		Integer.valueOf(configPropertiesObj.getProperty(WEConfigConstants.CONFIG_LOG_FILE_MAX_SIZE));
			appPropertiesModel.logLevel					=		Integer.valueOf(configPropertiesObj.getProperty(WEConfigConstants.CONFIG_LOG_LEVEL));
			appPropertiesModel.logFileName				=		configPropertiesObj.getProperty(WEConfigConstants.CONFIG_LOG_FILE_NAME_START_WITH);

			appPropertiesModel.extDirname				=		configPropertiesObj.getProperty(WEConfigConstants.CONFIG_EXT_DIR_NAME);
			
			appPropertiesModel.buildType				=		configPropertiesObj.getProperty(WEConfigConstants.CONFIG_BUILD_TYPE);
			
			appPropertiesModel.testMode					=		Boolean.valueOf(configPropertiesObj.getProperty(WEConfigConstants.CONFIG_TEST_MODE));

			WEFrameworkDataInjector.getInstance().setAppProperties(appPropertiesModel);
		} catch (IOException exception) {
			Log.e(TAG, "readPropertiesFile :: Exception while reading the Properties file : ",exception); 
		}
	}

}
