package com.winit.airarabia.returnflight;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Vector;

import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.airarabia.BaseActivity;
import com.winit.airarabia.R;
import com.winit.airarabia.SelectFlightItemDetails;
import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirAvailabilityDO;
import com.winit.airarabia.objects.AirPriceQuoteDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.OriginDestinationInformationDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;
import com.winit.airarabia.objects.PricedItineraryDO;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.utils.LogUtils;
import com.winit.airarabia.utils.StringUtils;
import com.winit.airarabia.webaccess.Response;

public class ReturnFlightActivityNew extends BaseActivity implements
OnClickListener, DataListener {

	private LinearLayout llSelectflight, llDateReturn, llOneWayFlightView, llReturnFlightView;	
	private TextView tvDateOneWayPrev,tvDateOneWayCurrent,tvDateOneWayNext,
	tvDateReturnPrev,tvDateReturnCurrent,tvDateReturnNext;
	private Calendar curCal;
	private Calendar mCal,retCal;
	protected boolean isManageBook = false;
	private int year = 0, month = 0, day = 0;
	private Button btnBack_SelectFlight;
	private TextView tvHeadertitle_SelectFlight, tvSourceFlight1, tvDestFlight1;
	private ImageView ivmenu_SelectFlight, ivHeaderSelectFlight, ivDateOneWayPrev, ivDateOnewWayNext, ivDateReturnPrev, ivDateReturnNext ;
	private AirAvailabilityDO airAvailabilityDO,airAvailabilityDOReturn;
	private final String DATAFAIL = "DATAFAIL";
	private ImageView ivSepreatorDays;
	protected TextView tvTotalPrice, tvDepartTag, tvSelectFlightSub, tvTotalTagColan, tvTotalTag;
	protected float oneWayTotalPrice=0, returnTotalPrice=0, totalPrice=0; 

	private boolean isReturnServiceCalled,isDateChanged, isLoaderNeededToBeContinue = false;

	protected int flightCountOneWay = -1, flightCountReturn = -1;

	protected TextView tvNoDataFlightOneWay,tvNoDataFlightReturn;

	private LinearLayout llFragmentFlights, llTotalPrice;
	private TextView tvNoDataFlightAll;
	private String errorMsgPrice = "";
	private int allFlightCounts = -1;
	public int currentFlightCountPrice=0;
	private Vector<PricedItineraryDO> vecForPriceShowing = null;
	private ReturnFlightActivityNew.BCR bcr;
	
	private ListView departureList;
	private ListView arrivalList;
	public static boolean isFirstTimeSelected = false;
	private FlightOneWayAdapter adapter;
	private ReturnFlightAdapter returnFlightadapter;
	
	private Vector<OriginDestinationInformationDO> departureDos ;
	private Vector<OriginDestinationInformationDO> returningDos ;
	
	
	Vector<OriginDestinationInformationDO> vecDepartureDos ;
	private Vector<OriginDestinationInformationDO> vecReturnDos;
	
	private class BCR extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(AppConstants.HOME_CLICK))
				finish();
			if (intent.getAction().equalsIgnoreCase(AppConstants.BOOK_SUCCESS))
				finish();
		}
	}
	@Override
	protected void onDestroy() {

		super.onDestroy();
		unregisterReceiver(bcr);
	}

	@Override
	protected void onResume() {
		super.onResume();
		AppConstants.bookingFlightDO.bundleServiceID = "";
		AppConstants.bookingFlightDO.isFlexiOut = false;
		AppConstants.bookingFlightDO.isFlexiIn = false;
	}

	@Override
	public void initilize() {
		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		intentFilter.addAction(AppConstants.BOOK_SUCCESS);
		registerReceiver(bcr, intentFilter);

		btnSubmitNext.setText(getString(R.string.Continue));

		if (AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify != null
				&& AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.size() > 0)
			isManageBook = true;

		llSelectflight = (LinearLayout) layoutInflater.inflate(R.layout.return_flight_new, null);
		llMiddleBase.addView(llSelectflight, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lltop.setVisibility(View.GONE);

		departureList = (ListView) llSelectflight.findViewById(R.id.departureList);
		
		departureList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				flightCountOneWay = pos;
				adapter.refreshPos(pos);
				callServicePriceReturn( flightCountOneWay, flightCountReturn,true);
			}
		});
		
		arrivalList = (ListView) llSelectflight.findViewById(R.id.arrivalList);
		
		arrivalList .setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				flightCountReturn = pos;
				returnFlightadapter.refreshPos(pos);
				callServicePriceReturn( flightCountOneWay, flightCountReturn,true);
			}
		});
		
		llDateReturn = (LinearLayout) llSelectflight.findViewById(R.id.llDateReturn);

		tvDateOneWayPrev = (TextView) llSelectflight.findViewById(R.id.tvDateOneWayPrev);
		tvDateOneWayCurrent = (TextView) llSelectflight.findViewById(R.id.tvDateOneWayCurrent);
		tvDateOneWayNext = (TextView) llSelectflight.findViewById(R.id.tvDateOneWayNext);

		tvDateReturnPrev = (TextView) llSelectflight.findViewById(R.id.tvDateReturnPrev);
		tvDateReturnCurrent = (TextView) llSelectflight.findViewById(R.id.tvDateReturnCurrent);
		tvDateReturnNext = (TextView) llSelectflight.findViewById(R.id.tvDateReturnNext);

		ivDateOneWayPrev = (ImageView) llSelectflight.findViewById(R.id.ivDateOneWayPrev);
		ivDateOnewWayNext = (ImageView) llSelectflight.findViewById(R.id.ivDateOnewWayNext);
		ivDateReturnPrev = (ImageView) llSelectflight.findViewById(R.id.ivDateReturnPrev);
		ivDateReturnNext = (ImageView) llSelectflight.findViewById(R.id.ivDateReturnNext);

		tvSourceFlight1	 = (TextView) llSelectflight.findViewById(R.id.tvSourceFlight1);
		tvDestFlight1	 = (TextView) llSelectflight.findViewById(R.id.tvDestFlight1);
		ivSepreatorDays	 = (ImageView) llSelectflight.findViewById(R.id.ivSepreatorDays);
		tvHeadertitle_SelectFlight = (TextView) llSelectflight.findViewById(R.id.tvHeadertitle_SelectFlight);
		btnBack_SelectFlight = (Button) llSelectflight.findViewById(R.id.btn_back_SelectFlight);
		ivmenu_SelectFlight	= (ImageView) llSelectflight.findViewById(R.id.ivmenu_SelectFlight);
		ivHeaderSelectFlight= (ImageView) llSelectflight.findViewById(R.id.ivHeaderSelectFlight);
		tvTotalPrice	 = (TextView) llSelectflight.findViewById(R.id.tvTotalPrice);
		tvTotalTag	 = (TextView) llSelectflight.findViewById(R.id.tvTotalTag);
		tvTotalTagColan	 = (TextView) llSelectflight.findViewById(R.id.tvTotalTagColan);
		tvSelectFlightSub	 = (TextView) llSelectflight.findViewById(R.id.tvSelectFlightSub);
		llTotalPrice	 = (LinearLayout) llSelectflight.findViewById(R.id.llTotalPrice);

		airAvailabilityDO = new AirAvailabilityDO();
		airAvailabilityDOReturn = new AirAvailabilityDO();

		llOneWayFlightView = (LinearLayout) llSelectflight.findViewById(R.id.llOneWayFlightView);
		llReturnFlightView = (LinearLayout) llSelectflight.findViewById(R.id.llReturnFlightView);
		tvDepartTag			=(TextView) llSelectflight.findViewById(R.id.tvDepartTag);

		tvNoDataFlightOneWay			=(TextView) llSelectflight.findViewById(R.id.tvNoDataFlightOneWay);
		tvNoDataFlightReturn			=(TextView) llSelectflight.findViewById(R.id.tvNoDataFlightReturn);

		llFragmentFlights = (LinearLayout) llSelectflight.findViewById(R.id.llFragmentFlights);
		tvNoDataFlightAll =(TextView) llSelectflight.findViewById(R.id.tvNoDataFlightAll);

		btnBack_SelectFlight.setVisibility(View.INVISIBLE);

		setTypeFaceSemiBold(llSelectflight);
		tvTotalTag.setTypeface(typeFaceOpenSansLight);	
		tvTotalTagColan.setTypeface(typeFaceOpenSansLight);	
		tvDateReturnCurrent.setTypeface(null, Typeface.BOLD);
		tvDateOneWayCurrent.setTypeface(null, Typeface.BOLD);
	}

	@Override
	public void bindingControl() {

		loadData();

		btnBack_SelectFlight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				btnBack.performClick();
			}
		});
		ivmenu_SelectFlight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ivmenu.performClick();
			}
		});
		curCal = Calendar.getInstance();

		isDateChanged = false;

		if(AppConstants.bookingFlightDO != null)
		{
			updateCalendar();

			if (AppConstants.bookingFlightDO.myBookFlightDOReturn == null) {

				ivHeaderSelectFlight.setBackgroundResource(R.drawable.flight_oneside);
				ivSepreatorDays.setVisibility(View.GONE);
				llDateReturn.setVisibility(View.GONE);

				tvDateOneWayCurrent.setText(CalendarUtility.getSelectFlightDateNew(mCal,0));
				tvDateOneWayPrev.setText(CalendarUtility.getSelectFlightDateNew(mCal,-1));
				tvDateOneWayNext.setText(CalendarUtility.getSelectFlightDateNew(mCal,1));
			} else {

				ivHeaderSelectFlight.setBackgroundResource(R.drawable.flight_return_logo);
				tvDateOneWayCurrent.setText(CalendarUtility.getSelectFlightDateNew(mCal,0));
				tvDateOneWayPrev.setText(CalendarUtility.getSelectFlightDateNew(mCal,-1));
				tvDateOneWayNext.setText(CalendarUtility.getSelectFlightDateNew(mCal,1));

				tvDateReturnCurrent.setText(CalendarUtility.getSelectFlightDateNew(retCal,0));
				tvDateReturnPrev.setText(CalendarUtility.getSelectFlightDateNew(retCal,-1));
				tvDateReturnNext.setText(CalendarUtility.getSelectFlightDateNew(retCal,1));
			}

			tvSourceFlight1.setText(AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode);
			tvDestFlight1.setText(AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode);

			tvDateOneWayPrev.setOnClickListener(this);
			tvDateOneWayNext.setOnClickListener(this);
			tvDateReturnPrev.setOnClickListener(this);
			tvDateReturnNext.setOnClickListener(this);


			ivDateOneWayPrev.setOnClickListener(this);
			ivDateOnewWayNext.setOnClickListener(this);
			ivDateReturnPrev.setOnClickListener(this);
			ivDateReturnNext.setOnClickListener(this);

			if(isManageBook)
			{
				if (CalendarUtility.compareWithCurDate(mCal, curCal)) {
					if (tvDateOneWayPrev != null) {
						tvDateOneWayPrev.setAlpha(0.5f);
						tvDateOneWayPrev.setEnabled(false);
						ivDateOneWayPrev.setEnabled(false);
					}
				}
			}
			else
			{
				if(AppConstants.bookingFlightDO.myBookFlightDOReturn == null)
				{
					if (CalendarUtility.compareWithCurDate(mCal, curCal)) {
						if (tvDateOneWayPrev != null) {
							tvDateOneWayPrev.setAlpha(0.5f);
							tvDateOneWayPrev.setEnabled(false);
							ivDateOneWayPrev.setEnabled(false);
						}
					}
					llDateReturn.setVisibility(View.GONE);
				}
				else
				{
					if (CalendarUtility.compareWithCurDate(mCal, curCal)) {
						if (tvDateOneWayPrev != null) {
							tvDateOneWayPrev.setAlpha(0.5f);
							tvDateOneWayPrev.setEnabled(false);
							ivDateOneWayPrev.setEnabled(false);
						}
					}
					if (CalendarUtility.compareWithCurDate(mCal, retCal)) {
						if (tvDateOneWayNext != null) {
							tvDateOneWayNext.setAlpha(0.5f);
							tvDateOneWayNext.setEnabled(false);
							ivDateOnewWayNext.setEnabled(false);
						}
					}

					if (CalendarUtility.compareWithCurDate(retCal, curCal)) {
						if (tvDateReturnPrev != null) {
							tvDateReturnPrev.setAlpha(0.5f);
							tvDateReturnPrev.setEnabled(false);
							ivDateReturnPrev.setEnabled(false);
						}
					}
				}
			}
		}
		else
			clickHome();

		if(AppConstants.bookingFlightDO.myODIDOOneWay != null)
		{
			AppConstants.bookingFlightDO.myODIDOOneWay.mPTC_FareBreakdownDOPromo = null;
			AppConstants.bookingFlightDO.myODIDOOneWay.mPTC_FareBreakdownDOFlexi = null;
		}

		if(AppConstants.bookingFlightDO.myODIDOReturn != null)
		{
			AppConstants.bookingFlightDO.myODIDOReturn.mPTC_FareBreakdownDOPromo = null;
			AppConstants.bookingFlightDO.myODIDOReturn.mPTC_FareBreakdownDOFlexi = null;
		}

		callFlightListServiceReturn();
			
		btnSubmitNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String strMsg = validateFlight();
				if(TextUtils.isEmpty(strMsg))
					moveToFlightDetails();
				else
					showCustomDialog(ReturnFlightActivityNew.this,
							getString(R.string.Alert),
							strMsg,
							getString(R.string.Ok), "", "");
			}
		});
	}

	private String validateFlight()
	{
		String errorMsg = "";

		if(totalPrice <= 0)
			errorMsg = errorMsgPrice;
		else if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null
				&& flightCountReturn >= 0
				&& AppConstants.bookingFlightDO.myBookFlightDO != null
				&& flightCountOneWay >= 0)
			errorMsg = "";
		else if(AppConstants.bookingFlightDO.myBookFlightDOReturn == null
				&& AppConstants.bookingFlightDO.myBookFlightDO != null
				&& flightCountOneWay >= 0)
			errorMsg = "";
		else if(AppConstants.bookingFlightDO.myBookFlightDOReturn == null
				&& AppConstants.bookingFlightDO.myBookFlightDO != null
				&& flightCountOneWay < 0)
			errorMsg = getString(R.string.DepartureAirportErr);
		else if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null
				&& flightCountReturn < 0
				&& AppConstants.bookingFlightDO.myBookFlightDO != null
				&& flightCountOneWay >= 0)
			errorMsg = getString(R.string.ArrivalDateErr);
		else if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null
				&& flightCountReturn >= 0
				&& AppConstants.bookingFlightDO.myBookFlightDO != null
				&& flightCountOneWay < 0)
			errorMsg = getString(R.string.DepartureDateErr);
		else if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null
				&& flightCountReturn < 0
				&& AppConstants.bookingFlightDO.myBookFlightDO != null
				&& flightCountOneWay < 0)
			errorMsg = getString(R.string.DepartureDateErr);

		return errorMsg;
	}

	private void loadData()
	{
		String strPersons = "";
		if (AppConstants.bookingFlightDO.myBookFlightDO.adtQty > 0) {

			if (AppConstants.bookingFlightDO.myBookFlightDO.adtQty < 2)
				strPersons = AppConstants.bookingFlightDO.myBookFlightDO.adtQty + " " + getString(R.string.Adult);
			else
				strPersons = AppConstants.bookingFlightDO.myBookFlightDO.adtQty + " " + getString(R.string.Adults);
		}
		if (AppConstants.bookingFlightDO.myBookFlightDO.chdQty > 0) {

			if (AppConstants.bookingFlightDO.myBookFlightDO.chdQty < 2)
				strPersons = strPersons+", "+AppConstants.bookingFlightDO.myBookFlightDO.chdQty + " " + getString(R.string.Child);
			else
				strPersons = strPersons+", "+AppConstants.bookingFlightDO.myBookFlightDO.chdQty + " " + getString(R.string.Children);
		}
		if (AppConstants.bookingFlightDO.myBookFlightDO.infQty > 0) {

			if (AppConstants.bookingFlightDO.myBookFlightDO.infQty < 2)
				strPersons = strPersons+", "+AppConstants.bookingFlightDO.myBookFlightDO.infQty + " " + getString(R.string.Infant);
			else
				strPersons = strPersons+", "+AppConstants.bookingFlightDO.myBookFlightDO.infQty + " " + getString(R.string.Infants);
		}

		tvHeadertitle_SelectFlight.setText(strPersons);
	}
	private void moveToFlightDetails()
	{
		if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
		{
			Intent intenNext = new Intent(ReturnFlightActivityNew.this, SelectFlightItemDetails.class);
			startActivity(intenNext);
		}
		else
		{
			if(AppConstants.bookingFlightDO.myBookFlightDOReturn == null && ReturnFlightActivityNew.isFirstTimeSelected)
			{
				Intent intenNext = new Intent(ReturnFlightActivityNew.this, SelectFlightItemDetails.class);
				startActivity(intenNext);
			}
			else
			{
				showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.please_select_your_flight), getString(R.string.Ok), null, "");
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dataRetreived(Response data) {
		hideLoader();
		if (data != null) {
			switch (data.method) {
			case AIR_AVAILABILITY_RETURN:

				if (!data.isError) {
					airAvailabilityDOReturn = new AirAvailabilityDO();
					airAvailabilityDOReturn = (AirAvailabilityDO) data.data;

//					adapter.refresh(airAvailabilityDO.vecOriginDestinationInformationDOs,flightPos);
					if(airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() > 0) {
						try {
						tvNoDataFlightAll.setVisibility(View.GONE);
						departureList.setVisibility(View.VISIBLE);
						arrivalList.setVisibility(View.VISIBLE);
						tvSelectFlightSub.setVisibility(View.VISIBLE);
						
						departureDos = (Vector<OriginDestinationInformationDO>) deepCopy(airAvailabilityDOReturn.vecOriginDestinationInformationDOs);
						
//						departureDos.addAll(airAvailabilityDOReturn.vecOriginDestinationInformationDOs);
						
						for(int i = 0; i < airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() ; i++) {
							departureDos.get(i).originLocationCode = AppConstants.OriginLocation;
							departureDos.get(i).destinationLocationCode = AppConstants.DestinationLocation;
							departureDos.get(i).originLocation = AppConstants.OriginLocationName;
							departureDos.get(i).destinationLocation = AppConstants.DestinationLocationName;
							departureDos.get(i).vecOriginDestinationOptionDOs = 
									airAvailabilityDOReturn.vecOriginDestinationInformationDOs.get(0).departureVecOriginDestinationOptionDO;
							
//							departureDos.get(i).vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs = (Vector<FlightSegmentDO>)
//									deepCopy(airAvailabilityDOReturn.vecOriginDestinationInformationDOs.get(0).departureFlightSegments);
						}
						vecDepartureDos = new Vector<OriginDestinationInformationDO>();
						for(int i = 0 ;i < departureDos.size() ; i ++) {
							
							if(departureDos.get(i).departureFlightSegments!=null && departureDos.get(i).departureFlightSegments.size()>0){
								vecDepartureDos.add(departureDos.get(i));
							}
						}
						
						adapter = new FlightOneWayAdapter(this, vecDepartureDos,0);
						departureList.setAdapter(adapter);
						
//						returningDos = new Vector<OriginDestinationInformationDO> ();
						returningDos = (Vector<OriginDestinationInformationDO>) deepCopy(airAvailabilityDOReturn.vecOriginDestinationInformationDOs);
						for(int i = 0; i < airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() ; i++) {
							returningDos.get(i).originLocationCode = AppConstants.DestinationLocation;
							returningDos.get(i).destinationLocationCode = AppConstants.OriginLocation;
							returningDos.get(i).originLocation = AppConstants.DestinationLocationName;
							returningDos.get(i).destinationLocation = AppConstants.OriginLocationName;
							returningDos.get(i).vecOriginDestinationOptionDOs = 
									airAvailabilityDOReturn.vecOriginDestinationInformationDOs.get(0).arrivalVecOriginDestinationOptionDOs;
							
//							returningDos.get(i).vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs = (Vector<FlightSegmentDO>) deepCopy(airAvailabilityDOReturn.vecOriginDestinationInformationDOs.get(i).arrivalFlightSegments);
						}
						
						vecReturnDos = new Vector<OriginDestinationInformationDO>();
						for(int i = 0 ;i < returningDos.size() ; i ++) {
							
							if(returningDos.get(i).arrivalFlightSegments!=null && returningDos.get(i).arrivalFlightSegments.size()>0){
								vecReturnDos.add(returningDos.get(i));
							}
						}
						
						returnFlightadapter = new ReturnFlightAdapter(this, returningDos,0);
						arrivalList.setAdapter(returnFlightadapter);
						
						airAvailabilityDO.vecOriginDestinationInformationDOs = vecDepartureDos;
						airAvailabilityDOReturn.vecOriginDestinationInformationDOs = vecReturnDos;
						
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						llTotalPrice.setVisibility(View.VISIBLE);
						btnSubmitNext.setVisibility(View.VISIBLE);
						
//						tvTotalPrice.setText(airAvailabilityDOReturn.vecPricedItineraryDOs.get(0).totalFare.amount);
						
						tvTotalPrice.setText(AppConstants.CurrencyCodeAfterExchange+" "+
								updateCurrencyByFactor(airAvailabilityDOReturn.vecPricedItineraryDOs.get(0).totalFare.amount+"", 0));

						updateListReturn(airAvailabilityDOReturn);
						updateList(airAvailabilityDOReturn);
//						allFlightCounts = airAvailabilityDO.vecOriginDestinationInformationDOs.size();
						
//						callServicePriceReturn(0, 0 ,true);
					}
					else if(airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() == 0) {
						tvNoDataFlightAll.setVisibility(View.VISIBLE);
						departureList.setVisibility(View.GONE);
						arrivalList.setVisibility(View.GONE);
						tvSelectFlightSub.setVisibility(View.GONE);
						llTotalPrice.setVisibility(View.GONE);
						btnSubmitNext.setVisibility(View.GONE);
					}
				} else {
					if(data.data instanceof String)
					{
						if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
							showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
						else showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);

						hideLoader();
					}
					else
					{
						showCustomDialog(ReturnFlightActivityNew.this,
								getString(R.string.Alert),
								getString(R.string.TechProblem),
								getString(R.string.Ok), "", DATAFAIL);
						hideLoader();
					}
				}
				break;
			case AIR_PRICEQUOTE:

				if (!data.isError) {
					AirPriceQuoteDO airPriceQuoteDO = new AirPriceQuoteDO();
					airPriceQuoteDO = (AirPriceQuoteDO) data.data;

					if(airPriceQuoteDO != null)
					{
						if (airPriceQuoteDO.vecPricedItineraryDOs != null
								&& airPriceQuoteDO.vecPricedItineraryDOs.size() > 0) 
						{
							errorMsgPrice = "";
							
							tvTotalPrice.setText(AppConstants.CurrencyCodeAfterExchange+" "+
									updateCurrencyByFactor(airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.amount+"", 0));
							
							if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
							{
								airAvailabilityDOReturn.vecPricedItineraryDOsPromoFare = new Vector<PricedItineraryDO>();

								airAvailabilityDOReturn.vecPricedItineraryDOsPromoFare.addAll(airPriceQuoteDO.vecPricedItineraryDOs);

								if(airAvailabilityDOReturn.vecPricedItineraryDOsPromoFare.get(0).vecPTC_FareBreakdownDOs != null)
								{
									AppConstants.bookingFlightDO.myODIDOReturn.mPTC_FareBreakdownDOPromo = airPriceQuoteDO.vecPricedItineraryDOs.get(0).vecPTC_FareBreakdownDOs.get(0);
									
//											airAvailabilityDOReturn
//											.vecPricedItineraryDOsPromoFare.get(0)
//											.vecPTC_FareBreakdownDOs.get(0);

									AppConstants.bookingFlightDO.myODIDOReturn.arlBundledServiceDOs = airPriceQuoteDO.vecPricedItineraryDOs.get(0).arlBundledServiceDOs;
									
//									AppConstants.bookingFlightDO.myODIDOReturn.arlBundledServiceDOs = 
//											airAvailabilityDOReturn
//											.vecPricedItineraryDOsPromoFare.get(0)
//											.arlBundledServiceDOs;

									LogUtils.errorLog("Oneway Price Called",AppConstants.bookingFlightDO.myODIDOReturn.mPTC_FareBreakdownDOPromo.totalFare.amount+"" );
									returnTotalPrice = StringUtils.getFloat(AppConstants.bookingFlightDO.myODIDOReturn.mPTC_FareBreakdownDOPromo.totalFare.amount);
//									updateTotalPrice(true);
								}
							}
							else
							{
								airAvailabilityDOReturn.vecPricedItineraryDOsPromoFare = new Vector<PricedItineraryDO>();

								airAvailabilityDO.vecPricedItineraryDOsPromoFare.addAll(airPriceQuoteDO.vecPricedItineraryDOs);

								if(airAvailabilityDO.vecPricedItineraryDOsPromoFare != null 
										&& airAvailabilityDO.vecPricedItineraryDOsPromoFare.get(0).vecPTC_FareBreakdownDOs != null)
								{
									//									AppConstants.bookingFlightDO.myODIDOOneWay.mPTC_FareBreakdownDOPromo = 
									//											airAvailabilityDO
									//											.vecPricedItineraryDOsPromoFare.get(0)
									//											.vecPTC_FareBreakdownDOs.get(0);
									AppConstants.bookingFlightDO.myODIDOOneWay.mPTC_FareBreakdownDOPromo = 
											airAvailabilityDO
											.vecPricedItineraryDOsPromoFare.get(0)
											.vecPTC_FareBreakdownDOs.get(0);

									vecForPriceShowing = (Vector<PricedItineraryDO>) airAvailabilityDO
											.vecPricedItineraryDOsPromoFare.clone();

									AppConstants.bookingFlightDO.myODIDOOneWay.arlBundledServiceDOs = 
											airAvailabilityDO
											.vecPricedItineraryDOsPromoFare.get(0)
											.arlBundledServiceDOs;

									LogUtils.errorLog("Oneway Price Called",AppConstants.bookingFlightDO.myODIDOOneWay.mPTC_FareBreakdownDOPromo.totalFare.amount+"" );
									oneWayTotalPrice = StringUtils.getFloat(AppConstants.bookingFlightDO.myODIDOOneWay.mPTC_FareBreakdownDOPromo.totalFare.amount);
//									updateTotalPrice(true);
								}
							}
						}
						else 
						{
							if(airPriceQuoteDO.ErrorMsg.equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
								showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
							else if(!TextUtils.isEmpty(airPriceQuoteDO.ErrorMsg))
							{
								errorMsgPrice = airPriceQuoteDO.ErrorMsg;
								showCustomDialog(getApplicationContext(), getString(R.string.Alert), errorMsgPrice, getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
							}
							else
								showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);

							//							if(flightCountOneWay == 0 && flightCountReturn == 0)
							//							{
							//								if(!isReturnServiceCalled)
							//								{
							//									flightCountOneWay = -1;
							//									frFlightOneWay.DateSelectionOneWayFlight(airAvailabilityDO,0);
							//								}
							//								else
							//								{
							//									flightCountOneWay = -1;
							//									flightCountReturn = -1;
							//									frFlightOneWay.DateSelectionOneWayFlight(airAvailabilityDO,flightCountOneWay);
							//									frFlightReturn.DateSelectionReturnFlight(airAvailabilityDOReturn,flightCountOneWay);
							//								}
							//							}
//							updateTotalPrice(false);
						}
						if(AppConstants.bookingFlightDO.myBookFlightDOReturn==null && allFlightCounts>1 && 
								currentFlightCountPrice >= 0 && currentFlightCountPrice < allFlightCounts)
						{
							callServicePriceOneWay(currentFlightCountPrice,false);
							currentFlightCountPrice++;
							isLoaderNeededToBeContinue = true;

						}
						else if(AppConstants.bookingFlightDO.myBookFlightDOReturn==null && allFlightCounts>0 && 
								currentFlightCountPrice==allFlightCounts)
						{
//							frFlightOneWay.DateSelectionOneWayFlight(vecForPriceShowing);
							hideLoader();
						}
					}
					else 
					{
						if(data.data instanceof String)
						{
							String strMsg = (String)data.data;
							if(strMsg.equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
								showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
							else if(!TextUtils.isEmpty(strMsg))
							{
								errorMsgPrice = strMsg;
								showCustomDialog(getApplicationContext(), getString(R.string.Alert), strMsg, getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
							}
							else showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
						}
						updateTotalPrice(false);
					}
				}
				else 
				{
					showCustomDialog(ReturnFlightActivityNew.this,
							getString(R.string.Alert), getString(R.string.TechProblem),
							getString(R.string.Ok), "", DATAFAIL);
					updateTotalPrice(false);
				}
				
				
				
				if(AppConstants.bookingFlightDO.myBookFlightDOReturn==null && allFlightCounts>1 && 
						currentFlightCountPrice==allFlightCounts)
				{
					if(!isLoaderNeededToBeContinue)
						hideLoader();
					else
						isLoaderNeededToBeContinue = false;
				}
				else if(AppConstants.bookingFlightDO.myBookFlightDOReturn!=null)
					hideLoader();
				
			default:
				break;
			}
		} else {
			if(data.data instanceof String)
			{
				if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
					showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
				else showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
			}
			else
			{
				showCustomDialog(ReturnFlightActivityNew.this,
						getString(R.string.Alert), getString(R.string.TechProblem),
						getString(R.string.Ok), "", DATAFAIL);
				hideLoader();
			}
		}
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.tvDateOneWayPrev) {

			isReturnServiceCalled = false;

			//			if (checkLangArabic())
			//				clickRightArrow();
			//			else {
			//				ivFlighClickedLeft = ivFlightselectleft;
			//				ivFlighClickedRight = ivFlighselecttright;
			clickLeftArrow();
			//			}
		} else if (v.getId() == R.id.tvDateOneWayNext) {

			isReturnServiceCalled = false;

			//			if (checkLangArabic()) {
			//				clickLeftArrow();
			//			} else
			clickRightArrow();
		}
		else if (v.getId() == R.id.tvDateReturnPrev) {

			isReturnServiceCalled = true;

			//			if (checkLangArabic()) {
			//				clickRightArrowReturn();
			//			} else
			clickLeftArrowReturn();
		}
		else if (v.getId() == R.id.tvDateReturnNext) {

			isReturnServiceCalled = true;

			//			if (checkLangArabic()) {
			//				clickLeftArrowReturn();
			//			} else
			clickRightArrowReturn();
		}
		else if (v.getId() == R.id.ivDateOneWayPrev) {

			tvDateOneWayPrev.performClick();
		}
		else if (v.getId() == R.id.ivDateOnewWayNext) {
			tvDateOneWayNext.performClick();
		}
		else if (v.getId() == R.id.ivDateReturnPrev) {
			tvDateReturnPrev.performClick();
		}
		else if (v.getId() == R.id.ivDateReturnNext) {
			tvDateReturnNext.performClick();
		}

	}

	private void updateCalendar() {
		if(AppConstants.bookingFlightDO != null && AppConstants.bookingFlightDO.myBookFlightDO != null)
		{
			if (AppConstants.bookingFlightDO.myBookFlightDO != null) {
				year = CalendarUtility.getYearFromDate(AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime);
				month = CalendarUtility.getMonthFromDate(AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime) - 1;
				day = CalendarUtility.getDayFromDate(AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime);

				mCal = Calendar.getInstance();
				mCal.set(year, month, day);

				if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
				{
					int ryear = CalendarUtility.getYearFromDate(AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime);
					int rmonth = CalendarUtility.getMonthFromDate(AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime) - 1;
					int rday = CalendarUtility.getDayFromDate(AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime);
					retCal = Calendar.getInstance();
					retCal.set(ryear, rmonth, rday);
				}
			} else {
				year = CalendarUtility.getYearFromDate(AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime);
				month = CalendarUtility.getMonthFromDate(AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime) - 1;
				day = CalendarUtility.getDayFromDate(AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime);

				mCal = Calendar.getInstance();
				mCal.set(year, month, day);

				int ryear = CalendarUtility.getYearFromDate(AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime);
				int rmonth = CalendarUtility.getMonthFromDate(AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime) - 1;
				int rday = CalendarUtility.getDayFromDate(AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime);
				retCal = Calendar.getInstance();
				retCal.set(ryear, rmonth, rday);
			}
		}
		else
			clickHome();
	}

	// New UpdateList.. 
	private void updateListReturn(AirAvailabilityDO airAvailabilityDO) {
		
		if (!airAvailabilityDO.echoToken.equalsIgnoreCase("")
				&& !airAvailabilityDO.primaryLangID.equalsIgnoreCase("")
				&& !airAvailabilityDO.sequenceNmbr.equalsIgnoreCase("")
				&& !airAvailabilityDO.transactionIdentifier.equalsIgnoreCase("")
				&& !airAvailabilityDO.version.equalsIgnoreCase(""))
		{
			AppConstants.bookingFlightDO.requestParameterDOReturn.srvUrlType = AppConstants.bookingFlightDO.myBookFlightDOReturn.srvUrlType;
			AppConstants.bookingFlightDO.requestParameterDOReturn.echoToken = airAvailabilityDO.echoToken;
			AppConstants.bookingFlightDO.requestParameterDOReturn.primaryLangID = airAvailabilityDO.primaryLangID;
			AppConstants.bookingFlightDO.requestParameterDOReturn.sequenceNmbr = airAvailabilityDO.sequenceNmbr;
			AppConstants.bookingFlightDO.requestParameterDOReturn.transactionIdentifier = airAvailabilityDO.transactionIdentifier;
			AppConstants.bookingFlightDO.requestParameterDOReturn.version = airAvailabilityDO.version;
			
			AppConstants.bookingFlightDO.requestParameterDO.srvUrlType = AppConstants.bookingFlightDO.myBookFlightDOReturn.srvUrlType;
			AppConstants.bookingFlightDO.requestParameterDO.echoToken = airAvailabilityDO.echoToken;
			AppConstants.bookingFlightDO.requestParameterDO.primaryLangID = airAvailabilityDO.primaryLangID;
			AppConstants.bookingFlightDO.requestParameterDO.sequenceNmbr = airAvailabilityDO.sequenceNmbr;
			AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airAvailabilityDO.transactionIdentifier;
			AppConstants.bookingFlightDO.requestParameterDO.version = airAvailabilityDO.version;
			
			flightCountOneWay = 0;
			flightCountReturn = 0;
			
		}
	}	
	


	private void updateList(AirAvailabilityDO airAvailabilityDO) {
		if (!airAvailabilityDO.echoToken.equalsIgnoreCase("")
				&& !airAvailabilityDO.primaryLangID.equalsIgnoreCase("")
				&& !airAvailabilityDO.sequenceNmbr.equalsIgnoreCase("")
				&& !airAvailabilityDO.transactionIdentifier.equalsIgnoreCase("")
				&& !airAvailabilityDO.version.equalsIgnoreCase(""))
		{
			{
				AppConstants.bookingFlightDO.requestParameterDOReturn.srvUrlType = AppConstants.bookingFlightDO.myBookFlightDOReturn.srvUrlType;
				AppConstants.bookingFlightDO.requestParameterDOReturn.echoToken = airAvailabilityDO.echoToken;
				AppConstants.bookingFlightDO.requestParameterDOReturn.primaryLangID = airAvailabilityDO.primaryLangID;
				AppConstants.bookingFlightDO.requestParameterDOReturn.sequenceNmbr = airAvailabilityDO.sequenceNmbr;
				AppConstants.bookingFlightDO.requestParameterDOReturn.transactionIdentifier = airAvailabilityDO.transactionIdentifier;
				AppConstants.bookingFlightDO.requestParameterDOReturn.version = airAvailabilityDO.version;

				if (AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs != null
						&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0)
					AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.clear();

				OriginDestinationOptionDO originDestinationOptionDO = new OriginDestinationOptionDO();

				originDestinationOptionDO.vecFlightSegmentDOs = new Vector<FlightSegmentDO>();

				if(airAvailabilityDO.vecOriginDestinationInformationDOs != null 
						&& airAvailabilityDO.vecOriginDestinationInformationDOs.size() > 0
						&& airAvailabilityDO.vecOriginDestinationInformationDOs.get(0).vecOriginDestinationOptionDOs != null
						&& airAvailabilityDO.vecOriginDestinationInformationDOs.get(0).vecOriginDestinationOptionDOs.size() > 0)
					for (int i = 0; i < airAvailabilityDO.vecOriginDestinationInformationDOs.get(0).vecOriginDestinationOptionDOs.size(); i++) {

						originDestinationOptionDO.vecFlightSegmentDOs
						.addAll(airAvailabilityDO.vecOriginDestinationInformationDOs
								.get(0).vecOriginDestinationOptionDOs
								.get(i).vecFlightSegmentDOs);
					}
				AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.add(originDestinationOptionDO);

//				frFlightReturn.DateSelectionReturnFlight(airAvailabilityDOReturn,0);

				llFragmentFlights.setVisibility(View.VISIBLE);
//				llTotalPrice.setVisibility(View.VISIBLE);
				tvSelectFlightSub.setVisibility(View.VISIBLE);
				tvNoDataFlightAll.setVisibility(View.GONE);
				tvNoDataFlightReturn.setVisibility(View.GONE);

				if(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs != null
						&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0
						&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs != null
						&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs.size() > 0)
				{
					flightCountReturn = 0;
					tvNoDataFlightReturn.setVisibility(View.GONE);

//					if(flightCountOneWay == -1)
//					{
//						updateTotalPrice(false);
//						showCustomDialog(ReturnFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
//					}
				}
				else
				{
					flightCountReturn = -1;
					/*if(flightCountOneWay == -1)
					{
						llFragmentFlights.setVisibility(View.GONE);
						llTotalPrice.setVisibility(View.GONE);
						tvSelectFlightSub.setVisibility(View.GONE);
						tvNoDataFlightAll.setVisibility(View.VISIBLE);
						showCustomDialog(ReturnFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
					}
					else
					{
						tvSelectFlightSub.setVisibility(View.GONE);
						tvNoDataFlightReturn.setVisibility(View.VISIBLE);
						showCustomDialog(ReturnFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
					}*/
//					updateTotalPrice(false);
				}
			}
		} 
		else
		{
			{
				flightCountReturn = -1;
//				frFlightReturn.DateSelectionReturnFlight(airAvailabilityDOReturn,-1);
				AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.clear();

				/*if(flightCountOneWay == -1)
				{
					llFragmentFlights.setVisibility(View.GONE);
					llTotalPrice.setVisibility(View.GONE);
					tvSelectFlightSub.setVisibility(View.GONE);
					tvNoDataFlightAll.setVisibility(View.VISIBLE);
					showCustomDialog(ReturnFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
				}
				else
				{
					llFragmentFlights.setVisibility(View.VISIBLE);
//					llTotalPrice.setVisibility(View.VISIBLE);
					tvSelectFlightSub.setVisibility(View.GONE);
					tvNoDataFlightReturn.setVisibility(View.VISIBLE);
					showCustomDialog(ReturnFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
				}*/
			}
//			updateTotalPrice(false);
		}
	}


	private void clickLeftArrow() {

		mCal.add(Calendar.DAY_OF_MONTH, -1);
		tvDateOneWayCurrent.setText(CalendarUtility.getSelectFlightDateNew(mCal,0));
		tvDateOneWayPrev.setText(CalendarUtility.getSelectFlightDateNew(mCal,-1));
		tvDateOneWayNext.setText(CalendarUtility.getSelectFlightDateNew(mCal,1));

		if(AppConstants.bookingFlightDO.myBookFlightDO != null
				&& CalendarUtility.compareWithCurDate(mCal, curCal)) {
			tvDateOneWayPrev.setAlpha(0.5f);
			tvDateOneWayPrev.setEnabled(false);
			ivDateOneWayPrev.setEnabled(false);
		}
		else
		{
			tvDateOneWayPrev.setAlpha(1f);
			tvDateOneWayPrev.setEnabled(true);
			ivDateOneWayPrev.setEnabled(true);
		}
		if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null
				&& CalendarUtility.compareWithCurDate(mCal, retCal)) {
			tvDateOneWayNext.setAlpha(0.5f);
			tvDateOneWayNext.setEnabled(false);
			ivDateOnewWayNext.setEnabled(false);

			tvDateReturnPrev.setAlpha(0.5f);
			tvDateReturnPrev.setEnabled(false);
			ivDateReturnPrev.setEnabled(false);
		}
		else
		{
			tvDateOneWayNext.setAlpha(1f);
			tvDateOneWayNext.setEnabled(true);
			ivDateOnewWayNext.setEnabled(true);

			tvDateReturnPrev.setAlpha(1f);
			tvDateReturnPrev.setEnabled(true);			
			ivDateReturnPrev.setEnabled(true);			
		}
		updateBookFlightDates();

		isDateChanged = true;

		AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();

//		callFlightListServiceOneWay();
		callFlightListServiceReturn();
	}

	private void clickRightArrow() {

		mCal.add(Calendar.DAY_OF_MONTH, 1);
		tvDateOneWayCurrent.setText(CalendarUtility.getSelectFlightDateNew(mCal,0));
		tvDateOneWayPrev.setText(CalendarUtility.getSelectFlightDateNew(mCal,-1));
		tvDateOneWayNext.setText(CalendarUtility.getSelectFlightDateNew(mCal,1));

		updateBookFlightDates();

		isDateChanged = true;

		AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();
//		callFlightListServiceOneWay();
		callFlightListServiceReturn();
		
		tvDateOneWayPrev.setAlpha(1f);
		tvDateOneWayPrev.setEnabled(true);
		ivDateOneWayPrev.setEnabled(true);
		if(!isManageBook)
		{
			if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null
					&& CalendarUtility.compareWithCurDate(mCal, retCal)) {
				tvDateOneWayNext.setAlpha(0.5f);
				tvDateOneWayNext.setEnabled(false);
				ivDateOnewWayNext.setEnabled(false);

				tvDateReturnPrev.setAlpha(0.5f);
				tvDateReturnPrev.setEnabled(false);
				ivDateReturnPrev.setEnabled(false);
			}
		}
	}

	private void clickLeftArrowReturn() {

		retCal.add(Calendar.DAY_OF_MONTH, -1);
		tvDateReturnCurrent.setText(CalendarUtility.getSelectFlightDateNew(retCal,0));
		tvDateReturnPrev.setText(CalendarUtility.getSelectFlightDateNew(retCal,-1));
		tvDateReturnNext.setText(CalendarUtility.getSelectFlightDateNew(retCal,1));

		updateBookFlightDates();

		isDateChanged = true;
		isReturnServiceCalled = true;

		AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();

		callFlightListServiceReturn(true);
		LogUtils.errorLog("DateSelectionReturnFlight", "called");

		if(isManageBook)
		{
			if (CalendarUtility.compareWithCurDate(retCal, curCal)) {
				tvDateReturnPrev.setAlpha(0.5f);
				tvDateReturnPrev.setEnabled(false);
				ivDateReturnPrev.setEnabled(false);
			}
		}
		else
		{
			if(AppConstants.bookingFlightDO.myBookFlightDO != null
					&& CalendarUtility.compareWithCurDate(mCal, retCal)) {
				tvDateReturnPrev.setAlpha(0.5f);
				tvDateReturnPrev.setEnabled(false);
				ivDateReturnPrev.setEnabled(false);

				tvDateOneWayNext.setAlpha(0.5f);
				tvDateOneWayNext.setEnabled(false);
				ivDateOnewWayNext.setEnabled(false);
			}
			else
			{
				tvDateOneWayNext.setAlpha(1f);
				tvDateOneWayNext.setEnabled(true);
				ivDateOnewWayNext.setEnabled(true);
			}
		}
	}

	private void clickRightArrowReturn() {

		retCal.add(Calendar.DAY_OF_MONTH, 1);
		tvDateReturnCurrent.setText(CalendarUtility.getSelectFlightDateNew(retCal,0));
		tvDateReturnPrev.setText(CalendarUtility.getSelectFlightDateNew(retCal,-1));
		tvDateReturnNext.setText(CalendarUtility.getSelectFlightDateNew(retCal,1));

		updateBookFlightDates();

		isDateChanged = true;
		isReturnServiceCalled = true;

		AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();

		callFlightListServiceReturn(true);
		LogUtils.errorLog("DateSelectionReturnFlight", "called");
		if (tvDateReturnPrev != null) {
			tvDateReturnPrev.setAlpha(1f);
			tvDateReturnPrev .setEnabled(true);
			ivDateReturnPrev .setEnabled(true);
		}
		if(!isManageBook)
		{
			if(AppConstants.bookingFlightDO.myBookFlightDO != null
					&& CalendarUtility.compareWithCurDate(retCal, curCal)) {
				tvDateReturnNext.setAlpha(0.5f);
				tvDateReturnNext.setEnabled(false);
				ivDateReturnNext.setEnabled(false);
			}
			else if(!tvDateOneWayNext.isEnabled())
			{
				tvDateOneWayNext.setAlpha(1f);
				tvDateOneWayNext.setEnabled(true);
				ivDateOnewWayNext.setEnabled(true);
			}
		}
	}

	private void updateBookFlightDates() {

		if (AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
		{
			AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime = CalendarUtility.getBookingDate(retCal);

			AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime = CalendarUtility.getBookingDate(mCal);
		}
		else {
			AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime = CalendarUtility.getBookingDate(mCal);
		}
	}

	protected void updateTotalPrice(boolean isDataAvailable)
	{

			if(isDataAvailable)
			{
				totalPrice = oneWayTotalPrice+returnTotalPrice;
				btnSubmitNext.setVisibility(View.VISIBLE);
				if(AppConstants.bookingFlightDO.myODIDOReturn != null)
					llTotalPrice.setVisibility(View.VISIBLE);
				else
					llTotalPrice.setVisibility(View.GONE);
			}
			else
			{
				totalPrice = 0;
				btnSubmitNext.setVisibility(View.GONE);
				llTotalPrice.setVisibility(View.GONE);
			}
			tvTotalPrice.setText(AppConstants.CurrencyCodeAfterExchange+" "+updateCurrencyByFactor(totalPrice+"", 0));
			
//			if(AppConstants.bookingFlightDO.myODIDOReturn == null && !isLoaderNeededToBeContinue)
//				hideLoader();
	}
	protected void updateTotalPriceForOneWay(String price, int flightCount)
	{
		AppConstants.bookingFlightDO.myODIDOOneWay = airAvailabilityDO
				.vecOriginDestinationInformationDOs
				.get(flightCount);

		AppConstants.bookingFlightDO.myODIDOOneWay.mPTC_FareBreakdownDOPromo = 
				airAvailabilityDO
				.vecPricedItineraryDOsPromoFare.get(flightCount)
				.vecPTC_FareBreakdownDOs.get(0);
		AppConstants.bookingFlightDO.myODIDOOneWay.arlBundledServiceDOs = 
				airAvailabilityDO
				.vecPricedItineraryDOsPromoFare.get(flightCount)
				.arlBundledServiceDOs;
		
		llTotalPrice.setVisibility(View.GONE);
		hideLoader();

//		if(!price.equalsIgnoreCase("0"))
//		{
//			totalPrice = oneWayTotalPrice+returnTotalPrice;
//			btnSubmitNext.setVisibility(View.VISIBLE);
//			llTotalPrice.setVisibility(View.VISIBLE);
//		}
//		else
//		{
//			totalPrice = 0;
//			btnSubmitNext.setVisibility(View.GONE);
//			llTotalPrice.setVisibility(View.GONE);
//		}
//		tvTotalPrice.setText(AppConstants.CurrencyCodeAfterExchange+" "+updateCurrencyByFactor(price+"", 0));
	}

/*	private void callFlightListServiceOneWay() {

		airAvailabilityDO= null;
		airAvailabilityDO = new AirAvailabilityDO();
		airAvailabilityDO.vecPricedItineraryDOsPromoFare = null;
		airAvailabilityDO.vecPricedItineraryDOsFlexiFare = null;

		airAvailabilityDO.vecOriginDestinationInformationDOs = new Vector<OriginDestinationInformationDO>();

		AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();

		if (new CommonBL(ReturnFlightActivityNew.this, ReturnFlightActivityNew.this)
		.getAirAvailability(AppConstants.bookingFlightDO.myBookFlightDO,
				AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs))
			showLoader("");
		else
			showCustomDialog(this, getString(R.string.Alert),getString(R.string.InternetProblem),getString(R.string.Ok), "", "");
	}
*/	
	/*
	 *  Calling service for Return Flights too with new request. 
	 */
	
	private void callFlightListServiceReturn() {
		
		airAvailabilityDO= null;
		airAvailabilityDO = new AirAvailabilityDO();
		airAvailabilityDO.vecPricedItineraryDOsPromoFare = null;
		airAvailabilityDO.vecPricedItineraryDOsFlexiFare = null;
		
		airAvailabilityDO.vecOriginDestinationInformationDOs = new Vector<OriginDestinationInformationDO>();
		
		AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();
		
		if (new CommonBL(ReturnFlightActivityNew.this, ReturnFlightActivityNew.this)
				.getReturnAirAvailability(AppConstants.bookingFlightDO.myBookFlightDO,
						AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs, AppConstants.OriginLocation, 
						AppConstants.DestinationLocation, AppConstants.ArrivalTime ))
			showLoader("");
		else
			showCustomDialog(this, getString(R.string.Alert),getString(R.string.InternetProblem),getString(R.string.Ok), "", "");
	}

	private void callFlightListServiceReturn(boolean isShowLoader) {

		if(isShowLoader)
			showLoader("");

		airAvailabilityDOReturn= null;
		airAvailabilityDOReturn = new AirAvailabilityDO();
		airAvailabilityDOReturn.vecPricedItineraryDOsPromoFare = null;
		airAvailabilityDOReturn.vecPricedItineraryDOsFlexiFare = null;
		airAvailabilityDOReturn.vecOriginDestinationInformationDOs = new Vector<OriginDestinationInformationDO>();

		AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();

		if (new CommonBL(this, this)
		.getAirAvailabilityReturn(AppConstants.bookingFlightDO.myBookFlightDOReturn,
				AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs))
		{
		}
		else
		{
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert),getString(R.string.InternetProblem),getString(R.string.Ok), "", "");
		}
	}

	protected void callServicePriceOneWay(int flightCount, boolean isShowLoader) {

		if(isShowLoader)
			showLoader("");

		AppConstants.bookingFlightDO.myODIDOOneWay = airAvailabilityDOReturn.vecOriginDestinationInformationDOs.get(flightCount);

		if(isManageBook)
		{
			if (new CommonBL(ReturnFlightActivityNew.this, ReturnFlightActivityNew.this)
			.getAirPriceQuote(
					AppConstants.bookingFlightDO.requestParameterDO,
					AppConstants.AIRPORT_CODE,
					AppConstants.bookingFlightDO.myBookFlightDO,
					AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
					AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
					null,
					false, false, 
					AppConstants.BookingReferenceID_Id, AppConstants.BookingReferenceID_Type,
					AppConstants.flexiOperationsDOCancel, AppConstants.flexiOperationsDOModify,"")) {
			}
			else
			{
				hideLoader();
				showCustomDialog(this, getString(R.string.Alert),getString(R.string.InternetProblem),getString(R.string.Ok), "", "");
			}
		}
		else
		{
			if (new CommonBL(ReturnFlightActivityNew.this, ReturnFlightActivityNew.this)
			.getAirPriceQuote(
					AppConstants.bookingFlightDO.requestParameterDO,
					AppConstants.AIRPORT_CODE,
					AppConstants.bookingFlightDO.myBookFlightDO,
					null,
					AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
					null,
					false, false,
					"", "",null,null,"")) {
			}
			else
			{
				hideLoader();
				showCustomDialog(this, getString(R.string.Alert),getString(R.string.InternetProblem),getString(R.string.Ok), "", "");
			}
		}
	}
	protected void callServicePriceReturn(int flightCount,int flightCountReturn, boolean isShowLoader) {

		if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
		{
			if(flightCount >= 0 && flightCountReturn >= 0)
			{
				if(isShowLoader)
					showLoader("");

				AppConstants.bookingFlightDO.myODIDOOneWay = airAvailabilityDO.vecOriginDestinationInformationDOs.get(flightCount);
				AppConstants.bookingFlightDO.myODIDOReturn = airAvailabilityDOReturn.vecOriginDestinationInformationDOs.get(flightCountReturn);

				if(AppConstants.bookingFlightDO.requestParameterDO != null
						&& !TextUtils.isEmpty(AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier))
					AppConstants.bookingFlightDO.requestParameterDOReturn.transactionIdentifier = AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier;

				if(isManageBook)
				{
					if (new CommonBL(ReturnFlightActivityNew.this, ReturnFlightActivityNew.this)
					.getAirPriceQuote(
							AppConstants.bookingFlightDO.requestParameterDOReturn,
							AppConstants.AIRPORT_CODE,
							AppConstants.bookingFlightDO.myBookFlightDOReturn,
							AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
							AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
							null,
							false, false, AppConstants.BookingReferenceID_Id,
							AppConstants.BookingReferenceID_Type,
							AppConstants.flexiOperationsDOCancel, AppConstants.flexiOperationsDOModify,"")) {
					}
					else
					{
						hideLoader();
						showCustomDialog(this, getString(R.string.Alert),getString(R.string.InternetProblem),getString(R.string.Ok), "", "");
					}
				}
				else
				{
					
					if (new CommonBL(ReturnFlightActivityNew.this, ReturnFlightActivityNew.this)
					.getAirPriceQuoteReturn(
							AppConstants.bookingFlightDO.requestParameterDOReturn,
							AppConstants.AIRPORT_CODE,
							AppConstants.bookingFlightDO.myBookFlightDOReturn,
							null,
							AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
							AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs,
							false, false, "", "",null,null,"", vecDepartureDos.get(flightCountOneWay).departureFlightSegments, vecReturnDos.get(flightCountReturn).arrivalFlightSegments)) {
					}
					else
					{
						hideLoader();
						showCustomDialog(this, getString(R.string.Alert),getString(R.string.InternetProblem),getString(R.string.Ok), "", "");
					}
				}
			}
		}
	}

	@Override
	public void onButtonYesClick(String from) {
		super.onButtonYesClick(from);
		if(from.equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg))
				|| from.equalsIgnoreCase(getString(R.string.TechProblem)))
			clickHome();
	}
	
	
	// Adapter Class...
	
	
	public class FlightOneWayAdapter extends BaseAdapter {

		private Vector<PricedItineraryDO> vecForPriceShowing=null;
		private Context con;
		private int pos ;
		public FlightOneWayAdapter(Context ctx,Vector<OriginDestinationInformationDO> vecAirport2, int pos) 
		{
			this.con = ctx;
			this.pos = pos;
		}


		@Override
		public int getCount() 
		{
			if(vecDepartureDos != null && vecDepartureDos.size() > 0)
				return vecDepartureDos.size();
			else
				return 0;
		}

		@Override
		public Object getItem(int position) 
		{
			return position;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ViewHolder viewHolder;
			OriginDestinationInformationDO originDestinationDO = vecDepartureDos.get(position);

			if(convertView == null)
			{
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(con).inflate(R.layout.select_flight_list_item_new, null);
				
				viewHolder.llSelectFlightMain = (LinearLayout) convertView.findViewById(R.id.llSelectFlightMain);
				viewHolder.llPriceTag = (LinearLayout) convertView.findViewById(R.id.llPriceTag);
				viewHolder.tvSelectedOriginTime = (TextView) convertView.findViewById(R.id.tvSelectedOriginTime);
				viewHolder.tvSelectedDestTime = (TextView) convertView.findViewById(R.id.tvSelectedDestTime);
				viewHolder.tvSelectedOrigin = (TextView) convertView.findViewById(R.id.tvSelectedOrigin);
				viewHolder.tvSelectedDest = (TextView) convertView.findViewById(R.id.tvSelectedDest);
				viewHolder.tvSelectedFlightType = (TextView) convertView.findViewById(R.id.tvSelectedFlightType);
				viewHolder.tvSelectedDestTime1D = (TextView) convertView.findViewById(R.id.tvSelectedDestTime1D);
				viewHolder.tvCurrencyType = (TextView) convertView.findViewById(R.id.tvCurrencyType);
				viewHolder.tvTotalPrice = (TextView) convertView.findViewById(R.id.tvTotalPrice);
				
				viewHolder.ivFlightConnectType = (ImageView) convertView.findViewById(R.id.ivFlightConnectType);
				
				convertView.setTag(viewHolder);
			}
			else
				viewHolder = (ViewHolder)convertView.getTag();
			
			viewHolder.llPriceTag.setVisibility(View.GONE);
			
			int totalFlightSeg = vecDepartureDos.size();
			if(totalFlightSeg > 1)
			{
				if(pos == position)
				{	
					if(position == 0)
						viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.blue_box_n);
					else if(position == totalFlightSeg-1)
					{
						viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.blue_box_n);
					}
					else
						viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.blue_box_n);
					
					if (position==0 && !isFirstTimeSelected && vecForPriceShowing != null) {
						if(pos==1)				
							viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
							else
								viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
					}
				}
			
				else
				{
					if(position == 0){
						if(pos==1)				
						viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
						else
							viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
					}
					else if(position == totalFlightSeg-1)
						viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
					else if(position == pos-1)
						viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
					else
						viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
				}
			}
			else
			{
//				if(pos == position)
//					viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_single_select);
//				else
//					viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_single_unselected);
				
				if(pos == position)
					viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.blue_box_n);
				else
					viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
			}
			
			String strTimeDip = CalendarUtility.getBookingTimeFromDate(originDestinationDO.departureDateTime);
			String strTimeArr = CalendarUtility.getBookingTimeFromDate(originDestinationDO.arrivalDateTime);
			
			viewHolder.tvSelectedOriginTime.setText(strTimeDip);
			viewHolder.tvSelectedDestTime.setText(strTimeArr);
			viewHolder.tvSelectedOrigin.setText(originDestinationDO.originLocationCode);
			viewHolder.tvSelectedDest.setText(originDestinationDO.destinationLocationCode);
			
			if(StringUtils.getFloat(strTimeArr.split(":")[0]) < StringUtils.getFloat(strTimeDip.split(":")[0]))
			{
				viewHolder.tvSelectedDestTime1D.setVisibility(View.VISIBLE);
			}
			else
			{
				viewHolder.tvSelectedDestTime1D.setVisibility(View.GONE);
			}
			
			int totalConnectedFlights = 0;
			if(originDestinationDO.vecOriginDestinationOptionDOs != null 
					&& originDestinationDO.vecOriginDestinationOptionDOs.size() > 0)
				totalConnectedFlights = originDestinationDO.vecOriginDestinationOptionDOs.size();
			
			if(totalConnectedFlights == 1)
			{
				viewHolder.ivFlightConnectType.setBackgroundResource(R.drawable.f_stops_direct);
				viewHolder.tvSelectedFlightType.setText(con.getString(R.string.direct));
			}
			else if(totalConnectedFlights > 1 && totalConnectedFlights <=2)
			{
				viewHolder.ivFlightConnectType.setBackgroundResource(R.drawable.f_stops_1);
				viewHolder.tvSelectedFlightType.setText(totalConnectedFlights-1+" "+con.getString(R.string.Stop));
			}
			else
			{
				viewHolder.ivFlightConnectType.setBackgroundResource(R.drawable.f_stops_1);
				viewHolder.tvSelectedFlightType.setText("1 "+con.getString(R.string.Stops));
			}
			
			if (vecForPriceShowing != null && position < vecForPriceShowing.size()) 
			{
				viewHolder.llPriceTag.setVisibility(View.VISIBLE);
				viewHolder.tvCurrencyType.setText(AppConstants.CurrencyCodeAfterExchange);
//				viewHolder.tvTotalPrice.setText(vecForPriceShowing.get(position).vecPTC_FareBreakdownDOs.get(0).totalFare.amount+"");
				viewHolder.tvTotalPrice.setText(((BaseActivity)con).updateCurrencyByFactor(vecForPriceShowing.get(position).vecPTC_FareBreakdownDOs.get(0).totalFare.amount+"", 0));
				///Need to change it here
				viewHolder.ivFlightConnectType.setBackground(con.getResources().getDrawable(R.drawable.img_oneway_dumbble_direct));
				
			
			}
			
			return convertView;
		}

		class ViewHolder {
			LinearLayout llSelectFlightMain,llPriceTag;
			ImageView ivFlightConnectType;
			TextView tvSelectedOriginTime,tvSelectedDestTime,tvSelectedOrigin,tvSelectedDest,tvSelectedFlightType,tvSelectedDestTime1D,
			tvCurrencyType,tvTotalPrice;
		}


		/*public void refresh(Vector<OriginDestinationInformationDO> arrayListPa, int pos)
		{
			vecFlightOriginDestinationDo = arrayListPa;
			this.pos = pos;
			notifyDataSetChanged();
		}
		public void refresh(Vector<OriginDestinationInformationDO> arrayListPa, int pos, Vector<PricedItineraryDO> vecForPriceShowing)
		{
			vecFlightOriginDestinationDo = arrayListPa;
			this.pos = pos;
			this.vecForPriceShowing = new Vector<PricedItineraryDO>();
			this.vecForPriceShowing = vecForPriceShowing;
//			if(AppConstants.bookingFlightDO.myODIDOReturn == null)
//				((BaseActivity)con).showLoader();
			notifyDataSetChanged();
		}*/
		
		public void refreshPos(int pos)
		{
			this.pos = pos;
			notifyDataSetChanged();
			isFirstTimeSelected = true;
		}

		public Vector<OriginDestinationInformationDO> getData()
		{
			return vecDepartureDos;
		}

	}
	
	
	// Adapter for showing the returning list...
	
	public class ReturnFlightAdapter extends BaseAdapter {

		private Vector<PricedItineraryDO> vecForPriceShowing=null;
		private Context con;
		private int pos ;

		public ReturnFlightAdapter (Context ctx,Vector<OriginDestinationInformationDO> vecAirport2, int pos) 
		{
			this.con = ctx;
			this.pos = pos;
		}


		@Override
		public int getCount() 
		{
			if(vecReturnDos != null && vecReturnDos.size() > 0)
				return vecReturnDos.size();
			else
				return 0;
		}

		@Override
		public Object getItem(int position) 
		{
			return position;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ViewHolder viewHolder;
			OriginDestinationInformationDO originDestinationDO = vecReturnDos.get(position);

			if(convertView == null)
			{
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(con).inflate(R.layout.select_flight_list_item_new, null);
				
				viewHolder.llSelectFlightMain = (LinearLayout) convertView.findViewById(R.id.llSelectFlightMain);
				viewHolder.llPriceTag = (LinearLayout) convertView.findViewById(R.id.llPriceTag);
				viewHolder.tvSelectedOriginTime = (TextView) convertView.findViewById(R.id.tvSelectedOriginTime);
				viewHolder.tvSelectedDestTime = (TextView) convertView.findViewById(R.id.tvSelectedDestTime);
				viewHolder.tvSelectedOrigin = (TextView) convertView.findViewById(R.id.tvSelectedOrigin);
				viewHolder.tvSelectedDest = (TextView) convertView.findViewById(R.id.tvSelectedDest);
				viewHolder.tvSelectedFlightType = (TextView) convertView.findViewById(R.id.tvSelectedFlightType);
				viewHolder.tvSelectedDestTime1D = (TextView) convertView.findViewById(R.id.tvSelectedDestTime1D);
				viewHolder.tvCurrencyType = (TextView) convertView.findViewById(R.id.tvCurrencyType);
				viewHolder.tvTotalPrice = (TextView) convertView.findViewById(R.id.tvTotalPrice);
				
				viewHolder.ivFlightConnectType = (ImageView) convertView.findViewById(R.id.ivFlightConnectType);
				
				convertView.setTag(viewHolder);
			}
			else
				viewHolder = (ViewHolder)convertView.getTag();
			
			viewHolder.llPriceTag.setVisibility(View.GONE);
			
			int totalFlightSeg = vecReturnDos.size();
			if(totalFlightSeg > 1)
			{
				if(pos == position)
				{	
					if(position == 0)
						viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_top_select);
					else if(position == totalFlightSeg-1)
					{
						viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_bottom_select);
					}
					else
						viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_middle_select);
					
					if (position==0 && !isFirstTimeSelected && vecForPriceShowing != null) {
						if(pos==1)				
							viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_top_unselected);
							else
								viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_top_unselected_two);
					}
				}
			
				else
				{
					if(position == 0){
						if(pos==1)				
						viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_top_unselected);
						else
							viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_top_unselected_two);
					}
					else if(position == totalFlightSeg-1)
						viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_bottom_unselected);
					else if(position == pos-1)
						viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_middle_unselected_two);
					else
						viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_middle_unselected);
				}
			}
			else
			{
//				if(pos == position)
//					viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_single_select);
//				else
//					viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_single_unselected);
				
				if(pos == position)
					viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_top_select);
				else
					viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_top_unselected);
			}
			
			String strTimeDip = CalendarUtility.getBookingTimeFromDate(originDestinationDO.departureDateTime);
			String strTimeArr = CalendarUtility.getBookingTimeFromDate(originDestinationDO.arrivalDateTime);
			
			viewHolder.tvSelectedOriginTime.setText(strTimeDip);
			viewHolder.tvSelectedDestTime.setText(strTimeArr);
			viewHolder.tvSelectedOrigin.setText(originDestinationDO.originLocationCode);
			viewHolder.tvSelectedDest.setText(originDestinationDO.destinationLocationCode);
			
			if(StringUtils.getFloat(strTimeArr.split(":")[0]) < StringUtils.getFloat(strTimeDip.split(":")[0]))
			{
				viewHolder.tvSelectedDestTime1D.setVisibility(View.VISIBLE);
			}
			else
			{
				viewHolder.tvSelectedDestTime1D.setVisibility(View.GONE);
			}
			
			int totalConnectedFlights = 0;
			if(originDestinationDO.vecOriginDestinationOptionDOs != null 
					&& originDestinationDO.vecOriginDestinationOptionDOs.size() > 0)
				totalConnectedFlights = originDestinationDO.vecOriginDestinationOptionDOs.size();
			
			if(totalConnectedFlights == 1)
			{
				viewHolder.ivFlightConnectType.setBackgroundResource(R.drawable.f_stops_direct);
				viewHolder.tvSelectedFlightType.setText(con.getString(R.string.direct));
			}
			else if(totalConnectedFlights > 1 && totalConnectedFlights <=2)
			{
				viewHolder.ivFlightConnectType.setBackgroundResource(R.drawable.f_stops_1);
				viewHolder.tvSelectedFlightType.setText(totalConnectedFlights-1+" "+con.getString(R.string.Stop));
			}
			else
			{
				viewHolder.ivFlightConnectType.setBackgroundResource(R.drawable.f_stops_1);
				viewHolder.tvSelectedFlightType.setText("1 "+con.getString(R.string.Stops));
			}
			
			if (vecForPriceShowing != null && position < vecForPriceShowing.size()) 
			{
				viewHolder.llPriceTag.setVisibility(View.VISIBLE);
				viewHolder.tvCurrencyType.setText(AppConstants.CurrencyCodeAfterExchange);
//				viewHolder.tvTotalPrice.setText(vecForPriceShowing.get(position).vecPTC_FareBreakdownDOs.get(0).totalFare.amount+"");
				viewHolder.tvTotalPrice.setText(((BaseActivity)con).updateCurrencyByFactor(vecForPriceShowing.get(position).vecPTC_FareBreakdownDOs.get(0).totalFare.amount+"", 0));
				///Need to change it here
				viewHolder.ivFlightConnectType.setBackground(con.getResources().getDrawable(R.drawable.img_oneway_dumbble_direct));
				
			
			}
			
			return convertView;
		}

		class ViewHolder {
			LinearLayout llSelectFlightMain,llPriceTag;
			ImageView ivFlightConnectType;
			TextView tvSelectedOriginTime,tvSelectedDestTime,tvSelectedOrigin,tvSelectedDest,tvSelectedFlightType,tvSelectedDestTime1D,
			tvCurrencyType,tvTotalPrice;
		}


		/*public void refresh(Vector<OriginDestinationInformationDO> arrayListPa, int pos)
		{
			vecFlightOriginDestinationDo = arrayListPa;
			this.pos = pos;
			notifyDataSetChanged();
		}
		public void refresh(Vector<OriginDestinationInformationDO> arrayListPa, int pos, Vector<PricedItineraryDO> vecForPriceShowing)
		{
			vecFlightOriginDestinationDo = arrayListPa;
			this.pos = pos;
			this.vecForPriceShowing = new Vector<PricedItineraryDO>();
			this.vecForPriceShowing = vecForPriceShowing;
//			if(AppConstants.bookingFlightDO.myODIDOReturn == null)
//				((BaseActivity)con).showLoader();
			notifyDataSetChanged();
		}*/
		
		public void refreshPos(int pos)
		{
			this.pos = pos;
			notifyDataSetChanged();
			isFirstTimeSelected = true;
		}

		public Vector<OriginDestinationInformationDO> getData()
		{
			return vecReturnDos;
		}

	}
	
	
	static public Object deepCopy(Object oldObj) throws Exception
	   {
	      ObjectOutputStream oos = null;
	      ObjectInputStream ois = null;
	      try
	      {
	         ByteArrayOutputStream bos = 
	               new ByteArrayOutputStream(); // A
	         oos = new ObjectOutputStream(bos); // B
	         // serialize and pass the object
	         oos.writeObject(oldObj);   // C
	         oos.flush();               // D
	         ByteArrayInputStream bin = 
	               new ByteArrayInputStream(bos.toByteArray()); // E
	         ois = new ObjectInputStream(bin);                  // F
	         // return the new object
	         return ois.readObject(); // G
	      }
	      catch(Exception e)
	      {
	         System.out.println("Exception in ObjectCloner = " + e);
	         throw(e);
	      }
	      finally
	      {
	         oos.close();
	         ois.close();
	      }
	   }
	
}