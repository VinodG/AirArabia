package com.winit.airarabia.objects;

import java.util.Vector;


public class AirportsDO extends BaseDO{
	
	public String code = "";
	public String en = "";
	public String ar = "";
	public String fr = "";
	public String service_type1 = "";
	public String name = "";
	public String countryname = "";
	public String countryid = "";
	public String language = "";
	public String currency = "";
	public Vector<AirportsDestDO> destList =new Vector<AirportsDestDO>();
}
