package com.we.common.api.data.results;

import java.util.List;

/**
 * Generic data results that will be returned by the builder, the builder is responsible  
 * to assign respective entity or entities as a result 
 * T could be the response JSON/XML  model that to be given to the resisted UI Activity/class
 * @author shashi
 *  
 * @param <T>
 */

public class DataResult<T> {
	public boolean successful;
	public String result;
	public int statusCode;
	public T entity;
	public List entities;
}
