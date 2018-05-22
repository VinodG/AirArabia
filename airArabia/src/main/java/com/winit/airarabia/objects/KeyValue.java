package com.winit.airarabia.objects;

public class KeyValue extends BaseDO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String key = "";
	public String value = "";
	public int dataType = -1;
	public boolean valueBool = false;

	public KeyValue() {
		// default
	}

	public KeyValue(String k, String v) {
		key = k;
		value = v;
	}
	public KeyValue(String k, boolean v) {
		key = k;
		valueBool = v;
	}
}
