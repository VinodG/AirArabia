package com.winit.airarabia.objects;

import java.io.Serializable;
import java.util.Vector;

public class CountriesDO implements Serializable {

	public String value = "";
	public String name = "";
	
	public Vector<CityDO> vecCityDO = new Vector<CityDO>();
}
