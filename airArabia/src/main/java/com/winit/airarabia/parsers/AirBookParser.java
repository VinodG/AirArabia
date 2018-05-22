package com.winit.airarabia.parsers;

import java.util.Comparator;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.text.TextUtils;

import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirBookDO;
import com.winit.airarabia.objects.ETicketInfomationDO;
import com.winit.airarabia.objects.FareDO;
import com.winit.airarabia.objects.FlexiOperationsDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.ItinTotalFareDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;
import com.winit.airarabia.objects.PTC_FareBreakdownDO;
import com.winit.airarabia.objects.PassengerInfoContactDO;
import com.winit.airarabia.objects.PassengerInfoPersonDO;
import com.winit.airarabia.objects.PaymentDO;
import com.winit.airarabia.objects.RequestDO;
import com.winit.airarabia.objects.RequestParameterDO;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.utils.LogUtils;
import com.winit.airarabia.utils.QuickSort;

public class AirBookParser extends BaseHandler{

	private StringBuffer stringBuffer = null;
	private boolean isActive = false;
	private AirBookDO airBookDO;
	private Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs;
	private OriginDestinationOptionDO originDestinationOptionDO;
	private FlightSegmentDO flightSegmentDO;
	private ItinTotalFareDO itinTotalFareDO = new ItinTotalFareDO();
	private Vector<PTC_FareBreakdownDO> vecPTC_FareBreakdownDOs;
	private PTC_FareBreakdownDO ptc_FareBreakdownDO;
	private PassengerInfoContactDO passengerInfoContactDO;
	private PassengerInfoPersonDO passengerInfoPersonDO;
	private ETicketInfomationDO eTicketInfomationDO;
	private RequestDO requestDO;
	private Vector<RequestDO> vecRequestDOs;
	private Vector<PaymentDO> vecPaymentDO;
	private PaymentDO paymentDO;
	private String passengerType = "";
	private long lastDatetime = 0;
	
