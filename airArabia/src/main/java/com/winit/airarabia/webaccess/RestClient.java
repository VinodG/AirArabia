package com.winit.airarabia.webaccess;

import java.io.IOException;
import java.io.InputStream;

import com.winit.airarabia.common.AppConstants;

public class RestClient 
{
	/**
	 * Method to send the request to the server.
	 * @param method
	 * @param parameters
	 * @return InputStream
	 * @throws IOException
	 */
	public InputStream sendRequest(ServiceMethods method, String parameters, String srvUrlType) throws IOException
	{
		int reqType = ServiceURLs.getRequestType(method);
		if(reqType == AppConstants.GET)
			return new HttpHelper().sendGETRequest(ServiceURLs.getRequestedURL(method, srvUrlType));
		else if(reqType == AppConstants.POST)
			return new HttpHelper().sendPOSTRequest(ServiceURLs.getRequestedURL(method, srvUrlType),parameters,ServiceURLs.getSoapAction(method));
		
		return null;
	}
	public InputStream sendRequest(ServiceMethods method, String parameters, String srvUrlType,String requestedURLType) throws IOException
	{
		int reqType = ServiceURLs.getRequestType(method);
		if(reqType == AppConstants.GET)
			return new HttpHelper().sendGETRequest(ServiceURLs.getRequestedURL(method, srvUrlType));
		else if(reqType == AppConstants.POST)
			if(!requestedURLType.equalsIgnoreCase(""))
				return new HttpHelper().sendPOSTRequest(ServiceURLs.getRequestedURLForLOGIN(method, srvUrlType,requestedURLType),parameters,ServiceURLs.getSoapAction(method));
			else
				return new HttpHelper().sendPOSTRequest(ServiceURLs.getRequestedURL(method, srvUrlType),parameters,ServiceURLs.getSoapAction(method));
		
		return null;
	}
}
