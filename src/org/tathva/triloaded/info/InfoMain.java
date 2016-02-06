package org.tathva.triloaded.info;

import org.tathva.triloaded.mainmenu.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class InfoMain extends Activity {

	
	
	TextView reach, developers,sponsers, schedule, tathva, nitc;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
        setContentView(R.layout.info_main_page);
    
        developers = (TextView) findViewById(R.id.info_dev);
        developers.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(getApplicationContext(), InfoWebView.class);
				i.putExtra(InfoWebView.KEY, InfoWebView.DEVELOPERS);
				startActivity(i);
				overridePendingTransition (R.anim.activity_open_translate, R.anim.activity_close_scale);
			}
		});
        
        tathva = (TextView) findViewById(R.id.info_tathva);
        tathva.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(getApplicationContext(), InfoWebView.class);
				i.putExtra(InfoWebView.KEY, InfoWebView.ABOUT_TATHVA);
				startActivity(i);
				overridePendingTransition (R.anim.activity_open_translate, R.anim.activity_close_scale);
			}
		});
        
        nitc = (TextView) findViewById(R.id.info_nitc);
        nitc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(getApplicationContext(), InfoWebView.class);
				i.putExtra(InfoWebView.KEY, InfoWebView.ABOUT_NITC);
				startActivity(i);
				overridePendingTransition (R.anim.activity_open_translate, R.anim.activity_close_scale);
			}
		});
        
        sponsers = (TextView) findViewById(R.id.info_sponsors);
        sponsers.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(getApplicationContext(), InfoWebView.class);
				i.putExtra(InfoWebView.KEY, InfoWebView.SPONSERS);
				startActivity(i);
				overridePendingTransition (R.anim.activity_open_translate, R.anim.activity_close_scale);
			}
		});
        
        reach = (TextView) findViewById(R.id.info_reach);
        reach.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), Map.class);
				startActivityForResult(i, 0);
				overridePendingTransition (R.anim.activity_open_translate, R.anim.activity_close_scale);
			}	
				
		});
		
    }
    
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition (R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
