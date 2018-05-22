package com.winit.airarabia.webaccess;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

import android.content.Context;
import android.os.Environment;

import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.parsers.AirportsDataParser;
import com.winit.airarabia.parsers.BaseHandler;
import com.winit.airarabia.utils.NetworkUtility;

public class BaseWA implements HttpListener
{
	private WebAccessListener listener;
	private Context mContext;
	
	public BaseWA(Context mContext, WebAccessListener webAccessListener)
	{
		this.mContext = mContext;
		listener = webAccessListener;
	}
	
	/**
	 * Method to start downloading the data.
	 * @param method
	 * @param parameters
	 * @return boolean
	 */
	public boolean startDataDownload(ServiceMethods method, String parameters, String srvUrlType)
	{
		if(NetworkUtility.isNetworkConnectionAvailable(mContext))
		{
			HTTPSimulator downloader = new HTTPSimulator(this, method, parameters,srvUrlType);
			downloader.start();
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean startDataDownload(ServiceMethods method, String parameters, String srvUrlType, String requestedURLType)
	{
		if(NetworkUtility.isNetworkConnectionAvailable(mContext))
		{
			HTTPSimulator downloader;
			if (requestedURLType.equalsIgnoreCase("")) {
				
				downloader = new HTTPSimulator(this, method, parameters,srvUrlType);
			}
			else
				downloader = new HTTPSimulator(this, method, parameters,srvUrlType,requestedURLType);
			downloader.start();
			
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean startDataDownload(ServiceMethods method, int rawSource)
	{
		if(rawSource == 0)
		{
			HTTPSimulator downloader = new HTTPSimulator(this, method, rawSource,"");
			downloader.start();
		}
		else
		{
			HTTPSimulator downloader = new HTTPSimulator(this, method, rawSource,"");
			downloader.start();
		}
		return true;
	}

	class HTTPSimulator extends Thread
	{
		HttpListener listener;
		ServiceMethods method;
		String parameters;
		String srvUrlType;
		int rawSource;
		String requestedURLType = "";
		public HTTPSimulator(HttpListener listener, ServiceMethods method, String parameters, String srvUrlType, String requestedURLType)
		{
			this.listener = listener;
			this.method = method;
			this.parameters = parameters;
			this.srvUrlType = srvUrlType;
			this.requestedURLType = requestedURLType;
		}
		public HTTPSimulator(HttpListener listener, ServiceMethods method, String parameters, String srvUrlType)
		{
			this.listener = listener;
			this.method = method;
			this.parameters = parameters;
			this.srvUrlType = srvUrlType;
		}
		public HTTPSimulator(HttpListener listener, ServiceMethods method, int rawSource,String key)
		{
			this.listener = listener;
			this.method = method;
			this.rawSource = rawSource;
		}
		
		@Override
		public void run()
		{
			Response response = new Response(method, true, "Unable to connect server, please try again.");
			InputStream isResponse = null;
			
			try {
				if(parameters == null)
				{
					isResponse = mContext.getResources().openRawResource(rawSource);
				}
				else if(!requestedURLType.equalsIgnoreCase(""))
					isResponse = new RestClient().sendRequest(method,parameters,srvUrlType,requestedURLType);
				else
				isResponse = new RestClient().sendRequest(method,parameters,srvUrlType);
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
			try 
			{
				if(isResponse != null)
				{
					response = new Response(method, true, "There is some technical problem while processing your request.");
					if(method == ServiceMethods.AIR_PORT_SDATA)
					{
						AirportsDataParser airportsDataParser = new AirportsDataParser(mContext);
						airportsDataParser.getAirportsData(isResponse);
						if(airportsDataParser.getData() != null)
						{
							response.isError = false;
							response.data = airportsDataParser.getData();
						}
						else
						{
							response.isError = true;
						}
					}
					else
					{
						/** 
						 * for saving to sd card
						 */
//						if(method == ServiceMethods.AIR_BAGGAGE_DETAILS)
//						{
							writeIntoLog("\n\n Response: ", isResponse);
//						}
						/** saved to sd card */
						
						BaseHandler handler = BaseHandler.getParser(method);
						if(handler != null)
						{
							SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

//							if(method == ServiceMethods.AIR_BAGGAGE_DETAILS)
								parser.parse(new InputSource(new FileInputStream(AppConstants.DIR_PATH+"AirArabiaServiceLog.xml")), handler);
//							else
//								parser.parse(isResponse, handler);
								
							Object handlerData =handler.getData(); 
							if(handlerData != null)
							{
								response.isError = false;
								response.data = handlerData;
							}
							else
							{
								response.isError = true;
							}
						}
					}
				}
				else 
				{
				}
			} 
			catch (Exception e2) 
			{
				e2.printStackTrace();
			}
			finally
			{
		    	try
		    	{
			    	if(isResponse != null)
			    	{
			    		isResponse.close();
			    		isResponse = null;
			    	}
		    	}catch(Exception e3){
		    		e3.printStackTrace();
				}
			}
			
			listener.onResponseReceived(response);
		}
	}
	
	/**
	 * Method to perform operation after receiving the response
	 */
	@Override
	public void onResponseReceived(Response response) 
	{
		this.respondWithData(response);
	}
	
	/**
	 * Method to respond for the data
	 * @param data
	 */
	protected void respondWithData(Response data)
	{
	    listener.dataDownloaded(data);
	}
	
	
	
	/**
	 * For convert InputStream to String
	 */
	public static void writeIntoLog(String str) throws IOException
	{
		try
		{
			deleteLogFile(Environment.getExternalStorageDirectory().toString()+"/AirArabiaLog.xml");
			FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/AirArabiaLog.xml", true);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(str.getBytes());
			
			bos.flush();
			bos.close();
			fos.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	 }
	public static void writeIntoLog(String str, InputStream is) throws IOException
	{
		try
		{
			 BufferedInputStream bis = new BufferedInputStream(is);
			 
			 FileOutputStream fosOrderFile = new FileOutputStream(AppConstants.DIR_PATH+"AirArabiaServiceLog.xml");
			 BufferedOutputStream bossOrderFile = new BufferedOutputStream(fosOrderFile);
			 deleteLogFile(Environment.getExternalStorageDirectory().toString()+"/AirArabiaLog.xml");
			 FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/AirArabiaLog.xml", true);
			 BufferedOutputStream bos = new BufferedOutputStream(fos);
			 
			 bos.write(str.getBytes());
			 
			 byte byt[] = new byte[1024];
			 int noBytes;
			 
			 while((noBytes = bis.read(byt)) != -1)
			 {	 
				 bossOrderFile.write(byt,0,noBytes);
				 bos.write(byt,0,noBytes);
			 }
			 
			 bossOrderFile.flush();
			 bossOrderFile.close();
			 fosOrderFile.close();
			 bos.flush();
			 bos.close();
			 fos.close();
			 bis.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	 }
	public static void deleteLogFile(String path)
	{
		try
		{
			File file = new File(path);
			if(file.exists())
			{
				long sizeInMB = file.length()/1048576;
				if(sizeInMB >= 100)
					file.delete();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}