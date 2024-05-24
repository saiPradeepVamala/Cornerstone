/**
 * 
 */
package com.cornerstonehospice.android.api.builders;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.cornerstonehospice.android.utils.AppConstants;
import com.we.common.api.data.request.BaseDelegatorRequest;
import com.we.common.api.data.results.DataResult;
import com.we.common.api.http.results.HttpResult;
import com.we.common.builders.BaseBuilder;

/**
 * @author shashi
 *
 */
public class AppBuilder extends BaseBuilder {

	public Header[] getTokenHeaders(){
		Header[] headers =	{new BasicHeader("Content-Type", "application/json"), new BasicHeader(AppConstants.TOKEN_ID_KEY, AppConstants.STATIC_TOKEN) };
		return headers;
	}

	protected Header[] getDefaultHeaders(){
		Header[] headers =	{new BasicHeader("Content-Type", "application/json")};
		return headers;
	}

	@Override
	public <T> DataResult<T> execute(BaseDelegatorRequest dataRequest) {
		return null;
	}
	
	/**
	 * If the result code is 201
	 */
	protected boolean isResultCreated(HttpResult httpResult) {
		return !(httpResult == null || httpResult.statusCode != 201);
	}
	
	/**
	 * If the result code is 202
	 * @param httpResult
	 */
	protected boolean isResultAccepted(HttpResult httpResult) {
		return !(httpResult == null || httpResult.statusCode != 202);
	}
	
	/**
	 * if the result is bad request 400
	 * @param httpResult
	 * @return
	 */
	protected boolean isBadRequest(HttpResult httpResult) {
		return !(httpResult == null || httpResult.statusCode != 400);
	}
	
	/**
	 * if the result is un authorized request 401
	 * @param httpResult
	 * @return
	 */
	protected boolean isUnAuthResponse(HttpResult httpResult) {
		return !(httpResult == null || httpResult.statusCode != 401);
	}
	
	/**
	 * if the result is un authorized request 200
	 * @param httpResult
	 * @return
	 */
	protected boolean isResultOk(HttpResult httpResult) {
		return !(httpResult == null || httpResult.statusCode != 200);
	}

	
}