package com.oaks.bitsnow;



import java.util.ArrayList;

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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MessOption extends Activity
{
	
	String status,id,messop,response;
	Button submit;
	JSONArray array;
	JSONObject obj;
	HttpClient httpclient;
	String folder;
	String postURL;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		
		 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	    
	   // status = prefs.getString("tempstatus", "");
	    id = prefs.getString("id", "");
	    Log.d("ID",id);
		setContentView(R.layout.messoption_layout);
		View rg = (View)findViewById(R.id.radioGroupMess);
		View btn = (Button)findViewById(R.id.buttonMess);
		rg.setVisibility(4);
		btn.setVisibility(4);
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		nameValuePairs.add(new BasicNameValuePair("id", id));
		
		
	    httpclient = new DefaultHttpClient();
		folder = getString(R.string.network_folder);
		postURL = folder+"status.php";
		//Log.d("loginPage","login folder="+postURL);
		try
		{
		HttpPost httppost = new HttpPost(postURL);
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse httpResponse = httpclient.execute(httppost);
		//Toast.makeText(getApplicationContext(),"Selected "+messop+" mess successfully!", Toast.LENGTH_SHORT).show();
		HttpEntity resEntityGet = httpResponse.getEntity(); 
		
		if (resEntityGet != null) 
        {  
			response = EntityUtils.toString(resEntityGet);
            Log.d("login response", response);
            array = new JSONArray(response);
            Log.d("array",array.toString());
           // Log.d("login response", response);
			int arrlen = array.length();
			Log.d("Array Length", Integer.toString(arrlen));
			/*
			for(int i=0;i<arrlen;i++)
			{
				obj = array.getJSONObject(i);
				status = obj.getString("status");
				Log.d("newstatus",status);
			}*/
            obj = array.getJSONObject(0);
			status = obj.getString("status");
			if(status.equals("0"))
			{
				rg.setVisibility(0);
				btn.setVisibility(0);
			}
			else if(status.equals("1"))
			{
				Toast.makeText(this,"Mess option filled!" , Toast.LENGTH_SHORT).show();
				finish();
			}
			else
			{
				Toast.makeText(this,"Mess option closed!" , Toast.LENGTH_SHORT).show();
				finish();
			}
			Log.d("newstatus",status);
        }
		//onBackPressed();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Toast.makeText(getApplicationContext(), id+status, Toast.LENGTH_SHORT).show();
		submit = (Button)findViewById(R.id.buttonMess);
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				
				
				
				
				RadioGroup g = (RadioGroup) findViewById(R.id.radioGroupMess); 
				int selected = g.getCheckedRadioButtonId();
				RadioButton b = (RadioButton) findViewById(selected);
				
				if("A mess".equals((String) b.getText()))
				{
					messop="A";
				}
				else
					messop="C";
						
				
				if(status.equals("0")){
					ArrayList<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>();
					
					nameValuePairs2.add(new BasicNameValuePair("messop", messop));
					nameValuePairs2.add(new BasicNameValuePair("id", id));
					
					
					httpclient = new DefaultHttpClient();
					folder = getString(R.string.network_folder);
					postURL = folder+"mess.php";
					//Log.d("loginPage","login folder="+postURL);
					try{
					HttpPost httppost = new HttpPost(postURL);
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
					HttpResponse httpResponse = httpclient.execute(httppost);
					Toast.makeText(getApplicationContext(),"Selected "+messop+" mess successfully!", Toast.LENGTH_SHORT).show();
					finish();
					HttpEntity resEntityGet = httpResponse.getEntity(); 
					//onBackPressed();
					}catch(Exception e){
						
					}
				}
				else if(status.equals("1")){
					Toast.makeText(getApplicationContext(),"Mess option already filled!", Toast.LENGTH_SHORT).show();

				}
				else{
					Toast.makeText(getApplicationContext(),"Mess option closed", Toast.LENGTH_SHORT).show();

				}
			}
		});
		
	}	
	
}
