package com.winit.airarabia;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.objects.CountryDO;
import com.winit.airarabia.objects.OtherPassengerDo;
import com.winit.airarabia.objects.SavedPassengerDO;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.utils.StringUtils;
import com.winit.airarabia.webaccess.Response;

public class UpdateNewPassenger extends BaseActivity implements DataListener {

	private LinearLayout llAddNewPassenger, llDob;
	private TextView tvPassengerFetchContacts, tvTitle, tvCancel, tvSelectHeader, tvDone;
	private int PICK_CONTACT = 1000, COUNTRY_RESULT_CODE = 1001;
	private EditText etFirstName, etLastName, etAirwardId, etPassPortNo, tvNationality,tvDob;
	private Vector<CountryDO> vecCountryNationalityDO;
	private String[] arrTitleADT, arrTitleCHD, arrTitleINF,arrTitleADTDesc, arrTitleCHDDesc, arrTitleINFDesc;
	private OtherPassengerDo otherPassengerDo;
	private SavedPassengerDO savedPassengerDo;
	private SavedPassengerDO tempSavedPassengerDo;
	private String passengerType ="";
	private OnFocusChangeListener  focusChangeListener;
	private OnTouchListener touchListener;

	private final int REQUEST_CAL_DOB_CHILD = 100,REQUEST_CAL_DOB_INF = 101;

