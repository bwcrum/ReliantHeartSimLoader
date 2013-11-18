package com.numerex.micromed.scdl.log;

import java.util.ArrayList;
import java.util.List;

import com.numerex.micromed.scdl.constants.Constants;
import com.numerex.micromed.scdl.views.MicroMedFrame;

public class MicroMedLog {

    private List<String> stack;
    private boolean icSelected = false;

    public MicroMedLog(){
    	stack = new ArrayList<String>();
    }

    public void log(String info){
        this.addStackInfo(info);
    }

    public void addStackInfo(String info){
        if (this.stack.size() == Constants.Log.STACK_SIZE) {
            this.stack.remove(0);            
        }
        this.stack.add(info);
        MicroMedFrame.getFrame().log(getLog());
    }
    
    public String getLog(){
        String log = "";
        for (String stackInfo : stack) {
            log += stackInfo + System.getProperty("line.separator");
        }
        return log;        
    }

    public boolean isIcSelected() {
        return icSelected;
    }

    public void setIcSelected(boolean icSelected) {
        this.icSelected = icSelected;
    }    
}
