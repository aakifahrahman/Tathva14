package org.tathva.triloaded.mainmenu;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.tathva.triloaded.announcements.Announcements;
import org.tathva.triloaded.anubhava.AnubhavaMain;
import org.tathva.triloaded.clueless.Clueless;
import org.tathva.triloaded.customviews.MenuCircle;
import org.tathva.triloaded.customviews.MenuTitleBar;
import org.tathva.triloaded.customviews.MenuCircle.onClickMenu;
import org.tathva.triloaded.events.Competition;
import org.tathva.triloaded.events.EventsMenu;
import org.tathva.triloaded.gcm.GcmUtils;
import org.tathva.triloaded.gcm.RegisterApp;
import org.tathva.triloaded.info.InfoMain;
import org.tathva.triloaded.mainmenu.R;
import org.tathva.triloaded.navigation.ShowAll;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.R.id;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainMenu extends Activity {

	/*Gcm related Variables */
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private GoogleCloudMessaging gcm;
	
    private MenuCircle menu;
    private MenuTitleBar title;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
		setContentView(R.layout.main_menu);
		overridePendingTransition(R.anim.fade, R.anim.hold);
		
		//GCM Registeration if not registered!!
		String regId = GcmUtils.getRegistrationId(this);
	
		if(regId.isEmpty()){
			//need Registeration!!
			if (checkPlayServices()) {
				gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
				int appVersion = GcmUtils.getAppVersion(this);
				new RegisterApp(getApplicationContext(), gcm, appVersion).execute();
			}
		}
		
        menu = (MenuCircle) findViewById(R.id.menuCircle);
        title = (MenuTitleBar) findViewById(R.id.titlebar);
        title.setVisibility(View.INVISIBLE);
        
        /* MENU EVENTS */
        menu.setOnClickMenu(new onClickMenu() {
			
			@Override
			public void onClick(int SectionCode) {
				/* Click Implementation */
				
				switch(SectionCode){
					case MenuCircle.EVENTS:
					
						startActivity(new Intent(MainMenu.this, EventsMenu.class));
						overridePendingTransition (R.anim.activity_open_translate, R.anim.activity_close_scale);
						
						break;
					case MenuCircle.ANNOUNCEMENTS:
						
						startActivity(new Intent(MainMenu.this, Announcements.class));
						overridePendingTransition (R.anim.activity_open_translate, R.anim.activity_close_scale);
						
						break;
					case MenuCircle.INFO:
						
						startActivity(new Intent(MainMenu.this, InfoMain.class));
						overridePendingTransition (R.anim.activity_open_translate, R.anim.activity_close_scale);
						
						break;
					case MenuCircle.CLUELESS:
						
						startActivity(new Intent(MainMenu.this, Clueless.class));
						overridePendingTransition (R.anim.activity_open_translate, R.anim.activity_close_scale);
						
						break;
					case MenuCircle.NAVIGATION:
						
						startActivity(new Intent(MainMenu.this, ShowAll.class));
						overridePendingTransition (R.anim.activity_open_translate, R.anim.activity_close_scale);
						
						break;
					case MenuCircle.EXPERIENCE:
						
						startActivity(new Intent(MainMenu.this, AnubhavaMain.class));
						overridePendingTransition (R.anim.activity_open_translate, R.anim.activity_close_scale);
						
						break;
					default: break;
				}
				
				
			}
		});
        
        menu.setHoverListener(new MenuCircle.onHoverMenu() {
			
			@Override
			public void onHover(int section) {
				
				if(section == 0){
					title.flyOut();
				}else{
					title.setVisibility(View.VISIBLE);
					title.setSectionCode(section);
					title.flyIn();
				}
				
			}
		});
      
    }
        
	private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("anas", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
	
	
	@Override
	protected void onResume() {
		menu.refreshState();
		super.onPause();
	}
	@Override
	protected void onPause() {
		title.flyOut();
		super.onPause();
	}
	
	
}
