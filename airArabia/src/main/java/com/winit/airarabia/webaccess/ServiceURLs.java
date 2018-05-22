package com.winit.airarabia.webaccess;

import java.util.LinkedHashMap;

import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.utils.CalendarUtility;

public class ServiceURLs {

	// ===========================Latest Login Services Live URLS
	// ================================================================
	LinkedHashMap<String, String> linkedHMLoginServices = new LinkedHashMap<String, String>();
	// ===================================================================================================

	// Test Service URLs

	//Old URLs
//	public final static String HOST_URL_G9 = "https://airarabia.isaaviations.com/webservices/services/AAResWebServices";
////	public final static String HOST_URL_G9 ="http://g9cmb3.isaaviations.com/webservices/services/AAResWebServices";
//	
//	public final static String HOST_URL_3O = "https://airarabiama.isaaviations.com/webservices/services/AAResWebServices";
//	public final static String HOST_URL_E5 = "https://airarabia.isaaviations.com/webservices/services/AAResWebServices";
////	public final static String HOST_URL_9P = "https://reservations9p.airarabia.com/webservices/services/AAResWebServices";
//	public final static String HOST_URL_9P = "https://9p.isaaviations.com/webservices/services/AAResWebServices";



	//Mukesh reference urls

//	public final static String HOST_URL_G9 = "http://airarabia29.isaaviations.com/webservices/services/AAResWebServices";
//	public final static String HOST_URL_3O = "https://airarabia3O.isaaviations.com/webservices/services/AAResWebServices";
////	public final static String HOST_URL_E5 = "https://airarabia29.isaaviations.com/webservices/services/AAResWebServices";
//	public final static String HOST_URL_E5 = "https://airarabiaEg.isaaviations.com/webservices/services/AAResWebServices";
//	public final static String HOST_URL_9P = "https://9p.isaaviations.com/webservices/services/AAResWebServices";
//
//	// Test Password
//	public final static String ServicePassword_G9 = "p@ss1234";
//	public final static String ServicePassword_3O = "p@ss1234";
//	public final static String ServicePassword_E5 = "p@ss1234";
//	public final static String ServicePassword_9P = "p@ss1234";
//	//this is for specific
//	public final static String ServicePasswordNew_3O = "arabia123";
//
//	public static final String ServiceUserName_G9 = "WSMOBILE";
//	public static final String ServiceUserName_3O = "APPANDROID3O";
////	public static final String ServiceUserName_E5 = "APPIPHONEE5";
//	public static final String ServiceUserName_E5 = "WSMOBILEE5";
////	public static final String ServiceUserName_9P = "WSMOBILE";
//	public static final String ServiceUserName_9P = "WSMOBILE9P";
//	//this is for specific
//	public static final String ServiceUserNameNew_3O = "ANDROIDAPPMAD3O";

	// APP


	//	// Live Service
//	// URLs===========================================================================
////
	public final static String HOST_URL_G9 ="https://reservations.airarabia.com/webservices/services/AAResWebServices";
//	public final static String HOST_URL_G9 ="http://airarabiag9.cmb.isaaviations.com/webservices/services/AAResWebServices";
	public final static String HOST_URL_3O ="https://reservationsma.airarabia.com/webservices/services/AAResWebServices";
	public final static String HOST_URL_E5 ="https://reservationseg.airarabia.com/webservices/services/AAResWebServices";
	public final static String HOST_URL_9P ="https://reservations9p.airarabia.com/webservices/services/AAResWebServices";
	// Live Password
	public final static String ServicePassword_G9 ="pass1234";  //juko2015@ash
	public final static String ServicePassword_3O = "pass1234";
	public final static String ServicePassword_E5 = "pass1234";
	public final static String ServicePassword_9P = "pass1234";
	public final static String ServicePasswordNew_3O = "arabia123";

