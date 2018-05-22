package com.winit.airarabia;

import java.util.Vector;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.airarabia.adapters.BookingMealAdapter;
import com.winit.airarabia.adapters.BookingMealMainAdapter;
import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirMealDetailsDO;
import com.winit.airarabia.objects.AirPriceQuoteDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.MealCategoriesDO;
import com.winit.airarabia.objects.MealDO;
import com.winit.airarabia.objects.MealDetailsDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;
import com.winit.airarabia.objects.PassengerInfoPersonDO;
import com.winit.airarabia.objects.RequestDO;
import com.winit.airarabia.utils.StringUtils;
import com.winit.airarabia.webaccess.Response;

public class PersonaliseMealDetailsActivity extends BaseActivity implements DataListener {

	private LinearLayout llDetails, llMain;
	private AirMealDetailsDO airMealDetailsDO;
	private AirPriceQuoteDO airPriceQuoteDOTotal;
	private boolean isServiceCalled = false;
	private Vector<FlightSegmentDO> vecFlights;
	private Dialog dialogMealMain, dialogMealSub;
	private boolean isDataChanged = false;
	private Boolean flag = true;
	private TextView tv_selecTiltle;
	private TextView tvCancel;
	private TextView tvDone;
	private TextView tvSelectHeader;

	@Override
	public void initilize() {

		lltop.setVisibility(View.VISIBLE);
		tvHeaderTitle.setText(getString(R.string.mealselection));

		initClassMembers();

		vecFlights = new Vector<FlightSegmentDO>();
	}

	private void initClassMembers() {
		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(getString(R.string.Continue));
//		lltop.setVisibility(View.GONE);
		airPriceQuoteDOTotal = (AirPriceQuoteDO) getIntent().getExtras().getSerializable(AppConstants.AIRPORTS_PRICE_TOTAL);

		llDetails = (LinearLayout) layoutInflater.inflate(R.layout.personalize_meal_details_selection, null);

		tvCancel = (TextView) llDetails.findViewById(R.id.tvCancel);
		tvDone = (TextView) llDetails.findViewById(R.id.tvDone);
		tvSelectHeader = (TextView) llDetails.findViewById(R.id.tvSelectHeader);

		llMiddleBase.addView(llDetails, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

		llMain = (LinearLayout) llDetails.findViewById(R.id.llBaggageMain);
		tv_selecTiltle = (TextView) llDetails.findViewById(R.id.tv_selecTiltle);
		tv_selecTiltle.setText(getString(R.string.SelectMealsmall));
		setTypeFaceOpenSansLight(llDetails);

		tv_selecTiltle.setTypeface(typefaceOpenSansSemiBold);
		tvDone.setTypeface(typefaceOpenSansSemiBold);
		tvCancel.setTypeface(typefaceOpenSansSemiBold);
		tvSelectHeader.setTypeface(typefaceOpenSansSemiBold);
//		lockDrawer("Seat Selection");
	}

	@Override
	public void bindingControl() {

		if (AppConstants.bookingFlightDO.myODIDOOneWay != null)
			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size(); i++) {
				vecFlights.addAll(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
						.get(i).vecFlightSegmentDOs);
			}
		if (AppConstants.bookingFlightDO.myODIDOReturn != null)
			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size(); i++) {
				vecFlights.addAll(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
						.get(i).vecFlightSegmentDOs);
			}

		if (AppConstants.airMealDetailsDO != null && AppConstants.airMealDetailsDO.vecMealDetailsDOs != null && AppConstants.airMealDetailsDO.vecMealDetailsDOs.size() > 0)
			addMealItems(AppConstants.airMealDetailsDO);
		else
			callMealsService();

		btnSubmitNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isServiceCalled) {
					Intent intenNext = new Intent();
					intenNext.putExtra(AppConstants.AIRPORTS_PRICE_TOTAL, airPriceQuoteDOTotal);
					setResult(RESULT_OK, intenNext);
				} else if (isDataChanged) {
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

	private void addMealItems(AirMealDetailsDO airMealDetailsDOData) {
		showLoader("");
		llMain.removeAllViews();
		llMain.setVisibility(View.VISIBLE);

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
				for (int j = 0; j < airMealDetailsDOData.vecMealDetailsDOs.size(); j++) {
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
				int returnSize = airMealDetailsDO.vecMealDetailsDOs.size();

				for (int i = 0; i < airMealDetailsDO.vecMealDetailsDOs.size(); i++) {

					final MealDetailsDO mealDetailsDO = airMealDetailsDO.vecMealDetailsDOs.get(i);

					if (mealDetailsDO != null
							&& mealDetailsDO.vecMealCategoriesDO != null
							&& mealDetailsDO.vecMealCategoriesDO.size() > 0) {

						LinearLayout llMealChild = (LinearLayout) layoutInflater
								.inflate(R.layout.personalise_meal_details_item, null);
						LinearLayout llPersonalTripItemData = (LinearLayout) llMealChild
								.findViewById(R.id.llPersonalTripItemData);
						TextView tvPersonalTripDetailsItemSource = (TextView) llMealChild
								.findViewById(R.id.tvPersonalTripDetailsItemSource);
						TextView tvPersonalTripDetailsItemDest = (TextView) llMealChild
								.findViewById(R.id.tvPersonalTripDetailsItemDest);
						LinearLayout llPersonalTripDetailsItemMain = (LinearLayout) llMealChild
								.findViewById(R.id.llPersonalTripDetailsItemMain);

						tvPersonalTripDetailsItemSource.setTypeface(typefaceOpenSansSemiBold);
						tvPersonalTripDetailsItemDest.setTypeface(typefaceOpenSansSemiBold);

						TextView tvPersonalTripItemNameTitle = (TextView) llMealChild
								.findViewById(R.id.tvPersonalTripItemNameTitle);
						ImageView flightLogo = (ImageView) llMealChild
								.findViewById(R.id.flightLogo);
						final TextView tvPersonalTripItemAmounttile = (TextView) llMealChild
								.findViewById(R.id.tvPersonalTripItemAmounttile);
						final TextView tvPersonalTripItemPriceTitle = (TextView) llMealChild
								.findViewById(R.id.tvPersonalTripItemAEDValueTitle);
						final LinearLayout ll_heading = (LinearLayout) llMealChild
								.findViewById(R.id.ll_heading);

						tvPersonalTripItemNameTitle.setTypeface(typefaceOpenSansSemiBold);
						tvPersonalTripItemAmounttile.setTypeface(typefaceOpenSansSemiBold);
						tvPersonalTripItemPriceTitle.setTypeface(typefaceOpenSansSemiBold);

						if (mealDetailsDO.vecMealRequestDOs == null) {
							mealDetailsDO.vecMealRequestDOs = new Vector<Vector<RequestDO>>();
						}
						flag = true;
//						if (vecPassengerInfoPersonDO.size()>3) {
//							llPersonalTripItemData.setBackgroundResource(R.drawable.bg_white_big);
//						}else {
//							llPersonalTripItemData.setBackgroundResource(R.drawable.bg_white_midum);
//						}

						for (int j = 0; j < vecPassengerInfoPersonDO.size(); j++) {

							PassengerInfoPersonDO passengerInfoPersonDO = vecPassengerInfoPersonDO.get(j);

							LinearLayout llItemMealSub = (LinearLayout) layoutInflater
									.inflate(R.layout.personalise_meal_item_sub_meal, null);
							TextView tvPersonalTripItemName = (TextView) llItemMealSub
									.findViewById(R.id.tvPersonalTripItemName);
							final LinearLayout llPersonalTripItemAmount = (LinearLayout) llItemMealSub
									.findViewById(R.id.llPersonalTripItemAmount);
							final LinearLayout llPersonalTripItemPrice = (LinearLayout) llItemMealSub
									.findViewById(R.id.llPersonalTripItemPrice);
//=================================added mewly=====================================================================

							tvPersonalTripItemName.setTypeface(typefaceOpenSansSemiBold);

							if (flag) {

								ll_heading.setVisibility(View.VISIBLE);
//							tvPersonalTripItemNameTitle.setText(getString(R.string.Name));
								tvPersonalTripItemAmounttile.setText(getString(R.string.Meals));
								tvPersonalTripItemAmounttile.setTextColor(getResources().getColor(R.color.text_color));
								tvPersonalTripItemPriceTitle.setText(AppConstants.CurrencyCodeAfterExchange);
							} else {
								ll_heading.setVisibility(View.GONE);
							}


							llPersonalTripDetailsItemMain.addView(llItemMealSub);

							Vector<RequestDO> vecRequestDOs = new Vector<RequestDO>();

							if (mealDetailsDO.vecMealRequestDOs.size() > j
									&& mealDetailsDO.vecMealRequestDOs.get(j) != null
									&& mealDetailsDO.vecMealRequestDOs.get(j).size() > 0) {
								vecRequestDOs = mealDetailsDO.vecMealRequestDOs.get(j);

								tvPersonalTripItemName.setText(passengerInfoPersonDO.personfirstname +
										"\n" +
										passengerInfoPersonDO.personlastname);

								llPersonalTripItemAmount.setTag(j + "");
								llPersonalTripItemPrice.setTag(j + "");
								final String travelerRefNumberRPHList = passengerInfoPersonDO.travelerRefNumberRPHList;

								llPersonalTripItemPrice.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										showSelMealMenuPopup(
												travelerRefNumberRPHList,
												mealDetailsDO,
												llPersonalTripItemAmount,
												llPersonalTripItemPrice,
												mealDetailsDO.vecMealRequestDOs.get(StringUtils.getInt(v.getTag().toString())));
									}
								});
								for (RequestDO requestDO : vecRequestDOs) {
//									AppConstants.bookingFlightDO.vecMealReqDOs.add(requestDO);
									LinearLayout llPersonalTripItemAmountItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text, null);

									LinearLayout llPersonalTripItemPriceItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text_price, null);
									TextView tvPersonalTripItemAEDValue = (TextView) llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAEDValue);
									LinearLayout llPersonalTripItemCross = (LinearLayout) llPersonalTripItemPriceItem.findViewById(R.id.llPersonalTripItemCross);
									TextView tvPersonalTripItemAmount = (TextView) llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAmount);

									tvPersonalTripItemAEDValue.setTypeface(typefaceOpenSansSemiBold);
									tvPersonalTripItemAmount.setTypeface(typefaceOpenSansSemiBold);

									llPersonalTripItemAmount.addView(llPersonalTripItemAmountItem);
									llPersonalTripItemPrice.addView(llPersonalTripItemPriceItem);

									tvPersonalTripItemAmount.setText(getString(R.string.Select));
									tvPersonalTripItemAEDValue.setText("0");
									tvPersonalTripItemAEDValue.setText("-");
									tvPersonalTripItemAEDValue.setPadding(0, 0, 15, 0);
									llPersonalTripItemCross.setVisibility(View.VISIBLE);

									tvPersonalTripItemAmount.setText(requestDO.mealQuantity + "x" + requestDO.mealName);
									tvPersonalTripItemAmount.setTypeface(typefaceOpenSansSemiBold);

//									tvPersonalTripItemAEDValue.setText(StringUtils.getStringFromFloat(StringUtils.getFloat(requestDO.mealQuantity)* requestDO.mealCharge));

//=========================================newly added for price=================================================

									tvPersonalTripItemAEDValue.setText(updateCurrencyByFactor(StringUtils.getStringFromFloat(StringUtils.getFloat(requestDO.mealQuantity) * requestDO.mealCharge), 0) + "");
									tvPersonalTripItemAEDValue.setTypeface(typefaceOpenSansSemiBold);
									tvPersonalTripItemAEDValue.setPadding(0, 0, 0, 0);
									llPersonalTripItemCross.setTag(requestDO);
									llPersonalTripItemAmountItem.setTag(requestDO);
									llPersonalTripItemPriceItem.setTag(requestDO);
									llPersonalTripItemCross.setTag(R.string.Address, vecRequestDOs);

									llPersonalTripItemCross.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {

											isDataChanged = true;

											Vector<RequestDO> vecRequestDOs = (Vector<RequestDO>) v.getTag(R.string.Address);
											vecRequestDOs.remove((RequestDO) v.getTag());

											for (int k = 0; k < llPersonalTripItemAmount.getChildCount(); k++) {
												if (llPersonalTripItemAmount.getChildAt(k).getTag() != null && llPersonalTripItemAmount.getChildAt(k).getTag().equals((RequestDO) v.getTag())) {
													llPersonalTripItemAmount.removeViewAt(k);
												}
											}

											for (int k = 0; k < llPersonalTripItemPrice.getChildCount(); k++) {
												if (llPersonalTripItemPrice.getChildAt(k).getTag() != null && llPersonalTripItemPrice.getChildAt(k).getTag().equals((RequestDO) v.getTag())) {
													llPersonalTripItemPrice.removeViewAt(k);
												}
											}

											if (llPersonalTripItemPrice.getChildCount() == 0) {
												LinearLayout llPersonalTripItemAmountItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text, null);

												LinearLayout llPersonalTripItemPriceItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text_price, null);
												TextView tvPersonalTripItemAEDValue = (TextView) llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAEDValue);
												LinearLayout llPersonalTripItemCross = (LinearLayout) llPersonalTripItemPriceItem.findViewById(R.id.llPersonalTripItemCross);
												TextView tvPersonalTripItemAmount = (TextView) llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAmount);

												tvPersonalTripItemAmount.setText(getString(R.string.Select));
												tvPersonalTripItemAmount.setTypeface(typefaceOpenSansSemiBold);
