package com.winit.airarabia.objects;

import java.io.Serializable;
import java.util.Vector;

public class OfficeLocationDO implements Serializable{

	public String value = "";
	public String country = "";
	
	public Vector<OfficeDO> vecOfficeDO = new Vector<OfficeDO>();
}
