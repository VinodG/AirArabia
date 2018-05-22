package com.winit.airarabia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import android.app.ActionBar.LayoutParams;
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
import com.winit.airarabia.controls.CustomDialog;
import com.winit.airarabia.insider.GcmBroadcastReceiver;
import com.winit.airarabia.objects.AirBookDO;
import com.winit.airarabia.objects.AirPriceQuoteDO;
import com.winit.airarabia.objects.FareDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.ModifiedPNRResDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;
import com.winit.airarabia.objects.PTC_FareBreakdownDO;
import com.winit.airarabia.objects.PricedItineraryDO;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.utils.StringUtils;
import com.winit.airarabia.webaccess.Response;

public class BookingSummaryActivity extends BaseActivity implements DataListener {

	private LinearLayout llBookingsummary, llInboundData, llInboundPlaces, llBaggageCost, llSeatsCost, llInsuranceCost,
			llMealsCost, llHalaCost, llOutSrcDest, llOutData, llAdditionalServices, llPassangerTop;
	private TextView tvBookSummaryPassengers, tvBookSummarySourceOut, tvBookSummaryDestOut, tvBookSummaryDepatureOut,
			tvBookSummaryDepatureTimeOut, tvBookSummaryArrivalTimeOut, tvBookSummaryDepatureTimeIn,
			tvBookSummaryArrivalTimeIn, tvTotalFlightCost, tvBookSummarySourceIn, tvBookSummaryDestIn,
			tvBookSummaryDepatureIn, tvBookSummaryTotal, tvPassengerCountTitle, tvBaggageCost, tvMealsCost,
			tvInsuranceCost, tvSeatsCost, tvHalaCost, /* tvOutText, */tvAdditionalServicesHeader,
			tvFlightConnectTypeOut, tvFlightConnectTypeIn, tvTime1DOut, tvTime1DIn, tvTotalFlightTag;
	private String priceCode = "";
	private static Calendar calenderDeparture;
	private float totalprice = 0, totalpriceOutBound = 0, totalPriceInOutBound = 0;
	private AirPriceQuoteDO airPriceQuoteDO;
	private Vector<FareDO> vecFareDOMeals, vecFareDOBaggages, vecFareDOSeats, vecFareDOInsurance, vecFareDOHala;

	private ImageView ivFlightInfoOneWay, ivFlightInfoReturn, ivFlightConnectTypeOut, ivFlightConnectTypeIn;
	private boolean isManageBooking = false;

	// private final String FLIGHT_BOOK = "FLIGHT_BOOK";
	private final String DATAFAIL = "DATAFAIL";

	private BookingSummaryActivity.BCR bcr;
	private TextView tvFlight, tvBaggage, tvSeats, tvMeals, tvAirportServices, tvTravelSecure;

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
		tvHeaderTitle.setText(getString(R.string.PriceSummary));

		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		intentFilter.addAction(AppConstants.BOOK_SUCCESS);
		registerReceiver(bcr, intentFilter);

