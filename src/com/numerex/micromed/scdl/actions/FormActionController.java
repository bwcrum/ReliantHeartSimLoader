package com.numerex.micromed.scdl.actions;

import gnu.io.PortInUseException;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.numerex.micromed.scdl.constants.Constants;
import com.numerex.micromed.scdl.constants.PropertyConstants;
import com.numerex.micromed.scdl.core.ConfigFields;
import com.numerex.micromed.scdl.core.DeviceResponse;
import com.numerex.micromed.scdl.core.InformationFactory;
import com.numerex.micromed.scdl.core.MicroMedException;
import com.numerex.micromed.scdl.core.SerialPortController;
import com.numerex.micromed.scdl.core.UserMessage;
import com.numerex.micromed.scdl.helpers.FileHelper;
import com.numerex.micromed.scdl.helpers.FormHelper;
import com.numerex.micromed.scdl.helpers.PropertyReader;
import com.numerex.micromed.scdl.helpers.PropertyValue;
import com.numerex.micromed.scdl.helpers.ValidateHelper;
import com.numerex.micromed.scdl.helpers.properties.CompoundPropertyValue;
import com.numerex.micromed.scdl.helpers.properties.PropertiesGroup;
import com.numerex.micromed.scdl.helpers.properties.SinglePropertyValue;
import com.numerex.micromed.scdl.log.MicroMedLogPoller;
import com.numerex.micromed.scdl.views.MicroMedFrame;

public class FormActionController {

	private static SerialPortController serialPortController = SerialPortController
			.getInstance();

	private static MicroMedFrame form = MicroMedFrame.getFrame();

