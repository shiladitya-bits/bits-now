package com.oaks.bitsnow;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class FacultyPage extends Activity 
{
	private WebView webView;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facultypage_layout);
		String recv_fid = getIntent().getExtras().getString("fid");
		String recv_url = getIntent().getExtras().getString("url");
		webView = (WebView) findViewById(R.id.webView1);
		webView.setWebViewClient(new MyWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(recv_url);
		
		
		Toast.makeText(getApplicationContext(), recv_url, Toast.LENGTH_SHORT).show();
		
	}
	private class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	       // if (Uri.parse(url).getHost().equals("www.example.com")) {
	            // This is my web site, so do not override; let my WebView load the page
	            return false;
	        //}
	        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
	        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	        //startActivity(intent);
	        //return true;
	    }
	}
}
