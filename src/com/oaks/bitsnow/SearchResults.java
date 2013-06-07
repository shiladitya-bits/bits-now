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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResults extends Activity{
	String response;
	JSONArray array;
	JSONObject entry;
	String build;
	String s_name, s_id, s_hostel, s_room;
	ListView itemlist;
	List<Map<String, String>> data;
	SimpleAdapter adapter;
	HttpResponse responseGet;
	HttpClient client;
	HttpPost post;
	String url;
	List<NameValuePair> pairs;
	HttpEntity resEntityGet;
	int status=0;
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.searchresults_layout);
		Log.d("chk8","chk8");
		
		SharedPreferences shared = getSharedPreferences(StudentSearch.sharedData2,0);
		//SharedPreferences shared = getSharedPreferences(,0);
		s_name = shared.getString("name", "");
		s_id = shared.getString("id", "");
		s_hostel = shared.getString("hostel", "");
		s_room = shared.getString("room", "");
		
		Log.d("sname",s_name);
		itemlist = (ListView) findViewById(R.id.listview_searchresults);
		
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
	            url = getString(R.string.network_folder)+"studentsearch.php";
		        post = new HttpPost(url);
		        pairs = new ArrayList<NameValuePair>();
		        if(s_name!=""){
		        	pairs.add(new BasicNameValuePair("name", s_name));
		        }
		        if(s_id!=""){
		        	pairs.add(new BasicNameValuePair("id", s_id));
		        }
		        if(s_hostel!=""){
		        	pairs.add(new BasicNameValuePair("hostel", s_hostel));
		        }
		        if(s_room!=""){
		        	pairs.add(new BasicNameValuePair("room", s_room));
		        }
		        

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
			        	String tmp_name = entry.getString("name");
			        	String tmp_id = entry.getString("id");
			        	String tmp_hostel = entry.getString("hostel");
			        	String tmp_room = entry.getString("room");
			        	String tmp_contact = entry.getString("contact");
			        	Log.d("name",tmp_name);
			        	Map<String,String> datum = new HashMap<String,String>(2);
						
			        	String info = "Contact: "+tmp_contact+"\n"+"Hostel: "+tmp_hostel+"-"+tmp_room+"\n"+"ID: "+tmp_id;
			        	result_string=info;
						datum.put("Name", tmp_name);
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
//		        	itemlist.setAdapter(adapt);
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
				Toast.makeText(getApplicationContext(), "Found "+count+" "+"Result(s)", Toast.LENGTH_SHORT).show();
				itemlist.setAdapter(adapt);
				//status = 1;
			}
			else{
				Toast.makeText(getApplicationContext(), "No match found", Toast.LENGTH_LONG).show();
				onBackPressed();
			}
		}
		
		
	}
	
	
}

