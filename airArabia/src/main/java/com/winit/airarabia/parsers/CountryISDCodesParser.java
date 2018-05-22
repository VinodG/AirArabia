package com.winit.airarabia.parsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.winit.airarabia.objects.CountryISDDO;

public class CountryISDCodesParser extends BaseHandler{

	private CountryISDDO objcountryISDDO;
	private Vector<CountryISDDO> vecCountryISDDO;
	
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		if(localName.equalsIgnoreCase("ISDCodes"))	//	GetCountryISDCodesResult
			vecCountryISDDO = new Vector<CountryISDDO>();
		else if(localName.equalsIgnoreCase("CountryISDCodes"))
		{
			objcountryISDDO = new CountryISDDO();
			objcountryISDDO.CountryCode = getString(attributes.getValue("CountryCode"));
			objcountryISDDO.ISDCode = getString(attributes.getValue("ISDCode"));
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		
		if (localName.equalsIgnoreCase("CountryISDCodes")) 
			vecCountryISDDO.add(objcountryISDDO);
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
	}
	
	@Override
	public Object getData() {
		
		if(vecCountryISDDO != null)
			return vecCountryISDDO;
		else
			return "";
	}

	@Override
	public Object getErrorData() {
		return null;
	}
}