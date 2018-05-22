package com.winit.airarabia.adapters;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winit.airarabia.R;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;
import com.winit.airarabia.utils.CalendarUtility;

public class FlightSheduleAdapter extends BaseAdapter
{
	private Context context;
	private Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs;
	public FlightSheduleAdapter(Context context,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs)
	{
		this.context = context;
		this.vecOriginDestinationOptionDOs = vecOriginDestinationOptionDOs;
	}
	@Override
	public int getCount() {
		if(vecOriginDestinationOptionDOs != null && vecOriginDestinationOptionDOs.size() > 0)
			return vecOriginDestinationOptionDOs.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.flight_schedule_names_item, null);
			viewHolder.tvFlightNo = (TextView)convertView.findViewById(R.id.tvFlightNo);
			viewHolder.tvFlightDepartureTime = (TextView)convertView.findViewById(R.id.tvFlightDepartureTime);
//			viewHolder.tvFlightDepartureDate = (TextView)convertView.findViewById(R.id.tvFlightDepartureDate);
			viewHolder.tvFlightOperationTime = (TextView)convertView.findViewById(R.id.tvFlightOperationTime);
			viewHolder.tvFlightArrivalTime = (TextView)convertView.findViewById(R.id.tvFlightArrivalTime);
//			viewHolder.tvFlightArrivalDate = (TextView)convertView.findViewById(R.id.tvFlightArrivalDate);

			convertView.setTag(viewHolder);
		}
		else
			viewHolder = (ViewHolder)convertView.getTag();

		viewHolder.tvFlightNo .setTypeface(AppConstants.typefaceOpenSansSemiBold);
		viewHolder.tvFlightDepartureTime .setTypeface(AppConstants.typefaceOpenSansSemiBold);
		viewHolder.tvFlightOperationTime.setTypeface(AppConstants.typefaceOpenSansSemiBold);
		viewHolder.tvFlightArrivalTime.setTypeface(AppConstants.typefaceOpenSansSemiBold);
		
		FlightSegmentDO flightSegmentDO = vecOriginDestinationOptionDOs.get(position).vecFlightSegmentDOs.get(0);

		viewHolder.tvFlightNo.setText(flightSegmentDO.flightNumber);
		viewHolder.tvFlightDepartureTime.setText(/*context.getString(R.string.Dep)+" "+*/CalendarUtility.getScheduleFlightTimeFromDate(flightSegmentDO.departureDateTime));
//		viewHolder.tvFlightDepartureDate.setText(CalendarUtility.getScheduleFlightDateFromDate(flightSegmentDO.ScheduleValidStartDate));
		viewHolder.tvFlightArrivalTime.setText(/*context.getString(R.string.Arr)+" "+*/CalendarUtility.getScheduleFlightTimeFromDate(flightSegmentDO.arrivalDateTime));
//		viewHolder.tvFlightArrivalDate.setText(CalendarUtility.getScheduleFlightDateFromDate(flightSegmentDO.ScheduleValidEndDate));

		if(flightSegmentDO.operationTimeFri.equalsIgnoreCase(AppConstants.TRUE)
				&& flightSegmentDO.operationTimeSat.equalsIgnoreCase(AppConstants.TRUE)
				&& flightSegmentDO.operationTimeSun.equalsIgnoreCase(AppConstants.TRUE)
				&& flightSegmentDO.operationTimeMon.equalsIgnoreCase(AppConstants.TRUE)
				&& flightSegmentDO.operationTimeTue.equalsIgnoreCase(AppConstants.TRUE)
				&& flightSegmentDO.operationTimeWeds.equalsIgnoreCase(AppConstants.TRUE)
				&& flightSegmentDO.operationTimeThur.equalsIgnoreCase(AppConstants.TRUE))
			viewHolder.tvFlightOperationTime.setText(context.getString(R.string.Daily));
		else
		{
			String strDays = "";
			if(flightSegmentDO.operationTimeFri.equalsIgnoreCase(AppConstants.TRUE))
				if(strDays.equalsIgnoreCase(""))
					strDays = context.getString(R.string.Friday);
				else
					strDays = context.getString(R.string.Friday);
			if(flightSegmentDO.operationTimeSat.equalsIgnoreCase(AppConstants.TRUE))
				if(strDays.equalsIgnoreCase(""))
					strDays = context.getString(R.string.Saturday);
				else
					strDays = strDays+","+context.getString(R.string.Saturday);
			if(flightSegmentDO.operationTimeSun.equalsIgnoreCase(AppConstants.TRUE))
				if(strDays.equalsIgnoreCase(""))
					strDays = context.getString(R.string.Sunday);
				else
					strDays = strDays+","+context.getString(R.string.Sunday);
			if(flightSegmentDO.operationTimeMon.equalsIgnoreCase(AppConstants.TRUE))
				if(strDays.equalsIgnoreCase(""))
					strDays = context.getString(R.string.Monday);
				else
					strDays = strDays+","+context.getString(R.string.Monday);
			if(flightSegmentDO.operationTimeTue.equalsIgnoreCase(AppConstants.TRUE))
				if(strDays.equalsIgnoreCase(""))
					strDays = context.getString(R.string.Tuesday);
				else
					strDays = strDays+","+context.getString(R.string.Tuesday);
			if(flightSegmentDO.operationTimeWeds.equalsIgnoreCase(AppConstants.TRUE))
				if(strDays.equalsIgnoreCase(""))
					strDays = context.getString(R.string.Wednessday);
				else
					strDays = strDays+","+context.getString(R.string.Wednessday);
			if(flightSegmentDO.operationTimeThur.equalsIgnoreCase(AppConstants.TRUE))
				if(strDays.equalsIgnoreCase(""))
					strDays = context.getString(R.string.Thursday);
				else
					strDays = strDays+","+context.getString(R.string.Thursday);

			viewHolder.tvFlightOperationTime.setText(strDays);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvFlightNo,tvFlightDepartureTime/*,tvFlightDepartureDate*/,tvFlightOperationTime,
		tvFlightArrivalTime/*,tvFlightArrivalDate*/;
	}
}