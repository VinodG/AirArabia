package com.winit.airarabia.parsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.winit.airarabia.objects.OfficeDO;
import com.winit.airarabia.objects.OfficeLocationDO;

public class OfficeLocationParser extends BaseHandler{

	private OfficeLocationDO objOfficeLocationDO;
	private OfficeDO objOfficeDO;
	private Vector<OfficeLocationDO> vecOfficeLocation;
	
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		if(localName.equalsIgnoreCase("OfficeLocations"))	//	GetOfficeLocationsResult
			vecOfficeLocation = new Vector<OfficeLocationDO>();
		else if(localName.equalsIgnoreCase("OfficeLocation"))
		{
			objOfficeLocationDO = new OfficeLocationDO();
			objOfficeLocationDO.vecOfficeDO = new Vector<OfficeDO>();
			objOfficeLocationDO.value = getString(attributes.getValue("value"));
			objOfficeLocationDO.country = getString(attributes.getValue("country"));
			if(objOfficeLocationDO.country.trim().length() <= 0)
				objOfficeLocationDO.country = getString(attributes.getValue("name"));
		}
		else if (localName.equalsIgnoreCase("Office")) 
		{
			objOfficeDO			 = new OfficeDO();
			objOfficeDO.name	 = getString(attributes.getValue("name"));
			objOfficeDO.city	 = getString(attributes.getValue("city"));
			objOfficeDO.phone	 = getString(attributes.getValue("phone"));
			objOfficeDO.location = getString(attributes.getValue("location"));
			objOfficeDO.latitude = getString(attributes.getValue("latitude"));
			objOfficeDO.longitude = getString(attributes.getValue("longitude"));
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)	throws SAXException {
		super.endElement(uri, localName, qName);
		
		if (localName.equalsIgnoreCase("Office")) 
			objOfficeLocationDO.vecOfficeDO.add(objOfficeDO);
		else if (localName.equalsIgnoreCase("OfficeLocation")) 
			vecOfficeLocation.add(objOfficeLocationDO);
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
	}
	
	@Override
	public Object getData() {
		
		if(vecOfficeLocation != null)
			return vecOfficeLocation;
		else
			return "";
	}

	@Override
	public Object getErrorData() {
		return null;
	}
}