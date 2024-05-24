/**
 * 
 */
package com.we.common.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class is responsible for encryption  to perform encryption related operation
 * @author shashi
 *
 */
public class WEEncryption {


	public static String TAG = WEEncryption.class.getName();

	/**
	 * @return SHA-256 of password
	 */
	public static String getEncryptedPassword(String password) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			WELogger.errorLog(TAG, "getEncryptedPassword() :: " + e.getMessage());
			return String.format("%s", password.hashCode());
		}
		md.update(password.getBytes());
		byte byteData[] = md.digest();
		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	/* @formatter:off
 * consumer code can use to pass to this class. The masterkey needs to be a
 * hash of various things. The following is a suggestion.
 * masterkey = some constant in the app + a unique id from the device + the userId + the user's password
 */
	//@formatter:off
	public static String encrypt(String masterkey, String cleartext) {
		byte[] rawKey = makeAKey(masterkey);
		byte[] result = encrypt(rawKey, cleartext);
		return Base64.encodeToString(result, Base64.DEFAULT);
	}

	public static String decrypt(String masterpassword, String crypto) {
		byte[] rawKey = makeAKey(masterpassword);
		byte[] result = decrypt(rawKey, crypto);
		return new String(result);
	}

	private static byte[] encrypt(byte[] raw, String stringToEncrypt) {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			WELogger.errorLog(WEEncryption.class.getName(), "Error occurred", e);
		} catch (NoSuchPaddingException e) {
			WELogger.errorLog(WEEncryption.class.getName(), "Error occurred", e);
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		} catch (InvalidKeyException e1) {
			WELogger.errorLog(WEEncryption.class.getName(), "Error occurred", e1);
		}
		byte[] encrypted = null;
		try {
			encrypted = cipher.doFinal(getBytesForString(stringToEncrypt));
		} catch (IllegalBlockSizeException e) {
			WELogger.errorLog(WEEncryption.class.getName(), "Error occurred", e);
		} catch (BadPaddingException e) {
			WELogger.errorLog(WEEncryption.class.getName(), "Error occurred", e);
		}
		WELogger.infoLog(WEEncryption.class.getName(), String.format("The value produced is: %s", encrypted == null ? "null" : new String(encrypted)));
		WELogger.infoLog(WEEncryption.class.getName(), String.format("The length of the value produced is: %s", encrypted == null ? "null" : String.valueOf(encrypted.length)));
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, String encryptedValue) {
		WELogger.infoLog(WEEncryption.class.getName(), String.format("The length of the encrypted value STRING before decrypting it: %s", encryptedValue == null ? "null" : encryptedValue.length()));
		byte[] encrypted = null;
		encrypted = Base64.decode(encryptedValue, Base64.DEFAULT);
		WELogger.infoLog(WEEncryption.class.getName(), String.format("The length of the encrypted value BYTE[] before decrypting it: %s", encrypted == null ? "null" : String.valueOf(encrypted.length)));
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			WELogger.errorLog(WEEncryption.class.getName(), "Error occurred", e);
		} catch (NoSuchPaddingException e) {
			WELogger.errorLog(WEEncryption.class.getName(), "Error occurred", e);
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		} catch (InvalidKeyException e) {
			WELogger.errorLog(WEEncryption.class.getName(), "Error occurred", e);
		}
		byte[] decrypted = null;
		try {
			decrypted = cipher.doFinal(encrypted);
		} catch (IllegalBlockSizeException e) {
			WELogger.errorLog(WEEncryption.class.getName(), "Error occurred", e);
		} catch (BadPaddingException e) {
			WELogger.errorLog(WEEncryption.class.getName(), "Error occurred", e);
		}
		WELogger.infoLog(WEEncryption.class.getName(), String.format("The value produced is: %s", decrypted == null ? "null" : new String(decrypted)));
		WELogger.infoLog(WEEncryption.class.getName(), String.format("The length of the value produced is: %s", decrypted == null ? "null" : String.valueOf(decrypted.length)));
		return decrypted;
	}

	private static byte[] makeAKey(String seed) {
		KeyGenerator keyGen = null;
		SecureRandom sr = null;

		try {
			keyGen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			WELogger.errorLog(WEEncryption.class.getName(), "Error occurred", e);
		}
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			WELogger.errorLog(WEEncryption.class.getName(), "Error occurred", e);
		}
		sr.setSeed(getBytesForString(seed));
		keyGen.init(256, sr);
		SecretKey secretKey = keyGen.generateKey();
		return secretKey.getEncoded();
	}

	private static byte[] getBytesForString(String value) {
		try {
			return value == null ? null : value.trim().getBytes("UTF-8"); //specifying UTF8 to ensure this char set is used vs a default that isn't this charset
		} catch (UnsupportedEncodingException e) {
			WELogger.errorLog(WEEncryption.class.getName(), "Error occurred", e);
		}
		return null;
	}

}
