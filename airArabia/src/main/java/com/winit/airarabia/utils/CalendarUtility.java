package com.winit.airarabia.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import com.winit.airarabia.common.AppConstants;

import android.util.Log;

public class CalendarUtility 
{
	private final static String DATE_PATTERN_BOOKING_SUMMARY = "EEE, dd MMM yy, HH:mm";
	private final static String DATE_PATTERN_SEL_FLIGHT = "EEEE, MMMM dd, yyyy";
	private final static String DATE_PATTERN_SEL_FLIGHT_NEW = "EEE dd";
	private final static String DATE_PATTERN_CURRENT_DATE = "yyyy-MM-dd";
	private final static String DATE_PATTERN_DOB = "dd MMM yyyy";
	private final static String MONTH_FULL_PATTERN = "MMMM";
	private final static String YEAR_FULL_PATTERN = "yyyy";
	private final static String MONTH_SHORT_PATTERN = "MMM";
	private final static String WEEK_FULL_PATTERN = "EEEE";
	private final static String SERVICE_TIME_PATTERN = "HH:mm:ss.SSS";
	private final static String SERVICE_TIME_HOUR_MIN_PATTERN = "HH:mm";

	private final static String DATE_PATTERN_CURRENT_DATE_NEW = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ";
	private final static String DATE_PATTERN_CURRENT_DATE_LOG = "yyyy-MM-dd'T'HH:mm:ss";

	private final static String DATE_PATTERN_SUMMARY = "EEE, dd MMM, yyyy";

	public static String getMonth(int month)
	{
		String strMonth = "";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);