//												tvPersonalTripItemAEDValue.setText("0.00");
												tvPersonalTripItemAEDValue.setText("-");
												tvPersonalTripItemAEDValue.setTypeface(typefaceOpenSansSemiBold);
												tvPersonalTripItemAEDValue.setPadding(0, 0, 15, 0);
												llPersonalTripItemCross.setVisibility(View.GONE);

												llPersonalTripItemAmount.addView(llPersonalTripItemAmountItem);
												llPersonalTripItemPrice.addView(llPersonalTripItemPriceItem);
											}
										}
									});
								}
							} else {
								vecRequestDOs = new Vector<RequestDO>();
								mealDetailsDO.vecMealRequestDOs.add(vecRequestDOs);
//								AppConstants.bookingFlightDO.vecMealReqDOs.addAll(vecRequestDOs);
								LinearLayout llPersonalTripItemAmountItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text, null);

								LinearLayout llPersonalTripItemPriceItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text_price, null);
								TextView tvPersonalTripItemAEDValue = (TextView) llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAEDValue);
								LinearLayout llPersonalTripItemCross = (LinearLayout) llPersonalTripItemPriceItem.findViewById(R.id.llPersonalTripItemCross);
								TextView tvPersonalTripItemAmount = (TextView) llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAmount);

								tvPersonalTripItemAmount.setText(getString(R.string.Select));
								tvPersonalTripItemAmount.setTypeface(typefaceOpenSansSemiBold);
