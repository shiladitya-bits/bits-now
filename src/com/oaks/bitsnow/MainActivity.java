package com.oaks.bitsnow;

import static com.oaks.bitsnow.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.oaks.bitsnow.CommonUtilities.EXTRA_MESSAGE;
import static com.oaks.bitsnow.CommonUtilities.SENDER_ID;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {
	// label to display gcm messages
	TextView lblMessage;
	
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	
	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	
	// Connection detector
	ConnectionDetector cd;
	public static String email;
	Button b;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		b = (Button)findViewById(R.id.buttonCont);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				String prof = prefs.getString("profile", "s");
				if(prof.equals("s"))
				{
					startActivity(new Intent(MainActivity.this,StudentHome.class));
					finish();
				}
				else if(prof.equals("f"))
				{
					startActivity(new Intent(MainActivity.this,FacultyHome.class));
					finish();
				}
			}
		});
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(MainActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		email = prefs.getString("userid", "");	
				
		
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		Log.d("gcm checker","device ok");
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);
		Log.d("gcm checker","manifest ok");
		
		lblMessage = (TextView) findViewById(R.id.lblMessage);
		
		registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
		
		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);
		
		Log.d("current registration id",regId);
		// Check if regid already presents
		if (regId.equals("")) 
		{
			// Registration is not present, register now with GCM			
			GCMRegistrar.register(this, SENDER_ID);
		} 
		else 
		{
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) 
			{
				// Skips registration.				
				//Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
				lblMessage.setText("Already registered with GCM. Click Continue");
				b.setClickable(true);
			} 
			else
			{
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						ServerUtilities.register(context, email, regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
						b.setClickable(true);
						lblMessage.setText("Already registered with GCM. Click Continue");
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}		

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());
			
			/**
			 * Take appropriate action on this message
			 * depending upon your app requirement
			 * For now i am just displaying it on the screen
			 * */
			
			// Showing received message
			lblMessage.append(newMessage + "\n");			
			Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
			
			// Releasing wake lock
			WakeLocker.release();
		}
	};
	
	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
