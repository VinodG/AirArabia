package com.winit.airarabia.objects;

import java.util.Calendar;
import java.util.Vector;


public class PassengerInfoPersonDO extends BaseDO
{
	public String persontype = "";
	public String persontitle = "";
	public String personnationality = "";
	public String personfirstname = "";
	public String personlastname = "";
	public String contactRPH = "";
	public String personPhonenum = "";
	public String personDOB = "";
	public String travelerRefNumberRPHList = "";
	public Vector<ETicketInfomationDO> vecETicketInfomationDOs = new Vector<ETicketInfomationDO>();
	
	public String MembershipID = "";
	public String personCountryCode = ""; //as reference DocHolderNationality
	public Calendar passengerDOBCal = Calendar.getInstance();; // for update DOB
}
