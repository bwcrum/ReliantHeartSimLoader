package com.numerex.micromed.scdl.helpers;

/**
 * String Utility class 
 * 
 * @author dmoretti
 * 
 */
public class StringHelper {

	public static boolean blank(String description) {
		return description == null || description.trim().equals("");
	}
	

}
