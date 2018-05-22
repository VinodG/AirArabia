package com.winit.airarabia.parsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirMealDetailsDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.MealDO;
import com.winit.airarabia.objects.MealDetailsDO;

public class AirMealDetailsParser extends BaseHandler{

	private StringBuffer stringBuffer = null;
	private boolean isActive = false;
	private AirMealDetailsDO airMealDetailsDO;
	private Vector<MealDetailsDO> vecMealDetailsDOs;
	private MealDetailsDO mealDetailsDO;
	private FlightSegmentDO flightSegmentDO;
	private MealDO mealDO;
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		isActive = true;
		if(localName.equalsIgnoreCase("AA_OTA_AirMealDetailsRS"))
		{
			airMealDetailsDO = new AirMealDetailsDO();
			airMealDetailsDO.echoToken = getString(attributes.getValue("EchoToken"));
			airMealDetailsDO.primaryLangID = getString(attributes.getValue("PrimaryLangID"));
			airMealDetailsDO.sequenceNmbr = getString(attributes.getValue("SequenceNmbr"));
			airMealDetailsDO.transactionIdentifier = getString(attributes.getValue("TransactionIdentifier"));
			airMealDetailsDO.version = getString(attributes.getValue("Version"));
		}
		else if(localName.equalsIgnoreCase("MealDetailsResponses"))
			vecMealDetailsDOs = new Vector<MealDetailsDO>();
		else if(localName.equalsIgnoreCase("MealDetailsResponse"))
			mealDetailsDO = new MealDetailsDO();
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
		else if(localName.equalsIgnoreCase("Meal"))
			mealDO = new MealDO();
		stringBuffer = new StringBuffer();
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException 
	{
		isActive = false;
		if(localName.equalsIgnoreCase("multipleMealSelectionEnabled"))
			airMealDetailsDO.isMultipleMealSel = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("FlightSegmentInfo"))
			mealDetailsDO.flightSegmentDO = flightSegmentDO;
		else if(localName.equalsIgnoreCase("mealCode"))
			mealDO.mealCode = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("mealDescription"))
			mealDO.mealDescription = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("mealCharge"))
			mealDO.mealCharge = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("mealName"))
			mealDO.mealName = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("availableMeals"))
			mealDO.availableMeals = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("soldMeals"))
			mealDO.soldMeals = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("allocatedMeals"))
			mealDO.allocatedMeals = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("mealImageLink"))
			mealDO.mealImageLink = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("mealCategoryCode"))
			mealDO.mealCategoryCode = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("Meal"))
			mealDetailsDO.vecMealsDO.add(mealDO);
		else if(localName.equalsIgnoreCase("MealDetailsResponse"))
			vecMealDetailsDOs.add(mealDetailsDO);
		else if(localName.equalsIgnoreCase("MealDetailsResponses"))
			airMealDetailsDO.vecMealDetailsDOs = vecMealDetailsDOs;
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
		if(airMealDetailsDO != null)
			return airMealDetailsDO;
		return null;
	}
	@Override
	public Object getErrorData() {
			return null;
	}
}