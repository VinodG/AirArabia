package com.winit.airarabia;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirScheduleDO;
import com.winit.airarabia.objects.AirportNamesDO;
import com.winit.airarabia.objects.AirportsDO;
import com.winit.airarabia.objects.AirportsDestDO;
import com.winit.airarabia.objects.AllAirportNamesDO;
import com.winit.airarabia.objects.AllAirportsDO;
import com.winit.airarabia.objects.AllAirportsMainDO;
import com.winit.airarabia.objects.BookFlightDO;
import com.winit.airarabia.objects.BookingFlightDO;
import com.winit.airarabia.objects.RequestParameterDO;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.webaccess.Response;

public class FlightScheduleActivity extends BaseActivity implements DataListener, OnClickListener {
	private LinearLayout llFlightSchedule;
	private TextView tv_selectdate, tv_returnSelectedDate, btn_search;
	private Button btn_roundtrip, btn_oneWay;
	private LinearLayout ll_returnSelectedDate, llSubmit;
	private RequestParameterDO requestParameterDO;
	private AirScheduleDO airScheduleDOto, airScheduleDOreturn;
	private Calendar calendar, calendar1;
	private boolean roundtrip = true;
	private boolean firstHit = false;
	private FlightScheduleActivity.BCR bcr;
	private ImageView img_seperator;
	private TextView tvAirportFrom, tvAirportTo;
	private AirportsDO airportsOriginDo;
	private AirportsDestDO airportsDestDo;
	private int DEPARTURE_RESULT_CODE = 3000;
	private int ARRIVAL_RESULT_CODE = 4000;

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

