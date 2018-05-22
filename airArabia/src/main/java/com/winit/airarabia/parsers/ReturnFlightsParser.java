package com.winit.airarabia.parsers;

import java.util.ArrayList;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirAvailabilityDO;
import com.winit.airarabia.objects.FareDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.OriginDestinationInformationDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;
import com.winit.airarabia.objects.PTC_FareBreakdownDO;
import com.winit.airarabia.objects.PricedItineraryDO;

public class ReturnFlightsParser extends BaseHandler{

	private StringBuffer stringBuffer = null;
	private boolean isActive = false;
	private AirAvailabilityDO airAvailabilityDO;
	private Vector<OriginDestinationInformationDO> vecOriginDestinationInformationDOs;
	private OriginDestinationInformationDO originDestinationInformationDO;
	private Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs;
	private OriginDestinationOptionDO originDestinationOptionsDO;
	private FlightSegmentDO flightSegmentDO;
	
	private ArrayList<FlightSegmentDO> arrDepartureFlightSegmentDO;
	private ArrayList<FlightSegmentDO> arrReturnFlightSegmentDO;
	
	private Vector<PricedItineraryDO> vecPricedItineraryDOs;
	private PricedItineraryDO pricedItineraryDO;
	private Vector<PTC_FareBreakdownDO> vecPTC_FareBreakdownDOs;
	private PTC_FareBreakdownDO ptc_FareBreakdownDO;
	private boolean isOrgDesInfo = false;
	private boolean isPassengerFare = false;
	private boolean isDeparture = false;
	private boolean isArrival = false;
	private boolean PricedItineraries = false;
	
	private ArrayList<String> departureArrivalFlights;
	private String departureFlights = "";
	
