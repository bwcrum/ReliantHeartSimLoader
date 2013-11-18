package com.numerex.micromed.scdl.helpers;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.numerex.micromed.scdl.constants.PropertyConstants;
import com.numerex.micromed.scdl.helpers.properties.CompoundPropertyValue;
import com.numerex.micromed.scdl.helpers.properties.PropertiesGroup;
import com.numerex.micromed.scdl.helpers.properties.SinglePropertyValue;

/**
 * Utility class to manage Property files
 * @author dmoretti
 *
 */
public class PropertyReader {

    public static String path;
    
    private ResourceBundle prop ;
    
    /*
     * Properties information hash
     */
	private Map<String, PropertyValue> info = null;
	private static PropertyReader instance = null;

    private PropertyReader() {
        this.info = new HashMap<String,PropertyValue>();
    }

    public static PropertyReader getInstance() {
        if (instance == null) {
        	instance = new PropertyReader();
        	instance.loadProperties();
        }
        return instance;
    }

    public PropertyValue getInfo(String key) {
    	PropertyValue value = null;
    	try {
            value = this.info.get(key);
		} catch (Exception e) {}
    	
    	return value;
	}
    
    public SinglePropertyValue getSingleProperty(String key) {
    	return (SinglePropertyValue) this.getInfo(key);
    }
    
    public PropertiesGroup getPropertiesGroup(String key) {
    	return (PropertiesGroup) this.getInfo(key);
    }

    private void loadProperties() {

    	prop = ResourceBundle.getBundle("conf.directoryPath");
    	
    	try {
            for (Enumeration<String> e = prop.getKeys(); e.hasMoreElements() ; ) {
                String obj = e.nextElement();
                loadPropertyInfo(obj,prop.getString(obj));
            }
    	} catch(Exception ex) {        	
        	ex.printStackTrace();
        	// TODO: Revisar el manejo de errores con exceptions
            // EnforaLogPoller.getInstance().addErrorLog(Constants.Log.UNKNOWN_ERROR, ex, null);
        }
    	
    }
    
    private void loadPropertyInfo(String key, String value){
    	try {
    		Pattern multiplePropertyPattern = Pattern.compile(PropertyConstants.MULTIPLE_PROPERTY_PATTERN);
    		Matcher multiplePropertyMatcher = multiplePropertyPattern.matcher(key);
    		
    		if (multiplePropertyMatcher.matches()) {
    			String groupName = multiplePropertyMatcher.group(1);
    			
    			loadGroupProperty(groupName, key, value);
    			
    		} else {
    			loadProperty(key, value);
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Loads a property which does not belongs to a group
     * 
     * @param name
     * @param value
     */
    private void loadProperty(String name, String value) {
    	Pattern pattern = Pattern.compile(PropertyConstants.COMPOUND_PROPERTY_VALUES_PATTERN);
    	Matcher matcher = pattern.matcher(value);
    	
    	if (matcher.matches()) {
    		String compoundProperty = matcher.group(1);
    		CompoundPropertyValue cpv = parseCompoundProperty(name, compoundProperty);
    		this.info.put(name, cpv);
    		
    	} else {
    		SinglePropertyValue single = new SinglePropertyValue(name, value);
    		this.info.put(name, single);
    	}
	}

    /**
     * Loads a property that belongs to a group into the configuration
     * 
     * Value param examples:
     * 
     *   [NAME=USA;VALUE=nmrx.intl.apn;USERNAME=user_1;PASSWORD=pass_1]
     *   Cinterion
     * 
     * 
     * @param groupName The name of the group that this property belong to, eg: CONFIG_APN
     * @param fullName The full name of the property, eg: CONFIG_APN.1
     * @param value The value to be parsed
     */
	private void loadGroupProperty(String groupName, String fullName, String value) {
    	if (!this.info.containsKey(groupName)) {
			this.info.put(groupName, new PropertiesGroup(groupName));
		}
		
    	PropertiesGroup propertiesGroup = (PropertiesGroup) this.info.get(groupName);

    	Pattern pattern = Pattern.compile(PropertyConstants.COMPOUND_PROPERTY_VALUES_PATTERN);
    	Matcher matcher = pattern.matcher(value);
    	
    	if (matcher.matches()) {
    		String compoundProperty = matcher.group(1);
    		CompoundPropertyValue cpv = parseCompoundProperty(fullName, compoundProperty);
    		propertiesGroup.add(cpv);
    		
    	} else {
    		SinglePropertyValue single = new SinglePropertyValue(fullName, value);
    		propertiesGroup.add(single);
    	}
	}
	
	/**
	 * Parses a compound property
	 * 
	 * @param name The name of the compound property eg: CONFIG_APN.1
	 * @param string The value to be parsed eg: NAME=USA;VALUE=nmrx.intl.apn;USERNAME=user_1;PASSWORD=pass_1
	 * @return
	 */
	private static CompoundPropertyValue parseCompoundProperty(String name, String string) {
		String[] singleProperties = string.split(PropertyConstants.COMPOUND_PROPERTIES_SEPARATOR);
		
		CompoundPropertyValue compoundProperty = new CompoundPropertyValue(name);
		
		for(String singleProperty : singleProperties) {
			SinglePropertyValue spv = parseSingleProperty(singleProperty);
			compoundProperty.add(spv);
		}
		
		return compoundProperty;
	}
	
	/**
	 * Parses a single property value.
	 * 
	 * Checks if the string matches (.+)=(.+) If the string does not match then both name and value are
	 * set to the given string.
	 * 
	 * @param string The string to be parsed eg: USERNAME=user_1
	 * @return
	 */
	private static SinglePropertyValue parseSingleProperty(String string) {
		Pattern pattern = Pattern.compile(PropertyConstants.SIMPLE_PROPERTY_FIELD_PATTERN);
		
		Matcher matcher = pattern.matcher(string);
		
		String name = null;
		String value = null;
		
		
		if (matcher.matches()) {
			name = matcher.group(1);
			value = matcher.group(2);
			
		} else {
			value = string;
		}
		
		return new SinglePropertyValue(name, value);
	}    
}