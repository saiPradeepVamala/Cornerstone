package com.cornerstonehospice.android.api.results;

import com.we.common.api.data.results.DataResult;


/**
 * 
 * @author shashi
 *
 */
public class LoginResult extends DataResult<LoginResult> {
	
	public String token;
	public UserModel userBean;
	
}
