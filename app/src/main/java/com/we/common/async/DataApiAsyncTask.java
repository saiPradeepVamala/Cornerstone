package com.we.common.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.we.common.api.data.request.BaseDelegatorRequest;
import com.we.common.api.data.results.DataResult;
import com.we.common.utils.NetworkConnectionUtil;
import com.we.common.utils.WELogger;

/**
 * Wrapper on top of the generic Asycntask, where it takes the data delegate request to execute in builder.  
 * This will hold the hanlder where it send back the data to resisted UI Activity/Class   
 * @author shashi
 */


public class DataApiAsyncTask extends AbstractDataApiAsyncTask {

	private ProgressDialog mProgressDialog;
	private Handler		   mHandler;


	public DataApiAsyncTask(boolean showToasts, Context context, Handler handle, ProgressDialog progressDialog) {
		super(context, showToasts);
		this.mHandler			 =		 handle;
		this.mProgressDialog 	 =		 progressDialog;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		try{
			if (mProgressDialog != null && !mProgressDialog.isShowing() && mContext != null){ 
				mProgressDialog.show();
				mProgressDialog.setCancelable(false);//If not set to false, gets cancels when user touches the screen
			}

			//check Internet connection
			if (!NetworkConnectionUtil.isConnectedToNetwork(mContext)){
				Toast.makeText(mContext, "No internet connection!", Toast.LENGTH_SHORT).show();
				if(mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.cancel();
				cancel(true);
			}
		}catch(Exception e){
			WELogger.infoLog("Data api async task", "Activity is not available");
			e.printStackTrace();
		}
	}

	private void showToast(int stringId) {
		if (mShowToasts)
			Toast.makeText(mContext, mContext.getText(stringId), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected DataResult<?> doInBackground(BaseDelegatorRequest... params) {
		super.doInBackground(params);
		if (params == null || params[0] == null) return null;
		return params[0].requestDelegate.execute(params[0]);
	}

	@Override
	protected void onPostExecute(DataResult<?> result) {
		super.onPostExecute(result);
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		sendMessage(result);

	}

	private void sendMessage(DataResult<?> result) {
		Message m = Message.obtain();
		m.obj = result;
		if(mHandler != null)
			mHandler.sendMessage(m);
		else{
				WELogger.infoLog(WELogger.LOG_TAG, "Handler is null cannot send data to UI..");
		}
	}
}