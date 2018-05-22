package com.winit.airarabia.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import android.graphics.Typeface;

import com.winit.airarabia.objects.AirBaggageDetailsDO;
import com.winit.airarabia.objects.AirMealDetailsDO;
import com.winit.airarabia.objects.AirSeatMapDO;
import com.winit.airarabia.objects.AllAirportNamesDO;
import com.winit.airarabia.objects.AllAirportsDO;
import com.winit.airarabia.objects.BookingFlightDO;
import com.winit.airarabia.objects.BundledServiceDO;
import com.winit.airarabia.objects.CountryDO;
import com.winit.airarabia.objects.CountryISDDO;
import com.winit.airarabia.objects.CurrencyDo;
import com.winit.airarabia.objects.FlexiOperationsDO;
import com.winit.airarabia.objects.HalaDO;
import com.winit.airarabia.objects.InsuranceQuoteDO;
import com.winit.airarabia.objects.LoginDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;
import com.winit.airarabia.objects.PTC_FareBreakdownDO;
import com.winit.airarabia.objects.PosterImagesDO;

public class AppConstants {

	public static int DEVICE_WIDTH = 0, DEVICE_HEIGHT = 0;

	public static final int GET = 1, POST = 2;

	public static final String HOME_CLICK = "ACTION.HOME.CLICK", LANGUAGE_CHANGE = "ACTION.LANGUAGE.CHANGE",
			BOOK_SUCCESS = "ACTION.BOOK_SUCCESS", BOOKING_FLIGHT = "ACTION.BOOKING.FLIGHT",
			MODIFIED_RES_QRY = "ACTION.BOOKING.MODIFIED_RES_QRY",
			BOOKING_FLIGHT_AIRPRICE = "ACTION.BOOKING.FLIGHT.AIRPRICE", ALL_AIRPORTS_NAMES = "ACTION.AIRPORT.NAME",
			ALL_AIRPORTS = "ACTION.AIRPORT", AIR_BOOK = "ACTION.AIRPORT.AIR_BOOK", SPLASH = "ACTION.AIRPORT.SPLASH",
			AIRPORTS_PRICE_TOTAL = "AIRPORTS_PRICE_TOTAL", PERSON_TYPE_ADULT = "Adult", PERSON_TYPE_CHILD = "Child",
			PERSON_TYPE_INFANT = "Infant", PERSON_TYPE_ADT = "ADT", PERSON_TYPE_CHD = "CHD", PERSON_TYPE_INF = "INF",
			TRAVEL_TYPE_ONEWAY = "OneWay", TRAVEL_TYPE_RETURN = "Return", SERVICE_URL_TYPE_G9 = "G9",
			SERVICE_URL_TYPE_3O = "3O", SERVICE_URL_TYPE_E5 = "E5", SERVICE_URL_TYPE_9P = "9P",
			SERVICE_URL_TYPE_EMPTY = "LOGIN", ROUND_TRIP = "roundtrip", RETURN = "Return", FROM = "from", TO = "to",
			FROM_LOCATION = "FromLocation", TO_LOCATION = "ToLocation", SEL_DATE = "selDate",
			SEL_DATE_CHILD = "SEL_DATE_CHILD", SEL_DATE_RETURN = "selDate", SEL_DATE_ARR = "selDateArrival",
			SEL_DATE_DEP = "selDateDeparture", IS_DEPARTURE = "IS_DEPARTURE", IS_ARRIVAL_CLICKED = "IS_ARRIVAL_CLICKED",
			LOCATION = "location", LATITUDE = "latitude", LONGITUDE = "longitude",
			SELECT_FLIGHT_CAL = "SELECT_FLIGHT_CAL", ONEWAY = "OneWay", INTERNET_PROBLEM = "INTERNET_PROBLEM",
			PROFILE_PROBLEM = "PROFILE_PROBLEM", IS_FROM_PERSONALIZED_TRIP = "IS_FROM_PERSONALIZED_TRIP",
			PASS_INFO = "PASS_INFO";

	public static final String AIRPORT_CODE = "CMN";

	public static final String SEAT_VAC = "VAC";

