package com.winit.airarabia;

import java.util.Calendar;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.controls.CustomDialog;
import com.winit.airarabia.objects.AirBookDO;
import com.winit.airarabia.objects.AirPriceQuoteDO;
import com.winit.airarabia.objects.ModifiedPNRResDO;
import com.winit.airarabia.objects.PTC_FareBreakdownDO;
import com.winit.airarabia.objects.PaymentDO;
import com.winit.airarabia.utils.AnalyticsApplication;
import com.winit.airarabia.utils.StringUtils;
import com.winit.airarabia.utils.AnalyticsApplication.TrackerName;
import com.winit.airarabia.webaccess.Response;
import com.winit.airarabia.wheel.widget.WheelView;
import com.winit.airarabia.wheel.widget.adapters.DateArrayAdapter;

public class PaymentActivity extends BaseActivity implements DataListener {

	private LinearLayout llpayment;
	private TextView tvPaymentTotalFlightsServicesCharge, tvPaymentTotalAdminCharge, tvPaymentTotalCharge,
			tvPaymentTotalFlightsServicesText, tvPaymentTotalAdminText, tvPaymentTotalText, tvHeaderSecurePayment;
	private EditText etCardholderName, etCardNumber, etSecurityCode, edCardExpiryDate;
	private final String FLIGHT_BOOK = "FLIGHT_BOOK";
	private AirPriceQuoteDO airPriceQuoteDO;
	private PaymentDO paymentDO;
	private ModifiedPNRResDO modifiedPNRResDO;
	private WheelView wheelMonth, wheelYear;
	private String months[], years[];
	private boolean isManageBook = false;
	private String VISA = "Visa", MASTER_CARD = "Mastercard", CARD_TYPE = "Card Type";
	private String strBalanceToPayValue = "";
	private boolean isDirectPayment = false;
	private ImageView ivVisaLogo;
	private TextView btn_totalprice, tvPaymentCardInfo, tvPaymentCardInfo2;

	private boolean isDateShown = false;

	private PaymentActivity.BCR bcr;

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
		airPriceQuoteDO = (AirPriceQuoteDO) getIntent().getExtras()
				.getSerializable(AppConstants.BOOKING_FLIGHT_AIRPRICE);
		if (AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify != null
				&& AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.size() > 0)
			isManageBook = true;
		if (getIntent().hasExtra(AppConstants.MODIFIED_RES_QRY))
			modifiedPNRResDO = (ModifiedPNRResDO) getIntent().getExtras()
					.getSerializable(AppConstants.MODIFIED_RES_QRY);

		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		intentFilter.addAction(AppConstants.BOOK_SUCCESS);
		registerReceiver(bcr, intentFilter);
		tvHeaderTitle.setText(getString(R.string.Payment));

		llpayment = (LinearLayout) layoutInflater.inflate(R.layout.payment, null);

