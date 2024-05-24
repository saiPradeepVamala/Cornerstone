/**
 * 
 */
package com.cornerstonehospice.android.utils;


import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author shashi
 *
 */
public class AppUtils {

	public static String getParsedString(){
		String toBeParseString = "Username : WEFramework";
		return null;
	}

	public static String getJsonFromFile(String assetsPath, Context ctx){
		StringBuilder buf=new StringBuilder();
		String fileJson="";
		try {
			String str;
			InputStream json= ctx.getAssets().open(assetsPath);
			BufferedReader in=
					new BufferedReader(new InputStreamReader(json, StandardCharsets.UTF_8));


			while ((str=in.readLine()) != null) {
				buf.append(str);
				fileJson+=str;
			}

			in.close();
		} catch (Exception e){
		}

		return fileJson;
	}
}
