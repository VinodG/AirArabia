package com.winit.airarabia.objects;

import java.io.Serializable;
import java.util.Vector;

public class OtherPassengerDo extends BaseDO implements Serializable{
	public Vector<SavedPassengerDO> vecSavedPassengerDoAdult = new Vector<SavedPassengerDO>();
	public Vector<SavedPassengerDO> vecSavedPassengerDoChild = new Vector<SavedPassengerDO>();
	public Vector<SavedPassengerDO> vecSavedPassengerDoInfant = new Vector<SavedPassengerDO>();
}