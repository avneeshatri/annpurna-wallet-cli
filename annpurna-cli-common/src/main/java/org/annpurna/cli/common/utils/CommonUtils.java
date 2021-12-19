package org.annpurna.cli.common.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;

public class CommonUtils {

	 public static String deserialize(byte[] data) {
	        return new String(data, UTF_8);
	 }
	 
	 public static String resolvePrivateKeyPath(String dirPath) {
		    String privateKey = null ;
	        File f = new File(dirPath);
	        String[] pathnames = f.list();
	        for (String pathname : pathnames) {
	            if(pathname != null) {
	            	privateKey = dirPath + File.separator + pathname ;
	            	break ;
	            }
	        }
	        
		    return privateKey ;
	 }
}
