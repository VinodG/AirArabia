package com.winit.airarabia;

import java.util.Vector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirBaggageDetailsDO;
import com.winit.airarabia.objects.AirBookDO;
import com.winit.airarabia.objects.AirHalaDO;
import com.winit.airarabia.objects.AirMealDetailsDO;
import com.winit.airarabia.objects.AirPriceQuoteDO;
import com.winit.airarabia.objects.AirSeatMapDO;
import com.winit.airarabia.objects.BaggageDO;
import com.winit.airarabia.objects.BundledServiceDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.HalaDO;
import com.winit.airarabia.objects.InsuranceQuoteDO;
import com.winit.airarabia.objects.MealCategoriesDO;
import com.winit.airarabia.objects.MealDO;
import com.winit.airarabia.objects.MealDetailsDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;
import com.winit.airarabia.objects.PassengerInfoPersonDO;
import com.winit.airarabia.objects.RequestDO;
import com.winit.airarabia.objects.SeatMapDO;
import com.winit.airarabia.utils.MathUtils;
import com.winit.airarabia.webaccess.Response;

public class PersonalizeyourTripActivityNew extends BaseActivity implements
		OnClickListener, DataListener {

	private LinearLayout llPersonalize,
			llBaggageMain, llSeatMain, llMealMain, llInsuranceMain, llHalaMain, llInsurance, llHalaTotal;
	private TextView tvseatselectdiscription, tvmealselectdiscription, tvinsuravcediscription, tvairportservicediscription, tvTagAirportService, tvTagTravelInsurance, tv_baggae_textname, tvTagMealSelection, tv_seatselecName;
	private boolean isManageBook = false;
	private AirPriceQuoteDO airPriceQuoteDOTotal;
	private Vector<FlightSegmentDO> vecFlights;
	private AirBookDO airBookDO;
	private AirSeatMapDO airSeatMapDO;
	private AirMealDetailsDO airMealDetailsDO;
	private InsuranceQuoteDO insuranceQuoteDO;
	private Vector<HalaDO> vecHalaDOs;
	private boolean isHalaService = false;
	private boolean isAddExtras = false;


	private LinearLayout llEditBaggage, llEditSeat, llEditInsurance, llEditMeal, llEditHala;
	private Button btnEditBaggage, btnEditSeat, btnEditInsurance, btnEditMeal, btnEditHala;


	private boolean isValueFair = false, isExtraFare = false, SeatNotSelected = false, /*mealExceeds = false,*/
			mealNotSelected = false, baggageNotSelected = false;

	private final int REQ_CODE_BAGGAGE = 100,
			REQ_CODE_SEAT = 101,
			REQ_CODE_INSURANCE = 102,
			REQ_CODE_MEAL = 103,
			REQ_CODE_SSL = 104;

	private PersonalizeyourTripActivityNew.BCR bcr;

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
		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		intentFilter.addAction(AppConstants.BOOK_SUCCESS);
		registerReceiver(bcr, intentFilter);
		airBookDO = (AirBookDO) getIntent().getExtras().getSerializable(AppConstants.AIR_BOOK);
		isManageBook = getIntent().getBooleanExtra("MANAGE_BOOKING", false);
		isAddExtras = getIntent().getBooleanExtra("ADD_EXTRA", false);
		airPriceQuoteDOTotal = (AirPriceQuoteDO) getIntent().getExtras()
				.getSerializable(AppConstants.AIRPORTS_PRICE_TOTAL);

		if (AppConstants.bookingFlightDO != null && AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify != null
				&& AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.size() > 0)
			isManageBook = true;

		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(getString(R.string.Continue));

		llPersonalize = (LinearLayout) layoutInflater.inflate(R.layout.personalizeyourtrip_new, null);
		llMiddleBase.addView(llPersonalize, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		llBaggageMain = (LinearLayout) llPersonalize.findViewById(R.id.llBaggageMain);
		llSeatMain = (LinearLayout) llPersonalize.findViewById(R.id.llSeatMain);
		llMealMain = (LinearLayout) llPersonalize.findViewById(R.id.llMealMain);
		llInsuranceMain = (LinearLayout) llPersonalize.findViewById(R.id.llInsuranceMain);
		llInsurance = (LinearLayout) llPersonalize.findViewById(R.id.llInsurance);
		llHalaMain = (LinearLayout) llPersonalize.findViewById(R.id.llHalaMain);

		llHalaTotal = (LinearLayout) llPersonalize.findViewById(R.id.llHalaTotal);

		llEditBaggage = (LinearLayout) llPersonalize.findViewById(R.id.llEditBaggage);
		llEditSeat = (LinearLayout) llPersonalize.findViewById(R.id.llEditSeat);
		llEditInsurance = (LinearLayout) llPersonalize.findViewById(R.id.llEditInsurance);
		llEditMeal = (LinearLayout) llPersonalize.findViewById(R.id.llEditMeal);
		llEditHala = (LinearLayout) llPersonalize.findViewById(R.id.llEditHala);

		tv_baggae_textname = (TextView) llPersonalize.findViewById(R.id.tv_baggae_textname);
		tv_seatselecName = (TextView) llPersonalize.findViewById(R.id.tv_seatselecName);
		tvTagMealSelection = (TextView) llPersonalize.findViewById(R.id.tvTagMealSelection);
		tvTagTravelInsurance = (TextView) llPersonalize.findViewById(R.id.tvTagTravelInsurance);
		tvTagAirportService = (TextView) llPersonalize.findViewById(R.id.tvTagAirportService);
		btnEditBaggage = (Button) llPersonalize.findViewById(R.id.btnEditBaggage);
		btnEditSeat = (Button) llPersonalize.findViewById(R.id.btnEditSeat);
		btnEditInsurance = (Button) llPersonalize.findViewById(R.id.btnEditInsurance);
		btnEditMeal = (Button) llPersonalize.findViewById(R.id.btnEditMeal);
		btnEditHala = (Button) llPersonalize.findViewById(R.id.btnEditHala);

		if (AppConstants.bookingFlightDO != null) {
			AppConstants.bookingFlightDO.vecSeatRequestDOs = new Vector<RequestDO>();
			AppConstants.bookingFlightDO.vecMealReqDOs = new Vector<RequestDO>();
			AppConstants.bookingFlightDO.vecInsrRequestDOs = new Vector<RequestDO>();
			AppConstants.bookingFlightDO.vecSSRRequests = new Vector<RequestDO>();
		}

//==================================newly added for issue===========================================================
		tvairportservicediscription = (TextView) llPersonalize.findViewById(R.id.tvairportservicediscription);
		tvseatselectdiscription = (TextView) llPersonalize.findViewById(R.id.tvseatselectdiscription);
		tvmealselectdiscription = (TextView) llPersonalize.findViewById(R.id.tvmealselectdiscription);
		tvinsuravcediscription = (TextView) llPersonalize.findViewById(R.id.tvinsuravcediscription);
		tvairportservicediscription = (TextView) llPersonalize.findViewById(R.id.tvairportservicediscription);

		vecFlights = new Vector<FlightSegmentDO>();

		airSeatMapDO = new AirSeatMapDO();
		insuranceQuoteDO = new InsuranceQuoteDO();
		airMealDetailsDO = new AirMealDetailsDO();
		vecHalaDOs = new Vector<HalaDO>();
		setTypeFaceOpenSansLight(llPersonalize);
		tv_baggae_textname.setTypeface(typefaceOpenSansSemiBold);
		tv_seatselecName.setTypeface(typefaceOpenSansSemiBold);
		tvTagMealSelection.setTypeface(typefaceOpenSansSemiBold);
		tvTagTravelInsurance.setTypeface(typefaceOpenSansSemiBold);
		tvTagAirportService.setTypeface(typefaceOpenSansSemiBold);
	}

	@Override
	public void bindingControl() {

		if (isManageBook) {
			llInsurance.setVisibility(View.GONE);
			llInsuranceMain.setVisibility(View.GONE);
		}

		//Added on 23-Feb-2018 by Mukesh to Enable/Disable Airport service from Layout
			if (AppConstants.bookingFlightDO != null && AppConstants.bookingFlightDO.myBookFlightDO != null && AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode != null && AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode.equalsIgnoreCase("SHJ")) {
				llHalaTotal.setVisibility(View.VISIBLE);
			} else if (AppConstants.bookingFlightDO != null && AppConstants.bookingFlightDO.myBookFlightDO != null && AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode != null && AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode.equalsIgnoreCase("SHJ")) {
				llHalaTotal.setVisibility(View.VISIBLE);
			} else {
				llHalaTotal.setVisibility(View.GONE);
			}

		vecFlights.clear();
		if (AppConstants.bookingFlightDO != null) {
			if (airPriceQuoteDOTotal == null)
				AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airBookDO.requestParameterDO.transactionIdentifier;
			else
				AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airPriceQuoteDOTotal.transactionIdentifier;

			if (AppConstants.bookingFlightDO.myODIDOOneWay != null) {
				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size(); i++) {
					vecFlights.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
							.get(i).vecFlightSegmentDOs);
				}
			}
			if (AppConstants.bookingFlightDO.myODIDOReturn != null) {
				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size(); i++) {
					vecFlights.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
							.get(i).vecFlightSegmentDOs);
				}
			}
		}

		addBaggageItems(AppConstants.vecAirBaggageDetailsDO);

		callSeatsService();

		tvHeaderTitle.setText(getString(R.string.PersonaliseYourTrip));
		btnSubmitNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppConstants.bookingFlightDO.vecMealReqDOs.clear();
				trackEvent("Personalize Trip", "Continue_PersonalizeTrip_button_clicked", "");

				if (airMealDetailsDO != null
						&& airMealDetailsDO.vecMealDetailsDOs != null
						&& airMealDetailsDO.vecMealDetailsDOs.size() > 0)
					for (MealDetailsDO mealDetailsDO : airMealDetailsDO.vecMealDetailsDOs) {
						for (Vector<RequestDO> vecReqDos : mealDetailsDO.vecMealRequestDOs) {
							AppConstants.bookingFlightDO.vecMealReqDOs.addAll(vecReqDos);
						}
					}

				if (CheckForBundleValidation())
					moveToBookingSummaryActivity();
				else {
					if (isValueFair) {
						if (baggageNotSelected && mealNotSelected)
							showCustomDialog(PersonalizeyourTripActivityNew.this, getString(R.string.Alert), "The fare you have selected includes [BAGGAGE, MEAL]. Please make your selection to continue.", getString(R.string.Ok), "", "NO_BAGGAGE");
						else if (baggageNotSelected)
							showCustomDialog(PersonalizeyourTripActivityNew.this, getString(R.string.Alert), "The fare you have selected includes [BAGGAGE]. Please make your selection to continue.", getString(R.string.Ok), "", "NO_BAGGAGE");
						else if (mealNotSelected)
							showCustomDialog(PersonalizeyourTripActivityNew.this, getString(R.string.Alert), "The fare you have selected includes [MEAL]. Please make your selection to continue.", getString(R.string.Ok), "", "NO_BAGGAGE");
					}
					else
					{
						if(SeatNotSelected && mealNotSelected)
							showCustomDialog(PersonalizeyourTripActivityNew.this, getString(R.string.Alert), "The fare you have selected includes [SEAT, MEAL]. Please make your selection to continue.", getString(R.string.Ok), "", "NO_BAGGAGE");
						else if (SeatNotSelected)
							showCustomDialog(PersonalizeyourTripActivityNew.this, getString(R.string.Alert), "The fare you have selected includes [SEAT]. Please make your selection to continue.", getString(R.string.Ok), "", "NO_BAGGAGE");
						else if (mealNotSelected)
							showCustomDialog(PersonalizeyourTripActivityNew.this, getString(R.string.Alert), "The fare you have selected includes [MEAL]. Please make your selection to continue.", getString(R.string.Ok), "", "NO_BAGGAGE");
					}
				}
			}
		});

		btnEditBaggage.setOnClickListener(this);
		btnEditSeat.setOnClickListener(this);
		btnEditInsurance.setOnClickListener(this);
		btnEditMeal.setOnClickListener(this);
		btnEditHala.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnEditBaggage) {
			trackEvent("Personalize Trip", "EditButton_Baggage_clicked", "");
			moveToNextPageDetails(REQ_CODE_BAGGAGE);
		} else if (v.getId() == R.id.btnEditSeat) {
			trackEvent("Personalize Trip", "EditButton_seat_clicked", "");
			moveToNextPageDetails(REQ_CODE_SEAT);

		} else if (v.getId() == R.id.btnEditInsurance) {
			trackEvent("Personalize Trip", "EditButton_Insurance_clicked", "");
			moveToNextPageDetails(REQ_CODE_INSURANCE);
		} else if (v.getId() == R.id.btnEditMeal) {
			trackEvent("Personalize Trip", "EditButton_Meal_clicked", "");
			moveToNextPageDetails(REQ_CODE_MEAL);
		} else if (v.getId() == R.id.btnEditHala) {
			trackEvent("Personalize Trip", "EditButton_Hala_clicked", "");
			moveToNextPageDetails(REQ_CODE_SSL);
		}
	}

	private void updateData(int selCode) {
		switch (selCode) {
			case REQ_CODE_BAGGAGE:

				if (AppConstants.bookingFlightDO.vecBaggageRequestDOs != null
						&& AppConstants.bookingFlightDO.vecBaggageRequestDOs.size() > 0) {
					btnEditBaggage.setText(R.string.edit);
				} else {
					btnEditBaggage.setText(R.string.add);
				}

				addBaggageItems(AppConstants.vecAirBaggageDetailsDO);

				break;
			case REQ_CODE_SEAT:

				if (AppConstants.bookingFlightDO.vecSeatRequestDOs != null
						&& AppConstants.bookingFlightDO.vecSeatRequestDOs.size() > 0) {

					btnEditSeat.setText(R.string.edit);
					tvseatselectdiscription.setVisibility(View.GONE);
				} else {
					btnEditSeat.setText(R.string.add);
					tvseatselectdiscription.setVisibility(View.VISIBLE);
				}

				addSeatItems(AppConstants.AirSeatMapDO);

				break;
			case REQ_CODE_INSURANCE:

				if (AppConstants.bookingFlightDO.vecInsrRequestDOs != null
						&& AppConstants.bookingFlightDO.vecInsrRequestDOs.size() > 0) {
					btnEditInsurance.setText(R.string.edit);
					tvinsuravcediscription.setVisibility(View.GONE);
				} else {
					btnEditInsurance.setText(R.string.add);
					tvinsuravcediscription.setVisibility(View.GONE);
				}

				addtravelinsuranceItems();

				break;
			case REQ_CODE_MEAL:

				if (AppConstants.bookingFlightDO.vecMealReqDOs != null
						&& AppConstants.bookingFlightDO.vecMealReqDOs.size() > 0) {
					btnEditMeal.setText(R.string.edit);
					tvmealselectdiscription.setVisibility(View.GONE);
				} else {
					btnEditMeal.setText(R.string.add);
					tvmealselectdiscription.setVisibility(View.VISIBLE);
				}

				addMealItems(AppConstants.airMealDetailsDO);

				break;
			case REQ_CODE_SSL:

				if (AppConstants.bookingFlightDO.vecSSRRequests != null
						&& AppConstants.bookingFlightDO.vecSSRRequests.size() > 0) {
					btnEditHala.setText(R.string.edit);
					tvairportservicediscription.setVisibility(View.GONE);
				} else {
					btnEditHala.setText(R.string.add);
					tvairportservicediscription.setVisibility(View.VISIBLE);
				}

				addHalaItems(AppConstants.vecHalaDOs);

				break;

			default:
				break;
		}
	}

	private void addNoDataAvailable(String strMsg, LinearLayout llSelected, boolean isNoBg) {
		TextView tvNoDataAvailable = (TextView) layoutInflater.inflate(R.layout.no_data_found, null);
		tvNoDataAvailable.setText(strMsg);
		if (isNoBg)
			tvNoDataAvailable.setBackgroundResource(0);
		llSelected.addView(tvNoDataAvailable);
	}

	private void addBaggageItems(Vector<AirBaggageDetailsDO> vecAirBaggageDetailsDO) {

		if (vecAirBaggageDetailsDO != null && vecAirBaggageDetailsDO.size() > 0) {
			llBaggageMain.removeAllViews();
			Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDO = new Vector<PassengerInfoPersonDO>();
			for (PassengerInfoPersonDO passengerInfoPersonDO : AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO) {
				if (!passengerInfoPersonDO.persontype
						.equalsIgnoreCase(AppConstants.PERSON_TYPE_INFANT))
					vecPassengerInfoPersonDO.add(passengerInfoPersonDO);
			}

			int sourceNameTemp = 0;
//            Boolean returnFlag=false;
			int bagFlightno = -1;
			for (AirBaggageDetailsDO airBaggageDetailsDO : vecAirBaggageDetailsDO) {
				bagFlightno++;
				if (airBaggageDetailsDO.vecBaggageDetailDOs != null
						&& airBaggageDetailsDO.vecBaggageDetailDOs.size() > 0
						&& airBaggageDetailsDO.vecBaggageDetailDOs.get(0).vecFlightSegmentDOs.size() > 0) {

					int returnSize = airBaggageDetailsDO.vecBaggageDetailDOs.size();

					for (int i = 0; i < airBaggageDetailsDO.vecBaggageDetailDOs.size(); i++) {
						Vector<BaggageDO> vecBaggageList = airBaggageDetailsDO.vecBaggageDetailDOs.get(i).vecBaggageDO;
						LinearLayout llBaggageChild = (LinearLayout) layoutInflater.inflate(R.layout.personalise_item, null);

						llBaggageMain.addView(llBaggageChild);

						TextView tvPersonalTripItemSource = (TextView) llBaggageChild
								.findViewById(R.id.tvPersonalTripDetailsItemSource);

						TextView tvPersonalTripItemDest = (TextView) llBaggageChild
								.findViewById(R.id.tvPersonalTripDetailsItemDest);

						TextView tvPersonalTripItemName = (TextView) llBaggageChild
								.findViewById(R.id.tvPersonalTripItemName);

						TextView tvPersonalTripItemValue = (TextView) llBaggageChild
								.findViewById(R.id.tvPersonalTripItemValue);

						ImageView ivFlightType = (ImageView) llBaggageChild
								.findViewById(R.id.ivFlightType);
						tvPersonalTripItemName.setTypeface(typefaceOpenSansRegular);
						tvPersonalTripItemValue.setTypeface(typefaceOpenSansRegular);
						tvPersonalTripItemName.setText(R.string.Baggage);

						String strValues = "";
						if (vecBaggageList != null && vecBaggageList.size() > 0) {

							for (int j = 0; j < vecPassengerInfoPersonDO.size(); j++) {

								for (int k = 0; k < airBaggageDetailsDO.vecBaggageDetailDOs
										.get(i).vecFlightSegmentDOs.size(); k++) {

									FlightSegmentDO flightSegmentDO = airBaggageDetailsDO.vecBaggageDetailDOs
											.get(i).vecFlightSegmentDOs.get(k);

									if (AppConstants.bookingFlightDO.vecBaggageRequestDOs != null
											&& AppConstants.bookingFlightDO.vecBaggageRequestDOs.size() > 0) {
										for (int x = 0; x < AppConstants.bookingFlightDO.vecBaggageRequestDOs.size(); x++) {
											RequestDO requestDO = AppConstants.bookingFlightDO.vecBaggageRequestDOs.get(x);
											if (requestDO.flightNumber.equalsIgnoreCase(flightSegmentDO.flightNumber)
													&& requestDO.travelerRefNumberRPHList.equalsIgnoreCase(vecPassengerInfoPersonDO.get(j).travelerRefNumberRPHList)) {
												if (TextUtils.isEmpty(strValues))
													strValues = requestDO.baggageCode;
												else
													strValues = strValues + ", " + requestDO.baggageCode;
											}
										}
									}
								}
							}
						}
						if (!TextUtils.isEmpty(strValues))
							tvPersonalTripItemValue.setText(strValues);
						else
							tvPersonalTripItemValue.setText(R.string.NoBaggage);

						if (AppConstants.allAirportNamesDO != null
								&& AppConstants.allAirportNamesDO.vecAirport != null
								&& AppConstants.allAirportNamesDO.vecAirport
								.size() > 0) {
							String sourceName = airBaggageDetailsDO.vecBaggageDetailDOs.get(i).vecFlightSegmentDOs.get(0).departureAirportCode,
									originName = airBaggageDetailsDO.vecBaggageDetailDOs.get(i).vecFlightSegmentDOs.get(airBaggageDetailsDO.vecBaggageDetailDOs
											.get(i).vecFlightSegmentDOs
											.size() - 1).arrivalAirportCode;
//							if(i==0)
//								i++;
//							else{
//								if(tv_baggae_textname.getText().toString().equalsIgnoreCase("قبل حجز الأمتعة")){
//								ivFlightType.setImageResource(R.drawable.flight_oneway);
//								}else{
//								ivFlightType.setImageResource(R.drawable.flight_return);
//								}
//							}
//
////==================================newly added by	for retrun==========================================
//
//							if(returnFlag){
//
//								if(tv_baggae_textname.getText().toString().equalsIgnoreCase("قبل حجز الأمتعة")){
//									ivFlightType.setImageResource(R.drawable.flight_oneway);
//									}else{
//									ivFlightType.setImageResource(R.drawable.flight_return);
//									}
//							}
//							else
//							   returnFlag=true;

							if (!checkLangArabic())

							{

								if (bagFlightno == 0) {
									ivFlightType.setImageResource(R.drawable.flight_oneway);
								} else {
									ivFlightType.setImageResource(R.drawable.flight_return);
								}
							} else {
								if (bagFlightno == 0) {
									ivFlightType.setImageResource(R.drawable.flight_return);
								} else {
									ivFlightType.setImageResource(R.drawable.flight_oneway);
								}
							}


							tvPersonalTripItemSource.setText(sourceName);
							tvPersonalTripItemDest.setText(originName);
						} else {
							tvPersonalTripItemSource
									.setText(airBaggageDetailsDO.vecBaggageDetailDOs
											.get(i).vecFlightSegmentDOs
											.get(0).departureAirportCode);
							tvPersonalTripItemDest
									.setText(airBaggageDetailsDO.vecBaggageDetailDOs
											.get(i).vecFlightSegmentDOs
											.get(airBaggageDetailsDO.vecBaggageDetailDOs
													.get(i).vecFlightSegmentDOs
													.size() - 1).arrivalAirportCode);
							if (tv_baggae_textname.getText().toString().equalsIgnoreCase("قبل حجز الأمتعة")) {
								ivFlightType.setBackgroundResource(R.drawable.flight_oneway);
							} else {
								ivFlightType.setBackgroundResource(R.drawable.flight_return);
							}
						}
					}
				}
			}
		}
	}

	private void addHalaItems(Vector<HalaDO> vecHalaDOs) {
		llHalaMain.removeAllViews();
		llHalaMain.setVisibility(View.VISIBLE);

		if (vecHalaDOs != null && vecHalaDOs.size() > 0) {

			llHalaTotal.setVisibility(View.VISIBLE);
			Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDO = new Vector<PassengerInfoPersonDO>();
			for (PassengerInfoPersonDO passengerInfoPersonDO : AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO) {
				if (!passengerInfoPersonDO.persontype.equalsIgnoreCase(AppConstants.PERSON_TYPE_INFANT))
					vecPassengerInfoPersonDO.add(passengerInfoPersonDO);
			}
			for (final HalaDO halaDO : vecHalaDOs) {
				if (halaDO.vecAirportServiceDOs != null
						&& halaDO.vecAirportServiceDOs.size() > 0) {

					LinearLayout llBaggageChild = (LinearLayout) layoutInflater
							.inflate(R.layout.personalise_item, null);

					llHalaMain.addView(llBaggageChild);

					TextView tvPersonalTripItemSource = (TextView) llBaggageChild
							.findViewById(R.id.tvPersonalTripDetailsItemSource);

					TextView tvPersonalTripItemDest = (TextView) llBaggageChild
							.findViewById(R.id.tvPersonalTripDetailsItemDest);

					TextView tvPersonalTripItemName = (TextView) llBaggageChild
							.findViewById(R.id.tvPersonalTripItemName);

					TextView tvPersonalTripItemValue = (TextView) llBaggageChild
							.findViewById(R.id.tvPersonalTripItemValue);
					tvPersonalTripItemName.setTypeface(typefaceOpenSansRegular);
					tvPersonalTripItemValue.setTypeface(typefaceOpenSansRegular);

					tvPersonalTripItemName.setText(R.string.airportservice);

					String strValues = "";

					for (int j = 0; j < vecPassengerInfoPersonDO.size(); j++) {

						final PassengerInfoPersonDO passengerInfoPersonDO = vecPassengerInfoPersonDO.get(j);

						for (int k = 0; k < AppConstants.bookingFlightDO.vecSSRRequests.size(); k++) {
							if (AppConstants.bookingFlightDO.vecSSRRequests.get(k).flightNumber
									.equalsIgnoreCase(halaDO.flightSegmentDO.flightNumber)
									&& AppConstants.bookingFlightDO.vecSSRRequests.get(k).travelerRefNumberRPHList
									.equalsIgnoreCase(passengerInfoPersonDO.travelerRefNumberRPHList)) {
								if (TextUtils.isEmpty(strValues))
									strValues = AppConstants.bookingFlightDO.vecSSRRequests.get(k).ssrName;
								else
									strValues = strValues + ", " + AppConstants.bookingFlightDO.vecSSRRequests.get(k).ssrName;
							}
						}
					}
					if (!TextUtils.isEmpty(strValues))
						tvPersonalTripItemValue.setText(strValues);
					else
						tvPersonalTripItemValue.setText(R.string.NoHala);

					if (AppConstants.allAirportNamesDO != null
							&& AppConstants.allAirportNamesDO.vecAirport != null
							&& AppConstants.allAirportNamesDO.vecAirport.size() > 0) {
						String sourceName = halaDO.flightSegmentDO.departureAirportCode, originName = halaDO.flightSegmentDO.arrivalAirportCode;

						tvPersonalTripItemSource.setText(sourceName);
						tvPersonalTripItemDest.setText(originName);

					} else {
						tvPersonalTripItemSource.setText(halaDO.flightSegmentDO.departureAirportCode);
						tvPersonalTripItemDest.setText(halaDO.flightSegmentDO.arrivalAirportCode);
					}
				}
			}
		} else
			llHalaTotal.setVisibility(View.GONE);
//			addNoDataAvailable(getString(R.string.NoHala), llHalaMain, false);
	}

	private void addSeatItems(AirSeatMapDO airSeatMapDOData) {
		llSeatMain.removeAllViews();
		llSeatMain.setVisibility(View.VISIBLE);

		if (airSeatMapDOData != null && airSeatMapDOData.vecSeatMapDOs != null
				&& airSeatMapDOData.vecSeatMapDOs.size() > 0) {
			AirSeatMapDO airSeatMapDO = new AirSeatMapDO();
			airSeatMapDO.vecSeatMapDOs = new Vector<SeatMapDO>();
			airSeatMapDO.vecSeatMapDOs.clear();

			airSeatMapDO.echoToken = airSeatMapDOData.echoToken;
			airSeatMapDO.primaryLangID = airSeatMapDOData.primaryLangID;
			airSeatMapDO.sequenceNmbr = airSeatMapDOData.sequenceNmbr;
			airSeatMapDO.transactionIdentifier = airSeatMapDOData.transactionIdentifier;
			airSeatMapDO.version = airSeatMapDOData.version;

			if (airSeatMapDOData.vecSeatMapDOs.size() > 0)
				for (int i = 0; i < vecFlights.size(); i++) {
					for (int j = 0; j < airSeatMapDOData.vecSeatMapDOs.size(); j++) {
						if (vecFlights.get(i).flightNumber
								.equalsIgnoreCase(airSeatMapDOData.vecSeatMapDOs
										.get(j).flightSegmentDO.flightNumber)) {
							airSeatMapDO.vecSeatMapDOs
									.add(airSeatMapDOData.vecSeatMapDOs.get(j));
						}
					}
				}

			Boolean returnFlag = false;
			if (airSeatMapDO.vecSeatMapDOs.size() > 0) {
				Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDO = new Vector<PassengerInfoPersonDO>();
				for (PassengerInfoPersonDO passengerInfoPersonDO : AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO) {
					if (!passengerInfoPersonDO.persontype.equalsIgnoreCase(AppConstants.PERSON_TYPE_INFANT))
						vecPassengerInfoPersonDO.add(passengerInfoPersonDO);
				}
				int returnSize = airSeatMapDO.vecSeatMapDOs.size();
				for (int i = 0; i < airSeatMapDO.vecSeatMapDOs.size(); i++) {
					final SeatMapDO seatMapDO = airSeatMapDO.vecSeatMapDOs.get(i);

					if (seatMapDO.cabinType != "") {

						LinearLayout llItemChild = (LinearLayout) layoutInflater.inflate(R.layout.personalise_item, null);

						llSeatMain.addView(llItemChild);

						TextView tvPersonalTripItemSource = (TextView) llItemChild
								.findViewById(R.id.tvPersonalTripDetailsItemSource);

						TextView tvPersonalTripItemDest = (TextView) llItemChild
								.findViewById(R.id.tvPersonalTripDetailsItemDest);

						TextView tvPersonalTripItemName = (TextView) llItemChild
								.findViewById(R.id.tvPersonalTripItemName);

						TextView tvPersonalTripItemValue = (TextView) llItemChild
								.findViewById(R.id.tvPersonalTripItemValue);

						ImageView ivFlightType = (ImageView) llItemChild
								.findViewById(R.id.ivFlightType);
						tvPersonalTripItemName.setTypeface(typefaceOpenSansRegular);
						tvPersonalTripItemValue.setTypeface(typefaceOpenSansRegular);
//					tvPersonalTripItemName.setText(R.string.Seat);  //===================
						tvPersonalTripItemName.setText(R.string.SeatSelection);

						String strValues = "";
						if (seatMapDO != null && seatMapDO.vecAirRowDOs != null
								&& seatMapDO.vecAirRowDOs.size() > 0) {

							for (int j = 0; j < vecPassengerInfoPersonDO.size(); j++) {

								final PassengerInfoPersonDO passengerInfoPersonDO = vecPassengerInfoPersonDO.get(j);

//							if(AppConstants.bookingFlightDO.vecSeatRequestDOs != null
//									&& AppConstants.bookingFlightDO.vecSeatRequestDOs.size() == airSeatMapDO.vecSeatMapDOs.size()*vecPassengerInfoPersonDO.size())
								if (AppConstants.bookingFlightDO.vecSeatRequestDOs != null) {
									for (int x = 0; x < AppConstants.bookingFlightDO.vecSeatRequestDOs.size(); x++) {
										RequestDO requestDO = AppConstants.bookingFlightDO.vecSeatRequestDOs.get(x);

										if (requestDO.flightNumber.equalsIgnoreCase(seatMapDO.flightSegmentDO.flightNumber)
												&& requestDO.travelerRefNumberRPHList.equalsIgnoreCase(passengerInfoPersonDO.travelerRefNumberRPHList)
												&& !requestDO.seatNumber.equalsIgnoreCase("")) {
											if (TextUtils.isEmpty(strValues))
												strValues = requestDO.seatNumber;
											else
												strValues = strValues + ", " + requestDO.seatNumber;
										}
									}
								}
							}
						}
						if (!TextUtils.isEmpty(strValues))
							tvPersonalTripItemValue.setText(strValues);
						else
							tvPersonalTripItemValue.setText(R.string.NA);

						if (AppConstants.allAirportNamesDO != null
								&& AppConstants.allAirportNamesDO.vecAirport != null
								&& AppConstants.allAirportNamesDO.vecAirport.size() > 0) {
							String sourceName = seatMapDO.flightSegmentDO.departureAirportCode, originName = seatMapDO.flightSegmentDO.arrivalAirportCode;

							tvPersonalTripItemSource.setText(sourceName);
							tvPersonalTripItemDest.setText(originName);

//						if(i==0)
//							i++;
//						else{
//							if(tv_baggae_textname.getText().toString().equalsIgnoreCase("قبل حجز الأمتعة")){
//							ivFlightType.setBackgroundResource(R.drawable.flight_oneway);
//							}else{
//							ivFlightType.setBackgroundResource(R.drawable.flight_return);
//							}
//						}

							if (!checkLangArabic())

							{

								if (returnSize > airSeatMapDO.vecSeatMapDOs.size() / 2) {
									ivFlightType.setImageResource(R.drawable.flight_oneway);
								} else {
									ivFlightType.setImageResource(R.drawable.flight_return);
								}
								returnSize--;
							} else {
								if (returnSize > airSeatMapDO.vecSeatMapDOs.size() / 2) {
									ivFlightType.setImageResource(R.drawable.flight_return);
								} else {
									ivFlightType.setImageResource(R.drawable.flight_oneway);
								}
								returnSize--;
							}

						/*if(returnFlag){

							if(tv_baggae_textname.getText().toString().equalsIgnoreCase("قبل حجز الأمتعة")){
								ivFlightType.setBackgroundResource(R.drawable.flight_oneway);
								}else{
								ivFlightType.setBackgroundResource(R.drawable.flight_return);
								}
						}
						else
						   returnFlag=true;*/

						} else {
							tvPersonalTripItemSource.setText(seatMapDO.flightSegmentDO.departureAirportCode);
							tvPersonalTripItemDest.setText(seatMapDO.flightSegmentDO.arrivalAirportCode);
						}
					}
				}
			}
		} else
			addNoDataAvailable(getString(R.string.NoSeatMap), llSeatMain, false);
	}

	private void addMealItems(AirMealDetailsDO airMealDetailsDOData) {

		llMealMain.removeAllViews();
		llMealMain.setVisibility(View.VISIBLE);

		if (airMealDetailsDOData != null
				&& airMealDetailsDOData.vecMealDetailsDOs != null) {
			AirMealDetailsDO airMealDetailsDO = new AirMealDetailsDO();
			airMealDetailsDO.vecMealDetailsDOs = new Vector<MealDetailsDO>();
			airMealDetailsDO.vecMealDetailsDOs.clear();

			airMealDetailsDO.echoToken = airMealDetailsDOData.echoToken;
			airMealDetailsDO.primaryLangID = airMealDetailsDOData.primaryLangID;
			airMealDetailsDO.sequenceNmbr = airMealDetailsDOData.sequenceNmbr;
			airMealDetailsDO.transactionIdentifier = airMealDetailsDOData.transactionIdentifier;
			airMealDetailsDO.version = airMealDetailsDOData.version;

			for (int i = 0; i < vecFlights.size(); i++) {
				for (int j = 0; j < airMealDetailsDOData.vecMealDetailsDOs
						.size(); j++) {
					if (vecFlights.get(i).flightNumber.equalsIgnoreCase(
							airMealDetailsDOData.vecMealDetailsDOs.get(j).flightSegmentDO.flightNumber)) {
						airMealDetailsDO.vecMealDetailsDOs.add(airMealDetailsDOData.vecMealDetailsDOs.get(j));
					}
				}
			}

			Boolean returnFlag = false;
			if (airMealDetailsDO.vecMealDetailsDOs.size() > 0) {
				Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDO = new Vector<PassengerInfoPersonDO>();
				for (PassengerInfoPersonDO passengerInfoPersonDO : AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO) {
					if (!passengerInfoPersonDO.persontype.equalsIgnoreCase(AppConstants.PERSON_TYPE_INFANT))
						vecPassengerInfoPersonDO.add(passengerInfoPersonDO);
				}

				int returnSize = airSeatMapDO.vecSeatMapDOs.size();
				for (int i = 0; i < airMealDetailsDO.vecMealDetailsDOs.size(); i++) {

					final MealDetailsDO mealDetailsDO = airMealDetailsDO.vecMealDetailsDOs.get(i);

					if (mealDetailsDO != null
							&& mealDetailsDO.vecMealCategoriesDO != null
							&& mealDetailsDO.vecMealCategoriesDO.size() > 0) {

						LinearLayout llItemChild = (LinearLayout) layoutInflater.inflate(R.layout.personalise_item, null);

						llMealMain.addView(llItemChild);

						TextView tvPersonalTripItemSource = (TextView) llItemChild
								.findViewById(R.id.tvPersonalTripDetailsItemSource);

						TextView tvPersonalTripItemDest = (TextView) llItemChild
								.findViewById(R.id.tvPersonalTripDetailsItemDest);

						TextView tvPersonalTripItemName = (TextView) llItemChild
								.findViewById(R.id.tvPersonalTripItemName);

						TextView tvPersonalTripItemValue = (TextView) llItemChild
								.findViewById(R.id.tvPersonalTripItemValue);
						ImageView ivFlightType = (ImageView) llItemChild
								.findViewById(R.id.ivFlightType);
						tvPersonalTripItemName.setTypeface(typefaceOpenSansRegular);
						tvPersonalTripItemValue.setTypeface(typefaceOpenSansRegular);

						tvPersonalTripItemName.setText(R.string.MealSelection);

						String strValues = "";

						if (mealDetailsDO.vecMealRequestDOs == null) {
							mealDetailsDO.vecMealRequestDOs = new Vector<Vector<RequestDO>>();
						}

						for (int j = 0; j < vecPassengerInfoPersonDO.size(); j++) {

//							PassengerInfoPersonDO passengerInfoPersonDO = vecPassengerInfoPersonDO.get(j);

							Vector<RequestDO> vecRequestDOs = new Vector<RequestDO>();
							if (mealDetailsDO.vecMealRequestDOs.size() > j
									&& mealDetailsDO.vecMealRequestDOs.get(j) != null
									&& mealDetailsDO.vecMealRequestDOs.get(j).size() > 0) {
								vecRequestDOs = mealDetailsDO.vecMealRequestDOs.get(j);
							} else {
								vecRequestDOs = new Vector<RequestDO>();
								mealDetailsDO.vecMealRequestDOs.add(vecRequestDOs);
							}
							if (vecRequestDOs != null && vecRequestDOs.size() > 0) {
								for (int k = 0; k < vecRequestDOs.size(); k++) {
									RequestDO requestDO = vecRequestDOs.get(k);

									if (TextUtils.isEmpty(strValues))
										strValues = requestDO.mealQuantity + " " + requestDO.mealName;
									else
										strValues = strValues + ", \n" + requestDO.mealQuantity + " " + requestDO.mealName;
								}
							}
						}

						if (!TextUtils.isEmpty(strValues))
							tvPersonalTripItemValue.setText(strValues);
						else
							tvPersonalTripItemValue.setText(R.string.NoMeals);

						if (AppConstants.allAirportNamesDO != null
								&& AppConstants.allAirportNamesDO.vecAirport != null
								&& AppConstants.allAirportNamesDO.vecAirport.size() > 0) {

							String sourceName = mealDetailsDO.flightSegmentDO.departureAirportCode, originName = mealDetailsDO.flightSegmentDO.arrivalAirportCode;
							tvPersonalTripItemSource.setText(sourceName);
							tvPersonalTripItemDest.setText(originName);

//							if(i==0)
//								i++;
//							else{
//								if(tv_baggae_textname.getText().toString().equalsIgnoreCase("قبل حجز الأمتعة")){
//								ivFlightType.setBackgroundResource(R.drawable.flight_oneway);
//								}else{
//								ivFlightType.setBackgroundResource(R.drawable.flight_return);
//								}
//							}


							/*if(returnFlag){

								if(tv_baggae_textname.getText().toString().equalsIgnoreCase("قبل حجز الأمتعة")){
									ivFlightType.setBackgroundResource(R.drawable.flight_oneway);
									}else{
									ivFlightType.setBackgroundResource(R.drawable.flight_return);
									}
							}
							else
							   returnFlag=true;*/
							if (!checkLangArabic()) {
								if (returnSize > airSeatMapDO.vecSeatMapDOs.size() / 2) {
									ivFlightType.setImageResource(R.drawable.flight_oneway);
								} else {
									ivFlightType.setImageResource(R.drawable.flight_return);
								}
								returnSize--;
							} else {
								if (returnSize > airSeatMapDO.vecSeatMapDOs.size() / 2) {
									ivFlightType.setImageResource(R.drawable.flight_return);
								} else {
									ivFlightType.setImageResource(R.drawable.flight_oneway);
								}
								returnSize--;
							}


						} else {
							tvPersonalTripItemSource.setText(mealDetailsDO.flightSegmentDO.departureAirportCode);
							tvPersonalTripItemDest.setText(mealDetailsDO.flightSegmentDO.arrivalAirportCode);
						}
					}
				}
			}
		} else
			addNoDataAvailable(getString(R.string.NoMeals), llMealMain, false);
	}

	private void addtravelinsuranceItems() {

		llInsuranceMain.removeAllViews();

		if (AppConstants.insuranceQuoteDO != null) {
			LinearLayout llItemChild = (LinearLayout) layoutInflater.inflate(R.layout.personalise_item, null);

			llInsuranceMain.addView(llItemChild);

			LinearLayout llPersonalTripItemDataSource = (LinearLayout) llItemChild
					.findViewById(R.id.llPersonalTripItemDataSource);

			TextView tvPersonalTripItemName = (TextView) llItemChild.findViewById(R.id.tvPersonalTripItemName);

			TextView tvPersonalTripItemValue = (TextView) llItemChild.findViewById(R.id.tvPersonalTripItemValue);
			tvPersonalTripItemName.setTypeface(typefaceOpenSansRegular);
			tvPersonalTripItemValue.setTypeface(typefaceOpenSansRegular);
			tvPersonalTripItemName.setText(R.string.travelinsurance);
			//**************************remove margin
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 0, 0, 0);
			tvPersonalTripItemName.setLayoutParams(params);

			llPersonalTripItemDataSource.setVisibility(View.GONE);

			String strValues = "";

			if (AppConstants.bookingFlightDO.vecInsrRequestDOs != null
					&& AppConstants.bookingFlightDO.vecInsrRequestDOs.size() > 0) {

				RequestDO requestDO = AppConstants.bookingFlightDO.vecInsrRequestDOs.get(0);
				strValues = requestDO.policyAmount;
			}
			if (!TextUtils.isEmpty(strValues)) {
				float tempInsuranceValue = Float.parseFloat(strValues + "") * Float.parseFloat(AppConstants.currencyConversionFactor + "");
				tvPersonalTripItemValue.setText(AppConstants.CurrencyCodeAfterExchange + " " + MathUtils.round(Double.parseDouble(tempInsuranceValue + ""), 2));
			} else
				tvPersonalTripItemValue.setText(R.string.NoInsurance);
		} else
			addNoDataAvailable(getString(R.string.NoInsurance), llInsuranceMain, false);
	}

	private void callSeatsService() {
		showLoader("");
		Vector<FlightSegmentDO> vecFlightSegmentDOs = new Vector<FlightSegmentDO>();

		if (!isManageBook) {
			if (AppConstants.bookingFlightDO.myODIDOOneWay != null)
				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size(); i++) {
					vecFlightSegmentDOs
							.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
									.get(i).vecFlightSegmentDOs);
				}
			if (AppConstants.bookingFlightDO.myODIDOReturn != null)
				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size(); i++) {
					vecFlightSegmentDOs
							.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
									.get(i).vecFlightSegmentDOs);
				}

			for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
				vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
			}
		} else {
			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size(); i++) {
				vecFlightSegmentDOs
						.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
								.get(i).vecFlightSegmentDOs);
			}
			for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
				vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
			}
		}

		if (new CommonBL(PersonalizeyourTripActivityNew.this,
				PersonalizeyourTripActivityNew.this).getAirSeatMap(
				AppConstants.bookingFlightDO.requestParameterDO,
				vecFlightSegmentDOs)) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	private void callMealsService() {
		showLoader("");
		Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();
		if (!isManageBook) {
			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size(); i++) {
				vecOriginDestinationOptionDOs
						.add(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.get(i));
			}
			if (AppConstants.bookingFlightDO.myODIDOReturn != null
					&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs != null
					&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0)
				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
						.size(); i++) {
					vecOriginDestinationOptionDOs
							.add(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.get(i));
				}
		} else {
			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
					.size(); i++) {
				vecOriginDestinationOptionDOs
						.add(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.get(i));
			}
		}

		if (vecOriginDestinationOptionDOs.size() > 0
				&& new CommonBL(PersonalizeyourTripActivityNew.this,
				PersonalizeyourTripActivityNew.this).getAirMealDetails(
				AppConstants.bookingFlightDO.requestParameterDO,
				vecOriginDestinationOptionDOs)) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	private void callInsuranceService() {
		showLoader("");
		String travelType = "";
		if (AppConstants.bookingFlightDO.myBookFlightDO != null)
			travelType = AppConstants.bookingFlightDO.myBookFlightDO.travelType;

		if (!TextUtils.isEmpty(travelType)
				&& AppConstants.bookingFlightDO.myBookFlightDOReturn != null
				&& !AppConstants.bookingFlightDO.myBookFlightDOReturn.travelType.equalsIgnoreCase(""))
			travelType = AppConstants.TRAVEL_TYPE_RETURN;
		else
			travelType = AppConstants.TRAVEL_TYPE_ONEWAY;

		FlightSegmentDO flightSegmentDO = new FlightSegmentDO();
		int sizeReturnFlt = 0, sizeReturnFltOrig = 0, sizeReturnFltArvTime = 0, sizeReturnFltArvAirPortCode = 0;

		flightSegmentDO.departureDateTime = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
				.get(0).vecFlightSegmentDOs.get(0).departureDateTime;
		flightSegmentDO.departureAirportCode = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
				.get(0).vecFlightSegmentDOs.get(0).departureAirportCode;

		if (AppConstants.bookingFlightDO.myODIDOOneWay != null
				&& AppConstants.bookingFlightDO.myODIDOReturn == null) {
			sizeReturnFlt = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size();
			sizeReturnFltArvTime = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
					.get(sizeReturnFlt - 1).vecFlightSegmentDOs.size();
			flightSegmentDO.arrivalDateTime = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
					.get(sizeReturnFlt - 1).vecFlightSegmentDOs
					.get(sizeReturnFltArvTime - 1).arrivalDateTime;
			sizeReturnFltArvAirPortCode = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
					.get(sizeReturnFlt - 1).vecFlightSegmentDOs.size();
			flightSegmentDO.arrivalAirportCode = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
					.get(sizeReturnFlt - 1).vecFlightSegmentDOs
					.get(sizeReturnFltArvAirPortCode - 1).arrivalAirportCode;
		} else {

			if (AppConstants.bookingFlightDO.myODIDOReturn != null) {
				sizeReturnFlt = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size();
				sizeReturnFltArvTime = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
						.get(sizeReturnFlt - 1).vecFlightSegmentDOs.size();
				flightSegmentDO.arrivalDateTime = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
						.get(sizeReturnFlt - 1).vecFlightSegmentDOs
						.get(sizeReturnFltArvTime - 1).arrivalDateTime;
				sizeReturnFltOrig = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size();
				sizeReturnFltArvAirPortCode = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
						.get(sizeReturnFlt - 1).vecFlightSegmentDOs.size();
				flightSegmentDO.arrivalAirportCode = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
						.get(sizeReturnFltOrig - 1).vecFlightSegmentDOs
						.get(sizeReturnFltArvAirPortCode - 1).arrivalAirportCode;
			}
		}

		if (new CommonBL(PersonalizeyourTripActivityNew.this, PersonalizeyourTripActivityNew.this)
				.getInsuranceQuote(
						AppConstants.bookingFlightDO.requestParameterDO,
						travelType,
						AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO,
						flightSegmentDO)) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	private void callHalaServiceOneWay() {
		showLoader("");
		isHalaService = false;
		Vector<FlightSegmentDO> vecFlightSegmentDOs = new Vector<FlightSegmentDO>();

		if (AppConstants.bookingFlightDO.myODIDOOneWay != null)
			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
					.size(); i++) {
				vecFlightSegmentDOs
						.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
								.get(i).vecFlightSegmentDOs);
			}
		for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
			vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
		}
		if (new CommonBL(PersonalizeyourTripActivityNew.this,
				PersonalizeyourTripActivityNew.this).getHalaReq(
				AppConstants.bookingFlightDO.requestParameterDO,
				vecFlightSegmentDOs)) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	private void callHalaServiceReturn() {
		showLoader("");
		isHalaService = true;
		if (AppConstants.bookingFlightDO.myODIDOReturn != null
				&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0) {
			Vector<FlightSegmentDO> vecFlightSegmentDOs = new Vector<FlightSegmentDO>();

			if (AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs != null
					&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
					.size() > 0)
				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
						.size(); i++) {
					vecFlightSegmentDOs
							.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
									.get(i).vecFlightSegmentDOs);
				}
			for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
				vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
			}
			if (new CommonBL(PersonalizeyourTripActivityNew.this,
					PersonalizeyourTripActivityNew.this).getHalaReq(
					AppConstants.bookingFlightDO.requestParameterDO,
					vecFlightSegmentDOs)) {
			} else {
				hideLoader();
				showCustomDialog(this, getString(R.string.Alert),
						getString(R.string.InternetProblem),
						getString(R.string.Ok), "", "");
			}
		} else
			hideLoader();
	}

	@Override
	public void dataRetreived(Response data) {
		if (data != null) {
			switch (data.method) {
				case AIR_SEAT_DETAILS:

					if (!data.isError) {
						airSeatMapDO = (AirSeatMapDO) data.data;
						AppConstants.AirSeatMapDO = airSeatMapDO;
						if (!airSeatMapDO.transactionIdentifier.equalsIgnoreCase(""))
							AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airSeatMapDO.transactionIdentifier;
						callMealsService();
					} else if (data.data instanceof String) {
						if (((String) data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg))) {
							hideLoader();
							showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
						} else callMealsService();
					} else
						callMealsService();
					break;
				case AIR_MEAL_DETAILS:

					if (!data.isError) {
						airMealDetailsDO = (AirMealDetailsDO) data.data;
						AppConstants.airMealDetailsDO = airMealDetailsDO;
						if (!airMealDetailsDO.transactionIdentifier.equalsIgnoreCase(""))
							AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airMealDetailsDO.transactionIdentifier;
						if (airMealDetailsDO != null
								&& airMealDetailsDO.vecMealDetailsDOs != null
								&& airMealDetailsDO.vecMealDetailsDOs.size() > 0)
							for (int i = 0; i < airMealDetailsDO.vecMealDetailsDOs.size(); i++) {
								MealDetailsDO mealDetailsDO = airMealDetailsDO.vecMealDetailsDOs.get(i);
								mealDetailsDO.vecMealCategoriesDO = new Vector<MealCategoriesDO>();
								if (mealDetailsDO.vecMealsDO != null && mealDetailsDO.vecMealsDO.size() > 0)
									for (MealDO mealDO : mealDetailsDO.vecMealsDO) {
										if (!mealDetailsDO.vecMealcategoryNames
												.contains(mealDO.mealCategoryCode)) {
											mealDetailsDO.vecMealcategoryImageUrls
													.add(mealDO.mealImageLink);
											mealDetailsDO.vecMealcategoryNames
													.add(mealDO.mealCategoryCode);
										}
									}
								if (mealDetailsDO.vecMealcategoryNames != null && mealDetailsDO.vecMealcategoryNames.size() > 0)
									for (int x = 0; x < mealDetailsDO.vecMealcategoryNames.size(); x++) {
										MealCategoriesDO mealCategoriesDO = new MealCategoriesDO();
										mealCategoriesDO.mealCategory = mealDetailsDO.vecMealcategoryNames.get(x);
										mealCategoriesDO.vecMealsDO = new Vector<MealDO>();
										if (mealDetailsDO.vecMealsDO != null && mealDetailsDO.vecMealsDO.size() > 0)
											for (MealDO mealDO : mealDetailsDO.vecMealsDO) {
												if (mealDetailsDO.vecMealcategoryNames.get(x).equalsIgnoreCase(
														mealDO.mealCategoryCode))
													mealCategoriesDO.vecMealsDO.add(mealDO);
											}
										mealDetailsDO.vecMealCategoriesDO.add(mealCategoriesDO);
									}
							}

						if (!isManageBook)
							callInsuranceService();
						else if (AppConstants.bookingFlightDO.myODIDOOneWay != null)
							callHalaServiceOneWay();
						else
							hideLoader();
					} else {
						if (data.data instanceof String) {
							if (((String) data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg))) {
								hideLoader();
								showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
							} else {
								if (!isManageBook)
									callInsuranceService();
								else if (AppConstants.bookingFlightDO.myODIDOOneWay != null)
									callHalaServiceOneWay();
								else
									hideLoader();
							}
						}
					}
					break;
				case INSURANCE_QUOTE:

					if (!data.isError) {
						insuranceQuoteDO = (InsuranceQuoteDO) data.data;
						AppConstants.insuranceQuoteDO = insuranceQuoteDO;
						if (!insuranceQuoteDO.transactionIdentifier.equalsIgnoreCase(""))
							AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = insuranceQuoteDO.transactionIdentifier;
						callHalaServiceOneWay();
					} else {
						if (data.data instanceof String) {
							if (((String) data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg))) {
								hideLoader();
								showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
							} else {
								callHalaServiceOneWay();
							}
						}
					}

					break;
				case HALA_REQ:

					if (!data.isError) {
						AirHalaDO airHalaDO = (AirHalaDO) data.data;
						if (airHalaDO != null && airHalaDO.vecHalaDOs != null
								&& airHalaDO.vecHalaDOs.size() > 0) {
							vecHalaDOs.add(airHalaDO.vecHalaDOs.get(0));
						}
						if (!isHalaService && AppConstants.bookingFlightDO.myODIDOReturn != null) {
							callHalaServiceReturn();
						} else {
							AppConstants.vecHalaDOs = vecHalaDOs;
//						if(AppConstants.vecHalaDOs != null && AppConstants.vecHalaDOs.size() > 0)
//							llHalaTotal.setVisibility(View.VISIBLE);
//						else
//							llHalaTotal.setVisibility(View.GONE);
							hideLoader();
						}
					} else {
						llHalaTotal.setVisibility(View.GONE);
						if (data.data instanceof String) {
							if (((String) data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg))) {
								hideLoader();
								showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
							} else {
								if (!isHalaService && AppConstants.bookingFlightDO.myODIDOReturn != null) {
									callHalaServiceReturn();
								} else
									hideLoader();
							}
						} else
							hideLoader();
					}
					break;

				default:
					break;
			}
		} else
			hideLoader();
	}
	@Override
	public void onButtonYesClick(String from) {
		super.onButtonYesClick(from);
//		if(from.equalsIgnoreCase(AppConstants.INTERNET_PROBLEM))
//			clickHome();
	}

	protected void moveToBookingSummaryActivity() {
		Intent in = new Intent(PersonalizeyourTripActivityNew.this, BookingSummaryActivity.class);
		in.putExtra(AppConstants.AIRPORTS_PRICE_TOTAL, airPriceQuoteDOTotal);
		startActivity(in);
	}

	private void moveToNextPageDetails(int mSelectionNo) {
		Intent in = null;
		int reqCode = 0;
		switch (mSelectionNo) {
			case REQ_CODE_BAGGAGE:
				in = new Intent(PersonalizeyourTripActivityNew.this, PersonaliseBaggageDetailsActivity.class);
				in.putExtra(AppConstants.IS_FROM_PERSONALIZED_TRIP, true);
				if (isManageBook && isAddExtras) {
					in.putExtra("MANAGE_BOOKING", true);
					in.putExtra("ADD_EXTRA", true);
				}
				reqCode = REQ_CODE_BAGGAGE;
				break;
			case REQ_CODE_SEAT:
				in = new Intent(PersonalizeyourTripActivityNew.this, PersonaliseSeatDetailsActivity.class);
				reqCode = REQ_CODE_SEAT;
				break;
			case REQ_CODE_INSURANCE:
				in = new Intent(PersonalizeyourTripActivityNew.this, PersonaliseInsuranceDetailsActivity.class);
				reqCode = REQ_CODE_INSURANCE;
				break;
			case REQ_CODE_MEAL:
				in = new Intent(PersonalizeyourTripActivityNew.this, PersonaliseMealDetailsActivity.class);
				reqCode = REQ_CODE_MEAL;
				break;
			case REQ_CODE_SSL:
				in = new Intent(PersonalizeyourTripActivityNew.this, PersonaliseSSLServiceDetailsActivity.class);
				reqCode = REQ_CODE_SSL;
				break;
			default:
				break;
		}

		in.putExtra(AppConstants.AIRPORTS_PRICE_TOTAL, airPriceQuoteDOTotal);
		startActivityForResult(in, reqCode);
	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent intentData) {
		super.onActivityResult(reqCode, resCode, intentData);
		if (reqCode == REQ_CODE_BAGGAGE && resCode == RESULT_OK) {
			if (intentData != null && intentData.getData() != null) {
				if (intentData.hasExtra(AppConstants.AIRPORTS_PRICE_TOTAL))
					airPriceQuoteDOTotal = (AirPriceQuoteDO) intentData.getExtras()
							.getSerializable(AppConstants.AIRPORTS_PRICE_TOTAL);
			}

			updateData(REQ_CODE_BAGGAGE);
		} else if (reqCode == REQ_CODE_SEAT && resCode == RESULT_OK) {
			if (intentData != null && intentData.getData() != null) {
				if (intentData.hasExtra(AppConstants.AIRPORTS_PRICE_TOTAL))
					airPriceQuoteDOTotal = (AirPriceQuoteDO) intentData.getExtras()
							.getSerializable(AppConstants.AIRPORTS_PRICE_TOTAL);
			}

			updateData(REQ_CODE_SEAT);
		} else if (reqCode == REQ_CODE_INSURANCE && resCode == RESULT_OK) {
			if (intentData != null && intentData.getData() != null) {
				if (intentData.hasExtra(AppConstants.AIRPORTS_PRICE_TOTAL))
					airPriceQuoteDOTotal = (AirPriceQuoteDO) intentData.getExtras()
							.getSerializable(AppConstants.AIRPORTS_PRICE_TOTAL);
			}

			updateData(REQ_CODE_INSURANCE);
		} else if (reqCode == REQ_CODE_MEAL && resCode == RESULT_OK) {
			if (intentData != null && intentData.getData() != null) {
				if (intentData.hasExtra(AppConstants.AIRPORTS_PRICE_TOTAL))
					airPriceQuoteDOTotal = (AirPriceQuoteDO) intentData.getExtras()
							.getSerializable(AppConstants.AIRPORTS_PRICE_TOTAL);
			}

			updateData(REQ_CODE_MEAL);
		} else if (reqCode == REQ_CODE_SSL && resCode == RESULT_OK) {
			if (intentData != null && intentData.getData() != null) {
				if (intentData.hasExtra(AppConstants.AIRPORTS_PRICE_TOTAL))
					airPriceQuoteDOTotal = (AirPriceQuoteDO) intentData.getExtras()
							.getSerializable(AppConstants.AIRPORTS_PRICE_TOTAL);
			}
			updateData(REQ_CODE_SSL);
		}
	}


	private boolean checkisSeatsSelected() {
		boolean isAllSeatsselected = true;


		if (AppConstants.bookingFlightDO.vecSeatRequestDOs != null && AppConstants.bookingFlightDO.vecSeatRequestDOs.size() > 0)
			for (int i = 0; i < AppConstants.bookingFlightDO.vecSeatRequestDOs.size(); i++) {
				if (TextUtils.isEmpty(AppConstants.bookingFlightDO.vecSeatRequestDOs.get(i).seatNumber)) {
					isAllSeatsselected = false;
					break;
				}
			}
		else
			isAllSeatsselected = false;

		return isAllSeatsselected;
	}

	private boolean checkisBaggageSelected() {
		boolean isAllBaggageselected = true;


		if (AppConstants.bookingFlightDO.vecBaggageRequestDOs != null && AppConstants.bookingFlightDO.vecBaggageRequestDOs.size() > 0)
			for (int i = 0; i < AppConstants.bookingFlightDO.vecBaggageRequestDOs.size(); i++) {
				if (TextUtils.isEmpty(AppConstants.bookingFlightDO.vecBaggageRequestDOs.get(i).baggageCode)) {
					isAllBaggageselected = false;
					break;
				}
			}
		else
			isAllBaggageselected = false;

		return isAllBaggageselected;
	}

	private boolean checkisMealsSelected() {


		Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDO = new Vector<PassengerInfoPersonDO>();
		for (PassengerInfoPersonDO passengerInfoPersonDO : AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO) {
			if (!passengerInfoPersonDO.persontype.equalsIgnoreCase(AppConstants.PERSON_TYPE_INFANT))
				vecPassengerInfoPersonDO.add(passengerInfoPersonDO);
		}

		if (AppConstants.airMealDetailsDO != null && AppConstants.airMealDetailsDO.vecMealDetailsDOs != null && AppConstants.airMealDetailsDO.vecMealDetailsDOs.size() > 0) {
			for (int i = 0; i < AppConstants.airMealDetailsDO.vecMealDetailsDOs.size(); i++) {
				if (AppConstants.airMealDetailsDO.vecMealDetailsDOs.get(i).vecMealRequestDOs != null && AppConstants.airMealDetailsDO.vecMealDetailsDOs.get(i).vecMealRequestDOs.size() > 0) {
					for (int j = 0; j < AppConstants.airMealDetailsDO.vecMealDetailsDOs.get(i).vecMealRequestDOs.size() && j < vecPassengerInfoPersonDO.size(); j++) {
						if (AppConstants.airMealDetailsDO.vecMealDetailsDOs.get(i).vecMealRequestDOs.get(j) != null && AppConstants.airMealDetailsDO.vecMealDetailsDOs.get(i).vecMealRequestDOs.get(j).size() > 0) {

						} else
							return false;
					}

				} else {
					return false;
				}
			}
		} else
			return false;


		return true;
	}



	private boolean CheckForBundleValidation()
	{
		boolean isValid = true;
		isValueFair = false;
		isExtraFare = false;
		SeatNotSelected = false;
		mealNotSelected = false;
		baggageNotSelected = false;
//        mealExceeds = false;

		BundledServiceDO bundledServiceDO = null;

		if (!TextUtils.isEmpty(AppConstants.bookingFlightDO.bundleServiceID)) {
			if (AppConstants.bookingFlightDO.myODIDOReturn != null) {
				if (AppConstants.bookingFlightDO.myODIDOReturn.arlBundledServiceDOs != null
						&& AppConstants.bookingFlightDO.myODIDOReturn.arlBundledServiceDOs.size() > 0)
					for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.arlBundledServiceDOs.size(); i++) {
						if (AppConstants.bookingFlightDO.bundleServiceID.equalsIgnoreCase(AppConstants.bookingFlightDO.myODIDOReturn.arlBundledServiceDOs.get(i).bunldedServiceId)) {
							bundledServiceDO = AppConstants.bookingFlightDO.myODIDOReturn.arlBundledServiceDOs.get(i);
							break;
						}

					}
				if (bundledServiceDO != null) {
					if (bundledServiceDO.bundledServiceName.toLowerCase().contains("Value".toLowerCase())) {
						isValueFair = true;
						if(checkisBaggageSelected() && checkisMealsSelected())
							isValid =true;
						else
						{
							isValid=false;
							if(!checkisBaggageSelected())
								baggageNotSelected =true;
							if(!checkisMealsSelected())
								mealNotSelected =true;
						}
					} else if (bundledServiceDO.bundledServiceName.toLowerCase().contains("Extra".toLowerCase())) {
						isExtraFare = true;
						if(checkisBaggageSelected() && checkisSeatsSelected() && checkisMealsSelected())
							isValid =true;
						else
						{
							isValid=false;
							if(!checkisBaggageSelected())
								baggageNotSelected =true;
							if(!checkisSeatsSelected())
								SeatNotSelected = true;
							if(!checkisMealsSelected())
								mealNotSelected =true;
						}
					}

				}
			} else {
				if (AppConstants.bookingFlightDO.myODIDOOneWay.arlBundledServiceDOs != null
						&& AppConstants.bookingFlightDO.myODIDOOneWay.arlBundledServiceDOs.size() > 0) {
					for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.arlBundledServiceDOs.size(); i++) {
						if (AppConstants.bookingFlightDO.bundleServiceID.equalsIgnoreCase(AppConstants.bookingFlightDO.myODIDOOneWay.arlBundledServiceDOs.get(i).bunldedServiceId)) {
							bundledServiceDO = AppConstants.bookingFlightDO.myODIDOOneWay.arlBundledServiceDOs.get(i);
							break;
						}
					}
					if (bundledServiceDO != null) {
						if (bundledServiceDO.bundledServiceName.toLowerCase().contains("Value".toLowerCase())) {
							isValueFair = true;
							if(checkisBaggageSelected() && checkisMealsSelected())
								isValid =true;
							else
							{
								isValid=false;
								if(!checkisBaggageSelected())
									baggageNotSelected =true;
								if(!checkisMealsSelected())
									mealNotSelected =true;
							}
						} else if (bundledServiceDO.bundledServiceName.toLowerCase().contains("Extra".toLowerCase())) {
							isExtraFare = true;
							if (checkisBaggageSelected() && checkisSeatsSelected() && checkisMealsSelected() /*&& checkisMealsExceeds()*/)
								isValid = true;
							else {
								isValid = false;
								if (!checkisBaggageSelected())
									baggageNotSelected = true;
								if (!checkisSeatsSelected())
									SeatNotSelected = true;
								if(!checkisMealsSelected())
									mealNotSelected =true;
							}
						}

					}
				}

			}
		}

		return isValid;
	}
}