	private boolean isItinTotalFare = false,isPassengerFare = false,isAirTraveler = false,isCountryName = false;
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		isActive = true;
		if(localName.equalsIgnoreCase("OTA_AirBookRS"))
		{
			airBookDO = new AirBookDO();
			airBookDO.requestParameterDO = new RequestParameterDO();
			
			airBookDO.requestParameterDO.echoToken = getString(attributes.getValue("EchoToken"));
			airBookDO.requestParameterDO.primaryLangID = getString(attributes.getValue("PrimaryLangID"));
			airBookDO.requestParameterDO.sequenceNmbr = getString(attributes.getValue("SequenceNmbr"));
			airBookDO.requestParameterDO.transactionIdentifier = getString(attributes.getValue("TransactionIdentifier"));
			airBookDO.requestParameterDO.version = getString(attributes.getValue("Version"));
		}
		else if(localName.equalsIgnoreCase("OriginDestinationOptions"))
			vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();
		else if(localName.equalsIgnoreCase("OriginDestinationOption"))
			originDestinationOptionDO = new OriginDestinationOptionDO();
		else if(localName.equalsIgnoreCase("FlightSegment"))
		{
			flightSegmentDO = new FlightSegmentDO();
			flightSegmentDO.arrivalDateTime = getString(attributes.getValue("ArrivalDateTime"));
			flightSegmentDO.departureDateTime = getString(attributes.getValue("DepartureDateTime"));
			
			flightSegmentDO.departureDateTimeInMillies = CalendarUtility.getDateTimeInMillies(flightSegmentDO.departureDateTime);
			flightSegmentDO.arrivalDateTimeInMillies = CalendarUtility.getDateTimeInMillies(flightSegmentDO.arrivalDateTime);
			
			flightSegmentDO.flightNumber = getString(attributes.getValue("FlightNumber"));
			flightSegmentDO.RPH = getString(attributes.getValue("RPH"));
			flightSegmentDO.resCabinClass = getString(attributes.getValue("ResCabinClass"));
			flightSegmentDO.status = getString(attributes.getValue("Status"));
			
			if(lastDatetime == 0)
			{
				lastDatetime = flightSegmentDO.departureDateTimeInMillies;
			}
			else if(lastDatetime > flightSegmentDO.departureDateTimeInMillies)
			{
				lastDatetime = flightSegmentDO.departureDateTimeInMillies ;
			}
				
		}
		else if(localName.equalsIgnoreCase("DepartureAirport"))
		{
			flightSegmentDO.departureAirportCode = getString(attributes.getValue("LocationCode"));
			flightSegmentDO.departureAirportTerminal = getString(attributes.getValue("Terminal"));
			flightSegmentDO.departureAirportCodeContext = getString(attributes.getValue("CodeContext"));
		}
		else if(localName.equalsIgnoreCase("ArrivalAirport"))
		{
			flightSegmentDO.arrivalAirportCode = getString(attributes.getValue("LocationCode"));
			flightSegmentDO.arrivalAirportTerminal = getString(attributes.getValue("Terminal"));
			flightSegmentDO.arrivalAirportCodeContext = getString(attributes.getValue("CodeContext"));
		}
		else if(localName.equalsIgnoreCase("ItinTotalFare"))
		{
			isItinTotalFare = true;
			itinTotalFareDO = new ItinTotalFareDO();
		}
		else if(localName.equalsIgnoreCase("BaseFare"))
		{
			if(isItinTotalFare)
			{
				itinTotalFareDO.baseFare.amount = getString(attributes.getValue("Amount"));
				itinTotalFareDO.baseFare.currencyCode = getString(attributes.getValue("CurrencyCode"));
				itinTotalFareDO.baseFare.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
			}
			else if(isPassengerFare)
			{
				ptc_FareBreakdownDO.baseFare.amount = getString(attributes.getValue("Amount"));
				ptc_FareBreakdownDO.baseFare.currencyCode = getString(attributes.getValue("CurrencyCode"));
				ptc_FareBreakdownDO.baseFare.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
			}
		}
		else if(localName.equalsIgnoreCase("Tax"))
		{
			FareDO tax = new FareDO();
			tax.amount = getString(attributes.getValue("Amount"));
			tax.currencyCode = getString(attributes.getValue("CurrencyCode"));
			tax.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
			tax.taxCode = getString(attributes.getValue("TaxCode"));
			if(isItinTotalFare)
				itinTotalFareDO.vecTaxes.add(tax);
			else if(isPassengerFare)
				ptc_FareBreakdownDO.vecTaxes.add(tax);
		}
		else if(localName.equalsIgnoreCase("Fee"))
		{
			FareDO fee = new FareDO();
			fee.amount = getString(attributes.getValue("Amount"));
			fee.currencyCode = getString(attributes.getValue("CurrencyCode"));
			fee.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
			fee.feeCode = getString(attributes.getValue("FeeCode"));
			if(isItinTotalFare)
				itinTotalFareDO.vecFees.add(fee);
			else if(isPassengerFare)
				ptc_FareBreakdownDO.vecFees.add(fee);
		}
		else if(localName.equalsIgnoreCase("TotalFare"))
		{
			if(isItinTotalFare)
			{
				itinTotalFareDO.totalFare.amount = getString(attributes.getValue("Amount"));
				itinTotalFareDO.totalFare.currencyCode = getString(attributes.getValue("CurrencyCode"));
				itinTotalFareDO.totalFare.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
			}
			else if(isPassengerFare)
			{
				ptc_FareBreakdownDO.totalFare.amount = getString(attributes.getValue("Amount"));
				ptc_FareBreakdownDO.totalFare.currencyCode = getString(attributes.getValue("CurrencyCode"));
				ptc_FareBreakdownDO.totalFare.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
			}
		}
		else if(localName.equalsIgnoreCase("TotalEquivFare"))
		{
			if(isItinTotalFare)
			{
				itinTotalFareDO.totalEquivFare.amount = getString(attributes.getValue("Amount"));
				itinTotalFareDO.totalEquivFare.currencyCode = getString(attributes.getValue("CurrencyCode"));
				itinTotalFareDO.totalEquivFare.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
			}
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
			if(ptc_FareBreakdownDO != null)
			{
				ptc_FareBreakdownDO.code = getString(attributes.getValue("Code"));
				ptc_FareBreakdownDO.quantity = getString(attributes.getValue("Quantity"));
			}
		}
		else if(localName.equalsIgnoreCase("FareBasisCodes"))
			ptc_FareBreakdownDO.vecFareBasicCodes = new Vector<String>();
		else if(localName.equalsIgnoreCase("PassengerFare"))
			isPassengerFare = true;
		else if(localName.equalsIgnoreCase("TravelerRefNumber"))
		{
			if(!isAirTraveler)
				ptc_FareBreakdownDO.vecTravelerRefNumbers.add(getString(attributes.getValue("RPH")));
			else if(isAirTraveler)
				passengerInfoPersonDO.contactRPH = getString(attributes.getValue("RPH"));
		}
		else if(localName.equalsIgnoreCase("TravelerInfo"))
		{
		}
		else if(localName.equalsIgnoreCase("AirTraveler"))
		{
			passengerInfoPersonDO = new PassengerInfoPersonDO();
			passengerInfoPersonDO.persontype = getString(attributes.getValue("PassengerTypeCode"));
			isAirTraveler = true;
		}
		else if(localName.equalsIgnoreCase("Telephone"))
			passengerInfoPersonDO.personPhonenum = getString(attributes.getValue("PhoneNumber"));
		else if(localName.equalsIgnoreCase("Document"))
			passengerInfoPersonDO.personnationality = getString(attributes.getValue("DocHolderNationality"));
		else if(localName.equalsIgnoreCase("TravelerRefNumber"))
			passengerInfoPersonDO.contactRPH = getString(attributes.getValue("RPH"));
		else if(localName.equalsIgnoreCase("ETicketInfomation"))
		{
			eTicketInfomationDO = new ETicketInfomationDO();
			eTicketInfomationDO.couponNo = getString(attributes.getValue("couponNo"));
			eTicketInfomationDO.eTicketNo = getString(attributes.getValue("eTicketNo"));
			eTicketInfomationDO.flightSegmentRPH = getString(attributes.getValue("flightSegmentRPH"));
			eTicketInfomationDO.status = getString(attributes.getValue("status"));
			eTicketInfomationDO.usedStatus = getString(attributes.getValue("usedStatus"));
			passengerInfoPersonDO.vecETicketInfomationDOs.add(eTicketInfomationDO);
		}
		else if(localName.equalsIgnoreCase("SeatRequests") || 
				localName.equalsIgnoreCase("MealRequests") || 
				localName.equalsIgnoreCase("InsuranceRequests")|| 
				localName.equalsIgnoreCase("BaggageRequests"))
		{
			vecRequestDOs = new Vector<RequestDO>();
		}
		else if(localName.equalsIgnoreCase("SeatRequest"))
		{
			requestDO = new RequestDO();
			requestDO.departureDate = getString(attributes.getValue("DepartureDate"));
			requestDO.flightNumber = getString(attributes.getValue("FlightNumber"));
			requestDO.flightRefNumberRPHList = getString(attributes.getValue("FlightRefNumberRPHList"));
			requestDO.seatNumber = getString(attributes.getValue("SeatNumber"));
			requestDO.travelerRefNumberRPHList = getString(attributes.getValue("TravelerRefNumberRPHList"));
			vecRequestDOs.add(requestDO);
		}
		else if(localName.equalsIgnoreCase("MealRequest"))
		{
			requestDO = new RequestDO();
			requestDO.departureDate = getString(attributes.getValue("DepartureDate"));
			requestDO.flightNumber = getString(attributes.getValue("FlightNumber"));
			requestDO.flightRefNumberRPHList = getString(attributes.getValue("FlightRefNumberRPHList"));
			requestDO.travelerRefNumberRPHList = getString(attributes.getValue("TravelerRefNumberRPHList"));
			requestDO.mealCode = getString(attributes.getValue("mealCode"));
			requestDO.mealQuantity = getString(attributes.getValue("mealQuantity"));
			vecRequestDOs.add(requestDO);
		}
		else if(localName.equalsIgnoreCase("BaggageRequest"))
		{
			requestDO = new RequestDO();
			requestDO.departureDate = getString(attributes.getValue("DepartureDate"));
			requestDO.flightNumber = getString(attributes.getValue("FlightNumber"));
			requestDO.flightRefNumberRPHList = getString(attributes.getValue("FlightRefNumberRPHList"));
			requestDO.travelerRefNumberRPHList = getString(attributes.getValue("TravelerRefNumberRPHList"));
			requestDO.baggageCode = getString(attributes.getValue("baggageCode"));
			requestDO.baggageOndGroupId = getString(attributes.getValue("baggageOndGroupId"));
			vecRequestDOs.add(requestDO);
		}
		else if(localName.equalsIgnoreCase("InsuranceRequest"))
		{
			requestDO = new RequestDO();
			requestDO.arrivalDate = getString(attributes.getValue("ArrivalDate"));
			requestDO.departureDate = getString(attributes.getValue("DepartureDate"));
			requestDO.destination = getString(attributes.getValue("Destination"));
			requestDO.origin = getString(attributes.getValue("Origin"));
			requestDO.policyCode = getString(attributes.getValue("PolicyCode"));
			requestDO.rPH = getString(attributes.getValue("RPH"));
			requestDO.state = getString(attributes.getValue("State"));
			vecRequestDOs.add(requestDO);
		}
		else if(localName.equalsIgnoreCase("PaymentDetails"))
			vecPaymentDO = new Vector<PaymentDO>();
		else if(localName.equalsIgnoreCase("PaymentDetail"))
			paymentDO = new PaymentDO();
		else if(localName.equalsIgnoreCase("PaymentCard"))
		{
			paymentDO.cardNo = getString(attributes.getValue("CardNumber"));
			paymentDO.cardType = getString(attributes.getValue("CardType"));
		}
		else if(localName.equalsIgnoreCase("PaymentAmount"))
		{
			paymentDO.amount = getString(attributes.getValue("Amount"));
			paymentDO.currencyCode = getString(attributes.getValue("CurrencyCode"));
			paymentDO.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
		}
		else if(localName.equalsIgnoreCase("PaymentAmountInPayCur"))
		{
			paymentDO.curAmount = getString(attributes.getValue("Amount"));
			paymentDO.curCurrencyCode = getString(attributes.getValue("CurrencyCode"));
			paymentDO.curDecimalPlaces = getString(attributes.getValue("DecimalPlaces"));
		}
		else if(localName.equalsIgnoreCase("Ticketing"))
		{
			airBookDO.ticketType = getString(attributes.getValue("TicketType"));
			airBookDO.ticketingStatus = getString(attributes.getValue("TicketingStatus"));
		}
		else if(localName.equalsIgnoreCase("BookingReferenceID"))
		{
			airBookDO.bookingID = getString(attributes.getValue("ID"));
			airBookDO.bookingType = getString(attributes.getValue("Type"));
		}
		else if(localName.equalsIgnoreCase("ContactInfo"))
			passengerInfoContactDO = new PassengerInfoContactDO();
		else if(localName.equalsIgnoreCase("FlexiOperations"))
		{
			FlexiOperationsDO flexiOperationsDO = new FlexiOperationsDO();
			flexiOperationsDO.allowedOperationName = getString(attributes.getValue("AllowedOperationName"));
			flexiOperationsDO.flexiOperationCutoverTimeInMinutes = getString(attributes.getValue("FlexiOperationCutoverTimeInMinutes"));
			flexiOperationsDO.numberOfAllowedOperations = getString(attributes.getValue("NumberOfAllowedOperations"));

			if(flexiOperationsDO.allowedOperationName.equalsIgnoreCase("Cancellation"))
				airBookDO.flexiOperationsDOCancel = flexiOperationsDO;
			else if(flexiOperationsDO.allowedOperationName.equalsIgnoreCase("Modification(s)"))
					airBookDO.flexiOperationsDOModify = flexiOperationsDO;
		}
		else if(localName.equalsIgnoreCase("Error"))
		{
			String strErrorCode = getString(attributes.getValue("Code"));
			String strErrorText = getString(attributes.getValue("ShortText"));
			airBookDO.errorMessage = strErrorText;
			
		}
		/*else if(localName.equalsIgnoreCase("Error"))
		{
			String strErrorCode = getString(attributes.getValue("Code"));
			String strErrorText = getString(attributes.getValue("ShortText"));
			if(strErrorCode.equalsIgnoreCase("41"))
				airBookDO.errorMessage = "Card payment failed. Check card details.";
			else if(strErrorCode.equalsIgnoreCase("167")
					&& strErrorText.equalsIgnoreCase("Payment rejected  [Quoted price no longer available,Please search again.]"))
				airBookDO.errorMessage = strErrorText;
			else if(strErrorCode.equalsIgnoreCase("167"))
				airBookDO.errorMessage = "Payment rejected. Invalid Card Expiry Date";
			else
				airBookDO.errorMessage = "";
		}
		else if(localName.equalsIgnoreCase("Errors"))
		{
			String strErrorCode = getString(attributes.getValue("Code"));
			String strErrorText = getString(attributes.getValue("ShortText"));//Card payment failed
			if(strErrorCode.equalsIgnoreCase("41") && strErrorText.contains(AppConstants.CARD_PAYMENT_FAIL_TEXT))
				airBookDO.errorMessage = AppConstants.CARD_PAYMENT_FAIL_TEXT;
			else if(strErrorText.contains(AppConstants.CARD_EXP_DATE_INVALID_TEXT))
				airBookDO.errorMessage = AppConstants.CARD_EXP_DATE_INVALID_TEXT;
			else if(strErrorCode.equalsIgnoreCase("167")
					&& strErrorText.equalsIgnoreCase("Payment rejected  [Quoted price no longer available,Please search again]"))
				airBookDO.errorMessage = strErrorText;
			else if(!TextUtils.isEmpty(strErrorCode))
				airBookDO.errorMessage = AppConstants.UNKNOWN_ERROR;
//			else if(strErrorText.equalsIgnoreCase(""))
//				airBookDO.errorMessage = strErrorText;
		}*/
		stringBuffer = new StringBuffer();
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException 
	{
		isActive = false;
		if(localName.equalsIgnoreCase("Comment"))
			flightSegmentDO.comment = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("FlightSegment"))
		{
			if(originDestinationOptionDO.vecFlightSegmentDOs.size() > 0)
			{
				FlightSegmentDO tempObj  =  originDestinationOptionDO.vecFlightSegmentDOs.get(0);
				if(tempObj.departureDateTimeInMillies > flightSegmentDO.departureDateTimeInMillies)
					originDestinationOptionDO.vecFlightSegmentDOs.add(0,flightSegmentDO);
				else
					originDestinationOptionDO.vecFlightSegmentDOs.add(flightSegmentDO);
			}
			
			else
				originDestinationOptionDO.vecFlightSegmentDOs.add(flightSegmentDO);
			
		}
		else if(localName.equalsIgnoreCase("OriginDestinationOption"))
		{
			vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
		}
		else if(localName.equalsIgnoreCase("OriginDestinationOptions"))
		{
			LogUtils.debugLog("Before Sorting Date - ",vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs.get(0).departureDateTime);
			airBookDO.vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();
			
			QuickSort quickSort = new QuickSort();
			if (vecOriginDestinationOptionDOs.size()>1) {

				for (int i = 0; i < vecOriginDestinationOptionDOs.size(); i++) {
					if (vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs.size()>1) {
						quickSort.sort(vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs);
					}
				}
				quickSort.sortOriginDestinationOptionDO(vecOriginDestinationOptionDOs);
			}
			airBookDO.vecOriginDestinationOptionDOs.addAll(vecOriginDestinationOptionDOs);
		}
		else if(localName.equalsIgnoreCase("ItinTotalFare"))
		{
			isItinTotalFare = false;
			airBookDO.itinTotalFareDO = itinTotalFareDO;
		}
		else if(localName.equalsIgnoreCase("PassengerFare"))
			isPassengerFare = false;
		else if(localName.equalsIgnoreCase("PTC_FareBreakdown"))
			vecPTC_FareBreakdownDOs.add(ptc_FareBreakdownDO);
		else if(localName.equalsIgnoreCase("PTC_FareBreakdowns"))
			airBookDO.vecPTC_FareBreakdownDOs = vecPTC_FareBreakdownDOs;
		else if(localName.equalsIgnoreCase("GivenName"))
			passengerInfoPersonDO.personfirstname = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("Surname"))
			passengerInfoPersonDO.personlastname = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("NameTitle"))
			passengerInfoPersonDO.persontitle = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("AirTraveler"))
		{
			airBookDO.vecAirTravelers.add(passengerInfoPersonDO);
			isAirTraveler = false;
		}
		else if(localName.equalsIgnoreCase("SeatRequests"))
			airBookDO.vecSeatRequestDOs = vecRequestDOs;
		else if(localName.equalsIgnoreCase("MealRequests"))
			airBookDO.vecMealRequestDOs = vecRequestDOs;
		else if(localName.equalsIgnoreCase("BaggageRequests"))
			airBookDO.vecBaggageRequestDOs = vecRequestDOs;
		else if(localName.equalsIgnoreCase("InsuranceRequests"))
			airBookDO.vecInsuranceRequestDOs = vecRequestDOs;
		else if(localName.equalsIgnoreCase("CardHolderName"))
			paymentDO.cardHolderName = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("PaymentDetail"))
			vecPaymentDO.add(paymentDO);
		else if(localName.equalsIgnoreCase("PaymentDetails"))
			airBookDO.vecPaymentDO = vecPaymentDO;
		else if(localName.equalsIgnoreCase("TicketAdvisory"))
			airBookDO.ticketAdvisory = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("Title"))
			passengerInfoContactDO.contacttitle = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("FirstName"))
			passengerInfoContactDO.contactfirstname = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("LastName"))
			passengerInfoContactDO.contactlastname = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("PhoneNumber"))
			passengerInfoContactDO.contactphonenum = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("AreaCode"))
			passengerInfoContactDO.contactphonenumCountryCode = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("Email"))
			passengerInfoContactDO.contactemail = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("CityName"))
			passengerInfoContactDO.contactcity = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("CountryName"))
		{
			if(!isCountryName)
			{
				isCountryName = true;
				passengerInfoContactDO.countryName = stringBuffer.toString();
			}
		}
		else if(localName.equalsIgnoreCase("CountryCode"))
			passengerInfoContactDO.countryCode = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("PreferredLanguage"))
			passengerInfoContactDO.contactlanguage = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("ContactInfo"))
			airBookDO.contactInfo = passengerInfoContactDO;
		else if(localName.equalsIgnoreCase("OriginAgentCode"))
			airBookDO.originAgentCode = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("OriginSalesTerminal"))
			airBookDO.originSalesTerminal = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("PassengerTypeCode"))
			passengerType = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("PassengerTypeQuantity"))
		{
			if(passengerType.equalsIgnoreCase("ADT"))
				airBookDO.adultCount = strToInt(stringBuffer.toString());
			else if(passengerType.equalsIgnoreCase("CHD"))
				airBookDO.chdCount = strToInt(stringBuffer.toString());
			else if(passengerType.equalsIgnoreCase("INF"))
				airBookDO.infCount = strToInt(stringBuffer.toString());
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
		if(airBookDO != null)
			return airBookDO;
		else if(!airBookDO.errorMessage.equalsIgnoreCase(""))
			return airBookDO.errorMessage;
		return null;
	}
	@Override
	public Object getErrorData() {
			return null;
	}
}

/// -------------------- This comparator class is for sorting Vector of OriginDestinationOptionDo ------------------------///

class vecOriginDestOptDo implements Comparator <OriginDestinationOptionDO>
{

	@Override
	public int compare(OriginDestinationOptionDO lhs,
			OriginDestinationOptionDO rhs) {
		
		return (lhs.vecFlightSegmentDOs.get(0).departureDateTimeInMillies > rhs.vecFlightSegmentDOs.get(0).departureDateTimeInMillies) ? 1 : 0;
	}
	
}

///-----------------------------------------------------------------------------------------------------------------------//

// -------------------------- This class is for Sorting Vector FlightSegmentDo -------------------------------------------//
class VecSortFlightSegmentDo implements Comparator<FlightSegmentDO> {
	
	@Override
	public int compare(FlightSegmentDO lhs, FlightSegmentDO rhs) 
	{
		return (lhs.departureDateTimeInMillies > rhs.departureDateTimeInMillies) ? 1 : 0;
	}
	
}

//-------------------------------------------------------------------------------------------------------------------------//