	@Override
	public void initilize() {
		llAddNewPassenger = (LinearLayout) layoutInflater.inflate(R.layout.activity_add_new_passenger, null);
		llMiddleBase.addView(llAddNewPassenger, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		lltop.setVisibility(View.GONE);

		tvPassengerFetchContacts = (TextView) llAddNewPassenger.findViewById(R.id.tvPassengerFetchContacts);
		etFirstName = (EditText) llAddNewPassenger.findViewById(R.id.etFirstName);
		etLastName = (EditText) llAddNewPassenger.findViewById(R.id.etLastName);
		etAirwardId = (EditText) llAddNewPassenger.findViewById(R.id.etAirwardId);
		etPassPortNo = (EditText) llAddNewPassenger.findViewById(R.id.etPassPortNo);
		tvNationality = (EditText) llAddNewPassenger.findViewById(R.id.etNationality);
		tvDob = (EditText) llAddNewPassenger.findViewById(R.id.etDob);
		tvTitle = (TextView) llAddNewPassenger.findViewById(R.id.tvTitle);
		tvCancel = (TextView) llAddNewPassenger.findViewById(R.id.tvCancel);
		tvSelectHeader = (TextView) llAddNewPassenger.findViewById(R.id.tvSelectHeader);
		tvDone = (TextView) llAddNewPassenger.findViewById(R.id.tvDone);
		llDob = (LinearLayout) llAddNewPassenger.findViewById(R.id.llDob);

		tvCancel.setTypeface(typefaceOpenSansSemiBold);
		tvDone.setTypeface(typefaceOpenSansSemiBold);
		tvSelectHeader.setTypeface(typefaceOpenSansSemiBold);

		if (getIntent().hasExtra("PassengerType")) {
			passengerType = getIntent().getStringExtra("PassengerType");
		}
		if (getIntent().hasExtra("PassengerObject")) {
			
			
			savedPassengerDo = new SavedPassengerDO();
			savedPassengerDo = (SavedPassengerDO) getIntent().getSerializableExtra("PassengerObject");
			
	/////==================Cloning of object going on here =============================///		
			tempSavedPassengerDo = new SavedPassengerDO();
			tempSavedPassengerDo.title = savedPassengerDo.title;
			tempSavedPassengerDo.firstName = savedPassengerDo.firstName;
			tempSavedPassengerDo.lastName = savedPassengerDo.lastName;
			tempSavedPassengerDo.nationality = savedPassengerDo.nationality;
			tempSavedPassengerDo.dob = savedPassengerDo.dob;
			tempSavedPassengerDo.passportNo = savedPassengerDo.passportNo;
			tempSavedPassengerDo.airwardId = savedPassengerDo.airwardId;
//			=============================================================================/////
		}

		if (passengerType.equalsIgnoreCase("Adult")) {
			llDob.setVisibility(View.GONE);
		}
		
		

		showLoader("");
		otherPassengerDo = gettingObjecFromSharedPrerenceOtherPassengerDo(SharedPreferenceStrings.otherPassengerDo);
		if (otherPassengerDo == null)
			otherPassengerDo = new OtherPassengerDo();
		if(savedPassengerDo == null)
		savedPassengerDo = new SavedPassengerDO();
		hideLoader();
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

		tvPassengerFetchContacts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_CONTACT);
			}
		});

		tvNationality.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeyBoard(v);
				if (vecCountryNationalityDO != null && vecCountryNationalityDO.size()>0) {
					//					int pos = StringUtils.getInt(v.getTag().toString());
					Intent i = new Intent(UpdateNewPassenger.this,SelectCountry.class);
					//					i.putExtra("pos", pos);
					i.putExtra("vecCountry", vecCountryNationalityDO);
					i.putExtra("HeaderTitle", getString(R.string.selectNationality));
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					//i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivityForResult(i, COUNTRY_RESULT_CODE);
				}
			}
		});

		tvTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(passengerType.toString().equalsIgnoreCase("Adult"))
				{
					showSelTextPopup(arrTitleADT,arrTitleADTDesc, tvTitle,
						savedPassengerDo,"persontitle", getString(R.string.Title));
				}
				else if(passengerType.toString().equalsIgnoreCase("Child"))
				{
					showSelTextPopup(arrTitleCHD,arrTitleCHDDesc, tvTitle,
						savedPassengerDo,"persontitle", getString(R.string.Title));
				}
				else if(passengerType.toString().equalsIgnoreCase("Infant"))
				{
					showSelTextPopup(arrTitleINF,arrTitleINFDesc, tvTitle,
						savedPassengerDo,"persontitle", getString(R.string.Title));
				}
			}
		});

		tvDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(validatePassengerInfo())
				{

					showLoader("");

					if (passengerType.equalsIgnoreCase("Adult")) {
						for (int tempK = 0; tempK < otherPassengerDo.vecSavedPassengerDoAdult.size(); tempK++) {
							if(otherPassengerDo.vecSavedPassengerDoAdult.get(tempK).firstName.equals(tempSavedPassengerDo.firstName)
									&& otherPassengerDo.vecSavedPassengerDoAdult.get(tempK).lastName.equals(tempSavedPassengerDo.lastName)
											&& otherPassengerDo.vecSavedPassengerDoAdult.get(tempK).dob.equals(tempSavedPassengerDo.dob))
							{
								otherPassengerDo.vecSavedPassengerDoAdult.remove(tempK);
								otherPassengerDo.vecSavedPassengerDoAdult.add(savedPassengerDo);
							}
						}
					}
					else if (passengerType.equalsIgnoreCase("Child")) {
						
						for (int tempK = 0; tempK < otherPassengerDo.vecSavedPassengerDoChild.size(); tempK++) {
							if(otherPassengerDo.vecSavedPassengerDoChild.get(tempK).firstName.equals(tempSavedPassengerDo.firstName)
									&& otherPassengerDo.vecSavedPassengerDoChild.get(tempK).lastName.equals(tempSavedPassengerDo.lastName)
											&& otherPassengerDo.vecSavedPassengerDoChild.get(tempK).dob.equals(tempSavedPassengerDo.dob))
							{
								otherPassengerDo.vecSavedPassengerDoChild.remove(tempK);
								otherPassengerDo.vecSavedPassengerDoChild.add(savedPassengerDo);
							}
						}
					}
					else if (passengerType.equalsIgnoreCase("Infant")) {
						
						for (int tempK = 0; tempK < otherPassengerDo.vecSavedPassengerDoInfant.size(); tempK++) {
							if(otherPassengerDo.vecSavedPassengerDoInfant.get(tempK).firstName.equals(tempSavedPassengerDo.firstName)
									&& otherPassengerDo.vecSavedPassengerDoInfant.get(tempK).lastName.equals(tempSavedPassengerDo.lastName)
											&& otherPassengerDo.vecSavedPassengerDoInfant.get(tempK).dob.equals(tempSavedPassengerDo.dob))
							{
								otherPassengerDo.vecSavedPassengerDoInfant.remove(tempK);
								otherPassengerDo.vecSavedPassengerDoInfant.add(savedPassengerDo);
							}
						}
					}

					savingObjecInSharedPrerence(otherPassengerDo, SharedPreferenceStrings.otherPassengerDo);
					hideLoader();
					showCustomDialog(getApplicationContext(), "", getString(R.string.passenger_details_saved), getString(R.string.Ok), null, getString(R.string.otherPassengers));
				}
			}
		});


		etFirstName.addTextChangedListener(new TextWatcher() {

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

				savedPassengerDo.firstName 	= etFirstName.getText().toString();
				if(!(etFirstName.getText().toString()).equalsIgnoreCase("")){

					etFirstName.setTypeface(typefaceOpenSansSemiBold);
				}else {
					etFirstName.setTypeface(typefaceOpenSansRegular);
				}
			}
		});
		etLastName.addTextChangedListener(new TextWatcher() {

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

				savedPassengerDo.lastName 	= etLastName.getText().toString();
				if(!(etLastName.getText().toString()).equalsIgnoreCase("")){

					etLastName.setTypeface(typefaceOpenSansSemiBold);
				}else {
					etLastName.setTypeface(typefaceOpenSansRegular);
				}
			}
		});

		etAirwardId.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				savedPassengerDo.airwardId 	= etAirwardId.getText().toString();
			}
		});
		etPassPortNo.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				savedPassengerDo.passportNo 	= etPassPortNo.getText().toString();
			}
		});

		tvDob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeyBoard(v);
				Calendar curCal = Calendar.getInstance();
				String mDate = savedPassengerDo.dob.toString();
				if(!TextUtils.isEmpty(mDate))
				{
					if(mDate.contains("T"))
						mDate = mDate.split("T")[0];

					int date = StringUtils.getInt(mDate.split("-")[2]);
					int month = StringUtils.getInt(mDate.split("-")[1]);
					int year = StringUtils.getInt(mDate.split("-")[0]);

					month -= 1;

					curCal.set(Calendar.DATE, date);
					curCal.set(Calendar.MONTH, month);
					curCal.set(Calendar.YEAR, year);
				}

				if(passengerType.equalsIgnoreCase("Child"))
				{
					Intent in = new Intent(UpdateNewPassenger.this,SelectDateDobActivityNew.class);
					in.putExtra(AppConstants.SEL_DATE, curCal);
					in.putExtra(AppConstants.SEL_DATE_CHILD, AppConstants.SEL_DATE_CHILD);
					//in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivityForResult(in, REQUEST_CAL_DOB_CHILD);
				}
				else if(passengerType.equalsIgnoreCase("Infant"))
				{
					Intent in = new Intent(UpdateNewPassenger.this,SelectDateDobActivityNew.class);
					in.putExtra(AppConstants.SEL_DATE, curCal);
					//in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivityForResult(in, REQUEST_CAL_DOB_INF);
				}

			}
		});

		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		touchListener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.performClick();
				return false;
			}
		};


		focusChangeListener = new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				hideKeyBoard(v);
			}
		};
		tvNationality.setOnFocusChangeListener(focusChangeListener);
		tvDob.setOnFocusChangeListener(focusChangeListener);
		
		tvNationality.setOnTouchListener(touchListener);
		tvDob.setOnTouchListener(touchListener);
		
		
		updateView();

	}

	private void updateView() {
		if(!TextUtils.isEmpty(savedPassengerDo.title.toString()))
		{
			tvTitle.setText(savedPassengerDo.title.toString());
		}
		if(!TextUtils.isEmpty(savedPassengerDo.firstName.toString()))
		{
			etFirstName.setText(savedPassengerDo.firstName.toString());
		}
		if(!TextUtils.isEmpty(savedPassengerDo.lastName.toString()))
		{
			etLastName.setText(savedPassengerDo.lastName.toString());
		}
		if(!TextUtils.isEmpty(savedPassengerDo.nationality.toString()))
		{
			tvNationality.setText(savedPassengerDo.nationality.toString());
		}
		if(!TextUtils.isEmpty(savedPassengerDo.passportNo.toString()))
		{
			etPassPortNo.setText(savedPassengerDo.passportNo.toString());
		}
		if(!TextUtils.isEmpty(savedPassengerDo.airwardId.toString()))
		{
			etAirwardId.setText(savedPassengerDo.airwardId.toString());
		}
		if(!TextUtils.isEmpty(savedPassengerDo.dob.toString()))
		{
			
			String [] tempDate = savedPassengerDo.dob.split("T");
			String[] dates = tempDate[0].split("-");
			int mYear = StringUtils.getInt(dates[0]);
			int mMonth = StringUtils.getInt(dates[1]);
			int mDate = StringUtils.getInt(dates[2]);
			
			Calendar mcal = Calendar.getInstance();
			mcal.set(mYear, mMonth-1, mDate);
			
			tvDob.setText("" + mcal.get(Calendar.DAY_OF_MONTH) + " "
					+ CalendarUtility.getDobMonth(mcal.get(Calendar.MONTH))
					+ " " + mcal.get(Calendar.YEAR));
			
		}
		
		
	}

	@Override public void onActivityResult(int reqCode, int resultCode, Intent data){ super.onActivityResult(reqCode, resultCode, data);


	//	if (reqCode == 1000 && resultCode == Activity.RESULT_OK)
	//		{
	//			Uri contactData = data.getData();
	//			String birthday ="";
	//			
	//			Cursor c = managedQuery(contactData, null, null, null, null);
	//			ContentResolver cr = getContentResolver();
	//			if (c.moveToFirst())
	//			{
	//				String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
	//
	//
	//				String hasPhone =
	//						c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
	//
	//				if (hasPhone.equalsIgnoreCase("1")) 
	//				{
	//					Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
	//							ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
	//					phones.moveToFirst();
	//					String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME));
	//					//            String email = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
	//					String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	//					//      
	//					
	//					
	//					 String columns[] = {
	//							 ContactsContract.CommonDataKinds.Event.START_DATE,
	//							 ContactsContract.CommonDataKinds.Event.TYPE,
	//							 ContactsContract.CommonDataKinds.Event.MIMETYPE,
	//					 };
	//
	//					 String where = Event.TYPE + "=" + Event.TYPE_BIRTHDAY + 
	//							 " and " + Event.MIMETYPE + " = '" + Event.CONTENT_ITEM_TYPE + "' and " + ContactsContract.Data.CONTACT_ID + " = " + id;
	//
	//					 String[] selectionArgs = null;
	//					 String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;
	//					
	//					Cursor birthdayCur = cr.query(ContactsContract.Data.CONTENT_URI, columns, where, selectionArgs, sortOrder); 
	//				    if (birthdayCur.getCount() > 0) {
	//				        while (birthdayCur.moveToNext()) {
	//				             birthday = birthdayCur.getString(birthdayCur.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
	//				        }
	//				    }
	//				    birthdayCur.close();
	//						
	//						
	//					String arrName[] = name.split(" ");
	//
	//					int length = arrName.length;
	//
	//					String firstName = "";
	//					String lastName ="";
	//					for (int i = 0; i < arrName.length-1; i++) {
	//						firstName = firstName+" "+ arrName[i];
	//					}
	//
	//					etFirstName.setText(firstName.trim()+"");
	//					etLastName.setText(arrName[length-1].trim());
	//					savedPassengerDo.firstName = firstName.trim()+"";
	//					savedPassengerDo.lastName = arrName[length-1].trim();
	//
	//				}
	//			}
	//		}
	if (reqCode == 1000 && resultCode == Activity.RESULT_OK)
	{
		Uri contactData = data.getData();
		Cursor c = managedQuery(contactData, null, null, null, null);
		if (c.moveToFirst())
		{
			String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));


			String hasPhone =
					c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

			if (hasPhone.equalsIgnoreCase("1")) 
			{
				Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
				phones.moveToFirst();
				String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME));
				//String email = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
				String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				//            
				String arrName[] = name.split(" ");

				int length = arrName.length;

				String firstName = "";
				String lastName ="";
				if(arrName.length > 1)
				{
					for (int i = 0; i < arrName.length-1; i++) {
						firstName = firstName+" "+ arrName[i];
					}

					etFirstName.setText(firstName.trim()+"");
					etLastName.setText(arrName[length-1].trim());
					savedPassengerDo.firstName = firstName.trim()+"";
					savedPassengerDo.lastName = arrName[length-1].trim();
				}
				else if(arrName.length == 1)
				{
					etFirstName.setText(arrName[length-1].trim()+"");
					etLastName.setText("");
					savedPassengerDo.firstName = arrName[length-1].trim();
					savedPassengerDo.lastName = "";
				}

			}
		}
	}
	else if(reqCode == COUNTRY_RESULT_CODE && resultCode == Activity.RESULT_OK)
	{
		CountryDO countryDO;
		int tempPosFromPassengerInformation = -1;
		if (data.hasExtra("pos")) {
			tempPosFromPassengerInformation = data.getExtras().getInt("pos");
		}
		countryDO=new CountryDO();
		countryDO = ((CountryDO)data.getSerializableExtra("Country_Selected"));
		if (countryDO != null && !countryDO.CountryName.equalsIgnoreCase(""))
		{
			tvNationality.setText(countryDO.CountryName);
			savedPassengerDo.nationality = countryDO.CountryName;
		}
	}
	else if (reqCode == REQUEST_CAL_DOB_CHILD && resultCode == RESULT_OK) {
		Calendar mcal = (Calendar) data.getExtras().getSerializable(
				AppConstants.SEL_DATE);
		tvDob.setText("" + mcal.get(Calendar.DAY_OF_MONTH) + " "
				+ CalendarUtility.getDobMonth(mcal.get(Calendar.MONTH))
				+ " " + mcal.get(Calendar.YEAR));
		savedPassengerDo.dob = CalendarUtility.getBookingDate(mcal);
		if(!(tvDob.getText().toString().equalsIgnoreCase(""))){
			tvDob.setTypeface(typefaceOpenSansSemiBold);
		}
	}
	else if (reqCode == REQUEST_CAL_DOB_INF && resultCode == RESULT_OK) {
		Calendar mcal = (Calendar) data.getExtras().getSerializable(
				AppConstants.SEL_DATE);
		tvDob.setText("" + mcal.get(Calendar.DAY_OF_MONTH) + " "
				+ CalendarUtility.getDobMonth(mcal.get(Calendar.MONTH))
				+ " " + mcal.get(Calendar.YEAR));
		savedPassengerDo.dob = CalendarUtility.getBookingDate(mcal);
		if(!(tvDob.getText().toString().equalsIgnoreCase(""))){
			tvDob.setTypeface(typefaceOpenSansSemiBold);
		}
	}
	}

	private void callServiceGetNationalitiesData() {
		showLoader("");
		if (new CommonBL(UpdateNewPassenger.this,
				UpdateNewPassenger.this).getNationalitiesData()) {
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
				vecCountryNationalityDO = (Vector<CountryDO>) data.data;

				Collections.sort(vecCountryNationalityDO, new Comparator<CountryDO>() {

					@Override
					public int compare(CountryDO compR , CountryDO compL) 
					{
						return compR.CountryName.compareToIgnoreCase(compL.CountryName);
					}
				});
				hideLoader();
				break;

			default:
				break;


			}
		} else
		{
			hideLoader();
			if(data.data instanceof String)
			{
				if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
					showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
				else showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
			}
		}
	}


	private void showSelTextPopup(String[] arrTitle, String[] arrTitleDesc, final TextView tvTitleSel,
			final SavedPassengerDO obj,
			final String objValue, String titleOfPopup) {
		LinearLayout ll = (LinearLayout) layoutInflater.inflate(R.layout.popup_titles, null);
		TextView tvTitleHeader = (TextView) ll.findViewById(R.id.tvTitleHeader);
		tvTitleHeader.setTypeface(typefaceOpenSansSemiBold);
		tvTitleHeader.setText(titleOfPopup+"");
		final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_No_Title);
		dialog.setContentView(ll);
		setTypefaceOpenSansRegular(ll);
		tvTitleHeader.setTypeface(typefaceOpenSansSemiBold);
		LinearLayout llPopupTitleMain = (LinearLayout) ll.findViewById(R.id.llPopupTitleMain);
		for (int i = 0; i < arrTitle.length; i++) {
			LinearLayout llTitle = (LinearLayout) layoutInflater.inflate(
					R.layout.popup_title_item, null);

			final TextView tvTitleItem = (TextView) llTitle.findViewById(R.id.tvTitleItem);
			//			tvTitleItem.setTypeface(typeFaceOpenSansLight); Mrs
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

					if(!(tvTitleItem.getText().toString()).equalsIgnoreCase("")){

						tvTitle.setVisibility(View.VISIBLE);
						tvTitleSel.setTypeface(typefaceOpenSansSemiBold);
					}

					tvTitleItem.setBackgroundColor(resources.getColor(R.color.ash_color));

					dialog.dismiss();

					savedPassengerDo = (SavedPassengerDO) obj;
					savedPassengerDo.title = tvTitleSel.getText().toString();
				}
			});
		}
		if(tvTitleSel != null && !tvTitleSel.getText().toString().isEmpty())
			savedPassengerDo.title = tvTitleSel.getText().toString();
		dialog.show();
	}

	@Override
	public void onButtonNoClick(String from) {
		//		super.onButtonNoClick(from);
		if (from.equalsIgnoreCase(getString(R.string.otherPassengers))) {
			finish();
		}
	}
	@Override
	public void onButtonYesClick(String from) {
		//		super.onButtonYesClick(from);

		if (from.equalsIgnoreCase(getString(R.string.otherPassengers))) {
			finish();
		}
	}

	private boolean validatePassengerInfo() {

		boolean isVaild = false, isValidFirstName = false, isValidLastName = false, isValidDob = true, 
				isValidNationality = false, isVaildTitle = false, isValidAireward = false;

		if(!isVaild)
		{
			if (savedPassengerDo.title.length() >= 1) {
				isVaildTitle = true;
			} else {
				isVaildTitle = false;
			}

			if (savedPassengerDo.firstName.length() >= 1) {
				isValidFirstName = true;
			} else {
				isValidFirstName = false;
			}

			if (savedPassengerDo.lastName.length() >= 1) {
				isValidLastName = true;
			} 
			else {
				isValidLastName = false;
			}

			if (savedPassengerDo.nationality.length() >= 1) {
				isValidNationality = true;
			} 
			else {
				isValidNationality = false;
			}

			if (savedPassengerDo.airwardId.length() >= 1) {
				if(emailValidate(savedPassengerDo.airwardId.toString().trim()).matches())
				{
					isValidAireward = true;
				}
				else
					isValidAireward = false;
			} 
			else {
				isValidAireward = true;
			}

			isVaild = true;
		}

		if (!isVaildTitle) {
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.TitleError), getString(R.string.Ok),
					"", "");
			return false;
		} else if (!isValidFirstName) {
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.FirstNameError), getString(R.string.Ok),
					"", "");
			return false;
		} else if (!isValidLastName) {
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.LastNameError), getString(R.string.Ok),
					"", "");
			return false;
		}
		else if (!isValidNationality) {
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.NationalityError), getString(R.string.Ok), "",
					"");
			return false;
		}
		else if (!isValidAireward) {
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.airewardsIdError), getString(R.string.Ok), "",
					"");
			return false;
		}
		else
			return true;
	}
}
