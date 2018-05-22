package com.winit.airarabia;

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
import com.winit.airarabia.objects.CountryISDDO;
import com.winit.airarabia.objects.LoginDO;
import com.winit.airarabia.objects.PassengerInfoDO;
import com.winit.airarabia.objects.RequestDO;
import com.winit.airarabia.utils.SharedPrefUtils;
import com.winit.airarabia.webaccess.Response;

public class PassengerInformationContactActivity extends BaseActivity implements OnClickListener, DataListener{

	private ScrollView svPassenger;
	private TextView tvContactPhoneISDCode;
	private EditText etContactfirstname,etContactlastname,etContactphonenum, etContactemail;
	private PassengerInfoDO passengerInfoDO;
	private final String contactcity = "contactcity";
	private final String personnationality = "personnationality";
	private AirPriceQuoteDO airPriceQuoteDOTotal;

	private String[] arrTitleADT,arrTitleADTDesc;
	private TextView tvContacttitle,tv_header;
	private EditText edContactnationality,edContactcity;
	private ImageView ivCheckContactP1;
	private LinearLayout llCheckContactP1;
	private int COUNTRY_RESULT_CODE=8000, NATIONALITY_RESULT_CODE=7000;
	private CountryDO countryDO, nationalityDo;
	private TextView tvContacttitleTag,tvNationalityTag,tvContactcityTag,tvFirstnameTag,tvLastnameTag,tvContactphonenumTag,tvContactemailTag, tvTitleSelTag,tvContactinformation,tv_passengertextboxbeside; 
	private String countryCodeMobileTemp = "";

	private PassengerInformationContactActivity.BCR bcr;

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


		tvHeaderTitle.setText(getString(R.string.contactdetails));
		if(getIntent().hasExtra(AppConstants.AIRPORTS_PRICE_TOTAL))
			airPriceQuoteDOTotal = (AirPriceQuoteDO) getIntent().getExtras().getSerializable(AppConstants.AIRPORTS_PRICE_TOTAL);

		svPassenger  = (ScrollView) layoutInflater.inflate(R.layout.passengerinformation_contact, null);
		
		OnFocusChangeListener focusChangeListener,focusChangeListener1,focusChangeListener2,focusChangeListener3,focusChangeListener4;
		OnTouchListener touchListener;
		
		llMiddleBase.addView(svPassenger, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

		tvContacttitle 					= (TextView) svPassenger.findViewById(R.id.tvContacttitle);
		edContactnationality 			= (EditText) svPassenger.findViewById(R.id.tvContactnationality);
		edContactcity 					= (EditText) svPassenger.findViewById(R.id.tvContactcity);
		etContactfirstname 				= (EditText) svPassenger.findViewById(R.id.etContactfirstname);
		etContactlastname 				= (EditText) svPassenger.findViewById(R.id.etContactlastname);
		tvContactPhoneISDCode 			= (TextView) svPassenger.findViewById(R.id.tvContactphonenumCountryCode);
		etContactphonenum 				= (EditText) svPassenger.findViewById(R.id.etContactphonenum);
		etContactemail 					= (EditText) svPassenger.findViewById(R.id.etContactemail);

		//=============================newly added=====================================

		tv_header 							= (TextView) svPassenger.findViewById(R.id.tv_header);
		tvContacttitleTag 					= (TextView) svPassenger.findViewById(R.id.tvContacttitleTag);
		tvNationalityTag		 			= (TextView) svPassenger.findViewById(R.id.tvNationalityTag);
		tvContactcityTag 					= (TextView) svPassenger.findViewById(R.id.tvContactcityTag);
		tvFirstnameTag		 				= (TextView) svPassenger.findViewById(R.id.tvFirstnameTag);
		tvLastnameTag		 				= (TextView) svPassenger.findViewById(R.id.tvLastnameTag);
		tvContactphonenumTag 				= (TextView) svPassenger.findViewById(R.id.tvContactphonenumTag);
		tvContactemailTag 					= (TextView) svPassenger.findViewById(R.id.tvContactemailTag);
		tvContactinformation 					= (TextView) svPassenger.findViewById(R.id.tvContactinformation);
		tv_passengertextboxbeside 					= (TextView) svPassenger.findViewById(R.id.tv_passengertextboxbeside);
		tvTitleSelTag						= tvNationalityTag;

		llCheckContactP1	 				= (LinearLayout) svPassenger.findViewById(R.id.llCheckContactP1);
		ivCheckContactP1	 				= (ImageView) svPassenger.findViewById(R.id.ivCheckContactP1);


		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(getString(R.string.Continue));
		
		setTypefaceOpenSansRegular(svPassenger);
		tvContacttitleTag.setTypeface(typefaceOpenSansSemiBold);
		tvNationalityTag.setTypeface(typefaceOpenSansSemiBold);
		tvContactcityTag.setTypeface(typefaceOpenSansSemiBold);
		tvFirstnameTag.setTypeface(typefaceOpenSansSemiBold);
		tvLastnameTag.setTypeface(typefaceOpenSansSemiBold);
		tvContactphonenumTag.setTypeface(typefaceOpenSansSemiBold);
		tvContactemailTag.setTypeface(typefaceOpenSansSemiBold);
		tv_header.setTypeface(typefaceOpenSansSemiBold);
		tvContactinformation.setTypeface(typefaceOpenSansSemiBold);
		tv_passengertextboxbeside.setTypeface(typefaceOpenSansSemiBold);
		
		if(AppConstants.bookingFlightDO.passengerInfoDO != null)
			passengerInfoDO 	= AppConstants.bookingFlightDO.passengerInfoDO;
		else
			finish();
		
		focusChangeListener = new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					tvFirstnameTag.setVisibility(View.VISIBLE);
				}else if(((EditText) v).getText().toString().isEmpty()){
					tvFirstnameTag.setVisibility(View.GONE);
				}
			}
		};
		focusChangeListener1 = new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					tvLastnameTag.setVisibility(View.VISIBLE);
				}else if(((EditText) v).getText().toString().isEmpty()){
					tvLastnameTag.setVisibility(View.GONE);
				}
			}
		};
		focusChangeListener2 = new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					tvContactemailTag.setVisibility(View.VISIBLE);
				}else if(((EditText) v).getText().toString().isEmpty()){
					tvContactemailTag.setVisibility(View.GONE);
				}
			}
		};
		focusChangeListener3 = new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					tvContactphonenumTag.setVisibility(View.VISIBLE);
				}else if(((EditText) v).getText().toString().isEmpty()){
					tvContactphonenumTag.setVisibility(View.GONE);
				}
			}
		};
		etContactfirstname.setOnFocusChangeListener(focusChangeListener);
		etContactlastname.setOnFocusChangeListener(focusChangeListener1);
		etContactemail.setOnFocusChangeListener(focusChangeListener2);
		etContactphonenum.setOnFocusChangeListener(focusChangeListener3);
		
