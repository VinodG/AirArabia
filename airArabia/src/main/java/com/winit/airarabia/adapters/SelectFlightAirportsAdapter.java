//package com.winit.airarabia.adapters;
//
//import java.util.Vector;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.winit.airarabia.R;
//import com.winit.airarabia.SelectFlightActivity;
//import com.winit.airarabia.TotalPriceActivity;
//import com.winit.airarabia.common.AppConstants;
//import com.winit.airarabia.objects.AirAvailabilityDO;
//import com.winit.airarabia.objects.BookingFlightDO;
//import com.winit.airarabia.objects.FareDO;
//import com.winit.airarabia.objects.FlightSegmentDO;
//import com.winit.airarabia.objects.OriginDestinationInformationDO;
//import com.winit.airarabia.objects.OriginDestinationOptionDO;
//import com.winit.airarabia.objects.PricedItineraryDO;
//import com.winit.airarabia.utils.CalendarUtility;
//import com.winit.airarabia.utils.StringUtils;
//
//public class SelectFlightAirportsAdapter extends BaseAdapter {
//
//	private Context con;
//	private BookingFlightDO bookingFlightDO;
//	private Vector<OriginDestinationInformationDO> vecOriginDestInfoDOs = new Vector<OriginDestinationInformationDO>();
//	private FareDO fare;
//	private Vector<PricedItineraryDO> vecPricedItineraryDOsPromoFare, vecPricedItineraryDOsFlexiFare;
//	private boolean isDeparture = false;
//	private boolean isModifyBook = false,isReturn = false;
//
//	public SelectFlightAirportsAdapter(Context bookFlight,AirAvailabilityDO airAvailabilityDO,BookingFlightDO bookingFlightDO,boolean isDeparture,boolean isModifyBook,boolean isReturn/*,boolean isFlexi*/) 
//	{
//		this.con = bookFlight;
//		this.vecOriginDestInfoDOs = airAvailabilityDO.vecOriginDestinationInformationDOs;
//		this.bookingFlightDO = bookingFlightDO;
//		this.vecPricedItineraryDOsPromoFare = airAvailabilityDO.vecPricedItineraryDOsPromoFare;
//		this.vecPricedItineraryDOsFlexiFare = airAvailabilityDO.vecPricedItineraryDOsFlexiFare;
//		this.isDeparture = isDeparture;
//		this.isModifyBook = isModifyBook;
//		this.isReturn = isReturn;
//	}
//
//	@Override
//	public int getCount() 
//	{
//		if(vecOriginDestInfoDOs != null && vecOriginDestInfoDOs.size() > 0)
//			return vecOriginDestInfoDOs.size();
//		else
//			return 0;
//	}
//
//	@Override
//	public Object getItem(int position) 
//	{
//		return position;
//	}
//
//	@Override
//	public long getItemId(int position)
//	{
//		return position;
//	}
//
//	public void refreshPrice(Vector<PricedItineraryDO> vecPricedItineraryDOsPromoFare, Vector<PricedItineraryDO> vecPricedItineraryDOsFlexiFare/*, boolean isFlexi*/)
//	{
//		this.vecPricedItineraryDOsPromoFare = vecPricedItineraryDOsPromoFare;
//		this.vecPricedItineraryDOsFlexiFare = vecPricedItineraryDOsFlexiFare;
//		notifyDataSetChanged();
//	}
//
//	public void refreshFlight()
//	{
//		this.vecOriginDestInfoDOs =  new Vector<OriginDestinationInformationDO>();
//		notifyDataSetChanged();
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) 
//	{
//		ViewHolder viewHolder = null;
//		OriginDestinationInformationDO odiDO = vecOriginDestInfoDOs.get(position);
//		if(convertView == null)
//		{
//			viewHolder = new ViewHolder();
//			convertView = LayoutInflater.from(con).inflate(R.layout.select_flight_list_item, null);
//			viewHolder.llFligheName = (LinearLayout) convertView.findViewById(R.id.llFligheName);
//			viewHolder.llFligheNo = (LinearLayout) convertView.findViewById(R.id.llFligheNo);
//			viewHolder.llFligheStops = (LinearLayout) convertView.findViewById(R.id.llFligheStops);
//			viewHolder.llFlightListBottom = (LinearLayout) convertView.findViewById(R.id.llFlightListBottom);
//			viewHolder.tvPromoFare = (TextView) convertView.findViewById(R.id.tvPromoFare);
//			viewHolder.tvFlexiFare = (TextView) convertView.findViewById(R.id.tvFlexiFare);
//			viewHolder.ivPromoFareArrow = (ImageView) convertView.findViewById(R.id.ivPromoFareArrow);
//			viewHolder.ivFlexiFareArrow = (ImageView) convertView.findViewById(R.id.ivFlexiFareArrow);
//			convertView.setTag(viewHolder);
//		}
//		else
//			viewHolder = (ViewHolder)convertView.getTag();
//
//		viewHolder.llFligheName.setTag(position+"");
//		viewHolder.llFligheNo.setTag(position+"");
//		viewHolder.llFligheStops.setTag(position+"");
//		if(viewHolder.llFligheName.getTag().toString().equalsIgnoreCase(position+""))
//		{
//			viewHolder.llFligheNo.removeAllViews();
//			viewHolder.llFligheStops.removeAllViews();
//			for (int i = 0; i < odiDO.vecOriginDestinationOptionDOs.size(); i++) {
//
//				FlightSegmentDO flightSegmentDO = odiDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs.get(0);
//				LinearLayout llFLightNames = (LinearLayout)LayoutInflater.from(con).inflate(R.layout.flight_names_item, null);
//				TextView tvFlightNo = (TextView)llFLightNames.findViewById(R.id.tvFlightNo);
//				TextView tvFlightDeparture = (TextView)llFLightNames.findViewById(R.id.tvFlightDeparture);
//				TextView tvFlightArrival = (TextView)llFLightNames.findViewById(R.id.tvFlightArrival);
//				tvFlightNo.setText(flightSegmentDO.flightNumber);
//				tvFlightDeparture.setText(CalendarUtility.getBookingTimeFromDate(flightSegmentDO.departureDateTime));
//				tvFlightArrival.setText(CalendarUtility.getBookingTimeFromDate(flightSegmentDO.arrivalDateTime));
//				viewHolder.llFligheNo.addView(llFLightNames);
//				if(i>0)
//				{
//					LinearLayout llStops = (LinearLayout)LayoutInflater.from(con).inflate(R.layout.flight_names_stops_item, null);
//					TextView tvFlightStops = (TextView)llStops.findViewById(R.id.tvFlightStops);
//					tvFlightStops.setText(i+" "+con.getString(R.string.Stop)+"("+flightSegmentDO.departureAirportCode+")");
//					viewHolder.llFligheStops.addView(llStops);
//				}
//				else if(odiDO.vecOriginDestinationOptionDOs.size() <= 1)
//				{
//					LinearLayout llStops = (LinearLayout)LayoutInflater.from(con).inflate(R.layout.flight_names_stops_item, null);
//					TextView tvFlightStops = (TextView)llStops.findViewById(R.id.tvFlightStops);
//					tvFlightStops.setText(con.getString(R.string.NoStops));
//					viewHolder.llFligheStops.addView(llStops);
//				}
//			}
//		}
//		if(vecPricedItineraryDOsPromoFare != null && vecPricedItineraryDOsPromoFare.size() > 0/* && !isFlexi*/)
//		{
//			PricedItineraryDO pricedItineraryDOPromo = vecPricedItineraryDOsPromoFare.get(position);
//			if(pricedItineraryDOPromo != null 
//					&& pricedItineraryDOPromo.vecPTC_FareBreakdownDOs != null
//					&& pricedItineraryDOPromo.vecPTC_FareBreakdownDOs.size() > 0)
//			{
//				viewHolder.ivPromoFareArrow.setVisibility(View.VISIBLE);
//				fare = pricedItineraryDOPromo.vecPTC_FareBreakdownDOs.get(0).totalFare;
//				viewHolder.tvPromoFare.setTextColor(con.getResources().getColor(R.color.red));
//				viewHolder.tvPromoFare.setText(fare.currencyCode+" "+fare.amount);
//				viewHolder.tvPromoFare.setEnabled(true);
//			}
//			else
//			{
//				viewHolder.ivPromoFareArrow.setVisibility(View.GONE);
//				viewHolder.tvPromoFare.setTextColor(con.getResources().getColor(R.color.black));
//				viewHolder.tvPromoFare.setText(con.getString(R.string.NotAvailable));
//				viewHolder.tvPromoFare.setEnabled(false);
//			}
//		}
//		else
//		{
//			viewHolder.ivPromoFareArrow.setVisibility(View.GONE);
//			viewHolder.tvPromoFare.setTextColor(con.getResources().getColor(R.color.black));
//			viewHolder.tvPromoFare.setText(con.getString(R.string.NotAvailable));
//			viewHolder.tvPromoFare.setEnabled(false);
//		}
//		if(vecPricedItineraryDOsFlexiFare != null && vecPricedItineraryDOsFlexiFare.size() > 0/* && isFlexi*/)
//		{
//			PricedItineraryDO pricedItineraryDOFlexi = vecPricedItineraryDOsFlexiFare.get(position);
//			if(pricedItineraryDOFlexi != null
//					&& pricedItineraryDOFlexi.vecPTC_FareBreakdownDOs != null
//					&& pricedItineraryDOFlexi.vecPTC_FareBreakdownDOs.size() > 0)
//			{
//				viewHolder.ivFlexiFareArrow.setVisibility(View.VISIBLE);
//				fare = pricedItineraryDOFlexi.vecPTC_FareBreakdownDOs.get(0).totalFare;
//				viewHolder.tvFlexiFare.setTextColor(con.getResources().getColor(R.color.red));
//				viewHolder.tvFlexiFare.setText(fare.currencyCode+" "+fare.amount);
//				viewHolder.tvFlexiFare.setEnabled(true);
//			}
//			else
//			{
//				viewHolder.ivFlexiFareArrow.setVisibility(View.GONE);
//				viewHolder.tvFlexiFare.setTextColor(con.getResources().getColor(R.color.black));
//				viewHolder.tvFlexiFare.setText(con.getString(R.string.NotAvailable));
//				viewHolder.tvFlexiFare.setEnabled(false);
//			}
//		}
//		else
//		{
//			viewHolder.ivFlexiFareArrow.setVisibility(View.GONE);
//			viewHolder.tvFlexiFare.setTextColor(con.getResources().getColor(R.color.black));
//			viewHolder.tvFlexiFare.setText(con.getString(R.string.NotAvailable));
//			viewHolder.tvFlexiFare.setEnabled(false);
//		}
//		viewHolder.tvPromoFare.setTag(position+"");
//		viewHolder.tvPromoFare.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if(isDeparture)
//					bookingFlightDO.isFlexiOut = false;
//				else
//					bookingFlightDO.isFlexiIn = false;
//				if(!bookingFlightDO.myBookFlightDOReturn.travelType.equalsIgnoreCase("")
//						&& bookingFlightDO.requestParameterDOReturn.transactionIdentifier.equalsIgnoreCase(""))
//				{
//					OriginDestinationInformationDO odiDO = vecOriginDestInfoDOs.get(StringUtils.getInt(v.getTag().toString()));
//					OriginDestinationOptionDO originDestinationOptionDO = new OriginDestinationOptionDO();
//					originDestinationOptionDO.vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//					for (int i = 0; i < odiDO.vecOriginDestinationOptionDOs.size(); i++) {
//
//						originDestinationOptionDO.vecFlightSegmentDOs.addAll(odiDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
//					}
//					
//					
////					//new modification
////					if(originDestinationOptionDO.vecFlightSegmentDOs!= null
////							&& originDestinationOptionDO.vecFlightSegmentDOs.size() > 0)
////					AppConstants.flightRefNumberRPHListReturn = originDestinationOptionDO.vecFlightSegmentDOs.get(0).RPH;
//					
//					
//					bookingFlightDO.myODIDO = odiDO;
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.clear();
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
//					if(isModifyBook)
//						goToTotalPrice();
//					else if(isDeparture)
//						goToSelectFlight();
//				}
//				else if(!bookingFlightDO.myBookFlightDOReturn.travelType.equalsIgnoreCase("")
//						&& !bookingFlightDO.requestParameterDOReturn.transactionIdentifier.equalsIgnoreCase(""))
//				{
//					OriginDestinationInformationDO odiDO = vecOriginDestInfoDOs.get(StringUtils.getInt(v.getTag().toString()));
//					OriginDestinationOptionDO originDestinationOptionDO = new OriginDestinationOptionDO();
//					originDestinationOptionDO.vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//					for (int i = 0; i < odiDO.vecOriginDestinationOptionDOs.size(); i++) {
//
//						originDestinationOptionDO.vecFlightSegmentDOs.addAll(odiDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
//					}
//					
////					//new modification
////					if(originDestinationOptionDO.vecFlightSegmentDOs!= null
////							&& originDestinationOptionDO.vecFlightSegmentDOs.size() > 0)
////					AppConstants.flightRefNumberRPHListReturn = originDestinationOptionDO.vecFlightSegmentDOs.get(0).RPH;
//					
//					
//					bookingFlightDO.myODIDOReturn = odiDO;
//					bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.clear();
//					bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
//					goToTotalPrice();
//				}
//				else  if(bookingFlightDO.myBookFlightDOReturn.travelType.equalsIgnoreCase(""))
//				{
//					OriginDestinationInformationDO odiDO = vecOriginDestInfoDOs.get(StringUtils.getInt(v.getTag().toString()));
//					OriginDestinationOptionDO originDestinationOptionDO = new OriginDestinationOptionDO();
//					originDestinationOptionDO.vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//					for (int i = 0; i < odiDO.vecOriginDestinationOptionDOs.size(); i++) {
//
//						originDestinationOptionDO.vecFlightSegmentDOs.addAll(odiDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
//					}
//					
////					//new modification
////					if(originDestinationOptionDO.vecFlightSegmentDOs!= null
////							&& originDestinationOptionDO.vecFlightSegmentDOs.size() > 0)
////					AppConstants.flightRefNumberRPHListReturn = originDestinationOptionDO.vecFlightSegmentDOs.get(0).RPH;
//					
//					
//					bookingFlightDO.myODIDO = odiDO;
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.clear();
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
//					goToTotalPrice();
//				}
//			}
//		});
//
//		viewHolder.ivPromoFareArrow.setTag(position+"");
//		viewHolder.ivPromoFareArrow.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if(isDeparture)
//					bookingFlightDO.isFlexiOut = false;
//				else
//					bookingFlightDO.isFlexiIn = false;
//				if(!bookingFlightDO.myBookFlightDOReturn.travelType.equalsIgnoreCase("")
//						&& bookingFlightDO.requestParameterDOReturn.transactionIdentifier.equalsIgnoreCase(""))
//				{
//					OriginDestinationInformationDO odiDO = vecOriginDestInfoDOs.get(StringUtils.getInt(v.getTag().toString()));
//					OriginDestinationOptionDO originDestinationOptionDO = new OriginDestinationOptionDO();
//					originDestinationOptionDO.vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//					for (int i = 0; i < odiDO.vecOriginDestinationOptionDOs.size(); i++) {
//
//						originDestinationOptionDO.vecFlightSegmentDOs.addAll(odiDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
//					}
//					
////					//new modification
////					if(originDestinationOptionDO.vecFlightSegmentDOs!= null
////							&& originDestinationOptionDO.vecFlightSegmentDOs.size() > 0)
////					AppConstants.flightRefNumberRPHListReturn = originDestinationOptionDO.vecFlightSegmentDOs.get(0).RPH;
//					
//					
//					bookingFlightDO.myODIDO = odiDO;
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.clear();
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
//					if(isModifyBook)
//						goToTotalPrice();
//					else if(isDeparture)
//						goToSelectFlight();
//				}
//				else if(!bookingFlightDO.myBookFlightDOReturn.travelType.equalsIgnoreCase("")
//						&& !bookingFlightDO.requestParameterDOReturn.transactionIdentifier.equalsIgnoreCase(""))
//				{
//					OriginDestinationInformationDO odiDO = vecOriginDestInfoDOs.get(StringUtils.getInt(v.getTag().toString()));
//					OriginDestinationOptionDO originDestinationOptionDO = new OriginDestinationOptionDO();
//					originDestinationOptionDO.vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//					for (int i = 0; i < odiDO.vecOriginDestinationOptionDOs.size(); i++) {
//
//						originDestinationOptionDO.vecFlightSegmentDOs.addAll(odiDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
//					}
//					
////					//new modification
////					if(originDestinationOptionDO.vecFlightSegmentDOs!= null
////							&& originDestinationOptionDO.vecFlightSegmentDOs.size() > 0)
////					AppConstants.flightRefNumberRPHListReturn = originDestinationOptionDO.vecFlightSegmentDOs.get(0).RPH;
//					
//					
//					bookingFlightDO.myODIDOReturn = odiDO;
//					bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.clear();
//					bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
//					goToTotalPrice();
//				}
//				else  if(bookingFlightDO.myBookFlightDOReturn.travelType.equalsIgnoreCase(""))
//				{
//					OriginDestinationInformationDO odiDO = vecOriginDestInfoDOs.get(StringUtils.getInt(v.getTag().toString()));
//					OriginDestinationOptionDO originDestinationOptionDO = new OriginDestinationOptionDO();
//					originDestinationOptionDO.vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//					for (int i = 0; i < odiDO.vecOriginDestinationOptionDOs.size(); i++) {
//
//						originDestinationOptionDO.vecFlightSegmentDOs.addAll(odiDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
//					}
//					
////					//new modification
////					if(originDestinationOptionDO.vecFlightSegmentDOs!= null
////							&& originDestinationOptionDO.vecFlightSegmentDOs.size() > 0)
////					AppConstants.flightRefNumberRPHList = originDestinationOptionDO.vecFlightSegmentDOs.get(0).RPH;
//					
//					
//					bookingFlightDO.myODIDO = odiDO;
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.clear();
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
//					goToTotalPrice();
//				}
//			}
//		});
//
//		viewHolder.tvFlexiFare.setTag(position+"");
//		viewHolder.tvFlexiFare.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if(isDeparture)
//					bookingFlightDO.isFlexiOut = true;
//				else
//					bookingFlightDO.isFlexiIn = true;
//				if(!bookingFlightDO.myBookFlightDOReturn.travelType.equalsIgnoreCase("")
//						&& bookingFlightDO.requestParameterDOReturn.transactionIdentifier.equalsIgnoreCase(""))
//				{
//					OriginDestinationInformationDO odiDO = vecOriginDestInfoDOs.get(StringUtils.getInt(v.getTag().toString()));
//
//					OriginDestinationOptionDO originDestinationOptionDO = new OriginDestinationOptionDO();
//					originDestinationOptionDO.vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//					for (int i = 0; i < odiDO.vecOriginDestinationOptionDOs.size(); i++) {
//
//						originDestinationOptionDO.vecFlightSegmentDOs.addAll(odiDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
//					}
//					
////					//new modification
////					if(originDestinationOptionDO.vecFlightSegmentDOs!= null
////							&& originDestinationOptionDO.vecFlightSegmentDOs.size() > 0)
////					AppConstants.flightRefNumberRPHListReturn = originDestinationOptionDO.vecFlightSegmentDOs.get(0).RPH;
//					
//					
//					bookingFlightDO.myODIDO = odiDO;
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.clear();
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
//
//					if(isModifyBook)
//						goToTotalPrice();
//					else
//						goToSelectFlight();
//				}
//				else if(!bookingFlightDO.myBookFlightDOReturn.travelType.equalsIgnoreCase("")
//						&& !bookingFlightDO.requestParameterDOReturn.transactionIdentifier.equalsIgnoreCase(""))
//				{
//					OriginDestinationInformationDO odiDO = vecOriginDestInfoDOs.get(StringUtils.getInt(v.getTag().toString()));
//
//					OriginDestinationOptionDO originDestinationOptionDO = new OriginDestinationOptionDO();
//					originDestinationOptionDO.vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//					if(odiDO.vecOriginDestinationOptionDOs != null && odiDO.vecOriginDestinationOptionDOs.size() > 0)
//					for (int i = 0; i < odiDO.vecOriginDestinationOptionDOs.size(); i++) {
//
//						originDestinationOptionDO.vecFlightSegmentDOs.addAll(odiDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
//					}
//					
////					//new modification
////					if(originDestinationOptionDO.vecFlightSegmentDOs!= null
////							&& originDestinationOptionDO.vecFlightSegmentDOs.size() > 0)
////					AppConstants.flightRefNumberRPHListReturn = originDestinationOptionDO.vecFlightSegmentDOs.get(0).RPH;
//					
//					
//					bookingFlightDO.myODIDOReturn = odiDO;
//					bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.clear();
//					bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
//
//					goToTotalPrice();
//				}
//				else  if(bookingFlightDO.myBookFlightDOReturn.travelType.equalsIgnoreCase(""))
//				{
//					OriginDestinationInformationDO odiDO = vecOriginDestInfoDOs.get(StringUtils.getInt(v.getTag().toString()));
//
//					OriginDestinationOptionDO originDestinationOptionDO = new OriginDestinationOptionDO();
//					originDestinationOptionDO.vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//					for (int i = 0; i < odiDO.vecOriginDestinationOptionDOs.size(); i++) {
//
//						originDestinationOptionDO.vecFlightSegmentDOs.addAll(odiDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
//					}
//					
////					//new modification
////					if(originDestinationOptionDO.vecFlightSegmentDOs!= null
////							&& originDestinationOptionDO.vecFlightSegmentDOs.size() > 0)
////					AppConstants.flightRefNumberRPHList = originDestinationOptionDO.vecFlightSegmentDOs.get(0).RPH;
//					
//					
//					bookingFlightDO.myODIDO = odiDO;
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.clear();
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
//
//					goToTotalPrice();
//				}
//			}
//		});
//
//		viewHolder.ivFlexiFareArrow.setTag(position+"");
//		viewHolder.ivFlexiFareArrow.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if(isDeparture)
//					bookingFlightDO.isFlexiOut = true;
//				else
//					bookingFlightDO.isFlexiIn = true;
//				if(!bookingFlightDO.myBookFlightDOReturn.travelType.equalsIgnoreCase("")
//						&& bookingFlightDO.requestParameterDOReturn.transactionIdentifier.equalsIgnoreCase(""))
//				{
//					OriginDestinationInformationDO odiDO = vecOriginDestInfoDOs.get(StringUtils.getInt(v.getTag().toString()));
//
//					OriginDestinationOptionDO originDestinationOptionDO = new OriginDestinationOptionDO();
//					originDestinationOptionDO.vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//					for (int i = 0; i < odiDO.vecOriginDestinationOptionDOs.size(); i++) {
//
//						originDestinationOptionDO.vecFlightSegmentDOs.addAll(odiDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
//					}
//					
////					//new modification
////					if(originDestinationOptionDO.vecFlightSegmentDOs!= null
////							&& originDestinationOptionDO.vecFlightSegmentDOs.size() > 0)
////					AppConstants.flightRefNumberRPHListReturn = originDestinationOptionDO.vecFlightSegmentDOs.get(0).RPH;
//					
//					
//					bookingFlightDO.myODIDO = odiDO;
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.clear();
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
//
//					if(isModifyBook)
//						goToTotalPrice();
//					else
//						goToSelectFlight();
//				}
//				else if(!bookingFlightDO.myBookFlightDOReturn.travelType.equalsIgnoreCase("")
//						&& !bookingFlightDO.requestParameterDOReturn.transactionIdentifier.equalsIgnoreCase(""))
//				{
//					OriginDestinationInformationDO odiDO = vecOriginDestInfoDOs.get(StringUtils.getInt(v.getTag().toString()));
//
//					OriginDestinationOptionDO originDestinationOptionDO = new OriginDestinationOptionDO();
//					originDestinationOptionDO.vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//					for (int i = 0; i < odiDO.vecOriginDestinationOptionDOs.size(); i++) {
//
//						originDestinationOptionDO.vecFlightSegmentDOs.addAll(odiDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
//					}
//					
////					//new modification
////					if(originDestinationOptionDO.vecFlightSegmentDOs!= null
////							&& originDestinationOptionDO.vecFlightSegmentDOs.size() > 0)
////					AppConstants.flightRefNumberRPHListReturn = originDestinationOptionDO.vecFlightSegmentDOs.get(0).RPH;
//					
//					
//					bookingFlightDO.myODIDOReturn = odiDO;
//					bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.clear();
//					bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
//
//					goToTotalPrice();
//				}
//				else  if(bookingFlightDO.myBookFlightDOReturn.travelType.equalsIgnoreCase(""))
//				{
//					OriginDestinationInformationDO odiDO = vecOriginDestInfoDOs.get(StringUtils.getInt(v.getTag().toString()));
//
//					OriginDestinationOptionDO originDestinationOptionDO = new OriginDestinationOptionDO();
//					originDestinationOptionDO.vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//					for (int i = 0; i < odiDO.vecOriginDestinationOptionDOs.size(); i++) {
//
//						originDestinationOptionDO.vecFlightSegmentDOs.addAll(odiDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
//					}
//					
////					//new modification
////					if(originDestinationOptionDO.vecFlightSegmentDOs!= null
////							&& originDestinationOptionDO.vecFlightSegmentDOs.size() > 0)
////					AppConstants.flightRefNumberRPHList = originDestinationOptionDO.vecFlightSegmentDOs.get(0).RPH;
//					
//					
//					bookingFlightDO.myODIDO = odiDO;
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.clear();
//					bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
//
//					goToTotalPrice();
//				}
//			}
//		});
//		return convertView;
//	}
//
//	class ViewHolder{
//		LinearLayout llFligheName,llFligheNo,llFligheStops,llFlightListBottom;
//		TextView tvPromoFare,tvFlexiFare;
//		ImageView ivPromoFareArrow, ivFlexiFareArrow;
//	}
//
//	private void goToSelectFlight()
//	{
//		Intent inSelectFlight = new Intent(con, SelectFlightActivity.class);
//		inSelectFlight.putExtra("IS_DEPARTURE", false);
//		((Activity)con).startActivityForResult(inSelectFlight, 100);
//	}
//
//	private void goToTotalPrice()
//	{
//		Intent inSelectFlight = new Intent(con, TotalPriceActivity.class);
//		inSelectFlight.putExtra(AppConstants.RETURN, isReturn);
//		inSelectFlight.putExtra(AppConstants.MANAGE_BOOKING, isModifyBook);
//		inSelectFlight.putExtra(AppConstants.IS_DEPARTURE, isDeparture);
//		((Activity)con).startActivityForResult(inSelectFlight, 200);
//	}
//}