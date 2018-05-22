package com.winit.airarabia.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class PnrParser extends BaseHandler{

	private StringBuffer stringBuffer = null;
	private boolean isActive = false;
	private String result = "";
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		isActive = true;
		stringBuffer = new StringBuffer();
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException 
	{
		isActive = false;
		if(localName.equalsIgnoreCase("LogPNRResult"))
			result = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("ModifyPNRResult"))
			result = stringBuffer.toString();
	}
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException 
	{
		if(isActive)
		{
			try {
				stringBuffer.append(ch,start,length);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public Object getData() 
	{
		if(result != null)
			return result;
		else
			return null;
	}
	@Override
	public Object getErrorData() {
			return null;
	}
}