	// public static final String MEAL_TAG = "ML/Meal Selection";
	// public static final String SEAT_TAG = "Seat Selection";
	// public static final String BAGGAGE_TAG = "BG/Baggage Selection";
	// public static final String INSURANCE_TAG = "Insurance";
	// public static final String HALA_TAG = "HALA/Hala Service Charge";

	public static boolean ISMANAGE_BOOK = false;
	public static boolean CAL_VALUE = true;
	public static String DATE = "";
	public static final String MEAL_TAG = "Meal Selection";
	public static final String SEAT_TAG = "Seat Selection";
	public static final String BAGGAGE_TAG = "Baggage Selection";
	public static final String INSURANCE_TAG = "Insurance Selection";
	public static final String HALA_TAG = "Hala Service";

	public static int GCMRegistrationAttempts ;
	public final static int MaximumGCMRegistrationAttempts = 3;
	public final static String SENDER_ID = "691128499392";
	
	public static final int MAX_PERSON = 9;
	public static final int MIN_ADULT = 1;
	public static final int MIN_CHILD = 0;
	public static final int MIN_INFANT = 0;

	public static final int EXPIRY_YEAR_LIMIT = 15;

	public static final String TRUE = "true";

	public static final String LANG_EN = "En";
	public static final String LANG_AR = "Ar";
	public static final String LANG_FR = "Fr";
	public static final String LANG_RU = "Ru";

	public static final String LoginServiceEndPoint_G9 = "LoginServiceEndPoint_G9";
	public static final String LoginServiceEndPoint_3O = "LoginServiceEndPoint_3O";
	public static final String LoginServiceEndPoint_E5 = "LoginServiceEndPoint_E5";
	public static final String LoginServiceEndPoint_9P = "LoginServiceEndPoint_9P";

	public static String[] selectedOneWayBaggage = new String[10], selectedReturnBaggage = new String[10];
	public static int[] selectedOneWayBaggagePos = new int[10], selectedReturnBaggagePos = new int[10];

	public static String country = "";
	public static String countryCode = "";
	public static String currencyCode = "";

	public static final String LANG_LOCAL_EN = "en";
	public static final String LANG_LOCAL_AR = "ar";
	public static final String LANG_LOCAL_FR = "fr";
	public static final String LANG_LOCAL_RU = "ru";

	public static final String CARD_EXP_DATE_INVALID_TEXT = "Payment rejected. Invalid Card Expiry Date";
	public static final String CARD_PAYMENT_FAIL_TEXT = "Card payment failed. Check card details.";
	public static final String SESSIONE_EXP_TEXT = "Payment rejected  [Quoted price no longer available,Please search again.]";
	public static final String BOOKING_UNCHANGED_SEGMENT_TEXT = "Selected flight information is different from the last performed price quote.";
	public static final String UNKNOWN_ERROR = "UnKnownError";

	public static final String ONE_WAY = "ONE_WAY";
	public static final String RETURN_WAY = "RETURN_WAY";

	public static String BookingStatus = "";
	public static String BookingComment = "";
	public static String BookingReferenceID_Id = "";
	public static String BookingReferenceID_Type = "";
	public static FlexiOperationsDO flexiOperationsDOCancel, flexiOperationsDOModify;
	public static BookingFlightDO bookingFlightDO;
	public static AllAirportNamesDO allAirportNamesDO;
	public static ArrayList<CurrencyDo> arrListCurrencies;
	public static PosterImagesDO imagePosterArr;
	public static Vector<CountryDO> vecCountryDO, vecCountryNationalityDO;
	public static Vector<CountryISDDO> vecCountryISDDO;
	public static AllAirportsDO allAirportsDO;
	public static Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsModify = new Vector<OriginDestinationOptionDO>();

	public static String flightType = "";

	public static Double currencyConversionFactor = 0.0D;
	public static String currentLocation = "";

	// new modification
	public final static String SetCookieTag = "Set-Cookie";
	public final static String CookieTag = "Cookie";
	public static String Cookie = "";

	public static String CurrencyCodeActual = "";
	public static String CurrencyCodeAfterExchange = "";
	public static String currentUserEmail = "";
	public static String DIR_PATH = ""; // for saving req res to sd card file

	public static Vector<AirBaggageDetailsDO> vecAirBaggageDetailsDO;
	public static AirSeatMapDO AirSeatMapDO;
	public static InsuranceQuoteDO insuranceQuoteDO;
	public static AirMealDetailsDO airMealDetailsDO;
	public static Vector<HalaDO> vecHalaDOs;
	public static LoginDO userLoginDoForMYProfile;