	@SuppressLint("InlinedApi")
	@Override
	public void initilize() {
		tvHeaderTitle.setText(getString(R.string.TimeTable));

		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		registerReceiver(bcr, intentFilter);

		llFlightSchedule = (LinearLayout) layoutInflater.inflate(R.layout.flightschedule, null);
		llMiddleBase.addView(llFlightSchedule, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// spinner_From = (Spinner)
		// llFlightSchedule.findViewById(R.id.spinner_From);
		// spinner_To = (Spinner)
		// llFlightSchedule.findViewById(R.id.spinner_To);
		tv_selectdate = (TextView) llFlightSchedule.findViewById(R.id.tv_selectdate);
		tv_returnSelectedDate = (TextView) llFlightSchedule.findViewById(R.id.tv_returnSelectedDate);
		/*
		 * btn_searchinflightschedule = (Button)
		 * llFlightSchedule.findViewById(R.id.btn_searchinflightschedule);
		 */
		btn_roundtrip = (Button) llFlightSchedule.findViewById(R.id.btn_roundtrip);
		btn_oneWay = (Button) llFlightSchedule.findViewById(R.id.btn_oneWay);
		ll_returnSelectedDate = (LinearLayout) llFlightSchedule.findViewById(R.id.ll_returnSelectedDate);
		llSubmit = (LinearLayout) llFlightSchedule.findViewById(R.id.llSubmit);
		img_seperator = (ImageView) llFlightSchedule.findViewById(R.id.sep_flightSchedule_Return);
		btn_search = (TextView) llFlightSchedule.findViewById(R.id.btn_search);

		tvAirportFrom = (TextView) llFlightSchedule.findViewById(R.id.tvAirportFrom);
		tvAirportTo = (TextView) llFlightSchedule.findViewById(R.id.tvAirportTo);

		setTypefaceOpenSansRegular(llFlightSchedule);

		AppConstants.bookingFlightDO = new BookingFlightDO();
		// calendar = Calendar.getInstance();
		// calendar1 = Calendar.getInstance();

		btnSubmitNext.setVisibility(View.GONE);
		btnSubmitNext.setText(R.string.Search);

		tvAirportFrom.setTypeface(typeFaceOpenSansLight);
		tvAirportTo.setTypeface(typeFaceOpenSansLight);
		// tvAdultCount.setTypeface(typeFaceOpenSansLight);
		tv_selectdate.setTypeface(typeFaceOpenSansLight);
		tv_returnSelectedDate.setTypeface(typeFaceOpenSansLight);
		btn_roundtrip.setTypeface(typefaceOpenSansSemiBold);
		btn_oneWay.setTypeface(typefaceOpenSansSemiBold);
		btn_search.setTypeface(typefaceOpenSansSemiBold);
		btnSubmitNext.setTypeface(typefaceOpenSansSemiBold);
		btnBack.setTypeface(typefaceOpenSansSemiBold);
		tvHeaderTitle.setTypeface(typefaceOpenSansSemiBold);
	}

	@Override
	public void bindingControl() {
		if (AppConstants.allAirportsDO != null && AppConstants.allAirportsDO.vecAirport != null
				&& AppConstants.allAirportsDO.vecAirport.size() > 0 && AppConstants.allAirportNamesDO != null
				&& AppConstants.allAirportNamesDO.vecAirport != null
				&& AppConstants.allAirportNamesDO.vecAirport.size() > 0) {
			showLoader("");
			updateAirPortNames();
			updateSpinner();
		} else
			callFlightListService();

		tv_selectdate.setOnClickListener(this);
		tv_returnSelectedDate.setOnClickListener(this);
		llSubmit.setOnClickListener(this);
		btn_roundtrip.setOnClickListener(this);
		btn_oneWay.setOnClickListener(this);
		// tv_selectdate.setTag("");
		// tv_returnSelectedDate.setTag("");
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_selectdate:
			trackEvent("Timetable Screen",AppConstants.SelectDepartingDateTimetable, "");
			Intent in = new Intent(FlightScheduleActivity.this, SelectDateActivityOneWay.class);
			in.putExtra(AppConstants.RETURN, false);
			in.putExtra(AppConstants.SEL_DATE_DEP, calendar);
			// overridePendingTransition(R.anim.bottom_top_popup_share,R.anim.top_bottom_popup_share);
			startActivityForResult(in, 1000);

			break;

		case R.id.tv_returnSelectedDate:
			trackEvent("Timetable Screen",AppConstants.SelectReturningDateTimetable, "");
			if (!TextUtils.isEmpty(tv_selectdate.getText().toString())) {
				Intent intent = new Intent(FlightScheduleActivity.this, SelectDateActivityOneWay.class);
				intent.putExtra(AppConstants.RETURN, true);
				intent.putExtra(AppConstants.SEL_DATE_ARR, calendar1);
				intent.putExtra(AppConstants.SEL_DATE_DEP, calendar);
				// overridePendingTransition(R.anim.bottom_top_popup_share,R.anim.top_bottom_popup_share);
				startActivityForResult(intent, 2000);
			} else
				showCustomDialog(getApplicationContext(), getString(R.string.Alert),
						getString(R.string.DepartureDateErr), getString(R.string.Ok), null, "");
			break;

		case R.id.llSubmit:
			trackEvent("Timetable Screen",AppConstants.SearchButtonTimetable, "");
			if (isValid()) {
				requestParameterDO = new RequestParameterDO();
				if (roundtrip)
					updateReturnData();
				else
					updateOneWayData();
				showLoader("");
				firstHit = true;
				requestParameterDO.srvUrlType = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
				callServiceFlightNotRoundtrip();
			}
			break;
		case R.id.btn_roundtrip:
			trackEvent("Timetable Screen",AppConstants.ReturnButtonTimetable, "");
			btn_roundtrip.setTextColor(resources.getColor(R.color.white));
			btn_roundtrip.setBackgroundDrawable(resources.getDrawable(R.drawable.button_red));
			btn_oneWay.setTextColor(resources.getColor(R.color.red));
			btn_oneWay.setBackgroundResource(0);
			ll_returnSelectedDate.setVisibility(View.VISIBLE);
			img_seperator.setVisibility(View.VISIBLE);
			roundtrip = true;

			break;
		case R.id.btn_oneWay:
			trackEvent("Timetable Screen",AppConstants.OneWayButtonTimetable, "");
			btn_oneWay.setTextColor(resources.getColor(R.color.white));
			btn_oneWay.setBackgroundDrawable(resources.getDrawable(R.drawable.button_red));
			btn_roundtrip.setTextColor(resources.getColor(R.color.text_color_red));
			btn_roundtrip.setBackgroundResource(0);
			ll_returnSelectedDate.setVisibility(View.GONE);
			img_seperator.setVisibility(View.GONE);
			roundtrip = false;

			break;
		}
	}

