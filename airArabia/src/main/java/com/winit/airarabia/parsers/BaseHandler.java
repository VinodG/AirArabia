package com.winit.airarabia.parsers;

import org.xml.sax.helpers.DefaultHandler;

import com.winit.airarabia.webaccess.ServiceMethods;

public abstract class BaseHandler extends DefaultHandler {
	public StringBuffer buffer = new StringBuffer();; 

	public abstract Object getData();
	public abstract Object getErrorData();
	
	public static BaseHandler getParser(ServiceMethods wsMethod) {
		
		BaseHandler handler = null;
		switch (wsMethod) 
		{
			case AIR_AVAILABILITY:
				handler = new AirAvailabilityParser();
				break;
			case AIR_AVAILABILITY_RETURN:
				handler =  new ReturnFlightsParser();
				break;
			case AIR_PRICEQUOTE:
			case ANCILLARY_PRICEQUOTE:
				handler =  new AirPriceQuoteParser();
				break;
			case AIR_BAGGAGE_DETAILS:
				handler =  new AirBaggageDetailsParser();
				break;
			case AIR_MEAL_DETAILS:
				handler =  new AirMealDetailsParser();
				break;
			case AIR_SEAT_DETAILS:
				handler =  new AirSeatMapParser();
				break;
			case INSURANCE_QUOTE:
				handler =  new InsuranceQuoteParser();
				break;
			case HALA_REQ:
				handler =  new HalaReqParser();
				break;
			case CANCEL_FLIGHT:
			case AIR_BOOK:
			case AIR_PNR:
			case MODIFY_RESERVATION:
				handler =  new AirBookParser();
				break;
			case MODIFY_BOOK:
			case MODIFIED_RESQUERY:
				handler =  new ModifiedResQueryParser();
				break;
			case AIR_FLIGHT_SHEDULE:
				handler =  new AirScheduleParser();
				break;
			case LOG_PNR:
			case MODIFY_PNR:
				handler =  new PnrParser();
				break;
			case WS_CITY:
				handler =  new CityParser();
				break;
			case WS_NATIONALITIES:
				handler =  new NationalityParser();
				break;
			case WS_COUNTRYISDCODES:
				handler =  new CountryISDCodesParser();
				break;
			case WS_COUNTRYNAMES:
				handler =  new CountryNamesParser();
				break;
			case WS_OFFICELOCATIONS:
				handler =  new OfficeLocationParser();
				break;
			case WS_LOGIN:
				handler =  new LoginParser();
				break;
			case WS_POSTER_DOWNLOAD:
				handler =  new ImagesPosterParser();
				break;
			case CURRENCY_CONVERSION_SERVICE:
				handler =  new CurrencyExchangeParser();
				break;
				
		default:
			break;
		}
		return handler;
	}
	
	public String getString(String string)
	{
		if(string != null)
			return string;
		else
			return "";
	}
	
	public int strToInt(String string)
	{
		if(string == null)
			return 0;
		else
			return Integer.parseInt(string);
	}
}