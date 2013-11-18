package com.numerex.micromed.scdl.core;

import javax.swing.JOptionPane;

import com.numerex.micromed.scdl.constants.Constants;

public class UserMessage {
	
	public MicroMedException getException() {
		return exception;
	}

	private String message;
	private MicroMedException exception;
	
	private int messageType;
	
	public UserMessage(String message, int messageType) {
		super();
		this.message = message;
		this.messageType = messageType;
	}
	
	public UserMessage(MicroMedException exception) {
		super();
		this.exception = exception;
		this.messageType = JOptionPane.ERROR_MESSAGE;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	
	public void display(){
		
		String message2Display = Constants.Front.GENERAL_EXCEPTION_MESSAGE;
		
		if (exception != null) {
			message2Display = this.exception.getUserMessage() + "\n";
			if (this.exception.getMessage() != null) {
				message2Display += "EXCEPTION: " + this.exception.getMessage() + "\n";
			}
			
		}else if (message != null) {
			message2Display = message;
		}
		
		JOptionPane.showMessageDialog(null, message2Display, "System message",messageType);
	}

}
