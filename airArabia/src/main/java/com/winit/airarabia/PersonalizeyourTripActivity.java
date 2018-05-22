//package com.winit.airarabia;
//
//import java.util.Vector;
//
//import android.annotation.SuppressLint;
//import android.app.Dialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.ScrollView;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.winit.airarabia.adapters.BookingMealAdapter;
//import com.winit.airarabia.adapters.BookingMealMainAdapter;
//import com.winit.airarabia.adapters.SpinnerBaggageAdapter;
//import com.winit.airarabia.busynesslayer.CommonBL;
//import com.winit.airarabia.busynesslayer.DataListener;
//import com.winit.airarabia.common.AppConstants;
//import com.winit.airarabia.controls.CustomDialog;
//import com.winit.airarabia.objects.AirBaggageDetailsDO;
//import com.winit.airarabia.objects.AirHalaDO;
//import com.winit.airarabia.objects.AirMealDetailsDO;
//import com.winit.airarabia.objects.AirPriceQuoteDO;
//import com.winit.airarabia.objects.AirRowDO;
//import com.winit.airarabia.objects.AirSeatDO;
//import com.winit.airarabia.objects.AirSeatMapDO;
//import com.winit.airarabia.objects.AirportServiceDO;
//import com.winit.airarabia.objects.BaggageDO;
//import com.winit.airarabia.objects.FlightSegmentDO;
//import com.winit.airarabia.objects.HalaDO;
//import com.winit.airarabia.objects.InsuranceQuoteDO;
//import com.winit.airarabia.objects.MealCategoriesDO;
//import com.winit.airarabia.objects.MealDO;
//import com.winit.airarabia.objects.MealDetailsDO;
//import com.winit.airarabia.objects.OriginDestinationOptionDO;
//import com.winit.airarabia.objects.PassengerInfoPersonDO;
//import com.winit.airarabia.objects.RequestDO;
//import com.winit.airarabia.objects.SeatMapDO;
//import com.winit.airarabia.utils.StringUtils;
//import com.winit.airarabia.webaccess.Response;
//
//public class PersonalizeyourTripActivity extends BaseActivity implements
//OnClickListener, DataListener {
//
//	private LinearLayout llPersonalize;
//	private LinearLayout llBaggageMain, llSeatMain, llMealMain,
//	llInsuranceMain, llHalaMain, llInsuranceHeader;
//	private TextView tvMealText, tvHalaText;
//	private ImageView ivCheckseatbox, ivUnCheckseatbox, ivChecktravelinsurance,
//	ivUnchecktravelinsurance, ivCheckmeal, ivUncheckmeal,
//	ivCheckhalaservices, ivUncheckhalaservices;
//	private TextView tvCheckseatbox, tvUnCheckseatbox, tvChecktravelinsurance,
//	tvUnchecktravelinsurance, tvCheckmeal, tvUncheckmeal,
//	tvCheckhalaservices, tvUncheckhalaservices, tvInsuranceHeader;
//	private View seatlayoutview;
//	private Vector<BaggageDO> vecBaggageList;
//	private Dialog dialogMealMain, dialogMealSub;
//	private Vector<AirBaggageDetailsDO> vecAirBaggageDetailsDO;
//	private AirSeatMapDO airSeatMapDO;
//	private AirMealDetailsDO airMealDetailsDO;
//	private InsuranceQuoteDO insuranceQuoteDO;
//	private Vector<AirHalaDO> vecAirHalaDOs;
//	private Vector<HalaDO> vecHalaDos;
//	private boolean isBaggageService = false, isHalaService = false;
//	private boolean isInsuranceChecked = false;
//	private boolean isManageBook = false, isReturn = false, isOutBound = false;
//	private AirPriceQuoteDO airPriceQuoteDOOutBound, airPriceQuoteDOInBound,
//	airPriceQuoteDOTotal;
//	private PersonalizeyourTripActivity.BCR bcr;
//	private Vector<FlightSegmentDO> vecFlights;
//
//	private class BCR extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			if (intent.getAction().equalsIgnoreCase(AppConstants.HOME_CLICK))
//				finish();
//			if (intent.getAction().equalsIgnoreCase(AppConstants.BOOK_SUCCESS))
//				finish();
//		}
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		unregisterReceiver(bcr);
//	}
//
//	@Override
//	public void initilize() {
//		bcr = new BCR();
//		intentFilter.addAction(AppConstants.HOME_CLICK);
//		intentFilter.addAction(AppConstants.BOOK_SUCCESS);
//		registerReceiver(bcr, intentFilter);
//		airPriceQuoteDOOutBound = (AirPriceQuoteDO) getIntent().getExtras()
//				.getSerializable(AppConstants.AIRPORTS_PRICE_OUTBOUND);
//		airPriceQuoteDOInBound = (AirPriceQuoteDO) getIntent().getExtras()
//				.getSerializable(AppConstants.AIRPORTS_PRICE_INBOUND);
//		airPriceQuoteDOTotal = (AirPriceQuoteDO) getIntent().getExtras()
//				.getSerializable(AppConstants.AIRPORTS_PRICE_TOTAL);
//
//		if (getIntent().hasExtra(AppConstants.MANAGE_BOOKING))
//			isManageBook = getIntent().getBooleanExtra(
//					AppConstants.MANAGE_BOOKING, isManageBook);
//		if (getIntent().hasExtra(AppConstants.RETURN))
//			isReturn = getIntent().getBooleanExtra(AppConstants.RETURN,
//					isReturn);
//		if (getIntent().hasExtra(AppConstants.IS_DEPARTURE))
//			isOutBound = getIntent().getBooleanExtra(AppConstants.IS_DEPARTURE,
//					isOutBound);
//
//		llPersonalize = (LinearLayout) layoutInflater.inflate(
//				R.layout.personalizeyourtrip, null);
//		llMiddleBase.addView(llPersonalize, LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT);
//
//		llBaggageMain = (LinearLayout) llPersonalize
//				.findViewById(R.id.llBaggageMain);
//		llInsuranceMain = (LinearLayout) llPersonalize
//				.findViewById(R.id.llInsuranceMain);
//		tvInsuranceHeader = (TextView) llPersonalize
//				.findViewById(R.id.tvInsuranceHeader);
//		llInsuranceHeader = (LinearLayout) llPersonalize
//				.findViewById(R.id.llInsuranceHeader);
//		llHalaMain = (LinearLayout) llPersonalize.findViewById(R.id.llHalaMain);
//		tvMealText = (TextView) llPersonalize.findViewById(R.id.tvMealText);
//		tvHalaText = (TextView) llPersonalize.findViewById(R.id.tvHalaText);
//		llSeatMain = (LinearLayout) llPersonalize.findViewById(R.id.llSeatMain);
//		llMealMain = (LinearLayout) llPersonalize.findViewById(R.id.llMealMain);
//		ivCheckseatbox = (ImageView) llPersonalize
//				.findViewById(R.id.ivCheckseatbox);
//		ivUnCheckseatbox = (ImageView) llPersonalize
//				.findViewById(R.id.ivUnCheckseatbox);
//		ivChecktravelinsurance = (ImageView) llPersonalize
//				.findViewById(R.id.ivChecktravelinsurance);
//		ivUnchecktravelinsurance = (ImageView) llPersonalize
//				.findViewById(R.id.ivUnchecktravelinsurance);
//		ivCheckmeal = (ImageView) llPersonalize.findViewById(R.id.ivCheckmeal);
//		ivUncheckmeal = (ImageView) llPersonalize
//				.findViewById(R.id.ivUncheckmeal);
//		ivCheckhalaservices = (ImageView) llPersonalize
//				.findViewById(R.id.ivCheckhalaservices);
//		ivUncheckhalaservices = (ImageView) llPersonalize
//				.findViewById(R.id.ivUncheckhalaservices);
//		tvCheckseatbox = (TextView) llPersonalize
//				.findViewById(R.id.tvCheckseatbox);
//		tvUnCheckseatbox = (TextView) llPersonalize
//				.findViewById(R.id.tvUnCheckseatbox);
//		tvChecktravelinsurance = (TextView) llPersonalize
//				.findViewById(R.id.tvChecktravelinsurance);
//		tvUnchecktravelinsurance = (TextView) llPersonalize
//				.findViewById(R.id.tvUnchecktravelinsurance);
//		tvCheckmeal = (TextView) llPersonalize.findViewById(R.id.tvCheckmeal);
//		tvUncheckmeal = (TextView) llPersonalize
//				.findViewById(R.id.tvUncheckmeal);
//		tvCheckhalaservices = (TextView) llPersonalize
//				.findViewById(R.id.tvCheckhalaservices);
//		tvUncheckhalaservices = (TextView) llPersonalize
//				.findViewById(R.id.tvUncheckhalaservices);
//		btnSubmitNext.setVisibility(View.VISIBLE);
//		btnSubmitNext.setText(getString(R.string.Continue));
//
//		AppConstants.bookingFlightDO.vecSeatRequestDOs = new Vector<RequestDO>();
//		AppConstants.bookingFlightDO.vecMealReqDOs = new Vector<RequestDO>();
//		AppConstants.bookingFlightDO.vecBaggageRequestDOs = new Vector<RequestDO>();
//		AppConstants.bookingFlightDO.vecInsrRequestDOs = new Vector<RequestDO>();
//		AppConstants.bookingFlightDO.vecSSRRequests = new Vector<RequestDO>();
//		vecAirBaggageDetailsDO = new Vector<AirBaggageDetailsDO>();
//		vecHalaDos = new Vector<HalaDO>();
//		vecAirHalaDOs = new Vector<AirHalaDO>();
//		vecFlights = new Vector<FlightSegmentDO>();
//	}
//
//	@Override
//	public void bindingControl() {
//
//		if (isManageBook) {
//			tvInsuranceHeader.setVisibility(View.GONE);
//			llInsuranceHeader.setVisibility(View.GONE);
//			llInsuranceMain.setVisibility(View.GONE);
//		}
//		vecFlights.clear();
//		vecAirBaggageDetailsDO.clear();
//		AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airPriceQuoteDOTotal.transactionIdentifier;
//
//		for (int i = 0; i < AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//				.size(); i++) {
//			vecFlights
//			.addAll(AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//					.get(i).vecFlightSegmentDOs);
//		}
//		for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//				.size(); i++) {
//			vecFlights
//			.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//					.get(i).vecFlightSegmentDOs);
//		}
//
//		if (isManageBook && isOutBound)
//			callBaggageServiceOneWay();
//		else if (isManageBook && !isOutBound)
//			callBaggageServiceReturn();
//		else
//			callBaggageServiceOneWay();
//
//		tvHeaderTitle.setText(getString(R.string.PersonaliseYourTrip));
//		btnSubmitNext.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				AppConstants.bookingFlightDO.vecMealReqDOs.clear();
//
//				if (airMealDetailsDO != null
//						&& airMealDetailsDO.vecMealDetailsDOs != null
//						&& airMealDetailsDO.vecMealDetailsDOs.size() > 0)
//					for (MealDetailsDO mealDetailsDO : airMealDetailsDO.vecMealDetailsDOs) {
//						for (Vector<RequestDO> vecReqDos : mealDetailsDO.vecMealRequestDOs) {
//							AppConstants.bookingFlightDO.vecMealReqDOs.addAll(vecReqDos);
//						}
//					}
//
//				moveToBookingSummaryActivity();
//			}
//		});
//
//		ivCheckseatbox.setOnClickListener(this);
//		ivUnCheckseatbox.setOnClickListener(this);
//		ivChecktravelinsurance.setOnClickListener(this);
//		ivUnchecktravelinsurance.setOnClickListener(this);
//		ivCheckmeal.setOnClickListener(this);
//		ivUncheckmeal.setOnClickListener(this);
//		ivCheckhalaservices.setOnClickListener(this);
//		ivUncheckhalaservices.setOnClickListener(this);
//		tvCheckseatbox.setOnClickListener(this);
//		tvUnCheckseatbox.setOnClickListener(this);
//		tvChecktravelinsurance.setOnClickListener(this);
//		tvUnchecktravelinsurance.setOnClickListener(this);
//		tvCheckmeal.setOnClickListener(this);
//		tvUncheckmeal.setOnClickListener(this);
//		tvCheckhalaservices.setOnClickListener(this);
//		tvUncheckhalaservices.setOnClickListener(this);
//	}
//
//	protected void moveToBookingSummaryActivity() {
//		Intent in = new Intent(PersonalizeyourTripActivity.this,
//				BookingSummaryActivity.class);
//		in.putExtra(AppConstants.AIRPORTS_PRICE_OUTBOUND,
//				airPriceQuoteDOOutBound);
//		in.putExtra(AppConstants.AIRPORTS_PRICE_INBOUND, airPriceQuoteDOInBound);
//		in.putExtra(AppConstants.AIRPORTS_PRICE_TOTAL, airPriceQuoteDOTotal);
//		in.putExtra(AppConstants.MANAGE_BOOKING, isManageBook);
//		in.putExtra(AppConstants.RETURN, isReturn);
//		in.putExtra(AppConstants.IS_DEPARTURE, isOutBound);
//		startActivity(in);
//	}
//
//	@Override
//	public void onClick(View v) {
//		if (v.getId() == R.id.ivCheckseatbox)
//			addSeatItems(airSeatMapDO);
//		else if (v.getId() == R.id.ivUnCheckseatbox)
//			removeSeatItems();
//		else if (v.getId() == R.id.ivChecktravelinsurance)
//			addtravelinsuranceItems(insuranceQuoteDO);
//		else if (v.getId() == R.id.ivUnchecktravelinsurance)
//			removetravelinsuranceItems();
//		else if (v.getId() == R.id.ivCheckmeal)
//			addMealItems(airMealDetailsDO);
//		else if (v.getId() == R.id.ivUncheckmeal)
//			removeMealItems();
//		else if (v.getId() == R.id.ivCheckhalaservices)
//			addHalaItems();
//		else if (v.getId() == R.id.ivUncheckhalaservices)
//			removeHalaItems();
//		else if (v.getId() == R.id.tvCheckseatbox)
//			ivCheckseatbox.performClick();
//		else if (v.getId() == R.id.tvUnCheckseatbox)
//			ivUnCheckseatbox.performClick();
//		else if (v.getId() == R.id.tvChecktravelinsurance)
//			ivChecktravelinsurance.performClick();
//		else if (v.getId() == R.id.tvUnchecktravelinsurance)
//			ivUnchecktravelinsurance.performClick();
//		else if (v.getId() == R.id.tvCheckmeal)
//			ivCheckmeal.performClick();
//		else if (v.getId() == R.id.tvUncheckmeal)
//			ivUncheckmeal.performClick();
//		else if (v.getId() == R.id.tvCheckhalaservices)
//			ivCheckhalaservices.performClick();
//		else if (v.getId() == R.id.tvUncheckhalaservices)
//			ivUncheckhalaservices.performClick();
//	}
//
//	private void addBaggageItems(
//			Vector<AirBaggageDetailsDO> vecAirBaggageDetailsDO) {
////		llBaggageMain.removeAllViews();
////		llBaggageMain.setVisibility(View.VISIBLE);
//
//		if (vecAirBaggageDetailsDO != null && vecAirBaggageDetailsDO.size() > 0) {
//			AppConstants.bookingFlightDO.vecBaggageRequestDOs.clear();
////			if (vecAirBaggageDetailsDO.size() == 1
////					&& (vecAirBaggageDetailsDO.get(0).vecBaggageDetailDOs == null
////					|| vecAirBaggageDetailsDO.get(0).vecBaggageDetailDOs
////					.size() == 0 || vecAirBaggageDetailsDO
////					.get(0).vecBaggageDetailDOs.get(0).vecFlightSegmentDOs
////					.size() == 0)) {
////				addNoDataAvailable(getString(R.string.NoBaggage), llBaggageMain, false);
////			} else {
//				for (AirBaggageDetailsDO airBaggageDetailsDO : vecAirBaggageDetailsDO) {
//					if (airBaggageDetailsDO.vecBaggageDetailDOs != null
//							&& airBaggageDetailsDO.vecBaggageDetailDOs.size() > 0
//							&& airBaggageDetailsDO.vecBaggageDetailDOs.get(0).vecFlightSegmentDOs
//							.size() > 0) {
//						for (int i = 0; i < airBaggageDetailsDO.vecBaggageDetailDOs
//								.size(); i++) {
//							vecBaggageList = airBaggageDetailsDO.vecBaggageDetailDOs
//									.get(i).vecBaggageDO;
//							LinearLayout llBaggageChild = (LinearLayout) layoutInflater
//									.inflate(R.layout.personalise_meal_item, null);
//
//							LinearLayout llPersonalTripItemData = (LinearLayout) llBaggageChild
//									.findViewById(R.id.llPersonalTripItemData);
//							LinearLayout llPersonalTripItemNoData = (LinearLayout) llBaggageChild
//									.findViewById(R.id.llPersonalTripItemNoData);
//							LinearLayout llPersonalTripItemNoDataSub = (LinearLayout) llBaggageChild
//									.findViewById(R.id.llPersonalTripItemNoDataSub);
//							TextView tvTravelType = (TextView) llBaggageChild
//									.findViewById(R.id.tvTravelType);
//
//							TextView tvPersonalTripItemMealSource = (TextView) llBaggageChild
//									.findViewById(R.id.tvPersonalTripItemMealSource);
//							TextView tvPersonalTripItemMealDest = (TextView) llBaggageChild
//									.findViewById(R.id.tvPersonalTripItemMealDest);
//							LinearLayout llPersonalTripItemMealSubMain = (LinearLayout) llBaggageChild
//									.findViewById(R.id.llPersonalTripItemMealSubMain);
//							TextView tvServiceName = (TextView) llBaggageChild
//									.findViewById(R.id.tvServiceName);
//							TextView tvServiceCurencyCode = (TextView) llBaggageChild
//									.findViewById(R.id.tvServiceCurencyCode);
//
//							tvServiceCurencyCode.setText(AppConstants.CurrencyCode);
//							tvServiceName.setText(getString(R.string.Baggage));
//
//							if (vecBaggageList != null && vecBaggageList.size() > 0) {
//								Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDO = new Vector<PassengerInfoPersonDO>();
//								for (PassengerInfoPersonDO passengerInfoPersonDO : AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO) {
//									if (!passengerInfoPersonDO.persontype
//											.equalsIgnoreCase(AppConstants.PERSON_TYPE_INFANT))
//										vecPassengerInfoPersonDO.add(passengerInfoPersonDO);
//								}
//
//								for (int j = 0; j < vecPassengerInfoPersonDO
//										.size(); j++) {
//									LinearLayout llItemSeatSub = (LinearLayout) layoutInflater
//											.inflate(R.layout.personalise_meal_item_sub, null);
//
//									TextView tvPersonalTripItemName = (TextView) llItemSeatSub
//											.findViewById(R.id.tvPersonalTripItemName);
//									final TextView tvPersonalTripItemAmount = (TextView) llItemSeatSub
//											.findViewById(R.id.tvPersonalTripItemAmount);
//									final TextView tvPersonalTripItemAEDValue = (TextView) llItemSeatSub
//											.findViewById(R.id.tvPersonalTripItemAEDValue);
//									llPersonalTripItemMealSubMain.addView(llItemSeatSub);
//									tvPersonalTripItemName
//									.setText(vecPassengerInfoPersonDO.get(j).personfirstname);
//									tvPersonalTripItemAmount.setVisibility(View.GONE);
//									final LinearLayout llPersonalTripItemAmount = (LinearLayout) llItemSeatSub
//											.findViewById(R.id.llPersonalTripItemAmount);
//
//									final Spinner spPersonalTripItemAmount = (Spinner) llItemSeatSub
//											.findViewById(R.id.spPersonalTripItemAmount);
//
//									final Vector<RequestDO> vecBaggageRequestDOsNew = new Vector<RequestDO>();
//									for (int k = 0; k < airBaggageDetailsDO.vecBaggageDetailDOs
//											.get(i).vecFlightSegmentDOs.size(); k++) {
//
//										FlightSegmentDO flightSegmentDO = airBaggageDetailsDO.vecBaggageDetailDOs
//												.get(i).vecFlightSegmentDOs.get(k);
//										final RequestDO requestDO = new RequestDO();
//										requestDO.flightNumber = flightSegmentDO.flightNumber;
//
//										final PassengerInfoPersonDO passengerInfoPersonDO = vecPassengerInfoPersonDO.get(j);
//										requestDO.departureDate = flightSegmentDO.departureDateTime;
//										requestDO.travelerRefNumberRPHList = passengerInfoPersonDO.travelerRefNumberRPHList;
//										requestDO.flightNumber = flightSegmentDO.flightNumber;
//										requestDO.flightRefNumberRPHList = flightSegmentDO.RPH;
//										if (vecBaggageList != null&& vecBaggageList.size() > 0)
//											requestDO.baggageCode = vecBaggageList.get(0).baggageCode;
//										else
//											requestDO.baggageCode = "";
//
//										vecBaggageRequestDOsNew.add(requestDO);
//										AppConstants.bookingFlightDO.vecBaggageRequestDOs.add(requestDO);
//									}
//									//									tvPersonalTripItemAmount.setTag(j + "");
//									llPersonalTripItemAmount.setVisibility(View.VISIBLE);
//									spPersonalTripItemAmount.setVisibility(View.VISIBLE);
//
//									final SpinnerBaggageAdapter adapter = new SpinnerBaggageAdapter(PersonalizeyourTripActivity.this, vecBaggageList);
//									spPersonalTripItemAmount.setAdapter(adapter);
//									spPersonalTripItemAmount.setTag(vecBaggageList);
//
//									spPersonalTripItemAmount.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//										@Override
//										public void onItemSelected(
//												AdapterView<?> arg0, View arg1,
//												int pos, long arg3) {
//											Vector<BaggageDO> vecBaggageListSel = (Vector<BaggageDO>)spPersonalTripItemAmount.getTag();
//											tvPersonalTripItemAEDValue.setText(StringUtils.getStrFloatFromString(vecBaggageListSel
//													.get(pos).baggageCharge));
//											for (int j = 0; j < vecBaggageRequestDOsNew.size(); j++) {
//
//												vecBaggageRequestDOsNew.get(j).baggageCode = vecBaggageListSel.get(pos).baggageCode;
//											}
//											adapter.refreshPos(pos);
//										}
//
//										@Override
//										public void onNothingSelected(
//												AdapterView<?> arg0) {
//
//										}
//									});
//								}
//							} else {
//								llPersonalTripItemData.setVisibility(View.GONE);
//								llPersonalTripItemNoData
//								.setVisibility(View.VISIBLE);
//								if (i == 0)
//									tvTravelType.setText(R.string.Outbound);
//								else
//									tvTravelType.setText(R.string.Inbound);
//								addNoDataAvailable(getString(R.string.NoBaggage),
//										llPersonalTripItemNoDataSub, true);
//							}
//							llBaggageMain.addView(llBaggageChild);
//
//							if (AppConstants.allAirportNamesDO != null
//									&& AppConstants.allAirportNamesDO.vecAirport != null
//									&& AppConstants.allAirportNamesDO.vecAirport
//									.size() > 0) {
//								String sourceName = airBaggageDetailsDO.vecBaggageDetailDOs
//										.get(i).vecFlightSegmentDOs.get(0).departureAirportCode, originName = airBaggageDetailsDO.vecBaggageDetailDOs
//										.get(i).vecFlightSegmentDOs
//										.get(airBaggageDetailsDO.vecBaggageDetailDOs
//												.get(i).vecFlightSegmentDOs
//												.size() - 1).arrivalAirportCode;
//
//								updateCountryNameFromCode(
//										sourceName,
//										AppConstants.allAirportNamesDO.vecAirport,
//										tvPersonalTripItemMealSource);
//								updateCountryNameFromCode(
//										originName,
//										AppConstants.allAirportNamesDO.vecAirport,
//										tvPersonalTripItemMealDest);
//							} else {
//								tvPersonalTripItemMealSource
//								.setText(airBaggageDetailsDO.vecBaggageDetailDOs
//										.get(i).vecFlightSegmentDOs
//										.get(0).departureAirportCode);
//								tvPersonalTripItemMealDest
//								.setText(airBaggageDetailsDO.vecBaggageDetailDOs
//										.get(i).vecFlightSegmentDOs
//										.get(airBaggageDetailsDO.vecBaggageDetailDOs
//												.get(i).vecFlightSegmentDOs
//												.size() - 1).arrivalAirportCode);
//							}
//						}
//					}
//				}
//			}
////		} else
////			addNoDataAvailable(getString(R.string.NoBaggage), llBaggageMain, false);
//	}
//
//	private void addHalaItems(Vector<HalaDO> vecHalaDOs) {
//		llHalaMain.removeAllViews();
//		llHalaMain.setVisibility(View.VISIBLE);
//
//		if (vecHalaDOs != null && vecHalaDOs.size() > 0) {
//			AppConstants.bookingFlightDO.vecSSRRequests.clear();
//			for (final HalaDO halaDO : vecHalaDOs) {
//				if (halaDO.vecAirportServiceDOs != null
//						&& halaDO.vecAirportServiceDOs.size() > 0) {
//					LinearLayout llBaggageChild = (LinearLayout) layoutInflater
//							.inflate(R.layout.personalise_meal_item, null);
//					TextView tvPersonalTripItemMealSource = (TextView) llBaggageChild
//							.findViewById(R.id.tvPersonalTripItemMealSource);
//					TextView tvPersonalTripItemMealDest = (TextView) llBaggageChild
//							.findViewById(R.id.tvPersonalTripItemMealDest);
//					LinearLayout llPersonalTripItemMealSubMain = (LinearLayout) llBaggageChild
//							.findViewById(R.id.llPersonalTripItemMealSubMain);
//					TextView tvServiceName = (TextView) llBaggageChild
//							.findViewById(R.id.tvServiceName);
//					TextView tvServiceCurencyCode = (TextView) llBaggageChild
//							.findViewById(R.id.tvServiceCurencyCode);
//
//					tvServiceCurencyCode.setText(AppConstants.CurrencyCode);
//					tvServiceName.setText(getString(R.string.AirportService));
//
//
//					Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDO = new Vector<PassengerInfoPersonDO>();
//					for (PassengerInfoPersonDO passengerInfoPersonDO : AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO) {
//						if (!passengerInfoPersonDO.persontype
//								.equalsIgnoreCase(AppConstants.PERSON_TYPE_INFANT))
//							vecPassengerInfoPersonDO
//							.add(passengerInfoPersonDO);
//					}
//
//					for (int j = 0; j < vecPassengerInfoPersonDO.size(); j++) {
//						LinearLayout llItemSeatSub = (LinearLayout) layoutInflater
//								.inflate(
//										R.layout.personalise_meal_item_sub,
//										null);
//
//						TextView tvPersonalTripItemName = (TextView) llItemSeatSub
//								.findViewById(R.id.tvPersonalTripItemName);
//						final TextView tvPersonalTripItemAmount = (TextView) llItemSeatSub
//								.findViewById(R.id.tvPersonalTripItemAmount);
//						final TextView tvPersonalTripItemAEDValue = (TextView) llItemSeatSub
//								.findViewById(R.id.tvPersonalTripItemAEDValue);
//						final LinearLayout llPersonalTripItemCross = (LinearLayout) llItemSeatSub
//								.findViewById(R.id.llPersonalTripItemCross);
//
//						llPersonalTripItemMealSubMain
//						.addView(llItemSeatSub);
//						tvPersonalTripItemName
//						.setText(vecPassengerInfoPersonDO.get(j).personfirstname);
//						tvPersonalTripItemAEDValue.setText("0.00");
//
//						final PassengerInfoPersonDO passengerInfoPersonDO = vecPassengerInfoPersonDO
//								.get(j);
//
//						tvPersonalTripItemAmount.setTag(j + "");
//
//						final Vector<AirportServiceDO> vecAirportServiceDOs = new Vector<AirportServiceDO>();
//						if (halaDO.vecAirportServiceDOs.size() > 0)
//							for (AirportServiceDO airportServiceDO : halaDO.vecAirportServiceDOs) {
//								vecAirportServiceDOs.add(airportServiceDO);
//							}
//
//						tvPersonalTripItemAmount
//						.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//
//								showSelHalaPopup(
//										passengerInfoPersonDO,
//										halaDO,
//										vecAirportServiceDOs,
//										tvPersonalTripItemAmount,
//										tvPersonalTripItemAEDValue,
//										llPersonalTripItemCross);
//							}
//						});
//						llPersonalTripItemCross
//						.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								llPersonalTripItemCross
//								.setVisibility(View.INVISIBLE);
//								tvPersonalTripItemAEDValue
//								.setText("0.00");
//								tvPersonalTripItemAmount
//								.setText(getString(R.string.Select));
//
//								for (int k = AppConstants.bookingFlightDO.vecSSRRequests
//										.size() - 1; k >= 0; k--) {
//									if (AppConstants.bookingFlightDO.vecSSRRequests
//											.get(k).flightNumber
//											.equalsIgnoreCase(halaDO.flightSegmentDO.flightNumber)
//											&& AppConstants.bookingFlightDO.vecSSRRequests
//											.get(k).travelerRefNumberRPHList
//											.equalsIgnoreCase(passengerInfoPersonDO.travelerRefNumberRPHList)) {
//										AppConstants.bookingFlightDO.vecSSRRequests
//										.remove(k);
//									}
//								}
//							}
//						});
//					}
//					llHalaMain.addView(llBaggageChild);
//
//					if (AppConstants.allAirportNamesDO != null
//							&& AppConstants.allAirportNamesDO.vecAirport != null
//							&& AppConstants.allAirportNamesDO.vecAirport.size() > 0) {
//						String sourceName = halaDO.flightSegmentDO.departureAirportCode, originName = halaDO.flightSegmentDO.arrivalAirportCode;
//
//						updateCountryNameFromCode(sourceName,
//								AppConstants.allAirportNamesDO.vecAirport,
//								tvPersonalTripItemMealSource);
//						updateCountryNameFromCode(originName,
//								AppConstants.allAirportNamesDO.vecAirport,
//								tvPersonalTripItemMealDest);
//					} else {
//						tvPersonalTripItemMealSource
//						.setText(halaDO.flightSegmentDO.departureAirportCode);
//						tvPersonalTripItemMealDest
//						.setText(halaDO.flightSegmentDO.arrivalAirportCode);
//					}
//				}
//			}
//		} else
//			addNoDataAvailable(getString(R.string.NoHala), llHalaMain, false);
//	}
//
//	private void addNoDataAvailable(String strMsg, LinearLayout llSelected,boolean isNoBg) {
//		TextView tvNoDataAvailable = (TextView) layoutInflater.inflate(
//				R.layout.no_data_found, null);
//		tvNoDataAvailable.setText(strMsg);
//		if(isNoBg)
//			tvNoDataAvailable.setBackgroundResource(0);
//		llSelected.addView(tvNoDataAvailable);
//	}
//
//	private void addNoDataAvailableWithFlight(String strMsg, LinearLayout llSelected,boolean isNoBg, int isOutBound) {
//
//		LinearLayout llEmptyText = (LinearLayout) layoutInflater.inflate(
//				R.layout.empty_text, null);
//
//		TextView tvEmptyText = (TextView) llEmptyText.findViewById(R.id.tvEmpty);
//
//		TextView tvNoDataAvailable = (TextView) layoutInflater.inflate(
//				R.layout.no_data_found, null);
//		tvNoDataAvailable.setText(strMsg);
//		if(isNoBg)
//		{
//			llEmptyText.setBackgroundResource(0);
//			tvNoDataAvailable.setBackgroundResource(0);
//		}
//
//		if(isOutBound == 1)
//		{
//			tvEmptyText.setText(getString(R.string.Outbound));
//		}
//		else
//			tvEmptyText.setText(getString(R.string.Inbound));
//		llSelected.addView(llEmptyText);
//		llSelected.addView(tvNoDataAvailable);
//	}
//
//	private void addSeatItems(AirSeatMapDO airSeatMapDOData) {
//		ivCheckseatbox.setBackgroundResource(R.drawable.check);
//		ivUnCheckseatbox.setBackgroundResource(R.drawable.uncheck);
//		llSeatMain.removeAllViews();
//		llSeatMain.setVisibility(View.VISIBLE);
//
//		if (airSeatMapDOData != null && airSeatMapDOData.vecSeatMapDOs != null
//				&& airSeatMapDOData.vecSeatMapDOs.size() > 0) {
//			AirSeatMapDO airSeatMapDO = new AirSeatMapDO();
//			airSeatMapDO.vecSeatMapDOs = new Vector<SeatMapDO>();
//			airSeatMapDO.vecSeatMapDOs.clear();
//
//			airSeatMapDO.echoToken = airSeatMapDOData.echoToken;
//			airSeatMapDO.primaryLangID = airSeatMapDOData.primaryLangID;
//			airSeatMapDO.sequenceNmbr = airSeatMapDOData.sequenceNmbr;
//			airSeatMapDO.transactionIdentifier = airSeatMapDOData.transactionIdentifier;
//			airSeatMapDO.version = airSeatMapDOData.version;
//
//			if (airSeatMapDOData.vecSeatMapDOs.size() > 0)
//				for (int i = 0; i < vecFlights.size(); i++) {
//					for (int j = 0; j < airSeatMapDOData.vecSeatMapDOs.size(); j++) {
//						if (vecFlights.get(i).flightNumber
//								.equalsIgnoreCase(airSeatMapDOData.vecSeatMapDOs
//										.get(j).flightSegmentDO.flightNumber)) {
//							airSeatMapDO.vecSeatMapDOs
//							.add(airSeatMapDOData.vecSeatMapDOs.get(j));
//						}
//					}
//				}
//
//			AppConstants.bookingFlightDO.vecSeatRequestDOs.clear();
//
//			if (airSeatMapDO.vecSeatMapDOs.size() > 0)
//				for (int i = 0; i < airSeatMapDO.vecSeatMapDOs.size(); i++) {
//					final SeatMapDO seatMapDO = airSeatMapDO.vecSeatMapDOs.get(i);
//
//					LinearLayout llSeatChild = (LinearLayout) layoutInflater
//							.inflate(R.layout.personalise_meal_item, null);
//					TextView tvPersonalTripItemMealSource = (TextView) llSeatChild
//							.findViewById(R.id.tvPersonalTripItemMealSource);
//					TextView tvPersonalTripItemMealDest = (TextView) llSeatChild
//							.findViewById(R.id.tvPersonalTripItemMealDest);
//					LinearLayout llPersonalTripItemMealSubMain = (LinearLayout) llSeatChild
//							.findViewById(R.id.llPersonalTripItemMealSubMain);
//					TextView tvServiceName = (TextView) llSeatChild
//							.findViewById(R.id.tvServiceName);
//					TextView tvServiceCurencyCode = (TextView) llSeatChild
//							.findViewById(R.id.tvServiceCurencyCode);
//
//					tvServiceCurencyCode.setText(AppConstants.CurrencyCode);
//					tvServiceName.setText(getString(R.string.SeatNo));
//
//					int infCount = 0;
//					LinearLayout llPersonalTripItemData = (LinearLayout) llSeatChild
//							.findViewById(R.id.llPersonalTripItemData);
//					LinearLayout llPersonalTripItemNoData = (LinearLayout) llSeatChild
//							.findViewById(R.id.llPersonalTripItemNoData);
//					LinearLayout llPersonalTripItemDataHeader = (LinearLayout) llSeatChild
//							.findViewById(R.id.llPersonalTripItemDataHeader);
//
//					if (seatMapDO != null && seatMapDO.vecAirRowDOs != null
//							&& seatMapDO.vecAirRowDOs.size() > 0) {
//						Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDO = new Vector<PassengerInfoPersonDO>();
//						for (PassengerInfoPersonDO passengerInfoPersonDO : AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO) {
//							if (!passengerInfoPersonDO.persontype
//									.equalsIgnoreCase(AppConstants.PERSON_TYPE_INFANT))
//								vecPassengerInfoPersonDO
//								.add(passengerInfoPersonDO);
//						}
//
//						infCount = AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO
//								.size() - vecPassengerInfoPersonDO.size();
//
//						for (int j = 0; j < vecPassengerInfoPersonDO.size(); j++) {
//							LinearLayout llItemSeatSub = (LinearLayout) layoutInflater
//									.inflate(R.layout.personalise_meal_item_sub,null);
//
//							TextView tvPersonalTripItemName = (TextView) llItemSeatSub
//									.findViewById(R.id.tvPersonalTripItemName);
//							final TextView tvPersonalTripItemAmount = (TextView) llItemSeatSub
//									.findViewById(R.id.tvPersonalTripItemAmount);
//							final TextView tvPersonalTripItemAEDValue = (TextView) llItemSeatSub
//									.findViewById(R.id.tvPersonalTripItemAEDValue);
//							final LinearLayout llPersonalTripItemCross = (LinearLayout) llItemSeatSub
//									.findViewById(R.id.llPersonalTripItemCross);
//
//							llPersonalTripItemMealSubMain.addView(llItemSeatSub);
//							tvPersonalTripItemName
//							.setText(AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO
//									.get(j).personfirstname);
//							tvPersonalTripItemAEDValue.setText("");
//							llPersonalTripItemCross.setTag(j + "");
//
//							final PassengerInfoPersonDO passengerInfoPersonDO = vecPassengerInfoPersonDO
//									.get(j);
//							final RequestDO requestDO = new RequestDO();
//							requestDO.departureDate = seatMapDO.flightSegmentDO.departureDateTime;
//							requestDO.travelerRefNumberRPHList = passengerInfoPersonDO.travelerRefNumberRPHList;
//							requestDO.flightNumber = seatMapDO.flightSegmentDO.flightNumber;
//							requestDO.flightRefNumberRPHList = seatMapDO.flightSegmentDO.RPH;
//
//							tvPersonalTripItemAEDValue.setText(StringUtils.getStrFloatFromString(""));
//
//							if (infCount > 0) {
//								if (passengerInfoPersonDO.persontype
//										.equalsIgnoreCase(AppConstants.PERSON_TYPE_ADULT)) {
//									requestDO.isINF = true;
//									infCount -= 1;
//								}
//							}
//
//							AppConstants.bookingFlightDO.vecSeatRequestDOs
//							.add(requestDO);
//							tvPersonalTripItemAmount
//							.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									clickSelectSeat(seatMapDO,
//											tvPersonalTripItemAmount,
//											requestDO,
//											tvPersonalTripItemAEDValue,
//											llPersonalTripItemCross);
//								}
//							});
//							llPersonalTripItemCross
//							.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
////									AppConstants.bookingFlightDO.vecSeatRequestDOs
////									.get(StringUtils.getInt(v.getTag().toString())).seatNumber = "";
//									tvPersonalTripItemAmount.setText(getString(R.string.Select));
//									tvPersonalTripItemAEDValue.setText("0.00");
//									llPersonalTripItemCross.setVisibility(View.INVISIBLE);
//
//									for (int k = 0; k < AppConstants.bookingFlightDO.vecSeatRequestDOs
//											.size(); k++) {
//										if (AppConstants.bookingFlightDO.vecSeatRequestDOs
//												.get(k).flightNumber
//												.equalsIgnoreCase(requestDO.flightNumber)
//												&& AppConstants.bookingFlightDO.vecSeatRequestDOs
//												.get(k).travelerRefNumberRPHList
//												.equalsIgnoreCase(requestDO.travelerRefNumberRPHList)) {
//											AppConstants.bookingFlightDO.vecSeatRequestDOs.get(k).seatNumber = "";
//											break;
//										}
//									}
//								}
//							});
//						}
//					} else {
//						llPersonalTripItemData.setVisibility(View.VISIBLE);
//						llPersonalTripItemNoData.setVisibility(View.GONE);
//						llPersonalTripItemDataHeader.setVisibility(View.GONE);
//						addNoDataAvailable(getString(R.string.NoSeatMap),
//								llPersonalTripItemMealSubMain, true);
//					}
//					llSeatMain.addView(llSeatChild);
//
//					if (AppConstants.allAirportNamesDO != null
//							&& AppConstants.allAirportNamesDO.vecAirport != null
//							&& AppConstants.allAirportNamesDO.vecAirport.size() > 0) {
//						String sourceName = seatMapDO.flightSegmentDO.departureAirportCode, originName = seatMapDO.flightSegmentDO.arrivalAirportCode;
//
//						updateCountryNameFromCode(sourceName,
//								AppConstants.allAirportNamesDO.vecAirport,
//								tvPersonalTripItemMealSource);
//						updateCountryNameFromCode(originName,
//								AppConstants.allAirportNamesDO.vecAirport,
//								tvPersonalTripItemMealDest);
//					} else {
//						tvPersonalTripItemMealSource
//						.setText(seatMapDO.flightSegmentDO.departureAirportCode);
//						tvPersonalTripItemMealDest
//						.setText(seatMapDO.flightSegmentDO.arrivalAirportCode);
//					}
//				}
//		} else
//			addNoDataAvailable(getString(R.string.NoSeatMap), llSeatMain, false);
//	}
//
//	private void addMealItems(AirMealDetailsDO airMealDetailsDOData) {
//		ivCheckmeal.setBackgroundResource(R.drawable.check);
//		ivUncheckmeal.setBackgroundResource(R.drawable.uncheck);
//		tvMealText.setVisibility(View.VISIBLE);
//		llMealMain.removeAllViews();
//		llMealMain.setVisibility(View.VISIBLE);
//
//		if (airMealDetailsDOData != null
//				&& airMealDetailsDOData.vecMealDetailsDOs != null) {
//			AirMealDetailsDO airMealDetailsDO = new AirMealDetailsDO();
//			airMealDetailsDO.vecMealDetailsDOs = new Vector<MealDetailsDO>();
//			airMealDetailsDO.vecMealDetailsDOs.clear();
//
//			airMealDetailsDO.echoToken = airMealDetailsDOData.echoToken;
//			airMealDetailsDO.primaryLangID = airMealDetailsDOData.primaryLangID;
//			airMealDetailsDO.sequenceNmbr = airMealDetailsDOData.sequenceNmbr;
//			airMealDetailsDO.transactionIdentifier = airMealDetailsDOData.transactionIdentifier;
//			airMealDetailsDO.version = airMealDetailsDOData.version;
//
//			for (int i = 0; i < vecFlights.size(); i++) {
//				for (int j = 0; j < airMealDetailsDOData.vecMealDetailsDOs
//						.size(); j++) {
//					if (vecFlights.get(i).flightNumber.equalsIgnoreCase(
//							airMealDetailsDOData.vecMealDetailsDOs.get(j).flightSegmentDO.flightNumber))
//					{
//						airMealDetailsDO.vecMealDetailsDOs.add(airMealDetailsDOData.vecMealDetailsDOs.get(j));
//					}
//				}
//			}
//
//			if (airMealDetailsDO.vecMealDetailsDOs.size() > 0)
//				for (int i = 0; i < airMealDetailsDO.vecMealDetailsDOs.size(); i++) {
//
//					final MealDetailsDO mealDetailsDO = airMealDetailsDO.vecMealDetailsDOs.get(i);
//
//					if (mealDetailsDO != null
//							&& mealDetailsDO.vecMealCategoriesDO != null
//							&& mealDetailsDO.vecMealCategoriesDO.size() > 0) {
//
//						LinearLayout llMealChild = (LinearLayout) layoutInflater
//								.inflate(R.layout.personalise_meal_item, null);
//						TextView tvPersonalTripItemMealSource = (TextView) llMealChild
//								.findViewById(R.id.tvPersonalTripItemMealSource);
//						TextView tvPersonalTripItemMealDest = (TextView) llMealChild
//								.findViewById(R.id.tvPersonalTripItemMealDest);
//						LinearLayout llPersonalTripItemMealSubMain = (LinearLayout) llMealChild
//								.findViewById(R.id.llPersonalTripItemMealSubMain);
//						TextView tvServiceName = (TextView) llMealChild
//								.findViewById(R.id.tvServiceName);
//						TextView tvServiceCurencyCode = (TextView) llMealChild
//								.findViewById(R.id.tvServiceCurencyCode);
//
//						tvServiceCurencyCode.setText(AppConstants.CurrencyCode);
//						tvServiceName.setText(getString(R.string.Menu));
//
//						mealDetailsDO.vecMealRequestDOs = new Vector<Vector<RequestDO>>();
//						Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDO = new Vector<PassengerInfoPersonDO>();
//						for (PassengerInfoPersonDO passengerInfoPersonDO : AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO) {
//							if (!passengerInfoPersonDO.persontype.equalsIgnoreCase(AppConstants.PERSON_TYPE_INFANT))
//								vecPassengerInfoPersonDO.add(passengerInfoPersonDO);
//						}
//
//						for (int j = 0; j < vecPassengerInfoPersonDO.size(); j++) {
//							PassengerInfoPersonDO passengerInfoPersonDO = vecPassengerInfoPersonDO
//									.get(j);
//
//							Vector<RequestDO> vecRequestDOs = new Vector<RequestDO>();
//							mealDetailsDO.vecMealRequestDOs.add(vecRequestDOs);
//
//							LinearLayout llItemMealSub = (LinearLayout) layoutInflater
//									.inflate(R.layout.personalise_meal_item_sub_meal,null);
//							TextView tvPersonalTripItemName = (TextView) llItemMealSub
//									.findViewById(R.id.tvPersonalTripItemName);
//							final LinearLayout llPersonalTripItemAmount = (LinearLayout) llItemMealSub
//									.findViewById(R.id.llPersonalTripItemAmount);
//							final LinearLayout llPersonalTripItemPrice = (LinearLayout) llItemMealSub
//									.findViewById(R.id.llPersonalTripItemPrice);
//
//							llPersonalTripItemMealSubMain.addView(llItemMealSub);
//
//							LinearLayout llPersonalTripItemAmountItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text, null);
//
//							LinearLayout llPersonalTripItemPriceItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text_price, null);
//							TextView tvPersonalTripItemAEDValue = (TextView)llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAEDValue);
//							LinearLayout llPersonalTripItemCross = (LinearLayout)llPersonalTripItemPriceItem.findViewById(R.id.llPersonalTripItemCross);
//							TextView tvPersonalTripItemAmount = (TextView)llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAmount);
//
//							tvPersonalTripItemAmount.setText(getString(R.string.Select));
//							tvPersonalTripItemAEDValue.setText("0.00");
//							llPersonalTripItemCross.setVisibility(View.INVISIBLE);
//
//							llPersonalTripItemAmount.addView(llPersonalTripItemAmountItem);
//							llPersonalTripItemPrice.addView(llPersonalTripItemPriceItem);
//
//							tvPersonalTripItemName.setText(passengerInfoPersonDO.personfirstname);
//							llPersonalTripItemAmount.setTag(j + "");
//							llPersonalTripItemPrice.setTag(j + "");
//							final String travelerRefNumberRPHList = passengerInfoPersonDO.travelerRefNumberRPHList;
//
//							llPersonalTripItemPrice.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									showSelMealMenuPopup(
//											travelerRefNumberRPHList,
//											mealDetailsDO,
//											llPersonalTripItemAmount,
//											llPersonalTripItemPrice,
//											mealDetailsDO.vecMealRequestDOs.get(StringUtils.getInt(v.getTag().toString())));
//								}
//							});
//						}
//						llMealMain.addView(llMealChild);
//
//						if (AppConstants.allAirportNamesDO != null
//								&& AppConstants.allAirportNamesDO.vecAirport != null
//								&& AppConstants.allAirportNamesDO.vecAirport.size() > 0) {
//
//							String sourceName = mealDetailsDO.flightSegmentDO.departureAirportCode, originName = mealDetailsDO.flightSegmentDO.arrivalAirportCode;
//
//							updateCountryNameFromCode(sourceName,
//									AppConstants.allAirportNamesDO.vecAirport,
//									tvPersonalTripItemMealSource);
//							updateCountryNameFromCode(originName,
//									AppConstants.allAirportNamesDO.vecAirport,
//									tvPersonalTripItemMealDest);
//						} else {
//							tvPersonalTripItemMealSource
//							.setText(mealDetailsDO.flightSegmentDO.departureAirportCode);
//							tvPersonalTripItemMealDest
//							.setText(mealDetailsDO.flightSegmentDO.arrivalAirportCode);
//						}
//					}
//				}
//		} else
//			addNoDataAvailable(getString(R.string.NoMeals), llMealMain, false);
//	}
//
//	private void addtravelinsuranceItems(final InsuranceQuoteDO insuranceQuoteDO) {
//		ivChecktravelinsurance.setBackgroundResource(R.drawable.check);
//		ivUnchecktravelinsurance.setBackgroundResource(R.drawable.uncheck);
//
//		llInsuranceMain.removeAllViews();
//		AppConstants.bookingFlightDO.vecInsrRequestDOs.clear();
//		if (insuranceQuoteDO != null
//				&& !insuranceQuoteDO.amount.equalsIgnoreCase("")) {
//			LinearLayout llInsuranceChild = (LinearLayout) layoutInflater
//					.inflate(R.layout.insurance_item, null);
//
//			final LinearLayout llInsuranceCheckPurchase = (LinearLayout) llInsuranceChild
//					.findViewById(R.id.llInsuranceCheckPurchase);
//			final ImageView ivInsuranceCheckPurchase = (ImageView) llInsuranceChild
//					.findViewById(R.id.ivInsuranceCheckPurchase);
//
//			TextView tvInsurance2 = (TextView) llInsuranceChild
//					.findViewById(R.id.tvInsurance2);
//				tvInsurance2.setText(Html.fromHtml(getString(R.string.InsuranceP1_sub3_1)+" "+" <b>"+ insuranceQuoteDO.currencyCode+" " + insuranceQuoteDO.amount+"</b> "+getString(R.string.InsuranceP1_sub3_2)));
//
//			llInsuranceCheckPurchase.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					if (!isInsuranceChecked) {
//						isInsuranceChecked = true;
//						final RequestDO requestDO = new RequestDO();
//
//						int sizeReturnFlt = 0;
//
//						requestDO.departureDate = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//								.get(0).vecFlightSegmentDOs.get(0).departureDateTime;
//						if (AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//								.size() > 0) {
//							sizeReturnFlt = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//									.size();
//							requestDO.arrivalDate = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//									.get(sizeReturnFlt - 1).vecFlightSegmentDOs
//									.get(0).arrivalDateTime;
//						} else {
//							sizeReturnFlt = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//									.size();
//							requestDO.arrivalDate = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//									.get(sizeReturnFlt - 1).vecFlightSegmentDOs
//									.get(0).arrivalDateTime;
//						}
//
//						requestDO.rPH = insuranceQuoteDO.rPH;
//						requestDO.policyCode = insuranceQuoteDO.planID;
//						AppConstants.bookingFlightDO.vecInsrRequestDOs
//						.add(requestDO);
//						ivInsuranceCheckPurchase
//						.setBackgroundResource(R.drawable.check);
//					} else {
//						showInsuranceUnSelPopup(ivInsuranceCheckPurchase);
//					}
//				}
//			});
//
//			ivInsuranceCheckPurchase.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					llInsuranceCheckPurchase.performClick();
//				}
//			});
//
//			llInsuranceMain.addView(llInsuranceChild);
//		} else
//			addNoDataAvailable(getString(R.string.NoInsurance), llInsuranceMain, false);
//	}
//
//	private void addHalaItems() {
//		ivCheckhalaservices.setBackgroundResource(R.drawable.check);
//		ivUncheckhalaservices.setBackgroundResource(R.drawable.uncheck);
//		tvHalaText.setVisibility(View.VISIBLE);
//		addHalaItems(vecHalaDos);
//	}
//
//	private void removeSeatItems() {
//		ivCheckseatbox.setBackgroundResource(R.drawable.uncheck);
//		ivUnCheckseatbox.setBackgroundResource(R.drawable.check);
//		llSeatMain.removeAllViews();
//		llSeatMain.setVisibility(View.GONE);
//		AppConstants.bookingFlightDO.vecSeatRequestDOs.clear();
//	}
//
//	private void removeMealItems() {
//		ivCheckmeal.setBackgroundResource(R.drawable.uncheck);
//		ivUncheckmeal.setBackgroundResource(R.drawable.check);
//		tvMealText.setVisibility(View.GONE);
//		llMealMain.removeAllViews();
//		llMealMain.setVisibility(View.GONE);
//		AppConstants.bookingFlightDO.vecMealReqDOs.clear();
//	}
//
//	private void removetravelinsuranceItems() {
//		ivChecktravelinsurance.setBackgroundResource(R.drawable.uncheck);
//		ivUnchecktravelinsurance.setBackgroundResource(R.drawable.check);
//		llInsuranceMain.removeAllViews();
//		AppConstants.bookingFlightDO.vecInsrRequestDOs.clear();
//		isInsuranceChecked = false;
//	}
//
//	private void removeHalaItems() {
//		ivCheckhalaservices.setBackgroundResource(R.drawable.uncheck);
//		ivUncheckhalaservices.setBackgroundResource(R.drawable.check);
//		tvHalaText.setVisibility(View.GONE);
//		llHalaMain.removeAllViews();
//		AppConstants.bookingFlightDO.vecSSRRequests.clear();
//	}
//
//	private void showSelHalaPopup(
//			final PassengerInfoPersonDO passengerInfoPersonDO,
//			final HalaDO halaDO, final Vector<AirportServiceDO> vecList,
//			final TextView tvTitleSel, final TextView tvTitleSelCharge,
//			final LinearLayout llPersonalTripItemCross) {
//		LinearLayout ll = (LinearLayout) layoutInflater.inflate(
//				R.layout.popup_titles, null);
//		final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_No_Title);
//		dialog.setContentView(ll);
//
//		LinearLayout llPopupTitleMain = (LinearLayout) ll
//				.findViewById(R.id.llPopupTitleMain);
//		Button btnPopupConfirm = (Button) ll
//				.findViewById(R.id.btnPopupTitleCheckConfirm);
//		btnPopupConfirm.setVisibility(View.VISIBLE);
//		btnPopupConfirm.setTag(passengerInfoPersonDO.travelerRefNumberRPHList);
//		btnPopupConfirm.setTag(R.string.hala_flight_tag,
//				halaDO.flightSegmentDO.flightNumber);
//
//		for (int i = 0; i < vecList.size(); i++) {
//			LinearLayout llTitle = (LinearLayout) layoutInflater.inflate(
//					R.layout.popup_title_item_withcheck, null);
//
//			final TextView tvTitleItem = (TextView) llTitle
//					.findViewById(R.id.tvTitleItemNew);
//			final ImageView ivTitleItemNewCheck = (ImageView) llTitle
//					.findViewById(R.id.ivTitleItemNewCheck);
//
//			llPopupTitleMain.addView(llTitle);
//			tvTitleItem.setText(vecList.get(i).ssrName);
//			tvTitleItem.setTag(i + "");
//			tvTitleItem.setTag(R.string.hala_flight_tag,
//					halaDO.flightSegmentDO.flightNumber);
//			tvTitleItem.setTag(R.string.hala_name_tag, vecList.get(i).ssrCode);
//			tvTitleItem.setTag(R.string.hala_person_tag,
//					passengerInfoPersonDO.travelerRefNumberRPHList);
//
//			ivTitleItemNewCheck.setTag(i + "");
//			ivTitleItemNewCheck.setTag(R.string.hala_flight_tag,
//					halaDO.flightSegmentDO.flightNumber);
//			ivTitleItemNewCheck.setTag(R.string.hala_name_tag,
//					vecList.get(i).ssrCode);
//			ivTitleItemNewCheck.setTag(R.string.hala_person_tag,
//					passengerInfoPersonDO.travelerRefNumberRPHList);
//			ivTitleItemNewCheck.setTag(R.string.hala_check_tag, false);
//
//			boolean isFound = false;
//			for (int j = 0; j < AppConstants.bookingFlightDO.vecSSRRequests
//					.size(); j++) {
//				if (AppConstants.bookingFlightDO.vecSSRRequests.get(j).flightNumber
//						.equalsIgnoreCase(ivTitleItemNewCheck.getTag(
//								R.string.hala_flight_tag).toString())
//								&& AppConstants.bookingFlightDO.vecSSRRequests.get(j).ssrCode
//								.equalsIgnoreCase(ivTitleItemNewCheck.getTag(
//										R.string.hala_name_tag).toString())
//										&& AppConstants.bookingFlightDO.vecSSRRequests.get(j).travelerRefNumberRPHList
//										.equalsIgnoreCase(ivTitleItemNewCheck.getTag(
//												R.string.hala_person_tag).toString())) {
//					isFound = true;
//					break;
//				}
//			}
//			if (isFound) {
//				ivTitleItemNewCheck
//				.setImageResource(com.winit.airarabia.R.drawable.check);
//				ivTitleItemNewCheck.setTag(R.string.hala_check_tag, true);
//			} else {
//				ivTitleItemNewCheck
//				.setImageResource(com.winit.airarabia.R.drawable.uncheck);
//				ivTitleItemNewCheck.setTag(R.string.hala_check_tag, false);
//			}
//
//			tvTitleItem.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					ivTitleItemNewCheck.performClick();
//				}
//			});
//
//			ivTitleItemNewCheck.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					if ((Boolean) v.getTag(R.string.hala_check_tag)) {
//						for (int j = 0; j < AppConstants.bookingFlightDO.vecSSRRequests
//								.size(); j++) {
//							if (AppConstants.bookingFlightDO.vecSSRRequests
//									.get(j).flightNumber.equalsIgnoreCase(v
//											.getTag(R.string.hala_flight_tag)
//											.toString())
//											&& AppConstants.bookingFlightDO.vecSSRRequests
//											.get(j).ssrCode.equalsIgnoreCase(v
//													.getTag(R.string.hala_name_tag)
//													.toString())
//													&& AppConstants.bookingFlightDO.vecSSRRequests
//													.get(j).travelerRefNumberRPHList
//													.equalsIgnoreCase(v.getTag(
//															R.string.hala_person_tag)
//															.toString())) {
//								((ImageView) v)
//								.setImageResource(R.drawable.uncheck);
//								v.setTag(R.string.hala_check_tag, false);
//								AppConstants.bookingFlightDO.vecSSRRequests
//								.remove(j);
//								break;
//							}
//						}
//					} else {
//						final RequestDO requestDO = new RequestDO();
//						requestDO.flightNumber = halaDO.flightSegmentDO.flightNumber;
//						requestDO.departureDate = halaDO.flightSegmentDO.departureDateTime;
//						requestDO.travelerRefNumberRPHList = passengerInfoPersonDO.travelerRefNumberRPHList;
//						requestDO.flightNumber = halaDO.flightSegmentDO.flightNumber;
//						requestDO.flightRefNumberRPHList = halaDO.flightSegmentDO.RPH;
//						requestDO.airportType = halaDO.airportType;
//						requestDO.airportCode = halaDO.airportCode;
//						requestDO.ssrCode = vecList.get(StringUtils.getInt(v
//								.getTag().toString())).ssrCode;
//						requestDO.ssrName = vecList.get(StringUtils.getInt(v
//								.getTag().toString())).ssrName;
//						AppConstants.bookingFlightDO.vecSSRRequests
//						.add(requestDO);
//
//						((ImageView) v).setImageResource(R.drawable.check);
//						v.setTag(R.string.hala_check_tag, true);
//					}
//				}
//			});
//		}
//		dialog.show();
//		btnPopupConfirm.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				String strHalaNames = "";
//				float mTotalHalaPrice = 0;
//				for (int j = 0; j < AppConstants.bookingFlightDO.vecSSRRequests
//						.size(); j++) {
//					if (AppConstants.bookingFlightDO.vecSSRRequests.get(j).travelerRefNumberRPHList
//							.equalsIgnoreCase(v.getTag().toString())
//							&& AppConstants.bookingFlightDO.vecSSRRequests
//							.get(j).flightNumber.equalsIgnoreCase(v
//									.getTag(R.string.hala_flight_tag)
//									.toString())) {
//						if (j == 0)
//							strHalaNames = AppConstants.bookingFlightDO.vecSSRRequests
//							.get(j).ssrName;
//						else
//							strHalaNames = strHalaNames
//							+ "\n"
//							+ AppConstants.bookingFlightDO.vecSSRRequests
//							.get(j).ssrName;
//
//						for (AirportServiceDO airportServiceDO : vecList) {
//							if (airportServiceDO.ssrCode
//									.equalsIgnoreCase(AppConstants.bookingFlightDO.vecSSRRequests
//											.get(j).ssrCode)) {
//								mTotalHalaPrice = mTotalHalaPrice
//										+ StringUtils
//										.getFloat(airportServiceDO.adultAmount);
//							}
//						}
//					}
//				}
//
//				if (strHalaNames.equalsIgnoreCase("")) {
//					llPersonalTripItemCross.setVisibility(View.INVISIBLE);
//					tvTitleSel.setText(getString(R.string.Select));
//				} else {
//					llPersonalTripItemCross.setVisibility(View.VISIBLE);
//					tvTitleSel.setText(strHalaNames);
//				}
//				tvTitleSelCharge.setText(StringUtils
//						.getStringFromFloat(mTotalHalaPrice));
//				dialog.dismiss();
//			}
//		});
//	}
//
//	private void showSelMealMenuPopup(final String travelerRefNumberRPHList,
//			final MealDetailsDO mealDetailsDO,
//			final LinearLayout llPersonalTripItemAmount,
//			final LinearLayout llPersonalTripItemPrice, final Vector<RequestDO> vectorMealReq) {
//
//		LinearLayout ll = (LinearLayout) layoutInflater.inflate(
//				R.layout.popup_meal_mainmenu, null);
//		dialogMealMain = new Dialog(this, R.style.Theme_Dialog_No_Title);
//		dialogMealMain.setContentView(ll);
//
//		ImageView ivCancel = (ImageView) ll.findViewById(R.id.ivCancel);
//		GridView grid_mealmenu = (GridView) ll.findViewById(R.id.grid_mealmenu);
//		Button btn_backinmealmenu = (Button) ll
//				.findViewById(R.id.btn_backinmealmenu);
//
//		BookingMealMainAdapter bookingMealMainAdapter = new BookingMealMainAdapter(
//				getApplicationContext(), mealDetailsDO.vecMealcategoryNames,
//				mealDetailsDO.vecMealcategoryImageUrls);
//		grid_mealmenu.setAdapter(bookingMealMainAdapter);
//
//		grid_mealmenu.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				showSelMealPopup(travelerRefNumberRPHList, mealDetailsDO.flightSegmentDO,
//						mealDetailsDO.vecMealCategoriesDO.get(arg2).vecMealsDO,
//						llPersonalTripItemAmount,
//						llPersonalTripItemPrice,
//						mealDetailsDO.vecMealcategoryNames.get(arg2),
//						vectorMealReq);
//			}
//		});
//		btn_backinmealmenu.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				dialogMealMain.dismiss();
//			}
//		});
//		ivCancel.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				dialogMealMain.dismiss();
//			}
//		});
//		dialogMealMain.show();
//	}
//
//	private void showSelMealPopup(final String travelerRefNumberRPHList,
//			FlightSegmentDO flightSegmentDO,
//			final Vector<MealDO> vecList,
//			final LinearLayout llPersonalTripItemAmount,
//			final LinearLayout llPersonalTripItemPrice,
//			String categoryName, final Vector<RequestDO> vectorMealReq) {
//		LinearLayout ll = (LinearLayout) layoutInflater.inflate(
//				R.layout.popup_meal_selectitem, null);
//		dialogMealSub = new Dialog(this, R.style.Theme_Dialog_No_Title);
//		dialogMealSub.setContentView(ll);
//
//		TextView tvMealsHeader = (TextView) ll.findViewById(R.id.tvMealsHeader);
//		ImageView ivCancelMealList = (ImageView) ll
//				.findViewById(R.id.ivCancelMealList);
//		ListView list_mealselecteditems = (ListView) ll
//				.findViewById(R.id.list_mealselecteditems);
//		Button btnConfirminslecteditem = (Button) ll
//				.findViewById(R.id.btnConfirminslecteditem);
//		Button btnBackinslecteditem = (Button) ll
//				.findViewById(R.id.btnBackinslecteditem);
//
//		tvMealsHeader.setText(categoryName);
//
//		final Vector<RequestDO> vecRequestDOsNew = new Vector<RequestDO>();
//		vecRequestDOsNew.addAll(vectorMealReq);
//
//		for (int j = 0; j < vecList.size(); j++) {
//			vecList.get(j).mealCount = 0;
//			for (int i = 0; i < llPersonalTripItemAmount.getChildCount(); i++) {
//				if(llPersonalTripItemAmount.getChildAt(i).getTag() != null)
//				{
//					RequestDO requestDO = (RequestDO)llPersonalTripItemAmount.getChildAt(i).getTag();
//					if (travelerRefNumberRPHList.equalsIgnoreCase(requestDO.travelerRefNumberRPHList)
//							&& flightSegmentDO.flightNumber.equalsIgnoreCase(requestDO.flightNumber)
//							&& vecList.get(j).mealCode.equalsIgnoreCase(requestDO.mealCode)) {
//						vecList.get(j).mealCount = StringUtils.getInt(requestDO.mealQuantity);
//					}
//				}
//			}
//		}
//
//		BookingMealAdapter bookingMealAdapter = new BookingMealAdapter(
//				getApplicationContext(), travelerRefNumberRPHList,
//				flightSegmentDO, vecRequestDOsNew, vecList);
//		list_mealselecteditems.setAdapter(bookingMealAdapter);
//		list_mealselecteditems.setCacheColorHint(0);
//
//		btnConfirminslecteditem.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				llPersonalTripItemAmount.removeAllViews();
//				llPersonalTripItemPrice.removeAllViews();
//				vectorMealReq.clear();
//				if(vecRequestDOsNew == null || (vecRequestDOsNew != null && vecRequestDOsNew.size() == 0))
//				{
//					LinearLayout llPersonalTripItemAmountItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text, null);
//
//					LinearLayout llPersonalTripItemPriceItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text_price, null);
//					TextView tvPersonalTripItemAEDValue = (TextView)llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAEDValue);
//					LinearLayout llPersonalTripItemCross = (LinearLayout)llPersonalTripItemPriceItem.findViewById(R.id.llPersonalTripItemCross);
//					TextView tvPersonalTripItemAmount = (TextView)llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAmount);
//
//					tvPersonalTripItemAmount.setText(getString(R.string.Select));
//					tvPersonalTripItemAEDValue.setText("0.00");
//					llPersonalTripItemCross.setVisibility(View.INVISIBLE);
//
//					llPersonalTripItemAmount.addView(llPersonalTripItemAmountItem);
//					llPersonalTripItemPrice.addView(llPersonalTripItemPriceItem);
//				}
//				else
//				{
//					for (RequestDO requestDO : vecRequestDOsNew) {
//						vectorMealReq.add(requestDO);
//						LinearLayout llPersonalTripItemAmountItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text, null);
//
//						LinearLayout llPersonalTripItemPriceItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text_price, null);
//						TextView tvPersonalTripItemAEDValue = (TextView)llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAEDValue);
//						LinearLayout llPersonalTripItemCross = (LinearLayout)llPersonalTripItemPriceItem.findViewById(R.id.llPersonalTripItemCross);
//						TextView tvPersonalTripItemAmount = (TextView)llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAmount);
//
//						tvPersonalTripItemAmount.setText(requestDO.mealQuantity + "x" + requestDO.mealName);
//						tvPersonalTripItemAEDValue.setText(StringUtils.getStringFromFloat(StringUtils.getFloat(requestDO.mealQuantity)* requestDO.mealCharge));
//
//						llPersonalTripItemAmount.addView(llPersonalTripItemAmountItem);
//						llPersonalTripItemPrice.addView(llPersonalTripItemPriceItem);
//
//						llPersonalTripItemCross.setTag(requestDO);
//						llPersonalTripItemAmountItem.setTag(requestDO);
//						llPersonalTripItemPriceItem.setTag(requestDO);
//
//						llPersonalTripItemCross.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								vecRequestDOsNew.remove((RequestDO)v.getTag());
//								vectorMealReq.remove((RequestDO)v.getTag());
//								for (int k = 0; k < llPersonalTripItemAmount.getChildCount(); k++) {
//									if (llPersonalTripItemAmount.getChildAt(k).getTag() != null && llPersonalTripItemAmount.getChildAt(k).getTag().equals((RequestDO)v.getTag()))
//									{
//										llPersonalTripItemAmount.removeViewAt(k);
//									}
//								}
//
//								for (int k = 0; k < llPersonalTripItemPrice.getChildCount(); k++) {
//									if (llPersonalTripItemPrice.getChildAt(k).getTag() != null && llPersonalTripItemPrice.getChildAt(k).getTag().equals((RequestDO)v.getTag()))
//									{
//										llPersonalTripItemPrice.removeViewAt(k);
//									}
//								}
//
//								if(llPersonalTripItemPrice.getChildCount() == 0)
//								{
//									LinearLayout llPersonalTripItemAmountItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text, null);
//
//									LinearLayout llPersonalTripItemPriceItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text_price, null);
//									TextView tvPersonalTripItemAEDValue = (TextView)llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAEDValue);
//									LinearLayout llPersonalTripItemCross = (LinearLayout)llPersonalTripItemPriceItem.findViewById(R.id.llPersonalTripItemCross);
//									TextView tvPersonalTripItemAmount = (TextView)llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAmount);
//
//									tvPersonalTripItemAmount.setText(getString(R.string.Select));
//									tvPersonalTripItemAEDValue.setText("0.00");
//									llPersonalTripItemCross.setVisibility(View.INVISIBLE);
//
//									llPersonalTripItemAmount.addView(llPersonalTripItemAmountItem);
//									llPersonalTripItemPrice.addView(llPersonalTripItemPriceItem);
//								}
//							}
//						});
//					}
//				}
//
//
//				dialogMealSub.dismiss();
//				dialogMealMain.dismiss();
//			}
//		});
//		btnBackinslecteditem.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				dialogMealSub.dismiss();
//			}
//		});
//		ivCancelMealList.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				dialogMealSub.dismiss();
//			}
//		});
//		dialogMealSub.show();
//	}
//
//	private void showGateSeatPopup() {
//		LinearLayout llInsuranceChild = (LinearLayout) layoutInflater.inflate(
//				R.layout.seat_gate_error, null);
//		final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_No_Title);
//		dialog.setContentView(llInsuranceChild,
//				new LinearLayout.LayoutParams(AppConstants.DEVICE_WIDTH - 20,
//						AppConstants.DEVICE_HEIGHT - 40));
//		dialog.setCancelable(true);
//		dialog.setCanceledOnTouchOutside(true);
//
//		Button btnOkSeatPop = (Button) llInsuranceChild
//				.findViewById(R.id.btnOkSeatPop);
//		Button btnCancelSeatPop = (Button) llInsuranceChild
//				.findViewById(R.id.btnCancelSeatPop);
//
//		btnOkSeatPop.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
//
//		btnCancelSeatPop.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
//
//		dialog.show();
//	}
//
//	private void showInsuranceUnSelPopup(
//			final ImageView ivInsuranceCheckPurchase) {
//		ScrollView svInsuranceChild = (ScrollView) layoutInflater.inflate(
//				R.layout.insurance_item_popup, null);
//		final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_No_Title);
//		dialog.setContentView(svInsuranceChild);
//		dialog.setCancelable(true);
//		dialog.setCanceledOnTouchOutside(true);
//
//		LinearLayout llInsuranceChild = (LinearLayout)svInsuranceChild
//				.findViewById(R.id.llInsuranceChild);
//
////		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AppConstants.DEVICE_WIDTH - 40,
////				AppConstants.DEVICE_HEIGHT - 80);
////		llInsuranceChild.setLayoutParams(params);
//		TextView tvInsuranceDesc = (TextView) llInsuranceChild
//				.findViewById(R.id.tvInsuranceDesc);
//		TextView tvInsurance2 = (TextView) llInsuranceChild
//				.findViewById(R.id.tvInsurance2);
//		TextView tvInsuranceSub1 = (TextView) llInsuranceChild
//				.findViewById(R.id.tvInsuranceSub1);
//		TextView tvInsuranceSub2 = (TextView) llInsuranceChild
//				.findViewById(R.id.tvInsuranceSub2);
//		TextView tvInsuranceSub3 = (TextView) llInsuranceChild
//				.findViewById(R.id.tvInsuranceSub3);
//		TextView tvInsuranceSub4 = (TextView) llInsuranceChild
//				.findViewById(R.id.tvInsuranceSub4);
//
//		tvInsurance2.setVisibility(View.GONE);
//		tvInsuranceSub4.setVisibility(View.GONE);
//
//		tvInsuranceDesc.setText(getString(R.string.InsPopupHead));
//		tvInsuranceSub1.setText(getString(R.string.InsPopupP1));
//		tvInsuranceSub2.setText(getString(R.string.InsPopupP2));
//		tvInsuranceSub3.setText(getString(R.string.InsPopupP3));
//
//		LinearLayout llinsurance_popup_bottom = (LinearLayout) layoutInflater
//				.inflate(R.layout.insurance_popup_bottom, null);
//		Button btnInsurancePopupYes = (Button) llinsurance_popup_bottom
//				.findViewById(R.id.btnInsurancePopupYes);
//		Button btnInsurancePopupNo = (Button) llinsurance_popup_bottom
//				.findViewById(R.id.btnInsurancePopupNo);
//
//		llInsuranceChild.addView(llinsurance_popup_bottom);
//
//		btnInsurancePopupYes.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				isInsuranceChecked = true;
//				AppConstants.bookingFlightDO.vecInsrRequestDOs.clear();
//				ivInsuranceCheckPurchase
//				.setBackgroundResource(R.drawable.check);
//
//				final RequestDO requestDO = new RequestDO();
//
//				int sizeReturnFlt = 0;
//
//				requestDO.departureDate = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//						.get(0).vecFlightSegmentDOs.get(0).departureDateTime;
//				if (AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//						.size() > 0) {
//					sizeReturnFlt = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//							.size();
//					requestDO.arrivalDate = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//							.get(sizeReturnFlt - 1).vecFlightSegmentDOs.get(0).arrivalDateTime;
//				} else {
//					sizeReturnFlt = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//							.size();
//					requestDO.arrivalDate = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//							.get(sizeReturnFlt - 1).vecFlightSegmentDOs.get(0).arrivalDateTime;
//				}
//
//				requestDO.rPH = insuranceQuoteDO.rPH;
//				requestDO.policyCode = insuranceQuoteDO.planID;
//				AppConstants.bookingFlightDO.vecInsrRequestDOs.add(requestDO);
//			}
//		});
//
//		btnInsurancePopupNo.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				isInsuranceChecked = false;
//				AppConstants.bookingFlightDO.vecInsrRequestDOs.clear();
//				ivInsuranceCheckPurchase
//				.setBackgroundResource(R.drawable.uncheck);
//			}
//		});
//
//		dialog.show();
//	}
//
//	@SuppressLint("NewApi")
//	private void clickSelectSeat(final SeatMapDO seatMapDO,
//			final TextView tvPersonalTripItemAmount,
//			final RequestDO requestDOSel,
//			final TextView tvPersonalTripItemAEDValue,
//			final LinearLayout llPersonalTripItemCross) {
//		LinearLayout layout = (LinearLayout) layoutInflater.inflate(
//				R.layout.popup_seat_selection, null);
//		final CustomDialog dialog = new CustomDialog(
//				PersonalizeyourTripActivity.this, layout,
//				AppConstants.DEVICE_WIDTH - 40, LayoutParams.WRAP_CONTENT,
//				false);
//		dialog.setCancelable(false);
//		dialog.show();
//		LinearLayout llseatlayout = (LinearLayout) layout
//				.findViewById(R.id.llseatlayout);
//
//		ImageView imgvseat_left3 = (ImageView) layout
//				.findViewById(R.id.imgvseat_left3);
//		Button btnCofirmseat = (Button) layout.findViewById(R.id.btnCofirmseat);
//
//		imgvseat_left3.setAlpha(0.5f);
//
//		btnCofirmseat.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
//		final Vector<ImageView> vecSeatsSelected = new Vector<ImageView>();
//
//		for (int i1 = 0; i1 < seatMapDO.vecAirRowDOs.size(); i1++) {
//			seatlayoutview = LayoutInflater.from(
//					PersonalizeyourTripActivity.this).inflate(
//							R.layout.bookingseat_liststyle, null);
//			LinearLayout llSeatSec1 = (LinearLayout) seatlayoutview.findViewById(R.id.llSeatSec1);
//			LinearLayout llSeatSec2 = (LinearLayout) seatlayoutview.findViewById(R.id.llSeatSec2);
//			TextView tvSeatRow = (TextView) seatlayoutview.findViewById(R.id.tvSeatRow);
//			final AirRowDO airRowDO = seatMapDO.vecAirRowDOs.get(i1);
//			tvSeatRow.setText(airRowDO.rowNumber);
//			for (int i2 = 0; i2 < airRowDO.vecAirSeatDOs.size(); i2++) {
//
//				final AirSeatDO airSeatDO = airRowDO.vecAirSeatDOs.get(i2);
//				ImageView ivFlightSeat = (ImageView) layoutInflater.inflate(R.layout.flight_seats_item, null);
//
//				if (airSeatDO.seatAvailability.equalsIgnoreCase(AppConstants.SEAT_VAC)) {
//					ivFlightSeat.setImageResource(R.drawable.btseatnormal);
//					ivFlightSeat.setTag(R.string.seatRowNo, airRowDO.rowNumber);
//					ivFlightSeat.setTag(R.string.seatNo, airSeatDO.seatNumber);
//					ivFlightSeat.setTag(R.string.seatSelected, false);
//				} else {
//					ivFlightSeat.setImageResource(R.drawable.btseatnormal);
//					ivFlightSeat.setAlpha(0.5f);
//					ivFlightSeat.setEnabled(false);
//				}
//				if (airSeatDO.seatNumber.equalsIgnoreCase("A")) {
//					llSeatSec1.addView(ivFlightSeat);
//				} else if (airSeatDO.seatNumber.equalsIgnoreCase("B")) {
//					llSeatSec1.addView(ivFlightSeat);
//				} else if (airSeatDO.seatNumber.equalsIgnoreCase("C")) {
//					llSeatSec1.addView(ivFlightSeat);
//				} else if (airSeatDO.seatNumber.equalsIgnoreCase("D")) {
//					llSeatSec2.addView(ivFlightSeat);
//				} else if (airSeatDO.seatNumber.equalsIgnoreCase("E")) {
//					llSeatSec2.addView(ivFlightSeat);
//				} else if (airSeatDO.seatNumber.equalsIgnoreCase("F")) {
//					llSeatSec2.addView(ivFlightSeat);
//				}
//
//				if (AppConstants.bookingFlightDO.vecSeatRequestDOs.size() > 0)
//					for (RequestDO requestDO : AppConstants.bookingFlightDO.vecSeatRequestDOs) {
//
//						if (requestDO.seatNumber.length() > 1)
//						{
//							if (requestDO.flightNumber.equalsIgnoreCase(seatMapDO.flightSegmentDO.flightNumber))
//							{
//								String seatNo = "", rowNo = "";
//								int strLength = requestDO.seatNumber.length();
//
//								seatNo = requestDO.seatNumber.substring(strLength - 1, strLength);
//								rowNo = requestDO.seatNumber.substring(0,strLength - 1);
//
//								if (rowNo.equalsIgnoreCase(airRowDO.rowNumber)
//										&& seatNo.equalsIgnoreCase(airSeatDO.seatNumber))
//								{
//									vecSeatsSelected.add(ivFlightSeat);
//									ivFlightSeat.setTag(R.string.seatSelected,true);
//									ivFlightSeat.setImageResource(R.drawable.btseatselected_x);
//
//									if (!requestDO.travelerRefNumberRPHList.equalsIgnoreCase(requestDOSel.travelerRefNumberRPHList)) {
//										ivFlightSeat.setEnabled(false);
//									} else
//										ivFlightSeat.setEnabled(true);
//								}
//							}
//						}
//					}
//
//				ivFlightSeat.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						String strSeatNo = v.getTag(R.string.seatRowNo).toString()
//								+ v.getTag(R.string.seatNo).toString();
//
//						if ((Boolean) v.getTag(R.string.seatSelected))
//						{
//							boolean isFound = false;
//							for (RequestDO requestDO : AppConstants.bookingFlightDO.vecSeatRequestDOs) {
//
//								if (requestDO.flightNumber.equalsIgnoreCase(seatMapDO.flightSegmentDO.flightNumber)) {
//									if (requestDO.seatNumber.equalsIgnoreCase(strSeatNo)) {
//										if (requestDO.travelerRefNumberRPHList.equalsIgnoreCase(requestDOSel.travelerRefNumberRPHList)) {
//											requestDO.seatNumber = "";
//										} else
//											isFound = true;
//										break;
//									}
//								}
//							}
//							if (!isFound) {
//								requestDOSel.seatNumber = "";
//
//								tvPersonalTripItemAmount.setText(getString(R.string.Select));
//								tvPersonalTripItemAEDValue.setText(StringUtils.getStrFloatFromString(""));
//								v.setTag(R.string.seatSelected, false);
//								llPersonalTripItemCross.setVisibility(View.INVISIBLE);
//								((ImageView) v).setImageResource(R.drawable.btseatnormal);
//							}
//						}
//						else
//						{
//							if (!requestDOSel.isINF)
//							{
//								boolean isFound = false;
//								if (vecSeatsSelected.size() > 0)
//								{
//									for (int i = 0; i < vecSeatsSelected.size(); i++) {
//										ImageView imageView = vecSeatsSelected.get(i);
//										String seatNoSel = "", rowNo = "";
//										int strLength = requestDOSel.seatNumber.length();
//
//										if (strLength > 0)
//										{
//											seatNoSel = requestDOSel.seatNumber.substring(strLength - 1,strLength);
//											rowNo = requestDOSel.seatNumber.substring(0, strLength - 1);
//
//											if (rowNo.equalsIgnoreCase(imageView.getTag(R.string.seatRowNo).toString())
//													&& seatNoSel.equalsIgnoreCase(imageView.getTag(R.string.seatNo).toString()))
//											{
//												vecSeatsSelected.remove(imageView);
//												vecSeatsSelected.add((ImageView) v);
//												imageView.setTag(R.string.seatSelected,false);
//												imageView.setImageResource(R.drawable.btseatnormal);
//
//												isFound = true;
//											}
//										}
//									}
//								}
//								requestDOSel.seatNumber = strSeatNo;
//								tvPersonalTripItemAmount.setText(requestDOSel.seatNumber);
//								tvPersonalTripItemAEDValue.setText(StringUtils.getStrFloatFromString(airSeatDO.seatCharacteristics));
//								v.setTag(R.string.seatSelected, true);
//								((ImageView) v).setImageResource(R.drawable.btseatselected_x);
//
//								if (!isFound)
//								{
//									llPersonalTripItemCross.setVisibility(View.VISIBLE);
//									vecSeatsSelected.add((ImageView) v);
//								}
//							}
//							else
//							{
//								int seatNo = StringUtils.getInt(v.getTag(R.string.seatRowNo).toString());
//								if (seatNo == 11 || seatNo == 12)
//								{
//									showGateSeatPopup();
//								}
//								else if (seatNo % 2 == 0)
//								{
//									if (v.getTag(R.string.seatNo).toString().equalsIgnoreCase("B"))
//									{
//										showCustomDialog(
//												PersonalizeyourTripActivity.this,
//												getString(R.string.Alert),
//												getString(R.string.SeatSelectionError),
//												getString(R.string.Ok), "", "");
//									}
//									else
//									{
//										boolean isFound = false;
//										if (vecSeatsSelected.size() > 0)
//										{
//											for (int i = 0; i < vecSeatsSelected.size(); i++) {
//												ImageView imageView = vecSeatsSelected.get(i);
//												String seatNoSel = "", rowNo = "";
//												int strLength = requestDOSel.seatNumber.length();
//
//												if (strLength > 0)
//												{
//													seatNoSel = requestDOSel.seatNumber.substring(strLength - 1,strLength);
//													rowNo = requestDOSel.seatNumber.substring(0,strLength - 1);
//
//													if (rowNo.equalsIgnoreCase(imageView.getTag(R.string.seatRowNo).toString())
//															&& seatNoSel.equalsIgnoreCase(imageView.getTag(R.string.seatNo).toString())) {
//														vecSeatsSelected.remove(imageView);
//														vecSeatsSelected.add((ImageView) v);
//														imageView.setTag(R.string.seatSelected,false);
//														imageView.setImageResource(R.drawable.btseatnormal);
//
//														isFound = true;
//													}
//												}
//											}
//										}
//										requestDOSel.seatNumber = strSeatNo;
//										tvPersonalTripItemAmount.setText(requestDOSel.seatNumber);
//										tvPersonalTripItemAEDValue.setText(StringUtils.getStrFloatFromString(airSeatDO.seatCharacteristics));
//										v.setTag(R.string.seatSelected, true);
//										((ImageView) v).setImageResource(R.drawable.btseatselected_x);
//
//										if (!isFound)
//										{
//											llPersonalTripItemCross.setVisibility(View.VISIBLE);
//											vecSeatsSelected.add((ImageView) v);
//										}
//									}
//								}
//								else
//								{
//									boolean isFound = false;
//									if (vecSeatsSelected.size() > 0)
//									{
//										for (int i = 0; i < vecSeatsSelected.size(); i++) {
//											ImageView imageView = vecSeatsSelected.get(i);
//											String seatNoSel = "", rowNo = "";
//											int strLength = requestDOSel.seatNumber.length();
//
//											if (strLength > 0)
//											{
//												seatNoSel = requestDOSel.seatNumber.substring(strLength - 1,strLength);
//												rowNo = requestDOSel.seatNumber.substring(0,strLength - 1);
//
//												if (rowNo.equalsIgnoreCase(imageView.getTag(R.string.seatRowNo).toString())
//														&& seatNoSel.equalsIgnoreCase(imageView.getTag(R.string.seatNo).toString())) {
//													vecSeatsSelected.remove(imageView);
//													vecSeatsSelected.add((ImageView) v);
//													imageView.setTag(R.string.seatSelected,false);
//													imageView.setImageResource(R.drawable.btseatnormal);
//
//													isFound = true;
//												}
//											}
//										}
//									}
//									requestDOSel.seatNumber = strSeatNo;
//									tvPersonalTripItemAmount.setText(requestDOSel.seatNumber);
//									tvPersonalTripItemAEDValue.setText(StringUtils.getStrFloatFromString(airSeatDO.seatCharacteristics));
//									v.setTag(R.string.seatSelected, true);
//									((ImageView) v).setImageResource(R.drawable.btseatselected_x);
//
//									if (!isFound)
//									{
//										llPersonalTripItemCross.setVisibility(View.VISIBLE);
//										vecSeatsSelected.add((ImageView) v);
//									}
//								}
//
//							}
//						}
//					}
//				});
//			}
//			llseatlayout.addView(seatlayoutview);
//		}
//	}
//
//	private void callBaggageServiceOneWay() {
//		showLoader("");
//		Vector<FlightSegmentDO> vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//
//		for (int i = 0; i < AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//				.size(); i++) {
//			vecFlightSegmentDOs
//			.addAll(AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//					.get(i).vecFlightSegmentDOs);
//		}
//		for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
//			vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
//		}
//		if (!airPriceQuoteDOTotal.transactionIdentifier.equalsIgnoreCase(""))
//			AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airPriceQuoteDOTotal.transactionIdentifier;
//		if (new CommonBL(PersonalizeyourTripActivity.this,
//				PersonalizeyourTripActivity.this).getAirBaggageDetails(
//						AppConstants.bookingFlightDO.requestParameterDO, "AED",
//						vecFlightSegmentDOs)) {
//
//		} else {
//			hideLoader();
//			showCustomDialog(this, getString(R.string.Alert),
//					getString(R.string.InternetProblem),
//					getString(R.string.Ok), "", "");
//		}
//	}
//
//	private void callBaggageServiceReturn() {
//
//		isBaggageService = true;
//		if (isManageBook)
//			showLoader("");
//		if (AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0) {
//			Vector<FlightSegmentDO> vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size(); i++) {
//				vecFlightSegmentDOs
//				.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//						.get(i).vecFlightSegmentDOs);
//			}
//			for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
//				vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
//			}
//			if (new CommonBL(PersonalizeyourTripActivity.this,
//					PersonalizeyourTripActivity.this).getAirBaggageDetails(
//							AppConstants.bookingFlightDO.requestParameterDO, "AED",
//							vecFlightSegmentDOs)) {
//			} else {
//				hideLoader();
//				showCustomDialog(this, getString(R.string.Alert),
//						getString(R.string.InternetProblem),
//						getString(R.string.Ok), "", "");
//			}
//		} else {
//			if(vecAirBaggageDetailsDO != null && vecAirBaggageDetailsDO.size() > 0
//					&& vecAirBaggageDetailsDO.get(0).vecBaggageDetailDOs == null)
//			{
//				addNoDataAvailableWithFlight(getString(R.string.NoBaggage), llBaggageMain, false,1);
//			}
//			else
//				addBaggageItems(vecAirBaggageDetailsDO);
//			callSeatsService();
//		}
//	}
//
//	private void callSeatsService() {
//		Vector<FlightSegmentDO> vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//
//		if (!isManageBook) {
//			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//					.size(); i++) {
//				vecFlightSegmentDOs
//				.addAll(AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//						.get(i).vecFlightSegmentDOs);
//			}
//			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//					.size(); i++) {
//				vecFlightSegmentDOs
//				.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//						.get(i).vecFlightSegmentDOs);
//			}
//
//			for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
//				vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
//			}
//		} else {
//			if (isOutBound) {
//				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//						.size(); i++) {
//					vecFlightSegmentDOs
//					.addAll(AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//							.get(i).vecFlightSegmentDOs);
//				}
//				for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
//					vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
//				}
//			} else {
//				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//						.size(); i++) {
//					vecFlightSegmentDOs
//					.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//							.get(i).vecFlightSegmentDOs);
//				}
//
//				for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
//					vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
//				}
//			}
//		}
//
//		if (new CommonBL(PersonalizeyourTripActivity.this,
//				PersonalizeyourTripActivity.this).getAirSeatMap(
//						AppConstants.bookingFlightDO.requestParameterDO,
//						vecFlightSegmentDOs)) {
//		} else {
//			hideLoader();
//			showCustomDialog(this, getString(R.string.Alert),
//					getString(R.string.InternetProblem),
//					getString(R.string.Ok), "", "");
//		}
//	}
//
//	private void callMealsService() {
//		Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();
//		if (!isManageBook) {
//			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//					.size(); i++) {
//				vecOriginDestinationOptionDOs
//				.add(AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//						.get(i));
//			}
//			if (AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs != null
//					&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//					.size() > 0)
//				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//						.size(); i++) {
//					vecOriginDestinationOptionDOs
//					.add(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//							.get(i));
//				}
//		} else {
//			if (isOutBound) {
//				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//						.size(); i++) {
//					vecOriginDestinationOptionDOs
//					.add(AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//							.get(i));
//				}
//			} else {
//				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//						.size(); i++) {
//					vecOriginDestinationOptionDOs
//					.add(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//							.get(i));
//				}
//			}
//		}
//
//		if (new CommonBL(PersonalizeyourTripActivity.this,
//				PersonalizeyourTripActivity.this).getAirMealDetails(
//						AppConstants.bookingFlightDO.requestParameterDO,
//						vecOriginDestinationOptionDOs)) {
//		} else {
//			hideLoader();
//			showCustomDialog(this, getString(R.string.Alert),
//					getString(R.string.InternetProblem),
//					getString(R.string.Ok), "", "");
//		}
//	}
//
//	private void callInsuranceService() {
//		String travelType = AppConstants.bookingFlightDO.myBookFlightDO.travelType;
//
//		if (!AppConstants.bookingFlightDO.myBookFlightDO.travelType
//				.equalsIgnoreCase("")
//				&& !AppConstants.bookingFlightDO.myBookFlightDOReturn.travelType
//				.equalsIgnoreCase(""))
//			travelType = AppConstants.TRAVEL_TYPE_RETURN;
//		else
//			travelType = AppConstants.TRAVEL_TYPE_ONEWAY;
//
//		FlightSegmentDO flightSegmentDO = new FlightSegmentDO();
//		int sizeReturnFlt = 0, sizeReturnFltOrig = 0, sizeReturnFltArvTime = 0, sizeReturnFltArvAirPortCode = 0;
//
//		flightSegmentDO.departureDateTime = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//				.get(0).vecFlightSegmentDOs.get(0).departureDateTime;
//		flightSegmentDO.departureAirportCode = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//				.get(0).vecFlightSegmentDOs.get(0).departureAirportCode;
//		if (!(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//				.size() > 0)) {
//			sizeReturnFlt = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//					.size();
//			sizeReturnFltArvTime = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//					.get(sizeReturnFlt - 1).vecFlightSegmentDOs.size();
//			flightSegmentDO.arrivalDateTime = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//					.get(sizeReturnFlt - 1).vecFlightSegmentDOs
//					.get(sizeReturnFltArvTime - 1).arrivalDateTime;
//			sizeReturnFltArvAirPortCode = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//					.get(sizeReturnFlt - 1).vecFlightSegmentDOs.size();
//			flightSegmentDO.arrivalAirportCode = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//					.get(sizeReturnFlt - 1).vecFlightSegmentDOs
//					.get(sizeReturnFltArvAirPortCode - 1).arrivalAirportCode;
//		} else {
//			sizeReturnFlt = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//					.size();
//			sizeReturnFltArvTime = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//					.get(sizeReturnFlt - 1).vecFlightSegmentDOs.size();
//			flightSegmentDO.arrivalDateTime = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//					.get(sizeReturnFlt - 1).vecFlightSegmentDOs
//					.get(sizeReturnFltArvTime - 1).arrivalDateTime;
//			sizeReturnFltOrig = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//					.size();
//			sizeReturnFltArvAirPortCode = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//					.get(sizeReturnFlt - 1).vecFlightSegmentDOs.size();
//			flightSegmentDO.arrivalAirportCode = AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//					.get(sizeReturnFltOrig - 1).vecFlightSegmentDOs
//					.get(sizeReturnFltArvAirPortCode - 1).arrivalAirportCode;
//		}
//
//		if (new CommonBL(PersonalizeyourTripActivity.this,
//				PersonalizeyourTripActivity.this)
//		.getInsuranceQuote(
//				AppConstants.bookingFlightDO.requestParameterDO,
//				travelType,
//				AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO,
//				flightSegmentDO)) {
//		} else {
//			hideLoader();
//			showCustomDialog(this, getString(R.string.Alert),
//					getString(R.string.InternetProblem),
//					getString(R.string.Ok), "", "");
//		}
//	}
//
//	private void callHalaServiceOneWay() {
//		vecAirHalaDOs.clear();
//		isHalaService = false;
//		Vector<FlightSegmentDO> vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//		for (int i = 0; i < AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//				.size(); i++) {
//			vecFlightSegmentDOs
//			.addAll(AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//					.get(i).vecFlightSegmentDOs);
//		}
//		for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
//			vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
//		}
//		if (new CommonBL(PersonalizeyourTripActivity.this,
//				PersonalizeyourTripActivity.this).getHalaReq(
//						AppConstants.bookingFlightDO.requestParameterDO,
//						vecFlightSegmentDOs)) {
//		} else {
//			hideLoader();
//			showCustomDialog(this, getString(R.string.Alert),
//					getString(R.string.InternetProblem),
//					getString(R.string.Ok), "", "");
//		}
//	}
//
//	private void callHalaServiceReturn() {
//		isHalaService = true;
//		if (AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//				.size() > 0) {
//			Vector<FlightSegmentDO> vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
//
//			if (AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs != null
//					&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//					.size() > 0)
//				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//						.size(); i++) {
//					vecFlightSegmentDOs
//					.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//							.get(i).vecFlightSegmentDOs);
//				}
//			for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
//				vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
//			}
//			if (new CommonBL(PersonalizeyourTripActivity.this,
//					PersonalizeyourTripActivity.this).getHalaReq(
//							AppConstants.bookingFlightDO.requestParameterDO,
//							vecFlightSegmentDOs)) {
//			} else {
//				hideLoader();
//				showCustomDialog(this, getString(R.string.Alert),
//						getString(R.string.InternetProblem),
//						getString(R.string.Ok), "", "");
//			}
//		} else
//			hideLoader();
//	}
//
//	@Override
//	public void dataRetreived(Response data) {
//		if (data != null) {
//			switch (data.method) {
//			case AIR_BAGGAGE_DETAILS:
//				if (!data.isError) {
//					llBaggageMain.setVisibility(View.VISIBLE);
//					AirBaggageDetailsDO airBaggageDetailsDO = (AirBaggageDetailsDO) data.data;
//					vecAirBaggageDetailsDO.add(airBaggageDetailsDO);
//					if (!airBaggageDetailsDO.transactionIdentifier.equalsIgnoreCase(""))
//						AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airBaggageDetailsDO.transactionIdentifier;
//					if (isBaggageService) {
//						if(airBaggageDetailsDO != null && airBaggageDetailsDO.vecBaggageDetailDOs == null)
//						{
//							addNoDataAvailableWithFlight(getString(R.string.NoBaggage), llBaggageMain, false,0);
//						}
//						else if(vecAirBaggageDetailsDO != null && vecAirBaggageDetailsDO.size() > 0
//								&& vecAirBaggageDetailsDO.get(0).vecBaggageDetailDOs == null)
//						{
//						}
//						else
//							addBaggageItems(vecAirBaggageDetailsDO);
//						callSeatsService();
//					} else
//					{
//						if(vecAirBaggageDetailsDO != null && vecAirBaggageDetailsDO.size() > 0
//								&& vecAirBaggageDetailsDO.get(0).vecBaggageDetailDOs == null)
//						{
//							addNoDataAvailableWithFlight(getString(R.string.NoBaggage), llBaggageMain, false,1);
//						}
////						else
////							addBaggageItems(vecAirBaggageDetailsDO);
//						callBaggageServiceReturn();
//					}
//				} else
//				{
//					if(data.data instanceof String)
//					{
//						if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
//						{
//							hideLoader();
//							showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
//						}
//						else callSeatsService();
//					}
//					else
//						callSeatsService();
//				}
//
//				break;
//			case AIR_SEAT_DETAILS:
//
//				if (!data.isError) {
//					airSeatMapDO = (AirSeatMapDO) data.data;
//					if (!airSeatMapDO.transactionIdentifier.equalsIgnoreCase(""))
//						AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airSeatMapDO.transactionIdentifier;
//					callMealsService();
//				}
//				else if(data.data instanceof String)
//				{
//					if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
//					{
//						hideLoader();
//						showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
//					}
//					else callMealsService();
//				}
//				else
//					callMealsService();
//				break;
//			case AIR_MEAL_DETAILS:
//
//				if (!data.isError) {
//					airMealDetailsDO = (AirMealDetailsDO) data.data;
//					if (!airMealDetailsDO.transactionIdentifier.equalsIgnoreCase(""))
//						AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airMealDetailsDO.transactionIdentifier;
//					if (airMealDetailsDO != null
//							&& airMealDetailsDO.vecMealDetailsDOs != null
//							&& airMealDetailsDO.vecMealDetailsDOs.size() > 0)
//						for (int i = 0; i < airMealDetailsDO.vecMealDetailsDOs.size(); i++) {
//							MealDetailsDO mealDetailsDO = airMealDetailsDO.vecMealDetailsDOs.get(i);
//							mealDetailsDO.vecMealCategoriesDO = new Vector<MealCategoriesDO>();
//							if(mealDetailsDO.vecMealsDO != null && mealDetailsDO.vecMealsDO.size() > 0)
//							for (MealDO mealDO : mealDetailsDO.vecMealsDO) {
//								if (!mealDetailsDO.vecMealcategoryNames
//										.contains(mealDO.mealCategoryCode)) {
//									mealDetailsDO.vecMealcategoryImageUrls
//									.add(mealDO.mealImageLink);
//									mealDetailsDO.vecMealcategoryNames
//									.add(mealDO.mealCategoryCode);
//								}
//							}
//							if(mealDetailsDO.vecMealcategoryNames != null && mealDetailsDO.vecMealcategoryNames.size() > 0)
//							for (int x = 0; x < mealDetailsDO.vecMealcategoryNames.size(); x++) {
//								MealCategoriesDO mealCategoriesDO = new MealCategoriesDO();
//								mealCategoriesDO.mealCategory = mealDetailsDO.vecMealcategoryNames.get(x);
//								mealCategoriesDO.vecMealsDO = new Vector<MealDO>();
//								if(mealDetailsDO.vecMealsDO != null && mealDetailsDO.vecMealsDO.size() > 0)
//								for (MealDO mealDO : mealDetailsDO.vecMealsDO) {
//									if (mealDetailsDO.vecMealcategoryNames.get(x).equalsIgnoreCase(
//													mealDO.mealCategoryCode))
//										mealCategoriesDO.vecMealsDO.add(mealDO);
//								}
//								mealDetailsDO.vecMealCategoriesDO.add(mealCategoriesDO);
//							}
//						}
//
//					if (!isManageBook)
//						callInsuranceService();
//					else if (isOutBound)
//						callHalaServiceOneWay();
//					else if (isManageBook && !isOutBound)
//						callHalaServiceReturn();
//				}
//				else
//				{
//					if(data.data instanceof String)
//					{
//						if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
//						{
//							hideLoader();
//							showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
//						}
//						else {
//							if (!isManageBook)
//								callInsuranceService();
//							else if (isOutBound)
//								callHalaServiceOneWay();
//							else if (isManageBook && !isOutBound)
//								callHalaServiceReturn();
//						}
//					}
//				}
//				break;
//			case INSURANCE_QUOTE:
//
//				if (!data.isError) {
//					insuranceQuoteDO = (InsuranceQuoteDO) data.data;
//					if (!insuranceQuoteDO.transactionIdentifier.equalsIgnoreCase(""))
//						AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = insuranceQuoteDO.transactionIdentifier;
//					callHalaServiceOneWay();
//				}
//				else
//				{
//					if(data.data instanceof String)
//					{
//						if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
//						{
//							hideLoader();
//							showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
//						}
//						else {
//							callHalaServiceOneWay();
//						}
//					}
//				}
//
//				break;
//			case HALA_REQ:
//
//				if (!data.isError) {
//					AirHalaDO airHalaDO = (AirHalaDO) data.data;
//					if (airHalaDO != null && airHalaDO.vecHalaDOs != null
//							&& airHalaDO.vecHalaDOs.size() > 0) {
//						vecHalaDos.add(airHalaDO.vecHalaDOs.get(0));
////						if (!airHalaDO.transactionIdentifier.equalsIgnoreCase(""))
////							AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airHalaDO.transactionIdentifier;
//					}
//					if (!isHalaService) {
//						callHalaServiceReturn();
//					} else
//						hideLoader();
//				}
//				else
//				{
//					if(data.data instanceof String)
//					{
//						if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
//						{
//							hideLoader();
//							showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
//						}
//						else {
//							if (!isHalaService) {
//								callHalaServiceReturn();
//							} else
//								hideLoader();
//						}
//					}else
//						hideLoader();
//				}
//				break;
//
//			default:
//				break;
//			}
//		} else
//			hideLoader();
//	}
//	@Override
//	public void onButtonYesClick(String from) {
//		super.onButtonYesClick(from);
//		if(from.equalsIgnoreCase(AppConstants.INTERNET_PROBLEM))
//			clickHome();
//	}
//}