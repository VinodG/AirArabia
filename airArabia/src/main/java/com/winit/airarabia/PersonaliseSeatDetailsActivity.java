package com.winit.airarabia;

import java.util.Vector;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.controls.CustomDialog;
import com.winit.airarabia.objects.AirPriceQuoteDO;
import com.winit.airarabia.objects.AirRowDO;
import com.winit.airarabia.objects.AirSeatDO;
import com.winit.airarabia.objects.AirSeatMapDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.PassengerInfoPersonDO;
import com.winit.airarabia.objects.RequestDO;
import com.winit.airarabia.objects.SeatMapDO;
import com.winit.airarabia.utils.LogUtils;
import com.winit.airarabia.utils.StringUtils;
import com.winit.airarabia.webaccess.Response;

public class PersonaliseSeatDetailsActivity extends BaseActivity implements DataListener{

	private LinearLayout llDetails,llMain;
	private AirPriceQuoteDO airPriceQuoteDOTotal;
	private Vector<FlightSegmentDO> vecFlights;
	private AirSeatMapDO airSeatMapDO;
	private View seatlayoutview;
	private boolean isServiceCalled= false;
	private boolean isSeatChanged = false;
	private boolean isManageBook = false;
	TextView tv_selecTiltle;
	Boolean flag=true;
	private TextView tvCancel;
	private TextView tvDone;
	private TextView tvSelectHeader;

	@Override
	public void initilize() {

		tvHeaderTitle.setText(getString(R.string.seatselection));
		initClassMembers();
		
		vecFlights = new Vector<FlightSegmentDO>();
		
	}
	private void initClassMembers()
	{
		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(getString(R.string.Continue));
		airPriceQuoteDOTotal = (AirPriceQuoteDO) getIntent().getExtras().getSerializable(AppConstants.AIRPORTS_PRICE_TOTAL);

		llDetails = (LinearLayout) layoutInflater.inflate(R.layout.personalize_seat_details_selection, null);

//		lltop.setVisibility(View.GONE);
		tvCancel = (TextView) llDetails.findViewById(R.id.tvCancel);
		tvDone = (TextView) llDetails.findViewById(R.id.tvDone);
		tvSelectHeader = (TextView) llDetails.findViewById(R.id.tvSelectHeader);
		
		llMiddleBase.addView(llDetails, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
		tv_selecTiltle= (TextView) llDetails.findViewById(R.id.tv_selecTiltle);
		llMain    = (LinearLayout) llDetails.findViewById(R.id.llBaggageMain);
		tv_selecTiltle.setText(getString(R.string.SelectSeatsmall));
//		setTypeFaceOpenSansLight(llDetails);
		tv_selecTiltle.setTypeface(typefaceOpenSansSemiBold);
		
		tvDone.setTypeface(typefaceOpenSansSemiBold);
		tvCancel.setTypeface(typefaceOpenSansSemiBold);
		tvSelectHeader.setTypeface(typefaceOpenSansSemiBold);
//		lockDrawer("Seat Selection");
	}
	@Override
	public void bindingControl() {

//		if(AppConstants.bookingFlightDO.myODIDO != null)
//		for (int i = 0; i < AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs.size(); i++) {
//			vecFlights.addAll(AppConstants.bookingFlightDO.myODIDO.vecOriginDestinationOptionDOs
//					.get(i).vecFlightSegmentDOs);
//		}
//		if(AppConstants.bookingFlightDO.myODIDOReturn != null)
//		for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size(); i++) {
//			vecFlights.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
//					.get(i).vecFlightSegmentDOs);
//		}
		if (!isManageBook) {
			
			if(AppConstants.bookingFlightDO.myODIDOOneWay != null)
			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
					.size(); i++) {
				vecFlights
				.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
						.get(i).vecFlightSegmentDOs);
			}
			if(AppConstants.bookingFlightDO.myODIDOReturn != null)
			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
					.size(); i++) {
				vecFlights
				.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
						.get(i).vecFlightSegmentDOs);
			}
			
			for (int i = 0; i < vecFlights.size(); i++) {
				vecFlights.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
			}
		} else {
			if (AppConstants.bookingFlightDO.myODIDOOneWay != null) {
				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
						.size(); i++) {
					vecFlights
					.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
							.get(i).vecFlightSegmentDOs);
				}
				for (int i = 0; i < vecFlights.size(); i++) {
					vecFlights.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
				}
			} else {
				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
						.size(); i++) {
					vecFlights
					.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
							.get(i).vecFlightSegmentDOs);
				}

				for (int i = 0; i < vecFlights.size(); i++) {
					vecFlights.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDOReturn.srvUrlType;
				}
			}
		}
		
		if(AppConstants.AirSeatMapDO != null && AppConstants.AirSeatMapDO.vecSeatMapDOs != null && AppConstants.AirSeatMapDO.vecSeatMapDOs.size() > 0)
			addSeatItems(AppConstants.AirSeatMapDO);
		else 
			callSeatsService();

		btnSubmitNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(isServiceCalled)
				{
					Intent intenNext = new Intent();
					intenNext.putExtra(AppConstants.AIRPORTS_PRICE_TOTAL, airPriceQuoteDOTotal);
					setResult(RESULT_OK, intenNext);
				}
				else if(isSeatChanged)
				{
					Intent intenNext = new Intent();
					setResult(RESULT_OK, intenNext);
				}
				finish();
			}
		});
		
		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		tvDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				btnSubmitNext.performClick();				
			}
		});
		
	}

	private void addSeatItems(AirSeatMapDO airSeatMapDOData) {
		llMain.removeAllViews();
		llMain.setVisibility(View.VISIBLE);

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
							airSeatMapDO.vecSeatMapDOs.add(airSeatMapDOData.vecSeatMapDOs.get(j));
						}
					}
				}

