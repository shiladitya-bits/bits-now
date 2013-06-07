package com.oaks.bitsnow;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends Activity
{
	JSONArray array;
	JSONObject obj;

	EditText uname,pass;
	Button login;
	String send_user,send_pass,response;
	TextView test,test2;
	RadioGroup g;
	int selected ;
	RadioButton b;
	RadioButton b1,b2;
	String p;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	
		String saved_uname = prefs.getString("user", "");
		test=(TextView)findViewById(R.id.testViewText);
		test2=(TextView)findViewById(R.id.testViewLogin);
		b1 = (RadioButton)findViewById(R.id.radioButton1);
		b2 = (RadioButton)findViewById(R.id.radioButton2);
		
		if(saved_uname != "")
		{
			String saved_type = prefs.getString("profile", "s");
			if(saved_type.equals("s"))
			{
				startActivity(new Intent(getApplicationContext(),StudentHome.class));
					
			}
			else 
			{
				startActivity(new Intent(getApplicationContext(),FacultyHome.class));
				
			}
			finish();
		}
		setupViews();
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				
				send_user = uname.getText().toString();
				send_pass = pass.getText().toString();
				if(send_user.equals("") || send_pass.equals(""))
				{
					test2.setText("0");
					Toast.makeText(LoginPage.this, "Enter all details.", Toast.LENGTH_SHORT).show();

					return;
				}
				if(!b1.isChecked() && !b2.isChecked())
				{
					test2.setText("0");	
					Toast.makeText(LoginPage.this, "Select radio button", Toast.LENGTH_SHORT).show();
					return;
					
				}
				test2.setText("1");
				g = (RadioGroup) findViewById(R.id.radioGroupLogin); 
				selected = g.getCheckedRadioButtonId();
				b = (RadioButton) findViewById(selected);
				p=(String) b.getText();
				
				AuthenticateUser task = new AuthenticateUser();
				task.execute();
			}
		});
	}
	/**
	 * @param none
	 * @return none
	 * This function sets up the initial links for the views with the code
	 */
	public void setupViews()
	{
		uname = (EditText)findViewById(R.id.etUsername);
		pass = (EditText)findViewById(R.id.etPassword);
		login = (Button)findViewById(R.id.buttonLogin);
		//forgot = (Button)findViewById(R.id.buttonForgot);
		
	}

	public void forgotPassword()
	{
		//dialog box to get id, then send as mail
		Toast.makeText(getApplicationContext(), "Password sent to mail", Toast.LENGTH_SHORT).show();
	}

	private class AuthenticateUser extends AsyncTask<String,Void,Boolean>
	{
		String tmp_status,tmp_id,tmp_name,tmp_room,tmp_contact;
		String tmp_auth="0";
	
		@Override
		protected Boolean doInBackground(String... params) 
		{
			return authenticateUser();
		}
		@Override
		protected void onPostExecute(Boolean result)
		{
			if(result == true)
			{
				Toast.makeText(getApplicationContext(), "User authenticated", Toast.LENGTH_SHORT).show();
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putString("user",send_user);
			    editor.putString("tempstatus", tmp_status);
			    editor.putString("name", tmp_name);
			    editor.putString("id", tmp_id);
			    
				if("Student".equals((String) b.getText()))
				{
					editor.putString("profile","s");
					editor.commit();
					//startActivity(new Intent(getApplicationContext(),StudentHome.class));
					startActivity(new Intent(getApplicationContext(),MainActivity.class));
					finish();
				}
				else
				{
					editor.putString("profile","f");
					editor.commit();
					//startActivity(new Intent(getApplicationContext(),FacultyHome.class));
					startActivity(new Intent(getApplicationContext(),MainActivity.class));
					finish();
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Incorrect username or password!", Toast.LENGTH_SHORT).show();
			}
		}

		public boolean authenticateUser() 
		{
			//send_user & send_pass --> HTTP request using AsyncTask
			
			try
			{
				
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			    SharedPreferences.Editor editor = prefs.edit();
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("user", send_user));
				nameValuePairs.add(new BasicNameValuePair("pwd", send_pass));
				nameValuePairs.add(new BasicNameValuePair("type",p));
				Log.d("loginPage","user="+send_user);
				Log.d("loginPage","password="+send_pass);
				Log.d("loginPage","type="+p);
				
				HttpClient httpclient = new DefaultHttpClient();
				String folder = getString(R.string.network_folder);
				String postURL = folder+"authenticate.php";
				Log.d("loginPage","login folder="+postURL);
				
				HttpPost httppost = new HttpPost(postURL);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse httpResponse = httpclient.execute(httppost);
				HttpEntity resEntityGet = httpResponse.getEntity();  
				
				if (resEntityGet != null) 
		        {  
					response = EntityUtils.toString(resEntityGet);
		            Log.d("login response", response);
		            array = new JSONArray(response);
		            Log.d("Array",array.toString());
					int arrlen = array.length();
					Log.d("Array Length", Integer.toString(arrlen));
					
					for(int i=0;i<arrlen;i++)
					{
						obj = array.getJSONObject(i);
						tmp_status = obj.getString("status");
						tmp_id = obj.getString("id");
						tmp_auth = obj.getString("auth");
						tmp_name = obj.getString("name");
						//tmp_room = obj.getString("room");
						tmp_contact = obj.getString("contact");
						
						test.setText(tmp_auth);
						editor.putString("userid",tmp_id);
						editor.putString("username", tmp_id);
						//editor.putString("hostel", tmp_hostel);
						//editor.putString("room", tmp_room);
						editor.putString("contact", tmp_contact);
						editor.putString("name", tmp_name);
						editor.commit();
						Log.d("temp status",tmp_status);
						Log.d("temp auth",tmp_auth);
					}
					if(tmp_auth.equals("1")){
						
						Log.d("Check","Authenticated!");
						return true;
					}
					else{
						return false;
					}
  
		        }
				else return false;
			}
			catch(Exception e)
			{
				Log.d("LoginPage error",e.getMessage());
				return false;
			}
		}
		
	}
}
