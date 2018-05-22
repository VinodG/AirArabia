package com.winit.airarabia;

import java.util.Vector;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirHalaDO;
import com.winit.airarabia.objects.AirPriceQuoteDO;
import com.winit.airarabia.objects.AirportServiceDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.HalaDO;
import com.winit.airarabia.objects.PassengerInfoPersonDO;
import com.winit.airarabia.objects.RequestDO;
import com.winit.airarabia.utils.StringUtils;
import com.winit.airarabia.webaccess.Response;

public class PersonaliseSSLServiceDetailsActivity extends BaseActivity implements DataListener{

	private LinearLayout llDetails,llMain;
	private AirPriceQuoteDO airPriceQuoteDOTotal;
	private boolean isServiceCalled= false;
	private boolean isHalaService = false;
	private Vector<HalaDO> vecHalaDOs;
	private boolean isDataChanged = false;
	private TextView tv_selecTiltle;
	private TextView tvCancel;
	private TextView tvDone;
	private TextView tvSelectHeader;

	@Override
	public void initilize() {

		lltop.setVisibility(View.VISIBLE);
		tvHeaderTitle.setText(getString(R.string.airportservice));
		initClassMembers();

		vecHalaDOs = new Vector<HalaDO>();
	}
	private void initClassMembers()
	{
		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(getString(R.string.Continue));

		airPriceQuoteDOTotal = (AirPriceQuoteDO) getIntent().getExtras().getSerializable(AppConstants.AIRPORTS_PRICE_TOTAL);

		llDetails = (LinearLayout) layoutInflater.inflate(R.layout.personalize_airportserv_details_selection, null);

//		lltop.setVisibility(View.GONE);
		tvCancel = (TextView) llDetails.findViewById(R.id.tvCancel);
		tvDone = (TextView) llDetails.findViewById(R.id.tvDone);
		tvSelectHeader = (TextView) llDetails.findViewById(R.id.tvSelectHeader);

		llMiddleBase.addView(llDetails, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

		llMain    = (LinearLayout) llDetails.findViewById(R.id.llBaggageMain);
		tv_selecTiltle    = (TextView) llDetails.findViewById(R.id.tv_selecTiltle);

		tv_selecTiltle.setText(getString(R.string.AirportServices));
		setTypeFaceOpenSansLight(llMain);
		tv_selecTiltle.setTypeface(typefaceOpenSansSemiBold);
		tvDone.setTypeface(typefaceOpenSansSemiBold);
		tvCancel.setTypeface(typefaceOpenSansSemiBold);
		tvSelectHeader.setTypeface(typefaceOpenSansSemiBold);
//		lockDrawer("Airport Services Selection");
	}

	public void staticInit(){
		TextView tvPersonalTripDetailsItemSource = (TextView) llDetails
				.findViewById(R.id.tvPersonalTripDetailsItemSource);
		TextView tvPersonalTripDetailsItemDest = (TextView) llDetails
				.findViewById(R.id.tvPersonalTripDetailsItemDest);
		TextView tvPersonalTripDetailsItemSource1 = (TextView) llDetails
				.findViewById(R.id.tvPersonalTripDetailsItemSource1);
		TextView tvPersonalTripDetailsItemDest1 = (TextView) llDetails
				.findViewById(R.id.tvPersonalTripDetailsItemDest1);
		LinearLayout llPersonalTripDetailsItemMain = (LinearLayout) llDetails
				.findViewById(R.id.llPersonalTripDetailsItemMain);

		TextView tvPersonalTripItemNameTitle = (TextView) llDetails
				.findViewById(R.id.tvPersonalTripItemNameTitle);
		TextView tvPersonalTripItemName = (TextView) llDetails
				.findViewById(R.id.tvPersonalTripItemName);
		TextView tvPersonalTripItemName1 = (TextView) llDetails
				.findViewById(R.id.tvPersonalTripItemName1);

		final TextView tvPersonalTripItemAmounttile = (TextView) llDetails
				.findViewById(R.id.tvPersonalTripItemAmounttile);
		final TextView tvPersonalTripItemAmount = (TextView) llDetails
				.findViewById(R.id.tvPersonalTripItemAmount);
		final TextView tvPersonalTripItemAmount1 = (TextView) llDetails
				.findViewById(R.id.tvPersonalTripItemAmount1);
		final TextView tvPersonalTripItemAEDValueTitle = (TextView) llDetails
				.findViewById(R.id.tvPersonalTripItemAEDValueTitle);
		final TextView tvPersonalTripItemAEDValue = (TextView) llDetails
				.findViewById(R.id.tvPersonalTripItemAEDValue);
		final TextView tvPersonalTripItemAEDValue1 = (TextView) llDetails
				.findViewById(R.id.tvPersonalTripItemAEDValue1);
		final LinearLayout llPersonalTripItemCrossTitle = (LinearLayout) llDetails
				.findViewById(R.id.llPersonalTripItemCrossTitle);
		final LinearLayout ll_subheading = (LinearLayout) llDetails
				.findViewById(R.id.ll_heading);

		final LinearLayout llReturnWay = (LinearLayout) llDetails.findViewById(R.id.llReturnWay);

		if(AppConstants.bookingFlightDO.myBookFlightDOReturn == null)
			llReturnWay.setVisibility(View.GONE);

		tvPersonalTripItemAEDValueTitle.setText(AppConstants.CurrencyCodeAfterExchange);
		tvPersonalTripDetailsItemSource.setText(""+AppConstants.OriginLocation);
		tvPersonalTripDetailsItemDest.setText(""+AppConstants.DestinationLocation);
		tvPersonalTripDetailsItemDest1.setText(""+AppConstants.OriginLocation);
		tvPersonalTripDetailsItemSource1.setText(""+AppConstants.DestinationLocation);
		tvPersonalTripItemAEDValue.setText("-");
		tvPersonalTripItemAEDValue1.setText("-");
		if(AppConstants.bookingFlightDO !=null && AppConstants.bookingFlightDO.passengerInfoDO != null && AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO != null && AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO.size()>0){
			tvPersonalTripItemName.setText(AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO.get(0).personfirstname+"\n"+AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO.get(0).personlastname);
			tvPersonalTripItemName1.setText(AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO.get(0).personfirstname+"\n"+AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO.get(0).personlastname);
		}

		setTypeFaceSemiBold(llDetails);

		tvPersonalTripItemAmount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showLoader("");
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						hideLoader();
					}
				}, 2000);

			}
		});
		tvPersonalTripItemAmount1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showLoader("");
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						hideLoader();
					}
				}, 2000);

			}
		});
	}
	@Override
	public void bindingControl() {

//		if(AppConstants.vecHalaDOs != null && AppConstants.vecHalaDOs.size() > 0)
//		{
//			vecHalaDOs = AppConstants.vecHalaDOs;
//			addSSLItems(AppConstants.vecHalaDOs);
//		}
//		else 
		callHalaServiceOneWay();

		btnSubmitNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isServiceCalled)
				{
					Intent intenNext = new Intent();
					intenNext.putExtra(AppConstants.AIRPORTS_PRICE_TOTAL, airPriceQuoteDOTotal);
					setResult(RESULT_OK, intenNext);
				}
				else if(isDataChanged)
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

	private void addSSLItems(Vector<HalaDO> vecHalaDOs) {
		showLoader("");
		if (vecHalaDOs != null && vecHalaDOs.size() > 0) {

			Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDO = new Vector<PassengerInfoPersonDO>();
			for (PassengerInfoPersonDO passengerInfoPersonDO : AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO) {
				if (!passengerInfoPersonDO.persontype.equalsIgnoreCase(AppConstants.PERSON_TYPE_INFANT))
					vecPassengerInfoPersonDO.add(passengerInfoPersonDO);
			}
			for (final HalaDO halaDO : vecHalaDOs) {
				if (halaDO.vecAirportServiceDOs != null
						&& halaDO.vecAirportServiceDOs.size() > 0) {
					LinearLayout llBaggageChild = (LinearLayout) layoutInflater
							.inflate(R.layout.personalise_trip_details_item, null);
					TextView tvPersonalTripDetailsItemSource = (TextView) llBaggageChild
							.findViewById(R.id.tvPersonalTripDetailsItemSource);
					TextView tvPersonalTripDetailsItemDest = (TextView) llBaggageChild
							.findViewById(R.id.tvPersonalTripDetailsItemDest);
					LinearLayout llPersonalTripDetailsItemMain = (LinearLayout) llBaggageChild
							.findViewById(R.id.llPersonalTripDetailsItemMain);

					TextView tvPersonalTripItemNameTitle = (TextView) llBaggageChild
							.findViewById(R.id.tvPersonalTripItemNameTitle);

					final TextView tvPersonalTripItemAmounttile = (TextView) llBaggageChild
							.findViewById(R.id.tvPersonalTripItemAmounttile);
					final TextView tvPersonalTripItemAEDValueTitle = (TextView) llBaggageChild
							.findViewById(R.id.tvPersonalTripItemAEDValueTitle);
					final LinearLayout llPersonalTripItemCrossTitle = (LinearLayout) llBaggageChild
							.findViewById(R.id.llPersonalTripItemCrossTitle);
					final LinearLayout ll_subheading = (LinearLayout) llBaggageChild
							.findViewById(R.id.ll_heading);


					setTypeFaceOpenSansLight(llBaggageChild);
//==================================newly added for  titles=========================================================
					ll_subheading.setVisibility(View.VISIBLE);
					tvPersonalTripItemNameTitle.setText(getString(R.string.Name));
					tvPersonalTripItemAmounttile.setText(getString(R.string.seatnumber));
					tvPersonalTripItemAEDValueTitle.setText(AppConstants.CurrencyCodeAfterExchange);
					tvPersonalTripItemAEDValueTitle.setVisibility(View.VISIBLE);
					tvPersonalTripItemAmounttile.setTextColor(getResources().getColor(R.color.text_color));




					for (int j = 0; j < vecPassengerInfoPersonDO.size(); j++) {
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

//====================newly added for issue================================================================================

//						TextView tvPersonalTripItemNameTitle = (TextView) llItemSeatSub
//								.findViewById(R.id.tvPersonalTripItemNameTitle);
//
//						final TextView tvPersonalTripItemAmounttile = (TextView) llItemSeatSub
//								.findViewById(R.id.tvPersonalTripItemAmounttile);
//						final TextView tvPersonalTripItemAEDValueTitle = (TextView) llItemSeatSub
//								.findViewById(R.id.tvPersonalTripItemAEDValueTitle);
//						final LinearLayout llPersonalTripItemCrossTitle = (LinearLayout) llItemSeatSub
//								.findViewById(R.id.llPersonalTripItemCrossTitle);
						if(!TextUtils.isEmpty(tvPersonalTripItemAmounttile.getText().toString()))
							tvPersonalTripItemAmounttile.setText("Select Service");
						tvPersonalTripItemAEDValueTitle.setText(AppConstants.CurrencyCodeAfterExchange);


						llPersonalTripDetailsItemMain.addView(llItemSeatSub);
						tvPersonalTripItemName.setText(vecPassengerInfoPersonDO.get(j).personfirstname);
						tvPersonalTripItemAEDValue.setText("0.00");

						final PassengerInfoPersonDO passengerInfoPersonDO = vecPassengerInfoPersonDO.get(j);

						tvPersonalTripItemAmount.setTag(j + "");

						final Vector<AirportServiceDO> vecAirportServiceDOs = new Vector<AirportServiceDO>();
						if (halaDO.vecAirportServiceDOs.size() > 0)
							for (AirportServiceDO airportServiceDO : halaDO.vecAirportServiceDOs) {
								vecAirportServiceDOs.add(airportServiceDO);
							}

						if(AppConstants.bookingFlightDO.vecSSRRequests != null
								&& AppConstants.bookingFlightDO.vecSSRRequests.size() > 0)
						{
							String strValue = "";
							float mTotalHalaPrice = 0;
							for (int i = 0; i < AppConstants.bookingFlightDO.vecSSRRequests.size(); i++) {
								RequestDO requestDO = AppConstants.bookingFlightDO.vecSSRRequests.get(i);

								if(requestDO.flightNumber.equalsIgnoreCase(halaDO.flightSegmentDO.flightNumber)
										&& requestDO.travelerRefNumberRPHList
										.equalsIgnoreCase(passengerInfoPersonDO.travelerRefNumberRPHList))
								{
									if(TextUtils.isEmpty(strValue))
										strValue = requestDO.ssrName;
									else
										strValue = strValue+"\n"+requestDO.ssrName;
									for (AirportServiceDO airportServiceDO : vecAirportServiceDOs) {
										if (airportServiceDO.ssrCode.equalsIgnoreCase(requestDO.ssrCode))
										{
											mTotalHalaPrice = mTotalHalaPrice
													+ StringUtils.getFloat(airportServiceDO.adultAmount);
										}
									}
								}
							}
							if(!TextUtils.isEmpty(strValue))
							{
								tvPersonalTripItemAmount.setText(strValue);
//								tvPersonalTripItemAEDValue.setText(StringUtils.getStringFromFloat(mTotalHalaPrice));

//====================================newly added for price==========================================================

								tvPersonalTripItemAEDValue.setText(updateCurrencyByFactor(StringUtils.getStringFromFloat(mTotalHalaPrice), 0)+"");
								llPersonalTripItemCross.setVisibility(View.VISIBLE);
							}
						}
						tvPersonalTripItemAmount.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								showSelHalaPopup(
										passengerInfoPersonDO,
										halaDO,
										vecAirportServiceDOs,
										tvPersonalTripItemAmount,
										tvPersonalTripItemAEDValue,
										llPersonalTripItemCross);
							}
						});
						llPersonalTripItemCross.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								isDataChanged = true;
								llPersonalTripItemCross.setVisibility(View.INVISIBLE);
								tvPersonalTripItemAEDValue.setText("0.00");
								tvPersonalTripItemAmount.setText(getString(R.string.Select));

								for (int k = AppConstants.bookingFlightDO.vecSSRRequests
										.size() - 1; k >= 0; k--) {
									if (AppConstants.bookingFlightDO.vecSSRRequests.get(k).flightNumber
											.equalsIgnoreCase(halaDO.flightSegmentDO.flightNumber)
											&& AppConstants.bookingFlightDO.vecSSRRequests.get(k).travelerRefNumberRPHList
											.equalsIgnoreCase(passengerInfoPersonDO.travelerRefNumberRPHList)) {
										AppConstants.bookingFlightDO.vecSSRRequests.remove(k);
									}
								}
							}
						});
					}
					llMain.addView(llBaggageChild);

					if (AppConstants.allAirportNamesDO != null
							&& AppConstants.allAirportNamesDO.vecAirport != null
							&& AppConstants.allAirportNamesDO.vecAirport.size() > 0) {
						String sourceName = halaDO.flightSegmentDO.departureAirportCode, originName = halaDO.flightSegmentDO.arrivalAirportCode;

						tvPersonalTripDetailsItemSource.setText(sourceName);
						tvPersonalTripDetailsItemDest.setText(originName);

					} else {
						tvPersonalTripDetailsItemSource
								.setText(halaDO.flightSegmentDO.departureAirportCode);
						tvPersonalTripDetailsItemDest
								.setText(halaDO.flightSegmentDO.arrivalAirportCode);
					}
				}
			}
		} else
			addNoDataAvailable(getString(R.string.NoHala), llMain);

		hideLoader();
	}

	private void addNoDataAvailable(String strMsg, LinearLayout llSelected) {
		TextView tvNoDataAvailable = (TextView) layoutInflater.inflate(R.layout.no_data_found, null);
		tvNoDataAvailable.setText(strMsg);
		llSelected.addView(tvNoDataAvailable);
	}

	private void callHalaServiceOneWay() {
		showLoader("");
		isServiceCalled = true;
		isHalaService = false;
		Vector<FlightSegmentDO> vecFlightSegmentDOs = new Vector<FlightSegmentDO>();
		for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
				.size(); i++) {
			vecFlightSegmentDOs
					.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
							.get(i).vecFlightSegmentDOs);
		}
		for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
			vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
		}
		if (new CommonBL(PersonaliseSSLServiceDetailsActivity.this,PersonaliseSSLServiceDetailsActivity.this)
				.getHalaReq(AppConstants.bookingFlightDO.requestParameterDO,
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
		isServiceCalled = true;
		isHalaService = true;
		if (AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0) {
			Vector<FlightSegmentDO> vecFlightSegmentDOs = new Vector<FlightSegmentDO>();

			if (AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs != null
					&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0)
				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size(); i++) {
					vecFlightSegmentDOs
							.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
									.get(i).vecFlightSegmentDOs);
				}
			for (int i = 0; i < vecFlightSegmentDOs.size(); i++) {
				vecFlightSegmentDOs.get(i).operatingAirlineCode = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
			}
			if (new CommonBL(PersonaliseSSLServiceDetailsActivity.this,PersonaliseSSLServiceDetailsActivity.this)
					.getHalaReq(AppConstants.bookingFlightDO.requestParameterDO,vecFlightSegmentDOs)) {
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
				case HALA_REQ:

					if (!data.isError) {
						AirHalaDO airHalaDO = (AirHalaDO) data.data;
						if (airHalaDO != null && airHalaDO.vecHalaDOs != null
								&& airHalaDO.vecHalaDOs.size() > 0) {
							vecHalaDOs.add(airHalaDO.vecHalaDOs.get(0));
						}
						if (!isHalaService && AppConstants.bookingFlightDO.myODIDOReturn != null) {
							callHalaServiceReturn();
						}
						else
						{
							AppConstants.vecHalaDOs = vecHalaDOs;
							if(vecHalaDOs!=null && vecHalaDOs.size() > 0) {
								llMain.setVisibility(View.VISIBLE);
								addSSLItems(vecHalaDOs);
							}
							else
								staticInit();

							hideLoader();
						}
					}
					else
					{
						if(data.data instanceof String)
						{
							if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
							{
								hideLoader();
								showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
							}
							else {
								if (!isHalaService && AppConstants.bookingFlightDO.myODIDOReturn != null) {
									callHalaServiceReturn();
								} else
									hideLoader();
							}
						}else
							hideLoader();
					}
					break;
			}
			hideLoader();
		}
	}

	private void showSelHalaPopup(
			final PassengerInfoPersonDO passengerInfoPersonDO,
			final HalaDO halaDO, final Vector<AirportServiceDO> vecList,
			final TextView tvTitleSel, final TextView tvTitleSelCharge,
			final LinearLayout llPersonalTripItemCross) {
		LinearLayout ll = (LinearLayout) layoutInflater.inflate(R.layout.popup_titles, null);
		final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_No_Title);
		dialog.setContentView(ll);

		LinearLayout llPopupTitleMain = (LinearLayout) ll.findViewById(R.id.llPopupTitleMain);
		TextView tvTitleHeader = (TextView) ll.findViewById(R.id.tvTitleHeader);

		tvTitleHeader.setText(getString(R.string.select_services));

		for (int i = 0; i < vecList.size(); i++) {
			LinearLayout llTitle = (LinearLayout) layoutInflater.inflate(R.layout.popup_title_item_withcheck, null);

			final TextView tvTitleItem = (TextView) llTitle.findViewById(R.id.tvTitleItemNew);
			final ImageView ivTitleItemNewCheck = (ImageView) llTitle.findViewById(R.id.ivTitleItemNewCheck);

			llPopupTitleMain.addView(llTitle);
			tvTitleItem.setText(vecList.get(i).ssrName);
			tvTitleItem.setTag(i + "");
			tvTitleItem.setTag(R.string.hala_flight_tag,halaDO.flightSegmentDO.flightNumber);
			tvTitleItem.setTag(R.string.hala_name_tag, vecList.get(i).ssrCode);
			tvTitleItem.setTag(R.string.hala_person_tag,passengerInfoPersonDO.travelerRefNumberRPHList);

			ivTitleItemNewCheck.setTag(i + "");
			ivTitleItemNewCheck.setTag(R.string.hala_flight_tag,halaDO.flightSegmentDO.flightNumber);
			ivTitleItemNewCheck.setTag(R.string.hala_name_tag,vecList.get(i).ssrCode);
			ivTitleItemNewCheck.setTag(R.string.hala_person_tag,passengerInfoPersonDO.travelerRefNumberRPHList);
			ivTitleItemNewCheck.setTag(R.string.hala_check_tag, false);

			boolean isFound = false;
			for (int j = 0; j < AppConstants.bookingFlightDO.vecSSRRequests.size(); j++)
			{
				if (AppConstants.bookingFlightDO.vecSSRRequests.get(j).flightNumber
						.equalsIgnoreCase(ivTitleItemNewCheck.getTag(R.string.hala_flight_tag).toString())
						&& AppConstants.bookingFlightDO.vecSSRRequests.get(j).ssrCode
						.equalsIgnoreCase(ivTitleItemNewCheck.getTag(R.string.hala_name_tag).toString())
						&& AppConstants.bookingFlightDO.vecSSRRequests.get(j).travelerRefNumberRPHList
						.equalsIgnoreCase(ivTitleItemNewCheck.getTag(R.string.hala_person_tag).toString()))
				{
					isFound = true;
					break;
				}
			}
			if (isFound) {
				ivTitleItemNewCheck.setImageResource(com.winit.airarabia.R.drawable.checkblack);
				ivTitleItemNewCheck.setTag(R.string.hala_check_tag, true);
			} else {
				ivTitleItemNewCheck.setImageResource(com.winit.airarabia.R.drawable.uncheckblack);
				ivTitleItemNewCheck.setTag(R.string.hala_check_tag, false);
			}

			tvTitleItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ivTitleItemNewCheck.performClick();
				}
			});

			ivTitleItemNewCheck.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if ((Boolean) v.getTag(R.string.hala_check_tag))
					{
						for (int j = 0; j < AppConstants.bookingFlightDO.vecSSRRequests.size(); j++)
						{
							if (AppConstants.bookingFlightDO.vecSSRRequests.get(j).flightNumber
									.equalsIgnoreCase(v.getTag(R.string.hala_flight_tag).toString())
									&& AppConstants.bookingFlightDO.vecSSRRequests.get(j).ssrCode
									.equalsIgnoreCase(v.getTag(R.string.hala_name_tag).toString())
									&& AppConstants.bookingFlightDO.vecSSRRequests.get(j).travelerRefNumberRPHList
									.equalsIgnoreCase(v.getTag(R.string.hala_person_tag).toString()))
							{
								((ImageView) v).setImageResource(R.drawable.uncheck);
								v.setTag(R.string.hala_check_tag, false);
								AppConstants.bookingFlightDO.vecSSRRequests.remove(j);
								break;
							}
						}
					}
					else
					{
						final RequestDO requestDO = new RequestDO();
						requestDO.flightNumber = halaDO.flightSegmentDO.flightNumber;
						requestDO.departureDate = halaDO.flightSegmentDO.departureDateTime;
						requestDO.travelerRefNumberRPHList = passengerInfoPersonDO.travelerRefNumberRPHList;
						requestDO.flightNumber = halaDO.flightSegmentDO.flightNumber;
						requestDO.flightRefNumberRPHList = halaDO.flightSegmentDO.RPH;
						requestDO.airportType = halaDO.airportType;
						requestDO.airportCode = halaDO.airportCode;
						requestDO.ssrCode = vecList.get(StringUtils.getInt(v.getTag().toString())).ssrCode;
						requestDO.ssrName = vecList.get(StringUtils.getInt(v.getTag().toString())).ssrName;
						AppConstants.bookingFlightDO.vecSSRRequests.add(requestDO);

						((ImageView) v).setImageResource(R.drawable.check);
						v.setTag(R.string.hala_check_tag, true);
					}
				}
			});
		}
		Button btnPopupConfirm = (Button) ll.findViewById(R.id.btnPopupTitleCheckConfirm);
		btnPopupConfirm.setVisibility(View.VISIBLE);
		btnPopupConfirm.setTag(passengerInfoPersonDO.travelerRefNumberRPHList);
		btnPopupConfirm.setTag(R.string.hala_flight_tag,halaDO.flightSegmentDO.flightNumber);
		dialog.show();

		btnPopupConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				isDataChanged = true;

				String strHalaNames = "";
				float mTotalHalaPrice = 0;
				for (int j = 0; j < AppConstants.bookingFlightDO.vecSSRRequests.size(); j++) {
					if (AppConstants.bookingFlightDO.vecSSRRequests.get(j).travelerRefNumberRPHList
							.equalsIgnoreCase(v.getTag().toString())
							&& AppConstants.bookingFlightDO.vecSSRRequests.get(j).flightNumber
							.equalsIgnoreCase(v.getTag(R.string.hala_flight_tag).toString())) {
						if (j == 0)
							strHalaNames = AppConstants.bookingFlightDO.vecSSRRequests.get(j).ssrName;
						else
							strHalaNames = strHalaNames
									+ "\n"
									+ AppConstants.bookingFlightDO.vecSSRRequests.get(j).ssrName;

						for (AirportServiceDO airportServiceDO : vecList) {
							if (airportServiceDO.ssrCode
									.equalsIgnoreCase(AppConstants.bookingFlightDO.vecSSRRequests.get(j).ssrCode)) {
								mTotalHalaPrice = mTotalHalaPrice
										+ StringUtils.getFloat(airportServiceDO.adultAmount);
							}
						}
					}
				}

				if (strHalaNames.equalsIgnoreCase("")) {
					llPersonalTripItemCross.setVisibility(View.INVISIBLE);
					tvTitleSel.setText(getString(R.string.Select));
				} else {
					llPersonalTripItemCross.setVisibility(View.VISIBLE);
					tvTitleSel.setText(strHalaNames);
				}
				tvTitleSelCharge.setText(updateCurrencyByFactor(StringUtils.getStringFromFloat(mTotalHalaPrice), 0)+"");
				dialog.dismiss();
			}
		});
	}
}
