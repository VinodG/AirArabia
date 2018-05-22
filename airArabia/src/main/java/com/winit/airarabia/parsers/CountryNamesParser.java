package com.winit.airarabia.parsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.winit.airarabia.objects.CountryDO;

public class CountryNamesParser extends BaseHandler{

	private CountryDO objcountryDO;
	private Vector<CountryDO> vecCountryDO;
	
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		if(localName.equalsIgnoreCase("Countries"))	//	GetCountryOfResidencesResult
			vecCountryDO = new Vector<CountryDO>();
		else if(localName.equalsIgnoreCase("Country"))
		{
			objcountryDO = new CountryDO();
			objcountryDO.CountryId = getString(attributes.getValue("CountryId"));
			objcountryDO.CountryCode = getString(attributes.getValue("CountryCode"));
			objcountryDO.CountryName = getString(attributes.getValue("CountryName"));
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if (localName.equalsIgnoreCase("Country")) 
			vecCountryDO.add(objcountryDO);
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
	}
	
	@Override
	public Object getData() {
		
		if(vecCountryDO != null)
			return vecCountryDO;
		else
			return "";
	}

	@Override
	public Object getErrorData() {
		return null;
	}
}