	private void callServiceFlightNotRoundtrip() {
		if (new CommonBL(FlightScheduleActivity.this, FlightScheduleActivity.this)
				.getAirFlightSchedule(requestParameterDO, AppConstants.bookingFlightDO.myBookFlightDO)) {
		} else {
			hideLoader();
		}
	}

	private void callServiceFlightWithRoundtrip() {
		if (new CommonBL(FlightScheduleActivity.this, FlightScheduleActivity.this)
				.getAirFlightSchedule(requestParameterDO, AppConstants.bookingFlightDO.myBookFlightDOReturn)) {
		} else {
			hideLoader();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1000 && resultCode == RESULT_OK) {
			if (data.hasExtra("selDate")) {
				calendar = (Calendar) data.getExtras().getSerializable("selDate");
				updateDepartureView();
				if (calendar1 != null && calendar != null && isValidDateArr()) {

				} else if (calendar1 != null && calendar != null && !isValidDateArr()) {
					calendar1 = null;
					// updateArrivalCal(calendarDeparture);
					if (tv_returnSelectedDate != null && !tv_returnSelectedDate.getText().toString()
							.equalsIgnoreCase(getString(R.string.selectDate)))
						updateArrivalView();
				}
			}
		}
		if (requestCode == 2000 && resultCode == RESULT_OK) {
			if (data.hasExtra("selDate")) {
				calendar1 = (Calendar) data.getExtras().getSerializable("selDate");

				if (calendar1 != null) {
					int i = calendar.compareTo(calendar1);
					if (i <= 0) {
						AppConstants.ArrivalTime = CalendarUtility.getBookingDate(calendar1);
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
		}

		else if (requestCode == DEPARTURE_RESULT_CODE && resultCode == RESULT_OK) {
			if (data.hasExtra("Departure_Airport")) {
				airportsOriginDo = (AirportsDO) data.getSerializableExtra("Departure_Airport");

				tvAirportFrom.setText(airportsOriginDo.name);
				tvAirportFrom.setTypeface(typefaceOpenSansSemiBold);
				tvAirportTo.setText(null);
				tvAirportTo.setTypeface(typeFaceOpenSansLight);
				ItemNameDestComparator itemNameDestComparator = new ItemNameDestComparator();
				Collections.sort(airportsOriginDo.destList, itemNameDestComparator);

				updateDepartureObject();
			}
		} else if (requestCode == ARRIVAL_RESULT_CODE && resultCode == RESULT_OK) {
			airportsDestDo = (AirportsDestDO) data.getSerializableExtra("Arrival_Airport");
			AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode = airportsDestDo.code;
			tvAirportTo.setText(airportsDestDo.name);
			tvAirportTo.setTypeface(typefaceOpenSansSemiBold);

			updateArrivalObject();
		}
	}

	private void callFlightListService() {
		showLoader("");
		if (new CommonBL(this, this).getAirportsData()) {
		} else {
			hideLoader();
			showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	private boolean isValid() {
		boolean flag = true;
		String strMessage = "";
		// if (selPosFrom == 0)
		// strMessage = getString(R.string.ArrivalAirportErr);
		// else if (selPosTo == 0)
		// strMessage = getString(R.string.DestinationErrorMessage);

		if (airportsOriginDo == null)
			strMessage = getString(R.string.DepartureAirportErr);
		else if (airportsDestDo == null)
			strMessage = getString(R.string.DestinationErrorMessage);

		else if (tv_selectdate.getText().toString().trim().length() <= 0)
			strMessage = getString(R.string.DepartureDateErr);
		else if (tv_returnSelectedDate.getText().toString().trim().length() <= 0 && roundtrip)
			strMessage = getString(R.string.ArrivalDateErr);
		if (strMessage.length() > 0) {
			flag = false;
			showCustomDialog(this, getString(R.string.Alert), strMessage, getString(R.string.Ok), "", "");
		}
		return flag;
	}

	private void updateOneWayData() {
		AppConstants.bookingFlightDO.myBookFlightDO.travelType = AppConstants.TRAVEL_TYPE_ONEWAY;
		AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime = CalendarUtility.getBookingDate(calendar);
		AppConstants.bookingFlightDO.myBookFlightDO.departureDateTimeReturn = CalendarUtility.getBookingDate(calendar);
		AppConstants.bookingFlightDO.myBookFlightDOReturn = new BookFlightDO();
	}

	private void updateReturnData() {
		updateOneWayData();
		AppConstants.bookingFlightDO.myBookFlightDOReturn.srvUrlType = AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType;
		AppConstants.bookingFlightDO.myBookFlightDOReturn.travelType = AppConstants.TRAVEL_TYPE_ONEWAY;
		AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime = CalendarUtility.getBookingDate(calendar1);
		AppConstants.bookingFlightDO.myBookFlightDOReturn.destinationLocationCode = AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode;
		AppConstants.bookingFlightDO.myBookFlightDOReturn.originLocationCode = AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode;
	}

	private void updateData(AirScheduleDO airScheduleDO) {
		if (roundtrip) {
			if (firstHit)
				airScheduleDOto = airScheduleDO;
			else {
				airScheduleDOreturn = airScheduleDO;
				moveToNextActivity();
			}
		} else {
			airScheduleDOto = airScheduleDO;
			moveToNextActivity();
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
				updateAllAirPortsNames();
				updateAirPortNames();
				updateSpinner();
				break;

			case AIR_FLIGHT_SHEDULE:
				AirScheduleDO airScheduleDO = (AirScheduleDO) data.data;
				updateData(airScheduleDO);
				if (roundtrip && firstHit) {
					firstHit = false;
					callServiceFlightWithRoundtrip();
				} else
					hideLoader();
				break;
			case AIR_BOOK:
				hideLoader();
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

	private class ItemNameDestComparator implements Comparator<AirportsDestDO> {
		public int compare(AirportsDestDO o1, AirportsDestDO o2) {
			return o1.en.compareTo(o2.en);
		}
	}

	private void moveToNextActivity() {
		hideLoader();
		String strOrigin = AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode;
		String strDest = AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode;
		strOrigin = updateStrCountryName(strOrigin);
		strDest = updateStrCountryName(strDest);
		moveToCheckFlightScheduleActivity(strOrigin, strDest);
	}

	private void moveToCheckFlightScheduleActivity(String strOrigin, String strDest) {
		Intent intent = new Intent(FlightScheduleActivity.this, CheckFlightScheduleActivity.class);
		intent.putExtra(AppConstants.ROUND_TRIP, roundtrip);
		intent.putExtra(AppConstants.ONEWAY, airScheduleDOto);
		intent.putExtra(AppConstants.RETURN, airScheduleDOreturn);
		intent.putExtra(AppConstants.FROM_LOCATION, strOrigin);
		intent.putExtra(AppConstants.TO_LOCATION, strDest);
		startActivity(intent);
	}

	private void updateAirPortNames() {
		for (int i = 0; i < AppConstants.allAirportsDO.vecAirport.size(); i++) {
			if (i != 0 && AppConstants.allAirportsDO.vecAirport.get(i).destList != null
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
		// if(AppConstants.allAirportsDO.vecAirport.get(0) != null &&
		// AppConstants.allAirportsDO.vecAirport.get(0).code != null)
		// {
		// AppConstants.allAirportsDO.vecAirport.add(0, null);
		// }
	}

	private void updateSpinner() {
		tvAirportFrom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				trackEvent("Timetable Screen",AppConstants.FlyingFromButton, "");
				Intent i = new Intent(FlightScheduleActivity.this, SelectAirport_new.class);
				startActivityForResult(i, DEPARTURE_RESULT_CODE);

			}
		});
		tvAirportTo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				trackEvent("Timetable Screen",AppConstants.FlyingToButton, "");
				Intent i = new Intent(FlightScheduleActivity.this, SelectAirport_new.class);
				i.putExtra("Arrival_Object", airportsOriginDo);
				startActivityForResult(i, ARRIVAL_RESULT_CODE);

			}
		});

		hideLoader();
	}

	@Override
	public void onButtonYesClick(String from) {
		super.onButtonYesClick(from);
		if (from.equalsIgnoreCase(AppConstants.INTERNET_PROBLEM))
			clickHome();
	}

	private void updateDepartureObject() {
		if (airportsOriginDo != null) {
			airportsDestDo = null;
			AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode = airportsOriginDo.code;
			Collections.sort(airportsOriginDo.destList, new ItemNameDestComparator());

			updateArrivalObject();
		} else {
			AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode = "";
		}
	}

	private void updateArrivalObject() {
		if (airportsDestDo != null) {
			AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode = airportsDestDo.code;
			AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType = airportsDestDo.service_type1;
		} else
			AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode = "";
	}

	private void updateDepartureView() {
		if (calendar != null) {
			tv_selectdate.setText("" + calendar.get(Calendar.DAY_OF_MONTH) + " "
					+ CalendarUtility.getMonthMMM(calendar.get(Calendar.MONTH)) + ", "
					+ CalendarUtility.getYear(calendar.get(Calendar.YEAR)));
			tv_selectdate.setTag("" + calendar.get(Calendar.DAY_OF_MONTH) + " "
					+ CalendarUtility.getMonthMMM(calendar.get(Calendar.MONTH)) + ", "
					+ CalendarUtility.getYear(calendar.get(Calendar.YEAR)));
			tv_selectdate.setTextColor(getResources().getColor(R.color.text_color));
			// tvDate.setTypeface(typefaceOpenSansRegular);
			tv_selectdate.setTypeface(typefaceOpenSansSemiBold);

		} else {
			{
				tv_selectdate.setText(null);
				tv_selectdate.setTag("");
				tv_selectdate.setTypeface(typeFaceOpenSansLight);
			}

		}
	}

	private void updateArrivalView() {
		if (calendar1 != null) {
			tv_returnSelectedDate.setText("" + calendar1.get(Calendar.DAY_OF_MONTH) + " "
					+ CalendarUtility.getMonthMMM(calendar1.get(Calendar.MONTH)) + ", "
					+ CalendarUtility.getYear(calendar1.get(Calendar.YEAR)));
			tv_returnSelectedDate.setTextColor(getResources().getColor(R.color.cal_color));
			tv_returnSelectedDate.setTypeface(typefaceOpenSansRegular);
			tv_returnSelectedDate.setTypeface(typefaceOpenSansSemiBold);
		} else {
			tv_returnSelectedDate.setText(null);
			tv_returnSelectedDate.setTypeface(typeFaceOpenSansLight);
		}
	}

	private boolean isValidDateArr() {
		if (calendar.get(Calendar.YEAR) < calendar1.get(Calendar.YEAR))
			return true;
		else if (calendar.get(Calendar.MONTH) < calendar1.get(Calendar.MONTH)
				&& calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR))
			return true;
		else if (calendar.get(Calendar.DATE) <= calendar1.get(Calendar.DATE)
				&& calendar.get(Calendar.MONTH) == calendar1.get(Calendar.MONTH)
				&& calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR))
			return true;
		else
			return false;
	}
}