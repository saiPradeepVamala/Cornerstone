package com.we.common.db;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Context;
import android.os.Environment;

import com.cornerstonehospice.android.manager.WEFrameworkDataInjector;
import com.we.common.models.AppPropertiesModel;
import com.we.common.tests.TestAdapter;
import com.we.common.tests.TestAdapter.TestModes;
import com.we.common.utils.ApplicationContextAdapter;
import com.we.common.utils.WELogger;

/**
 * DBAdapter to provide the relevant APIs for DBSerVice class
 * @author shashi
 *
 */

public class DbAdapter extends ApplicationContextAdapter {
	private static final String TAG 			= 		DbAdapter.class.getName();

	public static String createDbFile() {
		String fqFilename 	= 		null;
		try {
			File localDirectoryPath 			= 		getLocalDirectoryPath();
			createDbDirectory(getFullQualifiedDirectoryName(localDirectoryPath));
			fqFilename 							= 		getFullyQualifiedDbFilename(localDirectoryPath).trim();
			if (new File(fqFilename.trim()).exists()){ 
				WELogger.infoLog(TAG, "createDbFile :: Db is already exist in a locaiton : "+fqFilename);
				return fqFilename;
			}
			WELogger.infoLog(TAG, "createDbFile :: Creating new DB : "+fqFilename.trim());
			createTheDbFile(fqFilename.trim());
		}
		catch (Exception e) {
			WELogger.errorLog(TAG,"createDbFile :: Exception in DB file creation", e);
		}
		return fqFilename;
	}

	private static String getFullyQualifiedDbFilename(File localDirectoryPath) {
		AppPropertiesModel appProperties		=		WEFrameworkDataInjector.getInstance().getAppProperties();
		return String.format("%s/%s/%s", localDirectoryPath, appProperties.dbFileDirectory, appProperties.dbName);
	}

	private static String getFullQualifiedDirectoryName(File localDirectoryPath) {
		AppPropertiesModel appProperties		=		WEFrameworkDataInjector.getInstance().getAppProperties();
		return String.format("%s/%s", localDirectoryPath, appProperties.dbFileDirectory);
	}

	/**
	 * This method should only be used by test classes.
	 */
	public static void deleteDbFile() {
		try {
			File file = new File(getFullyQualifiedDbFilename(getLocalDirectoryPath()));
			boolean result = file.delete();
			WELogger.infoLog(TAG,String.format("Was file *%s* deleted: %s", file == null ? "null" : file.toString(), String.valueOf(result)));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createDbDirectory(String fqDirectoryName) {
		File dbDirectory = new File(fqDirectoryName);
		boolean result = false;
		if (!dbDirectory.exists()) result = dbDirectory.mkdirs();
		WELogger.infoLog(TAG,String.format("Directory named *%s* created: %s", fqDirectoryName, String.valueOf(result)));
    }

	private static void createTheDbFile(String fqFilename) throws FileNotFoundException {
		if (TestAdapter.testMode == TestModes.TEST) {
			new File(fqFilename);
		}
		else {
			context.openFileOutput(fqFilename, Context.MODE_PRIVATE);
		}
	}

	private static File getLocalDirectoryPath() throws Exception {		// The test mode should be always in Dev or Prod in dev phase while writing the test it should be in Test mode so that DB will be created on SDCard 
		if (TestAdapter.testMode != TestModes.TEST && context == null) throw new Exception(String.format("%s: CurrentContext field was not set and is currently null.  Make sure to set this from your Activity.  ", DbAdapter.class.getName()));
		File DBDirPath = (TestAdapter.testMode == TestModes.TEST) ? new File(Environment.getExternalStorageDirectory().getAbsolutePath()) : context.getFilesDir();
		WELogger.infoLog(TAG, "getLocalDirectoryPath :: Local direcotry path to craete DB is : "+DBDirPath);
		return TestAdapter.testMode == TestModes.TEST ? new File(Environment.getExternalStorageDirectory().getAbsolutePath()) : context.getFilesDir();
	}
}
