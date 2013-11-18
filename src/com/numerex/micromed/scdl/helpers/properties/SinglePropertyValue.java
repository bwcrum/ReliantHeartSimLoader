package com.numerex.micromed.scdl.helpers.properties;

import com.numerex.micromed.scdl.helpers.PropertyValue;

public class SinglePropertyValue extends PropertyValue {

	private static final long serialVersionUID = 546719340905881442L;
	
	private String value;
	
	public SinglePropertyValue(String name, String value) {
		super.setName(name);
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object o) {
 		if (o instanceof SinglePropertyValue) {
			SinglePropertyValue spv = (SinglePropertyValue) o;
			return this.getName().equals(spv.getName())
					&& this.getValue().equals(spv.getValue());
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("%s = %s", this.getName() ,this.getValue());
	}
}
