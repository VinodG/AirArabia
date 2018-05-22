package com.winit.airarabia.objects;

import java.util.Vector;

public class ModifiedPNRResDO extends BaseDO
{
	public RequestParameterDO requestParameterDO;
	public AAModONDBalancesDO aaModONDBalancesDO;
	public Vector<TravelerCnxModAddResBalancesDO> vecTravelerCnxModAddResBalancesDO;
	public String TotalModChargeForCurrentOperation = "";
	public String TotalCnxChargeForCurrentOperation = "";
	public String TotalAmountDue = "";
	public String TotalAmountDueCC = "";
	public String TotalPrice = "";
	public String TotalPriceCC = "";
	
	public String errorMessage= "";
}
