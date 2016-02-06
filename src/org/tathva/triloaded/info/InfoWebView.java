package org.tathva.triloaded.info;

import org.tathva.triloaded.mainmenu.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

public class InfoWebView extends Activity{
	
	public static final int DEVELOPERS = 1;
	public static final int ABOUT_TATHVA = 2;
	public static final int SPONSERS = 3;
	public static final int ABOUT_NITC = 4;
	public static final int NITES = 5;
	
	public static final String KEY ="key";
	
	 @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
	    setContentView(R.layout.webview_layout);
	    
	    Bundle info =  getIntent().getExtras();
	    int type = info.getInt(KEY);
	    

	    WebView browser = (WebView) findViewById(R.id.webview);
        
	    
	    switch(type){
	    case ABOUT_NITC: browser.loadUrl("file:///android_asset/aboutnitc.html");break;
	    case ABOUT_TATHVA: browser.loadUrl("file:///android_asset/abouttathva.html");break;
	    case DEVELOPERS:browser.loadUrl("file:///android_asset/developers.html"); break;
	    case SPONSERS:
		    	if(networkCheckIn()){
		    		browser.loadUrl("http://tathva-paperback.rhcloud.com/app/sponsers.html");
		    	}else{
		    		browser.loadUrl("file:///android_asset/offline.html");
		    	}; 
	    	break;
	    case NITES:
	    	browser.loadUrl("file:///android_asset/nites.html");
	    	break;
	    	
	    }
	   
	 }
	 
	 public boolean networkCheckIn() {
			try {

				ConnectivityManager networkInfo = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				networkInfo.getActiveNetworkInfo().isConnectedOrConnecting();

				Log.d("1", "Net avail:"
						+ networkInfo.getActiveNetworkInfo()
								.isConnectedOrConnecting());

				ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo netInfo = cm.getActiveNetworkInfo();
				if (netInfo != null && netInfo.isConnectedOrConnecting()) {
					// Log.d("2", "Network available:true");
					return true;

				} else {
					// Log.d("3", "Network available:false");
					return false;
				}

			} catch (Exception e) {
				return false;
			}
		}
		
	 @Override
	    public void onBackPressed() {
	        // TODO Auto-generated method stub
	        super.onBackPressed();
	        overridePendingTransition (R.anim.activity_open_scale, R.anim.activity_close_translate);
	    }
	 
	 
}