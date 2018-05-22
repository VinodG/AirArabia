package com.winit.airarabia.objects;

import java.util.Vector;

public class AirBookDO extends BaseDO{

	public RequestParameterDO requestParameterDO = new RequestParameterDO();
	public Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs;
	public Vector<PTC_FareBreakdownDO> vecPTC_FareBreakdownDOs;
	public Vector<PassengerInfoPersonDO> vecAirTravelers = new Vector<PassengerInfoPersonDO>();
	public Vector<RequestDO> vecSeatRequestDOs;
	public Vector<RequestDO> vecMealRequestDOs;
	public Vector<RequestDO> vecBaggageRequestDOs;
	public Vector<RequestDO> vecHalaRequestDOs;
	public Vector<RequestDO> vecInsuranceRequestDOs;
	public Vector<PaymentDO> vecPaymentDO;
	public String ticketAdvisory = "";
	public String ticketType = "";
	public String ticketingStatus = "";
	public String bookingID = "";
	public String bookingType = "";
	public PassengerInfoContactDO contactInfo;
	public String originAgentCode = "";
	public String originSalesTerminal = "";
	public int infCount = 0;
	public int chdCount = 0;
	public int adultCount = 0;
	public ItinTotalFareDO itinTotalFareDO = new ItinTotalFareDO();
	public FlexiOperationsDO flexiOperationsDOCancel = new FlexiOperationsDO(), flexiOperationsDOModify = new FlexiOperationsDO();
	public String errorMessage = "";
}
