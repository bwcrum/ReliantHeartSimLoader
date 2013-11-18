package com.numerex.micromed.scdl.core;

import com.numerex.micromed.scdl.constants.PropertyConstants;
import com.numerex.micromed.scdl.helpers.PropertyReader;

public class DeviceResponse {
	
	private String name;
	private String value;
	private String message;
	
	public DeviceResponse(String value, String message) {
		super();
		this.name = value;
		this.value = value;
		this.message = message;
	}
	
	public DeviceResponse(String name, String value, String message) {
		super();
		this.name = name;
		this.value = value;
		this.message = message;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isDeviceResponse() {
		String atiReponseKey = PropertyReader.getInstance()
				.getSingleProperty(PropertyConstants.DEVICE_ATI_RESPONSE_KEY)
				.getValue();
		 return this.message.contains(atiReponseKey);
	}
	
	@Override
	public String toString() {
		return ((this.name == null) ? "" : this.name + " - ") +
				((this.value == null) ? "" : this.value + " - ") + 
				((this.message == null) ? "" : this.message);
	}

}
