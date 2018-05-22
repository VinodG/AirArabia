package com.winit.airarabia.objects;

import java.util.ArrayList;
import java.util.Vector;

public class PricedItineraryDO extends BaseDO
{
	public String sequenceNumber = "";
	public String pricingSource  = "";
	public FareDO baseFare = new FareDO();
	public FareDO totalFare = new FareDO();
	public FareDO totalEquivFare = new FareDO();
	public FareDO totalFareWithCCFee = new FareDO();
	public FareDO totalEquivFareWithCCFee = new FareDO();
	public Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs;
	public Vector<PTC_FareBreakdownDO> vecPTC_FareBreakdownDOs;
	public AvailableFlexiFaresDO availableFlexiFaresDO = new AvailableFlexiFaresDO();
	
	public ArrayList<BundledServiceDO> arlBundledServiceDOs;
}
