package com.winit.airarabia.objects;

import java.util.Vector;

public class BookingFlightDO extends BaseDO
{
	public BookFlightDO myBookFlightDO = new BookFlightDO(),myBookFlightDOReturn = new BookFlightDO();
	public OriginDestinationInformationDO myODIDOOneWay = new OriginDestinationInformationDO(),
											myODIDOReturn = new OriginDestinationInformationDO();
	public RequestParameterDO requestParameterDO = new RequestParameterDO(),
								requestParameterDOReturn = new RequestParameterDO();
	public PassengerInfoDO passengerInfoDO = new PassengerInfoDO();
	public boolean isFlexiOut = false,isFlexiIn = false;
	public boolean isOutBoundClick = false, isInBoundClick = false;
	
	public Vector<RequestDO> vecSeatRequestDOs = new Vector<RequestDO>(),
								vecMealReqDOs = new Vector<RequestDO>(),
								vecBaggageRequestDOs = new Vector<RequestDO>(),
								vecInsrRequestDOs = new Vector<RequestDO>(),
								vecSSRRequests = new Vector<RequestDO>();
	public String pnr = "", pnrType = "", modificationType = "";
	public Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOsModify = new Vector<OriginDestinationOptionDO>();

	public String bundleServiceID = "";
	
//	private BookingFlightDO()
//	{
//		
//	}
//	
//	public static BookingFlightDO bookingFlightDO = new BookingFlightDO();
}