//			AppConstants.bookingFlightDO.vecSeatRequestDOs.clear();

			int reqCount = -1;
			if (airSeatMapDO.vecSeatMapDOs.size() > 0)
			{
				Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDO = new Vector<PassengerInfoPersonDO>();
				for (PassengerInfoPersonDO passengerInfoPersonDO : AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO) {
					if (!passengerInfoPersonDO.persontype
							.equalsIgnoreCase(AppConstants.PERSON_TYPE_INFANT))
						vecPassengerInfoPersonDO
						.add(passengerInfoPersonDO);
				}
				Boolean retrunseatFlag=false;
				int returnSize = airSeatMapDO.vecSeatMapDOs.size();
				for (int i = 0; i < airSeatMapDO.vecSeatMapDOs.size(); i++) {
					
					final SeatMapDO seatMapDO = airSeatMapDO.vecSeatMapDOs.get(i);
					LinearLayout llSeatChild = (LinearLayout) layoutInflater
							.inflate(R.layout.personalise_trip_details_item, null);
					TextView tvPersonalTripDetailsItemSource = (TextView) llSeatChild
							.findViewById(R.id.tvPersonalTripDetailsItemSource);
					TextView tvPersonalTripDetailsItemDest = (TextView) llSeatChild
							.findViewById(R.id.tvPersonalTripDetailsItemDest);
					LinearLayout llPersonalTripDetailsItemMain = (LinearLayout) llSeatChild
							.findViewById(R.id.llPersonalTripDetailsItemMain);
					
					TextView tvPersonalTripItemNameTitle = (TextView) llSeatChild
							.findViewById(R.id.tvPersonalTripItemNameTitle);
					ImageView flightLogo = (ImageView) llSeatChild
							.findViewById(R.id.flightLogo);
					
					tvPersonalTripDetailsItemSource.setTypeface(typefaceOpenSansSemiBold);
					tvPersonalTripDetailsItemDest.setTypeface(typefaceOpenSansSemiBold);
					
					final TextView tvPersonalTripItemAmounttile = (TextView) llSeatChild
							.findViewById(R.id.tvPersonalTripItemAmounttile);
					final TextView tvPersonalTripItemAEDValueTitle = (TextView) llSeatChild
							.findViewById(R.id.tvPersonalTripItemAEDValueTitle);
					final LinearLayout llPersonalTripItemCrossTitle = (LinearLayout) llSeatChild
							.findViewById(R.id.llPersonalTripItemCrossTitle);
					final LinearLayout ll_subheading = (LinearLayout) llSeatChild
							.findViewById(R.id.ll_heading);

					int infCount = 0;
					LinearLayout llPersonalTripItemData = (LinearLayout) llSeatChild
							.findViewById(R.id.llPersonalTripItemData);

					if (seatMapDO != null && seatMapDO.vecAirRowDOs != null
							&& seatMapDO.vecAirRowDOs.size() > 0) {

						infCount = AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO
								.size() - vecPassengerInfoPersonDO.size();

//==============================newly added for tittles===================================================
						ll_subheading.setVisibility(View.VISIBLE);
						tvPersonalTripItemNameTitle.setText(getString(R.string.Name));
						tvPersonalTripItemNameTitle.setTypeface(typefaceOpenSansSemiBold);
						tvPersonalTripItemAmounttile.setText(getString(R.string.seatnumber));
						tvPersonalTripItemAmounttile.setTypeface(typefaceOpenSansSemiBold);
						tvPersonalTripItemAEDValueTitle.setText(AppConstants.CurrencyCodeAfterExchange);
						tvPersonalTripItemAEDValueTitle.setTypeface(typefaceOpenSansSemiBold);
						tvPersonalTripItemAEDValueTitle.setVisibility(View.VISIBLE);
						tvPersonalTripItemAmounttile.setTextColor(getResources().getColor(R.color.text_color));
						tvPersonalTripItemAmounttile.setTypeface(typefaceOpenSansSemiBold);
						flag=true;
						
						
						for (int j = 0; j < vecPassengerInfoPersonDO.size(); j++) {
							
							reqCount += 1;
							
							LinearLayout llItemSeatSub = (LinearLayout) layoutInflater
									.inflate(R.layout.personalise_trip_details_item_sub,null);

							TextView tvPersonalTripItemName = (TextView) llItemSeatSub
									.findViewById(R.id.tvPersonalTripItemName);
							final TextView tvPersonalTripItemAmount = (TextView) llItemSeatSub
									.findViewById(R.id.tvPersonalTripItemAmount);
							final TextView tvPersonalTripItemAEDValue = (TextView) llItemSeatSub
									.findViewById(R.id.tvPersonalTripItemAEDValue);
							final LinearLayout llPersonalTripItemCross = (LinearLayout) llItemSeatSub
									.findViewById(R.id.llPersonalTripItemCross);
							
							tvPersonalTripItemName.setTypeface(typefaceOpenSansSemiBold);
							tvPersonalTripItemAmount.setTypeface(typefaceOpenSansSemiBold);
							tvPersonalTripItemAEDValue.setTypeface(typefaceOpenSansSemiBold);
							
//====================newly added for issue================================================================================							
							
							
						if (flag) {
//							ll_subheading.setVisibility(View.VISIBLE);
//							tvPersonalTripItemNameTitle.setText(getString(R.string.Name));
//							tvPersonalTripItemAmounttile.setText(getString(R.string.seatnumber));
//							tvPersonalTripItemAEDValueTitle.setText(AppConstants.CurrencyCodeAfterExchange);
//							tvPersonalTripItemAEDValueTitle.setVisibility(View.VISIBLE);
//							tvPersonalTripItemAmounttile.setTextColor(getResources().getColor(R.color.text_color));
						}else{
//							ll_subheading.setVisibility(View.GONE);
						}
							llPersonalTripDetailsItemMain.addView(llItemSeatSub);
//							tvPersonalTripItemName
//							.setText(AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO
//									.get(j).personfirstname+" "+AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO
//									.get(j).personlastname);
							tvPersonalTripItemName.setText(AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO
									.get(j).personfirstname+"\n" +
									AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO.get(j).personlastname);
							tvPersonalTripItemAEDValue.setText("");
							tvPersonalTripItemAEDValue.setPadding(0, 0, 12, 0);
							llPersonalTripItemCross.setTag(j + "");

							final PassengerInfoPersonDO passengerInfoPersonDO = vecPassengerInfoPersonDO.get(j);
							
							//newly Added code to bind data first time.earlier it was not present to bind data first time.
							if(AppConstants.bookingFlightDO.vecSeatRequestDOs != null 
									&& AppConstants.bookingFlightDO.vecSeatRequestDOs.size()>0)
							{
								for (int x = 0; x < AppConstants.bookingFlightDO.vecSeatRequestDOs.size(); x++)
								{
									final RequestDO requestDO = AppConstants.bookingFlightDO.vecSeatRequestDOs.get(x);
									if(requestDO.flightNumber.equalsIgnoreCase(seatMapDO.flightSegmentDO.flightNumber)
											&& requestDO.travelerRefNumberRPHList.equalsIgnoreCase(passengerInfoPersonDO.travelerRefNumberRPHList)
											&& !requestDO.seatNumber.equalsIgnoreCase(""))
									{
										tvPersonalTripItemAmount.setText(requestDO.seatNumber);
//										tvPersonalTripItemAEDValue.setText(StringUtils.getStrFloatFromString(requestDO.seatAmount));
										tvPersonalTripItemAEDValue.setText(updateCurrencyByFactor(requestDO.seatAmount, 0)+"");
										tvPersonalTripItemAEDValue.setPadding(0, 0, 0, 0);
										llPersonalTripItemCross.setVisibility(View.VISIBLE);
									}
								}
							}
							
														
						
							if(AppConstants.bookingFlightDO.vecSeatRequestDOs != null 
									&& AppConstants.bookingFlightDO.vecSeatRequestDOs.size() == (airSeatMapDO.vecSeatMapDOs.size())*(vecPassengerInfoPersonDO.size()))
							{
								boolean isFound = false;
								for (int x = 0; x < AppConstants.bookingFlightDO.vecSeatRequestDOs.size(); x++)
								{
									final RequestDO requestDO = AppConstants.bookingFlightDO.vecSeatRequestDOs.get(x);
									if(requestDO.flightNumber.equalsIgnoreCase(seatMapDO.flightSegmentDO.flightNumber)
											&& requestDO.travelerRefNumberRPHList.equalsIgnoreCase(passengerInfoPersonDO.travelerRefNumberRPHList)
											&& !requestDO.seatNumber.equalsIgnoreCase(""))
									{
										isFound = true;
										tvPersonalTripItemAmount.setText(requestDO.seatNumber);

//										tvPersonalTripItemAEDValue.setText(StringUtils.getStrFloatFromString(requestDO.seatAmount));
										tvPersonalTripItemAEDValue.setText(updateCurrencyByFactor(requestDO.seatAmount, 0)+"");
										tvPersonalTripItemAEDValue.setPadding(0, 0, 0, 0);
										llPersonalTripItemCross.setVisibility(View.VISIBLE);
										
										tvPersonalTripItemAmount.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												clickSelectSeat(seatMapDO,
														tvPersonalTripItemAmount,
														requestDO,
														tvPersonalTripItemAEDValue,
														llPersonalTripItemCross);
											}
										});
										llPersonalTripItemCross.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												tvPersonalTripItemAmount.setText(getString(R.string.Select));
//												tvPersonalTripItemAEDValue.setText("0");
												tvPersonalTripItemAEDValue.setText("-");
												tvPersonalTripItemAEDValue.setPadding(0, 0, 12, 0);
												llPersonalTripItemCross.setVisibility(View.VISIBLE);
												isSeatChanged = true;
												for (int k = 0; k < AppConstants.bookingFlightDO.vecSeatRequestDOs
														.size(); k++) {
													if (AppConstants.bookingFlightDO.vecSeatRequestDOs
															.get(k).flightNumber
															.equalsIgnoreCase(requestDO.flightNumber)
															&& AppConstants.bookingFlightDO.vecSeatRequestDOs
															.get(k).travelerRefNumberRPHList
															.equalsIgnoreCase(requestDO.travelerRefNumberRPHList)) {
														AppConstants.bookingFlightDO.vecSeatRequestDOs.get(k).seatNumber = "";
														break;
													}
												}
											}
										});
									}
								}
								
								if(!isFound)
								{
									LogUtils.infoLog("reqCount seat after selection===", reqCount+"---"+AppConstants.bookingFlightDO.vecSeatRequestDOs.size());
										final RequestDO requestDO = AppConstants.bookingFlightDO.vecSeatRequestDOs.get(reqCount);
//									final RequestDO requestDO = new RequestDO();
//									requestDO.departureDate = seatMapDO.flightSegmentDO.departureDateTime;
//									requestDO.travelerRefNumberRPHList = passengerInfoPersonDO.travelerRefNumberRPHList;
//									requestDO.flightNumber = seatMapDO.flightSegmentDO.flightNumber;
//									requestDO.flightRefNumberRPHList = seatMapDO.flightSegmentDO.RPH;

									tvPersonalTripItemAEDValue.setText(StringUtils.getStrFloatFromString(""));
									tvPersonalTripItemAEDValue.setText("-");
									tvPersonalTripItemAEDValue.setPadding(0, 0, 12, 0);

									if (infCount > 0) {
										if (passengerInfoPersonDO.persontype
												.equalsIgnoreCase(AppConstants.PERSON_TYPE_ADULT)) {
											requestDO.isINF = true;
											infCount -= 1;
										}
									}
								
//									AppConstants.bookingFlightDO.vecSeatRequestDOs.add(requestDO);
									tvPersonalTripItemAmount.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											clickSelectSeat(seatMapDO,
													tvPersonalTripItemAmount,
													requestDO,
													tvPersonalTripItemAEDValue,
													llPersonalTripItemCross);
										}
									});
									llPersonalTripItemCross.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											tvPersonalTripItemAmount.setText(getString(R.string.Select));
