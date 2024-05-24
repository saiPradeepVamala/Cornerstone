package com.we.common.api.data.request;

import com.we.common.api.data.results.DataResult;

/**
 * Interface which will executes the delegator/builder execute method
 * @author shashi
 */

public interface IRequestExecutor {
	
	public <T> DataResult<T> execute(BaseDelegatorRequest dataRequest);
}
