package com.winit.airarabia.parsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.winit.airarabia.objects.CityDO;
import com.winit.airarabia.objects.CountriesDO;

public class CityParser extends BaseHandler {

	private Vector<CountriesDO> arrListCountriesDo;
	private CountriesDO objcountriesDO;
	private CityDO objcityDO;
	
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		if(localName.equalsIgnoreCase("countries"))	// GetCitiesResult
			arrListCountriesDo = new Vector<CountriesDO>();
		else if (localName.equalsIgnoreCase("MoreCountry")) 
		{
			objcountriesDO = new CountriesDO();
			
			objcountriesDO.value = getString(attributes.getValue("value"));
			objcountriesDO.name = getString(attributes.getValue("name"));
		}
		else if (localName.equalsIgnoreCase("city")) 
		{
			objcityDO = new CityDO();
			
			objcityDO.callcenter = getString(attributes.getValue("callcenter"));
			objcityDO.name = getString(attributes.getValue("name"));
			objcityDO.other = getString(attributes.getValue("other"));
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)	throws SAXException {
		super.endElement(uri, localName, qName);
		
		if (localName.equalsIgnoreCase("city")) 
			objcountriesDO.vecCityDO.add(objcityDO);
		else if (localName.equalsIgnoreCase("MoreCountry")) 
			arrListCountriesDo.add(objcountriesDO);
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
	}
	
	@Override
	public Object getData() {
		
		if(arrListCountriesDo != null)
			return arrListCountriesDo;
		else
			return "";
	}

	@Override
	public Object getErrorData() {
		return null;
	}
}