	public static String classType = "Y";
	public static String OriginLocation = "";
	public static String DestinationLocation = "";

	public static String OriginLocationName = "";
	public static String DestinationLocationName = "";

	public static String ArrivalTime = "";

	public static boolean isCameFromBookFlightForLocation = false;

	public static Typeface typefaceOpenSansSemiBold, typeFaceOpenSansLight, typefaceOpenSansRegular,
			typefaceHelveticaLight;

	public static String transactionIdentifier = "";

	public static boolean IsTwoWay;

	public static boolean IsReturnServiceCalled = false;

	public static final String ProjectName = "airarabia";

	public final static String GoogleProjectNumber = "765083446640";

	// Search Flight Screen
	public final static String BackButtonSearchFlight = "Back_SearchFlight_button_clicked";
	public final static String OneWayButton = "OneWay_button_clicked";
	public final static String ReturnButton = "Return_button_clicked";
	public final static String SorceButton = "Source_button_clicked";
	public final static String DestinationButton = "Destination_button_clicked";
	public final static String SearchFlightButton = "SearchFlight_button_clicked";
	public final static String DateButton = "Date_button_clicked";
	public final static String CurrencyButton = "Currency_button_clicked";
	public final static String AddAdult = "Add_adult_button_clicked";
	public final static String RemoveAdult = "Remove_adult_button_clicked";
	public final static String AddChild = "Add_child_button_clicked";
	public final static String RemoveChild = "Remove_child_button_clicked";
	public final static String AddInfant = "Add_infant_button_clicked";
	public final static String RemoveInfant = "Remove_infant_button_clicked";

	// Select Flight Screen..
	// Flight List Screen
	public final static String BackButtonFlightList = "Back_FlightList_button_clicked";
	public final static String AlertButtonFlightList = "Alert_FlightList_button_clicked";
	public final static String SelectFlightBoxFlightList = "SelectFlightBox_button_clicked";
	public final static String ContinueFlightList = "Continue_button_clicked";

	// Details View
	public static String PromoFare = "Promo_fare_clicked";
	public static String FlexiFare = "Flexi_fare_clicked";
	public static String SaverFare = "Saver_fare_clicked";
	public static String SmartFare = "Smart_fare_clicked";
	public static String SuperFare = "Super_fare_clicked";
	public static String BasicFare = "basic_fare_clicked";
	public static String ContinueDetailView = "Continue_button_clicked";
	public static String BackButtonDetailView = "Back_DetailView_button_clicked";

	// Flight Summary Screen
	public static String BackButtonFlightSummary = "Back_FlightSummary_button_clicked";
	public static String ContinueFlightSummary = "Continue_button_clicked_FlightSummary";
	public static String AlertButtonFlightSummary = "Alert_FlightSummary_button_clicked";

	// Passenger Detal Screen
	public static String BackButtonPassengerDetail = "Back_PassengerDetail_button_clicked";
	public static String ContinuePassengerDetail = "Continue_button_clicked";
	public static String LoginForFasterBooking = "LoginForFasterBooking_button_clicked";
	public static String CollectAirewards = "CollectAirewards_button_clicked";
	public static String SelectFromSaved = "SelectFromSaved_button_clicked";
	public static String TitlePassengerDetail = "Title_field_Adult_clicked";
	public static String DOBPassengerDetail = "DateOfBirth_field_Adult_clicked";
	public static String DOBChildPassengerDetail = "DateOfBirth_field_Child_clicked";
	public static String DOBPassengerDetailInfant = "DateOfBirth_field_Infant_clicked";
	public static String NationalityPassengerDetail = "Nationality_field_Adult_clicked";
	public static String NationalityPassengerDetailChild = "Nationality_field_Child_clicked";
	public static String NationalityPassengerDetailInfant = "Nationality_field_Infant_clicked";
	public static String FirstNamePassengerDetail = "FirstName_Adult_Clicked";
	public static String TitlePassengerDetailChild = "Title_field_Child_clicked";
	public static String TitlePassengerDetailInfant = "Title_field_Infant_clicked";
	public static String FirstNameAdult = "FirstName_field_adult_clicked";
	public static String FirstNameChild = "FirstName_field_child_clicked";
	public static String FirstNameInfant = "FirstName_field_infant_clicked";
	public static String LastNameAdult = "LastName_field_adult_clicked";
	public static String LastNameChild = "LastName_field_child_clicked";
	public static String LastNameInfant = "LastName_field_infant_clicked";
	public static String BackButtonContactDetail = "Back_ContactDetail_button_clicked";
	public static String ContinueContactDetail = "Continue_button_clicked_ContactInformation";
	public static String TitleFieldContactDetails = "Title_field_contactDetails_clicked";
	public static String FirstNameContactDetails = "FirstName_field_contactDetails_clicked";
	public static String LastNameContactDetails = "LastName_field_contactDetails_clicked";
	public static String NationalityContactDetails = "Nationality_field_contactDetails_clicked";
	public static String CountryOfResidenceContactDetails = "CountryOfResidence_field_contactDetails_clicked";
	public static String MobileContactDetails = "Mobile_field_contactDetails_clicked";
	public static String EmailContactDetails = "Email_field_contactDetails_clicked";
	public static String CheckBoxContactInfo = "CheckBox_clicked_contactInformation";

