package com.winit.airarabia.objects;

import java.util.ArrayList;

public class AirPriceQuoteDO extends BaseDO
{
	public String echoToken = "";
	public String primaryLangID = "";
	public String sequenceNmbr = "";
	public String transactionIdentifier = "";
	public String version = "";
	public ArrayList<PricedItineraryDO> vecPricedItineraryDOs = new ArrayList<PricedItineraryDO>();
	public String ErrorMsg = "";
}
