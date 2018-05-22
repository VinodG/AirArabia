package com.winit.airarabia.parsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AAModONDBalancesDO;
import com.winit.airarabia.objects.ModifiedPNRResDO;
import com.winit.airarabia.objects.RequestParameterDO;
import com.winit.airarabia.objects.TravelerCnxModAddResBalancesDO;

public class ModifiedResQueryParser extends BaseHandler{

	private StringBuffer stringBuffer = null;
	private boolean isActive = false,
					isUpdatingONDCharges = false,
					isNewONDCharges = false,
					isFare = false,
					isTaxAndSurcharge = false,
					isAdjustments = false,
					isOtherCharges = false,
					isTotalCharges = false,
					isNonRefundableAmount = false,
					isRefundableAmount = false,
					isCurrentTotalCharges = false,
					isNewTotalCharges = false,
					isCurrentTotalPayments = false,
					isBalance = false,
					isTotalModChargeForCurrentOperation = false,
					isTotalCnxChargeForCurrentOperation = false,
					isTotalAmountDue = false,
					isTotalAmountDueCC = false,
					isTotalPrice = false,
					isTotalPriceCC = false;
	private ModifiedPNRResDO modifiedPNRResDO;
	private TravelerCnxModAddResBalancesDO travelerCnxModAddResBalancesDO;
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		isActive = true;
		if(localName.equalsIgnoreCase("OTA_AirBookRS"))
		{
			modifiedPNRResDO = new ModifiedPNRResDO();
			modifiedPNRResDO.requestParameterDO = new RequestParameterDO();
			modifiedPNRResDO.requestParameterDO.echoToken = getString(attributes.getValue("EchoToken"));
			modifiedPNRResDO.requestParameterDO.primaryLangID = getString(attributes.getValue("PrimaryLangID"));
			modifiedPNRResDO.requestParameterDO.sequenceNmbr = getString(attributes.getValue("SequenceNmbr"));
			modifiedPNRResDO.requestParameterDO.transactionIdentifier = getString(attributes.getValue("TransactionIdentifier"));
			modifiedPNRResDO.requestParameterDO.version = getString(attributes.getValue("Version"));
		}
		else if(localName.equalsIgnoreCase("AlterationBalances"))
			modifiedPNRResDO.aaModONDBalancesDO = new AAModONDBalancesDO();
		else if(localName.equalsIgnoreCase("UpdatingONDCharges"))
			isUpdatingONDCharges = true;
		else if(localName.equalsIgnoreCase("NewONDCharges"))
			isNewONDCharges = true;
		else if(localName.equalsIgnoreCase("Fare"))
			isFare = true;
		else if(localName.equalsIgnoreCase("TaxAndSurcharge"))
			isTaxAndSurcharge = true;
		else if(localName.equalsIgnoreCase("Adjustments"))
			isAdjustments = true;
		else if(localName.equalsIgnoreCase("OtherCharges"))
			isOtherCharges = true;
		else if(localName.equalsIgnoreCase("TotalCharges"))
			isTotalCharges = true;
		else if(localName.equalsIgnoreCase("NonRefundableAmount"))
			isNonRefundableAmount = true;
		else if(localName.equalsIgnoreCase("RefundableAmount"))
			isRefundableAmount = true;
		else if(localName.equalsIgnoreCase("TravelersCnxModAddResBalances"))
			modifiedPNRResDO.vecTravelerCnxModAddResBalancesDO = new Vector<TravelerCnxModAddResBalancesDO>();
		else if(localName.equalsIgnoreCase("TravelerCnxModAddResBalances"))
			travelerCnxModAddResBalancesDO = new TravelerCnxModAddResBalancesDO();
		else if(localName.equalsIgnoreCase("CurrentTotalCharges"))
			isCurrentTotalCharges = true;
		else if(localName.equalsIgnoreCase("NewTotalCharges"))
			isNewTotalCharges = true;
		else if(localName.equalsIgnoreCase("CurrentTotalPayments"))
			isCurrentTotalPayments = true;
		else if(localName.equalsIgnoreCase("Balance"))
			isBalance = true;
		else if(localName.equalsIgnoreCase("TotalModChargeForCurrentOperation"))
			isTotalModChargeForCurrentOperation = true;
		else if(localName.equalsIgnoreCase("TotalCnxChargeForCurrentOperation"))
			isTotalCnxChargeForCurrentOperation = true;
		else if(localName.equalsIgnoreCase("TotalAmountDue"))
			isTotalAmountDue = true;
		else if(localName.equalsIgnoreCase("TotalAmountDueCC"))
			isTotalAmountDueCC = true;
		else if(localName.equalsIgnoreCase("TotalPrice"))
			isTotalPrice = true;
		else if(localName.equalsIgnoreCase("TotalPriceCC"))
			isTotalPriceCC = true;
		else if(localName.equalsIgnoreCase("Error"))
		{
			String strErrorCode = getString(attributes.getValue("Code"));
			if(strErrorCode.equalsIgnoreCase("40"))
				modifiedPNRResDO.errorMessage = AppConstants.SESSIONE_EXP_TEXT;
			if(strErrorCode.equalsIgnoreCase("59"))
				modifiedPNRResDO.errorMessage = AppConstants.BOOKING_UNCHANGED_SEGMENT_TEXT;
		}
		stringBuffer = new StringBuffer();
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException 
	{
		isActive = false;
		if(localName.equalsIgnoreCase("UpdatingONDCharges"))
			isUpdatingONDCharges = false;
		else if(localName.equalsIgnoreCase("NewONDCharges"))
			isNewONDCharges = false;
		else if(localName.equalsIgnoreCase("Fare"))
			isFare = false;
		else if(localName.equalsIgnoreCase("TaxAndSurcharge"))
			isTaxAndSurcharge = false;
		else if(localName.equalsIgnoreCase("Adjustments"))
			isAdjustments = false;
		else if(localName.equalsIgnoreCase("OtherCharges"))
			isOtherCharges = false;
		else if(localName.equalsIgnoreCase("TotalCharges"))
			isTotalCharges = false;
		else if(localName.equalsIgnoreCase("NonRefundableAmount"))
			isNonRefundableAmount = false;
		else if(localName.equalsIgnoreCase("RefundableAmount"))
			isRefundableAmount = false;
		else if(localName.equalsIgnoreCase("CurrentTotalCharges"))
			isCurrentTotalCharges = false;
		else if(localName.equalsIgnoreCase("NewTotalCharges"))
			isNewTotalCharges = false;
		else if(localName.equalsIgnoreCase("CurrentTotalPayments"))
			isCurrentTotalPayments = false;
		else if(localName.equalsIgnoreCase("Balance"))
			isBalance = false;
		else if(localName.equalsIgnoreCase("TotalModChargeForCurrentOperation"))
			isTotalModChargeForCurrentOperation = false;
		else if(localName.equalsIgnoreCase("TotalCnxChargeForCurrentOperation"))
			isTotalCnxChargeForCurrentOperation = false;
		else if(localName.equalsIgnoreCase("TotalAmountDue"))
			isTotalAmountDue = false;
		else if(localName.equalsIgnoreCase("TotalAmountDueCC"))
			isTotalAmountDueCC = false;
		else if(localName.equalsIgnoreCase("TotalPrice"))
			isTotalPrice = false;
		else if(localName.equalsIgnoreCase("TotalPriceCC"))
			isTotalPriceCC = false;
		else if(localName.equalsIgnoreCase("Amount"))
		{
			if(isUpdatingONDCharges && isFare)
				modifiedPNRResDO.aaModONDBalancesDO.UpdatingONDChargesFare = stringBuffer.toString();
			else if(isUpdatingONDCharges && isTaxAndSurcharge)
				modifiedPNRResDO.aaModONDBalancesDO.UpdatingONDChargesTaxAndSurcharge = stringBuffer.toString();
			else if(isUpdatingONDCharges && isAdjustments)
				modifiedPNRResDO.aaModONDBalancesDO.UpdatingONDChargesAdjustments = stringBuffer.toString();
			else if(isUpdatingONDCharges && isOtherCharges)
				modifiedPNRResDO.aaModONDBalancesDO.UpdatingONDChargesOtherCharges = stringBuffer.toString();
			else if(isUpdatingONDCharges && isTotalCharges)
				modifiedPNRResDO.aaModONDBalancesDO.UpdatingONDChargesTotalCharges = stringBuffer.toString();
			else if(isUpdatingONDCharges && isNonRefundableAmount)
				modifiedPNRResDO.aaModONDBalancesDO.UpdatingONDChargesNonRefundableAmount = stringBuffer.toString();
			else if(isUpdatingONDCharges && isRefundableAmount)
				modifiedPNRResDO.aaModONDBalancesDO.UpdatingONDChargesRefundableAmount = stringBuffer.toString();
			else if(isNewONDCharges && isFare)
				modifiedPNRResDO.aaModONDBalancesDO.NewONDChargesFare = stringBuffer.toString();
			else if(isNewONDCharges && isTaxAndSurcharge)
				modifiedPNRResDO.aaModONDBalancesDO.NewONDChargesTaxAndSurcharge = stringBuffer.toString();
			else if(isNewONDCharges && isAdjustments)
				modifiedPNRResDO.aaModONDBalancesDO.NewONDChargesAdjustments = stringBuffer.toString();
			else if(isNewONDCharges && isOtherCharges)
				modifiedPNRResDO.aaModONDBalancesDO.NewONDChargesOtherCharges = stringBuffer.toString();
			else if(isNewONDCharges && isTotalCharges)
				modifiedPNRResDO.aaModONDBalancesDO.NewONDChargesTotalCharges = stringBuffer.toString();
			else if(isNewONDCharges && isNonRefundableAmount)
				modifiedPNRResDO.aaModONDBalancesDO.NewONDChargesNonRefundableAmount = stringBuffer.toString();
			else if(isNewONDCharges && isRefundableAmount)
				modifiedPNRResDO.aaModONDBalancesDO.NewONDChargesRefundableAmount = stringBuffer.toString();
			else if(isCurrentTotalCharges)
				travelerCnxModAddResBalancesDO.CurrentTotalCharges = stringBuffer.toString();
			else if(isNewTotalCharges)
				travelerCnxModAddResBalancesDO.NewTotalCharges = stringBuffer.toString();
			else if(isCurrentTotalPayments)
				travelerCnxModAddResBalancesDO.CurrentTotalPayments = stringBuffer.toString();
			else if(isBalance)
				travelerCnxModAddResBalancesDO.Balance = stringBuffer.toString();
			else if(isTotalModChargeForCurrentOperation)
				modifiedPNRResDO.TotalModChargeForCurrentOperation = stringBuffer.toString();
			else if(isTotalCnxChargeForCurrentOperation)
				modifiedPNRResDO.TotalCnxChargeForCurrentOperation = stringBuffer.toString();
			else if(isTotalAmountDue)
				modifiedPNRResDO.TotalAmountDue = stringBuffer.toString();
			else if(isTotalAmountDueCC)
				modifiedPNRResDO.TotalAmountDueCC = stringBuffer.toString();
			else if(isTotalPrice)
				modifiedPNRResDO.TotalPrice = stringBuffer.toString();
			else if(isTotalPriceCC)
				modifiedPNRResDO.TotalPriceCC = stringBuffer.toString();
		}
		else if(localName.equalsIgnoreCase("TravelerRefNumberRPH"))
			travelerCnxModAddResBalancesDO.TravelerRefNumberRPH = stringBuffer.toString();
		else if(localName.equalsIgnoreCase("TravelerCnxModAddResBalances"))
			modifiedPNRResDO.vecTravelerCnxModAddResBalancesDO.add(travelerCnxModAddResBalancesDO);
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
		if(modifiedPNRResDO != null)
			return modifiedPNRResDO;
		else
			return null;
	}
	@Override
	public Object getErrorData() {
			return null;
	}
}