//											tvPersonalTripItemAEDValue.setText("0.00");
											tvPersonalTripItemAEDValue.setText("-");
											tvPersonalTripItemAEDValue.setPadding(0, 0, 12, 0);
											llPersonalTripItemCross.setVisibility(View.VISIBLE);
											isSeatChanged = true;
											for (int k = 0; k < AppConstants.bookingFlightDO.vecSeatRequestDOs
													.size(); k++) {
												if (AppConstants.bookingFlightDO.vecSeatRequestDOs
														.get(k).flightNumber
														.equalsIgnoreCase(requestDO.flightNumber)
														&& AppConstants.bookingFlightDO.vecSeatRequestDOs
														.get(k).travelerRefNumberRPHList
														.equalsIgnoreCase(requestDO.travelerRefNumberRPHList)) {
													AppConstants.bookingFlightDO.vecSeatRequestDOs.get(k).seatNumber = "";
													break;
												}
											}
										}
									});
								
								
							}
						}
							else
							{
								LogUtils.infoLog("reqCount seat before selection===", reqCount+"");
								
								final RequestDO requestDO = new RequestDO();
								requestDO.departureDate = seatMapDO.flightSegmentDO.departureDateTime;
								requestDO.travelerRefNumberRPHList = passengerInfoPersonDO.travelerRefNumberRPHList;
								requestDO.flightNumber = seatMapDO.flightSegmentDO.flightNumber;
								requestDO.flightRefNumberRPHList = seatMapDO.flightSegmentDO.RPH;
															
//===================newly added for final build========================================								
								if(AppConstants.bookingFlightDO.vecSeatRequestDOs != null 
										&& AppConstants.bookingFlightDO.vecSeatRequestDOs.size()==0)
								{
									tvPersonalTripItemAEDValue.setText(StringUtils.getStrFloatFromString(""));
									tvPersonalTripItemAEDValue.setText("-");
									tvPersonalTripItemAEDValue.setPadding(0, 0, 12, 0);

								}
								
								if (infCount > 0) {
									if (passengerInfoPersonDO.persontype
											.equalsIgnoreCase(AppConstants.PERSON_TYPE_ADULT)) {
										requestDO.isINF = true;
										infCount -= 1;
									}
								}
								AppConstants.bookingFlightDO.vecSeatRequestDOs.add(requestDO);
								LogUtils.infoLog("reqCount seat before selection===", reqCount+"---"+AppConstants.bookingFlightDO.vecSeatRequestDOs.size());
								tvPersonalTripItemAmount.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										clickSelectSeat(seatMapDO,
												tvPersonalTripItemAmount,
												requestDO,
												tvPersonalTripItemAEDValue,
												llPersonalTripItemCross);
									}
								});
								llPersonalTripItemCross.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										tvPersonalTripItemAmount.setText(getString(R.string.Select));
