package com.we.common.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.we.common.api.data.request.BaseDelegatorRequest;
import com.we.common.api.data.results.DataResult;
import com.we.common.utils.NetworkConnectionUtil;
import com.we.common.utils.WELogger;

/**
 * A generic asyc task which will execute the respective delegator execute() method which will be passed in 
 * request cooking. Use this class to perform any non main thread operations. This class will provide handler constructor
 * to send the messages to registered handlers
 * 
 * @author shashi
 * */

public abstract class AbstractDataApiAsyncTask extends AsyncTask<BaseDelegatorRequest, Void, DataResult<?>> {

	protected Context mContext;
	protected boolean miIsConnectedToNW;
	protected boolean mShowToasts;

	/**
	 * If context isn't available, pass null. Note that for this case, the
	 * system will not be able to detect whether or not a network connection
	 * exists.
	 */
	protected AbstractDataApiAsyncTask(Context context, boolean showToasts) {
		if (context != null) this.mContext = context.getApplicationContext();
		this.mShowToasts = showToasts;
	}

	@Override
	protected DataResult<?> doInBackground(BaseDelegatorRequest... params) {
		if (mContext != null) {
			miIsConnectedToNW = NetworkConnectionUtil.isConnectedToNetwork(mContext);
			WELogger.infoLog(this.getClass().getName(), String.format("isConnected to network = %s", String.valueOf(miIsConnectedToNW)));
		}
		return null;
	}

	@Override
	protected void onPostExecute(DataResult<?> result) {
		if (mContext != null && !miIsConnectedToNW && mShowToasts) {
			Toast.makeText(mContext, "No internet connection!", Toast.LENGTH_SHORT).show();
		}
		super.onPostExecute(result);
	}
}
