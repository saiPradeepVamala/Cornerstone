package com.we.common.db;

import com.cornerstonehospice.android.manager.WEFrameworkDataInjector;
//import com.db4o.Db4oEmbedded;
//import com.db4o.config.EmbeddedConfiguration;
import com.we.common.models.AppPropertiesModel;
import com.we.common.utils.WELogger;


/**
 * DB40 Configuration builder to set the configurations.  
 */
public class DbConfigurationBuilder {
	
	private static String TAG		=		DbConfigurationBuilder.class.getName();
	
	/**
	 * Will be used to set some rules to DB and get that configuration. 
	 * Rules such as a cascade or setting activation depth.
	 */
//	public EmbeddedConfiguration getConfiguration() {
//		EmbeddedConfiguration newConfiguration		= 		Db4oEmbedded.newConfiguration();
//		AppPropertiesModel	appProperties	 		=		WEFrameworkDataInjector.getInstance().getAppProperties();
//		WELogger.infoLog(TAG, "getConfiguration :: Read ActivationDepth from Properties and Activation depth defined as a "+appProperties.dbActivationDepth);
//		newConfiguration.common().activationDepth(appProperties.dbActivationDepth);
//		return newConfiguration;
//	}
}