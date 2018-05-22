package com.winit.airarabia.parsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.winit.airarabia.objects.AirScheduleDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;

public class AirScheduleParser extends BaseHandler{

	private StringBuffer stringBuffer = null;
	private boolean isActive = false;
	private AirScheduleDO airScheduleDO;
	private Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs;
	private OriginDestinationOptionDO originDestinationOptionsDO;
	private FlightSegmentDO flightSegmentDO;
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		isActive = true;
		if(localName.equalsIgnoreCase("OTA_AirScheduleRS"))
		{
			airScheduleDO = new AirScheduleDO();
			airScheduleDO.echoToken = getString(attributes.getValue("EchoToken"));
			airScheduleDO.primaryLangID = getString(attributes.getValue("PrimaryLangID"));
			airScheduleDO.sequenceNmbr = getString(attributes.getValue("SequenceNmbr"));
			airScheduleDO.version = getString(attributes.getValue("Version"));
		}
		else if(localName.equalsIgnoreCase("OriginDestinationOptions"))
			vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();
		else if(localName.equalsIgnoreCase("OriginDestinationOption"))
			originDestinationOptionsDO = new OriginDestinationOptionDO();
		else if(localName.equalsIgnoreCase("FlightSegment"))
		{
			flightSegmentDO = new FlightSegmentDO();
			flightSegmentDO.arrivalDateTime = getString(attributes.getValue("ArrivalDateTime"));
			flightSegmentDO.departureDateTime = getString(attributes.getValue("DepartureDateTime"));
			flightSegmentDO.ScheduleValidEndDate = getString(attributes.getValue("ScheduleValidEndDate"));
			flightSegmentDO.ScheduleValidStartDate = getString(attributes.getValue("ScheduleValidStartDate"));
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
		else if(localName.equalsIgnoreCase("OperationTime"))
		{
			flightSegmentDO.operationTimeFri = getString(attributes.getValue("Fri"));
			flightSegmentDO.operationTimeSat = getString(attributes.getValue("Sat"));
			flightSegmentDO.operationTimeSun = getString(attributes.getValue("Sun"));
			flightSegmentDO.operationTimeMon = getString(attributes.getValue("Mon"));
			flightSegmentDO.operationTimeTue = getString(attributes.getValue("Tue"));
			flightSegmentDO.operationTimeWeds = getString(attributes.getValue("Weds"));
			flightSegmentDO.operationTimeThur = getString(attributes.getValue("Thur"));
		}
		stringBuffer = new StringBuffer();
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException 
	{
		isActive = false;
		if(localName.equalsIgnoreCase("FlightSegment"))
			originDestinationOptionsDO.vecFlightSegmentDOs.add(flightSegmentDO);
		else if(localName.equalsIgnoreCase("OriginDestinationOption"))
			vecOriginDestinationOptionDOs.add(originDestinationOptionsDO);
		else if(localName.equalsIgnoreCase("OriginDestinationOptions"))
			airScheduleDO.vecOriginDestinationOptionDOs = vecOriginDestinationOptionDOs;
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
		if(airScheduleDO != null)
			return airScheduleDO;
		else
			return null;
	}
	@Override
	public Object getErrorData() {
			return null;
	}
}