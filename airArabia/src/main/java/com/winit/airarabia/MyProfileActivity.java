package com.winit.airarabia;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import android.app.ActionBar.LayoutParams;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.objects.CountryDO;
import com.winit.airarabia.objects.CountryISDDO;
import com.winit.airarabia.objects.FavouriteDestinationDO;
import com.winit.airarabia.objects.LoginDO;
import com.winit.airarabia.objects.OtherPassengerDo;
import com.winit.airarabia.objects.SavedPassengerDO;
import com.winit.airarabia.utils.SharedPrefUtils;
import com.winit.airarabia.webaccess.Response;

public class MyProfileActivity extends BaseActivity implements DataListener{


	private DatePickerDialog fromDatePickerDialog;
	private SimpleDateFormat dateFormatter;
	private int NATIONALITY_RESULT_CODE=7000;
	private int COUNTRY_RESULT_CODE=8000;
	private CountryDO countryDO,natioanlityDo;
	private LinearLayout llProfile,llDob;
	private TextView tvContactDetails,tvPassengerDetails,tvPreference,tvFavDestination,tvOtherPassengers,tvCountryOfResidence, tvTitle, tvmobilecountrycode, tvTitlePassengers, tvTitle_destinstion;
	private EditText etLastName,etFirstName,etEmail,etMobileNo,etPassPortNo,etAirewards,etNationality,etDob;
	private LoginDO userLoginDo;
	private String isCameFromLogin = "", Email_LoggedInPrevious = "";
	private String[] arrTitleADT;
	private SharedPreferences  mPrefs;
	private LoginDO loginDoFromService;
	private boolean isCameFromChooser = false;
	private OnFocusChangeListener focusChangeListener;
	private OnTouchListener touchListener;
	private OtherPassengerDo otherPassengerDo ;
	private ArrayList<String> selectedDest;
	private Vector<SavedPassengerDO> vecSavedPassengerDo = new Vector<SavedPassengerDO>();
	private SavedPassengerDO savedPassengerDo;

	private final String DATAFAIL = "DATAFAIL";
	
	private String mobileNumberForShowing="";
	
	private boolean isDialogShowing = false;


