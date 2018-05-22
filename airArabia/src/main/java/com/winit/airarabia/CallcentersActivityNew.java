package com.winit.airarabia;

import java.util.ArrayList;
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
import com.winit.airarabia.objects.CityDO;
import com.winit.airarabia.objects.CountriesDO;
import com.winit.airarabia.webaccess.Response;

public class CallcentersActivityNew extends BaseActivity implements DataListener {

	private LinearLayout llCallCenters, llcitycallcenters,
	llcitycallcentersinmain;
	private Vector<CountriesDO> vecCountryDO;
	private TextView tv_contactsCountry, tv_contactsCity;
	private CallcentersActivityNew.BCR bcr;

	//======================newly added for country================================
	private int COUNTRY_RESULT_CODE=8000;
	private int CITY_RESULT_CODE=9000;
	private CountriesDO countryDO;
	private CityDO  cityDO;
	private Boolean isCountrySelectEnable=false;
	private Boolean isCitySelectEnable=false;
	private ArrayList<CountriesDO> vecCountryNewDO;

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
		tvHeaderTitle.setText(getString(R.string.CallCenters));
		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		registerReceiver(bcr, intentFilter);
		llCallCenters = (LinearLayout) layoutInflater.inflate(
				R.layout.callcenters, null);

		setTypefaceOpenSansRegular(llCallCenters);

		//=======================newly added for select country and city for intentto replace spinner===================================

		///===============Need to uncomment==========================//
		tv_contactsCountry=(TextView) llCallCenters
				.findViewById(R.id.tv_contactsCountry);
		tv_contactsCity=(TextView) llCallCenters
				.findViewById(R.id.tv_contactsCity);

		////////////////////////////////////////////////

		llcitycallcenters = (LinearLayout) llCallCenters
				.findViewById(R.id.llcitycallcenters);
		llcitycallcentersinmain = (LinearLayout) llCallCenters
				.findViewById(R.id.ll_mainincallcenters);
		llMiddleBase.addView(llCallCenters, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		tv_contactsCountry.setTypeface(typefaceOpenSansRegular);
		tv_contactsCity.setTypeface(typefaceOpenSansRegular);
	}

