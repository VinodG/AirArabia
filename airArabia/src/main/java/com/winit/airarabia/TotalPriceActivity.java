package com.winit.airarabia;

import java.util.Vector;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.insider.android.insider.Insider;
import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.insider.GcmBroadcastReceiver;
import com.winit.airarabia.objects.AirPriceQuoteDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;
import com.winit.airarabia.objects.PTC_FareBreakdownDO;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.utils.StringUtils;
import com.winit.airarabia.webaccess.Response;

public class TotalPriceActivity extends BaseActivity implements DataListener {

	private LinearLayout llTotalPrice, llInSrcDest, llReturnDiscount,
	llNumOfPersonsADT,llNumOfPersonsCHD,llNumOfPersonsINF,llTaxNSurcharge, llOutSrcDest,llOutFilghtDetails,llInFilghtDetails;
	
	private ImageView ivTermsCheck;
	private TextView tvTermsCheckNew,tvPriceTotal, tvTermsCheck, tvReturnDiscount;
	private LinearLayout llTermsCheck;
	private float baseFareADT = 0, baseFareCHD = 0, baseFareINF = 0,totalPrice = 0, totalTax = 0, totalPriceForUI = 0;

	private AirPriceQuoteDO airPriceQuoteDOTotal;

	private final String TERMSNCOND = "TERMSNCOND";
	private final String DATAFAIL = "DATAFAIL";
	
	private float totalAdultPrice=0, totalChildPrice=0, totalInfantPrice=0, totalTaxPrice=0;
	
	private TextView tvPersonsCount,
	tvNumOfPersonsADT,personColan,tvNumOfPersonsCHD, tvNumOfPersonsINF,
	tvPricePersonADT,tvPricePersonCHD,tvPricePersonINF,tvPriceTax, tvPriceTotalTag, tvColan,tv_pricingname,tvOutText,tvInText;

	private TotalPriceActivity.BCR bcr;

