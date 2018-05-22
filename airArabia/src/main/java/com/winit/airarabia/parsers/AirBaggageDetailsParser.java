package com.winit.airarabia.parsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.winit.airarabia.objects.AirBaggageDetailsDO;
import com.winit.airarabia.objects.BaggageDO;
import com.winit.airarabia.objects.BaggageDetailDO;
import com.winit.airarabia.objects.FlightSegmentDO;

public class AirBaggageDetailsParser extends BaseHandler{

	private StringBuffer stringBuffer = null;
	private boolean isActive = false;
	private AirBaggageDetailsDO airBaggageDetailsDO;
	private Vector<BaggageDetailDO> vecBaggageDetailDOs;
	private BaggageDetailDO baggageDetailDO;
	private FlightSegmentDO flightSegmentDO;
	private BaggageDO baggageDO;
	private boolean isFirstOnDBaggageDetailsResponseDone = false;
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		isActive = true;
		if(localName.equalsIgnoreCase("AA_OTA_AirBaggageDetailsRS"))
		{
			airBaggageDetailsDO = new AirBaggageDetailsDO();
			airBaggageDetailsDO.echoToken = getString(attributes.getValue("EchoToken"));
			airBaggageDetailsDO.primaryLangID = getString(attributes.getValue("PrimaryLangID"));
			airBaggageDetailsDO.sequenceNmbr = getString(attributes.getValue("SequenceNmbr"));
			airBaggageDetailsDO.transactionIdentifier = getString(attributes.getValue("TransactionIdentifier"));
			airBaggageDetailsDO.version = getString(attributes.getValue("Version"));
		}
		else if(localName.equalsIgnoreCase("Error"))
		{
			airBaggageDetailsDO.ErrorMsg = getString(attributes.getValue("ShortText"));
		}
		else if(localName.equalsIgnoreCase("BaggageDetailsResponses"))
			vecBaggageDetailDOs = new Vector<BaggageDetailDO>();
		else if(localName.equalsIgnoreCase("OnDBaggageDetailsResponse") && !isFirstOnDBaggageDetailsResponseDone)
			baggageDetailDO = new BaggageDetailDO();
		else if(localName.equalsIgnoreCase("OnDFlightSegmentInfo"))
		{
			flightSegmentDO = new FlightSegmentDO();
			flightSegmentDO.arrivalDateTime = getString(attributes.getValue("ArrivalDateTime"));
			flightSegmentDO.departureDateTime = getString(attributes.getValue("DepartureDateTime"));
			flightSegmentDO.flightNumber = getString(attributes.getValue("FlightNumber"));
			flightSegmentDO.segmentCode = getString(attributes.getValue("SegmentCode"));
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
		else if(localName.equalsIgnoreCase("Baggage"))
			baggageDO = new BaggageDO();
		stringBuffer = new StringBuffer();
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException 
	{
		isActive = false;
		if(localName.equalsIgnoreCase("OnDBaggagesEnabled"))
			airBaggageDetailsDO.isBaggageEnable = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("OnDFlightSegmentInfo"))
			baggageDetailDO.vecFlightSegmentDOs.add(flightSegmentDO);
		else if(localName.equalsIgnoreCase("baggageCode"))
			baggageDO.baggageCode = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("baggageDescription"))
			baggageDO.baggageDescription = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("baggageCharge"))
			baggageDO.baggageCharge = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("currencyCode"))
			baggageDO.currencyCode = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("Baggage") && !isFirstOnDBaggageDetailsResponseDone)
			baggageDetailDO.vecBaggageDO.add(baggageDO);
		else if(localName.equalsIgnoreCase("OnDBaggageDetailsResponse"))
		{
			if(!isFirstOnDBaggageDetailsResponseDone)
			{
				isFirstOnDBaggageDetailsResponseDone = true;
				vecBaggageDetailDOs.add(baggageDetailDO);
			}
		}
		else if(localName.equalsIgnoreCase("BaggageDetailsResponses"))
			airBaggageDetailsDO.vecBaggageDetailDOs = vecBaggageDetailDOs;
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
		if(airBaggageDetailsDO != null)
			return airBaggageDetailsDO;
		return null;
	}
	@Override
	public Object getErrorData() {
			return null;
	}

}
