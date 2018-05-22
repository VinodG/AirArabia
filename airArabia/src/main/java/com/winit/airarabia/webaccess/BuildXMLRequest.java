package com.winit.airarabia.webaccess;

import java.util.Vector;

import android.text.TextUtils;

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
import com.winit.airarabia.utils.CalendarUtility;

public class BuildXMLRequest {
    // for login only
    public static String SOAP_HEADER_LOGIN_1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
            + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
            + "xmlns:log=\"http://www.isa.ae/api/login\">" + "<soapenv:Header><wsse:Security "
            + "xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" "
            + "xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">"
            + "<wsse:UsernameToken wsu:Id=\"UsernameToken-19\">" + "<wsse:Username>";

    public static String SOAP_HEADER_LOGIN_2 = "</wsse:Username>"
            + "<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">";

    public static String SOAP_HEADER_LOGIN_3 = "</wsse:Password>"
            + "<wsse:Nonce EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">x1V/TRUquyQGuHn3amiOZA==</wsse:Nonce>"
            + "<wsu:Created>2013-09-27T12:02:10.949Z</wsu:Created>"
            + "</wsse:UsernameToken></wsse:Security></soapenv:Header>";

    public static String SOAP_FOOTER_LOGIN = "</soapenv:Body>" + "</soapenv:Envelope>";
    // for login only

    public static String SOAP_HEADER_1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
            + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" "
            + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" + "<soap:Header>"
            + "<wsse:Security soap:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">"
            + "<wsse:UsernameToken wsu:Id=\"UsernameToken-17855236\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">"
            + "<wsse:Username>";
    public static String SOAP_HEADER_2 = "</wsse:Username>"
            + "<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">";

    public static String SOAP_HEADER_3 = "</wsse:Password>" + "</wsse:UsernameToken>" + "</wsse:Security>"
            + "</soap:Header>";

    public static String SOAP_HEADER_SUB1 = "<soap:Body xmlns:ns2=\"http://www.opentravel.org/OTA/2003/05\">";
    public static String SOAP_HEADER_SUB2 = "<soap:Body xmlns:ns2=\"http://www.opentravel.org/OTA/2003/05\" xmlns:ns1=\"http://www.isaaviation.com/thinair/webservices/OTA/Extensions/2003/05\" >";

    public static String SOAP_HEADER_LOCAL = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
            + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
            + "<soap:Body>";

    public static String SOAP_FOOTER = "</soap:Body>" + "</soap:Envelope>";
    public static String SOAP_ACTION_URL = "http://tempuri.org/";

