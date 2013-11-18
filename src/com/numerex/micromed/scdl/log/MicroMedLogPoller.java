package com.numerex.micromed.scdl.log;

import java.io.File;
import java.io.PrintStream;
import java.util.Date;

import com.numerex.micromed.scdl.constants.Constants;
import com.numerex.micromed.scdl.core.UserMessage;
import com.numerex.micromed.scdl.helpers.DateHelper;
import com.numerex.micromed.scdl.helpers.FileHelper;
import com.numerex.micromed.scdl.views.MicroMedFrame;

public class MicroMedLogPoller {

    MicroMedLog log = new MicroMedLog();

    private static MicroMedLogPoller instance;
    private File file;
    private PrintStream print;


    private MicroMedLogPoller(){
    }

    public static MicroMedLogPoller getInstance(){
        if (instance == null) {
            instance = new MicroMedLogPoller();
            instance.createLogFile();
        }
        return instance;
    }

    public void createLogFile(){
        try {
            String date = DateHelper.dateToString(new Date(),Constants.DATE_PATTERN);
            String path = FileHelper.getLogFolder();

            String logPath = path + Constants.Log.ERROR_LOG_NAME + "_" + date +  Constants.EXTENSION;

            file = new File(logPath);
            print = new PrintStream(file);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void addLog(UserMessage message){
        if (message != null) {
        	this.addLog(message.getMessage());
        	print.println(Constants.Log.LOG_MESSAGE + message);

            if (message.getException() != null && message.getException().getException() != null) {
                print.println(Constants.Log.ERROR_STACK);
                print.println(message.getException().getException().getMessage());
                for (int i = 0; i < message.getException().getException().getStackTrace().length; i++) {
                    print.println(message.getException().getException().getStackTrace()[i]);
                }
            }
		}
    }
    
    public void addLog(String message){
    	log.addStackInfo(message);
    	print.println(Constants.Log.LOG_MESSAGE + message);
    }
    
    public void addLog(Exception exception){
    	if (exception != null) {
            print.println(Constants.Log.ERROR_STACK);
            print.println(exception.getMessage());
            print.println(exception.getCause());
            for (int i = 0; i < exception.getStackTrace().length; i++) {
                print.println(exception.getStackTrace()[i]);
            }
        }
    }

    public void clear(){
        log = new MicroMedLog();
        MicroMedFrame.getFrame().log("");
    }

    public void showLog(String deviceId){
        clear();
        log.setIcSelected(true);
        MicroMedFrame.getFrame().log(log.getLog());
    }
    
}
