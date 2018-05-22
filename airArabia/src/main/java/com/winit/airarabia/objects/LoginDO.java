package com.winit.airarabia.objects;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Vector;

public class LoginDO extends BaseDO implements Serializable{

	public String title = "";
	public String firstName = "";
	public String lastName = "";
	public String countryOfResidence = "";
	public String countryCode = "";
	public String nationality = "";
	public String dob = "";
	public String passportNo = "";
	public String airwardId = "";
	public LoginContactInformationDO loginContactInformationDO = new LoginContactInformationDO();
	public Vector<FavouriteDestinationDO> vectFavDestinationDo = new Vector<FavouriteDestinationDO>();

	// public ArrayList<LoginContactInformationDO> arlLoginContactInformationDO
	// = new ArrayList<LoginContactInformationDO>();
}
