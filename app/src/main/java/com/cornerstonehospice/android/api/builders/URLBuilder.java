package com.cornerstonehospice.android.api.builders;


public class URLBuilder {

//	private static String baseUrl= "http://52.24.94.191/HospiceBackend/rest/";   //Dev04
	private static String baseUrl= "https://cornerstone.cloudbpa.com/HospiceBackend/rest/";   //Prod

//	private static String baseUrl= "http://192.168.1.69:8080/HospiceBackend/rest/"; // Local
	
	public static String getContactURL() {
		return  String.format("%scontact",baseUrl);
	}


	public static String getPostReferralDataUrl(String recepientEmail) {
		return String.format("%sreferral?recipient=%s", baseUrl, recepientEmail);
	}
}
