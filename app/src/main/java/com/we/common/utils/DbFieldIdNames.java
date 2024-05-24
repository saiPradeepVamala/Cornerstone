package com.we.common.utils;

/**
 * Place field names in this class that will be used to query for. Since we use
 * DB4O SODA (i.e. low-level) queries so that we don't rely on the DB4O native
 * optimizer as it doesn't work with the Dalvik VM, references to field names
 * aren't type-safe. This is a small - yet slightly risky - price to pay. In an
 * effort to mitigate the risk of hard-coding field names in our consumer code,
 * place them in this class so that at least they are kept in one place.
 * 
 * @author chamu
 * 
 */
public class DbFieldIdNames {
	public static final String ID = "id";
}
