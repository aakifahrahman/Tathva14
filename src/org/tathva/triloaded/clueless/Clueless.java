package org.tathva.triloaded.clueless;

import org.tathva.triloaded.mainmenu.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Clueless extends Activity {
	WebView web;
	Boolean onActivity=true;
	private static ProgressDialog pdialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
		
		setContentView(R.layout.clueless);
		web=(WebView) findViewById(R.id.webviewclueless);
		web.setWebViewClient(new myWebClient());          
		web.getSettings().setJavaScriptEnabled(true);
	    web.getSettings().setDomStorageEnabled(true); 
	    pdialog = new ProgressDialog(this);
		pdialog.setCancelable(true);
		//pdialog.setTitle("Please Wait");
		pdialog.setMessage("Please Wait...");
		pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    
	    if (!networkCheckIn()) {
			/*Toast.makeText(getApplicationContext(),
					"No Internet Access, Review network settings !",
					Toast.LENGTH_LONG).show();*/
	    	//load offline url
	    	web.loadUrl("file:///android_asset/offline.html");
	    	
		} else {
			//load clueless
			 web.loadUrl("http://clueless.tathva.org"); 
			//web.loadUrl("http://tathva-paperback.rhcloud.com/app/abouttathva.html"); 
			    
			    

			
		}
	    
	   		

	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		onActivity=true;
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		onActivity=false;
	}
	public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            if(onActivity)
            pdialog.show();
        }
 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
        		
            view.loadUrl(url);
            if(onActivity)
            pdialog.show();
            return true;
 
        }
        @Override
        public void onPageFinished(WebView view, String url)
        {
        	super.onPageFinished(view, url);
        	if(onActivity)
        	pdialog.hide();
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