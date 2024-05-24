package com.we.common.api.http.helpers;

import com.cornerstonehospice.android.manager.WEFrameworkDataInjector;
import com.we.common.models.AppPropertiesModel;
import com.we.common.utils.WELogger;

import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class WEHttpHelper extends AbstractHttpHelper {

    private static final int HTTP_PORT_NUMBER = 80;
    private static final int HTTPS_PORT_NUMBER = 443;

    @Override
    protected void setupRequest(HttpRequestBase request) {
    }

    @Override
    protected DefaultHttpClient createHttpClient(String url) {
        return url.contains("https") ? getHttpsClient() : getHttpClient();
    }

    /**
     * For HTTP Client requests
     * @return
     */
    private DefaultHttpClient getHttpClient() {
        HttpParams httpParameters = new BasicHttpParams();

        AppPropertiesModel applicationModel = WEFrameworkDataInjector.getInstance().getAppProperties();
        if (!WELogger.testMode) {                            // FIXME: we don't get application model if we are in test mode,
            // so lets process these only when test mode is false
            ConnManagerParams.setMaxTotalConnections(httpParameters, applicationModel.httpMaxTotalConnections);
            HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
            HttpConnectionParams.setConnectionTimeout(httpParameters, applicationModel.httpConnectionTimeOut);
            HttpConnectionParams.setSoTimeout(httpParameters, applicationModel.httpSOTimeOut);
            HttpConnectionParams.setSocketBufferSize(httpParameters, applicationModel.httpSoketBuffer);
        }

        return new DefaultHttpClient(httpParameters);
    }

    /**
     * For HTTPS Client requests
     * @return
     */
    private DefaultHttpClient getHttpsClient() {
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new WESSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), HTTP_PORT_NUMBER));
            registry.register(new Scheme("https", sf, HTTPS_PORT_NUMBER));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            return new DefaultHttpClient(ccm, params);
        } catch (KeyStoreException | CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;

    }

    public DefaultHttpClient getHTTPClient(String url){
        return	createHttpClient(url);
    }
}
