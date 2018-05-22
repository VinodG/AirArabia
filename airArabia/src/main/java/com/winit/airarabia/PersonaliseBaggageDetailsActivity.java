package com.winit.airarabia;

import java.util.ArrayList;
import java.util.Vector;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.winit.airarabia.adapters.SpinnerBaggageAdapter;
import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirBaggageDetailsDO;
import com.winit.airarabia.objects.AirPriceQuoteDO;
import com.winit.airarabia.objects.BaggageDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.PassengerInfoPersonDO;
import com.winit.airarabia.objects.RequestDO;
import com.winit.airarabia.webaccess.Response;

public class PersonaliseBaggageDetailsActivity extends BaseActivity implements DataListener{

	private LinearLayout llBaggageDetails,llBaggageMain;
	private TextView tv_selecTiltle;
	private Vector<AirBaggageDetailsDO> vecAirBaggageDetailsDO;
	private boolean isBaggageService = false;
	private AirPriceQuoteDO airPriceQuoteDOTotal;
	private boolean isServiceCalled= false;
	private boolean isFromPersonalizeTrip = false;
	private boolean isManageBook = false;
	private boolean isAddExtras = false;
	private final String DATAFAIL = "DATAFAIL";

	@Override
	public void initilize() {
		tvHeaderTitle.setText(getString(R.string.Select_Baggage));
		initClassMembers();
		if(!isFromPersonalizeTrip){
			AppConstants.selectedOneWayBaggage = new String[10];
			AppConstants.selectedReturnBaggage = new String[10];
			AppConstants.selectedOneWayBaggagePos = new int[10];
			AppConstants.selectedReturnBaggagePos = new int[10];
		}
	}
	private void initClassMembers()
	{
		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(getString(R.string.Continue));

		if(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify != null
				&& AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.size() > 0)
			isManageBook = true;

		airPriceQuoteDOTotal = (AirPriceQuoteDO) getIntent().getExtras().getSerializable(AppConstants.AIRPORTS_PRICE_TOTAL);

		if(getIntent().hasExtra(AppConstants.IS_FROM_PERSONALIZED_TRIP))
			isFromPersonalizeTrip = getIntent().getExtras().getBoolean(AppConstants.IS_FROM_PERSONALIZED_TRIP, false);

		if(getIntent().hasExtra("MANAGE_BOOKING"))
			isManageBook = getIntent().getBooleanExtra("MANAGE_BOOKING", false);
		if(getIntent().hasExtra("ADD_EXTRA"))
			isAddExtras = getIntent().getBooleanExtra("ADD_EXTRA", false);
		
		llBaggageDetails = (LinearLayout) layoutInflater.inflate(R.layout.personalize_trip_details_selection, null);

		llMiddleBase.addView(llBaggageDetails, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

		llBaggageMain    = (LinearLayout) llBaggageDetails.findViewById(R.id.llBaggageMain);
		tv_selecTiltle    = (TextView) llBaggageDetails.findViewById(R.id.tv_selecTiltle);
		//setTypeFaceOpenSansLight(llBaggageDetails);
		setTypeFaceSemiBold(llBaggageDetails);
		if (isFromPersonalizeTrip) {
			tvHeaderTitle.setText(getString(R.string.Baggageselection));	

		}else{
			tvHeaderTitle.setText(getString(R.string.Select_Baggage));
		}
		//		ivmenu.setVisibility(View.INVISIBLE);
		//		lockDrawer("Baggage Selection");
	}
	@Override
	public void bindingControl() {

		vecAirBaggageDetailsDO = new Vector<AirBaggageDetailsDO>();

		if(AppConstants.vecAirBaggageDetailsDO != null && AppConstants.vecAirBaggageDetailsDO.size() > 0)
			addBaggageItems(AppConstants.vecAirBaggageDetailsDO);
		else
			callBaggageServiceOneWay();

		btnSubmitNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				trackEvent("Baggage Selection", AppConstants.ContinueBaggageDetail, "");
				if(AppConstants.bookingFlightDO.vecBaggageRequestDOs != null && AppConstants.bookingFlightDO.vecBaggageRequestDOs.size() > 0)
				{
					ArrayList<String> arlBagCodesReq = new ArrayList<String>();
					for (int i = 0; i < AppConstants.bookingFlightDO.vecBaggageRequestDOs.size(); i++) {
						arlBagCodesReq.add(AppConstants.bookingFlightDO.vecBaggageRequestDOs.get(i).baggageCode);
					}
					if(arlBagCodesReq.contains(getString(R.string.Select)))
						showCustomDialog(PersonaliseBaggageDetailsActivity.this, getString(R.string.Alert), getString(R.string.AlertMsgSelectBaggage), getString(R.string.Ok), "", "NO_BAGGAGE");
					else
					{
						if(isServiceCalled && isFromPersonalizeTrip)
						{
							Intent intenNext = new Intent();
							intenNext.putExtra(AppConstants.AIRPORTS_PRICE_TOTAL, airPriceQuoteDOTotal);
							setResult(RESULT_OK, intenNext);
						}
						else if(isFromPersonalizeTrip)
						{
							Intent intenNext = new Intent();
							intenNext.putExtra(AppConstants.AIRPORTS_PRICE_TOTAL, airPriceQuoteDOTotal);
							setResult(RESULT_OK, intenNext);
						}
						else moveToNextPage();
						finish();
					}
				}
			}
		});
	}
	private void moveToNextPage()
	{
		Intent intenNext = new Intent(PersonaliseBaggageDetailsActivity.this, PersonalizeyourTripActivityNew.class);
		intenNext.putExtra(AppConstants.AIRPORTS_PRICE_TOTAL, airPriceQuoteDOTotal);
		startActivity(intenNext);
	}

	private void addBaggageItems(final Vector<AirBaggageDetailsDO> vecAirBaggageDetailsDO) {

		if (vecAirBaggageDetailsDO != null && vecAirBaggageDetailsDO.size() > 0) {
			AppConstants.bookingFlightDO.vecBaggageRequestDOs.clear();
			Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDO = new Vector<PassengerInfoPersonDO>();
			for (PassengerInfoPersonDO passengerInfoPersonDO : AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO) {
				if (!passengerInfoPersonDO.persontype
						.equalsIgnoreCase(AppConstants.PERSON_TYPE_INFANT))
					vecPassengerInfoPersonDO.add(passengerInfoPersonDO);
			}
			Boolean imageflage=true;
			int bagFlightno = -1;
			for (AirBaggageDetailsDO airBaggageDetailsDO : vecAirBaggageDetailsDO) {
				bagFlightno ++;
				if (airBaggageDetailsDO.vecBaggageDetailDOs != null
						&& airBaggageDetailsDO.vecBaggageDetailDOs.size() > 0
						&& airBaggageDetailsDO.vecBaggageDetailDOs.get(0).vecFlightSegmentDOs.size() > 0) {

					for (int i = 0; i < airBaggageDetailsDO.vecBaggageDetailDOs.size(); i++) {
						Vector<BaggageDO> vecBaggageList = airBaggageDetailsDO.vecBaggageDetailDOs.get(i).vecBaggageDO;
						LinearLayout llBaggageChild = (LinearLayout) layoutInflater
								.inflate(R.layout.personalise_trip_details_item, null);

						LinearLayout llPersonalTripItemData = (LinearLayout) llBaggageChild
								.findViewById(R.id.llPersonalTripItemData);
						LinearLayout llPersonalTripItemMain = (LinearLayout) llBaggageChild
								.findViewById(R.id.llPersonalTripDetailsItemMain);
						final TextView tvPersonalTripItemSource = (TextView) llBaggageChild
								.findViewById(R.id.tvPersonalTripDetailsItemSource);
						ImageView flightLogo = (ImageView) llBaggageChild
								.findViewById(R.id.flightLogo);

						TextView tvPersonalTripItemDest = (TextView) llBaggageChild
								.findViewById(R.id.tvPersonalTripDetailsItemDest);

						tvPersonalTripItemSource.setTypeface(typefaceOpenSansSemiBold);
						tvPersonalTripItemDest.setTypeface(typefaceOpenSansSemiBold);

						if (vecBaggageList != null && vecBaggageList.size() > 0) {

							if(!vecBaggageList.get(0).baggageCode.equalsIgnoreCase(getString(R.string.Select)))
							{
								BaggageDO object = new BaggageDO();
								object.baggageCode = getString(R.string.Select);
								vecBaggageList.add(0,object);
							}
							//							if (vecPassengerInfoPersonDO.size()>3) {
							//								llPersonalTripItemData.setBackgroundResource(R.drawable.bg_white_big);
							//							}else {
							//								llPersonalTripItemData.setBackgroundResource(R.drawable.bg_white_midum);
							//							}

							for (int j = 0; j < vecPassengerInfoPersonDO.size(); j++) {
								LinearLayout llItemSeatSub = (LinearLayout) layoutInflater
										.inflate(R.layout.personalise_trip_details_item_sub_new, null);

								TextView tvPersonalTripItemName = (TextView) llItemSeatSub
										.findViewById(R.id.tvPersonalTripItemName);
								final TextView tvPersonalTripItemAmount = (TextView) llItemSeatSub
										.findViewById(R.id.tvPersonalTripItemAmount);
								//								final TextView tvPersonalTripItemAEDValue = (TextView) llItemSeatSub
								//										.findViewById(R.id.tvPersonalTripItemAEDValue);
								llPersonalTripItemMain.addView(llItemSeatSub);
								tvPersonalTripItemName.setTypeface(typefaceOpenSansSemiBold);
								tvPersonalTripItemAmount.setTypeface(typefaceOpenSansRegular);
								tvPersonalTripItemName.setText(vecPassengerInfoPersonDO.get(j).personfirstname+"\n" +vecPassengerInfoPersonDO.get(j).personlastname);
								tvPersonalTripItemAmount.setVisibility(View.GONE);
								final LinearLayout llPersonalTripItemAmount = (LinearLayout) llItemSeatSub
										.findViewById(R.id.llPersonalTripItemAmount);
								setTypeFaceSemiBold(llItemSeatSub);
								final Spinner spPersonalTripItemAmount = (Spinner) llItemSeatSub
										.findViewById(R.id.spPersonalTripItemAmount);

								Vector<RequestDO> vecBagReq = new Vector<RequestDO>();

								for (int k = 0; k < airBaggageDetailsDO.vecBaggageDetailDOs
										.get(i).vecFlightSegmentDOs.size(); k++) {

									FlightSegmentDO flightSegmentDO = airBaggageDetailsDO.vecBaggageDetailDOs
											.get(i).vecFlightSegmentDOs.get(k);
									RequestDO requestDO = new RequestDO();
									requestDO.flightNumber = flightSegmentDO.flightNumber;

									final PassengerInfoPersonDO passengerInfoPersonDO = vecPassengerInfoPersonDO.get(j);
									requestDO.departureDate = flightSegmentDO.departureDateTime;
									requestDO.travelerRefNumberRPHList = passengerInfoPersonDO.travelerRefNumberRPHList;
									requestDO.flightNumber = flightSegmentDO.flightNumber;
									requestDO.flightRefNumberRPHList = flightSegmentDO.RPH;
									if (vecBaggageList != null&& vecBaggageList.size() > 0)
										requestDO.baggageCode = vecBaggageList.get(0).baggageCode;
									else
										requestDO.baggageCode = "";

									vecBagReq.add(requestDO);

									AppConstants.bookingFlightDO.vecBaggageRequestDOs.add(requestDO);

									llPersonalTripItemAmount.setVisibility(View.VISIBLE);
									spPersonalTripItemAmount.setVisibility(View.VISIBLE);

									//									SPANNABLE WORDTOSPAN = NEW SPANNABLESTRING(GETSTRING(R.STRING.SELECT));
									//								    WORDTOSPAN.SETSPAN(NEW FOREGROUNDCOLORSPAN(GETRESOURCES().GETCOLOR(R.COLOR.TEXT_COLOR)), 0,
									//								            WORDTOSPAN.LENGTH(), SPANNABLE.SPAN_EXCLUSIVE_EXCLUSIVE);
									//								    WORDTOSPAN.SETSPAN(NEW BACKGROUNDCOLORSPAN(COLOR.WHITE), 0,
									//								    		WORDTOSPAN.LENGTH(), SPANNABLE.SPAN_PARAGRAPH);
									//
									//									SPPERSONALTRIPITEMAMOUNT.SETPROMPT(WORDTOSPAN);

									final SpinnerBaggageAdapter adapter = new SpinnerBaggageAdapter(PersonaliseBaggageDetailsActivity.this, vecBaggageList);
									spPersonalTripItemAmount.setAdapter(adapter);
									spPersonalTripItemAmount.setTag(vecBaggageList);
									spPersonalTripItemAmount.setTag(R.string.Address,vecBagReq);

									spPersonalTripItemAmount.setOnItemSelectedListener(new OnItemSelectedListener() {

										@Override
										public void onItemSelected(
												AdapterView<?> arg0, View view,
												int pos, long arg3) {
											Vector<BaggageDO> vecBaggageListSel = (Vector<BaggageDO>)spPersonalTripItemAmount.getTag();
											//											tvPersonalTripItemAEDValue.setText(StringUtils.getStrFloatFromString(vecBaggageListSel
											//													.get(pos).baggageCharge));
											view.findViewById(R.id.ivTic).setVisibility(View.GONE);
											//											tvPersonalTripItemAEDValue.setVisibility(View.GONE);

											Vector<RequestDO> vecReq = (Vector<RequestDO>) spPersonalTripItemAmount.getTag(R.string.Address);

											for (int l = 0; l < vecReq.size(); l++) {

												RequestDO requestDO = vecReq.get(l);
												if (isCancelableLoader) {
													requestDO.baggageCode = vecBaggageListSel.get(pos).baggageCode;
												}else{
													requestDO.baggageCode = vecBaggageListSel.get(pos).baggageCode;
												}

												if(!isFromPersonalizeTrip && pos!=0){
													//														if(count < (vecAirBaggageDetailsDO.size()/2)){
													if(tvPersonalTripItemSource.getText().toString().equalsIgnoreCase(AppConstants.OriginLocation)){
														AppConstants.selectedOneWayBaggagePos[l] = pos;
														AppConstants.selectedOneWayBaggage[l] = arg0.getSelectedItem().toString();
														trackEvent("Baggage Selection", AppConstants.OneWayBaggage, "");
													}else{
														AppConstants.selectedReturnBaggagePos[l] = pos;
														AppConstants.selectedReturnBaggage[l] = arg0.getSelectedItem().toString();
														trackEvent("Baggage Selection", AppConstants.ReturnBaggage, "");
													}
												}
												if(isFromPersonalizeTrip && l==pos){
													if(tvPersonalTripItemSource.getText().toString().equalsIgnoreCase(AppConstants.OriginLocation)){
														spPersonalTripItemAmount.setSelection(AppConstants.selectedOneWayBaggagePos[l]);
														trackEvent("Baggage Selection", AppConstants.OneWayBaggage, "");
													}else{
														spPersonalTripItemAmount.setSelection(AppConstants.selectedReturnBaggagePos[l]);
														trackEvent("Baggage Selection", AppConstants.ReturnBaggage, "");
													}
												}

											}

											adapter.refreshPos(pos);
										}

										@Override
										public void onNothingSelected(
												AdapterView<?> arg0) {}
									});




								}
							}

						} else {
							addNoDataAvailable(getString(R.string.NoBaggage),llPersonalTripItemMain);
						}
						setTypeFaceSemiBold(llBaggageChild);
						llBaggageMain.addView(llBaggageChild);

						if (AppConstants.allAirportNamesDO != null
								&& AppConstants.allAirportNamesDO.vecAirport != null
								&& AppConstants.allAirportNamesDO.vecAirport.size() > 0) {
							String sourceName = airBaggageDetailsDO.vecBaggageDetailDOs
									.get(i).vecFlightSegmentDOs.get(0).departureAirportCode, 
									originName = airBaggageDetailsDO.vecBaggageDetailDOs
									.get(i).vecFlightSegmentDOs
									.get(airBaggageDetailsDO.vecBaggageDetailDOs
											.get(i).vecFlightSegmentDOs
											.size() - 1).arrivalAirportCode;

							tvPersonalTripItemSource.setText(sourceName);
							tvPersonalTripItemSource.setTypeface(typefaceOpenSansSemiBold);
							tvPersonalTripItemDest.setText(originName);
							tvPersonalTripItemDest.setTypeface(typefaceOpenSansSemiBold);
							if(!checkLangArabic())

							{

								if(bagFlightno == 0){
									flightLogo.setImageResource(R.drawable.flight_oneway);
								}else{
									flightLogo.setImageResource(R.drawable.flight_return);
								}
							}
							else
							{
								if(bagFlightno == 0){
									flightLogo.setImageResource(R.drawable.flight_return);
								}else{
									flightLogo.setImageResource(R.drawable.flight_oneway);
								}
							}
							//							if (!imageflage) {
							//								if(tv_selecTiltle.getText().toString().equalsIgnoreCase("اختر أمتعتك")){
							//									flightLogo.setImageResource(R.drawable.flight_oneway);
							//								}
							//								else{
							//									flightLogo.setImageResource(R.drawable.flight_return);
							//								}
							//								imageflage=true;
							//							}else
							//							imageflage=false;
						} else {
							tvPersonalTripItemSource.setText(airBaggageDetailsDO.vecBaggageDetailDOs.get(i).vecFlightSegmentDOs
									.get(0).departureAirportCode);
							tvPersonalTripItemDest.setText(airBaggageDetailsDO.vecBaggageDetailDOs.get(i).vecFlightSegmentDOs
									.get(airBaggageDetailsDO.vecBaggageDetailDOs.get(i).vecFlightSegmentDOs.size() - 1).arrivalAirportCode);

						}
					}
				}
			}
		}
	}

	//	@Override
	//	protected void onResume() {
	//		if(isFromPersonalizeTrip){
	//			for(int i = 0;i<vecAirBaggageDetailsDO.size();i++){
	//			spPersonalTripItemAmount.setSelection(index);
	//			}
	//	        }
	//		
	//		super.onResume();
	//	}
	private void addNoDataAvailable(String strMsg, LinearLayout llSelected) {
		TextView tvNoDataAvailable = (TextView) layoutInflater.inflate(R.layout.no_data_found, null);
		tvNoDataAvailable.setText(strMsg);
		llSelected.addView(tvNoDataAvailable);
	}

	private void callBaggageServiceOneWay() {

		isServiceCalled = true;
		showLoader("");
		Vector<FlightSegmentDO> vecFlightSegmentDOs = new Vector<FlightSegmentDO>();

		if(AppConstants.bookingFlightDO.myODIDOOneWay != null)
			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size(); i++) {
				vecFlightSegmentDOs
				.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
						.get(i).vecFlightSegmentDOs);
			}
		for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
			vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
		}
		if (!airPriceQuoteDOTotal.transactionIdentifier.equalsIgnoreCase(""))
			AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airPriceQuoteDOTotal.transactionIdentifier;
		if (new CommonBL(PersonaliseBaggageDetailsActivity.this,PersonaliseBaggageDetailsActivity.this).getAirBaggageDetails(
				AppConstants.bookingFlightDO.requestParameterDO, "AED", vecFlightSegmentDOs)) {

		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	private void callBaggageServiceReturn() {
		isServiceCalled = true;
		isBaggageService = true;
		if (isManageBook)
			showLoader("");
		if (AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0) {
			Vector<FlightSegmentDO> vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size(); i++) {
				vecFlightSegmentDOs
				.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
						.get(i).vecFlightSegmentDOs);
			}
			for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
				vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
			}
			if (new CommonBL(PersonaliseBaggageDetailsActivity.this,PersonaliseBaggageDetailsActivity.this).getAirBaggageDetails(
					AppConstants.bookingFlightDO.requestParameterDO, "AED",
					vecFlightSegmentDOs)) {
			} else {
				hideLoader();
				showCustomDialog(this, getString(R.string.Alert),
						getString(R.string.InternetProblem),
						getString(R.string.Ok), "", "");
			}
		} else {
			if(vecAirBaggageDetailsDO != null && vecAirBaggageDetailsDO.size() > 0 
					&& vecAirBaggageDetailsDO.get(0).vecBaggageDetailDOs == null)
			{
				addNoDataAvailable(getString(R.string.NoBaggage), llBaggageMain);
			}
			else
				addBaggageItems(vecAirBaggageDetailsDO);
			hideLoader();
		}
	}
	@Override
	public void dataRetreived(Response data) {
		if (data != null) {
			switch (data.method) {
			case AIR_BAGGAGE_DETAILS:
				if (!data.isError)
				{
					llBaggageMain.setVisibility(View.VISIBLE);
					AirBaggageDetailsDO airBaggageDetailsDO = (AirBaggageDetailsDO) data.data;

					if(airBaggageDetailsDO.vecBaggageDetailDOs != null && airBaggageDetailsDO.vecBaggageDetailDOs.size() > 0)
					{
						vecAirBaggageDetailsDO.add(airBaggageDetailsDO);
						AppConstants.vecAirBaggageDetailsDO = vecAirBaggageDetailsDO;
					}

					if (!airBaggageDetailsDO.transactionIdentifier.equalsIgnoreCase(""))
						AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airBaggageDetailsDO.transactionIdentifier;
					if (isBaggageService) {
						if(airBaggageDetailsDO != null && airBaggageDetailsDO.vecBaggageDetailDOs == null)
						{
							if(!TextUtils.isEmpty(airBaggageDetailsDO.ErrorMsg))
								showCustomDialog(PersonaliseBaggageDetailsActivity.this,
										getString(R.string.Alert),
										airBaggageDetailsDO.ErrorMsg,
										getString(R.string.Ok), "", DATAFAIL);
							else
								addNoDataAvailable(getString(R.string.NoBaggage), llBaggageMain);
						}
						else if(vecAirBaggageDetailsDO != null && vecAirBaggageDetailsDO.size() > 0 
								&& vecAirBaggageDetailsDO.get(0).vecBaggageDetailDOs == null)
						{
							if(!TextUtils.isEmpty(airBaggageDetailsDO.ErrorMsg))
								showCustomDialog(PersonaliseBaggageDetailsActivity.this,
										getString(R.string.Alert),
										airBaggageDetailsDO.ErrorMsg,
										getString(R.string.Ok), "", DATAFAIL);
							else
								addNoDataAvailable(getString(R.string.NoBaggage), llBaggageMain);
						}
						else
							addBaggageItems(vecAirBaggageDetailsDO);

						hideLoader();
					} else
					{
						if(vecAirBaggageDetailsDO != null && vecAirBaggageDetailsDO.size() > 0 
								&& vecAirBaggageDetailsDO.get(0).vecBaggageDetailDOs == null)
						{
							if(!TextUtils.isEmpty(airBaggageDetailsDO.ErrorMsg))
								showCustomDialog(PersonaliseBaggageDetailsActivity.this,
										getString(R.string.Alert),
										airBaggageDetailsDO.ErrorMsg,
										getString(R.string.Ok), "", DATAFAIL);
							else
								addNoDataAvailable(getString(R.string.NoBaggage), llBaggageMain);
						}
						if(TextUtils.isEmpty(airBaggageDetailsDO.ErrorMsg) && AppConstants.bookingFlightDO.myODIDOReturn != null)
							callBaggageServiceReturn();
						else
						{
							addBaggageItems(vecAirBaggageDetailsDO);
							hideLoader();
						}
					}
				} else
				{
					if(data.data instanceof String)
					{
						if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
						{
							showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
						}
					}
					hideLoader();
				}
				break;
			}
		}
	}
	@Override
	public void onButtonYesClick(String from) {
		super.onButtonYesClick(from);

		if(from.equalsIgnoreCase("NO_BAGGAGE"))
		{

		}
		else if (from.equalsIgnoreCase(DATAFAIL) && (from.equalsIgnoreCase(AppConstants.INTERNET_PROBLEM)))
			clickHome();
		else finish();
	}

	//	private void showSelBaggagePopup(final Vector<AirBaggageDetailsDO> vecAirBaggageDetailsDO, final TextView tvTitleSel,
	//			final Object obj,
	//			final String objValue, String titleOfPopup) {
	//		LinearLayout ll = (LinearLayout) layoutInflater.inflate(R.layout.popup_titles, null);
	//		TextView tvTitleHeader = (TextView) ll.findViewById(R.id.tvTitleHeader);
	//		tvTitleHeader.setTypeface(typefaceOpenSansSemiBold);
	//		tvTitleHeader.setText(titleOfPopup+"");
	//		final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_No_Title);
	//		dialog.setContentView(ll);
	//		setTypefaceOpenSansRegular(ll);
	//		tvTitleHeader.setTypeface(typefaceOpenSansSemiBold);
	//		LinearLayout llPopupTitleMain = (LinearLayout) ll.findViewById(R.id.llPopupTitleMain);
	//		for (int i = 0; i < vecAirBaggageDetailsDO.size(); i++) {
	//			LinearLayout llTitle = (LinearLayout) layoutInflater.inflate(
	//					R.layout.popup_title_item, null);
	//
	//			final TextView tvTitleItem = (TextView) llTitle.findViewById(R.id.tvTitleItem);
	//			//			tvTitleItem.setTypeface(typeFaceOpenSansLight); Mrs
	//			llPopupTitleMain.addView(llTitle);
	//
	//			tvTitleItem.setText(vecAirBaggageDetailsDO.get(i).vecBaggageDetailDOs);
	//			tvTitleItem.setTag(arrTitle[i]);
	//
	//			if (tvTitleSel.getText().toString().equalsIgnoreCase(arrTitleDesc[i]))
	//				tvTitleItem.setBackgroundColor(resources.getColor(R.color.ash_color));
	//
	//			tvTitleItem.setOnClickListener(new OnClickListener() {
	//
	//				@Override
	//				public void onClick(View v) {
	//
	//					tvTitleSel.setText(tvTitleItem.getText().toString());
	//					tvTitleSel.setTag(tvTitleItem.getTag().toString());
	//
	//					if(!(tvTitleItem.getText().toString()).equalsIgnoreCase("")){
	//
	//						tvTitletemptag.setVisibility(View.VISIBLE);
	//						tvTitleSel.setTypeface(typefaceOpenSansSemiBold);
	//					}else {
	//						tvTitletemptag.setVisibility(View.GONE);
	//					}
	//
	//					tvTitleItem.setBackgroundColor(resources.getColor(R.color.ash_color));
	//
	//					dialog.dismiss();
	//
	//					PassengerInfoPersonDO passengerInfoPersonDO = (PassengerInfoPersonDO) obj;
	//					passengerInfoPersonDO.persontitle = tvTitleSel.getText().toString();
	//				}
	//			});
	//		}
	//		dialog.show();
	//	}
}
