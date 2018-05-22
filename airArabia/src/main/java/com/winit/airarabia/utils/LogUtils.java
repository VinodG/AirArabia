package com.winit.airarabia.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Environment;
import android.util.Log;

public class LogUtils
{
	public static boolean isLogEnable = true;
	public static String SERVICE_LOG_TAG = "service log";
	public static void errorLog(String tag, String msg)
	{
		if(isLogEnable)
		{
			if(msg != null)
				Log.e(tag, msg);
			else
				Log.e(tag, "null");
		}
	}
	
	public static void debugLog(String tag, String msg)
	{
		if(isLogEnable)
		{
			if(msg != null)
				Log.d(tag, msg);
			else
				Log.d(tag, "null");
		}
	}
	
	public static void infoLog(String tag, String msg)
	{
		if(isLogEnable)
			Log.i(tag, msg);
		else
			Log.i(tag, "null");
	}
	
	public static void printMessage(String msg)
	{
		if(isLogEnable)
			System.out.println(msg);
	}
	
	public static void setLogEnable(boolean isEnable)
	{
		isLogEnable = isEnable;
	}
	/**
	 * This method stores InputStream data into a file at specified location
	 * @param is
	 */
	public static void convertResponseToFile(InputStream is, String fileName) throws IOException
	{
		 BufferedInputStream bis = new BufferedInputStream(is);
		 FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/"+fileName+".txt");
		 BufferedOutputStream bos = new BufferedOutputStream(fos);
		 
		 byte byt[] = new byte[1024];
		 int noBytes;
		 
		 while((noBytes = bis.read(byt)) != -1)
			 bos.write(byt,0,noBytes);
		 
		 bos.flush();
		 bos.close();
		 fos.close();
		 bis.close();
	 }
	/**
	 * This method stores data in String into a file at specified location
	 * @param is
	 */
	
	
	/** This method will read data from the inputStream and return as StringBuffer
	 * @param inpStrData
	 */
	public static StringBuffer getDataFromInputStream(InputStream inpStrData)
	{
		if(inpStrData != null)
		{
			try
			{
				BufferedReader brResp = new BufferedReader(new InputStreamReader(inpStrData));
				String stringTemporaryVariable;
				StringBuffer sbResp = new StringBuffer();
				
				//Converts response as a StringBuffer
				while((stringTemporaryVariable = brResp.readLine()) != null)
					sbResp.append(stringTemporaryVariable); 
				brResp.close();
				inpStrData.close();
				return sbResp;
			}
			catch(Exception e)
			{
				LogUtils.errorLog("LogUtils", "Exception occured in getDataFromInputStream(): "+e.toString());
			}
		}
		return null;
	}
	public static String getExceptionTrace(Exception e)
	{
		if(e != null && e.getMessage() != null)
		{
			StringBuffer sbf = new StringBuffer(e.getMessage());
			StackTraceElement[] elements = e.getStackTrace();
			for(StackTraceElement element : elements)
			{
				sbf.append("\n"+element.toString());
			}
			
			return sbf.toString();
		}
		else
			return "";
	}
	
	public static void writeStringinFile(String string,String name)
	{
		try
		{
			FileOutputStream fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/AirArabiaErr"+"log.txt",true);
			DataOutputStream out =  new DataOutputStream(fileOutputStream);
			out.writeBytes("\n===============================================\n");
			out.writeBytes(string);
			out.writeBytes("\n===============================================");
			out.close();
		} 
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
