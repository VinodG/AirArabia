package com.winit.airarabia.objects;

import java.io.Serializable;

public class SavedPassengerDO extends BaseDO implements Serializable, Cloneable{

	public String title = "";
	public String firstName = "";
	public String lastName = "";
	public String nationality = "";
	public String dob = "";
	public String passportNo = "";
	public String airwardId = "";
	public boolean isSelected = false;
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	public void setSelected(boolean checked) {
		// TODO Auto-generated method stub
		this.isSelected = checked;
	}
}