//								tvPersonalTripItemAEDValue.setText("0.00");
								tvPersonalTripItemAEDValue.setText("-");
//==============================newly aedded for final bulid==================================

								tvPersonalTripItemAEDValue.setText("-");
								tvPersonalTripItemAEDValue.setPadding(0, 0, 15, 0);
								llPersonalTripItemCross.setVisibility(View.VISIBLE);

								tvPersonalTripItemAEDValue.setTypeface(typefaceOpenSansSemiBold);
								tvPersonalTripItemAmount.setTypeface(typefaceOpenSansSemiBold);

								llPersonalTripItemAmount.addView(llPersonalTripItemAmountItem);
								llPersonalTripItemPrice.addView(llPersonalTripItemPriceItem);

								tvPersonalTripItemName.setText(passengerInfoPersonDO.personfirstname
										+ "\n" +
										passengerInfoPersonDO.personlastname);
								llPersonalTripItemAmount.setTag(j + "");
								llPersonalTripItemPrice.setTag(j + "");
								final String travelerRefNumberRPHList = passengerInfoPersonDO.travelerRefNumberRPHList;

								llPersonalTripItemPrice.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										showSelMealMenuPopup(
												travelerRefNumberRPHList,
												mealDetailsDO,
												llPersonalTripItemAmount,
												llPersonalTripItemPrice,
												mealDetailsDO.vecMealRequestDOs.get(StringUtils.getInt(v.getTag().toString())));
									}
								});
							}
						}
						llMain.addView(llMealChild);

						if (AppConstants.allAirportNamesDO != null
								&& AppConstants.allAirportNamesDO.vecAirport != null
								&& AppConstants.allAirportNamesDO.vecAirport.size() > 0) {

							String sourceName = mealDetailsDO.flightSegmentDO.departureAirportCode, originName = mealDetailsDO.flightSegmentDO.arrivalAirportCode;

							tvPersonalTripDetailsItemSource.setText(sourceName);
							tvPersonalTripDetailsItemDest.setText(originName);
							tvPersonalTripDetailsItemSource.setTypeface(typefaceOpenSansSemiBold);
							tvPersonalTripDetailsItemDest.setTypeface(typefaceOpenSansSemiBold);

//							if(returnFlag){
//							if(tv_selecTiltle.getText().toString().equalsIgnoreCase("اختر وجبتك")){
							if (returnSize > airMealDetailsDO.vecMealDetailsDOs.size() / 2) {
//									flightLogo.setBackground(getResources().getDrawable(R.color.white));
								flightLogo.setImageResource(R.drawable.flight_oneway);
							} else {
								flightLogo.setImageResource(R.drawable.flight_return);
							}
							returnSize--;
//							}else{
//								returnFlag = true;
//							}

						} else {
							tvPersonalTripDetailsItemSource
									.setText(mealDetailsDO.flightSegmentDO.departureAirportCode);
							tvPersonalTripDetailsItemDest
									.setText(mealDetailsDO.flightSegmentDO.arrivalAirportCode);
						}
					}
				}
			}
		} else
			addNoDataAvailable(getString(R.string.NoMeals), llMain);

		hideLoader();
	}

	private void addNoDataAvailable(String strMsg, LinearLayout llSelected) {
		TextView tvNoDataAvailable = (TextView) layoutInflater.inflate(R.layout.no_data_found, null);
		tvNoDataAvailable.setText(strMsg);
		llSelected.addView(tvNoDataAvailable);
	}

	private void callMealsService() {
		showLoader("");
		isServiceCalled = true;
		Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();
		if (AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify != null
				&& AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.size() > 0) {
			for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
					.size(); i++) {
				vecOriginDestinationOptionDOs
						.add(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.get(i));
			}
		} else {
			if (AppConstants.bookingFlightDO.myODIDOOneWay != null)
				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
						.size(); i++) {
					vecOriginDestinationOptionDOs
							.add(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs
									.get(i));
				}
			if (AppConstants.bookingFlightDO.myODIDOReturn != null
					&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs != null
					&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0)
				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
						.size(); i++) {
					vecOriginDestinationOptionDOs
							.add(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs
									.get(i));
				}

		}

		showLoader("");
		if (new CommonBL(PersonaliseMealDetailsActivity.this, PersonaliseMealDetailsActivity.this).
				getAirMealDetails(AppConstants.bookingFlightDO.requestParameterDO,
						vecOriginDestinationOptionDOs)) {
		} else {
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
				case AIR_MEAL_DETAILS:
					hideLoader();
					if (!data.isError) {
						airMealDetailsDO = (AirMealDetailsDO) data.data;

						AppConstants.airMealDetailsDO = airMealDetailsDO;

						if (!airMealDetailsDO.transactionIdentifier.equalsIgnoreCase(""))
							AppConstants.bookingFlightDO.requestParameterDO.transactionIdentifier = airMealDetailsDO.transactionIdentifier;
						if (airMealDetailsDO != null
								&& airMealDetailsDO.vecMealDetailsDOs != null
								&& airMealDetailsDO.vecMealDetailsDOs.size() > 0) {
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
						}
						addMealItems(airMealDetailsDO);
					} else {
						if (data.data instanceof String) {
							if (((String) data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg))) {
								showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
							}
						}
						hideLoader();
					}
					break;
			}
			hideLoader();
		}
	}

	private void showSelMealMenuPopup(final String travelerRefNumberRPHList,
									  final MealDetailsDO mealDetailsDO,
									  final LinearLayout llPersonalTripItemAmount,
									  final LinearLayout llPersonalTripItemPrice, final Vector<RequestDO> vectorMealReq) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int screenHeight = (int) (metrics.heightPixels * 0.75);

		LinearLayout ll = (LinearLayout) layoutInflater.inflate(
				R.layout.popup_meal_mainmenu, null);
		dialogMealMain = new Dialog(this, R.style.Theme_Dialog_No_Title);
		dialogMealMain.setContentView(ll);
