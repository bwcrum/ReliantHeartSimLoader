package com.numerex.micromed.scdl.constants;

public class Constants {

    public static final String DATE_PATTERN = "MMddyyyy_hhmmaaa";
    public static final String EXTENSION = ".log";
    public static final int PROGRESS_COLUMN_INDEX = 3;
    public static final String HEY_COMMAND = "AT+CGSN";
    public static final String BACKSLASH = "\\";
    public static final String SLASH = "//";
    public static final String STRING_SEPARATOR = ";";
    
    public interface Log {
        public static final int STACK_SIZE = 50;
        public static final String ERROR_LOG_NAME = "Stack_trace_log";
        public static final String LOG_MESSAGE = "LOG MESSAGE: ";
        public static final String ERROR_STACK = "ERROR STACK: ";
        public static final String DEVICE = "DEVICE: ";
        public static final String PORT = "PORT: ";
        public static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
        public static final String READ_ERROR = "READ_ERROR";
        public static final String WRITE_ERROR = "WRITE_ERROR";
        public static final String CONNECTION_ERROR = "CONNECTION_ERROR";
    }

    public interface Front {
        public static final String JOPTION_PANE_TITLE = "System message";
        public static final String CONNECT_LABEL = "Connect";
        public static final String OPTION_SELECT_LABEL = "Select an option ... ";
        
        public static final String GENERAL_EXCEPTION_MESSAGE = "An unexpected error has occurred. please contact the administrator";
        
    }
    
    public interface Validation {
    	public static final int MIN_NUMBER_RANGE = 0;
    	public static final int MAX_NUMBER_RANGE = 255;
		public static final int MAX_TEXT_LENGTH = 18;
    }
    
    public interface Versioning {
    	public static final String JAD_PATH = "files/ConsolidatedMicromed.jad";
    	
    	public static final String MANIFEST_PATH = "/META-INF/MANIFEST.MF";
    	
    	public static final String FIRMWARE_VERSION_TAG = "FirmwareVersion";
    	public static final String SCDL_VERSION_TAG = "SCDL-Version";
    	
    	public static final String DEFAULT_VERSION_NUMBER = "-";
    }
}
