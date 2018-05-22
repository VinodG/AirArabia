//package com.winit.airarabia;
//
//import java.util.ArrayList;
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
//import com.winit.airarabia.adapters.SpinnercityAdapter;
//import com.winit.airarabia.adapters.SpinnercountryAdapter;
//import com.winit.airarabia.busynesslayer.CommonBL;
//import com.winit.airarabia.busynesslayer.DataListener;
//import com.winit.airarabia.common.AppConstants;
//import com.winit.airarabia.objects.CountriesDO;
//import com.winit.airarabia.webaccess.Response;
//
//public class CallcentersActivity extends BaseActivity implements DataListener {
//
//	private LinearLayout llCallCenters, llcitycallcenters,
//			llcitycallcentersinmain;
//	private Vector<CountriesDO> vecCountryDO;
//	private SpinnercountryAdapter spinnercountryadap;
//	private SpinnercityAdapter spinnercityadap;
//	private Spinner CountrySpinner, CitySpinner;
//	private int selectedPosincallcenters, cityselectedposincallcenters = -1;
//	private CallcentersActivity.BCR bcr;
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
//		tvHeaderTitle.setText(getString(R.string.CallCenters));
//		bcr = new BCR();
//		intentFilter.addAction(AppConstants.HOME_CLICK);
//		registerReceiver(bcr, intentFilter);
//		llCallCenters = (LinearLayout) layoutInflater.inflate(
//				R.layout.callcenters, null);
//		CountrySpinner = (Spinner) llCallCenters
//				.findViewById(R.id.spinnerincallcenters);
//		CitySpinner = (Spinner) llCallCenters
//				.findViewById(R.id.spinner1incallcenters);
//		llcitycallcenters = (LinearLayout) llCallCenters
//				.findViewById(R.id.llcitycallcenters);
//		llcitycallcentersinmain = (LinearLayout) llCallCenters
//				.findViewById(R.id.ll_mainincallcenters);
//		llMiddleBase.addView(llCallCenters, LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT);
//	}
//
//	@Override
//	public void bindingControl() {
//		showLoader("");
//		if (new CommonBL(CallcentersActivity.this, this).getCityData()) {
//		} else {
//			hideLoader();
//			showCustomDialog(getApplicationContext(),
//					getString(R.string.Alert),
//					getString(R.string.InternetProblem),
//					getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
//		}
//
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public void dataRetreived(Response data) {
//		hideLoader();
//		if (data != null && !data.isError) {
//			switch (data.method) {
//			case WS_CITY:
//				vecCountryDO = new Vector<CountriesDO>();
//				vecCountryDO = (Vector<CountriesDO>) data.data;
//				if (vecCountryDO.get(selectedPosincallcenters).vecCityDO != null) {
//					spinnercountryadap = new SpinnercountryAdapter(
//							CallcentersActivity.this, vecCountryDO);
//					CountrySpinner.setAdapter(spinnercountryadap);
//				
//					
//				}
//
//				CountrySpinner
//						.setOnItemSelectedListener(new OnItemSelectedListener() {
//							@Override
//							public void onItemSelected(AdapterView<?> arg0,
//									View arg1, int pos, long pos1) {
//								selectedPosincallcenters = pos;
//								if (vecCountryDO.get(selectedPosincallcenters).vecCityDO
//										.size() > 0
//										&& vecCountryDO
//												.get(selectedPosincallcenters).vecCityDO
//												.get(0) != null)
//									vecCountryDO.get(selectedPosincallcenters).vecCityDO
//											.add(0, null);
//								else if (vecCountryDO
//										.get(selectedPosincallcenters).vecCityDO
//										.size() == 0)
//									vecCountryDO.get(selectedPosincallcenters).vecCityDO
//											.add(0, null);
//								spinnercityadap = new SpinnercityAdapter(
//										CallcentersActivity.this,
//										vecCountryDO
//												.get(selectedPosincallcenters).vecCityDO);
//								CitySpinner.setAdapter(spinnercityadap);
//								CitySpinner
//										.setOnItemSelectedListener(new OnItemSelectedListener() {
//											@Override
//											public void onItemSelected(
//													AdapterView<?> arg0,
//													View arg1, int posi,
//													long arg3) {
//												cityselectedposincallcenters = posi;
//												TextView tvCityname, tvCountryname, tvCallcenternum, tv_callTiming;
//												CountriesDO mcountriesDO = vecCountryDO
//														.get(selectedPosincallcenters);
//												boolean isArabic = checkLangArabic();
//												if (cityselectedposincallcenters != -1		&& cityselectedposincallcenters != 0) {
//													llcitycallcenters
//															.setVisibility(View.GONE);
//													llcitycallcentersinmain
//															.setVisibility(View.VISIBLE);
//													tvCityname = (TextView) findViewById(R.id.tv_officelocationsincallcenters);
//													tvCallcenternum = (TextView) findViewById(R.id.tv_callcenternum);
//													tv_callTiming = (TextView) findViewById(R.id.tv_callTiming);
//													tvCountryname = (TextView) findViewById(R.id.tv_countryincallcenters);
//													if (isArabic)
//														tvCityname.setText(getString(R.string.CallCenterLocationsIn)
//																+ " "
//																+ mcountriesDO.vecCityDO
//																		.get(cityselectedposincallcenters).name
//																+ " "
//																+ "<<"
//																+ " "
//																+ mcountriesDO.name);
//													else
//														tvCityname.setText(getString(R.string.CallCenterLocationsIn)
//																+ " "
//																+ mcountriesDO.name
//																+ " "
//																+ ">>"
//																+ " "
//																+ mcountriesDO.vecCityDO
//																		.get(cityselectedposincallcenters).name);
//													tvCountryname
//															.setText(mcountriesDO.vecCityDO
//																	.get(cityselectedposincallcenters).name);
//													tvCallcenternum.setText(""
//															+ mcountriesDO.vecCityDO
//																	.get(cityselectedposincallcenters).callcenter);
//													tv_callTiming
//															.setText(mcountriesDO.vecCityDO
//																	.get(cityselectedposincallcenters).other);
//
//													// -------------------------------------------------
//													// for calling on click
//													// functionality on touch
//													// ---------------------------------------------------//
//													final String tvCallCenterString = tvCallcenternum
//															.getText()
//															.toString().trim();
//													tvCallcenternum
//															.setOnClickListener(new OnClickListener() {
//
//																@Override
//																public void onClick(
//																		View v) {
//																	String[] parts = tvCallCenterString
//																			.split("/");
//																	List<String> callCenterNumbersList = new ArrayList<String>();
//																	for (int i = 0; i < parts.length; i++) {
//																		if (parts[i]
//																				.contains("Hotline ")) {
//																			String[] temp = parts[i]
//																					.split("Hotline ");
//																			callCenterNumbersList
//																					.add(temp[1]
//																							.trim());
//																		} else {
//																			callCenterNumbersList
//																					.add(parts[i]);
//																		}
//																	}
//																	showCallOnTouchPopup(callCenterNumbersList);
//																}
//															});
//													// ========================================================================================================================================//
//												}
//
//												if (cityselectedposincallcenters == 0) {
//													llcitycallcentersinmain
//															.setVisibility(View.GONE);
//													llcitycallcenters
//															.setVisibility(View.VISIBLE);
//													llcitycallcenters
//															.removeAllViews();
//													if (mcountriesDO.vecCityDO != null
//															&& mcountriesDO.vecCityDO
//																	.size() > 0) {
//														for (int i = 0; i < mcountriesDO.vecCityDO
//																.size(); i++) {
//															if (i != 0) {
//																LinearLayout layoutcell = (LinearLayout) layoutInflater
//																		.inflate(
//																				R.layout.layoutcitycallcenterscell,
//																				null);
//																tvCityname = (TextView) layoutcell
//																		.findViewById(R.id.tv_officelocationsincallcenters1);
//																tvCallcenternum = (TextView) layoutcell
//																		.findViewById(R.id.tv_callcenternum1);
//																tv_callTiming = (TextView) layoutcell
//																		.findViewById(R.id.tv_callTiming);
//
//																tvCountryname = (TextView) layoutcell
//																		.findViewById(R.id.tv_countryincallcenters1);
//																if (isArabic)
//																	tvCityname
//																			.setText(getString(R.string.CallCenterLocationsIn)
//																					+ " "
//																					+ mcountriesDO.vecCityDO
//																							.get(i).name
//																					+ " "
//																					+ "<<"
//																					+ " "
//																					+ mcountriesDO.name);
//																else
//																	tvCityname
//																			.setText(getString(R.string.CallCenterLocationsIn)
//																					+ " "
//																					+ mcountriesDO.name
//																					+ " "
//																					+ ">>"
//																					+ " "
//																					+ mcountriesDO.vecCityDO
//																							.get(i).name);
//																tvCountryname
//																		.setText(mcountriesDO.vecCityDO
//																				.get(i).name);
//																tvCallcenternum
//																		.setText(""
//																				+ mcountriesDO.vecCityDO
//																						.get(i).callcenter);
//																tv_callTiming
//																		.setText(mcountriesDO.vecCityDO
//																				.get(i).other);
//
//																// -------------------------------------------------
//																// for call
//																// functionality
//																// on touch
//																// ------------------------------------------//
//																final String tvCallCenterString = tvCallcenternum
//																		.getText()
//																		.toString()
//																		.trim();
//																tvCallcenternum
//																		.setOnClickListener(new OnClickListener() {
//
//																			@Override
//																			public void onClick(
//																					View v) {
//																				String[] parts = tvCallCenterString
//																						.split("/");
//																				List<String> callCenterNumbersList = new ArrayList<String>();
//																				for (int i = 0; i < parts.length; i++) {
//																					if (parts[i]
//																							.contains("Hotline ")) {
//																						String[] temp = parts[i]
//																								.split("Hotline ");
//																						callCenterNumbersList
//																								.add(temp[1]
//																										.trim());
//																					} else {
//																						callCenterNumbersList
//																								.add(parts[i]);
//																					}
//																				}
//
//																				showCallOnTouchPopup(callCenterNumbersList);
//																			}
//																		});
//
//																// ---------------------------------------------------------------------------------------------------------------------------------------//
//
//																llcitycallcenters
//																		.addView(
//																				layoutcell,
//																				LayoutParams.MATCH_PARENT,
//																				LayoutParams.WRAP_CONTENT);
//															}
//														}
//													}
//
//												}
//											}
//
//											@Override
//											public void onNothingSelected(
//													AdapterView<?> arg0) {
//											}
//										});
//							}
//
//							@Override
//							public void onNothingSelected(AdapterView<?> arg0) {
//							}
//						});
//				break;
//			default:
//				break;
//			}
//		} else {
//			if (data.data instanceof String) {
//				if (((String) data.data)
//						.equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
//					showCustomDialog(getApplicationContext(),
//							getString(R.string.Alert),
//							getString(R.string.ConnenectivityTimeOutExpMsg),
//							getString(R.string.Ok), "",
//							AppConstants.INTERNET_PROBLEM);
//				else
//					showCustomDialog(getApplicationContext(),
//							getString(R.string.Alert),
//							getString(R.string.TechProblem),
//							getString(R.string.Ok), "",
//							AppConstants.INTERNET_PROBLEM);
//			} else
//				showCustomDialog(this, getString(R.string.Alert),
//						getString(R.string.InternetProblem),
//						getString(R.string.Ok), "",
//						AppConstants.INTERNET_PROBLEM);
//		}
//	}
//
//	@Override
//	public void onButtonYesClick(String from) {
//		super.onButtonYesClick(from);
//		if (from.equalsIgnoreCase(AppConstants.INTERNET_PROBLEM))
//			clickHome();
//	}
//}