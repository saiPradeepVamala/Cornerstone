package com.we.common.api.http.helpers;

import com.we.common.api.http.results.HttpResult;
import com.we.common.utils.WELogger;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Abstract HTTP helper class which will provide the APIs to POST, GET, PUT the data from server
 *
 * @author Shashi
 */
public abstract class AbstractHttpHelper {

    private static String TAG = AbstractHttpHelper.class.getName();

    protected abstract void setupRequest(HttpRequestBase request);

    protected abstract DefaultHttpClient createHttpClient(String url);

    public HttpResult getString(String url, Header[] headers) {
        WELogger.infoLog(TAG, String.format("%s  HTTP type is: GET  ", this.getClass().getName()));
        return doRequest(new HttpGet(), url, headers);
    }

    public HttpResult putString(String url, Header[] headers) {
        WELogger.infoLog(TAG, String.format("%s  HTTP type is: PUT  ", this.getClass().getName()));
        return doRequest(new HttpPut(), url, headers);
    }

    public HttpResult deleteString(String url, Header[] headers) {
        WELogger.infoLog(TAG, String.format("%s  HTTP type is: DELETE  ", this.getClass().getName()));
        return doRequest(new HttpDelete(), url, headers);
    }

    public HttpResult postString(String url, String data, Header[] headers) {
        WELogger.infoLog(TAG, String.format("%s  HTTP type is: POST  ", this.getClass().getName()));
        HttpPost postRequest = new HttpPost();
        if (data != null) postRequest.setEntity(new StringEntity(data.trim(), HTTP.UTF_8));
        WELogger.infoLog(TAG, String.format("%S Data Sent to Server:  %s", this.getClass().getName(), data));
        return doRequest(postRequest, url, headers);
    }


    public HttpResult postNameValuePairs(String url, Map<String, String> data, Header[] headers) {
        List<NameValuePair> nameValuePairs = new LinkedList<NameValuePair>();

        for (Entry<String, String> entry : data.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            WELogger.infoLog(TAG, String.format("%s  Data sent to server:  %s=%s", this.getClass().getName(), entry.getKey(), entry.getValue()));
        }

        WELogger.infoLog(TAG, String.format("%s  HTTP type is: POST  ", this.getClass().getName()));
        HttpPost postRequest = new HttpPost();
        try {
            if (data != null) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs);
                postRequest.setEntity(entity);
            }
        } catch (UnsupportedEncodingException e) {
            WELogger.errorLog(TAG, String.format("%s  UnsupportedEncodingException: postString(): Error occurred.  ", this.getClass().getName()), e);
            e.printStackTrace();
        }
        return doRequest(postRequest, url, headers);
    }

    private HttpResult doRequest(HttpRequestBase request, String url, Header[] headers) {
        HttpResult httpResult = null;
        DefaultHttpClient client = createHttpClient(url);
        try {
            WELogger.infoLog(TAG, String.format("%s  URL is = %s  ", this.getClass().getName(), url));
            request.setURI(URI.create(url.trim()));
            if (headers != null) {
                for (Header header : headers) {
                    request.addHeader(header);
                    request.setHeaders(headers);
                    WELogger.infoLog(TAG, "doRequest() :: Adding headers : " + header);
                }
            }
            setupRequest(request);
            httpResult = getHttpResult(client.execute(request));
            WELogger.infoLog(TAG, String.format("%s  doRequest(): Status code from server is = %s  ", this.getClass().getName(), String.valueOf(httpResult.statusCode)));
            WELogger.infoLog(TAG, String.format("%s  doRequest(): Response from server is = %s  ", this.getClass().getName(), httpResult.result));
            client.getConnectionManager().shutdown();
        } catch (ClientProtocolException e) {
            WELogger.errorLog(TAG, String.format("%s  ClientProtocolException: doRequest(): Error occurred while contacting server.  ", this.getClass().getName()), e);
            client.getConnectionManager().shutdown();
        } catch (IOException exception) {
            WELogger.errorLog(TAG, String.format("%s  IOException: doRequest(): Error occurred while contacting server.  ", this.getClass().getName()), exception);
            client.getConnectionManager().shutdown();
        } catch (Exception e) {
            WELogger.errorLog(TAG, String.format("%s  Exception: doRequest(): Error occurred while contacting server.  ", this.getClass().getName()), e);
            client.getConnectionManager().shutdown();
        }
        return httpResult;
    }

    private HttpResult getHttpResult(HttpResponse httpResponse) throws ParseException, IOException {
        String responseMessage = null;
        HttpEntity httpEntity = httpResponse.getEntity();
        if (httpEntity != null) {
            responseMessage = EntityUtils.toString(httpEntity);
        }
        HttpResult httpResult = new HttpResult();
        httpResult.result = responseMessage;
        int statusCode = httpResponse.getStatusLine() == null ? 0 : httpResponse.getStatusLine().getStatusCode();
        httpResult.statusCode = statusCode;
        return httpResult;
    }

    public HttpResult postString(String url, HttpEntity data, Header[] headers) {
        WELogger.infoLog(WELogger.LOG_TAG, String.format("%s  HTTP type is: POST with Entity  ", this.getClass().getName()));
        HttpPost postRequest = new HttpPost();
        try {
            if (data != null) postRequest.setEntity(data);
            WELogger.infoLog(WELogger.LOG_TAG, String.format("%S Data Sent to Server:  %s", this.getClass().getName(), data));
        } catch (Exception exception) {
            WELogger.errorLog(WELogger.LOG_TAG, String.format("%s  exception: postString(): with Entity Data Error occurred.  ", this.getClass().getName()), exception);
            exception.printStackTrace();
        }
        return doRequest(postRequest, url, headers);
    }
}
