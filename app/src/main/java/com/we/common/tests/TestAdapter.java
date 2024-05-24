package com.we.common.tests;

import com.we.common.utils.AppTestPrefs;

/**
 * Test adapter to set the mode of application running on 
 * @author shashi
 *
 */


public class TestAdapter {
	public static TestModes testMode =  AppTestPrefs.BUILD_STATE.isProduction() ? TestModes.SILENT : TestModes.DEVELOPMENT;
	public static enum TestModes {TEST, DEVELOPMENT, SILENT }
}