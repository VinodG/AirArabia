package com.winit.airarabia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import com.google.android.gms.analytics.HitBuilders;
import com.insider.android.insider.Insider;
import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.insider.GcmBroadcastReceiver;
import com.winit.airarabia.objects.AirportNamesDO;
import com.winit.airarabia.objects.AirportsDO;
import com.winit.airarabia.objects.AirportsDestDO;
import com.winit.airarabia.objects.AllAirportNamesDO;
import com.winit.airarabia.objects.AllAirportsDO;
import com.winit.airarabia.objects.AllAirportsMainDO;
import com.winit.airarabia.objects.BookFlightDO;
import com.winit.airarabia.objects.BookingFlightDO;
import com.winit.airarabia.objects.CurrencyDo;
import com.winit.airarabia.objects.OriginDestinationInformationDO;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.utils.QuickSortCurrencyDo;
import com.winit.airarabia.utils.SharedPrefUtils;
import com.winit.airarabia.utils.StringUtils;
import com.winit.airarabia.webaccess.Response;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class BookFlightActivity extends BaseActivity implements DataListener {
	private LinearLayout llBookflight, llcairo;
	private ImageView ivAdultPlus, ivAdultMinus, ivChildPlus, ivChildMinus, ivInfantPlus, ivInfantMinus;
	private Button btnOneway, btnReturn;
	private LinearLayout llonewaycal, llreturncal;
	private TextView tvAdultCount, tvChildCount, tvInfantCount, tvDate, tvReturnDate, tvClass;
	private TextView tv_from, tv_to, tv_departing, tv_returning, tv_passenger, tv_currency;
	private Intent in;
	private int adultcount = 1, childcount = 0, infantcount = 0, totalcount = 1;
	private Calendar calendarArrival;
	private static Calendar calendarDeparture;
	private static Calendar cal;
	private boolean isManageBook = false;
	private ItemNameDestComparator itemNameDestComparator;
	private ImageView calSeprator;
	private TextView tvAirportDept, tvAirportArr, tvCurrencyType, tv_submit;
	private TextView adultTag, childTag, infantTag;
	private int DEPARTURE_RESULT_CODE = 3000;
	private int ARRIVAL_RESULT_CODE = 4000;
	private int CURRENCY_RESULT_CODE = 5000;
	private int CLASS_RESULT_CODE = 6000;
	private String date;

	private AirportsDO airportsOriginDo;
	private AirportsDestDO airportsDestDo;
	private String selectedCurrencyCode;

	private LinearLayout llSubmit;

	private BookFlightActivity.BCR bcr;

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
	protected void onResume() {
		super.onResume();
		AppConstants.fareType = "";
		AppConstants.Cookie = "";
		if(!AppConstants.classType.equalsIgnoreCase("C"))
			AppConstants.classType = "Y";
		// AppConstants.CurrencyCodeActual = "";
		if (AppConstants.bookingFlightDO != null) {
			AppConstants.bookingFlightDO.bundleServiceID = "";
			AppConstants.bookingFlightDO.isFlexiOut = false;
			AppConstants.bookingFlightDO.isFlexiIn = false;
		}
//		 tracker.setScreenName("Booking Flight"); 
//		 tracker.send(new HitBuilders.ScreenViewBuilder().build());
		 
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
		// AppConstants.isCameFromBookFlightForLocation = false;
	}

	@Override
	public void initilize() {
		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		intentFilter.addAction(AppConstants.BOOK_SUCCESS);
		registerReceiver(bcr, intentFilter);

		tvHeaderTitle.setText(getString(R.string.BookFlighta));
		btnSubmitNext.setVisibility(View.GONE);

		if (AppConstants.bookingFlightDO != null
				&& AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify != null
				&& AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.size() > 0)
			isManageBook = true;

		llBookflight = (LinearLayout) layoutInflater.inflate(R.layout.bookflight, null);

		llcairo = (LinearLayout) llBookflight.findViewById(R.id.llcairo);
		AppConstants.IsTwoWay = true;
		btnOneway = (Button) llBookflight.findViewById(R.id.btn_oneway);
		btnReturn = (Button) llBookflight.findViewById(R.id.btn_return);
		llonewaycal = (LinearLayout) llBookflight.findViewById(R.id.llonewaycal);
		llreturncal = (LinearLayout) llBookflight.findViewById(R.id.llreturncal);
		ivAdultPlus = (ImageView) llBookflight.findViewById(R.id.ivAdultplus);
		ivAdultMinus = (ImageView) llBookflight.findViewById(R.id.ivAdultminus);
		ivChildPlus = (ImageView) llBookflight.findViewById(R.id.ivChildplus);
		ivChildMinus = (ImageView) llBookflight.findViewById(R.id.ivChildminus);
		ivInfantPlus = (ImageView) llBookflight.findViewById(R.id.ivInfantplus);
		ivInfantMinus = (ImageView) llBookflight.findViewById(R.id.ivInfantminus);
		tvAdultCount = (TextView) llBookflight.findViewById(R.id.tv_adultmiddleinput);
		tvChildCount = (TextView) llBookflight.findViewById(R.id.tv_childmiddleinput);
		tvInfantCount = (TextView) llBookflight.findViewById(R.id.tv_infantmiddleinput);
		tv_submit = (TextView) llBookflight.findViewById(R.id.tv_submit);
		tvDate = (TextView) llBookflight.findViewById(R.id.tv_date);
		tvReturnDate = (TextView) llBookflight.findViewById(R.id.tv_returndate);
		tvClass = (TextView) llBookflight.findViewById(R.id.tvClass);

		llMiddleBase.addView(llBookflight, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		calSeprator = (ImageView) llBookflight.findViewById(R.id.calSeprator);

		tvAirportDept = (TextView) llBookflight.findViewById(R.id.tvAirportDept);
		tvAirportArr = (TextView) llBookflight.findViewById(R.id.tvAirportArr);
		tvCurrencyType = (TextView) llBookflight.findViewById(R.id.tvCurrencyType);
		adultTag = (TextView) llBookflight.findViewById(R.id.adultTag);
		childTag = (TextView) llBookflight.findViewById(R.id.childTag);
		infantTag = (TextView) llBookflight.findViewById(R.id.infantTag);

		llSubmit = (LinearLayout) llBookflight.findViewById(R.id.llSubmit);
		tv_from = (TextView) llBookflight.findViewById(R.id.tv_from);
		tv_to = (TextView) llBookflight.findViewById(R.id.tv_to);
		tv_departing = (TextView) llBookflight.findViewById(R.id.tv_departing);
		tv_returning = (TextView) llBookflight.findViewById(R.id.tv_returning);
		tv_passenger = (TextView) llBookflight.findViewById(R.id.tv_passenger);
		tv_currency = (TextView) llBookflight.findViewById(R.id.tv_currency);

		tvDate.setTextColor(getResources().getColor(R.color.cal_color));
		tvReturnDate.setTextColor(getResources().getColor(R.color.cal_color));
		setTypefaceOpenSansRegular(llBookflight);
		setTypeFaceSemiBold(llSubmit);
		tvHeaderTitle.setTypeface(typefaceOpenSansSemiBold);
		btnBack.setTypeface(typefaceOpenSansSemiBold);
		AppConstants.isCameFromBookFlightForLocation = false;

		// adultTag.setTypeface(typefaceOpenSansSemiBold);
		// tvAdultCount.setTypeface(typefaceOpenSansSemiBold);
		// findLocation();\

		if (!isManageBook) {
			AppConstants.bookingFlightDO = new BookingFlightDO();
			calendarArrival = null;
			calendarDeparture = null;
		} else {
			calendarArrival = Calendar.getInstance();
			calendarDeparture = Calendar.getInstance();
		}
		itemNameDestComparator = new ItemNameDestComparator();

		AppConstants.classType = "Y";
		tvAirportDept.setTypeface(typeFaceOpenSansLight);
		tvAirportArr.setTypeface(typeFaceOpenSansLight);
		// tvAdultCount.setTypeface(typeFaceOpenSansLight);
		tvChildCount.setTypeface(typeFaceOpenSansLight);
		tvInfantCount.setTypeface(typeFaceOpenSansLight);
		tvDate.setTypeface(typeFaceOpenSansLight);
		tvReturnDate.setTypeface(typeFaceOpenSansLight);
		btnReturn.setTypeface(typefaceOpenSansRegular);
		btnOneway.setTypeface(typefaceOpenSansRegular);
		tv_submit.setTypeface(typefaceOpenSansSemiBold);
		tvCurrencyType.setTypeface(typeFaceOpenSansLight);
		// tvCurrencyType.setTypeface(typefaceOpenSansSemiBold);
		tvClass.setTypeface(typefaceOpenSansSemiBold);
		tv_from.setTypeface(typefaceOpenSansRegular);
		tv_to.setTypeface(typefaceOpenSansRegular);
		tv_departing.setTypeface(typefaceOpenSansRegular);
		tv_returning.setTypeface(typefaceOpenSansRegular);
		tv_passenger.setTypeface(typefaceOpenSansRegular);
		tv_currency.setTypeface(typefaceOpenSansRegular);

		tvAdultCount.setTypeface(typeFaceOpenSansLight);
		if (setCurrencyPrefrences()) {
		} else {
			if (AppConstants.country != null && !AppConstants.country.equals("")) {
				String id = CalendarUtility.getCurrencyCodeBasedonLocation(AppConstants.country);
				if (id != null && !id.equals("")) {
					tvCurrencyType.setText(id);
					tvCurrencyType.setTypeface(typefaceOpenSansSemiBold);
					tvCurrencyType.setTextColor(getResources().getColor(R.color.text_color));
					// tv_currency.setText(getString(R.string.currency));
				} else {

					tvCurrencyType.setText(getString(R.string.selectCurrency));

				}

			} else {
				tvCurrencyType.setText(getString(R.string.selectCurrency));

			}

		}

		Insider insider = new Insider();
		insider.openSession(this, AppConstants.ProjectName);
		insider.registerInBackground(this, AppConstants.GoogleProjectNumber);
		Insider.setLandingActivity(GcmBroadcastReceiver.class, this);
		Insider.setNotificationIcon(R.drawable.ic_launcher, this);
	}

	@Override
	public void bindingControl() {

		/*
		 * updateAllAirPortsNames(); updateAirPortNames(); updateSpinner();
		 */

		if (AppConstants.allAirportsDO != null && AppConstants.allAirportsDO.vecAirport != null
				&& AppConstants.allAirportsDO.vecAirport.size() > 0 && AppConstants.allAirportNamesDO != null
				&& AppConstants.allAirportNamesDO.vecAirport != null
				&& AppConstants.allAirportNamesDO.vecAirport.size() > 0) {
			showLoader("");
			new Thread(new Runnable() {

				@Override
				public void run() {
					updateAllAirPortsNames();
					updateAirPortNames();
					updateSpinner();
					runOnUiThread(new Runnable() {
						public void run() {
							hideLoader();
						}
					});

				}
			}).start();
		} else
			callFlightListService();

		if (isManageBook) {
			// need to check here .
			// String departureAirport =
			// getAirportNameByCode(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).departureAirportCode.toString());
			// String arrivalAirport =
			// getAirportNameByCode(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).arrivalAirportCode.toString());
			if (AppConstants.bookingFlightDO.myBookFlightDOReturn != null) {
				// AppConstants.bookingFlightDO.myBookFlightDOReturn.originLocationCode
				// = departureAirport;
				// AppConstants.bookingFlightDO.myBookFlightDOReturn.destinationLocationCode
				// = arrivalAirport;
				// AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime
				// =
				// AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).departureDateTime.toString();
				adultcount = AppConstants.bookingFlightDO.myBookFlightDOReturn.adtQty;
				childcount = AppConstants.bookingFlightDO.myBookFlightDOReturn.chdQty;
				infantcount = AppConstants.bookingFlightDO.myBookFlightDOReturn.infQty;

				btnReturn.setVisibility(View.VISIBLE);
				btnOneway.setVisibility(View.INVISIBLE);
				calendarDeparture = CalendarUtility
						.getCalendarFromDate(AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime);
			} else {
				// AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode
				// = departureAirport;
				// AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode
				// = arrivalAirport;
				// AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime
				// =
				// AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).arrivalAirportCode.toString();

				adultcount = AppConstants.bookingFlightDO.myBookFlightDO.adtQty;
				childcount = AppConstants.bookingFlightDO.myBookFlightDO.chdQty;
				infantcount = AppConstants.bookingFlightDO.myBookFlightDO.infQty;

				btnReturn.setVisibility(View.INVISIBLE);
				// btnOneway.setBackground(resources.getDrawable(R.drawable.button_red));
				btnOneway.setBackgroundResource(R.drawable.button_red);
				calendarDeparture = CalendarUtility
						.getCalendarFromDate(AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime);
			}
			String originCode="", destCode="", origin_full="", dest_full="";
			origin_full=AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).comment;
			if(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.size()>1)
				dest_full=AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(1).vecFlightSegmentDOs.get(0).comment;
			if(AppConstants.bookingFlightDO.isOutBoundClick)
			{
				if(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.size()==2)
				{
					originCode=origin_full.split("=")[1].split(",")[0]+" (";
					originCode=originCode.concat(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).departureAirportCode)+")";
//					originCode=AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).departureAirportCode;
					destCode= dest_full.split(",")[1].split("=")[1]+" (";
					destCode=destCode.concat(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(1).vecFlightSegmentDOs.get(0).arrivalAirportCode)+")";
//					destCode= AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(1).vecFlightSegmentDOs.get(0).arrivalAirportCode;
				}
				else
				{
					
					originCode=origin_full.split("=")[1].split(",")[0]+" (";
					originCode=originCode.concat(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).departureAirportCode)+")";
//					originCode=AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).departureAirportCode;
					destCode= origin_full.split(",")[1].split("=")[1]+" (";
					destCode=destCode.concat(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).arrivalAirportCode)+")";
//					destCode= AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).arrivalAirportCode;
				}
				calendarDeparture = CalendarUtility
						.getCalendarFromDate(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).departureDateTime);				
				date = AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).departureDateTime;
			}
			else if(AppConstants.bookingFlightDO.isInBoundClick)
			{
				btnReturn.setText(getString(R.string.Return));
				btnReturn.setTextColor(getResources().getColor(R.color.white));
				if(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.size()==2)
				{
					originCode=dest_full.split("=")[1].split(",")[0]+" (";
					originCode=originCode.concat(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(1).vecFlightSegmentDOs.get(0).departureAirportCode)+")";
//					originCode=AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(1).vecFlightSegmentDOs.get(0).departureAirportCode;
					destCode=origin_full.split(",")[1].split("=")[1]+" (";
					destCode=destCode.concat(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).arrivalAirportCode)+")";
//					destCode= AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).arrivalAirportCode;
				}
				else
				{
					originCode=origin_full.split("=")[1].split(",")[0]+" (";
					originCode=originCode.concat(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).departureAirportCode)+")";
