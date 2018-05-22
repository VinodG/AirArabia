//package com.winit.airarabia;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Vector;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.LinearLayout;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.winit.airarabia.adapters.SpinnerofficecityAdapter;
//import com.winit.airarabia.adapters.SpinnerofficecountryAdapter;
//import com.winit.airarabia.busynesslayer.CommonBL;
//import com.winit.airarabia.busynesslayer.DataListener;
//import com.winit.airarabia.common.AppConstants;
//import com.winit.airarabia.objects.OfficeDO;
//import com.winit.airarabia.objects.OfficeLocationDO;
//import com.winit.airarabia.utils.StringUtils;
//import com.winit.airarabia.webaccess.Response;
//
//public class OfficeLocationActivity extends BaseActivity implements
//DataListener {
//
//	private LinearLayout llOfficelocation;
//	private LinearLayout lloffloc, llofflocmain;
//	private Vector<OfficeLocationDO> vecOfficeLocationDO;
//	private Spinner CountrySpinner, CitySpinner;
//	private int selectedPos, cityselectedpos = -1, i;
//	
//	private SpinnerofficecountryAdapter spinnerofficecountryadap;
//	private SpinnerofficecityAdapter spinnerofficecityadap;
//	private Vector<OfficeDO> vecOfficeDO;
//
//	private OfficeLocationActivity.BCR bcr;
//
//	private class BCR extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			if (intent.getAction().equalsIgnoreCase(AppConstants.HOME_CLICK))
//				finish();
//		}
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		unregisterReceiver(bcr);
//	}
//
//	@Override
//	public void initilize() {
//		bcr = new BCR();
//		intentFilter.addAction(AppConstants.HOME_CLICK);
//		registerReceiver(bcr, intentFilter);
//		llOfficelocation = (LinearLayout) layoutInflater.inflate(
//				R.layout.officelocation, null);
//
//		CountrySpinner = (Spinner) llOfficelocation
//				.findViewById(R.id.spinnerinofficelocation);
//		CitySpinner = (Spinner) llOfficelocation
//				.findViewById(R.id.spinner1inofficelocation);
//		lloffloc = (LinearLayout) llOfficelocation
//				.findViewById(R.id.ll_officeloc);
//		llofflocmain = (LinearLayout) llOfficelocation
//				.findViewById(R.id.ll_officelocinmain);
//		llMiddleBase.addView(llOfficelocation, LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT);
//		tvHeaderTitle.setText(getString(R.string.OfficeLocation));
//		vecOfficeDO = new Vector<OfficeDO>();
//		vecOfficeLocationDO = new Vector<OfficeLocationDO>();
//	}
//
//	@Override
//	public void bindingControl() {
//		if (new CommonBL(OfficeLocationActivity.this, this)
//		.getOfficeLocationData()) {
//			showLoader("");
//		} else
//			hideLoader();
//	}
//
//	@Override
//	public void onButtonYesClick(String from) {
//		super.onButtonYesClick(from);
//		if(from.equalsIgnoreCase(AppConstants.INTERNET_PROBLEM))
//			clickHome();
//	}
// 
//	@SuppressWarnings("unchecked")
//	@Override
//	public void dataRetreived(Response data) {
//		if (data != null && !data.isError) {
//			switch (data.method) {
//			case WS_OFFICELOCATIONS:
//
//				vecOfficeLocationDO.clear();
//				vecOfficeLocationDO = (Vector<OfficeLocationDO>) data.data;
//
///// ------------------------- Vector's Sorting Done by Rahul-----------------------////			
//				
//				Collections.sort(vecOfficeLocationDO, new OfficeLocationComparator());
//				
///// ------------------------------------------------------------------------------////	
//				if (vecOfficeLocationDO.get(selectedPos).vecOfficeDO != null) {
//					spinnerofficecountryadap = new SpinnerofficecountryAdapter(
//							OfficeLocationActivity.this, vecOfficeLocationDO);
//					CountrySpinner.setAdapter(spinnerofficecountryadap);
//				}
//
//				CountrySpinner
//				.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//					@Override
//					public void onItemSelected(AdapterView<?> arg0,
//							View arg1, int pos, long pos1) {
//						final int selectedCountryPosition= pos;
//						selectedPos = pos;
//						vecOfficeDO.clear();
//						vecOfficeDO.add(null);
//						vecOfficeDO.addAll(vecOfficeLocationDO
//								.get(selectedPos).vecOfficeDO);
//
//						spinnerofficecityadap = new SpinnerofficecityAdapter(
//								OfficeLocationActivity.this,
//								vecOfficeDO);
//						CitySpinner.setAdapter(spinnerofficecityadap);
//
//						CitySpinner
//						.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//							@Override
//							public void onItemSelected(
//									AdapterView<?> arg0,
//									View arg1, int posi,
//									long arg3) {
//								cityselectedpos = posi;
//								TextView tvCountryheader, tvCityHeader, tvName, tvCity, tvPhone, tvLocation;
//
//								boolean isArabic = checkLangArabic();
//
//								if (cityselectedpos != -1
//										&& cityselectedpos != 0) {
//									lloffloc.setVisibility(View.GONE);
//									llofflocmain.setVisibility(View.VISIBLE);
//
//									tvCountryheader = (TextView) findViewById(R.id.tv_officelocationsinofficexml);
//									tvCityHeader = (TextView) findViewById(R.id.tv_cityinofficelocation);
//									tvName = (TextView) findViewById(R.id.tv_nameinofficeloc);
//									tvCity = (TextView) findViewById(R.id.tv_cityinoffice);
//									tvPhone = (TextView) findViewById(R.id.tv_phoneinoffice);
//									tvLocation = (TextView) findViewById(R.id.tv_locationinoffice);
//									if (isArabic)
//										tvCountryheader.setText(getString(R.string.OfficeLocationsIn)
//												+ " "
//												+ vecOfficeDO
//												.get(cityselectedpos).city
//												+ " << "
//												+ vecOfficeLocationDO
//												.get(selectedPos).country);
//									else
//										tvCountryheader.setText(getString(R.string.OfficeLocationsIn)
//												+ " "
//												+ vecOfficeLocationDO
//												.get(selectedPos).country
//												+ " >> "
//												+ vecOfficeDO
//												.get(cityselectedpos).city);
//									tvCityHeader
//									.setText(vecOfficeDO
//											.get(cityselectedpos).city);
//									/*tvName.setText(getString(R.string.AirArabia)+" - "
//											+ vecOfficeDO
//											.get(cityselectedpos).city);*/
//									
//									tvName.setText(vecOfficeDO
//											.get(cityselectedpos).name);
//									
//									tvCity.setText(vecOfficeDO
//											.get(cityselectedpos).city);
//									tvPhone.setText(vecOfficeDO
//											.get(cityselectedpos).phone);
//
//									
/////------------------------------------------- Functionality for call on touch --------------------------------------------//									
//									final String tvCallCenterString = tvPhone.getText().toString().trim();
//									tvPhone.setOnClickListener(new OnClickListener() {
//										
//										@Override
//										public void onClick(View v) {
//											String[] parts = tvCallCenterString.split("/");
//											List<String> callCenterNumbersList = new ArrayList<String>();
//											for (int i = 0; i < parts.length; i++) {
//												if (parts[i].contains("Hotline ")) {
//													String[] temp = parts[i].split("Hotline ");
//													callCenterNumbersList.add(temp[1].trim()) ;
//												}
//												else
//												{
//													callCenterNumbersList.add(parts[i]);
//												}
//											}
//											showCallOnTouchPopup(callCenterNumbersList);
//										}
//									});
//								
////---------------------------------------------------------------------------------------------------------------------------//
//									
//									tvLocation
//									.setText(vecOfficeDO
//											.get(cityselectedpos).location);
//
//									llofflocmain
//									.setOnClickListener(new OnClickListener() {
//
//										@Override
//										public void onClick(
//												View v) {
//											Intent in = new Intent(
//													OfficeLocationActivity.this,
//													MapActivity.class);
//											in.putExtra(
//													AppConstants.LOCATION,
//													vecOfficeDO
//													.get(cityselectedpos).city+
//													"+"+vecOfficeLocationDO
//													.get(selectedCountryPosition)
//													.country);
//											in.putExtra(
//													AppConstants.LATITUDE,
//													vecOfficeDO
//													.get(cityselectedpos).latitude);
//											in.putExtra(
//													AppConstants.LONGITUDE,
//													vecOfficeDO
//													.get(cityselectedpos).longitude);
//											startActivity(in);
//										}
//									});
//									///========================================
//								} else if (cityselectedpos == 0) {
//									llofflocmain
//									.setVisibility(View.GONE);
//									lloffloc.setVisibility(View.VISIBLE);
//
//									lloffloc.removeAllViews();
//									for (i = 0; i < vecOfficeDO
//											.size(); i++) {
//										if (i != 0) {
//											LinearLayout lloffcityloc = (LinearLayout) layoutInflater
//													.inflate(
//															R.layout.layoutcitylocationcell,
//															null);
//											TextView tvCountryheader1, tvCityHeader1, tvName1, tvCity1, tvPhone1, tvLocation1;
//											tvCountryheader1 = (TextView) lloffcityloc
//													.findViewById(R.id.tv_officelocationsinofficexml1);
//											tvCityHeader1 = (TextView) lloffcityloc
//													.findViewById(R.id.tv_cityinofficelocation1);
//											tvName1 = (TextView) lloffcityloc
//													.findViewById(R.id.tv_nameinofficeloc1);
//											tvCity1 = (TextView) lloffcityloc
//													.findViewById(R.id.tv_cityinoffice1);
//											tvPhone1 = (TextView) lloffcityloc
//													.findViewById(R.id.tv_phoneinoffice1);
//											tvLocation1 = (TextView) lloffcityloc
//													.findViewById(R.id.tv_locationinoffice1);
//											if (isArabic)
//												tvCountryheader1
//												.setText(getString(R.string.OfficeLocationsIn)
//														+ " "
//														+ vecOfficeDO
//														.get(i).city
//														+ " << "
//														+ vecOfficeLocationDO
//														.get(selectedPos).country);
//											else
//												tvCountryheader1
//												.setText(getString(R.string.OfficeLocationsIn)
//														+ " "
//														+ vecOfficeLocationDO
//														.get(selectedPos).country
//														+ " >> "
//														+ vecOfficeDO
//														.get(i).city);
//											tvCityHeader1
//											.setText(vecOfficeDO
//													.get(i).city);
//											/*tvName1.setText(getString(R.string.AirArabia)
//													+ " - "
//													+ vecOfficeDO
//													.get(i).city);*/
//											tvName1.setText(vecOfficeDO
//													.get(i).name);
//											tvCity1.setText(vecOfficeDO
//													.get(i).city);
//											tvPhone1.setText(vecOfficeDO
//													.get(i).phone);
//											
/////------------------------------------------- Functionality for call on touch --------------------------------------------//									
//											final String tvCallCenterString = tvPhone1.getText().toString().trim();
//											tvPhone1.setOnClickListener(new OnClickListener() {
//												
//												@Override
//												public void onClick(View v) {
//													String[] parts = tvCallCenterString.split("/");
//													List<String> callCenterNumbersList = new ArrayList<String>();
//													for (int i = 0; i < parts.length; i++) {
//														if (parts[i].contains("Hotline ")) {
//															String[] temp = parts[i].split("Hotline ");
//															callCenterNumbersList.add(temp[1].trim()) ;
//														}
//														else
//														{
//															callCenterNumbersList.add(parts[i]);
//														}
//													}
//													showCallOnTouchPopup(callCenterNumbersList);
//													
//												}
//											});
//										
//		//---------------------------------------------------------------------------------------------------------------------------//
//											
//											tvLocation1
//											.setText(vecOfficeDO
//													.get(i).location);
//
//											lloffcityloc
//											.setTag(i
//													+ "");
//											lloffcityloc
//											.setOnClickListener(new OnClickListener() {
//
//												@Override
//												public void onClick(
//														View v) {
//													
//													
//													Intent in = new Intent(
//															OfficeLocationActivity.this,
//															MapActivity.class);
//													in.putExtra(
//															AppConstants.LOCATION,
//															vecOfficeDO
//															.get(StringUtils
//																	.getInt(v
//																			.getTag()
//																			.toString())).city+"+"+
//																			vecOfficeLocationDO
//																			.get(selectedPos).country);
//													in.putExtra(
//															AppConstants.LATITUDE,
//															vecOfficeDO
//															.get(StringUtils
//																	.getInt(v
//																			.getTag()
//																			.toString())).latitude);
//													in.putExtra(
//															AppConstants.LONGITUDE,
//															vecOfficeDO
//															.get(StringUtils
//																	.getInt(v
//																			.getTag()
//																			.toString())).longitude);
//													//------------------------
//													startActivity(in);
//												}
//											});
//											lloffloc.addView(lloffcityloc);
//										}
//									}
//								}
//							}
//
//							@Override
//							public void onNothingSelected(
//									AdapterView<?> arg0) {
//							}
//						});
//					}
//
//					@Override
//					public void onNothingSelected(AdapterView<?> arg0) {
//					}
//				});
//				break;
//			default:
//				break;
//			}
//		}
//		else if(data.data instanceof String)
//		{
//			if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
//				showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
//			else showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
//		}
//		hideLoader();
//		
//		
//	}
//	
//	
//}