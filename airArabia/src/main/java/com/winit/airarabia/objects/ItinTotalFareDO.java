package com.winit.airarabia.objects;

import java.util.Vector;

public class ItinTotalFareDO extends BaseDO
{
	public FareDO baseFare = new FareDO();
	public FareDO totalFare = new FareDO();
	public FareDO totalEquivFare = new FareDO();
	public Vector<FareDO> vecTaxes = new Vector<FareDO>();
	public Vector<FareDO> vecFees = new Vector<FareDO>();
}
