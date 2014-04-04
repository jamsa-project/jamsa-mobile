package com.jamsaproject;

import org.json.JSONObject;

public class NetParse {
	// last error in parse process
	private static String _lastMessage;

	// parse tag json data
	public static boolean ParseTag(String jString)
	{
		NetParse._lastMessage = "";
		
		try {
			JSONObject jObject = new JSONObject(jString);
			
			if(jObject.getInt("status") == 0)
			{
				Resources.lastTagId = jObject.getString("id");
				Resources.lastTagStatus = jObject.getString("status");
				Resources.lastTagCommand = jObject.getString("command");
				Resources.lastTagData = jObject.getString("data");
				
			}else
			{
				Resources.lastTagId = "";
				Resources.lastTagStatus = "";
				Resources.lastTagCommand = "";
				Resources.lastTagData = "";
				
				return false;
			}

			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			NetParse._lastMessage = "EXCEPTION: " + e;
		}
		
		return false;
	}
	
	public static String lastMessage()
	{
		return NetParse._lastMessage;
	}
}
