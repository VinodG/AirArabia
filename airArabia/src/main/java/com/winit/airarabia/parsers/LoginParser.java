package com.winit.airarabia.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.winit.airarabia.objects.LoginContactInformationDO;
import com.winit.airarabia.objects.LoginDO;

public class LoginParser extends BaseHandler{

	private LoginDO loginDO;
	private String errorMsg = "";
	private StringBuffer stringBuffer = null;
	private boolean isActive = false;
	
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		if(localName.equalsIgnoreCase("customer"))	//	ns1:AA_API_LoginRS 
			loginDO = new LoginDO();
		else if(localName.equalsIgnoreCase("contactInformation"))
			loginDO.loginContactInformationDO = new LoginContactInformationDO();
		
		isActive = true;
		stringBuffer = new StringBuffer();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		
		isActive = false;
		
		if (localName.equalsIgnoreCase("title")) 
			loginDO.title = getString(stringBuffer.toString());
//			loginDO.title = getString(localName.toString());
		else if (localName.equalsIgnoreCase("firstName")) 
			loginDO.firstName = getString(stringBuffer.toString());
//			loginDO.firstName = getString(localName.toString());
		else if (localName.equalsIgnoreCase("lastName"))
			loginDO.lastName = getString(stringBuffer.toString());
//		loginDO.lastName = getString(localName.toString());
		else if (localName.equalsIgnoreCase("countryOfResidence")) 
			loginDO.countryOfResidence = getString(stringBuffer.toString());
//		loginDO.countryOfResidence = getString(localName.toString());
		else if (localName.equalsIgnoreCase("nationality")) 
			loginDO.nationality = getString(stringBuffer.toString());
//		loginDO.nationality = getString(localName.toString());
		
		else if (localName.equalsIgnoreCase("mobileNumber"))
			loginDO.loginContactInformationDO.mobileNumber = getString(stringBuffer.toString());
//		loginDO.loginContactInformationDO.mobileNumber = getString(localName.toString());
		else if (localName.equalsIgnoreCase("emailAddress")) 
			loginDO.loginContactInformationDO.emailAddress = getString(stringBuffer.toString());
//		loginDO.loginContactInformationDO.emailAddress = getString(localName.toString());
		
		
		if(localName.equalsIgnoreCase("description"))
			errorMsg = getString(stringBuffer.toString());
//		<ns1:status>FAIL</ns1:status><ns1:error><ns1:error><ns1:errorCode>1001</ns1:errorCode><ns1:description>User Name / Password does not match.</ns1:description></ns1:error></ns1:error></ns1:AA_API_LoginRS></soap:Body></soap:Envelope>
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
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
	public Object getData() {
		
		if(loginDO != null)
			return loginDO;
		else
			return errorMsg;
	}

	@Override
	public Object getErrorData() {
		return null;
	}
}