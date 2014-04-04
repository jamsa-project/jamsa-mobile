package com.jamsaproject;


public class Resources {
	// connection constants
	public static final String SERVER = "jamsa.no-ip.org";
	public static final String PORT = "3000";
	public static final String PATH = "/";
	public static final String SCRIPT = "";
	
	// hack: activity always referenced
	public static JamsaProjectActivity activity = null;
	
	public static String lastCommand = "";
	public static String lastSpeech = "";

	// tag data received from server
	public static String lastTagId = "";
	public static String lastTagStatus = "";
	public static String lastTagCommand = "";
	public static String lastTagData = "";
}
