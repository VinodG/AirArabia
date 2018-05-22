package com.winit.airarabia.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CurrencyExchangeParser extends BaseHandler{

	private String currencyExchange;
	private StringBuilder builder;
    
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		builder=new StringBuilder();
//		if(localName.equalsIgnoreCase("GetExchangeRateResult"))
//			currencyExchange = getString(attributes.getValue("GetExchangeRateResult"));
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if(localName.equalsIgnoreCase("GetExchangeRateResult"))
			currencyExchange = getString(builder.toString());
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
		String tempString=new String(ch, start, length);
        builder.append(tempString);
	}
	
	@Override
	public Object getData() {
		
		if(!currencyExchange.isEmpty())
			return currencyExchange;
		else
			return "";
	}

	@Override
	public Object getErrorData() {
		return null;
	}
}