package com.we.common.logging;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.logging.log4j.Level;

import android.os.Environment;
import android.util.Log;

import com.cornerstonehospice.android.manager.WEFrameworkDataInjector;
import com.we.common.models.AppPropertiesModel;
import com.we.common.utils.WELogger;

public class LogConfigurator {

	private String TAG		=		LogConfigurator.class.getName();
	private Level level = Level.DEBUG;
	private String filePattern = "%d - [%p::%c::%C] - %m%n";
	private String logCatPattern = "%m%n";
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

	private String fileName = null;
	private int maxBackupSize = 5;
	private long maxFileSize = 1024 * 1024;
	private boolean immediateFlush = true;
	private boolean useLogCatAppender = true;
	private boolean useFileAppender = true;

	public LogConfigurator() {
		AppPropertiesModel		properties		=		WEFrameworkDataInjector.getInstance().getAppProperties();
		String	fileName						=       Environment.getExternalStorageDirectory() + File.separator + properties.extDirname + File.separator + "Logs" + File.separator
				+ properties.logFileName +"_" + simpleDateFormat.format(new Date()) + ".txt";
		WELogger.infoLog(TAG, "LogConfigurator :: fileName : "+fileName);
		setFileName(fileName);
	}

	public LogConfigurator(final String fileName) {
		setFileName(fileName);
	}

	public LogConfigurator(final String fileName, final Level level) {
		this(fileName);
		setLevel(level);
	}

	public LogConfigurator(final String fileName, final Level level, final String pattern) {
		this(fileName);
		setLevel(level);
		setFilePattern(pattern);
	}

	public LogConfigurator(final String fileName, final int maxBackupSize, final long maxFileSize, final String pattern, final Level level) {
		this(fileName, level, pattern);
		setMaxBackupSize(maxBackupSize);
		setMaxFileSize(maxFileSize);
	}

	public void configure() {
//		final Logger root = Logger.getRootLogger();
		if (isUseFileAppender()) {
			configureFileAppender();
		}
		if (isUseLogCatAppender()) {
			configureLogCatAppender();
		}
//		root.setLevel(getLevel());
	}

	private void configureFileAppender() {
//		final Logger root = Logger.getRootLogger();
//		final RollingFileAppender rollingFileAppender;
//		final Layout fileLayout = new PatternLayout(getFilePattern());
//		try {
////			rollingFileAppender = new RollingFileAppender(fileLayout, getFileName());
//			Log.e("LogConfigurator", "Rolling file set");
//		} catch (final IOException e) {
//			throw new RuntimeException("Exception configuring log system", e);
//		}
//		rollingFileAppender.setMaxBackupIndex(getMaxBackupSize());
//		rollingFileAppender.setMaximumFileSize(getMaxFileSize());
//		rollingFileAppender.setImmediateFlush(isImmediateFlush());
//		rollingFileAppender.setAppend(true);
//		root.addAppender(rollingFileAppender);
//		root.setLevel(getLevel());
	}

	private void configureLogCatAppender() {
//		final Logger root = Logger.getRootLogger();
//		final Layout logCatLayout = new PatternLayout(getLogCatPattern());
//		final LogCatAppender logCatAppender = new LogCatAppender(logCatLayout);
//		root.addAppender(logCatAppender);
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(final Level level) {
		this.level = level;
	}

	public String getFilePattern() {
		return filePattern;
	}

	public void setFilePattern(final String filePattern) {
		this.filePattern = filePattern;
	}

	public String getLogCatPattern() {
		return logCatPattern;
	}

	public void setLogCatPattern(final String logCatPattern) {
		this.logCatPattern = logCatPattern;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public int getMaxBackupSize() {
		return maxBackupSize;
	}

	public void setMaxBackupSize(final int maxBackupSize) {
		this.maxBackupSize = maxBackupSize;
	}

	public long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(final long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public boolean isImmediateFlush() {
		return immediateFlush;
	}

	public void setImmediateFlush(final boolean immediateFlush) {
		this.immediateFlush = immediateFlush;
	}

	public boolean isUseFileAppender() {
		return useFileAppender;
	}

	/**
	 * @param useFileAppender
	 *            the useFileAppender to set
	 */
	public void setUseFileAppender(final boolean useFileAppender) {
		this.useFileAppender = useFileAppender;
	}

	/**
	 * @return the useLogCatAppender
	 */
	public boolean isUseLogCatAppender() {
		return useLogCatAppender;
	}

	/**
	 * @param useLogCatAppender
	 *            the useLogCatAppender to set
	 */
	public void setUseLogCatAppender(final boolean useLogCatAppender) {
		this.useLogCatAppender = useLogCatAppender;
	}
}