	private ArrayList<String> returnArrivalFlights;
	private String returnFlights = "";
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		isActive = true;
		if(localName.equalsIgnoreCase("OTA_AirAvailRS"))
		{
			departureArrivalFlights = new ArrayList<String>();
			arrDepartureFlightSegmentDO = new ArrayList<FlightSegmentDO>();
			
			returnArrivalFlights = new ArrayList<String>();
			arrReturnFlightSegmentDO = new ArrayList<FlightSegmentDO>();
			
			
			airAvailabilityDO = new AirAvailabilityDO();
			vecOriginDestinationInformationDOs = new Vector<OriginDestinationInformationDO>();
			airAvailabilityDO.echoToken = getString(attributes.getValue("EchoToken"));
			airAvailabilityDO.primaryLangID = getString(attributes.getValue("PrimaryLangID"));
			airAvailabilityDO.sequenceNmbr = getString(attributes.getValue("SequenceNmbr"));
			airAvailabilityDO.transactionIdentifier = getString(attributes.getValue("TransactionIdentifier"));
			airAvailabilityDO.version = getString(attributes.getValue("Version"));
		}
		else if(localName.equalsIgnoreCase("OriginDestinationInformation"))
		{
			originDestinationInformationDO = new OriginDestinationInformationDO();
			isOrgDesInfo = true;
		}
		else if(localName.equalsIgnoreCase("OriginLocation"))
			originDestinationInformationDO.originLocationCode = getString(attributes.getValue("LocationCode"));
		else if(localName.equalsIgnoreCase("DestinationLocation"))
			originDestinationInformationDO.destinationLocationCode = getString(attributes.getValue("LocationCode"));
		else if(localName.equalsIgnoreCase("OriginDestinationOptions")){
			vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();
			departureFlights = "";
			returnFlights = "";
			
		}
		else if(localName.equalsIgnoreCase("OriginDestinationOption")){
			originDestinationOptionsDO = new OriginDestinationOptionDO();
		}
		else if(localName.equalsIgnoreCase("FlightSegment"))
		{
			flightSegmentDO = new FlightSegmentDO();
			flightSegmentDO.arrivalDateTime = getString(attributes.getValue("ArrivalDateTime"));
			flightSegmentDO.departureDateTime = getString(attributes.getValue("DepartureDateTime"));
			flightSegmentDO.flightNumber = getString(attributes.getValue("FlightNumber"));
			flightSegmentDO.journeyDuration = getString(attributes.getValue("JourneyDuration"));
			flightSegmentDO.RPH = getString(attributes.getValue("RPH"));
		}
		else if(localName.equalsIgnoreCase("DepartureAirport"))
		{
			flightSegmentDO.departureAirportCode = getString(attributes.getValue("LocationCode"));
			flightSegmentDO.departureAirportTerminal = getString(attributes.getValue("Terminal"));
		}
		else if(localName.equalsIgnoreCase("ArrivalAirport"))
		{
			flightSegmentDO.arrivalAirportCode = getString(attributes.getValue("LocationCode"));
			flightSegmentDO.arrivalAirportTerminal = getString(attributes.getValue("Terminal"));
		}
		else if(localName.equalsIgnoreCase("PricedItineraries")) {
			vecPricedItineraryDOs = new Vector<PricedItineraryDO>();
			PricedItineraries = true;
		}
		else if(localName.equalsIgnoreCase("PricedItinerary"))
		{
			pricedItineraryDO = new PricedItineraryDO();
			pricedItineraryDO.sequenceNumber = getString(attributes.getValue("SequenceNumber"));
		}
		else if(localName.equalsIgnoreCase("AirItineraryPricingInfo"))
			pricedItineraryDO.pricingSource = getString(attributes.getValue("PricingSource"));
		else if(localName.equalsIgnoreCase("BaseFare"))
		{
			if(!isPassengerFare)
			{
				pricedItineraryDO.baseFare.amount = getString(attributes.getValue("Amount"));
				pricedItineraryDO.baseFare.currencyCode = getString(attributes.getValue("CurrencyCode"));
				pricedItineraryDO.baseFare.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
				AppConstants.CurrencyCodeActual = pricedItineraryDO.baseFare.currencyCode;
			}
			else
			{
				ptc_FareBreakdownDO.baseFare.amount = getString(attributes.getValue("Amount"));
				ptc_FareBreakdownDO.baseFare.currencyCode = getString(attributes.getValue("CurrencyCode"));
				ptc_FareBreakdownDO.baseFare.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
				AppConstants.CurrencyCodeActual = pricedItineraryDO.baseFare.currencyCode;
			}
		}
		else if(localName.equalsIgnoreCase("TotalFare"))
		{
			if(!isPassengerFare)
			{
				pricedItineraryDO.totalFare.amount = getString(attributes.getValue("Amount"));
				pricedItineraryDO.totalFare.currencyCode = getString(attributes.getValue("CurrencyCode"));
				pricedItineraryDO.totalFare.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
			}
			else
			{
				ptc_FareBreakdownDO.totalFare.amount = getString(attributes.getValue("Amount"));
				ptc_FareBreakdownDO.totalFare.currencyCode = getString(attributes.getValue("CurrencyCode"));
				ptc_FareBreakdownDO.totalFare.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
			}
		}
		else if(localName.equalsIgnoreCase("TotalEquivFare"))
		{
			pricedItineraryDO.totalEquivFare.amount = getString(attributes.getValue("Amount"));
			pricedItineraryDO.totalEquivFare.currencyCode = getString(attributes.getValue("CurrencyCode"));
			pricedItineraryDO.totalEquivFare.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
		}
		else if(localName.equalsIgnoreCase("TotalFareWithCCFee"))
		{
			pricedItineraryDO.totalFareWithCCFee.amount = getString(attributes.getValue("Amount"));
			pricedItineraryDO.totalFareWithCCFee.currencyCode = getString(attributes.getValue("CurrencyCode"));
			pricedItineraryDO.totalFareWithCCFee.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
		}
		else if(localName.equalsIgnoreCase("TotalEquivFareWithCCFee"))
		{
			pricedItineraryDO.totalEquivFareWithCCFee.amount = getString(attributes.getValue("Amount"));
			pricedItineraryDO.totalEquivFareWithCCFee.currencyCode = getString(attributes.getValue("CurrencyCode"));
			pricedItineraryDO.totalEquivFareWithCCFee.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
		}
		else if(localName.equalsIgnoreCase("PTC_FareBreakdowns"))
			vecPTC_FareBreakdownDOs = new Vector<PTC_FareBreakdownDO>();
		else if(localName.equalsIgnoreCase("PTC_FareBreakdown"))
		{
			ptc_FareBreakdownDO = new PTC_FareBreakdownDO();
			ptc_FareBreakdownDO.pricingSource = getString(attributes.getValue("PricingSource"));
		}
		else if(localName.equalsIgnoreCase("PassengerTypeQuantity"))
		{
			ptc_FareBreakdownDO.code = getString(attributes.getValue("Code"));
			ptc_FareBreakdownDO.quantity = getString(attributes.getValue("Quantity"));
		}
		else if(localName.equalsIgnoreCase("FareBasisCodes"))
			ptc_FareBreakdownDO.vecFareBasicCodes = new Vector<String>();
		else if(localName.equalsIgnoreCase("PassengerFare"))
			isPassengerFare = true;
		else if(localName.equalsIgnoreCase("Tax"))
		{
			FareDO tax = new FareDO();
			tax.amount = getString(attributes.getValue("Amount"));
			tax.currencyCode = getString(attributes.getValue("CurrencyCode"));
			tax.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
			tax.taxCode = getString(attributes.getValue("TaxCode"));
			tax.taxName = getString(attributes.getValue("TaxName"));
			ptc_FareBreakdownDO.vecTaxes.add(tax);
		}
		else if(localName.equalsIgnoreCase("Fee"))
		{
			FareDO fee = new FareDO();
			fee.amount = getString(attributes.getValue("Amount"));
			fee.currencyCode = getString(attributes.getValue("CurrencyCode"));
			fee.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
			fee.feeCode = getString(attributes.getValue("FeeCode"));
			ptc_FareBreakdownDO.vecFees.add(fee);
		}
		else if(localName.equalsIgnoreCase("TravelerRefNumber"))
			ptc_FareBreakdownDO.vecTravelerRefNumbers.add(getString(attributes.getValue("RPH")));
		stringBuffer = new StringBuffer();
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException 
	{
		isActive = false;
		if(localName.equalsIgnoreCase("DepartureDateTime"))
			originDestinationInformationDO.departureDateTime = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("ArrivalDateTime"))
			originDestinationInformationDO.arrivalDateTime = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("OriginLocation"))
			originDestinationInformationDO.originLocation = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("DestinationLocation"))
			originDestinationInformationDO.destinationLocation = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("FlightSegment")) {
			
			if(!PricedItineraries) {
				if(flightSegmentDO.departureAirportCode.equalsIgnoreCase(AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode) || 
						flightSegmentDO.arrivalAirportCode.equalsIgnoreCase(AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode)) {
//					originDestinationInformationDO.departureFlightSegments.add(flightSegmentDO);
					isDeparture = true;
					isArrival = false;
					arrDepartureFlightSegmentDO.add(flightSegmentDO);
//					departureFlights = departureFlights+flightSegmentDO.departureAirportCode + flightSegmentDO . departureDateTime + flightSegmentDO.arrivalDateTime;
					departureFlights = departureFlights+flightSegmentDO.flightNumber ;
				}
				
				if(flightSegmentDO.departureAirportCode.equalsIgnoreCase(AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode) || 
						flightSegmentDO.arrivalAirportCode.equalsIgnoreCase(AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode)) {
					isDeparture = false;
					isArrival = true;
					arrReturnFlightSegmentDO.add(flightSegmentDO);
					returnFlights = returnFlights + flightSegmentDO.flightNumber ;
										
//					originDestinationInformationDO.arrivalFlightSegments.add(flightSegmentDO);
//					originDestinationOptionsDO.vecFlightSegmentDOs.add(flightSegmentDO);
				}
			}
		}
		else if(localName.equalsIgnoreCase("OriginDestinationOption")) {
			if(!PricedItineraries && isDeparture && !isArrival) {
				originDestinationInformationDO.departureVecOriginDestinationOptionDO.add(originDestinationOptionsDO);
			}
			else if(!PricedItineraries && !isDeparture && isArrival) {
				originDestinationInformationDO.arrivalVecOriginDestinationOptionDOs.add(originDestinationOptionsDO);
			}
			vecOriginDestinationOptionDOs.add(originDestinationOptionsDO);
		}
		else if(localName.equalsIgnoreCase("OriginDestinationOptions"))
		{
			if(isOrgDesInfo)
				originDestinationInformationDO.vecOriginDestinationOptionDOs = vecOriginDestinationOptionDOs;
			else 
				pricedItineraryDO.vecOriginDestinationOptionDOs = vecOriginDestinationOptionDOs;
			
			if(!departureArrivalFlights.contains(departureFlights)){
				departureArrivalFlights.add(departureFlights);
				originDestinationInformationDO.departureFlightSegments.addAll(arrDepartureFlightSegmentDO);
			}
			
			if(!returnArrivalFlights.contains(returnFlights)){
				returnArrivalFlights.add(returnFlights);
				originDestinationInformationDO.arrivalFlightSegments.addAll(arrReturnFlightSegmentDO);
			}
			arrDepartureFlightSegmentDO = new ArrayList<FlightSegmentDO>();
			arrReturnFlightSegmentDO = new ArrayList<FlightSegmentDO>();
		}
		else if(localName.equalsIgnoreCase("OriginDestinationInformation"))
		{
			vecOriginDestinationInformationDOs.add(originDestinationInformationDO);
			isOrgDesInfo = false;
		}
		else if(localName.equalsIgnoreCase("FareBasisCode"))
			ptc_FareBreakdownDO.vecFareBasicCodes.add(stringBuffer.toString());
		else if(localName.equalsIgnoreCase("PassengerFare"))
			isPassengerFare = false;
		else if(localName.equalsIgnoreCase("PTC_FareBreakdown"))
			vecPTC_FareBreakdownDOs.add(ptc_FareBreakdownDO);
		else if(localName.equalsIgnoreCase("PTC_FareBreakdowns"))
			pricedItineraryDO.vecPTC_FareBreakdownDOs = vecPTC_FareBreakdownDOs;
		else if(localName.equalsIgnoreCase("PricedItinerary"))
			vecPricedItineraryDOs.add(pricedItineraryDO);
		else if(localName.equalsIgnoreCase("OTA_AirAvailRS"))
		{
			airAvailabilityDO.vecOriginDestinationInformationDOs = vecOriginDestinationInformationDOs;
			airAvailabilityDO.vecPricedItineraryDOs = vecPricedItineraryDOs;
		}
	}
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException 
	{
		if(isActive)
		{
			try {
				stringBuffer.append(ch,start,length);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public Object getData() 
	{
		if(airAvailabilityDO != null)
			return airAvailabilityDO;
		else
			return null;
	}
	@Override
	public Object getErrorData() {
			return null;
	}

}
