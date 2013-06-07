package com.oaks.bitsnow;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CourseList extends Activity
{
	ArrayList<String> cname;
	ArrayList<String> cid,room;
	String did, dname, response;
	JSONArray array;
	JSONObject obj;
	ArrayAdapter<String> adapter,adap_autocomplete;
	AutoCompleteTextView atv;
	ListView lv;
	ArrayList<HashMap<String,String>> map_list,map_backup;
	SimpleAdapter map_adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.courselist_layout);
		
		did = getIntent().getExtras().getString("did");
		dname = getIntent().getExtras().getString("dname");
		Log.d("dep id",did);
		Log.d("dep name",dname);
		cname = new ArrayList<String>();
		cid = new ArrayList<String>();
		room = new ArrayList<String>();
		map_list = new ArrayList<HashMap<String,String>>();
		//map_backup = new ArrayList<HashMap<String,String>>();  //new
		
		TextView tv = (TextView)findViewById(R.id.textViewCourses);
		tv.setText(dname+": Courses");
		atv = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewCourses);
		atv.setThreshold(1);
		lv = (ListView)findViewById(R.id.listViewCourses);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			{
				String clicked_cid = cid.get(arg2);
				String clicked_cname = cname.get(arg2);
				String clicked_room = room.get(arg2);
				//String clicked_cname = cname.get(arg2);
				Intent i = new Intent(getApplicationContext(),CoursePage.class);
				i.putExtra("cid", clicked_cid);
				i.putExtra("cname", clicked_cname);
				i.putExtra("room", clicked_room);
				startActivity(i);
			}
		});
		GetCourseList task = new GetCourseList();
		task.execute();
		
		/*
		HashMap<String,String> mmp = new HashMap<String, String>();
		mmp.put("name", "gandu khan");
		mmp.put("id","id11");
		map_backup.add(mmp);
		map_adapter = new SimpleAdapter(getApplicationContext(),map_backup,android.R.layout.simple_list_item_2,
				new String[]{"name","id"},new int[]{android.R.id.text1,android.R.id.text2})
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
		//set to the above adapter later
		lv.setAdapter(map_adapter);
		*/
		


		
	}
	private class GetCourseList extends AsyncTask<String,Void, String>
	{

		@Override
		protected String doInBackground(String... arg0) 
		{
			
			try
			{
				HttpClient client = new DefaultHttpClient();  
				String folder = getString(R.string.network_folder);
				String getURL = folder+"courselist.php?did="+did;
				Log.d("course list","login folder="+getURL);
				HttpGet get = new HttpGet(getURL);
		        HttpResponse responseGet = client.execute(get);  
		        HttpEntity resEntityGet = responseGet.getEntity();
		        if (resEntityGet != null) 
		        {  
		                 	response = EntityUtils.toString(resEntityGet);
		                    Log.d("response", response);
		        }
				array = new JSONArray(response);
				int arrlen = array.length();
				Log.d("Array Length", Integer.toString(arrlen));
				for(int i=0;i<arrlen;i++)
				{
					obj = array.getJSONObject(i);
					String tmp_cid = obj.getString("cid");
					String tmp_cname = obj.getString("cname");
					String tmp_room = obj.getString("room");
					
					cid.add(tmp_cid);
					cname.add(tmp_cname);
					room.add(tmp_room);
					HashMap<String,String> tmp_map = new HashMap<String,String>();
					tmp_map.put("id", tmp_cid);
					tmp_map.put("name", tmp_cname);
					map_list.add(tmp_map);
					
					Log.d("temp cid",tmp_cid);
					Log.d("temp cname",tmp_cname);
				}
				
			}
			catch(Exception e)
			{
				Log.d("get course list problem",e.getMessage());
				e.printStackTrace();
			}
			return "done";
		}
		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);
			adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,cname);
			adap_autocomplete = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line,cname);
			map_adapter = new SimpleAdapter(getApplicationContext(),map_list,android.R.layout.simple_list_item_2,
					new String[]{"name","id"},new int[]{android.R.id.text1,android.R.id.text2})
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
			//set to the above adapter later
			lv.setAdapter(map_adapter);
			atv.setAdapter(adap_autocomplete);
		}
	}
}