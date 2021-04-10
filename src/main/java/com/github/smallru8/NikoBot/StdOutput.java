package com.github.smallru8.NikoBot;

import java.util.Date;

public class StdOutput {

	public static void infoPrintln(String str) {
		Date date = new Date();
		String pstr = "[" + date.toString() + "][INFO] " + str;
		
		System.out.println(pstr);
	}
	
	public static void errorPrintln(String str) {
		Date date = new Date();
		String pstr = "[" + date.toString() + "][ERROR] " + str;
		
		System.err.println(pstr);
	}
	
	public static void warnPrintln(String str) {
		Date date = new Date();
		String pstr = "[" + date.toString() + "][WARN] " + str;
		
		System.out.println(pstr);
	}
}
