package com.winit.airarabia.parsers;

import java.util.ArrayList;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirPriceQuoteDO;
import com.winit.airarabia.objects.AvailableFlexiFaresDO;
import com.winit.airarabia.objects.BundledServiceDO;
import com.winit.airarabia.objects.FareDO;
import com.winit.airarabia.objects.FlexiOperationsDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;
import com.winit.airarabia.objects.PTC_FareBreakdownDO;
import com.winit.airarabia.objects.PricedItineraryDO;

public class AirPriceQuoteParser extends BaseHandler {

    private StringBuffer stringBuffer = null;
    private boolean isActive = false;
    private AirPriceQuoteDO airPriceQuoteDO;
    private Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs;
    private OriginDestinationOptionDO originDestinationOptionsDO;
    private FlightSegmentDO flightSegmentDO;
    private ArrayList<PricedItineraryDO> vecPricedItineraryDOs;
    private PricedItineraryDO pricedItineraryDO;
    private Vector<PTC_FareBreakdownDO> vecPTC_FareBreakdownDOs;
    private PTC_FareBreakdownDO ptc_FareBreakdownDO;
    private AvailableFlexiFaresDO availableFlexiFaresDO;
    private FlexiOperationsDO flexiOperationsDO;
    private boolean isPassengerFare = false;

    private BundledServiceDO bundledServiceDO;

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        isActive = true;
        if (localName.equalsIgnoreCase("OTA_AirPriceRS")) {
            airPriceQuoteDO = new AirPriceQuoteDO();
            airPriceQuoteDO.echoToken = getString(attributes.getValue("EchoToken"));
            airPriceQuoteDO.primaryLangID = getString(attributes.getValue("PrimaryLangID"));
            airPriceQuoteDO.sequenceNmbr = getString(attributes.getValue("SequenceNmbr"));
            airPriceQuoteDO.transactionIdentifier = getString(attributes.getValue("TransactionIdentifier"));
            airPriceQuoteDO.version = getString(attributes.getValue("Version"));
        } else if (localName.equalsIgnoreCase("Error")) {
            airPriceQuoteDO.ErrorMsg = getString(attributes.getValue("ShortText"));
        } else if (localName.equalsIgnoreCase("OriginDestinationOptions"))
            vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();
        else if (localName.equalsIgnoreCase("OriginDestinationOption"))
            originDestinationOptionsDO = new OriginDestinationOptionDO();
        else if (localName.equalsIgnoreCase("FlightSegment")) {
            flightSegmentDO = new FlightSegmentDO();
            flightSegmentDO.arrivalDateTime = getString(attributes.getValue("ArrivalDateTime"));
            flightSegmentDO.departureDateTime = getString(attributes.getValue("DepartureDateTime"));
            flightSegmentDO.flightNumber = getString(attributes.getValue("FlightNumber"));
            flightSegmentDO.journeyDuration = getString(attributes.getValue("JourneyDuration"));
            flightSegmentDO.RPH = getString(attributes.getValue("RPH"));
        } else if (localName.equalsIgnoreCase("DepartureAirport")) {
            flightSegmentDO.departureAirportCode = getString(attributes.getValue("LocationCode"));
            flightSegmentDO.departureAirportTerminal = getString(attributes.getValue("Terminal"));
        } else if (localName.equalsIgnoreCase("ArrivalAirport")) {
            flightSegmentDO.arrivalAirportCode = getString(attributes.getValue("LocationCode"));
            flightSegmentDO.arrivalAirportTerminal = getString(attributes.getValue("Terminal"));
        } else if (localName.equalsIgnoreCase("PricedItineraries"))
            vecPricedItineraryDOs = new ArrayList<PricedItineraryDO>();
        else if (localName.equalsIgnoreCase("PricedItinerary")) {
            pricedItineraryDO = new PricedItineraryDO();
            pricedItineraryDO.sequenceNumber = getString(attributes.getValue("SequenceNumber"));
        }
        /* AABundledServiceExt Start */
        else if (localName.equalsIgnoreCase("AABundledServiceExt")) {
            if (pricedItineraryDO.arlBundledServiceDOs != null && pricedItineraryDO.arlBundledServiceDOs.size() > 0) {

            } else
                pricedItineraryDO.arlBundledServiceDOs = new ArrayList<BundledServiceDO>();
        } else if (localName.equalsIgnoreCase("bundledService")) {
            bundledServiceDO = new BundledServiceDO();
            bundledServiceDO.arlIncludedServies = new ArrayList<String>();
        }
        /* AABundledServiceExt End */