//										tvPersonalTripItemAEDValue.setText("0.00");
										tvPersonalTripItemAEDValue.setText("-");
										tvPersonalTripItemAEDValue.setPadding(0, 0, 12, 0);
										llPersonalTripItemCross.setVisibility(View.VISIBLE);

										for (int k = 0; k < AppConstants.bookingFlightDO.vecSeatRequestDOs
												.size(); k++) {
											if (AppConstants.bookingFlightDO.vecSeatRequestDOs
													.get(k).flightNumber
													.equalsIgnoreCase(requestDO.flightNumber)
													&& AppConstants.bookingFlightDO.vecSeatRequestDOs
													.get(k).travelerRefNumberRPHList
													.equalsIgnoreCase(requestDO.travelerRefNumberRPHList)) {
												AppConstants.bookingFlightDO.vecSeatRequestDOs.get(k).seatNumber = "";
												break;
											}
										}
									}
								});
							}
						}
					} else {
//						llPersonalTripItemData.setVisibility(View.VISIBLE);
//						addNoDataAvailable(getString(R.string.NoSeatMap),llPersonalTripDetailsItemMain);
						
						// Added for new requirement to remove Bus information in seat selection
						llPersonalTripItemData.setVisibility(View.GONE);
//						addNoDataAvailable(getString(R.string.NoSeatMap),llPersonalTripDetailsItemMain);
					}
					llMain.addView(llSeatChild);
					
					if (AppConstants.allAirportNamesDO != null
							&& AppConstants.allAirportNamesDO.vecAirport != null
							&& AppConstants.allAirportNamesDO.vecAirport.size() > 0) {
						String sourceName = seatMapDO.flightSegmentDO.departureAirportCode, originName = seatMapDO.flightSegmentDO.arrivalAirportCode;
//
//						updateAirportNameFromCode(sourceName,
//								AppConstants.allAirportNamesDO.vecAirport,
//								tvPersonalTripDetailsItemSource);
//						updateAirportNameFromCode(originName,
//								AppConstants.allAirportNamesDO.vecAirport,
//								tvPersonalTripDetailsItemDest);
						tvPersonalTripDetailsItemSource
						.setText(sourceName);
						tvPersonalTripDetailsItemDest
						.setText(originName);
						
//						if(retrunseatFlag){
//							if(tv_selecTiltle.getText().toString().equalsIgnoreCase("اختر مقعدك")){
						
						if(!checkLangArabic())
						{
						
							if(returnSize > airSeatMapDO.vecSeatMapDOs.size()/2){
								flightLogo.setImageResource(R.drawable.flight_oneway);
							}else{
								flightLogo.setImageResource(R.drawable.flight_return);
							}
						}
						else
						{
							if(returnSize > airSeatMapDO.vecSeatMapDOs.size()/2){
								flightLogo.setImageResource(R.drawable.flight_return);
							}else{
								flightLogo.setImageResource(R.drawable.flight_oneway);
							}
						}
						returnSize--;
//						}else
//							retrunseatFlag=true;
					} else {
						tvPersonalTripDetailsItemSource
						.setText(seatMapDO.flightSegmentDO.departureAirportCode);
						tvPersonalTripDetailsItemDest
						.setText(seatMapDO.flightSegmentDO.arrivalAirportCode);
					}
			}
		}
		} else
			addNoDataAvailable(getString(R.string.NoSeatMap), llMain);
	}

	private void addNoDataAvailable(String strMsg, LinearLayout llSelected) {
		TextView tvNoDataAvailable = (TextView) layoutInflater.inflate(R.layout.no_data_found, null);
		tvNoDataAvailable.setText(strMsg);
		llSelected.addView(tvNoDataAvailable);
	}

	private void callSeatsService() {
		isServiceCalled = true;
		Vector<FlightSegmentDO> vecFlightSegmentDOs = new Vector<FlightSegmentDO>();

		if (!isManageBook) {
			if(AppConstants.bookingFlightDO.myODIDOOneWay != null)
			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size(); i++) {
				vecFlightSegmentDOs
				.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
						.get(i).vecFlightSegmentDOs);
			}
			if(AppConstants.bookingFlightDO.myODIDOReturn != null)
			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size(); i++) {
				vecFlightSegmentDOs
				.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
						.get(i).vecFlightSegmentDOs);
			}

			for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
				vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
			}
		} 
		else
		{
			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size(); i++) {
				vecFlightSegmentDOs
				.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
						.get(i).vecFlightSegmentDOs);
			}
			for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
				vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
			}
		}
		if (new CommonBL(PersonaliseSeatDetailsActivity.this,
				PersonaliseSeatDetailsActivity.this).getAirSeatMap(
						AppConstants.bookingFlightDO.requestParameterDO,
						vecFlights)) 
		{}
		else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	@Override
	public void dataRetreived(Response data) {
		if (data != null) {
			switch (data.method) {
			case AIR_SEAT_DETAILS:
				if (!data.isError) {
					airSeatMapDO = (AirSeatMapDO) data.data;
					
					AppConstants.AirSeatMapDO = airSeatMapDO;
					
					addSeatItems(airSeatMapDO);
					
					if (!TextUtils.isEmpty(airSeatMapDO.transactionIdentifier))
						AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airSeatMapDO.transactionIdentifier;
				}
				else if(data.data instanceof String)
				{
					if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
					{
						hideLoader();
						showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
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
				}
				hideLoader();
				break;
			}
		}
	}
	
	private void clickSelectSeat(final SeatMapDO seatMapDO,
			final TextView tvPersonalTripItemAmount,
			final RequestDO requestDOSel,
			final TextView tvPersonalTripItemAEDValue,
			final LinearLayout llPersonalTripItemCross) {
		
		isSeatChanged = true;
		
		LinearLayout layout = (LinearLayout) layoutInflater.inflate(
				R.layout.popup_seat_selection, null);
		
//		final CustomDialog dialog = new CustomDialog(
//				PersonaliseSeatDetailsActivity.this, layout,
//				AppConstants.DEVICE_WIDTH - 40, LayoutParams.WRAP_CONTENT,
//				false);
		final CustomDialog dialog = new CustomDialog(
				PersonaliseSeatDetailsActivity.this, layout,
				AppConstants.DEVICE_WIDTH - 100, AppConstants.DEVICE_HEIGHT -300,
				false);
		
		dialog.setCancelable(false);
		dialog.show();
		LinearLayout llseatlayout = (LinearLayout) layout
				.findViewById(R.id.llseatlayout);

		ImageView imgvseat_left3 = (ImageView) layout
				.findViewById(R.id.imgvseat_left3);
		Button btnCofirmseat = (Button) layout.findViewById(R.id.btnCofirmseat);
		Button btnCancel = (Button) layout.findViewById(R.id.btnCancel);
		setTypeFaceSemiBold(layout);
		btnCofirmseat.setTypeface(typefaceOpenSansSemiBold);
		imgvseat_left3.setAlpha(0.5f);
		btnCofirmseat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		 final Vector<ImageView> vecSeatsSelected = new Vector<ImageView>();
		
		for (int i1 = 0; i1 < seatMapDO.vecAirRowDOs.size(); i1++) 
		{
			seatlayoutview = LayoutInflater.from(
					PersonaliseSeatDetailsActivity.this).inflate(R.layout.bookingseat_liststyle, null);
			LinearLayout llSeatSec1 = (LinearLayout) seatlayoutview.findViewById(R.id.llSeatSec1);
			LinearLayout llSeatSec2 = (LinearLayout) seatlayoutview.findViewById(R.id.llSeatSec2);
			TextView tvSeatRow = (TextView) seatlayoutview.findViewById(R.id.tvSeatRow);
			final AirRowDO airRowDO = seatMapDO.vecAirRowDOs.get(i1);
			tvSeatRow.setText(airRowDO.rowNumber);
			tvSeatRow.setTypeface(typefaceOpenSansSemiBold);
			for (int i2 = 0; i2 < airRowDO.vecAirSeatDOs.size(); i2++)
			{

				final AirSeatDO airSeatDO = airRowDO.vecAirSeatDOs.get(i2);
				ImageView ivFlightSeat = (ImageView) layoutInflater.inflate(R.layout.flight_seats_item, null);

				if (airSeatDO.seatAvailability.equalsIgnoreCase(AppConstants.SEAT_VAC)) 
				{
					ivFlightSeat.setImageResource(R.drawable.btseatnormal);
					ivFlightSeat.setTag(R.string.seatRowNo, airRowDO.rowNumber);
					ivFlightSeat.setTag(R.string.seatNo, airSeatDO.seatNumber);
					ivFlightSeat.setTag(R.string.seatSelected, false);
				} else 
				{
					ivFlightSeat.setImageResource(R.drawable.btseatnormal);
					ivFlightSeat.setAlpha(0.5f);
					ivFlightSeat.setEnabled(false);
				}
				if (airSeatDO.seatNumber.equalsIgnoreCase("A")) {
					llSeatSec1.addView(ivFlightSeat);
				} else if (airSeatDO.seatNumber.equalsIgnoreCase("B")) {
					llSeatSec1.addView(ivFlightSeat);
				} else if (airSeatDO.seatNumber.equalsIgnoreCase("C")) {
					llSeatSec1.addView(ivFlightSeat);
				} else if (airSeatDO.seatNumber.equalsIgnoreCase("D")) {
					llSeatSec2.addView(ivFlightSeat);
				} else if (airSeatDO.seatNumber.equalsIgnoreCase("E")) {
					llSeatSec2.addView(ivFlightSeat);
				} else if (airSeatDO.seatNumber.equalsIgnoreCase("F"))
				{
					llSeatSec2.addView(ivFlightSeat);
				}

				if (AppConstants.bookingFlightDO.vecSeatRequestDOs.size() > 0)
					for (RequestDO requestDO : AppConstants.bookingFlightDO.vecSeatRequestDOs)
					{

						if (requestDO.seatNumber.length() > 1) 
						{
							if (requestDO.flightNumber.equalsIgnoreCase(seatMapDO.flightSegmentDO.flightNumber))
							{
								String seatNo = "", rowNo = "";
								int strLength = requestDO.seatNumber.length();

								seatNo = requestDO.seatNumber.substring(strLength - 1, strLength);
								rowNo = requestDO.seatNumber.substring(0,strLength - 1);

								if (rowNo.equalsIgnoreCase(airRowDO.rowNumber)
										&& seatNo.equalsIgnoreCase(airSeatDO.seatNumber)) 
								{
									vecSeatsSelected.add(ivFlightSeat);
									ivFlightSeat.setTag(R.string.seatSelected,true);
									ivFlightSeat.setImageResource(R.drawable.btseatselected_x);

									if (!requestDO.travelerRefNumberRPHList.equalsIgnoreCase(requestDOSel.travelerRefNumberRPHList)) {
										ivFlightSeat.setEnabled(false);
									} else
										ivFlightSeat.setEnabled(true);
								}
							}
						}
					}

				ivFlightSeat.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v) 
					{
						String strSeatNo = v.getTag(R.string.seatRowNo).toString()+ v.getTag(R.string.seatNo).toString();

						if ((Boolean) v.getTag(R.string.seatSelected))
						{
							boolean isFound = false;
							for (RequestDO requestDO : AppConstants.bookingFlightDO.vecSeatRequestDOs) 
							{

								if (requestDO.flightNumber.equalsIgnoreCase(seatMapDO.flightSegmentDO.flightNumber)) 
								{
									if (requestDO.seatNumber.equalsIgnoreCase(strSeatNo)) {
										if (requestDO.travelerRefNumberRPHList.equalsIgnoreCase(requestDOSel.travelerRefNumberRPHList))
										{
											requestDO.seatNumber = "";
										} 
										else
											isFound = true;
										break;
									}
								}
							}
							if (!isFound) 
							{
								requestDOSel.seatNumber = "";

								tvPersonalTripItemAmount.setText(getString(R.string.Select));
								tvPersonalTripItemAEDValue.setText(StringUtils.getStrFloatFromString(""));
								tvPersonalTripItemAEDValue.setPadding(0, 0, 12, 0);
								v.setTag(R.string.seatSelected, false);
								llPersonalTripItemCross.setVisibility(View.VISIBLE);
								((ImageView) v).setImageResource(R.drawable.btseatnormal);
							}
						}
						else 
						{
							if (!requestDOSel.isINF) 
							{
								boolean isFound = false;
								if (vecSeatsSelected.size() > 0)
								{
									for (int i = 0; i < vecSeatsSelected.size(); i++) 
									{
										//int size=vecSeatsSelected.size();
										ImageView imageView = vecSeatsSelected.get(i);
										String seatNoSel = "", rowNo = "";
										//String s_no = requestDOSel.seatNumber;
										int strLength = requestDOSel.seatNumber.length();

										if (strLength > 0) 
										{
											seatNoSel = requestDOSel.seatNumber.substring(strLength - 1,strLength);
											rowNo = requestDOSel.seatNumber.substring(0, strLength - 1);

											if (rowNo.equalsIgnoreCase(imageView.getTag(R.string.seatRowNo).toString())
													&& seatNoSel.equalsIgnoreCase(imageView.getTag(R.string.seatNo).toString())) 
											{
												vecSeatsSelected.remove(imageView);
												vecSeatsSelected.add((ImageView) v);
												imageView.setTag(R.string.seatSelected,false);
												imageView.setImageResource(R.drawable.btseatnormal);

												isFound = true;
											}
										}
									}
								}
								requestDOSel.seatNumber = strSeatNo;
								requestDOSel.seatAmount = airSeatDO.seatCharacteristics;
								
								tvPersonalTripItemAmount.setText(requestDOSel.seatNumber);
								tvPersonalTripItemAEDValue.setText(updateCurrencyByFactor(StringUtils.getStrFloatFromString(airSeatDO.seatCharacteristics), 0)+"");
								tvPersonalTripItemAEDValue.setPadding(0, 0, 0, 0);
								v.setTag(R.string.seatSelected, true);
								((ImageView) v).setImageResource(R.drawable.btseatselected_x);

								if (!isFound)
								{
									llPersonalTripItemCross.setVisibility(View.VISIBLE);
									vecSeatsSelected.add((ImageView) v);
								}
							}
							else 
							{
								int seatNo = StringUtils.getInt(v.getTag(R.string.seatRowNo).toString());
								if (seatNo == 11 || seatNo == 12)
								{
									showGateSeatPopup();
								}
								else if (seatNo % 2 == 0) 
								{
									if (v.getTag(R.string.seatNo).toString().equalsIgnoreCase("B"))
									{
										showCustomDialog(
												PersonaliseSeatDetailsActivity.this,
												getString(R.string.Alert),
												getString(R.string.SeatSelectionError),
												getString(R.string.Ok), "", "");
									} 
									else 
									{
										boolean isFound = false;
										if (vecSeatsSelected.size() > 0)
										{
											for (int i = 0; i < vecSeatsSelected.size(); i++) {
												ImageView imageView = vecSeatsSelected.get(i);
												String seatNoSel = "", rowNo = "";
												int strLength = requestDOSel.seatNumber.length();

												if (strLength > 0) 
												{
													seatNoSel = requestDOSel.seatNumber.substring(strLength - 1,strLength);
													rowNo = requestDOSel.seatNumber.substring(0,strLength - 1);

													if (rowNo.equalsIgnoreCase(imageView.getTag(R.string.seatRowNo).toString())
															&& seatNoSel.equalsIgnoreCase(imageView.getTag(R.string.seatNo).toString())) {
														vecSeatsSelected.remove(imageView);
														vecSeatsSelected.add((ImageView) v);
														imageView.setTag(R.string.seatSelected,false);
														imageView.setImageResource(R.drawable.btseatnormal);

														isFound = true;
													}
												}
											}
										}
										requestDOSel.seatNumber = strSeatNo;
										requestDOSel.seatAmount = airSeatDO.seatCharacteristics;
										
										tvPersonalTripItemAmount.setText(requestDOSel.seatNumber);
										tvPersonalTripItemAEDValue.setText(updateCurrencyByFactor(StringUtils.getStrFloatFromString(airSeatDO.seatCharacteristics), 0)+"");
										tvPersonalTripItemAEDValue.setPadding(0, 0, 0, 0);
										v.setTag(R.string.seatSelected, true);
										((ImageView) v).setImageResource(R.drawable.btseatselected_x);

										if (!isFound) 
										{
											llPersonalTripItemCross.setVisibility(View.VISIBLE);
											vecSeatsSelected.add((ImageView) v);
										}
									}
								}
								else 
								{
									boolean isFound = false;
									if (vecSeatsSelected.size() > 0)
									{
										for (int i = 0; i < vecSeatsSelected.size(); i++) {
											ImageView imageView = vecSeatsSelected.get(i);
											String seatNoSel = "", rowNo = "";
											int strLength = requestDOSel.seatNumber.length();

											if (strLength > 0) 
											{
												seatNoSel = requestDOSel.seatNumber.substring(strLength - 1,strLength);
												rowNo = requestDOSel.seatNumber.substring(0,strLength - 1);

												if (rowNo.equalsIgnoreCase(imageView.getTag(R.string.seatRowNo).toString())
														&& seatNoSel.equalsIgnoreCase(imageView.getTag(R.string.seatNo).toString())) {
													vecSeatsSelected.remove(imageView);
													vecSeatsSelected.add((ImageView) v);
													imageView.setTag(R.string.seatSelected,false);
													imageView.setImageResource(R.drawable.btseatnormal);

													isFound = true;
												}
											}
										}
									}
									requestDOSel.seatNumber = strSeatNo;
									requestDOSel.seatAmount = airSeatDO.seatCharacteristics;
									
									tvPersonalTripItemAmount.setText(requestDOSel.seatNumber);
//									tvPersonalTripItemAEDValue.setText(StringUtils.getStrFloatFromString(airSeatDO.seatCharacteristics));
									
//=====================================newly added for price================================================================================									
									
									tvPersonalTripItemAEDValue.setText(updateCurrencyByFactor(airSeatDO.seatCharacteristics, 0)+"");
									tvPersonalTripItemAEDValue.setPadding(0, 0, 0, 0);
									v.setTag(R.string.seatSelected, true);
									((ImageView) v).setImageResource(R.drawable.btseatselected_x);

									if (!isFound) 
									{
										llPersonalTripItemCross.setVisibility(View.VISIBLE);
										vecSeatsSelected.add((ImageView) v);
									}
								}

							}
						}
					}
				});
			}
			llseatlayout.addView(seatlayoutview);
		}
	}
	
	private void showGateSeatPopup() {
		LinearLayout llInsuranceChild = (LinearLayout) layoutInflater.inflate(
				R.layout.seat_gate_error, null);
		final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_No_Title);
//		dialog.setContentView(llInsuranceChild,
//				new LinearLayout.LayoutParams(AppConstants.DEVICE_WIDTH - 20,
//						AppConstants.DEVICE_HEIGHT - 100));
		dialog.setContentView(llInsuranceChild);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);

		Button btnOkSeatPop = (Button) llInsuranceChild.findViewById(R.id.btnOkSeatPop);
		Button btnCancelSeatPop = (Button) llInsuranceChild.findViewById(R.id.btnCancelSeatPop);

		btnOkSeatPop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		btnCancelSeatPop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
}
