/**
 * 
 */
package com.cornerstonehospice.android.tests.integration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.cornerstonehospice.android.manager.WEFrameworkDataInjector;
import com.we.common.models.AppPropertiesModel;
import com.we.common.utils.WEConfigConstants;

/**
 * @author shashi
 *
 */
public abstract class BaseIntegrationTest {

	public BaseIntegrationTest() {
		readPropertiesFile();
	}

	/**
	 * Read the properties file from assets/ this will be configurable. 
	 */
	private void readPropertiesFile() {
		//		Log.d(TAG, "readPropertiesFile ::  Reading config.properties frile from assets");
		//		Resources resources		  = 		this.getResources();
		//		AssetManager assetManager = 		resources.getAssets();
		//		
		ClassLoader classLoader = getClass().getClassLoader();


		// Read from the /assets directory
		try {
			AppPropertiesModel	appPropertiesModel		=		new AppPropertiesModel();
			//InputStream propertiesInputStream 		= 		assetManager.open("config.properties");
			String current 								= 		System.getProperty("user.dir");
			InputStream propertiesInputStream 			= 		getClass().getResourceAsStream("/config.properties");
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

			appPropertiesModel.buildType				=		configPropertiesObj.getProperty(WEConfigConstants.CONFIG_BUILD_TYPE);			// These

			WEFrameworkDataInjector.getInstance().setAppProperties(appPropertiesModel);
		} catch (IOException exception) {
			//	Log.e(TAG, "readPropertiesFile :: Exception while reading the Properties file : ",exception); 
		}
	}
}
