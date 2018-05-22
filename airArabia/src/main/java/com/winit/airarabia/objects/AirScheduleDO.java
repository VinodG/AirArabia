package com.winit.airarabia.objects;

import java.io.Serializable;
import java.util.Vector;

public class AirScheduleDO implements Serializable
{
	public String echoToken = "";
	public String primaryLangID = "";
	public String sequenceNmbr = "";
	public String version = "";
	public Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs;
}
