package org.tathva.triloaded.mainmenu;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.tathva.triloaded.announcements.Announcements;
import org.tathva.triloaded.customviews.FontastiqueText;
import org.tathva.triloaded.customviews.OnFinishListener;
import org.tathva.triloaded.customviews.TathvaMan;
import org.tathva.triloaded.customviews.TathvaText;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends Activity implements OnFinishListener{
	
	private TathvaMan tathvaman;
	private TathvaText tathvatext;
	private FontastiqueText flashText;
	private String dbpath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.splashscreen);
		//startActivity(new Intent(Splash.this, Announcements.class));
	
		//Tap to accelerate!!
		
		if(!checkDB()){
			copyDataBase();
		}
		
		tathvaman = (TathvaMan) findViewById(R.id.tathvaMan2);
		tathvatext = (TathvaText) findViewById(R.id.tathvaText1);
		flashText = (FontastiqueText) findViewById(R.id.flashText);
		tathvaman.setOnFinishListener(this);
		tathvatext.setOnFinishListener(this);
		flashText.setOnFinishListener(this);
		tathvaman.start();
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!isFirstTime()){
			switch(event.getAction()){
				case MotionEvent.ACTION_UP: 
					startActivity(new Intent(Splash.this, MainMenu.class));
					finish();
					break;
			}
		}
		return super.onTouchEvent(event);
	}
	@Override
	public void onFinish(int id) {
		switch(id){
		case R.id.tathvaMan2: 	tathvatext.start();break;
		case R.id.tathvaText1: 	flashText.start();break;
		case R.id.flashText:	startActivity(new Intent(Splash.this, MainMenu.class));
								finish();
								break;
		}
		
	}
	
	private boolean isFirstTime(){
		SharedPreferences loginInfo = getSharedPreferences("LOGIN_INFO", 0);
		String isFirst = loginInfo.getString("first_time", "yes");
		if(isFirst.equals("yes")){
			return true;
		}
		return false;
		
	}
	
	@Override
	protected void onDestroy() {
		SharedPreferences loginInfo = getSharedPreferences("LOGIN_INFO", 0);
		SharedPreferences.Editor editor = loginInfo.edit();
		editor.putString("first_time", "no");
		editor.commit();
		super.onDestroy();
	}
	
	
	private boolean checkDB(){
		File fil=this.getDatabasePath("tathva.db");
		dbpath=fil.getPath();
		
		File db=new File(dbpath);
		
		if(!db.exists()){
			File db_dir=db.getParentFile();
			db_dir.mkdirs();
		}
		
		return db.exists();
	}
	
	private void copyDataBase(){ 
	     try{
	    	 
	    	InputStream myInput = this.getAssets().open("tathva.db");
		    String outFileName = dbpath; 
		    OutputStream myOutput = new FileOutputStream(outFileName); 
		    byte[] buffer = new byte[1024];
		    int length;
	    	while ((length = myInput.read(buffer))>0){
	        myOutput.write(buffer, 0, length);
	    }

	    myOutput.flush();
	    myOutput.close();
	    myInput.close(); 
	    }
	    catch(Exception e){
	    	//db can't b copied - abort!
	    	Log.d("error", e.getMessage());
	    }
	    
	}

}
