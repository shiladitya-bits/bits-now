package com.oaks.bitsnow;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyScheduleReceiver extends BroadcastReceiver {
	// this class is required for only boot time
	// Restart service every hour
	public static final long REPEAT_TIME = 60*1000;
	@Override
	public void onReceive(Context context, Intent intent) 
	{
	    AlarmManager service = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    Intent i = new Intent(context, MyStartServiceReceiver.class);
	    PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
	    		PendingIntent.FLAG_CANCEL_CURRENT);
	    Calendar cal = Calendar.getInstance();
	    // Start 30 seconds after boot completed
	    //cal.add(Calendar.MINUTE, 1);
	    //cal.set(Calendar.SECOND, 0);
		
	    Log.d("calling NotifyService","Hour = "+cal.get(Calendar.HOUR)+
				" Minute="+cal.get(Calendar.MINUTE));
	    // Fetch every 30 seconds
	    // InexactRepeating allows Android to optimize the energy consumption
	    service.setRepeating(AlarmManager.RTC_WAKEUP,
	        cal.getTimeInMillis(), REPEAT_TIME, pending);

	    // service.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
	    // REPEAT_TIME, pending);
	}

}
