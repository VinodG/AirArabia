package com.winit.airarabia.parsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirRowDO;
import com.winit.airarabia.objects.AirSeatDO;
import com.winit.airarabia.objects.AirSeatMapDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.SeatMapDO;

public class AirSeatMapParser extends BaseHandler{

	private StringBuffer stringBuffer = null;
	private boolean isActive = false;
	private AirSeatMapDO airSeatMapDO;
	private Vector<SeatMapDO> vecSeatMapDOs;
	private SeatMapDO seatMapDO;
	private FlightSegmentDO flightSegmentDO;
	private Vector<AirRowDO> vecAirRowDOs;
	private AirRowDO airRowDO;
	private Vector<AirSeatDO> vecAirSeatDOs;
	private AirSeatDO airSeatDO;
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		isActive = true;
		if(localName.equalsIgnoreCase("OTA_AirSeatMapRS"))
		{
			airSeatMapDO = new AirSeatMapDO();
			airSeatMapDO.echoToken = getString(attributes.getValue("EchoToken"));
			airSeatMapDO.primaryLangID = getString(attributes.getValue("PrimaryLangID"));
			airSeatMapDO.sequenceNmbr = getString(attributes.getValue("SequenceNmbr"));
			airSeatMapDO.version = getString(attributes.getValue("Version"));
		}
		else if(localName.equalsIgnoreCase("SeatMapResponses"))
			vecSeatMapDOs = new Vector<SeatMapDO>();
		else if(localName.equalsIgnoreCase("SeatMapResponse"))
			seatMapDO = new SeatMapDO();
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
		else if(localName.equalsIgnoreCase("CabinClass"))
			seatMapDO.cabinType =  getString(attributes.getValue("CabinType"));
		else if(localName.equalsIgnoreCase("AirRows"))
			vecAirRowDOs = new Vector<AirRowDO>();
		else if(localName.equalsIgnoreCase("AirRow"))
		{
			airRowDO = new AirRowDO();
			airRowDO.rowNumber = getString(attributes.getValue("RowNumber"));
		}
		else if(localName.equalsIgnoreCase("AirSeats"))
			vecAirSeatDOs = new Vector<AirSeatDO>();
		else if(localName.equalsIgnoreCase("AirSeat"))
		{
			airSeatDO = new AirSeatDO();
			airSeatDO.seatAvailability   = getString(attributes.getValue("SeatAvailability"));
			airSeatDO.seatCharacteristics   = getString(attributes.getValue("SeatCharacteristics"));
			airSeatDO.seatNumber   = getString(attributes.getValue("SeatNumber"));
		}
		stringBuffer = new StringBuffer();
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException 
	{
		isActive = false;
		if(localName.equalsIgnoreCase("FlightSegmentInfo"))
			seatMapDO.flightSegmentDO = flightSegmentDO;
		else if(localName.equalsIgnoreCase("AirSeat"))
			vecAirSeatDOs.add(airSeatDO);
		else if(localName.equalsIgnoreCase("AirSeats"))
			airRowDO.vecAirSeatDOs = vecAirSeatDOs;
		else if(localName.equalsIgnoreCase("AirRow"))
			vecAirRowDOs.add(airRowDO);
		else if(localName.equalsIgnoreCase("AirRows"))
			seatMapDO.vecAirRowDOs = vecAirRowDOs;
		else if(localName.equalsIgnoreCase("SeatMapResponse"))
			vecSeatMapDOs.add(seatMapDO);
		else if(localName.equalsIgnoreCase("SeatMapResponses"))
			airSeatMapDO.vecSeatMapDOs = vecSeatMapDOs;
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
		if(airSeatMapDO != null)
			return airSeatMapDO;
		return null;
	}
	@Override
	public Object getErrorData() {
			return null;
	}
}