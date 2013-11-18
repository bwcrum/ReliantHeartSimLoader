package com.numerex.micromed.scdl.helpers.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.numerex.micromed.scdl.helpers.PropertyValue;

public class CompoundPropertyValue extends PropertyValue {

	private static final long serialVersionUID = -6373971490088409542L;
	
	private Map<String, SinglePropertyValue> singleProperties;

	private static final String DEFAULT_KEY = "NAME";
	
	public CompoundPropertyValue(String name) {
		super.setName(name);
		this.singleProperties = new HashMap<String, SinglePropertyValue>();
	}
	
	public void add(SinglePropertyValue propertyValue) {
		this.singleProperties.put(propertyValue.getName(), propertyValue);
	}
	
	public void add(String name, String value) {
		SinglePropertyValue property = new SinglePropertyValue(name, value);
		this.add(property);
	}
	
	public SinglePropertyValue get(String name) {
		return this.singleProperties.get(name);
	}
	
	@Override
	/**
	 * Returns true if the speficied object is also a CompoundPropertyValue, and both singleProperties map are equal. 
	 * It doesn't check name properties.
	 * 
	 * TODO refactor, not override equals with a implementation not compatible with the standard equals meaning
	 * @param o
	 * @return
	 */
	public boolean equals(Object o) {
		if (o instanceof CompoundPropertyValue) {
			CompoundPropertyValue other = (CompoundPropertyValue) o;
			
			if (this.singleProperties.size() != other.singleProperties.size()) {
				return false;
			}
			
			for(Entry<String, SinglePropertyValue> singlePropertyEntry : this.singleProperties.entrySet()) {
				if (singlePropertyEntry.getKey().equals(DEFAULT_KEY)) {
					continue;
				}
				
				SinglePropertyValue otherProperty = other.get(singlePropertyEntry.getKey());
 				if (!singlePropertyEntry.getValue().equals(otherProperty)) {
					return false;
				}
			}
			
		} else {
			return false;
		}
		
		return true;
	}
	
	
	@Override
	public String toString() {
		String string = this.get(DEFAULT_KEY).getValue();
		
		if (string == null) {
			string = super.toString();
		}
		
		return string;
	}
}
