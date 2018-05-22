package com.winit.airarabia.webaccess;


public class Response 
{
	public ServiceMethods method;
	public boolean isError;
	public Object data;
	
	public Response(ServiceMethods method, boolean isError, Object data)
	{
		this.method = method;
		this.isError = isError;
		this.data = data;
	}
}
