package com.numerex.micromed.scdl.core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.numerex.micromed.scdl.constants.PropertyConstants;
import com.numerex.micromed.scdl.helpers.FileHelper;
import com.numerex.micromed.scdl.helpers.StringHelper;
import com.numerex.micromed.scdl.helpers.properties.CompoundPropertyValue;

public class ConfigFields implements Serializable {
	
	private CompoundPropertyValue apnValue = null;
	private String txtPumpID = PropertyConstants.DEFAULT_VALUE;
	private String txtFlowGain = PropertyConstants.DEFAULT_VALUE;
	private String txtFlowBalance = PropertyConstants.DEFAULT_VALUE;
	private String txtFlowNormA = PropertyConstants.DEFAULT_VALUE;
	private String txtFlowNormB = PropertyConstants.DEFAULT_VALUE;
	
	private static String path = FileHelper.getConfigFolder() + PropertyConstants.CONFIG_FIELD_FILE_NAME;
	
	private static final long serialVersionUID = 1L;

	public ConfigFields(CompoundPropertyValue apnValue, String txtPumpID,
			String txtFlowGain, String txtFlowBalance, String txtFlowNormA,
			String txtFlowNormB) {
		super();
		this.setApnValue(apnValue);
		this.txtPumpID = StringHelper.blank(txtPumpID)? PropertyConstants.DEFAULT_VALUE:txtPumpID;
		this.txtFlowGain = StringHelper.blank(txtFlowGain)? PropertyConstants.DEFAULT_VALUE:txtFlowGain;;
		this.txtFlowBalance = StringHelper.blank(txtFlowBalance)? PropertyConstants.DEFAULT_VALUE:txtFlowBalance;;
		this.txtFlowNormA = StringHelper.blank(txtFlowNormA)? PropertyConstants.DEFAULT_VALUE:txtFlowNormA;;
		this.txtFlowNormB = StringHelper.blank(txtFlowNormB)? PropertyConstants.DEFAULT_VALUE:txtFlowNormB;;
	}
	
	public ConfigFields() {
		super();
	}

	public void serializar () throws IOException {
        
    	ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(path));
        output.writeObject(this);
        output.close();
    }
   
    public static ConfigFields deserializar() throws IOException, ClassNotFoundException {
        
    	try {
    		ObjectInputStream input = new ObjectInputStream(new FileInputStream(path));
            ConfigFields configFields = (ConfigFields) input.readObject();
            input.close();
            
            return configFields;
		} catch (Exception e) {
			return new ConfigFields();
		}
        
    }
    
	public String getTxtPumpID() {
		return txtPumpID;
	}

	public void setTxtPumpID(String txtPumpID) {
		this.txtPumpID = txtPumpID;
	}

	public String getTxtFlowGain() {
		return txtFlowGain;
	}

	public void setTxtFlowGain(String txtFlowGain) {
		this.txtFlowGain = txtFlowGain;
	}

	public String getTxtFlowBalance() {
		return txtFlowBalance;
	}

	public void setTxtFlowBalance(String txtFlowBalance) {
		this.txtFlowBalance = txtFlowBalance;
	}

	public String getTxtFlowNormA() {
		return txtFlowNormA;
	}

	public void setTxtFlowNormA(String txtFlowNormA) {
		this.txtFlowNormA = txtFlowNormA;
	}

	public String getTxtFlowNormB() {
		return txtFlowNormB;
	}

	public void setTxtFlowNormB(String txtFlowNormB) {
		this.txtFlowNormB = txtFlowNormB;
	}

	public CompoundPropertyValue getApnValue() {
		return apnValue;
	}

	public void setApnValue(CompoundPropertyValue apnValue) {
		this.apnValue = apnValue;
	}
    
}
