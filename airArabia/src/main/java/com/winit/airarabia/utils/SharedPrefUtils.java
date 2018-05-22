package com.winit.airarabia.utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.winit.airarabia.AirArabiaApp;
//import com.winit.airarabia.GCMIntentService;
import com.winit.airarabia.objects.KeyValue;

public class SharedPrefUtils 
{
	private SharedPreferences preferences;
	private SharedPreferences.Editor edit;
	public static final int DATA_TYPE_INT 	 = 101;
	public static final int DATA_TYPE_BOOL 	 = 102;
	public static final int DATA_TYPE_FLOAT  = 103;
	public static final int DATA_TYPE_STRING = 104;
	public static final int DATA_TYPE_LONG 	 = 105;
	public static final String gcmId		 = 	"gcmId";
	
	/**
	 * This method returns Vector<KeyValue> which contains key-value pairs retrieved from SharedPreference
//	 * @param ctx
	 * @param prefName
	 */
	public SharedPrefUtils(Context context)
	{
		preferences		=	PreferenceManager.getDefaultSharedPreferences(context);
		edit			=	preferences.edit();
	}
	
	public SharedPrefUtils()
	{
		 
	}
	
	public static Vector<KeyValue> getVales(Context ctx,String prefName)
	{
		if(ctx == null)
			ctx = AirArabiaApp.mContext;
		
		SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		Map<String,?> keyMap 	= pref.getAll();
		Set<String> keySet 		=keyMap.keySet();
		
		Vector<KeyValue> vKeyValues = new Vector<KeyValue>();
		
		for(String key:keySet)
		{
			KeyValue value = new KeyValue();
			value.key 	= key;
			
			value.value = pref.getString(key, "");
			
			vKeyValues.add(value);
		}
		
		return vKeyValues;
	}
	public String getStringFromPreference(String strKey,String defaultValue )
	{
		return preferences.getString(strKey, defaultValue);
	}
	public void saveStringInPreference(String strKey,String strValue)
	{
		edit.putString(strKey, strValue);
	}
	/**
	 * This method returns value of a key in String type 
	 * @param ctx
	 * @param prefName
	 * @param key
	 */
	public static String getKeyValue(Context ctx, String prefName, String key)
	{
		if(ctx == null)
			ctx = AirArabiaApp.mContext;
		
		String value = "";
		
		SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		value = pref.getString(key, "");
		return value;
	}
//	public static boolean getKeyValueBoolean(Context ctx, String prefName, String key)
//	{
//		if(ctx == null)
//			ctx = AirArabiaApp.mContext;
//		
//		boolean value = false;
//		
//		SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
//		value = pref.getBoolean(key,false);
//		
//		return value;
//	}
	
	/**
	 * This method returns value of a key in String type 
	 * @param ctx
	 * @param prefName
	 * @param key
	 */
	public static String getKeyValues_Decrypted(Context ctx, String prefName, String key)
	{
		String value = "";
		if(ctx == null)
			ctx = AirArabiaApp.mContext;
		
		SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		value = pref.getString(key, "");
		return value;
	}
	/**
	 * This method returns value of a key in String type 
	 * @param ctx
	 * @param prefName
	 * @param key
	 * @param defaultValue
	 */
	public static String getKeyValue(Context ctx, String prefName, String key, String defaultValue)
	{
		if(ctx == null)
			ctx = AirArabiaApp.mContext;
		
		String value = "";
		
		SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		value = pref.getString(key, defaultValue);
		
		if(value.equalsIgnoreCase(""))
			value = defaultValue;
		
		return value;
	}
	public void commitPreference()
	{
		edit.commit();
	}

	/**
	 * This method stores Values in SharePreference
	 * @param ctx
	 * @param prefName
	 * @param vecKeyValue
	 */
	public static void setValues(Context ctx,String prefName,Vector<KeyValue> vecKeyValue)
	{
		if(ctx == null)
			ctx = AirArabiaApp.mContext;
		
		SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		for(int i=0;i<vecKeyValue.size();i++)
		{
			KeyValue value = vecKeyValue.get(i);
			switch(value.dataType)
			{
				case DATA_TYPE_INT:
								editor.putInt(value.key, StringUtils.getInt(value.value));	
								break;
				case DATA_TYPE_BOOL: 
								editor.putBoolean(value.key, Boolean.parseBoolean(value.value));
								break;
				case DATA_TYPE_FLOAT: 
								editor.putFloat(value.key, Float.parseFloat(value.value));
								break;
				case DATA_TYPE_STRING:
								editor.putString(value.key,value.value);
								break;
				case DATA_TYPE_LONG: 
								editor.putLong(value.key, Long.parseLong(value.value));
								break;
				default:editor.putString(value.key,value.value);
			}
		}
		
		editor.commit();
	}
	/**
	 * This method Values in SharePreference
	 * @param ctx
	 * @param prefName
	 * @param value
	 */
	public static void setValue(Context ctx,String prefName,KeyValue value)
	{
		if(ctx == null)
			ctx = AirArabiaApp.mContext;
		
		SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		switch(value.dataType)
		{
			case DATA_TYPE_INT:
							editor.putInt(value.key, StringUtils.getInt(value.value));	
							break;
			case DATA_TYPE_BOOL: 
							editor.putBoolean(value.key, Boolean.parseBoolean(value.value));
							break;
			case DATA_TYPE_FLOAT: 
							editor.putFloat(value.key, Float.parseFloat(value.value));
							break;
			case DATA_TYPE_STRING: 
							editor.putString(value.key,value.value);
							break;
			case DATA_TYPE_LONG: 
							editor.putLong(value.key, Long.parseLong(value.value));
							break;
			default:editor.putString(value.key,value.value);
		}
		
		editor.commit();
	}
	/**
	 * This method Clears the SharedPreference
	 * @param ctx
	 * @param prefName
	 */
	public static void clearValues(Context ctx,String prefName)
	{
		if(ctx == null)
			ctx = AirArabiaApp.mContext;
		
		SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * This method Clears the key value
	 * @param ctx
	 * @param prefName
	 */
	public static void clearKeyValues(Context ctx,String prefName,String key)
	{
		if(ctx == null)
			ctx = AirArabiaApp.mContext;
		
		SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(key);
		editor.commit();
	}
	public static void saveArrayInPreference(ArrayList<String> array, String arrayName,Context c ) {   
	    SharedPreferences prefs = c.getSharedPreferences("preferencename", 0);  
	    SharedPreferences.Editor editor = prefs.edit();  
	    editor.putInt(arrayName +"_size", array.size());  
	    for(int i=0;i<array.size();i++)  
	        editor.putString(arrayName + "_" + i, array.get(i)); 
	 
	    editor.commit();
	} 
	public static ArrayList<String> loadArrayFromPreference(String arrayName,Context c ) {  
	    SharedPreferences prefs = c.getSharedPreferences("preferencename", 0);  
	    int size = prefs.getInt(arrayName + "_size", 0);  
	    ArrayList<String> arrayList = new ArrayList<String>();  
	    for(int i=0;i<size;i++)  
	    	arrayList.add(prefs.getString(arrayName + "_" + i, null));  
	    return arrayList;  
	}
	
}
