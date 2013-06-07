package com.oaks.bitsnow;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StudentHome extends Activity {
	String uname,status,id,profile,name;
	Button view_courses, view_faculty, my_courses, mess_option,stud_search,sched; 
	private NotifyService s;
	private static final long REPEAT_TIME = MyScheduleReceiver.REPEAT_TIME;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_main);
    	
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	uname = prefs.getString("user", "");
    	status = prefs.getString("tempstatus", "");
    	id = prefs.getString("id", "");
    	profile= prefs.getString("profile","");
    	name = prefs.getString("name","");
		boolean test = prefs.getBoolean("once_set",false);
		if(!test)
		{
			startAlarm();
		}
    	TextView tvname = (TextView)findViewById(R.id.tvName);
    	tvname.setText("Welcome!\n"+name);	
    	
    	view_courses = (Button)findViewById(R.id.buttonCourseList);
    	view_faculty = (Button)findViewById(R.id.buttonFacultyList);
    	my_courses = (Button)findViewById(R.id.buttonMyCourses);
    	mess_option = (Button)findViewById(R.id.buttonMessOption);
    	stud_search = (Button)findViewById(R.id.buttonStudSearch);
       	sched= (Button)findViewById(R.id.schedule);
    	view_courses.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				show_depts("course");
			}
		});
    	
    	view_faculty.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				show_depts("faculty");
			}
		});
    	
    	my_courses.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putString("id",id);
			    editor.commit();
				startActivity(new Intent(getApplicationContext(),Mycourses.class));
			}
		});
       	mess_option.setOnClickListener(new View.OnClickListener() {
			
    			@Override
    			public void onClick(View v) {
    				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    			    SharedPreferences.Editor editor = prefs.edit();
    			   
    			    editor.putString("tempstatus",status);
    			    editor.putString("id",id);
    			    editor.commit();
    				startActivity(new Intent(getApplicationContext(),MessOption.class));
    				//finish();
    				
    			}
    		});
    	stud_search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),StudentSearch.class));
			}
		});
    	sched.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				Integer day = (calendar.get(Calendar.DAY_OF_WEEK)); 
				
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			    SharedPreferences.Editor editor = prefs.edit();
			    day = 2;
			    editor.putString("day",day.toString());
			    editor.putString("type","s");
			    editor.putString("id",id);
			    editor.commit();
				startActivity(new Intent(getApplicationContext(),Schedule.class));
			}
		});
    }

 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        
        return true;
    }
 
    @Override
	public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
    	case R.id.menu_logout:
    		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    		SharedPreferences.Editor editor = prefs.edit();
    		editor.clear();
    		editor.commit();
    		startActivity(new Intent(StudentHome.this,LoginPage.class));
    		finish();
    	}
		return super.onOptionsItemSelected(item);
	}


	public void show_depts(String type)
    {
    	Intent i = new Intent(StudentHome.this,DeptList.class);
    	i.putExtra("type", type);
    	startActivity(i);
    }
    
    public void show_mycourses()
    {
    	
    }
	 private void startAlarm() 
	 {
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.SECOND, 30);
			Log.d("calling NotifyService","Hour = "+cal.get(Calendar.HOUR)+
					" Minute="+cal.get(Calendar.MINUTE));
	
			AlarmManager service = (AlarmManager)getApplicationContext().
					getSystemService(Context.ALARM_SERVICE);
			
			Intent i = new Intent(getApplicationContext(),MyStartServiceReceiver.class);
			
			PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 
					0, i, PendingIntent.FLAG_CANCEL_CURRENT);
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		    SharedPreferences.Editor editor = prefs.edit();
		    editor.putBoolean("once_set",true);
		    editor.commit();
			service.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 
					REPEAT_TIME, pending);
			
	 }
	 private ServiceConnection mConnection = new ServiceConnection() {
		 	@Override
		    public void onServiceConnected(ComponentName className, IBinder binder) 
		 	{
		      s = ((NotifyService.MyBinder) binder).getService();
		      //Toast.makeText(HomeActivity.this, "Connected", Toast.LENGTH_SHORT).show();
		    }
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				// TODO Auto-generated method stub
				
			}
	 };
	  void doBindService() 
	  {
		    bindService(new Intent(this, NotifyService.class), mConnection,
		        Context.BIND_AUTO_CREATE);
	  }
}