	@Override
	public void initilize() {
		// TODO Auto-generated method stub

		llProfile = (LinearLayout) layoutInflater.inflate(R.layout.activity_myprofile, null);
		lltop.setVisibility(View.VISIBLE);
		btnBack.setVisibility(View.INVISIBLE);
		tvHeaderTitle.setText(getString(R.string.my_profile));
		llMiddleBase.addView(llProfile, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		etLastName=(EditText)llProfile.findViewById(R.id.etLastName);
		etFirstName=(EditText)llProfile.findViewById(R.id.etFirstName);
		etNationality=(EditText)llProfile.findViewById(R.id.tvNationality);
		tvTitle=(TextView)llProfile.findViewById(R.id.tvTitle);
		etDob=(EditText)llProfile.findViewById(R.id.tvDob);
		etEmail=(EditText)llProfile.findViewById(R.id.etEmail);
		etPassPortNo=(EditText)llProfile.findViewById(R.id.etPassPortNo);
		etAirewards=(EditText)llProfile.findViewById(R.id.etAirwardId);
		tvFavDestination=(TextView)llProfile.findViewById(R.id.tvFev_destinstion);
		tvTitle_destinstion=(TextView)llProfile.findViewById(R.id.tvTitle_destinstion);
		tvTitlePassengers=(TextView)llProfile.findViewById(R.id.tvTitlePassengers);
		etMobileNo=(EditText)llProfile.findViewById(R.id.etMobile);
		tvOtherPassengers=(TextView)llProfile.findViewById(R.id.tvOtherPassengers);
		tvCountryOfResidence=(TextView)llProfile.findViewById(R.id.tvCountryOfResidence);
		tvContactDetails=(TextView)llProfile.findViewById(R.id.tvContactDetail);
		tvPassengerDetails=(TextView)llProfile.findViewById(R.id.tvPassengerDetail);
		tvPreference=(TextView)llProfile.findViewById(R.id.tvPreference);

		//=====================newly added for isdcod3e===========================//
		tvmobilecountrycode=(TextView)llProfile.findViewById(R.id.tvmobilecountrycode);


		llDob		= (LinearLayout) llProfile.findViewById(R.id.llDob);
		if (getIntent().hasExtra("CameFromLogin"))
			isCameFromLogin = getIntent().getStringExtra("CameFromLogin");
		mPrefs = this.getSharedPreferences(SharedPreferenceStrings.APP_PREFERENCES, Context.MODE_PRIVATE);
		Email_LoggedInPrevious = mPrefs.getString(SharedPreferenceStrings.Email_LoggedInPrevious, "");
		loginDoFromService = gettingObjecFromSharedPrerence(SharedPreferenceStrings.loginDo);
//		otherPassengerDo = gettingObjecFromSharedPrerenceOtherPassengerDo(SharedPreferenceStrings.otherPassengerDo);
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(getString(R.string.save_profile));
		if (isCameFromLogin.equalsIgnoreCase("Yes")) {

			llDob.setVisibility(View.VISIBLE);
		}
		else
		{
			llDob.setVisibility(View.VISIBLE);
			ivmenu.setVisibility(View.VISIBLE);
		}
		
		initializeData();

//		callServiceGetNationalitiesData();

		if(AppConstants.vecCountryNationalityDO == null )
		{
			callServiceGetNationalitiesData();
		}
		else if(AppConstants.vecCountryDO == null)
		{
			callServiceGetCountryNamesData(true);
		}
		else if(AppConstants.vecCountryISDDO == null)
		{
			callServiceGetCountryISDCodesData(true);
		}
		
		bindingControl();
		setTypeFaceOpenSansLight(llProfile);
		etFirstName.setOnClickListener(listener);
		etLastName.setOnClickListener(listener);
		etPassPortNo.setOnClickListener(listener);
		etAirewards.setOnClickListener(listener);
		etEmail.setOnClickListener(listener);
		etMobileNo.setOnClickListener(listener);
		tvPassengerDetails.setTypeface(typefaceOpenSansSemiBold);
		tvContactDetails.setTypeface(typefaceOpenSansSemiBold);
		tvPreference.setTypeface(typefaceOpenSansSemiBold);
		touchListener = new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.performClick();
				return false;
			}
		};
		
				
		focusChangeListener	= new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				hideKeyBoard(v);
			}
		};
		
		etNationality.setOnTouchListener(touchListener);
		etNationality.setOnFocusChangeListener(focusChangeListener);
		etDob.setOnTouchListener(touchListener);
		etDob.setOnFocusChangeListener(focusChangeListener);
				
		tvOtherPassengers.setTypeface(typefaceHelveticaLight);
	}

	@Override
	public void bindingControl() {


		arrTitleADT = (resources.getStringArray(R.array.title_adt_desc));
		btnSubmitNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String valid=validateProfile();
				trackEvent("MyProfile Screen",AppConstants.SaveButtonProfile,"");

				/*if(isCameFromLogin.equalsIgnoreCase("Yes") && valid.toString().equalsIgnoreCase("success")){

					userLoginDo=new LoginDO();
					userLoginDo.title=tvTitle.getText().toString();
					userLoginDo.firstName=etFirstName.getText().toString();
					userLoginDo.lastName=etLastName.getText().toString();
					userLoginDo.nationality=tvNationality.getText().toString();
					userLoginDo.dob=tvDob.getText().toString();
					userLoginDo.passportNo=etPassPortNo.getText().toString();				
					userLoginDo.airwardId=etAirewards.getText().toString();
					userLoginDo.countryOfResidence=tvCountryOfResidence.getText().toString();
					userLoginDo.loginContactInformationDO.emailAddress=etEmail.getText().toString();
					userLoginDo.loginContactInformationDO.mobileNumber=etMobileNo.getText().toString();
					userLoginDo.loginContactInformationDO.countryCode=tvmobilecountrycode.getText().toString();
					userLoginDo.loginContactInformationDO.countryName=tvCountryOfResidence.getText().toString();

					ArrayList<String> favDestList=SharedPrefUtils.loadArrayFromPreference(SharedPreferenceStrings.FAVLIST, MyProfileActivity.this);

					for( String item: favDestList){
						FavouriteDestinationDO favDestinationDo=new FavouriteDestinationDO();
						favDestinationDo.countryName=item;
						userLoginDo.vectFavDestinationDo.add(favDestinationDo);
					}


					savingObjecInSharedPrerence(userLoginDo,"LoginDO");

					if(AppConstants.currentUserEmail.equalsIgnoreCase(Email_LoggedInPrevious) && !isCameFromChooser && isCameFromLogin.equalsIgnoreCase("Yes"))
					{
						KeyValue keyValue = new KeyValue(SharedPreferenceStrings.Email_LoggedInPrevious, AppConstants.currentUserEmail);
						SharedPrefUtils.setValue(MyProfileActivity.this, SharedPreferenceStrings.APP_PREFERENCES, keyValue);
					}
					if (isCancelableLoader) {
						Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
						startActivity(intent);
						clickHome();
					}

					if (isCameFromLogin.equalsIgnoreCase("Yes")) {
						Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
						startActivity(intent);
						clickHome();
					}
					finish();



				}
				else*/ 
				if(/*isCameFromLogin.equalsIgnoreCase("Yes") &&*/ valid.toString().equalsIgnoreCase("success"))
				{
					saveProfile("success",true);
//					userLoginDo=new LoginDO();
//					userLoginDo.title=tvTitle.getText().toString().trim();
//					userLoginDo.firstName=etFirstName.getText().toString().trim();
//					userLoginDo.lastName=etLastName.getText().toString().trim();
//					userLoginDo.nationality=tvNationality.getText().toString();
//					userLoginDo.dob=tvDob.getText().toString();
//					userLoginDo.passportNo=etPassPortNo.getText().toString();				
//					userLoginDo.airwardId=etAirewards.getText().toString();
//					userLoginDo.countryOfResidence=tvCountryOfResidence.getText().toString();
//					userLoginDo.loginContactInformationDO.emailAddress=etEmail.getText().toString();
//					userLoginDo.loginContactInformationDO.mobileNumber=tvmobilecountrycode.getText().toString()+"-"+etMobileNo.getText().toString();
//					userLoginDo.loginContactInformationDO.countryCode=getCountryCodeByCountryName(userLoginDo.countryOfResidence);
//
//
//
//					ArrayList<String> favDestList = SharedPrefUtils.loadArrayFromPreference(SharedPreferenceStrings.FAVLIST, MyProfileActivity.this);
//
//					for( String item: favDestList){
//						FavouriteDestinationDO favDestinationDo=new FavouriteDestinationDO();
//						favDestinationDo.countryName=item;
//						userLoginDo.vectFavDestinationDo.add(favDestinationDo);
//					}
//
//
//					savingObjecInSharedPrerence(userLoginDo,"LoginDO");
//
//					if (isCancelableLoader) {
//						Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
//						startActivity(intent);
//						clickHome();
//					}
//
//					if (isCameFromLogin.equalsIgnoreCase("Yes")) {
//						Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
//						startActivity(intent);
//						clickHome();
//					}
//					finish();



				}
				else
					showCustomDialog(getApplicationContext(),
							getString(R.string.Alert),
							valid,
							getString(R.string.Ok), "", "");
			}

		});
		
		

		tvFavDestination.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				trackEvent("MyProfile Screen",AppConstants.FavDestination_Profile,"");
				Intent favIntent =new Intent(MyProfileActivity.this,FavouriteListActivity.class);
				startActivity(favIntent);
			}
		});
		etDob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeyBoard(v);
				trackEvent("MyProfile Screen",AppConstants.DOB_Profile,"");
				getDOB();