		strMonth = new SimpleDateFormat(MONTH_FULL_PATTERN).format(calendar.getTime());
		return strMonth;
	}
	public static String getMonthMON(int month)
	{
		String strMonth = "";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);

		strMonth = new SimpleDateFormat(MONTH_SHORT_PATTERN).format(calendar.getTime());
		return strMonth;
	}
	public static String getMonthMMM(int month)
	{
		String strMonth = "";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);
		if (Locale.getDefault().equals(new Locale(AppConstants.LANG_LOCAL_AR))) {
			strMonth = new SimpleDateFormat(MONTH_SHORT_PATTERN, Locale.ENGLISH).format(calendar.getTime());
		}
		else
			strMonth = new SimpleDateFormat(MONTH_SHORT_PATTERN).format(calendar.getTime());
		
		if (!Locale.getDefault().equals(new Locale(AppConstants.LANG_LOCAL_FR))) {
			switch(month)
			{
			case 0:
				strMonth = "Jan";
				break;
			case 1:
				strMonth = "Feb";break;
			case 2:
				strMonth = "Mar";break;
			case 3:
				strMonth = "Apr";break;
			case 4:
				strMonth = "May";break;
			case 5:
				strMonth = "Jun";break;
			case 6:
				strMonth = "Jul";break;
			case 7:
				strMonth = "Aug";break;
			case 8:
				strMonth = "Sep";break;
			case 9:
				strMonth = "Oct";break;
			case 10:
				strMonth = "Nov";break;
			case 11:
				strMonth = "Dec";break;
			}
		}
		else
		{
			switch(month)
			{
			case 0:
				strMonth = "Jan";break;
			case 1:
				strMonth = "Fév";break;
			case 2:
				strMonth = "Mar";break;
			case 3:
				strMonth = "Avr";break;
			case 4:
				strMonth = "Mai";break;
			case 5:
				strMonth = "Jui";break;
			case 6:
				strMonth = "Jui";break;
			case 7:
				strMonth = "Aoû";break;
			case 8:
				strMonth = "Sep";break;
			case 9:
				strMonth = "Oct";break;
			case 10:
				strMonth = "Nov";break;
			case 11:
				strMonth = "Déc";break;
			}
		}
		
		return strMonth;
	}
	public static String getYear(int year)
	{
		String strYear = "";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		if (Locale.getDefault().equals(new Locale(AppConstants.LANG_LOCAL_AR))) {
			strYear = new SimpleDateFormat(YEAR_FULL_PATTERN, Locale.ENGLISH).format(calendar.getTime());
		}
		else
			strYear = new SimpleDateFormat(YEAR_FULL_PATTERN, Locale.ENGLISH).format(calendar.getTime());
		return strYear;
	}

	public static String getDDMMMYYYYDate(Calendar calendar)
	{
		String strMonth = "";


		if (Locale.getDefault().equals(new Locale(AppConstants.LANG_LOCAL_AR))) {
			strMonth = new SimpleDateFormat(DATE_PATTERN_DOB, Locale.ENGLISH).format(calendar.getTime());
		}
		else
			strMonth = new SimpleDateFormat(DATE_PATTERN_DOB).format(calendar.getTime());
		return strMonth;
	}

	public static String getDobMonth(int month)
	{
		String strMonth = "";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);

		strMonth = new SimpleDateFormat(MONTH_SHORT_PATTERN).format(calendar.getTime());
		return strMonth;
	}
	public static String getDayOfWeek(int dayOfWeeks) {
		String strWeek = "";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, dayOfWeeks);

		strWeek = new SimpleDateFormat(WEEK_FULL_PATTERN).format(calendar.getTime());
		return strWeek;
	}

	public static int getMonthTotalDay(int month)
	{
		int monthMaxDay = 0;

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.MONTH, month);
		monthMaxDay = calendar.getMaximum(Calendar.DAY_OF_MONTH);
		return monthMaxDay;
	}

	public static String getSelectFlightDate(Calendar calendar)
	{
		String strDate = "";
		try {
			strDate = new SimpleDateFormat(DATE_PATTERN_SEL_FLIGHT).format(calendar.getTime());
		} catch (Exception e) {
		}
		return strDate;
	}

	public static String getCurrentDateTimeStamp() {
		String sdfTime = new SimpleDateFormat(DATE_PATTERN_CURRENT_DATE_NEW, new Locale("en")).format(System.currentTimeMillis());
		return sdfTime.substring(0,sdfTime.length()-2)+":"+sdfTime.substring(sdfTime.length()-2);
	}

	public static String getCurrentTimeStamp() {
		return System.currentTimeMillis()+"";
	}

	public static String getCurrentDate() {
		Date date = new Date(System.currentTimeMillis());
		String sdf = new SimpleDateFormat(DATE_PATTERN_CURRENT_DATE).format(date);

		return sdf;
	}

	public static String getBookingDate(Calendar calendar) {
		String sdf = new SimpleDateFormat(DATE_PATTERN_CURRENT_DATE, new Locale("en")).format(calendar.getTime());
		sdf = sdf+"T00:00:00";
		return sdf;
	}

	public static String getBookingTimeFromDate(String date) {
		try {
			String sdf = date.split("T")[1];
			sdf = sdf.substring(0, 5);
			return sdf;
		} catch (Exception e) {
			return "0000";
		}
	}

	public static Integer getYearFromDate(String date) {
		try {
			String sdf = date.split("T")[0];
			sdf = sdf.split("-")[0];
			return StringUtils.getInt(sdf);
		} catch (Exception e) {
			return 0;
		}
	}

	public static Integer getMonthFromDate(String date) {
		try {
			String sdf = date.split("T")[0];
			sdf = sdf.split("-")[1];
			return StringUtils.getInt(sdf);
		} catch (Exception e) {
			return 0;
		}
	}

	public static Integer getDayFromDate(String date) {
		try {
			String sdf = date.split("T")[0];
			sdf = sdf.split("-")[2];
			return StringUtils.getInt(sdf);
		} catch (Exception e) {
			return 0;
		}
	}

	public static Integer getHourFromDate(String date) {
		try {
			String sdf = date.split("T")[1];
			sdf = sdf.split(":")[0];
			return StringUtils.getInt(sdf);
		} catch (Exception e) {
			return 0;
		}
	}

	public static Integer getMinFromDate(String date) {
		try {
			String sdf = date.split("T")[1];
			sdf = sdf.split(":")[1];
			return StringUtils.getInt(sdf);
		} catch (Exception e) {
			return 0;
		}
	}

	public static String getStrYearFromDate(String date) {
		try {
			String sdf = date.split("T")[0];
			sdf = sdf.split("-")[0];
			return sdf;
		} catch (Exception e) {
			return "";
		}
	}

	public static String getStrMonthFromDate(String date) {
		try {
			String sdf = date.split("T")[0];
			sdf = sdf.split("-")[1];
			return sdf;
		} catch (Exception e) {
			return "";
		}
	}

	public static String getStrDayFromDate(String date) {
		try {
			String sdf = date.split("T")[0];
			sdf = sdf.split("-")[2];
			return sdf;
		} catch (Exception e) {
			return "";
		}
	}

	public static String getStrHourFromDate(String date) {
		try {
			String sdf = date.split("T")[1];
			sdf = sdf.split(":")[0];
			return sdf;
		} catch (Exception e) {
			return "";
		}
	}

	public static String getStrMinFromDate(String date) {
		try {
			String sdf = date.split("T")[1];
			sdf = sdf.split(":")[1];
			return sdf;
		} catch (Exception e) {
			return "";
		}
	}

	public static String getBookingSummaryDateFromDate(String date) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, getDayFromDate(date));
		calendar.set(Calendar.MONTH, getMonthFromDate(date)-1);
		calendar.set(Calendar.YEAR, getYearFromDate(date));
		calendar.set(Calendar.HOUR_OF_DAY, getHourFromDate(date));
		calendar.set(Calendar.MINUTE, getMinFromDate(date));

		String sdf = new SimpleDateFormat(DATE_PATTERN_BOOKING_SUMMARY).format(calendar.getTime());
		return sdf;
	}
	public static String getDateWithNameofDayFromDate(String date) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, getDayFromDate(date));
		calendar.set(Calendar.MONTH, getMonthFromDate(date)-1);
		calendar.set(Calendar.YEAR, getYearFromDate(date));
		calendar.set(Calendar.HOUR_OF_DAY, getHourFromDate(date));
		calendar.set(Calendar.MINUTE, getMinFromDate(date));

		String sdf = "";
		//		"EEEE, MMMM dd, yyyy"

		if (Locale.getDefault().equals(new Locale(AppConstants.LANG_LOCAL_AR))) {
			sdf  = new SimpleDateFormat("EEEE").format(calendar.getTime())+ ", "+ 
					new SimpleDateFormat("dd", Locale.ENGLISH).format(calendar.getTime()) + " "+
					new SimpleDateFormat("MMMM").format(calendar.getTime())+ " "+
					new SimpleDateFormat("yyyy",Locale.ENGLISH).format(calendar.getTime());
		} else {
			sdf = new SimpleDateFormat(DATE_PATTERN_SEL_FLIGHT, Locale.ENGLISH).format(calendar.getTime());
		}


		return sdf;
	}
	public static String getTimeInHourMinuteFromDate(String date) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, getDayFromDate(date));
		calendar.set(Calendar.MONTH, getMonthFromDate(date)-1);
		calendar.set(Calendar.YEAR, getYearFromDate(date));
		calendar.set(Calendar.HOUR_OF_DAY, getHourFromDate(date));
		calendar.set(Calendar.MINUTE, getMinFromDate(date));

		String sdf = new SimpleDateFormat(SERVICE_TIME_HOUR_MIN_PATTERN,Locale.ENGLISH).format(calendar.getTime());
		return sdf;
	}


	public static String getDateForSummary(String date) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, getDayFromDate(date));
		calendar.set(Calendar.MONTH, getMonthFromDate(date)-1);
		calendar.set(Calendar.YEAR, getYearFromDate(date));
		calendar.set(Calendar.HOUR_OF_DAY, getHourFromDate(date));
		calendar.set(Calendar.MINUTE, getMinFromDate(date));

		String sdf= "";
		//		"EEE, dd MMM, yyyy"

		if (Locale.getDefault().equals(new Locale(AppConstants.LANG_LOCAL_AR))) {
			sdf  = new SimpleDateFormat("EEE").format(calendar.getTime())+ ", "+ 
					new SimpleDateFormat("dd",Locale.ENGLISH).format(calendar.getTime()) + " "+
					new SimpleDateFormat("MMM").format(calendar.getTime()) + " "+
					new SimpleDateFormat("yyyy",Locale.ENGLISH).format(calendar.getTime());
			//			String year = CalendarUtility.getYear(calendarDeparture.get(Calendar.YEAR));
		} else {
			//			strDate = new SimpleDateFormat(DATE_PATTERN_SEL_FLIGHT_NEW).format(cal.getTime());
			sdf = new SimpleDateFormat(DATE_PATTERN_SUMMARY).format(calendar.getTime());
		}

		return sdf;
	}

	public static Calendar getCalendarFromDate(String date) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, getDayFromDate(date));
		calendar.set(Calendar.MONTH, getMonthFromDate(date)-1);
		calendar.set(Calendar.YEAR, getYearFromDate(date));
		calendar.set(Calendar.HOUR_OF_DAY, getHourFromDate(date));
		calendar.set(Calendar.MINUTE, getMinFromDate(date));

		return calendar;
	}

	public static String getBookingTimeDate() {

		Calendar calendar = Calendar.getInstance();

		String sdf = new SimpleDateFormat(DATE_PATTERN_BOOKING_SUMMARY).format(calendar.getTime());
		return sdf;
	}

	public static String getScheduleFlightDateFromDate(String date) {

		String sdf = "";

		sdf = getStrMonthFromDate(date) + "/"+getStrDayFromDate(date)+ "/"+getStrYearFromDate(date);

		return sdf;
	}

	public static String getScheduleFlightTimeFromDate(String date) {

		String sdf = "";

		sdf = getStrHourFromDate(date)+":"+getStrMinFromDate(date);

		return sdf;
	}

	public static double getHourDiffFromCurDate(String date) {

		double diff = 0;

		Calendar calCur = Calendar.getInstance();
		Calendar calDate = getCalendarFromDate(date);

		diff = calDate.getTimeInMillis() - calCur.getTimeInMillis();

		diff = diff/(1*60*60*1000);

		return diff;
	}

	public static double getHourFromMin(String min) {

		double hr = 0;

		try {
			hr = StringUtils.getLong(min)/(1*60*60*1000);
		} catch (Exception e) {
		}

		return hr;
	}

	public static String getTimeOfService() {

		Calendar calCur = Calendar.getInstance();

		String strTime = new SimpleDateFormat(SERVICE_TIME_PATTERN).format(calCur.getTime());

		return strTime;
	}

	public static boolean compareWithCurDate(Calendar cal1,Calendar cal2)
	{
		if(getBookingDate(cal1).equalsIgnoreCase(getBookingDate(cal2)))
			return true;
		return false;
	}

	public static String getCurrentDateLog() {
		String sdfTime = new SimpleDateFormat(DATE_PATTERN_CURRENT_DATE_LOG, new Locale("en")).format(System.currentTimeMillis());
		return sdfTime;
	}

	public static long getDateTimeInMillies(String mDate) {
		long d = 0;
		Calendar c = getCalendarFromDate(mDate);
		try {
			d = c.getTimeInMillis();
		} catch (Exception e) {
		}

		return d;
	}

	public static String getSelectFlightDateNew(Calendar calendar, int count)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, calendar.get(Calendar.DATE));
		cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		String strDate = "";
		try {
			cal.add(Calendar.DATE, count);

			if (Locale.getDefault().equals(new Locale(AppConstants.LANG_LOCAL_AR))) {
				strDate  = new SimpleDateFormat("EEE").format(cal.getTime())+ " "+ 
						new SimpleDateFormat("dd",Locale.ENGLISH).format(cal.getTime());
				//				String year = CalendarUtility.getYear(calendarDeparture.get(Calendar.YEAR));
			} else {
				strDate = new SimpleDateFormat(DATE_PATTERN_SEL_FLIGHT_NEW).format(cal.getTime());
			}


		} catch (Exception e) {
		}
		return strDate;
	}
	
	public static String getCurrencyCodeBasedonLocation(String cy) {
		
		String currencyCode = "";
		try {
				Locale[] locales = Locale.getAvailableLocales();
		
				Map<String, String> currencies = new TreeMap();
				for (Locale locale : locales) {
					try {
						currencies.put(locale.getDisplayCountry(new Locale(AppConstants.LANG_EN)), Currency.getInstance(locale).getCurrencyCode());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				for (String country : currencies.keySet()) {
					if(country.equalsIgnoreCase(cy)){
						currencyCode = (String) currencies.get(country);
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		return currencyCode;
	}


}