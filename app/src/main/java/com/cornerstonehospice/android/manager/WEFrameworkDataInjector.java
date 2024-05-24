/**
 * 
 */
package com.cornerstonehospice.android.manager;

import java.util.concurrent.atomic.AtomicReference;

import com.we.common.models.AppPropertiesModel;
import com.we.common.utils.WELogger;

/**
 * This is a Bridge class which will hold the data that is needed by the Framework, this class only 
 * responsible to hold the data which is required for the Framework 
 * @author shashi
 *
 */
public class WEFrameworkDataInjector {
	
	private static final AtomicReference<WEFrameworkDataInjector> sInstance 	 = 		new AtomicReference<WEFrameworkDataInjector>();
	private static final String TAG 										 = 		WEFrameworkDataInjector.class.getName();
	private AppPropertiesModel	mAppPropertiesModel;
	private boolean mIsProduction;
	private boolean mIsTestMode;
	
    public static WEFrameworkDataInjector getInstance() {
        if (sInstance.get() == null){
            sInstance.set(new WEFrameworkDataInjector());
        }
        return sInstance.get();
    }

    private WEFrameworkDataInjector() {
	}
    
    public void setAppProperties(AppPropertiesModel propModel){
		this.mAppPropertiesModel		=		propModel;
		this.mIsTestMode				=		propModel.testMode;
		checkBuildType(propModel.buildType);
	}
    
    private void checkBuildType(String buildType) {
    	if(buildType != null){
    		mIsProduction		=		(buildType.equalsIgnoreCase("prod") ||  buildType.contains("prod"))  ? true : false;
    		WELogger.infoLog(TAG, "checkBuildType :: mIsProduction  value "+mIsProduction);
    	}
	}

    /**
     * Method will return the whether the build is production or not.
     * Use this value to handle the flow in application.  
     * @return
     */
	public boolean isProductionBuild(){
    	return mIsProduction;
    }
	
	/**
	 * This method will let application knows whether the application running on test mode or not
	 * If the application in test mode it will run the tests and disable some of the features
	 * @return mIsTestMode
	 */
	public boolean isInTestMode(){
    	return mIsTestMode;
    }


	/**
	 * Method to get the properties/configurations from /assets/config.properties  
	 * @return mAppPropertiesModel
	 */
	public AppPropertiesModel getAppProperties(){
		return mAppPropertiesModel;

	}
    

}
