package com.winit.airarabia;

import java.util.Calendar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirBookDO;
import com.winit.airarabia.objects.AirportNamesDO;
import com.winit.airarabia.objects.AirportsDO;
import com.winit.airarabia.objects.AllAirportNamesDO;
import com.winit.airarabia.objects.AllAirportsDO;
import com.winit.airarabia.objects.RequestParameterDO;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.webaccess.Response;

public class ManageYourBookingActivity extends BaseActivity implements OnClickListener, DataListener {

	private LinearLayout llManageyourbooking;
	private EditText edt_pnr, edt_lastnameinmanagebooking;
	private TextView tv_depaturedate, tv_pnrTag, tv_lastnameinmanagebookingTag, tv_depaturedateTag,
			tvManageBookingTitle;
	private Calendar calendar;
	private AirBookDO airBookDO;
	private ManageYourBookingActivity.BCR bcr;
	//newly added
	private WebView wvManageBooking;
	private final long TIME_LIMIT = 5 * 1000;

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

		tvHeaderTitle.setText(getString(R.string.ManageBooking));		

		llManageyourbooking = (LinearLayout) layoutInflater.inflate(R.layout.managebooking, null);
		llMiddleBase.addView(llManageyourbooking, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		edt_pnr = (EditText) llManageyourbooking.findViewById(R.id.edt_pnr);
		edt_lastnameinmanagebooking = (EditText) llManageyourbooking.findViewById(R.id.edt_lastnameinmanagebooking);
		tv_depaturedate = (TextView) llManageyourbooking.findViewById(R.id.tv_depaturedate);
		tvManageBookingTitle = (TextView) llManageyourbooking.findViewById(R.id.tvManageBookingTitle);
		tv_pnrTag = (TextView) llManageyourbooking.findViewById(R.id.tv_pnrTag);
		tv_lastnameinmanagebookingTag = (TextView) llManageyourbooking
				.findViewById(R.id.edt_lastnameinmanagebookingTag);
		tv_depaturedateTag = (TextView) llManageyourbooking.findViewById(R.id.tv_depaturedateTag);
		
		setTypeFaceOpenSansLight(llManageyourbooking);
		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(R.string.Continue);
		btnBack.setTypeface(typefaceOpenSansSemiBold);
		tvHeaderTitle.setTypeface(typefaceOpenSansSemiBold);
		btnSubmitNext.setTypeface(typefaceOpenSansSemiBold);
		tvManageBookingTitle.setTypeface(typefaceOpenSansSemiBold);
		// calendar = Calendar.getInstance();
//		if(!checkLangArabic()&&!checkLangFrench()){
//			wvManageBooking = (WebView) llManageyourbooking.findViewById(R.id.wvMangaeBook);
//			btnSubmitNext.setVisibility(View.GONE);
//			showLoader("");
//			goToWebview();
//		}
	}
//***********************************************
	private void goToWebview() {
		// TODO Auto-generated method stub
		 final Activity MyActivity = this;
		getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
				Window.PROGRESS_VISIBILITY_ON);
		wvManageBooking.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				/** Make the bar disappear after URL is loaded, and changes
				 string to Loading...*/
				MyActivity.setTitle(getString(R.string.Loading));
				MyActivity.setProgress(progress * 100); // Make the bar disappear after URL is loaded

