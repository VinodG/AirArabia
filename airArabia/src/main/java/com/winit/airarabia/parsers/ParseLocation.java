package com.winit.airarabia.parsers;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.winit.airarabia.utils.StringUtils;

public class ParseLocation {

	private String Locaddress, lat, lang, response;
	private LatLng latLng;

	public void parsing(String loc) {

		String url = "http://maps.googleapis.com/maps/api/geocode/json?address="
				+ loc + "&sensor=false";
		try {
			
			
			response = new DefaultHttpClient().execute(new HttpGet(url),
					new BasicResponseHandler());
			JSONObject jsonObject = new JSONObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("results");
			JSONObject jsonObject2 = jsonArray.getJSONObject(0);
			Locaddress = jsonObject2.getString("formatted_address");
			JSONObject geometryobject = jsonObject2.getJSONObject("geometry");
			JSONObject locationobject = geometryobject
					.getJSONObject("location");
			lat = locationobject.getString("lat");
			lang = locationobject.getString("lng");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public String getloc(){
		return Locaddress;
	}
	public LatLng getlatlng(){
		latLng=new LatLng(StringUtils.getDouble(lat), StringUtils.getDouble(lang));
		return latLng;
	}
}