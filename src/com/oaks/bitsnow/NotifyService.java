package com.oaks.bitsnow;


import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class NotifyService extends Service {

	private final IBinder mBinder = new MyBinder();
    private static final int NOTIF_ID = 1;
	String response;
	int hour;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		Calendar cal = Calendar.getInstance();
		//get value from backend
		Log.d("notify service","received intent for hour "+cal.get(Calendar.HOUR_OF_DAY)+
				" minute="+cal.get(Calendar.MINUTE));

		if(cal.get(Calendar.MINUTE) == 50)
		{
			hour = cal.get(Calendar.HOUR_OF_DAY)+1;
			Log.d("notify service","received intent for hour "+hour);
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			String saved_uname = prefs.getString("id", "");
			if(saved_uname == "")
			{
				return Service.START_NOT_STICKY;
			}
			else 
			{	
				if(hour>18 && hour<8)
				{
					return Service.START_NOT_STICKY;
				}
				else 
				{
					try
					{
						HttpClient client = new DefaultHttpClient();  
						String folder = getString(R.string.network_folder);
						int day_num = cal.get(Calendar.DAY_OF_WEEK);
			
						//String getURL = folder+"background.php?id="+saved_uname;
						String getURL = folder+"background.php?";
						getURL+= "id="+saved_uname;
						getURL+= "&hour="+hour;
						getURL+= "&day="+day_num;
						//getURL+="&day="+day;
						Log.d("background task","folder="+getURL);
						HttpGet get = new HttpGet(getURL);
				        HttpResponse responseGet = client.execute(get);  
				        HttpEntity resEntityGet = responseGet.getEntity();
				        if (resEntityGet != null) 
				        {  
				                 	response = EntityUtils.toString(resEntityGet);
				                    Log.d("notify response", response);
				                    
				        }
						decodeJSON();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
		
				}
			}
		}
		return Service.START_STICKY;
	}
	public void decodeJSON()
	{
		try{
			
			JSONArray arr = new JSONArray(response);
			JSONObject data = arr.getJSONObject(0);
			int status = data.getInt("status");
			if(status ==1 )
			{
				String course = data.getString("course");
				String room = data.getString("room");
				if(hour<12)
					showNotification(course+" "+hour+" AM, "+room);
				else if(hour>12)
					showNotification(course+" "+(hour-12)+" PM, "+room);
				else
					showNotification(course+" "+hour+" noon, "+room);
				AudioManager audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				audiomanage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			}
			else
			{
				if(hour<12)
					showNotification("No Class at "+hour+" AM");
				else if(hour>12)
					showNotification("No Class at "+(hour-12)+" PM");
				else
					showNotification("No Class at "+hour+" noon");
				AudioManager audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				audiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			}
			
		}
		catch(Exception e)
		{
			Log.d("error","error in decoding json");
			e.printStackTrace();
		}

	}
	@Override
	public IBinder onBind(Intent arg0) 
	{
		return mBinder;
	}
	 public class MyBinder extends Binder 
	 {
		 NotifyService getService() 
		 {
			 return NotifyService.this;
		 }
	 }
	 public void showNotification(String msg)
	 {
	        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	        long when = System.currentTimeMillis();         // notification time
	        Notification notification = new Notification(R.drawable.ic_launcher,msg, when);
	        notification.defaults |= Notification.DEFAULT_SOUND;
	        notification.flags |= Notification.FLAG_AUTO_CANCEL;
	        Intent notificationIntent = new Intent(this, MessOption.class);
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,notificationIntent , 0);
	        
	        notification.setLatestEventInfo(getApplicationContext(), "BITS Now", msg, contentIntent);
	        
	        nm.notify(NOTIF_ID, notification);
	 }
}