//		 getWindow().setLayout(LayoutParams.WRAP_CONTENT, screenHeight);
		ImageView ivCancel = (ImageView) ll.findViewById(R.id.ivCancel);
		GridView grid_mealmenu = (GridView) ll.findViewById(R.id.grid_mealmenu);
		Button btn_backinmealmenu = (Button) ll
				.findViewById(R.id.btn_backinmealmenu);
		TextView tvCategoryTitle = (TextView) ll.findViewById(R.id.tvCategoryTitle);
		tvCategoryTitle.setTypeface(typefaceOpenSansSemiBold);
		btn_backinmealmenu.setTypeface(typefaceOpenSansSemiBold);

		BookingMealMainAdapter bookingMealMainAdapter = new BookingMealMainAdapter(
				getApplicationContext(), mealDetailsDO.vecMealcategoryNames,
				mealDetailsDO.vecMealcategoryImageUrls);
		grid_mealmenu.setAdapter(bookingMealMainAdapter);

		grid_mealmenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				showSelMealPopup(travelerRefNumberRPHList, mealDetailsDO.flightSegmentDO,
						mealDetailsDO.vecMealCategoriesDO.get(arg2).vecMealsDO,
						llPersonalTripItemAmount,
						llPersonalTripItemPrice,
						mealDetailsDO.vecMealcategoryNames.get(arg2),
						vectorMealReq);
			}
		});
		btn_backinmealmenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogMealMain.dismiss();
			}
		});
		ivCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogMealMain.dismiss();
			}
		});
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialogMealMain.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = screenHeight;
		dialogMealMain.show();
		dialogMealMain.getWindow().setAttributes(lp);
	}

	private void showSelMealPopup(final String travelerRefNumberRPHList,
								  FlightSegmentDO flightSegmentDO,
								  final Vector<MealDO> vecList,
								  final LinearLayout llPersonalTripItemAmount,
								  final LinearLayout llPersonalTripItemPrice,
								  String categoryName, final Vector<RequestDO> vectorMealReq) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int screenHeight = (int) (metrics.heightPixels * 0.75);
		LinearLayout ll = (LinearLayout) layoutInflater.inflate(
				R.layout.popup_meal_selectitem, null);
		dialogMealSub = new Dialog(this, R.style.Theme_Dialog_No_Title);
		dialogMealSub.setContentView(ll);

		TextView tvMealsHeader = (TextView) ll.findViewById(R.id.tvMealsHeader);
		ImageView ivCancelMealList = (ImageView) ll
				.findViewById(R.id.ivCancelMealList);
		ListView list_mealselecteditems = (ListView) ll
				.findViewById(R.id.list_mealselecteditems);
		Button btnConfirminslecteditem = (Button) ll
				.findViewById(R.id.btnConfirminslecteditem);
		Button btnBackinslecteditem = (Button) ll
				.findViewById(R.id.btnBackinslecteditem);

		tvMealsHeader.setText(getString(R.string.ChooseItem));
		tvMealsHeader.setTypeface(typefaceOpenSansSemiBold);

		final Vector<RequestDO> vecRequestDOsNew = new Vector<RequestDO>();
		vecRequestDOsNew.addAll(vectorMealReq);

		for (int j = 0; j < vecList.size(); j++) {
			vecList.get(j).mealCount = 0;
			for (int i = 0; i < llPersonalTripItemAmount.getChildCount(); i++) {
				if (llPersonalTripItemAmount.getChildAt(i).getTag() != null) {
					RequestDO requestDO = (RequestDO) llPersonalTripItemAmount.getChildAt(i).getTag();
					if (travelerRefNumberRPHList.equalsIgnoreCase(requestDO.travelerRefNumberRPHList)
							&& flightSegmentDO.flightNumber.equalsIgnoreCase(requestDO.flightNumber)
							&& vecList.get(j).mealCode.equalsIgnoreCase(requestDO.mealCode)) {
						vecList.get(j).mealCount = StringUtils.getInt(requestDO.mealQuantity);
					}
				}
			}
		}

		BookingMealAdapter bookingMealAdapter = new BookingMealAdapter(
				this, travelerRefNumberRPHList,
				flightSegmentDO, vecRequestDOsNew, vecList);
		list_mealselecteditems.setAdapter(bookingMealAdapter);
		list_mealselecteditems.setCacheColorHint(0);

		btnConfirminslecteditem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				isDataChanged = true;
				Boolean mealFlag = true;

				llPersonalTripItemAmount.removeAllViews();
				llPersonalTripItemPrice.removeAllViews();
				vectorMealReq.clear();
				if (vecRequestDOsNew == null || (vecRequestDOsNew != null && vecRequestDOsNew.size() == 0)) {
					LinearLayout llPersonalTripItemAmountItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text, null);

					LinearLayout llPersonalTripItemPriceItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text_price, null);
					TextView tvPersonalTripItemAEDValue = (TextView) llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAEDValue);
					LinearLayout llPersonalTripItemCross = (LinearLayout) llPersonalTripItemPriceItem.findViewById(R.id.llPersonalTripItemCross);
					TextView tvPersonalTripItemAmount = (TextView) llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAmount);


					tvPersonalTripItemAmount.setText(getString(R.string.Select));
					tvPersonalTripItemAmount.setTypeface(typefaceOpenSansSemiBold);
