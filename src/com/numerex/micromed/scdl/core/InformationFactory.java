package com.numerex.micromed.scdl.core;

import com.numerex.micromed.scdl.constants.PropertyConstants;
import com.numerex.micromed.scdl.helpers.PropertyReader;
import com.numerex.micromed.scdl.helpers.properties.SinglePropertyValue;

/**
 * @author dmoretti Class to load static information of the form
 * 
 */

public class InformationFactory {

	private static InformationFactory instance = null;
	private static SerialPortController serialPortController = SerialPortController
			.getInstance();

	public static InformationFactory getInstance() {
		if (instance == null) {
			instance = new InformationFactory();
		}
		return instance;
	}

	private InformationFactory() {
		super();
	}

	public DeviceResponse getDeviceInformation(String command) throws MicroMedException {
		SinglePropertyValue commandProperty = PropertyReader.getInstance().getSingleProperty(command); 
		
		DeviceResponse deviceResponse = serialPortController.writeData(commandProperty.getValue());

		if (deviceResponse == null) {
			deviceResponse = new DeviceResponse(command.replace(PropertyConstants.COMMAND_READ_PREFIX, ""), null);
		}
		
		if (deviceResponse.getValue() == null) {

			String defaultValueKey = command.replace(PropertyConstants.COMMAND_READ_PREFIX, "DEFAULT");
			
			SinglePropertyValue defaultProperty = PropertyReader
					.getInstance().getSingleProperty(defaultValueKey);
			
			deviceResponse.setValue(defaultProperty.getValue());
		}
		return deviceResponse;
	}

}
