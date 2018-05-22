package com.winit.airarabia.busynesslayer;

import java.util.Vector;

import android.content.Context;

import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.BookFlightDO;
import com.winit.airarabia.objects.BookingModificationDO;
import com.winit.airarabia.objects.FlexiOperationsDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;
import com.winit.airarabia.objects.PassengerInfoDO;
import com.winit.airarabia.objects.PassengerInfoPersonDO;
import com.winit.airarabia.objects.PaymentDO;
import com.winit.airarabia.objects.RequestDO;
import com.winit.airarabia.objects.RequestParameterDO;
import com.winit.airarabia.webaccess.BaseWA;
import com.winit.airarabia.webaccess.BuildXMLRequest;
import com.winit.airarabia.webaccess.ServiceMethods;

public class CommonBL extends BaseBL
{

	public CommonBL(Context mContext, DataListener listener)
	{
		super(mContext, listener);
	}
	
	public boolean getAirportsData()
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.AIR_PORT_SDATA, "AIR_PORT_SDATA","");
	}
	public boolean getAirAvailability(BookFlightDO bookFlightDO,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.AIR_AVAILABILITY, BuildXMLRequest.getAirAvailability(bookFlightDO,vecOriginDestinationOptionDOs),bookFlightDO.srvUrlType);
	}
	public boolean getAirAvailabilityReturn(BookFlightDO bookFlightDO,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.AIR_AVAILABILITY, BuildXMLRequest.getAirAvailability(bookFlightDO,vecOriginDestinationOptionDOs),bookFlightDO.srvUrlType);
	}
	// New for Return Flights
	public boolean getReturnAirAvailability(BookFlightDO bookFlightDO,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs, String Origin, String Destination, String ArrivalTime)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.AIR_AVAILABILITY_RETURN,
				BuildXMLRequest.getAirAvailabilityReturn(bookFlightDO,vecOriginDestinationOptionDOs, Origin,Destination, ArrivalTime),bookFlightDO.srvUrlType);
	}
	public boolean getAirPriceQuote(RequestParameterDO requestParameterDO,String airportCode,BookFlightDO bookFlightDO,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsReturn,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsNew,boolean isFlexiOut, boolean isFlexiIn,String bookID,String bookType,FlexiOperationsDO flexiOperationsDOCancel, FlexiOperationsDO flexiOperationsDOModify,String BundleServiceID)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.AIR_PRICEQUOTE, BuildXMLRequest.getAirPriceQuote(requestParameterDO, airportCode, bookFlightDO, vecOriginDestinationOptionDOs,vecOriginDestinationOptionDOsReturn, vecOriginDestinationOptionDOsNew, null, null, null, null, null, isFlexiOut, isFlexiIn, bookID, bookType, flexiOperationsDOCancel, flexiOperationsDOModify,BundleServiceID),bookFlightDO.srvUrlType);
	}
	
	public boolean getAncillaryPriceQuote(RequestParameterDO requestParameterDO,String airportCode,BookFlightDO bookFlightDO,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsReturn,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsNew,Vector<RequestDO> vecMealRequestDOs,Vector<RequestDO> vecBaggageRequestDOs,Vector<RequestDO> vecInsrRequestDOs,Vector<RequestDO> vecSeatRequestDOs,Vector<RequestDO> vecHalaRequestDOs,boolean isFlexiOut, boolean isFlexiIn,String bookID,String bookType,FlexiOperationsDO flexiOperationsDOCancel, FlexiOperationsDO flexiOperationsDOModify,String BundleServiceID)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.ANCILLARY_PRICEQUOTE, BuildXMLRequest.getAirPriceQuote(requestParameterDO, airportCode, bookFlightDO, vecOriginDestinationOptionDOs,vecOriginDestinationOptionDOsReturn,vecOriginDestinationOptionDOsNew,vecMealRequestDOs,vecBaggageRequestDOs,vecInsrRequestDOs,vecSeatRequestDOs,vecHalaRequestDOs,isFlexiOut, isFlexiIn, bookID, bookType, flexiOperationsDOCancel, flexiOperationsDOModify,BundleServiceID),bookFlightDO.srvUrlType);
	}
	public boolean getAirBaggageDetails(RequestParameterDO requestParameterDO,String currencyCode,Vector<FlightSegmentDO> vecFlightSegmentDOs)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.AIR_BAGGAGE_DETAILS, BuildXMLRequest.getAirBaggageDetails(requestParameterDO, currencyCode, vecFlightSegmentDOs),requestParameterDO.srvUrlType);
	}
	public boolean getAirMealDetails(RequestParameterDO requestParameterDO,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.AIR_MEAL_DETAILS, BuildXMLRequest.getAirMealDetails(requestParameterDO, vecOriginDestinationOptionDOs),requestParameterDO.srvUrlType);
	}
	public boolean getAirSeatMap(RequestParameterDO requestParameterDO,Vector<FlightSegmentDO> vecFlightSegmentDOs)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.AIR_SEAT_DETAILS, BuildXMLRequest.getAirSeatMap(requestParameterDO, vecFlightSegmentDOs),requestParameterDO.srvUrlType);
	}
	public boolean getInsuranceQuote(RequestParameterDO requestParameterDO,String travelType,Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDOs,FlightSegmentDO flightSegmentDO)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.INSURANCE_QUOTE, BuildXMLRequest.getInsuranceQuote(requestParameterDO, travelType, vecPassengerInfoPersonDOs, flightSegmentDO),requestParameterDO.srvUrlType);
	}
	public boolean getHalaReq(RequestParameterDO requestParameterDO,Vector<FlightSegmentDO> vecFlightSegmentDOs)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.HALA_REQ, BuildXMLRequest.getHalaReq(requestParameterDO, vecFlightSegmentDOs),requestParameterDO.srvUrlType);
	}
	public boolean getAirBook(RequestParameterDO requestParameterDO,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs,PassengerInfoDO passengerInfoDO,PaymentDO paymentDO,Vector<RequestDO> vecMealRequestDOs,Vector<RequestDO> vecBaggageRequestDOs,Vector<RequestDO> vecInsrRequestDOs,Vector<RequestDO> vecSeatRequestDOs,Vector<RequestDO> vecHalaRequestDOs)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.AIR_BOOK, BuildXMLRequest.getAirBook(requestParameterDO, vecOriginDestinationOptionDOs, passengerInfoDO, paymentDO, vecMealRequestDOs, vecBaggageRequestDOs, vecInsrRequestDOs, vecSeatRequestDOs, vecHalaRequestDOs),requestParameterDO.srvUrlType);
	}
	
	public boolean getCancelFlight(RequestParameterDO requestParameterDO,String PNR, String pnrType )
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.AIR_PNR, BuildXMLRequest.getCancelFlight(requestParameterDO,PNR,pnrType),requestParameterDO.srvUrlType);
	}
	
	public boolean getCancelFlightConfirm(RequestParameterDO requestParameterDO,String PNR, String pnrType )
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.MODIFY_RESERVATION, BuildXMLRequest.getCancelFlightConfirm(requestParameterDO,PNR,pnrType),requestParameterDO.srvUrlType);
	}
	
	public boolean getAirPNRData(RequestParameterDO requestParameterDO,String strPNR)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.AIR_PNR, BuildXMLRequest.getAirPNRData(requestParameterDO, strPNR),requestParameterDO.srvUrlType);
	}
	public boolean getAirFlightSchedule(RequestParameterDO requestParameterDO,BookFlightDO bookFlightDO)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.AIR_FLIGHT_SHEDULE, BuildXMLRequest.getAirFlightSchedule(requestParameterDO, bookFlightDO),requestParameterDO.srvUrlType);
	}
	public boolean getLogPNR(BookingModificationDO bookingModificationDO/*,Vector<BookingModificationDO> vecModificationDOs*/)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.LOG_PNR, BuildXMLRequest.getLogPNR(bookingModificationDO/*,vecModificationDOs*/),"");
	}
	public boolean getLogModifyPNR(BookingModificationDO bookingModificationDO)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.MODIFY_PNR, BuildXMLRequest.getLogModifyPNR(bookingModificationDO),"");
	}
	public boolean getModifiedResQuery(RequestParameterDO requestParameterDO,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsNew,String pnr,String pnrType,String modificationType)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.MODIFIED_RESQUERY, BuildXMLRequest.getAirBookModify(requestParameterDO, vecOriginDestinationOptionDOs, vecOriginDestinationOptionDOsNew, null, pnr, pnrType, modificationType),requestParameterDO.srvUrlType);
	}
	public boolean getModifyReservation(RequestParameterDO requestParameterDO,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsNew,PaymentDO paymentDO,String pnr,String pnrType,String modificationType)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.MODIFY_RESERVATION, BuildXMLRequest.getAirBookModify(requestParameterDO, vecOriginDestinationOptionDOs,vecOriginDestinationOptionDOsNew, paymentDO, pnr, pnrType, modificationType),requestParameterDO.srvUrlType);
	}
	public boolean getCityData()
	{
//		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_CITY, R.raw.city);
		
//		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_CITY, BuildXMLRequest.getCities(),"");
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_CITY, "","");
	}
	public boolean getNationalitiesData()
	{
//		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_NATIONALITIES, R.raw.countries);
//		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_NATIONALITIES, BuildXMLRequest.getNationalities(),"");
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_NATIONALITIES, "","");
	}
