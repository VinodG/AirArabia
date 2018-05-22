package com.winit.airarabia.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.winit.airarabia.objects.InsuranceQuoteDO;

public class InsuranceQuoteParser extends BaseHandler{

	private StringBuffer stringBuffer = null;
	private boolean isActive = false;
	private InsuranceQuoteDO insuranceQuoteDO;
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		isActive = true;
		if(localName.equalsIgnoreCase("OTA_InsuranceQuoteRS"))
		{
			insuranceQuoteDO = new InsuranceQuoteDO();
			insuranceQuoteDO.echoToken = getString(attributes.getValue("EchoToken"));
			insuranceQuoteDO.primaryLangID = getString(attributes.getValue("PrimaryLangID"));
			insuranceQuoteDO.sequenceNmbr = getString(attributes.getValue("SequenceNmbr"));
			insuranceQuoteDO.transactionIdentifier = getString(attributes.getValue("TransactionIdentifier"));
			insuranceQuoteDO.version = getString(attributes.getValue("Version"));
		}
		else if(localName.equalsIgnoreCase("PlanForQuoteRS"))
		{
			insuranceQuoteDO.name = getString(attributes.getValue("Name"));
			insuranceQuoteDO.planID = getString(attributes.getValue("PlanID"));
			insuranceQuoteDO.rPH = getString(attributes.getValue("RPH"));
			insuranceQuoteDO.type = getString(attributes.getValue("Type"));
		}
		else if(localName.equalsIgnoreCase("ProviderCompany"))
			insuranceQuoteDO.companyShortName = getString(attributes.getValue("CompanyShortName"));
		else if(localName.equalsIgnoreCase("PlanCost"))
		{
			insuranceQuoteDO.amount = getString(attributes.getValue("Amount"));
			insuranceQuoteDO.currencyCode = getString(attributes.getValue("CurrencyCode"));
		}
		stringBuffer = new StringBuffer();
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException 
	{
		isActive = false;
		if(localName.equalsIgnoreCase("QuoteDetailURL"))
			insuranceQuoteDO.quoteDetailURL = stringBuffer.toString();
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
		if(insuranceQuoteDO != null)
			return insuranceQuoteDO;
		return null;
	}
	@Override
	public Object getErrorData() {
			return null;
	}
}