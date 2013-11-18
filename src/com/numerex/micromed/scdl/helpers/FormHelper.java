package com.numerex.micromed.scdl.helpers;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.swing.border.LineBorder;

import com.numerex.micromed.scdl.constants.Constants;
import com.numerex.micromed.scdl.core.UserMessage;
import com.numerex.micromed.scdl.log.MicroMedLogPoller;
import com.numerex.micromed.scdl.views.MicroMedFrame;

public class FormHelper {
	
	public final static int flowGainMin = 0;
	public final static int flowGainMax = 255;
	public final static int flowBalanceMin = 0;
	public final static int flowBalanceMax = 255;
	public final static int flowNormAMin = 0;
	public final static int flowNormAMax = 255;
	public final static int flowNormBMin = 0;
	public final static int flowNormBMax = 255;
	private static MicroMedFrame form;
	
	public static void toggleControls(boolean icConnected) {
	
		form = MicroMedFrame.getFrame();
		form.cBoxPorts.setEnabled(!icConnected);
		toggleFormControls(icConnected);
		form.btnLoad.setEnabled(icConnected);
		form.btnLoadFirmware.setEnabled(icConnected);
		form.btnReset.setEnabled(icConnected);
		form.btnDefault.setEnabled(icConnected);

	}
	
	public static void toggleFormControls(boolean icConnected) {
		
		form = MicroMedFrame.getFrame();
		form.cBoxAPNs.setEnabled(icConnected);
		form.txtPumpID.setEnabled(icConnected);
		form.lblPumpID.setEnabled(icConnected);
		form.lblMessageLVAD.setEnabled(icConnected);
		form.lblAPN.setEnabled(icConnected);
		form.lblServer.setEnabled(icConnected);
		form.txtFlowGain.setEnabled(icConnected);
		form.lblFlowGain.setEnabled(icConnected);
		form.lblMessageGain.setEnabled(icConnected);	
		form.txtFlowBalance.setEnabled(icConnected);
		form.lblFlowBalance.setEnabled(icConnected);
		form.lblMessageFirmware.setEnabled(icConnected);
		form.lblMessageBalance.setEnabled(icConnected);
		form.txtFlowNormA.setEnabled(icConnected);
		form.lblFlowNormA.setEnabled(icConnected);
		form.lblMessageNormA.setEnabled(icConnected);
		form.txtFlowNormB.setEnabled(icConnected);
		form.lblFlowNormB.setEnabled(icConnected);
		form.lblMessageNormB.setEnabled(icConnected);

	}
	
	public static void setConfigButtonsDisplay(){
		form.btnConnect.setEnabled(!form.icConfigMode());
		setConfigButton(form.icConfigMode());
		setConfigSaveButton(false);
	}
	
	public static void setConfigButton(boolean enable){
		form.btnSetup.setEnabled(enable);
	}
	
	public static void setConfigSaveButton(boolean enable){
		form.btnSave.setEnabled(enable);
	}

	public static void setConfigDefaultButton(boolean enable){
		form.btnDefault.setEnabled(enable);
	}


	
	public static void setEnableButtons(boolean enable){
		form.btnConnect.setEnabled(enable);
		form.btnReset.setEnabled(enable);
		form.btnLoad.setEnabled(enable);
		form.btnLoadFirmware.setEnabled(enable);
	}
	
	public static void showSpinner(){
		form.spinner.setVisible(true);
	}
	
	public static void hideSpinner(){
		form.spinner.setVisible(false);
	}
	
	public static void showMessage2User(UserMessage message){
		if (message != null) {
			message.display();
			if (message.getException() != null) {
				MicroMedLogPoller.getInstance().addLog(message.getException());
				if (message.getException().getException() != null) {
					MicroMedLogPoller.getInstance().addLog(message.getException().getException());
				}
			}
		}
	}
	
	public static void displayVersions() {
		try {
			
			form.lblSCDLVersion.setText(form.lblSCDLVersion.getText() + FormHelper.readSCDLVersion());
			
			form.lblFirmwareVersion.setText(form.lblFirmwareVersion.getText() + FormHelper.readFirmwareVersion());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String readFirmwareVersion() {
		String version = null;
		
		try {
			InputStream is = new FileInputStream(Constants.Versioning.JAD_PATH);
			Manifest manifest = new Manifest(is);
			version = manifest.getMainAttributes().getValue(Constants.Versioning.FIRMWARE_VERSION_TAG);

		} catch (Exception e) {
			MicroMedLogPoller.getInstance().addLog(e);
		}
		
		if (version == null) {
			MicroMedLogPoller.getInstance().addLog("Could not obtain Firmware version number.");
			version = Constants.Versioning.DEFAULT_VERSION_NUMBER;
		}
		
		return version;
	}

	private static String readSCDLVersion() {
		String version = null;
		
		try {
			InputStream is = MicroMedFrame.class.getResourceAsStream(Constants.Versioning.MANIFEST_PATH);
			Manifest manifest = new Manifest(is);
			version = manifest.getMainAttributes().getValue(Constants.Versioning.SCDL_VERSION_TAG);
			
		} catch (Exception e) {
			MicroMedLogPoller.getInstance().addLog(e);
		}
		
		if (version == null) {
			MicroMedLogPoller.getInstance().addLog("Could not obtain SCDL version number.");
			version = Constants.Versioning.DEFAULT_VERSION_NUMBER;
		}
		
		return version;
	}
	
	// Resets input fields and sets background to white
	public static void resetText() {
		form.txtPumpID.setText("");
		form.txtFlowGain.setText("");
		form.txtFlowBalance.setText("");
		form.txtFlowNormA.setText("");
		form.txtFlowNormB.setText("");
		clearErrorStyle();
		
		form.loadAPNs(null);
	}
	
	public static void clearErrorStyle() {
		form.txtPumpID.setBorder(new LineBorder(Color.GRAY));
		form.txtFlowGain.setBorder(new LineBorder(Color.GRAY));
		form.txtFlowBalance.setBorder(new LineBorder(Color.GRAY));
		form.txtFlowNormA.setBorder(new LineBorder(Color.GRAY));
		form.txtFlowNormB.setBorder(new LineBorder(Color.GRAY));
		form.cBoxAPNs.setBorder(new LineBorder(Color.GRAY));		
	}
	
}
