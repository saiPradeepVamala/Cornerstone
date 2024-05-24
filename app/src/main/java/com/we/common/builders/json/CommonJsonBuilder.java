package com.we.common.builders.json;


/**
 * Generic JSON parser which is responsible to convert the Json to entity and Entity to Json String.
 * It uses Gson internally
 * @author Shashi
 *
 */

import android.util.Log;

import com.google.gson.Gson;
import com.we.common.utils.WELogger;

import java.lang.reflect.Type;
import java.util.List;

public class CommonJsonBuilder {
	public static <T> T getEntityForJson(String json, Class<T> entity) {
		try {
			return new Gson().fromJson(json, entity);
		}
		catch (Exception e) {
			WELogger.errorLog(WELogger.LOG_TAG, String.format("%s  ", CommonJsonBuilder.class.getName()), e);
	
		}
		return null;
	}

	public static <T> String getJsonForEntity(Object entity) {
		try {
			return new Gson().toJson(entity);
		}
		catch (Exception e) {
			WELogger.errorLog(WELogger.LOG_TAG, String.format("%s  ", CommonJsonBuilder.class.getName()), e);
		}
		return null;
	}

	public static <T> List<T> getListForJson(String json, Type type) {
		try {
            return new Gson().fromJson(json, type);
		}
		catch (Exception e) {
			Log.e(WELogger.LOG_TAG, String.format("%s  ", CommonJsonBuilder.class.getName()), e);
		}
		return null;
	}
}
