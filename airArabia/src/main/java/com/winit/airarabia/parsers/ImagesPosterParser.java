package com.winit.airarabia.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.winit.airarabia.objects.PosterImagesDO;

/**
 * Created by Rahul.M on 07-01-2016.
 */
public class ImagesPosterParser extends BaseHandler {

	boolean isActive = false;
    private PosterImagesDO posterImagesDO;
    private String errorMsg = "";
    private StringBuffer stringBuffer = null;

    @Override
    public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        isActive = true;
        if(localName.equalsIgnoreCase("GetBannersResult"))	//	ns1:AA_API_LoginRS
            posterImagesDO = new PosterImagesDO();
        stringBuffer = new StringBuffer();
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
        isActive = false;
        if (localName.equalsIgnoreCase("BannerImageEn")){
            if(getString(stringBuffer.toString())+"" != null)
                posterImagesDO.arrayListEN.add((getString(stringBuffer.toString())+"").replace("m.airarabia.com","52.33.71.7"));
        }
        else if (localName.equalsIgnoreCase("BannerImageAr")){
            if(getString(stringBuffer.toString())+"" != null)
                posterImagesDO.arrayListAR.add((getString(stringBuffer.toString())+"").replace("m.airarabia.com","52.33.71.7"));
        }
        else if (localName.equalsIgnoreCase("BannerImageFr")) {
            if(getString(stringBuffer.toString())+"" != null)
                posterImagesDO.arrayListFR.add((getString(stringBuffer.toString())+"").replace("m.airarabia.com","52.33.71.7"));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
    	if (isActive) {
    		try {
				stringBuffer.append(ch,start,length);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

    @Override
    public Object getData() {

        if(posterImagesDO != null)
            return posterImagesDO;
        else
            return errorMsg;
    }

    @Override
    public Object getErrorData() {
        return null;
    }
}