	private float adtAdminFee,chdAdminFee,infAdminFee;

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
	public void initilize() {
		tvHeaderTitle.setText(getString(R.string.FlightSummary));
		
		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(getString(R.string.Continue));
		
		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		intentFilter.addAction(AppConstants.BOOK_SUCCESS);
		registerReceiver(bcr, intentFilter);
		llTotalPrice = (LinearLayout) layoutInflater.inflate(R.layout.totalprice, null);
		llMiddleBase.addView(llTotalPrice, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		
		tvPriceTotal = (TextView) llTotalPrice.findViewById(R.id.tvPriceTotal);
		tvReturnDiscount = (TextView) llTotalPrice.findViewById(R.id.tvReturnBookingDiscount);
		tvTermsCheck = (TextView) llTotalPrice.findViewById(R.id.tvTermsCheck);
		tvTermsCheckNew = (TextView) llTotalPrice.findViewById(R.id.tvTermsCheckNew);
		llTermsCheck = (LinearLayout) llTotalPrice.findViewById(R.id.llTermsCheck);
		ivTermsCheck = (ImageView) llTotalPrice.findViewById(R.id.ivTermsCheck);
		llReturnDiscount = (LinearLayout) llTotalPrice.findViewById(R.id.llReturnDiscount);
		
		//new ids
		llOutSrcDest = (LinearLayout) llTotalPrice.findViewById(R.id.llOutSrcDest);
		llInSrcDest = (LinearLayout) llTotalPrice.findViewById(R.id.llInSrcDest);
		
		llOutFilghtDetails = (LinearLayout) llTotalPrice.findViewById(R.id.llOutFilghtDetails);
		llInFilghtDetails = (LinearLayout) llTotalPrice.findViewById(R.id.llInFilghtDetails);
		
		llNumOfPersonsADT = (LinearLayout) llTotalPrice.findViewById(R.id.llNumOfPersonsADT);
		llNumOfPersonsCHD = (LinearLayout) llTotalPrice.findViewById(R.id.llNumOfPersonsCHD);
		llNumOfPersonsINF = (LinearLayout) llTotalPrice.findViewById(R.id.llNumOfPersonsINF);
		llTaxNSurcharge = (LinearLayout) llTotalPrice.findViewById(R.id.llTaxNSurcharge);
		
		tvPersonsCount = (TextView) llTotalPrice.findViewById(R.id.tvPersonsCount);
		
		tvNumOfPersonsADT = (TextView) llTotalPrice.findViewById(R.id.tvNumOfPersonsADT);
		personColan 		= (TextView) llTotalPrice.findViewById(R.id.personColan);
		tvNumOfPersonsCHD = (TextView) llTotalPrice.findViewById(R.id.tvNumOfPersonsCHD);
		tvNumOfPersonsINF = (TextView) llTotalPrice.findViewById(R.id.tvNumOfPersonsINF);
		tv_pricingname = (TextView) llTotalPrice.findViewById(R.id.tv_pricingname);
		
		tvPricePersonADT = (TextView) llTotalPrice.findViewById(R.id.tvPricePersonADT);
		tvPricePersonCHD = (TextView) llTotalPrice.findViewById(R.id.tvPricePersonCHD);
		tvPricePersonINF = (TextView) llTotalPrice.findViewById(R.id.tvPricePersonINF);
		tvPriceTax = (TextView) llTotalPrice.findViewById(R.id.tvPriceTax);
		tvPriceTotalTag = (TextView) llTotalPrice.findViewById(R.id.tvPriceTotalTag);
		tvColan 		= (TextView) llTotalPrice.findViewById(R.id.tvColan);
		tvOutText 		= (TextView) llTotalPrice.findViewById(R.id.tvOutText);
		tvInText 		= (TextView) llTotalPrice.findViewById(R.id.tvInText);
		airPriceQuoteDOTotal = new AirPriceQuoteDO();
		
		setTypeFaceOpenSansLight(llTotalPrice);
		tvPersonsCount.setTypeface(typefaceOpenSansRegular);
		tvPriceTotalTag.setTypeface(typefaceOpenSansRegular);	
		tvColan.setTypeface(typefaceOpenSansRegular);
		tvPriceTotal.setTypeface(typefaceOpenSansSemiBold);
		tv_pricingname.setTypeface(typefaceOpenSansSemiBold);
		tvOutText.setTypeface(typefaceOpenSansSemiBold);
		tvInText.setTypeface(typefaceOpenSansSemiBold);
		tvNumOfPersonsADT.setTypeface(typefaceOpenSansRegular);
		personColan.setTypeface(typefaceOpenSansRegular);
		tvPricePersonADT.setTypeface(typefaceOpenSansRegular);
		setTypefaceOpenSansRegular(llNumOfPersonsADT);
		setTypefaceOpenSansRegular(llNumOfPersonsCHD);
		setTypefaceOpenSansRegular(llNumOfPersonsINF);
		setTypefaceOpenSansRegular(llTaxNSurcharge);
		
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

		callServiceTotalPriceTotal();

		if (AppConstants.bookingFlightDO.myODIDOReturn == null)
			showOutBoundView();
		else
		{
			showOutBoundView();
			showInBoundView();
		}

		btnSubmitNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if ((Boolean) ivTermsCheck.getTag()) {
								Insider.tagEvent(AppConstants.ContinueFlightSummary, TotalPriceActivity.this);
				//Insider.Instance.tagEvent(TotalPriceActivity.this,AppConstants.ContinueFlightSummary);
				trackEvent("Flight Summary Screen", AppConstants.ContinueFlightSummary, "");
					if (airPriceQuoteDOTotal != null
							&& airPriceQuoteDOTotal.vecPricedItineraryDOs != null
							&& airPriceQuoteDOTotal.vecPricedItineraryDOs.size() > 0) {
						if (AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify != null
								&& AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.size() > 0)
							moveToPersonaliseTrip();
						else
							moveToPassengerInfo();
					} else
						showCustomDialog(TotalPriceActivity.this,
								getString(R.string.Alert),
								getString(R.string.TechProblem),
								getString(R.string.Ok), "", DATAFAIL);
//				} else
//					showCustomDialog(TotalPriceActivity.this,
//							getString(R.string.Alert),
//							getString(R.string.TermsErrorMessage),
//							getString(R.string.Ok), "", "TERMSNCOND");
			}
		});

		llTermsCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ivTermsCheck.performClick();
			}
		});
		tvTermsCheckNew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ivTermsCheck.performClick();
			}
		});
		tvTermsCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTermsNcond();
			}
		});

		ivTermsCheck.setTag(false);
		ivTermsCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!(Boolean) v.getTag()) {
					ivTermsCheck.setTag(true);
					ivTermsCheck.setImageResource(R.drawable.check);
				} else {
					ivTermsCheck.setTag(false);
					ivTermsCheck.setImageResource(R.drawable.uncheck);
				}
			}
		});
	
		String strPersons;
		if(selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_FR))
		{
			 strPersons = getString(R.string.Passenger)+": "+"";
		}
		else
		{
			strPersons = getString(R.string.Passenger)+": "+"";
		}
		if (AppConstants.bookingFlightDO.myBookFlightDO.adtQty > 0) {
			
			if (AppConstants.bookingFlightDO.myBookFlightDO.adtQty < 2)
				strPersons = strPersons+ AppConstants.bookingFlightDO.myBookFlightDO.adtQty + " " + getString(R.string.adult_flight_summary);
			else
				strPersons = strPersons+ AppConstants.bookingFlightDO.myBookFlightDO.adtQty + " " + getString(R.string.Adultss);
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
		
		tvPersonsCount.setText(strPersons);
		
		tvNumOfPersonsADT.setText("");
		tvNumOfPersonsCHD.setText("");
		tvNumOfPersonsINF.setText("");
	}

	protected void moveToPassengerInfo() {
		Intent in = new Intent(TotalPriceActivity.this, PassengerInformationActivityNew.class);
		in.putExtra(AppConstants.AIRPORTS_PRICE_TOTAL, airPriceQuoteDOTotal);
		startActivity(in);
	}

	protected void moveToPersonaliseTrip() {
		Intent in = new Intent(TotalPriceActivity.this, PersonalizeyourTripActivityNew.class);
		in.putExtra(AppConstants.AIRPORTS_PRICE_TOTAL, airPriceQuoteDOTotal);
		startActivity(in);
	}

	@Override
	public void onButtonYesClick(String from) {
		super.onButtonYesClick(from);
		if (from.equalsIgnoreCase(TERMSNCOND)) {
			ivTermsCheck.setTag(true);
			ivTermsCheck.setImageResource(R.drawable.check);
		} else if (from.equalsIgnoreCase(DATAFAIL))
			clickHome();
	}

	private void showTermsNcond() {
		builder = new AlertDialog.Builder(TotalPriceActivity.this);
		LinearLayout view = (LinearLayout) layoutInflater.inflate(R.layout.termsndconditionspopup, null);
		LinearLayout llTerms = (LinearLayout) view.findViewById(R.id.llTerms);
		view.setLayoutParams(new LinearLayout.LayoutParams(AppConstants.DEVICE_WIDTH, AppConstants.DEVICE_HEIGHT));
		view.findViewById(R.id.ivCancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						aDialog.dismiss();
					}
				});
		view.findViewById(R.id.btnOk).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				aDialog.dismiss();
			}
		});

		builder.setView(view);
		builder.setCancelable(false);
		aDialog = builder.create();
		aDialog.show();
	}

	private void showOutBoundView() {

		Vector<FlightSegmentDO> vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
		for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size(); i++) {
			
			vecFlightSegmentDOs.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
		}
		for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
			
			FlightSegmentDO flightSegmentDO = vecFlightSegmentDOs.get(i);
			
			LinearLayout llFlightDetails = (LinearLayout) getLayoutInflater().inflate(R.layout.totalprice_flight_details, null); 
			TextView tvOriginName = (TextView) llFlightDetails.findViewById(R.id.tvOriginName);
			TextView tvDestName = (TextView) llFlightDetails.findViewById(R.id.tvDestName);
			
			TextView tvFlightOriginDate = (TextView) llFlightDetails.findViewById(R.id.tvFlightOriginDate);
			TextView tvFlightOriginTime = (TextView) llFlightDetails.findViewById(R.id.tvFlightOriginTime);
			TextView tvFlightDestDate = (TextView) llFlightDetails.findViewById(R.id.tvFlightDestDate);
			TextView tvFlightDestTime = (TextView) llFlightDetails.findViewById(R.id.tvFlightDestTime);
			TextView tvFlightNo = (TextView) llFlightDetails.findViewById(R.id.tvFlightNo);
			TextView tv_trav_dep = (TextView) llFlightDetails.findViewById(R.id.tv_trav_dep);
			TextView tv_trav_dep_col = (TextView) llFlightDetails.findViewById(R.id.tv_trav_dep_col);
			TextView tv_trav_arv = (TextView) llFlightDetails.findViewById(R.id.tv_trav_arv);
			TextView tv_trav_arv_col = (TextView) llFlightDetails.findViewById(R.id.tv_trav_arv_col);
			
