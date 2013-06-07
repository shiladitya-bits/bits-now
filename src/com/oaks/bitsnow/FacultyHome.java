package com.oaks.bitsnow;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class FacultyHome extends Activity
{
	String uname,status,id,profile,name;
	CheckBox chkbox;
	HttpClient httpclient;
	String folder;
	String postURL,op;
	Button course_list, faculty_list, my_courses, student_search,schedule;
	private static final long REPEAT_TIME = MyScheduleReceiver.REPEAT_TIME;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faculty_main);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	uname = prefs.getString("user", "");
    	name = prefs.getString("name", "");
    	status = prefs.getString("tempstatus", "");
    	id = prefs.getString("id", "");
    	profile= prefs.getString("profile","");
    	
    	//Toast.makeText(getApplicationContext(), "Status :"+status, Toast.LENGTH_LONG).show();
		TextView tvname = (TextView)findViewById(R.id.tvName);
		
    	tvname.setText("Welcome\n"+name+" !");	
 
		boolean test = prefs.getBoolean("once set",false);
		if(!test)
		{
			startAlarm();
		}
		schedule = (Button)findViewById(R.id.buttonToday);
		schedule.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				Integer day = (calendar.get(Calendar.DAY_OF_WEEK)); 
				day=2;//for testing
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			    SharedPreferences.Editor editor = prefs.edit();
			   
			    editor.putString("day",day.toString());
			    editor.putString("id",id);
			    editor.putString("type", "t");
			    editor.commit();
				startActivity(new Intent(getApplicationContext(),Schedule.class));
			}
		});
		
		course_list = (Button)findViewById(R.id.buttonCourseList);
		course_list.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				show_depts("course");
			}
		});
		faculty_list = (Button)findViewById(R.id.buttonFacultyList);
		faculty_list.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				show_depts("faculty");
			}
		});
		
		student_search = (Button)findViewById(R.id.buttonStudSearchFac);
		student_search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),StudentSearch.class));
			}
		});
		
		
    	my_courses = (Button)findViewById(R.id.buttonMyCourses);
		my_courses.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putString("id",id);
			    editor.commit();
				startActivity(new Intent(getApplicationContext(),MyCoursesFaculty.class));
			}
		});
		
		chkbox = (CheckBox)findViewById(R.id.checkBox1);
		if(status.equals("1")){
			chkbox.setChecked(true);
		}else{
			chkbox.setChecked(false);
		}
		chkbox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(chkbox.isChecked()){
					Toast.makeText(getApplicationContext(), "Status set to Available", Toast.LENGTH_LONG).show();
					op="Available";
				}else{
					Toast.makeText(getApplicationContext(), "Status set to Away", Toast.LENGTH_LONG).show();
					op="Away";
				}
				ArrayList<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>();
				
				nameValuePairs2.add(new BasicNameValuePair("tid", id));
				nameValuePairs2.add(new BasicNameValuePair("op", op));
				
				
				httpclient = new DefaultHttpClient();
				folder = getString(R.string.network_folder);
				postURL = folder+"setchamber.php";
				//Log.d("loginPage","login folder="+postURL);
				try{
				HttpPost httppost = new HttpPost(postURL);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
				HttpResponse httpResponse = httpclient.execute(httppost);
				//Toast.makeText(getApplicationContext(),"Selected "+messop+" mess successfully!", Toast.LENGTH_SHORT).show();
				//finish();
				HttpEntity resEntityGet = httpResponse.getEntity(); 
				//onBackPressed();
				}catch(Exception e){
					Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
				}
					
				
			}
		});
	}
	 private void startAlarm() 
	 {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, 30);
			Log.d("calling NotifyService","Hour = "+cal.get(Calendar.HOUR)+
					" Minute="+cal.get(Calendar.MINUTE));
	
			AlarmManager service = (AlarmManager)getApplicationContext().
					getSystemService(Context.ALARM_SERVICE);
			
			Intent i = new Intent(getApplicationContext(),MyStartServiceReceiver.class);
			
			PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 
					0, i, PendingIntent.FLAG_CANCEL_CURRENT);
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		    SharedPreferences.Editor editor = prefs.edit();
		    editor.putBoolean("once set",true);
		    editor.commit();
			service.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 
					REPEAT_TIME, pending);
			
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
	    		startActivity(new Intent(FacultyHome.this,LoginPage.class));
	    		finish();
	    	}
			return super.onOptionsItemSelected(item);
		}
	    
	    public void show_depts(String type)
	    {
	    	Intent i = new Intent(FacultyHome.this,DeptList.class);
	    	i.putExtra("type", type);
	    	startActivity(i);
	    }
	    
	    
}
