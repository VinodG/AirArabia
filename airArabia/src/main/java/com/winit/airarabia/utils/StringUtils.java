package com.winit.airarabia.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils 
{
	/**
	 * This method returns an int value
	 * @param str
	 */
	public static int getInt(String str)
	{
		int value = 0;
		
		if(str == null || str.equalsIgnoreCase(""))
			return value;
		
		try
		{
			value = Integer.parseInt(str);
		}
		catch (Exception e)
		{
		}
		return value;
	}
	/**
	 * This method returns an String value
	 * @param str
	 */
	public static String getString(boolean str)
	{
		String value = "";
		try
		{
			value = String.valueOf((str));
		}
		catch (Exception e)
		{
		}
		return value;
	}
	/**
	 * This method returns an String value
	 * @param str
	 */
	public static String getString(int str)
	{
		String value = "";
		try
		{
			value = String.valueOf((str));
		}
		catch (Exception e)
		{
		}
		return value;
	}
	
	/**
	 * This method returns an String value
	 * @param floatValue
	 */
	public static String getStringFromFloat(float floatValue)
	{
		String value = "";
		try
		{
			value = String.valueOf((floatValue));
		}
		catch (Exception e)
		{
		}
		
		if(!value.equalsIgnoreCase(""))
		{
			if(value.split("\\.").length == 1)
				return value+".00";
			else
			{
				if(value.split("\\.")[1].length() == 2)
					return value;
				else if(value.split("\\.")[1].length() == 1)
					return value+"0";
				else if(value.split("\\.")[1].length() > 2)
					return value.split("\\.")[0]+"."+value.split("\\.")[1].substring(0, 2);
			}
		}
		return value;
	}
	
	/**
	 * This method returns an String value
	 * @param floatValue
	 */
	public static String getStringFromDouble(double floatValue)
	{
		String value = "";
		try
		{
			value = String.valueOf(floatValue);
		}
		catch (Exception e)
		{
		}
		
		if(!value.equalsIgnoreCase(""))
		{
			if(value.split("\\.").length == 1)
				return value+".00";
			else
			{
				if(value.split("\\.")[1].length() == 2)
					return value;
				else if(value.split("\\.")[1].length() == 1)
					return value+"0";
				else if(value.split("\\.")[1].length() > 2)
					return value.split("\\.")[0]+"."+value.split("\\.")[1].substring(0, 2);
			}
		}
		return value;
	}
	
	/**
	 * This method returns double value after rounding it till n decimal numbers
	 * @param double value and integer Places
	 */
	
	public static double roundDouble(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	
	/**
	 * This method returns an String value
	 * @param floatValue
	 */
	public static String getStrFloatFromString(String value)
	{
		try {
			if(!value.equalsIgnoreCase(""))
			{
				if(value.split("\\.").length == 1)
					return value+"";
				else
				{
					if(value.split("\\.")[1].length() == 2)
						return value;
					else if(value.split("\\.")[1].length() == 1)
						return value+"0";
					else if(value.split("\\.")[1].length() > 2)
						return value.split("\\.")[0]+"."+value.split("\\.")[1].substring(0, 2);
				}
			}
			else
			{
				return "0.00";
			}
		} catch (Exception e) {
			value = "0";
		}
		
		return value;
	}
	
	/**
	 * This method returns matcher if given String is a valid email
	 * @param string
	 */
	public static Matcher isValidEmail(String string)
	{
	   Matcher matcher;
	  
	    String expression =  "^[\\w\\.-]{2,}+@([\\ww\\-]{2,}+\\.)+[A-Z]{2,4}$";  
	  
	    CharSequence inputStr = string;  
	    Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);  
	    matcher = pattern.matcher(inputStr);  
	    return matcher;
	}
	
	/**
	 * This Method returns matcher if given String is valid password
	 * @param string
	 */
	public static Matcher isValidPassword(String string)
	{
		Matcher matcher;
		String expression = "(?=.{7,})(?=.*\\d)(?=.*[a-z])[a-zA-Z0-9]*";
		CharSequence inString = string;
		Pattern pattern = Pattern.compile(expression);
		matcher = pattern.matcher(inString);
		
		return matcher;
	}
	
	/**
	 * This method returns float value
	 * @param string
	 */
	public static float getFloat(String string) 
	{
		float value = 0;
		
		if(string == null || string.equalsIgnoreCase(""))
			return value;
		
		try
		{
			value = Float.parseFloat(string);
		}
		catch(Exception e)
		{
		}
		
		return value;
	}
	
	
	/**
	 * This method returns long value
	 * @param string
	 */
	public static long getLong(String string) 
	{
		long value = 0;
		
		if(string == null || string.equalsIgnoreCase(""))
			return value;
		
		try
		{
			value = Long.parseLong(string);
		}
		catch(Exception e)
		{
		}
		
		return value;
	}

	
	/**
	 * This method returns int value
	 * @param str
	 */
	public static int toInt(String str)
	{
		int value = -1;
		
		if(str == null || str.equalsIgnoreCase(""))
			return value;
		
		try
		{
			value = Integer.parseInt(str);
		}
		catch (Exception e)
		{
		}
		return value;
	}
	/**
	 * This method returns a String value
	 * @param checkString
	 */
	public static String checkString(String checkString)
	{
		return checkString != null ? checkString : "";
	}
	
	/**
	 * This method returns a String value with commas
	 * @param fareString
	 */
	public static String includeCommasInFare(String fareString)
	{
		String Amount = "";
		try {
			long amountln = Long.parseLong(fareString);
			int i=0;
			int lenght= 0;

			lenght = fareString.length();
			StringBuffer Amountbuf = new StringBuffer(fareString);

			if(amountln > 999)
			{
				for(i=lenght ; i > 0 ; i = i-2)
				{
					if(i==lenght)
					{
						i= i-3;
						Amountbuf = Amountbuf.insert(i, ",");
					}
					else
					{
						Amountbuf = Amountbuf.insert(i, ",");
					}
				}
			}

			Amount = Amountbuf.toString();
			
		} catch (Exception e) {
			return fareString;
		}
		
		return Amount;
	}
	
	public static String convertStreamToString(InputStream inputStream)
			   throws IOException {
			  // Initialize variables
			  String responce = "";
			  
			  if (inputStream != null) {
				  
				  Writer writer = new StringWriter();
				  char[] buffer = new char[1024];
				  try {
					  // Reader
					  Reader reader = new BufferedReader(new InputStreamReader(
							  inputStream, "UTF-8"));
					  int n;
					  while ((n = reader.read(buffer)) != -1) {
						  writer.write(buffer, 0, n);
					  }
					  responce = writer.toString();
					  writer.close();
				  } finally {
				  }

				  return responce;
			  } else {
				  return "";
			  }
	}
	
	/**
	 * This method returns float value
	 * @param string
	 */
	public static String getStrFloatDiffFromString(String stringAmount1, String stringAmount2) 
	{
		double value1 = 0;
		double value2 = 0;
		double valueTotal = 0;
		String stringAmountTotal = "";
		
		if(stringAmount1 == null || stringAmount2.equalsIgnoreCase(""))
			value1 = 0;
		else
		{
			try
			{
				value1 = Double.parseDouble(stringAmount1);
			}
			catch(Exception e)
			{
			}
		}
		
		if(stringAmount2 == null || stringAmount2.equalsIgnoreCase(""))
			value2 = 0;
		else
		{
			try
			{
				value2 = Double.parseDouble(stringAmount2);
			}
			catch(Exception e)
			{
			}
		}
		
		valueTotal = value1 - value2;
	    double roundOff = (double)Math.round(valueTotal*100)/100;
		stringAmountTotal = roundOff+"";
		if(!stringAmountTotal.equalsIgnoreCase(""))
		{
			if(stringAmountTotal.split("\\.").length == 1)
				return stringAmountTotal+".00";
			else
			{
				if(stringAmountTotal.split("\\.")[1].length() == 2)
					return stringAmountTotal;
				else if(stringAmountTotal.split("\\.")[1].length() == 1)
					return stringAmountTotal+"0";
				else if(stringAmountTotal.split("\\.")[1].length() > 2)
					return stringAmountTotal.split("\\.")[0]+"."+stringAmountTotal.split("\\.")[1].substring(0, 2);
			}
		}
		else
		{
			return "0.00";
		}
		return stringAmountTotal;
	}
	
	/**
	 * This method returns float value
	 * @param string
	 */
	public static double getDouble(String string) 
	{
		double value = 0;
		
		if(string == null || string.equalsIgnoreCase(""))
			return value;
		
		try
		{
			value = Double.parseDouble(string);
		}
		catch(Exception e)
		{
		}
		
		return value;
	}
}
