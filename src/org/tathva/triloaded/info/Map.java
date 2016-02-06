package org.tathva.triloaded.info;


import org.tathva.triloaded.mainmenu.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Map extends Activity {
	
	 private GoogleMap map;
	 LocationManager locationManager ;
	 Marker MyLocation;
	 LatLng pl;
	 
	 @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
	    setContentView(R.layout.info_map);
		
	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
	            .getMap();
	    
	    
	    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	        
	    map.setMyLocationEnabled(true);
	    
	    pl = new LatLng(11.319869,75.932757);
	    MyLocation = map.addMarker(new MarkerOptions().
	    	                                       position(pl).
	    	                                       title("NIT Calicut").
	    	                                       icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
	    
	    MyLocation.showInfoWindow();
	    
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(pl,18));
	 }
	 
	 @Override
	    public void onBackPressed() {
	        // TODO Auto-generated method stub
	        super.onBackPressed();
	        overridePendingTransition (R.anim.activity_open_scale, R.anim.activity_close_translate);
	    }
}