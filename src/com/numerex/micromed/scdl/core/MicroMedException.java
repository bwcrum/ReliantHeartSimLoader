package com.numerex.micromed.scdl.core;

import com.numerex.micromed.scdl.constants.Constants;

public class MicroMedException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private String userMessage = "";
	private Exception exception = null;
	
	
	/*
	 * Constructors
	 */
	public MicroMedException(Exception exception) {
		super();
		this.exception = exception;
		this.userMessage = Constants.Front.GENERAL_EXCEPTION_MESSAGE;
	}
	public MicroMedException(String userMessage) {
		super();
		this.userMessage = userMessage;
	}
	public MicroMedException(String userMessage, Exception exception) {
		super();
		this.userMessage = userMessage;
		this.exception = exception;
	}
	
	
	/*
	 * Getters and Setters
	 */
	public String getUserMessage() {
		return userMessage;
	}
	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}

	
}