				/** Return the app name after finish loading*/
				if (progress == 100)
					MyActivity.setTitle(R.string.app_name);
			}
		});
		/** to open the all urls in web view only.*/
		wvManageBooking.setWebViewClient(new WebViewClient(){
			 public void onPageFinished(WebView view, String url) {
			        // do your stuff here
					hideLoader();
			    }
		});
		wvManageBooking.getSettings().setJavaScriptEnabled(true);
		wvManageBooking.getSettings().setUseWideViewPort(true);
		wvManageBooking.setInitialScale(10);
		wvManageBooking.getSettings().setBuiltInZoomControls(true);
		String url="http://holidays.airarabia.com/";
		wvManageBooking.loadUrl(url);
		
		
	}
	/** To handle the back press in webview*/
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (event.getAction() == KeyEvent.ACTION_DOWN) {
//			switch (keyCode) {
//			case KeyEvent.KEYCODE_BACK:
//				if (wvManageBooking.canGoBack() == true) {
//					wvManageBooking.goBack();
//				} else {
//					finish();
//				}
//				return true;
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	//**************************************
	@Override
	public void bindingControl() {
		tv_depaturedate.setOnClickListener(this);
		btnSubmitNext.setOnClickListener(this);
		tv_depaturedate.setTag("");
		
		edt_pnr.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

				if (!(edt_pnr.getText().toString()).equalsIgnoreCase("")) {

					tv_pnrTag.setVisibility(View.VISIBLE);
				} else {
					tv_pnrTag.setVisibility(View.GONE);
				}
			}
		});
		edt_lastnameinmanagebooking.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

				if (!(edt_lastnameinmanagebooking.getText().toString()).equalsIgnoreCase("")) {

					tv_lastnameinmanagebookingTag.setVisibility(View.VISIBLE);
				} else {
					tv_lastnameinmanagebookingTag.setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_depaturedate:
			moveToSelectDateActivity();
			break;
		case R.id.btnSubmitNext:
			if (isValid()) {
				RequestParameterDO requestParameterDO = new RequestParameterDO();
				String strPNR = edt_pnr.getText().toString().trim();
				showLoader("");

				// ---------------------------- According to Starting of PNR, we
				// are changing service URL-----------------------------------//
				String tempPnrStarting = String.valueOf(strPNR.charAt(0));
				if (tempPnrStarting.equals("3") || tempPnrStarting.equals("4") || tempPnrStarting.equals("9")) {
					requestParameterDO.srvUrlType = AppConstants.SERVICE_URL_TYPE_G9;
				} else if (tempPnrStarting.equals("5")) {
					requestParameterDO.srvUrlType = AppConstants.SERVICE_URL_TYPE_E5;
				} else if (tempPnrStarting.equals("7")) {
					requestParameterDO.srvUrlType = AppConstants.SERVICE_URL_TYPE_3O;
				} else if (tempPnrStarting.equals("1")) {
					requestParameterDO.srvUrlType = AppConstants.SERVICE_URL_TYPE_9P;
				}
				
				/// ------------------------------------------------------------------------------------------------------------------------//

				// requestParameterDO.srvUrlType =
				// AppConstants.SERVICE_URL_TYPE_G9;
				if (new CommonBL(ManageYourBookingActivity.this, ManageYourBookingActivity.this)
						.getAirPNRData(requestParameterDO, strPNR)) {
				} else
					hideLoader();
			}
			break;
		}
	}

	private void moveToSelectDateActivity() {
		Intent in = new Intent(ManageYourBookingActivity.this, SelectDateActivityOneWay.class);
		in.putExtra(AppConstants.RETURN, false);
		in.putExtra(AppConstants.FROM, "ManageYourBookingActivity");
		in.putExtra(AppConstants.SEL_DATE, calendar);
		startActivityForResult(in, 1000);
		overridePendingTransition(R.anim.bottom_top_popup_share, R.anim.top_bottom_popup_share);
	}

	private boolean isValid() {
		boolean flag = true;
		String strMessage = "";
		if (edt_pnr.getText().toString().trim().length() < 8)
			strMessage = getString(R.string.PNRError);
		else if (edt_lastnameinmanagebooking.getText().toString().trim().length() <= 0)
			strMessage = getString(R.string.LastNameError);
		else if (tv_depaturedate.getTag().toString().trim().length() <= 0)
			strMessage = getString(R.string.DepartureDateErr);
		if (strMessage.length() > 0) {
			flag = false;
			showCustomDialog(this, getString(R.string.Alert), getString(R.string.Please_enter_all_details),
					getString(R.string.Ok), "", "");
		}
		return flag;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1000 && resultCode == RESULT_OK) {
			if (data.hasExtra(AppConstants.SEL_DATE)) {
				calendar = (Calendar) data.getExtras().getSerializable(AppConstants.SEL_DATE);
				if (calendar != null) {
					String dateTimeStamp = CalendarUtility.getBookingDate(calendar);
					tv_depaturedate.setText(CalendarUtility.getDDMMMYYYYDate(calendar));
					tv_depaturedate.setTextColor(getResources().getColor(R.color.text_color));
					tv_depaturedate.setTag(dateTimeStamp);
					tv_depaturedateTag.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	private void updateAllAirPortsNames() {
		AppConstants.allAirportNamesDO = new AllAirportNamesDO();

		if (checkLangArabic()) {
			for (AirportsDO airportsDO : AppConstants.allAirportsDO.vecAirport) {
				AirportNamesDO airportNamesDO = new AirportNamesDO();
				airportNamesDO.code = airportsDO.code;
				airportNamesDO.ar = airportsDO.ar;
				airportNamesDO.en = airportsDO.en;
				airportNamesDO.name = "(" + airportNamesDO.code + ") " + airportsDO.ar;
				airportsDO.name = airportNamesDO.name;
				AppConstants.allAirportNamesDO.vecAirport.add(airportNamesDO);
			}
		} else if (checkLangFrench()) {
			for (AirportsDO airportsDO : AppConstants.allAirportsDO.vecAirport) {
				AirportNamesDO airportNamesDO = new AirportNamesDO();
				airportNamesDO.code = airportsDO.code;
				airportNamesDO.fr = airportsDO.fr;
				airportNamesDO.en = airportsDO.en;
				airportNamesDO.name = airportsDO.fr + " (" + airportNamesDO.code + ")";
				airportsDO.name = airportNamesDO.name;
				AppConstants.allAirportNamesDO.vecAirport.add(airportNamesDO);
			}
		} else {
			for (AirportsDO airportsDO : AppConstants.allAirportsDO.vecAirport) {
				AirportNamesDO airportNamesDO = new AirportNamesDO();
				airportNamesDO.code = airportsDO.code;
				airportNamesDO.en = airportsDO.en;
				airportNamesDO.name = airportsDO.en + " (" + airportNamesDO.code + ")";
				airportsDO.name = airportNamesDO.name;
				AppConstants.allAirportNamesDO.vecAirport.add(airportNamesDO);
			}
		}

		// AppConstants.allAirportsDO.vecAirport.add(0, null);
	}

	@Override
	public void dataRetreived(Response data) {

		if (data != null && !data.isError) {
			switch (data.method) {
			case AIR_PNR:
				airBookDO = (AirBookDO) data.data;

				/*
				 * //------------------------ Sorting airBookDo Vector on the
				 * basis of Departure time ----------------------//
				 * 
				 * //
				 * Collections.sort(airBookDO.vecOriginDestinationOptionDOs.get(
				 * 0).vecFlightSegmentDOs, new DateComparator());
				 * Collections.sort(airBookDO.vecOriginDestinationOptionDOs, new
				 * vecOriginDestOptDo());
				 * 
				 * 
				 * ///----------------------------------------------------------
				 * --------------------------------------------//
				 * 
				 */
				if (airBookDO != null) {
					if (!airBookDO.errorMessage.equalsIgnoreCase("")) {
						hideLoader();
						showCustomDialog(ManageYourBookingActivity.this, getString(R.string.Alert),
								getString(R.string.InvalidPNRCB), getString(R.string.Ok), "", "");
					} else {
						if (airBookDO.bookingID.equalsIgnoreCase("") || airBookDO.ticketingStatus.equals("6")) {
							hideLoader();
							showCustomDialog(ManageYourBookingActivity.this, getString(R.string.Alert),
									getString(R.string.InvalidPNRCB), getString(R.string.Ok), "", "");
						} else {
							if (airBookDO.contactInfo != null && !airBookDO.contactInfo.contactlastname
									.equalsIgnoreCase(edt_lastnameinmanagebooking.getText().toString().trim())) {
								hideLoader();
								showCustomDialog(ManageYourBookingActivity.this, getString(R.string.Alert),
										getString(R.string.LastNameMismatch), getString(R.string.Ok), "", "");
							}
							/*
							 * else if (airBookDO.vecOriginDestinationOptionDOs
							 * != null &&
							 * airBookDO.vecOriginDestinationOptionDOs.size() >
							 * 0 &&
							 * (airBookDO.vecOriginDestinationOptionDOs.get(0).
							 * vecFlightSegmentDOs != null &&
							 * airBookDO.vecOriginDestinationOptionDOs.get(0).
							 * vecFlightSegmentDOs.size() > 0 &&
							 * !airBookDO.vecOriginDestinationOptionDOs.get(0).
							 * vecFlightSegmentDOs.get(0).departureDateTime
							 * .split("T")[0].equalsIgnoreCase(CalendarUtility
							 * .getBookingDate(calendar).split("T")[0]) &&
							 * (airBookDO.vecOriginDestinationOptionDOs.get(1).
							 * vecFlightSegmentDOs != null &&
							 * airBookDO.vecOriginDestinationOptionDOs.get(1).
							 * vecFlightSegmentDOs.size() > 0 &&
							 * !airBookDO.vecOriginDestinationOptionDOs.get(1).
							 * vecFlightSegmentDOs.get(0).departureDateTime
							 * .split("T")[0].equalsIgnoreCase(CalendarUtility
							 * .getBookingDate(calendar).split("T")[0])))) {
							 * hideLoader();
							 * showCustomDialog(ManageYourBookingActivity.this,
							 * getString(R.string.Alert),
							 * getString(R.string.DepartureDateMismatch),
							 * getString(R.string.Ok), "", ""); }
							 */

							else if (airBookDO.vecOriginDestinationOptionDOs != null
									&& airBookDO.vecOriginDestinationOptionDOs.size() > 0)

							{
								for (int i = 0; i < airBookDO.vecOriginDestinationOptionDOs.size(); i++) {
									if (airBookDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs != null
											&& airBookDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs
													.size() > 0
											&& !airBookDO.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs
													.get(0).departureDateTime.split("T")[0].equalsIgnoreCase(
															CalendarUtility.getBookingDate(calendar).split("T")[0])) {

										if (i == airBookDO.vecOriginDestinationOptionDOs.size() - 1) {
											hideLoader();
											showCustomDialog(ManageYourBookingActivity.this, getString(R.string.Alert),
													getString(R.string.DepartureDateMismatch), getString(R.string.Ok),
													"", "");
											break;
										}

									} else {
										break;
									}

								}

							}

							if ((airBookDO.contactInfo != null && airBookDO.contactInfo.contactlastname
									.equalsIgnoreCase(edt_lastnameinmanagebooking.getText().toString()))
									&& (airBookDO.vecOriginDestinationOptionDOs != null
											&& airBookDO.vecOriginDestinationOptionDOs.size() > 0
											&& (airBookDO.vecOriginDestinationOptionDOs
													.get(0).vecFlightSegmentDOs != null
													&& airBookDO.vecOriginDestinationOptionDOs
															.get(0).vecFlightSegmentDOs.size() > 0
													&& airBookDO.vecOriginDestinationOptionDOs
															.get(0).vecFlightSegmentDOs.get(0).departureDateTime
																	.split("T")[0].equalsIgnoreCase(
																			CalendarUtility.getBookingDate(calendar)
																					.split("T")[0]))
											|| (airBookDO.vecOriginDestinationOptionDOs.size() > 1
													&& airBookDO.vecOriginDestinationOptionDOs
															.get(1).vecFlightSegmentDOs != null
													&& airBookDO.vecOriginDestinationOptionDOs
															.get(1).vecFlightSegmentDOs.size() > 0
													&& airBookDO.vecOriginDestinationOptionDOs
															.get(1).vecFlightSegmentDOs.get(0).departureDateTime
																	.split("T")[0].equalsIgnoreCase(
																			CalendarUtility.getBookingDate(calendar)
																					.split("T")[0])))) {
								// if(AppConstants.allAirportNamesDO != null &&
								// AppConstants.allAirportNamesDO.vecAirport !=
								// null &&
								// AppConstants.allAirportNamesDO.vecAirport.size()
								// > 0)
								// {
								movetoNextActivity(airBookDO);
								// }
								// else
								hideLoader();
							}

							else {
								hideLoader();
								showCustomDialog(ManageYourBookingActivity.this, getString(R.string.Alert),
										getString(R.string.noData), getString(R.string.Ok), "", "");
							}
						}
					}
				} else {
					hideLoader();
					showCustomDialog(ManageYourBookingActivity.this, getString(R.string.Alert),
							getString(R.string.noData), getString(R.string.Ok), "", "");
				}

				break;
			case AIR_PORT_SDATA:
				AppConstants.allAirportsDO = new AllAirportsDO();
				AppConstants.allAirportsDO = (AllAirportsDO) data.data;

				updateAllAirPortsNames();
				movetoNextActivity(airBookDO);
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
					showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.noData),
							getString(R.string.Ok), "", "");
			} else
				showCustomDialog(ManageYourBookingActivity.this, getString(R.string.Alert), getString(R.string.noData),
						getString(R.string.Ok), "", "");
		}
	}

	private void movetoNextActivity(AirBookDO airBookDO) {
		if (!airBookDO.bookingID.equalsIgnoreCase("")) {
			Intent in = new Intent(ManageYourBookingActivity.this, FindBooking.class);
			in.putExtra(AppConstants.AIR_BOOK, airBookDO);
			in.putExtra("MANAGE_BOOKING", true);
			startActivity(in);
		}
		hideLoader();
	}

	@Override
	public void onButtonYesClick(String from) {
		super.onButtonYesClick(from);
		if (from.equalsIgnoreCase(AppConstants.INTERNET_PROBLEM))
			clickHome();
	}
}
