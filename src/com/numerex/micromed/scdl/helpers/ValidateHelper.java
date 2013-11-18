package com.numerex.micromed.scdl.helpers;

import com.numerex.micromed.scdl.constants.Constants;

/**
 * Utility class to validate user inputs
 * 
 * @author dmoretti
 * 
 */
public class ValidateHelper {

	public static boolean validateNumber(String number) {
		boolean valid = false;
		try {
			Integer value = new Integer(number);
			valid = value.intValue() >= Constants.Validation.MIN_NUMBER_RANGE
					&& value.intValue() <= Constants.Validation.MAX_NUMBER_RANGE;
		} catch (Exception e) {
		}
		return valid;
	}
	
	public static boolean validateNumberRange(String number) {
		boolean valid = (number == null || number.trim().equals(""));
		try {
			Integer value = new Integer(number);
			valid = value.intValue() >= Constants.Validation.MIN_NUMBER_RANGE
					&& value.intValue() <= Constants.Validation.MAX_NUMBER_RANGE;
		} catch (Exception e) {
		}
		return valid;
	}
	
	public static boolean validateText(String text) {
		return text != null && !text.trim().equals("");
	}
	
	public static boolean validateNoSelectOption(String text) {
		return validateText(text) && !text.contains("Select");
	}
	
	public static boolean validateTextLength(String text) {
		if (text != null) {
			return text.length() <= Constants.Validation.MAX_TEXT_LENGTH;
		}
		
		return true;
	}

	/**
	 * Validates if the ip conforms to a serie of 4 sets of {1,3} digits
	 * If defaultIpValue is null, returns true.
	 * 
	 * @param defaultIpValue
	 * @return
	 */
	public static boolean validateIP(String ip) {
		boolean valid = true;
		
		if (ip != null) {
			valid = ip.matches("^\\d{1,3}\\.\\d{1,3}.\\d{1,3}.\\d{1,3}$");
		}
		
		return valid;
	}
	
	public static boolean validateOnlyDigits(String string) {
		boolean valid = true;
		
		if (string != null) {
			valid = string.matches("^\\d+$");
		}
		
		return valid;
	}
	

}
