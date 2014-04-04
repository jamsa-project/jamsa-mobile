package com.jamsaproject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.speech.*;
import android.speech.tts.TextToSpeech;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;


public class JamsaProjectActivity extends Activity {

	private Hashtable<String, String> simpleTags = new Hashtable<String, String>();

	private static Context mContext = null;
	private static Activity mActivity = null;

	private TextToSpeech textToSpeech;
	Locale spanishLocale = new Locale("es", "ES");

	private static Typeface mTypeface = null;

	// Get UsbManager from Android.
	UsbManager manager;// = (UsbManager) getSystemService(Context.USB_SERVICE);

	// Find the first available driver.
	UsbSerialDriver driver;// = UsbSerialProber.acquire(manager);
	
	private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
	
	private SerialInputOutputManager mSerialIoManager;
	
	private String tagRead = "";

	// handles reads
    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

        @Override
        public void onRunError(Exception e) {
            Log.d("JAMSA", "Runner stopped.");
        }

        @Override
        public void onNewData(final byte[] data) {
            JamsaProjectActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
 
                	String s = "";
                	
                	try {
						s = new String(data, "US-ASCII");
						
						tagRead += s;
						
						// is the end of a tag id?
						if(data[data.length - 1] == 0x03)
						{
							tagRead = tagRead.replaceAll("\\p{C}", "");
							
							JamsaProjectActivity.this.dialog(tagRead.length() + " - " +tagRead);
							
							//speechTag(tagRead);
							
							Utils.threadTag(tagRead);
							
							tagRead = "";
						}
						
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                	

                }
            });
        }
    };
    
    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.i("JAMSA", "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }
    
    private void startIoManager() {
        driver = UsbSerialProber.acquire(manager);
        
    	if (driver != null) {
    		
            try {
    			driver.setBaudRate(9600);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
            Log.i("JAMSA", "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(driver, mListener);
            mExecutor.submit(mSerialIoManager);
            
            Toast toast1 = Toast.makeText(getApplicationContext(),
					"IOManager Started.",
					Toast.LENGTH_LONG);

			toast1.show();
        }
    }
    
    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

	public static Context getAppContext() {
		return JamsaProjectActivity.mContext;
	}

	public static Activity getAppActivity() {
		return JamsaProjectActivity.mActivity;
	}

	public static Typeface getAppTypeface() {
		return JamsaProjectActivity.mTypeface;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		JamsaProjectActivity.mContext = this;
		JamsaProjectActivity.mActivity = this;
		
		// hash for testing
		simpleTags.put("010DC60667AB", "Buenos dias Fernando");
		simpleTags.put("010DC60628E4", "Buenos dias Rafa");
		simpleTags.put("010DC60627EB", "Buenos dias Ruben");
		simpleTags.put("010DC60625E9", "Buenos dias Hack For Good");
		simpleTags.put("010DC60633FF", "Buenos dias Mundo");
		simpleTags.put("010DC60666AA", "Buenos dias A todos");


		// pretty font ;)
		JamsaProjectActivity.mTypeface = Typeface.createFromAsset(
				getAssets(), "fonts/Roboto-Light.ttf");

		TextView tv = (TextView) findViewById(R.id.lblTitle);
		tv.setTypeface(JamsaProjectActivity.getAppTypeface());

		// Create tts object
		textToSpeech = new TextToSpeech(getApplicationContext(),
				new TextToSpeech.OnInitListener() {
					@Override
					public void onInit(int status) {
						if (status != TextToSpeech.ERROR) {
							textToSpeech.setLanguage(spanishLocale);
						}
					}
				});

		// Get UsbManager from Android.
		manager = (UsbManager) getSystemService(Context.USB_SERVICE);
		
	}
	
	public void dialog(String text)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
 
			// set title
			alertDialogBuilder.setTitle("JAMSA");
 
			// set dialog message
			alertDialogBuilder
				.setMessage(text)
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}
	
	public void speechTag(String tag) {
		String text = simpleTags.get(tag);

		if (text == null) {
			talk("Clave inválida");
		} else {
			talk(text);
		}

	}

	public void talk(String text) {
		textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

	public void openUrl(String url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onPause() {
		if (textToSpeech != null) {
			textToSpeech.stop();
			textToSpeech.shutdown();
		}
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		onDeviceStateChange();
		Resources.activity = this;
	}

}