	public static final String ServiceUserName_G9 = "APPANDROIDG9";   //WSMOBILE
	public static final String ServiceUserName_3O = "APPANDROID3O";
	public static final String ServiceUserName_E5 = "APPANDROIDE5";
	public static final String ServiceUserName_9P = "APPANDROID9P";
	public static final String ServiceUserNameNew_3O = "ANDROIDAPPMAD3O";
//////

	// Fixed Flight Regions..
	public static final String GET_G9_CURRENCY = "AED";
	public static final String GET_3O_CURRENCY = "MAD";
	public static final String GET_E5_CURRENCY = "EGP";
	public static final String GET_9P_CURRENCY = "JOD";

	// test login
//	 public final static String LoginServiceEndPoint_G9
//	 ="http://airarabia.isaaviations.com/webservices/services/AAWebServicesForLoginAPI";
//	 public final static String LoginServiceEndPoint_3O
//	 ="https://reservationsma.airarabia.com/webservices/services/AAWebServicesForLoginAPI";
//	 public final static String LoginServiceEndPoint_E5
//	 ="https://reservationseg.airarabia.com/webservices/services/AAWebServicesForLoginAPI";
//	 public final static String LoginServiceEndPoint_9P
//	 ="https://reservations9p.airarabia.com/webservices/services/AAWebServicesForLoginAPI";

	// live login
	public final static String LoginServiceEndPoint_G9 = "https://reservations.airarabia.com/webservices/services/AAWebServicesForLoginAPI";
	public final static String LoginServiceEndPoint_3O = "https://reservationsma.airarabia.com/webservices/services/AAWebServicesForLoginAPI";
	public final static String LoginServiceEndPoint_E5 = "https://reservationseg.airarabia.com/webservices/services/AAWebServicesForLoginAPI";
	public final static String LoginServiceEndPoint_9P = "https://reservations9p.airarabia.com/webservices/services/AAWebServicesForLoginAPI";

	public final static String LoginServiceUserName_G9 = "WSLOGINAPI";
	public final static String LoginServiceUserName_3O = "WSLOGINAPI";
	public final static String LoginServiceUserName_E5 = "WSLOGINAPI";
	public final static String LoginServiceUserName_9P = "WSLOGINAPI";

	public final static String LoginServicePassword_G9 = "G9login#123";
	public final static String LoginServicePassword_3O = "3Ologin#123";
	public final static String LoginServicePassword_E5 = "E5login#123";
	public final static String LoginServicePassword_9P = "9Plogin#123";

//	public final static String HOST_URL_LOCAL = "http://m.airarabia.com/AirArabiaWebServices.asmx";
		public final static String HOST_URL_LOCAL = "http://52.33.71.7/AirArabiaWebServices.asmx";
	// -------------- By Rahul Maurya
	// public final static String ALL_AIRPORT_URL =
	// "http://m.airarabia.com/js/AirportsData.json";
	public final static String ALL_AIRPORT_URL_TIMESTAMP = "?a=";

//	public final static String GET_NATIONALITIES = "http://m.airarabia.com/StaticXML/Country-Codes.xml";
		public final static String GET_NATIONALITIES = "http://52.33.71.7/StaticXML/Country-Codes.xml";
//	public final static String GET_COUNTRYISDCODES = "http://m.airarabia.com/StaticXML/CountryISDCodes.xml";
		public final static String GET_COUNTRYISDCODES = "http://52.33.71.7/StaticXML/CountryISDCodes.xml";
//	public final static String GET_COUNTRY_NAMES = "http://m.airarabia.com/StaticXML/CountryNames.xml";
		public final static String GET_COUNTRY_NAMES = "http://52.33.71.7/StaticXML/CountryNames.xml";
//	public final static String GET_CITIES = "http://m.airarabia.com/StaticXML/City.xml";
		public final static String GET_CITIES = "http://52.33.71.7/StaticXML/City.xml";
//	public final static String GET_OFFICE_LOCATIONS = "http://m.airarabia.com/StaticXML/officelocations.xml";
	public final static String GET_OFFICE_LOCATIONS = "http://52.33.71.7/StaticXML/officelocations.xml";


