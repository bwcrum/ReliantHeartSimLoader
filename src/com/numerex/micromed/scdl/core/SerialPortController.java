package com.numerex.micromed.scdl.core;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

import javax.swing.JOptionPane;

import com.numerex.micromed.scdl.constants.PropertyConstants;
import com.numerex.micromed.scdl.helpers.PropertyReader;
import com.numerex.micromed.scdl.log.MicroMedLogPoller;

public class SerialPortController implements SerialPortEventListener {

	// for containing the ports that will be found
	@SuppressWarnings("rawtypes")
	private Enumeration ports = null;
	// map the port names to CommPortIdentifiers
	private Map<String, CommPortIdentifier> portMap = new HashMap<String, CommPortIdentifier>();

	// this is the object that contains the opened port
	private CommPortIdentifier selectedPortIdentifier = null;
	private SerialPort serialPort = null;

	// input and output streams for sending and receiving data
	private InputStream input = null;
	private OutputStream output = null;

	// just a boolean flag that i use for enabling
	// and disabling buttons depending on whether the program
	// is connected to a serial port or not
	private boolean icConnected = false;

	// the timeout value for connecting with the port
	final static int TIMEOUT = 2000;

	// some ascii values for for certain things
	final static int SPACE_ASCII = 32;
	final static int DASH_ASCII = 45;
	final static int NEW_LINE_ASCII = 10;

	// a string for recording what goes on in the program
	// this string is written to the GUI
	public String logText = "";
	String[] lines;
	int counter = 0;
	
	private static SerialPortController instance = null;

	private SerialPortController() {
		
	}
	
	public static SerialPortController getInstance(){
		if (instance == null) {
			instance = new SerialPortController();
		}
		return instance;
	}

	public String getLogText() {
		return logText;
	}

	public void setLogText(String logText) {
		this.logText = logText;
	}