	// AireWards Screen
	public static String RedeemingButton = "Redeeming_button_clicked";
	public static String EarningButton = "Earning_button_clicked";
	public static String FAQButton = "Faq_button_clicked";
	public static String JoinNowButton = "JoinNow_button_clicked";
	public static String BackButtonAboutAirewards = "Back_button_aboutAirewards_clicked";
	public static String BackButtonEarning = "Back_button_earning_clicked";
	public static String BackButtonRedeeming = "Back_button_redeeming_clicked";
	public static String BackButtonFAQS = "Back_button_FAQA_clicked";
	public static String BackButtonJoinNow = "Back_button_JoinNow_clicked";
	public static String BackButtonAirewards = "Back_button_Airewards_clicked";

	// Airport Service Custom View
	public static String RemoveButton = "Remove_button_clicked";
	public static String ContinueAirportService = "Continue_button_clicked";
	public static String SelectAirportService = "Select_button_clicked";

	// Menu
	public static String HomeButton = "Home_button_clicked";

	// Booking Summary Screen
	public static String BackButtonBookingSummary = "Back_BookingSummary_button_clicked";
	public static String ContinueBookingSummary = "Continue_button_clicked_BookingSummary";
	public static String AlertButtonBookingSummary = "Alert_BookingSummary_button_clicked";
	public static String StartOverBookingSummary = "StartOver_BookingSummary_button_clicked";
	public static String FlightInfo = "FlightInformation_button_clicked";

	// Calender screen
	public static String DoneButtonCalender = "Done_button_clicked";
	public static String BackButtonCalender = "Back_Calender_button_clicked";
	public static String DateClicked = "Date_button_clicked";

	// Call Center screen
	public static String BackButtonCallCenter = "Back_CallCenter_button_clicked";
	public static String CountryButtonCallCenter = "Country_button_clicked_callCenter";
	public static String CityButtonCallCenter = "City_button_clicked_callCenter";
	public static String AlertButtonCallCenter = "Alert_CallCenter_button_clicked";

	// Check In Screen
	public static String BackButtonCheckIn = "Back_CheckIn_button_clicked";

	// Contact Us Screen
	public static String AlertButtonContactUs = "Alert_ContactUs_button_clicked";

	// Feedback Screen
	public static String BackButtonFeedback = "Back_Feedback_button_clicked";

	// FFMonth Screen
	public static String DateButtonFFMonth = "Date_button_clicked";

	// Global Screen
	public static String MenuButton = "Menu_button_clicked";
	public static String Home_Button = "home_button_clicked";
	public static String LogInButtonGlobal = "Login_button_clicked";
	public static String RegisterButtonGlobal = "Register_button_clicked";
	public static String ProfileButton = "Profile_button_clicked";
	public static String BookFlight_Button = "BookFlight_button_clicked";
	public static String ManageBooking_Button = "ManageBooking_button_clicked";
	public static String CheckIn_Button = "CheckIn_button_clicked";
	public static String Timetable_Button = "Timetable_button_clicked";
	public static String Airewards_Button = "Airewards_button_clicked";
	public static String Contact_Button = "Contact_button_clicked";
	public static String EmailUs_Button = "EmailUs_button_clicked";
	public static String AlertButtonGlobal = "Alert_button_clicked";
	public static String Setting_Button = "Setting_button_clicked";