	//for test
//	public final static String ALL_AIRPORT_URL = "http://dev4.winitsoftware.com/airarabiadynamic/js/AirportsData_24jul.json";
	//for live
//	public final static String ALL_AIRPORT_URL ="http://m.airarabia.com/js/AirportsData_24jul.json";
	public final static String ALL_AIRPORT_URL ="http://52.33.71.7/js/AirportsData_24jul.json";

//	public final static String ALL_AIRPORT_URL ="http://dev4.winitsoftware.com/airarabiatest/js/AirportsData_24jul.json";


//  Mukesh local url
//	public final static String ALL_AIRPORT_URL = "http://10.20.53.170/AirArabiaNewDesign/js/AirportsData_24jul.json";
	// Test New
	// test local http://m.airarabia.com/js/AirportsData.json
	// public final static String ALL_AIRPORT_URL =
	// "http://dev4.winitsoftware.com/airarabiadynamic/js/AirportsData.json";
	// public final static String ALL_AIRPORT_URL =
	// "http://dev4.winitsoftware.com/airarabiadynamic/js/AirportsDataNew.json";
	// http://10.20.53.170/AirArabiaNewDesign/js/AirportsData_24Jul.json
	// public final static String ALL_AIRPORT_URL
	// ="http://10.20.53.170/AirArabiaNewDesign/js/AirportsData_24Jul.json";

	// public final static String CURRENCY_CONVERSION_SERVICE =
	// "http://m.airarabia.com/AirarabiaExchangeRate.asmx";
//	public final static String CURRENCY_CONVERSION_SERVICE = "http://m.airarabia.com/AirarabiaExchangeRate.asmx?op=GetExchangeRate";
	public final static String CURRENCY_CONVERSION_SERVICE = "http://52.33.71.7/AirarabiaExchangeRate.asmx?op=GetExchangeRate";

	// -------------- By Rahul Maurya
	// public final static String GET_CITIES =
	// "http://dev4.winitsoftware.com/airarabiadynamic/StaticXML/City.xml";
	// public final static String GET_OFFICE_LOCATIONS =
	// "http://dev4.winitsoftware.com/airarabiadynamic/StaticXML/officelocations.xml";
	// public final static String ALL_CITIES_URL =
	// "http://dev4.winitsoftware.com/AirArabiaDynamic/AirArabiaWebServices.asmx";

	// http://www.qfdevelop.com/data/AirportsData.json
	// http://10.20.53.17/airarabiadynamicnew/js/AirportsData.json
	// 10.20.53.17/airarabiadynamicnew/
	// http://m.airarabia.com/js/AirportsData.json

	// test
	public final static String ServicePassword_LOGIN = "p@ssw0rd123";
	public static final String ServiceUserName_LOGIN = "WSLOGINAPI";
	// Test URLs
	public final static String HOST_URL_Login = "https://airarabia.isaaviations.com/webservices/services/AAWebServicesForLoginAPI";
//	public final static String POSTER_IMAGES_URL = "http://m.airarabia.com/AirArabiaWebServices.asmx?op=GetBanners";
	public final static String POSTER_IMAGES_URL = "http://52.33.71.7/AirArabiaWebServices.asmx?op=GetBanners";
	// //Test URL
	// public final static String HOST_URL_G9 =
	// "http://airarabia.isaaviations.com/webservices/services/AAResWebServices";
	// public final static String HOST_URL_3O =
	// "http://airarabiama.isaaviations.com/webservices/services/AAResWebServices";
	// public final static String HOST_URL_E5 =
	// "http://airarabia.isaaviations.com/webservices/services/AAResWebServices";
	// // Test Password
	// public final static String ServicePassword_G9 = "password123";
	// public final static String ServicePassword_3O = "password123";
	// public final static String ServicePassword_E5 = "password123";
	// public static final String ServiceUserName_G9 = "APPANDROIDG9";
	// public static final String ServiceUserName_3O = "APPANDROID3O";
	// public static final String ServiceUserName_E5 = "APPANDROIDE5";

