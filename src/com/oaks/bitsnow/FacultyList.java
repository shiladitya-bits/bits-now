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
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FacultyList extends Activity
{
	ArrayList<String> fname;
	ArrayList<String> fid;
	ArrayList<String> url;
	ArrayList<String> fcontact;
	
	String did, dname, response;
	JSONArray array;
	JSONObject obj;
	ArrayAdapter<String> adapter,adap_autocomplete;
	SimpleAdapter map_adapter,map_newadapter;
	AutoCompleteTextView atv;
	ListView lv;
	ArrayList<HashMap<String,String>> map_list;
	
	ArrayList<HashMap<String,Object>> map_newlist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facultylist_layout);
	    
		did = getIntent().getExtras().getString("did");
		dname = getIntent().getExtras().getString("dname");
		Log.d("dep id",did);
		Log.d("dep name",dname);
		fname = new ArrayList<String>();
		fid = new ArrayList<String>();
		url = new ArrayList<String>();
		fcontact = new ArrayList<String>();
		map_list = new ArrayList<HashMap<String,String>>();
		map_newlist = new ArrayList<HashMap<String,Object>>();
		atv = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewFaculty);
		atv.setThreshold(1);
		lv = (ListView)findViewById(R.id.listViewFaculty);
		//call async task
		GetFacultyList task = new GetFacultyList();
		task.execute();
		
		TextView tv = (TextView)findViewById(R.id.textViewFaculty);
		tv.setText(dname+": Faculty");
		/*
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String clicked_fid = fid.get(arg2);
				String clicked_url = url.get(arg2);
				
				return false;
			}
		});
		*/
		registerForContextMenu(lv);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			{
				String clicked_fid = fid.get(arg2);
				String clicked_url = url.get(arg2);
				//String clicked_cname = cname.get(arg2);
				Intent i = new Intent(getApplicationContext(),FacultyPage.class);
				i.putExtra("fid", clicked_fid);
				i.putExtra("url", clicked_url);
				startActivity(i);
			}
		});
	}
	private class GetFacultyList extends AsyncTask<String,Void, String>
	{

		@Override
		protected String doInBackground(String... arg0) 
		{
			
			try
			{
				HttpClient client = new DefaultHttpClient();  
				String folder = getString(R.string.network_folder);
				String getURL = folder+"facultylist.php?did="+did;
				Log.d("faculty list","login folder="+getURL);
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
					String tmp_fid = obj.getString("tid");
					String tmp_fname = obj.getString("name");
					String tmp_url = obj.getString("url");
					String tmp_status = obj.getString("status");
					String tmp_chamber = obj.getString("chamber");
					String tmp_contact = obj.getString("contact");
					Log.d("URL",tmp_url);
					fid.add(tmp_fid);
					url.add(tmp_url);
					fname.add(tmp_fname);
					fcontact.add(tmp_contact);
					HashMap<String,String> tmp_map = new HashMap<String,String>();
					tmp_map.put("id", tmp_fid);
					tmp_map.put("name", tmp_fname);
					map_list.add(tmp_map);
					
					HashMap<String,Object> tmp_newmap = new HashMap<String,Object>();
					tmp_newmap.put("name", tmp_fname);
					tmp_newmap.put("id", tmp_fid);
					tmp_newmap.put("chamber", tmp_chamber);
					if(tmp_status.equals("1"))
					{
						tmp_newmap.put("image",R.drawable.available );
							
					}
					else
					{
						tmp_newmap.put("image",R.drawable.away );						
					}
					map_newlist.add(tmp_newmap);
					Log.d("temp fid",tmp_fid);
					Log.d("temp fname",tmp_fname);
				}
				
			}
			catch(Exception e)
			{
				Log.d("get faculty list problem",e.getMessage());
				e.printStackTrace();
			}
			
			return "done";
		}
		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);
			adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,fname);
			adap_autocomplete = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line,fname);
			
			/*
			 * map_adapter = new SimpleAdapter(getApplicationContext(),map_list,
					android.R.layout.simple_list_item_2,new String[]{"name","id"},
					new int[]{android.R.id.text1,android.R.id.text2})
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
		  */
		    map_newadapter = new SimpleAdapter(FacultyList.this,map_newlist,R.layout.facultylistrow,
		    		new String[]{"name","chamber","image"}, 
		    		new int[]{R.id.tvNameFac,R.id.tvChamberFac,R.id.imageViewFac}) ;
		    
			//set to the above adapter later
			lv.setAdapter(map_newadapter);
			
			atv.setAdapter(adap_autocomplete); //change input of autocomplete adapter later
		}
		
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		  int menuItemIndex = item.getItemId();
		  if(menuItemIndex == 0)
		  {
			  Intent callIntent = new Intent(Intent.ACTION_CALL);
			  callIntent.setData(Uri.parse("tel:"+fcontact.get(info.position)));
			  startActivity(callIntent);	 	 
		  }
		  else if(menuItemIndex == 1)
		  {
			  Toast.makeText(FacultyList.this, "Feature not implemented", Toast.LENGTH_SHORT).show();
		  }
		  else
		  {
				String clicked_fid = fid.get(info.position);
				String clicked_url = url.get(info.position);
				//String clicked_cname = cname.get(arg2);
				Intent i = new Intent(getApplicationContext(),FacultyPage.class);
				i.putExtra("fid", clicked_fid);
				i.putExtra("url", clicked_url);
				startActivity(i);
		  }
		  return true;
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		  if (v.getId()==R.id.listViewFaculty) {
			    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
			    menu.setHeaderTitle(fname.get(info.position));
			    String[] menuItems = {"Call","Email","See Profile"};
			    for (int i = 0; i<menuItems.length; i++) {
			      menu.add(Menu.NONE, i, i, menuItems[i]);
			    }
			  }
	}
   
}
