package com.we.common.utils;

public enum AppDBType {
	DEVELOPMENT, PRODUCTION;
	
	public boolean isProduction(){
		return this == AppDBType.PRODUCTION;
	}
}