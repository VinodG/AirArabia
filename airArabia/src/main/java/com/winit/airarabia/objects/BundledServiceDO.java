package com.winit.airarabia.objects;

import java.util.ArrayList;

public class BundledServiceDO extends BaseDO
{
	public String bunldedServiceId = "";
	public String bundledServiceName  = "";
	public String perPaxBundledFee = "";
	public String bookingClasses  = "";
	public String description = "";
	public String includedServies = "";
	
	public float fare = 0f;

	public ArrayList<String> arlIncludedServies;
}