	@Override
	public void bindingControl() {
		showLoader("");
		if(new CommonBL(CallcentersActivityNew.this, this).getCityData())
		{}
		else
		{
			hideLoader();			
			showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.InternetProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
		}

		//=============================================newly added for city===============================================================================	

		tv_contactsCountry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (vecCountryNewDO != null && vecCountryNewDO.size()>0) {
					Intent i = new Intent(CallcentersActivityNew.this,SelectCountryContacsNew.class);
					i.putExtra("vecCountry", vecCountryNewDO);
					if(!tv_contactsCountry.getText().toString().equalsIgnoreCase(getString(R.string.selectCountry)))
						i.putExtra("presentlySelectedCountry", tv_contactsCountry.getText().toString()+"");
					startActivityForResult(i, COUNTRY_RESULT_CODE);
				}

			}
		});
		tv_contactsCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//				if (vecCountryNewDO != null && vecCountryNewDO.size()>0) {
				if (countryDO != null ) {
					Intent i = new Intent(CallcentersActivityNew.this,SelectCityContacsNew.class);
					i.putExtra("countryDO", countryDO);
					if(!tv_contactsCity.getText().toString().equalsIgnoreCase(getString(R.string.selectCity)))
						i.putExtra("presentlySelectedCity", tv_contactsCity.getText().toString()+"");
					startActivityForResult(i, CITY_RESULT_CODE);
				}else{
					//===============================uncomment it=========================================================					
					showCustomDialog(CallcentersActivityNew.this, getString(R.string.Alert), getString(R.string.alert_msg_empty_country), "OK", null, "CallcentersActivityNew");
				}

			}
		});

	}

	@Override
	public void onButtonYesClick(String from) {
		super.onButtonYesClick(from);
		if(from.equalsIgnoreCase(AppConstants.INTERNET_PROBLEM))
			clickHome();
	}

	//========================newly aded for city and country==============================
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);


		if (requestCode == COUNTRY_RESULT_CODE && resultCode == RESULT_OK) 
		{
			if(data!=null){
				if(data.hasExtra("Country_Selected")){
					countryDO=new CountriesDO();
					countryDO = ((CountriesDO)data.getSerializableExtra("Country_Selected"));
					tv_contactsCountry.setText(countryDO.name);
					tv_contactsCity.setText("All");
					tv_contactsCountry.setTypeface(typefaceOpenSansSemiBold);
					tv_contactsCity.setTypeface(typefaceOpenSansSemiBold);
					isCitySelectEnable=true;
					if(countryDO.name.equalsIgnoreCase("sudan"))
					{
						countryDO.vecCityDO.get(0).callcenter = "00249 183 746768";
						countryDO.vecCityDO.get(0).callcenter2 = "00249 183 746769";
					}
					loadCallCenterInfo();
				}
			}
			
		}
		if (requestCode == CITY_RESULT_CODE && resultCode == RESULT_OK) 
		{
			if(data!=null){
				if(data.hasExtra("Country_Selected")){
					cityDO=new CityDO();
					cityDO = ((CityDO)data.getSerializableExtra("Country_Selected"));
					tv_contactsCity.setText(cityDO.name);
					if(countryDO.name.equalsIgnoreCase("sudan"))
					{
					cityDO.callcenter = "00249 183 746768";
					cityDO.callcenter2 = "00249 183 746769";
					}
					loadCallCenterInfo();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dataRetreived(Response data) {
		hideLoader();
		if (data != null && !data.isError) {
			switch (data.method) {
			case WS_CITY:
				vecCountryDO = new Vector<CountriesDO>();
				vecCountryNewDO=new ArrayList<CountriesDO>();

				vecCountryDO =  (Vector<CountriesDO>) data.data;
				vecCountryNewDO.addAll(vecCountryDO);
				isCountrySelectEnable=true;


				break;
			default:
				break;
			}
		} else {
			if(data.data instanceof String)
			{
				if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
					showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
				else showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
			}
			else
				showCustomDialog(this, getString(R.string.Alert), getString(R.string.InternetProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isCitySelectEnable=false;
		isCountrySelectEnable=false;
	}
	void loadCallCenterInfo(){
		LinearLayout llCallCenter;
		TextView tvCityname, tvCountryname, tvCallcenternum, tv_callTiming, tvCallcenternum2;
		//		CountriesDO mcountriesDO = vecCountryDO
		//				.get(selectedPosincallcenters);
		boolean isArabic = checkLangArabic();
		if (tv_contactsCity!=null && !tv_contactsCity.getText().toString().equalsIgnoreCase("all")) {
			llcitycallcenters.setVisibility(View.GONE);
			llcitycallcentersinmain.setVisibility(View.VISIBLE);
			tvCityname = (TextView) findViewById(R.id.tv_officelocationsincallcenters);
			tvCallcenternum = (TextView) findViewById(R.id.tv_callcenternum);
			tvCallcenternum2 = (TextView) findViewById(R.id.tv_callcenternum2);
			tv_callTiming = (TextView) findViewById(R.id.tv_callTiming);
			tvCountryname = (TextView) findViewById(R.id.tv_countryincallcenters);
			llCallCenter = (LinearLayout) findViewById(R.id.llCallCenter);
			if (isArabic)
				tvCityname.setText(getString(R.string.CallCenterLocationsIn)
						+ " " + cityDO.name
						+ " "
						+ "<<"
						+ " "
						+ countryDO.name);
			else
				tvCityname.setText(getString(R.string.CallCenterLocationsIn)
						+ " "+countryDO.name
						+ " "
						+ ">>"
						+ " "
						+ cityDO.name);
			tvCountryname
			.setText(cityDO.name);
			tvCallcenternum.setText(""
					+ cityDO.callcenter);
			if(countryDO.name.equalsIgnoreCase("sudan"))
			{
				llCallCenter.setVisibility(View.VISIBLE);
				tvCallcenternum2.setText(cityDO.callcenter2);
			}
			else
			{
				llCallCenter.setVisibility(View.GONE);
			}
			cityDO.other = " "+cityDO.other;
			if(cityDO.other.contains(",")){
				cityDO.other = cityDO.other.replace(",",",\n");
			}
			tv_callTiming.setText(cityDO.other);


			//------------------------------------------------- for calling on click functionality on touch ---------------------------------------------------//									
			final String tvCallCenterString = tvCallcenternum.getText().toString().trim();
			tvCallcenternum.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String[] parts = tvCallCenterString.split("/");
					List<String> callCenterNumbersList = new ArrayList<String>();
					for (int i = 0; i < parts.length; i++) {
						if (parts[i].contains("Hotline ")) {
							String[] temp = parts[i].split("Hotline ");
							callCenterNumbersList.add(temp[1].trim()) ;
						}
						else
						{
							callCenterNumbersList.add(parts[i]);
						}
					}
					if(countryDO.name.equalsIgnoreCase("sudan"))
					{
						callCenterNumbersList.add(cityDO.callcenter2);
					}
					showCallOnTouchPopup(callCenterNumbersList);
				}
			});
			//========================================================================================================================================//
		}

		if (tv_contactsCity!=null &&tv_contactsCity.getText().toString().equalsIgnoreCase("all")) {
			llcitycallcentersinmain
			.setVisibility(View.GONE);
			llcitycallcenters
			.setVisibility(View.VISIBLE);
			llcitycallcenters
			.removeAllViews();
			if (countryDO.vecCityDO != null
					&& countryDO.vecCityDO
					.size() > 0) {
				for (int i = 0; i < countryDO.vecCityDO
						.size(); i++) {

					LinearLayout layoutcell = (LinearLayout) layoutInflater
							.inflate(
									R.layout.layoutcitycallcenterscell,
									null);
					tvCityname = (TextView) layoutcell
							.findViewById(R.id.tv_officelocationsincallcenters1);
					tvCallcenternum = (TextView) layoutcell
							.findViewById(R.id.tv_callcenternum1);
					tvCallcenternum2 = (TextView) layoutcell
							.findViewById(R.id.tv_callcenternum2);
					llCallCenter = (LinearLayout) layoutcell
							.findViewById(R.id.llCallCenter);
					tv_callTiming = (TextView) layoutcell.findViewById(R.id.tv_callTiming);

					tvCountryname = (TextView) layoutcell
							.findViewById(R.id.tv_countryincallcenters1);
					if (isArabic)
						tvCityname
						.setText(getString(R.string.CallCenterLocationsIn)
								+ " "+countryDO.vecCityDO
								.get(i).name
								+ " "
								+ "<<"
								+ " "
								+ countryDO.name);
					else
						tvCityname
						.setText(getString(R.string.CallCenterLocationsIn)
								+ " "
								+ countryDO.name
								+ " "
								+ ">>"
								+ " "
								+ countryDO.vecCityDO
								.get(i).name);
					tvCountryname
					.setText(countryDO.vecCityDO
							.get(i).name);
					tvCallcenternum
					.setText(""
							+ countryDO.vecCityDO
							.get(i).callcenter);
					if(countryDO.name.equalsIgnoreCase("sudan"))
					{
						llCallCenter.setVisibility(View.VISIBLE);
						tvCallcenternum2.setText(countryDO.vecCityDO.get(0).callcenter2);
					}
					else
					{
						llCallCenter.setVisibility(View.GONE);
					}
					countryDO.vecCityDO.get(i).other = " "+countryDO.vecCityDO.get(i).other;
					if(countryDO.vecCityDO.get(i).other.contains(",")){
						countryDO.vecCityDO.get(i).other = countryDO.vecCityDO.get(i).other.replace(",",",\n");
					}
					
					tv_callTiming
					.setText(countryDO.vecCityDO
							.get(i).other);

					//------------------------------------------------- for call functionality on touch ------------------------------------------//												
					final String tvCallCenterString = tvCallcenternum.getText().toString().trim();
					tvCallcenternum.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String[] parts = tvCallCenterString.split("/");
							List<String> callCenterNumbersList = new ArrayList<String>();
							for (int i = 0; i < parts.length; i++) {
								if (parts[i].contains("Hotline ")) {
									String[] temp = parts[i].split("Hotline ");
									callCenterNumbersList.add(temp[1].trim()) ;
								}
								else
								{
									callCenterNumbersList.add(parts[i]);
								}
							}
							if(countryDO.name.equalsIgnoreCase("sudan"))
							{
								callCenterNumbersList.add(countryDO.vecCityDO.get(0).callcenter2);
							}
							showCallOnTouchPopup(callCenterNumbersList);
						}
					});

					//---------------------------------------------------------------------------------------------------------------------------------------//
					llcitycallcenters
					.addView(
							layoutcell,
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT);

				}
			}

		}




	}



}