	// Search for all the serial ports
	public List<CommPortIdentifier> searchPorts() {
		
		ports = CommPortIdentifier.getPortIdentifiers();
		List<CommPortIdentifier> lstPorts = new ArrayList<CommPortIdentifier>();
		
		while (ports.hasMoreElements()) {
			CommPortIdentifier curPort = (CommPortIdentifier) ports
					.nextElement();

			// get only serial ports
			if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				portMap.put(curPort.getName(), curPort);
				lstPorts.add(curPort);
			}
		}
		return lstPorts;
	}

	
	// connect to the selected port in the combo box
	// pre: ports are already found by using the searchForPorts method
	// post: the connected comm port is stored in commPort, otherwise,
	// an exception is generated
	public void connect(String selectedPort) throws Exception {
		
		selectedPortIdentifier = (CommPortIdentifier) portMap.get(selectedPort);

		CommPort commPort = null;

//		try {
			Thread.sleep(2000);
			// the method below returns an object of type CommPort
			commPort = selectedPortIdentifier.open("SIMCardPreloader", TIMEOUT);
			// the CommPort object can be casted to a SerialPort object
			serialPort = (SerialPort) commPort;

			// for controlling GUI elements
			icConnected = true;

			// logging
			MicroMedLogPoller.getInstance().addLog(selectedPort + " opened successfully.");
			
//			JOptionPane.showMessageDialog(null, logText, logText,
//					JOptionPane.INFORMATION_MESSAGE);

			// enables the controls on the GUI if a successful connection is
			// made
//			window.MiscFunctions.toggleControls();
//		} catch (PortInUseException e) {
//			logText = selectedPort
//					+ " is in use. Please make sure no other programs have "
//					+ selectedPort + " open.";
//			JOptionPane.showMessageDialog(null, logText, logText,
//					JOptionPane.ERROR_MESSAGE);
//		} catch (Exception e) {
//			logText = "Failed to open. Please make sure device is connected, and powered on.";
//			JOptionPane.showMessageDialog(null, logText, logText,
//					JOptionPane.ERROR_MESSAGE);
//		}
	}

	// open the input and output streams
	// pre: an open port
	// post: initialized input and output streams for use to communicate data
	
	
	public boolean initIOStream() throws MicroMedException {
		try {
			// Attempts to write to the dev kit
			input = serialPort.getInputStream();
			output = serialPort.getOutputStream();
			
			return true;
			
		} catch (IOException e1) {
			MicroMedException exception = new MicroMedException("I/O Streams failed to open", e1);
			throw exception;
			
		} catch (Exception e2) {
			MicroMedException exception = new MicroMedException(e2);
			throw exception;
		}
	}


	// starts the event listener that knows whenever data is available to be
	// read
	// pre: an open serial port
	// post: an event listener for the serial port that knows when data is
	// recieved
	public void initListener() {
		try {
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (TooManyListenersException e) {
			logText = "Too many listeners. (" + e.toString() + ")";
			JOptionPane.showMessageDialog(null, logText, logText,
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// disconnect the serial port
	// pre: an open serial port
	// post: closed serial port
	public void disconnect() throws Exception {
		// close the serial port
			serialPort.removeEventListener();
			serialPort.close();
			serialPort = null;
			input.close();
			output.close();
			input =  null;
			output = null;
			icConnected = false;

			Thread.sleep(3000);
	}

	public boolean isIcConnected() {
		return icConnected;
	}

	public void setIcConnected(boolean icConnected) {
		this.icConnected = icConnected;
	}

	// what happens when data is received
	// pre: serial event is triggered
	// post: processing on the data it reads
	public void serialEvent(SerialPortEvent evt) {
		if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				byte singleData = (byte) input.read();

				if (singleData != NEW_LINE_ASCII) {
					logText += new String(new byte[] { singleData });
					
				} 
			} catch (Exception e) {
				logText = "Failed to read data. (" + e.toString() + ")";
				JOptionPane.showMessageDialog(null, logText, logText,
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Method that can be called to send data
	 * 
	 * @param input. String to write on COM port
	 * @return
	 * 
	 * precondition: open serial port
	 * postcondition: data sent to the other device
	 * 
	 */
	public DeviceResponse writeData(String input) throws MicroMedException {
		
		DeviceResponse deviceResponse = null;
		try {
			Thread.sleep(200);
			
			String newline = "\r\n";
			String text = input + newline;
			output.write(text.getBytes());
			output.flush();
			
			int delay = new Integer(PropertyReader.getInstance().getSingleProperty(PropertyConstants.DELAY).getValue());
			Thread.sleep(delay);
			
			MicroMedLogPoller.getInstance().addLog("Sending : " + input);
			deviceResponse = decodeCommandResponse(logText);
			MicroMedLogPoller.getInstance().addLog("Device response : " + deviceResponse);

			logText = "";
			
			if (deviceResponse.getMessage() != null && deviceResponse.getMessage().equals("ERROR")) {			
				logText = "Failed to write data on COM port";
				throw new MicroMedException(logText);
			}	
			
		} catch (Exception e) {
			logText = "Failed to write data on COM port";
			throw new MicroMedException(logText,e);
		}
		return deviceResponse;
	}

	public String encodeCommand(String command, String parameter, boolean icString){
		
		command = command.replaceAll("\"", "");
		if (icString) {
			parameter = "\"" + parameter + "\"";
		}
		command = command.replaceAll(PropertyConstants.COMMAND_VALUE, parameter);
		return command;
	}
	
	public DeviceResponse decodeCommandResponse(String response){
		
		DeviceResponse deviceResponse = null;
		String name, value, message = null;
		
		try {
			int lastIndex = response.lastIndexOf(",");
			if (lastIndex >= 0) {
				
				name = this.extractMiddleSubstring(logText.substring(0, lastIndex));
				
				logText = logText.substring(++lastIndex);
				logText = this.sanitizeString(logText);
				lines = logText.split("\\r");
				
				if (lines.length > 1) {
					value = lines[0];
					message = lines[1];
					
				}else{
					value = lines[0];
					message = lines[0];
				}
				
				deviceResponse = new DeviceResponse(name, value, message);
			}else{
				deviceResponse = new DeviceResponse(null, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return deviceResponse;
	
	}

	/**
	 * Receives a string like: $a,"$b",$c and returns $b  
	 * 
	 * If the substring doesn't match the expected input, then returns null.
	 * 
	 * Example:
	 *   substring = AT+CPBW=36,"2",209
	 *   returns 2
	 *   
	 * @param substring
	 * @return
	 */
	private String extractMiddleSubstring(String substring) {
		String value = null;

		String[] fragments = substring.split(",");
		
		if (fragments.length == 3) {
			value = fragments[1];
			value = this.sanitizeString(value);
		}
		
		return value;
	}

	private String sanitizeString(String string) {
		string = string.replaceAll("\\r\\r", "\\\r")
						.replaceAll(";", "")
						.replaceAll("\"", "");

		return string;
	}
	
	
}