package com.winit.airarabia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.insider.android.insider.Insider;
import com.winit.airarabia.adapters.FlightOneWayAdapter;
import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.insider.GcmBroadcastReceiver;
import com.winit.airarabia.objects.AirAvailabilityDO;
import com.winit.airarabia.objects.AirPriceQuoteDO;
import com.winit.airarabia.objects.BundledServiceDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.OriginDestinationInformationDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;
import com.winit.airarabia.objects.PTC_FareBreakdownDO;
import com.winit.airarabia.objects.PricedItineraryDO;
import com.winit.airarabia.objects.RequestParameterDO;
import com.winit.airarabia.returnflight.ReturnFlightActivityNew;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.utils.LogUtils;
import com.winit.airarabia.utils.StringUtils;
import com.winit.airarabia.webaccess.Response;

public class SelectFlightActivityNew extends BaseActivity implements
OnClickListener, DataListener {

	private LinearLayout llSelectflight, llDateReturn, llOneWayFlightView, llReturnFlightView, llFareBottomCondition, llHajj;
	private TextView tvDateOneWayPrev,tvDateOneWayCurrent,tvDateOneWayNext,tv_lable,tv_star, tvHajj,
	tvDateReturnPrev,tvDateReturnCurrent,tvDateReturnNext;
	private Calendar curCal, cal;
	private Calendar mCal,retCal;
	protected boolean isManageBook = false;
	private int year = 0, month = 0, day = 0;
	private Button btnBack_SelectFlight;
	private TextView tvHeadertitle_SelectFlight, tvSourceFlight1, tvDestFlight1;
	private ImageView ivmenu_SelectFlight, ivHeaderSelectFlight, ivDateOneWayPrev, ivDateOnewWayNext, ivDateReturnPrev, ivDateReturnNext ;
	private AirAvailabilityDO airAvailabilityDO,airAvailabilityDOReturn;
	
	private AirAvailabilityDO airAvailability ;
	private final String DATAFAIL = "DATAFAIL";
	private ImageView ivSepreatorDays;
	protected TextView tvTotalPrice, tvDepartTag,tvReturnTag, tvSelectFlightSub, tvTotalTagColan, tvTotalTag;
	protected float oneWayTotalPrice=0, returnTotalPrice=0, totalPrice=0; 
	public Boolean isValuesToShow = false; 
	private SelectFlightOneWayFragment frFlightOneWay;
	private SelectFlightReturnFragment frFlightReturn;
	private FragmentManager fragmentManager;
	private boolean isReturnServiceCalled,isDateChanged, isLoaderNeededToBeContinue = false, isOneWayFlight=false, isReturnFlight = false;


	protected int flightCountOneWay = -1, flightCountReturn = -1;

	protected TextView tvNoDataFlightOneWay,tvNoDataFlightReturn;

	private LinearLayout llFragmentFlights, llTotalPrice;
	private TextView tvNoDataFlightAll;
	private String errorMsgPrice = "";
	private int allFlightCounts = -1;
	public int currentFlightCountPrice=0;
	private Vector<PricedItineraryDO> vecForPriceShowing = null;
	private SelectFlightActivityNew.BCR bcr;
	private View viewSpace;
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
		AppConstants.fareType = "";
		AppConstants.bookingFlightDO.bundleServiceID = "";
		AppConstants.bookingFlightDO.isFlexiOut = false;
		AppConstants.bookingFlightDO.isFlexiIn = false;
		if(tvNoDataFlightOneWay.getVisibility() == View.GONE) {
			llFareBottomCondition.setVisibility(View.VISIBLE);
			llTotalPrice.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void initilize() {
		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		intentFilter.addAction(AppConstants.BOOK_SUCCESS);
		registerReceiver(bcr, intentFilter);
		cal =(Calendar) getIntent().getExtras().getSerializable("SELECT_FLIGHT_CAL");
		
		btnSubmitNext.setText(getString(R.string.Continue));
		if(AppConstants.bookingFlightDO != null){
			if (AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify != null
					&& AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.size() > 0) {
				isManageBook = true;
				AppConstants.ISMANAGE_BOOK = true;
			}
			else
				AppConstants.ISMANAGE_BOOK = false;
		}
		llSelectflight = (LinearLayout) layoutInflater.inflate(R.layout.selectflight_new, null);
		llMiddleBase.addView(llSelectflight, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lltop.setVisibility(View.GONE);


		llDateReturn = (LinearLayout) llSelectflight.findViewById(R.id.llDateReturn);
		llHajj = (LinearLayout) llSelectflight.findViewById(R.id.llHajj);

		tvDateOneWayPrev = (TextView) llSelectflight.findViewById(R.id.tvDateOneWayPrev);
		tvDateOneWayCurrent = (TextView) llSelectflight.findViewById(R.id.tvDateOneWayCurrent);
		tvDateOneWayNext = (TextView) llSelectflight.findViewById(R.id.tvDateOneWayNext);

		tvHajj = (TextView) llSelectflight.findViewById(R.id.tvHajj);

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
		tv_lable		 = (TextView) llSelectflight.findViewById(R.id.tv_lable);
		tv_star		 = (TextView) llSelectflight.findViewById(R.id.tv_star);
		llFareBottomCondition		 = (LinearLayout) llSelectflight.findViewById(R.id.llFareBottomCondition);

		viewSpace = llSelectflight.findViewById(R.id.viewSpace);
		airAvailabilityDO = new AirAvailabilityDO();
		airAvailabilityDOReturn = new AirAvailabilityDO();

		frFlightOneWay = (SelectFlightOneWayFragment) getSupportFragmentManager().findFragmentById(R.id.frFlightOneWay);
		frFlightReturn = (SelectFlightReturnFragment) getSupportFragmentManager().findFragmentById(R.id.frFlightReturn);


		llOneWayFlightView = (LinearLayout) llSelectflight.findViewById(R.id.llOneWayFlightView);
		llReturnFlightView = (LinearLayout) llSelectflight.findViewById(R.id.llReturnFlightView);
		tvDepartTag			=(TextView) llSelectflight.findViewById(R.id.tvDepartTag);
		tvReturnTag			=(TextView) llSelectflight.findViewById(R.id.tvReturnTag);

		tvNoDataFlightOneWay			=(TextView) llSelectflight.findViewById(R.id.tvNoDataFlightOneWay);
		tvNoDataFlightReturn			=(TextView) llSelectflight.findViewById(R.id.tvNoDataFlightReturn);

		llFragmentFlights = (LinearLayout) llSelectflight.findViewById(R.id.llFragmentFlights);
		tvNoDataFlightAll =(TextView) llSelectflight.findViewById(R.id.tvNoDataFlightAll);

		fragmentManager = getSupportFragmentManager();

		if (AppConstants.bookingFlightDO.myBookFlightDOReturn == null) {

			ivDateOneWayPrev.setVisibility(View.VISIBLE);
			ivDateOnewWayNext.setVisibility(View.VISIBLE);
			llFareBottomCondition.setVisibility(View.VISIBLE);
			FragmentTransaction FRAGMENTTRANSACTION = fragmentManager.beginTransaction();
			FRAGMENTTRANSACTION.hide(frFlightReturn);
			FRAGMENTTRANSACTION.commit();
		}

		if (AppConstants.bookingFlightDO.myBookFlightDOReturn != null) {
			llFareBottomCondition.setVisibility(View.VISIBLE);
		}

		btnBack_SelectFlight.setVisibility(View.VISIBLE);

		//		setTypeFaceOpenSansLight(llSelectflight);
		setTypefaceOpenSansRegular(llSelectflight);
		tvTotalTag.setTypeface(typefaceOpenSansRegular);	
		tvTotalTagColan.setTypeface(typefaceOpenSansSemiBold);	
		setTypefaceOpenSansRegular(llTotalPrice);
		tvDateReturnCurrent.setTypeface(typefaceOpenSansSemiBold);
		tvDateOneWayCurrent.setTypeface(typefaceOpenSansSemiBold);
		tvTotalPrice.setTypeface(typefaceOpenSansSemiBold);
		tvSourceFlight1.setTypeface(typefaceOpenSansSemiBold);
		tvDestFlight1.setTypeface(typefaceOpenSansSemiBold);
		//		tvTotalTag.setTypeface(typefaceOpenSansRegular);
		tvHeadertitle_SelectFlight.setTypeface(typefaceOpenSansSemiBold);
		btnBack_SelectFlight.setTypeface(typefaceOpenSansSemiBold);
		tvDateOneWayPrev.setTypeface(typefaceOpenSansRegular);
		tvDateOneWayNext.setTypeface(typefaceOpenSansRegular);
		tvDateReturnPrev.setTypeface(typefaceOpenSansRegular);
		tvDateReturnNext.setTypeface(typefaceOpenSansRegular);
		tvSelectFlightSub.setTypeface(typefaceOpenSansSemiBold);
		tvDepartTag.setTypeface(typefaceOpenSansSemiBold);
		tvReturnTag.setTypeface(typefaceOpenSansSemiBold);
		tv_lable.setTypeface(typefaceOpenSansRegular);
		tv_star.setTypeface(typefaceOpenSansRegular);

		Insider insider = new Insider();
		insider.openSession(this, AppConstants.ProjectName);
		insider.registerInBackground(this, AppConstants.GoogleProjectNumber);
		Insider.setLandingActivity(GcmBroadcastReceiver.class, this);
		Insider.setNotificationIcon(R.drawable.ic_launcher, this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		Insider.start(this, intent);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Insider.stop(this);
	}

	@Override
	public void bindingControl() {

		loadData();

		btnBack_SelectFlight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
								Insider.tagEvent(AppConstants.BackButtonFlightList, SelectFlightActivityNew.this);
				//Insider.Instance.tagEvent(SelectFlightActivityNew.this,AppConstants.BackButtonFlightList);
				trackEvent("Select Flight",AppConstants.BackButtonFlightList,"");
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
				
				if(!checkLangFrench())
				{
					tvDateOneWayCurrent.setText(getFirstLetterCapital((CalendarUtility.getSelectFlightDateNew(mCal,0))));
					tvDateOneWayPrev.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,-1)));
					tvDateOneWayNext.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,1)));
				}
				else
				{
					
					tvDateOneWayCurrent.setText(getFirstLetterCapital((CalendarUtility.getSelectFlightDateNew(mCal,0))).toString().replace(".", ""));
					tvDateOneWayPrev.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,-1)).toString().replace(".", ""));
					tvDateOneWayNext.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,1)).toString().replace(".", ""));
				}
			} else {

				ivHeaderSelectFlight.setBackgroundResource(R.drawable.flight_return_logo);
				if(!checkLangFrench())
				{
					tvDateOneWayCurrent.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,0)));
					tvDateOneWayPrev.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,-1)));
					tvDateOneWayNext.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,1)));

					tvDateReturnCurrent.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,0)));
					tvDateReturnPrev.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,-1)));
					tvDateReturnNext.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,1)));
				}
				else
				{
					tvDateOneWayCurrent.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,0)).toString().replace(".", ""));
					tvDateOneWayPrev.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,-1)).toString().replace(".", ""));
					tvDateOneWayNext.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,1)).toString().replace(".", ""));

					tvDateReturnCurrent.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,0)).toString().replace(".", ""));
					tvDateReturnPrev.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,-1)).toString().replace(".", ""));
					tvDateReturnNext.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,1)).toString().replace(".", ""));
				}
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

		callFlightListServiceOneWay();

		btnSubmitNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

								Insider.tagEvent(AppConstants.ContinueFlightList, SelectFlightActivityNew.this);
				//Insider.Instance.tagEvent(SelectFlightActivityNew.this,AppConstants.ContinueFlightList);
				trackEvent("Select Flight",AppConstants.ContinueFlightList,"");
				String strMsg = validateFlight();
