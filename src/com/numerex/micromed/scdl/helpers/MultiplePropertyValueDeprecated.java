package com.numerex.micromed.scdl.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated
 */
public class MultiplePropertyValueDeprecated extends PropertyValue {
	
	private static final long serialVersionUID = -7150375976794548902L;
	
	private List<SinglePropertyValueDeprecated> lstValues = new ArrayList<SinglePropertyValueDeprecated>();
	
	public void addValue(String name, String value){
		lstValues.add(new SinglePropertyValueDeprecated(name, value));
	}

	public List<SinglePropertyValueDeprecated> getLstValues() {
		return lstValues;
	}
	
	public String getName() {
		if (lstValues.size() > 0){
			return lstValues.get(0).getValue();
		}
		return name;
	}

}
