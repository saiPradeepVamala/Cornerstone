
package com.cornerstonehospice.android.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.cornerstonehospice.android.activities.SplashActivity;
import com.we.common.utils.NetworkConnectionUtil;

public class SetupAsyncTask extends AsyncTask<Void, Void, Void> {

	private SplashActivity mSplashScreen;
	private Context mContext;

	
	public SetupAsyncTask(SplashActivity splashScreen) {
		
		this.mSplashScreen = splashScreen; 
		this.mContext = splashScreen.getApplicationContext();
		
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Void doInBackground(Void... params) {
		// You can setub local DB here or request for the network.
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);

		if (!NetworkConnectionUtil.isConnectedToNetwork(mContext)) {
			mSplashScreen.closeApp();
		}
		else{
			mSplashScreen.goToNextScreen();
		}

	}	


}