//				strMsg = null;
//				strMsg = strMsg.toUpperCase();
				if(TextUtils.isEmpty(strMsg))
					moveToFlightDetails();
				else
					showCustomDialog(SelectFlightActivityNew.this,
							getString(R.string.Alert),
							strMsg,
							getString(R.string.Ok), "", "");
			}
		});
	}

	private void callFlightListServiceReturn() {
		
		airAvailability= null;
		airAvailability = new AirAvailabilityDO();
		
		if (new CommonBL(SelectFlightActivityNew.this, SelectFlightActivityNew.this)
				.getReturnAirAvailability(AppConstants.bookingFlightDO.myBookFlightDO,
						AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs, AppConstants.OriginLocation, 
						AppConstants.DestinationLocation, AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime ))
			showLoader("");
		else
			showCustomDialog(this, getString(R.string.Alert),getString(R.string.InternetProblem),getString(R.string.Ok), "", "");
	}

	private String validateFlight()
	{
		String errorMsg = "";

		if(totalPrice <= 0)
			errorMsg = errorMsgPrice;
		else if(!AppConstants.CAL_VALUE)
			errorMsg = getString(R.string.SelectAnotherFlight);
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
				strPersons = AppConstants.bookingFlightDO.myBookFlightDO.adtQty + " " + getString(R.string.Adultss);
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
				strPersons = strPersons+", "+AppConstants.bookingFlightDO.myBookFlightDO.infQty + " " + getString(R.string.Infantss);
		}

		tvHeadertitle_SelectFlight.setText(strPersons);
	}
	private void moveToFlightDetails()
	{
		if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
		{
			Intent intenNext = new Intent(SelectFlightActivityNew.this, SelectFlightItemDetails.class);
			startActivity(intenNext);
		}
		else
		{
			if(AppConstants.bookingFlightDO.myBookFlightDOReturn == null && FlightOneWayAdapter.isFirstTimeSelected)
			{
				Intent intenNext = new Intent(SelectFlightActivityNew.this, SelectFlightItemDetails.class);
				startActivity(intenNext);
			}
			else
			{
				showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.please_select_your_flight), getString(R.string.Ok), null, "");
			}
		}
	}

	@Override
	public void dataRetreived(Response data) {
		if (data != null) {
			switch (data.method) {
			case AIR_AVAILABILITY_RETURN:

				if (!data.isError) {
					airAvailability = new AirAvailabilityDO();
					airAvailability = (AirAvailabilityDO) data.data;
					if(!airAvailability.transactionIdentifier.equals("")) {
						AppConstants.transactionIdentifier = airAvailability.transactionIdentifier; 
						AppConstants.IsReturnServiceCalled = true;
						callServicePriceReturn(flightCountOneWay,flightCountReturn,false);
					}
					else {
						String str = "";

						if(airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() == 0 && 
								airAvailabilityDO.vecOriginDestinationInformationDOs.size() == 0) {
							str = getString(R.string.change_flights);
						}
						else if(airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() > 0 && 
								airAvailabilityDO.vecOriginDestinationInformationDOs.size() == 0) {
							str = getString(R.string.no_flights) +" "+ AppConstants.OriginLocationName+" "+getString(R.string.to)+ " "+ 
									AppConstants.DestinationLocationName +" "+ getString(R.string.on)+" "+ AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0] +
									". "+getString(R.string.please_change_your_depart);
						}
						else if(airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() == 0 && 
								airAvailabilityDO.vecOriginDestinationInformationDOs.size() > 0) {
							str = getString(R.string.no_flights) +" "+ AppConstants.DestinationLocationName +" "+getString(R.string.to)+ " "+ 
									AppConstants.OriginLocationName +" "+ getString(R.string.on)+" "+ AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime.split("T")[0] +
									". "+getString(R.string.please_change_your_return);
						}

						showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), str,	getString(R.string.Ok), "", "");

						hideLoader();
						llTotalPrice.setVisibility(View.GONE);
						//llFareBottomCondition.setVisibility(View.GONE);
						llFragmentFlights.setVisibility(View.GONE);
						tvNoDataFlightAll.setVisibility(View.VISIBLE);
						tvNoDataFlightAll.setTextColor(Color.RED);
						tvSelectFlightSub.setVisibility(View.GONE);
					}
				}
				break;
				
			case AIR_AVAILABILITY:

				if (!data.isError) {

					if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null
							&& isReturnServiceCalled)
					{
						airAvailabilityDOReturn = new AirAvailabilityDO();
						airAvailabilityDOReturn = (AirAvailabilityDO) data.data;

						if(!airAvailabilityDOReturn.transactionIdentifier.equals(""))
							AppConstants.transactionIdentifier = airAvailabilityDOReturn.transactionIdentifier;
						
						airAvailabilityDOReturn.vecPricedItineraryDOsPromoFare = new Vector<PricedItineraryDO>();
						airAvailabilityDOReturn.vecPricedItineraryDOsFlexiFare = new Vector<PricedItineraryDO>();

						if(airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() > 0 &&
								airAvailabilityDO.vecOriginDestinationInformationDOs.size() > 0) {
							//							
							updateList(airAvailabilityDOReturn);

							if(flightCountOneWay >= 0 && flightCountReturn >= 0 && airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size()>0) {
//								callServicePriceReturn(flightCountOneWay,flightCountReturn,false);
								callFlightListServiceReturn();
							}
							else
							{
								hideLoader();
							}
						}
						else {

							String str = "";

							if(airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() == 0 && 
									airAvailabilityDO.vecOriginDestinationInformationDOs.size() == 0) {
								str = getString(R.string.change_flights);
							}
							else if(airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() > 0 && 
									airAvailabilityDO.vecOriginDestinationInformationDOs.size() == 0) {
								str = getString(R.string.no_flights) +" "+ AppConstants.OriginLocationName+" "+getString(R.string.to)+ " "+ 
										AppConstants.DestinationLocationName +" "+ getString(R.string.on)+" "+ AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0] +
										". "+getString(R.string.please_change_your_depart);
							}
							else if(airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() == 0 && 
									airAvailabilityDO.vecOriginDestinationInformationDOs.size() > 0) {
								str = getString(R.string.no_flights) +" "+ AppConstants.DestinationLocationName +" "+getString(R.string.to)+ " "+ 
										AppConstants.OriginLocationName +" "+ getString(R.string.on)+" "+ AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime.split("T")[0] +
										". "+getString(R.string.please_change_your_return);
							}

							showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), str,	getString(R.string.Ok), "", "");

							hideLoader();
							llTotalPrice.setVisibility(View.GONE);
							llFragmentFlights.setVisibility(View.GONE);
							tvNoDataFlightAll.setVisibility(View.VISIBLE);
							tvNoDataFlightAll.setTextColor(Color.RED);
							tvSelectFlightSub.setVisibility(View.GONE);
							llFareBottomCondition.setVisibility(View.GONE);
						}
					}
					else
					{
						airAvailabilityDO = new AirAvailabilityDO();
						airAvailabilityDO = (AirAvailabilityDO) data.data;

						if(!airAvailabilityDO.transactionIdentifier.equals(""))
							AppConstants.transactionIdentifier = airAvailabilityDO.transactionIdentifier;
						
						airAvailabilityDO.vecPricedItineraryDOsPromoFare = new Vector<PricedItineraryDO>();
						airAvailabilityDO.vecPricedItineraryDOsFlexiFare = new Vector<PricedItineraryDO>();
						allFlightCounts = airAvailabilityDO.vecOriginDestinationInformationDOs.size();

						//						if(airAvailabilityDO.vecOriginDestinationInformationDOs.size() > 0) {

						llTotalPrice.setVisibility(View.VISIBLE);
						llFareBottomCondition.setVisibility(View.VISIBLE);
						llFragmentFlights.setVisibility(View.VISIBLE);
						tvNoDataFlightAll.setVisibility(View.GONE);
						tvSelectFlightSub.setVisibility(View.VISIBLE);
						//
						updateList(airAvailabilityDO);

						if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null && isDateChanged)
						{
							if(flightCountOneWay >= 0 && flightCountReturn >= 0 && airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size()>0) {
//								callServicePriceReturn(flightCountOneWay,flightCountReturn,false);
//								callFlightListServiceReturn();
								isReturnServiceCalled = true;
								callFlightListServiceReturn(false);
							}
							else
							{
								if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null && !isReturnServiceCalled && isDateChanged)
								{
									isReturnServiceCalled = true;
									vecForPriceShowing = new Vector<PricedItineraryDO>();
									callFlightListServiceReturn(false);
									llReturnFlightView.setVisibility(View.VISIBLE);
									viewSpace.setVisibility(View.VISIBLE);
									tvDepartTag.setVisibility(View.VISIBLE);
								}
								hideLoader();
							}
						}
						else if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null && !isReturnServiceCalled && !isDateChanged)
						{
							isReturnServiceCalled = true;
							vecForPriceShowing = new Vector<PricedItineraryDO>();
							callFlightListServiceReturn(false);
							llReturnFlightView.setVisibility(View.VISIBLE);
							viewSpace.setVisibility(View.VISIBLE);
							tvDepartTag.setVisibility(View.VISIBLE);
						}
						else
						{
							isReturnServiceCalled = false;
							if(flightCountOneWay >= 0)
							{
								currentFlightCountPrice =0;
								callServicePriceOneWay(flightCountOneWay,false);
								currentFlightCountPrice++;
							}
							else
							{
								hideLoader();
							}
							llReturnFlightView.setVisibility(View.GONE);
							viewSpace.setVisibility(View.GONE);
							tvDepartTag.setVisibility(View.GONE);
						}
						//						}
						//						else
						//						{
						//							String str = getString(R.string.no_flights) +" "+ AppConstants.OriginLocationName +" "+getString(R.string.to)+" " + 
						//									AppConstants.DestinationLocationName +" "+ getString(R.string.on)+" "+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]  +
						//								". "+getString(R.string.please_change_your_depart);
						////							showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
						//							showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), str, getString(R.string.Ok), "", "");
						//							hideLoader();
						//							tvSelectFlightSub.setVisibility(View.GONE);
						//							llTotalPrice.setVisibility(View.GONE);
						//							llFragmentFlights.setVisibility(View.GONE);
						//							tvNoDataFlightAll.setVisibility(View.VISIBLE);
						//							tvNoDataFlightAll.setTextColor(Color.RED);
						//							btnSubmitNext.setVisibility(View.GONE);
						//							llFareBottomCondition.setVisibility(View.GONE);
						//						}
					}

					if(isValuesToShow){
						if(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs != null && 
								AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size() > 0 
								&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs != null &&	
								AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0) {

							//						updateList(airAvailabilityDOReturn);
							if(airAvailabilityDOReturn != null && airAvailabilityDOReturn.vecOriginDestinationInformationDOs != null &&
									airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() > 0 ) {
								llTotalPrice.setVisibility(View.VISIBLE);
								llFareBottomCondition.setVisibility(View.VISIBLE);
								llFragmentFlights.setVisibility(View.VISIBLE);
								tvNoDataFlightAll.setVisibility(View.GONE);
								tvSelectFlightSub.setVisibility(View.VISIBLE);
							}
							//						updateList(airAvailabilityDO);
						}else {

							if(!isReturnServiceCalled) {
								String str = "";

								if(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs != null && 
										AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size() == 0 && 
										AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs != null && 
										AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() == 0 &&  airAvailabilityDOReturn != null &&
										airAvailabilityDOReturn.vecOriginDestinationInformationDOs != null &&
										airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() == 0 && 
										airAvailabilityDO.vecOriginDestinationInformationDOs.size() == 0) {
									str = getString(R.string.change_flights);
								}
								else if(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs != null && 
										AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size() == 0 ) {
									str = getString(R.string.no_flights) +" "+ AppConstants.OriginLocationName+" "+getString(R.string.to)+ " "+ 
											AppConstants.DestinationLocationName +" "+ getString(R.string.on)+" "+ AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0] +
											". "+getString(R.string.please_change_your_depart);
								}
								else if(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs != null && 
										AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() == 0 && 
										airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() == 0) {
									str = getString(R.string.no_flights) +" "+ AppConstants.DestinationLocationName +" "+getString(R.string.to)+ " "+ 
											AppConstants.OriginLocationName +" "+ getString(R.string.on)+" "+ AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime.split("T")[0] +
											". "+getString(R.string.please_change_your_return);
								}

								showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), str, getString(R.string.Ok), "", "");
								hideLoader();
								llTotalPrice.setVisibility(View.GONE);
								llFareBottomCondition.setVisibility(View.GONE);
								llFragmentFlights.setVisibility(View.GONE);
								tvNoDataFlightAll.setVisibility(View.VISIBLE);
								tvNoDataFlightAll.setTextColor(Color.RED);
								tvSelectFlightSub.setVisibility(View.GONE);
							}
							else {
//								hideLoader();
								llTotalPrice.setVisibility(View.GONE);
								llFareBottomCondition.setVisibility(View.GONE);
								llFragmentFlights.setVisibility(View.GONE);
//								tvNoDataFlightAll.setVisibility(View.VISIBLE);
								tvNoDataFlightAll.setTextColor(Color.RED);
								tvSelectFlightSub.setVisibility(View.GONE);
								btnSubmitNext.setVisibility(View.GONE);
							}
						}
					}else{

					}
					isDateChanged = false;
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
						showCustomDialog(SelectFlightActivityNew.this,
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
							if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
							{
								airAvailabilityDOReturn.vecPricedItineraryDOsPromoFare = new Vector<PricedItineraryDO>();

								airAvailabilityDOReturn.vecPricedItineraryDOsPromoFare.addAll(airPriceQuoteDO.vecPricedItineraryDOs);

								if(airAvailabilityDOReturn.vecPricedItineraryDOsPromoFare.get(0).vecPTC_FareBreakdownDOs != null)
								{
									AppConstants.bookingFlightDO.myODIDOReturn.mPTC_FareBreakdownDOPromo = 
											airAvailabilityDOReturn
											.vecPricedItineraryDOsPromoFare.get(0)
											.vecPTC_FareBreakdownDOs.get(0);

									AppConstants.bookingFlightDO.myODIDOReturn.arlBundledServiceDOs = findCommonBundles(airAvailabilityDOReturn
											.vecPricedItineraryDOsPromoFare.get(0)
											.arlBundledServiceDOs);
									//									AppConstants.bookingFlightDO.myODIDOReturn.arlBundledServiceDOs = airAvailabilityDOReturn
									//											.vecPricedItineraryDOsPromoFare.get(0)
									//											.arlBundledServiceDOs;

									LogUtils.errorLog("Oneway Price Called",AppConstants.bookingFlightDO.myODIDOReturn.mPTC_FareBreakdownDOPromo.totalFare.amount+"" );
					//===========Added on 20-Feb-2018 by Mukesh to calculate Total price without Admin Fee==========================================
									PTC_FareBreakdownDO fareBreakDownDos = AppConstants.bookingFlightDO.myODIDOReturn.mPTC_FareBreakdownDOPromo;
									float adminFees = 0, totalAmount = 0;

									for (int i=0; i<fareBreakDownDos.vecFees.size(); i++){

										if (fareBreakDownDos.vecFees.get(i).feeCode.equalsIgnoreCase("CC/Transaction Fees"))
											adminFees = StringUtils.getFloat(fareBreakDownDos.vecFees.get(i).amount.toString());
									}

									totalAmount = StringUtils.getFloat(fareBreakDownDos.totalFare.amount.toString());

									if (adminFees != 0.0)
										totalAmount -= adminFees;

									returnTotalPrice = totalAmount;
					//=======================================================================================================================================
									updateTotalPrice(true);
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
				//===========Added on 20-Feb-2018 by Mukesh to calculate Total price without Admin Fee==========================================
									PTC_FareBreakdownDO fareBreakDownDos = AppConstants.bookingFlightDO.myODIDOOneWay.mPTC_FareBreakdownDOPromo;
									float adminFees = 0, totalAmount = 0;

									for (int i=0; i<fareBreakDownDos.vecFees.size(); i++){

										if (fareBreakDownDos.vecFees.get(i).feeCode.equalsIgnoreCase("CC/Transaction Fees"))
											adminFees = StringUtils.getFloat(fareBreakDownDos.vecFees.get(i).amount.toString());
									}

									totalAmount = StringUtils.getFloat(fareBreakDownDos.totalFare.amount.toString());

									if (adminFees != 0.0)
										totalAmount -= adminFees;
									oneWayTotalPrice = totalAmount;
				//==============================================================================================================================
//									oneWayTotalPrice = StringUtils.getFloat(AppConstants.bookingFlightDO.myODIDOOneWay.mPTC_FareBreakdownDOPromo.totalFare.amount);
									updateTotalPrice(true);
								}
							}
						}
						else 
						{
							errorMsgPrice = airPriceQuoteDO.ErrorMsg;
							if(airPriceQuoteDO.ErrorMsg.equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
								showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
							else if(!TextUtils.isEmpty(airPriceQuoteDO.ErrorMsg))
							{
								errorMsgPrice = airPriceQuoteDO.ErrorMsg;
								showCustomDialog(getApplicationContext(), getString(R.string.Alert), errorMsgPrice, getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
								llTotalPrice.setVisibility(View.GONE);
								llFareBottomCondition.setVisibility(View.GONE);
								llFragmentFlights.setVisibility(View.GONE);
								tvNoDataFlightAll.setVisibility(View.VISIBLE);
								tvNoDataFlightAll.setTextColor(Color.RED);
								tvSelectFlightSub.setVisibility(View.GONE);
							}
							else
								showCustomDialog(getApplicationContext(), getString(R.string.Alert), errorMsgPrice, getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);

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
							updateTotalPrice(false);
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
							frFlightOneWay.DateSelectionOneWayFlight(vecForPriceShowing);
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
					showCustomDialog(SelectFlightActivityNew.this,
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
				showCustomDialog(SelectFlightActivityNew.this,
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



	private void updateList(AirAvailabilityDO airAvailabilityDO) {
		if (!airAvailabilityDO.echoToken.equalsIgnoreCase("")
				&& !airAvailabilityDO.primaryLangID.equalsIgnoreCase("")
				&& !airAvailabilityDO.sequenceNmbr.equalsIgnoreCase("")
				&& !airAvailabilityDO.transactionIdentifier.equalsIgnoreCase("")
				&& !airAvailabilityDO.version.equalsIgnoreCase(""))
		{
			if (!isReturnServiceCalled) {
				//			if (airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() <= 0 && airAvailabilityDO.vecOriginDestinationInformationDOs.size() > 0) {
				AppConstants.bookingFlightDO.requestParameterDO.srvUrlType = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
				AppConstants.bookingFlightDO.requestParameterDO.echoToken = airAvailabilityDO.echoToken;
				AppConstants.bookingFlightDO.requestParameterDO.primaryLangID = airAvailabilityDO.primaryLangID;
				AppConstants.bookingFlightDO.requestParameterDO.sequenceNmbr = airAvailabilityDO.sequenceNmbr;
				AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airAvailabilityDO.transactionIdentifier;
				AppConstants.bookingFlightDO.requestParameterDO.version = airAvailabilityDO.version;

				if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
				{
					AppConstants.bookingFlightDO.requestParameterDOReturn = new RequestParameterDO();

					AppConstants.bookingFlightDO.requestParameterDOReturn.srvUrlType = AppConstants.bookingFlightDO.myBookFlightDOReturn.srvUrlType;
					AppConstants.bookingFlightDO.requestParameterDOReturn.echoToken = airAvailabilityDO.echoToken;
					AppConstants.bookingFlightDO.requestParameterDOReturn.primaryLangID = airAvailabilityDO.primaryLangID;
					AppConstants.bookingFlightDO.requestParameterDOReturn.sequenceNmbr = airAvailabilityDO.sequenceNmbr;
					AppConstants.bookingFlightDO.requestParameterDOReturn.transactionIdentifier = airAvailabilityDO.transactionIdentifier;
					AppConstants.bookingFlightDO.requestParameterDOReturn.version = airAvailabilityDO.version;
				}

				OriginDestinationOptionDO originDestinationOptionDO = new OriginDestinationOptionDO();

				originDestinationOptionDO.vecFlightSegmentDOs = new Vector<FlightSegmentDO>();

				if(airAvailabilityDO.vecOriginDestinationInformationDOs != null 
						&& airAvailabilityDO.vecOriginDestinationInformationDOs.size() > 0
						&& airAvailabilityDO.vecOriginDestinationInformationDOs.get(0).vecOriginDestinationOptionDOs != null 
						&& airAvailabilityDO.vecOriginDestinationInformationDOs.get(0).vecOriginDestinationOptionDOs.size() > 0)
					for (int i = 0; i < airAvailabilityDO.vecOriginDestinationInformationDOs.get(0)
							.vecOriginDestinationOptionDOs.size(); i++) {

						originDestinationOptionDO.vecFlightSegmentDOs.addAll(
								airAvailabilityDO.vecOriginDestinationInformationDOs.get(0)
								.vecOriginDestinationOptionDOs.get(i)
								.vecFlightSegmentDOs);
					}
				AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.add(originDestinationOptionDO);

				frFlightOneWay.DateSelectionOneWayFlight(airAvailabilityDO,0);

				llFragmentFlights.setVisibility(View.VISIBLE);
				//				llTotalPrice.setVisibility(View.VISIBLE);
				tvSelectFlightSub.setVisibility(View.VISIBLE);
				tvNoDataFlightAll.setVisibility(View.GONE);
				tvNoDataFlightOneWay.setVisibility(View.GONE);

				if(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs != null
						&& AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size() > 0
						&& AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs != null
						&& AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs.size() > 0)
				{
					flightCountOneWay = 0;
					tvNoDataFlightOneWay.setVisibility(View.GONE);

					if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null
							&& flightCountReturn < 0)
					{
						llReturnFlightView.setVisibility(View.VISIBLE);
						viewSpace.setVisibility(View.VISIBLE);
						llTotalPrice.setVisibility(View.GONE);
						llFareBottomCondition.setVisibility(View.GONE);
						//===========newly added for No flight availability==============================================					
						tvSelectFlightSub.setVisibility(View.GONE);
						//						tvNoDataFlightReturn.setVisibility(View.VISIBLE);	
					}
				}
				else
				{
					flightCountOneWay = -1;

					//===========newly added for No flight availability==============================================					
					tvSelectFlightSub.setVisibility(View.GONE);
//					tvNoDataFlightOneWay.setVisibility(View.VISIBLE);
					tvNoDataFlightAll.setVisibility(View.VISIBLE);
					llFragmentFlights.setVisibility(View.GONE);
					if(AppConstants.bookingFlightDO.myBookFlightDOReturn == null)
					{
						showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), getResources().getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]+")."+getResources().getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
						updateTotalPrice(false);
					}
					else if(flightCountReturn < 0)
					{
						llFragmentFlights.setVisibility(View.GONE);
						llTotalPrice.setVisibility(View.GONE);
						llFareBottomCondition.setVisibility(View.GONE);
						tvSelectFlightSub.setVisibility(View.GONE);
						//						tvNoDataFlightAll.setVisibility(View.VISIBLE);
						//						tvNoDataFlightReturn.setVisibility(View.VISIBLE);	
					}
				}
			} 
			else
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

				frFlightReturn.DateSelectionReturnFlight(airAvailabilityDOReturn,0);

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

					if(flightCountOneWay == -1)
					{
						updateTotalPrice(false);
						showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
					}
				}
				else
				{
					flightCountReturn = -1;
					if(flightCountOneWay == -1)
					{
						llFragmentFlights.setVisibility(View.GONE);
						llTotalPrice.setVisibility(View.GONE);
						llFareBottomCondition.setVisibility(View.GONE);
						tvSelectFlightSub.setVisibility(View.GONE);
						tvNoDataFlightAll.setVisibility(View.VISIBLE);
						showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
					}
					else
					{
						tvSelectFlightSub.setVisibility(View.GONE);
						tvNoDataFlightReturn.setVisibility(View.VISIBLE);
						showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
					}
					updateTotalPrice(false);
				}
			}
		} 
		else
		{
			if (!isReturnServiceCalled) 
			{
				flightCountOneWay = -1;
				//===========newly added for No flight availability==============================================					
				tvSelectFlightSub.setVisibility(View.GONE);
//				tvNoDataFlightOneWay.setVisibility(View.VISIBLE);
				tvNoDataFlightAll.setVisibility(View.VISIBLE);
				llFragmentFlights.setVisibility(View.GONE);
				frFlightOneWay.DateSelectionOneWayFlight(airAvailabilityDO,-1);
				AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.clear();

				if(AppConstants.bookingFlightDO.myBookFlightDOReturn == null)
				{
					showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
				}
				//				else if(flightCountReturn >= 0)
				//				{
				//					showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
				//				}
				else
				{
					llFragmentFlights.setVisibility(View.GONE);
					llTotalPrice.setVisibility(View.GONE);
					llFareBottomCondition.setVisibility(View.GONE);
					tvSelectFlightSub.setVisibility(View.GONE);
					tvNoDataFlightAll.setVisibility(View.VISIBLE);
					//					showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
				}
			}
			else 
			{
				flightCountReturn = -1;
				frFlightReturn.DateSelectionReturnFlight(airAvailabilityDOReturn,-1);
				AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.clear();

				if(flightCountOneWay == -1)
				{
					llFragmentFlights.setVisibility(View.GONE);
					llTotalPrice.setVisibility(View.GONE);
					llFareBottomCondition.setVisibility(View.GONE);
					tvSelectFlightSub.setVisibility(View.GONE);
					tvNoDataFlightAll.setVisibility(View.VISIBLE);
					showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
				}
				else
				{
					llFragmentFlights.setVisibility(View.VISIBLE);
					//					llTotalPrice.setVisibility(View.VISIBLE);
					tvSelectFlightSub.setVisibility(View.GONE);
					tvNoDataFlightReturn.setVisibility(View.VISIBLE);
					showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
				}
			}
			updateTotalPrice(false);
		}
	}


	private void clickLeftArrow() {

		mCal.add(Calendar.DAY_OF_MONTH, -1);
		if(!checkLangFrench())
		{
			tvDateOneWayCurrent.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,0)));
			tvDateOneWayPrev.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,-1)));
			tvDateOneWayNext.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,1)));
		}
		else
		{

			tvDateOneWayCurrent.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,0)).toString().replace(".", ""));
			tvDateOneWayPrev.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,-1)).toString().replace(".", ""));
			tvDateOneWayNext.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,1)).toString().replace(".", ""));
		
		}

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

		callFlightListServiceOneWay();
		if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
			isValuesToShow = true;
		//		if(airAvailabilityDOReturn.vecOriginDestinationInformationDOs != null && airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() > 0 && airAvailabilityDO.vecOriginDestinationInformationDOs != null &&
		//				airAvailabilityDO.vecOriginDestinationInformationDOs.size() > 0) {
		//			
		////			updateList(airAvailabilityDOReturn);
		//			llTotalPrice.setVisibility(View.VISIBLE);
		//			llFragmentFlights.setVisibility(View.VISIBLE);
		//			tvNoDataFlightAll.setVisibility(View.GONE);
		//			tvSelectFlightSub.setVisibility(View.VISIBLE);
		//			isValuesToShow = true;
		////			updateList(airAvailabilityDO);
		//		}else {
		//			showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
		//			hideLoader();
		//			llTotalPrice.setVisibility(View.GONE);
		//			llFragmentFlights.setVisibility(View.GONE);
		//			tvNoDataFlightAll.setVisibility(View.VISIBLE);
		//			tvNoDataFlightAll.setTextColor(Color.RED);
		//			tvSelectFlightSub.setVisibility(View.GONE);
		//		}
	}

	private void clickRightArrow() {

		mCal.add(Calendar.DAY_OF_MONTH, 1);
		if(!checkLangFrench())
		{
			tvDateOneWayCurrent.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,0)).toString());
			tvDateOneWayPrev.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,-1)).toString());
			tvDateOneWayNext.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,1)).toString());
		}
		else
		{
			tvDateOneWayCurrent.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,0)).toString().replace(".", ""));
			tvDateOneWayPrev.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,-1)).toString().replace(".", ""));
			tvDateOneWayNext.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(mCal,1)).toString().replace(".", ""));
		}

		updateBookFlightDates();

		isDateChanged = true;

		AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();
		callFlightListServiceOneWay();

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
		if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
			isValuesToShow = true;
		//		if(airAvailabilityDOReturn.vecOriginDestinationInformationDOs != null && airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() > 0 && airAvailabilityDO.vecOriginDestinationInformationDOs != null &&
		//				airAvailabilityDO.vecOriginDestinationInformationDOs.size() > 0) {
		//			
		////			updateList(airAvailabilityDOReturn);
		//			llTotalPrice.setVisibility(View.VISIBLE);
		//			llFragmentFlights.setVisibility(View.VISIBLE);
		//			tvNoDataFlightAll.setVisibility(View.GONE);
		//			tvSelectFlightSub.setVisibility(View.VISIBLE);
		////			updateList(airAvailabilityDO);
		//		}else {
		//			showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
		//			hideLoader();
		//			llTotalPrice.setVisibility(View.GONE);
		//			llFragmentFlights.setVisibility(View.GONE);
		//			tvNoDataFlightAll.setVisibility(View.VISIBLE);
		//			tvNoDataFlightAll.setTextColor(Color.RED);
		//			tvSelectFlightSub.setVisibility(View.GONE);
		//		}
	}

	private void clickLeftArrowReturn() {

		retCal.add(Calendar.DAY_OF_MONTH, -1);
		if(!checkLangFrench())
		{
			tvDateReturnCurrent.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,0)));
			tvDateReturnPrev.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,-1)));
			tvDateReturnNext.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,1)));
		}
		else
		{
			tvDateReturnCurrent.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,0)).toString().replace(".", ""));
			tvDateReturnPrev.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,-1)).toString().replace(".", ""));
			tvDateReturnNext.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,1)).toString().replace(".", ""));
		
		}

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
		if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
			isValuesToShow = true;
		//		if(airAvailabilityDOReturn.vecOriginDestinationInformationDOs != null && airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() > 0 && airAvailabilityDO.vecOriginDestinationInformationDOs != null &&
		//				airAvailabilityDO.vecOriginDestinationInformationDOs.size() > 0) {
		//			
		////			updateList(airAvailabilityDOReturn);
		//			llTotalPrice.setVisibility(View.VISIBLE);
		//			llFragmentFlights.setVisibility(View.VISIBLE);
		//			tvNoDataFlightAll.setVisibility(View.GONE);
		//			tvSelectFlightSub.setVisibility(View.VISIBLE);
		////			updateList(airAvailabilityDO);
		//		}else {
		//			showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
		//			hideLoader();
		//			llTotalPrice.setVisibility(View.GONE);
		//			llFragmentFlights.setVisibility(View.GONE);
		//			tvNoDataFlightAll.setVisibility(View.VISIBLE);
		//			tvNoDataFlightAll.setTextColor(Color.RED);
		//			tvSelectFlightSub.setVisibility(View.GONE);
		//		}
	}

	private void clickRightArrowReturn() {

		retCal.add(Calendar.DAY_OF_MONTH, 1);
		if(!checkLangFrench())
		{
			tvDateReturnCurrent.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,0)));
			tvDateReturnPrev.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,-1)));
			tvDateReturnNext.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,1)));
		}
		else
		{
			tvDateReturnCurrent.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,0)).toString().replace(".", ""));
			tvDateReturnPrev.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,-1)).toString().replace(".", ""));
			tvDateReturnNext.setText(getFirstLetterCapital(CalendarUtility.getSelectFlightDateNew(retCal,1)).toString().replace(".", ""));
		
		}

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
		if(AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
			isValuesToShow = true;
		//		if(airAvailabilityDOReturn.vecOriginDestinationInformationDOs != null && airAvailabilityDOReturn.vecOriginDestinationInformationDOs.size() > 0 && airAvailabilityDO.vecOriginDestinationInformationDOs != null &&
		//				airAvailabilityDO.vecOriginDestinationInformationDOs.size() > 0) {
		//			
		////			updateList(airAvailabilityDOReturn);
		//			llTotalPrice.setVisibility(View.VISIBLE);
		//			llFragmentFlights.setVisibility(View.VISIBLE);
		//			tvNoDataFlightAll.setVisibility(View.GONE);
		//			tvSelectFlightSub.setVisibility(View.VISIBLE);
		////			updateList(airAvailabilityDO);
		//		}else {
		//			showCustomDialog(SelectFlightActivityNew.this, getString(R.string.Alert), getString(R.string.Alert_MSG_NoFlightAvailable1)+" ("+AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime.split("T")[0]+")."+getString(R.string.Alert_MSG_NoFlightAvailable2), getString(R.string.Ok), "", "");
		//			hideLoader();
		//			llTotalPrice.setVisibility(View.GONE);
		//			llFragmentFlights.setVisibility(View.GONE);
		//			tvNoDataFlightAll.setVisibility(View.VISIBLE);
		//			tvNoDataFlightAll.setTextColor(Color.RED);
		//			tvSelectFlightSub.setVisibility(View.GONE);
		//		}
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
			{
				llTotalPrice.setVisibility(View.VISIBLE);
				llFareBottomCondition.setVisibility(View.VISIBLE);
				llFareBottomCondition.setVisibility(View.VISIBLE);
			}
			else
			{
				llTotalPrice.setVisibility(View.GONE);
				llFareBottomCondition.setVisibility(View.GONE);
				if(tvNoDataFlightOneWay.getVisibility() == View.GONE){
					llFareBottomCondition.setVisibility(View.VISIBLE);
					llTotalPrice.setVisibility(View.VISIBLE);
				}
				//llFareBottomCondition.setVisibility(View.VISIBLE);
			}
		}
		else
		{
			totalPrice = 0;
			btnSubmitNext.setVisibility(View.GONE);
			llTotalPrice.setVisibility(View.GONE);
			llFareBottomCondition.setVisibility(View.GONE);
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
		llFareBottomCondition.setVisibility(View.GONE);
		
		if(tvNoDataFlightOneWay.getVisibility() == View.GONE) {
			llFareBottomCondition.setVisibility(View.VISIBLE);
			llTotalPrice.setVisibility(View.VISIBLE);
		}
		
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

	private void callFlightListServiceOneWay() {

		airAvailabilityDO= null;
		airAvailabilityDO = new AirAvailabilityDO();
		airAvailabilityDO.vecPricedItineraryDOsPromoFare = null;
		airAvailabilityDO.vecPricedItineraryDOsFlexiFare = null;

		airAvailabilityDO.vecOriginDestinationInformationDOs = new Vector<OriginDestinationInformationDO>();

		AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();
		
		// Added to get srvurltype when pnr is available by ibr.
		if(!AppConstants.bookingFlightDO.pnr.isEmpty())
		{
			char route=AppConstants.bookingFlightDO.pnr.charAt(0);
			switch(route)
			{
				case '1':
					AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType="9P";
					break;
				case '3':
				case '4':
				case '9':
					AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType="G9";
					break;
				case '7':
					AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType="E5";
					break;
				case '5':
					AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType="3O";
					break;
			}
		}

		if (new CommonBL(SelectFlightActivityNew.this, SelectFlightActivityNew.this)
		.getAirAvailability(AppConstants.bookingFlightDO.myBookFlightDO,
				AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs))
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
		.getAirAvailabilityReturn(
				AppConstants.bookingFlightDO.myBookFlightDOReturn,
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

		AppConstants.bookingFlightDO.myODIDOOneWay = airAvailabilityDO.vecOriginDestinationInformationDOs.get(flightCount);

		if(isManageBook)
		{
			if (new CommonBL(SelectFlightActivityNew.this, SelectFlightActivityNew.this)
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
			if (new CommonBL(SelectFlightActivityNew.this, SelectFlightActivityNew.this)
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
				
				//New Change..
				
				if(!AppConstants.transactionIdentifier.equals(""))
					AppConstants.bookingFlightDO.requestParameterDOReturn.transactionIdentifier = AppConstants.transactionIdentifier;
				

				if(isManageBook)
				{
					if (new CommonBL(SelectFlightActivityNew.this, SelectFlightActivityNew.this)
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
					if (new CommonBL(SelectFlightActivityNew.this, SelectFlightActivityNew.this)
					.getAirPriceQuote(
							AppConstants.bookingFlightDO.requestParameterDOReturn,
							AppConstants.AIRPORT_CODE,
							AppConstants.bookingFlightDO.myBookFlightDOReturn,
							null,
							AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
							AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs,
							false, false, "", "",null,null,"")) {
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
		else if(from.equalsIgnoreCase(AppConstants.INTERNET_PROBLEM))
		{
			Intent i = new Intent(SelectFlightActivityNew.this,BookFlightActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
		}
	}

	/*public String getFirstLetterCapital(String str)
	{
		char c=str.charAt(0);
		char[] tmp=str.toCharArray();
		c=Character.toUpperCase(c);
		tmp[0]=c;
		return String.valueOf(tmp);

	}*/

	ArrayList<BundledServiceDO> findCommonBundles(ArrayList<BundledServiceDO> oneWayList) {
		ArrayList<BundledServiceDO> list = new ArrayList<BundledServiceDO>();

		ArrayList<BundledServiceDO> list1 = new ArrayList<BundledServiceDO>();

		if(oneWayList != null && oneWayList.size() > 0)
		{
			for(int i = 0;i<oneWayList.size();i++) {

				for(int j=i+1 ; j<oneWayList.size() ; j++) {
                     String str=oneWayList.get(j).bundledServiceName.replaceAll("\\s+","");
					//if(oneWayList.get(i).bunldedServiceId.equalsIgnoreCase(oneWayList.get(j).bunldedServiceId)) {
					if(oneWayList.get(i).bundledServiceName.toLowerCase().contains(str.toLowerCase())) {
						list.add(oneWayList.get(i));
						list1.add(oneWayList.get(j));
						break;
					}
				}
			}
		}
		else
			list = oneWayList;

		if(list1.size() > 0)
			list.addAll(list1);

		return list;
	}

	public void showUmrahMessage(OriginDestinationOptionDO originDestinationOptionDO, String way)
	{
		int flightLength = 0;
		if(originDestinationOptionDO!=null && originDestinationOptionDO.vecFlightSegmentDOs!=null && originDestinationOptionDO.vecFlightSegmentDOs.size()>0)
		{
			flightLength = originDestinationOptionDO.vecFlightSegmentDOs.get(0).flightNumber.length();
		}

		if(AppConstants.bookingFlightDO != null && AppConstants.bookingFlightDO.myODIDOReturn!=null) {
			if (way.equalsIgnoreCase(AppConstants.ONE_WAY)) {
				if (flightLength == 6)
					isOneWayFlight = true;
				else
					isOneWayFlight = false;
			} else {
				if (flightLength == 6)
					isReturnFlight = true;
				else
					isReturnFlight = true;
			}
			if (isOneWayFlight && isReturnFlight)
				llHajj.setVisibility(View.VISIBLE);
			else
				llHajj.setVisibility(View.GONE);
		}
		else
		{
			if(flightLength == 6)
				llHajj.setVisibility(View.VISIBLE);
			else
				llHajj.setVisibility(View.GONE);
		}
	}

}