package com.jamsaproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class NetRequest {

	// simplified version
	public static String tagRequest(String tag) throws Exception
	{
		return NetRequest.tagRequest(Resources.SERVER, Resources.PORT, Resources.PATH, Resources.SCRIPT, tag);
	}
	
	public static String tagRequest(String serverIpAddress, String serverPort, String serverPath, String script, String tag) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		/*
        HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + script);
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("tag", tag));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        */
		
		HttpGet get = new HttpGet("http://" + serverIpAddress + ":" + serverPort + serverPath + script + tag);
		
        try
	    {
	    	HttpResponse rp = hc.execute(get);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
        
        return serverResponse;
	}
	
}