	// ======================Live URL for
	// registration======================================

	public final static String URL_New_Registration_UAE_G9 = "https://reservations.airarabia.com/ibe/public/showReservation.action?hdnParamData=EN^MRE&hdnCarrier=G9";
	public final static String URL_New_Registration_MOROCCO_3O = "https://reservationsma.airarabia.com/ibe/public/showReservation.action?hdnParamData=EN^MRE&hdnCarrier=3O";
	public final static String URL_New_Registration_Egypt_E5 = "https://reservationseg.airarabia.com/ibe/public/showReservation.action?hdnParamData=EN^MRE&hdnCarrier=E5";
	public final static String URL_New_Registration_Jorden_9P = "https://reservations9p.airarabia.com/ibe/public/showReservation.action?hdnParamData=EN^MRE&hdnCarrier=9P";

	public static String LOGPNR = "LogPNR";
	public static String MODIFYPNR = "ModifyPNR";

	private static String AIR_PNR_SOAP_ACTION = "getReservationbyPNR";
	private static String MODIFY_RES_QUERY_SOAP_ACTION = "modifyResQuery";
	private static String MODIFY_RESERVATION_SOAP_ACTION  = "modifyReservation";

	// public static String GET_CITIES="GetCities";
	// public static String GET_NATIONALITIES="GetNationalities";
	// public static String GET_COUNTRYISDCODES="GetCountryISDCodes";
	// public static String GET_COUNTRYOFRESIDENCES="GetCountryOfResidences";
	// public static String GET_OFFICE_LOCATIONS = "GetOfficeLocations";

	/**
	 * This method will return request type of this method. i.e, GET or POST
	 *
	 * @param method
	 * @return AppConstants.GET or AppConstants.POST CollectScreenId
	 */
	public static int getRequestType(ServiceMethods method) {
		if (method == ServiceMethods.WS_TEST || method == ServiceMethods.AIR_PORT_SDATA
				|| method == ServiceMethods.WS_CITY || method == ServiceMethods.WS_NATIONALITIES
				|| method == ServiceMethods.WS_COUNTRYISDCODES || method == ServiceMethods.WS_COUNTRYNAMES
				|| method == ServiceMethods.WS_OFFICELOCATIONS) {
			return AppConstants.GET;
		} else if (method == ServiceMethods.AIR_AVAILABILITY || method == ServiceMethods.AIR_AVAILABILITY_RETURN
				|| method == ServiceMethods.AIR_PRICEQUOTE || method == ServiceMethods.ANCILLARY_PRICEQUOTE
				|| method == ServiceMethods.AIR_BAGGAGE_DETAILS || method == ServiceMethods.AIR_MEAL_DETAILS
				|| method == ServiceMethods.AIR_SEAT_DETAILS || method == ServiceMethods.INSURANCE_QUOTE
				|| method == ServiceMethods.HALA_REQ || method == ServiceMethods.AIR_BOOK
				|| method == ServiceMethods.AIR_PNR || method == ServiceMethods.AIR_FLIGHT_SHEDULE
				|| method == ServiceMethods.MODIFIED_RESQUERY || method == ServiceMethods.MODIFY_RESERVATION
				|| method == ServiceMethods.LOG_PNR || method == ServiceMethods.MODIFY_PNR
				|| method == ServiceMethods.INSURANCE_QUOTE || method == ServiceMethods.WS_LOGIN
				|| method == ServiceMethods.CURRENCY_CONVERSION_SERVICE || method == ServiceMethods.WS_POSTER_DOWNLOAD
				|| method == ServiceMethods.CANCEL_FLIGHT_CONFIRMATION || method == ServiceMethods.CANCEL_FLIGHT || method == ServiceMethods.MODIFY_BOOK)
			return AppConstants.POST;
		return 0;
	}