        else if (localName.equalsIgnoreCase("AirItineraryPricingInfo"))
            pricedItineraryDO.pricingSource = getString(attributes.getValue("PricingSource"));
        else if (localName.equalsIgnoreCase("BaseFare")) {
            if (!isPassengerFare) {
                pricedItineraryDO.baseFare.amount = getString(attributes.getValue("Amount"));
                pricedItineraryDO.baseFare.currencyCode = getString(attributes.getValue("CurrencyCode"));
                pricedItineraryDO.baseFare.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
            } else {
                ptc_FareBreakdownDO.baseFare.amount = getString(attributes.getValue("Amount"));
                ptc_FareBreakdownDO.baseFare.currencyCode = getString(attributes.getValue("CurrencyCode"));
                ptc_FareBreakdownDO.baseFare.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
            }
        } else if (localName.equalsIgnoreCase("TotalFare")) {
            if (!isPassengerFare) {
                pricedItineraryDO.totalFare.amount = getString(attributes.getValue("Amount"));
                pricedItineraryDO.totalFare.currencyCode = getString(attributes.getValue("CurrencyCode"));
                pricedItineraryDO.totalFare.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
            } else {
                ptc_FareBreakdownDO.totalFare.amount = getString(attributes.getValue("Amount"));
                ptc_FareBreakdownDO.totalFare.currencyCode = getString(attributes.getValue("CurrencyCode"));
                ptc_FareBreakdownDO.totalFare.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
            }
        } else if (localName.equalsIgnoreCase("TotalEquivFare")) {
            pricedItineraryDO.totalEquivFare.amount = getString(attributes.getValue("Amount"));
            pricedItineraryDO.totalEquivFare.currencyCode = getString(attributes.getValue("CurrencyCode"));
            pricedItineraryDO.totalEquivFare.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
        } else if (localName.equalsIgnoreCase("TotalFareWithCCFee")) {
            pricedItineraryDO.totalFareWithCCFee.amount = getString(attributes.getValue("Amount"));
            pricedItineraryDO.totalFareWithCCFee.currencyCode = getString(attributes.getValue("CurrencyCode"));
            pricedItineraryDO.totalFareWithCCFee.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
        } else if (localName.equalsIgnoreCase("TotalEquivFareWithCCFee")) {
            pricedItineraryDO.totalEquivFareWithCCFee.amount = getString(attributes.getValue("Amount"));
            pricedItineraryDO.totalEquivFareWithCCFee.currencyCode = getString(attributes.getValue("CurrencyCode"));
            pricedItineraryDO.totalEquivFareWithCCFee.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
        } else if (localName.equalsIgnoreCase("PTC_FareBreakdowns"))
            vecPTC_FareBreakdownDOs = new Vector<PTC_FareBreakdownDO>();
        else if (localName.equalsIgnoreCase("PTC_FareBreakdown")) {
            ptc_FareBreakdownDO = new PTC_FareBreakdownDO();
            ptc_FareBreakdownDO.pricingSource = getString(attributes.getValue("PricingSource"));
        } else if (localName.equalsIgnoreCase("PassengerTypeQuantity")) {
            ptc_FareBreakdownDO.code = getString(attributes.getValue("Code"));
            ptc_FareBreakdownDO.quantity = getString(attributes.getValue("Quantity"));
        } else if (localName.equalsIgnoreCase("FareBasisCodes"))
            ptc_FareBreakdownDO.vecFareBasicCodes = new Vector<String>();
        else if (localName.equalsIgnoreCase("PassengerFare"))
            isPassengerFare = true;
        else if (localName.equalsIgnoreCase("Tax")) {
            FareDO tax = new FareDO();
            tax.amount = getString(attributes.getValue("Amount"));
            tax.currencyCode = getString(attributes.getValue("CurrencyCode"));
            tax.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
            tax.taxCode = getString(attributes.getValue("TaxCode"));
            tax.taxName = getString(attributes.getValue("TaxName"));
            ptc_FareBreakdownDO.vecTaxes.add(tax);
        } else if (localName.equalsIgnoreCase("Fee")) {
            FareDO fee = new FareDO();
            fee.amount = getString(attributes.getValue("Amount"));
            fee.currencyCode = getString(attributes.getValue("CurrencyCode"));
            fee.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
            fee.feeCode = getString(attributes.getValue("FeeCode"));
            ptc_FareBreakdownDO.vecFees.add(fee);
        } else if (localName.equalsIgnoreCase("TravelerRefNumber"))
            ptc_FareBreakdownDO.vecTravelerRefNumbers.add(getString(attributes.getValue("RPH")));
        else if (localName.equalsIgnoreCase("AvailableFlexiFares"))
            availableFlexiFaresDO = new AvailableFlexiFaresDO();
        else if (localName.equalsIgnoreCase("FlexiFare"))
            availableFlexiFaresDO.applicableJourneyType = getString(attributes.getValue("ApplicableJourneyType"));
        else if (localName.equalsIgnoreCase("FlexiFareAmount")) {
            availableFlexiFaresDO.flexiFareAmountDO = new FareDO();
            availableFlexiFaresDO.flexiFareAmountDO.amount = getString(attributes.getValue("Amount"));
            availableFlexiFaresDO.flexiFareAmountDO.currencyCode = getString(attributes.getValue("CurrencyCode"));
            availableFlexiFaresDO.flexiFareAmountDO.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
        } else if (localName.equalsIgnoreCase("AllowedFlexiOperations"))
            availableFlexiFaresDO.vecFlexiOperationsDO = new Vector<FlexiOperationsDO>();
        else if (localName.equalsIgnoreCase("FlexiOperations")) {
            flexiOperationsDO = new FlexiOperationsDO();
            flexiOperationsDO.allowedOperationName = getString(attributes.getValue("AllowedOperationName"));
            flexiOperationsDO.flexiOperationCutoverTimeInMinutes = getString(attributes.getValue("FlexiOperationCutoverTimeInMinutes"));
            flexiOperationsDO.numberOfAllowedOperations = getString(attributes.getValue("NumberOfAllowedOperations"));
            availableFlexiFaresDO.vecFlexiOperationsDO.add(flexiOperationsDO);
        } else if (localName.equalsIgnoreCase("PerPaxFlexifareBDS"))
            availableFlexiFaresDO.vecPerPaxFlexifareBDS = new Vector<FareDO>();
        else if (localName.equalsIgnoreCase("PerPaxFlexiFareAmount")) {
            FareDO fee = new FareDO();
            fee.amount = getString(attributes.getValue("Amount"));
            fee.ApplicablePaxType = getString(attributes.getValue("ApplicablePaxType"));
            fee.currencyCode = getString(attributes.getValue("CurrencyCode"));
            fee.decimalPlaces = getString(attributes.getValue("DecimalPlaces"));
            availableFlexiFaresDO.vecPerPaxFlexifareBDS.add(fee);
        }
        stringBuffer = new StringBuffer();
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        isActive = false;
        if (localName.equalsIgnoreCase("FlightSegment"))
            originDestinationOptionsDO.vecFlightSegmentDOs.add(flightSegmentDO);
        else if (localName.equalsIgnoreCase("OriginDestinationOption"))
            vecOriginDestinationOptionDOs.add(originDestinationOptionsDO);
        else if (localName.equalsIgnoreCase("OriginDestinationOptions"))
            pricedItineraryDO.vecOriginDestinationOptionDOs = vecOriginDestinationOptionDOs;
        else if (localName.equalsIgnoreCase("FareBasisCode"))
            ptc_FareBreakdownDO.vecFareBasicCodes.add(stringBuffer.toString());
        else if (localName.equalsIgnoreCase("PassengerFare"))
            isPassengerFare = false;

