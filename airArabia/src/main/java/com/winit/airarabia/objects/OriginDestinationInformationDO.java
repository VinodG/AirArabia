package com.winit.airarabia.objects;

import java.util.ArrayList;
import java.util.Vector;

public class OriginDestinationInformationDO extends BaseDO
{
	public String originLocationCode = "";
	public String destinationLocationCode = "";
	public String originLocation = "";
	public String destinationLocation = "";
	public String departureDateTime = "";
	public String arrivalDateTime = "";
	
	public Vector<FlightSegmentDO> departureFlightSegments = new Vector<FlightSegmentDO>();
	public Vector<FlightSegmentDO> arrivalFlightSegments = new Vector<FlightSegmentDO>();
	
	public Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();

	public Vector<OriginDestinationOptionDO> departureVecOriginDestinationOptionDO = new Vector<OriginDestinationOptionDO>();
	public Vector<OriginDestinationOptionDO> arrivalVecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();
	
	public ArrayList<BundledServiceDO> arlBundledServiceDOs;
	
	public PTC_FareBreakdownDO mPTC_FareBreakdownDOPromo = null,
			mPTC_FareBreakdownDOFlexi = null;
}
