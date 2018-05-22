package com.winit.airarabia.busynesslayer;

import android.app.Activity;
import android.content.Context;

import com.winit.airarabia.SelectFlightOneWayFragment;
import com.winit.airarabia.SelectFlightReturnFragment;
import com.winit.airarabia.webaccess.Response;
import com.winit.airarabia.webaccess.WebAccessListener;


/** this class contains the control all over the Business Layer Classes **/
public class BaseBL implements WebAccessListener
{
	DataListener listener;
	public Context mContext;
	public boolean isFragment = false;
	public BaseBL(Context mContext,DataListener listener)
	{
		this.mContext = mContext;
		this.listener = listener;
	}
	
	@Override
	public void dataDownloaded(Response data)
	{
		if(listener != null)
		{
			try {
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(listener instanceof Activity)
				((Activity)listener).runOnUiThread(new DataRetreivedRunnable(listener, data));
			else
			{
				if(listener instanceof SelectFlightOneWayFragment)
				{
					if(((SelectFlightOneWayFragment)listener) != null && ((SelectFlightOneWayFragment)listener).getActivity() != null)
					((SelectFlightOneWayFragment)listener).getActivity().runOnUiThread(new DataRetreivedRunnable(listener, data));
				}
				else if(listener instanceof SelectFlightReturnFragment)
				{
					if(((SelectFlightReturnFragment)listener) != null && ((SelectFlightReturnFragment)listener).getActivity() != null)
					((SelectFlightReturnFragment)listener).getActivity().runOnUiThread(new DataRetreivedRunnable(listener, data));
				}
//				isFragment = true;
//				new DataRetreivedRunnable(listener, data);
			}
		}
	}
	
	/**
	 * Class to respond when data has received successfully.
	 */
	class DataRetreivedRunnable implements Runnable
	{
		DataListener listener;
		Response data;
		
		DataRetreivedRunnable(DataListener listener, Response data)
		{
			this.listener = listener;
			this.data = data;
			if(isFragment)
			listener.dataRetreived(data);
		}
		
		@Override
		public void run() 
		{
			if(!isFragment)
			listener.dataRetreived(data);
		}
	}
}
