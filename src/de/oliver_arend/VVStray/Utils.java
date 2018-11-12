package de.oliver_arend.VVStray;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {

	public static String readFile(String path, Charset encoding) throws IOException { 
		byte[] encoded = Files.readAllBytes(Paths.get(path));
	  	return new String(encoded, encoding);
	}
	
	public static void writeFile(String path, String body) throws IOException {
		byte[] encoded = body.getBytes("UTF-8");
		Files.write(Paths.get(path), encoded);
	}
	
    public static boolean isNumeric(String str)  
    {  
      try { int i = Integer.parseInt(str); }  
      catch(NumberFormatException nfe) { return false; }  
      return true;  
    }

}
