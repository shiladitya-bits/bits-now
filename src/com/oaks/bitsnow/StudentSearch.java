package com.oaks.bitsnow;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StudentSearch extends Activity {

	String s_name,s_id,s_hostel,s_room;
	EditText name, id, hostel, room;
	Button search_press;
	static String sharedData2="MySharedData";
	SharedPreferences shared2;
	
	public static Boolean isStringEmpty(String input){
		if(input.trim().length()>0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchstudent_layout);
		name = (EditText)findViewById(R.id.search_name_2);
		id = (EditText)findViewById(R.id.search_id_2);
		hostel = (EditText)findViewById(R.id.search_hostel_2);
		room = (EditText)findViewById(R.id.search_room_2);
		search_press = (Button)findViewById(R.id.search_button_2);
		final SharedPreferences someData2 = getSharedPreferences(sharedData2,0);
		
		
		search_press.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				s_name=name.getText().toString();
				s_id=id.getText().toString();
				s_hostel=hostel.getText().toString();
				s_room=room.getText().toString();
				
				SharedPreferences.Editor editor = someData2.edit();
				
				if(isStringEmpty(s_name)&& isStringEmpty(s_id) && isStringEmpty(s_hostel) && isStringEmpty(s_room)){
					Toast.makeText(getApplicationContext(), "Enter atleast 1 parameter", Toast.LENGTH_SHORT).show();
					Log.d("chk2", "chk2");
				}
				else{
					
					editor.putString("name", s_name);
					editor.putString("id", s_id);
					editor.putString("hostel", s_hostel);
					editor.putString("room", s_room);
					editor.commit();
					Intent ourIntent;
					Log.d("chk3", "ok");
					try {
						ourIntent = new Intent(getBaseContext(),SearchResults.class);
						startActivity(ourIntent);
						//Log.d("chk4", "chk4");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.d("chk5", "error");
					}
				}
			}
		});
	}

	

}