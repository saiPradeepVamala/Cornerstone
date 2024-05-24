package com.we.common.builders;

import com.we.common.api.data.request.IRequestExecutor;
import com.we.common.api.http.helpers.WEHttpHelper;


/**
 * @author Shashi
 *
 */


public abstract class BaseBuilder  implements IRequestExecutor{
	
	protected WEHttpHelper httpHelper =		new WEHttpHelper();
	
	protected void preExecute() {
		httpHelper = getHttpHelper();
	}
	private WEHttpHelper getHttpHelper() {
		httpHelper = new WEHttpHelper();
		return httpHelper;
	}
}
