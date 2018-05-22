package com.winit.airarabia;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirPriceQuoteDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.InsuranceQuoteDO;
import com.winit.airarabia.objects.RequestDO;
import com.winit.airarabia.webaccess.Response;

public class PersonaliseInsuranceDetailsActivity extends BaseActivity implements DataListener{

	private LinearLayout llDetails,llMain;
	private AirPriceQuoteDO airPriceQuoteDOTotal;
	private boolean isServiceCalled= false;
	private InsuranceQuoteDO insuranceQuoteDO;
	private boolean isInsuranceChecked = false;
	private boolean isDataChanged = false;
	private TextView tv_selecTiltle;
	private TextView tvCancel;
	private TextView tvDone;
	private TextView tvSelectHeader;

	@Override
	public void initilize() {

		lltop.setVisibility(View.VISIBLE);
		tvHeaderTitle.setText(getString(R.string.Select_travel_insurance_header));
		initClassMembers();
	}
	private void initClassMembers()
	{
		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(getString(R.string.Continue));

		airPriceQuoteDOTotal = (AirPriceQuoteDO) getIntent().getExtras().getSerializable(AppConstants.AIRPORTS_PRICE_TOTAL);

		llDetails = (LinearLayout) layoutInflater.inflate(R.layout.personalize_travelinsurance_details_selection, null);
//		lltop.setVisibility(View.GONE);
		tvCancel = (TextView) llDetails.findViewById(R.id.tvCancel);
		tvDone = (TextView) llDetails.findViewById(R.id.tvDone);
		tvSelectHeader = (TextView) llDetails.findViewById(R.id.tvSelectHeader);

		llMiddleBase.addView(llDetails, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

		llMain    = (LinearLayout) llDetails.findViewById(R.id.llBaggageMain);
		tv_selecTiltle    = (TextView) llDetails.findViewById(R.id.tv_selecTiltle);

		tv_selecTiltle.setText(getString(R.string.SelectTravekInsurance));

		setTypeFaceOpenSansLight(llMain);
		tv_selecTiltle.setTypeface(typefaceOpenSansSemiBold);
		tvDone.setTypeface(typefaceOpenSansSemiBold);
		tvCancel.setTypeface(typefaceOpenSansSemiBold);
		tvSelectHeader.setTypeface(typefaceOpenSansSemiBold);
//		lockDrawer("Seat Selection");
	}
	@Override
	public void bindingControl() {

//		if(AppConstants.insuranceQuoteDO != null)
//			addtravelinsuranceItems(AppConstants.insuranceQuoteDO);
//		else 
//		{
//			showLoader("");
		callInsuranceService();
//		}

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

	private void addNoDataAvailable(String strMsg, LinearLayout llSelected) {
		TextView tvNoDataAvailable = (TextView) layoutInflater.inflate(R.layout.no_data_found, null);
		tvNoDataAvailable.setText(strMsg);
		llSelected.addView(tvNoDataAvailable);
	}

	private void callInsuranceService() {
		showLoader("");
		isServiceCalled = true;
		String travelType = AppConstants.bookingFlightDO.myBookFlightDO.travelType;

		if (!AppConstants.bookingFlightDO.myBookFlightDO.travelType.equalsIgnoreCase("")
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
		if (AppConstants.bookingFlightDO.myODIDOReturn == null) {
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

		if (new CommonBL(PersonaliseInsuranceDetailsActivity.this,PersonaliseInsuranceDetailsActivity.this)
				.getInsuranceQuote(
						AppConstants.bookingFlightDO.requestParameterDO,
						travelType,
						AppConstants.bookingFlightDO.passengerInfoDO.vecPassengerInfoPersonDO,
						flightSegmentDO))
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
				case INSURANCE_QUOTE:

					if (!data.isError) {
						insuranceQuoteDO = (InsuranceQuoteDO) data.data;

						if (!TextUtils.isEmpty(insuranceQuoteDO.transactionIdentifier))
							AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = insuranceQuoteDO.transactionIdentifier;

						AppConstants.insuranceQuoteDO = insuranceQuoteDO;

						addtravelinsuranceItems(insuranceQuoteDO);
						hideLoader();
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
						}
					}
					break;
			}
			hideLoader();
		}
	}
	private void addtravelinsuranceItems(final InsuranceQuoteDO insuranceQuoteDO) {

		llMain.removeAllViews();

		if (insuranceQuoteDO != null
				&& !insuranceQuoteDO.amount.equalsIgnoreCase("")) {
			LinearLayout llInsuranceChild = (LinearLayout) layoutInflater
					.inflate(R.layout.insurance_item, null);

			final LinearLayout llInsuranceCheckPurchase = (LinearLayout) llInsuranceChild
					.findViewById(R.id.llInsuranceCheckPurchase);
			final ImageView ivInsuranceCheckPurchase = (ImageView) llInsuranceChild
					.findViewById(R.id.ivInsuranceCheckPurchase);

			TextView tvInsurance2 = (TextView) llInsuranceChild
					.findViewById(R.id.tvInsurance2);
//			tvInsurance2.setText(Html.fromHtml(getString(R.string.InsuranceP1_sub3_1)+" "+" <b>"+ insuranceQuoteDO.currencyCode+" " + insuranceQuoteDO.amount+"</b> "+getString(R.string.InsuranceP1_sub3_2)));
			float tempInsuranceValue = Float.parseFloat(insuranceQuoteDO.amount+"")* Float.parseFloat(AppConstants.currencyConversionFactor+"");
			tvInsurance2.setText(Html.fromHtml(getString(R.string.InsuranceP1_sub3_1)+" "+" <b>"+ AppConstants.CurrencyCodeAfterExchange+" " + tempInsuranceValue+"</b> "+getString(R.string.InsuranceP1_sub3_2)));
			tvInsurance2.setTypeface(typefaceOpenSansSemiBold);
			if(AppConstants.bookingFlightDO.vecInsrRequestDOs != null
					&& AppConstants.bookingFlightDO.vecInsrRequestDOs.size() > 0)
			{
				isInsuranceChecked = true;
				ivInsuranceCheckPurchase.setBackgroundResource(R.drawable.check);
			}
			else
			{
				isDataChanged = true;
				isInsuranceChecked = true;
				final RequestDO requestDO = new RequestDO();

				int sizeReturnFlt = 0;

				requestDO.departureDate = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
						.get(0).vecFlightSegmentDOs.get(0).departureDateTime;
				if (AppConstants.bookingFlightDO.myODIDOReturn != null
						&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0) {
					sizeReturnFlt = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size();
					requestDO.arrivalDate = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
							.get(sizeReturnFlt - 1).vecFlightSegmentDOs.get(0).arrivalDateTime;
				} else {
					sizeReturnFlt = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size();
					requestDO.arrivalDate = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
							.get(sizeReturnFlt - 1).vecFlightSegmentDOs
							.get(0).arrivalDateTime;
				}

				requestDO.rPH = insuranceQuoteDO.rPH;
				requestDO.policyCode = insuranceQuoteDO.planID;
				requestDO.policyAmount = insuranceQuoteDO.amount;

				AppConstants.bookingFlightDO.vecInsrRequestDOs.add(requestDO);
				ivInsuranceCheckPurchase.setBackgroundResource(R.drawable.checkblack);
			}

			llInsuranceCheckPurchase.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					isDataChanged = true;
					if (!isInsuranceChecked) {
						isInsuranceChecked = true;
						final RequestDO requestDO = new RequestDO();

						int sizeReturnFlt = 0;

						requestDO.departureDate = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
								.get(0).vecFlightSegmentDOs.get(0).departureDateTime;
						if (AppConstants.bookingFlightDO.myODIDOReturn != null
								&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0) {
							sizeReturnFlt = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
									.size();
							requestDO.arrivalDate = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
									.get(sizeReturnFlt - 1).vecFlightSegmentDOs.get(0).arrivalDateTime;
						} else {
							sizeReturnFlt = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
									.size();
							requestDO.arrivalDate = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
									.get(sizeReturnFlt - 1).vecFlightSegmentDOs
									.get(0).arrivalDateTime;
						}

						requestDO.rPH = insuranceQuoteDO.rPH;
						requestDO.policyCode = insuranceQuoteDO.planID;
						requestDO.policyAmount = insuranceQuoteDO.amount;
						AppConstants.bookingFlightDO.vecInsrRequestDOs.add(requestDO);
						ivInsuranceCheckPurchase.setBackgroundResource(R.drawable.check);
					} else {
						showInsuranceUnSelPopup(ivInsuranceCheckPurchase);
					}
				}
			});

