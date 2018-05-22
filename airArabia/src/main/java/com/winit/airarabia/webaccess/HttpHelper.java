package com.winit.airarabia.webaccess;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.utils.LogUtils;

//This class is used for getting connections and response.
public class HttpHelper 
{
	//Time out for the connection is set with settings CSCClientTimeout in milliseconds 
	private int TIMEOUT_CONNECT_MILLIS = (60 * 1000);
	private int TIMEOUT_READ_MILLIS = TIMEOUT_CONNECT_MILLIS - 5000;
	
	public DefaultHttpClient getHttpClient() 
	{
		// sets up parameters
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		params.setBooleanParameter("http.protocol.expect-continue", false);
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT_CONNECT_MILLIS);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT_READ_MILLIS);
		// registers schemes for both http and https
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		// Set verifier
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() 
		{
			@Override
			public boolean verify(String hostname, SSLSession session) 
			{
				return true;
			}
		});
		SocketFactory sslSocketFactory = new EasySSLSocketFactory();
		registry.register(new Scheme("https", sslSocketFactory, 443));
		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient(manager, params);
		return defaultHttpClient;
	}
	
	/**
	 * 
	 * @param strPostURL
	 * @param strParamToPost
	 * @param headers
	 * @return
	 */
	public InputStream sendPOSTRequest(String strPostURL, String strParamToPost,String strsoapActionUrl) 
	{
		try {
			BaseWA.writeIntoLog("\n--------------------------------------------------------");
			BaseWA.writeIntoLog("\n Posting Time: " + new Date().toString());
			BaseWA.writeIntoLog("\n URL: " + strPostURL);
			BaseWA.writeIntoLog("\n SoapAction: " + strsoapActionUrl);
			BaseWA.writeIntoLog("\n Request: " + strParamToPost);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		strPostURL = strPostURL.replace(" ", "%20");
		DefaultHttpClient defaultHttpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(strPostURL);
		httpPost.addHeader("Content-Type", "text/xml; charset=utf-8");
		
		LogUtils.infoLog("Service Url is ", strPostURL);
		LogUtils.infoLog("Service Request Param is ", strParamToPost);
		if(AppConstants.Cookie != null && !AppConstants.Cookie.equalsIgnoreCase(""))
		{
			httpPost.addHeader(AppConstants.CookieTag, AppConstants.Cookie);
			LogUtils.errorLog("HttpEntity setting Cookie Value is ", ""+AppConstants.Cookie);
		}
		if(strsoapActionUrl != null && strsoapActionUrl.length() > 0)
		{
			httpPost.addHeader("SOAPAction", strsoapActionUrl);
		}
		InputStream inputStream = null;
		
		try 
		{
			if(strParamToPost != null)
				httpPost.setEntity(new StringEntity(strParamToPost));
			HttpResponse response = defaultHttpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			
			String strCookie = "";
			if(response.getAllHeaders() != null && response.getAllHeaders().length > 0)
			{	
				Header[] headerValues = response.getAllHeaders();
				for (int i = 0; i < headerValues.length; i++) {
					String strValue = headerValues[i].getValue();
					if(strValue.contains("aeroID"))
						headerValues[i] = null;
				}
				
				for (int i = 0; i < headerValues.length; i++) {
					if(headerValues[i] != null)
					{
						String strHeader = headerValues[i].getName();
						LogUtils.errorLog("HttpEntity getting Header Name is ", ""+strHeader);
						if(strHeader.equalsIgnoreCase(AppConstants.SetCookieTag))
						{
							String strValue = headerValues[i].getValue();
							if(strValue != null && !strValue.equalsIgnoreCase("") && strValue.contains(";"))
								strCookie = strValue.split(";")[0];
							else
								strCookie = strValue;
							LogUtils.infoLog("HttpEntity getting SetCookie Value is ", ""+strCookie);
						}
					}
					}
			}
			if(!strCookie.equalsIgnoreCase("") 
					&& (AppConstants.Cookie== null 
						|| (AppConstants.Cookie != null && AppConstants.Cookie.equalsIgnoreCase(""))))
			{
				AppConstants.Cookie = strCookie;
			}
			inputStream = entity.getContent();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return inputStream;
	}
	
	/**
	 * 
	 * @param strGetURL
	 * @param headers
	 * @return 
	 */
	public InputStream sendGETRequest(String strGetURL) 
	{
		try {
			BaseWA.writeIntoLog("\n--------------------------------------------------------");
			BaseWA.writeIntoLog("\n Posting Time: GETRequest" + new Date().toString());
			BaseWA.writeIntoLog("\n URL: " + strGetURL);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		strGetURL = strGetURL.replace(" ", "%20");
		DefaultHttpClient defaultHttpClient = getHttpClient();
		HttpGet httpGet = new HttpGet(strGetURL);
		InputStream inputStream = null;
		
		try 
		{
			HttpResponse response = defaultHttpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
		}
		catch (Exception e)
		{
		}
		return inputStream;
	}
}
