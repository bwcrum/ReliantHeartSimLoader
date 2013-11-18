package com.numerex.micromed.scdl.helpers.properties;

import java.util.ArrayList;
import java.util.List;

import com.numerex.micromed.scdl.helpers.PropertyValue;

public class PropertiesGroup extends PropertyValue {

	private static final long serialVersionUID = 2012450427203870463L;
	
	private List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();

	public PropertiesGroup(String name) {
		super.setName(name);
	}
	
	public void add(PropertyValue propertyValue) {
		this.propertyValues.add(propertyValue);
	}

	public List<PropertyValue> getPropertyValues() {
		return this.propertyValues;
	}

}