			ivInsuranceCheckPurchase.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					isDataChanged = true;
					llInsuranceCheckPurchase.performClick();
				}
			});

			setTypeFaceOpenSansLight(llInsuranceChild);

			llMain.addView(llInsuranceChild);
		} else
			addNoDataAvailable(getString(R.string.NoInsurance), llMain);
	}
	private void showInsuranceUnSelPopup(
			final ImageView ivInsuranceCheckPurchase) {
		ScrollView svInsuranceChild = (ScrollView) layoutInflater.inflate(
				R.layout.insurance_item_popup, null);
		final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_No_Title);
		dialog.setContentView(svInsuranceChild);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);

		LinearLayout llInsuranceChild = (LinearLayout)svInsuranceChild
				.findViewById(R.id.llInsuranceChild);

//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AppConstants.DEVICE_WIDTH - 40,
//				AppConstants.DEVICE_HEIGHT - 80);
//		llInsuranceChild.setLayoutParams(params);
		TextView tvInsuranceDesc = (TextView) llInsuranceChild
				.findViewById(R.id.tvInsuranceDesc);
		TextView tvInsurance2 = (TextView) llInsuranceChild
				.findViewById(R.id.tvInsurance2);
		TextView tvInsuranceSub1 = (TextView) llInsuranceChild
				.findViewById(R.id.tvInsuranceSub1);
		TextView tvInsuranceSub2 = (TextView) llInsuranceChild
				.findViewById(R.id.tvInsuranceSub2);
		TextView tvInsuranceSub3 = (TextView) llInsuranceChild
				.findViewById(R.id.tvInsuranceSub3);
		TextView tvInsuranceSub4 = (TextView) llInsuranceChild
				.findViewById(R.id.tvInsuranceSub4);
		ImageView ivCancelMealList = (ImageView) llInsuranceChild.findViewById(R.id.ivCancelMealList);

		tvInsurance2.setVisibility(View.GONE);
		tvInsuranceSub4.setVisibility(View.GONE);

		tvInsuranceDesc.setText(getString(R.string.InsPopupHead));
		tvInsuranceSub1.setText(getString(R.string.InsPopupP1));
		tvInsuranceSub2.setText(getString(R.string.InsPopupP2));
		tvInsuranceSub3.setText(getString(R.string.InsPopupP3));

		LinearLayout llinsurance_popup_bottom = (LinearLayout) layoutInflater
				.inflate(R.layout.insurance_popup_bottom, null);
		Button btnInsurancePopupYes = (Button) llinsurance_popup_bottom
				.findViewById(R.id.btnInsurancePopupYes);
		Button btnInsurancePopupNo = (Button) llinsurance_popup_bottom
				.findViewById(R.id.btnInsurancePopupNo);
		setTypeFaceOpenSansLight(llInsuranceChild);

		llInsuranceChild.addView(llinsurance_popup_bottom);

		btnInsurancePopupYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				isInsuranceChecked = true;
				AppConstants.bookingFlightDO.vecInsrRequestDOs.clear();
				ivInsuranceCheckPurchase.setBackgroundResource(R.drawable.check);

				final RequestDO requestDO = new RequestDO();

				int sizeReturnFlt = 0;

				requestDO.departureDate = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
						.get(0).vecFlightSegmentDOs.get(0).departureDateTime;
				if (AppConstants.bookingFlightDO.myODIDOReturn != null
						&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0) {
					sizeReturnFlt = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
							.size();
					requestDO.arrivalDate = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
							.get(sizeReturnFlt - 1).vecFlightSegmentDOs.get(0).arrivalDateTime;
				} else {
					sizeReturnFlt = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
							.size();
					requestDO.arrivalDate = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
							.get(sizeReturnFlt - 1).vecFlightSegmentDOs.get(0).arrivalDateTime;
				}

				requestDO.rPH = AppConstants.insuranceQuoteDO.rPH;
				requestDO.policyCode = AppConstants.insuranceQuoteDO.planID;
				requestDO.policyAmount = AppConstants.insuranceQuoteDO.amount;
				AppConstants.bookingFlightDO.vecInsrRequestDOs.add(requestDO);
			}
		});

		btnInsurancePopupNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				isInsuranceChecked = false;
				AppConstants.bookingFlightDO.vecInsrRequestDOs.clear();
				ivInsuranceCheckPurchase.setBackgroundResource(R.drawable.uncheckblack);
			}
		});
		ivCancelMealList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();

			}
		});;

		dialog.show();
	}
}