        else if (localName.equalsIgnoreCase("FlexibilityDescription"))
            availableFlexiFaresDO.flexibilityDescription = stringBuffer.toString();
        else if (localName.equalsIgnoreCase("FlexiRuleCode"))
            availableFlexiFaresDO.flexiRuleCode = stringBuffer.toString();
        else if (localName.equalsIgnoreCase("AvailableFlexiFares"))
            pricedItineraryDO.availableFlexiFaresDO = availableFlexiFaresDO;

		/* AABundledServiceExt Start */
        else if (localName.equalsIgnoreCase("bunldedServiceId")) {
            if (bundledServiceDO != null)
                bundledServiceDO.bunldedServiceId = stringBuffer.toString();
        } else if (localName.equalsIgnoreCase("bundledServiceName")) {
            if (bundledServiceDO != null)
                bundledServiceDO.bundledServiceName = stringBuffer.toString();
        } else if (localName.equalsIgnoreCase("perPaxBundledFee")) {
            if (bundledServiceDO != null)
                bundledServiceDO.perPaxBundledFee = stringBuffer.toString();
        } else if (localName.equalsIgnoreCase("bookingClasses")) {
            if (bundledServiceDO != null)
                bundledServiceDO.bookingClasses = stringBuffer.toString();
        } else if (localName.equalsIgnoreCase("description")) {
            if (bundledServiceDO != null)
                bundledServiceDO.description = stringBuffer.toString();
        } else if (localName.equalsIgnoreCase("includedServies")) {
            if (bundledServiceDO != null)
                bundledServiceDO.arlIncludedServies.add(stringBuffer.toString());
        } else if (localName.equalsIgnoreCase("bundledService")) {
            pricedItineraryDO.arlBundledServiceDOs.add(bundledServiceDO);
            AppConstants.arlBundledServiceDOs = pricedItineraryDO.arlBundledServiceDOs;
        }
		/* AABundledServiceExt End */

