package com.we.common.logging;
/**
 * @author Wenable
 * Defining log constants
 */
public class LoggerConstants {
	
	public static String DEBUG = "D";
	public static String ERROR = "E";
	public static String INFO = "I";
	public static String VERB = "V";
	public static String WARN = "W";
	public static String FATAL = "F";
	
	public class HTTPParams {
		public static final int CONNECTION_TIMEOUT = 20 * 1000;		//	20 seconds
		public static final int SOCKET_TIMEOUT = 20 * 1000;			//	20 seconds
	}

	public class LogLevel {
		public static final int DISABLE 	= 	-1;
		public static final int LOGFATAL 	= 	0;
		public static final int LOGERROR	= 	1;
		public static final int LOGWARN 	= 	2;
		public static final int LOGINFO 	= 	3;
		public static final int LOGDEBUG 	= 	4;
		public static final int LOGVERBOSE 	= 	5;
	}

	public class StorageMechanism {
		public static final String FILE = "FILE";
		public static final String DB = "DB";
	}

	public class Notifications {
		public static final String LOG_STATUS = "log_status";
		public static final String RESTART_IAC = "restart_iac";
		public static final String GET_JOB_STATUS_ON_DEVICE = "get_job_status_in_device";
		public static final String TRIGGER_RESTART_JOB = "trigger_restart_job";
		public static final String STOP_JOB = "stop_job";
		public static final String TRIGGER_JOB = "trigger_job";
	}
}