		llMiddleBase.addView(llpayment, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		tvHeaderSecurePayment = (TextView) llpayment.findViewById(R.id.tvHeaderSecurePayment);

		tvPaymentTotalFlightsServicesCharge = (TextView) llpayment
				.findViewById(R.id.tvPaymentTotalFlightsServicesCharge);
		tvPaymentTotalAdminCharge = (TextView) llpayment.findViewById(R.id.tvPaymentTotalAdminCharge);
		tvPaymentTotalCharge = (TextView) llpayment.findViewById(R.id.tvPaymentTotalCharge);
		tvPaymentTotalFlightsServicesText = (TextView) llpayment.findViewById(R.id.tvPaymentTotalFlightsServicesText);
		tvPaymentTotalAdminText = (TextView) llpayment.findViewById(R.id.tvPaymentTotalAdminText);
		tvPaymentTotalText = (TextView) llpayment.findViewById(R.id.tvPaymentTotalText);

		etCardholderName = (EditText) llpayment.findViewById(R.id.etCardholderName);
		etCardNumber = (EditText) llpayment.findViewById(R.id.etCardNumber);
		edCardExpiryDate = (EditText) llpayment.findViewById(R.id.tvCardExpiryDate);
		etSecurityCode = (EditText) llpayment.findViewById(R.id.etSecurityCode);
		ivVisaLogo = (ImageView) llpayment.findViewById(R.id.ivVisaLogo);
		btn_totalprice = (TextView) llpayment.findViewById(R.id.btn_totalprice);

		paymentDO = new PaymentDO();

		setTypeFaceSansRegular(llpayment);

		btn_totalprice.setTypeface(typefaceOpenSansSemiBold);

		btnBack.setTypeface(typefaceOpenSansSemiBold);
		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(getString(R.string.ConfirmPayment));

		// tvPaymentTotalFlightsServicesText.setTypeface(typefaceOpenSansRegular);
		// tvPaymentTotalAdminCharge.setTypeface(typefaceOpenSansRegular);
		// tvPaymentTotalCharge.setTypeface(typefaceOpenSansRegular);
		edCardExpiryDate.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				hideKeyBoard(v);
			}
		});
		edCardExpiryDate.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.performClick();
				return false;
			}
		});

		btn_totalprice.setTypeface(typefaceOpenSansSemiBold);
		tvHeaderSecurePayment.setTypeface(typefaceOpenSansSemiBold);
		// *********************card details massege************************
		tvPaymentCardInfo = (TextView) llpayment.findViewById(R.id.tvPaymentCardInfo);
		tvPaymentCardInfo2 = (TextView) llpayment.findViewById(R.id.tvPaymentCardInfo2);
		String route = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
		String selcurrencycode = AppConstants.CurrencyCodeAfterExchange;
		String baseCurrency = AppConstants.CurrencyCodeActual;
		
		if (route.equalsIgnoreCase("3O")) {
			if (selcurrencycode.equalsIgnoreCase("MAD")) {
				tvPaymentCardInfo.setText("" + getString(R.string.payment_currency_mad));
				//tvPaymentCardInfo.setText("" + getString(R.string.payment_currency_other));
				tvPaymentCardInfo2.setText("" + String.format(getString(R.string.payment_accept), "MAD"));
			} else if (selcurrencycode.equalsIgnoreCase("EUR")) {
				tvPaymentCardInfo.setText("" + getString(R.string.payment_currency_EUR));
				//tvPaymentCardInfo.setText("" + getString(R.string.payment_currency_other));
				tvPaymentCardInfo2.setText("" + String.format(getString(R.string.payment_accept), "EUR"));
			}else{
				tvPaymentCardInfo.setText("" + getString(R.string.payment_currency_other));
				tvPaymentCardInfo2.setText("" + String.format(getString(R.string.payment_accept), "EUR"));
			}
		} else {
			tvPaymentCardInfo.setText("" + getString(R.string.payment_currency_other));
			tvPaymentCardInfo2.setText("" + String.format(getString(R.string.payment_accept), baseCurrency));
			
			/*  if(route.equalsIgnoreCase("G9")){ tvPaymentCardInfo2.setText("* "
			 +String.format(getString(R.string.payment_accept)," AED")); }else
			  if(route.equalsIgnoreCase("3O")){ tvPaymentCardInfo2.setText("* "
			 +String.format(getString(R.string.payment_accept)," EUR")); }else
			 if(route.equalsIgnoreCase("E5")){ tvPaymentCardInfo2.setText("* "
			 +String.format(getString(R.string.payment_accept)," EGP")); }else
			 if(route.equalsIgnoreCase("9P")){ tvPaymentCardInfo2.setText("* "
			 +String.format(getString(R.string.payment_accept)," JOD")); }*/
		}
		/*if (route.equalsIgnoreCase("3O")){
			tvPaymentCardInfo2.setText("" + String.format(getString(R.string.payment_accept), "EUR"));
		}else{
			tvPaymentCardInfo2.setText("" + String.format(getString(R.string.payment_accept), baseCurrency));
		}
		tvPaymentCardInfo.setText("" + getString(R.string.payment_currency_other));*/

	}

	@Override
	public void bindingControl() {

		Calendar calendar = Calendar.getInstance();
		edCardExpiryDate.setTag(R.string.hala_flight_tag, calendar.get(Calendar.MONTH));
		edCardExpiryDate.setTag(R.string.hala_name_tag, 1);

		edCardExpiryDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeyBoard(v);
				trackEvent("Payment", AppConstants.CardExpiration, "");
				if (!isDateShown)
					showNumberPicker(edCardExpiryDate);
				if (customDialog == null)
					showNumberPicker(edCardExpiryDate);
			}
		});

		if (isManageBook) {
			tvPaymentTotalFlightsServicesText.setText(getString(R.string.ReservationCredit));
			tvPaymentTotalAdminText.setText(getString(R.string.TotalAmount) + "(Flights + Services)");

			tvPaymentTotalFlightsServicesCharge
					.setText(AppConstants.CurrencyCodeAfterExchange + " " + updateCurrencyByFactor(
							modifiedPNRResDO.vecTravelerCnxModAddResBalancesDO.get(0).CurrentTotalPayments, 2));

			tvPaymentTotalAdminCharge.setText(AppConstants.CurrencyCodeAfterExchange + " "
					+ updateCurrencyByFactor(modifiedPNRResDO.TotalPriceCC, 2));

			strBalanceToPayValue = StringUtils.getStrFloatFromString(modifiedPNRResDO.TotalAmountDueCC);

			if (StringUtils.getFloat(strBalanceToPayValue) <= 0.0f) {
				strBalanceToPayValue = StringUtils.getStrFloatDiffFromString(
						modifiedPNRResDO.vecTravelerCnxModAddResBalancesDO.get(0).CurrentTotalCharges,
						modifiedPNRResDO.TotalPriceCC);

				tvHeaderSecurePayment.setVisibility(View.GONE);
				etCardholderName.setVisibility(View.GONE);
				etCardNumber.setVisibility(View.GONE);
				edCardExpiryDate.setVisibility(View.GONE);
				etSecurityCode.setVisibility(View.GONE);

				tvPaymentTotalText.setText(getString(R.string.CreditBalance));
				tvPaymentTotalCharge.setText(
						AppConstants.CurrencyCodeAfterExchange + " " + updateCurrencyByFactor(strBalanceToPayValue, 2));
				isDirectPayment = true;
			} else {
				tvPaymentTotalText.setText(getString(R.string.BalanceToPay));
				tvPaymentTotalCharge.setText(AppConstants.CurrencyCodeAfterExchange + " "
						+ updateCurrencyByFactor(modifiedPNRResDO.TotalAmountDueCC, 2));
			}
		} else {
			if (airPriceQuoteDO != null && airPriceQuoteDO.vecPricedItineraryDOs != null
					&& airPriceQuoteDO.vecPricedItineraryDOs.size() > 0) {

//==============Added on 28Feb2018 for getting total fare without admin fee and add admin fees separately===============================
				float adminFees = 0, totalFareAmount = 0, adminFeesINF = 0;
				for (int i=0; i<airPriceQuoteDO.vecPricedItineraryDOs.get(0).vecPTC_FareBreakdownDOs.size(); i++){
					PTC_FareBreakdownDO fareBreakdownDO = airPriceQuoteDO.vecPricedItineraryDOs.get(0).vecPTC_FareBreakdownDOs.get(i);
					for (int j=0; j<fareBreakdownDO.vecFees.size(); j++){

						if (fareBreakdownDO.vecFees.get(j).feeCode.equalsIgnoreCase("CC/Transaction Fees")
								&& !fareBreakdownDO.code.equalsIgnoreCase("INF"))
							adminFees += StringUtils.getFloat(fareBreakdownDO.vecFees.get(j).amount.toString());
						else if (fareBreakdownDO.vecFees.get(j).feeCode.equalsIgnoreCase("CC/Transaction Fees")
								&& fareBreakdownDO.code.equalsIgnoreCase("INF")){
							adminFeesINF = StringUtils.getFloat(fareBreakdownDO.vecFees.get(j).amount.toString()) * StringUtils.getFloat(fareBreakdownDO.quantity.toString());
						}
					}
				}
				adminFees += adminFeesINF;
				totalFareAmount = StringUtils.getFloat(airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.amount);
				totalFareAmount -= adminFees;
//=======================================================================================================
				tvPaymentTotalFlightsServicesCharge.setText(AppConstants.CurrencyCodeAfterExchange + " "
						+ updateCurrencyByFactor(totalFareAmount +"", 2));

				String aditionalFare = StringUtils.getStrFloatDiffFromString(
						airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalEquivFareWithCCFee.amount,
						airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.amount);
				tvPaymentTotalAdminCharge.setText(
						AppConstants.CurrencyCodeAfterExchange + " " + updateCurrencyByFactor((StringUtils.getFloat(aditionalFare)+adminFees) +"", 2));
				tvPaymentTotalCharge.setText(AppConstants.CurrencyCodeAfterExchange + " " + updateCurrencyByFactor(
						airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalEquivFareWithCCFee.amount, 2));
			}
		}

		btnSubmitNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				trackEvent("Payment", AppConstants.NextButtonPayment, "");

				if (isDirectPayment && isManageBook)
					callServiceModifyReservationMinus();
				else if (validateCreditCard() && isManageBook)
					callServiceModifyReservation();
				else if (validateCreditCard() && !isManageBook)
					callServiceBooking();
			}
		});

		// ============================newly added for visa
		// card====================================================
		etCardNumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				trackEvent("Payment", AppConstants.CardNumberField, "");

				if (etCardNumber != null && !etCardNumber.getText().toString().equalsIgnoreCase("")) {

					char[] tempCardNumber = etCardNumber.getText().toString().toCharArray();
					if (tempCardNumber.length != 0 && tempCardNumber[0] == '4') {
						ivVisaLogo.setVisibility(View.VISIBLE);
						ivVisaLogo.setImageDrawable(getResources().getDrawable(R.drawable.visa));

					} else if (tempCardNumber.length != 0 && tempCardNumber[0] == '5') {
						ivVisaLogo.setVisibility(View.VISIBLE);
						ivVisaLogo.setImageDrawable(getResources().getDrawable(R.drawable.master));

					} else
						ivVisaLogo.setVisibility(View.GONE);

				} else
					ivVisaLogo.setVisibility(View.GONE);

			}
		});

		etCardholderName.setFilters(new InputFilter[] { new InputFilter.AllCaps() });
		// etCardholderName.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence arg0, int arg1, int arg2, int
		// arg3) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
		// int arg3) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable arg0) {
		// if(!checkLangArabic())
		// {
		// String s=arg0.toString();
		// if(!s.equals(s.toUpperCase()))
		// {
		// s=s.toUpperCase();
		// etCardholderName.setText(s);
		// }
		// }
		// }
		// });

	}

	private void callServiceBooking() {
		showLoader("");
		if (new CommonBL(PaymentActivity.this, PaymentActivity.this).getAirBook(
				AppConstants.bookingFlightDO.requestParameterDO,
				airPriceQuoteDO.vecPricedItineraryDOs.get(0).vecOriginDestinationOptionDOs,
				AppConstants.bookingFlightDO.passengerInfoDO, paymentDO, AppConstants.bookingFlightDO.vecMealReqDOs,
				AppConstants.bookingFlightDO.vecBaggageRequestDOs, AppConstants.bookingFlightDO.vecInsrRequestDOs,
				AppConstants.bookingFlightDO.vecSeatRequestDOs, AppConstants.bookingFlightDO.vecSSRRequests)) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert), getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	private void callServiceModifyReservation() {
		showLoader("");

		paymentDO.curAmount = modifiedPNRResDO.TotalAmountDueCC;
		paymentDO.curCurrencyCode = airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.currencyCode;

		if (new CommonBL(PaymentActivity.this, PaymentActivity.this).getModifyReservation(
				AppConstants.bookingFlightDO.requestParameterDO,
				AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
				AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs, paymentDO,
				AppConstants.bookingFlightDO.pnr, AppConstants.bookingFlightDO.pnrType, "19")) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert), getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	private void callServiceModifyReservationMinus() {
		showLoader("");
		if (new CommonBL(PaymentActivity.this, PaymentActivity.this).getModifyReservation(
				AppConstants.bookingFlightDO.requestParameterDO,
				AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
				AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs, null,
				AppConstants.bookingFlightDO.pnr, AppConstants.bookingFlightDO.pnrType, "19")) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert), getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	@Override
	public void dataRetreived(Response data) {
		if (data != null) {
			switch (data.method) {
			case AIR_BOOK:
				if (data.data instanceof AirBookDO) {

					AirBookDO airBookDO = (AirBookDO) data.data;
					if (airBookDO != null) {
						if (!airBookDO.bookingID.equalsIgnoreCase("")) {
							googleEcommerce(airBookDO);
							moveToPaymentSummaryActivity(airBookDO);
						} else if (!airBookDO.errorMessage.equalsIgnoreCase("")) {
							if (airBookDO.errorMessage.contains("ogone.30051001")) {
								showCustomDialog(this, getString(R.string.Alert),
										(getString(R.string.payment_error_ogone30051001)) + "", getString(R.string.Ok),
										"", "PaymentActivityError");
							} else if (airBookDO.errorMessage.contains("ogone.30511001")) {
								showCustomDialog(this, getString(R.string.Alert),
										(getString(R.string.payment_error_ogone30511001)) + "", getString(R.string.Ok),
										"", "PaymentActivityError");
							} else {
								showCustomDialog(this, getString(R.string.Alert),
										(getString(R.string.payment_error_Default_message)) + "",
										getString(R.string.Ok), "", "PaymentActivityError");
							}

						} else {

							showCustomDialog(this, getString(R.string.Alert), getString(R.string.ErrorWhileProcessing),
									getString(R.string.Ok), "", FLIGHT_BOOK);

						}
					}
				} else if (data.data instanceof String) {
					if (((String) data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
						showCustomDialog(getApplicationContext(), getString(R.string.Alert),
								getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "",
								AppConstants.INTERNET_PROBLEM);
					else
						showCustomDialog(this, getString(R.string.Alert),
								getString(R.string.TechProblem) + getString(R.string.TryAgainAfter),
								getString(R.string.Ok), "", FLIGHT_BOOK);
				} else {
					showCustomDialog(this, getString(R.string.Alert), getString(R.string.Payment_Error),
							getString(R.string.Ok), "", FLIGHT_BOOK);
				}
				hideLoader();
				break;

			case MODIFY_RESERVATION:
				if (data.data instanceof AirBookDO) {
					AirBookDO airBookDO = (AirBookDO) data.data;
					if (!airBookDO.bookingID.equalsIgnoreCase("")) {
						moveToPaymentSummaryActivity(airBookDO);
					} else if (!airBookDO.errorMessage.equalsIgnoreCase(""))
						showCustomDialog(this, getString(R.string.Alert),
								getString(R.string.TechProblem) + getString(R.string.TryAgainAfter),
								getString(R.string.Ok), "", FLIGHT_BOOK);
					else
						showCustomDialog(this, getString(R.string.Alert), getString(R.string.Payment_Error),
								getString(R.string.Ok), "", FLIGHT_BOOK);
				} else if (data.data instanceof String) {
					if (((String) data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
						showCustomDialog(getApplicationContext(), getString(R.string.Alert),
								getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "",
								AppConstants.INTERNET_PROBLEM);
					else
						showCustomDialog(this, getString(R.string.Alert),
								getString(R.string.TechProblem) + getString(R.string.TryAgainAfter),
								getString(R.string.Ok), "", FLIGHT_BOOK);
				} else {
					showCustomDialog(this, getString(R.string.Alert), getString(R.string.Payment_Error),
							getString(R.string.Ok), "", FLIGHT_BOOK);
				}
				hideLoader();
				break;
			default:
				break;
			}
		} else {
			hideLoader();
			showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.TechProblem),
					getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
		}
	}

	private void googleEcommerce(AirBookDO airBookDO) {

		// TODO Auto-generated method stub
		Product product = new Product().setId(airBookDO.bookingID)
				.setName(airBookDO.vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs.get(0).flightNumber)
				.setCategory(AppConstants.flightType)
				.setPrice(Double.parseDouble(airBookDO.itinTotalFareDO.totalFare.amount))
				.setQuantity(airBookDO.adultCount + airBookDO.chdCount + airBookDO.infCount).setBrand("");

		ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
				.setTransactionId(airBookDO.bookingID)
				.setTransactionAffiliation(
						airBookDO.vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs.get(0).arrivalAirportCode)
				.setTransactionRevenue(Double.parseDouble(airBookDO.itinTotalFareDO.totalFare.amount))
				.setTransactionTax(0.00).setTransactionShipping(0.00);

		/*
		 * Product product = new Product().setId("PNR123545") .setName("mith")
		 * .setCategory("G9") .setBrand("Airarabia") .setPrice(20.00)
		 * .setQuantity(2); ProductAction productAction = new
		 * ProductAction(ProductAction.ACTION_PURCHASE)
		 * .setTransactionId("PNR123545") .setTransactionAffiliation("tickets")
		 * .setTransactionTax(1.02) .setTransactionShipping(2.00)
		 * .setTransactionRevenue(50.08);
		 */
		HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder().addProduct(product)
				.setProductAction(productAction);

		Tracker t = ((AnalyticsApplication) getApplication()).getTracker(TrackerName.ECOMMERCE_TRACKER);
		t.setScreenName("Payment Transaction");
		t.set("&cu", airBookDO.itinTotalFareDO.totalFare.currencyCode);// Set
																		// tracker
																		// currency
																		// to
																		// AED.
		t.send(builder.build());
	}

	private void moveToPaymentSummaryActivity(AirBookDO airBookDO) {
		Intent in = new Intent(PaymentActivity.this, PaymentSummaryActivity.class);
		in.putExtra(AppConstants.AIR_BOOK, airBookDO);
		if (isManageBook)
			in.putExtra(AppConstants.FROM, true);
		else
			in.putExtra(AppConstants.FROM, false);
		startActivity(in);
	}

	@Override
	public void onButtonYesClick(String from) {
		super.onButtonYesClick(from);
		if (from.equalsIgnoreCase(FLIGHT_BOOK) || from.equalsIgnoreCase(AppConstants.INTERNET_PROBLEM)
				/*|| from.equalsIgnoreCase("PaymentActivityError")*/)
			clickHome();
	}

	private boolean validateCreditCard() {
		if (etCardNumber.getText().toString().equalsIgnoreCase("")) {
			showCustomDialog(this, getString(R.string.Alert), getString(R.string.CardNumErrNew), getString(R.string.Ok),
					"", "");
			return false;
		} else if (!etCardNumber.getText().toString().equalsIgnoreCase("")
				&& cardType().toString().toLowerCase().equalsIgnoreCase("")) {
			showCustomDialog(this, getString(R.string.Alert), getString(R.string.CardNumInvalid),
					getString(R.string.Ok), "", "");
			return false;
		} else if (cardType().toString().toLowerCase().equalsIgnoreCase("") || cardNumberCount() < 16) {
			showCustomDialog(this, getString(R.string.Alert), getString(R.string.CardNumInvalid),
					getString(R.string.Ok), "", "");
			return false;
		} else if (edCardExpiryDate.getText().toString().equalsIgnoreCase("")) {
			if (edCardExpiryDate.getText().toString().equalsIgnoreCase("")) {
				showCustomDialog(this, getString(R.string.Alert), getString(R.string.alert_card_expirydate),
						getString(R.string.Ok), "", "");
				return false;
			} else {
				String[] strYear = edCardExpiryDate.getText().toString().split("/");
				if (strYear.length > 1) {
					Calendar cal = Calendar.getInstance();
					int year = cal.get(Calendar.YEAR);
					int mYear = StringUtils.getInt(strYear[1]);
					if (year > mYear) {
						showCustomDialog(this, getString(R.string.Alert), getString(R.string.alert_card_expirydate),
								getString(R.string.Ok), "", "");
						return false;
					}
				} else {
					showCustomDialog(this, getString(R.string.Alert), getString(R.string.alert_card_expirydate),
							getString(R.string.Ok), "", "");
					return false;
				}
			}
		}

		if (etCardholderName.getText().toString().equalsIgnoreCase("")) {
			showCustomDialog(this, getString(R.string.Alert), getString(R.string.CardNameErr), getString(R.string.Ok),
					"", "");
			return false;
		} else if (etSecurityCode.getText().toString().equalsIgnoreCase("")) {
			showCustomDialog(this, getString(R.string.Alert), getString(R.string.CardSecurityErrNew),
					getString(R.string.Ok), "", "");
			return false;
		} else {
			if (cardType().toLowerCase().equalsIgnoreCase("VISA"))
				paymentDO.cardType = "2";
			else if (cardType().toString().toLowerCase().equalsIgnoreCase("MASTER_CARD"))
				paymentDO.cardType = "1";
			else
				paymentDO.cardType = "0";

			String strDate = edCardExpiryDate.getText().toString();
			paymentDO.cardHolderName = etCardholderName.getText().toString();
			paymentDO.cardNo = etCardNumber.getText().toString();
			paymentDO.expireDate = strDate.substring(0, 2) + strDate.substring(strDate.length() - 2, strDate.length());
			paymentDO.seriesCode = etSecurityCode.getText().toString();
			paymentDO.amount = airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFareWithCCFee.amount;
			paymentDO.currencyCode = airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFareWithCCFee.currencyCode;

			return true;
		}

	}

	private String[] getMonthArray(int start, int end) {
		int size = end - start + 1;
		String datesArray[] = new String[size];

		// String month[] = resources.getStringArray(R.array.ArrMonth);

		for (int i = 0; i < size; i++) {
			datesArray[i] = String.valueOf(start + i);
		}
		return datesArray;
	}

	private String[] getYearArray(int start, int end) {
		int size = end - start + 1;
		String datesArray[] = new String[size];
		for (int i = 0; i < size; i++) {
			datesArray[i] = String.valueOf(start + i);
		}
		return datesArray;
	}

	/**
	 * show Number Picker
	 * 
	 * @param textView1
	 */
	protected void showNumberPicker(final TextView textView1) {
		LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.wheel_num_picker, null);
		TextView btnYes = (TextView) linearLayout.findViewById(R.id.btnPaymentCardExpDateYes);
		btnYes.setText(R.string.Ok);
		customDialog = new CustomDialog(PaymentActivity.this, linearLayout, AppConstants.DEVICE_WIDTH - 20,
				AppConstants.DEVICE_HEIGHT / 2, true);
		wheelMonth = (WheelView) linearLayout.findViewById(R.id.wheelMonth);
		wheelYear = (WheelView) linearLayout.findViewById(R.id.wheelYear);
		Calendar calExpDate = Calendar.getInstance();
		months = getMonthArray(1, 12);
		years = getYearArray(calExpDate.get(Calendar.YEAR),
				calExpDate.get(Calendar.YEAR) + AppConstants.EXPIRY_YEAR_LIMIT);
		wheelMonth.setCenterDrawable(R.drawable.btn_red);
		wheelYear.setCenterDrawable(R.drawable.btn_red);
		wheelMonth.setViewAdapter(new DateArrayAdapter(PaymentActivity.this, months, 0));
		wheelYear.setViewAdapter(new DateArrayAdapter(PaymentActivity.this, years, 0));
		if (textView1.getTag(R.string.hala_flight_tag) != null)
			wheelMonth.setCurrentItem((Integer) textView1.getTag(R.string.hala_flight_tag));
		if (textView1.getTag(R.string.hala_name_tag) != null)
			wheelYear.setCurrentItem((Integer) textView1.getTag(R.string.hala_name_tag));

		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				textView1.setTag(R.string.hala_flight_tag, wheelMonth.getCurrentItem());
				textView1.setTag(R.string.hala_name_tag, wheelYear.getCurrentItem());

				String strTextMonth = wheelMonth.getCurrentItem() + 1 + "";
				String strTextYear = years[wheelYear.getCurrentItem()] + "";
				textView1.setText((strTextMonth.length() == 1 ? "0" + strTextMonth : strTextMonth) + "/" + strTextYear);

				if (customDialog != null && customDialog.isShowing()) {
					customDialog.dismiss();
					isDateShown = false;
					customDialog = null;
				}
				etSecurityCode.requestFocus();
			}
		});
		customDialog.show();

		customDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				isDateShown = false;
			}
		});

		isDateShown = true;
	}

	private String cardType() {
		if (etCardNumber != null && !etCardNumber.toString().equalsIgnoreCase("")) {

			char[] tempCardNumber = etCardNumber.getText().toString().toCharArray();
			if (tempCardNumber[0] == '4') {
				// etCardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				// R.drawable.visa, 0);
				return "VISA";
			} else if (tempCardNumber[0] == '5') {
				// etCardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				// R.drawable.master, 0);
				return "MASTER_CARD";
			} else
				return "";
		} else
			return "";
	}

	private int cardNumberCount() {
		if (etCardNumber != null && !etCardNumber.toString().equalsIgnoreCase("")) {
			char[] tempCardNumber = etCardNumber.getText().toString().toCharArray();
			return tempCardNumber.length;
		} else
			return 0;
	}
}