//	public boolean getCountryCodesData()
//	{
//		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_COUNTRY_CODES, R.raw.country_codes);
//	}
	public boolean getCountryISDCodesData()
	{
//		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_COUNTRYISDCODES, R.raw.countryisdcodes);
//		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_COUNTRYISDCODES, BuildXMLRequest.getCountryIsdCodes(),"");
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_COUNTRYISDCODES, "","");
	}
	public boolean getCountryNamesData()
	{
//		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_COUNTRYNAMES, R.raw.countrynames);
//		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_COUNTRYNAMES, BuildXMLRequest.getCountryNames(),"");
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_COUNTRYNAMES, "","");
	}
	public boolean getOfficeLocationData()
	{
//		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_OFFICELOCATIONS, R.raw.officelocations);
//		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_OFFICELOCATIONS, BuildXMLRequest.getOfficeLocations(),"");
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_OFFICELOCATIONS, "","");
	}
	public boolean getLogin(String userName,String password,String requestURLType, String gcm)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_LOGIN, BuildXMLRequest.getLogin(userName, password,requestURLType, gcm),AppConstants.SERVICE_URL_TYPE_EMPTY, requestURLType);
	}

	public boolean getBannerImages()
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_POSTER_DOWNLOAD, BuildXMLRequest.getPosterImages(),AppConstants.SERVICE_URL_TYPE_EMPTY);
	}
	
	public boolean getCurrencyFactor(String currencyFrom, String currencyTo)
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.CURRENCY_CONVERSION_SERVICE, BuildXMLRequest.getCurrencyExchangeRate(currencyFrom, currencyTo),AppConstants.SERVICE_URL_TYPE_EMPTY);
	}
	
	public boolean getAirPriceQuoteReturn(RequestParameterDO requestParameterDO,String airportCode,BookFlightDO bookFlightDO,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsReturn,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsNew,boolean isFlexiOut, boolean isFlexiIn,String bookID,String bookType,FlexiOperationsDO flexiOperationsDOCancel, FlexiOperationsDO flexiOperationsDOModify,String BundleServiceID, Vector<FlightSegmentDO> departure , Vector<FlightSegmentDO> arr )
	{
		return new BaseWA(mContext,this).startDataDownload(ServiceMethods.AIR_PRICEQUOTE, BuildXMLRequest.getAirPriceQuoteForRetrun(requestParameterDO, airportCode, bookFlightDO, vecOriginDestinationOptionDOs,vecOriginDestinationOptionDOsReturn, vecOriginDestinationOptionDOsNew, null, null, null, null, null, isFlexiOut, isFlexiIn, bookID, bookType, flexiOperationsDOCancel, flexiOperationsDOModify,BundleServiceID, departure, arr),bookFlightDO.srvUrlType);
	}

}
