/**
 * 
 */
package com.we.common.api.data.request;



/**
 * Super/base class for every request that should extend BaseDelegateRequest, while cooking/preparing the request
 * the typically Delegator will beBuilder for respective operation, type will be Enum which will define what sort of operation
 * we need to perform in Builder 
 * 
 * @author Shashi
 */
public abstract class BaseDelegatorRequest {
	
	/**
	 * Uses command executor concept to execute the commands which uses command patterns, 
	 * typically the delegate class will be the builder
	 */
	public IRequestExecutor requestDelegate;
	
	/**
	 * The type of the command that to be executed with in the delegated class or a builder
	 */
	public IRequestType requestType;
	
}

