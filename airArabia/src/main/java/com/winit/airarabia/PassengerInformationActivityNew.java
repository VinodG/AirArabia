package com.winit.airarabia;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.insider.android.insider.Insider;
import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.insider.GcmBroadcastReceiver;
import com.winit.airarabia.objects.AirPriceQuoteDO;
import com.winit.airarabia.objects.CountryDO;
import com.winit.airarabia.objects.LoginDO;
import com.winit.airarabia.objects.OtherPassengerDo;
import com.winit.airarabia.objects.PassengerInfoContactDO;
import com.winit.airarabia.objects.PassengerInfoDO;
import com.winit.airarabia.objects.PassengerInfoPersonDO;
import com.winit.airarabia.objects.SavedPassengerDO;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.utils.SharedPrefUtils;
import com.winit.airarabia.utils.StringUtils;
import com.winit.airarabia.webaccess.Response;

public class PassengerInformationActivityNew extends BaseActivity implements OnClickListener, DataListener {

	private ScrollView svPassenger;
	private LinearLayout llAllAdultInfo, llFasterBookingLogin;
	private PassengerInfoDO passengerInfoDO;
	private PassengerInfoPersonDO passengerInfoPersonDO;
	private String[] arrTitleADT, arrTitleCHD, arrTitleINF, arrTitleADTDesc, arrTitleCHDDesc, arrTitleINFDesc;
	private final String contactcity = "contactcity";
	private final String persontitle = "persontitle";
	private final String personnationality = "personnationality";
	private final String contactlanguage = "contactlanguage";
	private AirPriceQuoteDO airPriceQuoteDOTotal;
	private PassengerInfoPersonDO mPersonDOSel;
	private int COUNTRY_RESULT_CODE = 8000;
	private int PASS_INFO_RESULT_CODE = 9000;
	private CountryDO countryDO;
	private int tempPosFromPassengerInformation = -1;
	private TextView tvNationalityTemp, tv_selectflight1;
	private TextView tvNationalityTempTag;
	private TextView tvTitletemptag;
	private TextView tvDOBtemptag;
	private TextView tvDobSel;
	private String SelectedCountry;
	private Boolean isCollectPoint = false;
	private String savedNationality = "";
	private final int REQUEST_CAL_DOB_CHILD = 100, REQUEST_CAL_DOB_INF = 101, REQUEST_SAVE_CHILD = 1000;
	private int position_layout = -1;
	private SavedPassengerDO savedPassengerObjectDo;
	private OtherPassengerDo otherPassengerDo;

	private PassengerInformationActivityNew.BCR bcr;

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

		tvHeaderTitle.setText(getString(R.string.PassengerInfo));

		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(getString(R.string.Continue));

		if (getIntent().hasExtra(AppConstants.AIRPORTS_PRICE_TOTAL))
			airPriceQuoteDOTotal = (AirPriceQuoteDO) getIntent().getExtras()
					.getSerializable(AppConstants.AIRPORTS_PRICE_TOTAL);