	// Insurance Screen
	public static String AlertButtonInsurance = "Alert_button_Insurance_clicked";

	// TextFields
	public static String EmailField = "EmailField_clicked_Login";
	public static String PasswordField = "PasswordField_clicked_Login";

	// Manage Booking
	public static String BackButtonManageBooking = "Back_ManageBooking_button_clicked";
	public static String AlertManageBooking = "Alert_ManageBooking_button_clicked";

	// Meal Selection
	public static String MealsAlert = "Alert_Meals_button_clicked";

	// Menu Category
	public static String CategoryButton = "Category_button_clicked";
	public static String ConfirmButton = "Confirm_button_clicked";

	// Contact Us Screen
	public static String BackButtonContactUs = "Back_ContactUs_button_clicked";
	public static String CallCenterButton = "CallCenter_button_clicked";
	public static String OfficeLocationButton = "OfficeLocation_button_clicked";
	public static String Feedback_Button = "Feedback_button_clicked";

	// My Profile Screen
	public static String Title_Profile = "TitleField_clicked";
	public static String FirstName_Profile = "FirstNameField_clicked";
	public static String LastName_Profile = "LastNameField_clicked";
	public static String Nationality_Profile = "NationalityField_clicked";
	public static String DOB_Profile = "DOBField_clicked";
	public static String Passport_Profile = "PassportField_clicked";
	public static String Airewards_Profile = "AirewardsField_clicked";
	public static String Country_Profile = "CountryField_clicked";
	public static String Email_Profile = "EmailField_clicked";
	public static String Mobile_Profile = "MobileField_clicked";
	public static String FavDestination_Profile = "FavoriteDestination_Field_clicked";
	public static String OtherPassenger_Profile = "Saved_Passenger_Field_clicked";
	public static String SaveButtonProfile = "Save_button_clicked";
	public static String Alert_Profile = "Alert_Profile_button_clicked";

	// New Passenger Screen
	public static String Alert_NewPassenger = "Alert_NewPassenger_clicked";

	// Office Location Screen
	public static String BackButtonOfficeLocation = "Back_OfficeLocation_button_clicked";
	public static String CountryButtonOfficeLocation = "CountryButton_OfficeLocation_clicked";
	public static String CityButtonOfficeLocation = "CityButton_OfficeLocation_clicked";
	public static String Alert_OfficeLocation = "Alert_OfficeLocation_clicked";

	// Passenger Login Screen
	public static String Alert_PassengerLogin = "Alert_PassengerLogin_clicked";

	// Payment Reservation Summary
	public static String Alert_PaymentReservation = "Alert_PaymentReservation_clicked";

	// Payment Screen
	public static String BackButtonPayment = "Back_Payment_button_clicked";
	public static String PreviousButtonPayment = "Previous_button_clicked";
	public static String DoneButtonPayment = "Done_Payment_button_clicked";
	public static String ContinueButtonPayment = "Continue_Payment_button_clicked";
	public static String AlertButtonPayment = "Alert_Payment_button_clicked";
	public static String NextButtonPayment = "Next_button_clicked";
	public static String CardNumberField = "CardNumber_field_clicked";
	public static String CardExpiration = "CardExpiry_field_clicked";
	public static String CardHolderNameField = "CardHolderName_field_clicked";
	
	// Personalize Baggage Screen
	public static String OneWayBaggage = "OneWayBaggageSelected";
	public static String ReturnBaggage = "ReturnBaggageSelected";
	public static String ContinueBaggageDetail = "Continue_button_clicked_BaggageInformation";

