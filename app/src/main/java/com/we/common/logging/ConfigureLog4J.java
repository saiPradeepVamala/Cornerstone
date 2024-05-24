package com.we.common.logging;

import org.apache.logging.log4j.Level;

public class ConfigureLog4J {
	
	public static void configure() {
		
		LogConfigurator logConfigurator = new LogConfigurator();
		// Set log level of a specific logger
		logConfigurator.setLevel(Level.ALL);
		logConfigurator.configure();
	}
}