		svPassenger = (ScrollView) layoutInflater.inflate(R.layout.passengerinformation, null);
		llMiddleBase.addView(svPassenger, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		llAllAdultInfo = (LinearLayout) svPassenger.findViewById(R.id.llAllAdultInfo);
		llFasterBookingLogin = (LinearLayout) svPassenger.findViewById(R.id.llFasterBookingLogin);
		tv_selectflight1 = (TextView) svPassenger.findViewById(R.id.tv_selectflight1);

		passengerInfoDO = new PassengerInfoDO();
		mPersonDOSel = new PassengerInfoPersonDO();

		// setTypeFaceOpenSansLight(svPassenger);
		// setTypeFaceOpenSansLight(llAllAdultInfo);
		setTypefaceOpenSansRegular(llAllAdultInfo);
		tv_selectflight1.setTypeface(typefaceOpenSansSemiBold);

		if ((SharedPrefUtils.getKeyValue(PassengerInformationActivityNew.this, SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.isLoggedIn)).equalsIgnoreCase("1"))
			btnSubmitNext.setText(getString(R.string.save_and_continue));

		showLoader("");
		otherPassengerDo = gettingObjecFromSharedPrerenceOtherPassengerDo(SharedPreferenceStrings.otherPassengerDo);
		hideLoader();

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

		arrTitleADT = (resources.getStringArray(R.array.title_adt));
		arrTitleCHD = (resources.getStringArray(R.array.title_chd));
		arrTitleINF = (resources.getStringArray(R.array.title_inf));

		arrTitleADTDesc = resources.getStringArray(R.array.title_adt_desc);
		arrTitleCHDDesc = resources.getStringArray(R.array.title_chd_desc);
		arrTitleINFDesc = resources.getStringArray(R.array.title_inf_desc);

		callServiceGetNationalitiesData();

		btnSubmitNext.setOnClickListener(this);

		passengerInfoDO.vecPassengerInfoPersonDO = new Vector<PassengerInfoPersonDO>();
		passengerInfoDO.passengerInfoContactDO = new PassengerInfoContactDO();

		llFasterBookingLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Insider.tagEvent(AppConstants.LoginForFasterBooking, PassengerInformationActivityNew.this);
				// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.LoginForFasterBooking);
				trackEvent("Passenger", AppConstants.LoginForFasterBooking, "");
				Intent in = new Intent(PassengerInformationActivityNew.this, LoginActivityScreen.class);
				in.putExtra(AppConstants.PASS_INFO, "passInfo");
				startActivityForResult(in, PASS_INFO_RESULT_CODE);
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnSubmitNext) {
			Insider.tagEvent(AppConstants.ContinuePassengerDetail, PassengerInformationActivityNew.this);
			// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.ContinuePassengerDetail);
			trackEvent("Passenger", AppConstants.ContinuePassengerDetail, "");

			AppConstants.bookingFlightDO.passengerInfoDO = passengerInfoDO;

			if (updatePassengerInfo()) {
				moveToPersonaliseTrip();
			}
		}
	}

	private void setPassengerInfo() {
		Vector<String> vecAdtTrRph = new Vector<String>();
		if (AppConstants.bookingFlightDO != null && AppConstants.bookingFlightDO.myBookFlightDO != null) {
			for (int i = 0; i < AppConstants.bookingFlightDO.myBookFlightDO.adtQty; i++) {
				String savedNat = "";
				passengerInfoPersonDO = new PassengerInfoPersonDO();

				LinearLayout child = (LinearLayout) layoutInflater.inflate(R.layout.adult_info_item, null);
				OnFocusChangeListener focusChangeListener, focusChangeListener1, focusChangeListener2;
				TextView tvHeader = (TextView) child.findViewById(R.id.tvHeader);
				final TextView tvTitle = (TextView) child.findViewById(R.id.tvTitle);
				final EditText etFirstname = (EditText) child.findViewById(R.id.etFirstname);
				final EditText etLastname = (EditText) child.findViewById(R.id.etLastname);
				final EditText edNationality = (EditText) child.findViewById(R.id.tvNationality);

				final LinearLayout ll_contactDetails = (LinearLayout) child.findViewById(R.id.ll_contactDetails);
				final LinearLayout ll_contactDetails_expanded = (LinearLayout) child
						.findViewById(R.id.ll_contactDetails_expanded);
				final ImageView ivAirRewardsCheck = (ImageView) child.findViewById(R.id.ivAirRewardsCheck);
				final LinearLayout llAirRewards = (LinearLayout) child.findViewById(R.id.llAirRewards);
				final EditText etAirRewards = (EditText) child.findViewById(R.id.etAirRewards);
				// ===========================newly added
				// ========================================================================

				final TextView tvTitleTag = (TextView) child.findViewById(R.id.tvTitleTag);
				final TextView tvFirstnameTag = (TextView) child.findViewById(R.id.tvFirstnameTag);
				final TextView tvLastnameTag = (TextView) child.findViewById(R.id.tvLastnameTag);
				final TextView tvNationalityTag = (TextView) child.findViewById(R.id.tvNationalityTag);
				final TextView tvDobTag = (TextView) child.findViewById(R.id.tvDobTag);
				final TextView tvSelectFromSaved = (TextView) child.findViewById(R.id.tvSelectFromSaved);
				final LinearLayout ll_collectPoint = (LinearLayout) child.findViewById(R.id.ll_collectPoint);
				// setTypeFaceOpenSansLight(child);
				// tvTitleTag.setTypeface(null,Typeface.NORMAL);
				// tvFirstnameTag.setTypeface(null,Typeface.NORMAL);
				// tvLastnameTag.setTypeface(null,Typeface.NORMAL);
				// tvNationalityTag.setTypeface(null,Typeface.NORMAL);
				// tvDobTag.setTypeface(null,Typeface.NORMAL);

				tvSelectFromSaved.setTag((passengerInfoDO.vecPassengerInfoPersonDO.size()) + "");
				if ((SharedPrefUtils.getKeyValue(PassengerInformationActivityNew.this, SharedPreferenceStrings.APP_PREFERENCES,
						SharedPreferenceStrings.isLoggedIn)).equalsIgnoreCase("1"))
				if (otherPassengerDo != null && (otherPassengerDo.vecSavedPassengerDoAdult == null
						|| otherPassengerDo.vecSavedPassengerDoAdult.size() <= 0))
					tvSelectFromSaved.setVisibility(View.GONE);// need visible
																// if use
				else
					tvSelectFromSaved.setVisibility(View.VISIBLE);

				llAllAdultInfo.addView(child);
				setTypefaceOpenSansRegular(child);
				tvHeader.setTypeface(typefaceOpenSansSemiBold);
				tvTitleTag.setTypeface(typefaceOpenSansSemiBold);
				tvFirstnameTag.setTypeface(typefaceOpenSansSemiBold);
				tvLastnameTag.setTypeface(typefaceOpenSansSemiBold);
				tvNationalityTag.setTypeface(typefaceOpenSansSemiBold);
				focusChangeListener = new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							tvFirstnameTag.setVisibility(View.VISIBLE);
						} else if (((EditText) v).getText().toString().isEmpty()) {
							tvFirstnameTag.setVisibility(View.GONE);
						}
					}
				};
				focusChangeListener1 = new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							tvLastnameTag.setVisibility(View.VISIBLE);
						} else if (((EditText) v).getText().toString().isEmpty()) {
							tvLastnameTag.setVisibility(View.GONE);
						}
					}
				};
				focusChangeListener2 = new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						hideKeyBoard(v);
					}
				};
				tvSelectFromSaved.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Insider.tagEvent(AppConstants.SelectFromSaved, PassengerInformationActivityNew.this);
						// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.SelectFromSaved);
						trackEvent("Passenger", AppConstants.SelectFromSaved, "");

						Intent intent = new Intent(getApplicationContext(), PassengerActivityChoose.class);
						intent.putExtra("passenger_category", "Adult");
						intent.putExtra("passenger_clicked_pos", tvSelectFromSaved.getTag().toString());
						startActivityForResult(intent, 1000);

					}
				});
				etFirstname.setOnFocusChangeListener(focusChangeListener);
				etLastname.setOnFocusChangeListener(focusChangeListener1);
				edNationality.setOnFocusChangeListener(focusChangeListener2);

				passengerInfoPersonDO.persontype = AppConstants.PERSON_TYPE_ADULT;

				passengerInfoPersonDO.travelerRefNumberRPHList = "A" + (i + 1);

				vecAdtTrRph.add(passengerInfoPersonDO.travelerRefNumberRPHList);

				if (checkLangArabic())
					tvHeader.setText((i + 1) + " " + getString(R.string.adult_flight_summary));
				else
					tvHeader.setText(getString(R.string.adult_flight_summary) + " " + (i + 1));

				tvTitle.setTag(i + "");
				edNationality.setTag((i) + "");
				etFirstname.setTag((i) + "");
				etLastname.setTag((i) + "");

				passengerInfoDO.vecPassengerInfoPersonDO.add(passengerInfoPersonDO);

				if (i == 0)
					UpdateAdult1Values(llAllAdultInfo);

				if (!TextUtils.isEmpty(savedNationality)) {
					savedNat = savedNationality;
					savedNationality = "";
					if (!etFirstname.getText().toString().equals("")) {
						edNationality.setText(savedNat);
						tvNationalityTag.setVisibility(View.VISIBLE);
						edNationality.setTypeface(typefaceOpenSansSemiBold);
					}

					passengerInfoPersonDO.personnationality = savedNat;
					passengerInfoPersonDO.personCountryCode = getCountryCodeByNationality(
							passengerInfoPersonDO.personnationality);
				}

				// ll_contactDetails.setTag(false);
				// ll_contactDetails.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				//
				// if (!(Boolean) ll_contactDetails.getTag()) {
				// ll_contactDetails_expanded.setVisibility(View.VISIBLE);
				// ll_contactDetails.setTag(true);
				// }
				// else
				// {
				// ll_contactDetails_expanded.setVisibility(View.GONE);
				// ll_contactDetails.setTag(false);
				// }
				//
				// }
				// });

				tvTitle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Insider.tagEvent(AppConstants.SelectFromSaved, PassengerInformationActivityNew.this);
						// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.SelectFromSaved);
						trackEvent("Passenger", AppConstants.SelectFromSaved, "");

						int pos = StringUtils.getInt(v.getTag().toString());
						tvTitletemptag = tvTitleTag;
						passengerInfoPersonDO = passengerInfoDO.vecPassengerInfoPersonDO.get(pos);
						showSelTextPopup(arrTitleADT, arrTitleADTDesc, tvTitle, passengerInfoPersonDO, persontitle,
								getString(R.string.Title));
					}
				});

				edNationality.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Insider.tagEvent(AppConstants.NationalityPassengerDetail, PassengerInformationActivityNew.this);
						// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.NationalityPassengerDetail);
						trackEvent("Passenger", AppConstants.NationalityPassengerDetail, "");

						hideKeyBoard(v);
						tvNationalityTemp = edNationality;
						tvNationalityTempTag = tvNationalityTag;
						if (AppConstants.vecCountryNationalityDO != null
								&& AppConstants.vecCountryNationalityDO.size() > 0) {
							int pos = StringUtils.getInt(v.getTag().toString());
							Intent i = new Intent(PassengerInformationActivityNew.this, SelectCountry.class);
							i.putExtra("pos", pos);
							i.putExtra("vecCountry", AppConstants.vecCountryNationalityDO);
							i.putExtra("HeaderTitle", getString(R.string.selectNationality));
							i.putExtra("SelectedCountry", SelectedCountry);
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
							startActivityForResult(i, COUNTRY_RESULT_CODE);
						}
					}
				});

				etFirstname.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {

						int pos = StringUtils.getInt(etFirstname.getTag().toString());
						passengerInfoPersonDO = passengerInfoDO.vecPassengerInfoPersonDO.get(pos);
						passengerInfoPersonDO.personfirstname = etFirstname.getText().toString();
						if (!(etFirstname.getText().toString()).equalsIgnoreCase("")) {

							// tvFirstnameTag.setVisibility(View.VISIBLE);
							etFirstname.setTypeface(typefaceOpenSansSemiBold);
						} else {
							// tvFirstnameTag.setVisibility(View.GONE);
							etFirstname.setTypeface(typefaceOpenSansRegular);
						}
					}
				});
				etLastname.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {

						int pos = StringUtils.getInt(etLastname.getTag().toString());
						passengerInfoPersonDO = passengerInfoDO.vecPassengerInfoPersonDO.get(pos);
						passengerInfoPersonDO.personlastname = etLastname.getText().toString();
						if (!(etLastname.getText().toString()).equalsIgnoreCase("")) {

							// tvLastnameTag.setVisibility(View.VISIBLE);
							etLastname.setTypeface(typefaceOpenSansSemiBold);
						} else {
							// tvLastnameTag.setVisibility(View.GONE);
							etLastname.setTypeface(typefaceOpenSansRegular);
						}
					}
				});

				etAirRewards.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void afterTextChanged(Editable s) {
						passengerInfoPersonDO = passengerInfoDO.vecPassengerInfoPersonDO.get(0);
						passengerInfoPersonDO.MembershipID = etAirRewards.getText().toString();
					}
				});

				edNationality.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						Insider.tagEvent(AppConstants.NationalityPassengerDetail, PassengerInformationActivityNew.this);
						// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.NationalityPassengerDetail);
						trackEvent("Passenger", AppConstants.NationalityPassengerDetail, "");

						v.performClick();
						return false;
					}
				});

				ivAirRewardsCheck.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Insider.tagEvent(AppConstants.CollectAirewards, PassengerInformationActivityNew.this);
						// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.CollectAirewards);
						trackEvent("Passenger", AppConstants.CollectAirewards, "");

						if (!isCollectPoint) {
							isCollectPoint = true;

							ivAirRewardsCheck
									.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_on_pasgr));
							llAirRewards.setVisibility(View.VISIBLE);
							etAirRewards.setVisibility(View.VISIBLE);
						} else {
							isCollectPoint = false;

							ivAirRewardsCheck
									.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_off_pasgr));
							llAirRewards.setVisibility(View.GONE);
							etAirRewards.setVisibility(View.GONE);
						}

					}
				});

			}

			for (int i = 0; i < AppConstants.bookingFlightDO.myBookFlightDO.chdQty; i++) {
				String savedNat = "";
				passengerInfoPersonDO = new PassengerInfoPersonDO();

				LinearLayout child = (LinearLayout) layoutInflater.inflate(R.layout.adult_info_item, null);
				OnFocusChangeListener focusChangeListener, focusChangeListener1, focusChangeListener2;

				TextView tvHeader = (TextView) child.findViewById(R.id.tvHeader);
				final TextView tvTitle = (TextView) child.findViewById(R.id.tvTitle);
				final EditText etFirstname = (EditText) child.findViewById(R.id.etFirstname);
				final EditText etLastname = (EditText) child.findViewById(R.id.etLastname);
				final EditText edNationality = (EditText) child.findViewById(R.id.tvNationality);
				final LinearLayout ll_contactDetails = (LinearLayout) child.findViewById(R.id.ll_contactDetails);
				final LinearLayout ll_contactDetails_expanded = (LinearLayout) child
						.findViewById(R.id.ll_contactDetails_expanded);
				final ImageView ivAirRewardsCheck = (ImageView) child.findViewById(R.id.ivAirRewardsCheck);
				final LinearLayout llAirRewards = (LinearLayout) child.findViewById(R.id.llAirRewards);
				final EditText etAirRewards = (EditText) child.findViewById(R.id.etAirRewards);

				// ===========================newly added
				// ========================================================================

				final TextView tvTitleTag = (TextView) child.findViewById(R.id.tvTitleTag);
				final TextView tvFirstnameTag = (TextView) child.findViewById(R.id.tvFirstnameTag);
				final TextView tvLastnameTag = (TextView) child.findViewById(R.id.tvLastnameTag);
				final TextView tvNationalityTag = (TextView) child.findViewById(R.id.tvNationalityTag);
				final TextView tvDobTag = (TextView) child.findViewById(R.id.tvDobTag);
				final TextView tvSelectFromSavedCHD = (TextView) child.findViewById(R.id.tvSelectFromSaved);
				final LinearLayout ll_collectPoint = (LinearLayout) child.findViewById(R.id.ll_collectPoint);
				final EditText edDob = (EditText) child.findViewById(R.id.tvDob);
				final LinearLayout llDOB = (LinearLayout) child.findViewById(R.id.llDOB);

				llDOB.setVisibility(View.VISIBLE);

				tvSelectFromSavedCHD.setTag((passengerInfoDO.vecPassengerInfoPersonDO.size()) + "");
				
				if ((SharedPrefUtils.getKeyValue(PassengerInformationActivityNew.this, SharedPreferenceStrings.APP_PREFERENCES,
						SharedPreferenceStrings.isLoggedIn)).equalsIgnoreCase("1"))
				if (otherPassengerDo != null && (otherPassengerDo.vecSavedPassengerDoChild == null
						|| otherPassengerDo.vecSavedPassengerDoChild.size() <= 0))
					tvSelectFromSavedCHD.setVisibility(View.GONE);// need to be
																	// visible
				else
					tvSelectFromSavedCHD.setVisibility(View.VISIBLE);
				llAllAdultInfo.addView(child);
				// tvTitleTag.setTypeface(null,Typeface.NORMAL);
				// tvFirstnameTag.setTypeface(null,Typeface.NORMAL);
				// tvLastnameTag.setTypeface(null,Typeface.NORMAL);
				// tvNationalityTag.setTypeface(null,Typeface.NORMAL);
				// tvDobTag.setTypeface(null,Typeface.NORMAL);

				// =============================newly added for
				// collectpoint======================
				ll_collectPoint.setVisibility(View.VISIBLE);

				passengerInfoPersonDO.persontype = AppConstants.PERSON_TYPE_CHILD;
				passengerInfoPersonDO.travelerRefNumberRPHList = "C"
						+ (AppConstants.bookingFlightDO.myBookFlightDO.adtQty + i + 1);

				if (checkLangArabic())
					tvHeader.setText((i + 1) + " " + getString(R.string.Childc));
				else
					tvHeader.setText(getString(R.string.Childc) + " " + (i + 1));

				tvTitle.setTag((passengerInfoDO.vecPassengerInfoPersonDO.size()) + "");
				edNationality.setTag((passengerInfoDO.vecPassengerInfoPersonDO.size()) + "");
				etFirstname.setTag((passengerInfoDO.vecPassengerInfoPersonDO.size()) + "");
				etLastname.setTag((passengerInfoDO.vecPassengerInfoPersonDO.size()) + "");
				edDob.setTag((passengerInfoDO.vecPassengerInfoPersonDO.size()) + "");
				setTypefaceOpenSansRegular(child);
				tvHeader.setTypeface(typefaceOpenSansSemiBold);
				tvTitleTag.setTypeface(typefaceOpenSansSemiBold);
				tvFirstnameTag.setTypeface(typefaceOpenSansSemiBold);
				tvLastnameTag.setTypeface(typefaceOpenSansSemiBold);
				tvNationalityTag.setTypeface(typefaceOpenSansSemiBold);
				tvDobTag.setTypeface(typefaceOpenSansSemiBold);
				// tvTitle.setTypeface(typefaceOpenSansSemiBold);
				tvHeader.setTypeface(typefaceOpenSansSemiBold);

				focusChangeListener = new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							tvFirstnameTag.setVisibility(View.VISIBLE);
						} else if (((EditText) v).getText().toString().isEmpty()) {
							tvFirstnameTag.setVisibility(View.GONE);
						}
					}
				};
				focusChangeListener1 = new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							tvLastnameTag.setVisibility(View.VISIBLE);
						} else if (((EditText) v).getText().toString().isEmpty()) {
							tvLastnameTag.setVisibility(View.GONE);
						}
					}
				};
				focusChangeListener2 = new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							hideKeyBoard(v);
						}
					}
				};
				tvSelectFromSavedCHD.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Insider.tagEvent(AppConstants.SelectFromSaved, PassengerInformationActivityNew.this);
						// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.SelectFromSaved);
						trackEvent("Passenger", AppConstants.SelectFromSaved, "");

						Intent intent = new Intent(getApplicationContext(), PassengerActivityChoose.class);
						intent.putExtra("passenger_category", "Child");
						intent.putExtra("passenger_clicked_pos", tvSelectFromSavedCHD.getTag().toString());
						startActivityForResult(intent, 1000);
					}
				});

				etFirstname.setOnFocusChangeListener(focusChangeListener);
				etLastname.setOnFocusChangeListener(focusChangeListener1);
				edNationality.setOnFocusChangeListener(focusChangeListener2);
				edDob.setOnFocusChangeListener(focusChangeListener2);

				if (!TextUtils.isEmpty(savedNationality)) {
					savedNat = savedNationality;
					savedNationality = "";
					if (!etFirstname.getText().toString().equals("")) {
						edNationality.setText(savedNat);
						tvNationalityTag.setVisibility(View.VISIBLE);
						edNationality.setTypeface(typefaceOpenSansSemiBold);
					}

					passengerInfoPersonDO.personnationality = savedNat;
					passengerInfoPersonDO.personCountryCode = getCountryCodeByNationality(
							passengerInfoPersonDO.personnationality);
				}

				passengerInfoDO.vecPassengerInfoPersonDO.add(passengerInfoPersonDO);

				// ll_contactDetails.setTag(false);
				// ll_contactDetails.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				//
				// if (!(Boolean) ll_contactDetails.getTag()) {
				// ll_contactDetails_expanded.setVisibility(View.VISIBLE);
				// ll_contactDetails.setTag(true);
				// }
				// else
				// {
				// ll_contactDetails_expanded.setVisibility(View.GONE);
				// ll_contactDetails.setTag(false);
				// }
				// }
				// });
				edDob.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Insider.tagEvent(AppConstants.DOBChildPassengerDetail, PassengerInformationActivityNew.this);
						// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.DOBChildPassengerDetail);
						trackEvent("Passenger", AppConstants.DOBChildPassengerDetail, "");

						hideKeyBoard(v);
						tvDobSel = (TextView) v;
						tvDOBtemptag = tvDobTag;

						Calendar curCal = Calendar.getInstance();

						mPersonDOSel = passengerInfoDO.vecPassengerInfoPersonDO
								.get(StringUtils.getInt(v.getTag().toString()));

						String mDate = mPersonDOSel.personDOB;
						if (!TextUtils.isEmpty(mDate)) {
							if (mDate.contains("T"))
								mDate = mDate.split("T")[0];

							int date = StringUtils.getInt(mDate.split("-")[2]);
							int month = StringUtils.getInt(mDate.split("-")[1]);
							int year = StringUtils.getInt(mDate.split("-")[0]);

							month -= 1;

							curCal.set(Calendar.DATE, date);
							curCal.set(Calendar.MONTH, month);
							curCal.set(Calendar.YEAR, year);
						}

						Intent in = new Intent(PassengerInformationActivityNew.this, SelectDateDobActivityNew.class);
						in.putExtra(AppConstants.SEL_DATE, curCal);
						in.putExtra(AppConstants.SEL_DATE_CHILD, AppConstants.SEL_DATE_CHILD);
						//in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivityForResult(in, REQUEST_CAL_DOB_CHILD);
					}
				});
				tvTitle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Insider.tagEvent(AppConstants.TitlePassengerDetailChild, PassengerInformationActivityNew.this);
						// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.TitlePassengerDetailChild);
						trackEvent("Passenger", AppConstants.TitlePassengerDetailChild, "");

						tvTitletemptag = tvTitleTag;
						int pos = StringUtils.getInt(v.getTag().toString());
						passengerInfoPersonDO = passengerInfoDO.vecPassengerInfoPersonDO.get(pos);
						showSelTextPopup(arrTitleCHD, arrTitleCHDDesc, tvTitle, passengerInfoPersonDO, persontitle,
								getString(R.string.Title));
					}
				});
				edNationality.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Insider.tagEvent(AppConstants.NationalityPassengerDetailChild,
								PassengerInformationActivityNew.this);
						// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.NationalityPassengerDetailChild);
						trackEvent("Passenger", AppConstants.NationalityPassengerDetailChild, "");

						hideKeyBoard(v);
						tvNationalityTemp = edNationality;
						tvNationalityTempTag = tvNationalityTag;
						if (AppConstants.vecCountryNationalityDO != null
								&& AppConstants.vecCountryNationalityDO.size() > 0) {
							int pos = StringUtils.getInt(v.getTag().toString());
							Intent i = new Intent(PassengerInformationActivityNew.this, SelectCountry.class);
							i.putExtra("pos", pos);
							i.putExtra("SelectedCountry", SelectedCountry);
							i.putExtra("vecCountry", AppConstants.vecCountryNationalityDO);
							i.putExtra("HeaderTitle", getString(R.string.selectNationality));
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
							//i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
							startActivityForResult(i, COUNTRY_RESULT_CODE);
						}
					}
				});

				etFirstname.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						int pos = StringUtils.getInt(etFirstname.getTag().toString());
						passengerInfoPersonDO = passengerInfoDO.vecPassengerInfoPersonDO.get(pos);
						passengerInfoPersonDO.personfirstname = etFirstname.getText().toString();
						if (!(etFirstname.getText().toString()).equalsIgnoreCase("")) {

							// tvFirstnameTag.setVisibility(View.VISIBLE);
							etFirstname.setTypeface(typefaceOpenSansSemiBold);
						} else {
							// tvFirstnameTag.setVisibility(View.GONE);
							etFirstname.setTypeface(typefaceOpenSansRegular);
						}
					}
				});

				etLastname.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						int pos = StringUtils.getInt(etLastname.getTag().toString());
						passengerInfoPersonDO = passengerInfoDO.vecPassengerInfoPersonDO.get(pos);
						passengerInfoPersonDO.personlastname = etLastname.getText().toString();
						if (!(etLastname.getText().toString()).equalsIgnoreCase("")) {

							// tvLastnameTag.setVisibility(View.VISIBLE);
							etLastname.setTypeface(typefaceOpenSansSemiBold);
						} else {
							// tvLastnameTag.setVisibility(View.GONE);
							etLastname.setTypeface(typefaceOpenSansRegular);
						}
					}
				});

				etAirRewards.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void afterTextChanged(Editable s) {
						passengerInfoPersonDO = passengerInfoDO.vecPassengerInfoPersonDO.get(0);
						passengerInfoPersonDO.MembershipID = etAirRewards.getText().toString();
					}
				});

				edNationality.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						v.performClick();
						return false;
					}
				});
				edDob.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						v.performClick();
						return false;
					}
				});

				ivAirRewardsCheck.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Insider.tagEvent(AppConstants.Airewards_Button, PassengerInformationActivityNew.this);
						// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.Airewards_Button);
						trackEvent("Passenger", AppConstants.Airewards_Button, "");

						if (!isCollectPoint) {
							isCollectPoint = true;

							ivAirRewardsCheck
									.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_on_pasgr));
							llAirRewards.setVisibility(View.VISIBLE);
							etAirRewards.setVisibility(View.VISIBLE);
						} else {
							isCollectPoint = false;

							ivAirRewardsCheck
									.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_off_pasgr));
							llAirRewards.setVisibility(View.GONE);
							etAirRewards.setVisibility(View.GONE);
						}

					}
				});
			}

			for (int i = 0; i < AppConstants.bookingFlightDO.myBookFlightDO.infQty; i++) {
				String savedNat = "";
				passengerInfoPersonDO = new PassengerInfoPersonDO();

				LinearLayout child = (LinearLayout) layoutInflater.inflate(R.layout.adult_info_item, null);

				OnFocusChangeListener focusChangeListener, focusChangeListener1, focusChangeListener2;

				TextView tvHeader = (TextView) child.findViewById(R.id.tvHeader);
				final TextView etTitle = (TextView) child.findViewById(R.id.tvTitle);
				final EditText etFirstname = (EditText) child.findViewById(R.id.etFirstname);
				final EditText etLastname = (EditText) child.findViewById(R.id.etLastname);

				final EditText edNationality = (EditText) child.findViewById(R.id.tvNationality);
				final EditText edDob = (EditText) child.findViewById(R.id.tvDob);
				final LinearLayout ll_contactDetails = (LinearLayout) child.findViewById(R.id.ll_contactDetails);
				final LinearLayout ll_contactDetails_expanded = (LinearLayout) child
						.findViewById(R.id.ll_contactDetails_expanded);

				// ===========================newly added
				// ========================================================================

				final TextView tvTitleTag = (TextView) child.findViewById(R.id.tvTitleTag);
				final TextView tvFirstnameTag = (TextView) child.findViewById(R.id.tvFirstnameTag);
				final TextView tvLastnameTag = (TextView) child.findViewById(R.id.tvLastnameTag);
				final TextView tvNationalityTag = (TextView) child.findViewById(R.id.tvNationalityTag);
				final TextView tvDobTag = (TextView) child.findViewById(R.id.tvDobTag);
				final TextView tvSelectFromSavedINF = (TextView) child.findViewById(R.id.tvSelectFromSaved);
				final LinearLayout llDOB = (LinearLayout) child.findViewById(R.id.llDOB);
				final LinearLayout ll_collectPoint = (LinearLayout) child.findViewById(R.id.ll_collectPoint);

				llDOB.setVisibility(View.VISIBLE);

				// ============================newly added for collect
				// points==================================
				ll_collectPoint.setVisibility(View.GONE);

				edDob.setVisibility(View.VISIBLE);
				tvSelectFromSavedINF.setTag((passengerInfoDO.vecPassengerInfoPersonDO.size()) + "");
				
				if ((SharedPrefUtils.getKeyValue(PassengerInformationActivityNew.this, SharedPreferenceStrings.APP_PREFERENCES,
						SharedPreferenceStrings.isLoggedIn)).equalsIgnoreCase("1"))
				if (otherPassengerDo != null && (otherPassengerDo.vecSavedPassengerDoInfant == null
						|| otherPassengerDo.vecSavedPassengerDoInfant.size() <= 0))
					tvSelectFromSavedINF.setVisibility(View.GONE);// neew to be
																	// visible
																	// if use
																	// save from
																	// uncomment
				else
					tvSelectFromSavedINF.setVisibility(View.VISIBLE);
				// setTypeFaceSemiBold(child);
				llAllAdultInfo.addView(child);
				// tvTitleTag.setTypeface(null,Typeface.NORMAL);
				// tvFirstnameTag.setTypeface(null,Typeface.NORMAL);
				// tvLastnameTag.setTypeface(null,Typeface.NORMAL);
				// tvNationalityTag.setTypeface(null,Typeface.NORMAL);
				// tvDobTag.setTypeface(null,Typeface.NORMAL);
				passengerInfoPersonDO.persontype = AppConstants.PERSON_TYPE_INFANT;
				passengerInfoPersonDO.travelerRefNumberRPHList = "I"
						+ (AppConstants.bookingFlightDO.myBookFlightDO.adtQty
								+ AppConstants.bookingFlightDO.myBookFlightDO.chdQty + i + 1)
						+ "/" + vecAdtTrRph.get(i);

				if (checkLangArabic())
					tvHeader.setText((i + 1) + " " + getString(R.string.Infantc));
				else
					tvHeader.setText(getString(R.string.Infantc) + " " + (i + 1));

				etTitle.setTag((passengerInfoDO.vecPassengerInfoPersonDO.size()) + "");
				edNationality.setTag((passengerInfoDO.vecPassengerInfoPersonDO.size()) + "");
				etFirstname.setTag((passengerInfoDO.vecPassengerInfoPersonDO.size()) + "");
				etLastname.setTag((passengerInfoDO.vecPassengerInfoPersonDO.size()) + "");
				edDob.setTag((passengerInfoDO.vecPassengerInfoPersonDO.size()) + "");

				// tvSelectFromSavedINF.setTag((passengerInfoDO.vecPassengerInfoPersonDO.size())
				// + "");
				// tvSelectFromSavedINF.setTag(R.string.accept,child);

				setTypefaceOpenSansRegular(child);
				tvHeader.setTypeface(typefaceOpenSansSemiBold);
				tvTitleTag.setTypeface(typefaceOpenSansSemiBold);
				tvFirstnameTag.setTypeface(typefaceOpenSansSemiBold);
				tvLastnameTag.setTypeface(typefaceOpenSansSemiBold);
				tvNationalityTag.setTypeface(typefaceOpenSansSemiBold);
				tvTitleTag.setTypeface(typefaceOpenSansSemiBold);
				tvDobTag.setTypeface(typefaceOpenSansSemiBold);
				tvHeader.setTypeface(typefaceOpenSansSemiBold);

				focusChangeListener = new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							tvFirstnameTag.setVisibility(View.VISIBLE);
						} else if (((EditText) v).getText().toString().isEmpty()) {
							tvFirstnameTag.setVisibility(View.GONE);
						}
					}
				};
				focusChangeListener1 = new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							tvLastnameTag.setVisibility(View.VISIBLE);
						} else if (((EditText) v).getText().toString().isEmpty()) {
							tvLastnameTag.setVisibility(View.GONE);
						}
					}
				};
				focusChangeListener2 = new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							hideKeyBoard(v);
						}
					}
				};

				// tvSelectFromSavedINF.setTag((passengerInfoDO.vecPassengerInfoPersonDO.size())
				// + "");
				// tvSelectFromSavedINF.setTag(R.string.accept,child);
				tvSelectFromSavedINF.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						// mPersonDOSel =
						// passengerInfoDO.vecPassengerInfoPersonDO.get(StringUtils.getInt(v.getTag().toString()));
						//
						// LinearLayout child = (LinearLayout)
						// v.getTag(R.string.accept);
						Insider.tagEvent(AppConstants.SelectFromSaved, PassengerInformationActivityNew.this);
						// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.SelectFromSaved);
						trackEvent("Passenger", AppConstants.SelectFromSaved, "");

						Intent intent = new Intent(getApplicationContext(), PassengerActivityChoose.class);
						intent.putExtra("passenger_category", "Infant");
						intent.putExtra("passenger_clicked_pos", tvSelectFromSavedINF.getTag().toString());
						startActivityForResult(intent, 1000);

						// REQUEST_SAVE_CHILD
					}
				});

				etFirstname.setOnFocusChangeListener(focusChangeListener);
				etLastname.setOnFocusChangeListener(focusChangeListener1);
				edNationality.setOnFocusChangeListener(focusChangeListener2);
				edDob.setOnFocusChangeListener(focusChangeListener2);

				if (!TextUtils.isEmpty(savedNationality)) {
					savedNat = savedNationality;
					savedNationality = "";
					if (!etFirstname.getText().toString().equals("")) {
						edNationality.setText(savedNat);
						tvNationalityTag.setVisibility(View.VISIBLE);
						edNationality.setTypeface(typefaceOpenSansSemiBold);
					}

					passengerInfoPersonDO.personnationality = savedNat;
					passengerInfoPersonDO.personCountryCode = getCountryCodeByNationality(
							passengerInfoPersonDO.personnationality);
				}

				passengerInfoDO.vecPassengerInfoPersonDO.add(passengerInfoPersonDO);

				// ll_contactDetails.setTag(false);
				// ll_contactDetails.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				//
				// if (!(Boolean) ll_contactDetails.getTag()) {
				// ll_contactDetails_expanded.setVisibility(View.VISIBLE);
				// ll_contactDetails.setTag(true);
				// }
				// else
				// {
				// ll_contactDetails_expanded.setVisibility(View.GONE);
				// ll_contactDetails.setTag(false);
				// }
				// }
				// });
				edDob.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Insider.tagEvent(AppConstants.DOBPassengerDetailInfant, PassengerInformationActivityNew.this);
						// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.DOBPassengerDetailInfant);
						trackEvent("Passenger", AppConstants.DOBPassengerDetailInfant, "");

						hideKeyBoard(v);
						tvDobSel = (TextView) v;
						tvDOBtemptag = tvDobTag;
						Calendar curCal = Calendar.getInstance();

						mPersonDOSel = passengerInfoDO.vecPassengerInfoPersonDO
								.get(StringUtils.getInt(v.getTag().toString()));

						String mDate = mPersonDOSel.personDOB;
						if (!TextUtils.isEmpty(mDate)) {
							if (mDate.contains("T"))
								mDate = mDate.split("T")[0];

							int date = StringUtils.getInt(mDate.split("-")[2]);
							int month = StringUtils.getInt(mDate.split("-")[1]);
							int year = StringUtils.getInt(mDate.split("-")[0]);

							month -= 1;

							curCal.set(Calendar.DATE, date);
							curCal.set(Calendar.MONTH, month);
							curCal.set(Calendar.YEAR, year);
						}

						Intent in = new Intent(PassengerInformationActivityNew.this, SelectDateDobActivityNew.class);
						in.putExtra(AppConstants.SEL_DATE, curCal);
						//in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivityForResult(in, REQUEST_CAL_DOB_INF);
					}
				});

				etTitle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Insider.tagEvent(AppConstants.DOBPassengerDetailInfant, PassengerInformationActivityNew.this);
						// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.DOBPassengerDetailInfant);
						trackEvent("Passenger", AppConstants.DOBPassengerDetailInfant, "");

						tvTitletemptag = tvTitleTag;

						int pos = StringUtils.getInt(v.getTag().toString());
						passengerInfoPersonDO = passengerInfoDO.vecPassengerInfoPersonDO.get(pos);
						showSelTextPopup(arrTitleINF, arrTitleINFDesc, etTitle, passengerInfoPersonDO, persontitle,
								getString(R.string.Title));
					}
				});
				edNationality.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Insider.tagEvent(AppConstants.NationalityPassengerDetailInfant,
								PassengerInformationActivityNew.this);
						// Insider.Instance.tagEvent(PassengerInformationActivityNew.this,AppConstants.NationalityPassengerDetailInfant);
						trackEvent("Passenger", AppConstants.NationalityPassengerDetailInfant, "");

						hideKeyBoard(v);
						tvNationalityTemp = edNationality;
						tvNationalityTempTag = tvNationalityTag;
						if (AppConstants.vecCountryNationalityDO != null
								&& AppConstants.vecCountryNationalityDO.size() > 0) {
							int pos = StringUtils.getInt(v.getTag().toString());
							Intent i = new Intent(PassengerInformationActivityNew.this, SelectCountry.class);
							i.putExtra("pos", pos);
							i.putExtra("vecCountry", AppConstants.vecCountryNationalityDO);
							i.putExtra("HeaderTitle", getString(R.string.selectNationality));
							//i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
							startActivityForResult(i, COUNTRY_RESULT_CODE);
						}
					}
				});

				edNationality.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						v.performClick();
						return false;
					}
				});
				edDob.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						v.performClick();
						return false;
					}
				});

				etFirstname.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						int pos = StringUtils.getInt(etFirstname.getTag().toString());
						passengerInfoPersonDO = passengerInfoDO.vecPassengerInfoPersonDO.get(pos);
						passengerInfoPersonDO.personfirstname = etFirstname.getText().toString();
						if (!(etFirstname.getText().toString()).equalsIgnoreCase("")) {

							// tvFirstnameTag.setVisibility(View.VISIBLE);
							etFirstname.setTypeface(typefaceOpenSansSemiBold);
						} else {
							// tvFirstnameTag.setVisibility(View.GONE);
							etFirstname.setTypeface(typefaceOpenSansRegular);
						}
					}
				});

				etLastname.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						int pos = StringUtils.getInt(etLastname.getTag().toString());
						passengerInfoPersonDO = passengerInfoDO.vecPassengerInfoPersonDO.get(pos);
						passengerInfoPersonDO.personlastname = etLastname.getText().toString();
						if (!(etLastname.getText().toString()).equalsIgnoreCase("")) {

							// tvLastnameTag.setVisibility(View.VISIBLE);
							etLastname.setTypeface(typefaceOpenSansSemiBold);
						} else {
							// tvLastnameTag.setVisibility(View.GONE);
							etLastname.setTypeface(typefaceOpenSansRegular);
						}
					}
				});

			}
		} else
			clickHome();

		hideLoader();
	}

	private boolean updatePassengerInfo() {

		boolean isValid = false;
		boolean isValidFirstName = false, isValidLastName = false, isValidDob = true;
		for (PassengerInfoPersonDO personDO : passengerInfoDO.vecPassengerInfoPersonDO) {
			if (!personDO.personfirstname.equalsIgnoreCase("") && !personDO.personlastname.equalsIgnoreCase("")
					&& !personDO.persontitle.equalsIgnoreCase("") && !personDO.personnationality.equalsIgnoreCase("")) {
				isValid = true;
			} else {
				isValid = false;
				break;
			}
		}
		if (isValid)
			for (PassengerInfoPersonDO personDO : passengerInfoDO.vecPassengerInfoPersonDO) {
				if (personDO.personfirstname.length() > 1) {
					isValidFirstName = true;
				} else {
					isValidFirstName = false;
					break;
				}
			}
		for (PassengerInfoPersonDO personDO : passengerInfoDO.vecPassengerInfoPersonDO) {
			if (personDO.personlastname.length() > 1) {
				isValidLastName = true;
			} else {
				isValidLastName = false;
				break;
			}
		}
		for (PassengerInfoPersonDO personDO : passengerInfoDO.vecPassengerInfoPersonDO) {
			if ((personDO.persontype.equalsIgnoreCase(AppConstants.PERSON_TYPE_INFANT)
					|| personDO.persontype.equalsIgnoreCase(AppConstants.PERSON_TYPE_CHILD))
					&& personDO.personDOB.equalsIgnoreCase("")) {
				// if(personDO.personDOB)
				isValidDob = false;
				break;
			} else {
				isValidDob = true;
			}
		}

		if (!isValid) {
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.alert_msg_fill_all_fields_required_by_passenger), getString(R.string.Ok), "",
					"");
			return false;
		} else if (!isValidFirstName) {
			showCustomDialog(this, getString(R.string.Alert), getString(R.string.FirstNameError),
					getString(R.string.Ok), "", "");
			return false;
		} else if (!isValidLastName) {
			showCustomDialog(this, getString(R.string.Alert), getString(R.string.LastNameError), getString(R.string.Ok),
					"", "");
			return false;
		} else if (!isValidDob) {
			showCustomDialog(this, getString(R.string.Alert), getString(R.string.DOBError), getString(R.string.Ok), "",
					"");
			return false;
		} else if (isCollectPoint) {
			EditText et = (EditText) llAllAdultInfo.getChildAt(0).findViewById(R.id.etAirRewards);
			if (!TextUtils.isEmpty(et.getText().toString())) {
				if (emailValidate(et.getText().toString()).matches())
					return true;
				else {
					showCustomDialog(this, getString(R.string.Alert), getString(R.string.invaild_airewards),
							getString(R.string.Ok), "", "");
					return false;
				}
			} else {
				showCustomDialog(this, getString(R.string.Alert), getString(R.string.invaild_airewards_incomplete),
						getString(R.string.Ok), "", "");
				return false;
			}
		} else
			return true;
	}

	//////////////// =======================================================================/////////////////////

	private void showSelTextPopup(String[] arrTitle, String[] arrTitleDesc, final TextView tvTitleSel, final Object obj,
			final String objValue, String titleOfPopup) {
		LinearLayout ll = (LinearLayout) layoutInflater.inflate(R.layout.popup_titles, null);
		TextView tvTitleHeader = (TextView) ll.findViewById(R.id.tvTitleHeader);
		tvTitleHeader.setTypeface(typefaceOpenSansSemiBold);
		tvTitleHeader.setText(titleOfPopup + "");
		final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_No_Title);
		dialog.setContentView(ll);
		setTypefaceOpenSansRegular(ll);
		tvTitleHeader.setTypeface(typefaceOpenSansSemiBold);
		LinearLayout llPopupTitleMain = (LinearLayout) ll.findViewById(R.id.llPopupTitleMain);
		for (int i = 0; i < arrTitle.length; i++) {
			LinearLayout llTitle = (LinearLayout) layoutInflater.inflate(R.layout.popup_title_item, null);

			final TextView tvTitleItem = (TextView) llTitle.findViewById(R.id.tvTitleItem);
			// tvTitleItem.setTypeface(typeFaceOpenSansLight); Mrs
			llPopupTitleMain.addView(llTitle);

			tvTitleItem.setText(arrTitleDesc[i]);
			tvTitleItem.setTag(arrTitle[i]);

			if (tvTitleSel.getText().toString().equalsIgnoreCase(arrTitleDesc[i]))
				tvTitleItem.setBackgroundColor(resources.getColor(R.color.ash_color));

			tvTitleItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					tvTitleSel.setText(tvTitleItem.getText().toString());
					tvTitleSel.setTag(tvTitleItem.getTag().toString());

					if (!(tvTitleItem.getText().toString()).equalsIgnoreCase("")) {

						tvTitletemptag.setVisibility(View.VISIBLE);
						tvTitleSel.setTypeface(typefaceOpenSansSemiBold);
					} else {
						tvTitletemptag.setVisibility(View.GONE);
					}

					tvTitleItem.setBackgroundColor(resources.getColor(R.color.ash_color));

					dialog.dismiss();

					PassengerInfoPersonDO passengerInfoPersonDO = (PassengerInfoPersonDO) obj;
					passengerInfoPersonDO.persontitle = tvTitleSel.getText().toString();
				}
			});
		}
		dialog.show();
	}

	private void callServiceGetNationalitiesData() {
		showLoader("");
		if (new CommonBL(PassengerInformationActivityNew.this, PassengerInformationActivityNew.this)
				.getNationalitiesData()) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert), getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dataRetreived(Response data) {
		if (data != null && !data.isError) {
			switch (data.method) {
			case WS_NATIONALITIES:
				AppConstants.vecCountryNationalityDO = (Vector<CountryDO>) data.data;

				Collections.sort(AppConstants.vecCountryNationalityDO, new Comparator<CountryDO>() {

					@Override
					public int compare(CountryDO compR, CountryDO compL) {
						return compR.CountryName.compareToIgnoreCase(compL.CountryName);
					}
				});
				setPassengerInfo();

				break;

			default:
				break;

			}
		} else {
			hideLoader();
			if (data.data instanceof String) {
				if (((String) data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
					showCustomDialog(getApplicationContext(), getString(R.string.Alert),
							getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "",
							AppConstants.INTERNET_PROBLEM);
				else
					showCustomDialog(getApplicationContext(), getString(R.string.Alert),
							getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CAL_DOB_CHILD && resultCode == RESULT_OK) {
			Calendar mcal = (Calendar) data.getExtras().getSerializable(AppConstants.SEL_DATE);
			tvDobSel.setText("" + mcal.get(Calendar.DAY_OF_MONTH) + " "
					+ CalendarUtility.getMonthMMM(mcal.get(Calendar.MONTH)) + " " + mcal.get(Calendar.YEAR));
			mPersonDOSel.personDOB = CalendarUtility.getBookingDate(mcal);
			if (!(tvDobSel.getText().toString().equalsIgnoreCase(""))) {
				tvDOBtemptag.setVisibility(View.VISIBLE);
				tvDobSel.setTypeface(typefaceOpenSansSemiBold);
			} else
				tvDOBtemptag.setVisibility(View.GONE);
		} else if (requestCode == REQUEST_CAL_DOB_INF && resultCode == RESULT_OK) {
			Calendar mcal = (Calendar) data.getExtras().getSerializable(AppConstants.SEL_DATE);
			tvDobSel.setText("" + mcal.get(Calendar.DAY_OF_MONTH) + " "
					+ CalendarUtility.getMonthMMM(mcal.get(Calendar.MONTH)) + " " + mcal.get(Calendar.YEAR));
			mPersonDOSel.personDOB = CalendarUtility.getBookingDate(mcal);
			if (!(tvDobSel.getText().toString().equalsIgnoreCase(""))) {
				tvDOBtemptag.setVisibility(View.VISIBLE);
				tvDobSel.setTypeface(typefaceOpenSansSemiBold);
			} else
				tvDOBtemptag.setVisibility(View.GONE);
		} else if (requestCode == COUNTRY_RESULT_CODE && resultCode == RESULT_OK) {
			if (data.hasExtra("pos")) {
				tempPosFromPassengerInformation = data.getExtras().getInt("pos");
			}
			countryDO = new CountryDO();
			countryDO = ((CountryDO) data.getSerializableExtra("Country_Selected"));
			if (tempPosFromPassengerInformation != -1) {
				passengerInfoPersonDO = passengerInfoDO.vecPassengerInfoPersonDO.get(tempPosFromPassengerInformation);
				passengerInfoPersonDO.personnationality = countryDO.CountryName;
				passengerInfoPersonDO.personCountryCode = countryDO.CountryCode;
			}
			tvNationalityTemp.setText(countryDO.CountryName + "");
			SelectedCountry = countryDO.CountryName;
			if (countryDO != null && !countryDO.CountryName.equalsIgnoreCase("")) {

				tvNationalityTempTag.setVisibility(View.VISIBLE);
				tvNationalityTemp.setTypeface(typefaceOpenSansSemiBold);
			} else {

				tvNationalityTempTag.setVisibility(View.GONE);
			}
		} else if (requestCode == PASS_INFO_RESULT_CODE && resultCode == RESULT_OK) {
			if (llAllAdultInfo.getChildCount() > 0) {
				UpdateAdult1Values(llAllAdultInfo);
			}
		}
		if (requestCode == REQUEST_SAVE_CHILD && resultCode == RESULT_OK) {
			String passangerCategory = "";
			if (data.hasExtra("result_saved_object")) {
				savedPassengerObjectDo = (SavedPassengerDO) data.getExtras().getSerializable("result_saved_object");
				if (data.hasExtra("passenger_clicked_pos"))
					position_layout = Integer.parseInt((String) data.getExtras().getString("passenger_clicked_pos"));

				if (data.hasExtra("passangerCategory"))
					passangerCategory = (String) data.getExtras().getString("passangerCategory");
				updatePassengerFromSaved(llAllAdultInfo, savedPassengerObjectDo, position_layout, passangerCategory);
			}

		}
	}

	private void UpdateAdult1Values(LinearLayout llAllAdultInfo) {
		if ((SharedPrefUtils.getKeyValue(PassengerInformationActivityNew.this, SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.isLoggedIn)).equalsIgnoreCase("1")) {
			LoginDO userLoginDoFromServices = gettingObjecFromSharedPrerence(SharedPreferenceStrings.loginDo);
			LoginDO userLoginDOFromPref = (LoginDO) gettingObjecFromSharedPrerence("LoginDO");

			if (llAllAdultInfo.getChildCount() > 0) {
				LinearLayout child = (LinearLayout) llAllAdultInfo.getChildAt(0);

				final TextView tvheader = (TextView) child.findViewById(R.id.tvHeader);
				final TextView tvTitle = (TextView) child.findViewById(R.id.tvTitle);
				final EditText etFirstname = (EditText) child.findViewById(R.id.etFirstname);
				final EditText etLastname = (EditText) child.findViewById(R.id.etLastname);
				final TextView tvNationality = (TextView) child.findViewById(R.id.tvNationality);

				final TextView tvTitleTag = (TextView) child.findViewById(R.id.tvTitleTag);
				final TextView tvFirstnameTag = (TextView) child.findViewById(R.id.tvFirstnameTag);
				final TextView tvLastnameTag = (TextView) child.findViewById(R.id.tvLastnameTag);
				final TextView tvNationalityTag = (TextView) child.findViewById(R.id.tvNationalityTag);
				final TextView tvDobTag = (TextView) child.findViewById(R.id.tvDobTag);
				final LinearLayout ll_collectPoint = (LinearLayout) child.findViewById(R.id.ll_collectPoint);
				setTypefaceOpenSansRegular(child);
				tvheader.setTypeface(typefaceOpenSansSemiBold);
				tvTitleTag.setTypeface(typefaceOpenSansSemiBold);
				tvFirstnameTag.setTypeface(typefaceOpenSansSemiBold);
				tvLastnameTag.setTypeface(typefaceOpenSansSemiBold);
				tvNationalityTag.setTypeface(typefaceOpenSansSemiBold);
				tvDobTag.setTypeface(typefaceOpenSansSemiBold);
				if (userLoginDOFromPref != null) {
					if (!TextUtils.isEmpty(userLoginDOFromPref.title)) {
						for (int i = 0; i < arrTitleADT.length; i++) {

							if (userLoginDOFromPref.title.toLowerCase().equalsIgnoreCase(arrTitleADT[i].toLowerCase())
									|| userLoginDOFromPref.title.toLowerCase()
											.equalsIgnoreCase(arrTitleADTDesc[i].toLowerCase())) {
								tvTitle.setText(arrTitleADTDesc[i]);
								tvTitle.setTag(arrTitleADT[i]);
							}
						}
						tvTitleTag.setVisibility(View.VISIBLE);
						tvTitle.setTypeface(typefaceOpenSansSemiBold);
					}

					if (!TextUtils.isEmpty(userLoginDOFromPref.firstName)) {
						etFirstname.setText(userLoginDOFromPref.firstName + "");
						etFirstname.setTypeface(typefaceOpenSansSemiBold);
						tvFirstnameTag.setVisibility(View.VISIBLE);
						tvFirstnameTag.setTypeface(typefaceOpenSansSemiBold);
					}

					if (!TextUtils.isEmpty(userLoginDOFromPref.lastName)) {
						etLastname.setText(userLoginDOFromPref.lastName + "");
						tvLastnameTag.setVisibility(View.VISIBLE);
						etLastname.setTypeface(typefaceOpenSansSemiBold);
						tvLastnameTag.setTypeface(typefaceOpenSansSemiBold);
					}

					if (!TextUtils.isEmpty(userLoginDOFromPref.nationality)) {
						if (!etFirstname.getText().toString().equals("")) {
							tvNationality.setText(userLoginDOFromPref.nationality + "");
							tvNationalityTag.setVisibility(View.VISIBLE);
							tvNationality.setTypeface(typefaceOpenSansSemiBold);
						}
					}
				}
				if (userLoginDoFromServices != null) {
					if (TextUtils.isEmpty(tvTitle.getText().toString())
							&& !TextUtils.isEmpty(userLoginDoFromServices.title)) {
						for (int i = 0; i < arrTitleADT.length; i++) {

							if (userLoginDoFromServices.title.toLowerCase()
									.equalsIgnoreCase(arrTitleADT[i].toLowerCase())
									|| userLoginDoFromServices.title.toLowerCase()
											.equalsIgnoreCase(arrTitleADTDesc[i].toLowerCase())) {
								tvTitle.setText(arrTitleADTDesc[i]);
								tvTitle.setTag(arrTitleADT[i]);
							}
						}
						tvTitleTag.setVisibility(View.VISIBLE);
						tvTitle.setTypeface(typefaceOpenSansSemiBold);
					}

					if (TextUtils.isEmpty(etFirstname.getText().toString())
							&& !TextUtils.isEmpty(userLoginDoFromServices.firstName)) {
						etFirstname.setText(userLoginDoFromServices.firstName + "");
						tvFirstnameTag.setVisibility(View.VISIBLE);
						etFirstname.setTypeface(typefaceOpenSansSemiBold);
					}

					if (TextUtils.isEmpty(etLastname.getText().toString())
							&& !TextUtils.isEmpty(userLoginDoFromServices.lastName)) {
						etLastname.setText(userLoginDoFromServices.lastName + "");
						tvLastnameTag.setVisibility(View.VISIBLE);
						etLastname.setTypeface(typefaceOpenSansSemiBold);
					}

					if (TextUtils.isEmpty(tvNationality.getText().toString())
							&& !TextUtils.isEmpty(userLoginDoFromServices.nationality)) {
						if (!etFirstname.getText().toString().equals("")) {

							tvNationality.setText(userLoginDoFromServices.nationality + "");
							tvNationalityTag.setVisibility(View.VISIBLE);
							tvNationality.setTypeface(typefaceOpenSansSemiBold);
						}
					}
				}

				passengerInfoPersonDO = passengerInfoDO.vecPassengerInfoPersonDO.get(0);

				passengerInfoPersonDO.persontitle = tvTitle.getText().toString();
				passengerInfoPersonDO.personfirstname = etFirstname.getText().toString();
				passengerInfoPersonDO.personlastname = etLastname.getText().toString();
				passengerInfoPersonDO.personnationality = tvNationality.getText().toString();
				EditText et = (EditText) llAllAdultInfo.getChildAt(0).findViewById(R.id.etAirRewards);
				if (!TextUtils.isEmpty(et.getText().toString()))
					passengerInfoPersonDO.MembershipID = et.getText().toString();
				savedNationality = passengerInfoPersonDO.personnationality;
				passengerInfoPersonDO.personCountryCode = getCountryCodeByNationality(savedNationality);
			}
			llFasterBookingLogin.setVisibility(View.GONE);
		}
	}

	protected void moveToPersonaliseTrip() {
		Intent in = new Intent(PassengerInformationActivityNew.this, PassengerInformationContactActivity.class);
		in.putExtra(AppConstants.AIRPORTS_PRICE_TOTAL, airPriceQuoteDOTotal);
		startActivity(in);
	}

	@Override
	public void onButtonYesClick(String from) {
		super.onButtonYesClick(from);
		if (from.equalsIgnoreCase(AppConstants.INTERNET_PROBLEM))
			clickHome();
	}

	private String getCountryCodeByNationality(String nationality) {
		String strCountryCode = "";
		if (AppConstants.vecCountryNationalityDO != null)
			for (int i = 0; i < AppConstants.vecCountryNationalityDO.size(); i++) {
				if (AppConstants.vecCountryNationalityDO.get(i).CountryName.equalsIgnoreCase(nationality)) {
					strCountryCode = AppConstants.vecCountryNationalityDO.get(i).CountryCode + "";
					break;
				}
			}
		return strCountryCode;
	}

	private void updatePassengerFromSaved(LinearLayout llAllAdultInfo, SavedPassengerDO savedPassengerObj, int pos,
			String passangerCategory) {
		LinearLayout child = (LinearLayout) llAllAdultInfo.getChildAt(pos);

		final TextView tvheader = (TextView) child.findViewById(R.id.tvHeader);
		final TextView tvTitle = (TextView) child.findViewById(R.id.tvTitle);
		final EditText etFirstname = (EditText) child.findViewById(R.id.etFirstname);
		final EditText etLastname = (EditText) child.findViewById(R.id.etLastname);
		final EditText tvDob = (EditText) child.findViewById(R.id.tvDob);
		final TextView tvNationality = (TextView) child.findViewById(R.id.tvNationality);

		final TextView tvTitleTag = (TextView) child.findViewById(R.id.tvTitleTag);
		final TextView tvFirstnameTag = (TextView) child.findViewById(R.id.tvFirstnameTag);
		final TextView tvLastnameTag = (TextView) child.findViewById(R.id.tvLastnameTag);
		final TextView tvNationalityTag = (TextView) child.findViewById(R.id.tvNationalityTag);
		final TextView tvDobTag = (TextView) child.findViewById(R.id.tvDobTag);
		final LinearLayout ll_collectPoint = (LinearLayout) child.findViewById(R.id.ll_collectPoint);
		setTypefaceOpenSansRegular(child);
		tvheader.setTypeface(typefaceOpenSansSemiBold);
		tvTitleTag.setTypeface(typefaceOpenSansSemiBold);
		tvFirstnameTag.setTypeface(typefaceOpenSansSemiBold);
		tvLastnameTag.setTypeface(typefaceOpenSansSemiBold);
		tvNationalityTag.setTypeface(typefaceOpenSansSemiBold);
		tvDobTag.setTypeface(typefaceOpenSansSemiBold);
		if (savedPassengerObj != null) {
			if (!TextUtils.isEmpty(savedPassengerObj.title)) {
				String[] title, arrTitleDesc;
				if (passangerCategory.equalsIgnoreCase("Adult")) {
					for (int i = 0; i < arrTitleADT.length; i++) {

						if (savedPassengerObj.title.toLowerCase().equalsIgnoreCase(arrTitleADT[i].toLowerCase())
								|| savedPassengerObj.title.toLowerCase()
										.equalsIgnoreCase(arrTitleADTDesc[i].toLowerCase())) {
							tvTitle.setText(arrTitleADTDesc[i]);
							tvTitle.setTag(arrTitleADT[i]);
						}
					}
				} else if (passangerCategory.equalsIgnoreCase("Child")) {
					for (int i = 0; i < arrTitleCHD.length; i++) {

						if (savedPassengerObj.title.toLowerCase().equalsIgnoreCase(arrTitleCHD[i].toLowerCase())
								|| savedPassengerObj.title.toLowerCase()
										.equalsIgnoreCase(arrTitleCHDDesc[i].toLowerCase())) {
							tvTitle.setText(arrTitleCHDDesc[i]);
							tvTitle.setTag(arrTitleCHD[i]);
						}
					}
				} else if (passangerCategory.equalsIgnoreCase("Infant")) {
					for (int i = 0; i < arrTitleINF.length; i++) {

						if (savedPassengerObj.title.toLowerCase().equalsIgnoreCase(arrTitleINF[i].toLowerCase())
								|| savedPassengerObj.title.toLowerCase()
										.equalsIgnoreCase(arrTitleINFDesc[i].toLowerCase())) {
							tvTitle.setText(arrTitleINFDesc[i]);
							tvTitle.setTag(arrTitleINF[i]);
						}
					}
				}
				tvTitleTag.setVisibility(View.VISIBLE);
				tvTitle.setTypeface(typefaceOpenSansSemiBold);
			}

			if (!TextUtils.isEmpty(savedPassengerObj.firstName)) {
				etFirstname.setText(savedPassengerObj.firstName + "");
				etFirstname.setTypeface(typefaceOpenSansSemiBold);
				tvFirstnameTag.setVisibility(View.VISIBLE);
				tvFirstnameTag.setTypeface(typefaceOpenSansSemiBold);
			}

			if (!TextUtils.isEmpty(savedPassengerObj.lastName)) {
				etLastname.setText(savedPassengerObj.lastName + "");
				tvLastnameTag.setVisibility(View.VISIBLE);
				etLastname.setTypeface(typefaceOpenSansSemiBold);
				tvLastnameTag.setTypeface(typefaceOpenSansSemiBold);
			}

			if (!TextUtils.isEmpty(savedPassengerObj.nationality)) {
				tvNationality.setText(savedPassengerObj.nationality + "");
				tvNationalityTag.setVisibility(View.VISIBLE);
				tvNationality.setTypeface(typefaceOpenSansSemiBold);
			}
			if (!TextUtils.isEmpty(savedPassengerObj.dob)) {
				String[] tempDate = savedPassengerObj.dob.split("T");
				String[] dates = tempDate[0].split("-");
				int mYear = StringUtils.getInt(dates[0]);
				int mMonth = StringUtils.getInt(dates[1]);
				int mDate = StringUtils.getInt(dates[2]);

				Calendar mcal = Calendar.getInstance();
				mcal.set(mYear, mMonth - 1, mDate);

				tvDob.setText("" + mcal.get(Calendar.DAY_OF_MONTH) + " "
						+ CalendarUtility.getDobMonth(mcal.get(Calendar.MONTH)) + " " + mcal.get(Calendar.YEAR));
				tvDobTag.setVisibility(View.VISIBLE);
				tvDob.setTypeface(typefaceOpenSansSemiBold);
			}
		}

		passengerInfoPersonDO = passengerInfoDO.vecPassengerInfoPersonDO.get(pos);

		passengerInfoPersonDO.persontitle = tvTitle.getText().toString();
		passengerInfoPersonDO.personfirstname = etFirstname.getText().toString();
		passengerInfoPersonDO.personlastname = etLastname.getText().toString();
		passengerInfoPersonDO.personnationality = tvNationality.getText().toString();
		passengerInfoPersonDO.personDOB = savedPassengerObj.dob.toString();
		EditText et = (EditText) llAllAdultInfo.getChildAt(pos).findViewById(R.id.etAirRewards);
		if (!TextUtils.isEmpty(et.getText().toString()))
			passengerInfoPersonDO.MembershipID = et.getText().toString();
		savedNationality = passengerInfoPersonDO.personnationality;
		passengerInfoPersonDO.personCountryCode = getCountryCodeByNationality(savedNationality);
	}

}