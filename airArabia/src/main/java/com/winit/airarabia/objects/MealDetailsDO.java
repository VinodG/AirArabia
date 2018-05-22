package com.winit.airarabia.objects;

import java.util.Vector;

public class MealDetailsDO extends BaseDO
{
	public FlightSegmentDO flightSegmentDO;
	public Vector<Float> vecTotalCost = new Vector<Float>();
	public Vector<MealDO> vecMealsDO = new Vector<MealDO>();
	public Vector<String> vecMealcategoryNames = new Vector<String>();
	public Vector<String> vecMealcategoryImageUrls = new Vector<String>();
	public Vector<MealCategoriesDO> vecMealCategoriesDO = new Vector<MealCategoriesDO>();
	public Vector<Vector<RequestDO>> vecMealRequestDOs = new Vector<Vector<RequestDO>>();
}