	public static String getRequestedURL(ServiceMethods wsMethod, String srvUrlType) {
		switch (wsMethod) {
			// for booking flow
			case AIR_AVAILABILITY:
			case AIR_AVAILABILITY_RETURN:
			case AIR_PRICEQUOTE:
			case ANCILLARY_PRICEQUOTE:
			case AIR_BAGGAGE_DETAILS:
			case AIR_MEAL_DETAILS:
			case AIR_SEAT_DETAILS:
			case INSURANCE_QUOTE:
			case HALA_REQ:
			case AIR_BOOK:
			case AIR_PNR:
			case AIR_FLIGHT_SHEDULE:
			case MODIFY_BOOK:
			case MODIFIED_RESQUERY:
			case MODIFY_RESERVATION:
			case CANCEL_FLIGHT_CONFIRMATION:
			case CANCEL_FLIGHT:
				if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_G9))
					return HOST_URL_G9;
				else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_3O))
					return HOST_URL_3O;
				else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_E5))
					return HOST_URL_E5;
				else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_9P))
					return HOST_URL_9P;
				// for log pnr
			case LOG_PNR:
			case MODIFY_PNR:
				return HOST_URL_LOCAL;
			// for all airport data
			case AIR_PORT_SDATA:
				return ALL_AIRPORT_URL + ALL_AIRPORT_URL_TIMESTAMP + CalendarUtility.getCurrentTimeStamp();
			// all other data getting from WINIT service URLs
			case WS_CITY:
				return GET_CITIES;
			case WS_NATIONALITIES:
				return GET_NATIONALITIES;
			case WS_COUNTRYISDCODES:
				return GET_COUNTRYISDCODES;
			case WS_COUNTRYNAMES:
				return GET_COUNTRY_NAMES;
			case WS_OFFICELOCATIONS:
				return GET_OFFICE_LOCATIONS;

			case WS_LOGIN:
				return HOST_URL_Login;

			case WS_POSTER_DOWNLOAD:
				return POSTER_IMAGES_URL;
			case CURRENCY_CONVERSION_SERVICE:
				return CURRENCY_CONVERSION_SERVICE;
			default:
				break;

		}
		return null;
	}

	public static String getRequestedURLForLOGIN(ServiceMethods wsMethod, String srvUrlType, String requestURLType) {
		switch (wsMethod) {
			case WS_LOGIN:
				if (requestURLType.equalsIgnoreCase(AppConstants.LoginServiceEndPoint_G9))
					return ServiceURLs.LoginServiceEndPoint_G9;
				else if (requestURLType.equalsIgnoreCase(AppConstants.LoginServiceEndPoint_3O))
					return LoginServiceEndPoint_3O;
				else if (requestURLType.equalsIgnoreCase(AppConstants.LoginServiceEndPoint_E5))
					return LoginServiceEndPoint_E5;
				else if (requestURLType.equalsIgnoreCase(AppConstants.LoginServiceEndPoint_9P))
					return LoginServiceEndPoint_9P;
			default:
				break;

		}
		return null;
	}

	public static String getSoapAction(ServiceMethods wsMethod) {
		switch (wsMethod) {
			case AIR_PNR:
				return AIR_PNR_SOAP_ACTION;
			case MODIFIED_RESQUERY:
				return MODIFY_RES_QUERY_SOAP_ACTION;
			case MODIFY_RESERVATION:
				return MODIFY_RESERVATION_SOAP_ACTION;
			default:
				break;

		}
		return null;
	}

	public static String getRequestedURL(ServiceMethods wsMethod, String parameters, String srvUrlType) {
		switch (wsMethod) {
			default:
				return getRequestedURL(wsMethod, srvUrlType);
		}
	}
}