	// Personalize Trip
	public static String BackButtonPersonalize = "Back_Personalize_button_clicked";
	public static String AlertButtonPersonalize = "Alert_Personalize_button_clicked";
	public static String ContinueButtonPersonalizeTrip = "Continue_PersonalizeTrip_button_clicked";
	public static String SeatButton = "AddButton_seat_clicked";
	public static String MealButton = "AddButton_meal_clicked";
	public static String AirportServiceButton = "AddButton_airportService_clicked";
	public static String InsuranceButton = "AddButton_insurance_clicked";
	public static String SeatButtonEdit = "EditButton_seat_clicked";
	public static String MealButtonEdit = "EditButton_Meal_clicked";
	public static String AirportServiceButtonEdit = "EditButton_AirportService_clicked";
	public static String InsuranceButtonEdit = "EditButton_Insurance_clicked";
	public static String BaggageAddButton = "AddButton_Baggage_clicked";
	public static String BaggageEditButton = "EditButton_Baggage_clicked";

	// Personalize Trip Common
	public static String BackButtonAnciallary = "Backbutton_AnciallaryPage_Clicked";
	public static String DoneButtonAnciallary = "Donebutton_AnciallaryPage_Clicked";
	public static String CloseButtonAnciallary = "Closebutton_AnciallaryPage_Clicked";
	public static String SeatConfirmButton = "Seat_confirmation_button_clicked";

	// Register Detail Screen
	public static String BackButtonRegisterDetail = "Backbutton_RegisterDetail_Clicked";

	// Register Screen
	public static String BackButtonRegister = "Backbutton_Register_Clicked";
	public static String RegisterG9Button = "RegisterWithG9_button_clicked";
	public static String Register3OButton = "RegisterWith3O_button_clicked";
	public static String RegisterE5Button = "RegisterWithE5_button_clicked";
	public static String Register9PButton = "RegisterWith9P_button_clicked";

	// Seat Selection Screen
	public static String ConfirmSeatButton = "ConfirmSeat_button_clicked";
	public static String AlertButtonSeat = "Alert_Seat_button_clicked";

	// Baggage selection screen
	public static String ContinueButtonBaggage = "Continue_button_clicked_baggage";
	public static String BaggageField = "BaggageField_clicked";

	// Select Destination
	public static String SearchFieldDestination = "SearchField_clicked";
	public static String CancleButtonDestination = "Cancel_button_clicked";
	public static String SelectAirport = "Select_Airport_clicked";

	// Select Meal Screen
	public static String RemoveButtonMeal = "Removebutton_meal_clicked";
	public static String ContinueButtonMeal = "Continuebutton_meal_clicked";
	public static String SelectMealButton = "SelectMeal_button_clicked";

	// Select Seat Screen
	public static String CrossButtonSeat = "Crossbutton_seat_clicked";
	public static String ContinueButtonSeat = "Continuebutton_seat_clicked";
	public static String SelectSeatButtonDeparting = "SelectSeat_button_Departing_clicked";
	public static String SelectSeatButtonReturning = "SelectSeat_button_Returning_clicked";

	// Setting Screen
	public static String SaveButtonSetting = "Savebutton_setting_clicked";
	public static String CountryButtonInSetting = "Countrybutton_setting_clicked";
	public static String LanguageButtonInSetting = "Languagebutton_setting_clicked";
	public static String CurrencyButtonInSetting = "Currencybutton_setting_clicked";

	// Timetable Result Screen
	public static String BackButtonTimetableResult = "Backbutton_TimetableResult_Clicked";

	// Timetable Screen
	public static String BackButtonTimetable = "Backbutton_Timetable_Clicked";
	public static String OneWayButtonTimetable = "OneWay_button_Timetable_Clicked";
	public static String ReturnButtonTimetable = "Return_button_Timetable_Clicked";
	public static String SearchButtonTimetable = "Search_button_Timetable_Clicked";
	public static String FlyingFromButton = "FlyingFrom_button_clicked_Timetable";
	public static String FlyingToButton = "FlyingTo_button_clicked_Timetable";
	public static String SelectDepartingDateTimetable = "DepartingDate_Timetable_clicked";
	public static String SelectReturningDateTimetable = "ReturningDate_Timetable_clicked";

	// Other Passenger screen

	public static String AddNewPassenger = "AddNewPassenger_button_clicked";
	public static String AdultNewPassenger = "Adult_button_clicked";
	public static String ChildNewPassenger = "Child_button_clicked";
	public static String InfantNewPassenger = "Infant_button_clicked";
	public static String DoneNewPassenger = "Done_button_clicked";
	public static String EditNewPassenger = "Edit_button_clicked";