//			tvOriginName.setText(flightSegmentDO.departureAirportCode);
//			tvDestName.setText(flightSegmentDO.arrivalAirportCode);

			//---getting Airport name by Code---// 
			if(updateAirportNameFromCode(flightSegmentDO.departureAirportCode,
					AppConstants.allAirportNamesDO.vecAirport, tvOriginName))
				continue;
				
			if(updateAirportNameFromCode(flightSegmentDO.arrivalAirportCode,
					AppConstants.allAirportNamesDO.vecAirport, tvDestName))
				continue;
				
			//---getting Airport name by Code---//
			
			
			String stt = tvOriginName.getText().toString();
			
			if(stt.length() > 8) {
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) tvOriginName.getLayoutParams();
				ll.width = getResources().getInteger(R.integer.orgn_width) ;
				ll.height = android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
				tvOriginName.setLayoutParams(ll);
			}
			
			
			setTypefaceOpenSansRegular(llFlightDetails);
			tvOriginName.setTypeface(typefaceOpenSansSemiBold);
			tvDestName.setTypeface(typefaceOpenSansSemiBold);
			tvFlightNo.setTypeface(typefaceOpenSansSemiBold);
			tv_trav_dep.setTypeface(typefaceOpenSansSemiBold);
			tv_trav_dep_col.setTypeface(typefaceOpenSansSemiBold);
			tv_trav_arv.setTypeface(typefaceOpenSansSemiBold);
			tv_trav_arv_col.setTypeface(typefaceOpenSansSemiBold);
			
			tvFlightOriginDate.setTypeface(typefaceOpenSansRegular);
			tvFlightOriginTime.setTypeface(typefaceOpenSansRegular);
			tvFlightDestDate.setTypeface(typefaceOpenSansRegular);
			tvFlightDestTime.setTypeface(typefaceOpenSansRegular);
			
			
			tvFlightOriginDate.setText(toTitleCase(CalendarUtility.getDateForSummary(flightSegmentDO.departureDateTime)));
			tvFlightOriginTime.setText(CalendarUtility.getTimeInHourMinuteFromDate(flightSegmentDO.departureDateTime));
			
			tvFlightDestDate.setText(toTitleCase(CalendarUtility.getDateForSummary(flightSegmentDO.arrivalDateTime)));
			tvFlightDestTime.setText(CalendarUtility.getTimeInHourMinuteFromDate(flightSegmentDO.arrivalDateTime));
			
			tvFlightNo.setText(flightSegmentDO.flightNumber);
			
			llOutFilghtDetails.addView(llFlightDetails);
		}
	}

	private void showInBoundView() {
		llInSrcDest.setVisibility(View.VISIBLE);
		llReturnDiscount.setVisibility(View.GONE);
		tvReturnDiscount.setVisibility(View.GONE);

		Vector<FlightSegmentDO> vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
		for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size(); i++) {
			
			vecFlightSegmentDOs.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
		}
		for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
			
			FlightSegmentDO flightSegmentDO = vecFlightSegmentDOs.get(i);
			
			LinearLayout llFlightDetails = (LinearLayout) getLayoutInflater().inflate(R.layout.totalprice_flight_details, null); 
			TextView tvOriginName = (TextView) llFlightDetails.findViewById(R.id.tvOriginName);
			TextView tvDestName = (TextView) llFlightDetails.findViewById(R.id.tvDestName);
			
			TextView tvFlightOriginDate = (TextView) llFlightDetails.findViewById(R.id.tvFlightOriginDate);
			TextView tvFlightOriginTime = (TextView) llFlightDetails.findViewById(R.id.tvFlightOriginTime);
			TextView tvFlightDestDate = (TextView) llFlightDetails.findViewById(R.id.tvFlightDestDate);
			TextView tvFlightDestTime = (TextView) llFlightDetails.findViewById(R.id.tvFlightDestTime);
			TextView tvFlightNo = (TextView) llFlightDetails.findViewById(R.id.tvFlightNo);
			TextView tv_trav_dep = (TextView) llFlightDetails.findViewById(R.id.tv_trav_dep);
			TextView tv_trav_dep_col = (TextView) llFlightDetails.findViewById(R.id.tv_trav_dep_col);
			TextView tv_trav_arv = (TextView) llFlightDetails.findViewById(R.id.tv_trav_arv);
			TextView tv_trav_arv_col = (TextView) llFlightDetails.findViewById(R.id.tv_trav_arv_col);
			
