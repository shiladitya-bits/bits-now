
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
import android.content.Intent;
	import android.content.SharedPreferences;
	import android.graphics.Color;
	import android.os.AsyncTask;
	import android.os.Bundle;
import android.preference.PreferenceManager;
	import android.util.Log;
	import android.view.View;
	import android.view.ViewGroup;
import android.widget.AdapterView;
	import android.widget.ListView;
	import android.widget.SimpleAdapter;
	import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

	public class Mycourses extends Activity{
		String response;
		JSONArray array;
		JSONObject entry;
		String build;
		ListView itemlist;
		List<Map<String, String>> data;
		SimpleAdapter adapter;
		HttpResponse responseGet;
		HttpClient client;
		HttpPost post;
		String url;
		String id,day,type;
		List<NameValuePair> pairs;
		HttpEntity resEntityGet;
		int status=0;
		ArrayList<String> cname,cid,room;
		
		

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			cname = new ArrayList<String>();
			cid = new ArrayList<String>();
			room = new ArrayList<String>();
			setContentView(R.layout.my_courses);
			Log.d("chk8","chk8");

			 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			
		   // status = prefs.getString("tempstatus", "");
			// id="2010a4ps286g";
		    id = prefs.getString("id", "");
		  //  day=prefs.getString("day", "");
		    Log.d("ID",id);
		  //  Log.d("DAY",day);
			
			
			itemlist = (ListView) findViewById(R.id.my_list_s);
			
			data = new ArrayList<Map<String, String>>();
		
			new getJSON().execute();
			
			itemlist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
				{
					String clicked_course = cname.get(arg2);
					String clicked_cid = cid.get(arg2);
					String clicked_room = room.get(arg2);

					//String clicked_cname = cname.get(arg2);
					Intent i = new Intent(getApplicationContext(),CoursePage.class);
					i.putExtra("cname", clicked_course);
					i.putExtra("cid", clicked_cid);
					i.putExtra("room", clicked_room);
					startActivity(i);
				}
			});
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
		            url = getString(R.string.network_folder)+"regcourse.php";
			        post = new HttpPost(url);
			        pairs = new ArrayList<NameValuePair>();
			        
			        pairs.add(new BasicNameValuePair("id",id));
			        
			      

			        

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
				        	String tmp_name = entry.getString("course");
				        	String tmp_id = entry.getString("cid");
				        	String tmp_room = entry.getString("room");

				        	Map<String,String> datum = new HashMap<String,String>(2);
							
				        	String info = "";
				        	result_string=info;
				        	
				        	
							datum.put("Name", tmp_name);
							
							cname.add(tmp_name);
							cid.add(tmp_id);
							room.add(tmp_room);
							
							datum.put("Info", info);
						    data.add(datum);
						    count++;
						    Log.d(tmp_name,data.toString());
						    
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
					Toast.makeText(getApplicationContext(), "No registered courses", Toast.LENGTH_LONG).show();
					onBackPressed();
				}
			}
			
			
		}
		
		
	}