//					tvPersonalTripItemAEDValue.setText("0.00");
					tvPersonalTripItemAEDValue.setText("-");
					tvPersonalTripItemAEDValue.setTypeface(typefaceOpenSansSemiBold);
					tvPersonalTripItemAEDValue.setPadding(0, 0, 15, 0);
					llPersonalTripItemCross.setVisibility(View.VISIBLE);

					llPersonalTripItemAmount.addView(llPersonalTripItemAmountItem);
					llPersonalTripItemPrice.addView(llPersonalTripItemPriceItem);
				}
				//Added on 23-Feb-2018 by Mukesh to restrict meal selection=================================================
				else if (vecRequestDOsNew == null || (vecRequestDOsNew != null && vecRequestDOsNew.size() > 1)
						|| StringUtils.getInt(vecRequestDOsNew.get(0).mealQuantity) > 1) {

					showCustomDialog(PersonaliseMealDetailsActivity.this, getString(R.string.Alert), "You have selected multi-meal. Please select One meal", getString(R.string.Ok), "", "NO_BAGGAGE");

					LinearLayout llPersonalTripItemAmountItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text, null);

					LinearLayout llPersonalTripItemPriceItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text_price, null);
					TextView tvPersonalTripItemAEDValue = (TextView) llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAEDValue);
					LinearLayout llPersonalTripItemCross = (LinearLayout) llPersonalTripItemPriceItem.findViewById(R.id.llPersonalTripItemCross);
					TextView tvPersonalTripItemAmount = (TextView) llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAmount);


					tvPersonalTripItemAmount.setText(getString(R.string.Select));
					tvPersonalTripItemAmount.setTypeface(typefaceOpenSansSemiBold);
