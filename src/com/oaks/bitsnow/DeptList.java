package com.oaks.bitsnow;

import java.util.ArrayList;

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
import android.widget.ListView;
import android.widget.TextView;

public class DeptList extends Activity
{
	ArrayList<String> dept_list;
	ArrayList<String> did;
	String response;
	JSONArray array;
	JSONObject entry;
	ListView lv;
	ArrayAdapter<String> adapter;
	String type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deptlist_layout);
		type = getIntent().getExtras().getString("type").toString();
		Log.d("dept list type",type);
		lv = (ListView)findViewById(R.id.listview_dept);
		dept_list = new ArrayList<String>();
		did = new ArrayList<String>();
		getDeptList task = new getDeptList();  //asynctask
		task.execute();
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			{
				String dep_id = did.get(arg2);
				String dep_name = dept_list.get(arg2);
				if(type.equals("course"))
				{
					Intent i = new Intent(getApplicationContext(),CourseList.class);
					i.putExtra("did", dep_id);
					i.putExtra("dname", dep_name);
					Log.d("send to courses",dep_id);
					Log.d("send to courses",dep_name);
					startActivity(i);
				}
				else if(type.equals("faculty"))
				{
					Intent i = new Intent(getApplicationContext(),FacultyList.class);
					i.putExtra("did", dep_id);
					i.putExtra("dname", dep_name);

					Log.d("send to faculty",dep_id);
					Log.d("send to faculty",dep_name);
					startActivity(i);
				}
					
			}
		});
	}
	public class getDeptList extends AsyncTask<String,Void, String>
	{
		
		@Override
		protected String doInBackground(String... params) {
			try
			{
				HttpClient client = new DefaultHttpClient();  
				String folder = getString(R.string.network_folder);
				String getURL = folder+"deptlist.php";
				Log.d("dept list","login folder="+getURL);
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
					entry = array.getJSONObject(i);
					String tmp_did = entry.getString("did");    
					String tmp_dname = entry.getString("dname");
					// @kaustav: add the above two
					
					dept_list.add(tmp_dname);
					did.add(tmp_did);
					
					Log.d("did",tmp_did);
					Log.d("dname",tmp_dname);
				}
				
			}
			catch(Exception e)
			{
				Log.d("problem in dept list",e.getMessage());
			}
	        return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,dept_list)
			{
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
		            View view =super.getView(position, convertView, parent);

		            TextView textView=(TextView) view.findViewById(android.R.id.text1);
		            textView.setTextColor(Color.DKGRAY);
		            
		            //TextView textView2=(TextView) view.findViewById(android.R.id.text2);
		            //textView2.setTextColor(Color.DKGRAY);
		            return view;
				}
		        
		    }		;
			lv.setAdapter(adapter);
		}
		

	}
	
	

}