		llBookingsummary = (LinearLayout) layoutInflater.inflate(R.layout.bookingsummary, null);
		llMiddleBase.addView(llBookingsummary, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		tvBookSummaryPassengers = (TextView) llBookingsummary.findViewById(R.id.tvBookSummaryPassengers);
		tvPassengerCountTitle = (TextView) llBookingsummary.findViewById(R.id.tvPassengerCountTitle);
		tvBookSummarySourceOut = (TextView) llBookingsummary.findViewById(R.id.tvBookSummarySourceOut);
		tvBookSummaryDestOut = (TextView) llBookingsummary.findViewById(R.id.tvBookSummaryDestOut);
		tvBookSummaryDepatureOut = (TextView) llBookingsummary.findViewById(R.id.tvBookSummaryDepatureOut);
		tvBookSummaryDepatureTimeOut = (TextView) llBookingsummary.findViewById(R.id.tvBookSummaryDepatureTimeOut);
		tvBookSummaryArrivalTimeOut = (TextView) llBookingsummary.findViewById(R.id.tvBookSummaryArrivalTimeOut);
		tvBookSummarySourceIn = (TextView) llBookingsummary.findViewById(R.id.tvBookSummarySourceIn);
		tvBookSummaryDestIn = (TextView) llBookingsummary.findViewById(R.id.tvBookSummaryDestIn);
		tvBookSummaryDepatureIn = (TextView) llBookingsummary.findViewById(R.id.tvBookSummaryDepatureIn);
		tvBookSummaryDepatureTimeIn = (TextView) llBookingsummary.findViewById(R.id.tvBookSummaryDepatureTimeIn);
		tvBookSummaryArrivalTimeIn = (TextView) llBookingsummary.findViewById(R.id.tvBookSummaryArrivalTimeIn);
		llInboundData = (LinearLayout) llBookingsummary.findViewById(R.id.llInboundData);
		llInboundPlaces = (LinearLayout) llBookingsummary.findViewById(R.id.llInboundPlaces);
		tvBaggageCost = (TextView) llBookingsummary.findViewById(R.id.tvBaggageCost);
		tvMealsCost = (TextView) llBookingsummary.findViewById(R.id.tvMealsCost);
		tvBookSummaryTotal = (TextView) llBookingsummary.findViewById(R.id.tvBookSummaryTotal);
		tvInsuranceCost = (TextView) llBookingsummary.findViewById(R.id.tvInsuranceCost);
		tvSeatsCost = (TextView) llBookingsummary.findViewById(R.id.tvSeatsCost);
		tvHalaCost = (TextView) llBookingsummary.findViewById(R.id.tvHalaCost);
		llBaggageCost = (LinearLayout) llBookingsummary.findViewById(R.id.llBaggageCost);
		llSeatsCost = (LinearLayout) llBookingsummary.findViewById(R.id.llSeatsCost);
		llInsuranceCost = (LinearLayout) llBookingsummary.findViewById(R.id.llInsuranceCost);
		llMealsCost = (LinearLayout) llBookingsummary.findViewById(R.id.llMealsCost);
		llHalaCost = (LinearLayout) llBookingsummary.findViewById(R.id.llHalaCost);
		llOutSrcDest = (LinearLayout) llBookingsummary.findViewById(R.id.llOutSrcDest);
		llOutData = (LinearLayout) llBookingsummary.findViewById(R.id.llOutData);
		llAdditionalServices = (LinearLayout) llBookingsummary.findViewById(R.id.llAdditionalServices);
		llPassangerTop = (LinearLayout) llBookingsummary.findViewById(R.id.llPassangerTop);
		tvAdditionalServicesHeader = (TextView) llBookingsummary.findViewById(R.id.tvAdditionalServicesHeader);
		tvTotalFlightTag = (TextView) llBookingsummary.findViewById(R.id.tvTotalFlightTag);
		tvTotalFlightCost = (TextView) llBookingsummary.findViewById(R.id.tvTotalFlightCost);

		ivFlightInfoOneWay = (ImageView) llBookingsummary.findViewById(R.id.ivFlightInfoOneWay);
		ivFlightInfoReturn = (ImageView) llBookingsummary.findViewById(R.id.ivFlightInfoReturn);

		tvTime1DOut = (TextView) llBookingsummary.findViewById(R.id.tvTime1DOut);
		tvTime1DIn = (TextView) llBookingsummary.findViewById(R.id.tvTime1DIn);

		tvFlightConnectTypeOut = (TextView) llBookingsummary.findViewById(R.id.tvFlightConnectTypeOut);
		tvFlightConnectTypeIn = (TextView) llBookingsummary.findViewById(R.id.tvFlightConnectTypeIn);

		ivFlightConnectTypeOut = (ImageView) llBookingsummary.findViewById(R.id.ivFlightConnectTypeOut);
		ivFlightConnectTypeIn = (ImageView) llBookingsummary.findViewById(R.id.ivFlightConnectTypeIn);

		tvFlight = (TextView) llBookingsummary.findViewById(R.id.tvFlight);
		tvBaggage = (TextView) llBookingsummary.findViewById(R.id.tvBaggage);
		tvSeats = (TextView) llBookingsummary.findViewById(R.id.tvSeats);

		tvMeals = (TextView) llBookingsummary.findViewById(R.id.tvMeals);
		tvAirportServices = (TextView) llBookingsummary.findViewById(R.id.tvAirportServices);
		tvTravelSecure = (TextView) llBookingsummary.findViewById(R.id.tvTravelSecure);

		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(getString(R.string.Continue));

		vecFareDOMeals = new Vector<FareDO>();
		vecFareDOBaggages = new Vector<FareDO>();
		vecFareDOSeats = new Vector<FareDO>();
		vecFareDOInsurance = new Vector<FareDO>();
		vecFareDOHala = new Vector<FareDO>();

		setTypefaceOpenSansRegular(llBookingsummary);
		setTypefaceOpenSansRegular(llPassangerTop);

		// tvBookSummaryDepatureOut.setTypeface(typefaceOpenSansSemiBold);
		// tvBookSummaryDepatureIn.setTypeface(typefaceOpenSansSemiBold);
		// tvAdditionalServicesHeader.setTypeface(typefaceOpenSansSemiBold);
		// tvTotalFlightTag.setTypeface(typefaceOpenSansSemiBold);
		// tvBookSummaryPassengers.setTypeface(typefaceOpenSansSemiBold);
		// tvPassengerCountTitle.setTypeface(typefaceOpenSansSemiBold);
		// tvBookSummaryTotal.setTypeface(typefaceOpenSansSemiBold);
		// tvBookSummaryDepatureTimeOut.setTypeface(typefaceOpenSansSemiBold);
		// tvBookSummarySourceOut.setTypeface(typefaceOpenSansSemiBold);
		// tvBookSummaryArrivalTimeOut.setTypeface(typefaceOpenSansSemiBold);
		// tvBookSummaryDestOut.setTypeface(typefaceOpenSansSemiBold);
		// tvFlightConnectTypeOut.setTypeface(typefaceOpenSansSemiBold);
		//
		// tvBookSummaryDepatureTimeIn.setTypeface(typefaceOpenSansSemiBold);
		// tvBookSummarySourceIn.setTypeface(typefaceOpenSansSemiBold);
		// tvFlightConnectTypeIn.setTypeface(typefaceOpenSansSemiBold);
		// tvBookSummaryArrivalTimeIn.setTypeface(typefaceOpenSansSemiBold);
		// tvBookSummaryDestIn.setTypeface(typefaceOpenSansSemiBold);
		// tvTime1DIn.setTypeface(typefaceOpenSansSemiBold);
		//
		// tvFlight.setTypeface(typefaceOpenSansSemiBold);
		// tvBaggage.setTypeface(typefaceOpenSansSemiBold);
		// tvSeats.setTypeface(typefaceOpenSansSemiBold);
		// tvMeals.setTypeface(typefaceOpenSansSemiBold);
		// tvAirportServices.setTypeface(typefaceOpenSansSemiBold);
		// tvTravelSecure.setTypeface(typefaceOpenSansSemiBold);

		tvBookSummaryDepatureOut.setTypeface(typefaceOpenSansRegular);
		tvBookSummaryDepatureIn.setTypeface(typefaceOpenSansRegular);
		tvAdditionalServicesHeader.setTypeface(typefaceOpenSansRegular);
		tvTotalFlightTag.setTypeface(typefaceOpenSansRegular);
		tvBookSummaryPassengers.setTypeface(typefaceOpenSansRegular);
		tvPassengerCountTitle.setTypeface(typefaceOpenSansRegular);
		tvBookSummaryTotal.setTypeface(typefaceOpenSansRegular);
		tvBookSummaryDepatureTimeOut.setTypeface(typefaceOpenSansRegular);
		tvBookSummarySourceOut.setTypeface(typefaceOpenSansRegular);
		tvBookSummaryArrivalTimeOut.setTypeface(typefaceOpenSansRegular);
		tvBookSummaryDestOut.setTypeface(typefaceOpenSansRegular);
		tvFlightConnectTypeOut.setTypeface(typefaceOpenSansRegular);

		tvBookSummaryDepatureTimeIn.setTypeface(typefaceOpenSansRegular);
		tvBookSummarySourceIn.setTypeface(typefaceOpenSansRegular);
		tvFlightConnectTypeIn.setTypeface(typefaceOpenSansRegular);
		tvBookSummaryArrivalTimeIn.setTypeface(typefaceOpenSansRegular);
		tvBookSummaryDestIn.setTypeface(typefaceOpenSansRegular);
		tvTime1DIn.setTypeface(typefaceOpenSansRegular);

		tvFlight.setTypeface(typefaceOpenSansRegular);
		tvBaggage.setTypeface(typefaceOpenSansRegular);
		tvSeats.setTypeface(typefaceOpenSansRegular);
		tvMeals.setTypeface(typefaceOpenSansRegular);
		tvAirportServices.setTypeface(typefaceOpenSansRegular);
		tvTravelSecure.setTypeface(typefaceOpenSansRegular);
		calenderDeparture = Calendar.getInstance();

		
		  Insider insider = new Insider(); insider.openSession(this,
		 AppConstants.ProjectName); insider.registerInBackground(this,
		  AppConstants.GoogleProjectNumber);
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

		if (AppConstants.bookingFlightDO!=null && AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify != null
				&& AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.size() > 0)
			isManageBooking = true;

		priceCode = AppConstants.CurrencyCodeAfterExchange;

		callServiceBookingTotalPriceTotal();

		showPersonDetails();

		if (isManageBooking) {
			showOutBoundView();
		} else {
			if (AppConstants.bookingFlightDO.myODIDOReturn == null)
				showOutBoundView();
			else {
				showOutBoundView();
				showInBoundView();
			}
		}

		ivFlightInfoOneWay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Insider.tagEvent(AppConstants.FlightInfo, BookingSummaryActivity.this);
				//Insider.Instance.tagEvent( BookingSummaryActivity.this,AppConstants.FlightInfo);
				trackEvent("Price Summary", AppConstants.FlightInfo, "");

				getOutBoundDetails();
			}
		});

		ivFlightInfoReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Insider.tagEvent(AppConstants.FlightInfo, BookingSummaryActivity.this);
				//Insider.Instance.tagEvent( BookingSummaryActivity.this,AppConstants.FlightInfo);
				trackEvent("Price Summary", AppConstants.FlightInfo, "");
				getInboundDetails();
			}
		});

		btnSubmitNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Insider.tagEvent(AppConstants.BackButtonBookingSummary, BookingSummaryActivity.this);
				//Insider.Instance.tagEvent( BookingSummaryActivity.this,AppConstants.BackButtonBookingSummary);
				trackEvent("Price Summary", AppConstants.ContinueBookingSummary, "");

				if (airPriceQuoteDO != null && airPriceQuoteDO.vecPricedItineraryDOs != null
						&& airPriceQuoteDO.vecPricedItineraryDOs.size() > 0) {
					if (isManageBooking)
						callServiceModifiedResQuery();
					else
						moveToPaymentActivity();
				}
			}
		});
	}

	private void getOutBoundDetails() {
		LinearLayout llEmptyMain = (LinearLayout) getLayoutInflater().inflate(R.layout.empty_layout_dialog, null);
		LinearLayout llEmpty = (LinearLayout) llEmptyMain.findViewById(R.id.llEmptyDialog);
		ImageView ivClose = (ImageView) llEmptyMain.findViewById(R.id.ivClose);
		ImageView ivFlightIcon = (ImageView) llEmptyMain.findViewById(R.id.ivFlightIcon);
		TextView tvBookStatus = (TextView) llEmptyMain.findViewById(R.id.tvBookStatus);
		ivFlightIcon.setBackgroundResource(R.drawable.flight_oneway);
		tvBookStatus.setText(getString(R.string.Departing));
		for (int i = 0; i < vecFlightSegmentDOsOneWay.size(); i++) {

			FlightSegmentDO flightSegmentDO = vecFlightSegmentDOsOneWay.get(i);

			LinearLayout llFlightDetails = (LinearLayout) getLayoutInflater()
					.inflate(R.layout.totalprice_flight_details, null);
			TextView tvOriginName = (TextView) llFlightDetails.findViewById(R.id.tvOriginName);
			TextView tvDestName = (TextView) llFlightDetails.findViewById(R.id.tvDestName);

			TextView tvFlightOriginDate = (TextView) llFlightDetails.findViewById(R.id.tvFlightOriginDate);
			TextView tvFlightOriginTime = (TextView) llFlightDetails.findViewById(R.id.tvFlightOriginTime);
			TextView tvFlightDestDate = (TextView) llFlightDetails.findViewById(R.id.tvFlightDestDate);
			TextView tvFlightDestTime = (TextView) llFlightDetails.findViewById(R.id.tvFlightDestTime);
			TextView tvFlightNo = (TextView) llFlightDetails.findViewById(R.id.tvFlightNo);

			tvOriginName.setText(flightSegmentDO.departureAirportCode);
			tvDestName.setText(flightSegmentDO.arrivalAirportCode);
			// tvOriginName.setTypeface(typefaceOpenSansSemiBold);
			// tvDestName.setTypeface(typefaceOpenSansSemiBold);
			// tvFlightNo.setTypeface(typefaceOpenSansSemiBold);

			tvOriginName.setTypeface(typefaceOpenSansRegular);
			tvDestName.setTypeface(typefaceOpenSansRegular);
			tvFlightNo.setTypeface(typefaceOpenSansRegular);

			tvFlightOriginDate.setTypeface(typefaceOpenSansRegular);
			tvFlightOriginTime.setTypeface(typefaceOpenSansRegular);
			tvFlightDestDate.setTypeface(typefaceOpenSansRegular);
			tvFlightDestTime.setTypeface(typefaceOpenSansRegular);

			updateAirportNameFromCode(flightSegmentDO.departureAirportCode, AppConstants.allAirportNamesDO.vecAirport,
					tvOriginName);
			updateAirportNameFromCode(flightSegmentDO.arrivalAirportCode, AppConstants.allAirportNamesDO.vecAirport,
					tvDestName);

			String stt = tvOriginName.getText().toString();
			if (stt.length() > 8) {
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) tvOriginName
						.getLayoutParams();
				ll.width = getResources().getInteger(R.integer.orgn_width_in_summary);
				ll.height = android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
				tvOriginName.setLayoutParams(ll);
			}

			tvFlightOriginDate
					.setText(toTitleCase(CalendarUtility.getDateForSummary(flightSegmentDO.departureDateTime)));
			// tvFlightOriginDate.setText(""+CalendarUtility.getDayOfWeek(calenderDeparture.get(Calendar.DAY_OF_MONTH))+calenderDeparture.get(Calendar.DAY_OF_MONTH)
			// +"
			// "+getFirstLetterCapital(CalendarUtility.getMonth(calenderDeparture.get(Calendar.MONTH)))
			// +", "
			// +CalendarUtility.getYear(calenderDeparture.get(Calendar.YEAR)));
			tvFlightOriginTime.setText(CalendarUtility.getTimeInHourMinuteFromDate(flightSegmentDO.departureDateTime));

			tvFlightDestDate.setText(toTitleCase(CalendarUtility.getDateForSummary(flightSegmentDO.arrivalDateTime)));
			// tvFlightDestDate.setText(""+CalendarUtility.getDayOfWeek(calenderDeparture.get(Calendar.DAY_OF_MONTH))+calenderDeparture.get(Calendar.DAY_OF_MONTH)
			// +"
			// "+getFirstLetterCapital(CalendarUtility.getMonth(calenderDeparture.get(Calendar.MONTH)))
			// +", "
			// +CalendarUtility.getYear(calenderDeparture.get(Calendar.YEAR)));
			tvFlightDestTime.setText(CalendarUtility.getTimeInHourMinuteFromDate(flightSegmentDO.arrivalDateTime));

			tvFlightNo.setText(flightSegmentDO.flightNumber);
			llEmpty.addView(llFlightDetails);
		}

		ivClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (customDialog != null)
					customDialog.dismiss();
			}
		});
		showFlightDetails(llEmptyMain);
	}

	private void getInboundDetails() {
		LinearLayout llEmptyMain = (LinearLayout) getLayoutInflater().inflate(R.layout.empty_layout_dialog, null);
		LinearLayout llEmpty = (LinearLayout) llEmptyMain.findViewById(R.id.llEmptyDialog);
		ImageView ivClose = (ImageView) llEmptyMain.findViewById(R.id.ivClose);
		ImageView ivFlightIcon = (ImageView) llEmptyMain.findViewById(R.id.ivFlightIcon);
		TextView tvBookStatus = (TextView) llEmptyMain.findViewById(R.id.tvBookStatus);
		ivFlightIcon.setBackgroundResource(R.drawable.flight_return);
		tvBookStatus.setText(getString(R.string.Returning));

		for (int i = 0; i < vecFlightSegmentDOsReturn.size(); i++) {

			FlightSegmentDO flightSegmentDO = vecFlightSegmentDOsReturn.get(i);

			LinearLayout llFlightDetails = (LinearLayout) getLayoutInflater()
					.inflate(R.layout.totalprice_flight_details, null);
			TextView tvOriginName = (TextView) llFlightDetails.findViewById(R.id.tvOriginName);
			TextView tvDestName = (TextView) llFlightDetails.findViewById(R.id.tvDestName);

			TextView tvFlightOriginDate = (TextView) llFlightDetails.findViewById(R.id.tvFlightOriginDate);
			TextView tvFlightOriginTime = (TextView) llFlightDetails.findViewById(R.id.tvFlightOriginTime);
			TextView tvFlightDestDate = (TextView) llFlightDetails.findViewById(R.id.tvFlightDestDate);
			TextView tvFlightDestTime = (TextView) llFlightDetails.findViewById(R.id.tvFlightDestTime);
			TextView tvFlightNo = (TextView) llFlightDetails.findViewById(R.id.tvFlightNo);

			updateAirportNameFromCode(flightSegmentDO.departureAirportCode, AppConstants.allAirportNamesDO.vecAirport,
					tvOriginName);
			updateAirportNameFromCode(flightSegmentDO.arrivalAirportCode, AppConstants.allAirportNamesDO.vecAirport,
					tvDestName);

			// tvOriginName.setText(flightSegmentDO.departureAirportCode);
			// tvDestName.setText(flightSegmentDO.arrivalAirportCode);

			String stt = tvOriginName.getText().toString();
			if (stt.length() > 8) {
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) tvOriginName
						.getLayoutParams();
				ll.width = getResources().getInteger(R.integer.orgn_width_in_summary);
				ll.height = android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
				tvOriginName.setLayoutParams(ll);
			}

			tvFlightOriginDate
					.setText(toTitleCase(CalendarUtility.getDateForSummary(flightSegmentDO.departureDateTime)));
			// tvFlightOriginDate.setText(""+CalendarUtility.getDayOfWeek(calenderDeparture.get(Calendar.DAY_OF_MONTH))+calenderDeparture.get(Calendar.DAY_OF_MONTH)
			// +"
			// "+getFirstLetterCapital(CalendarUtility.getMonth(calenderDeparture.get(Calendar.MONTH)))
			// +", "
			// +CalendarUtility.getYear(calenderDeparture.get(Calendar.YEAR)));
			tvFlightOriginTime.setText(CalendarUtility.getTimeInHourMinuteFromDate(flightSegmentDO.departureDateTime));

			tvFlightDestDate.setText(toTitleCase(CalendarUtility.getDateForSummary(flightSegmentDO.arrivalDateTime)));
			// tvFlightDestDate.setText(""+CalendarUtility.getDayOfWeek(calenderDeparture.get(Calendar.DAY_OF_MONTH))+calenderDeparture.get(Calendar.DAY_OF_MONTH)
			// +"
			// "+getFirstLetterCapital(CalendarUtility.getMonth(calenderDeparture.get(Calendar.MONTH)))
			// +", "
			// +CalendarUtility.getYear(calenderDeparture.get(Calendar.YEAR)));
			tvFlightDestTime.setText(CalendarUtility.getTimeInHourMinuteFromDate(flightSegmentDO.arrivalDateTime));

			tvFlightNo.setText(flightSegmentDO.flightNumber);
			llEmpty.addView(llFlightDetails);
		}
		ivClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (customDialog != null)
					customDialog.dismiss();
			}
		});
		showFlightDetails(llEmptyMain);
	}

	protected void moveToPaymentActivity(ModifiedPNRResDO modifiedPNRResDO) {
		hideLoader();
		Intent in = new Intent(BookingSummaryActivity.this, PaymentActivity.class);
		in.putExtra(AppConstants.BOOKING_FLIGHT_AIRPRICE, airPriceQuoteDO);
		in.putExtra(AppConstants.MODIFIED_RES_QRY, modifiedPNRResDO);
		startActivity(in);
	}

	protected void moveToPaymentActivity() {
		hideLoader();
		// Intent in = new
		// Intent(BookingSummaryActivity.this,PaymentActivityNew.class);
		Intent in = new Intent(BookingSummaryActivity.this, PaymentActivity.class);
		in.putExtra(AppConstants.BOOKING_FLIGHT_AIRPRICE, airPriceQuoteDO);
		startActivity(in);
	}

	private void moveToPaymentSummaryActivity(AirBookDO airBookDO) {
		hideLoader();
		Intent in = new Intent(BookingSummaryActivity.this, PaymentSummaryActivity.class);
		in.putExtra(AppConstants.AIR_BOOK, airBookDO);
		in.putExtra(AppConstants.FROM, true);
		startActivity(in);
	}

	private void showPersonDetails() {
		String strPersons = "";
		if (AppConstants.bookingFlightDO.myBookFlightDO.adtQty != 0) {
			if (AppConstants.bookingFlightDO.myBookFlightDO.adtQty == 1)
				strPersons = AppConstants.bookingFlightDO.myBookFlightDO.adtQty + " "
						+ getString(R.string.adult_flight_summary);
			else
				strPersons = AppConstants.bookingFlightDO.myBookFlightDO.adtQty + " " + getString(R.string.Adults);
		}
		if (AppConstants.bookingFlightDO.myBookFlightDO.chdQty != 0 && !strPersons.equalsIgnoreCase("")) {
			if (AppConstants.bookingFlightDO.myBookFlightDO.chdQty == 1)
				strPersons = strPersons + "," + AppConstants.bookingFlightDO.myBookFlightDO.chdQty + " "
						+ getString(R.string.Child);
			else
				strPersons = strPersons + "," + AppConstants.bookingFlightDO.myBookFlightDO.chdQty + " "
						+ getString(R.string.Childs);
		} else if (AppConstants.bookingFlightDO.myBookFlightDO.chdQty != 0) {
			if (AppConstants.bookingFlightDO.myBookFlightDO.chdQty == 1)
				strPersons = AppConstants.bookingFlightDO.myBookFlightDO.chdQty + " " + getString(R.string.Child);
			else
				strPersons = AppConstants.bookingFlightDO.myBookFlightDO.chdQty + " " + getString(R.string.Childs);
		}
		if (AppConstants.bookingFlightDO.myBookFlightDO.infQty != 0 && !strPersons.equalsIgnoreCase("")) {
			if (AppConstants.bookingFlightDO.myBookFlightDO.infQty == 1)
				strPersons = strPersons + "," + AppConstants.bookingFlightDO.myBookFlightDO.infQty + " "
						+ getString(R.string.Infant);
			else
				strPersons = strPersons + "," + AppConstants.bookingFlightDO.myBookFlightDO.infQty + " "
						+ getString(R.string.Infants);
		} else if (AppConstants.bookingFlightDO.myBookFlightDO.infQty != 0) {
			if (AppConstants.bookingFlightDO.myBookFlightDO.infQty == 1)
				strPersons = AppConstants.bookingFlightDO.myBookFlightDO.infQty + " " + getString(R.string.Infant);
			else
				strPersons = AppConstants.bookingFlightDO.myBookFlightDO.infQty + " " + getString(R.string.Infants);
		}
		tvBookSummaryPassengers.setText(strPersons);
	}

	Vector<FlightSegmentDO> vecFlightSegmentDOsOneWay, vecFlightSegmentDOsReturn;

	private void showOutBoundView() {

		llOutSrcDest.setVisibility(View.VISIBLE);
		llOutData.setVisibility(View.VISIBLE);

		vecFlightSegmentDOsOneWay = new Vector<FlightSegmentDO>();

		for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size(); i++) {

			vecFlightSegmentDOsOneWay.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
					.get(i).vecFlightSegmentDOs);
		}

		// tvBookSummarySourceOut.setText(AppConstants.bookingFlightDO.myODIDOOneWay.originLocationCode);
		// tvBookSummaryDestOut.setText(AppConstants.bookingFlightDO.myODIDOOneWay.destinationLocationCode);

		// By A
		/*
		 * updateAirportNameFromCode(AppConstants.bookingFlightDO.myODIDOOneWay.
		 * originLocationCode, AppConstants.allAirportNamesDO.vecAirport,
		 * tvBookSummarySourceOut);
		 * updateAirportNameFromCode(AppConstants.bookingFlightDO.myODIDOOneWay.
		 * destinationLocationCode, AppConstants.allAirportNamesDO.vecAirport,
		 * tvBookSummaryDestOut);
		 */
		tvBookSummarySourceOut.setText(AppConstants.bookingFlightDO.myODIDOOneWay.originLocationCode);
		tvBookSummaryDestOut.setText(AppConstants.bookingFlightDO.myODIDOOneWay.destinationLocationCode);

		int lastFlt = vecFlightSegmentDOsOneWay.size() - 1;
		String depDate = vecFlightSegmentDOsOneWay.get(0).departureDateTime;
		String arvDate = vecFlightSegmentDOsOneWay.get(lastFlt).arrivalDateTime;

		String strTimeDip = CalendarUtility.getTimeInHourMinuteFromDate(depDate);
		String strTimeArr = CalendarUtility.getTimeInHourMinuteFromDate(arvDate);

		// String dayofWeek =
		// getFirstLetterCapital(CalendarUtility.getMonth(calenderDeparture.get(Calendar.MONTH)));
		try {
			tvBookSummaryDepatureOut.setText(toTitleCase(CalendarUtility.getDateWithNameofDayFromDate(depDate)));
			// tvBookSummaryDepatureOut.setText(""+CalendarUtility.getDayOfWeek(calenderDeparture.get(Calendar.DAY_OF_MONTH))+calenderDeparture.get(Calendar.DAY_OF_MONTH)
			// +"
			// "+getFirstLetterCapital(CalendarUtility.getMonth(calenderDeparture.get(Calendar.MONTH)))
			// +", "
			// +CalendarUtility.getYear(calenderDeparture.get(Calendar.YEAR)));
		} catch (Exception e) {
			tvBookSummaryDepatureOut.setText(CalendarUtility.getDateWithNameofDayFromDate(depDate));
			// tvBookSummaryDepatureOut.setText(""+CalendarUtility.getDayOfWeek(calenderDeparture.get(Calendar.DAY_OF_MONTH))+calenderDeparture.get(Calendar.DAY_OF_MONTH)
			// +"
			// "+getFirstLetterCapital(CalendarUtility.getMonth(calenderDeparture.get(Calendar.MONTH)))
			// +", "
			// +CalendarUtility.getYear(calenderDeparture.get(Calendar.YEAR)));
		}

		tvBookSummaryDepatureTimeOut.setText(strTimeDip);
		tvBookSummaryArrivalTimeOut.setText(strTimeArr);

		if (StringUtils.getFloat(strTimeArr.split(":")[0]) < StringUtils.getFloat(strTimeDip.split(":")[0]))
			tvTime1DOut.setVisibility(View.VISIBLE);
		else
			tvTime1DOut.setVisibility(View.INVISIBLE);

		int totalConnectedFlights = 0;
		if (vecFlightSegmentDOsOneWay != null && vecFlightSegmentDOsOneWay.size() > 0)
			totalConnectedFlights = vecFlightSegmentDOsOneWay.size();

		if (totalConnectedFlights >= 0 && totalConnectedFlights <= 1) {
			ivFlightConnectTypeOut.setBackgroundResource(R.drawable.flight_connecting);
			tvFlightConnectTypeOut.setText(getString(R.string.direct));
		} else if (totalConnectedFlights > 1) {
			ivFlightConnectTypeOut.setBackgroundResource(R.drawable.f_stops_1);
			tvFlightConnectTypeOut.setText(totalConnectedFlights - 1 + " " + getString(R.string.Stops));
		} else {
			ivFlightConnectTypeOut.setBackgroundResource(R.drawable.f_stops_1);
			tvFlightConnectTypeOut.setText("1 " + getString(R.string.Stop));
		}
	}

	private void showInBoundView() {

		llInboundData.setVisibility(View.VISIBLE);
		llInboundPlaces.setVisibility(View.VISIBLE);
		tvBookSummarySourceIn.setVisibility(View.VISIBLE);

		vecFlightSegmentDOsReturn = new Vector<FlightSegmentDO>();

		for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size(); i++) {

			vecFlightSegmentDOsReturn.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
					.get(i).vecFlightSegmentDOs);
		}

		// tvBookSummarySourceIn.setText(AppConstants.bookingFlightDO.myODIDOReturn.originLocationCode);
		// tvBookSummaryDestIn.setText(AppConstants.bookingFlightDO.myODIDOReturn.destinationLocationCode);

		updateAirportNameFromCode(AppConstants.bookingFlightDO.myODIDOReturn.originLocationCode,
				AppConstants.allAirportNamesDO.vecAirport, tvBookSummarySourceIn);
		updateAirportNameFromCode(AppConstants.bookingFlightDO.myODIDOReturn.destinationLocationCode,
				AppConstants.allAirportNamesDO.vecAirport, tvBookSummaryDestIn);

		tvBookSummarySourceIn.setText(AppConstants.bookingFlightDO.myODIDOReturn.originLocationCode);
		tvBookSummaryDestIn.setText(AppConstants.bookingFlightDO.myODIDOReturn.destinationLocationCode);

		int lastFlt = vecFlightSegmentDOsReturn.size() - 1;
		String depDate = vecFlightSegmentDOsReturn.get(0).departureDateTime;
		String arvDate = vecFlightSegmentDOsReturn.get(lastFlt).arrivalDateTime;
		try {
			tvBookSummaryDepatureIn.setText(toTitleCase(CalendarUtility.getDateWithNameofDayFromDate(depDate)));
			// tvBookSummaryDepatureIn.setText(""+CalendarUtility.getDayOfWeek(calenderDeparture.get(Calendar.DAY_OF_MONTH))+calenderDeparture.get(Calendar.DAY_OF_MONTH)
			// +"
			// "+getFirstLetterCapital(CalendarUtility.getMonth(calenderDeparture.get(Calendar.MONTH)))
			// +", "
			// +CalendarUtility.getYear(calenderDeparture.get(Calendar.YEAR)));
		} catch (Exception e) {
			tvBookSummaryDepatureIn.setText(CalendarUtility.getDateWithNameofDayFromDate(depDate));
			// tvBookSummaryDepatureIn.setText(""+CalendarUtility.getDayOfWeek(calenderDeparture.get(Calendar.DAY_OF_MONTH))+calenderDeparture.get(Calendar.DAY_OF_MONTH)
			// +"
			// "+getFirstLetterCapital(CalendarUtility.getMonth(calenderDeparture.get(Calendar.MONTH)))
			// +", "
			// +CalendarUtility.getYear(calenderDeparture.get(Calendar.YEAR)));
		}

		String strTimeDip = CalendarUtility.getTimeInHourMinuteFromDate(depDate);
		String strTimeArr = CalendarUtility.getTimeInHourMinuteFromDate(arvDate);

		tvBookSummaryDepatureTimeIn.setText(strTimeDip);
		tvBookSummaryArrivalTimeIn.setText(strTimeArr);

		if (StringUtils.getFloat(strTimeDip.split(":")[0]) > StringUtils.getFloat(strTimeArr.split(":")[0]))
			tvTime1DIn.setVisibility(View.VISIBLE);
		else
			tvTime1DIn.setVisibility(View.INVISIBLE);

		int totalConnectedFlights = 0;
		if (vecFlightSegmentDOsReturn != null && vecFlightSegmentDOsReturn.size() > 0)
			totalConnectedFlights = vecFlightSegmentDOsReturn.size();

		if (totalConnectedFlights >= 0 && totalConnectedFlights <= 1) {
			ivFlightConnectTypeIn.setBackgroundResource(R.drawable.flight_connecting);
			tvFlightConnectTypeIn.setText(getString(R.string.direct));
		} else if (totalConnectedFlights > 1) {
			ivFlightConnectTypeIn.setBackgroundResource(R.drawable.f_stops_1);
			tvFlightConnectTypeIn.setText(totalConnectedFlights - 1 + " " + getString(R.string.Stops));
		} else {
			ivFlightConnectTypeIn.setBackgroundResource(R.drawable.f_stops_1);
			tvFlightConnectTypeIn.setText("1 " + getString(R.string.Stop));
		}
	}

	private void callServiceBookingTotalPriceTotal() {
		showLoader("");

		Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();
		Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsReturn = new Vector<OriginDestinationOptionDO>();
		AppConstants.bookingFlightDO.myBookFlightDO.travelType = AppConstants.TRAVEL_TYPE_ONEWAY;
		if (isManageBooking) {
			vecOriginDestinationOptionDOs
					.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs);
			AppConstants.bookingFlightDO.myBookFlightDO.travelType = AppConstants.TRAVEL_TYPE_ONEWAY;
		} else {
			vecOriginDestinationOptionDOs
					.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs);
			if (AppConstants.bookingFlightDO.myODIDOReturn != null
					&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0) {
				AppConstants.bookingFlightDO.myBookFlightDO.travelType = AppConstants.TRAVEL_TYPE_RETURN;
				vecOriginDestinationOptionDOsReturn
						.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs);
			}
		}

		if (isManageBooking) {
			if (vecOriginDestinationOptionDOsReturn != null && vecOriginDestinationOptionDOsReturn.size() > 0) {
				if (new CommonBL(BookingSummaryActivity.this, BookingSummaryActivity.this).getAncillaryPriceQuote(
						AppConstants.bookingFlightDO.requestParameterDOReturn, AppConstants.AIRPORT_CODE,
						AppConstants.bookingFlightDO.myBookFlightDOReturn,
						AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify, vecOriginDestinationOptionDOs,
						null, AppConstants.bookingFlightDO.vecMealReqDOs,
						AppConstants.bookingFlightDO.vecBaggageRequestDOs,
						AppConstants.bookingFlightDO.vecInsrRequestDOs, AppConstants.bookingFlightDO.vecSeatRequestDOs,
						AppConstants.bookingFlightDO.vecSSRRequests, AppConstants.bookingFlightDO.isFlexiOut,
						AppConstants.bookingFlightDO.isFlexiIn, AppConstants.bookingFlightDO.pnr,
						AppConstants.bookingFlightDO.pnrType, null, null,
						AppConstants.bookingFlightDO.bundleServiceID)) {
				} else {
					hideLoader();
					showCustomDialog(this, getString(R.string.Alert), getString(R.string.InternetProblem),
							getString(R.string.Ok), "", DATAFAIL);
				}
			} else {
				if (new CommonBL(BookingSummaryActivity.this, BookingSummaryActivity.this).getAncillaryPriceQuote(
						AppConstants.bookingFlightDO.requestParameterDO, AppConstants.AIRPORT_CODE,
						AppConstants.bookingFlightDO.myBookFlightDO,
						AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify, vecOriginDestinationOptionDOs,
						null, AppConstants.bookingFlightDO.vecMealReqDOs,
						AppConstants.bookingFlightDO.vecBaggageRequestDOs,
						AppConstants.bookingFlightDO.vecInsrRequestDOs, AppConstants.bookingFlightDO.vecSeatRequestDOs,
						AppConstants.bookingFlightDO.vecSSRRequests, AppConstants.bookingFlightDO.isFlexiOut,
						AppConstants.bookingFlightDO.isFlexiIn, AppConstants.bookingFlightDO.pnr,
						AppConstants.bookingFlightDO.pnrType, null, null,
						AppConstants.bookingFlightDO.bundleServiceID)) {
				} else {
					hideLoader();
					showCustomDialog(this, getString(R.string.Alert), getString(R.string.InternetProblem),
							getString(R.string.Ok), "", DATAFAIL);
				}
			}

		} else {
			if (vecOriginDestinationOptionDOsReturn != null && vecOriginDestinationOptionDOsReturn.size() > 0) {
				if (new CommonBL(BookingSummaryActivity.this, BookingSummaryActivity.this).getAncillaryPriceQuote(
						AppConstants.bookingFlightDO.requestParameterDOReturn, AppConstants.AIRPORT_CODE,
						AppConstants.bookingFlightDO.myBookFlightDOReturn, null, vecOriginDestinationOptionDOs,
						vecOriginDestinationOptionDOsReturn, AppConstants.bookingFlightDO.vecMealReqDOs,
						AppConstants.bookingFlightDO.vecBaggageRequestDOs,
						AppConstants.bookingFlightDO.vecInsrRequestDOs, AppConstants.bookingFlightDO.vecSeatRequestDOs,
						AppConstants.bookingFlightDO.vecSSRRequests, AppConstants.bookingFlightDO.isFlexiOut,
						AppConstants.bookingFlightDO.isFlexiIn, AppConstants.bookingFlightDO.pnr,
						AppConstants.bookingFlightDO.pnrType, null, null,
						AppConstants.bookingFlightDO.bundleServiceID)) {
				} else {
					hideLoader();
					showCustomDialog(this, getString(R.string.Alert), getString(R.string.InternetProblem),
							getString(R.string.Ok), "", DATAFAIL);
				}
			} else {
				if (new CommonBL(BookingSummaryActivity.this, BookingSummaryActivity.this).getAncillaryPriceQuote(
						AppConstants.bookingFlightDO.requestParameterDO, AppConstants.AIRPORT_CODE,
						AppConstants.bookingFlightDO.myBookFlightDO, null, vecOriginDestinationOptionDOs,
						vecOriginDestinationOptionDOsReturn, AppConstants.bookingFlightDO.vecMealReqDOs,
						AppConstants.bookingFlightDO.vecBaggageRequestDOs,
						AppConstants.bookingFlightDO.vecInsrRequestDOs, AppConstants.bookingFlightDO.vecSeatRequestDOs,
						AppConstants.bookingFlightDO.vecSSRRequests, AppConstants.bookingFlightDO.isFlexiOut,
						AppConstants.bookingFlightDO.isFlexiIn, AppConstants.bookingFlightDO.pnr,
						AppConstants.bookingFlightDO.pnrType, null, null,
						AppConstants.bookingFlightDO.bundleServiceID)) {
				} else {
					hideLoader();
					showCustomDialog(this, getString(R.string.Alert), getString(R.string.InternetProblem),
							getString(R.string.Ok), "", DATAFAIL);
				}
			}

		}
	}

	private void callServiceModifiedResQuery() {
		showLoader("");
		if (AppConstants.bookingFlightDO.myODIDOOneWay != null) {
			if (new CommonBL(BookingSummaryActivity.this, BookingSummaryActivity.this).getModifiedResQuery(
					AppConstants.bookingFlightDO.requestParameterDO,
					AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
					AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
					AppConstants.bookingFlightDO.pnr, AppConstants.bookingFlightDO.pnrType, "15")) {
			} else
				showCustomDialog(this, getString(R.string.Alert), getString(R.string.InternetProblem),
						getString(R.string.Ok), "", DATAFAIL);
		} else {
			if (new CommonBL(BookingSummaryActivity.this, BookingSummaryActivity.this).getModifiedResQuery(
					AppConstants.bookingFlightDO.requestParameterDO,
					AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
					AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs,
					AppConstants.bookingFlightDO.pnr, AppConstants.bookingFlightDO.pnrType, "15")) {
			} else
				showCustomDialog(this, getString(R.string.Alert), getString(R.string.InternetProblem),
						getString(R.string.Ok), "", DATAFAIL);
		}
	}

	@Override
	public void dataRetreived(Response data) {
		if (data != null && !data.isError) {
			switch (data.method) {
			case ANCILLARY_PRICEQUOTE:
				airPriceQuoteDO = new AirPriceQuoteDO();
				airPriceQuoteDO = (AirPriceQuoteDO) data.data;

				if (tvBookSummaryTotal.getText().toString().equalsIgnoreCase("") && airPriceQuoteDO != null
						&& airPriceQuoteDO.vecPricedItineraryDOs != null
						&& airPriceQuoteDO.vecPricedItineraryDOs.size() > 0) {
					priceCode = AppConstants.CurrencyCodeAfterExchange;
					totalprice = StringUtils.getFloat(airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.amount);

//					tvBookSummaryTotal.setText(AppConstants.CurrencyCodeAfterExchange + " " + updateCurrencyByFactor(
//							airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.amount + "", 0));

					updateAdditionalView(priceCode, airPriceQuoteDO.vecPricedItineraryDOs);
					hideLoader();
				} else {
					hideLoader();

					if (airPriceQuoteDO.ErrorMsg.equalsIgnoreCase("")) {
						showCustomDialog(BookingSummaryActivity.this, getString(R.string.Alert),
								getString(R.string.TechProblem), getString(R.string.Ok), "", DATAFAIL);
					} else {
						showCustomDialog(BookingSummaryActivity.this, getString(R.string.Alert),
								airPriceQuoteDO.ErrorMsg, getString(R.string.Ok), "", DATAFAIL);
					}
				}
				break;

			case MODIFIED_RESQUERY:
				if (data.data instanceof ModifiedPNRResDO) {
					ModifiedPNRResDO modifiedPNRResDO = (ModifiedPNRResDO) data.data;

					if (modifiedPNRResDO != null && modifiedPNRResDO.vecTravelerCnxModAddResBalancesDO != null
							&& modifiedPNRResDO.vecTravelerCnxModAddResBalancesDO.size() > 0
							&& modifiedPNRResDO.vecTravelerCnxModAddResBalancesDO.get(0) != null) {
						moveToPaymentActivity(modifiedPNRResDO);
					} else {
						hideLoader();
						if (modifiedPNRResDO != null && modifiedPNRResDO.errorMessage
								.equalsIgnoreCase(AppConstants.BOOKING_UNCHANGED_SEGMENT_TEXT))
							showCustomDialog(this, getString(R.string.Alert), getString(R.string.SelectedFlightsCannot),
									getString(R.string.Ok), "", DATAFAIL);
						else
							showCustomDialog(this, getString(R.string.Alert),
									getString(R.string.ErrorWhileProcessing) + " " + getString(R.string.StartAgain),
									getString(R.string.Ok), "", DATAFAIL);
					}
				} else if (data.data instanceof String) {
					hideLoader();
					showCustomDialog(this, getString(R.string.Alert),
							getString(R.string.TechProblem) + getString(R.string.TryAgainAfter), getString(R.string.Ok),
							"", DATAFAIL);
				} else {
					hideLoader();
					showCustomDialog(this, getString(R.string.Alert), getString(R.string.Payment_Error),
							getString(R.string.Ok), "", DATAFAIL);
				}
				break;

			case MODIFY_RESERVATION:
				if (data.data instanceof AirBookDO) {
					AirBookDO airBookDO = (AirBookDO) data.data;
					if (!airBookDO.bookingID.equalsIgnoreCase("")) {
						moveToPaymentSummaryActivity(airBookDO);
					} else if (!airBookDO.errorMessage.equalsIgnoreCase(""))
						showCustomDialog(this, getString(R.string.Alert),
								getString(R.string.TechProblem) + getString(R.string.TryAgainAfter),
								getString(R.string.Ok), "", DATAFAIL);
					else
						showCustomDialog(this, getString(R.string.Alert),
								getString(R.string.SessionExpired) + " " + getString(R.string.StartAgain),
								getString(R.string.Ok), "", DATAFAIL);
				} else if (data.data instanceof String) {
					showCustomDialog(this, getString(R.string.Alert), data.data.toString(), getString(R.string.Ok), "",
							DATAFAIL);
				} else {
					showCustomDialog(this, getString(R.string.Alert), getString(R.string.Payment_Error),
							getString(R.string.Ok), "", DATAFAIL);
				}
				hideLoader();
				break;
			default:
				break;
			}
		} else {
			hideLoader();
			if (data.data instanceof String) {
				if (((String) data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
					showCustomDialog(getApplicationContext(), getString(R.string.Alert),
							getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", DATAFAIL);
				else
					showCustomDialog(getApplicationContext(), getString(R.string.Alert),
							getString(R.string.TechProblem), getString(R.string.Ok), "", DATAFAIL);
			} else
				showCustomDialog(BookingSummaryActivity.this, getString(R.string.Alert),
						getString(R.string.TechProblem), getString(R.string.Ok), "", DATAFAIL);
		}
	}

	@Override
	public void onButtonYesClick(String from) {
		super.onButtonYesClick(from);

		if (from.equalsIgnoreCase(DATAFAIL))
			clickHome();
		else
			finish();
	}

	// =========================================================================newly
	// added for up
	// ===============================================================

	private void updateAdditionalView(String priceCode, ArrayList<PricedItineraryDO> vecPricedItineraryDOs) {
		vecFareDOMeals.clear();
		vecFareDOBaggages.clear();
		vecFareDOSeats.clear();
		vecFareDOInsurance.clear();
		vecFareDOHala.clear();

		float adminFees = 0, adminFeesINF = 0;
		for (int i = 0; i < vecPricedItineraryDOs.size(); i++) {
			for (PTC_FareBreakdownDO ptc_FareBreakdownDO : vecPricedItineraryDOs.get(i).vecPTC_FareBreakdownDOs) {
				for (FareDO fareDO : ptc_FareBreakdownDO.vecFees) {
					if (fareDO.feeCode.contains(AppConstants.MEAL_TAG))
						vecFareDOMeals.add(fareDO);
					else if (fareDO.feeCode.contains(AppConstants.BAGGAGE_TAG))
						vecFareDOBaggages.add(fareDO);
					else if (fareDO.feeCode.contains(AppConstants.SEAT_TAG))
						vecFareDOSeats.add(fareDO);
					else if (fareDO.feeCode.contains(AppConstants.INSURANCE_TAG))
						vecFareDOInsurance.add(fareDO);
					else if (fareDO.feeCode.contains(AppConstants.HALA_TAG))
						vecFareDOHala.add(fareDO);

					if (fareDO.feeCode.equalsIgnoreCase("CC/Transaction Fees")
							&& !ptc_FareBreakdownDO.code.equalsIgnoreCase("INF")){
						adminFees += StringUtils.getFloat(fareDO.amount.toString());
					} else if (fareDO.feeCode.equalsIgnoreCase("CC/Transaction Fees")
							&& ptc_FareBreakdownDO.code.equalsIgnoreCase("INF")){
						adminFeesINF = StringUtils.getFloat(fareDO.amount.toString()) * StringUtils.getFloat(ptc_FareBreakdownDO.quantity.toString());
					}
				}
			}
			adminFees += adminFeesINF;
		}

		float feesMeals = 0.0f, feesBaggages = 0.0f, feesSeats = 0.0f, feesInsurance = 0.0f, feesHala = 0.0f,
				feesTotal = 0.0f;

		if (vecFareDOMeals.size() > 0) {
			for (FareDO fareDO : vecFareDOMeals) {
				feesMeals = feesMeals + StringUtils.getFloat(fareDO.amount);
			}
			tvMealsCost.setText(priceCode + " " + updateCurrencyByFactor(feesMeals + "", 0));
			llMealsCost.setVisibility(View.VISIBLE);
		} else {
			llMealsCost.setVisibility(View.VISIBLE);
			tvMealsCost.setText(priceCode + " 0");
			// tvMealsCost.setText("-");
		}

		if (vecFareDOBaggages.size() > 0) {
			for (FareDO fareDO : vecFareDOBaggages) {
				feesBaggages = feesBaggages + StringUtils.getFloat(fareDO.amount);
			}
			tvBaggageCost.setText(priceCode + " " + updateCurrencyByFactor(feesBaggages + "", 0));
			llBaggageCost.setVisibility(View.VISIBLE);
		} else {
			llBaggageCost.setVisibility(View.VISIBLE);
			tvBaggageCost.setText(priceCode + " 0");
			// tvBaggageCost.setText("-");
		}
		if (vecFareDOSeats.size() > 0) {
			for (FareDO fareDO : vecFareDOSeats) {
				feesSeats = feesSeats + StringUtils.getFloat(fareDO.amount);
			}
			tvSeatsCost.setText(priceCode + " " + updateCurrencyByFactor(feesSeats + "", 0));
			llSeatsCost.setVisibility(View.VISIBLE);
		} else {
			llSeatsCost.setVisibility(View.VISIBLE);
			tvSeatsCost.setText(priceCode + " 0");
			// tvSeatsCost.setText("-");
		}
		if (vecFareDOInsurance.size() > 0) {
			for (FareDO fareDO : vecFareDOInsurance) {
				feesInsurance = feesInsurance + StringUtils.getFloat(fareDO.amount);
			}
			tvInsuranceCost.setText(priceCode + " " + updateCurrencyByFactor(feesInsurance + "", 0));
			llInsuranceCost.setVisibility(View.VISIBLE);
		} else {
			llInsuranceCost.setVisibility(View.VISIBLE);
			tvInsuranceCost.setText(priceCode + " 0");
			// tvInsuranceCost.setText("-");
		}
		if (vecFareDOHala.size() > 0) {
			for (FareDO fareDO : vecFareDOHala) {
				feesHala = feesHala + StringUtils.getFloat(fareDO.amount);
			}
			tvHalaCost.setText(priceCode + " " + updateCurrencyByFactor(feesHala + "", 0));
			llHalaCost.setVisibility(View.VISIBLE);
		} else {
			llHalaCost.setVisibility(View.VISIBLE);
			tvHalaCost.setText(priceCode + " 0");
			// tvHalaCost.setText("-");
		}
		feesTotal = feesMeals + feesBaggages + feesSeats + feesInsurance + feesHala + totalpriceOutBound;
		totalprice -= adminFees;
		totalPriceInOutBound = totalPriceInOutBound + (totalprice - feesTotal);
		tvTotalFlightCost.setText(priceCode + " " + updateCurrencyByFactor(totalPriceInOutBound + "", 0));

		tvBookSummaryTotal.setText(AppConstants.CurrencyCodeAfterExchange + " " + updateCurrencyByFactor(
				totalprice + "", 0));

		if (feesMeals == 0 && feesBaggages == 0 && feesSeats == 0 && feesInsurance == 0 && feesHala == 0) {
			llAdditionalServices.setVisibility(View.VISIBLE);
			tvAdditionalServicesHeader.setVisibility(View.VISIBLE);
		}
	}

	public void showFlightDetails(LinearLayout ll) {
		if (customDialog != null && customDialog.isShowing())
			customDialog.dismiss();

		customDialog = new CustomDialog(BookingSummaryActivity.this, ll, AppConstants.DEVICE_WIDTH - 40,
				LayoutParams.WRAP_CONTENT, true);

		customDialog.show();
	}
}

// package com.winit.airarabia;
//
// import java.util.ArrayList;
// import java.util.Vector;
//
// import android.app.ActionBar.LayoutParams;
// import android.content.BroadcastReceiver;
// import android.content.Context;
// import android.content.Intent;
// import android.view.View;
// import android.view.View.OnClickListener;
// import android.widget.ImageView;
// import android.widget.LinearLayout;
// import android.widget.TextView;
//
// import com.winit.airarabia.busynesslayer.CommonBL;
// import com.winit.airarabia.busynesslayer.DataListener;
// import com.winit.airarabia.common.AppConstants;
// import com.winit.airarabia.controls.CustomDialog;
// import com.winit.airarabia.objects.AirBookDO;
// import com.winit.airarabia.objects.AirPriceQuoteDO;
// import com.winit.airarabia.objects.FareDO;
// import com.winit.airarabia.objects.FlightSegmentDO;
// import com.winit.airarabia.objects.ModifiedPNRResDO;
// import com.winit.airarabia.objects.OriginDestinationOptionDO;
// import com.winit.airarabia.objects.PTC_FareBreakdownDO;
// import com.winit.airarabia.objects.PricedItineraryDO;
// import com.winit.airarabia.utils.CalendarUtility;
// import com.winit.airarabia.utils.StringUtils;
// import com.winit.airarabia.webaccess.Response;
//
// public class BookingSummaryActivity extends BaseActivity implements
// DataListener {
//
// private LinearLayout llBookingsummary, llInboundData, llInboundPlaces,
// llBaggageCost, llSeatsCost, llInsuranceCost, llMealsCost,
// llHalaCost, llOutSrcDest, llOutData,llAdditionalServices;
// private TextView tvBookSummaryPassengers, tvBookSummarySourceOut,
// tvBookSummaryDestOut, tvBookSummaryDepatureOut,tvBookSummaryDepatureTimeOut,
// tvBookSummaryArrivalTimeOut,tvBookSummaryDepatureTimeIn,tvBookSummaryArrivalTimeIn,tvTotalFlightCost,
// tvBookSummarySourceIn, tvBookSummaryDestIn,
// tvBookSummaryDepatureIn, tvBookSummaryTotal,
// tvBaggageCost, tvMealsCost, tvInsuranceCost, tvSeatsCost,
// tvHalaCost, /*tvOutText,*/tvAdditionalServicesHeader/*,tvInboundHeader*/;
// private String priceCode = "";
// private float totalprice = 0, totalpriceOutBound = 0, totalPriceInOutBound=0;
// private AirPriceQuoteDO airPriceQuoteDO;
// private Vector<FareDO> vecFareDOMeals, vecFareDOBaggages, vecFareDOSeats,
// vecFareDOInsurance, vecFareDOHala;
//
// private ImageView ivFlightInfoOneWay,ivFlightInfoReturn;
// private boolean isManageBooking = false;
//
//// private final String FLIGHT_BOOK = "FLIGHT_BOOK";
// private final String DATAFAIL = "DATAFAIL";
//
// private BookingSummaryActivity.BCR bcr;
//
// private class BCR extends BroadcastReceiver {
//
// @Override
// public void onReceive(Context context, Intent intent) {
// if (intent.getAction().equalsIgnoreCase(AppConstants.HOME_CLICK))
// finish();
// if (intent.getAction().equalsIgnoreCase(AppConstants.BOOK_SUCCESS))
// finish();
// }
// }
//
// @Override
// protected void onDestroy() {
// super.onDestroy();
// unregisterReceiver(bcr);
// }
//
// @Override
// public void initilize() {
// tvHeaderTitle.setText(getString(R.string.PriceSummary));
//
// bcr = new BCR();
// intentFilter.addAction(AppConstants.HOME_CLICK);
// intentFilter.addAction(AppConstants.BOOK_SUCCESS);
// registerReceiver(bcr, intentFilter);
//
// llBookingsummary = (LinearLayout)
// layoutInflater.inflate(R.layout.bookingsummary, null);
// llMiddleBase.addView(llBookingsummary,
// LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
//
// tvBookSummaryPassengers = (TextView)
// llBookingsummary.findViewById(R.id.tvBookSummaryPassengers);
// tvBookSummarySourceOut = (TextView)
// llBookingsummary.findViewById(R.id.tvBookSummarySourceOut);
// tvBookSummaryDestOut = (TextView)
// llBookingsummary.findViewById(R.id.tvBookSummaryDestOut);
// tvBookSummaryDepatureOut = (TextView)
// llBookingsummary.findViewById(R.id.tvBookSummaryDepatureOut);
// tvBookSummaryDepatureTimeOut= (TextView)
// llBookingsummary.findViewById(R.id.tvBookSummaryDepatureTimeOut);
// tvBookSummaryArrivalTimeOut = (TextView)
// llBookingsummary.findViewById(R.id.tvBookSummaryArrivalTimeOut);
// tvBookSummarySourceIn = (TextView)
// llBookingsummary.findViewById(R.id.tvBookSummarySourceIn);
// tvBookSummaryDestIn = (TextView)
// llBookingsummary.findViewById(R.id.tvBookSummaryDestIn);
// tvBookSummaryDepatureIn = (TextView)
// llBookingsummary.findViewById(R.id.tvBookSummaryDepatureIn);
// tvBookSummaryDepatureTimeIn = (TextView)
// llBookingsummary.findViewById(R.id.tvBookSummaryDepatureTimeIn);
// tvBookSummaryArrivalTimeIn = (TextView)
// llBookingsummary.findViewById(R.id.tvBookSummaryArrivalTimeIn);
// llInboundData = (LinearLayout)
// llBookingsummary.findViewById(R.id.llInboundData);
// llInboundPlaces = (LinearLayout)
// llBookingsummary.findViewById(R.id.llInboundPlaces);
// tvBaggageCost = (TextView) llBookingsummary.findViewById(R.id.tvBaggageCost);
// tvMealsCost = (TextView) llBookingsummary.findViewById(R.id.tvMealsCost);
// tvBookSummaryTotal = (TextView)
// llBookingsummary.findViewById(R.id.tvBookSummaryTotal);
// tvInsuranceCost = (TextView)
// llBookingsummary.findViewById(R.id.tvInsuranceCost);
// tvSeatsCost = (TextView) llBookingsummary.findViewById(R.id.tvSeatsCost);
// tvHalaCost = (TextView) llBookingsummary.findViewById(R.id.tvHalaCost);
// llBaggageCost = (LinearLayout)
// llBookingsummary.findViewById(R.id.llBaggageCost);
// llSeatsCost = (LinearLayout) llBookingsummary.findViewById(R.id.llSeatsCost);
// llInsuranceCost = (LinearLayout)
// llBookingsummary.findViewById(R.id.llInsuranceCost);
// llMealsCost = (LinearLayout) llBookingsummary.findViewById(R.id.llMealsCost);
// llHalaCost = (LinearLayout) llBookingsummary.findViewById(R.id.llHalaCost);
// llOutSrcDest = (LinearLayout)
// llBookingsummary.findViewById(R.id.llOutSrcDest);
// llOutData = (LinearLayout) llBookingsummary.findViewById(R.id.llOutData);
// llAdditionalServices = (LinearLayout)
// llBookingsummary.findViewById(R.id.llAdditionalServices);
// tvAdditionalServicesHeader = (TextView)
// llBookingsummary.findViewById(R.id.tvAdditionalServicesHeader);
// tvTotalFlightCost = (TextView)
// llBookingsummary.findViewById(R.id.tvTotalFlightCost);
//
// ivFlightInfoOneWay = (ImageView)
// llBookingsummary.findViewById(R.id.ivFlightInfoOneWay);
// ivFlightInfoReturn = (ImageView)
// llBookingsummary.findViewById(R.id.ivFlightInfoReturn);
//
// btnSubmitNext.setVisibility(View.VISIBLE);
// btnSubmitNext.setText(getString(R.string.Continue));
//
// vecFareDOMeals = new Vector<FareDO>();
// vecFareDOBaggages = new Vector<FareDO>();
// vecFareDOSeats = new Vector<FareDO>();
// vecFareDOInsurance = new Vector<FareDO>();
// vecFareDOHala = new Vector<FareDO>();
// }
//
// @Override
// public void bindingControl() {
//
// if(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify != null
// && AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.size() >
// 0)
// isManageBooking = true;
//
// priceCode=AppConstants.CurrencyCodeAfterExchange;
//
// callServiceBookingTotalPriceTotal();
//
// showPersonDetails();
//
// if (isManageBooking) {
// showOutBoundView();
// } else {
// if (AppConstants.bookingFlightDO.myODIDOReturn == null)
// showOutBoundView();
// else {
// showOutBoundView();
// showInBoundView();
// }
// }
//
// ivFlightInfoOneWay.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// getOutBoundDetails();
// }
// });
//
// ivFlightInfoReturn.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// getInboundDetails();
// }
// });
//
// btnSubmitNext.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// if (airPriceQuoteDO != null
// && airPriceQuoteDO.vecPricedItineraryDOs != null
// && airPriceQuoteDO.vecPricedItineraryDOs.size() > 0) {
// if (isManageBooking)
// callServiceModifiedResQuery();
// else
// moveToPaymentActivity();
// }
// }
// });
// }
// private void getOutBoundDetails()
// {
// LinearLayout llEmptyMain = (LinearLayout)
// getLayoutInflater().inflate(R.layout.empty_layout_dialog, null);
// LinearLayout llEmpty = (LinearLayout)
// llEmptyMain.findViewById(R.id.llEmptyDialog);
// ImageView ivClose = (ImageView) llEmptyMain.findViewById(R.id.ivClose);
//
// for (int i = 0; i < vecFlightSegmentDOsOneWay.size(); i++) {
//
// FlightSegmentDO flightSegmentDO = vecFlightSegmentDOsOneWay.get(i);
//
// LinearLayout llFlightDetails = (LinearLayout)
// getLayoutInflater().inflate(R.layout.totalprice_flight_details, null);
// TextView tvOriginName = (TextView)
// llFlightDetails.findViewById(R.id.tvOriginName);
// TextView tvDestName = (TextView)
// llFlightDetails.findViewById(R.id.tvDestName);
//
// TextView tvFlightOriginDate = (TextView)
// llFlightDetails.findViewById(R.id.tvFlightOriginDate);
// TextView tvFlightOriginTime = (TextView)
// llFlightDetails.findViewById(R.id.tvFlightOriginTime);
// TextView tvFlightDestDate = (TextView)
// llFlightDetails.findViewById(R.id.tvFlightDestDate);
// TextView tvFlightDestTime = (TextView)
// llFlightDetails.findViewById(R.id.tvFlightDestTime);
// TextView tvFlightNo = (TextView)
// llFlightDetails.findViewById(R.id.tvFlightNo);
//
// tvOriginName.setText(flightSegmentDO.departureAirportCode);
// tvDestName.setText(flightSegmentDO.arrivalAirportCode);
//
// tvFlightOriginDate.setText(CalendarUtility.
// getDateWithNameofDayFromDate(flightSegmentDO.departureDateTime));
// tvFlightOriginTime.setText(CalendarUtility.
// getTimeInHourMinuteFromDate(flightSegmentDO.departureDateTime));
//
// tvFlightDestDate.setText(CalendarUtility.
// getDateWithNameofDayFromDate(flightSegmentDO.arrivalDateTime));
// tvFlightDestTime.setText(CalendarUtility.
// getTimeInHourMinuteFromDate(flightSegmentDO.arrivalDateTime));
//
// tvFlightNo.setText(flightSegmentDO.flightNumber);
// llEmpty.addView(llFlightDetails);
// }
//
// ivClose.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// if(customDialog != null)
// customDialog.dismiss();
// }
// });
// showFlightDetails(llEmptyMain);
// }
// private void getInboundDetails()
// {
// LinearLayout llEmptyMain = (LinearLayout)
// getLayoutInflater().inflate(R.layout.empty_layout_dialog, null);
// LinearLayout llEmpty = (LinearLayout)
// llEmptyMain.findViewById(R.id.llEmptyDialog);
// ImageView ivClose = (ImageView) llEmptyMain.findViewById(R.id.ivClose);
//
// for (int i = 0; i < vecFlightSegmentDOsReturn.size(); i++) {
//
// FlightSegmentDO flightSegmentDO = vecFlightSegmentDOsReturn.get(i);
//
// LinearLayout llFlightDetails = (LinearLayout)
// getLayoutInflater().inflate(R.layout.totalprice_flight_details, null);
// TextView tvOriginName = (TextView)
// llFlightDetails.findViewById(R.id.tvOriginName);
// TextView tvDestName = (TextView)
// llFlightDetails.findViewById(R.id.tvDestName);
//
// TextView tvFlightOriginDate = (TextView)
// llFlightDetails.findViewById(R.id.tvFlightOriginDate);
// TextView tvFlightOriginTime = (TextView)
// llFlightDetails.findViewById(R.id.tvFlightOriginTime);
// TextView tvFlightDestDate = (TextView)
// llFlightDetails.findViewById(R.id.tvFlightDestDate);
// TextView tvFlightDestTime = (TextView)
// llFlightDetails.findViewById(R.id.tvFlightDestTime);
// TextView tvFlightNo = (TextView)
// llFlightDetails.findViewById(R.id.tvFlightNo);
//
// tvOriginName.setText(flightSegmentDO.departureAirportCode);
// tvDestName.setText(flightSegmentDO.arrivalAirportCode);
//
// tvFlightOriginDate.setText(CalendarUtility.
// getDateWithNameofDayFromDate(flightSegmentDO.departureDateTime));
// tvFlightOriginTime.setText(CalendarUtility.
// getTimeInHourMinuteFromDate(flightSegmentDO.departureDateTime));
//
// tvFlightDestDate.setText(CalendarUtility.
// getDateWithNameofDayFromDate(flightSegmentDO.arrivalDateTime));
// tvFlightDestTime.setText(CalendarUtility.
// getTimeInHourMinuteFromDate(flightSegmentDO.arrivalDateTime));
//
// tvFlightNo.setText(flightSegmentDO.flightNumber);
// llEmpty.addView(llFlightDetails);
// }
// ivClose.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// if(customDialog != null)
// customDialog.dismiss();
// }
// });
// showFlightDetails(llEmptyMain);
// }
//
// protected void moveToPaymentActivity(ModifiedPNRResDO modifiedPNRResDO) {
// hideLoader();
// Intent in = new Intent(BookingSummaryActivity.this,PaymentActivity.class);
// in.putExtra(AppConstants.BOOKING_FLIGHT_AIRPRICE, airPriceQuoteDO);
// in.putExtra(AppConstants.MODIFIED_RES_QRY, modifiedPNRResDO);
// startActivity(in);
// }
//
// protected void moveToPaymentActivity() {
// hideLoader();
//// Intent in = new
// Intent(BookingSummaryActivity.this,PaymentActivityNew.class);
// Intent in = new Intent(BookingSummaryActivity.this,PaymentActivity.class);
// in.putExtra(AppConstants.BOOKING_FLIGHT_AIRPRICE, airPriceQuoteDO);
// startActivity(in);
// }
//
// private void moveToPaymentSummaryActivity(AirBookDO airBookDO) {
// hideLoader();
// Intent in = new
// Intent(BookingSummaryActivity.this,PaymentSummaryActivity.class);
// in.putExtra(AppConstants.AIR_BOOK, airBookDO);
// in.putExtra(AppConstants.FROM, true);
// startActivity(in);
// }
//
// private void showPersonDetails() {
// String strPersons = "";
// if (AppConstants.bookingFlightDO.myBookFlightDO.adtQty != 0)
// strPersons = AppConstants.bookingFlightDO.myBookFlightDO.adtQty+" "+
// getString(R.string.Adults);
// if (AppConstants.bookingFlightDO.myBookFlightDO.chdQty != 0
// && !strPersons.equalsIgnoreCase(""))
// strPersons = strPersons + ","
// + AppConstants.bookingFlightDO.myBookFlightDO.chdQty
// + " "+getString(R.string.Children);
// else if (AppConstants.bookingFlightDO.myBookFlightDO.chdQty != 0)
// strPersons = AppConstants.bookingFlightDO.myBookFlightDO.chdQty
// +" "+ getString(R.string.Children);
// if (AppConstants.bookingFlightDO.myBookFlightDO.infQty != 0
// && !strPersons.equalsIgnoreCase(""))
// strPersons = strPersons + ","
// + AppConstants.bookingFlightDO.myBookFlightDO.infQty
// +" "+ getString(R.string.Infants);
// else if (AppConstants.bookingFlightDO.myBookFlightDO.infQty != 0)
// strPersons = AppConstants.bookingFlightDO.myBookFlightDO.infQty
// +" "+ getString(R.string.Infants);
// tvBookSummaryPassengers.setText(strPersons);
// }
//
// Vector<FlightSegmentDO> vecFlightSegmentDOsOneWay,vecFlightSegmentDOsReturn;
// private void showOutBoundView() {
//
// llOutSrcDest.setVisibility(View.VISIBLE);
// llOutData.setVisibility(View.VISIBLE);
//
// vecFlightSegmentDOsOneWay = new Vector<FlightSegmentDO>();
//
// for (int i = 0; i <
// AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size();
// i++) {
//
// vecFlightSegmentDOsOneWay.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
// }
//
// tvBookSummarySourceOut.setText(AppConstants.bookingFlightDO.myODIDOOneWay.originLocationCode);
// tvBookSummaryDestOut.setText(AppConstants.bookingFlightDO.myODIDOOneWay.destinationLocationCode);
//
// int lastFlt = vecFlightSegmentDOsOneWay.size() - 1;
// String depDate = vecFlightSegmentDOsOneWay.get(0).departureDateTime;
// String arvDate = vecFlightSegmentDOsOneWay.get(lastFlt).arrivalDateTime;
//
// tvBookSummaryDepatureOut.setText(CalendarUtility.getDateWithNameofDayFromDate(depDate));
//
// tvBookSummaryDepatureTimeOut.setText(CalendarUtility.getTimeInHourMinuteFromDate(depDate));
// tvBookSummaryArrivalTimeOut.setText(CalendarUtility.getTimeInHourMinuteFromDate(arvDate));
// }
//
// private void showInBoundView() {
//
// llInboundData.setVisibility(View.VISIBLE);
// llInboundPlaces.setVisibility(View.VISIBLE);
// tvBookSummarySourceIn.setVisibility(View.VISIBLE);
//
// vecFlightSegmentDOsReturn = new Vector<FlightSegmentDO>();
//
// for (int i = 0; i <
// AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size();
// i++) {
//
// vecFlightSegmentDOsReturn.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
// }
//
// tvBookSummarySourceIn.setText(AppConstants.bookingFlightDO.myODIDOReturn.originLocationCode);
// tvBookSummaryDestIn.setText(AppConstants.bookingFlightDO.myODIDOReturn.destinationLocationCode);
//
// int lastFlt = vecFlightSegmentDOsReturn.size() - 1;
// String depDate = vecFlightSegmentDOsReturn.get(0).departureDateTime;
// String arvDate = vecFlightSegmentDOsReturn.get(lastFlt).arrivalDateTime;
//
// tvBookSummaryDepatureIn.setText(CalendarUtility.getDateWithNameofDayFromDate(depDate));
// tvBookSummaryDepatureTimeIn.setText(CalendarUtility.getTimeInHourMinuteFromDate(depDate));
// tvBookSummaryArrivalTimeIn.setText(CalendarUtility.getTimeInHourMinuteFromDate(arvDate));
// }
//
// private void callServiceBookingTotalPriceTotal() {
// showLoader("");
//
// Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs = new
// Vector<OriginDestinationOptionDO>();
// Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsReturn = new
// Vector<OriginDestinationOptionDO>();
// AppConstants.bookingFlightDO.myBookFlightDO.travelType =
// AppConstants.TRAVEL_TYPE_ONEWAY;
// if (isManageBooking)
// {
// vecOriginDestinationOptionDOs.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs);
// AppConstants.bookingFlightDO.myBookFlightDO.travelType =
// AppConstants.TRAVEL_TYPE_ONEWAY;
// }
// else
// {
// vecOriginDestinationOptionDOs.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs);
// if (AppConstants.bookingFlightDO.myODIDOReturn != null
// &&
// AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size()
// > 0)
// {
// AppConstants.bookingFlightDO.myBookFlightDO.travelType =
// AppConstants.TRAVEL_TYPE_RETURN;
// vecOriginDestinationOptionDOsReturn
// .addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs);
// }
// }
//
// if (isManageBooking) {
// if(vecOriginDestinationOptionDOsReturn != null &&
// vecOriginDestinationOptionDOsReturn.size() > 0)
// {
// if (new CommonBL(BookingSummaryActivity.this,
// BookingSummaryActivity.this).getAncillaryPriceQuote(
// AppConstants.bookingFlightDO.requestParameterDOReturn,
// AppConstants.AIRPORT_CODE,
// AppConstants.bookingFlightDO.myBookFlightDOReturn,
// AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
// vecOriginDestinationOptionDOs,
// null,
// AppConstants.bookingFlightDO.vecMealReqDOs,
// AppConstants.bookingFlightDO.vecBaggageRequestDOs,
// AppConstants.bookingFlightDO.vecInsrRequestDOs,
// AppConstants.bookingFlightDO.vecSeatRequestDOs,
// AppConstants.bookingFlightDO.vecSSRRequests,
// AppConstants.bookingFlightDO.isFlexiOut,
// AppConstants.bookingFlightDO.isFlexiIn,
// AppConstants.bookingFlightDO.pnr,
// AppConstants.bookingFlightDO.pnrType,null,null,
// AppConstants.bookingFlightDO.bundleServiceID)) {
// }else {
// hideLoader();
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.InternetProblem),
// getString(R.string.Ok), "", DATAFAIL);
// }
// }
// else
// {
// if (new CommonBL(BookingSummaryActivity.this,
// BookingSummaryActivity.this).getAncillaryPriceQuote(
// AppConstants.bookingFlightDO.requestParameterDO,
// AppConstants.AIRPORT_CODE,
// AppConstants.bookingFlightDO.myBookFlightDO,
// AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
// vecOriginDestinationOptionDOs,
// null,
// AppConstants.bookingFlightDO.vecMealReqDOs,
// AppConstants.bookingFlightDO.vecBaggageRequestDOs,
// AppConstants.bookingFlightDO.vecInsrRequestDOs,
// AppConstants.bookingFlightDO.vecSeatRequestDOs,
// AppConstants.bookingFlightDO.vecSSRRequests,
// AppConstants.bookingFlightDO.isFlexiOut,
// AppConstants.bookingFlightDO.isFlexiIn,
// AppConstants.bookingFlightDO.pnr,
// AppConstants.bookingFlightDO.pnrType,null,null,
// AppConstants.bookingFlightDO.bundleServiceID)) {
// } else {
// hideLoader();
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.InternetProblem),
// getString(R.string.Ok), "", DATAFAIL);
// }
// }
//
// } else {
// if(vecOriginDestinationOptionDOsReturn != null &&
// vecOriginDestinationOptionDOsReturn.size() > 0)
// {
// if (new CommonBL(BookingSummaryActivity.this,
// BookingSummaryActivity.this).getAncillaryPriceQuote(
// AppConstants.bookingFlightDO.requestParameterDOReturn,
// AppConstants.AIRPORT_CODE,
// AppConstants.bookingFlightDO.myBookFlightDOReturn,
// null,
// vecOriginDestinationOptionDOs,
// vecOriginDestinationOptionDOsReturn,
// AppConstants.bookingFlightDO.vecMealReqDOs,
// AppConstants.bookingFlightDO.vecBaggageRequestDOs,
// AppConstants.bookingFlightDO.vecInsrRequestDOs,
// AppConstants.bookingFlightDO.vecSeatRequestDOs,
// AppConstants.bookingFlightDO.vecSSRRequests,
// AppConstants.bookingFlightDO.isFlexiOut,
// AppConstants.bookingFlightDO.isFlexiIn,
// AppConstants.bookingFlightDO.pnr,
// AppConstants.bookingFlightDO.pnrType,null,null,
// AppConstants.bookingFlightDO.bundleServiceID)) {
// } else {
// hideLoader();
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.InternetProblem),
// getString(R.string.Ok), "", DATAFAIL);
// }
// }
// else
// {
// if (new CommonBL(BookingSummaryActivity.this,
// BookingSummaryActivity.this).getAncillaryPriceQuote(
// AppConstants.bookingFlightDO.requestParameterDO,
// AppConstants.AIRPORT_CODE,
// AppConstants.bookingFlightDO.myBookFlightDO,
// null,
// vecOriginDestinationOptionDOs,
// vecOriginDestinationOptionDOsReturn,
// AppConstants.bookingFlightDO.vecMealReqDOs,
// AppConstants.bookingFlightDO.vecBaggageRequestDOs,
// AppConstants.bookingFlightDO.vecInsrRequestDOs,
// AppConstants.bookingFlightDO.vecSeatRequestDOs,
// AppConstants.bookingFlightDO.vecSSRRequests,
// AppConstants.bookingFlightDO.isFlexiOut,
// AppConstants.bookingFlightDO.isFlexiIn,
// AppConstants.bookingFlightDO.pnr,
// AppConstants.bookingFlightDO.pnrType,null,null,
// AppConstants.bookingFlightDO.bundleServiceID)) {
// } else {
// hideLoader();
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.InternetProblem),
// getString(R.string.Ok), "", DATAFAIL);
// }
// }
//
// }
// }
//
// private void callServiceModifiedResQuery() {
// showLoader("");
// if (AppConstants.bookingFlightDO.myODIDOOneWay != null) {
// if (new CommonBL(BookingSummaryActivity.this,
// BookingSummaryActivity.this).getModifiedResQuery(
// AppConstants.bookingFlightDO.requestParameterDO,
// AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
// AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
// AppConstants.bookingFlightDO.pnr, AppConstants.bookingFlightDO.pnrType,
// "15")) {
// } else
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.InternetProblem),
// getString(R.string.Ok), "", DATAFAIL);
// } else {
// if (new CommonBL(BookingSummaryActivity.this,
// BookingSummaryActivity.this)
// .getModifiedResQuery(
// AppConstants.bookingFlightDO.requestParameterDO,
// AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
// AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs,
// AppConstants.bookingFlightDO.pnr, AppConstants.bookingFlightDO.pnrType,
// "15")) {
// } else
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.InternetProblem),
// getString(R.string.Ok), "", DATAFAIL);
// }
// }
//
// @Override
// public void dataRetreived(Response data) {
// if (data != null && !data.isError) {
// switch (data.method) {
// case ANCILLARY_PRICEQUOTE:
// airPriceQuoteDO = new AirPriceQuoteDO();
// airPriceQuoteDO = (AirPriceQuoteDO) data.data;
//
// if (tvBookSummaryTotal.getText().toString().equalsIgnoreCase("")
// && airPriceQuoteDO != null
// && airPriceQuoteDO.vecPricedItineraryDOs != null
// && airPriceQuoteDO.vecPricedItineraryDOs.size() > 0) {
// priceCode =
// airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.currencyCode;
// totalprice =
// StringUtils.getFloat(airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.amount);
//
// tvBookSummaryTotal.setText(priceCode
// + " "
// + StringUtils.getStrFloatFromString(airPriceQuoteDO.vecPricedItineraryDOs
// .get(0).totalFare.amount));
// updateAdditionalView(priceCode,airPriceQuoteDO.vecPricedItineraryDOs);
// hideLoader();
// } else {
// hideLoader();
//
// if(airPriceQuoteDO.ErrorMsg.equalsIgnoreCase(""))
// {
// showCustomDialog(BookingSummaryActivity.this,
// getString(R.string.Alert),
// getString(R.string.TechProblem),
// getString(R.string.Ok), "", DATAFAIL);
// }
// else {
// showCustomDialog(BookingSummaryActivity.this,
// getString(R.string.Alert),
// airPriceQuoteDO.ErrorMsg,
// getString(R.string.Ok), "", DATAFAIL);
// }
// }
// break;
//
// case MODIFIED_RESQUERY:
// if (data.data instanceof ModifiedPNRResDO)
// {
// ModifiedPNRResDO modifiedPNRResDO = (ModifiedPNRResDO) data.data;
//
// if (modifiedPNRResDO != null
// && modifiedPNRResDO.vecTravelerCnxModAddResBalancesDO != null
// && modifiedPNRResDO.vecTravelerCnxModAddResBalancesDO.size() > 0
// && modifiedPNRResDO.vecTravelerCnxModAddResBalancesDO.get(0) != null)
// {
// moveToPaymentActivity(modifiedPNRResDO);
// }
// else {
// hideLoader();
// if (modifiedPNRResDO != null
// &&
// modifiedPNRResDO.errorMessage.equalsIgnoreCase(AppConstants.BOOKING_UNCHANGED_SEGMENT_TEXT))
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.SelectedFlightsCannot),
// getString(R.string.Ok), "", DATAFAIL);
// else
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.ErrorWhileProcessing) + " "
// + getString(R.string.StartAgain),
// getString(R.string.Ok), "", DATAFAIL);
// }
// } else if (data.data instanceof String) {
// hideLoader();
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.TechProblem)+getString(R.string.TryAgainAfter),
// getString(R.string.Ok), "",
// DATAFAIL);
// } else {
// hideLoader();
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.Payment_Error),
// getString(R.string.Ok), "", DATAFAIL);
// }
// break;
//
// case MODIFY_RESERVATION:
// if (data.data instanceof AirBookDO) {
// AirBookDO airBookDO = (AirBookDO) data.data;
// if (!airBookDO.bookingID.equalsIgnoreCase(""))
// {
// moveToPaymentSummaryActivity(airBookDO);
// }
// else if (!airBookDO.errorMessage.equalsIgnoreCase(""))
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.TechProblem)+getString(R.string.TryAgainAfter),
// getString(R.string.Ok),
// "", DATAFAIL);
// else
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.SessionExpired) + " "
// + getString(R.string.StartAgain),
// getString(R.string.Ok), "", DATAFAIL);
// } else if (data.data instanceof String) {
// showCustomDialog(this, getString(R.string.Alert),
// data.data.toString(), getString(R.string.Ok), "",
// DATAFAIL);
// } else {
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.Payment_Error),
// getString(R.string.Ok), "", DATAFAIL);
// }
// hideLoader();
// break;
// default:
// break;
// }
// } else {
// hideLoader();
// if(data.data instanceof String)
// {
// if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
// showCustomDialog(getApplicationContext(), getString(R.string.Alert),
// getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "",
// DATAFAIL);
// else showCustomDialog(getApplicationContext(), getString(R.string.Alert),
// getString(R.string.TechProblem), getString(R.string.Ok), "", DATAFAIL);
// }
// else
// showCustomDialog(BookingSummaryActivity.this, getString(R.string.Alert),
// getString(R.string.TechProblem), getString(R.string.Ok), "", DATAFAIL);
// }
// }
//
// @Override
// public void onButtonYesClick(String from) {
// super.onButtonYesClick(from);
//
// if (from.equalsIgnoreCase(DATAFAIL))
// clickHome();
// else finish();
// }
//
//// =========================================================================newly
// added for up ===============================================================
//
// private void updateAdditionalView(String priceCode,
// ArrayList<PricedItineraryDO> vecPricedItineraryDOs) {
// vecFareDOMeals.clear();
// vecFareDOBaggages.clear();
// vecFareDOSeats.clear();
// vecFareDOInsurance.clear();
// vecFareDOHala.clear();
// for (int i = 0; i < vecPricedItineraryDOs.size(); i++) {
// for (PTC_FareBreakdownDO ptc_FareBreakdownDO : vecPricedItineraryDOs
// .get(i).vecPTC_FareBreakdownDOs) {
// for (FareDO fareDO : ptc_FareBreakdownDO.vecFees) {
// if (fareDO.feeCode.contains(AppConstants.MEAL_TAG))
// vecFareDOMeals.add(fareDO);
// else if (fareDO.feeCode.contains(AppConstants.BAGGAGE_TAG))
// vecFareDOBaggages.add(fareDO);
// else if (fareDO.feeCode.contains(AppConstants.SEAT_TAG))
// vecFareDOSeats.add(fareDO);
// else if (fareDO.feeCode.contains(AppConstants.INSURANCE_TAG))
// vecFareDOInsurance.add(fareDO);
// else if (fareDO.feeCode.contains(AppConstants.HALA_TAG))
// vecFareDOHala.add(fareDO);
// }
// }
// }
//
// float feesMeals = 0.0f, feesBaggages = 0.0f, feesSeats = 0.0f, feesInsurance
// = 0.0f, feesHala = 0.0f, feesTotal = 0.0f;
//
// if (vecFareDOMeals.size() > 0) {
// for (FareDO fareDO : vecFareDOMeals) {
// feesMeals = feesMeals + StringUtils.getFloat(fareDO.amount);
// }
// tvMealsCost.setText(priceCode + " "+
// StringUtils.getStringFromFloat(feesMeals));
// llMealsCost.setVisibility(View.VISIBLE);
// }else{
// llMealsCost.setVisibility(View.VISIBLE);
// tvMealsCost.setText(priceCode +" 0.00");
// }
//
// if (vecFareDOBaggages.size() > 0) {
// for (FareDO fareDO : vecFareDOBaggages) {
// feesBaggages = feesBaggages+ StringUtils.getFloat(fareDO.amount);
// }
// tvBaggageCost.setText(priceCode + " "+
// StringUtils.getStringFromFloat(feesBaggages));
// llBaggageCost.setVisibility(View.VISIBLE);
// }else{
// llBaggageCost.setVisibility(View.VISIBLE);
// tvBaggageCost.setText(priceCode +" 0.00");
// }
// if (vecFareDOSeats.size() > 0) {
// for (FareDO fareDO : vecFareDOSeats) {
// feesSeats = feesSeats + StringUtils.getFloat(fareDO.amount);
// }
// tvSeatsCost.setText(priceCode + " "+
// StringUtils.getStringFromFloat(feesSeats));
// llSeatsCost.setVisibility(View.VISIBLE);
// }else{
// llSeatsCost.setVisibility(View.VISIBLE);
// tvSeatsCost.setText(priceCode +" 0.00");
// }
// if (vecFareDOInsurance.size() > 0) {
// for (FareDO fareDO : vecFareDOInsurance) {
// feesInsurance = feesInsurance+ StringUtils.getFloat(fareDO.amount);
// }
// tvInsuranceCost.setText(priceCode + " " +
// StringUtils.getStringFromFloat(feesInsurance));
// llInsuranceCost.setVisibility(View.VISIBLE);
// }else{
// llInsuranceCost.setVisibility(View.VISIBLE);
// tvInsuranceCost.setText(priceCode +" 0.00");
// }
// if (vecFareDOHala.size() > 0) {
// for (FareDO fareDO : vecFareDOHala) {
// feesHala = feesHala + StringUtils.getFloat(fareDO.amount);
// }
// tvHalaCost.setText(priceCode + " " +
// StringUtils.getStringFromFloat(feesHala));
// llHalaCost.setVisibility(View.VISIBLE);
// }else{
// llHalaCost.setVisibility(View.VISIBLE);
// tvHalaCost.setText(priceCode +" 0.00");
// }
// feesTotal = feesMeals + feesBaggages + feesSeats + feesInsurance + feesHala +
// totalpriceOutBound;
// totalPriceInOutBound = totalPriceInOutBound + (totalprice - feesTotal);
// tvTotalFlightCost.setText(priceCode+"
// "+StringUtils.getStringFromFloat(totalPriceInOutBound));
//
// if(feesMeals == 0
// && feesBaggages == 0
// && feesSeats == 0
// && feesInsurance == 0
// && feesHala == 0)
// {
// llAdditionalServices.setVisibility(View.VISIBLE);
// tvAdditionalServicesHeader.setVisibility(View.VISIBLE);
// }
// }
// public void showFlightDetails(LinearLayout ll)
// {
// if (customDialog != null && customDialog.isShowing())
// customDialog.dismiss();
//
// customDialog = new CustomDialog(BookingSummaryActivity.this, ll,
// AppConstants.DEVICE_WIDTH - 40, LayoutParams.WRAP_CONTENT, true);
//
// customDialog.show();
// }
// }