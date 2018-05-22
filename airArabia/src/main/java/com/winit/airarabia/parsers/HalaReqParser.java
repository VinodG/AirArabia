package com.winit.airarabia.parsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirHalaDO;
import com.winit.airarabia.objects.AirportServiceDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.HalaDO;

public class HalaReqParser extends BaseHandler{

	private StringBuffer stringBuffer = null;
	private boolean isActive = false;
	private AirHalaDO airHalaDO;
	private Vector<HalaDO> vecHalaDOs;
	private HalaDO halaDO;
	private FlightSegmentDO flightSegmentDO;
	private Vector<AirportServiceDO> vecAirportServiceDOs = new Vector<AirportServiceDO>();
	private AirportServiceDO airportServiceDO;
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		isActive = true;
		if(localName.equalsIgnoreCase("AA_OTA_AirSSRDetailsRS"))
		{
			airHalaDO = new AirHalaDO();
			airHalaDO.echoToken = getString(attributes.getValue("EchoToken"));
			airHalaDO.primaryLangID = getString(attributes.getValue("PrimaryLangID"));
			airHalaDO.sequenceNmbr = getString(attributes.getValue("SequenceNmbr"));
			airHalaDO.transactionIdentifier = getString(attributes.getValue("TransactionIdentifier"));
			airHalaDO.version = getString(attributes.getValue("Version"));
		}
		else if(localName.equalsIgnoreCase("SSRDetailsResponses"))
			vecHalaDOs = new Vector<HalaDO>();
		else if(localName.equalsIgnoreCase("SSRDetailsResponse"))
		{
			halaDO = new HalaDO();
			halaDO.airportCode = getString(attributes.getValue("AirportCode"));
			halaDO.airportType = getString(attributes.getValue("AirportType"));
		}
		else if(localName.equalsIgnoreCase("FlightSegmentInfo"))
		{
			flightSegmentDO = new FlightSegmentDO();
			flightSegmentDO.arrivalDateTime = getString(attributes.getValue("ArrivalDateTime"));
			flightSegmentDO.departureDateTime = getString(attributes.getValue("DepartureDateTime"));
			flightSegmentDO.flightNumber = getString(attributes.getValue("FlightNumber"));
			flightSegmentDO.segmentCode = getString(attributes.getValue("SegmentCode"));
			
			
			int isFound = 0;
			if(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs != null && AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size() > 0)
			{
				for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.size(); i++) {
					if(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs!= null
							&& AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs.size() > 0)
					{
						for (int j = 0; j < AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs.size(); j++) {
							if(AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs.get(j).flightNumber.equalsIgnoreCase(flightSegmentDO.flightNumber))
							{
								isFound = 1;
								flightSegmentDO.RPH = AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs.get(j).RPH;
								break;
							}
						}
					}
				}
			}
			if(isFound == 0)
			{
				if(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs != null && AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size() > 0)
				{
					for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.size(); i++) {
						if(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs!= null
								&& AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs.size() > 0)
						{
							for (int j = 0; j < AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs.size(); j++) {
								if(AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs.get(j).flightNumber.equalsIgnoreCase(flightSegmentDO.flightNumber))
								{
									flightSegmentDO.RPH = AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.get(i).vecFlightSegmentDOs.get(j).RPH;
									break;
								}
							}
						}
					}
				}
			}
//			else
//				flightSegmentDO.RPH = getString(attributes.getValue("RPH"));
//			flightSegmentDO.RPH = getString(attributes.getValue("RPH"));
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
		else if(localName.equalsIgnoreCase("AirportService"))
			airportServiceDO = new AirportServiceDO();
		stringBuffer = new StringBuffer();
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException 
	{
		isActive = false;
		if(localName.equalsIgnoreCase("FlightSegmentInfo"))
			halaDO.flightSegmentDO = flightSegmentDO;
		else if(localName.equalsIgnoreCase("ssrCode"))
			airportServiceDO.ssrCode = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("ssrName"))
			airportServiceDO.ssrName = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("ssrDescription"))
			airportServiceDO.ssrDescription = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("applicabilityType"))
			airportServiceDO.applicabilityType = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("adultAmount"))
			airportServiceDO.adultAmount = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("childAmount"))
			airportServiceDO.childAmount = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("infantAmount"))
			airportServiceDO.infantAmount = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("reservationAmount"))
			airportServiceDO.reservationAmount = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("AirportService"))
			vecAirportServiceDOs.add(airportServiceDO);
		else if(localName.equalsIgnoreCase("SSRDetailsResponse"))
		{
			halaDO.vecAirportServiceDOs = vecAirportServiceDOs;
			vecHalaDOs.add(halaDO);
		}
		else if(localName.equalsIgnoreCase("SSRDetailsResponses"))
			airHalaDO.vecHalaDOs = vecHalaDOs;
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
		if(airHalaDO != null)
			return airHalaDO;
		return null;
	}
	@Override
	public Object getErrorData() {
			return null;
	}
}