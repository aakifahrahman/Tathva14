package org.tathva.triloaded.navigation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.tathva.triloaded.customviews.Tile.OnOptionClickListener;
import org.tathva.triloaded.mainmenu.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ShowAll  extends Activity implements OnOptionClickListener {
	
static ArrayList<String> places=new ArrayList<String>();
	
	List<Location> loclist;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.nav_main);
		MapDB  db=new MapDB(this);
		loclist= db.getLocations();
		
		TileList tiles = (TileList) findViewById(R.id.tilelist);
		tiles.setOnOptionClickListener(this);
		tiles.setData(loclist);
		
	}
	
	
	
	public void showMap(String place){
    	Intent i=new Intent(this,Map.class);	
		i.putExtra("place", place);
		startActivity(i);
		overridePendingTransition (R.anim.activity_open_translate, R.anim.activity_close_scale);
		
	}

	@Override
	public void onOptionClick(String place) {
		showMap(place);	
	}

	 @Override
	    public void onBackPressed() {
	        // TODO Auto-generated method stub
	        super.onBackPressed();
	        overridePendingTransition (R.anim.activity_open_scale, R.anim.activity_close_translate);
	    }
	
}
