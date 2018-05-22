package com.winit.airarabia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.OfficeDO;
import com.winit.airarabia.objects.OfficeLocationDO;
import com.winit.airarabia.utils.StringUtils;
import com.winit.airarabia.webaccess.Response;

public class OfficeLocationActivityNew extends BaseActivity implements
		DataListener {

	private LinearLayout llOfficelocation, llcity, llcountry;
	private LinearLayout lloffloc, llofflocmain;
	private Vector<OfficeLocationDO> vecOfficeLocationDO;
	private int i;

	private Vector<OfficeDO> vecOfficeDO;
	ArrayList<OfficeLocationDO> vecofficeCountryNewDO;
	private OfficeLocationDO countryDO;
	private TextView tvofficeCity, tvofficeCountry, tvCountryTag, tvCityTag;
	private int COUNTRY_RESULT_CODE = 8000;
	private int CITY_RESULT_CODE = 9000;
	OfficeDO officeDO;

	private OfficeLocationActivityNew.BCR bcr;

	private class BCR extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(AppConstants.HOME_CLICK))
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
		registerReceiver(bcr, intentFilter);
		llOfficelocation = (LinearLayout) layoutInflater.inflate(R.layout.officelocation, null);

		lloffloc = (LinearLayout) llOfficelocation
				.findViewById(R.id.ll_officeloc);
		llofflocmain = (LinearLayout) llOfficelocation
				.findViewById(R.id.ll_officelocinmain);
		llMiddleBase.addView(llOfficelocation, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		tvHeaderTitle.setText(getString(R.string.OfficeLocation));

		// ======================= newly added =====================================================
		tvofficeCountry = (TextView) llOfficelocation.findViewById(R.id.tvofficeCountry);
		tvCountryTag = (TextView) llOfficelocation.findViewById(R.id.tvCountryTag);
		tvofficeCity = (TextView) llOfficelocation.findViewById(R.id.tvofficeCity);
		tvCityTag = (TextView) llOfficelocation.findViewById(R.id.tvCityTag);
		llcity = (LinearLayout) llOfficelocation.findViewById(R.id.llcity);
		llcountry = (LinearLayout) llOfficelocation
				.findViewById(R.id.llcountry);

		vecOfficeDO = new Vector<OfficeDO>();
		vecOfficeLocationDO = new Vector<OfficeLocationDO>();
		vecofficeCountryNewDO = new ArrayList<OfficeLocationDO>();
		
		setTypeFaceOpenSansLight(llOfficelocation);
		
		tvofficeCountry.setTypeface(typefaceOpenSansSemiBold);
		tvCountryTag.setTypeface(typefaceOpenSansSemiBold);
		tvofficeCity.setTypeface(typefaceOpenSansSemiBold);
		tvCityTag.setTypeface(typefaceOpenSansSemiBold);
		btnBack.setTypeface(typefaceOpenSansSemiBold);
		tvHeaderTitle.setTypeface(typefaceOpenSansSemiBold);
	}

	@Override
	public void bindingControl() {
		if (new CommonBL(OfficeLocationActivityNew.this, this)
				.getOfficeLocationData()) {
			showLoader("");
		} else
			hideLoader();

		// ==========================newly
		// added===========================================

		llcountry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				if (vecofficeCountryNewDO != null
						&& vecofficeCountryNewDO.size() > 0) {
					Intent i = new Intent(OfficeLocationActivityNew.this,SelectOfficeLocations.class);
					i.putExtra("vecCountry", vecofficeCountryNewDO);
					if(!tvofficeCountry.getText().toString().equalsIgnoreCase(""))
						i.putExtra("presentlySelectedCountry", tvofficeCountry.getText().toString()+"");
					startActivityForResult(i, COUNTRY_RESULT_CODE);
				}

			}
		});
		llcity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// if (vecCountryNewDO != null && vecCountryNewDO.size()>0) {
				if (countryDO != null) {
					Intent i = new Intent(OfficeLocationActivityNew.this,	SelectOfficeLocationsCity.class);
					i.putExtra("countryDO", countryDO);
					if(!tvofficeCity.getText().toString().equalsIgnoreCase(""))
						i.putExtra("presentlySelectedCity", tvofficeCity.getText().toString()+"");
					startActivityForResult(i, CITY_RESULT_CODE);
				}else{
					//===============================uncomment it=========================================================					
					showCustomDialog(OfficeLocationActivityNew.this, getString(R.string.Alert), getString(R.string.alert_msg_empty_country), "OK", null, "OfficeLocationActivityNew");
					}

			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		hideKeyBoard(llOfficelocation);
	}
	
	@Override
	public void onButtonYesClick(String from) {
		super.onButtonYesClick(from);
		if (from.equalsIgnoreCase(AppConstants.INTERNET_PROBLEM))
			clickHome();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dataRetreived(Response data) {
		if (data != null && !data.isError) {
			switch (data.method) {
			case WS_OFFICELOCATIONS:

				vecOfficeLocationDO.clear();
				vecOfficeLocationDO = (Vector<OfficeLocationDO>) data.data;

				// / ------------------------- Vector's Sorting Done by
				// Rahul-----------------------////

				Collections.sort(vecOfficeLocationDO,
						new OfficeLocationComparator());
				vecofficeCountryNewDO.addAll(vecOfficeLocationDO);
				// /
				// ------------------------------------------------------------------------------////

				break;
			default:
				break;
			}
		} else if (data.data instanceof String) {
			if (((String) data.data)
					.equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
				showCustomDialog(getApplicationContext(),
						getString(R.string.Alert),
						getString(R.string.ConnenectivityTimeOutExpMsg),
						getString(R.string.Ok), "",
						AppConstants.INTERNET_PROBLEM);
			else
				showCustomDialog(getApplicationContext(),
						getString(R.string.Alert),
						getString(R.string.TechProblem),
						getString(R.string.Ok), "",
						AppConstants.INTERNET_PROBLEM);
		}
		hideLoader();

	}

	
	// ========================newly aded for city and
	// country==============================
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == COUNTRY_RESULT_CODE && resultCode == RESULT_OK) {
			if (data != null) {
				if (data.hasExtra("Country_Selected")) {
					countryDO = new OfficeLocationDO();

					countryDO = ((OfficeLocationDO) data
							.getSerializableExtra("Country_Selected"));
					tvofficeCountry.setText(countryDO.country);
					tvofficeCity.setText("All");
					if(countryDO.country.equalsIgnoreCase("sudan"))
					{
						countryDO.vecOfficeDO.get(0).phone = "00249 183 746768 / 00249 183 746769";
					}
					loadCallCenterInfo();
				}
				// isCitySelectEnable=true;
			}

		}
		if (requestCode == CITY_RESULT_CODE && resultCode == RESULT_OK) {
			if (data != null) {
				if (data.hasExtra("Country_Selected")) {
					officeDO = new OfficeDO();

					officeDO = ((OfficeDO) data
							.getSerializableExtra("Country_Selected"));
					tvofficeCity.setText(officeDO.city);
					if(countryDO.country.equalsIgnoreCase("sudan"))
					{
						officeDO.phone = "00249 183 746768 / 00249 183 746769";
					}
					loadCallCenterInfo();
				}
			}
		}

	}

	private void loadCallCenterInfo() {
		// TODO Auto-generated method stub

		// cityselectedpos = posi;
		TextView tvCountryheader, tvCityHeader, tvName, tvCity, tvPhone, tvLocation;

		boolean isArabic = checkLangArabic();

		if (tvofficeCity != null
				&& !tvofficeCity.getText().toString().equalsIgnoreCase("all")) {
			lloffloc.setVisibility(View.GONE);
			llofflocmain.setVisibility(View.VISIBLE);

			tvCountryheader = (TextView) findViewById(R.id.tv_officelocationsinofficexml);
			tvCityHeader = (TextView) findViewById(R.id.tv_cityinofficelocation);
			tvName = (TextView) findViewById(R.id.tv_nameinofficeloc);
			tvCity = (TextView) findViewById(R.id.tv_cityinoffice);
			tvPhone = (TextView) findViewById(R.id.tv_phoneinoffice);
			tvLocation = (TextView) findViewById(R.id.tv_locationinoffice);
			if (isArabic)
				tvCountryheader.setText(getString(R.string.OfficeLocationsIn)
						+ " " + officeDO.city + " << " + countryDO.country);
			else
				tvCountryheader.setText(getString(R.string.OfficeLocationsIn)
						+ " " + countryDO.country + " >> " + officeDO.city);
			tvCityHeader.setText(officeDO.city);
			/*
			 * tvName.setText(getString(R.string.AirArabia)+" - " + vecOfficeDO
			 * .get(cityselectedpos).city);
			 */

			tvName.setText(officeDO.name);

			tvCity.setText(officeDO.city);
			tvPhone.setText(officeDO.phone);

			// /------------------------------------------- Functionality for
			// call on touch --------------------------------------------//
			final String tvCallCenterString = tvPhone.getText().toString()
					.trim();
			tvPhone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String[] parts = tvCallCenterString.split("/");
					List<String> callCenterNumbersList = new ArrayList<String>();
					for (int i = 0; i < parts.length; i++) {
						if (parts[i].contains("Hotline ")) {
							String[] temp = parts[i].split("Hotline ");
							callCenterNumbersList.add(temp[1].trim());
						} else {
							callCenterNumbersList.add(parts[i]);
						}
					}
					showCallOnTouchPopup(callCenterNumbersList);
				}
			});

			// ---------------------------------------------------------------------------------------------------------------------------//

			tvLocation.setText(officeDO.location);

			llofflocmain.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent in = new Intent(OfficeLocationActivityNew.this,
							MapActivity.class);
					in.putExtra(AppConstants.LOCATION, officeDO.city + "+"
							+ countryDO.country);
					in.putExtra(AppConstants.LATITUDE, officeDO.latitude);
					in.putExtra(AppConstants.LONGITUDE, officeDO.longitude);
					startActivity(in);
				}
			});
			// /========================================
		}
		if (tvofficeCity != null
				&& tvofficeCity.getText().toString().equalsIgnoreCase("all")) {
			llofflocmain.setVisibility(View.GONE);
			lloffloc.setVisibility(View.VISIBLE);

			lloffloc.removeAllViews();
			for (i = 0; i < countryDO.vecOfficeDO.size(); i++) {

				LinearLayout lloffcityloc = (LinearLayout) layoutInflater
						.inflate(R.layout.layoutcitylocationcell, null);
				TextView tvCountryheader1, tvCityHeader1, tvName1, tvCity1, tvPhone1, tvLocation1;
				tvCountryheader1 = (TextView) lloffcityloc
						.findViewById(R.id.tv_officelocationsinofficexml1);
				tvCityHeader1 = (TextView) lloffcityloc
						.findViewById(R.id.tv_cityinofficelocation1);
				tvName1 = (TextView) lloffcityloc
						.findViewById(R.id.tv_nameinofficeloc1);
				tvCity1 = (TextView) lloffcityloc
						.findViewById(R.id.tv_cityinoffice1);
				tvPhone1 = (TextView) lloffcityloc
						.findViewById(R.id.tv_phoneinoffice1);
				tvLocation1 = (TextView) lloffcityloc
						.findViewById(R.id.tv_locationinoffice1);
				if (isArabic)
					tvCountryheader1
							.setText(getString(R.string.OfficeLocationsIn)
									+ " " + countryDO.vecOfficeDO.get(i).city
									+ " << " + countryDO.country);
				else
					tvCountryheader1
							.setText(getString(R.string.OfficeLocationsIn)
									+ " " + countryDO.country + " >> "
									+ countryDO.vecOfficeDO.get(i).city);
				tvCityHeader1.setText(countryDO.vecOfficeDO.get(i).city);
				tvCityHeader1.setTypeface(typefaceOpenSansSemiBold);
				/*
				 * tvName1.setText(getString(R.string.AirArabia) + " - " +
				 * vecOfficeDO .get(i).city);
				 */
				tvName1.setText(countryDO.vecOfficeDO.get(i).name);
				tvCity1.setText(countryDO.vecOfficeDO.get(i).city);
				tvPhone1.setText(countryDO.vecOfficeDO.get(i).phone);

				// /------------------------------------------- Functionality
				// for call on touch
				// --------------------------------------------//
				final String tvCallCenterString = tvPhone1.getText().toString()
						.trim();
				tvPhone1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String[] parts = tvCallCenterString.split("/");
						List<String> callCenterNumbersList = new ArrayList<String>();
						for (int i = 0; i < parts.length; i++) {
							if (parts[i].contains("Hotline ")) {
								String[] temp = parts[i].split("Hotline ");
								callCenterNumbersList.add(temp[1].trim());
							} else {
								callCenterNumbersList.add(parts[i]);
							}
						}
						showCallOnTouchPopup(callCenterNumbersList);

					}
				});

				// ---------------------------------------------------------------------------------------------------------------------------//

				tvLocation1.setText(countryDO.vecOfficeDO.get(i).location);

				lloffcityloc.setTag(i + "");
				lloffcityloc.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent in = new Intent(OfficeLocationActivityNew.this,
								MapActivity.class);
						in.putExtra(
								AppConstants.LOCATION,
								countryDO.vecOfficeDO.get(StringUtils.getInt(v
										.getTag().toString())).city
										+ "+"
										+ countryDO.country);
						in.putExtra(AppConstants.LATITUDE,
								countryDO.vecOfficeDO.get(StringUtils.getInt(v
										.getTag().toString())).latitude);
						in.putExtra(AppConstants.LONGITUDE,
								countryDO.vecOfficeDO.get(StringUtils.getInt(v
										.getTag().toString())).longitude);
						// ------------------------
						startActivity(in);
					}
				});
				lloffloc.addView(lloffcityloc);

			}
		}

	}

}