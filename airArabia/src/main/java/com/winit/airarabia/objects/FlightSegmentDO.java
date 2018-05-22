package com.winit.airarabia.objects;

public class FlightSegmentDO extends BaseDO
{
	public String arrivalDateTime = "";
	public String departureDateTime = "";
	public String flightNumber = "";
	public String journeyDuration = "";
	public String RPH = "";
	public String departureAirportCode = "";
	public String departureAirportTerminal = "";
	public String arrivalAirportCode = "";
	public String arrivalAirportTerminal = "";
	public String operatingAirlineCode = "";
	public String currencyCode = "";
	public String segmentCode = "";
	public String resCabinClass = "";
	public String status = "35";
	public String departureAirportCodeContext = "";
	public String arrivalAirportCodeContext = "";
	public String comment = "";
	
	public String operationTimeFri = "";
	public String operationTimeSat = "";
	public String operationTimeSun = "";
	public String operationTimeMon = "";
	public String operationTimeTue = "";
	public String operationTimeWeds = "";
	public String operationTimeThur = "";
	public String ScheduleValidStartDate = "";
	public String ScheduleValidEndDate = "";
	
	public long departureDateTimeInMillies = 0;
	public long arrivalDateTimeInMillies = 0;
}
