package com.numerex.micromed.scdl.helpers;

import java.io.File;


/**
 * File Utility class 
 * 
 * @author dmoretti
 * 
 */
public class FileHelper {

	public static String getDeviceComponentsFolder() {
		
		File f = new File("");
 	    String path = f.getAbsolutePath();

 	    return path + getSlash2Path(path) + "files";	
 	    
	}
	
	public static String getLogFolder() {
	
		File f = new File("");
 	    String path = f.getAbsolutePath();

 	    String slash = getSlash2Path(path);
 	    return path + slash + "log" + slash;
	}
	
	private static String getSlash2Path(String path){
		String slash = "";
 	    if (path.contains("\\")) {
			slash = "\\";
		}else if (path.contains("/")){
			slash = "/";
		}
		return slash;
	}
	
	public static String getConfigFolder() {
		File f = new File("");
 	    String path = f.getAbsolutePath();

 	    String slash = "";
 	    if (path.contains("\\")) {
			slash = "\\";
		}else if (path.contains("/")){
			slash = "/";
		}
		
		path = path + slash + "conf" + slash;
		return path;
	}
	
	public static void main(String[] args) {
		System.out.println(getDeviceComponentsFolder());
	}

}