//		tvHeaderTitle.setText(getResources().getString(R.S));
		
		touchListener = new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.performClick();
				return false;
			}
				};
		
				
		focusChangeListener4	= new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				hideKeyBoard(v);
			}
				};
		edContactnationality.setOnTouchListener(touchListener);
		edContactnationality.setOnFocusChangeListener(focusChangeListener4);
		edContactcity.setOnTouchListener(touchListener);
		edContactcity.setOnFocusChangeListener(focusChangeListener4);
		
		if(AppConstants.country != null && !AppConstants.country.equalsIgnoreCase("")) {
			edContactcity.setText(AppConstants.country);
			tvContactcityTag.setVisibility(View.VISIBLE);
			tvContactPhoneISDCode.setText(getCountryISDCode(AppConstants.country));
			edContactcity.setTypeface(typefaceOpenSansSemiBold);
			tvContactPhoneISDCode.setTypeface(typefaceOpenSansSemiBold);
			if(AppConstants.country.equalsIgnoreCase("India"))
				tvContactPhoneISDCode.setText("91");
			else if(AppConstants.country.equalsIgnoreCase("united arab emirates"))
				tvContactPhoneISDCode.setText("971");
				
			if(tvContactPhoneISDCode.getText().toString().isEmpty())
				edContactcity.setText("");
		}
		
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
		arrTitleADTDesc = resources.getStringArray(R.array.title_adt_desc);
		if(AppConstants.vecCountryNationalityDO == null )
		{
			callServiceGetNationalitiesData();
		}
		else if(AppConstants.vecCountryDO == null)
		{
			callServiceGetCountryNamesData();
		}
		else if(AppConstants.vecCountryISDDO == null)
		{
			callServiceGetCountryISDCodesData();
		}
		else
		{
			updateContactWithPassanger1();
		}
		btnSubmitNext.setOnClickListener(this);
		tvContacttitle.setOnClickListener(this);
		edContactcity.setOnClickListener(this);
		edContactnationality.setOnClickListener(this);
		llCheckContactP1.setOnClickListener(this);
		ivCheckContactP1.setOnClickListener(this);
		
		etContactfirstname.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start,
					int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

								Insider.tagEvent(AppConstants.FirstNameContactDetails, PassengerInformationContactActivity.this);
				//Insider.Instance.tagEvent(PassengerInformationContactActivity.this,AppConstants.FirstNameContactDetails);
				trackEvent("Contact", AppConstants.FirstNameContactDetails, "");

				if(!(etContactfirstname.getText().toString()).equalsIgnoreCase("")){

//					tvFirstnameTag.setVisibility(View.VISIBLE);
					etContactfirstname.setTypeface(typefaceOpenSansSemiBold);
				}else {
//					tvFirstnameTag.setVisibility(View.GONE);
				}
			}
		});
		etContactlastname.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start,
					int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
								Insider.tagEvent(AppConstants.LastNameContactDetails, PassengerInformationContactActivity.this);
				//Insider.Instance.tagEvent(PassengerInformationContactActivity.this,AppConstants.LastNameContactDetails);
				trackEvent("Contact", AppConstants.LastNameContactDetails, "");

				if(!(etContactlastname.getText().toString()).equalsIgnoreCase("")){

//					tvLastnameTag.setVisibility(View.VISIBLE);
					etContactlastname.setTypeface(typefaceOpenSansSemiBold);
				}else {
//					tvLastnameTag.setVisibility(View.GONE);
				}
			}
		});
		etContactemail.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start,
					int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
								Insider.tagEvent(AppConstants.EmailContactDetails, PassengerInformationContactActivity.this);
				//Insider.Instance.tagEvent(PassengerInformationContactActivity.this,AppConstants.EmailContactDetails);
				trackEvent("Contact", AppConstants.EmailContactDetails, "");

				if(!(etContactemail.getText().toString()).equalsIgnoreCase("")){

//					tvContactemailTag.setVisibility(View.VISIBLE);
					etContactemail.setTypeface(typefaceOpenSansSemiBold);
				}else {
//					tvContactemailTag.setVisibility(View.GONE);
				}
			}
		});
		etContactphonenum.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start,
					int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
								Insider.tagEvent(AppConstants.MobileContactDetails, PassengerInformationContactActivity.this);
				//Insider.Instance.tagEvent(PassengerInformationContactActivity.this,AppConstants.MobileContactDetails);
				trackEvent("Contact", AppConstants.MobileContactDetails, "");

				if(!(etContactphonenum.getText().toString()).equalsIgnoreCase("")){

//					tvContactphonenumTag.setVisibility(View.VISIBLE);
					etContactphonenum.setTypeface(typefaceOpenSansSemiBold);
				}else {
//					tvContactphonenumTag.setVisibility(View.INVISIBLE);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnSubmitNext) {
						Insider.tagEvent(AppConstants.ContinueContactDetail, PassengerInformationContactActivity.this);
			//Insider.Instance.tagEvent(PassengerInformationContactActivity.this,AppConstants.ContinueContactDetail);
			trackEvent("Contact", AppConstants.ContinueContactDetail, "");

			AppConstants.bookingFlightDO.passengerInfoDO = passengerInfoDO;
			if (updatePassengerInfo()) {
				moveToPersonaliseTrip();
			}
		} 
		else if (v.getId() == R.id.tvContacttitle){
						Insider.tagEvent(AppConstants.TitleFieldContactDetails, PassengerInformationContactActivity.this);
			//Insider.Instance.tagEvent(PassengerInformationContactActivity.this,AppConstants.TitleFieldContactDetails);
			trackEvent("Contact", AppConstants.TitleFieldContactDetails, "");

			showSelTextPopup(tvContacttitle, getString(R.string.Title));
			tvContacttitleTag.setVisibility(View.VISIBLE);
		}
		else if (v.getId() == R.id.tvContactcity)
		{
						Insider.tagEvent(AppConstants.NationalityContactDetails, PassengerInformationContactActivity.this);
			//Insider.Instance.tagEvent(PassengerInformationContactActivity.this,AppConstants.NationalityContactDetails);
			trackEvent("Contact", AppConstants.NationalityContactDetails, "");

			hideKeyBoard(edContactcity);
			if (AppConstants.vecCountryDO != null && AppConstants.vecCountryDO.size()>0) {
				Intent i = new Intent(PassengerInformationContactActivity.this,SelectCountry.class);
				i.putExtra("vecCountry", AppConstants.vecCountryDO);
				if(!TextUtils.isEmpty(edContactcity.getText().toString()))
					i.putExtra("SelectedCountry", edContactcity.getText().toString());
				i.putExtra("HeaderTitle", getString(R.string.please_select_country));
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				//i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivityForResult(i, COUNTRY_RESULT_CODE);
			}
		}
		else if (v.getId() == R.id.tvContactnationality){
						Insider.tagEvent(AppConstants.CountryOfResidenceContactDetails, PassengerInformationContactActivity.this);
			//Insider.Instance.tagEvent(PassengerInformationContactActivity.this,AppConstants.CountryOfResidenceContactDetails);
			trackEvent("Contact", AppConstants.CountryOfResidenceContactDetails, "");

			hideKeyBoard(edContactnationality);
			if (AppConstants.vecCountryNationalityDO != null && AppConstants.vecCountryNationalityDO.size()>0) {
				Intent i = new Intent(PassengerInformationContactActivity.this,SelectCountry.class);
				if(!TextUtils.isEmpty(edContactnationality.getText().toString()))
					i.putExtra("SelectedCountry", edContactnationality.getText().toString());
				i.putExtra("HeaderTitle", getString(R.string.please_select_your_nationality));
				i.putExtra("vecCountry", AppConstants.vecCountryNationalityDO);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				//i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivityForResult(i, NATIONALITY_RESULT_CODE);
			}
		}
		else if (v.getId() == R.id.llCheckContactP1)
		{
			if((Boolean) llCheckContactP1.getTag()){
				ivCheckContactP1.setImageResource(R.drawable.uncheckblack);
				updateContactWithoutPassanger1();
				
			}
			else{
								Insider.tagEvent(AppConstants.CheckBoxContactInfo, PassengerInformationContactActivity.this);
				//Insider.Instance.tagEvent(PassengerInformationContactActivity.this,AppConstants.CheckBoxContactInfo);
				trackEvent("Contact", AppConstants.CheckBoxContactInfo, "");

				ivCheckContactP1.setBackgroundResource(R.drawable.checkblack);
				updateContactWithPassanger1();
			}
		}
		else if (v.getId() == R.id.ivCheckContactP1)
		{
			llCheckContactP1.performClick();
			//			if((Boolean) llCheckContactP1.getTag())
			//				updateContactWithoutPassanger1();
			//			else updateContactWithPassanger1();
		
		}
	}

	private void updateContactWithPassanger1()
	{
		passengerInfoDO.passengerInfoContactDO.contacttitle = passengerInfoDO.vecPassengerInfoPersonDO.get(0).persontitle;
		passengerInfoDO.passengerInfoContactDO.contactnationality = passengerInfoDO.vecPassengerInfoPersonDO.get(0).personnationality;
		passengerInfoDO.passengerInfoContactDO.contactfirstname = passengerInfoDO.vecPassengerInfoPersonDO.get(0).personfirstname;
		passengerInfoDO.passengerInfoContactDO.contactlastname = passengerInfoDO.vecPassengerInfoPersonDO.get(0).personlastname;
		//		passengerInfoDO.passengerInfoContactDO.contactcity = passengerInfoDO.vecPassengerInfoPersonDO.get(0).person;

		for (int i = 0; i < arrTitleADT.length; i++) {

			if(passengerInfoDO.passengerInfoContactDO.contacttitle.toLowerCase().equalsIgnoreCase(arrTitleADT[i].toLowerCase())
					|| passengerInfoDO.passengerInfoContactDO.contacttitle.toLowerCase().equalsIgnoreCase(arrTitleADTDesc[i].toLowerCase()))
			{
				tvContacttitle.setText(arrTitleADTDesc[i]);
				tvContacttitle.setTag(arrTitleADT[i]);
			}
		}
		if (!(tvContacttitle.getText().toString()).equalsIgnoreCase("")){ 
			tvContacttitleTag.setVisibility(View.VISIBLE);
			tvContacttitle.setTypeface(typefaceOpenSansSemiBold);
		}


		//		tvContactcity.setText(passengerInfoDO.passengerInfoContactDO.contacttitle);
		edContactnationality.setText(passengerInfoDO.passengerInfoContactDO.contactnationality);
		if (!(edContactnationality.getText().toString()).equalsIgnoreCase("")) {
			tvNationalityTag.setVisibility(View.VISIBLE);
			edContactnationality.setTypeface(typefaceOpenSansSemiBold);
		}
		etContactfirstname.setText(passengerInfoDO.passengerInfoContactDO.contactfirstname);
		if (!(etContactfirstname.getText().toString()).equalsIgnoreCase("")){ 
			tvFirstnameTag.setVisibility(View.VISIBLE);
			etContactfirstname.setTypeface(typefaceOpenSansSemiBold);
		}
		etContactlastname.setText(passengerInfoDO.passengerInfoContactDO.contactlastname);
		if (!(etContactlastname.getText().toString()).equalsIgnoreCase("")) {
			tvLastnameTag.setVisibility(View.VISIBLE);
			etContactlastname.setTypeface(typefaceOpenSansSemiBold);
		}
		if (!(etContactemail.getText().toString()).equalsIgnoreCase("")) {
			tvContactemailTag.setVisibility(View.VISIBLE);
			etContactemail.setTypeface(typefaceOpenSansSemiBold);
		}

		//		tvContactcity.setText(passengerInfoDO.passengerInfoContactDO.contactnationality);
		////		if (!(tvContactcity.getText().toString()).equalsIgnoreCase("")) 
		////			tvContactcityTag.setVisibility(View.VISIBLE);

		llCheckContactP1.setTag(true);
		ivCheckContactP1.setBackgroundResource(R.drawable.checkblack);
		ivCheckContactP1.setImageResource(R.drawable.checkblack);

		updateDataFromMyProfile();

	}
	private void updateContactWithoutPassanger1()
	{
		passengerInfoDO.passengerInfoContactDO.contacttitle = "";
		passengerInfoDO.passengerInfoContactDO.contactnationality = "";
		passengerInfoDO.passengerInfoContactDO.contactfirstname = "";
		passengerInfoDO.passengerInfoContactDO.contactlastname = "";
		passengerInfoDO.passengerInfoContactDO.contactnationality = "";
		passengerInfoDO.passengerInfoContactDO.contactcity = "";
		passengerInfoDO.passengerInfoContactDO.contactphonenum = "";
		passengerInfoDO.passengerInfoContactDO.contactphonenumCountryCode = "";
		passengerInfoDO.passengerInfoContactDO.contactemail = "";
		passengerInfoDO.passengerInfoContactDO.contacttitle = "";

		//		passengerInfoDO.passengerInfoContactDO.contactcity = passengerInfoDO.vecPassengerInfoPersonDO.get(0).personlastname;

		for (int i = 0; i < arrTitleADT.length; i++) {

			if(passengerInfoDO.passengerInfoContactDO.contacttitle.toLowerCase().equalsIgnoreCase(arrTitleADT[i].toLowerCase())
					|| passengerInfoDO.passengerInfoContactDO.contacttitle.toLowerCase().equalsIgnoreCase(arrTitleADTDesc[i].toLowerCase()))
			{
				tvContacttitle.setText(arrTitleADTDesc[i]);
				tvContacttitle.setTag(arrTitleADT[i]);
			}
		}
		//		tvContactcity.setText(passengerInfoDO.passengerInfoContactDO.contacttitle);
		edContactnationality.setText(passengerInfoDO.passengerInfoContactDO.contacttitle);
		etContactfirstname.setText(passengerInfoDO.passengerInfoContactDO.contactfirstname);
		etContactlastname.setText(passengerInfoDO.passengerInfoContactDO.contactlastname);
		edContactnationality.setText(passengerInfoDO.passengerInfoContactDO.contactnationality);
		edContactcity.setText(passengerInfoDO.passengerInfoContactDO.contactcity);
		etContactphonenum.setText(passengerInfoDO.passengerInfoContactDO.contactphonenum);
		tvContactPhoneISDCode.setText(passengerInfoDO.passengerInfoContactDO.contactphonenumCountryCode);
		etContactemail.setText(passengerInfoDO.passengerInfoContactDO.contactemail);
		edContactnationality.setTypeface(typefaceOpenSansSemiBold);
		etContactfirstname.setTypeface(typefaceOpenSansSemiBold);
		etContactlastname.setTypeface(typefaceOpenSansSemiBold);
		edContactnationality.setTypeface(typefaceOpenSansSemiBold);
		edContactcity.setTypeface(typefaceOpenSansSemiBold);
		etContactphonenum.setTypeface(typefaceOpenSansSemiBold);
		tvContactPhoneISDCode.setTypeface(typefaceOpenSansSemiBold);
		etContactemail.setTypeface(typefaceOpenSansSemiBold);
		
		if (!(tvContacttitle.getText().toString()).equalsIgnoreCase("")){ 
			tvContacttitleTag.setVisibility(View.VISIBLE);
			tvContacttitle.setTypeface(typefaceOpenSansSemiBold);
		}
		
		tvContacttitle.setText(passengerInfoDO.passengerInfoContactDO.contacttitle);
		if (edContactnationality.getText().toString().equalsIgnoreCase("")) {
			tvNationalityTag.setVisibility(View.GONE);
		}
		if (edContactcity.getText().toString().equalsIgnoreCase("")) {
			tvContactcityTag.setVisibility(View.GONE);
		}
		if (tvContacttitle.getText().toString().equalsIgnoreCase("")) {
			tvContacttitleTag.setVisibility(View.GONE);
		}

		llCheckContactP1.setTag(false);
		ivCheckContactP1.setBackgroundResource(R.drawable.uncheckblack);
		ivCheckContactP1.setImageResource(R.drawable.uncheckblack);

	}
	private boolean updatePassengerInfo() {

		if(tvContacttitle.getTag() != null)
			passengerInfoDO.passengerInfoContactDO.contacttitle = tvContacttitle.getTag().toString();

		passengerInfoDO.passengerInfoContactDO.contactnationality = edContactnationality.getText().toString();
		passengerInfoDO.passengerInfoContactDO.contactcity = edContactcity.getText().toString();
		passengerInfoDO.passengerInfoContactDO.countryName = edContactcity.getText().toString();
		passengerInfoDO.passengerInfoContactDO.countryCode = getCountryCodeByCountryName(edContactcity.getText().toString()+"");
		passengerInfoDO.passengerInfoContactDO.contactfirstname = etContactfirstname.getText().toString();
		passengerInfoDO.passengerInfoContactDO.contactlastname = etContactlastname.getText().toString();
		passengerInfoDO.passengerInfoContactDO.contactphonenumCountryCode = tvContactPhoneISDCode.getText().toString();
		
		String strMobileNum = etContactphonenum.getText().toString();
		
		if(!TextUtils.isEmpty(countryCodeMobileTemp))
			passengerInfoDO.passengerInfoContactDO.contactphonenum = countryCodeMobileTemp+"-"+ etContactphonenum.getText().toString();
		else
			passengerInfoDO.passengerInfoContactDO.contactphonenum = etContactphonenum.getText().toString();
		passengerInfoDO.passengerInfoContactDO.contactemail = etContactemail.getText().toString();

		if (passengerInfoDO.passengerInfoContactDO.contacttitle.equalsIgnoreCase("")
				|| passengerInfoDO.passengerInfoContactDO.contactnationality.equalsIgnoreCase("")
				|| passengerInfoDO.passengerInfoContactDO.contactcity.equalsIgnoreCase("")
				|| passengerInfoDO.passengerInfoContactDO.contactfirstname.equalsIgnoreCase("")
				|| passengerInfoDO.passengerInfoContactDO.contactlastname.equalsIgnoreCase("")
				|| passengerInfoDO.passengerInfoContactDO.contactphonenumCountryCode.equalsIgnoreCase("")
				|| passengerInfoDO.passengerInfoContactDO.contactphonenum.equalsIgnoreCase("")
				|| passengerInfoDO.passengerInfoContactDO.contactemail.equalsIgnoreCase("")) {
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.alert_msg_fill_all_fields_required_by_passenger_info),
					getString(R.string.Ok), "", "");
			return false;
		} else if (!passengerInfoDO.passengerInfoContactDO.contactemail.equalsIgnoreCase("")
				&& !emailValidate(passengerInfoDO.passengerInfoContactDO.contactemail).matches()) {
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.EmailIdError), getString(R.string.Ok),"", "");
			return false;
		}
		else if (TextUtils.isEmpty(strMobileNum)) {
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.MobileError), getString(R.string.Ok),"", "");
			return false;
		} 
		else if (strMobileNum.length() < 1) {
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.MobileError_10_digit), getString(R.string.Ok),"", "");
			return false;
		} 
		else
			return true;
	}

	private void showSelTextPopup(final TextView tvSel, String titleOfPopup) {

		LinearLayout ll = (LinearLayout) layoutInflater.inflate(R.layout.popup_titles, null);
		TextView tvTitleHeader = (TextView) ll.findViewById(R.id.tvTitleHeader);
		tvTitleHeader.setText(titleOfPopup+"");
		final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_No_Title);
		dialog.setContentView(ll);

		LinearLayout llPopupTitleMain = (LinearLayout) ll.findViewById(R.id.llPopupTitleMain);

		for (int i = 0; i < arrTitleADT.length; i++) {
			LinearLayout llTitle = (LinearLayout) layoutInflater.inflate(R.layout.popup_title_item, null);

			final TextView tvTitleItem = (TextView) llTitle.findViewById(R.id.tvTitleItem);
			llPopupTitleMain.addView(llTitle);

			tvTitleItem.setText(arrTitleADTDesc[i]);
			tvTitleItem.setTag(arrTitleADT[i]);

			if (tvSel.getText().toString().equalsIgnoreCase(arrTitleADTDesc[i]))
				tvTitleItem.setBackgroundColor(resources.getColor(R.color.ash_color));
			tvTitleItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					tvSel.setText(tvTitleItem.getText().toString());
					tvSel.setTag(tvTitleItem.getTag().toString());

					tvTitleItem.setBackgroundColor(resources.getColor(R.color.ash_color));

					dialog.dismiss();
				}
			});
		}

		dialog.show();
	}



	private void callServiceGetNationalitiesData() {
		showLoader("");
		if (new CommonBL(PassengerInformationContactActivity.this,
				PassengerInformationContactActivity.this).getNationalitiesData()) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	private void callServiceGetCountryNamesData() {
		showLoader("");
		if (new CommonBL(PassengerInformationContactActivity.this,
				PassengerInformationContactActivity.this).getCountryNamesData()) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	private void callServiceGetCountryISDCodesData() {
		showLoader("");
		if (new CommonBL(PassengerInformationContactActivity.this,
				PassengerInformationContactActivity.this).getCountryISDCodesData()) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dataRetreived(Response data) {
		hideLoader();
		if (data != null && !data.isError) {
			switch (data.method) {
			case WS_NATIONALITIES:
				AppConstants.vecCountryNationalityDO = (Vector<CountryDO>) data.data;

				Collections.sort(AppConstants.vecCountryNationalityDO, new Comparator<CountryDO>() {

					@Override
					public int compare(CountryDO compR , CountryDO compL) 
					{

						return compR.CountryName.compareToIgnoreCase(compL.CountryName);
					}
				});
				if(AppConstants.vecCountryDO == null)
					callServiceGetCountryNamesData();
				else if(AppConstants.vecCountryISDDO == null)
				{
					callServiceGetCountryISDCodesData();
				}
				else
				{
					updateContactWithPassanger1();
				}
					
				break;

			case WS_COUNTRYNAMES:
				AppConstants.vecCountryDO = (Vector<CountryDO>) data.data;

				Collections.sort(AppConstants.vecCountryDO, new Comparator<CountryDO>() {

					@Override
					public int compare(CountryDO compR , CountryDO compL) 
					{

						return compR.CountryName.compareToIgnoreCase(compL.CountryName);
					}
				});
				if(AppConstants.vecCountryISDDO == null)
					callServiceGetCountryISDCodesData();
				else
					updateContactWithPassanger1();
				break;

			case WS_COUNTRYISDCODES:
				AppConstants.vecCountryISDDO = (Vector<CountryISDDO>) data.data;
				updateContactWithPassanger1();
				//				updateDataFromMyProfile();
				break;

			default:
				break;
			}
		} else
		{
			if(data.data instanceof String)
			{
				if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
					showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
				else showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
			}
		}
	}

	protected void moveToPersonaliseTrip() {
		Intent in = new Intent(PassengerInformationContactActivity.this,PersonaliseBaggageDetailsActivity.class);
		in.putExtra(AppConstants.AIRPORTS_PRICE_TOTAL, airPriceQuoteDOTotal);
		startActivity(in);
	}

	@Override
	public void onButtonYesClick(String from) {
		super.onButtonYesClick(from);
		if(from.equalsIgnoreCase(AppConstants.INTERNET_PROBLEM))
			clickHome();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == COUNTRY_RESULT_CODE && resultCode == RESULT_OK) 
		{
			countryDO=new CountryDO();
			countryDO = ((CountryDO)data.getSerializableExtra("Country_Selected"));
			edContactcity.setText(countryDO.CountryName+"");
			tvContactcityTag.setVisibility(View.VISIBLE);
			tvContactPhoneISDCode.setText(getCountryISDCode(countryDO.CountryName+""));
			edContactcity.setTypeface(typefaceOpenSansSemiBold);
			tvContactPhoneISDCode.setTypeface(typefaceOpenSansSemiBold);
		}

		if (requestCode == NATIONALITY_RESULT_CODE && resultCode == RESULT_OK) 
		{
			nationalityDo=new CountryDO();
			nationalityDo = ((CountryDO)data.getSerializableExtra("Country_Selected"));
			edContactnationality.setText(nationalityDo.CountryName+"");
			tvNationalityTag.setVisibility(View.VISIBLE);
			edContactnationality.setTypeface(typefaceOpenSansSemiBold);
		}
	}
	protected void onResume() {
		super.onResume();
		AppConstants.bookingFlightDO.vecBaggageRequestDOs = new Vector<RequestDO>();
		AppConstants.bookingFlightDO.vecSeatRequestDOs = new Vector<RequestDO>();
		AppConstants.bookingFlightDO.vecMealReqDOs = new Vector<RequestDO>();
		AppConstants.bookingFlightDO.vecSSRRequests = new Vector<RequestDO>();
		AppConstants.bookingFlightDO.vecInsrRequestDOs = new Vector<RequestDO>();

		AppConstants.vecAirBaggageDetailsDO = null;
		AppConstants.AirSeatMapDO = null;
		AppConstants.insuranceQuoteDO = null;
		AppConstants.airMealDetailsDO = null;
		AppConstants.vecHalaDOs = null;
	}


	private void updateDataFromMyProfile()
	{
		if ((SharedPrefUtils.getKeyValue(PassengerInformationContactActivity.this, SharedPreferenceStrings.APP_PREFERENCES, SharedPreferenceStrings.isLoggedIn)).equalsIgnoreCase("1"))
		{
			LoginDO userLoginDoFromServices=gettingObjecFromSharedPrerence(SharedPreferenceStrings.loginDo);
			LoginDO userLoginDo	=(LoginDO)	gettingObjecFromSharedPrerence("LoginDO");
			if(userLoginDo != null && passengerInfoDO.vecPassengerInfoPersonDO!=null 
					&& passengerInfoDO.vecPassengerInfoPersonDO.size()>0)
			{

				if(!TextUtils.isEmpty(userLoginDo.countryOfResidence))
				{
					String countryCodePhone = getCountryISDCode(userLoginDo.countryOfResidence)+"";
					edContactcity.setText(userLoginDo.countryOfResidence+"");
					tvContactPhoneISDCode.setText(countryCodePhone+"");
					tvContactPhoneISDCode.setTypeface(typefaceOpenSansSemiBold);
					tvContactcityTag.setVisibility(View.VISIBLE);
					passengerInfoDO.passengerInfoContactDO.contactcity =userLoginDo.countryOfResidence + "";
					passengerInfoDO.passengerInfoContactDO.contactphonenumCountryCode = countryCodePhone+"";
					edContactcity.setTypeface(typefaceOpenSansSemiBold);
					passengerInfoDO.passengerInfoContactDO.countryName = userLoginDo.countryOfResidence + "";
					passengerInfoDO.passengerInfoContactDO.countryCode = getCountryCodeByCountryName(userLoginDo.countryOfResidence+"");
				}
				else if(!TextUtils.isEmpty(userLoginDoFromServices.countryOfResidence))
				{
					String countryCodePhone = getCountryISDCode(userLoginDoFromServices.countryOfResidence)+"";
					edContactcity.setText(userLoginDoFromServices.countryOfResidence+"");
					tvContactPhoneISDCode.setText(countryCodePhone+"");
					tvContactPhoneISDCode.setTypeface(typefaceOpenSansSemiBold);
					tvContactcityTag.setVisibility(View.VISIBLE);
					passengerInfoDO.passengerInfoContactDO.contactcity =userLoginDoFromServices.countryOfResidence + "";
					passengerInfoDO.passengerInfoContactDO.contactphonenumCountryCode = countryCodePhone+"";
					edContactcity.setTypeface(typefaceOpenSansSemiBold);
					passengerInfoDO.passengerInfoContactDO.countryName = userLoginDoFromServices.countryOfResidence + "";
					passengerInfoDO.passengerInfoContactDO.countryCode = getCountryCodeByCountryName(userLoginDoFromServices.countryOfResidence+"");
				}



				if(!TextUtils.isEmpty(userLoginDo.loginContactInformationDO.mobileNumber))
				{
					countryCodeMobileTemp = getCountryMobileCodeFromMobileWithCountryCode(userLoginDo.loginContactInformationDO.mobileNumber+"");
					etContactphonenum.setText(getMobileNumberFromMobileWithoutCountryCode(userLoginDo.loginContactInformationDO.mobileNumber+""));
					//					etContactphonenum.setText(userLoginDo.loginContactInformationDO.mobileNumber+"");
					passengerInfoDO.passengerInfoContactDO.contactphonenum = userLoginDo.loginContactInformationDO.mobileNumber + "";
					tvContactphonenumTag.setVisibility(View.VISIBLE);
					etContactphonenum.setTypeface(typefaceOpenSansSemiBold);
				}
				else if(!TextUtils.isEmpty(userLoginDoFromServices.loginContactInformationDO.mobileNumber))
				{
					countryCodeMobileTemp = getCountryMobileCodeFromMobileWithCountryCode(userLoginDoFromServices.loginContactInformationDO.mobileNumber+"");
					etContactphonenum.setText(getMobileNumberFromMobileWithoutCountryCode(userLoginDoFromServices.loginContactInformationDO.mobileNumber+""));
					//					etContactphonenum.setText(userLoginDo.loginContactInformationDO.mobileNumber+"");
					passengerInfoDO.passengerInfoContactDO.contactphonenum = userLoginDoFromServices.loginContactInformationDO.mobileNumber + "";
					tvContactphonenumTag.setVisibility(View.VISIBLE);
					etContactphonenum.setTypeface(typefaceOpenSansSemiBold);
				}

				if(!TextUtils.isEmpty(userLoginDo.loginContactInformationDO.emailAddress))
				{
					etContactemail.setText(userLoginDo.loginContactInformationDO.emailAddress+"");
					passengerInfoDO.passengerInfoContactDO.contactemail = userLoginDo.loginContactInformationDO.emailAddress + "";
					tvContactemailTag.setVisibility(View.VISIBLE);
					etContactemail.setTypeface(typefaceOpenSansSemiBold);
				}
				else if(!TextUtils.isEmpty(userLoginDoFromServices.loginContactInformationDO.emailAddress))
				{
					etContactemail.setText(userLoginDoFromServices.loginContactInformationDO.emailAddress+"");
					passengerInfoDO.passengerInfoContactDO.contactemail = userLoginDoFromServices.loginContactInformationDO.emailAddress + "";
					tvContactemailTag.setVisibility(View.VISIBLE);
					etContactemail.setTypeface(typefaceOpenSansSemiBold);
				}
			}
			else if(userLoginDoFromServices != null && passengerInfoDO.vecPassengerInfoPersonDO!=null 
					&& passengerInfoDO.vecPassengerInfoPersonDO.size()>0)
			{

				if(!TextUtils.isEmpty(userLoginDoFromServices.countryOfResidence))
				{
					String countryCodePhone = getCountryISDCode(userLoginDoFromServices.countryOfResidence)+"";
					edContactcity.setText(userLoginDoFromServices.countryOfResidence+"");
					tvContactPhoneISDCode.setText(countryCodePhone+""); 
					tvContactPhoneISDCode.setTypeface(typefaceOpenSansSemiBold);
					tvContactcityTag.setVisibility(View.VISIBLE);
					passengerInfoDO.passengerInfoContactDO.contactcity = userLoginDoFromServices.countryOfResidence + "";
					passengerInfoDO.passengerInfoContactDO.contactphonenumCountryCode = countryCodePhone+"";
					edContactcity.setTypeface(typefaceOpenSansSemiBold);
				}

				if(!TextUtils.isEmpty(userLoginDoFromServices.loginContactInformationDO.mobileNumber))
				{
					countryCodeMobileTemp = getCountryMobileCodeFromMobileWithCountryCode(userLoginDo.loginContactInformationDO.mobileNumber+"");
					etContactphonenum.setText(getMobileNumberFromMobileWithoutCountryCode(userLoginDoFromServices.loginContactInformationDO.mobileNumber+""));
					//					etContactphonenum.setText(userLoginDoFromServices.loginContactInformationDO.mobileNumber+"");
					passengerInfoDO.passengerInfoContactDO.contactphonenum = userLoginDoFromServices.loginContactInformationDO.mobileNumber + "";
					tvContactphonenumTag.setVisibility(View.VISIBLE);
					etContactphonenum.setTypeface(typefaceOpenSansSemiBold);
				}

				if(!TextUtils.isEmpty(userLoginDoFromServices.loginContactInformationDO.emailAddress))
				{
					etContactemail.setText(userLoginDoFromServices.loginContactInformationDO.emailAddress+"");
					passengerInfoDO.passengerInfoContactDO.contactemail = userLoginDoFromServices.loginContactInformationDO.emailAddress + "";
					tvContactemailTag.setVisibility(View.VISIBLE);
					etContactemail.setTypeface(typefaceOpenSansSemiBold);
				}
			}
		}

	}

	private String getCountryISDCode(String CountryOfResidance)
	{
		String strCountryCode = "";
		String strCountryISDcode = "";
		if(AppConstants.vecCountryDO != null && AppConstants.vecCountryISDDO != null)
		{
			for (int i = 0; i < AppConstants.vecCountryDO.size(); i++) 
			{
				if (AppConstants.vecCountryDO.get(i).CountryName.equalsIgnoreCase(CountryOfResidance)) {
					strCountryCode = AppConstants.vecCountryDO.get(i).CountryCode+"";
					break;
				}
			}
			for (int j = 0; j < AppConstants.vecCountryISDDO.size(); j++) {
				if (strCountryCode.equalsIgnoreCase(AppConstants.vecCountryISDDO
						.get(j).CountryCode)) {
					strCountryISDcode = AppConstants.vecCountryISDDO.get(j).ISDCode;
					break;
				}
			}
		}
		return strCountryISDcode;
	}

	private String getCountryCodeByCountryName(String CountryOfResidance)
	{
		String strCountryCode = "";
		if(AppConstants.vecCountryDO != null)
		{
			for (int i = 0; i < AppConstants.vecCountryDO.size(); i++) 
			{
				if (AppConstants.vecCountryDO.get(i).CountryName.equalsIgnoreCase(CountryOfResidance)) {
					strCountryCode = AppConstants.vecCountryDO.get(i).CountryCode+"";
					break;
				}
			}
		}
		return strCountryCode;
	}

	private String getMobileNumberFromMobileWithoutCountryCode(String number)
	{
		String phone = number;
		String[] output = phone.split("-",2);
		return output[1].replace("-", "");
	}

	private String getCountryMobileCodeFromMobileWithCountryCode(String number)
	{
		String phone = number;
		String[] output = phone.split("-",2);
		return output[0];
	}

}