//=============logic for getting year before date======================================================				
				
				Calendar cal = Calendar.getInstance();
				Date today = cal.getTime();
				cal.add(Calendar.YEAR, -18); // to get previous year add -1
				Date beforeEighteenYear = cal.getTime();
//				fromDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
				fromDatePickerDialog.setButton(-2, MyProfileActivity.this.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int which) {
				        if (which == DialogInterface.BUTTON_NEGATIVE) {
				        	isDialogShowing = false;
				        	etDob.setFocusable(false);
							etDob.setFocusableInTouchMode(false);
				        }
				     }
				   });
				fromDatePickerDialog.getDatePicker().setMaxDate(beforeEighteenYear.getTime());
				if(!isDialogShowing) {
					fromDatePickerDialog.show();
					isDialogShowing = true;
				}
				else {
					fromDatePickerDialog.dismiss();
				}
				
				fromDatePickerDialog.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						isDialogShowing = false;
					}
				});
			}
		});
		etNationality.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				hideKeyBoard(arg0);
				trackEvent("MyProfile Screen",AppConstants.Nationality_Profile,"");
				if (AppConstants.vecCountryNationalityDO != null && AppConstants.vecCountryNationalityDO.size()>0) {
					Intent i = new Intent(MyProfileActivity.this,SelectCountry.class);
					i.putExtra("vecCountry", AppConstants.vecCountryNationalityDO);
					i.putExtra("SelectedCountry", etNationality.getText().toString());
					i.putExtra("HeaderTitle", getString(R.string.selectNationality));
					AppConstants.userLoginDoForMYProfile = userLoginDo;
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					//i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivityForResult(i, NATIONALITY_RESULT_CODE);
				}


			}
		});
		tvCountryOfResidence.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				trackEvent("MyProfile Screen",AppConstants.Country_Profile,"");
				if (AppConstants.vecCountryDO != null && AppConstants.vecCountryDO.size()>0) {
					Intent i = new Intent(MyProfileActivity.this,SelectCountry.class);
					i.putExtra("vecCountry", AppConstants.vecCountryDO);
					i.putExtra("SelectedCountry", tvCountryOfResidence.getText().toString());
					i.putExtra("HeaderTitle", getString(R.string.selectCountry));
					AppConstants.userLoginDoForMYProfile = userLoginDo;
					startActivityForResult(i, COUNTRY_RESULT_CODE);
				}



			}
		});
		tvTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				trackEvent("MyProfile Screen",AppConstants.Title_Profile,"");
				showSelTextPopup(arrTitleADT, tvTitle, tvTitle, getString(R.string.Title));

			}
		});
		
		tvOtherPassengers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				trackEvent("MyProfile Screen",AppConstants.OtherPassenger_Profile,"");

					Intent i = new Intent(MyProfileActivity.this,SavedPassengerActivity.class);
					/*i.putExtra("vecCountry", vecCountryDO);
					i.putExtra("SelectedCountry", tvCountryOfResidence.getText().toString());
				    i.putExtra("HeaderTitle", getString(R.string.otherPassengers));
					AppConstants.userLoginDoForMYProfile = userLoginDo;*/
				     startActivity(i);
				}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == NATIONALITY_RESULT_CODE && resultCode == RESULT_OK) 
		{
			natioanlityDo=new CountryDO();
			natioanlityDo= (CountryDO)data.getSerializableExtra("Country_Selected");
			userLoginDo = AppConstants.userLoginDoForMYProfile;
			if (userLoginDo != null)
				userLoginDo.nationality = natioanlityDo.CountryName;
			etNationality.setText(natioanlityDo.CountryName);
//			tvNationality.setTypeface(null, Typeface.BOLD);
			isCameFromChooser = true;
		}
		if (requestCode == COUNTRY_RESULT_CODE && resultCode == RESULT_OK) 
		{
			countryDO=new CountryDO();
			countryDO = ((CountryDO)data.getSerializableExtra("Country_Selected"));
			userLoginDo = AppConstants.userLoginDoForMYProfile;
			if (userLoginDo != null)
			{
				userLoginDo.countryOfResidence = countryDO.CountryName;
				userLoginDo.loginContactInformationDO.countryName = countryDO.CountryName;
				userLoginDo.loginContactInformationDO.countryCode = countryDO.CountryCode;
			}
			tvCountryOfResidence.setText(countryDO.CountryName);
			userLoginDo.loginContactInformationDO.countryCode=getCountryCodeByCountryName(userLoginDo.countryOfResidence);
			loginDoFromService.loginContactInformationDO.countryCode=getCountryCodeByCountryName(userLoginDo.countryOfResidence);
			String tempISDcode	=	getCountryISDCode(userLoginDo.countryOfResidence);
			if(!tempISDcode.startsWith("+"))
				tvmobilecountrycode.setText("+"+tempISDcode+"");
			else {
				tvmobilecountrycode.setText(tempISDcode+"");
			}
			isCameFromChooser = true;
		}
	}
	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		String valid=validateProfile();
		if(valid.toString().equalsIgnoreCase("success")){

		if (isUserLoggedIn)
			showCustomDialog(MyProfileActivity.this, getString(R.string.Alert),
					getString(R.string.do_you_want_to_save_profile), getString(R.string.Cancel), getString(R.string.save_profile),
					"MyProfileActivity");
		else
			super.onBackPressed();
		}else{
			showCustomDialog(getApplicationContext(),
					getString(R.string.Alert),
					valid,
					getString(R.string.Ok), "", "");
		}
	}
	
	@Override
	public void onButtonYesClick(String from) {
		// TODO Auto-generated method stub
		super.onButtonYesClick(from);
		if (from.equalsIgnoreCase("MyProfileActivityTwo")) 
			finish();
		
	}
	
	@Override
	public void onButtonNoClick(String from) {
		if (from.equalsIgnoreCase("MyProfileActivity")) 
		{
			String isValidated	=	validateProfile();
			if(isValidated.equalsIgnoreCase("success"))
			{
				saveProfile("success",true);
			}	
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		
		
		
		initializeData();
		if(!AppConstants.currentUserEmail.equalsIgnoreCase(Email_LoggedInPrevious) && !isCameFromChooser && isCameFromLogin.equalsIgnoreCase("Yes"))
		{
			if(!Email_LoggedInPrevious.toString().equalsIgnoreCase("")){
				savingObjecInSharedPrerence(new LoginDO(), "LoginDO");
				SharedPrefUtils.saveArrayInPreference(new ArrayList<String>(),SharedPreferenceStrings.FAVLIST,MyProfileActivity.this);
			}
		}
		if(!isCameFromChooser)
		{
			userLoginDo = (LoginDO) gettingObjecFromSharedPrerence("LoginDO");
			if(userLoginDo==null)
				userLoginDo = new LoginDO();
		}
		else
			userLoginDo = AppConstants.userLoginDoForMYProfile;
		loadUserInformation();
		if(isUserLoggedIn){

			if(userLoginDo!=null){

				FavouriteDestinationDO favDestinationDo;
				if(userLoginDo.vectFavDestinationDo!=null && userLoginDo.vectFavDestinationDo.size()>0){
					favDestinationDo=userLoginDo.vectFavDestinationDo.get(0);
				}
				if(tvTitle.getText().toString().equalsIgnoreCase("") || (!loginDoFromService.title.equalsIgnoreCase(userLoginDo.title)&& !userLoginDo.title.equalsIgnoreCase(""))){
					tvTitle.setText(userLoginDo.title);
					if(!(tvTitle.getText().toString()).equalsIgnoreCase("")){
						tvTitle.setTypeface(typefaceOpenSansSemiBold);
					}else {
						tvTitle.setTypeface(typeFaceOpenSansLight);
					}
				}
				if(etFirstName.getText().toString().equalsIgnoreCase("") || (!loginDoFromService.firstName.equalsIgnoreCase(userLoginDo.firstName)&& !userLoginDo.firstName.equalsIgnoreCase(""))){
					etFirstName.setText(userLoginDo.firstName+"");
					if(!(etFirstName.getText().toString()).equalsIgnoreCase("")){
						etFirstName.setTypeface(typefaceOpenSansSemiBold);
					}else {
						etFirstName.setTypeface(typeFaceOpenSansLight);
					}
				}
				if(etLastName.getText().toString().equalsIgnoreCase("")|| (!loginDoFromService.lastName.equalsIgnoreCase(userLoginDo.lastName)&& !userLoginDo.lastName.equalsIgnoreCase(""))){
					etLastName.setText(userLoginDo.lastName+"");
					if(!(etLastName.getText().toString()).equalsIgnoreCase("")){
						etLastName.setTypeface(typefaceOpenSansSemiBold);
					}else {
						etLastName.setTypeface(typeFaceOpenSansLight);
					}
				}
				if(tvCountryOfResidence.getText().toString().equalsIgnoreCase("") || (!tvCountryOfResidence.getText().toString().equalsIgnoreCase(userLoginDo.countryOfResidence)&& !userLoginDo.countryOfResidence.equalsIgnoreCase(""))){
					tvCountryOfResidence.setText(userLoginDo.countryOfResidence+"");
					if(!(tvCountryOfResidence.getText().toString()).equalsIgnoreCase("")){
						tvCountryOfResidence.setTypeface(typefaceOpenSansSemiBold);
					}else {
						tvCountryOfResidence.setTypeface(typeFaceOpenSansLight);
					}
				}
				if(etAirewards.getText().toString().equalsIgnoreCase("")|| (!loginDoFromService.airwardId.equalsIgnoreCase(userLoginDo.airwardId) && !userLoginDo.airwardId.equalsIgnoreCase(""))){
					etAirewards.setText(userLoginDo.airwardId+"");
					if(!(etAirewards.getText().toString()).equalsIgnoreCase("")){
						etAirewards.setTypeface(typefaceOpenSansSemiBold);
					}else {
						etAirewards.setTypeface(typeFaceOpenSansLight);
					}
				}
				if(etNationality.getText().toString().equalsIgnoreCase("")|| (!loginDoFromService.nationality.equalsIgnoreCase(userLoginDo.nationality)&& !userLoginDo.nationality.equalsIgnoreCase(""))){
					etNationality.setText(userLoginDo.nationality+"");
					if(!(etNationality.getText().toString()).equalsIgnoreCase("")){
						etNationality.setTypeface(typefaceOpenSansSemiBold);
					}else {
						etNationality.setTypeface(typeFaceOpenSansLight);
					}
				}
				if(etEmail.getText().toString().equalsIgnoreCase("")|| (!loginDoFromService.loginContactInformationDO.emailAddress.equalsIgnoreCase(userLoginDo.loginContactInformationDO.emailAddress)&& !userLoginDo.loginContactInformationDO.emailAddress.equalsIgnoreCase(""))){
					etEmail.setText(userLoginDo.loginContactInformationDO.emailAddress+"");
					if(!(etEmail.getText().toString()).equalsIgnoreCase("")){
						etEmail.setTypeface(typefaceOpenSansSemiBold);
					}else {
						etEmail.setTypeface(typeFaceOpenSansLight);
					}
				}
				if(etPassPortNo.getText().toString().equalsIgnoreCase("") || (!loginDoFromService.passportNo.equalsIgnoreCase(userLoginDo.passportNo)&& !userLoginDo.passportNo.equalsIgnoreCase(""))){
					etPassPortNo.setText(userLoginDo.passportNo+"");
					if(!(etPassPortNo.getText().toString()).equalsIgnoreCase("")){
						etPassPortNo.setTypeface(typefaceOpenSansSemiBold);
					}else {
						etPassPortNo.setTypeface(typeFaceOpenSansLight);
					}			
				}
				if(etMobileNo.getText().toString().equalsIgnoreCase("")|| (!loginDoFromService.loginContactInformationDO.mobileNumber.equalsIgnoreCase(userLoginDo.loginContactInformationDO.mobileNumber)&& !userLoginDo.loginContactInformationDO.mobileNumber.equalsIgnoreCase(""))){
					etMobileNo.setText(getMobileNumberFromMobileWithoutCountryCode(userLoginDo.loginContactInformationDO.mobileNumber+""));
					if(!(etMobileNo.getText().toString()).equalsIgnoreCase("")){
						etMobileNo.setTypeface(typefaceOpenSansSemiBold);
					}else {
						etMobileNo.setTypeface(typeFaceOpenSansLight);
					}
				}
				if(tvmobilecountrycode.getText().toString().equalsIgnoreCase("")|| (!loginDoFromService.loginContactInformationDO.countryCode.equalsIgnoreCase(userLoginDo.loginContactInformationDO.countryCode)&& !userLoginDo.loginContactInformationDO.countryCode.equalsIgnoreCase(""))){
					String str = getCountryMobileCodeFromMobileWithCountryCode(userLoginDo.loginContactInformationDO.mobileNumber);
					if(!str.startsWith("+"))
						tvmobilecountrycode.setText("+"+str);
					else
						tvmobilecountrycode.setText(str);
					
					if(!(tvmobilecountrycode.getText().toString()).equalsIgnoreCase("")){
						tvmobilecountrycode.setTypeface(typefaceOpenSansSemiBold);
					}else {
						tvmobilecountrycode.setTypeface(typeFaceOpenSansLight);
					}
				}
				if(etDob.getText().toString().equalsIgnoreCase("")|| (!loginDoFromService.dob.equalsIgnoreCase(userLoginDo.dob)&& !userLoginDo.dob.equalsIgnoreCase(""))){
					etDob.setText(userLoginDo.dob+"");
					if(!(etDob.getText().toString()).equalsIgnoreCase("")){
						etDob.setTypeface(typefaceOpenSansSemiBold);
					}else {
						etDob.setTypeface(typeFaceOpenSansLight);
					}
				}

				if(isCameFromLogin.equalsIgnoreCase("Yes")){

				}
			}
		}
	}

	private void getDOB() {
		Calendar newCalendar = Calendar.getInstance();
		fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				isDialogShowing = false;
				etDob.setFocusable(false);
				etDob.setFocusableInTouchMode(false);
				etDob.setText(dateFormatter.format(newDate.getTime()));
				etDob.setTypeface(typefaceOpenSansSemiBold);
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

	}

	private void callServiceGetNationalitiesData() {
		showLoader("");
		if (new CommonBL(MyProfileActivity.this,
				MyProfileActivity.this).getNationalitiesData()) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	private void callServiceGetCountryNamesData(boolean isLoaderShowing) {
		if(!isLoaderShowing)
			showLoader("");
		if (new CommonBL(MyProfileActivity.this,
				MyProfileActivity.this).getCountryNamesData()) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	@Override
	public void dataRetreived(Response data) {

		if (data != null && !data.isError) {
			switch (data.method) {
			case WS_NATIONALITIES:
				if(data.data instanceof Vector<?>)
				{
					AppConstants.vecCountryNationalityDO = (Vector<CountryDO>) data.data;

					Collections.sort(AppConstants.vecCountryNationalityDO, new Comparator<CountryDO>() {

						@Override
						public int compare(CountryDO compR , CountryDO compL) 
						{
							return compR.CountryName.compareToIgnoreCase(compL.CountryName);
						}
					});

					callServiceGetCountryNamesData(true);
				}
				else if(data.data instanceof String)
				{
					if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
						showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
					else showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
					hideLoader();
				}
				else
				{
					showCustomDialog(MyProfileActivity.this,
							getString(R.string.Alert), getString(R.string.TechProblem),
							getString(R.string.Ok), "", DATAFAIL);
					hideLoader();
				}
				break;

			case WS_COUNTRYNAMES:
				if(data.data instanceof Vector<?>)
				{
					AppConstants.vecCountryDO = (Vector<CountryDO>) data.data;

					Collections.sort(AppConstants.vecCountryDO, new Comparator<CountryDO>() {

						@Override
						public int compare(CountryDO compR , CountryDO compL) 
						{
							return compR.CountryName.compareToIgnoreCase(compL.CountryName);
						}
					});
					if(AppConstants.vecCountryISDDO == null)
						callServiceGetCountryISDCodesData(true);
				}
				else if(data.data instanceof String)
				{
					if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
						showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
					else showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
					hideLoader();
				}
				else
				{
					showCustomDialog(MyProfileActivity.this,
							getString(R.string.Alert), getString(R.string.TechProblem),
							getString(R.string.Ok), "", DATAFAIL);
					hideLoader();
				}
				break;

			case WS_COUNTRYISDCODES:
				if(data.data instanceof Vector<?>)
				{
					AppConstants.vecCountryISDDO = (Vector<CountryISDDO>) data.data;
				}
				else if(data.data instanceof String)
				{
					if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
						showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
					else showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
				}
				else
				{
					showCustomDialog(MyProfileActivity.this,
							getString(R.string.Alert), getString(R.string.TechProblem),
							getString(R.string.Ok), "", DATAFAIL);
				}
				hideLoader();
				//			updateDataFromMyProfile();
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
			else
			{
				showCustomDialog(MyProfileActivity.this,
						getString(R.string.Alert), getString(R.string.TechProblem),
						getString(R.string.Ok), "", DATAFAIL);
			}
			hideLoader();
		}
	}


	private void showSelTextPopup(String[] arrTitle, final TextView tvTitleSel,
			final TextView tvTitleUpdate, String titleOfPopup) {
		LinearLayout ll = (LinearLayout) layoutInflater.inflate(
				R.layout.popup_titles, null);
		TextView tvTitleHeader = (TextView) ll.findViewById(R.id.tvTitleHeader);
		tvTitleHeader.setText(titleOfPopup+"");
		tvTitleHeader.setTypeface(typefaceOpenSansSemiBold);
		final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_No_Title);
		dialog.setContentView(ll);
		LinearLayout llPopupTitleMain = (LinearLayout) ll
				.findViewById(R.id.llPopupTitleMain);

		for (int i = 0; i < arrTitle.length; i++) {
			LinearLayout llTitle = (LinearLayout) layoutInflater.inflate(
					R.layout.popup_title_item, null);

			final TextView tvTitleItem = (TextView) llTitle
					.findViewById(R.id.tvTitleItem);
			llPopupTitleMain.addView(llTitle);
			tvTitleItem.setText(arrTitle[i]);
			tvTitleItem.setTypeface(typefaceOpenSansRegular);
			
			if (tvTitleSel.getText().toString().equalsIgnoreCase(arrTitle[i]))
				tvTitleItem.setBackgroundColor(resources
						.getColor(R.color.ash_color));
			tvTitleItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					tvTitleSel.setText(tvTitleItem.getText().toString());
					tvTitleItem.setBackgroundColor(resources
							.getColor(R.color.ash_color));
					dialog.dismiss();
					tvTitleSel.setTypeface(typefaceOpenSansSemiBold);
					tvTitleUpdate.setText(tvTitleSel.getText()
							.toString());

				}
			});
		}
		dialog.show();
	}
	public String validateProfile(){
		String isEmpty="empty";

		if(!tvTitle.getText().toString().isEmpty()  &&	!tvTitle.getText().toString().equalsIgnoreCase(""))
		{
			if (!etFirstName.getText().toString().isEmpty() && !etFirstName.getText().toString().equalsIgnoreCase("")&&!etFirstName.getText().toString().trim().equalsIgnoreCase("")) {
				if (!etLastName.getText().toString().isEmpty() && !etLastName.getText().toString().equalsIgnoreCase("")&&!etFirstName.getText().toString().trim().equalsIgnoreCase("")) {
					if (!etNationality.getText().toString().isEmpty() && !etNationality.getText().toString().isEmpty()) {

						if (!tvCountryOfResidence.getText().toString().isEmpty() && !tvCountryOfResidence.getText().toString().isEmpty()) {
							if (!etEmail.getText().toString().isEmpty() && !etEmail.getText().toString().equalsIgnoreCase("")) 
							{
								if(emailValidate(etEmail.getText().toString()).matches())
								{
//									if (!etMobileNo.getText().toString().isEmpty() && !etMobileNo.getText().toString().isEmpty()) 
//									{
										if (!etAirewards.getText().toString().isEmpty() && !etAirewards.getText().toString().equalsIgnoreCase("")) 
										{
											if (emailValidate(etAirewards.getText().toString()).matches()) 
											{
												isEmpty="success";
											}
											else
											{
												isEmpty=getString(R.string.invaild_airewards);
											}
										}
										else
										{
											isEmpty="success";
										}
									}
									else
									{
										isEmpty= getString(R.string.InvalidMobile);
									}
//								}
//								else
//								{
//									isEmpty=getString(R.string.invaild_email);
//								}
							}
							else{
								isEmpty=getString(R.string.EmailIdError);
							}
						}
						else
						{
							isEmpty= getString(R.string.invaild_country_of_residence);
						}
					}
					else{
						isEmpty=getString(R.string.NationalityError);
					}
				}else{
					isEmpty="Please enter last name.";
				}
			}else{
				isEmpty="Please enter first name.";
			}

		}else{
			isEmpty="Please enter title.";
		}

		return isEmpty;
	}
	public void loadUserInformation(){
		if(userLoginDo != null){
			if(tvTitle.getText().toString().equalsIgnoreCase("") && userLoginDo.title.equalsIgnoreCase("")){
				tvTitle.setText(loginDoFromService.title);
				if(!(tvTitle.getText().toString()).equalsIgnoreCase("")){
					tvTitle.setTypeface(typefaceOpenSansSemiBold);
				}else {
					tvTitle.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(etFirstName.getText().toString().equalsIgnoreCase("") && userLoginDo.firstName.equalsIgnoreCase("")){
				etFirstName.setText(loginDoFromService.firstName+"");
				if(!(etFirstName.getText().toString()).equalsIgnoreCase("")){
					etFirstName.setTypeface(typefaceOpenSansSemiBold);
				}else {
					etFirstName.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(etLastName.getText().toString().equalsIgnoreCase("") && userLoginDo.lastName.equalsIgnoreCase("")){
				etLastName.setText(loginDoFromService.lastName+"");
				if(!(etLastName.getText().toString()).equalsIgnoreCase("")){
					etLastName.setTypeface(typefaceOpenSansSemiBold);
				}else {
					etLastName.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(tvCountryOfResidence.getText().toString().equalsIgnoreCase("") && userLoginDo.countryOfResidence.equalsIgnoreCase("")){
				tvCountryOfResidence.setText(loginDoFromService.countryOfResidence+"");
				if(!(tvCountryOfResidence.getText().toString()).equalsIgnoreCase("")){
					tvCountryOfResidence.setTypeface(typefaceOpenSansSemiBold);
				}else {
					tvCountryOfResidence.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(etNationality.getText().toString().equalsIgnoreCase("")&& userLoginDo.nationality.equalsIgnoreCase("")){
				etNationality.setText(loginDoFromService.nationality+"");
				if(!(etNationality.getText().toString()).equalsIgnoreCase("")){
					etNationality.setTypeface(typefaceOpenSansSemiBold);
				}else {
					etNationality.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(etEmail.getText().toString().equalsIgnoreCase("")&& userLoginDo.loginContactInformationDO.emailAddress.equalsIgnoreCase("")){
				etEmail.setText(loginDoFromService.loginContactInformationDO.emailAddress+"");
				if(!(etEmail.getText().toString()).equalsIgnoreCase("")){
					etEmail.setTypeface(typefaceOpenSansSemiBold);
				}else {
					etEmail.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(etPassPortNo.getText().toString().equalsIgnoreCase("")&& userLoginDo.passportNo.equalsIgnoreCase("")){
				etPassPortNo.setText(loginDoFromService.passportNo+"");
				if(!(etPassPortNo.getText().toString()).equalsIgnoreCase("")){
					etPassPortNo.setTypeface(typefaceOpenSansSemiBold);
				}else {
					etPassPortNo.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(etMobileNo.getText().toString().equalsIgnoreCase("")&& userLoginDo.loginContactInformationDO.mobileNumber.equalsIgnoreCase("")){
				etMobileNo.setText(getMobileNumberFromMobileWithoutCountryCode(loginDoFromService.loginContactInformationDO.mobileNumber+""));
				if(!(etMobileNo.getText().toString()).equalsIgnoreCase("")){
					etMobileNo.setTypeface(typefaceOpenSansSemiBold);
				}else {
					etMobileNo.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(tvmobilecountrycode.getText().toString().equalsIgnoreCase("")&& userLoginDo.loginContactInformationDO.countryCode.equalsIgnoreCase("")) {
				String str = getCountryMobileCodeFromMobileWithCountryCode(loginDoFromService.loginContactInformationDO.mobileNumber+"");
				if(!str.startsWith("+"))
					tvmobilecountrycode.setText("+"+str);
				else 
					tvmobilecountrycode.setText(str);
				
				if(!(tvmobilecountrycode.getText().toString()).equalsIgnoreCase("")){
					tvmobilecountrycode.setTypeface(typefaceOpenSansSemiBold);
				}else {
					tvmobilecountrycode.setTypeface(typeFaceOpenSansLight);
				}
			}
		}

		else
		{

			if(tvTitle.getText().toString().equalsIgnoreCase("")){
				tvTitle.setText(loginDoFromService.title);
				if(!(tvTitle.getText().toString()).equalsIgnoreCase("")){
					tvTitle.setTypeface(typefaceOpenSansSemiBold);
				}else {
					tvTitle.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(etFirstName.getText().toString().equalsIgnoreCase("")){
				etFirstName.setText(loginDoFromService.firstName+"");
				if(!(etFirstName.getText().toString()).equalsIgnoreCase("")){
					etFirstName.setTypeface(typefaceOpenSansSemiBold);
				}else {
					etFirstName.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(etLastName.getText().toString().equalsIgnoreCase("") ){
				etLastName.setText(loginDoFromService.lastName+"");
				if(!(etLastName.getText().toString()).equalsIgnoreCase("")){
					etLastName.setTypeface(typefaceOpenSansSemiBold);
				}else {
					etLastName.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(tvCountryOfResidence.getText().toString().equalsIgnoreCase("")){
				tvCountryOfResidence.setText(loginDoFromService.countryOfResidence+"");
				if(!(tvCountryOfResidence.getText().toString()).equalsIgnoreCase("")){
					tvCountryOfResidence.setTypeface(typefaceOpenSansSemiBold);
				}else {
					tvCountryOfResidence.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(etNationality.getText().toString().equalsIgnoreCase("")){
				etNationality.setText(loginDoFromService.nationality+"");
				if(!(etNationality.getText().toString()).equalsIgnoreCase("")){
					etNationality.setTypeface(typefaceOpenSansSemiBold);
				}else {
					etNationality.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(etEmail.getText().toString().equalsIgnoreCase("")){
				etEmail.setText(loginDoFromService.loginContactInformationDO.emailAddress+"");
				if(!(etEmail.getText().toString()).equalsIgnoreCase("")){
					etEmail.setTypeface(typefaceOpenSansSemiBold);
				}else {
					etEmail.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(etPassPortNo.getText().toString().equalsIgnoreCase("")){
				etPassPortNo.setText(loginDoFromService.passportNo+"");
				if(!(etPassPortNo.getText().toString()).equalsIgnoreCase("")){
					etPassPortNo.setTypeface(typefaceOpenSansSemiBold);
				}else {
					etPassPortNo.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(etMobileNo.getText().toString().equalsIgnoreCase("")){
				etMobileNo.setText(getMobileNumberFromMobileWithoutCountryCode(loginDoFromService.loginContactInformationDO.mobileNumber+""));
				if(!(etMobileNo.getText().toString()).equalsIgnoreCase("")){
					etMobileNo.setTypeface(typefaceOpenSansSemiBold);
				}else {
					etMobileNo.setTypeface(typeFaceOpenSansLight);
				}
			}
			if(etMobileNo.getText().toString().equalsIgnoreCase("")){
				tvmobilecountrycode.setText("+"+getCountryMobileCodeFromMobileWithCountryCode(loginDoFromService.loginContactInformationDO.mobileNumber+""));
				if(!(tvmobilecountrycode.getText().toString()).equalsIgnoreCase("")){
					tvmobilecountrycode.setTypeface(typefaceOpenSansSemiBold);
				}else {
					tvmobilecountrycode.setTypeface(typeFaceOpenSansLight);
				}
			}

		}

	}
	private String getCountryISDCode(String CountryOfResidance)
	{
		String strCountryCode = "";
		String strCountryISDcode = "";
		for (int i = 0; i < AppConstants.vecCountryDO.size(); i++) 
		{
			if (AppConstants.vecCountryDO.get(i).CountryName.equalsIgnoreCase(CountryOfResidance)) {
				strCountryCode = AppConstants.vecCountryDO.get(i).CountryCode+"";
				break;
			}
		}
		for (int j = 0; j < AppConstants.vecCountryISDDO.size(); j++) {
			if (strCountryCode.equalsIgnoreCase(AppConstants.vecCountryISDDO.get(j).CountryCode)) {
				strCountryISDcode = AppConstants.vecCountryISDDO.get(j).ISDCode;
				break;
			}
		}
		return strCountryISDcode;
	}

	private void callServiceGetCountryISDCodesData(boolean isLoaderShowing) {
		if(!isLoaderShowing)
			showLoader("");
		if (new CommonBL(MyProfileActivity.this, MyProfileActivity.this).getCountryISDCodesData()) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
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
	private void saveProfile(String isValidated,Boolean cameFromMyProfileMain)
	{
		
		if(isValidated.toString().equalsIgnoreCase("success") || isValidated.toString().equalsIgnoreCase("Profile saved successfully.") )
		{
			userLoginDo=new LoginDO();
			userLoginDo.title=tvTitle.getText().toString();
			userLoginDo.firstName=etFirstName.getText().toString();
			userLoginDo.lastName=etLastName.getText().toString();
			userLoginDo.nationality=etNationality.getText().toString();
			userLoginDo.dob=etDob.getText().toString();
			userLoginDo.passportNo=etPassPortNo.getText().toString();				
			userLoginDo.airwardId=etAirewards.getText().toString();
			userLoginDo.countryOfResidence=tvCountryOfResidence.getText().toString();
			userLoginDo.loginContactInformationDO.emailAddress=etEmail.getText().toString();
			userLoginDo.loginContactInformationDO.mobileNumber=tvmobilecountrycode.getText().toString()+"-"+etMobileNo.getText().toString();
			userLoginDo.loginContactInformationDO.countryCode=getCountryCodeByCountryName(userLoginDo.countryOfResidence);



			ArrayList<String> favDestList=SharedPrefUtils.loadArrayFromPreference(SharedPreferenceStrings.FAVLIST, MyProfileActivity.this);

			for( String item: favDestList){
				FavouriteDestinationDO favDestinationDo=new FavouriteDestinationDO();
				favDestinationDo.countryName=item;
				userLoginDo.vectFavDestinationDo.add(favDestinationDo);
			}
			savingObjecInSharedPrerence(userLoginDo,"LoginDO");
//			finish();
			
			//
			if(cameFromMyProfileMain)
			{
				if (isCancelableLoader) {
					Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
					startActivity(intent);
					clickHome();
				}

				if (isCameFromLogin.equalsIgnoreCase("Yes")) {
					Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
					startActivity(intent);
					clickHome();
				}
				finish();
			}
			else
				finish();
		}
		else
			showCustomDialog(getApplicationContext(),getString(R.string.Alert),getString(R.string.ErrorWhileProcessing),getString(R.string.Ok), "", "MyProfileActivityTwo");
	}
	private String getMobileNumberFromMobileWithoutCountryCode(String number)
	{
		if (number != null && !number.equalsIgnoreCase("")) {
			String phone = number;
			String[] output = phone.split("-",2);
			return output[1].replace("-", "");
		}
		else
			return "";
		
	}
	
	private String getCountryMobileCodeFromMobileWithCountryCode(String number)
	{if (number != null && !number.equalsIgnoreCase("")) {
		String phone = number;
		String[] output = phone.split("-",2);
		return output[0];
	}
	else
		return "";
		
	}
	
	OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(final View v) {
			((EditText) v).addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					if(!(((EditText) v).getText().toString()).equalsIgnoreCase("")){
						((EditText) v).setTypeface(typefaceOpenSansSemiBold);
					}else {
						((EditText) v).setTypeface(typeFaceOpenSansLight);
					}
				}
			});
		}
	};
	
	private void initializeData()
	{
		otherPassengerDo = gettingObjecFromSharedPrerenceOtherPassengerDo(SharedPreferenceStrings.otherPassengerDo);
		if(otherPassengerDo != null)
		{
			vecSavedPassengerDo.clear();
			for (int i = 0; i < otherPassengerDo.vecSavedPassengerDoAdult.size(); i++) {
				savedPassengerDo = new SavedPassengerDO();
				savedPassengerDo = (SavedPassengerDO) otherPassengerDo.vecSavedPassengerDoAdult.get(i);
				vecSavedPassengerDo.add(savedPassengerDo);
			}

			for (int i = 0; i < otherPassengerDo.vecSavedPassengerDoChild.size(); i++) {
				savedPassengerDo = new SavedPassengerDO();
				savedPassengerDo = (SavedPassengerDO) otherPassengerDo.vecSavedPassengerDoChild.get(i);
				vecSavedPassengerDo.add(savedPassengerDo);
			}

			for (int i = 0; i < otherPassengerDo.vecSavedPassengerDoInfant.size(); i++) {
				savedPassengerDo = new SavedPassengerDO();
				savedPassengerDo = (SavedPassengerDO) otherPassengerDo.vecSavedPassengerDoInfant.get(i);
				vecSavedPassengerDo.add(savedPassengerDo);
			}
			String tempPass = "";
			
			for (int j = 0; j < vecSavedPassengerDo.size(); j++) {
				if(j==0)
					tempPass = tempPass+""+vecSavedPassengerDo.get(j).firstName.toString();
				else
					tempPass = tempPass+", "+vecSavedPassengerDo.get(j).firstName.toString();
			}
			tvOtherPassengers.setText(tempPass+"");
			tvOtherPassengers.setTypeface(typefaceOpenSansSemiBold);
			if(tempPass.equals("")) {
				tvOtherPassengers.setTypeface(typeFaceOpenSansLight);
				tvTitlePassengers.setVisibility(View.GONE);
			}
			else
				tvTitlePassengers.setVisibility(View.VISIBLE);
		}

		selectedDest = SharedPrefUtils.loadArrayFromPreference(SharedPreferenceStrings.FAVLIST,MyProfileActivity.this);
		if(selectedDest!=null)
		{
			String tempDest = "";
			for(int i=0 ; i<selectedDest.size() ; i++)
			{
				if(i==0)
					tempDest = tempDest+""+selectedDest.get(i);
				else
					tempDest = tempDest+", "+selectedDest.get(i);
			}
			tvFavDestination.setText(tempDest);
			tvFavDestination.setTypeface(typefaceOpenSansSemiBold);
			if(tempDest.equals("")) {
				tvFavDestination.setTypeface(typeFaceOpenSansLight);
				tvTitle_destinstion.setVisibility(View.GONE);
			}
			else
			{
				tvTitle_destinstion.setVisibility(View.VISIBLE);
			}
		}

	}

}