    private static String getSoapHeader(String srvUrlType, String requestURLType) {
        AppConstants.flightType = srvUrlType;
        if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_G9))
            return SOAP_HEADER_1 + ServiceURLs.ServiceUserName_G9 + SOAP_HEADER_2 + ServiceURLs.ServicePassword_G9
                    + SOAP_HEADER_3;
        else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_3O)) {
            if (AppConstants.CurrencyCodeAfterExchange.equalsIgnoreCase("MAD"))
                return SOAP_HEADER_1 + ServiceURLs.ServiceUserNameNew_3O + SOAP_HEADER_2
                        + ServiceURLs.ServicePasswordNew_3O + SOAP_HEADER_3;
            else
                return SOAP_HEADER_1 + ServiceURLs.ServiceUserName_3O + SOAP_HEADER_2 + ServiceURLs.ServicePassword_3O
                        + SOAP_HEADER_3;
        } else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_E5))
            return SOAP_HEADER_1 + ServiceURLs.ServiceUserName_E5 + SOAP_HEADER_2 + ServiceURLs.ServicePassword_E5
                    + SOAP_HEADER_3;
        else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_9P))
            return SOAP_HEADER_1 + ServiceURLs.ServiceUserName_9P + SOAP_HEADER_2 + ServiceURLs.ServicePassword_9P
                    + SOAP_HEADER_3;
        else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_EMPTY)) {
            String ServiceUserName = "", servicePassword = "";
            if (requestURLType.equalsIgnoreCase(AppConstants.LoginServiceEndPoint_G9)) {
                ServiceUserName = ServiceURLs.LoginServiceUserName_G9;
                servicePassword = ServiceURLs.LoginServicePassword_G9;
            } else if (requestURLType.equalsIgnoreCase(AppConstants.LoginServiceEndPoint_3O)) {
                ServiceUserName = ServiceURLs.LoginServiceUserName_3O;
                servicePassword = ServiceURLs.LoginServicePassword_3O;
            } else if (requestURLType.equalsIgnoreCase(AppConstants.LoginServiceEndPoint_E5)) {
                ServiceUserName = ServiceURLs.LoginServiceUserName_E5;
                servicePassword = ServiceURLs.LoginServicePassword_E5;
            } else if (requestURLType.equalsIgnoreCase(AppConstants.LoginServiceEndPoint_9P)) {
                ServiceUserName = ServiceURLs.LoginServiceUserName_9P;
                servicePassword = ServiceURLs.LoginServicePassword_9P;
            }
            return SOAP_HEADER_LOGIN_1 + ServiceUserName + SOAP_HEADER_LOGIN_2 + servicePassword + SOAP_HEADER_LOGIN_3;
        } else
            return "";
    }

    private static String getSoapHeader(String srvUrlType) {
        AppConstants.flightType = srvUrlType;
        if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_G9))
            return SOAP_HEADER_1 + ServiceURLs.ServiceUserName_G9 + SOAP_HEADER_2 + ServiceURLs.ServicePassword_G9
                    + SOAP_HEADER_3;
        else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_3O)) {
            if (AppConstants.CurrencyCodeAfterExchange.equalsIgnoreCase("MAD")) {
                return SOAP_HEADER_1 + ServiceURLs.ServiceUserNameNew_3O + SOAP_HEADER_2
                        + ServiceURLs.ServicePasswordNew_3O + SOAP_HEADER_3;
            } else
                return SOAP_HEADER_1 + ServiceURLs.ServiceUserName_3O + SOAP_HEADER_2 + ServiceURLs.ServicePassword_3O
                        + SOAP_HEADER_3;
        } else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_E5))
            return SOAP_HEADER_1 + ServiceURLs.ServiceUserName_E5 + SOAP_HEADER_2 + ServiceURLs.ServicePassword_E5
                    + SOAP_HEADER_3;
        else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_9P))
            return SOAP_HEADER_1 + ServiceURLs.ServiceUserName_9P + SOAP_HEADER_2 + ServiceURLs.ServicePassword_9P
                    + SOAP_HEADER_3;
        else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_EMPTY)) {
            return SOAP_HEADER_LOGIN_1 + ServiceURLs.ServiceUserName_LOGIN + SOAP_HEADER_LOGIN_2
                    + ServiceURLs.ServicePassword_LOGIN + SOAP_HEADER_LOGIN_3;
        } else
            return "";
    }

    // FlightRefNumberRPHList JSessionID RPH

    private static String getRequesterId(String srvUrlType) {
        AppConstants.flightType = srvUrlType;
        if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_G9))
            return ServiceURLs.ServiceUserName_G9;
        else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_3O)) {
            if (AppConstants.CurrencyCodeAfterExchange.equalsIgnoreCase("MAD"))
                return ServiceURLs.ServiceUserNameNew_3O;
            else
                return ServiceURLs.ServiceUserName_3O;
        } else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_E5))
            return ServiceURLs.ServiceUserName_E5;
        else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_9P))
            return ServiceURLs.ServiceUserName_9P;
        else
            return "";
    }

    // Old Request
    /*
     * public static String getAirAvailability(BookFlightDO bookFlightDO
	 * ,Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs) {
	 * StringBuffer soapRequest = new StringBuffer(); soapRequest
	 * .append(getSoapHeader(bookFlightDO.srvUrlType)+ SOAP_HEADER_SUB1)
	 * .append(
	 * "<ns2:OTA_AirAvailRQ EchoToken=\"11868765275150-1300257933\" SequenceNmbr=\"1\" Target=\"Test\" TimeStamp=\""
	 * + CalendarUtility.getCurrentDateTimeStamp() +
	 * "\" Version=\"20061\" DirectFlightsOnly=\"false\" xmlns:ns2=\"http://www.opentravel.org/OTA/2003/05\">"
	 * )
	 * 
	 * .append("<ns2:POS>") .append(
	 * "<ns2:Source TerminalID=\"TestUser/Test Runner\">") .append(
	 * "<ns2:RequestorID ID=\""+getRequesterId(bookFlightDO.srvUrlType)+
	 * "\" Type=\"4\" />") .append("<ns2:BookingChannel Type=\"12\" />")
	 * .append("</ns2:Source>") .append("</ns2:POS>")
	 * 
	 * .append("<ns2:OriginDestinationInformation>")
	 * .append("<ns2:DepartureDateTime>" + bookFlightDO.departureDateTime +
	 * "</ns2:DepartureDateTime>") .append("<ns2:OriginLocation LocationCode=\""
	 * + bookFlightDO.originLocationCode + "\">") .append("<ns2:CarrierCodes />"
	 * ) .append("</ns2:OriginLocation>") .append(
	 * "<ns2:DestinationLocation LocationCode=\"" +
	 * bookFlightDO.destinationLocationCode + "\">") .append(
	 * "<ns2:CarrierCodes />") .append("</ns2:DestinationLocation>")
	 * 
	 * .append("<ns2:TravelPrefences>") .append(
	 * "<ns2:cabinPref PreferLavel=\"Preferred\" cabin=\"")
	 * .append(AppConstants.classType) .append("\"/>")
	 * .append("</ns2:TravelPrefences>")
	 * 
	 * .append("</ns2:OriginDestinationInformation>")
	 * 
	 * .append("<ns2:TravelerInfoSummary>") .append("<ns2:AirTravelerAvail>")
	 * .append(formPassangerPart(bookFlightDO.adtQty, bookFlightDO.infQty,
	 * bookFlightDO.chdQty)) .append("</ns2:AirTravelerAvail>") .append(
	 * "<ns2:SpecialReqDetails />") .append("</ns2:TravelerInfoSummary>")
	 * 
	 * .append("<ns2:OriginDestinationOptions>")
	 * .append(formAirAvailOrgDesOpt(vecOriginDestinationOptionDOs))
	 * .append("</ns2:OriginDestinationOptions>")
	 * .append("</ns2:OTA_AirAvailRQ>").append(SOAP_FOOTER);
	 * 
	 * return soapRequest.toString(); }
	 */

    // New Request....
    public static String getAirAvailability(BookFlightDO bookFlightDO,
                                            Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs) {
        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append(getSoapHeader(bookFlightDO.srvUrlType) + SOAP_HEADER_SUB1)
                .append("<ns2:OTA_AirAvailRQ EchoToken=\"11868765275150-1300257933\" PrimaryLangID=\"en-us\" SequenceNmbr=\"1\" Target=\"Test\" TimeStamp=\""
                        + System.currentTimeMillis()
                        + "\" Version=\"20061\" DirectFlightsOnly=\"false\" xmlns:ns2=\"http://www.opentravel.org/OTA/2003/05\">")

                .append("<ns2:POS>").append("<ns2:Source TerminalID=\"TestUser/Test Runner\">")
                .append("<ns2:RequestorID ID=\"" + getRequesterId(bookFlightDO.srvUrlType) + "\" Type=\"4\" />")
                .append("<ns2:BookingChannel Type=\"12\" />").append("</ns2:Source>").append("</ns2:POS>")

                .append("<ns2:OriginDestinationInformation>")
                .append("<ns2:DepartureDateTime>" + bookFlightDO.departureDateTime + "</ns2:DepartureDateTime>")
                .append("<ns2:OriginLocation LocationCode=\"" + bookFlightDO.originLocationCode + "\">")
                .append("<ns2:LocationId>0</ns2:LocationId>").append("</ns2:OriginLocation>")
                .append("<ns2:DestinationLocation LocationCode=\"" + bookFlightDO.destinationLocationCode + "\">")
                .append("<ns2:LocationId>0</ns2:LocationId>").append("</ns2:DestinationLocation>")
                .append(getClassType())
                .append("</ns2:OriginDestinationInformation>")

                .append("<ns2:TravelerInfoSummary>").append("<ns2:AirTravelerAvail>")

                .append(formPassangerPart(bookFlightDO.adtQty, bookFlightDO.infQty, bookFlightDO.chdQty))
                .append("</ns2:AirTravelerAvail>")
                // .append(" <ns2:PriceRequestInformation
                // CurrencyCode=\""+AppConstants.CurrencyCodeAfterExchange+"\"
                // ></ns2:PriceRequestInformation>" )
                .append(" <ns2:PriceRequestInformation CurrencyCode=\"" + "ADT" + "\" ></ns2:PriceRequestInformation>")
                .append("</ns2:TravelerInfoSummary>")

                .append("<ns2:FlexiQuote>true</ns2:FlexiQuote>")

                .append("</ns2:OTA_AirAvailRQ>").append(SOAP_FOOTER);

        return soapRequest.toString();
    }

    public static String getAirAvailabilityReturn(BookFlightDO bookFlightDO,
                                                  Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs, String Origin, String Destination,
                                                  String ArrivalTime) {
        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append(getSoapHeader(bookFlightDO.srvUrlType) + SOAP_HEADER_SUB1)
                .append("<ns2:OTA_AirAvailRQ EchoToken=\"11868765275150-1300257933\" PrimaryLangID=\"en-us\" SequenceNmbr=\"1\" Target=\"Test\" TimeStamp=\""
                        + System.currentTimeMillis()
                        + "\" Version=\"20061\" DirectFlightsOnly=\"false\" xmlns:ns2=\"http://www.opentravel.org/OTA/2003/05\">")

                .append("<ns2:POS>").append("<ns2:Source TerminalID=\"TestUser/Test Runner\">")
                .append("<ns2:RequestorID ID=\"" + getRequesterId(bookFlightDO.srvUrlType) + "\" Type=\"4\" />")
                .append("<ns2:BookingChannel Type=\"12\" />").append("</ns2:Source>").append("</ns2:POS>")

                .append("<ns2:OriginDestinationInformation>")
                .append("<ns2:DepartureDateTime>" + bookFlightDO.departureDateTime + "</ns2:DepartureDateTime>")
                .append("<ns2:OriginLocation LocationCode=\"" + Origin + "\">")
                .append("<ns2:LocationId>0</ns2:LocationId>").append("</ns2:OriginLocation>")
                .append("<ns2:DestinationLocation LocationCode=\"" + Destination + "\">")
                .append("<ns2:LocationId>0</ns2:LocationId>").append("</ns2:DestinationLocation>")
                .append(getClassType())
                .append("</ns2:OriginDestinationInformation>")

                .append("<ns2:OriginDestinationInformation>")
                .append("<ns2:DepartureDateTime>" + ArrivalTime + "</ns2:DepartureDateTime>")
                .append("<ns2:OriginLocation LocationCode=\"" + Destination + "\">")
                .append("<ns2:LocationId>0</ns2:LocationId>").append("</ns2:OriginLocation>")
                .append("<ns2:DestinationLocation LocationCode=\"" + Origin + "\">")
                .append("<ns2:LocationId>0</ns2:LocationId>").append("</ns2:DestinationLocation>")
                .append(getClassType())
                .append("</ns2:OriginDestinationInformation>")

                .append("<ns2:TravelerInfoSummary>").append("<ns2:AirTravelerAvail>")
                .append(formPassangerPart(bookFlightDO.adtQty, bookFlightDO.infQty, bookFlightDO.chdQty))
                .append("</ns2:AirTravelerAvail>")
                .append(" <ns2:PriceRequestInformation CurrencyCode=\"" + AppConstants.CurrencyCodeAfterExchange
                        + "\" ></ns2:PriceRequestInformation>")
                .append("</ns2:TravelerInfoSummary>").append("<ns2:FlexiQuote>true</ns2:FlexiQuote>")
                .append("</ns2:OTA_AirAvailRQ>").append(SOAP_FOOTER);

        return soapRequest.toString();
    }

    public static String getClassType() {
        String str = "";

        if (!AppConstants.classType.equalsIgnoreCase("")) {
            str = "<ns2:TravelPreferences>" +
                    "<ns2:CabinPref PreferLevel=\"Preferred\" Cabin=\"" + AppConstants.classType + "\" />" +
                    "</ns2:TravelPreferences>";
        }
        return str;
    }

    public static String getAirPriceQuote(RequestParameterDO requestParameterDO, String airportCode,
                                          BookFlightDO bookFlightDO, Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsNew,
                                          Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs,
                                          Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsReturn, Vector<RequestDO> vecMealRequestDOs,
                                          Vector<RequestDO> vecBaggageRequestDOs, Vector<RequestDO> vecInsrRequestDOs,
                                          Vector<RequestDO> vecSeatRequestDOs, Vector<RequestDO> vecHalaRequestDOs, boolean isFlexiOut,
                                          boolean isFlexiIn, String bookID, String bookType, FlexiOperationsDO flexiOperationsDOCancel,
                                          FlexiOperationsDO flexiOperationsDOModify, String BundleServiceID) {
        String BundleServiceIDOneWay = "", BundleServiceIDReturn = "";

//		if (vecOriginDestinationOptionDOs != null)
//			BundleServiceIDOneWay = BundleServiceID;
//      if (vecOriginDestinationOptionDOsReturn != null && vecOriginDestinationOptionDOsReturn.size() > 0)
//            BundleServiceIDReturn = BundleServiceID;

        if (bookFlightDO.travelType.equalsIgnoreCase("OneWay")) {

            if (vecOriginDestinationOptionDOs != null)
                BundleServiceIDOneWay = BundleServiceID;
        } else if (bookFlightDO.travelType.equalsIgnoreCase("Return") && !BundleServiceID.equalsIgnoreCase("")) {

            if (AppConstants.arlBundledServiceDOs.size() == 4) {
                if (AppConstants.fareType.equalsIgnoreCase("Value")) {
                    if (vecOriginDestinationOptionDOs != null)
                        BundleServiceIDOneWay = AppConstants.arlBundledServiceDOs.get(0).bunldedServiceId;
                    if (vecOriginDestinationOptionDOsReturn != null && vecOriginDestinationOptionDOsReturn.size() > 0)
                        BundleServiceIDReturn = AppConstants.arlBundledServiceDOs.get(2).bunldedServiceId;
                } else if (AppConstants.fareType.equalsIgnoreCase("Extra")) {
                    if (vecOriginDestinationOptionDOs != null)
                        BundleServiceIDOneWay = AppConstants.arlBundledServiceDOs.get(1).bunldedServiceId;
                    if (vecOriginDestinationOptionDOsReturn != null && vecOriginDestinationOptionDOsReturn.size() > 0)
                        BundleServiceIDReturn = AppConstants.arlBundledServiceDOs.get(3).bunldedServiceId;
                }

            } else if (AppConstants.arlBundledServiceDOs.size() == 2) {

                if (AppConstants.fareType.equalsIgnoreCase("Value")) {
                    if (vecOriginDestinationOptionDOs != null)
                        BundleServiceIDOneWay = AppConstants.arlBundledServiceDOs.get(0).bunldedServiceId;
                    if (vecOriginDestinationOptionDOsReturn != null && vecOriginDestinationOptionDOsReturn.size() > 0)
                        BundleServiceIDReturn = AppConstants.arlBundledServiceDOs.get(1).bunldedServiceId;
                } else if (AppConstants.fareType.equalsIgnoreCase("Extra")) {
                    if (vecOriginDestinationOptionDOs != null)
                        BundleServiceIDOneWay = AppConstants.arlBundledServiceDOs.get(0).bunldedServiceId;
                    if (vecOriginDestinationOptionDOsReturn != null && vecOriginDestinationOptionDOsReturn.size() > 0)
                        BundleServiceIDReturn = AppConstants.arlBundledServiceDOs.get(1).bunldedServiceId;
                }
            }
        }

        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append(getSoapHeader(bookFlightDO.srvUrlType) + SOAP_HEADER_SUB1)

                .append("<ns1:OTA_AirPriceRQ EchoToken=\"").append(requestParameterDO.echoToken)
                .append("\" PrimaryLangID=\"").append(requestParameterDO.primaryLangID).append("\" SequenceNmbr=\"")
                .append(requestParameterDO.sequenceNmbr).append("\" TransactionIdentifier=\"")
                .append(requestParameterDO.transactionIdentifier)
                // .append("\"
                // TransactionIdentifier=\"").append(AppConstants.transactionIdentifier)
                .append("\" TimeStamp=\"").append(CalendarUtility.getCurrentDateTimeStamp()).append("\" Version=\"")
                .append(requestParameterDO.version).append("\" xmlns:ns1=\"http://www.opentravel.org/OTA/2003/05\">")
                .append("<ns1:POS>").append("<ns1:Source AirportCode=\"").append(airportCode)
                .append("\" TerminalID=\"TestUser/Test Runner\">")
                .append("<ns1:RequestorID ID=\"" + getRequesterId(bookFlightDO.srvUrlType) + "\" Type=\"4\" />")
                .append("<ns1:BookingChannel Type=\"12\" />").append("</ns1:Source>").append("</ns1:POS>")
                .append("<ns1:AirItinerary DirectionInd=\"").append(bookFlightDO.travelType).append("\">")
                .append("<ns1:OriginDestinationOptions>")
                .append(formOrgDesOpt(vecOriginDestinationOptionDOs, vecOriginDestinationOptionDOsReturn, null, null))
                .append("</ns1:OriginDestinationOptions>").append("</ns1:AirItinerary>")
                .append("<ns1:ModifiedSegmentInfo>")
                .append(getModifiedSegmentInfo(vecOriginDestinationOptionDOsNew, bookID, bookType,
                        flexiOperationsDOCancel, flexiOperationsDOModify))
                .append("</ns1:ModifiedSegmentInfo>").append("<ns1:TravelerInfoSummary>")
                .append("<ns1:AirTravelerAvail>")
                .append(formOrgDesOptPas(bookFlightDO.adtQty, bookFlightDO.infQty, bookFlightDO.chdQty))
                .append("</ns1:AirTravelerAvail>")

                .append("<ns1:SpecialReqDetails>").append(formMealreq(vecMealRequestDOs))
                .append(formBaggagereq(vecBaggageRequestDOs))
                .append(formInsrreq(vecInsrRequestDOs, vecOriginDestinationOptionDOs))
                .append(formSeatreq(vecSeatRequestDOs)).append(formHalareq(vecHalaRequestDOs))
                .append("</ns1:SpecialReqDetails>")

                .append("</ns1:TravelerInfoSummary>")/*.append("<ns1:FlexiFareSelectionOptions>")
                .append("<ns1:OutBoundFlexiSelected>").append(isFlexiOut).append("</ns1:OutBoundFlexiSelected>")
				.append("<ns1:InBoundFlexiSelected>").append(isFlexiIn).append("</ns1:InBoundFlexiSelected>")
				.append("</ns1:FlexiFareSelectionOptions>")*/

                .append(getPriceBundleServiceDetails(BundleServiceIDOneWay, BundleServiceIDReturn, BundleServiceID))

                .append("</ns1:OTA_AirPriceRQ>").append(SOAP_FOOTER);

        return soapRequest.toString();
    }

    private static String getPriceBundleServiceDetails(String bundleServiceIDOneWay, String bundleServiceIDReturn,
                                                       String BundleServiceID) {
        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append("<ns1:BundledServiceSelectionOptions>");

        if (!TextUtils.isEmpty(bundleServiceIDOneWay)) {
            soapRequest.append("<ns1:OutBoundBunldedServiceId>").append(bundleServiceIDOneWay)
                    .append("</ns1:OutBoundBunldedServiceId>");
        }
        if (!TextUtils.isEmpty(bundleServiceIDReturn)) {
            soapRequest.append("<ns1:InBoundBunldedServiceId>").append(bundleServiceIDReturn)
                    .append("</ns1:InBoundBunldedServiceId>");
        }

        soapRequest.append("</ns1:BundledServiceSelectionOptions>");
        return soapRequest.toString();
    }

    private static String getModifiedSegmentInfo(Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsNew,
                                                 String bookID, String bookType, FlexiOperationsDO flexiOperationsDOCancel,
                                                 FlexiOperationsDO flexiOperationsDOModify) {
        StringBuffer subRequest = new StringBuffer();
        if (vecOriginDestinationOptionDOsNew != null && vecOriginDestinationOptionDOsNew.size() > 0) {
            subRequest.append("<ns1:AirItinerary>").append("<ns1:OriginDestinationOptions>")
                    .append(formOrgDesOpt(vecOriginDestinationOptionDOsNew, null, flexiOperationsDOCancel,
                            flexiOperationsDOModify))
                    .append("</ns1:OriginDestinationOptions>").append("</ns1:AirItinerary>")
                    .append("<ns1:BookingReferenceID ID=\"").append(bookID).append("\" Type=\"").append(bookType)
                    .append("\"/>");
        } else {
            subRequest.append("<ns1:AirItinerary xmlns:d3p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                    .append("<ns1:BookingReferenceID xmlns:d3p1=\"http://www.w3.org/2001/XMLSchema-instance\" />");
        }
        return subRequest.toString();
    }

    public static String getAirBaggageDetails(RequestParameterDO requestParameterDO, String currencyCode,
                                              Vector<FlightSegmentDO> vecFlightSegmentDOs) {
        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append(getSoapHeader(requestParameterDO.srvUrlType) + SOAP_HEADER_SUB1)
                .append("<ns:AA_OTA_AirBaggageDetailsRQ EchoToken=\"").append(requestParameterDO.echoToken)
                .append("\" PrimaryLangID=\"").append(requestParameterDO.primaryLangID)
                .append("\" TransactionIdentifier=\"").append(requestParameterDO.transactionIdentifier)
                .append("\" TransactionStatusCode=\"\" RetransmissionIndicator=\"\" SequenceNmbr=\"")
                .append(requestParameterDO.sequenceNmbr).append("\" Target=\"Test").append("\" TimeStamp=\"")
                .append(CalendarUtility.getCurrentDateTimeStamp()).append("\" Version=\"")
                .append(requestParameterDO.version).append("\" xmlns:ns=\"http://www.opentravel.org/OTA/2003/05\">")
                .append("<ns:POS>").append("<ns:Source TerminalID=\"TestUser/Test Runner\">")
                .append("<ns:RequestorID ID=\"" + getRequesterId(requestParameterDO.srvUrlType) + "\" Type=\"4\" />")
                .append("<ns:BookingChannel Type=\"12\" />")
                .append("<ns:Position Latitude=\"\" Longitude=\"\" Altitude=\"\" AltitudeUnitOfMeasureCode=\"\" />")
                .append("</ns:Source>").append("</ns:POS>").append("<ns:BaggageDetailsRequests>")
                .append(formBaggage(vecFlightSegmentDOs)).append("</ns:BaggageDetailsRequests>")

//                .append("<ns:CabinClass CabinType=\"").append(AppConstants.classType).append("\"/>")

                .append("<ns:PriceRequestCurrencyCode>").append(currencyCode).append("</ns:PriceRequestCurrencyCode>")
                .append("<ns:AirTravelers xmlns:d2p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                .append("</ns:AA_OTA_AirBaggageDetailsRQ>").append(SOAP_FOOTER);

        return soapRequest.toString();
    }

    public static String getAirMealDetails(RequestParameterDO requestParameterDO,
                                           Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs) {
        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append(getSoapHeader(requestParameterDO.srvUrlType) + SOAP_HEADER_SUB1)
                .append("<ns2:AA_OTA_AirMealDetailsRQ EchoToken=\"").append(requestParameterDO.echoToken)
                .append("\" PrimaryLangID=\"").append(requestParameterDO.primaryLangID).append("\" SequenceNmbr=\"")
                .append(requestParameterDO.sequenceNmbr).append("\" TransactionIdentifier=\"")
                .append(requestParameterDO.transactionIdentifier).append("\" Version=\"")
                .append(requestParameterDO.version).append("\" xmlns:ns2=\"http://www.opentravel.org/OTA/2003/05\">")
                .append("<ns2:POS>").append("<ns2:Source TerminalID=\"TestUser/Test Runner\">")
                .append("<ns2:RequestorID ID=\"" + getRequesterId(requestParameterDO.srvUrlType) + "\" Type=\"4\" />")
                .append("<ns2:BookingChannel Type=\"12\" />").append("</ns2:Source>").append("</ns2:POS>")
                .append("<ns2:MealDetailsRequests>").append(formMeal(vecOriginDestinationOptionDOs))
                .append("</ns2:MealDetailsRequests>").append("</ns2:AA_OTA_AirMealDetailsRQ>").append(SOAP_FOOTER);

        return soapRequest.toString();
    }

    public static String getAirSeatMap(RequestParameterDO requestParameterDO,
                                       Vector<FlightSegmentDO> vecFlightSegmentDOs) {
        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append(getSoapHeader(requestParameterDO.srvUrlType) + SOAP_HEADER_SUB1)
                .append("<ns:OTA_AirSeatMapRQ EchoToken=\"").append(requestParameterDO.echoToken)
                .append("\" SequenceNmbr=\"").append(requestParameterDO.sequenceNmbr).append("\" Target=\"Test")
                .append("\" TransactionIdentifier=\"").append(requestParameterDO.transactionIdentifier)
                .append("\" TimeStamp=\"").append(CalendarUtility.getCurrentDateTimeStamp()).append("\" Version=\"")
                .append(requestParameterDO.version).append("\" xmlns:ns=\"http://www.opentravel.org/OTA/2003/05\">")
                .append("<ns:POS>").append("<ns:Source TerminalID=\"TestUser/Test Runner\">")
                .append("<ns:RequestorID ID=\"" + getRequesterId(requestParameterDO.srvUrlType) + "\" Type=\"4\" />")
                .append("<ns:BookingChannel Type=\"12\" />").append("</ns:Source>").append("</ns:POS>")
                .append("<ns:SeatMapRequests>").append(formSeat(vecFlightSegmentDOs)).append("</ns:SeatMapRequests>")
                .append("</ns:OTA_AirSeatMapRQ>").append(SOAP_FOOTER);

        return soapRequest.toString();
    }

    public static String getInsuranceQuote(RequestParameterDO requestParameterDO, String travelType,
                                           Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDOs, FlightSegmentDO flightSegmentDO) {
        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append(getSoapHeader(requestParameterDO.srvUrlType) + SOAP_HEADER_SUB1)

                .append("<ns:OTA_InsuranceQuoteRQ xmlns:ns=\"http://www.opentravel.org/OTA/2003/05\" " + "EchoToken=\"")
                .append(requestParameterDO.echoToken).append("\" PrimaryLangID=\"")
                .append(requestParameterDO.primaryLangID).append("\" SequenceNmbr=\"")
                .append(requestParameterDO.sequenceNmbr).append("\" TransactionIdentifier=\"")
                .append(requestParameterDO.transactionIdentifier)

                // .append("\"
                // TimeStamp=\"").append(CalendarUtility.getCurrentDateTimeStamp())//"1409230454.969182"
                // )
                .append("\" TimeStamp=\"").append(CalendarUtility.getCurrentTimeStamp())// "1409230454.969182"
                // )

                .append("\" Target=\"Test\"").append(" Version=\"").append(requestParameterDO.version).append("\">")
                .append("<ns:POS><ns:Source TerminalID=\"TestUser/Test Runner\"><ns:RequestorID ID=\""
                        + getRequesterId(requestParameterDO.srvUrlType) + "\" Type=\"4\"/>")
                .append("<ns:BookingChannel Type=\"12\"/></ns:Source></ns:POS><ns:PlanForQuoteRQ PlanID=\"")
                .append("AIG").append("\" Name=\"").append("AIG").append("\" Type=\"Travel\"><ns:CoveredTravelers>")
                .append(formCoverTraveler(vecPassengerInfoPersonDOs))
                .append("</ns:CoveredTravelers><ns:InsCoverageDetail><ns:CoveredTrips>")
                .append("<ns:CoveredTrip Start=\"").append(flightSegmentDO.departureAirportCode).append("\" End=\"")
                .append(flightSegmentDO.arrivalAirportCode).append("\">").append("<ns:Destinations>")
                .append("<ns:Destination Type=\"").append(travelType).append("\" ArrivalDate=\"")
                .append(flightSegmentDO.arrivalDateTime).append("\" DepartureDate=\"")
                .append(flightSegmentDO.departureDateTime).append("\"/>")
                .append("</ns:Destinations></ns:CoveredTrip></ns:CoveredTrips></ns:InsCoverageDetail></ns:PlanForQuoteRQ></ns:OTA_InsuranceQuoteRQ>")

                .append(SOAP_FOOTER);

        return soapRequest.toString();
    }

    public static String getHalaReq(RequestParameterDO requestParameterDO,
                                    Vector<FlightSegmentDO> vecFlightSegmentDOs) {
        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append(getSoapHeader(requestParameterDO.srvUrlType) + SOAP_HEADER_SUB2)
                .append("<ns2:AA_OTA_AirSSRDetailsRQ EchoToken=\"").append(requestParameterDO.echoToken)
                .append("\" PrimaryLangID=\"").append(requestParameterDO.primaryLangID).append("\" SequenceNmbr=\"")
                .append(requestParameterDO.sequenceNmbr).append("\" TimeStamp=\"")
                .append(CalendarUtility.getCurrentDateTimeStamp()).append("\" Version=\"")
                .append(requestParameterDO.version).append("\">").append("<ns2:POS>")
                .append("<ns2:Source TerminalID=\"TestUser/Test Runner\">")
                .append("<ns2:RequestorID ID=\"" + getRequesterId(requestParameterDO.srvUrlType) + "\" Type=\"4\" />")
                .append("<ns2:BookingChannel Type=\"12\" />").append("</ns2:Source>").append("</ns2:POS>")
                .append("<ns2:SSRDetailsRequests>").append(formHala(vecFlightSegmentDOs))
                .append("</ns2:SSRDetailsRequests>").append("</ns2:AA_OTA_AirSSRDetailsRQ>").append(SOAP_FOOTER);

        return soapRequest.toString();
    }

//	public static String getAirBook(RequestParameterDO requestParameterDO,
//			Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs, PassengerInfoDO passengerInfoDO,
//			PaymentDO paymentDO, Vector<RequestDO> vecMealRequestDOs, Vector<RequestDO> vecBaggageRequestDOs,
//			Vector<RequestDO> vecInsrRequestDOs, Vector<RequestDO> vecSeatRequestDOs,
//			Vector<RequestDO> vecHalaRequestDOs) {
//
//		StringBuffer soapRequest = new StringBuffer();
//		soapRequest.append(getSoapHeader(requestParameterDO.srvUrlType) + SOAP_HEADER_SUB2)
//				.append("<ns2:OTA_AirBookRQ EchoToken=\"").append(requestParameterDO.echoToken)
//				.append("\" PrimaryLangID=\"").append(requestParameterDO.primaryLangID).append("\" SequenceNmbr=\"")
//				.append(requestParameterDO.sequenceNmbr).append("\" TransactionIdentifier=\"")
//				.append(requestParameterDO.transactionIdentifier).append("\" TimeStamp=\"")
//				.append(CalendarUtility.getCurrentDateTimeStamp()).append("\" Version=\"")
//				.append(requestParameterDO.version).append("\" xmlns:ns2=\"http://www.opentravel.org/OTA/2003/05\">")
//				.append("<ns2:POS>").append("<ns2:Source TerminalID=\"TestUser/Test Runner\">")
//				.append("<ns2:RequestorID ID=\"" + getRequesterId(requestParameterDO.srvUrlType) + "\" Type=\"4\" />")
//				.append("<ns2:BookingChannel Type=\"12\" />").append("</ns2:Source>").append("</ns2:POS>")
//				.append("<ns2:AirItinerary>").append("<ns2:OriginDestinationOptions>")
//				.append(formBookOrgDesOpt(vecOriginDestinationOptionDOs)).append("</ns2:OriginDestinationOptions>")
//				.append("</ns2:AirItinerary>").append("<ns2:TravelerInfo>").append(formAirTraveler(passengerInfoDO))
//
//				.append("<ns2:SpecialReqDetails>").append(formMealreq2(vecMealRequestDOs))
//				.append(formBaggagereq2(vecBaggageRequestDOs))
//				.append(formInsrreq2(vecInsrRequestDOs, vecOriginDestinationOptionDOs))
//				.append(formSeatreq2(vecSeatRequestDOs)).append(formHalareq2(vecHalaRequestDOs))
//				.append("</ns2:SpecialReqDetails>").append("</ns2:TravelerInfo>").append("<ns2:Fulfillment>")
//				.append("<ns2:PaymentDetails>").append("<ns2:PaymentDetail>").append("<ns2:PaymentCard CardType=\"")
//				.append(paymentDO.cardType).append("\" CardNumber=\"").append(paymentDO.cardNo)
//				.append("\" SeriesCode=\"").append(paymentDO.seriesCode).append("\" ExpireDate=\"")
//				.append(paymentDO.expireDate).append("\">").append("<ns2:CardHolderName>")
//				.append(paymentDO.cardHolderName).append("</ns2:CardHolderName>").append("</ns2:PaymentCard>")
//				.append("<ns2:PaymentAmount Amount=\"").append(paymentDO.amount).append("\" CurrencyCode=\"")
//				.append(paymentDO.currencyCode).append("\" DecimalPlaces=\"").append(paymentDO.decimalPlaces)
//				.append("\" />")
//				.append("<ns2:PaymentAmountInPayCur xmlns:d5p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
//				.append("</ns2:PaymentDetail>").append("</ns2:PaymentDetails>").append("</ns2:Fulfillment>")
//				.append("</ns2:OTA_AirBookRQ>")
//
//				.append("<ns1:AAAirBookRQExt >").append("<ns1:ContactInfo>").append("<ns1:PersonName>")
//				.append("<ns1:Title>").append(passengerInfoDO.passengerInfoContactDO.contacttitle)
//				.append("</ns1:Title>").append("<ns1:FirstName>")
//				.append(passengerInfoDO.passengerInfoContactDO.contactfirstname).append("</ns1:FirstName>")
//				.append("<ns1:LastName>").append(passengerInfoDO.passengerInfoContactDO.contactlastname)
//				.append("</ns1:LastName>").append("</ns1:PersonName>").append("<ns1:Telephone>")
//				.append("<ns1:PhoneNumber>").append(passengerInfoDO.passengerInfoContactDO.contactphonenum)
//				.append("</ns1:PhoneNumber>").append("<ns1:CountryCode>")
//				.append(passengerInfoDO.passengerInfoContactDO.contactphonenumCountryCode).append("</ns1:CountryCode>")
//				.append("<ns1:AreaCode />").append("</ns1:Telephone>").append("<ns1:Email>")
//				.append(passengerInfoDO.passengerInfoContactDO.contactemail).append("</ns1:Email>")
//				.append("<ns1:Address>").append("<ns1:CountryName>").append("<ns1:CountryName>")
//				.append(passengerInfoDO.passengerInfoContactDO.countryName).append("</ns1:CountryName>")
//				.append("<ns1:CountryCode>").append(passengerInfoDO.passengerInfoContactDO.countryCode)
//				.append("</ns1:CountryCode>").append("</ns1:CountryName>").append("<ns1:CityName>City</ns1:CityName>")
//				.append("</ns1:Address>").append("</ns1:ContactInfo>").append("</ns1:AAAirBookRQExt>")
//				.append(SOAP_FOOTER);
//
//		return soapRequest.toString();
//	}

    public static String getAirBook(RequestParameterDO requestParameterDO,
                                    Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs, PassengerInfoDO passengerInfoDO,
                                    PaymentDO paymentDO, Vector<RequestDO> vecMealRequestDOs, Vector<RequestDO> vecBaggageRequestDOs,
                                    Vector<RequestDO> vecInsrRequestDOs, Vector<RequestDO> vecSeatRequestDOs,
                                    Vector<RequestDO> vecHalaRequestDOs) {

        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append(getSoapHeader(requestParameterDO.srvUrlType) + SOAP_HEADER_SUB2)
                .append("<ns2:OTA_AirBookRQ EchoToken=\"").append(requestParameterDO.echoToken)
                .append("\" PrimaryLangID=\"").append(requestParameterDO.primaryLangID).append("\" SequenceNmbr=\"")
                .append(requestParameterDO.sequenceNmbr).append("\" TransactionIdentifier=\"")
                .append(requestParameterDO.transactionIdentifier).append("\" TimeStamp=\"")
                .append(CalendarUtility.getCurrentDateTimeStamp()).append("\" Version=\"")
                .append(requestParameterDO.version).append("\" xmlns:ns2=\"http://www.opentravel.org/OTA/2003/05\">")
                .append("<ns2:POS>").append("<ns2:Source TerminalID=\"TestUser/Test Runner\">")
                .append("<ns2:RequestorID ID=\"" + getRequesterId(requestParameterDO.srvUrlType) + "\" Type=\"4\" />")
                .append("<ns2:BookingChannel Type=\"12\" />").append("</ns2:Source>").append("</ns2:POS>")
                .append("<ns2:AirItinerary>").append("<ns2:OriginDestinationOptions>")
                .append(formBookOrgDesOpt(vecOriginDestinationOptionDOs)).append("</ns2:OriginDestinationOptions>")
                .append("</ns2:AirItinerary>").append("<ns2:TravelerInfo>").append(formAirTraveler(passengerInfoDO))

                .append("<ns2:SpecialReqDetails>").append(formMealreq2(vecMealRequestDOs))
                .append(formBaggagereq2(vecBaggageRequestDOs))
                .append(formInsrreq2(vecInsrRequestDOs, vecOriginDestinationOptionDOs))
                .append(formSeatreq2(vecSeatRequestDOs)).append(formHalareq2(vecHalaRequestDOs))
                .append("</ns2:SpecialReqDetails>").append("</ns2:TravelerInfo>").append("<ns2:Fulfillment>")
                .append("<ns2:PaymentDetails>").append("<ns2:PaymentDetail>").append("<ns2:PaymentCard CardType=\"")
                .append(paymentDO.cardType).append("\" CardNumber=\"").append(paymentDO.cardNo)
                .append("\" SeriesCode=\"").append(paymentDO.seriesCode).append("\" ExpireDate=\"")
                .append(paymentDO.expireDate).append("\">").append("<ns2:CardHolderName>")
                .append(paymentDO.cardHolderName).append("</ns2:CardHolderName>").append("</ns2:PaymentCard>")
                .append("<ns2:PaymentAmount Amount=\"").append(paymentDO.amount).append("\" CurrencyCode=\"")
                .append(paymentDO.currencyCode).append("\" DecimalPlaces=\"").append(paymentDO.decimalPlaces)
                .append("\" />")
                .append("<ns2:PaymentAmountInPayCur xmlns:d5p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                .append("</ns2:PaymentDetail>").append("</ns2:PaymentDetails>").append("</ns2:Fulfillment>")
                .append("</ns2:OTA_AirBookRQ>")

                .append("<ns1:AAAirBookRQExt >").append("<ns1:ContactInfo>").append("<ns1:PersonName>")
                .append("<ns1:Title>").append(passengerInfoDO.passengerInfoContactDO.contacttitle)
                .append("</ns1:Title>").append("<ns1:FirstName>")
                .append(passengerInfoDO.passengerInfoContactDO.contactfirstname).append("</ns1:FirstName>")
                .append("<ns1:LastName>").append(passengerInfoDO.passengerInfoContactDO.contactlastname)
                .append("</ns1:LastName>").append("</ns1:PersonName>").append("<ns1:Mobile>")
                .append("<ns1:MobileNumber>").append(passengerInfoDO.passengerInfoContactDO.contactphonenum.substring(2))
                .append("</ns1:MobileNumber>").append("<ns1:CountryCode>")
                .append(passengerInfoDO.passengerInfoContactDO.contactphonenumCountryCode).append("</ns1:CountryCode>")
                .append("<ns1:AreaCode>").append(passengerInfoDO.passengerInfoContactDO.contactphonenum.substring(0, 2)).append("</ns1:AreaCode>")
                .append("</ns1:Mobile>").append("<ns1:Email>")
                .append(passengerInfoDO.passengerInfoContactDO.contactemail).append("</ns1:Email>")
                .append("<ns1:Address>").append("<ns1:CountryName>").append("<ns1:CountryName>")
                .append(passengerInfoDO.passengerInfoContactDO.countryName).append("</ns1:CountryName>")
                .append("<ns1:CountryCode>").append(passengerInfoDO.passengerInfoContactDO.countryCode)
                .append("</ns1:CountryCode>").append("</ns1:CountryName>").append("<ns1:CityName>City</ns1:CityName>")
                .append("</ns1:Address>").append("</ns1:ContactInfo>").append("</ns1:AAAirBookRQExt>")
                .append(SOAP_FOOTER);

        return soapRequest.toString();
    }

    public static String getAirPNRData(RequestParameterDO requestParameterDO, String strPNR) {
        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append(getSoapHeader(requestParameterDO.srvUrlType) + SOAP_HEADER_SUB2)
                .append("<ns2:OTA_ReadRQ EchoToken=\"").append(requestParameterDO.echoToken)
                .append("\" PrimaryLangID=\"").append(requestParameterDO.primaryLangID).append("\" SequenceNmbr=\"")
                .append(requestParameterDO.sequenceNmbr).append("\" TimeStamp=\"")
                .append(CalendarUtility.getCurrentDateTimeStamp()).append("\" Version=\"")
                .append(requestParameterDO.version).append("\" >").append("<ns2:POS>")
                .append("<ns2:Source TerminalID=\"TestUser/Test Runner\">")
                .append("<ns2:RequestorID ID=\"" + getRequesterId(requestParameterDO.srvUrlType) + "\" Type=\"4\" />")
                .append("<ns2:BookingChannel Type=\"12\" />").append("</ns2:Source>").append("</ns2:POS>")
                .append("<ns2:ReadRequests>").append("<ns2:ReadRequest>").append("<ns2:UniqueID ID=\"").append(strPNR)
                .append("\" Type=\"14\" />").append("</ns2:ReadRequest>").append("</ns2:ReadRequests>")
                .append("</ns2:OTA_ReadRQ><ns1:AAReadRQExt >").append(" <ns1:AALoadDataOptions>")
                .append("<ns1:LoadTravelerInfo>true</ns1:LoadTravelerInfo>")
                .append("<ns1:LoadAirItinery>true</ns1:LoadAirItinery>")
                .append("<ns1:LoadPriceInfoTotals>true</ns1:LoadPriceInfoTotals>")
                .append("<ns1:LoadFullFilment>true</ns1:LoadFullFilment>")
                .append("<ns1:LoadPTCPriceInfo>false</ns1:LoadPTCPriceInfo>").append("</ns1:AALoadDataOptions>")
                .append("</ns1:AAReadRQExt>").append(SOAP_FOOTER);

        return soapRequest.toString();
    }

    public static String getAirFlightSchedule(RequestParameterDO requestParameterDO, BookFlightDO bookFlightDO) {
        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append(getSoapHeader(requestParameterDO.srvUrlType) + SOAP_HEADER_SUB1)
                .append("<ns:OTA_AirScheduleRQ EchoToken=\"").append(requestParameterDO.echoToken)
                .append("\" PrimaryLangID=\"").append(requestParameterDO.primaryLangID).append("\" SequenceNmbr=\"")
                .append(requestParameterDO.sequenceNmbr).append("\" TimeStamp=\"")
                .append(CalendarUtility.getCurrentDateTimeStamp()).append("\" Version=\"")
                .append(requestParameterDO.version).append("\" xmlns:ns=\"http://www.opentravel.org/OTA/2003/05\">")
                .append("<ns:POS>").append("<ns:Source TerminalID=\"TestUser/Test Runner\">")
                .append("<ns:RequestorID ID=\"" + getRequesterId(requestParameterDO.srvUrlType) + "\" Type=\"4\" />")
                .append("<ns:BookingChannel Type=\"12\" />").append("</ns:Source>").append("</ns:POS>")
                .append("<ns:OriginDestinationInformation>").append("<ns:DepartureDateTime>")
                .append(bookFlightDO.departureDateTime).append("</ns:DepartureDateTime>").append("<ns:ArrivalDateTime>")
                .append(bookFlightDO.departureDateTime).append("</ns:ArrivalDateTime>")
                .append("<ns:OriginLocation LocationCode=\"").append(bookFlightDO.originLocationCode).append("\">")
                .append(bookFlightDO.originLocationName)
                .append("<ns:CarrierCodes xmlns:d4p1=\"http://www.w3.org/2001/XMLSchema-instance\" /></ns:OriginLocation>")
                .append("<ns:DestinationLocation LocationCode=\"").append(bookFlightDO.destinationLocationCode)
                .append("\">").append(bookFlightDO.destinationLocationName)
                .append("<ns:CarrierCodes xmlns:d4p1=\"http://www.w3.org/2001/XMLSchema-instance\" /></ns:DestinationLocation>")
                .append("</ns:OriginDestinationInformation>").append("</ns:OTA_AirScheduleRQ>").append(SOAP_FOOTER);

        return soapRequest.toString();
    }

    public static String getAirBookModify(RequestParameterDO requestParameterDO,
                                          Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs,
                                          Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsNew, PaymentDO paymentDO, String pnr,
                                          String pnrType, String modificationType) {

        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append(getSoapHeader(requestParameterDO.srvUrlType) + SOAP_HEADER_SUB2)
                .append("<ns2:OTA_AirBookModifyRQ EchoToken=\"").append(requestParameterDO.echoToken)
                .append("\" PrimaryLangID=\"").append(requestParameterDO.primaryLangID).append("\" SequenceNmbr=\"")
                .append(requestParameterDO.sequenceNmbr).append("\" TransactionIdentifier=\"")
                .append(requestParameterDO.transactionIdentifier).append("\" TimeStamp=\"")
                .append(CalendarUtility.getCurrentDateTimeStamp()).append("\" Version=\"")
                .append(requestParameterDO.version).append("\" xmlns:ns2=\"http://www.opentravel.org/OTA/2003/05\">")
                .append("<ns2:POS>").append("<ns2:Source TerminalID=\"TestUser/Test Runner\">")
                .append("<ns2:RequestorID ID=\"" + getRequesterId(requestParameterDO.srvUrlType) + "\" Type=\"4\" />")
                .append("<ns2:BookingChannel Type=\"12\" />").append("</ns2:Source>").append("</ns2:POS>")
                .append("<ns2:AirBookModifyRQ ModificationType=\"").append(modificationType).append("\">")
                .append("<ns2:AirItinerary>").append("<ns2:OriginDestinationOptions>")
                .append(formModifyBookOrgDesOpt(vecOriginDestinationOptionDOsNew, false))
                .append("</ns2:OriginDestinationOptions>").append("</ns2:AirItinerary>")
                .append("<ns2:BookingReferenceID ID=\"").append(pnr).append("\" Type=\"").append(pnrType)
                .append("\" />").append(getPaymentXml(paymentDO)).append("</ns2:AirBookModifyRQ>")
                .append("<ns2:AirReservation>").append("<ns2:AirItinerary>").append("<ns2:OriginDestinationOptions>")
                .append(formModifyBookOrgDesOpt(vecOriginDestinationOptionDOs, true))
                .append("</ns2:OriginDestinationOptions>").append("</ns2:AirItinerary>")
                .append("<ns2:PriceInfo d3p1:nil=\"true\" xmlns:d3p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                .append("<ns2:TravelerInfo d3p1:nil=\"true\" xmlns:d3p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                .append("<ns2:Ticketing d3p1:nil=\"true\" xmlns:d3p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                .append("<ns2:BookingReferenceID ID=\"").append(pnr).append("\" Type=\"").append(pnrType)
                .append("\" />")
                .append("<ns2:TPA_Extensions d3p1:nil=\"true\" xmlns:d3p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                .append("<ns2:Fulfillment d3p1:nil=\"true\" xmlns:d3p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                .append("</ns2:AirReservation>").append("</ns2:OTA_AirBookModifyRQ>")
                .append("<ns1:AAAirBookModifyRQExt >").append("<ns1:AALoadDataOptions>")
                .append("<ns1:LoadTravelerInfo>true</ns1:LoadTravelerInfo>")
                .append("<ns1:LoadAirItinery>true</ns1:LoadAirItinery>")
                .append("<ns1:LoadPriceInfoTotals>true</ns1:LoadPriceInfoTotals>")
                .append("<ns1:LoadFullFilment>true</ns1:LoadFullFilment>")
                .append("<ns1:LoadPTCPriceInfo>true</ns1:LoadPTCPriceInfo>").append("</ns1:AALoadDataOptions>")
                .append("</ns1:AAAirBookModifyRQExt>").append(SOAP_FOOTER);

        return soapRequest.toString();
    }

    public static String getLogPNR(
            BookingModificationDO bookingModificationDO/*
														 * , Vector<
														 * BookingModificationDO>
														 * vecModificationDOs
														 */) {
        StringBuffer soapRequest = new StringBuffer();

        soapRequest.append(SOAP_HEADER_LOCAL).append("<").append(ServiceURLs.LOGPNR).append(" xmlns=\"")
                .append(SOAP_ACTION_URL).append("\">").append("<objBooking>").append("<BookingId>")
                .append(bookingModificationDO.bookingId).append("</BookingId>").append("<PNR>")
                .append(bookingModificationDO.pNR).append("</PNR>").append("<BookedOn>")
                .append(bookingModificationDO.bookedOn).append("</BookedOn>").append("<BookedBy>")
                .append(bookingModificationDO.bookedBy).append("</BookedBy>").append("<JourneyType>")
                .append(bookingModificationDO.journeyType).append("</JourneyType>").append("<DepartureAirport>")
                .append(bookingModificationDO.departureAirport).append("</DepartureAirport>").append("<ArrivalAirport>")
                .append(bookingModificationDO.arrivalAirport).append("</ArrivalAirport>").append("<AmountPaid>")
                .append(bookingModificationDO.amountPaid).append("</AmountPaid>").append("<Currency>")
                .append(bookingModificationDO.currency).append("</Currency>").append("<PassengerName>")
                .append(bookingModificationDO.passengerName).append("</PassengerName>").append("<DepartureDate>")
                .append(bookingModificationDO.departureDate).append("</DepartureDate>").append("<IsModified>")
                .append(bookingModificationDO.isModified).append("</IsModified>").append("<Adults>")
                .append(bookingModificationDO.adults).append("</Adults>").append("<Children>")
                .append(bookingModificationDO.children).append("</Children>").append("<Infants>")
                .append(bookingModificationDO.infants).append("</Infants>").append("<Stops>")
                .append(bookingModificationDO.stops).append("</Stops>").append("<FlightType>")
                .append(bookingModificationDO.flightType).append("</FlightType>").append("<FlightNumber>")
                .append(bookingModificationDO.flightNumber).append("</FlightNumber>").append("<Language>")
                .append(bookingModificationDO.language).append("</Language>").append("</objBooking>").append("</")
                .append(ServiceURLs.LOGPNR).append(">").append(SOAP_FOOTER);
        return soapRequest.toString();
    }

    public static String getLogModifyPNR(BookingModificationDO bookingModificationDO) {
        StringBuffer soapRequest = new StringBuffer();

        soapRequest.append(SOAP_HEADER_LOCAL).append("<").append(ServiceURLs.MODIFYPNR).append(" xmlns=\"")
                .append(SOAP_ACTION_URL).append("\">").append("<objBookingModification>").append("<ModificationId>")
                .append(bookingModificationDO.modificationId).append("</ModificationId>").append("<BookingId>")
                .append(bookingModificationDO.bookingId).append("</BookingId>").append("<ModifiedOn>")
                .append(bookingModificationDO.modifiedOn).append("</ModifiedOn>").append("<ModifiedBy>")
                .append(bookingModificationDO.modifiedBy).append("</ModifiedBy>").append("<JourneyType>")
                .append(bookingModificationDO.journeyType).append("</JourneyType>").append("<DepartureAirport>")
                .append(bookingModificationDO.departureAirport).append("</DepartureAirport>").append("<ArrivalAirport>")
                .append(bookingModificationDO.arrivalAirport).append("</ArrivalAirport>").append("<AmountPaid>")
                .append(bookingModificationDO.amountPaid).append("</AmountPaid>").append("<Currency>")
                .append(bookingModificationDO.currency).append("</Currency>").append("<PassengerName>")
                .append(bookingModificationDO.passengerName).append("</PassengerName>").append("<DepartureDate>")
                .append(bookingModificationDO.departureDate).append("</DepartureDate>").append("<PNR>")
                .append(bookingModificationDO.pNR).append("</PNR>").append("<Adults>")
                .append(bookingModificationDO.adults).append("</Adults>").append("<Children>")
                .append(bookingModificationDO.children).append("</Children>").append("<Infants>")
                .append(bookingModificationDO.infants).append("</Infants>").append("<Stops>")
                .append(bookingModificationDO.stops).append("</Stops>").append("<FlightType>")
                .append(bookingModificationDO.flightType).append("</FlightType>").append("<FlightNumber>")
                .append(bookingModificationDO.flightNumber).append("</FlightNumber>").append("<Language>")
                .append(bookingModificationDO.language).append("</Language>").append("</objBookingModification>")
                .append("</").append(ServiceURLs.MODIFYPNR).append(">").append(SOAP_FOOTER);
        return soapRequest.toString();
    }

    // private static String formPassangerPart(int adtQty, int infQty, int
    // chdQty) {
    // StringBuffer subRequest = new StringBuffer();
    // if (adtQty > 0)
    // subRequest
    // .append("<ns2:PassengerTypeQuantity Code=\"ADT\" Quantity=\""
    // + adtQty + "\" />");
    // if (infQty > 0)
    // subRequest
    // .append("<ns2:PassengerTypeQuantity Code=\"INF\" Quantity=\""
    // + infQty + "\" />");
    // if (chdQty > 0)
    // subRequest
    // .append("<ns2:PassengerTypeQuantity Code=\"CHD\" Quantity=\""
    // + chdQty + "\" />");
    //
    // return subRequest.toString();
    // }

    private static String formPassangerPart(int adtQty, int infQty, int chdQty) {
        StringBuffer subRequest = new StringBuffer();
        if (adtQty > 0)
            subRequest.append("<ns2:PassengerTypeQuantity Code=\"ADT\" Quantity=\"" + adtQty + "\" />");
        // if (chdQty > 0)
        subRequest.append("<ns2:PassengerTypeQuantity Code=\"CHD\" Quantity=\"" + chdQty + "\" />");
        // if (infQty > 0)
        subRequest.append("<ns2:PassengerTypeQuantity Code=\"INF\" Quantity=\"" + infQty + "\" />");

        return subRequest.toString();
    }

    private static String formAirAvailOrgDesOpt(Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs) {
        StringBuffer subRequest = new StringBuffer();
        if (vecOriginDestinationOptionDOs != null && vecOriginDestinationOptionDOs.size() > 0)
            for (OriginDestinationOptionDO originDestinationOptionDO : vecOriginDestinationOptionDOs) {
                if (originDestinationOptionDO != null) {
                    subRequest.append("<ns2:OriginDestinationOption>")
                            .append(formAirAvailFlightSegment(originDestinationOptionDO.vecFlightSegmentDOs))
                            .append("</ns2:OriginDestinationOption>");
                }
            }
        return subRequest.toString();
    }

    private static String formAirAvailFlightSegment(Vector<FlightSegmentDO> vecFlightSegmentDOs) {
        StringBuffer subRequest = new StringBuffer();
        if (vecFlightSegmentDOs != null && vecFlightSegmentDOs.size() > 0)
            for (FlightSegmentDO flightSegmentDO : vecFlightSegmentDOs) {
                subRequest.append("<ns2:FlightSegment ArrivalDateTime=\"").append(flightSegmentDO.arrivalDateTime)
                        .append("\" DepartureDateTime=\"").append(flightSegmentDO.departureDateTime)
                        .append("\" FlightNumber=\"").append(flightSegmentDO.flightNumber).append("\" RPH=\"")
                        .append(flightSegmentDO.RPH).append("\" Status=\"").append(flightSegmentDO.status).append("\">")
                        .append("<ns2:OperatingAirline/>").append("<ns2:DepartureAirport LocationCode=\"")
                        .append(flightSegmentDO.departureAirportCode).append("\" Terminal=\"")
                        .append(flightSegmentDO.departureAirportTerminal).append("\" CodeContext=\"")
                        .append(flightSegmentDO.departureAirportCodeContext).append("\" />")
                        .append("<ns2:ArrivalAirport LocationCode=\"").append(flightSegmentDO.arrivalAirportCode)
                        .append("\" Terminal=\"").append(flightSegmentDO.arrivalAirportTerminal)
                        .append("\" CodeContext=\"").append(flightSegmentDO.arrivalAirportCodeContext).append("\" />")
                        .append("<ns2:Comment>").append(flightSegmentDO.comment).append("</ns2:Comment>")
                        .append("<ns2:AvailableFlexiOperations/>").append("</ns2:FlightSegment>");
            }
        return subRequest.toString();
    }

    private static String formOrgDesOpt(Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs,
                                        Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsReturn,
                                        FlexiOperationsDO flexiOperationsDOCancel, FlexiOperationsDO flexiOperationsDOModify) {
        StringBuffer subRequest = new StringBuffer();
        if (vecOriginDestinationOptionDOs != null && vecOriginDestinationOptionDOs.size() > 0)
            for (OriginDestinationOptionDO originDestinationOptionDO : vecOriginDestinationOptionDOs) {
                if (originDestinationOptionDO != null) {
                    subRequest.append("<ns1:OriginDestinationOption>")
                            .append(formFlightSegment(originDestinationOptionDO.vecFlightSegmentDOs,
                                    flexiOperationsDOCancel, flexiOperationsDOModify))
                            .append("</ns1:OriginDestinationOption>");
                }
            }
        if (vecOriginDestinationOptionDOsReturn != null && vecOriginDestinationOptionDOsReturn.size() > 0) {
            for (OriginDestinationOptionDO originDestinationOptionDO : vecOriginDestinationOptionDOsReturn) {
                if (originDestinationOptionDO != null) {
                    subRequest.append("<ns1:OriginDestinationOption>")
                            .append(formFlightSegment(originDestinationOptionDO.vecFlightSegmentDOs,
                                    flexiOperationsDOCancel, flexiOperationsDOModify))
                            .append("</ns1:OriginDestinationOption>");
                }
            }
        }
        return subRequest.toString();
    }

    private static String formFlightSegment(Vector<FlightSegmentDO> vecFlightSegmentDOs,
                                            FlexiOperationsDO flexiOperationsDOCancel, FlexiOperationsDO flexiOperationsDOModify) {
        StringBuffer subRequest = new StringBuffer();
        if (vecFlightSegmentDOs != null && vecFlightSegmentDOs.size() > 0)
            for (FlightSegmentDO flightSegmentDO : vecFlightSegmentDOs) {
                subRequest.append("<ns1:FlightSegment ArrivalDateTime=\"").append(flightSegmentDO.arrivalDateTime)
                        .append("\" DepartureDateTime=\"").append(flightSegmentDO.departureDateTime)
                        .append("\" FlightNumber=\"").append(flightSegmentDO.flightNumber).append("\" RPH=\"")
                        .append(flightSegmentDO.RPH).append(getStatus(flightSegmentDO.journeyDuration)).append("\"")
                        .append(" ResCabinClass=\"" + AppConstants.classType + "\">")
                        .append("<ns1:OperatingAirline xmlns:d6p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                        .append("<ns1:DepartureAirport LocationCode=\"").append(flightSegmentDO.departureAirportCode)
                        .append("\" Terminal=\"").append(flightSegmentDO.departureAirportTerminal).append("\" />")
                        .append("<ns1:ArrivalAirport LocationCode=\"").append(flightSegmentDO.arrivalAirportCode)
                        .append("\" Terminal=\"").append(flightSegmentDO.arrivalAirportTerminal).append("\" />")
                        .append(getComment())
                        .append(formAvialableFlexifare(flexiOperationsDOCancel, flexiOperationsDOModify))
                        .append("</ns1:FlightSegment>");
            }
        return subRequest.toString();
    }

    private static String getComment() {
        StringBuffer subRequest = new StringBuffer();
        if (AppConstants.BookingComment.equalsIgnoreCase("")) {
            subRequest.append("<ns1:Comment xmlns:d6p1=\"http://www.w3.org/2001/XMLSchema-instance\" />");
        } else
            subRequest.append("<ns1:Comment>").append(AppConstants.BookingComment).append("</ns1:Comment>");
        return subRequest.toString();
    }

    private static String getStatus(String journeyDuration) {
        StringBuffer subRequest = new StringBuffer();
        if (!journeyDuration.equalsIgnoreCase("")) {
            subRequest.append("\" Status=\"").append(journeyDuration);
        } else
            subRequest.append("\" JourneyDuration=\"").append(AppConstants.BookingStatus);
        return subRequest.toString();
    }

    private static String formAvialableFlexifare(FlexiOperationsDO flexiOperationsDOCancel,
                                                 FlexiOperationsDO flexiOperationsDOModify) {
        StringBuffer subRequest = new StringBuffer();
        if (flexiOperationsDOCancel != null && !flexiOperationsDOCancel.allowedOperationName.equalsIgnoreCase("")
                && flexiOperationsDOModify != null
                && !flexiOperationsDOModify.allowedOperationName.equalsIgnoreCase("")) {
            subRequest
                    .append("<ns1:AvailableFlexiOperations><ns1:FlexiOperations AllowedOperationName=\"Modification(s)\"")
                    .append(" FlexiOperationCutoverTimeInMinutes=\"")
                    .append(flexiOperationsDOModify.flexiOperationCutoverTimeInMinutes)
                    .append("\" NumberOfAllowedOperations=\"").append(flexiOperationsDOModify.numberOfAllowedOperations)
                    .append("\"/><ns1:FlexiOperations AllowedOperationName=\"Cancellation\"")
                    .append(" FlexiOperationCutoverTimeInMinutes=\"")
                    .append(flexiOperationsDOCancel.flexiOperationCutoverTimeInMinutes)
                    .append("\" NumberOfAllowedOperations=\"").append(flexiOperationsDOCancel.numberOfAllowedOperations)
                    .append("\"/></ns1:AvailableFlexiOperations>");
        } else
            subRequest.append(
                    "<ns1:AvailableFlexiOperations xmlns:d6p1=\"http://www.w3.org/2001/XMLSchema-instance\" />");
        return subRequest.toString();
    }

    private static String formOrgDesOptPas(int adtQty, int infQty, int chdQty) {
        StringBuffer subRequest = new StringBuffer();
        if (adtQty > 0)
            subRequest.append("<ns1:PassengerTypeQuantity Code=\"ADT\" Quantity=\"" + adtQty + "\" />");
        if (infQty > 0)
            subRequest.append("<ns1:PassengerTypeQuantity Code=\"INF\" Quantity=\"" + infQty + "\" />");
        if (chdQty > 0)
            subRequest.append("<ns1:PassengerTypeQuantity Code=\"CHD\" Quantity=\"" + chdQty + "\" />");
        return subRequest.toString();
    }

    private static String formBaggage(Vector<FlightSegmentDO> vecFlightSegmentDOs) {
        StringBuffer subRequest = new StringBuffer();
        if (vecFlightSegmentDOs != null && vecFlightSegmentDOs.size() > 0)
            for (FlightSegmentDO flightSegmentDO : vecFlightSegmentDOs) {
                subRequest.append("<ns:BaggageDetailsRequest TravelerRefNumberRPHs=\"\">")
                        .append("<ns:FlightSegmentInfo ArrivalDateTime=\"").append(flightSegmentDO.arrivalDateTime)
                        .append("\" DepartureDateTime=\"").append(flightSegmentDO.departureDateTime)
                        .append("\" FlightNumber=\"").append(flightSegmentDO.flightNumber).append("\" RPH=\"")
                        .append(flightSegmentDO.RPH).append("\">").append("<ns:DepartureAirport LocationCode=\"")
                        .append(flightSegmentDO.departureAirportCode).append("\" Terminal=\"")
                        .append(flightSegmentDO.departureAirportTerminal).append("\" />")
                        .append("<ns:ArrivalAirport LocationCode=\"").append(flightSegmentDO.arrivalAirportCode)
                        .append("\" Terminal=\"").append(flightSegmentDO.arrivalAirportTerminal).append("\" />")
                        .append("<ns:OperatingAirline Code=\"").append(flightSegmentDO.operatingAirlineCode)
                        .append("\" />").append("</ns:FlightSegmentInfo>")
                        .append("<ns:BaggageDetails> <ns:CabinClass CabinType=\"" + AppConstants.classType + "\" />")
                        .append("<ns:ResBookDesignations><ns:ResBookDesignation ResBookDesigCode=\"\" />" +
                                "</ns:ResBookDesignations></ns:BaggageDetails>")
                        .append("</ns:BaggageDetailsRequest>");
            }

        return subRequest.toString();
    }

    private static String formMeal(Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs) {
        StringBuffer subRequest = new StringBuffer();
        if (vecOriginDestinationOptionDOs != null && vecOriginDestinationOptionDOs.size() > 0)
            for (OriginDestinationOptionDO originDestinationOptionDO : vecOriginDestinationOptionDOs) {
                if (originDestinationOptionDO.vecFlightSegmentDOs != null
                        && originDestinationOptionDO.vecFlightSegmentDOs.size() > 0)
                    for (FlightSegmentDO flightSegmentDO : originDestinationOptionDO.vecFlightSegmentDOs) {
                        subRequest.append("<ns2:MealDetailsRequest>")
                                .append("<ns2:FlightSegmentInfo ArrivalDateTime=\"")
                                .append(flightSegmentDO.arrivalDateTime).append("\" DepartureDateTime=\"")
                                .append(flightSegmentDO.departureDateTime).append("\" FlightNumber=\"")
                                .append(flightSegmentDO.flightNumber).append("\" RPH=\"").append(flightSegmentDO.RPH)
                                .append("\">").append("<ns2:OperatingAirline />")
                                .append("<ns2:DepartureAirport LocationCode=\"")
                                .append(flightSegmentDO.departureAirportCode).append("\" Terminal=\"")
                                .append(flightSegmentDO.departureAirportTerminal).append("\" />")
                                .append("<ns2:ArrivalAirport LocationCode=\"")
                                .append(flightSegmentDO.arrivalAirportCode).append("\" Terminal=\"")
                                .append(flightSegmentDO.arrivalAirportTerminal).append("\" />")
                                .append("<ns2:Comment xmlns:d5p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                                .append("<ns2:AvailableFlexiOperations xmlns:d5p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                                .append("</ns2:FlightSegmentInfo>").append("</ns2:MealDetailsRequest>");
                    }
            }
        return subRequest.toString();
    }

    private static String formSeat(Vector<FlightSegmentDO> vecFlightSegmentDOs) {
        StringBuffer subRequest = new StringBuffer();
        if (vecFlightSegmentDOs != null && vecFlightSegmentDOs.size() > 0)
            for (FlightSegmentDO flightSegmentDO : vecFlightSegmentDOs) {
                subRequest.append("<ns:SeatMapRequest>").append("<ns:FlightSegmentInfo ArrivalDateTime=\"")
                        .append(flightSegmentDO.arrivalDateTime).append("\" DepartureDateTime=\"")
                        .append(flightSegmentDO.departureDateTime).append("\" FlightNumber=\"")
                        .append(flightSegmentDO.flightNumber).append("\" RPH=\"").append(flightSegmentDO.RPH)
                        .append("\">")
                        .append("<ns:OperatingAirline xmlns:d5p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                        .append("<ns:DepartureAirport LocationCode=\"").append(flightSegmentDO.departureAirportCode)
                        .append("\" Terminal=\"").append(flightSegmentDO.departureAirportTerminal).append("\" />")
                        .append("<ns:ArrivalAirport LocationCode=\"").append(flightSegmentDO.arrivalAirportCode)
                        .append("\" Terminal=\"").append(flightSegmentDO.arrivalAirportTerminal).append("\" />")
                        .append("<ns:Comment xmlns:d5p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                        .append("<ns:AvailableFlexiOperations xmlns:d5p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                        .append("</ns:FlightSegmentInfo>")
                        .append("<ns:SeatMapDetails xmlns:d4p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                        .append("</ns:SeatMapRequest>");
            }

        return subRequest.toString();
    }

    private static String formCoverTraveler(Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDOs) {
        StringBuffer subRequest = new StringBuffer();
        if (vecPassengerInfoPersonDOs != null && vecPassengerInfoPersonDOs.size() > 0)
            for (int i = 0; i < vecPassengerInfoPersonDOs.size(); i++) {
                PassengerInfoPersonDO passengerInfoPersonDO = vecPassengerInfoPersonDOs.get(i);
                if (!passengerInfoPersonDO.travelerRefNumberRPHList.contains("I")) {
                    subRequest.append("<ns:CoveredTraveler RPH=\"")
                            .append(passengerInfoPersonDO.travelerRefNumberRPHList).append("\">")
                            .append("<ns:CoveredPerson>").append("<ns:GivenName>")
                            .append(passengerInfoPersonDO.personfirstname).append("</ns:GivenName>")
                            .append("<ns:Surname>").append(passengerInfoPersonDO.personlastname).append("</ns:Surname>")
                            .append("<ns:NameTitle>").append(passengerInfoPersonDO.persontitle)
                            .append("</ns:NameTitle>").append("</ns:CoveredPerson>").append("<ns:Address>")
                            .append("<ns:CountryName/>").append("<ns:AddressLine/>").append("<ns:CityName/>")
                            .append("</ns:Address>").append("</ns:CoveredTraveler>");
                }
            }
        return subRequest.toString();
    }

//	private static String formAirTraveler(PassengerInfoDO passengerInfoDO) {
//		StringBuffer subRequest = new StringBuffer();
//		String type = "";
//		Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDOs = passengerInfoDO.vecPassengerInfoPersonDO;
//		if (vecPassengerInfoPersonDOs != null && vecPassengerInfoPersonDOs.size() > 0)
//			for (int i = 0; i < vecPassengerInfoPersonDOs.size(); i++) {
//				PassengerInfoPersonDO passengerInfoPersonDO = vecPassengerInfoPersonDOs.get(i);
//				if (passengerInfoPersonDO.persontype.equalsIgnoreCase(AppConstants.PERSON_TYPE_ADULT))
//					type = "ADT";
//				if (passengerInfoPersonDO.persontype.equalsIgnoreCase(AppConstants.PERSON_TYPE_CHILD))
//					type = "CHD";
//				if (passengerInfoPersonDO.persontype.equalsIgnoreCase(AppConstants.PERSON_TYPE_INFANT))
//					type = "INF";
//				subRequest.append("<ns2:AirTraveler PassengerTypeCode=\"").append(type)
//						.append(getDOB(type, passengerInfoPersonDO)).append("<ns2:PersonName>")
//						.append("<ns2:GivenName>").append(passengerInfoPersonDO.personfirstname)
//						.append("</ns2:GivenName>").append("<ns2:Surname>").append(passengerInfoPersonDO.personlastname)
//						.append("</ns2:Surname>").append("<ns2:NameTitle>")
//						.append(passengerInfoPersonDO.persontitle.toUpperCase()).append("</ns2:NameTitle>")
//						.append("</ns2:PersonName>").append("<ns2:Telephone CountryAccessCode=\"")
//						.append(passengerInfoDO.passengerInfoContactDO.contactphonenumCountryCode)
//						.append("\" PhoneNumber=\"").append(passengerInfoDO.passengerInfoContactDO.contactphonenum)
//						.append("\" />").append("<ns2:Address>").append("<ns2:CountryName Code=\"")
//						.append(passengerInfoDO.passengerInfoContactDO.countryCode).append("\" />")
//						.append("</ns2:Address>").append("<ns2:Document DocHolderNationality=\"")
//						.append(passengerInfoPersonDO.personCountryCode).append("\" />")
//						.append("<ns2:CustLoyalty MembershipID=\"").append(passengerInfoPersonDO.MembershipID)
//						.append("\"/>").append("<ns2:TravelerRefNumber RPH=\"")
//						.append(passengerInfoPersonDO.travelerRefNumberRPHList).append("\" />")
//						.append("<ns2:PassengerTypeQuantity xmlns:d4p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
//						.append("</ns2:AirTraveler>");
//			}
//		return subRequest.toString();
//	}

    private static String formAirTraveler(PassengerInfoDO passengerInfoDO) {
        StringBuffer subRequest = new StringBuffer();
        String type = "";
        Vector<PassengerInfoPersonDO> vecPassengerInfoPersonDOs = passengerInfoDO.vecPassengerInfoPersonDO;
        if (vecPassengerInfoPersonDOs != null && vecPassengerInfoPersonDOs.size() > 0)
            for (int i = 0; i < vecPassengerInfoPersonDOs.size(); i++) {
                PassengerInfoPersonDO passengerInfoPersonDO = vecPassengerInfoPersonDOs.get(i);
                if (passengerInfoPersonDO.persontype.equalsIgnoreCase(AppConstants.PERSON_TYPE_ADULT))
                    type = "ADT";
                if (passengerInfoPersonDO.persontype.equalsIgnoreCase(AppConstants.PERSON_TYPE_CHILD))
                    type = "CHD";
                if (passengerInfoPersonDO.persontype.equalsIgnoreCase(AppConstants.PERSON_TYPE_INFANT))
                    type = "INF";
                subRequest.append("<ns2:AirTraveler PassengerTypeCode=\"").append(type)
                        .append(getDOB(type, passengerInfoPersonDO)).append("<ns2:PersonName>")
                        .append("<ns2:GivenName>").append(passengerInfoPersonDO.personfirstname)
                        .append("</ns2:GivenName>").append("<ns2:Surname>").append(passengerInfoPersonDO.personlastname)
                        .append("</ns2:Surname>").append("<ns2:NameTitle>")
                        .append(passengerInfoPersonDO.persontitle.toUpperCase()).append("</ns2:NameTitle>")
                        .append("</ns2:PersonName>").append("<ns2:Telephone />")
                        .append("<ns2:Address>").append("<ns2:CountryName Code=\"")
                        .append(passengerInfoDO.passengerInfoContactDO.countryCode).append("\" />")
                        .append("</ns2:Address>").append("<ns2:Document DocHolderNationality=\"")
                        .append(passengerInfoDO.passengerInfoContactDO.countryCode).append("\" />")
                        .append("<ns2:CustLoyalty MembershipID=\"").append(passengerInfoPersonDO.MembershipID)
                        .append("\"/>").append("<ns2:TravelerRefNumber RPH=\"")
                        .append(passengerInfoPersonDO.travelerRefNumberRPHList).append("\" />")
                        .append("<ns2:PassengerTypeQuantity xmlns:d4p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                        .append("</ns2:AirTraveler>");
            }
        return subRequest.toString();
    }

    private static String getDOB(String type, PassengerInfoPersonDO passengerInfoPersonDO) {
        StringBuffer subRequest = new StringBuffer();
        if (type.equalsIgnoreCase("INF"))
            subRequest.append("\" BirthDate=\"").append(passengerInfoPersonDO.personDOB).append("\">");
        else
            subRequest.append("\">");
        return subRequest.toString();
    }

    private static String formBookOrgDesOpt(Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs) {
        StringBuffer subRequest = new StringBuffer();
        if (vecOriginDestinationOptionDOs != null && vecOriginDestinationOptionDOs.size() > 0)
            for (OriginDestinationOptionDO originDestinationOptionDO : vecOriginDestinationOptionDOs) {
                if (originDestinationOptionDO != null) {
                    subRequest.append("<ns2:OriginDestinationOption>")
                            .append(formBookFlightSegment(originDestinationOptionDO.vecFlightSegmentDOs))
                            .append("</ns2:OriginDestinationOption>");
                }
            }
        return subRequest.toString();
    }

    private static String formBookFlightSegment(Vector<FlightSegmentDO> vecFlightSegmentDOs) {
        StringBuffer subRequest = new StringBuffer();
        if (vecFlightSegmentDOs != null && vecFlightSegmentDOs.size() > 0)
            for (FlightSegmentDO flightSegmentDO : vecFlightSegmentDOs) {
                subRequest.append("<ns2:FlightSegment ArrivalDateTime=\"").append(flightSegmentDO.arrivalDateTime)
                        .append("\" DepartureDateTime=\"").append(flightSegmentDO.departureDateTime)
                        .append("\" FlightNumber=\"").append(flightSegmentDO.flightNumber).append("\" RPH=\"")
                        .append(flightSegmentDO.RPH).append("\">")
                        .append("<ns2:OperatingAirline xmlns:d6p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                        .append("<ns2:DepartureAirport LocationCode=\"").append(flightSegmentDO.departureAirportCode)
                        .append("\" Terminal=\"").append(flightSegmentDO.departureAirportTerminal).append("\" />")
                        .append("<ns2:ArrivalAirport LocationCode=\"").append(flightSegmentDO.arrivalAirportCode)
                        .append("\" Terminal=\"").append(flightSegmentDO.arrivalAirportTerminal).append("\" />")
                        .append("<ns2:Comment xmlns:d6p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                        .append("<ns2:AvailableFlexiOperations xmlns:d6p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                        .append("</ns2:FlightSegment>");
            }
        return subRequest.toString();
    }

    private static String formMealreq(Vector<RequestDO> vecMealRequestDOs) {
        StringBuffer subRequest = new StringBuffer();
        subRequest.append("<ns1:MealRequests>");
        if (vecMealRequestDOs != null && vecMealRequestDOs.size() > 0) {
            for (RequestDO requestDO : vecMealRequestDOs) {
                subRequest.append("<ns1:MealRequest mealCode=\"").append(requestDO.mealCode)
                        .append("\" mealQuantity=\"").append(requestDO.mealQuantity)
                        .append("\" TravelerRefNumberRPHList=\"").append(requestDO.travelerRefNumberRPHList)
                        .append("\" FlightRefNumberRPHList=\"").append(requestDO.flightRefNumberRPHList)
                        .append("\" DepartureDate=\"").append(requestDO.departureDate).append("\" FlightNumber=\"")
                        .append(requestDO.flightNumber).append("\" />");
            }
        }
        subRequest.append("</ns1:MealRequests>");
        return subRequest.toString();
    }

    private static String formMealreq2(Vector<RequestDO> vecMealRequestDOs) {
        StringBuffer subRequest = new StringBuffer();
        subRequest.append("<ns2:MealRequests>");
        if (vecMealRequestDOs != null && vecMealRequestDOs.size() > 0) {
            for (RequestDO requestDO : vecMealRequestDOs) {
                subRequest.append("<ns2:MealRequest mealCode=\"").append(requestDO.mealCode)
                        .append("\" mealQuantity=\"").append(requestDO.mealQuantity)
                        .append("\" TravelerRefNumberRPHList=\"").append(requestDO.travelerRefNumberRPHList)
                        .append("\" FlightRefNumberRPHList=\"").append(requestDO.flightRefNumberRPHList)
                        .append("\" DepartureDate=\"").append(requestDO.departureDate).append("\" FlightNumber=\"")
                        .append(requestDO.flightNumber).append("\" />");
            }
        }
        subRequest.append("</ns2:MealRequests>");
        return subRequest.toString();
    }

    private static String formBaggagereq(Vector<RequestDO> vecBaggageRequestDOs) {
        StringBuffer subRequest = new StringBuffer();
        subRequest.append("<ns1:BaggageRequests>");
        if (vecBaggageRequestDOs != null && vecBaggageRequestDOs.size() > 0) {
            for (RequestDO requestDO : vecBaggageRequestDOs) {
                subRequest.append("<ns1:BaggageRequest baggageCode=\"").append(requestDO.baggageCode)
                        .append("\" TravelerRefNumberRPHList=\"").append(requestDO.travelerRefNumberRPHList)
                        .append("\" FlightRefNumberRPHList=\"").append(requestDO.flightRefNumberRPHList)
                        .append("\" DepartureDate=\"").append(requestDO.departureDate).append("\" FlightNumber=\"")
                        .append(requestDO.flightNumber).append("\" />");
            }
        }
        subRequest.append("</ns1:BaggageRequests>");
        return subRequest.toString();
    }

    private static String formBaggagereq2(Vector<RequestDO> vecBaggageRequestDOs) {
        StringBuffer subRequest = new StringBuffer();
        subRequest.append("<ns2:BaggageRequests>");
        if (vecBaggageRequestDOs != null && vecBaggageRequestDOs.size() > 0) {
            for (RequestDO requestDO : vecBaggageRequestDOs) {
                subRequest.append("<ns2:BaggageRequest baggageCode=\"").append(requestDO.baggageCode)
                        .append("\" TravelerRefNumberRPHList=\"").append(requestDO.travelerRefNumberRPHList)
                        .append("\" FlightRefNumberRPHList=\"").append(requestDO.flightRefNumberRPHList)
                        .append("\" DepartureDate=\"").append(requestDO.departureDate).append("\" FlightNumber=\"")
                        .append(requestDO.flightNumber).append("\" />");
            }
        }
        subRequest.append("</ns2:BaggageRequests>");
        return subRequest.toString();
    }

    private static String formInsrreq(Vector<RequestDO> vecInsrRequestDOs,
                                      Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs) {
        StringBuffer subRequest = new StringBuffer();
        subRequest.append("<ns1:InsuranceRequests>");
        if (vecOriginDestinationOptionDOs != null && vecOriginDestinationOptionDOs.size() > 0
                && vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs != null
                && vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs.size() > 0) {
            FlightSegmentDO flightSegmentDO = vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs.get(0);
            if (vecInsrRequestDOs != null && vecInsrRequestDOs.size() > 0) {
                for (RequestDO requestDO : vecInsrRequestDOs) {
                    subRequest.append("<ns1:InsuranceRequest RPH=\"").append(requestDO.rPH).append("\" PolicyCode=\"")
                            .append(requestDO.policyCode).append("\" DepartureDate=\"")
                            .append(flightSegmentDO.departureDateTime).append("\" ArrivalDate=\"")
                            .append(flightSegmentDO.arrivalDateTime).append("\" />");
                }
            }
        }
        subRequest.append("</ns1:InsuranceRequests>");
        return subRequest.toString();
    }

    private static String formInsrreq2(Vector<RequestDO> vecInsrRequestDOs,
                                       Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs) {
        StringBuffer subRequest = new StringBuffer();
        subRequest.append("<ns2:InsuranceRequests>");
        if (vecOriginDestinationOptionDOs != null && vecOriginDestinationOptionDOs.size() > 0
                && vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs != null
                && vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs.size() > 0) {
            FlightSegmentDO flightSegmentDO = vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs.get(0);
            if (vecInsrRequestDOs != null && vecInsrRequestDOs.size() > 0) {
                for (RequestDO requestDO : vecInsrRequestDOs) {
                    subRequest.append("<ns2:InsuranceRequest RPH=\"").append(requestDO.rPH).append("\" PolicyCode=\"")
                            .append(requestDO.policyCode).append("\" DepartureDate=\"")
                            .append(flightSegmentDO.departureDateTime).append("\" ArrivalDate=\"")
                            .append(flightSegmentDO.arrivalDateTime).append("\" />");
                }
            }
        }
        subRequest.append("</ns2:InsuranceRequests>");
        return subRequest.toString();
    }

    private static String formHalareq(Vector<RequestDO> vecHalaRequestDOs) {
        StringBuffer subRequest = new StringBuffer();
        subRequest.append("<ns1:SSRRequests>");
        if (vecHalaRequestDOs != null && vecHalaRequestDOs.size() > 0) {
            for (RequestDO requestDO : vecHalaRequestDOs) {
                subRequest.append("<ns1:SSRRequest ssrCode=\"").append(requestDO.ssrCode).append("\" ServiceType=\"")
                        .append("AIRPORT").append("\" airportCode=\"").append(requestDO.airportCode)
                        .append("\" airportType=\"").append(requestDO.airportType).append("\" DepartureDate=\"")
                        .append(requestDO.departureDate).append("\" FlightNumber=\"").append(requestDO.flightNumber)
                        .append("\" TravelerRefNumberRPHList=\"").append(requestDO.travelerRefNumberRPHList)
                        .append("\" FlightRefNumberRPHList=\"").append(requestDO.flightRefNumberRPHList)
                        .append("\" />");
            }
        }
        subRequest.append("</ns1:SSRRequests>");
        return subRequest.toString();
    }

    private static String formHalareq2(Vector<RequestDO> vecHalaRequestDOs) {
        StringBuffer subRequest = new StringBuffer();
        subRequest.append("<ns2:SSRRequests>");
        if (vecHalaRequestDOs != null && vecHalaRequestDOs.size() > 0) {
            for (RequestDO requestDO : vecHalaRequestDOs) {
                subRequest.append("<ns2:SSRRequest ssrCode=\"").append(requestDO.ssrCode).append("\" ServiceType=\"")
                        .append("AIRPORT").append("\" airportCode=\"").append(requestDO.airportCode)
                        .append("\" airportType=\"").append(requestDO.airportType).append("\" DepartureDate=\"")
                        .append(requestDO.departureDate).append("\" FlightNumber=\"").append(requestDO.flightNumber)
                        .append("\" TravelerRefNumberRPHList=\"").append(requestDO.travelerRefNumberRPHList)
                        .append("\" FlightRefNumberRPHList=\"").append(requestDO.flightRefNumberRPHList)
                        .append("\" />");
            }
        }
        subRequest.append("</ns2:SSRRequests>");
        return subRequest.toString();
    }

    private static String formSeatreq(Vector<RequestDO> vecSeatRequestDOs) {
        StringBuffer subRequest = new StringBuffer();
        subRequest.append("<ns1:SeatRequests>");
        if (vecSeatRequestDOs != null && vecSeatRequestDOs.size() > 0) {
            for (RequestDO requestDO : vecSeatRequestDOs) {
                if (!requestDO.seatNumber.equalsIgnoreCase("")) {
                    subRequest.append("<ns1:SeatRequest SeatNumber=\"").append(requestDO.seatNumber)
                            .append("\" TravelerRefNumberRPHList=\"").append(requestDO.travelerRefNumberRPHList)
                            .append("\" FlightRefNumberRPHList=\"").append(requestDO.flightRefNumberRPHList)
                            .append("\" DepartureDate=\"").append(requestDO.departureDate).append("\" FlightNumber=\"")
                            .append(requestDO.flightNumber).append("\" />");
                }
            }
        }
        subRequest.append("</ns1:SeatRequests>");
        return subRequest.toString();
    }

    private static String formSeatreq2(Vector<RequestDO> vecSeatRequestDOs) {
        StringBuffer subRequest = new StringBuffer();
        subRequest.append("<ns2:SeatRequests>");
        if (vecSeatRequestDOs != null && vecSeatRequestDOs.size() > 0) {
            for (RequestDO requestDO : vecSeatRequestDOs) {
                if (!requestDO.seatNumber.equalsIgnoreCase("")) {
                    subRequest.append("<ns2:SeatRequest SeatNumber=\"").append(requestDO.seatNumber)
                            .append("\" TravelerRefNumberRPHList=\"").append(requestDO.travelerRefNumberRPHList)
                            .append("\" FlightRefNumberRPHList=\"").append(requestDO.flightRefNumberRPHList)
                            .append("\" DepartureDate=\"").append(requestDO.departureDate).append("\" FlightNumber=\"")
                            .append(requestDO.flightNumber).append("\" />");
                }
            }
        }
        subRequest.append("</ns2:SeatRequests>");
        return subRequest.toString();
    }

    private static String formHala(Vector<FlightSegmentDO> vecFlightSegmentDOs) {
        StringBuffer subRequest = new StringBuffer();
        if (vecFlightSegmentDOs != null && vecFlightSegmentDOs.size() > 0)
            for (FlightSegmentDO flightSegmentDO : vecFlightSegmentDOs) {
                subRequest.append("<ns2:SSRDetailsRequest TravelerRefNumberRPHs=\"").append("A1").append("\">")
                        .append("<ns2:FlightSegmentInfo ArrivalDateTime=\"").append(flightSegmentDO.arrivalDateTime)
                        .append("\" DepartureDateTime=\"").append(flightSegmentDO.departureDateTime)
                        .append("\" FlightNumber=\"").append(flightSegmentDO.flightNumber).append("\" RPH=\"")
                        .append(flightSegmentDO.RPH).append("\">").append("<ns2:DepartureAirport LocationCode=\"")
                        .append(flightSegmentDO.departureAirportCode).append("\" Terminal=\"")
                        .append(flightSegmentDO.departureAirportTerminal).append("\" />")
                        .append("<ns2:ArrivalAirport LocationCode=\"").append(flightSegmentDO.arrivalAirportCode)
                        .append("\" Terminal=\"").append(flightSegmentDO.arrivalAirportTerminal).append("\" />")
                        .append("<ns2:OperatingAirline Code=\"").append(flightSegmentDO.operatingAirlineCode)
                        .append("\" />").append("</ns2:FlightSegmentInfo>").append("<ns2:SSRDetails ServiceType=\"")
                        .append("AIRPORT").append("\">").append("<ns2:CabinClass CabinType=\"").append("Y")
                        .append("\" />").append("</ns2:SSRDetails>").append("</ns2:SSRDetailsRequest>");
            }
        return subRequest.toString();
    }

    private static String getPaymentXml(PaymentDO paymentDO) {
        StringBuffer subRequest = new StringBuffer();
        if (paymentDO == null)
            subRequest.append(
                    "<ns2:Fulfillment d3p1:nil=\"true\" xmlns:d3p1=\"http://www.w3.org/2001/XMLSchema-instance\" />");
        else {
            subRequest.append("<ns2:Fulfillment>").append("<ns2:PaymentDetails>").append("<ns2:PaymentDetail>")
                    .append("<ns2:PaymentCard CardType=\"").append(paymentDO.cardType).append("\" CardNumber=\"")
                    .append(paymentDO.cardNo).append("\" SeriesCode=\"").append(paymentDO.seriesCode)
                    .append("\" ExpireDate=\"").append(paymentDO.expireDate).append("\">")
                    .append("<ns2:CardHolderName>").append(paymentDO.cardHolderName).append("</ns2:CardHolderName>")
                    .append("</ns2:PaymentCard>").append("<ns2:PaymentAmount Amount=\"").append(paymentDO.curAmount)
                    .append("\" CurrencyCode=\"").append(paymentDO.curCurrencyCode).append("\" DecimalPlaces=\"")
                    .append(paymentDO.curDecimalPlaces).append("\" />")
                    .append("<ns2:PaymentAmountInPayCur d6p1:nil=\"true\" xmlns:d6p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                    .append("</ns2:PaymentDetail>").append("</ns2:PaymentDetails>").append("</ns2:Fulfillment>");
        }

        return subRequest.toString();
    }

    private static String formModifyBookOrgDesOpt(Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs,
                                                  boolean isAirReservation) {
        StringBuffer subRequest = new StringBuffer();
        if (vecOriginDestinationOptionDOs != null && vecOriginDestinationOptionDOs.size() > 0)
            for (OriginDestinationOptionDO originDestinationOptionDO : vecOriginDestinationOptionDOs) {
                if (originDestinationOptionDO != null) {
                    subRequest.append("<ns2:OriginDestinationOption>")
                            .append(formModifyBookFlightSegment(originDestinationOptionDO.vecFlightSegmentDOs,
                                    isAirReservation))
                            .append("</ns2:OriginDestinationOption>");
                }
            }
        return subRequest.toString();
    }

    private static String formModifyBookFlightSegment(Vector<FlightSegmentDO> vecFlightSegmentDOs,
                                                      boolean isAirReservation) {
        StringBuffer subRequest = new StringBuffer();
        if (vecFlightSegmentDOs != null && vecFlightSegmentDOs.size() > 0)
            for (FlightSegmentDO flightSegmentDO : vecFlightSegmentDOs) {

                subRequest.append("<ns2:FlightSegment ArrivalDateTime=\"").append(flightSegmentDO.arrivalDateTime)
                        .append("\" DepartureDateTime=\"").append(flightSegmentDO.departureDateTime)
                        .append("\" FlightNumber=\"").append(flightSegmentDO.flightNumber).append("\" RPH=\"")
                        .append(flightSegmentDO.RPH);
                if (isAirReservation)
                    subRequest.append("\" Status=\"").append(flightSegmentDO.status);
                else
                    subRequest.append("\" JourneyDuration=\"").append(flightSegmentDO.journeyDuration);

                subRequest.append("\">");

                subRequest.append("<ns2:OperatingAirline xmlns:d7p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                        .append("<ns2:DepartureAirport LocationCode=\"").append(flightSegmentDO.departureAirportCode)
                        .append("\" Terminal=\"").append(flightSegmentDO.departureAirportTerminal);
                if (isAirReservation)
                    subRequest.append("\" CodeContext=\"").append(flightSegmentDO.departureAirportCodeContext);

                subRequest.append("\" />");

                subRequest.append("<ns2:ArrivalAirport LocationCode=\"").append(flightSegmentDO.arrivalAirportCode)
                        .append("\" Terminal=\"").append(flightSegmentDO.arrivalAirportTerminal);

                if (isAirReservation)
                    subRequest.append("\" CodeContext=\"").append(flightSegmentDO.arrivalAirportCodeContext);

                subRequest.append("\" />");

                if (!flightSegmentDO.comment.equalsIgnoreCase(""))
                    subRequest.append("<ns2:Comment>").append(flightSegmentDO.comment).append("</ns2:Comment>");
                else
                    subRequest.append("<ns2:Comment xmlns:d7p1=\"http://www.w3.org/2001/XMLSchema-instance\" />");

                subRequest
                        .append("<ns2:AvailableFlexiOperations xmlns:d7p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
                        .append("</ns2:FlightSegment>");
            }
        return subRequest.toString();
    }

    /**
     * Get All Cities
     *
     * @return
     */
    public static String getCities() {
        StringBuffer soapRequest = new StringBuffer();

        soapRequest.append(SOAP_HEADER_LOCAL).append("<").append(ServiceURLs.GET_CITIES).append(" xmlns=\"")
                .append(SOAP_ACTION_URL).append("\" />").append(SOAP_FOOTER);
        return soapRequest.toString();
    }

    /**
     * Get All Nationalities
     *
     * @return
     */
    public static String getNationalities() {
        StringBuffer soapRequest = new StringBuffer();

        soapRequest.append(SOAP_HEADER_LOCAL).append("<").append(ServiceURLs.GET_NATIONALITIES).append(" xmlns=\"")
                .append(SOAP_ACTION_URL).append("\" />").append(SOAP_FOOTER);
        return soapRequest.toString();
    }

    /**
     * Get All Country Isd Codes
     *
     * @return
     */
    public static String getCountryIsdCodes() {
        StringBuffer soapRequest = new StringBuffer();

        soapRequest.append(SOAP_HEADER_LOCAL).append("<").append(ServiceURLs.GET_COUNTRYISDCODES).append(" xmlns=\"")
                .append(SOAP_ACTION_URL).append("\" />").append(SOAP_FOOTER);
        return soapRequest.toString();
    }

    /**
     * Get All Country Names
     *
     * @return
     */
    public static String getCountryNames() {
        StringBuffer soapRequest = new StringBuffer();

        soapRequest.append(SOAP_HEADER_LOCAL).append("<").append(ServiceURLs.GET_COUNTRY_NAMES).append(" xmlns=\"")
                .append(SOAP_ACTION_URL).append("\" />").append(SOAP_FOOTER);
        return soapRequest.toString();
    }

    /**
     * Get All Office Locations
     *
     * @return
     */
    public static String getOfficeLocations() {
        StringBuffer soapRequest = new StringBuffer();

        soapRequest.append(SOAP_HEADER_LOCAL).append("<").append(ServiceURLs.GET_OFFICE_LOCATIONS).append(" xmlns=\"")
                .append(SOAP_ACTION_URL).append("\" />").append(SOAP_FOOTER);
        return soapRequest.toString();
    }

    /**
     * Check Login
     *
     * @param userName
     * @param password
     * @return
     */
    public static String getLogin(String userName, String password, String requestURLType, String gcmId) {
        StringBuffer soapRequest = new StringBuffer();

        soapRequest.append(getSoapHeader(AppConstants.SERVICE_URL_TYPE_EMPTY, requestURLType))
                .append("<soapenv:Body><log:AA_API_LoginRQ><log:securityInformation><log:token/></log:securityInformation>")
                .append("<log:userName>").append(userName).append("</log:userName>").append("<log:password>")
                .append(password).append("</log:password>").append("<GCMKey>").append(gcmId).append("</GCMKey>")
                .append("</log:AA_API_LoginRQ>").append(SOAP_FOOTER_LOGIN);

        return soapRequest.toString();
    }

    public static String getPosterImages() {
        StringBuffer soapRequest = new StringBuffer();

        soapRequest.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n"
                + "  <soap12:Body>\n" + "    <GetBanners xmlns=\"http://tempuri.org/\" />\n" + "  </soap12:Body>\n"
                + "</soap12:Envelope>");

        return soapRequest.toString();
    }

    public static String getCurrencyExchangeRate(String currencyFrom, String currencyTo) {
        StringBuffer soapRequest = new StringBuffer();

        soapRequest.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                + "<soap:Body>\n" + "<GetExchangeRate xmlns=\"http://tempuri.org/\">\n" + "<FromCurrency>"
                + currencyFrom + "</FromCurrency>\n" + "<ToCurrency>" + currencyTo + "</ToCurrency>\n"
                + "</GetExchangeRate>\n" + "</soap:Body>\n" + "</soap:Envelope>");
        return soapRequest.toString();
    }

    // New Request for AirPrice in Return Way....

    public static String getAirPriceQuoteForRetrun(RequestParameterDO requestParameterDO, String airportCode,
                                                   BookFlightDO bookFlightDO, Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsNew,
                                                   Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs,
                                                   Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsReturn, Vector<RequestDO> vecMealRequestDOs,
                                                   Vector<RequestDO> vecBaggageRequestDOs, Vector<RequestDO> vecInsrRequestDOs,
                                                   Vector<RequestDO> vecSeatRequestDOs, Vector<RequestDO> vecHalaRequestDOs, boolean isFlexiOut,
                                                   boolean isFlexiIn, String bookID, String bookType, FlexiOperationsDO flexiOperationsDOCancel,
                                                   FlexiOperationsDO flexiOperationsDOModify, String BundleServiceID, Vector<FlightSegmentDO> depa,
                                                   Vector<FlightSegmentDO> arr) {
        String BundleServiceIDOneWay = "", BundleServiceIDReturn = "";
        if (vecOriginDestinationOptionDOs != null)
            BundleServiceIDOneWay = BundleServiceID;
        if (vecOriginDestinationOptionDOsReturn != null)
            BundleServiceIDReturn = BundleServiceID;

        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append(getSoapHeader(bookFlightDO.srvUrlType) + SOAP_HEADER_SUB1)

                .append("<ns1:OTA_AirPriceRQ EchoToken=\"").append(requestParameterDO.echoToken)
                .append("\" PrimaryLangID=\"").append(requestParameterDO.primaryLangID).append("\" SequenceNmbr=\"")
                .append(requestParameterDO.sequenceNmbr).append("\" TransactionIdentifier=\"")
                .append(requestParameterDO.transactionIdentifier).append("\" TimeStamp=\"")
                .append(CalendarUtility.getCurrentDateTimeStamp()).append("\" Version=\"")
                .append(requestParameterDO.version).append("\" xmlns:ns1=\"http://www.opentravel.org/OTA/2003/05\">")
                .append("<ns1:POS>").append("<ns1:Source AirportCode=\"").append(airportCode)
                .append("\" TerminalID=\"TestUser/Test Runner\">")
                .append("<ns1:RequestorID ID=\"" + getRequesterId(bookFlightDO.srvUrlType) + "\" Type=\"4\" />")
                .append("<ns1:BookingChannel Type=\"12\" />").append("</ns1:Source>").append("</ns1:POS>")
                .append("<ns1:AirItinerary DirectionInd=\"").append(bookFlightDO.travelType).append("\">")
                .append("<ns1:OriginDestinationOptions>").append(formOrgDesOptReturn(depa, arr, null, null))
                .append("</ns1:OriginDestinationOptions>").append("</ns1:AirItinerary>")
                .append("<ns1:ModifiedSegmentInfo>")
                .append(getModifiedSegmentInfo(vecOriginDestinationOptionDOsNew, bookID, bookType,
                        flexiOperationsDOCancel, flexiOperationsDOModify))
                .append("</ns1:ModifiedSegmentInfo>").append("<ns1:TravelerInfoSummary>")
                .append("<ns1:AirTravelerAvail>")
                .append(formOrgDesOptPas(bookFlightDO.adtQty, bookFlightDO.infQty, bookFlightDO.chdQty))
                .append("</ns1:AirTravelerAvail>")

                .append("<ns1:SpecialReqDetails>").append(formMealreq(vecMealRequestDOs))
                .append(formBaggagereq(vecBaggageRequestDOs))
                .append(formInsrreq(vecInsrRequestDOs, vecOriginDestinationOptionDOs))
                .append(formSeatreq(vecSeatRequestDOs)).append(formHalareq(vecHalaRequestDOs))
                .append("</ns1:SpecialReqDetails>")

                .append("</ns1:TravelerInfoSummary>")/*.append("<ns1:FlexiFareSelectionOptions>")
				.append("<ns1:OutBoundFlexiSelected>").append(isFlexiOut).append("</ns1:OutBoundFlexiSelected>")
				.append("<ns1:InBoundFlexiSelected>").append(isFlexiIn).append("</ns1:InBoundFlexiSelected>")
				.append("</ns1:FlexiFareSelectionOptions>")*/

                .append(getPriceBundleServiceDetails(BundleServiceIDOneWay, BundleServiceIDReturn, BundleServiceID))

                .append("</ns1:OTA_AirPriceRQ>").append(SOAP_FOOTER);

        return soapRequest.toString();
    }

    private static String formOrgDesOptReturn(Vector<FlightSegmentDO> FlightSegmentDOs, Vector<FlightSegmentDO> arr,
                                              FlexiOperationsDO flexiOperationsDOCancel, FlexiOperationsDO flexiOperationsDOModify) {

        StringBuffer subRequest = new StringBuffer();
        if (FlightSegmentDOs != null && FlightSegmentDOs.size() > 0)

            for (FlightSegmentDO FlightSegmentOs : FlightSegmentDOs) {
                if (FlightSegmentOs != null) {
                    Vector<FlightSegmentDO> vv = new Vector<FlightSegmentDO>();
                    vv.add(FlightSegmentOs);
                    subRequest.append("<ns1:OriginDestinationOption>")
                            .append(formFlightSegment(vv, flexiOperationsDOCancel, flexiOperationsDOModify))
                            .append("</ns1:OriginDestinationOption>");
                }
            }

        if (arr != null && arr.size() > 0) {
            for (FlightSegmentDO FlightSegmentOs : arr) {
                if (FlightSegmentOs != null) {
                    Vector<FlightSegmentDO> vv = new Vector<FlightSegmentDO>();
                    vv.add(FlightSegmentOs);
                    subRequest.append("<ns1:OriginDestinationOption>")
                            .append(formFlightSegment(vv, flexiOperationsDOCancel, flexiOperationsDOModify))
                            .append("</ns1:OriginDestinationOption>");
                }
            }
        }
        return subRequest.toString();
    }

//	public static String getCancelFlight(RequestParameterDO requestParameterDO, String PNR, String pnrType) {
//		// TODO Auto-generated method stub
//		StringBuffer soapRequest = new StringBuffer();
//		soapRequest.append(getSoapHeader(requestParameterDO.srvUrlType) + SOAP_HEADER_SUB2)
//		.append("<ns2:OTA_AirBookModifyRQ  EchoToken=\"").append(requestParameterDO.echoToken)
//		.append("\" PrimaryLangID=\"").append(requestParameterDO.primaryLangID)
//		.append("\" SequenceNmbr=\"").append(requestParameterDO.sequenceNmbr)
//		.append("\" TransactionIdentifier=\"").append(requestParameterDO.transactionIdentifier)
//		.append("\" TimeStamp=\"").append(CalendarUtility.getCurrentDateTimeStamp())
//		.append("\" Version=\"").append(requestParameterDO.version).append("\" JsessionId=\"").append("\" >")		 
//		.append("<ns2:POS>").append("<ns2:Source TerminalID=\"TestUser/Test Runner\">")
//		.append("<ns2:RequestorID ID=\"" + getRequesterId(requestParameterDO.srvUrlType) + "\" Type=\"4\" />")
//		.append("<ns2:BookingChannel Type=\"12\" />").append("</ns2:Source>").append("</ns2:POS>")
//		.append("<ns2:AirBookModifyRQ ModificationType=\"14\">")
//		.append("<ns2:BookingReferenceID ID=\"" + PNR + "\" Type=\"" + pnrType + "\" />")
//		.append("<ns2:Fulfillment d3p1:nil=\"true\" xmlns:d3p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
//		.append("</ns2:AirBookModifyRQ>")
//		.append("<ns2:AirReservation d2p1:nil=\"true\" xmlns:d2p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
//		.append("</ns2:OTA_AirBookModifyRQ>")
//		.append("<ns1:AAAirBookModifyRQExt>").append("<ns1:AALoadDataOptions>")
//		.append("<ns1:LoadTravelerInfo>").append(true).append("</ns1:LoadTravelerInfo>")
//		.append("<ns1:LoadAirItinery>").append(true).append("</ns1:LoadAirItinery>")
//		.append("<ns1:LoadPriceInfoTotals>").append(true).append("</ns1:LoadPriceInfoTotals>")
//		.append("<ns1:LoadFullFilment>").append(true).append("</ns1:LoadFullFilment>")
//		.append("<ns1:LoadPTCPriceInfo>").append(false).append("</ns1:LoadPTCPriceInfo>")
//		.append("</ns1:AALoadDataOptions>").append("</ns1:AAAirBookModifyRQExt>")
//		.append(SOAP_FOOTER);
//		
//		return soapRequest.toString();
//	}

    public static String getCancelFlight(RequestParameterDO requestParameterDO, String PNR, String pnrType) {
        // TODO Auto-generated method stub
        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append(getSoapHeader(requestParameterDO.srvUrlType) + SOAP_HEADER_SUB2)
                .append("<ns2:OTA_ReadRQ  EchoToken=\"").append(requestParameterDO.echoToken)
                .append("\" PrimaryLangID=\"").append(requestParameterDO.primaryLangID)
                .append("\" SequenceNmbr=\"").append(requestParameterDO.sequenceNmbr)
                .append("\" TimeStamp=\"").append(CalendarUtility.getCurrentDateTimeStamp())
                .append("\" Version=\"").append(requestParameterDO.version).append("\" >")
                .append("<ns2:POS>").append("<ns2:Source TerminalID=\"TestUser/Test Runner\">")
                .append("<ns2:RequestorID ID=\"" + getRequesterId(requestParameterDO.srvUrlType) + "\" Type=\"4\" />")
                .append("<ns2:BookingChannel Type=\"12\" />").append("</ns2:Source>").append("</ns2:POS>")
                .append("<ns2:ReadRequests>").append("<ns2:ReadRequest>")
                .append("<ns2:UniqueID ID=\"" + PNR + "\" Type=\"" + pnrType + "\" />")
                .append("</ns2:ReadRequest>").append("</ns2:ReadRequests>")
                .append("</ns2:OTA_ReadRQ>")
                .append("<ns1:AAReadRQExt>")
                .append("<ns1:AALoadDataOptions>")
                .append("<ns1:LoadTravelerInfo>").append(true).append("</ns1:LoadTravelerInfo>")
                .append("<ns1:LoadAirItinery>").append(true).append("</ns1:LoadAirItinery>")
                .append("<ns1:LoadPriceInfoTotals>").append(true).append("</ns1:LoadPriceInfoTotals>")
                .append("<ns1:LoadPTCPriceInfo>").append(true).append("</ns1:LoadPTCPriceInfo>")
                .append("<ns1:LoadFullFilment>").append(true).append("</ns1:LoadFullFilment>")
                .append("</ns1:AALoadDataOptions>").append("</ns1:AAReadRQExt>")
                .append(SOAP_FOOTER);

        return soapRequest.toString();
    }

//	public static String getCancelFlightConfirm(RequestParameterDO requestParameterDO, String PNR, String pnrType) {
//		// TODO Auto-generated method stub
//		StringBuffer soapRequest = new StringBuffer();
//		soapRequest.append(getSoapHeader(requestParameterDO.srvUrlType) + SOAP_HEADER_SUB2)
//		.append("<ns2:OTA_AirBookModifyRQ  EchoToken=\"").append(requestParameterDO.echoToken)
//		.append("\" PrimaryLangID=\"").append(requestParameterDO.primaryLangID)
//		.append("\" SequenceNmbr=\"").append(requestParameterDO.sequenceNmbr)
//		.append("\" TimeStamp=\"").append(CalendarUtility.getCurrentDateTimeStamp())
//		.append("\" TransactionIdentifier=\"").append(requestParameterDO.transactionIdentifier)		
//		.append("\" Version=\"").append(requestParameterDO.version).append("\" JsessionId=\"").append(AppConstants.Cookie).append("\" >")		 
//		.append("<ns2:POS>").append("<ns2:Source TerminalID=\"TestUser/Test Runner\">")
//		.append("<ns2:RequestorID ID=\"" + getRequesterId(requestParameterDO.srvUrlType) + "\" Type=\"4\" />")
//		.append("<ns2:BookingChannel Type=\"12\" />").append("</ns2:Source>").append("</ns2:POS>")
//		.append("<ns2:AirBookModifyRQ ModificationType=\"1\">")
//		.append("<ns2:BookingReferenceID ID=\"" + PNR + "\" Type=\"" + pnrType + "\" />")
//		.append("<ns2:Fulfillment d3p1:nil=\"true\" xmlns:d3p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
//		.append("</ns2:AirBookModifyRQ>")
//		.append("<ns2:AirReservation d2p1:nil=\"true\" xmlns:d2p1=\"http://www.w3.org/2001/XMLSchema-instance\" />")
//		.append("</ns2:OTA_AirBookModifyRQ>")
//		.append("<ns1:AAAirBookModifyRQExt>").append("<ns1:AALoadDataOptions>")
//		.append("<ns1:LoadTravelerInfo>").append(true).append("</ns1:LoadTravelerInfo>")
//		.append("<ns1:LoadAirItinery>").append(true).append("</ns1:LoadAirItinery>")
//		.append("<ns1:LoadPriceInfoTotals>").append(true).append("</ns1:LoadPriceInfoTotals>")
//		.append("<ns1:LoadFullFilment>").append(true).append("</ns1:LoadFullFilment>")		
//		.append("<ns1:LoadPTCPriceInfo>").append(true).append("</ns1:LoadPTCPriceInfo>")
//		.append("</ns1:AALoadDataOptions>").append("</ns1:AAAirBookModifyRQExt>")
//		.append(SOAP_FOOTER);
//		
//		return soapRequest.toString();
//	}

    public static String getCancelFlightConfirm(RequestParameterDO requestParameterDO, String PNR, String pnrType) {
        // TODO Auto-generated method stub
        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append(getSoapHeader(requestParameterDO.srvUrlType) + SOAP_HEADER_SUB2)
                .append("<ns2:OTA_AirBookModifyRQ  EchoToken=\"").append(requestParameterDO.echoToken)
                .append("\" PrimaryLangID=\"").append(requestParameterDO.primaryLangID)
                .append("\" SequenceNmbr=\"").append(requestParameterDO.sequenceNmbr)
                .append("\" TimeStamp=\"").append(CalendarUtility.getCurrentDateTimeStamp())
                .append("\" TransactionIdentifier=\"").append(requestParameterDO.transactionIdentifier)
                .append("\" Version=\"").append(requestParameterDO.version).append("\" >")
                .append("<ns2:POS>").append("<ns2:Source TerminalID=\"TestUser/Test Runner\">")
                .append("<ns2:RequestorID ID=\"" + getRequesterId(requestParameterDO.srvUrlType) + "\" Type=\"4\" />")
                .append("<ns2:BookingChannel Type=\"12\" />").append("</ns2:Source>").append("</ns2:POS>")
                .append("<ns2:AirBookModifyRQ ModificationType=\"1\">")
                .append("<ns2:BookingReferenceID ID=\"" + PNR + "\" Type=\"" + pnrType + "\" />")
                .append("</ns2:AirBookModifyRQ>")
                .append("</ns2:OTA_AirBookModifyRQ>")
                .append("<ns1:AAAirBookModifyRQExt>").append("<ns1:AALoadDataOptions>")
                .append("<ns1:LoadTravelerInfo>").append(true).append("</ns1:LoadTravelerInfo>")
                .append("<ns1:LoadAirItinery>").append(true).append("</ns1:LoadAirItinery>")
                .append("<ns1:LoadPriceInfoTotals>").append(true).append("</ns1:LoadPriceInfoTotals>")
                .append("<ns1:LoadFullFilment>").append(true).append("</ns1:LoadFullFilment>")
                .append("</ns1:AALoadDataOptions>").append("</ns1:AAAirBookModifyRQExt>")
                .append(SOAP_FOOTER);

        return soapRequest.toString();
    }

}