	public static void connectAction(boolean fromLoad) throws MicroMedException {

		UserMessage message = null;
		try {
			if (!serialPortController.isIcConnected()) {
				MicroMedLogPoller.getInstance().clear();
				message = connect2COMPort(true);
				form.btnDefault.setEnabled(true);
			} else {
				message = disconnectCOMPort();
				if (fromLoad) {
					message = new UserMessage(
							"Values successfully loaded. Ready for the next controller",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

			form.btnConnect
					.setText(serialPortController.isIcConnected() ? "Disconnect"
							: "Connect");
			FormHelper.toggleControls(serialPortController.isIcConnected());

			MicroMedLogPoller.getInstance().addLog(message);
			FormHelper.showMessage2User(message);

		} catch (MicroMedException e) {
			throw e;
		} finally {
			FormHelper.hideSpinner();
			form.btnConnect.setEnabled(true);

		}

	}

	private static UserMessage connect2COMPort(boolean icLoadDeviceInformation)
			throws MicroMedException {

		UserMessage message = null;
		try {
			serialPortController.connect(form.getSelectedPortName());
			if (serialPortController.isIcConnected()) {

				serialPortController.initIOStream();
				serialPortController.initListener();
				DeviceResponse deviceResponse = serialPortController.writeData(PropertyConstants.COMMAND_ATI);

				if(deviceResponse.isDeviceResponse()){
					serialPortController.setIcConnected(true);

					// Load device information to the form
					if (icLoadDeviceInformation) {
						loadDeviceInformation();
					}

					message = new UserMessage("Communication port ready",
							JOptionPane.INFORMATION_MESSAGE);

				} else {
					disconnectCOMPort();
					message = new UserMessage("The selected port is not connected to any device",
							JOptionPane.ERROR_MESSAGE);
				
				}
			}

		} catch (PortInUseException e) {
			MicroMedException exception = new MicroMedException(
					"The COM port is in use. Please make sure no other programs have COM port open.",
					e);
			throw exception;

		} catch (MicroMedException e1) {
			e1.printStackTrace();
			throw e1;
		} catch (Exception e2) {
			MicroMedException exception = new MicroMedException(
					Constants.Front.GENERAL_EXCEPTION_MESSAGE, e2);
			throw exception;
		}

		return message;
	}

	private static UserMessage disconnectCOMPort() throws MicroMedException {

		UserMessage message = null;
		try {
			serialPortController.setIcConnected(false);
			serialPortController.disconnect();

			FormHelper.resetText();
			message = new UserMessage("Communication port disconnected",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			MicroMedException exception = new MicroMedException(
					"Failed to close. Please make sure device is connected and powered on.",
					e);
			throw exception;
		}

		return message;
	}

	private static void loadDeviceInformation() throws MicroMedException {

		try {

			InformationFactory informationFactory = InformationFactory
					.getInstance();

			String command = PropertyReader.getInstance()
					.getSingleProperty(PropertyConstants.COMMAND_AT_SCFG_0).getValue();
			serialPortController.writeData(command);

			int delay = new Integer(PropertyReader.getInstance()
					.getSingleProperty(PropertyConstants.J2ME_DELAY).getValue());
			Thread.sleep(delay);

			DeviceResponse deviceResponse = informationFactory
					.getDeviceInformation(PropertyConstants.COMMAND_READ_PUMPID);

			String txtPumpID = deviceResponse.getValue();
			if (txtPumpID == null
					|| txtPumpID.trim().equals("")) {

				txtPumpID = PropertyReader.getInstance()
						.getSingleProperty("DEFAULT_PUMPID").getValue();
			}

			deviceResponse = informationFactory
					.getDeviceInformation(PropertyConstants.COMMAND_READ_FLOWGAIN);

			String txtFlowGain = deviceResponse.getValue();

			deviceResponse = informationFactory
					.getDeviceInformation(PropertyConstants.COMMAND_READ_FLOWBALANCE);
			String txtFlowBalance = deviceResponse.getValue();

			deviceResponse = informationFactory
					.getDeviceInformation(PropertyConstants.COMMAND_READ_FLOWNORMA);
			String txtFlowNormA = deviceResponse.getValue();

			deviceResponse = informationFactory
					.getDeviceInformation(PropertyConstants.COMMAND_READ_FLOWNORMB);
			String txtFlowNormB = deviceResponse.getValue();

			CompoundPropertyValue apnConfiguration = readApnConfiguration(informationFactory);
			
			MicroMedLogPoller.getInstance().addLog("WARNING: Autostart flag set to 0, to restore it please load the data onto the sim card");
			
			form.txtPumpID.setText(txtPumpID);
			form.txtFlowGain.setText(txtFlowGain);
			form.txtFlowBalance.setText(txtFlowBalance);
			form.txtFlowNormA.setText(txtFlowNormA);
			form.txtFlowNormB.setText(txtFlowNormB);
			form.loadAPNs(apnConfiguration);

		} catch (Exception e) {
   	 		MicroMedException exception = new MicroMedException(
					Constants.Front.GENERAL_EXCEPTION_MESSAGE, e);
			throw exception;
		}

	}

	private static CompoundPropertyValue readApnConfiguration(InformationFactory informationFactory) throws MicroMedException {
		String apnValue = informationFactory
				.getDeviceInformation(PropertyConstants.COMMAND_READ_APN).getValue();
		
		String apnUserName = informationFactory
				.getDeviceInformation(PropertyConstants.COMMAND_READ_USERNAME).getValue();
		
		String apnPassword = informationFactory
				.getDeviceInformation(PropertyConstants.COMMAND_READ_PASS).getValue();
		
		
		CompoundPropertyValue apnConfiguration = new CompoundPropertyValue(PropertyConstants.STORED_APN);
		apnConfiguration.add(PropertyConstants.APN_NAME, PropertyConstants.NOT_FOUND_APN_NAME);
		apnConfiguration.add(PropertyConstants.APN_VALUE, apnValue);
		apnConfiguration.add(PropertyConstants.APN_USER_NAME, apnUserName);
		apnConfiguration.add(PropertyConstants.APN_PASSWORD, apnPassword);
						
		return apnConfiguration;
	}

	public static void loadFirstCommands() throws MicroMedException {

		try {

			String command = PropertyReader.getInstance()
					.getSingleProperty(PropertyConstants.COMMAND_AT_SCFG_0).getValue();
			serialPortController.writeData(command);

			int delay = new Integer(PropertyReader.getInstance()
					.getSingleProperty(PropertyConstants.J2ME_DELAY).getValue());
			Thread.sleep(delay);

			command = PropertyReader.getInstance()
					.getSingleProperty(PropertyConstants.COMMAND_PUMPID).getValue();
			String pumpID = serialPortController.encodeCommand(command,
					form.txtPumpID.getText(), true);

			command = PropertyReader.getInstance()
					.getSingleProperty(PropertyConstants.COMMAND_FLOWGAIN).getValue();
			String flowGain = serialPortController.encodeCommand(command,
					form.txtFlowGain.getText(), true);

			command = PropertyReader.getInstance()
					.getSingleProperty(PropertyConstants.COMMAND_FLOWBALANCE).getValue();
			String flowBalance = serialPortController.encodeCommand(command,
					form.txtFlowBalance.getText(), true);

			command = PropertyReader.getInstance()
					.getSingleProperty(PropertyConstants.COMMAND_FLOWNORMA).getValue();
			String flowNormA = serialPortController.encodeCommand(command,
					form.txtFlowNormA.getText(), true);

			command = PropertyReader.getInstance()
					.getSingleProperty(PropertyConstants.COMMAND_FLOWNORMB).getValue();
			String flowNormB = serialPortController.encodeCommand(command,
					form.txtFlowNormB.getText(), true);

			command = PropertyReader.getInstance()
					.getSingleProperty(PropertyConstants.COMMAND_APN).getValue();
			CompoundPropertyValue selectedAPN = (CompoundPropertyValue) form.cBoxAPNs
					.getSelectedItem();
			
			String apnValueCommand = serialPortController.encodeCommand(command,
					selectedAPN.get(PropertyConstants.APN_VALUE).getValue(), true);

			/*
			 * Username
			 */
			
			command = PropertyReader.getInstance()
					.getSingleProperty(PropertyConstants.COMMAND_APN_USERNAME).getValue();

			String apnUserNameCommand = serialPortController.encodeCommand(command,
					selectedAPN.get(PropertyConstants.APN_USER_NAME).getValue(), true);

			/*
			 * Password
			 */
			
			command = PropertyReader.getInstance()
					.getSingleProperty(PropertyConstants.COMMAND_APN_PASS).getValue();

			String apnPasswordCommand = serialPortController.encodeCommand(command,
					selectedAPN.get(PropertyConstants.APN_PASSWORD).getValue(), true);

			/*
			 * IP
			 */
			command = PropertyReader.getInstance()
					.getSingleProperty(PropertyConstants.COMMAND_IP).getValue();
			
			SinglePropertyValue ipValue = getIpFromProperties();
			String ip = serialPortController.encodeCommand(command, ipValue.getValue(), true);
			
			
			serialPortController.writeData(pumpID);
			serialPortController.writeData(flowGain);
			serialPortController.writeData(flowBalance);
			serialPortController.writeData(flowNormA);
			serialPortController.writeData(flowNormB);
		    serialPortController.writeData(ip);
			serialPortController.writeData(apnValueCommand);
			serialPortController.writeData(apnUserNameCommand);
			serialPortController.writeData(apnPasswordCommand);
			
			/* 
			 * Set autostart flag to 1
			 */
			String set_autostart_flag = PropertyReader.getInstance()
					.getSingleProperty(PropertyConstants.COMMAND_AT_SCFG_4).getValue();
			serialPortController.writeData(set_autostart_flag);

		} catch (Exception e) {
			MicroMedException exception = new MicroMedException(
					Constants.Front.GENERAL_EXCEPTION_MESSAGE, e);
			throw exception;
		}

	}

	public static void loadLastCommands() throws MicroMedException {
		String command = PropertyReader.getInstance()
				.getSingleProperty(PropertyConstants.COMMAND_AT_SCFG_1).getValue();
		serialPortController.writeData(command);

		command = PropertyReader.getInstance()
				.getSingleProperty(PropertyConstants.COMMAND_AT_SCFG_2).getValue();
		serialPortController.writeData(command);

		command = PropertyReader.getInstance()
				.getSingleProperty(PropertyConstants.COMMAND_AT_SCFG_3).getValue();
		serialPortController.writeData(command);

		command = PropertyReader.getInstance()
				.getSingleProperty(PropertyConstants.COMMAND_AT_SCFG_4).getValue();
		serialPortController.writeData(command);

		command = PropertyReader.getInstance()
				.getSingleProperty(PropertyConstants.COMMAND_AT_W).getValue();
		serialPortController.writeData(command);

	}

	public static void loadJarFiles() throws MicroMedException {
		serialPortController.setIcConnected(false);
		try {
			serialPortController.disconnect();
			loadFiles();
			connect2COMPort(false);
		} catch (MicroMedException e) {
			throw e;
		} catch (Exception e1) {
			throw new MicroMedException("Failed to write data on COM port", e1);
		}
	}

	public static void dataDownloadAction(ActionEvent arg0) {
		try {
			FormHelper.clearErrorStyle();
			
			validateDataFromProperties();
			
			if (validateFormInformation()) {

				loadFirstCommands();
				// loadJarFiles();
				// loadLastCommands();

				FormHelper.resetText();

				serialPortController.logText = "Values successfully loaded. Ready for the next controller";

				serialPortController.setIcConnected(true);
				connectAction(true);

			} else {
				FormHelper.toggleControls(true);
				FormHelper.setEnableButtons(true);

				UserMessage message = new UserMessage(
						"Please check invalid inputs",
						JOptionPane.ERROR_MESSAGE);
				FormHelper.showMessage2User(message);

			}
		} catch (MicroMedException e) {

			FormHelper.toggleControls(true);
			FormHelper.setEnableButtons(true);

			UserMessage userMessage = new UserMessage(e);
			FormHelper.showMessage2User(userMessage);

		} finally {
			form.btnConnect.setEnabled(true);
			FormHelper.hideSpinner();
		}

	}

	private static void validateDataFromProperties() throws MicroMedException {
		
		SinglePropertyValue defaultIp = getIpFromProperties();
		if (defaultIp == null) {
			throw new MicroMedException("There's no IPs configured. Check " + PropertyConstants.SERVER_OPTIONS + " configuration and restart the application.");
		}

		String defaultIpValue = defaultIp.getValue();
		
		if (!ValidateHelper.validateText(defaultIpValue)
			|| !ValidateHelper.validateIP(defaultIpValue)) {
			throw new MicroMedException("Invalid IP. Check " + PropertyConstants.SERVER_OPTIONS + " configuration and restart the application. ");
		}

	}
	
	private static SinglePropertyValue getIpFromProperties() {
		PropertiesGroup ipPropertiesGroup = PropertyReader.getInstance().getPropertiesGroup(PropertyConstants.SERVER_OPTIONS);
		
		List<PropertyValue> ipProperties = ipPropertiesGroup.getPropertyValues();
		
		if (ipProperties.isEmpty()) {
			return null;
		}
		
		Collections.sort(ipProperties);
		
		CompoundPropertyValue firstIp = (CompoundPropertyValue) ipProperties.get(0);
		return firstIp.get(PropertyConstants.IP_VALUE);
	}

	public static void loadFirmwareAction(ActionEvent arg0) {
		try {

			loadJarFiles();
			loadLastCommands();

			serialPortController.logText = "Firmware successfully loaded. Ready for the next controller";

			serialPortController.setIcConnected(true);
			connectAction(true);

		} catch (MicroMedException e) {

			UserMessage userMessage = new UserMessage(e);
			FormHelper.showMessage2User(userMessage);

		} finally {
			form.btnConnect.setEnabled(true);
			FormHelper.hideSpinner();
		}

	}

	public static void loadFiles() throws MicroMedException {

		try {
			String comPortName = form.getSelectedPortName();

			SinglePropertyValue command = PropertyReader
					.getInstance()
					.getSingleProperty(PropertyConstants.COMMAND_SEND_FILES);

			String command2execute = command.getValue();
			command2execute = command2execute.replaceAll(
					PropertyConstants.COMMAND_VALUE, comPortName);

			command2execute += " " + FileHelper.getDeviceComponentsFolder();
			executeMSDOSCommand(command2execute);

		} catch (MicroMedException e) {
			throw e;
		} catch (Exception e) {
			MicroMedException exception = new MicroMedException(
					Constants.Front.GENERAL_EXCEPTION_MESSAGE, e);
			throw exception;
		}

	}

	public static void executeMSDOSCommand(String command)
			throws MicroMedException {

		try {
			MicroMedLogPoller.getInstance().addLog("Executing : " + command);
			Runtime rt = Runtime.getRuntime();
			System.out.println(command);
			Process pr = rt.exec(command);

			BufferedReader input = new BufferedReader(new InputStreamReader(
					pr.getInputStream()));

			String line = null;

			while ((line = input.readLine()) != null) {
				MicroMedLogPoller.getInstance().addLog(line);
			}
			pr.waitFor();

		} catch (Exception e) {
			MicroMedException exception = new MicroMedException(
					Constants.Front.GENERAL_EXCEPTION_MESSAGE, e);
			throw exception;
		}
	}

	public static void setupClickBUtton() throws MicroMedException {
		try {
			FormHelper.toggleFormControls(true);
			FormHelper.setConfigSaveButton(true);
			form.loadAPNs(null);
		} catch (Exception e) {
			MicroMedException exception = new MicroMedException(
					Constants.Front.GENERAL_EXCEPTION_MESSAGE, e);
			throw exception;
		}
	}

	public static void resetAction(ActionEvent arg0) {
		MicroMedLogPoller.getInstance().clear();
		FormHelper.resetText();
	}

	public static boolean validateFormInformation() throws MicroMedException {
		return validateInputs() & validateRegion();
	}

	public static boolean validateSetUpInputs() {

		String[] fields = { "txtFlowGain:numeric", "txtFlowBalance:numeric",
				"txtFlowNormA:numeric", "txtFlowNormB:numeric" };
		boolean valid = true;
		boolean fieldValid = true;
		for (String fieldDescription : fields) {

			try {
				String fieldName = fieldDescription.split(":")[0];
				String fieldType = fieldDescription.split(":")[1];
				Field field = MicroMedFrame.class.getField(fieldName);
				JTextField textFieldForm = (JTextField) field
						.get(MicroMedFrame.getFrame());

				if (fieldType.toLowerCase().equals("numeric")) {
					fieldValid = ValidateHelper
							.validateNumberRange(textFieldForm.getText());
				}

				if (!fieldValid) {
					textFieldForm.setBorder(new LineBorder(Color.RED));
				}

			} catch (Exception e) {
				e.printStackTrace();
				fieldValid = false;
			}
			valid &= fieldValid;

		}
		return valid;
	}

	public static boolean validateInputs() {

		String[] fields = { "txtPumpID:text", "txtFlowGain:numeric",
				"txtFlowBalance:numeric", "txtFlowNormA:numeric",
				"txtFlowNormB:numeric" };
		boolean valid = true;
		boolean fieldValid = true;
		for (String fieldDescription : fields) {

			try {
				String fieldName = fieldDescription.split(":")[0];
				String fieldType = fieldDescription.split(":")[1];
				Field field = MicroMedFrame.class.getField(fieldName);
				JTextField textFieldForm = (JTextField) field
						.get(MicroMedFrame.getFrame());

				if (fieldType.toLowerCase().equals("numeric")) {
					fieldValid = ValidateHelper.validateNumber(textFieldForm
							.getText());
				} else {
					fieldValid = ValidateHelper.validateText(textFieldForm
							.getText());
				}

				if (!fieldValid) {
					textFieldForm.setBorder(new LineBorder(Color.RED));
				}

			} catch (Exception e) {
				e.printStackTrace();
				fieldValid = false;
			}
			valid &= fieldValid;

		}
		return valid;
	}

	public static boolean validateRegion() throws MicroMedException {

		JComboBox combo = MicroMedFrame.getFrame().cBoxAPNs;
		CompoundPropertyValue selectedApn = (CompoundPropertyValue) combo.getSelectedItem();
		
		if (selectedApn == null) {
			throw new MicroMedException("Please select a region.");
		}
		
		String apnValue = selectedApn.get(PropertyConstants.APN_VALUE).getValue();
		String apnUserName = selectedApn.get(PropertyConstants.APN_USER_NAME).getValue();
		String apnPassword = selectedApn.get(PropertyConstants.APN_PASSWORD).getValue();
		
		if (!ValidateHelper.validateText(apnValue)
				|| !ValidateHelper.validateTextLength(apnValue)) {
			
			combo.setBorder(new LineBorder(Color.RED));
			throw new MicroMedException("Invalid APN Value. Check " + PropertyConstants.APN_OPTIONS + " configuration and restart the application.");
		}
		
		if (apnUserName == null
				|| !ValidateHelper.validateTextLength(apnUserName)) {
			
			combo.setBorder(new LineBorder(Color.RED));
			throw new MicroMedException("Invalid APN UserName. Check " + PropertyConstants.APN_OPTIONS + " configuration and restart the application.");
		}
		
		if (apnPassword == null
				|| !ValidateHelper.validateTextLength(apnPassword)) {
			
			combo.setBorder(new LineBorder(Color.RED));
			throw new MicroMedException("Invalid APN Password. Check " + PropertyConstants.APN_OPTIONS + " configuration and restart the application.");
		}
		
		return true;
	}

	public static void saveDefaultFields(ActionEvent arg0) {
		try {
			FormHelper.clearErrorStyle();
			if (validateSetUpInputs()) {
				String txtPumpID = form.txtPumpID.getText();
				String txtFlowGain = form.txtFlowGain.getText();
				String txtFlowBalance = form.txtFlowBalance.getText();
				String txtFlowNormA = form.txtFlowNormA.getText();
				String txtFlowNormB = form.txtFlowNormB.getText();
				CompoundPropertyValue selectedAPN = (CompoundPropertyValue) form.cBoxAPNs
						.getSelectedItem();

				ConfigFields configFields = new ConfigFields(selectedAPN,
						txtPumpID, txtFlowGain, txtFlowBalance, txtFlowNormA,
						txtFlowNormB);

				configFields.serializar();
				Thread.sleep(1500);

				FormHelper.resetText();
				FormHelper.toggleFormControls(false);
				FormHelper.setConfigSaveButton(false);

				UserMessage message = new UserMessage("Set up complete !",
						JOptionPane.INFORMATION_MESSAGE);
				FormHelper.showMessage2User(message);

			} else {
				UserMessage message = new UserMessage(
						"Please check invalid inputs",
						JOptionPane.ERROR_MESSAGE);
				FormHelper.showMessage2User(message);
			}
		} catch (Exception e) {
			MicroMedException exception = new MicroMedException(
					Constants.Front.GENERAL_EXCEPTION_MESSAGE, e);
			UserMessage userMessage = new UserMessage(exception);
			FormHelper.showMessage2User(userMessage);

		} finally {
			FormHelper.hideSpinner();
		}

	}

	public static void getDefaultFields(ActionEvent arg0) {
		try {
			FormHelper.clearErrorStyle();
			ConfigFields configFields = ConfigFields.deserializar();

			form.txtPumpID.setText(configFields.getTxtPumpID());
			form.txtFlowGain.setText(configFields.getTxtFlowGain());
			form.txtFlowBalance.setText(configFields.getTxtFlowBalance());
			form.txtFlowNormA.setText(configFields.getTxtFlowNormA());
			form.txtFlowNormB.setText(configFields.getTxtFlowNormB());
			form.loadAPNs(configFields.getApnValue());

			Thread.sleep(1500);

		} catch (Exception e) {
			MicroMedException exception = new MicroMedException(
					Constants.Front.GENERAL_EXCEPTION_MESSAGE, e);
			UserMessage userMessage = new UserMessage(exception);
			FormHelper.showMessage2User(userMessage);

		} finally {
			FormHelper.hideSpinner();
		}

	}

}
