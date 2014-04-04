package com.jamsaproject;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class Utils {
	
	// new thread, not blocking ui
	public static void threadTag(final String tag)
	{
		try
			{
				new Thread(new Runnable() {
				    public void run() {
					    try {
					
							final String respuesta = NetRequest.tagRequest(tag);
							
							/*
							NetParse.ParseTag(respuesta);
							Resources.activity.runOnUiThread(new Runnable() {
						        public void run() {
						            Toast.makeText(Resources.activity, Resources.lastTagData, Toast.LENGTH_LONG).show();
						            Resources.activity.talk(Resources.lastTagData);
						        }
						    });
						    */
							
							
							if(NetParse.ParseTag(respuesta))
							{
								// Speech command
								if(Resources.lastTagCommand.equals("speech"))
								{
									Resources.activity.runOnUiThread(new Runnable() {
								        public void run() {
								            Toast.makeText(Resources.activity, Resources.lastTagData, Toast.LENGTH_LONG).show();
								            Resources.activity.talk(Resources.lastTagData);
								        }
								    });
								}
								
								if(Resources.lastTagCommand.equals("open_url"))
								{
									Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Resources.lastTagData));
									Resources.activity.startActivity(browserIntent);
								}
								
							}else
							{
								Resources.activity.runOnUiThread(new Runnable() {
							        public void run() {
							            Toast.makeText(Resources.activity, NetParse.lastMessage(), Toast.LENGTH_LONG).show();
							        }
							    });
							}
							
							
							Resources.activity.runOnUiThread(new Runnable() {
						        public void run() {
						            Toast.makeText(Resources.activity, respuesta, Toast.LENGTH_LONG).show();
						        }
						    });
						    
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							
							final String err = e.getMessage();
							Resources.activity.runOnUiThread(new Runnable() {
						        public void run() {
						            Toast.makeText(Resources.activity, "Ha sido imposible conectarse al servicio. 1", Toast.LENGTH_LONG).show();
						            Resources.activity.talk("Ha sido imposible conectarse al servicio.");
						            
						            Resources.activity.dialog(err);
						            
						        }
						    });
						}
	       				
				    }
				}).start();
			}catch(Exception exc)
			{
				Log.d("EXCEPTION", exc.toString());
				Resources.activity.runOnUiThread(new Runnable() {
			        public void run() {
			            Toast.makeText(Resources.activity, "Ha sido imposible conectarse al servicio. 2", Toast.LENGTH_LONG).show();
			        }
			    });
			}
	}
}