        else if (localName.equalsIgnoreCase("PTC_FareBreakdown")) {
            vecPTC_FareBreakdownDOs.add(ptc_FareBreakdownDO);
            if (AppConstants.hashForListOfDifferentPrice != null && ptc_FareBreakdownDO.code.equalsIgnoreCase("ADT")) {
//                AppConstants.count++;
                if (AppConstants.fareType.equalsIgnoreCase("Basic"))
                    AppConstants.hashForListOfDifferentPrice.put("Basic", ptc_FareBreakdownDO);
                else if (AppConstants.fareType.equalsIgnoreCase("Value"))
                    AppConstants.hashForListOfDifferentPrice.put("Value", ptc_FareBreakdownDO);
                else if (AppConstants.fareType.equalsIgnoreCase("Extra"))
                    AppConstants.hashForListOfDifferentPrice.put("Extra", ptc_FareBreakdownDO);
            }
        } else if (localName.equalsIgnoreCase("PTC_FareBreakdowns")) {
            pricedItineraryDO.vecPTC_FareBreakdownDOs = vecPTC_FareBreakdownDOs;
        } else if (localName.equalsIgnoreCase("PricedItinerary"))
            vecPricedItineraryDOs.add(pricedItineraryDO);
        else if (localName.equalsIgnoreCase("PricedItineraries"))
            airPriceQuoteDO.vecPricedItineraryDOs = vecPricedItineraryDOs;
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if (isActive) {
            try {
                stringBuffer.append(ch, start, length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object getData() {
        if (airPriceQuoteDO != null)
            return airPriceQuoteDO;
        else
            return null;
    }

    @Override
    public Object getErrorData() {
        return null;
    }
}