
	package com.oaks.bitsnow;

	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;

	import org.apache.http.HttpEntity;
	import org.apache.http.HttpResponse;
	import org.apache.http.NameValuePair;
	import org.apache.http.client.HttpClient;
	import org.apache.http.client.entity.UrlEncodedFormEntity;
	import org.apache.http.client.methods.HttpPost;
	import org.apache.http.impl.client.DefaultHttpClient;
	import org.apache.http.message.BasicNameValuePair;
	import org.apache.http.util.EntityUtils;
	import org.json.JSONArray;
	import org.json.JSONObject;
	import android.app.Activity;
	import android.content.SharedPreferences;
	import android.graphics.Color;
	import android.os.AsyncTask;
	import android.os.Bundle;
import android.preference.PreferenceManager;
	import android.util.Log;
	import android.view.View;
	import android.view.ViewGroup;
	import android.widget.ListView;
	import android.widget.SimpleAdapter;
	import android.widget.TextView;
import android.widget.Toast;

	public class CoursePage extends Activity{
		String response;
		JSONArray array;
		JSONObject entry;
		String build;
		ListView itemlist;
		TextView course_name, course_id, course_room;
		List<Map<String, String>> data;
		SimpleAdapter adapter;
		HttpResponse responseGet;
		HttpClient client;
		HttpPost post;
		String url;
		String id,day;
		List<NameValuePair> pairs;
		HttpEntity resEntityGet;
		int status=0;
		
		String cname, cid, room;
		

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.coursepage_layout);
			Log.d("chk8","chk8");
			course_name = (TextView)findViewById(R.id.textViewCourseName);
			course_id = (TextView)findViewById(R.id.textViewCourseId);
			course_room = (TextView)findViewById(R.id.textViewRoomNo);
			itemlist = (ListView)findViewById(R.id.listViewCoursepage);

			cname = getIntent().getExtras().getString("cname");
			cid = getIntent().getExtras().getString("cid");
			room = getIntent().getExtras().getString("room");
			course_name.setText(cname);
			course_id.setText(cid);
			course_room.setText(room);
			
			
			
			data = new ArrayList<Map<String, String>>();
		
			new getJSON().execute();
			/*
			Map<String,String> datum = new HashMap<String,String>(2);
			
			datum.put("Name", "John Doe");
			
		    datum.put("Info", "+919000800020");
		    data.add(datum);
		    Log.d("Data",data.toString());
			adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,new String[] {"Name", "Info"},new int[] {android.R.id.text1,android.R.id.text2});
			//itemlist.setAdapter(adapter);
			
	        */
			
			
			

		}
		public class getJSON extends AsyncTask<Void,Void,Boolean>{

			String result_string;
			SimpleAdapter adapt;
			int count=0;
			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				
				
				try
		        {
				    
		            client = new DefaultHttpClient();  
		            url = getString(R.string.network_folder)+"course_detail.php";
			        post = new HttpPost(url);
			        pairs = new ArrayList<NameValuePair>();
			        
			        pairs.add(new BasicNameValuePair("cid",cid));

			        

			        post.setEntity(new UrlEncodedFormEntity(pairs));
			        Log.d("chk","works fine");
			        responseGet = client.execute(post);
			        Log.d("chk9", "chk9");
			        resEntityGet = responseGet.getEntity();  
			        
			        if (resEntityGet != null) 	{
			        	response = EntityUtils.toString(resEntityGet); //got the string equivalent of echo result
			        	Log.d("response", ""+response);
			        
			        	array = new JSONArray(response);
			        	int len = array.length();
			        	Log.d("json Array Length", Integer.toString(len));
			        	for(int i=0;i<len;i++)
			        	{
				        	entry = array.getJSONObject(i);
				        	String tmp_day = entry.getString("day");
				        	
				        	if(tmp_day.equals("1"))
				        	{
				        		tmp_day="Sunday";
				        	}
				        	else if(tmp_day.equals("2"))
				        	{
				        		tmp_day="Monday";
				        	}
			        	
			        		else if(tmp_day.equals("3"))
			        		{
			        			tmp_day="Tuesday";
			        		}
			         		else if(tmp_day.equals("4"))
			        		{
			        		tmp_day="Wednesday";
			        		}
			         		else if(tmp_day.equals("5"))
			        		{
			        		tmp_day="Thursday";
			        		}
			         		else if(tmp_day.equals("6"))
			        		{
			        		tmp_day="Friday";
			        		}
			         		else if(tmp_day.equals("7"))
			        		{
			        		tmp_day="Saturday";
			        		}
				
				        	String tmp_hour = entry.getString("hour");
				        	
				        	if(tmp_hour.equals("1"))
				        	{
				        		tmp_hour="8 AM";
				        	}
				        	else if(tmp_hour.equals("2"))
				        	{
				        		tmp_hour="9 AM";
				        	}
			        	
			        		else if(tmp_hour.equals("3"))
			        		{
			        		tmp_hour="10 AM";
			        		}
			         		else if(tmp_hour.equals("4"))
			        		{
			        		tmp_hour="11 AM";
			        		}
			         		else if(tmp_hour.equals("5"))
			        		{
			        		tmp_hour="12 PM";
			        		}
			         		else if(tmp_hour.equals("6"))
			        		{
			        		tmp_hour="LUNCH";
			        		}
			         		else if(tmp_hour.equals("7"))
			        		{
			        		tmp_hour="2 PM";
			        		}
			         		else if(tmp_hour.equals("8"))
			        		{
			        		tmp_hour="3 PM";
			        		}
			         		else if(tmp_hour.equals("9"))
			        		{
			        		tmp_hour="4 PM";
			        		}
			         		else if(tmp_hour.equals("10"))
			        		{
			        		tmp_hour="5 PM";
			        		}
			         		else
			         		tmp_hour="EXTRA CLASS";
				        	
				        		
				        	//String tmp_room = entry.getString("room");
				        
				        	Log.d("day",tmp_day);
				        	Map<String,String> datum = new HashMap<String,String>(2);
							
				        	String info = "Time: "+tmp_hour;
				        	result_string=info;
							datum.put("Name", tmp_day);
							datum.put("Info", info);
						    data.add(datum);
						    count++;
						    Log.d(tmp_day,data.toString());
						    
			        	}
			        	adapt = new SimpleAdapter(getBaseContext(), data, android.R.layout.simple_list_item_2,
			        			new String[] {"Name", "Info"},
			        			new int[] {android.R.id.text1,android.R.id.text2})
			        	{
							@Override
							public View getView(int position, View convertView, ViewGroup parent) {
					            View view =super.getView(position, convertView, parent);

					            TextView textView=(TextView) view.findViewById(android.R.id.text1);
					            textView.setTextColor(Color.BLACK);
					            
					            TextView textView2=(TextView) view.findViewById(android.R.id.text2);
					            textView2.setTextColor(Color.DKGRAY);
					            return view;
							}
					        
					    };
//			        	itemlist.setAdapter(adapt);
			        }
		
					return true;
		        }
			 catch(Exception e)
			 {
				 Log.d("problem",""+e.getMessage());
				 e.printStackTrace();
				 return false;
			 }
			}

			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(result == true && count>0){
					
					itemlist.setAdapter(adapt);
					//status = 1;
				}
				else{
					Toast.makeText(getApplicationContext(), "Schedule not available", Toast.LENGTH_LONG).show();
					//onBackPressed();
				}
			}
			
			
		}
		
		
	}




