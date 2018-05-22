package com.winit.airarabia.objects;

import java.util.Vector;

public class AvailableFlexiFaresDO extends BaseDO
{
	public String applicableJourneyType = "";
	public FareDO flexiFareAmountDO = new FareDO();
	public String flexibilityDescription  = "";
	public Vector<FlexiOperationsDO> vecFlexiOperationsDO = new Vector<FlexiOperationsDO>();
	public Vector<FareDO> vecPerPaxFlexifareBDS = new Vector<FareDO>();
	public String flexiRuleCode = "";
}