//					originCode=AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).departureAirportCode;
					destCode= origin_full.split(",")[1].split("=")[1]+" (";
					destCode=destCode.concat(AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).arrivalAirportCode)+")";
					
//					originCode=AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).departureAirportCode;
//					destCode= AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.get(0).vecFlightSegmentDOs.get(0).arrivalAirportCode;
				}
				calendarDeparture = CalendarUtility
						.getCalendarFromDate(AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime);
				date = AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime;
			}
			AppConstants.DATE = date;
			tvAirportDept.setText(originCode);
			tvAirportArr.setText(destCode);
			tvAirportDept.setTextColor(getResources().getColor(R.color.text_color));
			tvAirportArr.setTextColor(getResources().getColor(R.color.text_color));
			tvAirportDept.setTypeface(typefaceOpenSansSemiBold);
			tvAirportArr.setTypeface(typefaceOpenSansSemiBold);
			tvDate.setText("" + calendarDeparture.get(Calendar.DAY_OF_MONTH) + " "
					+ getFirstLetterCapital(CalendarUtility.getMonthMMM(calendarDeparture.get(Calendar.MONTH)))
					+ ", " + CalendarUtility.getYear(calendarDeparture.get(Calendar.YEAR)));
			tvDate.setTextColor(getResources().getColor(R.color.text_color));
			// tvDate.setTypeface(typefaceOpenSansRegular);
			tvDate.setTypeface(typefaceOpenSansSemiBold);	
			tvCurrencyType.setText(AppConstants.CurrencyCodeAfterExchange+"");
			tvCurrencyType.setTypeface(typefaceOpenSansSemiBold);
			
			totalcount = adultcount + childcount + infantcount;

			// if(!TextUtils.isEmpty(departureAirport))
			// tvAirportDept.setText(departureAirport);
			// if(!TextUtils.isEmpty(arrivalAirport))
			// tvAirportArr.setText(arrivalAirport);

			tvAdultCount.setText(adultcount + "");
			tvChildCount.setText(childcount + "");
			tvInfantCount.setText(infantcount + "");

			if (childcount > 0) {
				tvChildCount.setTextColor(getResources().getColor(R.color.cal_color));
				childTag.setTextColor(getResources().getColor(R.color.cal_color));
				// tvChildCount.setTypeface(typefaceOpenSansSemiBold);
			} else {
				tvChildCount.setTextColor(getResources().getColor(R.color.cal_color));
				childTag.setTextColor(getResources().getColor(R.color.cal_color));
				tvChildCount.setTypeface(typeFaceOpenSansLight);
				childTag.setTypeface(typeFaceOpenSansLight);
			}
			if (infantcount > 0) {
				tvInfantCount.setTextColor(getResources().getColor(R.color.cal_color));
				infantTag.setTextColor(getResources().getColor(R.color.cal_color));
				// tvInfantCount.setTypeface(typefaceOpenSansSemiBold);
				// infantTag.setTypeface(typefaceOpenSansSemiBold);
			} else {
				tvInfantCount.setTextColor(getResources().getColor(R.color.cal_color));
				infantTag.setTextColor(getResources().getColor(R.color.cal_color));
				tvInfantCount.setTypeface(typeFaceOpenSansLight);
				// infantTag.setTypeface(typefaceOpenSansSemiBold);
			}
			
			btnOneway.setEnabled(false);
			btnReturn.setEnabled(false);
			ivAdultPlus.setEnabled(false);
			ivAdultMinus.setEnabled(false);
			ivChildPlus.setEnabled(false);
			ivChildMinus.setEnabled(false);
			ivInfantPlus.setEnabled(false);
			ivInfantMinus.setEnabled(false);
			tvAirportArr.setEnabled(false);
			tvAirportDept.setEnabled(false);
			tvCurrencyType.setEnabled(false);

			tvAirportArr.setClickable(false);
			tvAirportDept.setClickable(false);
			btnOneway.setClickable(false);
			btnReturn.setClickable(false);
			ivAdultPlus.setClickable(false);
			ivAdultMinus.setClickable(false);
			ivChildPlus.setClickable(false);
			ivChildMinus.setClickable(false);
			ivInfantPlus.setClickable(false);
			ivInfantMinus.setClickable(false);
			tvCurrencyType.setClickable(false);

			btnOneway.setTextColor(resources.getColor(R.color.white));
