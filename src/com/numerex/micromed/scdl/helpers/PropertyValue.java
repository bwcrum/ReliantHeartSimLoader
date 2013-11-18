package com.numerex.micromed.scdl.helpers;

import java.io.Serializable;

public class PropertyValue implements Serializable, Comparable<PropertyValue> {
	
	private static final long serialVersionUID = 3L;
	
	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	public int compareTo(PropertyValue o) {
		if (this.getName() == null) {
			return -1;
		}
		
		return this.getName().compareTo(o.getName());
	}
	
}
