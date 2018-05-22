package com.winit.airarabia.objects;

import java.util.Vector;

public class PTC_FareBreakdownDO extends BaseDO
{
	public String pricingSource  = "";
	public String code  = "";
	public String quantity  = "";
	public FareDO baseFare = new FareDO();
	public FareDO totalFare = new FareDO();
	public Vector<String> vecFareBasicCodes = new Vector<String>();
	public Vector<String> vecTravelerRefNumbers = new Vector<String>();
	public Vector<FareDO> vecTaxes = new Vector<FareDO>();
	public Vector<FareDO> vecFees = new Vector<FareDO>();
}
