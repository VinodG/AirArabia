package com.winit.airarabia.objects;

import java.util.Vector;

public class AirAvailabilityDO extends BaseDO
{
	public String echoToken = "";
	public String primaryLangID = "";
	public String sequenceNmbr = "";
	public String transactionIdentifier = "";
	public String version = "";
	public Vector<OriginDestinationInformationDO> vecOriginDestinationInformationDOs;
	public Vector<PricedItineraryDO> vecPricedItineraryDOs, 
								vecPricedItineraryDOsPromoFare, 
								vecPricedItineraryDOsFlexiFare;
}