//					tvPersonalTripItemAEDValue.setText("0.00");
					tvPersonalTripItemAEDValue.setText("-");
					tvPersonalTripItemAEDValue.setTypeface(typefaceOpenSansSemiBold);
					tvPersonalTripItemAEDValue.setPadding(0, 0, 15, 0);
					llPersonalTripItemCross.setVisibility(View.VISIBLE);

					llPersonalTripItemAmount.addView(llPersonalTripItemAmountItem);
					llPersonalTripItemPrice.addView(llPersonalTripItemPriceItem);

//      =====================================================================================================

				} else {
					for (RequestDO requestDO : vecRequestDOsNew) {
						vectorMealReq.add(requestDO);
						AppConstants.bookingFlightDO.vecMealReqDOs.add(requestDO);
						LinearLayout llPersonalTripItemAmountItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text, null);

						LinearLayout llPersonalTripItemPriceItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text_price, null);
						TextView tvPersonalTripItemAEDValue = (TextView) llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAEDValue);
						LinearLayout llPersonalTripItemCross = (LinearLayout) llPersonalTripItemPriceItem.findViewById(R.id.llPersonalTripItemCross);
						TextView tvPersonalTripItemAmount = (TextView) llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAmount);

						if (mealFlag) {
							mealFlag = false;
							tvPersonalTripItemAmount.setText(getString(R.string.Select_Meal));
							tvPersonalTripItemAmount.setTypeface(typefaceOpenSansSemiBold);
							tvPersonalTripItemAEDValue.setText("" + AppConstants.CurrencyCodeAfterExchange);
							tvPersonalTripItemAEDValue.setPadding(0, 0, 0, 0);
							llPersonalTripItemCross.setVisibility(View.VISIBLE);
						}
						llPersonalTripItemCross.setVisibility(View.VISIBLE);

						tvPersonalTripItemAmount.setText(requestDO.mealQuantity + "x" + requestDO.mealName);
						tvPersonalTripItemAmount.setTypeface(typefaceOpenSansSemiBold);
						tvPersonalTripItemAEDValue.setText(updateCurrencyByFactor(StringUtils.getStringFromFloat(StringUtils.getFloat(requestDO.mealQuantity) * requestDO.mealCharge), 0) + "");
						tvPersonalTripItemAEDValue.setTypeface(typefaceOpenSansSemiBold);
						tvPersonalTripItemAEDValue.setPadding(0, 0, 0, 0);
						llPersonalTripItemAmount.addView(llPersonalTripItemAmountItem);
						llPersonalTripItemPrice.addView(llPersonalTripItemPriceItem);

						llPersonalTripItemCross.setTag(requestDO);
						llPersonalTripItemAmountItem.setTag(requestDO);
						llPersonalTripItemPriceItem.setTag(requestDO);

						llPersonalTripItemCross.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								isDataChanged = true;

								vecRequestDOsNew.remove((RequestDO) v.getTag());
								vectorMealReq.remove((RequestDO) v.getTag());
								for (int k = 0; k < llPersonalTripItemAmount.getChildCount(); k++) {
									if (llPersonalTripItemAmount.getChildAt(k).getTag() != null && llPersonalTripItemAmount.getChildAt(k).getTag().equals((RequestDO) v.getTag())) {
										llPersonalTripItemAmount.removeViewAt(k);
									}
								}

								for (int k = 0; k < llPersonalTripItemPrice.getChildCount(); k++) {
									if (llPersonalTripItemPrice.getChildAt(k).getTag() != null && llPersonalTripItemPrice.getChildAt(k).getTag().equals((RequestDO) v.getTag())) {
										llPersonalTripItemPrice.removeViewAt(k);
									}
								}

								if (llPersonalTripItemPrice.getChildCount() == 0) {
									LinearLayout llPersonalTripItemAmountItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text, null);

									LinearLayout llPersonalTripItemPriceItem = (LinearLayout) layoutInflater.inflate(R.layout.meal_sub_item_text_price, null);
									TextView tvPersonalTripItemAEDValue = (TextView) llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAEDValue);
									LinearLayout llPersonalTripItemCross = (LinearLayout) llPersonalTripItemPriceItem.findViewById(R.id.llPersonalTripItemCross);
									TextView tvPersonalTripItemAmount = (TextView) llPersonalTripItemPriceItem.findViewById(R.id.tvPersonalTripItemAmount);

									tvPersonalTripItemAmount.setText(getString(R.string.Select));
									tvPersonalTripItemAmount.setTypeface(typefaceOpenSansSemiBold);
//									tvPersonalTripItemAEDValue.setText("0.00");
									tvPersonalTripItemAEDValue.setText("-");
									tvPersonalTripItemAEDValue.setPadding(0, 0, 15, 0);
									tvPersonalTripItemAEDValue.setTypeface(typefaceOpenSansSemiBold);
									llPersonalTripItemCross.setVisibility(View.VISIBLE);

									llPersonalTripItemAmount.addView(llPersonalTripItemAmountItem);
									llPersonalTripItemPrice.addView(llPersonalTripItemPriceItem);
								}
							}
						});
					}
				}


				dialogMealSub.dismiss();
				dialogMealMain.dismiss();
			}
		});
		btnBackinslecteditem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogMealSub.dismiss();
			}
		});
		ivCancelMealList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogMealSub.dismiss();
			}
		});
//		dialogMealSub.show();
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialogMealSub.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = screenHeight;
		dialogMealSub.show();
		dialogMealSub.getWindow().setAttributes(lp);
	}
}