	// Saved Passenger screen
	public static String AddFromContactList = "AddFromContactList_button_clicked";
	public static String DoneButtonNewPassenger = "Done_button_NewPassenger_clicked";
	public static String TitleNewPassenger = "Title_field_newPassenger_Clicked";
	public static String FirstNameNewPassenger = "FirstName_field_newPassenger_Clicked";
	public static String LastNameNewPassenger = "LastName_field_newPassenger_Clicked";
	public static String NationalityNewPassenger = "Title_field_newPassenger_Clicked";
	public static String DateOfBirthNewPassenger = "DateOfBirth_field_newPassenger_Clicked";
	public static String PassportNumberNewPassenger = "Title_field_newPassenger_Clicked";
	public static String AirWardsIdNewPassenger = "AirewardsId_field_newPassenger_Clicked";
	public static String BackButtonNewPassenger = "Back_button_newPassenger_clicked";

	// Currency screen

	public static String SearchFieldCurrency = "SearchField_currency_clicked";
	public static String CancleButtonCurrency = "Cancel_button_currency_clicked";
	public static String SelectCurrency = "Select_currency_clicked";

	// Date Selection in Flight List
	public static String DateselectionForReturn = "Date_selection_return_clicked";
	public static String DateselectionForOneWay = "Date_selection_oneway_clicked";

	// Menu Button actions--
	public static String MenuButtonFlightSummary = "Menu_button_FlightSummary_clicked";
	public static String MenuButtonContactInformation = "Menu_button_ContactInformation_clicked";
	public static String MenuButtonPassengerDetail = "Menu_button_PassengerInformation_clicked";
	public static String MenuButtonBookingSummary = "Menu_button_BookingSummary_clicked";
	public static String MenuButtonPayment = "Menu_button_Payment_clicked";
	public static String MenuButtonTimetable = "Menu_button_Timetable_clicked";
	public static String MenuButtonContactUs = "Menu_button_ContactUs_clicked";
	public static String MenuButtonBookFlight = "Menu_button_BookFlight_clicked";
	public static String MenuButtonDashboard = "Menu_button_Dashboard_clicked";
	public static String MenuButtonFlightList = "Menu_button_FlightList_clicked";
	public static String MenuButtonPersonalizeTrip = "Menu_button_PersonalizeYourTrip_clicked";
	public static String MenuButtonManageBooking = "Menu_button_ManageBooking_clicked";
	public static String MenuButtonAirewards = "Menu_button_Airewards_clicked";
	public static String MenuButtonCheckIn = "Menu_button_CheckIn_clicked";
	public static String MenuButtonLogIn = "Menu_button_LogIn_clicked";
	public static String MenuButtonEarning = "Menu_button_Earning_clicked";
	public static String MenuButtonRedeeming = "Menu_button_Redeeming_clicked";
	public static String MenuButtonFAQS = "Menu_button_FAQS_clicked";
	public static String MenuButtonJoinNow = "Menu_button_JoinNow_clicked";
	public static String MenuButtonSetting = "Menu_button_Setting_clicked";
	public static String MenuButtonEmailUs = "Menu_button_EmailUs_clicked";
	public static String MenuButtonLogout = "Menu_button_Logout_clicked";

	public static String DATABASE_NAME = "airarabia.sqlite";
	public static String DATABASE_PATH = "/data/data/com.winit.airarabia/";

	public static ArrayList<BundledServiceDO> arlBundledServiceDOs;
	public static String fareType = "";

	public static HashMap<String, PTC_FareBreakdownDO> hashForListOfDifferentPrice;
	public static int count = 0;
    public static int updateFaresCount = 0;

//	public static String bundledServiceName = "";

	// public static int fileNo = 0;
	// public static final String REQUEST_FILE_NAME =
	// AirArabiaApp.mContext.getExternalCacheDir().getAbsolutePath()+"/AirArabiaReq.xml";

	/// ---------------------- Office Location Comparator Done by Rahul
	/// --------------------------- ////

}

// class DateComparator implements Comparator<FlightSegmentDO> {
// @Override
// public int compare(FlightSegmentDO a, FlightSegmentDO b) {
// return (a.departureDateTimeInMillies > b.departureDateTimeInMillies) ? 1 : 0;
// }
// }