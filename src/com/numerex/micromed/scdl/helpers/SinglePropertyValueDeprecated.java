package com.numerex.micromed.scdl.helpers;

import java.io.Serializable;

/**
 * @deprecated
 */
public class SinglePropertyValueDeprecated extends PropertyValue implements Serializable {
	
	private static final long serialVersionUID = 2L;

	private String value;
	
	public SinglePropertyValueDeprecated(String name,String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	
	public boolean equals(Object obj) {
		try {
			SinglePropertyValueDeprecated value = (SinglePropertyValueDeprecated) obj;
			return this.value.equals(value.getValue()) &&
					this.name.equals(value.getName());
		} catch (Exception e) {
			return false;
		}
	}

}
