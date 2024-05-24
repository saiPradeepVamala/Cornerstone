package com.we.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Environment;
import android.util.Log;

import com.cornerstonehospice.android.manager.WEFrameworkDataInjector;

public class FileUtilsForLogger {

	private static final String TAG = FileUtilsForLogger.class.getSimpleName();
	public static String logPath = Environment.getExternalStorageDirectory() + File.separator + WEFrameworkDataInjector.getInstance().getAppProperties().extDirname + File.separator + "Logs";
	public static String fileName;
	public static File logsDirectory;

	@SuppressWarnings("finally")
	public static String readRomateFile(String romatefilename) {
		BufferedReader reader;
		String content = "";
		String inputLine;
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(romatefilename);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity);
			InputStream is 	= entity.getContent();
			reader 			= new BufferedReader(new InputStreamReader(is));
			inputLine = reader.readLine();
			while (inputLine != null) {
				content += inputLine;
				inputLine = reader.readLine();
			}
			reader.close();
			Log.d("FileUtils", "RemoteFileContent:" + content);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.e("FileUtils:MalformedURLException", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("FileUtils:IOException:", e.getMessage());
		} finally {
			return content;
		}
	}

	public static void main(String[] args) {
		readRomateFile("http://localhost:8080/aa/update.json");
	}

	public static void createFile(String log) {
		createDirectory();
		File file = createLogFile();
		StringBuffer buffer = new StringBuffer();
		if (file.exists()) {

			try {
				buffer.append(System.getProperty("line.separator"));
				buffer.append(new Date().toString() + " :: " + log);
				buffer.append(System.getProperty("line.separator"));
				writeToFile(file, buffer.toString());
			} catch (Exception ex) {
				Log.e(TAG, buffer.toString());
			}
		} else {
			Log.e(TAG, buffer.toString());
			return;
		}
	}

	/*public static byte[] convertFileToByteArray(File file) {
		FileInputStream fileInputStream = null;
		byte[] byteArrayOfFile = new byte[(int) file.length()];
		try {
			// convert file into array of bytes
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(byteArrayOfFile);
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteArrayOfFile;
	}*/

	public static boolean compressGzipFile(String file, String gzipFile) {
		try {
			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(gzipFile);
			GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = fis.read(buffer)) != -1) {
				gzipOS.write(buffer, 0, len);
			}
			// close resources
			gzipOS.close();
			fos.close();
			fis.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static byte[] compressGzipFile(File file) {
		GZIPOutputStream gzos = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			byte[] buffer = new byte[(int) file.length()];
			inputStream.read(buffer);
			gzos = new GZIPOutputStream(baos);
			gzos.write(new String(buffer).getBytes("UTF-8"));
		} catch (Exception ex) {
		} finally {
			if (gzos != null)
				try {
					gzos.close();
				} catch (IOException ignore) {
				};
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException ignore) {
				};
		}
		byte[] fooGzippedBytes = baos.toByteArray();
		return fooGzippedBytes;
	}

	private static File createLogFile() {
		Date date = new Date();
		fileName = "CrashReport_" + String.valueOf(date.getTime()) + ".txt";
		File file = new File(logPath + File.separator + fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
		return file;
	}

	private static void createDirectory() {
		logsDirectory = new File(logPath);
		if (!logsDirectory.exists())
			logsDirectory.mkdir();
	}

	private static void writeToFile(File file, String log) {
		try {
			if (file != null) {
				FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.append(log);
				bw.close();
			}
		} catch (IOException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			return;
		}
	}
}