//			tvOriginName.setText(flightSegmentDO.departureAirportCode);
//			tvDestName.setText(flightSegmentDO.arrivalAirportCode);
			
			//---getting Airport name by Code---// 
			if(updateAirportNameFromCode(flightSegmentDO.departureAirportCode, AppConstants.allAirportNamesDO.vecAirport, tvOriginName))
				continue;
			if(updateAirportNameFromCode(flightSegmentDO.arrivalAirportCode, AppConstants.allAirportNamesDO.vecAirport, tvDestName))
				continue;
			//---getting Airport name by Code---//
			
			String stt = tvOriginName.getText().toString();
			
			if(stt.length() > 8) {
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) tvOriginName.getLayoutParams();
				ll.width = getResources().getInteger(R.integer.orgn_width) ;
				ll.height = android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
				tvOriginName.setLayoutParams(ll);
			}
			
			setTypefaceOpenSansRegular(llFlightDetails);
			tvOriginName.setTypeface(typefaceOpenSansSemiBold);
			tvDestName.setTypeface(typefaceOpenSansSemiBold);
			tvFlightNo.setTypeface(typefaceOpenSansSemiBold);
			tv_trav_dep.setTypeface(typefaceOpenSansSemiBold);
			tv_trav_dep_col.setTypeface(typefaceOpenSansSemiBold);
			tv_trav_arv.setTypeface(typefaceOpenSansSemiBold);
			tv_trav_arv_col.setTypeface(typefaceOpenSansSemiBold);
			
			
			tvFlightOriginDate.setTypeface(typefaceOpenSansRegular);
			tvFlightOriginTime.setTypeface(typefaceOpenSansRegular);
			tvFlightDestDate.setTypeface(typefaceOpenSansRegular);
			tvFlightDestTime.setTypeface(typefaceOpenSansRegular);
			
			tvFlightOriginDate.setText(toTitleCase(CalendarUtility.getDateForSummary(flightSegmentDO.departureDateTime)));
			tvFlightOriginTime.setText(CalendarUtility.getTimeInHourMinuteFromDate(flightSegmentDO.departureDateTime));
			tvFlightDestDate.setText(toTitleCase(CalendarUtility.getDateForSummary(flightSegmentDO.arrivalDateTime)));
			tvFlightDestTime.setText(CalendarUtility.getTimeInHourMinuteFromDate(flightSegmentDO.arrivalDateTime));
			
			tvFlightNo.setText(flightSegmentDO.flightNumber);
			
			llInFilghtDetails.addView(llFlightDetails);
		}
	}

	private void callServiceTotalPriceTotal() {
		
		showLoader("");
		
		Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();
		Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsReturn = new Vector<OriginDestinationOptionDO>();

		if(AppConstants.bookingFlightDO != null && AppConstants.bookingFlightDO.myODIDOOneWay != null
				&& AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs != null
				&& AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size() > 0)
			vecOriginDestinationOptionDOs.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs);
		
		
		if (AppConstants.bookingFlightDO.myODIDOReturn != null
				&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0)
			vecOriginDestinationOptionDOsReturn.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs);

		
		
		if (AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify != null
				&& AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.size() > 0)
		{
			if (new CommonBL(TotalPriceActivity.this,
					TotalPriceActivity.this).getAirPriceQuote(
							AppConstants.bookingFlightDO.requestParameterDO,
							AppConstants.AIRPORT_CODE,
							AppConstants.bookingFlightDO.myBookFlightDO,
							AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
							vecOriginDestinationOptionDOs,
							null,
							AppConstants.bookingFlightDO.isFlexiOut, AppConstants.bookingFlightDO.isFlexiIn,
							AppConstants.bookingFlightDO.pnr, 
							AppConstants.bookingFlightDO.pnrType,
							null,null,
							AppConstants.bookingFlightDO.bundleServiceID)) {
			} else {
				hideLoader();
				showCustomDialog(this, getString(R.string.Alert),
						getString(R.string.InternetProblem),
						getString(R.string.Ok), "", "");
			}
		} 
		else 
		{
			if(AppConstants.bookingFlightDO.myODIDOReturn != null)
			{
				AppConstants.bookingFlightDO.myBookFlightDOReturn.travelType = AppConstants.TRAVEL_TYPE_RETURN;

				if (new CommonBL(TotalPriceActivity.this,
						TotalPriceActivity.this).getAirPriceQuote(
								AppConstants.bookingFlightDO.requestParameterDOReturn,
								AppConstants.AIRPORT_CODE,
								AppConstants.bookingFlightDO.myBookFlightDOReturn, null,
								vecOriginDestinationOptionDOs,
								vecOriginDestinationOptionDOsReturn,
								AppConstants.bookingFlightDO.isFlexiOut,
								AppConstants.bookingFlightDO.isFlexiIn,
								AppConstants.bookingFlightDO.pnr, 
								AppConstants.bookingFlightDO.pnrType,null,null,
								AppConstants.bookingFlightDO.bundleServiceID)) {
				} else {
					hideLoader();
					showCustomDialog(this, getString(R.string.Alert),
							getString(R.string.InternetProblem),
							getString(R.string.Ok), "", "");
				}
			}
			else
			{
				if (new CommonBL(TotalPriceActivity.this,
						TotalPriceActivity.this).getAirPriceQuote(
								AppConstants.bookingFlightDO.requestParameterDO,
								AppConstants.AIRPORT_CODE,
								AppConstants.bookingFlightDO.myBookFlightDO, null,
								vecOriginDestinationOptionDOs,
								null,
								AppConstants.bookingFlightDO.isFlexiOut, 
								AppConstants.bookingFlightDO.isFlexiIn,
								AppConstants.bookingFlightDO.pnr,
								AppConstants.bookingFlightDO.pnrType,null,null,
								AppConstants.bookingFlightDO.bundleServiceID)) {
				} else {
					hideLoader();
					showCustomDialog(this, getString(R.string.Alert),
							getString(R.string.InternetProblem),
							getString(R.string.Ok), "", "");
				}
			}
		}
	}

	@Override
	public void dataRetreived(Response data) {
		if (data != null && !data.isError) {
			switch (data.method) {
			case AIR_PRICEQUOTE:

				AirPriceQuoteDO airPriceQuoteDO = new AirPriceQuoteDO();
				airPriceQuoteDO = (AirPriceQuoteDO) data.data;
				
				if (tvPriceTotal.getText().toString().equalsIgnoreCase("")
						&& airPriceQuoteDO.vecPricedItineraryDOs != null
						&& airPriceQuoteDO.vecPricedItineraryDOs.size() > 0) {
					airPriceQuoteDOTotal = airPriceQuoteDO;
					
					totalPrice = StringUtils.getFloat(airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.amount);
					
					if (!airPriceQuoteDOTotal.transactionIdentifier.equalsIgnoreCase(""))
						AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airPriceQuoteDOTotal.transactionIdentifier;
					
					if (AppConstants.bookingFlightDO.myBookFlightDO.adtQty > 0) {
						llNumOfPersonsADT.setVisibility(View.VISIBLE);
						if (AppConstants.bookingFlightDO.myBookFlightDO.adtQty < 2)
							tvNumOfPersonsADT.setText(AppConstants.bookingFlightDO.myBookFlightDO.adtQty + " " + getString(R.string.adult_flight_summary));
						else
							tvNumOfPersonsADT.setText(AppConstants.bookingFlightDO.myBookFlightDO.adtQty + " " + getString(R.string.Adultss));
					}
					if (AppConstants.bookingFlightDO.myBookFlightDO.chdQty > 0) {
						llNumOfPersonsCHD.setVisibility(View.VISIBLE);
						if (AppConstants.bookingFlightDO.myBookFlightDO.chdQty < 2)
							tvNumOfPersonsCHD.setText(AppConstants.bookingFlightDO.myBookFlightDO.chdQty + " " + getString(R.string.Child));
						else
							tvNumOfPersonsCHD.setText(AppConstants.bookingFlightDO.myBookFlightDO.chdQty + " " + getString(R.string.Children));
					}
					if (AppConstants.bookingFlightDO.myBookFlightDO.infQty > 0) {
						llNumOfPersonsINF.setVisibility(View.VISIBLE);
						if (AppConstants.bookingFlightDO.myBookFlightDO.infQty < 2)
							tvNumOfPersonsINF.setText(AppConstants.bookingFlightDO.myBookFlightDO.infQty + " " + getString(R.string.Infant));
						else
							tvNumOfPersonsINF.setText(AppConstants.bookingFlightDO.myBookFlightDO.infQty + " " + getString(R.string.Infantss));
					}
					
//					totalPrice = StringUtils.getFloat(airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.amount);
//					totalPriceForUI = totalPriceForUI + totalPrice;
					
					for (int i = 0; i < airPriceQuoteDO.vecPricedItineraryDOs.get(0).vecPTC_FareBreakdownDOs.size(); i++) {
						PTC_FareBreakdownDO pTC_FareBreakdownDO = airPriceQuoteDO.vecPricedItineraryDOs.get(0).vecPTC_FareBreakdownDOs.get(i);
						if (pTC_FareBreakdownDO.code.equalsIgnoreCase(AppConstants.PERSON_TYPE_ADT))
						{
							baseFareADT = StringUtils.getFloat(pTC_FareBreakdownDO.baseFare.amount) * StringUtils.getInt(pTC_FareBreakdownDO.quantity);
							totalAdultPrice = totalAdultPrice + baseFareADT;

							//Added for getting admin fee and reduce it from totalfare==============================================
							for (int a=0; a<pTC_FareBreakdownDO.vecFees.size(); a++){
								if (pTC_FareBreakdownDO.vecFees.get(a).feeCode.equalsIgnoreCase("CC/Transaction Fees"))
									adtAdminFee = StringUtils.getFloat(pTC_FareBreakdownDO.vecFees.get(a).amount.toString());
							}
							if (adtAdminFee != 0){
								adtAdminFee *= StringUtils.getInt(pTC_FareBreakdownDO.quantity);
							}
							//======================================================================================================
						}
						if (pTC_FareBreakdownDO.code.equalsIgnoreCase(AppConstants.PERSON_TYPE_CHD))
						{
							baseFareCHD = StringUtils.getFloat(pTC_FareBreakdownDO.baseFare.amount) * StringUtils.getInt(pTC_FareBreakdownDO.quantity);
							totalChildPrice = totalChildPrice + baseFareCHD;

							//Added for getting admin fee and reduce it from totalfare==============================================
							for (int c=0; c<pTC_FareBreakdownDO.vecFees.size(); c++){
								if (pTC_FareBreakdownDO.vecFees.get(c).feeCode.equalsIgnoreCase("CC/Transaction Fees"))
									chdAdminFee = StringUtils.getFloat(pTC_FareBreakdownDO.vecFees.get(c).amount.toString());
							}
							if (chdAdminFee != 0){
								chdAdminFee *= StringUtils.getInt(pTC_FareBreakdownDO.quantity);
							}
							//======================================================================================================
						}
						if (pTC_FareBreakdownDO.code.equalsIgnoreCase(AppConstants.PERSON_TYPE_INF))
						{
							baseFareINF = StringUtils.getFloat(pTC_FareBreakdownDO.baseFare.amount) * StringUtils.getInt(pTC_FareBreakdownDO.quantity);
							totalInfantPrice = totalInfantPrice+ baseFareINF;
							//Added for getting admin fee and reduce it from totalfare==============================================
							for (int n=0; n<pTC_FareBreakdownDO.vecFees.size(); n++){
								if (pTC_FareBreakdownDO.vecFees.get(n).feeCode.equalsIgnoreCase("CC/Transaction Fees"))
									infAdminFee = StringUtils.getFloat(pTC_FareBreakdownDO.vecFees.get(n).amount.toString());
							}
							if (infAdminFee != 0){
								infAdminFee *= StringUtils.getInt(pTC_FareBreakdownDO.quantity);
							}
							//======================================================================================================
						}
					}

					totalTax = totalPrice - (baseFareADT + baseFareCHD + baseFareINF);
					totalTaxPrice = totalTaxPrice + totalTax;

					totalTaxPrice -= (adtAdminFee+chdAdminFee+infAdminFee);
					totalPriceForUI = totalTaxPrice + baseFareADT + baseFareCHD + baseFareINF;
					
					String tempAdultPrice = updateCurrencyByFactor(totalAdultPrice+"", 0)+"";
					if (tempAdultPrice.contains(".00")) {
						tempAdultPrice = tempAdultPrice.replace(".00", "");
					}					
					tvPricePersonADT.setText(AppConstants.CurrencyCodeAfterExchange + " " +tempAdultPrice );
					
					String tempChildPrice = updateCurrencyByFactor(totalChildPrice+"", 0)+"";
					if (tempChildPrice.contains(".00")) {
						tempChildPrice = tempChildPrice.replace(".00", "");
					}
					tvPricePersonCHD.setText(AppConstants.CurrencyCodeAfterExchange + " " + tempChildPrice);
					
					String tempInfantPrice = updateCurrencyByFactor(totalInfantPrice+"", 0)+"";
					if (tempInfantPrice.contains(".00")) {
						tempInfantPrice = tempInfantPrice.replace(".00", "");
					}
					tvPricePersonINF.setText(AppConstants.CurrencyCodeAfterExchange + " " + tempInfantPrice);
					
					String tempTaxPrice = updateCurrencyByFactor(totalTaxPrice+"", 0)+"";
					if (tempTaxPrice.contains(".00")) {
						tempTaxPrice = tempTaxPrice.replace(".00", "");
					}
					tvPriceTax.setText(AppConstants.CurrencyCodeAfterExchange + " " + tempTaxPrice);
					
					String tempPriceForUI = updateCurrencyByFactor(totalPriceForUI+"", 0)+"";
					if (tempPriceForUI.contains(".00")) {
						tempPriceForUI = tempPriceForUI.replace(".00", "");
					}
					tvPriceTotal.setText(AppConstants.CurrencyCodeAfterExchange + " " +tempPriceForUI);

					hideLoader();
				} else {
					hideLoader();
					showCustomDialog(TotalPriceActivity.this,
							getString(R.string.Alert),
							getString(R.string.TechProblem),
							getString(R.string.Ok), "", DATAFAIL);
				}
				break;
			default:
				break;
			}
		} else {
			hideLoader();
			if(data.data instanceof String)
				showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.TechProblem), getString(R.string.Ok), "", DATAFAIL);
			else
				showCustomDialog(TotalPriceActivity.this, getString(R.string.Alert), getString(R.string.TechProblem), getString(R.string.Ok), "", DATAFAIL);
		}
	}
	
	
}