// package com.winit.airarabia;
//
// import java.util.Calendar;
//
// import android.content.BroadcastReceiver;
// import android.content.Context;
// import android.content.Intent;
// import android.text.Editable;
// import android.text.TextWatcher;
// import android.view.View;
// import android.view.View.OnClickListener;
// import android.view.ViewGroup.LayoutParams;
// import android.widget.EditText;
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
// import com.winit.airarabia.objects.ModifiedPNRResDO;
// import com.winit.airarabia.objects.PaymentDO;
// import com.winit.airarabia.utils.StringUtils;
// import com.winit.airarabia.webaccess.Response;
// import com.winit.airarabia.wheel.widget.WheelView;
// import com.winit.airarabia.wheel.widget.adapters.DateArrayAdapter;
//
// public class PaymentActivity extends BaseActivity implements DataListener {
//
// private LinearLayout llpayment;
// private TextView tvPaymentTotalFlightsServicesCharge,
// tvPaymentTotalAdminCharge, tvPaymentTotalCharge,
// tvPaymentTotalFlightsServicesText, tvPaymentTotalAdminText,
// tvPaymentTotalText, tvCardExpiryDate,tvHeaderSecurePayment;
// private EditText etCardholderName, etCardNumber, etSecurityCode;
// private final String FLIGHT_BOOK = "FLIGHT_BOOK";
// private AirPriceQuoteDO airPriceQuoteDO;
// private PaymentDO paymentDO;
// private ModifiedPNRResDO modifiedPNRResDO;
// private WheelView wheelMonth, wheelYear;
// private String months[], years[];
// private boolean isManageBook = false;
// private String VISA = "Visa", MASTER_CARD = "Mastercard", CARD_TYPE = "Card
// Type";
// private String strBalanceToPayValue = "";
// private boolean isDirectPayment = false;
// private ImageView ivvisalogo;
//
// private PaymentActivity.BCR bcr;
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
// airPriceQuoteDO = (AirPriceQuoteDO) getIntent().getExtras()
// .getSerializable(AppConstants.BOOKING_FLIGHT_AIRPRICE);
// if (AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify != null
// && AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.size() >
// 0)
// isManageBook = true;
// if (getIntent().hasExtra(AppConstants.MODIFIED_RES_QRY))
// modifiedPNRResDO = (ModifiedPNRResDO) getIntent().getExtras()
// .getSerializable(AppConstants.MODIFIED_RES_QRY);
//
// bcr = new BCR();
// intentFilter.addAction(AppConstants.HOME_CLICK);
// intentFilter.addAction(AppConstants.BOOK_SUCCESS);
// registerReceiver(bcr, intentFilter);
// tvHeaderTitle.setText(getString(R.string.Payment));
//
// llpayment = (LinearLayout) layoutInflater.inflate(R.layout.payment,
// null);
// llMiddleBase.addView(llpayment, LayoutParams.MATCH_PARENT,
// LayoutParams.MATCH_PARENT);
//
//
// tvHeaderSecurePayment = (TextView) llpayment
// .findViewById(R.id.tvHeaderSecurePayment);
//
// tvPaymentTotalFlightsServicesCharge = (TextView) llpayment
// .findViewById(R.id.tvPaymentTotalFlightsServicesCharge);
// tvPaymentTotalAdminCharge = (TextView) llpayment
// .findViewById(R.id.tvPaymentTotalAdminCharge);
// tvPaymentTotalCharge = (TextView) llpayment
// .findViewById(R.id.tvPaymentTotalCharge);
// tvPaymentTotalFlightsServicesText = (TextView) llpayment
// .findViewById(R.id.tvPaymentTotalFlightsServicesText);
// tvPaymentTotalAdminText = (TextView) llpayment
// .findViewById(R.id.tvPaymentTotalAdminText);
// tvPaymentTotalText = (TextView) llpayment
// .findViewById(R.id.tvPaymentTotalText);
//
// etCardholderName = (EditText) llpayment
// .findViewById(R.id.etCardholderName);
// etCardNumber = (EditText) llpayment.findViewById(R.id.etCardNumber);
// tvCardExpiryDate = (TextView) llpayment
// .findViewById(R.id.tvCardExpiryDate);
// etSecurityCode = (EditText) llpayment.findViewById(R.id.etSecurityCode);
// ivvisalogo=(ImageView) llpayment.findViewById(R.id.ivvisalogo);
// paymentDO = new PaymentDO();
//
// btnSubmitNext.setVisibility(View.VISIBLE);
// btnSubmitNext.setText(getString(R.string.ConfirmPayment));
// }
//
// @Override
// public void bindingControl() {
//
// Calendar calendar = Calendar.getInstance();
// tvCardExpiryDate.setTag(R.string.hala_flight_tag,
// calendar.get(Calendar.MONTH));
// tvCardExpiryDate.setTag(R.string.hala_name_tag,
// 1);
//
// tvCardExpiryDate.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// showNumberPicker(tvCardExpiryDate);
// }
// });
//
// if (isManageBook) {
// tvPaymentTotalFlightsServicesText
// .setText(getString(R.string.ReservationCredit));
// tvPaymentTotalAdminText.setText(getString(R.string.TotalAmount)
// + "(Flights + Services)");
//
// tvPaymentTotalFlightsServicesCharge
// .setText(airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.currencyCode
// + " "
// +
// modifiedPNRResDO.vecTravelerCnxModAddResBalancesDO.get(0).CurrentTotalPayments);
//
// tvPaymentTotalAdminCharge
// .setText(airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.currencyCode
// + " " + modifiedPNRResDO.TotalPriceCC);
//
// strBalanceToPayValue =
// StringUtils.getStrFloatFromString(modifiedPNRResDO.TotalAmountDueCC);
//
// if(StringUtils.getFloat(strBalanceToPayValue) <= 0.0f)
// {
// strBalanceToPayValue =
// StringUtils.getStrFloatDiffFromString(modifiedPNRResDO.vecTravelerCnxModAddResBalancesDO.get(0).CurrentTotalCharges,
// modifiedPNRResDO.TotalPriceCC);
//
// tvHeaderSecurePayment.setVisibility(View.GONE);
// etCardholderName.setVisibility(View.GONE);
// etCardNumber.setVisibility(View.GONE);
// tvCardExpiryDate.setVisibility(View.GONE);
// etSecurityCode.setVisibility(View.GONE);
//
// tvPaymentTotalText.setText(getString(R.string.CreditBalance));
// tvPaymentTotalCharge.setText(airPriceQuoteDO.vecPricedItineraryDOs
// .get(0).totalFare.currencyCode
// + " "
// + strBalanceToPayValue);
// isDirectPayment = true;
// }
// else
// {
// tvPaymentTotalText.setText(getString(R.string.BalanceToPay));
// tvPaymentTotalCharge.setText(airPriceQuoteDO.vecPricedItineraryDOs
// .get(0).totalFare.currencyCode
// + " "
// + modifiedPNRResDO.TotalAmountDueCC);
// }
// } else {
// if (airPriceQuoteDO != null
// && airPriceQuoteDO.vecPricedItineraryDOs != null
// && airPriceQuoteDO.vecPricedItineraryDOs.size() > 0) {
//
// tvPaymentTotalFlightsServicesCharge
// .setText(airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.currencyCode
// + " "
// + airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.amount);
//
// String aditionalFare = StringUtils.getStrFloatDiffFromString(
// airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalEquivFareWithCCFee.amount,
// airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.amount);
// tvPaymentTotalAdminCharge
// .setText(airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.currencyCode
// + " "
// + aditionalFare);
// tvPaymentTotalCharge
// .setText(airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.currencyCode
// + " "
// +
// airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalEquivFareWithCCFee.amount);
// }
// }
//
// btnSubmitNext.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// if (isDirectPayment && isManageBook)
// callServiceModifyReservationMinus();
// else if (validateCreditCard() && isManageBook)
// callServiceModifyReservation();
// else if (validateCreditCard() && !isManageBook)
// callServiceBooking();
// }
// });
//
//// ============================newly added for visa
// card====================================================
// etCardNumber.addTextChangedListener(new TextWatcher() {
//
// @Override
// public void onTextChanged(CharSequence s, int start, int before, int count) {
// // TODO Auto-generated method stub
//
// }
//
// @Override
// public void beforeTextChanged(CharSequence s, int start, int count,
// int after) {
// // TODO Auto-generated method stub
//
// }
//
// @Override
// public void afterTextChanged(Editable s) {
// // TODO Auto-generated method stub
// if (etCardNumber != null &&
// !etCardNumber.getText().toString().equalsIgnoreCase("")) {
//
// char [] tempCardNumber = etCardNumber.getText().toString().toCharArray();
// if (tempCardNumber.length!=0 && tempCardNumber[0] =='4') {
// ivvisalogo.setVisibility(View.VISIBLE);
// ivvisalogo.setImageDrawable(getResources().getDrawable(R.drawable.visa));
//
// }
// else if (tempCardNumber.length!=0 && tempCardNumber[0] =='5') {
// ivvisalogo.setVisibility(View.VISIBLE);
// ivvisalogo.setImageDrawable(getResources().getDrawable(R.drawable.master));
//
// }
// else
// ivvisalogo.setVisibility(View.GONE);
//
// }
// else
// ivvisalogo.setVisibility(View.GONE);
//
//
// }
// });
// }
//
// private void callServiceBooking() {
// showLoader("");
// if (new CommonBL(PaymentActivity.this, PaymentActivity.this).getAirBook(
// AppConstants.bookingFlightDO.requestParameterDO,
// airPriceQuoteDO.vecPricedItineraryDOs.get(0).vecOriginDestinationOptionDOs,
// AppConstants.bookingFlightDO.passengerInfoDO, paymentDO,
// AppConstants.bookingFlightDO.vecMealReqDOs,
// AppConstants.bookingFlightDO.vecBaggageRequestDOs,
// AppConstants.bookingFlightDO.vecInsrRequestDOs,
// AppConstants.bookingFlightDO.vecSeatRequestDOs,
// AppConstants.bookingFlightDO.vecSSRRequests))
// {}
// else
// {
// hideLoader();
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.InternetProblem),
// getString(R.string.Ok), "", "");
// }
// }
//
// private void callServiceModifyReservation() {
// showLoader("");
//
// paymentDO.curAmount = modifiedPNRResDO.TotalAmountDueCC;
// paymentDO.curCurrencyCode =
// airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFare.currencyCode;
//
// if (new CommonBL(PaymentActivity.this, PaymentActivity.this)
// .getModifyReservation(
// AppConstants.bookingFlightDO.requestParameterDO,
// AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
// AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
// paymentDO, AppConstants.bookingFlightDO.pnr,
// AppConstants.bookingFlightDO.pnrType, "19")) {
// } else
// {
// hideLoader();
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.InternetProblem),
// getString(R.string.Ok), "", "");
// }
// }
//
// private void callServiceModifyReservationMinus() {
// showLoader("");
// if (new CommonBL(PaymentActivity.this,
// PaymentActivity.this).getModifyReservation(
// AppConstants.bookingFlightDO.requestParameterDO,
// AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
// AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
// null, AppConstants.bookingFlightDO.pnr, AppConstants.bookingFlightDO.pnrType,
// "19")) {
// } else
// {
// hideLoader();
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.InternetProblem),
// getString(R.string.Ok), "", "");
// }
// }
//
// @Override
// public void dataRetreived(Response data) {
// if (data != null) {
// switch (data.method) {
// case AIR_BOOK:
// if (data.data instanceof AirBookDO)
// {
// AirBookDO airBookDO = (AirBookDO) data.data;
// if(airBookDO != null)
// {
// if (!airBookDO.bookingID.equalsIgnoreCase("")) {
// moveToPaymentSummaryActivity(airBookDO);
// } else if (!airBookDO.errorMessage.equalsIgnoreCase("")) {
// if (airBookDO.errorMessage
// .equalsIgnoreCase(AppConstants.SESSIONE_EXP_TEXT)) {
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.SessionExpired) + " "
// + getString(R.string.StartAgain),
// getString(R.string.Ok), "", FLIGHT_BOOK);
// } else if(airBookDO.errorMessage
// .equalsIgnoreCase(AppConstants.CARD_EXP_DATE_INVALID_TEXT)
// || airBookDO.errorMessage
// .equalsIgnoreCase(AppConstants.CARD_PAYMENT_FAIL_TEXT))
// showCustomDialog(this, getString(R.string.Alert),
// airBookDO.errorMessage/*getString(R.string.TechProblem)+getString(R.string.TryAgainAfter)*/,
// getString(R.string.Ok), "", "");
// else if(airBookDO.errorMessage
// .equalsIgnoreCase(AppConstants.UNKNOWN_ERROR)
// || airBookDO.errorMessage
// .equalsIgnoreCase(AppConstants.CARD_PAYMENT_FAIL_TEXT))
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.TechProblem)+getString(R.string.TryAgainAfter),
// getString(R.string.Ok), "", FLIGHT_BOOK);
// else
// showCustomDialog(this, getString(R.string.Alert),
// airBookDO.errorMessage,
// getString(R.string.Ok), "", FLIGHT_BOOK);
// } else
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.Payment_Error),
// getString(R.string.Ok), "", FLIGHT_BOOK);
// }
// else
// {
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.ErrorWhileProcessing),
// getString(R.string.Ok), "", FLIGHT_BOOK);
// }
// } else if (data.data instanceof String) {
// if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
// showCustomDialog(getApplicationContext(), getString(R.string.Alert),
// getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "",
// AppConstants.INTERNET_PROBLEM);
// else
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.TechProblem)+getString(R.string.TryAgainAfter),
// getString(R.string.Ok), "",
// FLIGHT_BOOK);
// } else {
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.Payment_Error),
// getString(R.string.Ok), "", FLIGHT_BOOK);
// }
// hideLoader();
// break;
//
// case MODIFY_RESERVATION:
// if (data.data instanceof AirBookDO) {
// AirBookDO airBookDO = (AirBookDO) data.data;
// if (!airBookDO.bookingID.equalsIgnoreCase("")) {
// moveToPaymentSummaryActivity(airBookDO);
// } else if (!airBookDO.errorMessage.equalsIgnoreCase(""))
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.TechProblem)+getString(R.string.TryAgainAfter),
// getString(R.string.Ok),
// "", FLIGHT_BOOK);
// else
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.Payment_Error),
// getString(R.string.Ok), "", FLIGHT_BOOK);
// } else if (data.data instanceof String) {
// if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
// showCustomDialog(getApplicationContext(), getString(R.string.Alert),
// getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "",
// AppConstants.INTERNET_PROBLEM);
// else
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.TechProblem)+getString(R.string.TryAgainAfter),
// getString(R.string.Ok), "",
// FLIGHT_BOOK);
// } else {
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.Payment_Error),
// getString(R.string.Ok), "", FLIGHT_BOOK);
// }
// hideLoader();
// break;
// default:
// break;
// }
// } else {
// hideLoader();
// showCustomDialog(getApplicationContext(), getString(R.string.Alert),
// getString(R.string.TechProblem), getString(R.string.Ok), "",
// AppConstants.INTERNET_PROBLEM);
// }
// }
//
// private void moveToPaymentSummaryActivity(AirBookDO airBookDO) {
// Intent in = new Intent(PaymentActivity.this, PaymentSummaryActivity.class);
// in.putExtra(AppConstants.AIR_BOOK, airBookDO);
// if (isManageBook)
// in.putExtra(AppConstants.FROM, true);
// else
// in.putExtra(AppConstants.FROM, false);
// startActivity(in);
// }
//
// @Override
// public void onButtonYesClick(String from) {
// super.onButtonYesClick(from);
// if (from.equalsIgnoreCase(FLIGHT_BOOK) ||
// from.equalsIgnoreCase(AppConstants.INTERNET_PROBLEM))
// clickHome();
// }
//
// private boolean validateCreditCard() {
// if (etCardNumber.getText().toString().equalsIgnoreCase("")) {
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.CardNumErrNew), getString(R.string.Ok), "",
// "");
// return false;
// }
// else if (!etCardNumber.getText().toString().equalsIgnoreCase("")
// && cardType().toString().toLowerCase().equalsIgnoreCase("")) {
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.CardNumInvalid), getString(R.string.Ok), "",
// "");
// return false;
// }
// else if(cardType().toString().toLowerCase().equalsIgnoreCase("")||
// cardNumberCount()<16 ) {
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.CardNumInvalid), getString(R.string.Ok), "",
// "");
// return false;
// }
// else if(tvCardExpiryDate.getText().toString().equalsIgnoreCase("")) {
// if (tvCardExpiryDate.getText().toString().equalsIgnoreCase("")) {
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.alert_card_expirydate),
// getString(R.string.Ok), "", "");
// return false;
// } else {
// String[] strYear = tvCardExpiryDate.getText().toString()
// .split("/");
// if (strYear.length > 1) {
// Calendar cal = Calendar.getInstance();
// int year = cal.get(Calendar.YEAR);
// int mYear = StringUtils.getInt(strYear[1]);
// if (year > mYear) {
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.alert_card_expirydate),
// getString(R.string.Ok), "", "");
// return false;
// }
// } else {
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.alert_card_expirydate),
// getString(R.string.Ok), "", "");
// return false;
// }
// }
// }
//
// if (etCardholderName.getText().toString().equalsIgnoreCase("")) {
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.CardNameErr), getString(R.string.Ok),
// "", "");
// return false;
// }else if (etSecurityCode.getText().toString().equalsIgnoreCase("")) {
// showCustomDialog(this, getString(R.string.Alert),
// getString(R.string.CardSecurityErrNew),
// getString(R.string.Ok), "", "");
// return false;
// } else {
// if (cardType().toLowerCase()
// .equalsIgnoreCase("VISA"))
// paymentDO.cardType = "2";
// else if (cardType().toString().toLowerCase()
// .equalsIgnoreCase("MASTER_CARD"))
// paymentDO.cardType = "1";
// else
// paymentDO.cardType = "0";
//
// String strDate = tvCardExpiryDate.getText().toString();
// paymentDO.cardHolderName = etCardholderName.getText().toString();
// paymentDO.cardNo = etCardNumber.getText().toString();
// paymentDO.expireDate = strDate.substring(0, 2)
// + strDate.substring(strDate.length() - 2, strDate.length());
// paymentDO.seriesCode = etSecurityCode.getText().toString();
// paymentDO.amount =
// airPriceQuoteDO.vecPricedItineraryDOs.get(0).totalFareWithCCFee.amount;
// paymentDO.currencyCode = airPriceQuoteDO.vecPricedItineraryDOs
// .get(0).totalFareWithCCFee.currencyCode;
//
// return true;
// }
//
// }
//
// private String[] getMonthArray(int start, int end) {
// int size = end - start + 1;
// String datesArray[] = new String[size];
//
// String month[] = resources.getStringArray(R.array.ArrMonth);
//
// for (int i = 0; i < size; i++) {
// datesArray[i] = String.valueOf(month[(start + i - 1)]);
// }
// return datesArray;
// }
//
// private String[] getYearArray(int start, int end) {
// int size = end - start + 1;
// String datesArray[] = new String[size];
// for (int i = 0; i < size; i++) {
// datesArray[i] = String.valueOf(start + i);
// }
// return datesArray;
// }
//
// /**
// * show Number Picker
// *
// * @param maxValue
// * @param textView1
// * @param textView2
// * @param textView3
// */
// protected void showNumberPicker(final TextView textView1) {
// LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(
// R.layout.wheel_num_picker, null);
// TextView btnYes = (TextView) linearLayout
// .findViewById(R.id.btnPaymentCardExpDateYes);
// btnYes.setText(R.string.Ok);
// customDialog = new CustomDialog(PaymentActivity.this, linearLayout,
// AppConstants.DEVICE_WIDTH - 20, AppConstants.DEVICE_HEIGHT / 2,
// true);
// wheelMonth = (WheelView) linearLayout.findViewById(R.id.wheelMonth);
// wheelYear = (WheelView) linearLayout.findViewById(R.id.wheelYear);
// Calendar calExpDate = Calendar.getInstance();
// months = getMonthArray(1, 12);
// years = getYearArray(calExpDate.get(Calendar.YEAR),
// calExpDate.get(Calendar.YEAR)
// + AppConstants.EXPIRY_YEAR_LIMIT);
// wheelMonth.setCenterDrawable(R.drawable.img_background_fenetre_jauge);
// wheelYear.setCenterDrawable(R.drawable.img_background_fenetre_jauge);
// wheelMonth.setViewAdapter(new DateArrayAdapter(PaymentActivity.this,
// months, 0));
// wheelYear.setViewAdapter(new DateArrayAdapter(PaymentActivity.this,
// years, 0));
// if (textView1.getTag(R.string.hala_flight_tag) != null)
// wheelMonth.setCurrentItem((Integer) textView1
// .getTag(R.string.hala_flight_tag));
// if (textView1.getTag(R.string.hala_name_tag) != null)
// wheelYear.setCurrentItem((Integer) textView1
// .getTag(R.string.hala_name_tag));
//
// btnYes.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
//
// textView1.setTag(R.string.hala_flight_tag,
// wheelMonth.getCurrentItem());
// textView1.setTag(R.string.hala_name_tag,
// wheelYear.getCurrentItem());
//
// String strTextMonth = wheelMonth.getCurrentItem() + 1 + "";
// String strTextYear = years[wheelYear.getCurrentItem()] + "";
// textView1.setText((strTextMonth.length() == 1 ? "0"
// + strTextMonth : strTextMonth)
// + "/" + strTextYear);
//
// if (customDialog != null && customDialog.isShowing()) {
// customDialog.dismiss();
// }
// }
// });
// customDialog.show();
// }
//
// private String cardType()
// {
// if (etCardNumber != null && !etCardNumber.toString().equalsIgnoreCase("")) {
//
// char [] tempCardNumber = etCardNumber.getText().toString().toCharArray();
// if (tempCardNumber[0] =='4') {
// etCardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visa,
// 0);
// return "VISA";
// }
// else if (tempCardNumber[0] =='5') {
// etCardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.master,
// 0);
// return "MASTER_CARD";
// }
// else
// return "";
// }
// else
// return "";
// }
// private int cardNumberCount()
// {
// if (etCardNumber != null && !etCardNumber.toString().equalsIgnoreCase("")) {
// char [] tempCardNumber = etCardNumber.getText().toString().toCharArray();
// return tempCardNumber.length;
// }
// else
// return 0;
// }
// }