//			btnReturn.setTextColor(resources.getColor(R.color.text_color_red));

			btnOneway.setVisibility(View.VISIBLE);

			llreturncal.setVisibility(View.INVISIBLE);

			llonewaycal.setClickable(true);
			llreturncal.setClickable(false);

			tvDate.setTextColor(getResources().getColor(R.color.cal_color));
			// tvDate.setTypeface(typeFaceOpenSansLight);
			tvReturnDate.setTextColor(getResources().getColor(R.color.text_color));
			// tvReturnDate.setTypeface(typeFaceOpenSansLight);
		} else {
			adultcount = 1;
			childcount = 0;
			infantcount = 0;

			tvAdultCount.setText(StringUtils.getString(adultcount));
			tvChildCount.setText(StringUtils.getString(childcount));
			tvInfantCount.setText(StringUtils.getString(infantcount));

			tvChildCount.setTextColor(getResources().getColor(R.color.cal_color));
			childTag.setTextColor(getResources().getColor(R.color.cal_color));
			tvInfantCount.setTextColor(getResources().getColor(R.color.cal_color));
			infantTag.setTextColor(getResources().getColor(R.color.cal_color));

			// updateArrivalCal(calendarDeparture);
		}

		btnOneway.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Insider.tagEvent(AppConstants.OneWayButton, BookFlightActivity.this);
				//Insider.Instance.tagEvent(BookFlightActivity.this,AppConstants.OneWayButton);
				trackEvent("Book Flight", AppConstants.OneWayButton, "");
				AppConstants.IsTwoWay = false;
				showOneWayView();
				AppConstants.bookingFlightDO.myBookFlightDOReturn = null;
				AppConstants.bookingFlightDO.myODIDOReturn = null;

				// tvReturnDate.setText(getString(R.string.selectDate));
			}
		});

		btnReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Insider.tagEvent(AppConstants.ReturnButton, BookFlightActivity.this);
				//Insider.Instance.tagEvent(BookFlightActivity.this,AppConstants.ReturnButton);
				trackEvent("Book Flight", AppConstants.ReturnButton, "");
				AppConstants.IsTwoWay = true;
				showReturnView();
				AppConstants.bookingFlightDO.myBookFlightDOReturn = new BookFlightDO();
				AppConstants.bookingFlightDO.myODIDOReturn = new OriginDestinationInformationDO();
			}
		});

		/// ========================================================================================================================////
		ivAdultPlus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Insider.tagEvent(AppConstants.AddAdult, BookFlightActivity.this);
				//Insider.Instance.tagEvent(BookFlightActivity.this,AppConstants.AddAdult);
				trackEvent("Book Flight", AppConstants.AddAdult, "");
				if (adultcount < AppConstants.MAX_PERSON && totalcount < AppConstants.MAX_PERSON) {
					adultcount++;
					totalcount++;
					tvAdultCount.setText("" + adultcount);
				} else {
					showCustomDialog(BookFlightActivity.this, getString(R.string.Alert),
							getString(R.string.AdultChildCountErrorMessage), getString(R.string.Ok), "", "");
				}

				// tvAdultCount.setTextColor(getResources().getColor(R.color.text_color));
				tvAdultCount.setTypeface(typeFaceOpenSansLight);
			}
		});

		ivAdultMinus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Insider.tagEvent(AppConstants.RemoveAdult, BookFlightActivity.this);
				//Insider.Instance.tagEvent(BookFlightActivity.this,AppConstants.RemoveAdult);
				trackEvent("Book Flight", AppConstants.RemoveAdult, "");
				if (adultcount > AppConstants.MIN_ADULT) {
					if (infantcount == adultcount) {
						infantcount--;
					}
					adultcount--;
					totalcount--;
					tvAdultCount.setText("" + adultcount);
					tvInfantCount.setText("" + infantcount);
					// if(adultcount==0)
					// tvAdultCount.setTypeface(typefaceOpenSansSemiBold);
					// tvAdultCount.setTextColor(getResources().getColor(R.color.text_color));
				}
			}
		});

		ivChildPlus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Insider.tagEvent(AppConstants.AddChild, BookFlightActivity.this);
				//Insider.Instance.tagEvent(BookFlightActivity.this,AppConstants.AddChild);
				trackEvent("Book Flight", AppConstants.AddChild, "");
				if (totalcount < AppConstants.MAX_PERSON) {
					childcount++;
					totalcount++;
					tvChildCount.setText("" + childcount);
				} else {
					showCustomDialog(BookFlightActivity.this, getString(R.string.Alert),
							getString(R.string.AdultChildCountErrorMessage), getString(R.string.Ok), "", "");
				}
				if (childcount > 0) {
					// tvChildCount.setTextColor(getResources().getColor(R.color.text_color));
					// childTag.setTextColor(getResources().getColor(R.color.text_color));
					// tvChildCount.setTypeface(typefaceOpenSansSemiBold);
					// childTag.setTypeface(typefaceOpenSansSemiBold);

				} else {
					// tvChildCount.setTextColor(getResources().getColor(R.color.text_color_gray));
					// childTag.setTextColor(getResources().getColor(R.color.text_color_gray));
					tvChildCount.setTypeface(typeFaceOpenSansLight);
					childTag.setTypeface(typeFaceOpenSansLight);
				}
			}
		});

		ivChildMinus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Insider.tagEvent(AppConstants.RemoveChild, BookFlightActivity.this);
				//Insider.Instance.tagEvent(BookFlightActivity.this,AppConstants.RemoveChild);
				trackEvent("Book Flight", AppConstants.RemoveChild, "");
				if (childcount > AppConstants.MIN_CHILD) {
					childcount--;
					totalcount--;
					tvChildCount.setText("" + childcount);
				}
				// if (childcount>0) {
				// tvChildCount.setTypeface(typefaceOpenSansSemiBold);
				// childTag.setTypeface(typefaceOpenSansSemiBold);
				// }
				// else
				{
					tvChildCount.setTypeface(typeFaceOpenSansLight);
					childTag.setTypeface(typeFaceOpenSansLight);
				}
			}
		});

		ivInfantPlus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Insider.tagEvent(AppConstants.AddInfant, BookFlightActivity.this);
				//Insider.Instance.tagEvent(BookFlightActivity.this,AppConstants.AddInfant);
				trackEvent("Book Flight", AppConstants.AddInfant, "");
				if (infantcount < adultcount) {
					infantcount++;
					tvInfantCount.setText("" + infantcount);
				} else {
					showCustomDialog(BookFlightActivity.this,
							""/* getString(R.string.Alert) */, getString(R.string.AdultChildCountErrorMessage),
							getString(R.string.Ok), "", "");
				}
				// if (infantcount>0) {
				// tvInfantCount.setTypeface(typefaceOpenSansSemiBold);
				// infantTag.setTypeface(typefaceOpenSansSemiBold);
				// }
				// else
				{
					tvInfantCount.setTypeface(typeFaceOpenSansLight);
					infantTag.setTypeface(typeFaceOpenSansLight);
				}
			}
		});

		ivInfantMinus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Insider.tagEvent(AppConstants.RemoveInfant, BookFlightActivity.this);
				//Insider.Instance.tagEvent(BookFlightActivity.this,AppConstants.RemoveInfant);
				trackEvent("Book Flight", AppConstants.RemoveInfant, "");
				if (infantcount > AppConstants.MIN_INFANT) {
					infantcount--;
					tvInfantCount.setText("" + infantcount);
				}
				// if (infantcount>0) {
				// tvInfantCount.setTypeface(typefaceOpenSansSemiBold);
				// infantTag.setTypeface(typefaceOpenSansSemiBold);
				// }
				else {
					tvInfantCount.setTypeface(typeFaceOpenSansLight);
					infantTag.setTypeface(typeFaceOpenSansLight);
				}
			}

		});

		llonewaycal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				moveToSelectDateActivityOneWay();
			}
		});

		llreturncal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!(tvDate.getText().toString().equalsIgnoreCase(getString(R.string.selectDate)))) {
					moveToSelectDateActivityReturn();
				} else
					showCustomDialog(BookFlightActivity.this,
							""/* getString(R.string.Alert) */, getString(R.string.DepartureDateErr),
							getString(R.string.Ok), "", "");

			}
		});

		llSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				moveToNextActivity();
			}
		});

		tvAirportDept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Insider.tagEvent(AppConstants.DestinationButton, BookFlightActivity.this);
				//Insider.Instance.tagEvent(BookFlightActivity.this,AppConstants.DestinationButton);
				trackEvent("Book Flight", AppConstants.DestinationButton, "");
				Intent i = new Intent(BookFlightActivity.this, SelectAirport_new.class);
				// Intent i = new
				// Intent(BookFlightActivity.this,SelectAirport.class);
				AppConstants.isCameFromBookFlightForLocation = false;
				startActivityForResult(i, DEPARTURE_RESULT_CODE);
			}
		});
		tvAirportArr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Insider.tagEvent(AppConstants.SorceButton, BookFlightActivity.this);
				//Insider.Instance.tagEvent(BookFlightActivity.this,AppConstants.SorceButton);
				trackEvent("Book Flight", AppConstants.SorceButton, "");

				if (tvAirportDept != null && !tvAirportDept.getText().toString().equalsIgnoreCase("")) {
					Intent i = new Intent(BookFlightActivity.this, SelectAirport_new.class);
					i.putExtra("Arrival_Object", airportsOriginDo);
					AppConstants.isCameFromBookFlightForLocation = false;
					startActivityForResult(i, ARRIVAL_RESULT_CODE);
				} else
					showCustomDialog(getApplicationContext(),
							""/* getString(R.string.Alert) */, getString(R.string.DepartureAirportErr),
							getString(R.string.Ok), null, "BookFlight_Depart");
			}
		});
		
		tvCurrencyType.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Insider.tagEvent(AppConstants.CurrencyButton, BookFlightActivity.this);
				//Insider.Instance.tagEvent(BookFlightActivity.this,AppConstants.CurrencyButton);
				trackEvent("Book Flight", AppConstants.CurrencyButton, "");
				Intent i = new Intent(BookFlightActivity.this, SelectCurrency.class);
				i.putExtra("selected_currency", tvCurrencyType.getText().toString());
				startActivityForResult(i, CURRENCY_RESULT_CODE);
			}
		});

		tvClass.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Insider.tagEvent("Class_button_clicked", BookFlightActivity.this);
				//Insider.Instance.tagEvent(BookFlightActivity.this,"Class_button_clicked");
				trackEvent("Book Flight", "Class_button_clicked", "");
				Intent i = new Intent(BookFlightActivity.this, SelectClassType.class);
				if (!TextUtils.isEmpty(tvClass.getText().toString()))
					i.putExtra("classNameToCompare", tvClass.getText().toString());
				startActivityForResult(i, CLASS_RESULT_CODE);
			}

		});
	}

	protected void moveToSelectDateActivityOneWay() {

		// if (AppConstants.bookingFlightDO.myBookFlightDOReturn == null) {
		// in = new Intent(BookFlightActivity.this,
		// SelectDateActivityOneWay.class);
		// in.putExtra(AppConstants.RETURN, false);
		// in.putExtra(AppConstants.SEL_DATE, calendarDeparture);
		// startActivityForResult(in, 1000);
		// overridePendingTransition(R.anim.bottom_top_popup_share,
		// R.anim.top_bottom_popup_share);
		// }
		// else
		// {
		// in = new Intent(BookFlightActivity.this,
		// SelectDateActivityNew.class);
		// in.putExtra(AppConstants.RETURN, false);
		// in.putExtra(AppConstants.SEL_DATE, calendarDeparture);
		// in.putExtra(AppConstants.SEL_DATE_ARR, calendarArrival);
		// startActivityForResult(in, 1000);
		// overridePendingTransition(R.anim.bottom_top_popup_share,
		// R.anim.top_bottom_popup_share);
		// }
		Insider.tagEvent(AppConstants.DateButton, BookFlightActivity.this);
		//Insider.Instance.tagEvent(BookFlightActivity.this,AppConstants.DateButton);

		trackEvent("Book Flight", AppConstants.DateButton, "");
		/*if (calendarDeparture == null) {
			calendarDeparture = Calendar.getInstance();
		}*/
		Intent intent1 = new Intent(BookFlightActivity.this, SelectDateActivityOneWay.class);
		intent1.putExtra(AppConstants.RETURN, false);
		intent1.putExtra(AppConstants.SEL_DATE_DEP, calendarDeparture);
		startActivityForResult(intent1, 1000);
		overridePendingTransition(R.anim.bottom_top_popup_share, R.anim.top_bottom_popup_share);
	}

	protected void moveToSelectDateActivityReturn() {
		// in = new Intent(BookFlightActivity.this,
		// SelectDateActivityNew.class);
		// if(isManageBook && AppConstants.bookingFlightDO.myBookFlightDOReturn
		// != null)
		// {
		// in.putExtra(AppConstants.RETURN, false);
		// in.putExtra(AppConstants.SEL_DATE, calendarDeparture);
		// in.putExtra(AppConstants.SEL_DATE_ARR, calendarArrival);
		// startActivityForResult(in, 1000);
		// overridePendingTransition(R.anim.bottom_top_popup_share,
		// R.anim.top_bottom_popup_share);
		// }
		// else if(!tvDate.getText().toString().equalsIgnoreCase(""))
		// {
		//
		// in.putExtra(AppConstants.RETURN, true);
		// in.putExtra(AppConstants.SEL_DATE, calendarDeparture);
		// in.putExtra(AppConstants.SEL_DATE_ARR, calendarArrival);
		// startActivityForResult(in, 1000);
		// overridePendingTransition(R.anim.bottom_top_popup_share,
		// R.anim.top_bottom_popup_share);
		// }
		// else
		// showCustomDialog(getApplicationContext(),
		// ""/*getString(R.string.Alert)*/,
		// getString(R.string.DepartureDateErr), getString(R.string.Ok), null,
		// "return_date");

		Insider.tagEvent(AppConstants.DateButton, BookFlightActivity.this);
		//Insider.Instance.tagEvent(BookFlightActivity.this,AppConstants.DateButton);
		trackEvent("Book Flight", AppConstants.DateButton, "");
		if (calendarDeparture == null) {
			calendarDeparture = Calendar.getInstance();
		}

		if (calendarArrival == null)
			calendarArrival = Calendar.getInstance();

		if (!tvDate.getText().toString().equalsIgnoreCase("")) {
			in = new Intent(BookFlightActivity.this, SelectDateActivityOneWay.class);
			if (isManageBook)
				in.putExtra(AppConstants.RETURN, false);
			else
				in.putExtra(AppConstants.RETURN, true);
			in.putExtra(AppConstants.SEL_DATE_ARR, calendarArrival);
			in.putExtra(AppConstants.SEL_DATE_DEP, calendarDeparture);
			startActivityForResult(in, 2000);
			overridePendingTransition(R.anim.bottom_top_popup_share, R.anim.top_bottom_popup_share);

		} else
			showCustomDialog(getApplicationContext(),
					""/* getString(R.string.Alert) */, getString(R.string.DepartureDateErr), getString(R.string.Ok),
					null, "return_date");
	}

	protected void moveToSelectFlightActivity() {
		Insider.tagEvent(AppConstants.SearchFlightButton, BookFlightActivity.this);
		//Insider.Instance.tagEvent(BookFlightActivity.this,AppConstants.SearchFlightButton);
		trackEvent("Book Flight", AppConstants.SearchFlightButton, "");
		if (AppConstants.bookingFlightDO.myBookFlightDOReturn == null) {
			Intent inSelectFlight = new Intent(BookFlightActivity.this, SelectFlightActivityNew.class);
			inSelectFlight.putExtra(AppConstants.SELECT_FLIGHT_CAL, calendarDeparture);
			inSelectFlight.putExtra("SELECT_FLIGHT_CAL", calendarDeparture);
			startActivity(inSelectFlight);
		} else if (AppConstants.bookingFlightDO.myBookFlightDOReturn != null) {
			Intent inSelectFlight = new Intent(BookFlightActivity.this, SelectFlightActivityNew.class);
			// Intent inSelectFlight = new Intent(BookFlightActivity.this,
			// ReturnFlightActivityNew.class);
			inSelectFlight.putExtra(AppConstants.SELECT_FLIGHT_CAL, calendarDeparture);
			inSelectFlight.putExtra("SELECT_FLIGHT_CAL", calendarDeparture);
			startActivity(inSelectFlight);
		}

	}

	private void showOneWayView() {
		llreturncal.setVisibility(View.INVISIBLE);
		calSeprator.setVisibility(View.INVISIBLE);
		btnOneway.setTextColor(resources.getColor(R.color.white));
		// btnOneway.setBackground(resources.getDrawable(R.drawable.button_red));
		btnOneway.setBackgroundResource(R.drawable.button_red);
		btnReturn.setTextColor(resources.getColor(R.color.text_color_red));
		btnReturn.setBackgroundResource(0);
	}

	private void showReturnView() {
		llreturncal.setVisibility(View.VISIBLE);
		calSeprator.setVisibility(View.VISIBLE);
		btnReturn.setTextColor(resources.getColor(R.color.white));
		btnReturn.setBackgroundResource(R.drawable.button_red);
		btnOneway.setTextColor(resources.getColor(R.color.red));
		btnOneway.setBackgroundResource(0);
	}

	private void updateOneWayData() {
		if (AppConstants.bookingFlightDO.myBookFlightDO != null) {
			AppConstants.bookingFlightDO.myBookFlightDO.travelType = AppConstants.TRAVEL_TYPE_ONEWAY;
			AppConstants.bookingFlightDO.myBookFlightDO.adtQty = StringUtils.getInt(tvAdultCount.getText().toString());
			AppConstants.bookingFlightDO.myBookFlightDO.chdQty = StringUtils.getInt(tvChildCount.getText().toString());
			AppConstants.bookingFlightDO.myBookFlightDO.infQty = StringUtils.getInt(tvInfantCount.getText().toString());
			AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime = CalendarUtility
					.getBookingDate(calendarDeparture);
		}
	}

	private void updateReturnData() {
		if (!isManageBook)
			AppConstants.bookingFlightDO.myBookFlightDOReturn = new BookFlightDO();
		if (AppConstants.bookingFlightDO.myBookFlightDOReturn != null) {
			AppConstants.bookingFlightDO.myBookFlightDOReturn.srvUrlType = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
			AppConstants.bookingFlightDO.myBookFlightDOReturn.travelType = AppConstants.TRAVEL_TYPE_RETURN;
			AppConstants.bookingFlightDO.myBookFlightDOReturn.adtQty = StringUtils
					.getInt(tvAdultCount.getText().toString());
			AppConstants.bookingFlightDO.myBookFlightDOReturn.chdQty = StringUtils
					.getInt(tvChildCount.getText().toString());
			AppConstants.bookingFlightDO.myBookFlightDOReturn.infQty = StringUtils
					.getInt(tvInfantCount.getText().toString());
			AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime = CalendarUtility
					.getBookingDate(calendarArrival);
			// AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTimeReturn
			// = CalendarUtility.getBookingDate(calendarArrival);
		}

		if (!isManageBook) {
			AppConstants.bookingFlightDO.myBookFlightDOReturn.destinationLocationCode = AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode;
			AppConstants.bookingFlightDO.myBookFlightDOReturn.originLocationCode = AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode;
		}
	}

	private boolean validateTravel() {
		if (AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode.equalsIgnoreCase("")) {
			showCustomDialog(this, ""/* getString(R.string.Alert) */, getString(R.string.DepartureAirportErr),
					getString(R.string.Ok), "", "");
			return false;
		} else if (AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode.equalsIgnoreCase("")) {
			showCustomDialog(this, ""/* getString(R.string.Alert) */, getString(R.string.DestinationErrorMessage),
					getString(R.string.Ok), "", "");
			return false;
		} else if (tvDate.getText().toString().trim().length() <= 0) {
			showCustomDialog(this, ""/* getString(R.string.Alert) */, getString(R.string.DepartureDateErr),
					getString(R.string.Ok), "", "");
			return false;
		} else if (AppConstants.bookingFlightDO.myBookFlightDOReturn != null
				&& tvReturnDate.getText().toString().trim().length() <= 0) {
			showCustomDialog(this, ""/* getString(R.string.Alert) */, getString(R.string.ArrivalDateErr),
					getString(R.string.Ok), "", "");
			return false;
		} else
			return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1000 && resultCode == RESULT_OK) {
			if (data.hasExtra("selDate")) {
				calendarDeparture = (Calendar) data.getExtras().getSerializable("selDate");
				updateDepartureView();
				if (isManageBook) {

				} else {
					if (calendarArrival != null && calendarDeparture != null && isValidDateArr()) {

					} else if (calendarArrival != null && calendarDeparture != null && !isValidDateArr()) {
						calendarArrival = null;
						// updateArrivalCal(calendarDeparture);
						if (tvReturnDate != null
								&& !tvReturnDate.getText().toString().equalsIgnoreCase(getString(R.string.selectDate)))
							updateArrivalView();
					}
				}
			}
			// if(data.hasExtra("selDate"))
			// {
			// calendarArrival = (Calendar)
			// data.getExtras().getSerializable("selDate");
			//
			//
			// if(calendarArrival != null)
			// {
			// int i = calendarDeparture.compareTo(calendarArrival);
			// if(i <= 0) {
			// AppConstants.ArrivalTime =
			// CalendarUtility.getBookingDate(calendarArrival);
			// updateArrivalView();
			// }
			// else
			// showCustomDialog(getApplicationContext(),
			// getString(R.string.Alert),
			// getString(R.string.ArrivalDateErr_Incorrect),
			// getString(R.string.Ok), "", "");
			// }
			// else{
			// tvReturnDate.setText(null);
			//// showCustomDialog(getApplicationContext(),getString(R.string.Alert),
			// getString(R.string.ArrivalDateErr), getString(R.string.Ok), "",
			// "");
			// }
			// }
		} else if (requestCode == 2000 && resultCode == RESULT_OK) {
			if (data.hasExtra("selDate")) {
				calendarArrival = (Calendar) data.getExtras().getSerializable("selDate");

				if (calendarArrival != null) {
					int i = calendarDeparture.compareTo(calendarArrival);
					if (i <= 0) {
						AppConstants.ArrivalTime = CalendarUtility.getBookingDate(calendarArrival);
						updateArrivalView();
					} else
						showCustomDialog(getApplicationContext(),
								""/* getString(R.string.Alert) */, getString(R.string.ArrivalDateErr_Incorrect),
								getString(R.string.Ok), "", "");
				} else
					showCustomDialog(getApplicationContext(),
							""/* getString(R.string.Alert) */, getString(R.string.ArrivalDateErr),
							getString(R.string.Ok), "", "");
			}
		} else if (requestCode == DEPARTURE_RESULT_CODE && resultCode == RESULT_OK) {
			airportsOriginDo = (AirportsDO) data.getSerializableExtra("Departure_Airport");

			if (airportsOriginDo.en.contains("Cairo"))
				llcairo.setVisibility(View.VISIBLE);
			else
				llcairo.setVisibility(View.GONE);

			itemNameDestComparator = new ItemNameDestComparator();
			Collections.sort(airportsOriginDo.destList, itemNameDestComparator);

			tvAirportDept.setText(airportsOriginDo.name);
			// tvAirportDept.setTextColor(getResources().getColor(R.color.text_color));
			tvAirportDept.setTextColor(getResources().getColor(R.color.text_color));
			tvAirportDept.setTypeface(typefaceOpenSansSemiBold);
			tvAirportArr.setTypeface(typeFaceOpenSansLight);
			AppConstants.OriginLocation = airportsOriginDo.code;
			AppConstants.OriginLocationName = airportsOriginDo.en;

			/*if (airportsOriginDo.name.toString().contains("Alexandria")
					|| airportsOriginDo.code.toString().contains("HBE")
					|| airportsOriginDo.name.toString().contains("Amman")
					|| airportsOriginDo.code.toString().contains("AMM")) {
				tvCurrencyType.setText("EGP");
				tvCurrencyType.setTextColor(getResources().getColor(R.color.text_color));
				tvCurrencyType.setTypeface(typefaceOpenSansSemiBold);
			}*/

			updateDepartureObject();
		} else if (requestCode == ARRIVAL_RESULT_CODE && resultCode == RESULT_OK) {
			airportsDestDo = (AirportsDestDO) data.getSerializableExtra("Arrival_Airport");

			if(!airportsOriginDo.en.contains("Cairo")) {
				if (airportsDestDo.en.contains("Cairo"))
					llcairo.setVisibility(View.VISIBLE);
				else
					llcairo.setVisibility(View.GONE);
			}

			tvAirportArr.setText(airportsDestDo.name);
			tvAirportArr.setTextColor(getResources().getColor(R.color.text_color));

			AppConstants.DestinationLocation = airportsDestDo.code;
			AppConstants.DestinationLocationName = airportsDestDo.en;

			tvAirportArr.setTypeface(typefaceOpenSansSemiBold);
			// tvAirportArr.setTextColor(getResources().getColor(R.color.cal_color));

			/*if (airportsDestDo.name.toString().contains("Alexandria") || airportsDestDo.code.toString().contains("HBE")
					|| airportsDestDo.name.toString().contains("Amman")
					|| airportsDestDo.code.toString().contains("AMM")) {
				tvCurrencyType.setText("EGP");
				tvCurrencyType.setTextColor(getResources().getColor(R.color.text_color));
				tvCurrencyType.setTypeface(typefaceOpenSansSemiBold);
			}*/
			updateArrivalObject();
		} else if (requestCode == CURRENCY_RESULT_CODE && resultCode == RESULT_OK) {
			if (data.hasExtra("Currency_Selected")) {
				selectedCurrencyCode = (String) data.getSerializableExtra("Currency_Selected");
				tvCurrencyType.setText(selectedCurrencyCode.toString());
				tv_currency.setText(getString(R.string.currency));
				tvCurrencyType.setTextColor(getResources().getColor(R.color.text_color));
				tvCurrencyType.setTypeface(typefaceOpenSansSemiBold);

			}
		} else if (requestCode == CLASS_RESULT_CODE && resultCode == RESULT_OK) {
			if (data.hasExtra("Currency_Selected")) {
				String strClassType = (String) data.getSerializableExtra("Currency_Selected");
				if (strClassType.equalsIgnoreCase(getString(R.string.business)))
					AppConstants.classType = "C";
				else
					AppConstants.classType = "Y";
				tvClass.setText(strClassType);
				tvClass.setTypeface(typefaceOpenSansSemiBold);
				tvClass.setTextColor(getResources().getColor(R.color.cal_color));

			}
		}
	}

	private void updateDepartureView() {
		if (calendarDeparture != null) {
			// String year = "";
			// if (Locale.getDefault().equals(
			// new Locale(AppConstants.LANG_LOCAL_AR))) {
			// Locale.setDefault(new Locale(AppConstants.LANG_LOCAL_EN));
			// year =
			// CalendarUtility.getYear(calendarDeparture.get(Calendar.YEAR));
			// Locale.setDefault(new Locale(AppConstants.LANG_LOCAL_AR));
			// } else {
			// year =
			// CalendarUtility.getYear(calendarDeparture.get(Calendar.YEAR));
			// }
			if (checkLangFrench()) {
				tvDate.setText("" + calendarDeparture.get(Calendar.DAY_OF_MONTH) + " "
						+ getFirstLetterCapital(CalendarUtility.getMonthMMM(calendarDeparture.get(Calendar.MONTH)))
								.toString().replace(".", "")
						+ ", " + CalendarUtility.getYear(calendarDeparture.get(Calendar.YEAR)));
			} else
				tvDate.setText("" + calendarDeparture.get(Calendar.DAY_OF_MONTH) + " "
						+ getFirstLetterCapital(CalendarUtility.getMonthMMM(calendarDeparture.get(Calendar.MONTH)))
						+ ", " + CalendarUtility.getYear(calendarDeparture.get(Calendar.YEAR)));
			tvDate.setTextColor(getResources().getColor(R.color.text_color));
			// tvDate.setTypeface(typefaceOpenSansRegular);
			tvDate.setTypeface(typefaceOpenSansSemiBold);

		} else {
			{
				tvDate.setText(getString(R.string.selectDate));
				tvDate.setTypeface(typeFaceOpenSansLight);
			}

		}
	}

	private void updateArrivalView() {
		if (calendarArrival != null) {
			// String year = "";
			// if (Locale.getDefault().equals(
			// new Locale(AppConstants.LANG_LOCAL_AR))) {
			// Locale.setDefault(new Locale(AppConstants.LANG_LOCAL_EN));
			// year =
			// CalendarUtility.getYear(calendarArrival.get(Calendar.YEAR));
			// Locale.setDefault(new Locale(AppConstants.LANG_LOCAL_AR));
			// } else {
			// year =
			// CalendarUtility.getYear(calendarArrival.get(Calendar.YEAR));
			// }
			if (checkLangFrench()) {
				tvReturnDate.setText("" + calendarArrival.get(Calendar.DAY_OF_MONTH) + " "
						+ getFirstLetterCapital(CalendarUtility.getMonthMMM(calendarArrival.get(Calendar.MONTH)))
								.toString().replace(".", "")
						+ ", " + CalendarUtility.getYear(calendarArrival.get(Calendar.YEAR)));
			} else
				tvReturnDate.setText("" + calendarArrival.get(Calendar.DAY_OF_MONTH) + " "
						+ getFirstLetterCapital(CalendarUtility.getMonthMMM(calendarArrival.get(Calendar.MONTH))) + ", "
						+ CalendarUtility.getYear(calendarArrival.get(Calendar.YEAR)));
			tvReturnDate.setTextColor(getResources().getColor(R.color.cal_color));
			tvReturnDate.setTypeface(typefaceOpenSansRegular);
			tvReturnDate.setTypeface(typefaceOpenSansSemiBold);
		} else {
			tvReturnDate.setText(null);
			tvReturnDate.setTypeface(typeFaceOpenSansLight);
		}
	}

	private void callFlightListService() {
		showLoader("");
		if (new CommonBL(this, this).getAirportsData()) {
		} else {
			hideLoader();
			showCustomDialog(getApplicationContext(),
					"" /* getString(R.string.Alert) */, getString(R.string.InternetProblem), getString(R.string.Ok), "",
					AppConstants.INTERNET_PROBLEM);
		}
	}

	@Override
	public void dataRetreived(Response data) {
		if (data != null && !data.isError) {
			switch (data.method) {
			case AIR_PORT_SDATA:

				AppConstants.allAirportsDO = new AllAirportsDO();
				AppConstants.allAirportNamesDO = new AllAirportNamesDO();

				AllAirportsMainDO allAirportsMainDO = new AllAirportsMainDO();
				allAirportsMainDO = (AllAirportsMainDO) data.data;
				AppConstants.allAirportsDO = allAirportsMainDO.airportParserDO;
				AppConstants.allAirportNamesDO = allAirportsMainDO.allAirportNamesDO;
				AppConstants.arrListCurrencies = (ArrayList<CurrencyDo>) allAirportsMainDO.arlCurrencies.clone();
				new QuickSortCurrencyDo().sortAirPorts(AppConstants.allAirportsDO.vecAirport);
				new QuickSortCurrencyDo().sortAirPortNames(AppConstants.allAirportNamesDO.vecAirport);
				new QuickSortCurrencyDo().sort(AppConstants.arrListCurrencies);

				updateAllAirPortsNames();
				updateAirPortNames();
				updateSpinner();
				hideLoader();
				break;

			case CURRENCY_CONVERSION_SERVICE:
				if (!(((String) data.data).toString()).equalsIgnoreCase("0")
						&& !(((String) data.data).toString()).equalsIgnoreCase("-1")) {
					AppConstants.currencyConversionFactor = StringUtils.getDouble(data.data + "");
					moveToSelectFlightActivity();
				} else {
					String str = "";
					if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("3O")) {
						str = "EUR";
					} else {
						str = getCurrencyCode(AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType);
					}
					showCustomDialog(BookFlightActivity.this, getString(R.string.Alert),
							getString(R.string.currency_exchange_not_avail) + " "
									+ AppConstants.CurrencyCodeAfterExchange + "."
									+ getString(R.string.please_select_another_currency) + " " + str + ".",
							getString(R.string.Ok), null, "");

					// AppConstants.currencyConversionFactor =
					// StringUtils.getDouble(1+"");
				}
				// moveToSelectFlightActivity();
				hideLoader();
				break;
			default:
				break;
			}
		}

		else {
			hideLoader();
			if (data.data instanceof String) {
				if (((String) data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
					showCustomDialog(getApplicationContext(),
							""/* getString(R.string.Alert) */, getString(R.string.ConnenectivityTimeOutExpMsg),
							getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
				else
					showCustomDialog(getApplicationContext(),
							""/* getString(R.string.Alert) */, getString(R.string.TechProblem), getString(R.string.Ok),
							"", AppConstants.INTERNET_PROBLEM);
			} else
				showCustomDialog(getApplicationContext(),
						""/* getString(R.string.Alert) */, getString(R.string.TechProblem), getString(R.string.Ok), "",
						AppConstants.INTERNET_PROBLEM);
		}
	}

	private class ItemNameDestComparator implements Comparator<AirportsDestDO> {
		public int compare(AirportsDestDO o1, AirportsDestDO o2) {
			if (o2 != null)
				return o1.en.compareTo(o2.en);
			else
				return o1.en.compareTo("");
		}
	}

	@Override
	public void onButtonYesClick(String from) {
		super.onButtonYesClick(from);
		if (from.equalsIgnoreCase(AppConstants.INTERNET_PROBLEM)) {
			finish();
		}
	}

	private void updateAirPortNames() {
		for (int i = 0; i < AppConstants.allAirportsDO.vecAirport.size(); i++) {
			if (AppConstants.allAirportsDO.vecAirport.get(i).destList != null
					&& AppConstants.allAirportsDO.vecAirport.get(i).destList.size() > 0)
				for (int j = 0; j < AppConstants.allAirportsDO.vecAirport.get(i).destList.size(); j++) {
					for (int j2 = 0; j2 < AppConstants.allAirportNamesDO.vecAirport.size(); j2++) {
						if (AppConstants.allAirportNamesDO.vecAirport.get(j2).code
								.equalsIgnoreCase(AppConstants.allAirportsDO.vecAirport.get(i).destList.get(j).code))
							AppConstants.allAirportsDO.vecAirport.get(i).destList
									.get(j).name = AppConstants.allAirportNamesDO.vecAirport.get(j2).name;
					}
				}
		}
	}

	private void updateAllAirPortsNames() {
		AirportsDO airportsDO;
		AirportNamesDO airportNamesDO;
		if (AppConstants.allAirportsDO != null && AppConstants.allAirportsDO.vecAirport != null
				&& AppConstants.allAirportsDO.vecAirport.size() > 0
				&& (AppConstants.allAirportsDO.vecAirport.get(0) == null
						|| AppConstants.allAirportsDO.vecAirport.get(0).code.isEmpty())) {
			AppConstants.allAirportsDO.vecAirport.remove(0);
		}
		if (AppConstants.allAirportsDO != null && AppConstants.allAirportsDO.vecAirport != null
				&& AppConstants.allAirportsDO.vecAirport.size() > 0) {
			if (checkLangArabic()) {
				for (int i = 0; i < AppConstants.allAirportsDO.vecAirport.size(); i++) {
					airportsDO = AppConstants.allAirportsDO.vecAirport.get(i);
					airportNamesDO = AppConstants.allAirportNamesDO.vecAirport.get(i);
					if (airportsDO != null && airportsDO.code != null) {
						airportNamesDO.name = "(" + airportNamesDO.code + ") " + airportsDO.ar;
						airportsDO.name = airportNamesDO.name;
					}
				}
			} else if (checkLangFrench()) {
				for (int i = 0; i < AppConstants.allAirportsDO.vecAirport.size(); i++) {
					airportsDO = AppConstants.allAirportsDO.vecAirport.get(i);
					airportNamesDO = AppConstants.allAirportNamesDO.vecAirport.get(i);
					if (airportsDO != null && airportsDO.code != null) {
						airportNamesDO.name = airportsDO.fr + " (" + airportNamesDO.code + ")";
						airportsDO.name = airportNamesDO.name;
					}
				}
			} else {
				for (int i = 0; i < AppConstants.allAirportsDO.vecAirport.size(); i++) {
					airportsDO = AppConstants.allAirportsDO.vecAirport.get(i);
					airportNamesDO = AppConstants.allAirportNamesDO.vecAirport.get(i);
					if (airportsDO != null && airportsDO.code != null) {
						airportNamesDO.name = airportsDO.en + " (" + airportNamesDO.code + ")";
						airportsDO.name = airportNamesDO.name;
					}
				}
			}
		}
	}

	private void updateSpinner() {
		if (isManageBook) {
			if (AppConstants.bookingFlightDO.myBookFlightDOReturn == null) {
				for (int i = 1; i < AppConstants.allAirportsDO.vecAirport.size(); i++) {
					if (AppConstants.allAirportsDO.vecAirport.get(i).code
							.equalsIgnoreCase(AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode)) {
						airportsOriginDo = AppConstants.allAirportsDO.vecAirport.get(i);
					}
				}			
				
			} else {
				for (int i = 1; i < AppConstants.allAirportsDO.vecAirport.size(); i++) {
					if (AppConstants.allAirportsDO.vecAirport.get(i).code
							.equalsIgnoreCase(AppConstants.bookingFlightDO.myBookFlightDOReturn.originLocationCode)) {
						airportsOriginDo = AppConstants.allAirportsDO.vecAirport.get(i);
					}
				}
			}
		} else {
			airportsOriginDo = AppConstants.allAirportsDO.vecAirport.get(0);
		}
	}

	private void updateDepartureObject() {
		if (airportsOriginDo != null) {
			if (isManageBook && AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
				AppConstants.bookingFlightDO.myBookFlightDOReturn.originLocationCode = airportsOriginDo.code;
			else
				AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode = airportsOriginDo.code;

			tvAirportArr.setText(getString(R.string.select_u_destination));
			// tvAirportArr.setTextColor(getResources().getColor(R.color.text_color_gray));
			updateArrivalObject();
			// tvAirportDept.setTypeface(typefaceOpenSansSemiBold);
			// tvAirportDept.setTextColor(getResources().getColor(R.color.cal_color));
		} else {
			if (AppConstants.bookingFlightDO.myBookFlightDO != null)
				AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode = "";
			if (AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
				AppConstants.bookingFlightDO.myBookFlightDOReturn.originLocationCode = "";
		}
	}

	private void updateArrivalObject() {
		if (airportsDestDo != null) {
			if (isManageBook && AppConstants.bookingFlightDO.myBookFlightDOReturn != null) {
				AppConstants.bookingFlightDO.myBookFlightDOReturn.destinationLocationCode = airportsDestDo.code;
				AppConstants.bookingFlightDO.myBookFlightDOReturn.srvUrlType = airportsDestDo.service_type1;
			} else {
				AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode = airportsDestDo.code;
				AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType = airportsDestDo.service_type1;
			}
		} else {
			if (AppConstants.bookingFlightDO.myBookFlightDO != null)
				AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode = "";
			if (AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
				AppConstants.bookingFlightDO.myBookFlightDOReturn.destinationLocationCode = "";
		}
	}

	private void callServiceCurrencyExchange(String fromCurrency, String toCurrency) {
		showLoader("");
		if (new CommonBL(BookFlightActivity.this, BookFlightActivity.this).getCurrencyFactor(fromCurrency,
				toCurrency)) {

		} else {
			hideLoader();
			showCustomDialog(this, ""/* getString(R.string.Alert) */, getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	private void moveToNextActivity() {
		if (isManageBook) {
			if (AppConstants.bookingFlightDO.myBookFlightDOReturn != null) {
				AppConstants.bookingFlightDO.myBookFlightDO = AppConstants.bookingFlightDO.myBookFlightDOReturn;
				AppConstants.bookingFlightDO.myODIDOOneWay = AppConstants.bookingFlightDO.myODIDOReturn;
				AppConstants.bookingFlightDO.myBookFlightDOReturn = null;
				AppConstants.bookingFlightDO.myODIDOReturn = null;
			}
			updateOneWayData();
		} else if (llreturncal.isShown()) {
			if (!(tvDate.getText().toString().trim().length() <= 0)) {
				updateOneWayData();
			} else
				showCustomDialog(this, ""/* getString(R.string.Alert) */, getString(R.string.DepartureDateErr),
						getString(R.string.Ok), "", "");

			if (!(AppConstants.bookingFlightDO.myBookFlightDOReturn != null
					&& tvReturnDate.getText().toString().trim().length() <= 0)) {
				// if(calendarArrival != null){
				updateReturnData();
				// }else
				// {
				// showCustomDialog(this, ""/*getString(R.string.Alert)*/,
				// getString(R.string.ArrivalDateErr), getString(R.string.Ok),
				// "", "");
				// }
			} else {
				showCustomDialog(this, ""/* getString(R.string.Alert) */, getString(R.string.ArrivalDateErr),
						getString(R.string.Ok), "", "");
			}
		} else {
			if (!(tvDate.getText().toString().trim().length() <= 0)) {
				updateOneWayData();
			} else
				showCustomDialog(this, ""/* getString(R.string.Alert) */, getString(R.string.DepartureDateErr),
						getString(R.string.Ok), "", "");
		}

		if (validateTravel()) {
			AppConstants.CurrencyCodeAfterExchange = tvCurrencyType.getText().toString();

			if(!isManageBook)
			{
			synchronized (STORAGE_SERVICE) {
				if (!TextUtils.isEmpty(tvAirportDept.getText().toString()))
					saveRecentAirportsSrcInPref(airportsOriginDo.code + "");
				// saveRecentAirportsSrcInPref(tvAirportDept.getText().toString());

				if (!TextUtils.isEmpty(tvAirportArr.getText().toString()))
					saveRecentAirportsDestInPref(airportsDestDo.code);
				// saveRecentAirportsDestInPref(tvAirportArr.getText().toString());
			}
			}

			/*
			 * Toast.makeText(BookFlightActivity.this,
			 * AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType,
			 * Toast.LENGTH_LONG).show();
			 */
			// if(tvCurrencyType.getText().toString().equalsIgnoreCase("AED"))
			callServiceCurrencyExchange(getCurrencyCode(AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType),
					tvCurrencyType.getText().toString());
			// else
			// {
			// AppConstants.currencyConversionFactor = 1.0;
			// moveToSelectFlightActivity();
			// }
		}
	}

	// ======================================newly added for
	// currency==============================================

	private boolean setCurrencyPrefrences() {
		boolean check = false;
		selectedCurrencyCode = mPrefs.getString("selectedCurrency", null);
		if (selectedCurrencyCode != null && !selectedCurrencyCode.equalsIgnoreCase("")) {
			check = true;
			tvCurrencyType.setText(selectedCurrencyCode + "");
			tvCurrencyType.setTypeface(typefaceOpenSansSemiBold);
			tvCurrencyType.setTextColor(getResources().getColor(R.color.text_color));

		}
		return check;
	}

	private void saveRecentAirportsSrcInPref(String newAirportName) {
		ArrayList<String> arrListRecentAirports = new ArrayList<String>();
		arrListRecentAirports = (ArrayList<String>) SharedPrefUtils
				.loadArrayFromPreference(SharedPreferenceStrings.RECENT_AIRPORTS_SRC, getApplicationContext());
		if (arrListRecentAirports != null) {
			if (arrListRecentAirports.size() > 0 && arrListRecentAirports.size() == 5
					&& !arrListRecentAirports.contains(newAirportName))
				arrListRecentAirports.remove(0);
			if (!arrListRecentAirports.contains(newAirportName)) {
				arrListRecentAirports.add(newAirportName);
				SharedPrefUtils.saveArrayInPreference(arrListRecentAirports,
						SharedPreferenceStrings.RECENT_AIRPORTS_SRC, getApplicationContext());
			}
		}
	}

	private void saveRecentAirportsDestInPref(String newAirportName) {
		ArrayList<String> arrListRecentAirports = new ArrayList<String>();
		arrListRecentAirports = (ArrayList<String>) SharedPrefUtils
				.loadArrayFromPreference(SharedPreferenceStrings.RECENT_AIRPORTS_DEST, getApplicationContext());
		if (arrListRecentAirports != null) {
			if (arrListRecentAirports.size() > 0 && arrListRecentAirports.size() == 5
					&& !arrListRecentAirports.contains(newAirportName))
				arrListRecentAirports.remove(0);
			if (!arrListRecentAirports.contains(newAirportName)) {
				arrListRecentAirports.add(newAirportName);
				SharedPrefUtils.saveArrayInPreference(arrListRecentAirports,
						SharedPreferenceStrings.RECENT_AIRPORTS_DEST, getApplicationContext());
			}
		}
	}

	private boolean isValidDateArr() {
		if (calendarDeparture.get(Calendar.YEAR) < calendarArrival.get(Calendar.YEAR))
			return true;
		else if (calendarDeparture.get(Calendar.MONTH) < calendarArrival.get(Calendar.MONTH)
				&& calendarDeparture.get(Calendar.YEAR) == calendarArrival.get(Calendar.YEAR))
			return true;
		else if (calendarDeparture.get(Calendar.DATE) <= calendarArrival.get(Calendar.DATE)
				&& calendarDeparture.get(Calendar.MONTH) == calendarArrival.get(Calendar.MONTH)
				&& calendarDeparture.get(Calendar.YEAR) == calendarArrival.get(Calendar.YEAR))
			return true;
		else
			return false;
	}

	public String getAirportNameByCode(String airportCode) {
		String airportName = "";
		if (AppConstants.allAirportNamesDO != null && AppConstants.allAirportNamesDO.vecAirport != null
				&& AppConstants.allAirportNamesDO.vecAirport.size() > 0) {
			for (int i = 0; i < AppConstants.allAirportNamesDO.vecAirport.size(); i++) {
				if (AppConstants.allAirportNamesDO.vecAirport.get(i).code.equalsIgnoreCase(airportCode)) {
					airportName = AppConstants.allAirportNamesDO.vecAirport.get(i).name.toString();
					break;
				}
			}
		}
		return airportName;
	}

}